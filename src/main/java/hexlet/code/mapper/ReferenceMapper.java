package hexlet.code.mapper;

import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.BaseEntity;
import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import jakarta.persistence.EntityManager;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class ReferenceMapper {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;

    public <T extends BaseEntity> T toEntity(Long id, @TargetType Class<T> entityClass) {
        return id != null
            ? entityManager.find(entityClass, id)
            : null;
    }

    public TaskStatus toTaskStatus(String slug) {
        return slug != null
            ? taskStatusRepository.findBySlug(slug)
            .orElseThrow(() -> new ResourceNotFoundException("Task status with slug " + slug + "not found"))
            : null;
    }

    public Label toLabel(String name) {
        return name != null
            ? labelRepository.findByName(name)
            .orElseThrow(() -> new ResourceNotFoundException("Label with name " + name + "not found"))
            : null;
    }
}
