package plantgen;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

/**
 * ElevationMap inherits from DataMap.
 * It is a class to store the elevation data
 */
public class ElevationMap extends DataMap {
    private float gridSpacing;
    private float latitude;

    private double[][] data;

    protected double minElv = Double.MAX_VALUE;
    protected double maxElv = Double.MIN_VALUE;

    /**
     * Method to return the grid spacing
     * 
     * @return Grid spacing of the map
     */
    public float getGridSpacing() {
        return this.gridSpacing;
    }

    /**
     * Method to set the grid spacing
     * 
     * @param gs Grid spacing
     */
    public void setGridSpacing(float gs) {
        this.gridSpacing = gs;
    }

    /**
     * Method to return the latitude
     * 
     * @return the latitude
     */
    public float getLatitude() {
        return this.latitude;
    }

    /**
     * Method to set the latitude
     * 
     * @param l latitude
     */
    public void setLatitude(float l) {
        this.latitude = l;
    }

    /**
     * Method to return the elevation data
     * 
     * @return elevation data
     */
    public double[][] getData() {
        return this.data;
    }

    /**
     * Method to set the elevation data
     * 
     * @param d the elevation to set the data
     */
    public void setData(double[][] d) {
        this.data = d;
    }

    /**
     * Overwritten method from parent class to read the elevation data.
     * The X and Y dimensions, the grid spacing, and the latitude are also read.
     * The elevation data is stored in a 2D array [X][Y].
     * 
     * @param filepath Path to the file containing elevation data.
     */
    @Override
    public void readData(String filepath) {
        double[][] elvData = new double[0][0];

        try {
            // Read raw data
            String content = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] lines = content.split("\n");

            // Get header values
            String[] headerValues = lines[0].split(" ");

            // Parse header values
            this.dimX = Integer.parseInt(headerValues[0]);
            this.dimY = Integer.parseInt(headerValues[1]);
            this.gridSpacing = Float.parseFloat(headerValues[2]);
            this.latitude = Float.parseFloat(headerValues[3]);

            // Initialize the elevation data array with parsed dimensions
            elvData = new double[dimX][dimY];
            int fileIndex = 0;
            String[] lineData = lines[1].trim().split(" ");

            // Populate the elevation data array
            for (int x = 0; x < dimX; x++) {
                for (int y = 0; y < dimY; y++) {
                    elvData[x][y] = Double.parseDouble(lineData[fileIndex]);
                    minElv = Math.min(elvData[x][y], minElv);
                    maxElv = Math.max(elvData[x][y], maxElv);
                    fileIndex++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.data = elvData;
    }
}
