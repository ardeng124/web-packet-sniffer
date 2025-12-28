package backendsrc.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import backendsrc.service.exception.CaptureSessionStateInvalidException;
import backendsrc.service.exception.CaptureOperationFailedException;
import backendsrc.service.exception.InterfaceNotFoundException;

@RestControllerAdvice
public class ApiErrorHandler {
    @ExceptionHandler(InterfaceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleInterfaceNotFound(InterfaceNotFoundException ex) {
            ApiErrorResponse errorResponse = new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(),"Not Found", ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CaptureSessionStateInvalidException.class)
    public ResponseEntity<ApiErrorResponse> handleCaptureAlreadyRunning(CaptureSessionStateInvalidException ex) {
            ApiErrorResponse errorResponse = new ApiErrorResponse(HttpStatus.CONFLICT.value(),"Conflict", ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CaptureOperationFailedException.class)
    public ResponseEntity<ApiErrorResponse> handleCaptureEngineError(CaptureOperationFailedException ex) {
            ApiErrorResponse errorResponse = new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Capture Error", ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
