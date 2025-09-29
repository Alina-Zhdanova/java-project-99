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
public class TaskStatusDTO {
    @EqualsAndHashCode.Include
    private Long id;

    @EqualsAndHashCode.Include
    private String name;

    @EqualsAndHashCode.Include
    private String slug;

    private LocalDate createdAt;
}
