package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import debugger.PTraceDebugger;
import exceptions.ProcessNotFoundException;
import exceptions.model.DebuggerException;

public class MemoryAccess {
	
	private long pid;
	private RandomAccessFile memory;
	private PTraceDebugger debugger;
	
	/**
	 * Creates a new MemoryAccess Object using pPID (the process ID) as long.
	 * @param pPID Process ID as long
	 * @throws ProcessNotFoundException Thrown if the process with the PID pPID is not running
	 * @throws DebuggerException 
	 */
	public MemoryAccess(long pPID) throws ProcessNotFoundException, DebuggerException {
		pid = pPID;
		
		try {
			memory = new RandomAccessFile(new File("/proc/" + pid + "/mem"), "rw");
		} catch (FileNotFoundException e) {
			throw new ProcessNotFoundException("The process with id '" + pid + "' could not be found!");
		}
		
		debugger = new PTraceDebugger();
		debugger.attachTo(pid);
	}
	
	public void closeWriteChannel() throws DebuggerException {
		debugger.detach();
	}
	
	/**
	 * Reads a range of bytes pBytes at the address pAddress.
	 * @param pAddress Address to read at as long
	 * @param pbytes Size of the buffer in bytes
	 * @return Range of bytes as {@link ByteBuffer}
	 * @throws IllegalArgumentException Thrown if the address or amount of bytes is invalid
	 * @throws IOException Thrown when the address cannot be read
	 */
	public ByteBuffer read(final long pAddress, final int pBytes) throws IllegalArgumentException, IOException {
		
		if (pAddress <= 0) {
			throw new IllegalArgumentException("The adress must be greater 0!");
		}
		
		if (pBytes <= 0) {
			throw new IllegalArgumentException("The amount of bytes must be greater 0!");
		}
		
		ByteBuffer buffer = ByteBuffer.allocateDirect(pBytes);
		
		buffer.order(ByteOrder.nativeOrder());
		
		memory.seek(pAddress);
		memory.getChannel().read(buffer);
		
		buffer.position(0);
		
		return buffer;
	}
	
	/**
	 * Writes the {@link ByteBuffer} pBytes to the address pAddress
	 * @param pAddress Address to write at
	 * @param pBytes {@link ByteBuffer} to write
	 * @throws IllegalArgumentException Thrown if the address or buffer is invalid
	 * @throws DebuggerException Thrown if the buffer cannot be written to memory
	 */
	public void write(final long pAddress, final ByteBuffer pBytes) throws IllegalArgumentException, DebuggerException {
		
		if (pAddress <= 0) {
			throw new IllegalArgumentException("The adress must be greater 0!");
		}
		
		if (pBytes.limit() <= 0) {
			throw new IllegalArgumentException("The ByteBuffer is empty!");
		}
		
		pBytes.position(0);
		
		while (pBytes.hasRemaining()) {
			debugger.write(pid, pAddress + pBytes.position(), pBytes.get());
		}
		
	}

}
