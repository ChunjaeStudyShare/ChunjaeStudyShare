package net.fullstack7.studyShare.service.post;

import net.fullstack7.studyShare.domain.Post;
import net.fullstack7.studyShare.dto.post.PostRegistDTO;
import net.fullstack7.studyShare.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface PostServiceIf {
    boolean regist(PostRegistDTO postRegistDTO, String memberId) throws IOException;
}
