package hexlet.code.util;

import hexlet.code.dto.LabelCreateDTO;
import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.UserCreateDTO;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.instancio.Instancio;
import org.instancio.Model;

@Component
@Getter
public class ModelGenerator {
    private Model<UserCreateDTO> userCreateDTOModel;
    private Model<TaskStatusCreateDTO> taskStatusCreateDTOModel;
    private Model<TaskCreateDTO> taskCreateDTOModel;
    private Model<LabelCreateDTO> labelCreateDTOModel;

    @Autowired
    private Faker faker;

    @PostConstruct
    private void init() {
        userCreateDTOModel = Instancio.of(UserCreateDTO.class)
            .supply(Select.field(UserCreateDTO::getEmail), () -> faker.internet().emailAddress())
            .supply(Select.field(UserCreateDTO::getFirstName), () -> faker.name().firstName())
            .supply(Select.field(UserCreateDTO::getLastName), () -> faker.name().lastName())
            .supply(Select.field(UserCreateDTO::getPassword), () -> faker.internet().password())
            .toModel();

        taskStatusCreateDTOModel = Instancio.of(TaskStatusCreateDTO.class)
            .supply(Select.field(TaskStatusCreateDTO::getName), () -> faker.lorem().word())
            .supply(Select.field(TaskStatusCreateDTO::getSlug), () -> faker.lorem().word())
            .toModel();

        taskCreateDTOModel = Instancio.of(TaskCreateDTO.class)
            .supply(Select.field(TaskCreateDTO::getIndex), () -> faker.number().numberBetween(10, 101))
            .supply(Select.field(TaskCreateDTO::getTitle), () -> faker.lorem().word())
            .supply(Select.field(TaskCreateDTO::getContent), () -> faker.lorem().word())
            .supply(Select.field(TaskCreateDTO::getStatus), () -> faker.lorem().word())
            .toModel();

        labelCreateDTOModel = Instancio.of(LabelCreateDTO.class)
            .supply(Select.field(LabelCreateDTO::getName), () -> faker.lorem().characters(3, 1000))
            .toModel();
    }
}
