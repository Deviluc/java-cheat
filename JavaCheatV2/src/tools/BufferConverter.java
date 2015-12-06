package tools;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

import libs.Constants;

public abstract class BufferConverter {
	
	public static ByteBuffer shiftByteBuffer(final ByteBuffer pBuffer, int pAmount) {
			
			for (int i = 0; i < pAmount; i++) {
				pBuffer.get();
			}
			
			return pBuffer;
	}
	
	/**
	 * Converts a ByteBuffer in any of the types defined in {@link Constants}.
	 * @param pBuffer ByteBuffer to convert
	 * @param pType Type of buffer to convert to as {@link Constants}
	 * @return Buffer of the type pType
	 */
	public static Buffer fromByteBuffer(final ByteBuffer pBuffer, final int pType) throws IllegalArgumentException {
		
		Buffer result = null;
		
		if (pType == Constants.SHORT) {
			
			ShortBuffer noShift = pBuffer.asShortBuffer();
			ShortBuffer oneShift = shiftByteBuffer(pBuffer, 1).asShortBuffer();
			
			result = ShortBuffer.allocate(noShift.capacity() + oneShift.capacity());
			result = ((ShortBuffer) result).put(noShift).put(oneShift);
			
		} else if (pType == Constants.INT) {
			
			IntBuffer[] tmpBuffer = new IntBuffer[3];
			int capacity = 0;
			
			for (int i = 0; i < 3; i++) {
				tmpBuffer[i] = shiftByteBuffer(pBuffer, i).asIntBuffer();
				capacity += tmpBuffer[i].capacity();
			}
			
			result = IntBuffer.allocate(capacity);
			
			for (int i = 0; i < 3; i++) {
				result = ((IntBuffer) result).put(tmpBuffer[i]);
			}
			
		} else if (pType == Constants.LONG) {
			
			LongBuffer[] tmpBuffer = new LongBuffer[7];
			int capacity = 0;
			
			for (int i = 0; i < 3; i++) {
				tmpBuffer[i] = shiftByteBuffer(pBuffer, i).asLongBuffer();
				capacity += tmpBuffer[i].capacity();
			}
			
			result = IntBuffer.allocate(capacity);
			
			for (int i = 0; i < 3; i++) {
				result = ((LongBuffer) result).put(tmpBuffer[i]);
			}
			
			
		} else {
			throw new IllegalArgumentException("The type is not an allowed constant!");
		}
		
		return result;
		
	}
}
