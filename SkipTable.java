import java.io.*;
import java.util.*;

public class SkipTable{

    private String pattern;
    private Character[] alphabet;
    private int[][] skipTable;

    public SkipTable(String _pattern){

        pattern = _pattern;
        createAlphabet();
        createSkipTable();
    }


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

    private int calculateSkip(int mismatchPos, char mismatchChar) {
        if (mismatchPos == 0) return 1;

        for (int len = mismatchPos; len > 0; len--) {
            // Check prefix[0..len-2] vs suffix of length len-1 ending before mismatchPos
            boolean match = true;
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

        return mismatchPos + 1; // Default: skip all and start fresh
    }
    

    private void createAlphabet(){
        // Create Skip table from string
        Set<Character> uniqueChars = new TreeSet<>(); // TreeSet keeps it alphabetically sorted

        for (char c : pattern.toCharArray()) {
            uniqueChars.add(c);
        }

        alphabet = uniqueChars.toArray(new Character[0]);
    }


    public int alphabetConversion(char c){
        for(int i=0; i<alphabet.length; i++){
            if(alphabet[i] == c){
                return i;
            }
        }
        return alphabet.length;
    }


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

    public int getSkipNum(int patternIndex, int tableIndex){
        return skipTable[patternIndex][tableIndex];
    }

    public String getPattern(){
        return pattern;
    }
}