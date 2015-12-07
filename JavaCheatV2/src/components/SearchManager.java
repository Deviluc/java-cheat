package components;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;

import tools.MemoryAccess;

import comparision.Comparator;
import exceptions.ProcessNotFoundException;

import libs.Constants;
import libs.MemoryRange;
import models.Value;

public class SearchManager {
	
	private int pid;
	private MemoryAccess memory;
	private Properties searchProps;
	private DB db;
	
	private Comparator comp;
	private List<MemoryRange> memRanges;
	private int scanCount;
	private List<HTreeMap<Long, Object>> scanMaps;
	private List<Integer> scanResultCount;
	

	public SearchManager(final int pPID) throws ProcessNotFoundException, IOException {
		pid = pPID;
		db = DBMaker.newFileDB(new File("/tmp/javacheat-db")).closeOnJvmShutdown().make();
		
		memory = new MemoryAccess(pid);
		
		searchProps = new PropertyManager().getSearchProperties();
		scanResultCount = new ArrayList<Integer>();
	}
	
	public void resetSearch() {
		clearDB();
		
		scanMaps = new ArrayList<HTreeMap<Long, Object>>();
		scanResultCount = new ArrayList<Integer>();
		scanCount = 0;
	}
	
	public void startInitialSearch(final int pValueType, final int pSearchType, final List<MemoryRange> pScanRanges, final String... pValues) throws IllegalArgumentException, IOException {
		
		memRanges = pScanRanges;
		comp = new Comparator(pValueType);
		comp.setCompareType(pSearchType);
		
		if (pValues.length == 2 && pSearchType == Constants.BETWEEN) {
			comp.setValueRange(parseString(pValues[0], pValueType), parseString(pValues[1], pValueType));
		} else if (pValues.length == 1) {
			comp.setValue(parseString(pValues[0], pValueType));
		} else {
			throw new IllegalArgumentException("Unkown search type '" + pSearchType + "' with " + pValues.length + " parameters!");
		}
		
		resetSearch();
		
		HTreeMap<Long, Object> map = db.getHashMap("scan_0");
		
		
		int bufferSize = Integer.parseInt(searchProps.getProperty("buffer.size")) * 1024;
		
		int i = 1;
		
		for (MemoryRange range : pScanRanges) {
			
			System.out.println("Scanning range [" + i + "/" + pScanRanges.size() + "] with size " + ((float) range.getByteSize() / (float) (1024 * 1024)) + "mb");
			i++;
			
			long currentAddress = range.getStart();
			
			while (currentAddress < range.getEnd()) {
				
				if ((currentAddress + bufferSize) <= range.getEnd()) {
					comp.getResults(memory.read(currentAddress, bufferSize), currentAddress, map);
					currentAddress += bufferSize;
				} else {
					comp.getResults(memory.read(currentAddress, (int) (range.getEnd() - currentAddress)), currentAddress, map);
					currentAddress = range.getEnd();
				}
			}
			
		}
		
		db.commit();
		System.out.println("Map entries: " + map.size());
		scanResultCount.add(map.size());
		
		
		scanMaps.add(map);
		scanCount = 1;
	}
	
	public void startNextSearch(final int pSearchType, final String... pValues) throws IllegalArgumentException, IOException {
		
		comp.setCompareType(pSearchType);
		
		
		if (pValues.length == 2 && pSearchType == Constants.BETWEEN) {
			comp.setValueRange(parseString(pValues[0], comp.getValueType()), parseString(pValues[1], comp.getValueType()));
		} else if (pValues.length == 1) {
			comp.setValue(parseString(pValues[0], comp.getValueType()));
		} else {
			throw new IllegalArgumentException("Unkown search type '" + pSearchType + "' with " + pValues.length + " parameters!");
		}
		
		HTreeMap<Long, Object> oldMap = scanMaps.get(scanMaps.size() - 1);
		HTreeMap<Long, Object> newMap = db.getHashMap("scan_" + scanCount);
		
		if (oldMap.size() > 10000) {
			int bufferSize = Integer.parseInt(searchProps.getProperty("buffer.size")) * 1024;
			
			int i = 1;
			
			for (MemoryRange range : memRanges) {
				
				System.out.println("Scanning range [" + i + "/" + memRanges.size() + "] with size " + ((float) range.getByteSize() / (float) (1024 * 1024)) + "mb");
				i++;
				
				long currentAddress = range.getStart();
				
				while (currentAddress < range.getEnd()) {
					
					if ((currentAddress + bufferSize) <= range.getEnd()) {
						comp.getResultsNonComparative(memory.read(currentAddress, bufferSize), currentAddress, oldMap, newMap);
						currentAddress += bufferSize;
					} else {
						comp.getResultsNonComparative(memory.read(currentAddress, (int) (range.getEnd() - currentAddress)), currentAddress, oldMap, newMap);
						currentAddress = range.getEnd();
					}
				}
				
			}
		} else {
			comp.getResultsNonComparative(oldMap, readMapValues(oldMap, comp.getValueType()), newMap);
		}
		
		
		db.commit();
		System.out.println("Map entries: " + newMap.size());
		
		scanResultCount.add(newMap.size());
		
		scanMaps.add(newMap);
		scanCount++;
	}
	
	public int getCurrentResultCount() {
		return scanResultCount.get(scanCount - 1);
	}
	
	public HTreeMap<Long, Object> getCurrentResultMap() {
		return scanMaps.get(scanCount - 1);
	}
	
	private List<Object> readMapValues(final HTreeMap<Long, Object> map, final int valueType) throws IllegalArgumentException, IOException {
		List<Object> result = new ArrayList<Object>();
		
		for (Long key : map.keySet()) {
			ByteBuffer buffer = memory.read(key, Constants.getByteLength(valueType));
			buffer.position(0);
			
			result.add(readObject(buffer, valueType));
		}
		
		return result;
	}
	
	private Object readObject(final ByteBuffer pBuffer, final int valueType) {
		if (valueType == Constants.BYTE) {
			return pBuffer.get();
		} else if (valueType == Constants.SHORT) {
			return pBuffer.getShort();
		} else if (valueType == Constants.INT) {
			return pBuffer.getInt();
		} else if (valueType == Constants.LONG) {
			return pBuffer.getLong();
		} else if (valueType == Constants.FLOAT) {
			return pBuffer.getFloat();
		} else if (valueType == Constants.DOUBLE) {
			return pBuffer.getDouble();
		} else if (valueType == Constants.CHAR) {
			return pBuffer.getChar();
		} else {
			return null;
		}
	}
	
	private Object parseString(final String pValue, final int pValueType) {
		if (pValueType == Constants.BYTE) {
			return Byte.parseByte(pValue);
		} else if (pValueType == Constants.SHORT) {
			return Short.parseShort(pValue);
		} else if (pValueType == Constants.INT) {
			return Integer.parseInt(pValue);
		} else if (pValueType == Constants.LONG) {
			return Long.parseLong(pValue);
		} else if (pValueType == Constants.FLOAT) {
			return Float.parseFloat(pValue);
		} else if (pValueType == Constants.DOUBLE) {
			return Double.parseDouble(pValue);
		} else if (pValueType == Constants.CHAR) {
			return pValue.charAt(0);
		} else {
			throw new IllegalArgumentException("Unkown value-type '" + pValueType + "'!");
		}
	}
	
	private void clearDB() {
		Map<String, Object> dbRecords = db.getAll();
		
		for (String recordName : dbRecords.keySet()) {
			db.delete(recordName);
		}
	}

	
	
	

}
