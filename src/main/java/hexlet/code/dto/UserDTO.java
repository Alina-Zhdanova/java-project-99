package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;


@Getter
@Setter
@ToString(includeFieldNames = true, onlyExplicitlyIncluded = true)
public class UserDTO {

    @ToString.Include
    private Long id;
    @ToString.Include
    private String email;
    @ToString.Include
    private String firstName;
    @ToString.Include
    private String lastName;
    @ToString.Include
    private LocalDate createdAt;

}
