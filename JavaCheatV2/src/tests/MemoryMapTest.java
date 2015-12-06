package tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import libs.MemoryRange;
import org.testng.annotations.Test;
import tools.MemoryMap;

public class MemoryMapTest {
	
	@Test
	public void printFirefoxRanges() throws IOException {
		Process p = Runtime.getRuntime().exec("pidof firefox");
		BufferedReader processRead = new BufferedReader(new InputStreamReader(p.getInputStream()));
		
		long pid = Long.parseLong(processRead.readLine());
		
		MemoryMap map = new MemoryMap(pid);
		List<MemoryRange> ranges = map.getWritableRange();
		
		for (MemoryRange range : ranges) {
			System.out.println("Start: " + range.getStart() + " End: " + range.getEnd() + " Read: " + range.canRead() + " Write: " + range.canWrite() + " Execute: " + range.canExecute() + " Private: " + range.isPrivate() + " File: " + range.getFile());
		}
		
	}

}
