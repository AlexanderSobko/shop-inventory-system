package ru.kitayanochka.shop_inventory_system.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFoundExceptions(NotFoundException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> handleAlreadyExistsException(AlreadyExistsException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorMessage> handleAlreadyExistsException(ResponseStatusException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.status(exception.getStatusCode()).body(new ErrorMessage(exception.getReason()));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorMessage> handleHandlerMethodValidationException(HandlerMethodValidationException exception) {
        log.error(exception.getMessage());
        String errorMessage = exception.getAllValidationResults().stream()
                .flatMap(result -> result.getResolvableErrors().stream())
                .map(MessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(new ErrorMessage(errorMessage));
    }

//    @ExceptionHandler(MethodValidationException.class)
//    public ResponseEntity<ErrorMessage> handleMethodValidationException(MethodValidationException exception) {
//        log.error(exception.getMessage());
//        String errorMessage = exception.getAllValidationResults().stream()
//                .flatMap(e -> e.getResolvableErrors().stream())
//                .map(MessageSourceResolvable::getDefaultMessage)
//                .collect(Collectors.joining(";"));
//        return ResponseEntity.status(400).body(new ErrorMessage(errorMessage));
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationException(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage());
        String errorMessage = exception.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(";"));
        return ResponseEntity.status(exception.getStatusCode()).body(new ErrorMessage(errorMessage));
    }

}
