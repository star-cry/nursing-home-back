package neo.careplus.repository.signup;

import lombok.Data;

@Data
public class EmailVerificationDto {

    private String email;

    private int verificationCode;
}
