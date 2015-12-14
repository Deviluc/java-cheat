package debugger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import exceptions.model.DebuggerException;

public class GnuDebugger {
	
	private Process gdb;
	private InputStream processInputStream;
	private OutputStream processOutputStream;
	private BufferedWriter outputWriter;
	private Thread gdbThread;
	private Error lastError;
	
	private boolean attached, showOutput, processing, success, running, working;
	
	private List<String> informationList;
	

	public GnuDebugger() throws IOException {
		gdb = Runtime.getRuntime().exec("gdb --interpreter mi");
		processInputStream = gdb.getInputStream();
		processOutputStream = gdb.getOutputStream();
		outputWriter = new BufferedWriter(new OutputStreamWriter(processOutputStream));
		
		informationList = new ArrayList<String>();
		
		attached = false;
		showOutput = false;
		processing = false;
		success = false;
		working = true;
		
		gdbThread = new Thread(new GnuDebuggerInputParser());
		gdbThread.start();
		
		while (working) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void attachTo(final int pid) {
		if (commandExec("attach " + pid)) {
			commandExec("c");
		}		
	}
	
	public void detach() {
		commandExec("detach");
	}
	
	public void dispose() {
		gdbThread.interrupt();
	}
	
	public void setShowOutput(final boolean showOutput) {
		this.showOutput = showOutput;
	}
	
	private void commandDone(final String result) {
		if (result.startsWith("^running")) {
			processing = false;
			success = true;
		}
		
		if (result.startsWith("^done")) {
			processing = false;
			success = true;
			
			//TODO parse results
		}
		
		if (result.startsWith("^error")) {
			lastError = new Error(result.substring(result.lastIndexOf("=")).replace("\"", ""));
			processing = false;
			success = false;
		}
	}
	
	
	private void handleAsyncRecord(final String record) {
		StringTokenizer token = new StringTokenizer(record, ",");
		
		if (token.nextToken().equals("stopped")) {
			
			System.out.println("Process stopped:");
			
			while (token.hasMoreTokens()) {
				String param = token.nextToken();
				System.out.println(param);
			}
			
			running = false;
		} else {
			running = true;
		}
		
	}
	
	private boolean commandExec(final String command) {
		
		informationList.clear();
		
		try {
			outputWriter.write(command);
			outputWriter.newLine();
			outputWriter.flush();
		} catch (IOException e1) {
			return false;
		}
		
		processing = true;
		
		while (processing) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if (showOutput) {
			for (String line : informationList) {
				System.out.println(line.replace("\\n", ""));
			}
		}
		
		if (!success) {
			throw lastError;
		}
		
		return success;
	}
	
	
	private class GnuDebuggerInputParser implements Runnable {
		
		private BufferedReader bufferedReader;

		@Override
		public void run() {
			bufferedReader = new BufferedReader(new InputStreamReader(processInputStream));
			
			while (true) {
				String line = null;
				
				if (processing) {
					working = true;
				}
				
				if (working) {
					try {
						line = bufferedReader.readLine();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				if (line != null) {
					
					if (line.startsWith("&") || line.startsWith("@")) {
						// gdb debug-log and target output
					} else if (line.startsWith("~")) {
						informationList.add(line.substring(1));
					} else if (line.startsWith("^")) {
						commandDone(line);
					} else if (line.startsWith("*")) {
						handleAsyncRecord(line.substring(1));
					} else if (line.trim().equals("(gdb)")) {
						working = false;
					}
					
				} else {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		
		
		
	}

}
