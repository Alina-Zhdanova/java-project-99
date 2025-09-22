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
public class LabelDTO {
    @EqualsAndHashCode.Include
    private Long id;

    @EqualsAndHashCode.Include
    private String name;

    private LocalDate createdAt;
}
