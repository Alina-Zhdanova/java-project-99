package hexlet.code.mapper;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    uses = {JsonNullableMapper.class, ReferenceMapper.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {
    @Mapping(source = "taskLabelIds", target = "labels")
    @Mapping(source = "assigneeId", target = "assignee")
    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    @Mapping(source = "status", target = "taskStatus")
    public abstract Task map(TaskCreateDTO taskCreateDTO);

    @Mapping(source = "labels", target = "taskLabelIds")
    @Mapping(source = "assignee.id", target = "assigneeId")
    @Mapping(source = "name", target = "title")
    @Mapping(source = "description", target = "content")
    @Mapping(source = "taskStatus.slug", target = "status")
    public abstract TaskDTO map(Task task);

    @Mapping(source = "assigneeId", target = "assignee")
    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    @Mapping(source = "status", target = "taskStatus")
    public abstract void update(TaskUpdateDTO taskUpdateDTO, @MappingTarget Task task);
}
