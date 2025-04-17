
import java.io.*;
import java.util.*;

/**
 * Implements a string search using a skip table based on the
 * Knuth-Morris-Pratt (KMP) string matching algorithm.
 * 
 * @author Caleb Wallis - 1637640
 * 
 * Used ChatGpt to help write javadoc and inline comments
 */
public class KMPsearch{

    SkipTable skipTable;

    /**
     * Constructs a KMPsearch instance with the given skip table.
     *
     * @param skipTable the SkipTable to be used for searching
     */
	public KMPsearch(SkipTable skipTable){
        this.skipTable = skipTable;
	}


     /**
     * Searches for the pattern in the given file line by line.
     * If a line contains the pattern, it prints the index at which it found the pattern and the line.
     *
     * @param file the file to search
     */
	public void searchFile(File file){

        // get file input
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String target = skipTable.getPattern();

            while ((line = reader.readLine()) != null) {
                // Convert the current line to a character array
                char[] lineChar = line.toCharArray();
                int lineIndex = 0;
                int matrixIndex = 0;

                // Loop through the line one character at a time
                while(lineIndex < lineChar.length){
                    char currentChar = lineChar[lineIndex];

                    // Get the row index from the skip table for this character
                    int patternIndex = skipTable.alphabetConversion(currentChar);

                    // Get how many characters we should skip
                    int skipNum = skipTable.getSkipNum(patternIndex, matrixIndex);

                    if(skipNum != 0){
                         // Mismatch found: skip ahead by skipNum
                        matrixIndex = 0;
                        lineIndex+= skipNum;
                    }
                    else{
                        // Match so far: move forward one character
                        matrixIndex++;
                        lineIndex+= 1;
                    }

                    // If the whole pattern has been matched
                    if(target.length() == matrixIndex){
                        System.out.println(lineIndex + 1 - target.length() + ": " + line);
                        break;
                    }
                }
            }
        }
        catch (Exception e){
            System.out.println("Error reading file" + e);
        }
	}

	/**
     * Main method to run the search.
     * 
     * @param args the command-line arguments: <string> [filename]
     */
    public static void main(String[] args) {
    	 if (args.length < 1) {
            System.err.println("Usage: java KMPsearch <string> [filename]");
            return;
        }

        String pattern = args[0];

        SkipTable skipTable = new SkipTable(pattern);

        if (args.length >= 2) {
            // If a filename is provided, search through it
            String filename = args[1];
            KMPsearch kmp = new KMPsearch(skipTable);
            kmp.searchFile(new File(filename));
        } else {
            // Otherwise, just print the skip table for the pattern
            skipTable.print();
        }

    }
}