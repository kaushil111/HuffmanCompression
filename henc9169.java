//Kaushil Ruparelia CS610 9169 prp

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kaushilruparelia
 *
 */
public class henc9169 {

	private static String[] codes = new String[256];
	private static Write writer;
	private static Read reader;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//Read the filepath from arguments.
		if (args.length != 1) {
			System.out.println("Please enter a valid filename.");
			return;
		}
		
		String input = null;
		String filename = args[0];
		reader = new Read(filename);
		//Read the file into ASCII characters
		try {
			input = reader.readString();
		} catch (Exception e) {
			try {
				reader.close();
			} catch (IOException e1) {
				System.out.println(e.getMessage());
			}
			System.out.println(e.getMessage());
		}

		//Create a writer to write files.
		writer = new Write(filename+".huf");
		
		//compute the frequencies of characters.
		int[] frequencies = countFrequency(input);
		
		//Convert the frequencies into objects
		ArrayList<CharacterCount> characters = convertToCharacterCountArray(frequencies);
		
		//build a minHeap of characters
		try {
			buildMinHeap(characters);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		//Encode the characters and create a Hashmap
		try {
			encode(characters);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		//Write the output
		//System.out.println("Writing to file....");
		
		//Put the size
		writer.write(input.length());
		
		for (char ch : input.toCharArray()) {
			String code = codes[ch];
			for (char bit : code.toCharArray()) {
				if (bit == '0') {
					writer.write(false);
				}
				else if (bit == '1') {
					writer.write(true);
				}
				else System.out.println("Illegal Argument found");
			}
		}
		
		//Close the write
		writer.close();
		
		//Flush the original File
		eraseFile(filename);
		
	}
	
	/**
	 * Reads a file and  returns a Strings Array
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String filePath) throws IOException {
		 Path path = Paths.get(filePath);
		 List<String> inputFileList =  Files.readAllLines(path, StandardCharsets.US_ASCII);
		 String inputString = "";
		 for (String string : inputFileList) {
			 inputString = inputString.concat(string) + "\n";
		}
		 		 
		 return inputString;
	}
	
	/**
	 * Counts the number of characters in the string and returns its frequency
	 * @param input
	 */
	public static int[] countFrequency(String input) {
		char[] characters = input.toCharArray();
		int[] frequency = new int[256]; 
		
		for (char character : characters) {
			frequency[character]++;
		}
		return frequency;
	}

	/**
	 * Builds a Min Heap for the given frequencies for characters
	 * @param frequencies
	 * @return 
	 * @throws IOException 
	 */
	public static void buildMinHeap(ArrayList<CharacterCount> characters) throws IOException {
		int max = (int) Math.floor((characters.size()-1)/2)-1;
		for (int index = max; index>=0; index--) {
			minDownHeap(characters, index);
		}
	}
	
	/**
	 * 
	 * Converts frequencies array into objects with a character-count coupling
	 * @param frequencies
	 * @param count
	 * @return
	 */
	public static ArrayList<CharacterCount> convertToCharacterCountArray(int[] frequencies) {
		
		ArrayList<CharacterCount> characters = new ArrayList<CharacterCount>();
		
		for (int index = 0; index< frequencies.length; index++) {
			if (frequencies[index] > 0) {
				characters.add(new CharacterCount((char)index, frequencies[index]));
			}
		}
		
		return characters;
	}
	
	/**
	 * Heapifies the input with min at the top
	 * @param input
	 * @param index
	 */
	public static void minDownHeap(ArrayList<CharacterCount> input, int index) {
		
		int left = 2*index+1;
		int right = 2*index+2;
		CharacterCount current = input.get(index);
		
		CharacterCount lowest;
		if (right < input.size()) {
			lowest = findMinOfCharacterCount(input.get(index), input.get(2*index+1), input.get(2*index+2));
		}
		else {
			CharacterCount leftCC = input.get(left);
			lowest = current.count <= leftCC.count ? current : leftCC;
		}
		
		if (lowest != current) {

			input.set(input.indexOf(lowest), current);
			input.set(index, lowest);

			if ( input.indexOf(current) < (int) Math.floor((input.size()-1)/2)) {
				minDownHeap(input,input.indexOf(current));
			}
		}
		
	}
	
	/**
	 * Returns the lowest
	 * @param first
	 * @param second
	 * @param third
	 * @return
	 */
	public static CharacterCount findMinOfCharacterCount(CharacterCount first, CharacterCount second, CharacterCount third) {
		
		return first.count <= second.count ? first.count <= third.count ? first : third : second.count <= third.count ? second : third;
		
	}
	
	/**
	 * Encodes the Binary heap into huffman codes.
	 * @param characters
	 * @throws Exception
	 */
	public static void encode(ArrayList<CharacterCount> characters) throws Exception {
		
		while (characters.size() > 1) {
			
			CharacterCount left = extractMin(characters);
			CharacterCount right = extractMin(characters);
			
			CharacterCount node = new CharacterCount(left.count+right.count, left, right);
			insertMin(characters, node);
		}
		
		CharacterCount root = extractMin(characters);
		setCodes(root);
		writeHeap(root);
//		printHeap(root);
	}
	
	/**
	 * Extracts a Min of all characters
	 * @param characters
	 * @return
	 * @throws Exception
	 */
	public static CharacterCount extractMin(ArrayList<CharacterCount> characters) throws Exception {
		
		if (characters.size() <= 0) {
			throw new Exception("Size of arraylist exceeded!");
		}
		CharacterCount min = characters.get(0);
		CharacterCount last = characters.get(characters.size()-1);
		
		characters.set(0, last);
		characters.set(characters.size()-1, min);
		
		characters.remove(min);
		if (characters.size() > 1) {	
			
			minDownHeap(characters, 0);
		}
		return min;
	}
	
	/**
	 * Inserts an object and preserves the min-heap property
	 * @param characters
	 * @param character
	 */
	public static void insertMin(ArrayList<CharacterCount> characters, CharacterCount character) {
		
		int index = characters.size();
		characters.add(character);
		
		int parentIndex = (int)Math.floor((index -1)/2);
		
		while (index > 0 && characters.get(parentIndex).count > character.count) {
			characters.set(index, characters.get(parentIndex));
			index = parentIndex;

			parentIndex = (int)Math.floor((index -1)/2);
		}
		
		if (index > 0) {
			characters.set(index, character);
		}
		else {
			characters.set(0, character);
		}
	}
	
	/**
	 * @param root
	 */
	public static void setCodes(CharacterCount root) {
		
		if (root.isNode) {
			
			if (root.left != null) {
				root.left.code = root.left.code.concat(root.code+"0");
				setCodes(root.left);
			}
			else {
				System.out.println("Left found null");
			}
			if (root.right != null) {
				root.right.code = root.right.code.concat(root.code+"1");
				setCodes(root.right);
			}
			else {
				System.out.println("Right found null");
			}
		}
		else {
			codes[root.ch] = root.code;
		}
	}
	
	/**
	 * Prints the heap specified by Character
	 * @param character
	 */
	public static void writeHeap(CharacterCount character) {
		
		if (character.isNode) {
			writer.write(true);
			if (character.right != null) {
				writeHeap(character.right);
			}
			else {
				System.out.println("Right found null");
			}
			if (character.left != null) {
				writeHeap(character.left);
			}
			else {
				System.out.println("Left found null");
			}
			
		}
		else {
			writer.write(false);
			writer.write(character.ch);
		}		
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
		else {
			System.out.println(character+ " Code: " +character.code+ " ByteCode: " + Byte.parseByte(character.code,2));
		}		
	}
	
	public static void eraseFile(String filename) {
		File file = new File(filename);
		file.delete();
	}
	
}
