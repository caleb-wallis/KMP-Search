
import java.io.*;
import java.util.*;

/**
 * Represents a skip table used in the KMP search algorithm.
 * The table is built based on a pattern and contains skip values
 * for each character in the pattern and its alphabet.
 * 
 * @author Caleb Wallis - 1637640
 * 
 * Used ChatGpt to help write javadoc and inline comments
 */
public class SkipTable{

    private String pattern;
    private Character[] alphabet;
    private int[][] skipTable;

    /**
     * Constructs the SkipTable for the given pattern.
     *
     * @param _pattern the string pattern to build the skip table from
     */
    public SkipTable(String pattern){

        this.pattern = pattern;
        createAlphabet(); // Build alphabet from pattern
        createSkipTable(); // Build skip table from pattern and alphabet
    }

    /**
     * Builds the skip table matrix based on the pattern and alphabet.
     */
    private void createSkipTable() {
        int patLen = pattern.length();
        int rowCount = alphabet.length + 1; // +1 for wildcard row

        skipTable = new int[rowCount][patLen];
        
        // For each position in the pattern
        for (int j = 0; j < patLen; j++) {
            char patternChar = pattern.charAt(j);
            
            // Process each character in our alphabet
            for (int i = 0; i < alphabet.length; i++) {
                char currentChar = alphabet[i];
                
                if (currentChar == patternChar) {
                    // If characters match at this position, no skip needed
                    skipTable[i][j] = 0;
                } else {
                    // Calculate proper skip value for mismatch
                    int skip = calculateSkip(j, currentChar);
                    skipTable[i][j] = skip;
                }
            }
            
            // Handle the wildcard row (last row) - simply j+1 for each position 
            // Wildcard technically not part of the alphabet
            int wildcardRow = alphabet.length;
            skipTable[wildcardRow][j] = j + 1;
        }
    }

    /**
     * Calculates the number of characters to skip when a mismatch occurs.
     *
     * @param mismatchPos the index in the pattern where mismatch happened
     * @param mismatchChar the character that caused the mismatch
     * @return the number of characters to skip
     */
    private int calculateSkip(int mismatchPos, char mismatchChar) {
        if (mismatchPos == 0) return 1;

        // Try all prefix lengths from longest to shortest
        for (int len = mismatchPos; len > 0; len--) {
            boolean match = true;

            // Compare prefix with suffix
            for (int i = 0; i < len - 1; i++) {
                int suffixIndex = mismatchPos - len + i + 1;
                if (pattern.charAt(i) != pattern.charAt(suffixIndex)) {
                    match = false;
                    break;
                }
            }

            // Check if mismatchChar matches the expected continuation
            if (match && pattern.charAt(len - 1) == mismatchChar) {
                return mismatchPos - len + 1;
            }
        }

        // No match: skip all the way to next character
        return mismatchPos + 1; 
    }
    
    /**
     * Creates an alphabet of unique characters used in the pattern.
     */
    private void createAlphabet(){
        // Extract unique characters from the pattern
        Set<Character> uniqueChars = new TreeSet<>(); // TreeSet keeps it alphabetically sorted
        for (char c : pattern.toCharArray()) {
            uniqueChars.add(c);
        }

        // Store alphabet as sorted array
        alphabet = uniqueChars.toArray(new Character[0]);
    }


    /**
     * Converts a character to its index in the alphabet.
     * Returns the wildcard index if character is not in the alphabet.
     *
     * @param c the character to convert
     * @return the index of the character in the alphabet, or wildcard index
     */
    public int alphabetConversion(char c){
        for(int i=0; i<alphabet.length; i++){
            if(alphabet[i] == c){
                return i;
            }
        }
        return alphabet.length;
    }


    /**
     * Prints the skip table to stdout.
     */
    public void print() {
        // Print the header row: *,<pattern characters>
        System.out.print("*");
        for (int j = 0; j < pattern.length(); j++) {
            System.out.print("," + pattern.charAt(j));
        }
        System.out.println();

        // Print rows for each character in the alphabet
        for (int i = 0; i < alphabet.length; i++) {
            System.out.print(alphabet[i]); // Row label
            for (int j = 0; j < pattern.length(); j++) {
                System.out.print("," + skipTable[i][j]);
            }
            System.out.println();
        }

        // Print the wildcard row
        System.out.print("*");
        for (int j = 0; j < pattern.length(); j++) {
            System.out.print("," + skipTable[alphabet.length][j]);
        }
        System.out.println();
    }

    /**
     * Retrieves the skip number from the skip table.
     *
     * @param patternIndex the row index in the skip table
     * @param tableIndex the column index in the skip table
     * @return the skip number
     */
    public int getSkipNum(int patternIndex, int tableIndex){
        return skipTable[patternIndex][tableIndex];
    }

    /**
     * Gets the pattern string used to build the skip table.
     *
     * @return the pattern string
     */
    public String getPattern(){
        return pattern;
    }
}