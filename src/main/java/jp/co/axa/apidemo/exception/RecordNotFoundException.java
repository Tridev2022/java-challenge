package jp.co.axa.apidemo.exception;

/**
 * The Class RecordNotFoundException for result is missing for given search
 * query.
 */
public class RecordNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6393550964408266201L;

	public RecordNotFoundException(String message) {
		super(message);
	}
}
