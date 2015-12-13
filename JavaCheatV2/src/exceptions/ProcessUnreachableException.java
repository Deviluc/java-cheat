package exceptions;

import exceptions.model.DebuggerException;

public class ProcessUnreachableException extends DebuggerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 31465283902107067L;

	public ProcessUnreachableException() {
		// TODO Auto-generated constructor stub
	}

	public ProcessUnreachableException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ProcessUnreachableException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ProcessUnreachableException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ProcessUnreachableException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
