package br.edu.fatecsorocaba.system.handler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.edu.fatecsorocaba.system.error.ErrorDetails;
import br.edu.fatecsorocaba.system.error.ResourceNotFoundException;
import br.edu.fatecsorocaba.system.error.ValidationErrorDetails;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rfnException){
		ErrorDetails errorDetails = new ErrorDetails(
				"Resource not found", HttpStatus.NOT_FOUND.value(),
				rfnException.getMessage(), new Date().getTime(),
				rfnException.getClass().getName()); 
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(PropertyReferenceException.class)
	public ResponseEntity<Object> handlePropertyReferenceException(PropertyReferenceException prException){
		ErrorDetails errorDetails = new ErrorDetails(
				"Property do not exists", HttpStatus.BAD_REQUEST.value(),
				prException.getMessage(), new Date().getTime(),
				prException.getClass().getName()); 
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining("; "));
		String fieldMessages = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining("; "));
		ValidationErrorDetails manvDetails = new ValidationErrorDetails(
				"Field Validation Error", HttpStatus.BAD_REQUEST.value(),
				"The fields below does not match the expected values in validations", new Date().getTime(),
				ex.getClass().getName(),
				fields,
				fieldMessages); 
		return new ResponseEntity<>(manvDetails, HttpStatus.BAD_REQUEST);
	}
	
	@Override
	public ResponseEntity<Object> handleExceptionInternal(
			Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(
				"Internal Exception", status.value(),
				ex.getMessage(), new Date().getTime(),
				ex.getClass().getName()); 
		return new ResponseEntity<>(errorDetails, headers, status);
	}
}
