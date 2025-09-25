package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TaskDTO {
    private List<Long> labels;

    @EqualsAndHashCode.Include
    private Long id;

    @EqualsAndHashCode.Include
    private int index;

    private LocalDate createdAt;

    @EqualsAndHashCode.Include
    private Long assigneeId;

    @EqualsAndHashCode.Include
    private String title;

    @EqualsAndHashCode.Include
    private String content;

    @EqualsAndHashCode.Include
    private String status; // slug
}
