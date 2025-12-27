package backendsrc.api;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiErrorHandler {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiErrorResponse> handleInterfaceNotFound(NoSuchElementException ex) {
            ApiErrorResponse errorResponse = new ApiErrorResponse(HttpStatus.NOT_FOUND.value(),"Not Found", ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
