package org.control_parental;


import org.control_parental.exceptions.ResourceAlreadyExistsExpeption;
import org.control_parental.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(ResourceNotFoundException e) {return e.getMessage();}

    @ExceptionHandler(ResourceAlreadyExistsExpeption.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleResourseAlreadyExistsException(ResourceAlreadyExistsExpeption e) {return e.getMessage();}


}
