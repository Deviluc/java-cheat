package exceptions;

import exceptions.model.DebuggerException;

public class MemoryAllocationException extends DebuggerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9134255029763757588L;

	public MemoryAllocationException() {
		// TODO Auto-generated constructor stub
	}

	public MemoryAllocationException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public MemoryAllocationException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public MemoryAllocationException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public MemoryAllocationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
