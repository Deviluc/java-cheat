package comparision;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import org.mapdb.HTreeMap;
import libs.Constants;

public class Comparator {
	
	private int valueType;
	
	private Object value;
	private Object rangeStart;
	private Object rangeEnd;
	
	private int compareType;

	public Comparator(int pValueType) {
		valueType = pValueType;
	}
	
	public int getValueType() {
		return valueType;
	}
	
	public void setValue(final Object pValue) {
		value = pValue;
	}
	
	public void setValueRange(final Object pStart, final Object pEnd) {
		rangeStart = pStart;
		rangeEnd = pEnd;
	}
	
	public void setCompareType(final int pType) {
		compareType = pType;
	}
	
	public int getCompareType() {
		return compareType;
	}
	
	public void getResults(final ByteBuffer pBuffer, final long pStartAddress, final HTreeMap<Long, Object> pMap) {
		long offset = 0;
		long saveTime = 0;
		
		while (pBuffer.hasRemaining()) {
			offset = pBuffer.position();
			Object tempValue = readObject(pBuffer);
			
			if (offset % (1024 * 1024 * 30) == 0) {
				System.out.println("Percentage done: " + (offset * 100 / pBuffer.limit())) ;
			}
			
			
			
			if (compare(tempValue)) {
				long startTime = System.currentTimeMillis();
				pMap.put(pStartAddress + offset, tempValue);
				saveTime += (System.currentTimeMillis() - startTime);
			}
		}
		
		System.out.println("SaveTime: " + saveTime + "ms");
		
	}
	
	public void getResultsNonComparative(final HTreeMap<Long, Object> oldMap, final List<Object> newValues, final HTreeMap<Long, Object> newMap) {
		
		long saveTime = 0;
		int i = 0;
		
		for (Long key : oldMap.keySet()) {
			Object tempValue = newValues.get(i);
			
			int percentage = i * 100 / newValues.size();
			
			if (percentage % 10 == 0) {
				System.out.println("Percentage done: " + percentage);
			}
			
			if (compare(tempValue)) {
				long startTime = System.currentTimeMillis();
				newMap.put(key, tempValue);
				saveTime += (System.currentTimeMillis() - startTime);
			}
			
			i++;
		}
		
		System.out.println("SaveTime: " + saveTime + "ms");
	}
	
	public void getResultsNonComparative(final ByteBuffer pBuffer, final long pStartAddress, final HTreeMap<Long, Object> oldMap, final HTreeMap<Long, Object> newMap) {
		
		long offset = 0;
		long saveTime = 0;
		
		while (pBuffer.hasRemaining()) {
			offset = pBuffer.position();
			Object tempValue = readObject(pBuffer);
			
			if (offset % (1024 * 1024 * 30) == 0) {
				System.out.println("Percentage done: " + (offset * 100 / pBuffer.limit())) ;
			}
			
			if (oldMap.containsKey((pStartAddress + offset))) {
				if (compare(tempValue)) {
					long startTime = System.currentTimeMillis();
					newMap.put(pStartAddress + offset, tempValue);
					saveTime += (System.currentTimeMillis() - startTime);
				}
			}
			
			
		}
		
		System.out.println("SaveTime: " + saveTime + "ms");
		
	}
	
	private boolean compare(final Object pValue) {
		
		if (compareType == Constants.BETWEEN) {
			return compareBetween(pValue, rangeStart, rangeEnd);
		} else if (compareType == Constants.EXACT_VALUE) {
			return compareExact(pValue, value);
		} else if (compareType == Constants.BIGGER_THEN) {
			return compareBiggerThen(pValue, value);
		} else if (compareType == Constants.SMALLER_THEN) {
			return compareSmallerThen(pValue, value);
		} else if (compareType == Constants.UNKOWN_VALUE) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean compareExact(final Object pValue, final Object pValueComp) {
		if (valueType == Constants.BYTE) {
			return ((byte) pValue == (byte) pValueComp);
		} else if (valueType == Constants.SHORT) {
			return ((short) pValue == (short) pValueComp);
		} else if (valueType == Constants.INT) {
			return ((int) pValue == (int) pValueComp);
		} else if (valueType == Constants.LONG) {
			return ((long) pValue == (long) pValueComp);
		} else if (valueType == Constants.FLOAT) {
			return ((float) pValue == (float) pValueComp);
		} else if (valueType == Constants.DOUBLE) {
			return ((double) pValue == (double) pValueComp);
		} else if (valueType == Constants.CHAR) {
			return ((char) pValue == (char) pValueComp);
		} else {
			return false;
		}
	}
	
	private boolean compareBiggerThen(final Object pValue, final Object pValueComp) {
		if (valueType == Constants.BYTE) {
			return ((byte) pValue > (byte) pValueComp);
		} else if (valueType == Constants.SHORT) {
			return ((short) pValue > (short) pValueComp);
		} else if (valueType == Constants.INT) {
			return ((int) pValue > (int) pValueComp);
		} else if (valueType == Constants.LONG) {
			return ((long) pValue > (long) pValueComp);
		} else if (valueType == Constants.FLOAT) {
			return ((float) pValue > (float) pValueComp);
		} else if (valueType == Constants.DOUBLE) {
			return ((double) pValue > (double) pValueComp);
		} else if (valueType == Constants.CHAR) {
			return ((char) pValue > (char) pValueComp);
		} else {
			return false;
		}
	}
	
	private boolean compareSmallerThen(final Object pValue, final Object pValueComp) {
		if (valueType == Constants.BYTE) {
			return ((byte) pValue < (byte) pValueComp);
		} else if (valueType == Constants.SHORT) {
			return ((short) pValue < (short) pValueComp);
		} else if (valueType == Constants.INT) {
			return ((int) pValue < (int) pValueComp);
		} else if (valueType == Constants.LONG) {
			return ((long) pValue < (long) pValueComp);
		} else if (valueType == Constants.FLOAT) {
			return ((float) pValue < (float) pValueComp);
		} else if (valueType == Constants.DOUBLE) {
			return ((double) pValue < (double) pValueComp);
		} else if (valueType == Constants.CHAR) {
			return ((char) pValue < (char) pValueComp);
		} else {
			return false;
		}
	}
	
	private boolean compareBetween(final Object pValue, final Object pStart, final Object pEnd) {
		if (compareType <= Constants.LONG) {
			long tmpValue = (long) pValue;
			
			if (tmpValue < (Long) pStart  || tmpValue > (Long) pEnd) {
				return false;
			}
		} else if (compareType == Constants.FLOAT) {
			float tmpValue = (float) pValue;
			
			if (tmpValue < (Float) pStart  || tmpValue > (Float) pEnd) {
				return false;
			}
		} else if (compareType == Constants.DOUBLE) {
			double tmpValue = (double) pValue;
			
			if (tmpValue < (Double) pStart  || tmpValue > (Double) pEnd) {
				return false;
			}
		} else if (compareType == Constants.CHAR) {
			char tmpValue = (char) pValue;
			
			if (tmpValue < (Character) pStart  || tmpValue > (Character) pEnd) {
				return false;
			}
		} else {
			return false;
		}
		
		return true;
	}
	
	private Object readObject(final ByteBuffer pBuffer) {
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
	
	@Deprecated
	private byte[] generateByteArray(final Object pValue) {
		ByteBuffer buffer = ByteBuffer.allocate(Constants.getByteLength(valueType) * 8 / Byte.SIZE);
		
		if (valueType == Constants.BYTE) {
			buffer.put((Byte) pValue);
		} else if (valueType == Constants.SHORT) {
			buffer.putShort((Short) pValue);
		} else if (valueType == Constants.INT) {
			buffer.putInt((Integer) pValue);
		} else if (valueType == Constants.LONG) {
			buffer.putLong((Long) pValue);
		} else if (valueType == Constants.FLOAT) {
			buffer.putFloat((Float) pValue);
		} else if (valueType == Constants.DOUBLE) {
			buffer.putDouble((Double) pValue);
		} else if (valueType == Constants.CHAR) {
			buffer.putChar((Character) pValue);
		}
		
		return buffer.array();
	}
	
	@Deprecated
	private Object parseByteArray(final byte[] pArray) {
		if (valueType == Constants.BYTE) {
			return ByteBuffer.wrap(pArray).get();
		} else if (valueType == Constants.SHORT) {
			return ByteBuffer.wrap(pArray).getShort();
		} else if (valueType == Constants.INT) {
			return ByteBuffer.wrap(pArray).getInt();
		} else if (valueType == Constants.LONG) {
			return ByteBuffer.wrap(pArray).getLong();
		} else if (valueType == Constants.FLOAT) {
			return ByteBuffer.wrap(pArray).getFloat();
		} else if (valueType == Constants.DOUBLE) {
			return ByteBuffer.wrap(pArray).getDouble();
		} else if (valueType == Constants.CHAR) {
			return ByteBuffer.wrap(pArray).getChar();
		} else {
			return null;
		}
	}

}
