package plantgen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class to read the CSV file of the species data/parameters
 */
public class CSVReader {
    private String filepath;

    /**
     * Constructor to initialize the CSVReader with the file path
     * 
     * @param filepath the path to the CSV file
     */
    public CSVReader(String filepath) {
        this.filepath = filepath;
    }

    /**
     * Method to read the CSV file.
     * 
     * @return a 2D array containing the CSV data
     */
    public String[][] readCSV() {
        // Initialize a 2D array to store the CSV data
        String[][] CSVData = new String[8][20];

        // Store the file path and initialize variables for reading the file
        String filePath = this.filepath;
        String line = "";
        String delimiter = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            // Read each line of the CSV file and split it by the delimiter
            for (int i = 0; i < 8; i++) {
                line = br.readLine();
                CSVData[i] = line.split(delimiter);
            }

        } catch (IOException e) {
            // Print the stack trace if an IOException occurs
            e.printStackTrace();
        }

        return CSVData;
    }
}
