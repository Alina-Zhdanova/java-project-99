package hexlet.code.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TaskStatusCreateDTO {

    @NotNull
    private String name;

    @NotNull
    private String slug;

}
