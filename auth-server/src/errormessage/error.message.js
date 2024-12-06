//error message
const errorMessage = {
    // 인증 관련 에러
    INVALID_CREDENTIALS: "입력하신 아이디 또는 패스워드가 일치하지 않습니다.",
    ACCOUNT_LOCK : "5회 이상 로그인 실패로 잠금처리되었습니다.",
    ACCOUNT_LOCKED: "5회 이상 로그인 실패로 잠금처리된 아이디입니다. 관리자에게 문의해주세요.",
    ACCOUNT_DORMANT: "6개월 이상 로그인 이력이 없습니다. 관리자에게 문의해주세요.",
    ACCOUNT_RESTRICTED: "관리자 또는 이용 규칙 위반에 의해 이용이 제한된 아이디입니다. 관리자에게 문의해주세요.",
    EMAIL_NOT_VERIFIED: "이메일 인증을 진행해주세요.",
    
    // 회원가입 관련 에러
    DUPLICATE_ID: "이미 사용 중인 아이디입니다.",
    DUPLICATE_EMAIL: "이미 사용 중인 이메일입니다.",
    
    // 입력값 검증 에러
    MISSING_CREDENTIALS: "비밀번호또는 아이디를 입력해주세요.",
    INVALID_PASSWORD_FORMAT: "비밀번호는 영문, 숫자, 특수문자를 포함하여 8자 이상 입력해주세요.",
    INVALID_PHONE_FORMAT: "휴대폰번호는 '-' 없이 0~9 숫자 10~11자리로 입력해주세요.",
    INVALID_NAME_FORMAT: "이름은 한글 2~5자로 입력해주세요.",
    INVALID_EMAIL_FORMAT: "이메일 형식이 올바르지 않습니다.",
    INVALID_ID_FORMAT: "아이디는 영어 소문자 및 숫자 4~12자로 입력해주세요.",

    // 토큰 관련 에러
    INVALID_TOKEN: "유효하지 않은 토큰입니다. 다시 로그인해주세요.",
    EXPIRED_TOKEN: "만료된 토큰이 만료되었습니다. 다시 로그인해주세요.",
    INVALID_USER: "유효하지 않은 사용자 정보입니다.",
    TOKEN_GENERATION_FAILED: "토큰 생성에 실패했습니다."
}

module.exports = errorMessage;