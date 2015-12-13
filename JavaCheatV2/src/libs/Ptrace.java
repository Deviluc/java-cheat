package libs;

import com.sun.jna.Library;

public interface Ptrace extends Library {
	
	
	public long newProcess(String pExecPath, String[] pArguments);
	
	public long attachProcess(long pPID);
	
	public long detachProcess(long pPID);
	
	public long readMemory(long pPID, long pAddress);
	
	public int writeMemory(long pPID, long pAddress, int pData);
	
	public int getErrorCode();

}
