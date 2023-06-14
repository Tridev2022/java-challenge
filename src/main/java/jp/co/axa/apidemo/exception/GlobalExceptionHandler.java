package jp.co.axa.apidemo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * Global exception handler is responsible for handling all the exception of the
 * application.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final String ERROR_500 = "Oops! something went wrong, please contact your service provider.";

	/**
	 * Exception handler for record not found.
	 *
	 * @param ex
	 * @return the response entity
	 */
	@ExceptionHandler(RecordNotFoundException.class)
	public ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException ex) {
		log.error("Record is not found in the records: ", ex);
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	/**
	 * Handle all exception.
	 *
	 * @param ex the ex
	 * @return the response entity
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleException(Exception ex) {
		log.error("Exception occured: ", ex);
		return new ResponseEntity<>(ERROR_500, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
