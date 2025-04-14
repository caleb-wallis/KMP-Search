
import java.io.*;
import java.util.*;


public class KMPsearch{

    SkipTable skipTable;

	public KMPsearch(SkipTable _skipTable){
        skipTable = _skipTable;
	}

	public void searchFile(File file){

        // get file input
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String target = skipTable.getPattern();
            while ((line = reader.readLine()) != null) {
                // we have the line. Read it character by character
                char[] lineChar = line.toCharArray();
                int lineIndex = 0;
                int matrixIndex = 0;

                // loop until end of line
                while(lineIndex < lineChar.length){
                    // give which part of the skip table your up to (which column) and the character your checking (which row)
                    char currentChar = lineChar[lineIndex];
                    int patternIndex = skipTable.alphabetConversion(currentChar);

                    int skipNum = skipTable.getSkipNum(patternIndex, matrixIndex);

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
     * Main method to run the sort.
     */
    public static void main(String[] args) {
    	 if (args.length < 1) {
            System.err.println("Usage: java KMPsearch <string> [filename]");
            return;
        }

        String pattern = args[0];

        SkipTable skipTable = new SkipTable(pattern);

        if (args.length >= 2) {
            // If filename is provided, search file
            String filename = args[1];
            // Create kmp search instance
            KMPsearch kmp = new KMPsearch(skipTable);
            // Search using file
            kmp.searchFile(new File(filename));
        } else {
            // No filename provided, print the skip table
            skipTable.print();
        }

    }
}