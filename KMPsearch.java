
import java.io.*;
import java.util.*;


public class KMPsearch{

    int[][] skipTable;

	public KMPsearch(String target){
        createSkipTable(target);
	}

	public int[][] createSkipTable(String target){
		// Create Skip table from string

        Set<Character> patternChar = new LinkedHashSet<>();

        for (char c : target.toCharArray()) {
            patternChar.add(c);
        }

        Character[] pattern = patternChar.toArray(new Character[0]);


		// make array with pattern (split into say A B C as column and row as ABABABC)
		int rows = target.length();
		int cols = pattern.length;
		int[][] matrix = new int[rows][cols];



		// Fill the matrix
        for (int i = 0; i < rows; i++) {
            char tChar = target.charAt(i); // target character at position i
            for (int j = 0; j < cols; j++) {
                char pChar = pattern[j];   // pattern character at position j
                if (tChar == pChar) {
                    matrix[i][j] = 1;  // mark match
                } else {
                    matrix[i][j] = 0;  // mark no match
                }
            }
        }

        return matrix;
	}

    public void printSkipTable(){
        skipTable = new int[][]{
            {0, 1, 0, 3, 2}, // *
            {1, 0, 3, 0, 5}, // a
            {1, 2, 3, 4, 0}, // b
            {1, 2, 3, 4, 5}  // c
        };
        for (int[] row : skipTable) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
        
    }



	public void searchFile(File file){
        skipTable = new int[][]{
            {0, 1, 0, 3, 2}, // a
            {1, 0, 3, 0, 5}, // b
            {1, 2, 3, 4, 0}, // c
            {1, 2, 3, 4, 5}  // *
        };

        // get file input
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String target = "ababc";
            while ((line = reader.readLine()) != null) {
                // we have the line. Read it character by character
                char[] lineChar = line.toCharArray();
                int lineIndex = 0;
                int matrixIndex = 0;


                //System.out.println(line + "\n");


                // loop until end of line
                while(lineIndex < lineChar.length){
                    

                    // give which part of the skip table your up to (which column) and the character your checking (which row)
                    char currentChar = lineChar[lineIndex];
                    int patternIndex = patternConversion(currentChar);

                    //System.out.println(currentChar);

                    int skipNum = skipTable[patternIndex][matrixIndex];

                    // skip ahead amount returned by array
                    if(skipNum != 0){
                        matrixIndex = 0;
                        lineIndex+= skipNum;
                    }
                    else{
                        matrixIndex++;
                        lineIndex+= 1;
                    }

                    if(target.length() == matrixIndex){
                        System.out.println(lineIndex - target.length() + ": " + line + "\n");
                        break;
                    }
                }
            }
        }

        catch (Exception e){
            System.out.println("Error reading file" + e);
        }
	}


    public int patternConversion(char c){
        char[] pattern = new char[] {'a', 'b', 'c'};
        for(int i=0; i<pattern.length; i++){
            if(pattern[i] == c){
                return i;
            }
        }
        return pattern.length;
    }


	public void outputLine(){

	}


	/**    
     * Main method to run the sort.
     */
    public static void main(String[] args) {
    	 if (args.length < 1) {
            System.err.println("Usage: java KMPsearch <string> [filename]");
            return;
        }

        String target = args[0];
        KMPsearch kmp = new KMPsearch(target);

        if (args.length >= 2) {
            // If filename is provided, search file
            String filename = args[1];
            kmp.searchFile(new File(filename));
        } else {
            // No filename provided, print the skip table
            kmp.printSkipTable();
        }

    }


}