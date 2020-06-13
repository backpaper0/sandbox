package circuitbreaker;

public class CallingException extends RuntimeException {

	public CallingException(Exception e) {
		super(e);
	}
}
