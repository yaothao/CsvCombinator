import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CsvCombinator {
    public static void main(String[] args) throws IOException {

        /*
        pre-process each file and check if the file does exist
        and if the given file is a file
        throws file not found exception if the condition is not met
        */
        for (int i = 0; i < args.length; i++) {
            String fileName = args[i];
            File currentFile = new File(fileName);

            if (!currentFile.exists() || !currentFile.isFile()) {
                throw new FileNotFoundException(String.format("[%s]: does not exist or not a file", currentFile.getAbsolutePath()));
            }
        }

        // ask the CsvToStdout to process the csv file and output to Stdout
        CsvCombinator.CsvToStdout(args);
    }

    /*
    Input: an array of csv files
    Output: combined csv files to Standard Out
    The function process each line of the given csv files and append a column of
    the file name at the end.
     */
    private static void CsvToStdout(String[] files) throws IOException {
        // number of files given
        int numInput = files.length;

        // header stores the header of the combined csv file
        StringBuilder header = new StringBuilder();

        try (BufferedWriter csvWriter = new BufferedWriter(new OutputStreamWriter(System.out))) {

            StringBuilder currentLine = new StringBuilder();

            for (int fileNum = 0; fileNum < numInput; fileNum++) {
                String filePath = files[fileNum];
                File csvFile = new File(filePath);

                try (BufferedReader csvReader = new BufferedReader(new FileReader(csvFile))) {

                    // represent the current line number in each file
                    int fileLineIndex = 0;

                    // while the next line doesn't return null, process the line
                    while ((currentLine.append(csvReader.readLine())).length() > 0 &&
                            !currentLine.toString().equals("null")) {

                        // check if the input contains regular expression with escaping character
                        // currentLine = CsvCombinator.correctRegex(currentLine);

                        // if the header length is zero, update the header
                        // else if the line is not a header, process the current line
                        if (header.length() == 0) {
                            header.append(currentLine.toString());
                            header.append(",");
                            header.append("filename");
                            header.append("\n");
                            csvWriter.write(header.toString());
                        } else if (fileLineIndex > 0) {
                            currentLine.append(",");
                            currentLine.append(csvFile.getName());
                            currentLine.append("\n");
                            csvWriter.write(currentLine.toString());
                        }

                        fileLineIndex++;

                        csvWriter.flush();

                        currentLine.setLength(0);
                    }

                }

            }
        }
    }
    /*
    Input: a string builder that contains regular expression with escaping character '\'
    Output: return the corrected string
    The function takes the given string and removes all the backslash to escape character
     */
    @Deprecated
    private static StringBuilder correctRegex(StringBuilder line) {
        String corrected = line.toString().replaceAll("\\\\","");
        line.setLength(0);
        line.append(corrected);
        return line;
    }
}