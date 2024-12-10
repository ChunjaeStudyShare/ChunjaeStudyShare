package net.fullstack7.studyShare.service.share;

import net.fullstack7.studyShare.domain.Share;
import net.fullstack7.studyShare.dto.post.PostMyShareDTO;
import net.fullstack7.studyShare.dto.post.PostShareDTO;
import net.fullstack7.studyShare.dto.post.PostSharePagingDTO;
import net.fullstack7.studyShare.dto.post.ShareInfoDTO;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ShareServiceIf {
    Boolean isSharedByUser(String userId, String postId);
    Boolean shareRequest(PostShareDTO postShareDTO, String userId);

    Boolean shareCancelRequest(PostShareDTO postShareDTO, String userId);

    List<Share> getShareListByPostId(int postId);
}
