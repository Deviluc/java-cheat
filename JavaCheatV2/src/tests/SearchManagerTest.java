package tests;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import libs.Constants;
import libs.MemoryRangeFilter;

import org.testng.annotations.Test;

import tools.MemoryAccess;
import tools.MemoryMap;

import components.SearchManager;

public class SearchManagerTest {

	public SearchManagerTest() {
		// TODO Auto-generated constructor stub
	}
	
	@Test
	public void testInitialSearch() throws Exception {
		Process p = Runtime.getRuntime().exec("pidof Torchlight2.bin.x86_64");
		BufferedReader processRead = new BufferedReader(new InputStreamReader(p.getInputStream()));
		
		int pid = Integer.parseInt(processRead.readLine());
		
		MemoryMap memMap = new MemoryMap(pid);
		
		SearchManager manager = new SearchManager(new MemoryAccess(pid));
		
		long currentTime = System.currentTimeMillis();
		
		ArrayList<MemoryRangeFilter> rangeFilters = new ArrayList<MemoryRangeFilter>();
		
		MemoryRangeFilter filter = new MemoryRangeFilter("rw[x-][sp]");
		filter.setIsHeap(true);
		
		rangeFilters.add(filter);
		
		manager.startInitialSearch(Constants.INT, Constants.EXACT_VALUE, memMap.getFilteredRanges(rangeFilters), "1");
		
		System.out.println("Took " + (System.currentTimeMillis() - currentTime) / 1000 + "s");
		
	}
	
	@Test
	public void testMultipleSearch() throws Exception {
		Process p = Runtime.getRuntime().exec("pidof Torchlight2.bin.x86_64");
		BufferedReader processRead = new BufferedReader(new InputStreamReader(p.getInputStream()));
		
		int pid = Integer.parseInt(processRead.readLine());
		
		MemoryMap memMap = new MemoryMap(pid);
		
		SearchManager manager = new SearchManager(new MemoryAccess(pid));
		
		long currentTime = System.currentTimeMillis();
		
		ArrayList<MemoryRangeFilter> rangeFilters = new ArrayList<MemoryRangeFilter>();
		
		MemoryRangeFilter filter = new MemoryRangeFilter("rw[x-][sp]");
		filter.setIsHeap(true);
		
		MemoryRangeFilter filter2 = new MemoryRangeFilter("rw[x-][sp]");
		filter2.setIsStack(true);
		
		rangeFilters.add(filter);
		rangeFilters.add(filter2);
		
		manager.startInitialSearch(Constants.INT, Constants.EXACT_VALUE, memMap.getFilteredRanges(rangeFilters), "50");
		manager.startNextSearch(Constants.BETWEEN, "10", "100");
		manager.startNextSearch(Constants.BETWEEN, "10", "100");
		
		System.out.println("Took " + (System.currentTimeMillis() - currentTime) / 1000 + "s");
	}

}
