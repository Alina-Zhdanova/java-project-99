package hexlet.code.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDTO {

    @Column(unique = true)
    @NotNull
    @Email
    private String email;

    private String firstName;
    private String lastName;

    @NotNull
    @Size(min = 3)
    private String password;

}
