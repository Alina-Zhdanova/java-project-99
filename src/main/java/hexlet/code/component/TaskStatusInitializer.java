package hexlet.code.component;

import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class TaskStatusInitializer implements ApplicationRunner {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var defaultTaskStatuses = getDefaultTaskStatuses();

        for (var defaultTaskStatus : defaultTaskStatuses) {
            saveDefaultTaskStatus(defaultTaskStatus);
        }

        var tasks = taskStatusRepository.findAll();
    }

    private List<Map<String, String>> getDefaultTaskStatuses() {
        var draft = Map.of(
            "name", "Draft",
            "slug", "draft"
        );
        var toReview = Map.of(
            "name", "ToReview",
            "slug", "to_review"
        );
        var toBeFixed = Map.of(
            "name", "ToBeFixed",
            "slug", "to_be_fixed"
        );
        var toPublish = Map.of(
            "name", "ToPublish",
            "slug", "to_publish"
        );
        var published = Map.of(
            "name", "Published",
            "slug", "published"
        );

        return List.of(draft, toReview, toBeFixed, toPublish, published);
    }

    private void saveDefaultTaskStatus(Map<String, String> defaultTaskStatus) {
        var taskStatus = new TaskStatus();
        taskStatus.setName(defaultTaskStatus.get("name"));
        taskStatus.setSlug(defaultTaskStatus.get("slug"));
        taskStatusRepository.save(taskStatus);
    }

}
