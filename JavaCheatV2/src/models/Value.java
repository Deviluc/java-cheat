package models;

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
	
	public void setValueType(final int valueType) {
		this.valueType = valueType;
	}
	
	public long getAddress() {
		return address;
	}
	
	public String getFormattedAddress() {
		return Long.toHexString(address).toUpperCase();
	}
	
	
	public String getFormattedValue() {
		if (formatHex) {
			return Long.toHexString(value).toLowerCase();
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
