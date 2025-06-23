package neo.careplus.repository.signup;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class AccountSetupDto {

    @NotNull
    private Long userId;

    private String email;

    private String password;

    private String passwordConfirm;

}

