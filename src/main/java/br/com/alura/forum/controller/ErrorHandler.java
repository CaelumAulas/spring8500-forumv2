package br.com.alura.forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.alura.forum.controller.dto.output.ValidationErrorsOutputDto;

@RestControllerAdvice
public class ErrorHandler {
	@Autowired
	private MessageSource messageSource;
	
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationErrorsOutputDto handleArgumentNotValidException(MethodArgumentNotValidException e) {
    	ValidationErrorsOutputDto validationErrorsOutputDto = new ValidationErrorsOutputDto();
    	
    	BindingResult bindingResult = e.getBindingResult();
    	List<FieldError> fieldErrors = bindingResult.getFieldErrors();
    	List<ObjectError> globalErrors = bindingResult.getGlobalErrors();
    	
    	fieldErrors.forEach(fieldError ->
    		validationErrorsOutputDto.addFieldErrorDto(fieldError.getField(),
    			messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()))
    	);
    	
    	globalErrors.forEach(globalError -> 
    			validationErrorsOutputDto.addError(
    					messageSource.getMessage(globalError, LocaleContextHolder.getLocale())));
    	
    	return validationErrorsOutputDto;
    }
}
