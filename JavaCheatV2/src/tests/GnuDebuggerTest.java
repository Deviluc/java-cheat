package tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.testng.annotations.Test;

import debugger.GnuDebugger;

public class GnuDebuggerTest {

	@Test
	public void testGnuDebugger() throws Exception {
		Process p = Runtime.getRuntime().exec("pidof firefox");
		BufferedReader processRead = new BufferedReader(new InputStreamReader(p.getInputStream()));
		
		int pid = Integer.parseInt(processRead.readLine());
		
		GnuDebugger gdb = new GnuDebugger();
		gdb.setShowOutput(true);
		
		gdb.attachTo(pid);
		gdb.detach();
		
		gdb.dispose();
	}

}
