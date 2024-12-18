package plantgen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * AbioticMap inherits from DataMap.
 * It is used to read and format the abiotic data
 */
public class AbioticMap extends DataMap {
    private int numMonths = 12;
    private double[][][] data;
    private double[][][] originalData;

    /**
     * Method to return the abiotic data
     * 
     * @return abiotic data
     */
    public double[][][] getData() {
        return this.data;
    }

    /**
     * Method to set the abiotic data.
     * Used in the case of needing to use the original data again.
     * 
     * @param d abiotic data
     */
    public void setData(double[][][] d) {
        this.data = d;
    }

    /**
     * Method to return the original data
     * 
     * @return the original data
     */
    public double[][][] getOriginalData() {
        return this.originalData;
    }

    /**
     * Method to deep copy the data in the case of needing the original data.
     * This method creates a deep copy of the current data and stores it in the
     * originalData field.
     */
    public void copyData() {
        double[][][] copy = new double[this.data.length][][];
        for (int i = 0; i < this.data.length; i++) {
            copy[i] = new double[this.data[i].length][];
            for (int j = 0; j < this.data[i].length; j++) {
                copy[i][j] = Arrays.copyOf(this.data[i][j], this.data[i][j].length);
            }
        }

        this.originalData = copy;
    }

    /**
     * Method to read in the abiotic data from a file.
     * The file should contain data in a specific format where the second line
     * contains space-separated values.
     * 
     * @param filepath the path to the file containing the abiotic data.
     */
    @Override
    public void readData(String filepath) {
        double[][][] abioticData = new double[numMonths][dimX][dimY];

        try {
            String content = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] dataLines = content.split("\n");
            String[] dataList = dataLines[1].split(" ");
            int fileIndex = 0;

            for (int month = 0; month < numMonths; month++) {
                for (int x = 0; x < dimX; x++) {
                    for (int y = 0; y < dimY; y++) {
                        abioticData[month][x][y] = Double.parseDouble(dataList[fileIndex]);
                        fileIndex++;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.data = abioticData;
    }

    /**
     * Method to set the number of months
     * 
     * @param numMonths the number of months
     */

    public void setNumMonths(int numMonths) {
        this.numMonths = numMonths;
    }
}
