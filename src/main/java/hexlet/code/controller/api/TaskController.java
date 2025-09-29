package hexlet.code.controller.api;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskParamsDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.service.TaskServise;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskServise taskServise;

    @GetMapping("")
    public ResponseEntity<List<TaskDTO>> index(TaskParamsDTO taskParamsDTO,
                                               @RequestParam(defaultValue = "1") int page) {
        var tasks = taskServise.getAllTasks(taskParamsDTO, page).getContent();

        return ResponseEntity.ok()
            .header("X-Total-Count", String.valueOf(tasks.size()))
            .body(tasks);
    }

    @GetMapping("/{id}")
    public TaskDTO show(@PathVariable Long id) {
        return taskServise.findTaskById(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@Valid @RequestBody TaskCreateDTO taskCreateDTO) {
        return taskServise.createTask(taskCreateDTO);
    }

    @PutMapping("/{id}")
    public TaskDTO update(@Valid @RequestBody TaskUpdateDTO taskUpdateDTO, @PathVariable Long id) {
        return taskServise.updateTask(taskUpdateDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        taskServise.deleteTask(id);
    }
}
