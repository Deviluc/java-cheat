package components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.testng.internal.ConstructorOrMethod;

import events.LockEvent;
import events.UpdateEvent;
import exceptions.ProcessNotFoundException;
import interfaces.Event;
import interfaces.Notifiable;
import interfaces.Trigger;
import libs.Constants;
import libs.MemoryRange;
import libs.MemoryRangeFilter;
import tools.MemoryMap;
import tools.MemoryWatcher;

public class LogicManager implements Trigger {
	
	private boolean ready;
	
	private String[] initialScanTypes;
	private String[] comparableScanTypes;
	private String[] dataTypes;
	
	private int scanCount;
	private int pid;
	
	private List<MemoryRangeFilter> memoryRangeFilterList;
	
	private SearchManager searchManager;
	private MemoryWatcher memoryWatcher;
	private MemoryMap memoryMap;
	
	private Trigger parentComponent;

	public LogicManager(final Trigger parentComponent) {
		
		this.parentComponent = parentComponent;
		
		initialScanTypes = new String[] {"Exact value", "Smaller then", "Bigger then", "Unkown initial value [slow]"};
		comparableScanTypes = new String[] {"Increased value", "Increased by", "Decreased value", "Decreased by", "Changed value", "Unchanged value"};
		dataTypes = new String[] {"Byte", "Short", "Integer", "Long", "Float", "Double", "Char"};
		
		memoryRangeFilterList = new ArrayList<MemoryRangeFilter>();
		
		MemoryRangeFilter memoryRangeFilterHeap = new MemoryRangeFilter("rw[x-][sp]");
		memoryRangeFilterHeap.setIsHeap(true);
		memoryRangeFilterList.add(memoryRangeFilterHeap);
		
		MemoryRangeFilter memoryRangeFilterStack = new MemoryRangeFilter("rw[x-][sp]");
		memoryRangeFilterStack.setIsStack(true);
		memoryRangeFilterList.add(memoryRangeFilterStack);
		
		memoryWatcher = new MemoryWatcher();
		
		ready = false;
		scanCount = 0;
	}
	
	public void setPID(final int pid) throws ProcessNotFoundException, IOException {
		this.pid = pid;
		
		searchManager = new SearchManager(pid);
		memoryMap = new MemoryMap((long) pid);
		
		memoryWatcher.setPID(pid);
		
		scanCount = 0;
		ready = true;
	}
	
	public void setMemoryRangesFilters(final List<MemoryRangeFilter> memoryRangeFilterList) {
		this.memoryRangeFilterList = memoryRangeFilterList;
	}
	
	public MemoryWatcher getMemoryWatcher() {
		return memoryWatcher;
	}
	
	public String[] getValueTypes() {
		return dataTypes;
	}
	
	public String[] getScanTypes() {
		String result[];
		
		if (scanCount > 0) {
			result = new String[initialScanTypes.length - 1 + comparableScanTypes.length];

			int i = 0;
			for (String scanType : initialScanTypes) {
				if (!scanType.contains("initial")) {
					result[i] = scanType;
					i++;
				}
			}
			
			for (String scanType : comparableScanTypes) {
				result[i] = scanType;
				i++;
			}
			
		} else {
			result = initialScanTypes;
		}

		
		return result;
	}
	
	public List<MemoryRangeFilter> getMemoryRangeFilters() {
		return memoryRangeFilterList;
	}
	
	public void search(final int selectedValueType, final int selectedScanType, final String... values) throws IllegalStateException, IllegalArgumentException, IOException {
		if (ready) {
			if (scanCount == 0) {
				searchManager.startInitialSearch(selectedValueType, selectedScanType + 100, memoryMap.getFilteredRanges(memoryRangeFilterList), values);
				scanCount++;
				
				parentComponent.triggerEvent(new UpdateEvent(Constants.TARGET_COMBOBOX_SCANTYPE));
				parentComponent.triggerEvent(new LockEvent(Constants.TARGET_COMBOBOX_VALUETYPE));
			} else {
				searchManager.startNextSearch(selectedScanType + 100, values);
			}
			
			scanCount++;
			
			UpdateEvent updateResultLabel = new UpdateEvent(Constants.TARGET_LABEL_RESULTS);
			updateResultLabel.setUpdateValues("Results (" + searchManager.getCurrentResultCount() + "):");
			parentComponent.triggerEvent(updateResultLabel);
			
			if (searchManager.getCurrentResultCount() <= 1000) {
				UpdateEvent updateResultTable = new UpdateEvent(Constants.TARGET_TABLE_SEARCH_RESULTS);
				updateResultTable.setUpdateValues(searchManager.getCurrentResultMap(), selectedValueType);
				parentComponent.triggerEvent(updateResultTable);
			}
		} else {
			throw new IllegalStateException("No process selected!");
		}
	}

	@Override
	public void triggerEvent(Event event) {
		if (event.getEventClass() == Constants.EVENT_UPDATE) {
			UpdateEvent update = (UpdateEvent) event;
			
			if (update.getTarget() == Constants.TARGET_RANGE_FILTER) {
				
			}
		}
		
	}


}
