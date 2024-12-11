package net.fullstack7.studyShare.service.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.domain.File;
import net.fullstack7.studyShare.domain.Member;
import net.fullstack7.studyShare.domain.Post;
import net.fullstack7.studyShare.dto.post.*;
import net.fullstack7.studyShare.mapper.PostMapper;
import net.fullstack7.studyShare.repository.FileRepository;
import net.fullstack7.studyShare.repository.MemberRepository;
import net.fullstack7.studyShare.repository.PostRepository;
import net.fullstack7.studyShare.repository.ShareRepository;
import net.fullstack7.studyShare.util.CommonFileUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostServiceIf{
    private static final String UPLOAD_DIR = "/home/gyeongmini/upload/images"; // 실제 파일 저장 경로
    private static final String WEB_DIR = "/upload/images"; // 웹에서 접근할 경로
    private static final String DELETE_DIR = "/home/gyeongmini/upload/images"; // 삭제 경로
    private final PostRepository postRepository;
    private final FileRepository fileRepository;
    private final ShareRepository shareRepository;
    private final MemberRepository memberRepository;
    private final PostMapper postMapper;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public boolean regist(PostRegistDTO dto, String memberId) throws IOException {
        String fileName = null;
        String filePath = null;
        String thumbnailName = null;
        String thumbnailPath = null;
        long maxSize = 1024*1024*10L;
        List<String> allowedMimeTypes = Arrays.asList("image/jpeg", "image/png");
        List<String> allowedExtensions = Arrays.asList(".jpg", ".png");
        
        


        //노출 여부, 날짜 검증
        if(dto.getPrivacy() == 1){
            if(dto.getDisplayAt() == null && dto.getDisplayEnd() == null){
                throw new IllegalArgumentException("노출 설정 시 시작 날짜와 종료 날짜는 필수입니다.");
            }
            if(dto.getDisplayAt().isAfter(dto.getDisplayEnd())){ // 같은 날 허용
                throw new IllegalArgumentException("노출 종료 날짜는 시작 날짜 이후여야 합니다.");
            }
        }

        if(dto.getFile() != null && !dto.getFile().isEmpty()) {
            //파일 크기
            if (dto.getFile() != null && dto.getFile().getSize() > maxSize) {
                throw new IllegalArgumentException("파일 업로드 크기는 최대 10MB 입니다");
            }

            // MIME 타입 검증
            String fileType = dto.getFile().getContentType();
            System.out.println(fileType);
            if (!allowedMimeTypes.contains(fileType)) {
                throw new IllegalArgumentException("허용되지 않는 파일 형식입니다. JPG 또는 PNG 파일만 업로드 가능합니다.");
            }

            //확장자 확인
            String originalFileName = dto.getFile().getOriginalFilename();
            System.out.println(originalFileName);
            if(originalFileName != null){
                String extension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
                if(!allowedExtensions.contains(extension)){
                    throw new IllegalArgumentException("허용되지 않는 파일 확장자입니다. JPG 또는 PNG 파일만 업로드 가능합니다.");
                }
            }
        }

        // 파일 업로드 처리
        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            try {
                //원본
                String webPath = CommonFileUtil.uploadFile(dto.getFile()); // 웹 경로 반환받음
                fileName = webPath.substring(webPath.lastIndexOf("/") + 1); // 실제 파일명만 추출
                filePath = webPath; // 웹 경로 저장
                dto.setFileName(fileName);
                log.info("파일명: {}, 파일경로: {}", fileName, filePath);

                //썸네일
                thumbnailName = fileName;
                try {
                    CommonFileUtil.createThumbnail(fileName); // 파일명만 전달
                    thumbnailPath = WEB_DIR + thumbnailName + ".jpg"; // 웹 경로 구성
                    log.info("썸네일 생성 완료: {}", thumbnailPath);
                } catch (Exception e) {
                    log.error("썸네일 생성 실패: {}", e.getMessage());
                    throw new IllegalArgumentException("썸네일 생성 중 오류가 발생했습니다.");
                }
            } catch (Exception e) {
                log.info("파일 업로드 실패{}", e.getMessage(), e);
            }
        } else {
            log.info("없음");
        }

        // 이미 저장된 Member 엔티티 조회
        // 회원이 아니면 못 함
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 존재하지 않습니다."));

        if (dto != null) {
            try {
                Post post = Post.builder()
                        .title(dto.getTitle())
                        .content(dto.getContent())
                        .privacy(dto.getPrivacy())
                        .share(0)
                        .displayAt(dto.getDisplayAt())
                        .displayEnd(dto.getDisplayEnd())
                        .createdAt(LocalDateTime.now())
                        .domain(dto.getDomain())
                        .hashtag(dto.getHashtag())
                        .member(member)
                        .thumbnailName(thumbnailName) // 썸네일 이름 저장
                        .thumbnailPath(thumbnailPath) // 썸네일 경로 저장
                        .build();
                postRepository.save(post);
                log.info(" 성공  ID: {}", post.getId());

                // 파일 정보 저장
                if (fileName != null && filePath != null) {
                    File file = File.builder()
                            .fileName(fileName)
                            .path(filePath)
                            .post(post)
                            .build();
                    fileRepository.save(file);
                    log.info(" id: {}, 파일명: {}", file.getId(), file.getFileName());
                } else {
                    log.warn("정보없음");
                }
            } catch (Exception e) {
                log.error("저장 실패: {}", e.getMessage(), e);
            }
        } else {
            log.warn("DTO 없음");
        }
        return false;
    }


    @Override
    public int totalCnt(String searchCategory, String searchValue, String userId, String sortType, LocalDateTime displayAt, LocalDateTime displayEnd) {
        return postMapper.totalCnt(searchCategory, searchValue, userId, sortType, displayAt, displayEnd);
    }

    @Override
    public List<PostDTO> selectAllPost(int pageNo, int pageSize, String searchCategory,
                                       String searchValue, String userId, String sortType, LocalDateTime displayAt, LocalDateTime displayEnd) {
        Map<String, Object> map = new HashMap<>();
        map.put("offset", (pageNo - 1) * pageSize);
        map.put("limit", pageSize);
        map.put("searchCategory", searchCategory);
        map.put("searchValue", searchValue);
        map.put("userId", userId);
        map.put("sortType", sortType);
        map.put("displayAt", displayAt);
        map.put("displayEnd", displayEnd);

        List<PostDTO> list = postMapper.selectAllPost(map);
        return list.stream()
                .map(i -> modelMapper.map(i, PostDTO.class)).collect(Collectors.toList());
    }


    @Override
    public PostViewDTO findPostWithFile(String id) {
        return postMapper.findPostWithFile(id);
    }

    @Override
    public boolean checkWriter(int id, String userId) {
        return postMapper.checkWriter(id, userId);
    }

    @Override
    public PostRegistDTO modifyPost(PostRegistDTO dto, String userId) {
        String fileName = null;
        String filePath = null;
        String thumbnailName = null;
        String thumbnailPath = null;
        long maxSize = 1024 * 1024 * 10L;
        List<String> allowedMimeTypes = Arrays.asList("image/jpeg", "image/png");
        List<String> allowedExtensions = Arrays.asList(".jpg", ".png");

        Post post = postRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

//        if (!post.getMember().equals(userId)) {
//            throw new IllegalArgumentException("수정 권한이 없습니다.");
//        }

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 존재하지 않습니다."));

        // 노출 여부, 날짜 검증
        if (dto.getPrivacy() == 1) {
            if (dto.getDisplayAt() == null || dto.getDisplayEnd() == null) {
                throw new IllegalArgumentException("노출 설정 시 시작 날짜와 종료 날짜는 필수입니다.");
            }
            if (dto.getDisplayAt().isAfter(dto.getDisplayEnd())) {
                throw new IllegalArgumentException("노출 종료 날짜는 시작 날짜 이후여야 합니다.");
            }
        }

        //새 이미지 업로드
        if(dto.getFile() != null && !dto.getFile().isEmpty()) {
            //파일 크기
            if (dto.getFile() != null && dto.getFile().getSize() > maxSize) {
                throw new IllegalArgumentException("파일 업로드 크기는 최대 10MB 입니다");
            }

            // MIME 타입 검증
            String fileType = dto.getFile().getContentType();
            System.out.println(fileType);
            if (!allowedMimeTypes.contains(fileType)) {
                throw new IllegalArgumentException("허용되지 않는 파일 형식입니다. JPG 또는 PNG 파일만 업로드 가능합니다.");
            }

            //확장자 확인
            String originalFileName = dto.getFile().getOriginalFilename();
            System.out.println(originalFileName);
            if(originalFileName != null){
                String extension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
                if(!allowedExtensions.contains(extension)){
                    throw new IllegalArgumentException("허용되지 않는 파일 확장자입니다. JPG 또는 PNG 파일만 업로드 가능합니다.");
                }
            }
        }

        //사용자가 박스를 체크하지 않은 상태에서 새로운 이미지를 업로드 했을 때
        if(dto.getFile()!= null && !dto.getFile().isEmpty()){
            //기존에 존재하는 파일이 있는지 확인
            File exitFile = fileRepository.findByPostId(dto.getId());
            if(exitFile != null){
                boolean isDeleted =  CommonFileUtil.deleteFile(exitFile.getPath());
                if (isDeleted) {
                    log.info("파일 삭제 완료: {}", exitFile.getPath());
                } else {
                    log.warn("파일 삭제 실패: {}", exitFile.getPath());
                }
                fileRepository.delete(exitFile);
                log.info("DB에서 파일 정보 삭제 완료: id={}", exitFile.getId());
                // 기존 이미지 정보 초기화
                post.setThumbnailName(null);
                post.setThumbnailPath(null);
            }
        }


        //사용자가 박스를 체크했을 때
        if(dto.isDeleteImage()){
            File file = fileRepository.findByPostId(post.getId());
            if(file != null){
                boolean isDeleted =  CommonFileUtil.deleteFile(file.getPath());
                if (isDeleted) {
                    log.info("파일 삭제 완료: {}", file.getPath());
                } else {
                    log.warn("파일 삭제 실패: {}", file.getPath());
                }
                fileRepository.delete(file);
                log.info("DB에서 파일 정보 삭제 완료: id={}", file.getId());
            }
            // 기존 이미지 정보 초기화
            post.setThumbnailName(null);
            post.setThumbnailPath(null);
        }else{
            log.warn("삭제할 파일 정보가 없습니다.");
        }

        // 파일 업로드 처리
        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            try {
                //원본
                String webPath = CommonFileUtil.uploadFile(dto.getFile()); // 웹 경로 반환받음
                fileName = webPath.substring(webPath.lastIndexOf("/") + 1); // 실제 파일명만 추출
                filePath = webPath; // 웹 경로 저장
                dto.setFileName(fileName);
                log.info("파일명: {}, 파일경로: {}", fileName, filePath);

                //썸네일
                thumbnailName = fileName;
                try {
                    CommonFileUtil.createThumbnail(fileName); // 파일명만 전달
                    thumbnailPath = WEB_DIR + "/thumb_" + thumbnailName; // 웹 경로 구성
                    log.info("썸네일 생성 완료: {}", thumbnailPath);
                } catch (Exception e) {
                    log.error("썸네일 생성 실패: {}", e.getMessage());
                    throw new IllegalArgumentException("썸네일 생성 중 오류가 발생했습니다.");
                }
            } catch (Exception e) {
                log.info("파일 업로드 실패{}", e.getMessage(), e);
            }
        } else {
            log.info("없음");
        }

        if (dto != null) {
            try {
                post.setTitle(dto.getTitle());
                post.setContent(dto.getContent());
                post.setPrivacy(dto.getPrivacy());
                post.setShare(dto.getShare());
                post.setDisplayAt(dto.getDisplayAt());
                post.setDisplayEnd(dto.getDisplayEnd());
                post.setDomain(dto.getDomain());
                post.setHashtag(dto.getHashtag());
                post.setThumbnailName(thumbnailName);
                post.setThumbnailPath(thumbnailPath);
                postRepository.save(post);
                log.info(" 성공  ID: {}", post.getId());

                // 파일 정보 저장
                if (fileName != null && filePath != null) {
                    File file = File.builder()
                            .fileName(fileName)
                            .path(filePath)
                            .post(post)
                            .build();
                    fileRepository.save(file);
                    log.info(" id: {}, 파일명: {}", file.getId(), file.getFileName());
                } else {
                    log.warn("정보없음");
                }
            } catch (Exception e) {
                log.error("저장 실패: {}", e.getMessage(), e);
            }
        } else {
            log.warn("DTO 없음");
        }
        return null;
    }

    //2024-12-10 수미 수정
    @Override
    public boolean delete(int id) {
        boolean file = postMapper.deleteFile(id); //파일 삭제
        int hasShare = postMapper.hasShare(id); //공유 있는지
        boolean share = hasShare != 0 && postMapper.deleteShare(id); //있을때만 삭제
        boolean post = postMapper.deletePost(id); // 게시글 삭제
        return post && (hasShare == 0 || share); // 세 가지 경우
    }

    @Override
    public Optional<Post> findPostById(int id) {
        return postRepository.findById(id);
    }

    @Override
    public boolean isOwnerOrSharedWithUser(int id, String userId) {
//        // 멤버 객체 생성
//        Member member = memberRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
//        //글 객체 생성
//        Post post = postRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

        //작성자 확인
        boolean isOwner = postRepository.existsByMember_UserIdAndId(userId, id);

        //공유 받았는지 확인
        boolean isShared = shareRepository.existsByUser_UserIdAndPost_Id(userId, id);
        return isShared || isOwner;
    }


    @Override
    public List<PostShareDTO> getSharedPosts(PostSharePagingDTO dto, String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("offset", (dto.getPageNo() - 1) * dto.getPageSize());
        map.put("limit", dto.getPageSize());
        map.put("searchCategory", dto.getSearchCategory());
        map.put("searchValue", dto.getSearchValue());
        map.put("userId", userId);
        map.put("sortType", dto.getSortType());
        map.put("displayAt", dto.getDisplayAt());
        map.put("displayEnd", dto.getDisplayEnd());

        List<PostShareDTO> list = postMapper.selectMyShare(map);
        log.info("list, {}", list);
        return list.stream()
                .map(i -> modelMapper.map(i, PostShareDTO.class)).collect(Collectors.toList());

    }

    @Override
    public List<PostMyShareDTO> selectPostsByUserId(PostSharePagingDTO dto, String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("offset", (dto.getPageNo() - 1) * dto.getPageSize());
        map.put("limit", dto.getPageSize());
        map.put("searchCategory", dto.getSearchCategory());
        map.put("searchValue", dto.getSearchValue());
        map.put("userId", userId);
        map.put("sortType", dto.getSortType());
        map.put("displayAt", dto.getDisplayAt());
        map.put("displayEnd", dto.getDisplayEnd());

        // 게시글 조회
        List<PostMyShareDTO> posts = postMapper.selectPostsByUserId(map);
        if (posts == null || posts.isEmpty()) {
            return Collections.emptyList();
        }

        log.info("posts, {}", posts);

        // 게시글 ID 추출
        List<Integer> postIds = posts.stream()
                .map(PostMyShareDTO::getPostId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        log.info("postIds, {}", postIds);

        if (postIds.isEmpty()) {
            return posts; // 게시글은 있으나 postId가 없는 경우 그대로 반환
        }

        // 공유자 정보 조회
        List<ShareInfoDTO> shares = postMapper.selectSharesByPostId(postIds);

        log.info("shares, {}", shares);


        if (shares == null || shares.isEmpty()) {
            return posts; // 공유 정보가 없는 경우 그대로 반환
        }

        // 공유자 정보를 게시글 ID별로 그룹화
        Map<Integer, List<ShareInfoDTO>> sharesByPostId = shares.stream()
                .filter(share -> share.getPostId() != null)
                .collect(Collectors.groupingBy(ShareInfoDTO::getPostId));

        log.info("sharesByPostId, {}", sharesByPostId);

        // 게시글 리스트에 공유자 정보 매핑
        posts.forEach(post -> {
            // 변환된 리스트를 설정
            post.setShares(sharesByPostId.get(post.getPostId()));
            log.info("post, {}", post);
        });

        return posts;
    }

    @Override
    public List<ShareInfoDTO> selectSharesByPostId(List<Integer> postId) {
        return postMapper.selectSharesByPostId(postId);
    }
}
