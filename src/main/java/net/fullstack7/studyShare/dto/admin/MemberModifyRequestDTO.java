package net.fullstack7.studyShare.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import org.hibernate.validator.constraints.Length;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberModifyRequestDTO {
    @NotBlank(message = "아이디는 필수 입력 항목입니다")
    private String userId;

    @NotBlank(message = "이름은 필수 입력 항목입니다")
    @Length(min = 2, max = 10, message = "이름은 2~10자 사이로 입력해주세요")
    private String userName;

    @NotBlank(message = "이메일은 필수 입력 항목입니다")
    @Email(message = "이메일 형식이 올바르지 않습니다")
    private String userEmail;

    @NotBlank(message = "휴대폰 번호는 필수 입력 항목입니다")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "휴대폰 번호는 10~11자리의 숫자만 가능합니다")
    private String userPhone;

    @NotNull(message = "상태는 필수 입력 항목입니다")
    @Min(value = 0, message = "상태는 0 이상이어야 합니다")
    @Max(value = 4, message = "상태는 4 이하이어야 합니다")
    private Integer status;
}
