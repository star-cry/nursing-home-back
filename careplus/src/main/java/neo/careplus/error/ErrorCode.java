package neo.careplus.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    EMAIL_DUPLICATED(409, "E0001", "이미 가입된 이메일입니다."),
    VERIFICATION_FAILED(400, "E0002", "인증 코드가 올바르지 않습니다."),
    EXPIRED_CODE(400, "E0003", "인증 코드가 만료되었습니다."),
    USER_NOT_FOUND(404, "E0004", "유저가 존재하지 않습니다."),
    INTERNAL_SERVER_ERROR(500, "E9999", "서버 오류가 발생했습니다."),
    PASSWORD_MISMATCH(400,"E0005","비밀번호가 일치하지 않습니다");

    private final int status;
    private final String code;      // 유니크 에러 코드
    private final String message;
}