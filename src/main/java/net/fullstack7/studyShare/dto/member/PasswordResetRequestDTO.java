package net.fullstack7.studyShare.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequestDTO {
    private String userId;
    private String token;
    private String newPassword;
}
// request.getUserId(), 
// request.getToken(), 
// request.getNewPassword()