package hexlet.code.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabelCreateDTO {
    @NotNull
    private String name;
}
