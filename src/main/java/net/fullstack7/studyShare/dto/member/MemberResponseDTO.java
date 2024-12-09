package net.fullstack7.studyShare.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDTO {
    private String userId;
    private String name;
    private String email;
    private String phone;
}
