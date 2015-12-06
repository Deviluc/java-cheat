package models;

public class Value {
	
	private long address;
	private long value;
	private int valueType;
	private boolean readable;

	public Value(final long address) {
		this.address = address;
		valueType = -1;
		readable = true;
	}
	
	public void setAdress(final long address) {
		this.address = address;
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
	
	public void setValueType(final int valueType) {
		this.valueType = valueType;
	}
	
	public long getAddress() {
		return address;
	}
	
	public String getFormattedAddress() {
		return Long.toHexString(address);
	}
	
	
	public String getFormattedValue() {
		//TODO format by value-type
		return value + "";
	}
	
	public boolean isReadable() {
		return readable;
	}
	
	public long getValue() {
		return value;
	}
	
	public int getValueType() {
		return valueType;
	}
	
	private void guessValueType() {
		//TODO guess the value-type (Pointer, Float/Double, Number, String)
	}

}
