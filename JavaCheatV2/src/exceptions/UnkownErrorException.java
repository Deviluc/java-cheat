package exceptions;

import exceptions.model.DebuggerException;

public class UnkownErrorException extends DebuggerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -77568647531618553L;

	public UnkownErrorException() {
		// TODO Auto-generated constructor stub
	}

	public UnkownErrorException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UnkownErrorException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public UnkownErrorException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UnkownErrorException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
