package hexlet.code.exception;

public class TaskStatusHasTasksException extends RuntimeException {
    public TaskStatusHasTasksException(String message) {
        super(message);
    }
}
