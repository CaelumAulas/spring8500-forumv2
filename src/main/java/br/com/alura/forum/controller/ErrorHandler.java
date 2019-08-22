package br.com.alura.forum.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.alura.forum.controller.dto.output.ValidationErrorsOutputDto;

@RestControllerAdvice
public class ErrorHandler {
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationErrorsOutputDto handleArgumentNotValidException(MethodArgumentNotValidException e) {
    	ValidationErrorsOutputDto validationErrorsOutputDto = new ValidationErrorsOutputDto();
    	
    	BindingResult bindingResult = e.getBindingResult();
    	List<FieldError> fieldErrors = bindingResult.getFieldErrors();
    	
    	fieldErrors.forEach(fieldError ->
    		validationErrorsOutputDto.addFieldErrorDto(fieldError.getField(),
    			fieldError.getDefaultMessage())
    	);
    	
    	return validationErrorsOutputDto;
    }
}
