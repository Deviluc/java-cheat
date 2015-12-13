package models;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;

import libs.Constants;

public class Value {
	
	private long address;
	private long value;
	private int valueType;
	private boolean readable;
	
	private String description;
	private int[] offsets;
	private boolean freezeValue;
	private boolean formatHex;

	public Value(final long address) {
		this.address = address;
		valueType = -1;
		readable = true;
		formatHex = false;
	}
	
	public void setAdress(final long address) {
		this.address = address;
	}
	
	public void setDescription(final String description) {
		this.description = description;
	}
	
	public void setFormatHex(final boolean formatHex) {
		this.formatHex = formatHex;
	}
	
	public void setOffsets(final int[] offsets) {
		this.offsets = offsets;
	}
	
	public void setFreeze(final boolean freeze) {
		freezeValue = freeze;
	}
	
	public void setReadable(final boolean readable) {
		this.readable = readable;
	}
	
	public void setValue(final long value) {
		this.value = value;
		
		if (valueType == -1) {
			guessValueType();
		}
	}
	
	public void setValue(final ByteBuffer buffer) {
		if (valueType == Constants.BYTE || valueType == Constants.CHAR) {
			value = (long) buffer.get();
		} else if (valueType == Constants.SHORT) {
			value = (long) buffer.getShort();
		} else if (valueType == Constants.INT) {
			value = (long) buffer.getInt();
		} else {
			value = buffer.getLong();
		}
	}
	
	public void setValueType(final int valueType) {
		this.valueType = valueType;
	}
	
	public long getAddress() {
		return address;
	}
	
	public String getFormattedAddress() {
		
		if (address <= 0xFFFFFFFF) {
			return String.format("%08x", address).toUpperCase();
		}
		
		return String.format("%016x", address).toUpperCase();
	}
	
	
	public String getFormattedValue() {
		if (formatHex) {
			return Long.toHexString(value).toLowerCase();
		}
		
		if (valueType == Constants.FLOAT) {
			return Float.toString(Float.intBitsToFloat((int) value));
		}
		
		if (valueType == Constants.DOUBLE) {
			return Double.toString(Double.longBitsToDouble(value));
		}
		
		return value + "";
	}
	
	public boolean getFormatHex() {
		return formatHex;
	}
	
	public boolean isReadable() {
		return readable;
	}
	
	public long getValue() {
		return value;
	}
	
	public ByteBuffer getValueByteBuffer() {
		ByteBuffer buffer = ByteBuffer.allocate(Constants.getByteLength(valueType));
		buffer.order(ByteOrder.nativeOrder());
		
		if (valueType == Constants.BYTE || valueType == Constants.CHAR) {
			buffer.put((byte) value);
		} else if (valueType == Constants.SHORT) {
			buffer.putShort((short) value);
		} else if (valueType == Constants.INT) {
			buffer.putInt((int) value);
		} else {
			buffer.putLong(value);
		}
		
		buffer.position(0);
		
		return buffer;
	}
	
	public int getValueType() {
		return valueType;
	}
	
	public int[] getOffsets() {
		return offsets;
	}
	
	public String getDescription() {
		return description;
	}
	
	public boolean getFreeze() {
		return freezeValue;
	}
	
	private void guessValueType() {
		//TODO guess the value-type (Pointer, Float/Double, Number, String)
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj.getClass() == this.getClass()) {
			Value value = (Value) obj;
			
			if (value.getAddress() == this.getAddress()) {
				if (this.offsets == null) {
					return true;
				} else if (value.getOffsets().length == this.getOffsets().length) {
					for (int i = 0; i < offsets.length; i++) {
						if (offsets[i] != value.getOffsets()[i]) {
							return false;
						}
					}
					
					return true;
				}
			}
			
			
		}
		
		return false;
	}

}
