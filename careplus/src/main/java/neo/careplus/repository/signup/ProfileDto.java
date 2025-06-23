package neo.careplus.repository.signup;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ProfileDto {

    @NotNull
    private Long userId;

    private String name;
}
