package net.fullstack7.studyShare.service.post;

import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.domain.File;
import net.fullstack7.studyShare.domain.Member;
import net.fullstack7.studyShare.domain.Post;
import net.fullstack7.studyShare.dto.post.PostRegistDTO;
import net.fullstack7.studyShare.repository.FileRepository;
import net.fullstack7.studyShare.repository.PostRepository;
import net.fullstack7.studyShare.util.CommonFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Log4j2
@Service
public class PostServiceImpl implements PostServiceIf{
    @Autowired
    private PostRepository postrepository;

    @Autowired
    private FileRepository fileRepository;


    @Override
    public boolean regist(PostRegistDTO dto, String memberId) throws IOException {
        String fileName = null;
        String filePath = null;

        // 파일 업로드 처리
        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            try {
                fileName = CommonFileUtil.uploadFile(dto.getFile()); // 파일 업로드
                dto.setFileName(fileName);
                filePath = "/upload/path/" + fileName; // 여기가 잘못된 것 같음
                log.info("파일명: {}, 파일경로: {}", fileName, filePath);
            } catch (Exception e) {
                log.error("파일 업로드 실패{}", e.getMessage(), e);
            }
        } else {
            log.warn("없음");
        }

        if (dto != null) {
            try {
                Post post = Post.builder()
                        .title(dto.getTitle())
                        .content(dto.getContent())
                        .privacy(dto.getPrivacy())
                        .share(dto.getShare())
                        .displayAt(dto.getDisplayAt())
                        .displayEnd(dto.getDisplayEnd())
                        .createdAt(LocalDateTime.now())
                        .domain(dto.getDomain())
                        .hashtag(dto.getHashtag())
                        .member(Member.builder().userId(memberId).build())
                        .build();
                postrepository.save(post);
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

//        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
//            fileName = CommonFileUtil.uploadFile(dto.getFile()); //파일을 업로드하여 서버의 특정 디렉토리에 저장
//            dto.setFileName(fileName); //반환된 fileName으로 dto.setFileName() 설정
//            log.info("fileName" + fileName);
//        }
//
//        if(dto != null && dto!= null) {
//            // Post 엔티티 생성 및 매핑
//            Post post = Post.builder()
//                    .title(dto.getTitle())
//                    .content(dto.getContent())
//                    .privacy(dto.getPrivacy())
//                    .share(dto.getShare())
//                    .displayAt(dto.getDisplayAt())
//                    .displayEnd(dto.getDisplayEnd())
//                    .createdAt(LocalDateTime.now())
//                    .domain(dto.getDomain())
//                    .hashtag(dto.getHashtag())
//                    .member(Member.builder().userId(memberId).build())
//                    .build();
//            postrepository.save(post);
//
//            if(fileName != null && filePath!= null){
//                File file = File.builder()
//                        .fileName(fileName)
//                        .path(filePath)
//                        .post(post)
//                        .build();
//                fileRepository.save(file);
//            }
//        }
        return false;
    }
}
