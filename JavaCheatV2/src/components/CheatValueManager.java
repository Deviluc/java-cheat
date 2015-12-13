package components;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import exceptions.model.DebuggerException;
import interfaces.Event;
import interfaces.Trigger;
import libs.Constants;
import models.Value;
import tools.MemoryAccess;
import visuals.CheatValueEditWindow;
import visuals.tables.CheatValueTable;
import visuals.tables.CheatValueTable.CheatTableModel;

public class CheatValueManager implements Trigger {
	
	private MemoryAccess memoryAccess;
	private List<Value> valueList;
	private Thread valueCheckerThread;
	private CheatValueTable table;
	
	public CheatValueManager() {
		valueList = new ArrayList<Value>();
		valueCheckerThread = new Thread(new ValueChecker());
	}

	public CheatValueManager(final MemoryAccess memoryAccess) {
		this.memoryAccess = memoryAccess;
		valueList = new ArrayList<Value>();
		
		valueCheckerThread = new Thread(new ValueChecker());
		valueCheckerThread.start();
	}
	
	public void setMemoryAccess(final MemoryAccess memoryAccess) {
		this.memoryAccess = memoryAccess;
		valueCheckerThread.start();
	}
	
	public void setCheatValueTable(final CheatValueTable table) {
		this.table = table;
	}
	
	
	public void addValue(final Value value) {
		if (!valueList.contains(value)) {
			valueList.add(value);
		}
	}
	
	public void addValues(final List<Value> values) {
		for (Value value : values) {
			addValue(value);
		}
	}
	
	public void removeValue(final Value value) {
		valueList.remove(value);
	}
	
	public void removeValues(final List<Value> values) {
		for (Value value : values) {
			removeValue(value);
		}
	}
	
	public List<Value> getValues() {
		return valueList;
	}
	
	public void writeValue(Value value) {
		try {
			memoryAccess.write(value.getAddress(), value.getValueByteBuffer());
		} catch (IllegalArgumentException | DebuggerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void readValue(Value value) {
		if (value.getFreeze()) {
			writeValue(value);
		}
		
		try {
			ByteBuffer buffer = memoryAccess.read(value.getAddress(), Constants.getByteLength(value.getValueType()));
			value.setValue(buffer);			
		} catch (IllegalArgumentException | IOException e) {
			value.setReadable(false);
		}
	}
	
	private class ValueChecker implements Runnable {

		@Override
		public void run() {
			while (true) {
				for (Value value : valueList) {
					readValue(value);
				}
				
				if (table != null) {
					table.updateTable();
				}
				
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	

	@Override
	public void triggerEvent(Event event) {
		// TODO Auto-generated method stub
		
	}

}
