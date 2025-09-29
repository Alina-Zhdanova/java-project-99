package hexlet.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @EqualsAndHashCode.Include
    private Long id;

    private List<Long> taskLabelIds;

    @EqualsAndHashCode.Include
    private int index;

    private LocalDate createdAt;

    @JsonProperty("assignee_id")
    @EqualsAndHashCode.Include
    private Long assigneeId;

    @EqualsAndHashCode.Include
    private String title;

    @EqualsAndHashCode.Include
    private String content;

    @EqualsAndHashCode.Include
    private String status; // slug
}
