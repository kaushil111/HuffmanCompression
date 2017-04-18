//Kaushil Ruparelia CS610 9169 prp

/**
 * Object of Character-Count coupling
 * @author kaushilruparelia
 *
 */
public class CharacterCount {
	
	public char ch;
	public int count;
	public String code = "";
	public boolean isNode = false;
	public CharacterCount left;
	public CharacterCount right;
	
	public CharacterCount(char ch, int count) {
		
		this.ch = ch;
		this.count = count;
	}
	
	public CharacterCount(int count, CharacterCount left, CharacterCount right) {
		
		if (left != null || right != null) {
			isNode = true;
		}
		else {
			System.out.println("Found null in node");
		}
		
		this.ch = 0;
		this.count = count;
		this.left = left;
		this.right = right;
	}
	
	
	
	public CharacterCount() {
		ch = 0;
		count = 0;
	}
	
	@Override
	public String toString() {
		return "Character: "+ch+" Count: "+count;
	}
	
}
