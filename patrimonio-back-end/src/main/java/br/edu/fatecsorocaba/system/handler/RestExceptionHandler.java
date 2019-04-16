package br.edu.fatecsorocaba.system.handler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.edu.fatecsorocaba.system.error.ResourceNotFoundDetails;
import br.edu.fatecsorocaba.system.error.ResourceNotFoundException;
import br.edu.fatecsorocaba.system.error.ValidationErrorDetails;

@ControllerAdvice
public class RestExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rfnException){
		ResourceNotFoundDetails rnfDetails = new ResourceNotFoundDetails(
				"Resource not found", HttpStatus.NOT_FOUND.value(),
				rfnException.getMessage(), new Date().getTime(),
				rfnException.getClass().getName()); 
		return new ResponseEntity<>(rnfDetails, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException 
			manvException){
		List<FieldError> fieldErrors = manvException.getBindingResult().getFieldErrors();
		String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining("; "));
		String fieldMessages = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining("; "));
		ValidationErrorDetails manvDetails = new ValidationErrorDetails(
				"Field Validation Error", HttpStatus.BAD_REQUEST.value(),
				"Field Validation Error", new Date().getTime(),
				manvException.getClass().getName(),
				fields,
				fieldMessages); 
		return new ResponseEntity<>(manvDetails, HttpStatus.BAD_REQUEST);
	}

}
