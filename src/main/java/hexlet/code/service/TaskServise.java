package hexlet.code.service;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServise {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    public List<TaskDTO> getAllTasks() {
        var tasks = taskRepository.findAll();
        var result = tasks.stream()
            .map(taskMapper::map)
            .toList();

        return result;
    }

    public  TaskDTO createTask(TaskCreateDTO taskCreateDTO) {
        var task = taskMapper.map(taskCreateDTO);
        taskRepository.save(task);
        var taskDTO = taskMapper.map(task);

        return taskDTO;
    }

    public TaskDTO findTaskById(Long id) {
        var task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + "not found"));
        var taskDTO = taskMapper.map(task);

        return taskDTO;
    }

    public TaskDTO updateTask(TaskUpdateDTO taskUpdateDTO, Long id) {
        var task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + "not found"));
        taskMapper.update(taskUpdateDTO, task);
        taskRepository.save(task);
        var taskDTO = taskMapper.map(task);

        return taskDTO;
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
