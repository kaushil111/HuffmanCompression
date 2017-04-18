//Kaushil Ruparelia CS610 9169 prp

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author kaushilruparelia
 *
 */
public class Read {
	private static BufferedInputStream inputStream;
	
	private static int buffer;
	private static int lengthOfBuffer;
	
	public Read() {
		inputStream = new BufferedInputStream(System.in);
	}
	
	public Read(String filename) {
		try {
			inputStream = new BufferedInputStream(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public boolean readBit() throws Exception {

		if (lengthOfBuffer == 0) {
			readBuffer();
		} else if (lengthOfBuffer == -1) {
			throw new Exception("File read ended....");
		}
		
		lengthOfBuffer--;
		return  ((buffer >> lengthOfBuffer) & 1) == 1;
	}
	
	public void readBuffer() {
		try {
			buffer = inputStream.read();
			if(buffer == -1) {
				return;
			}
			lengthOfBuffer = 8;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			lengthOfBuffer = -1;
			buffer = -1;
		}
	}
	
	public char readChar() throws Exception {
		return (char)(readByte() & 0xFF);
	}
	
	public int readInt() throws Exception {
		//Read Byte by Byte
		int number = 0;
		
		for (int i = 0; i < 4; i++) {
			number<<=8;
			number |= (readByte() & 0xFF); 
		}		
		return number;
	}
	
	public char readByte() throws Exception {
		if (lengthOfBuffer == 0) {
			readBuffer();
			int myByte = (buffer & 0xFF);
			readBuffer();
			return (char)(myByte & 0xFF);
		} else if (lengthOfBuffer == -1) {
			throw new Exception("File read ended....");
		} else if (lengthOfBuffer == 8) {
			//Lucky to have the entire character
			//Do a bitwise AND to make sure no adulteration remain
			int myByte = buffer;
			readBuffer();
			return (char)(myByte & 0xFF);
		}
		else {
			int oldLength = lengthOfBuffer;
			int bits = buffer;
			//Shift the bits to the right
			bits <<= (8-oldLength);
			readBuffer();
			if (lengthOfBuffer == -1 || buffer == -1) {
				throw new Exception("File read ended....");
			}
			
			//Append the new bits
			lengthOfBuffer = oldLength;
			bits |= buffer >>> lengthOfBuffer; 
			//Do a bitwise AND to make sure no adulteration remain
			return (char)(bits & 0xFF);	
		}		
	}
	
	public String readString() throws Exception {
		StringBuilder sb = new StringBuilder();
		do {
			sb.append(readChar());
		}while (buffer != -1);
		
		return sb.toString();
	}
	
	public void close() throws IOException {           
		inputStream.close();
     }

}
