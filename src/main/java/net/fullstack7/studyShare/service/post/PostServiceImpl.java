package net.fullstack7.studyShare.service.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.domain.File;
import net.fullstack7.studyShare.domain.Member;
import net.fullstack7.studyShare.domain.Post;
import net.fullstack7.studyShare.dto.post.PostRegistDTO;
import net.fullstack7.studyShare.repository.FileRepository;
import net.fullstack7.studyShare.repository.MemberRepository;
import net.fullstack7.studyShare.repository.PostRepository;
import net.fullstack7.studyShare.util.CommonFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostServiceIf{
    private final PostRepository postRepository;
    private final FileRepository fileRepository;
    private final MemberRepository memberRepository;

    @Override
    public boolean regist(PostRegistDTO dto, String memberId) throws IOException {
        String fileName = null;
        String filePath = null;
        long maxSize = 1024*1024*500L;
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
                throw new IllegalArgumentException("파일 업로드 크기는 최대 500MB 입니다");
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
                fileName = CommonFileUtil.uploadFile(dto.getFile()); // 파일 업로드
                filePath = "/file/" + fileName; // 업로드된 파일의 전체 경로 설정
                dto.setFileName(fileName); // DTO에 파일 이름 저장
                log.info("파일명: {}, 파일경로: {}", fileName, filePath);
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
}
