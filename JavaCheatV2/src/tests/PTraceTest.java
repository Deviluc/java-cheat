package tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.testng.annotations.Test;

import com.sun.jna.Native;

import libs.Ptrace;

public class PTraceTest {
	
	@Test
	public void testPtraceLib() throws NumberFormatException, IOException {
		System.setProperty("jna.library.path", System.getProperty("user.dir") + "/native-libs");
		Ptrace ptrace = (Ptrace) Native.loadLibrary("ptrace", Ptrace.class);
		
		System.out.print("PID: ");
		
		BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
		
		long pid;

		pid = Long.parseLong(read.readLine());
		
		System.out.println("Attach test: "+ ptrace.attachProcess(pid));
		
		long readInt = getInput("Read address:");
		
		readInt = ptrace.readMemory(pid, readInt);
		
		System.out.println("Read test: " + readInt);
		
		if (readInt < 0) {
			System.out.println("Erro code: " + ptrace.getErrorCode());
		}
		
		/*for (long readCounter = 0x01FFFFFF; readCounter < 0x0FFFFFFF; readCounter = readCounter + 4) {
			readInt = readMemory(pid, readCounter);
			System.out.println("Read (" +readCounter+ "):\t"+readInt);
			if (readInt < 0) {
				System.out.println("Erro code: " + getErrorCode());
			}
		}*/
		
		System.out.println("Detach test: " + ptrace.detachProcess(pid));

		
		
	}
	
	private long getInput(String pMessage) {
		System.out.println(pMessage);
		BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
		long result;
		
		while (true) {
			try {
				result = Long.decode("0x" + read.readLine());
				break;
			} catch (Exception e) {
				System.out.println(pMessage);
			}
		}
		
		return result;
	}

}




