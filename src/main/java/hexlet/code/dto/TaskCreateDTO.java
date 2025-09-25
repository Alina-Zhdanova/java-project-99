package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskCreateDTO {
    private List<Long> labels;
    private int index;
    private Long assigneeId;

    @NotBlank
    private String title;

    private String content;

    @NotNull
    private String status; // slug
}
