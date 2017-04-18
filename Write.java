//Kaushil Ruparelia CS610 9169 prp

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Writes the data to a file.
 * @author kaushilruparelia
 *
 */
public class Write {
	private static BufferedOutputStream outputStream;
	
	private static int buffer;
	private static int lengthOfBuffer;
	
	public Write() {
		outputStream = new BufferedOutputStream(System.out);
	}
	
	public Write(String filename) {
		try {
			outputStream = new BufferedOutputStream(new FileOutputStream(filename));
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Write bit to file
	 * @param bit
	 */
	public void write(boolean bit) {
		
		buffer <<= 1;
		if (bit) buffer |= 1;

		lengthOfBuffer++;
        if (lengthOfBuffer == 8) writeBuffer();
		
	}
	
	/**
	 * Write char to file
	 * @param ch
	 */
	public void write(char ch) {
		writeByte(ch);
	}

	public void write(int number) {
        writeByte((number >>> 24) & 0xff);
        writeByte((number >>> 16) & 0xff);
        writeByte((number >>>  8) & 0xff);
        writeByte((number >>>  0) & 0xff);
	}
	/**
	 * Writes the buffer to the output file.
	 */
	private static void writeBuffer() {
		
		if(lengthOfBuffer == 0) 
			return;
		
		//If buffer is not full, shift the bits to right and pad with 0s
		if (lengthOfBuffer > 0) {
			buffer<<= 8 - lengthOfBuffer;
		}
		
		try {
			outputStream.write(buffer);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		lengthOfBuffer = 0;
		buffer = 0;
	}
	
	/**
	 * Writes a byte value
	 * @param myByte
	 */
	private void writeByte(int myByte) {
		//Check if buffer is empty
		if (lengthOfBuffer == 0) {
			try {
				outputStream.write(myByte);
			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		else {
			//Print bitwise
			for (int i = 0; i < 8; i++) {
	            boolean bit = ((myByte >>> (7-i)) & 1) == 1;
	            write(bit);
	        }
		}
	}
	
	/**
	 * Close the file write 
	 */
	public void close() {
		
		writeBuffer();
		try {
			outputStream.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		try {
			outputStream.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
