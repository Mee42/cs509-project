package cs509.backend.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.net.ConnectException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<String> handleDatabaseConnectException(ConnectException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fail connection with database: "+ e.getMessage());
    }
}
