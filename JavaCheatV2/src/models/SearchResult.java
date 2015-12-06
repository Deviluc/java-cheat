package models;

public class SearchResult {
	
	private long address, value;

	public SearchResult(final long pAddress, final long pValue) {
		address = pAddress;
		value = pValue;
	}
	
	public long getAddress() {
		return address;
	}
	
	public long getValue() {
		return value;
	}
	
	public String[] toStringArray() {
		return new String[]{Long.toString(address, 16).toUpperCase(), Long.toString(value)};
	}

}
