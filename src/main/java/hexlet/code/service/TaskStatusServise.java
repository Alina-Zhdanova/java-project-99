package hexlet.code.service;

import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.dto.TaskStatusUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.exception.UserHasTasksException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskStatusServise {
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    public List<TaskStatusDTO> getAllTaskStatuses() {
        var taskStatuses = taskStatusRepository.findAll();
        var result = taskStatuses.stream()
            .map(taskStatusMapper::map)
            .toList();

        return result;
    }

    public TaskStatusDTO createTaskStatus(TaskStatusCreateDTO taskStatusCreateDTO) {
        var taskStatus = taskStatusMapper.map(taskStatusCreateDTO);
        taskStatusRepository.save(taskStatus);
        var taskStatusDTO = taskStatusMapper.map(taskStatus);

        return taskStatusDTO;
    }

    public TaskStatusDTO findTaskStatusById(Long id) {
        var taskStatus = taskStatusRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task status with id " + id + "not found"));
        var taskStatusDTO = taskStatusMapper.map(taskStatus);

        return taskStatusDTO;
    }

    public TaskStatusDTO updateTaskStatus(TaskStatusUpdateDTO taskStatusUpdateDTO, Long id) {
        var taskStatus = taskStatusRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task status with id " + id + "not found"));
        taskStatusMapper.update(taskStatusUpdateDTO, taskStatus);
        taskStatusRepository.save(taskStatus);
        var taskStatusDTO = taskStatusMapper.map(taskStatus);

        return taskStatusDTO;
    }

    public void deleteTaskStatus(Long id) {
        try {
            taskStatusRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new UserHasTasksException("Deletion is not possible, "
                + "the TaskStatus is associated with one or more tasks.");
        }
    }
}
