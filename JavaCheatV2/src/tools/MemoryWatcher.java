package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import exceptions.ProcessNotFoundException;
import interfaces.Notifiable;
import libs.Constants;
import models.Value;

public class MemoryWatcher implements Runnable {
	
	private RandomAccessFile memory;
	private List<List<Value>> watchList;
	private List<Notifiable> watchListNotifiables;
	private Thread watcherThread;
	private boolean threadRunning;
	
	public MemoryWatcher() {
		threadRunning = false;
		
		watcherThread = new Thread(this);
		watcherThread.start();		
		
		watchList = new ArrayList<List<Value>>();
		watchListNotifiables = new ArrayList<Notifiable>();
	}

	public MemoryWatcher(int pid) throws ProcessNotFoundException {
		try {
			memory = new RandomAccessFile(new File("/proc/" + pid + "/mem"), "rw");
		} catch (FileNotFoundException e) {
			throw new ProcessNotFoundException("The process with id '" + pid + "' could not be found!");
		}
		
		threadRunning = false;
		
		watcherThread = new Thread(this);
		watcherThread.start();		
		
		watchList = new ArrayList<List<Value>>();
		watchListNotifiables = new ArrayList<Notifiable>();
	}
	
	public void registerComponent(final Notifiable component, final List<Value> valueList) {
		if (!watchListNotifiables.contains(component)) {
			watchList.add(valueList);
			watchListNotifiables.add(component);
			
			if (!threadRunning) {
				threadRunning = true;
			}
		}
	}
	
	public void unregisterComponent(final Notifiable component) {
		if (watchListNotifiables.contains(component)) {			
			int i = watchListNotifiables.indexOf(component);
			watchList.remove(i);
			watchListNotifiables.remove(i);
			
			if (watchListNotifiables.size() == 0) {
				threadRunning = false;
			}
		}
	}
	
	public void setPID(final int pid) throws ProcessNotFoundException {
		threadRunning = false;
		
		try {
			memory = new RandomAccessFile(new File("/proc/" + pid + "/mem"), "rw");
		} catch (FileNotFoundException e) {
			throw new ProcessNotFoundException("The process with id '" + pid + "' could not be found!");
		}
		
		watcherThread = new Thread(this);
		watcherThread.start();		
		
		watchList = new ArrayList<List<Value>>();
		watchListNotifiables = new ArrayList<Notifiable>();
	}

	@Override
	public void run() {
		
		while (true) {
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (!threadRunning || memory == null) {				
				continue;
			}
			
			int i = 0;
			
			for (Notifiable component : watchListNotifiables) {
				boolean doNotify = false;
				List<Integer> changedIndexes = new ArrayList<Integer>();
				List<Value> valueList = watchList.get(i);
				
				for (Value value : valueList) {
					try {
						long newValue = readValue(value);
						
						if (newValue != value.getValue()) {
							value.setValue(newValue);
							
							changedIndexes.add(valueList.indexOf(value));
							doNotify = true;
						}
						
						value.setReadable(true);
					} catch (IOException e) {
						value.setValue(0);
						value.setReadable(false);
						doNotify = true;
					}
				}
				
				if (doNotify) {
					component.notifyListChanged(changedIndexes);
				}
				
				i++;
			}
		}
		
	}
	
	private long readValue(Value value) throws IOException {
		int valueType = value.getValueType();
		
		ByteBuffer buffer = ByteBuffer.allocateDirect(Constants.getByteLength(valueType));
		
		buffer.order(ByteOrder.nativeOrder());
		
		memory.seek(value.getAddress());
		memory.getChannel().read(buffer);
		
		buffer.position(0);
		
		if (valueType == Constants.BYTE) {
			return (long) buffer.get();
		} else if (valueType == Constants.SHORT) {
			return (long) buffer.getShort();
		} else if (valueType == Constants.INT) {
			return (long) buffer.getInt();
		} else {
			return buffer.getLong();
		}
	}


}
