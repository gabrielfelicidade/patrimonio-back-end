package br.edu.fatecsorocaba.system.handler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.hibernate.exception.ConstraintViolationException;
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
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException cvException){
		String message;
		try {
			message = cvException.getCause().getMessage();
		}catch (NullPointerException e) {
			message = cvException.getMessage();
		}
		ErrorDetails errorDetails = new ErrorDetails(
				"Constraint violation", HttpStatus.BAD_REQUEST.value(),
				message, new Date().getTime(),
				cvException.getClass().getName()); 
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException enfException) {
		ErrorDetails errorDetails = new ErrorDetails(
				"Entity not found", HttpStatus.BAD_REQUEST.value(),
				enfException.getMessage(), new Date().getTime(),
				enfException.getClass().getName()); 
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining("; "));
		String fieldMessages = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining("; "));
		ValidationErrorDetails manvDetails = new ValidationErrorDetails(
				"Field Validation Error", HttpStatus.PRECONDITION_FAILED.value(),
				"The fields below does not match the expected values in validations", new Date().getTime(),
				ex.getClass().getName(),
				fields,
				fieldMessages); 
		return new ResponseEntity<>(manvDetails, HttpStatus.PRECONDITION_FAILED);
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
