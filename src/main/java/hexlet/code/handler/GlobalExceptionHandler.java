package hexlet.code.handler;

import hexlet.code.exception.LabelHasTasksException;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.exception.TaskStatusHasTasksException;
import hexlet.code.exception.UserHasTasksException;
import io.sentry.Sentry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Sentry.captureException(ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UserHasTasksException.class)
    public ResponseEntity<String> handleUserHasTasksException(UserHasTasksException ex) {
        Sentry.captureException(ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(TaskStatusHasTasksException.class)
    public ResponseEntity<String> handleTaskStatusHasTasksException(TaskStatusHasTasksException ex) {
        Sentry.captureException(ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(LabelHasTasksException.class)
    public ResponseEntity<String> handleLabelHasTasksException(LabelHasTasksException ex) {
        Sentry.captureException(ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
