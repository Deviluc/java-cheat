package libs;

public class MemoryRangeFilter {
	
	private String permissionRegex;
	private Boolean fromFile, isStack, isHeap;
	
	
	public MemoryRangeFilter(final String permissionRegex, final boolean fromFile, final boolean isStack, final boolean isHeap) {
		this.permissionRegex = permissionRegex;
		this.fromFile = fromFile;
		this.isStack = isStack;
		this.isHeap = isHeap;
	}
	
	public MemoryRangeFilter(final String permissionRegex) {
		this.permissionRegex = permissionRegex;
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
		
		return true;
	}

}
