package libs;

public class MemoryRange {
	
	private long start;
	private long end;
	private long offset;
	private long inode;
	private boolean read;
	private boolean write;
	private boolean shared;
	private boolean execute;
	private boolean isStack;
	private boolean isHeap;
	private String permissions;
	private String device;
	
	private String file;
	
	/**
	 * Creates a new MemoryRange object from pStart to pEnd with the permissions pPermissions
	 * @param pStart First address in memory range as long
	 * @param pEnd Last address in memory range as long
	 * @param pPermissions Access permission as {@link String} (format: [r/-][w/-][x/-][s/p])
	 */
	public MemoryRange(final long pStart, final long pEnd, final String pPermissions, final long pOffset, final String pDevice, final long pInode) {
		
		if (pPermissions.length() != 4) {
			throw new IllegalArgumentException("Wrong permission String!");
		}
		
		
		start = pStart;
		end = pEnd;
		
		permissions = pPermissions;
		
		if (pPermissions.charAt(0) == 'r') {
			read = true;
		} else {
			read = false;
		}
		
		if (pPermissions.charAt(1) == 'w') {
			write = true;
		} else {
			write = false;
		}
		
		if (pPermissions.charAt(2) == 'x') {
			execute = true;
		} else {
			execute = false;
		}
		
		if (pPermissions.charAt(3) == 's') {
			shared = true;
		} else {
			shared = false;
		}
		
		offset = pOffset;
		device = pDevice;
		inode = pInode;
		
	}
	
	/**
	 * Creates a new MemoryRange object from pStart to pEnd with the permissions pPermissions and the file pFile
	 * @param pStart First address in memory range as long
	 * @param pEnd Last address in memory range as long
	 * @param pFile File-path to mounted memory
	 * @param pPermissions Access permission as {@link String} (format: [r/-][w/-][x/-][s/p])
	 */
	public MemoryRange(final long pStart, final long pEnd, final String pPermissions, final long pOffset, final String pDevice, final long pInode, final String pFile) {
		if (pPermissions.length() != 4) {
			throw new IllegalArgumentException("Wrong permission String!");
		}
		
		start = pStart;
		end = pEnd;
		
		permissions = pPermissions;
		
		if (pPermissions.charAt(0) == 'r') {
			read = true;
		} else {
			read = false;
		}
		
		if (pPermissions.charAt(1) == 'w') {
			write = true;
		} else {
			write = false;
		}
		
		if (pPermissions.charAt(2) == 'x') {
			execute = true;
		} else {
			execute = false;
		}
		
		if (pPermissions.charAt(3) == 's') {
			shared = true;
		} else {
			shared = false;
		}
		
		file = pFile;
		offset = pOffset;
		device = pDevice;
		inode = pInode;
		
		isHeap = false;
		isStack = false;
		
		if (file.trim().equals("[heap]")) {
			isHeap = true;
		} else if (file.trim().equals("[stack]")) {
			isStack = true;
		}
	}
	
	/**
	 * Returns the first address in this range
	 * @return First address as long
	 */
	public long getStart() {
		return start;
	}
	
	/**
	 * Returns the last address in this range
	 * @return Last address as long
	 */
	public long getEnd() {
		return end;
	}
	
	/**
	 * Returns the offset in the file where the mapping begins	</br>
	 * If the region is not mapped from a file it returns 0
	 * @return file-offset in bytes
	 */
	public long getOffset() {
		return offset;
	}
	
	/**
	 * Returns the inode of the file from which the region is mapped	</br>
	 * If the region is not mapped from a file it returns 0
	 * @return inode
	 */
	public long getInode() {
		return inode;
	}
	
	/**
	 * Returns the size of this range in bytes.
	 * @return Size in bytes
	 */
	public long getByteSize() {
		return end - start;
	}
	
	/**
	 * Returns true if you can read in this range
	 * @return boolean
	 */
	public boolean canRead() {
		return read;
	}
	
	/**
	 * Returns true if you can write in this range
	 * @return boolean
	 */
	public boolean canWrite() {
		return write;
	}
	
	/**
	 * Returns true if you can execute in this range
	 * @return boolean
	 */
	public boolean canExecute() {
		return execute;
	}
	
	/**
	 * Returns true if this range is private
	 * @return boolean
	 */
	public boolean isPrivate() {
		return !shared;
	}
	
	/**
	 * Returns true if this {@link MemoryRange} contains the stack.
	 * @return boolean
	 */
	public boolean isStack() {
		return isStack;
	}
	
	/**
	 * Returns true if this {@link MemoryRange} contains the heap.
	 * @return boolean
	 */
	public boolean isHeap() {
		return isHeap;
	}
	
	/**
	 * Returns true if this range is mapped from file
	 * @return boolean
	 */
	public boolean fileMapped() {
		return inode != 0;
	}
	
	public boolean matchPermissionRegex(final String pRegex) {
		return permissions.matches(pRegex);
	}
	
	/**
	 * Returns device-number if mapped from file in hex
	 * Format: [major:minor]
	 * @return device-number in hex
	 */
	public String getDevice() {
		return device;
	}
	
	/**
	 * Return the path to the mounted memory file
	 * @return Path to file as String, empty if none is defined
	 */
	public String getFile() {
		
		if (isHeap || isStack) {
			return "";
		}
		
		if (file != null) {
			return file;
		} else {
			return "";
		}
	}
	
	

}
