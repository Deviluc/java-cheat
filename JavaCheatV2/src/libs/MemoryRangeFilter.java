package libs;

public class MemoryRangeFilter {
	
	private String permissionRegex, filenameRegex, filenameString;
	private Boolean fromFile, isStack, isHeap;
	private double minSize, maxSize; 
	
	
	public MemoryRangeFilter(final String permissionRegex, final boolean fromFile, final boolean isStack, final boolean isHeap) {
		this.permissionRegex = permissionRegex;
		this.fromFile = fromFile;
		this.isStack = isStack;
		this.isHeap = isHeap;
		
		minSize = -1.;
		maxSize = -1.;
	}
	
	public MemoryRangeFilter(final String permissionRegex) {
		this.permissionRegex = permissionRegex;
		minSize = -1.;
		maxSize = -1.;
	}
	
	public void setFromFile(final boolean fromFile) {
		this.fromFile = fromFile;
	}
	
	public void setIsStack(final boolean isStack) {
		this.isStack = isStack;
	}
	
	public void setIsHeap(final boolean isHeap) {
		this.isHeap = isHeap;
	}
	
	public void setMinSize(final double min) {
		minSize = min;
	}
	
	public void setMaxSize(final double max) {
		maxSize = max;
	}
	
	public void setFilenameRegex(final String filenameRegex) {
		this.filenameRegex = filenameRegex;
	}
	
	public void setFilenameString(final String filenameString) {
		this.filenameString = filenameString;
	}
	
	public boolean matchRange(MemoryRange range) {
		
		if (!range.matchPermissionRegex(permissionRegex)) {
			return false;
		}
		
		if (fromFile != null) {
			if (fromFile != range.fileMapped()) {
				return false;
			}
		}
		
		if (isStack != null) {
			if (isStack != range.isStack()) {
				return false;
			}
		}
		
		if (isHeap != null) {
			if (isHeap != range.isHeap()) {
				return false;
			}
		}
		
		double size = range.getByteSize() / (1024. * 1024.);
		
		if (minSize != -1.) {
			if (size < minSize) {
				return false;
			}
		}
		
		if (maxSize != -1.) {
			if (size > maxSize) {
				return false;
			}
		}
		
		if (filenameRegex != null) {
			if (!range.getFile().matches(filenameRegex)) {
				return false;
			}
		} else if (filenameString != null) {
			if (!range.getFile().toLowerCase().contains(filenameString.toLowerCase())) {
				return false;
			}
		}
		
		
		return true;
	}
	
	@Override
	public MemoryRangeFilter clone() {
		MemoryRangeFilter clone = new MemoryRangeFilter(permissionRegex);
		clone.setFilenameRegex(filenameRegex);
		clone.setFilenameString(filenameString);
		clone.setFromFile(fromFile);
		clone.setIsHeap(isHeap);
		clone.setIsStack(isStack);
		clone.setMaxSize(maxSize);
		clone.setMinSize(minSize);
		
		return clone;
	}

}
