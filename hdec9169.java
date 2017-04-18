//Kaushil Ruparelia CS610 9169 prp

import java.io.File;

/**
 * @author kaushilruparelia
 *
 */
public class hdec9169 {
	private static Read reader;
	private static Write writer;

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		//Read the file path from arguments.
		if (args.length != 1) {
			System.out.println("Please enter a valid filename.");
			return;
		}		
		
		String filename = args[0];
		reader = new Read(filename);
		writer = new Write(filename.substring(0, filename.length()-4));
		
		//Set up huffman codes
		CharacterCount root = decode();
		printHeap(root);
		
		//Read the length
		try {
			int lengthOfFile = reader.readInt();
			
			for (int i = 0; i < lengthOfFile; i++) {
				CharacterCount node = root;
				while(node.isNode) {
					boolean bit = reader.readBit();
					if (bit) {
						node = node.right;
					}
					else {
						node = node.left;
					}
				}				
				writer.write(node.ch);
			}
			reader.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		writer.close();
		
		// Erase the file
		eraseFile(filename);
	}
	
	public static CharacterCount decode() {
		boolean isNode;
		try {
			isNode = reader.readBit();
			
			if (isNode) {
				CharacterCount left = decode();
				CharacterCount right = decode();
				
				return new CharacterCount(-1, right, left);
			}
			else {
				return new CharacterCount(reader.readChar(), -1);
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	public static void printHeap(CharacterCount character) {
		
		if (character.isNode) {
			if (character.right != null) {
				printHeap(character.right);
			}
			else {
				System.out.println("Right found null");
			}
			if (character.left != null) {
				printHeap(character.left);
			}
			else {
				System.out.println("Left found null");
			}
			
		}		
	}

	public static void eraseFile(String filename) {
		File file = new File(filename);
		file.delete();
	}
}
