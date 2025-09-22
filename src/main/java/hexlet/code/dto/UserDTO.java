package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserDTO {
    @EqualsAndHashCode.Include
    private Long id;

    @EqualsAndHashCode.Include
    private String email;

    @EqualsAndHashCode.Include
    private String firstName;

    @EqualsAndHashCode.Include
    private String lastName;

    private LocalDate createdAt;
}
