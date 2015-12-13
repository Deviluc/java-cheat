package debugger;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;

import exceptions.InvalidOptionException;
import exceptions.InvalidRequestException;
import exceptions.MemoryAllocationException;
import exceptions.ProcessAccessDeniedException;
import exceptions.ProcessUnreachableException;
import exceptions.UnkownErrorException;
import exceptions.model.DebuggerException;
import libs.Ptrace;

public class PTraceDebugger {
	
	public long pid;
	public boolean isAttached;
	private Ptrace ptrace;
	
	public PTraceDebugger() {
		
		isAttached = false;
		


	    System.setProperty("jna.library.path", System.getProperty("user.dir") + "/native-libs");
		ptrace = (Ptrace) Native.loadLibrary("ptrace", Ptrace.class);
	}
	
	private void handleErrors(int pErr) throws InvalidRequestException, ProcessAccessDeniedException, ProcessUnreachableException, InvalidOptionException, MemoryAllocationException, UnkownErrorException {
		if (pErr == 5 || pErr == 14) {
			throw new InvalidRequestException("Ptrace error: EIO");
		} else if (pErr == 1) {
			throw new ProcessAccessDeniedException("Ptrace error: EPERM");
		} else if (pErr == 3) {
			throw new ProcessUnreachableException("Ptrace error: ESRCH");
		} else if (pErr == 22) {
			throw new InvalidOptionException("Ptrace error: EINVAL");
		} else if (pErr == 16) {
			throw new MemoryAllocationException("Ptrace error: EBUSY");
		} else if (pErr > 0) {
			throw new UnkownErrorException("Unkown errorcode: "+pErr);
		}
	}
	
	public void attachTo(final long pPID) throws DebuggerException {
		if (!isAttached) {
			int attachResult = (int) ptrace.attachProcess(pPID);
			
			if (attachResult < 0) {
				handleErrors(ptrace.getErrorCode());
			} else {
				pid = pPID;
				isAttached = true;
			}
		}
	}
	
	public void detach() throws DebuggerException {
		if (isAttached) {
			int detachResult = (int) ptrace.detachProcess(pid);
			
			if (detachResult < 0) {
				handleErrors(ptrace.getErrorCode());
			} else {
				isAttached = false;
			}
		}
	}
	
	public void reAttach() throws DebuggerException {
		attachTo(pid);
	}
	
	public long read(long pAddress) throws DebuggerException {
		long readResult = ptrace.readMemory(pid, pAddress);
		int errorCode = ptrace.getErrorCode();
		
		if (readResult == -1 && errorCode != 0) {
			handleErrors(errorCode);
		}
		
		return readResult;
	}
	
	public void write(long pid, long pAddress, byte pData) throws DebuggerException {
		int writeResult = ptrace.writeMemory(pid, pAddress, pData);
		
		if (writeResult != 0) {
			handleErrors(ptrace.getErrorCode());
		}
	}

}
