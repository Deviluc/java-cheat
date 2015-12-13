package exceptions;

import exceptions.model.DebuggerException;

public class InvalidOptionException extends DebuggerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6966937447940597942L;

	public InvalidOptionException() {
		// TODO Auto-generated constructor stub
	}

	public InvalidOptionException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public InvalidOptionException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidOptionException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidOptionException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
