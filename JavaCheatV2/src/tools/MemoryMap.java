package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import libs.MemoryRange;
import libs.MemoryRangeFilter;

public class MemoryMap {
	
	private long pid;
	private List<MemoryRange> ranges;
	
	/**
	 * Creates a new MemoryMap object using the process-ID pPID
	 * @param pPID Process-ID as long
	 * @throws IOException Thrown when the process is not existing
	 */
	public MemoryMap(final long pPID) throws IOException {
		pid = pPID;
		
		loadRanges();
	}
	
	/**
	 * Returns a {@link List} of {@Link MemoryRange}
	 * @return List of memory ranges as {@link List}
	 * @throws IOException Thrown when the process is not existing
	 */
	public List<MemoryRange> getRanges() throws IOException {
		loadRanges();
		return ranges;
	}
	
	/**
	 * Returns a {@link List} of writable  {@link MemoryRange}
	 * @return List of writable ranges as {@link List}
	 * @throws IOException Thrown when the process is not existing
	 */
	public List<MemoryRange> getWritableRange() throws IOException {
		loadRanges();
		List<MemoryRange> result = new ArrayList<MemoryRange>();
		
		for (MemoryRange range : ranges) {
			if (range.canWrite()) {
				result.add(range);
			}
		}
		
		return result;
		
	}
	
	public List<MemoryRange> getRange(final String pPermissionRegex, final boolean fromFile) throws IOException {
		loadRanges();
		List<MemoryRange> result = new ArrayList<MemoryRange>();
		
		for (MemoryRange range : ranges) {
			if (range.matchPermissionRegex(pPermissionRegex) && range.fileMapped() == fromFile) {
				result.add(range);
			}
		}
		
		return result;
	}
	
	public List<MemoryRange> getFilteredRanges(final List<MemoryRangeFilter> rangeFilters) throws IOException {
		loadRanges();
		List<MemoryRange> result = new ArrayList<MemoryRange>();
		
		for (MemoryRange range : ranges) {
			for (MemoryRangeFilter filter : rangeFilters) {
				if (filter.matchRange(range)) {
					result.add(range);
					break;
				}
			}
		}
		
		return result;
	}
	
	private void loadRanges() throws IOException {
		ranges = new ArrayList<MemoryRange>();
		
		BufferedReader read = new BufferedReader(new FileReader(new File("/proc/" + pid + "/maps")));
		
		String line;
		
		while ((line = read.readLine()) != null && !line.contains("[vsyscall]")) {
			StringTokenizer token = new StringTokenizer(line, " ");
			
			StringTokenizer rangeToken = new StringTokenizer(token.nextToken(), "-");
			long start = Long.decode("0x" + rangeToken.nextToken());
			long end = Long.decode("0x" + rangeToken.nextToken());
			String permissions = token.nextToken();
			long offset = Long.decode("0x" + token.nextToken());
			String device = token.nextToken();
			long inode = Long.parseLong(token.nextToken());
			
			if (token.countTokens() == 1) {
				String file = token.nextToken();
				ranges.add(new MemoryRange(start, end, permissions, offset, device, inode, file));
			} else {
				ranges.add(new MemoryRange(start, end, permissions, offset, device, inode));
			}
			
		}
		
		read.close();
	}

}
