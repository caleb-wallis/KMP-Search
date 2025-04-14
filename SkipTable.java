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


    private void createSkipTable(){
        // make array with pattern (split into say A B C as column and row as ABABABC)
        int rows = pattern.length();
        int cols = alphabet.length;
        skipTable = new int[rows][cols];

        // Fill the matrix
        for (int i = 0; i < rows; i++) {
            char tChar = pattern.charAt(i); // target character at position i
            for (int j = 0; j < cols; j++) {
                char pChar = alphabet[j];   // pattern character at position j
                if (tChar == pChar) {
                    skipTable[i][j] = 1;  // mark match
                } else {
                    skipTable[i][j] = 0;  // mark no match
                }
            }
        }


        skipTable = new int[][]{
            {0, 1, 0, 3, 2}, // *
            {1, 0, 3, 0, 5}, // a
            {1, 2, 3, 4, 0}, // b
            {1, 2, 3, 4, 5}  // c
        };
    }

    private void createAlphabet(){
        // Create Skip table from string
        Set<Character> uniqueChar = new LinkedHashSet<>();

        for (char c : pattern.toCharArray()) {
            uniqueChar.add(c);
        }

        alphabet = uniqueChar.toArray(new Character[0]);
    }


    public int alphabetConversion(char c){
        for(int i=0; i<alphabet.length; i++){
            if(alphabet[i] == c){
                return i;
            }
        }
        return alphabet.length;
    }


    public void print(){
        for (int[] row : skipTable) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }


    public int getSkipNum(int patternIndex, int tableIndex){
        return skipTable[patternIndex][tableIndex];
    }

    public String getPattern(){
        return pattern;
    }
}