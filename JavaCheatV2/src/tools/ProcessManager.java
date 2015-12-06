package tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class ProcessManager {
	
	private Hashtable<Integer, String> processTable;

	public ProcessManager() throws IOException {
		parseProcessList();
	}
	
	public void refreshList() throws IOException {
		parseProcessList();
	}
	
	public Hashtable<Integer, String> getProcessTable() {
		return processTable;
	}
	
	private void parseProcessList() throws IOException {
		processTable = new Hashtable<>();
		
		Process p = Runtime.getRuntime().exec("ps ax");
		BufferedReader processRead = new BufferedReader(new InputStreamReader(p.getInputStream()));
		
		processRead.readLine();
		String line;
		
		while ((line = processRead.readLine()) != null) {
			StringTokenizer token = new StringTokenizer(line, " ");
			int pid = Integer.parseInt(token.nextToken().trim());
			
			for (int i = 0; i < 3; i++) {
				token.nextToken();
			}
			
			String name = token.nextToken();
			
			while (token.hasMoreTokens()) {
				name += " " + token.nextToken();
			}
			
			if (name.contains("/")) {
				name = name.substring(name.lastIndexOf("/") + 1);
			}
			
			processTable.put(pid, name);
		}
	}

}
