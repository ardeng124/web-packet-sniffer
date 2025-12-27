package backendsrc.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import backendsrc.service.exception.CaptureSessionStateInvalidException;
import backendsrc.service.exception.InterfaceNotFoundException;

@RestControllerAdvice
public class ApiErrorHandler {
    @ExceptionHandler(InterfaceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleInterfaceNotFound(InterfaceNotFoundException ex) {
            ApiErrorResponse errorResponse = new ApiErrorResponse(HttpStatus.NOT_FOUND.value(),"Not Found", ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CaptureSessionStateInvalidException.class)
    public ResponseEntity<ApiErrorResponse> handleCaptureAlreadyRunning(CaptureSessionStateInvalidException ex) {
            ApiErrorResponse errorResponse = new ApiErrorResponse(HttpStatus.CONFLICT.value(),"Conflict", ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    
}
