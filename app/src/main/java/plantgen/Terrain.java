package plantgen;

/**
 * Terrain Class to store the dimensions and elevation, sunlight data,
 * temperature data and moisture data
 */
public class Terrain {
    private int dimX;
    private int dimY;
    private float gridSpacing;
    private int numMonths = 12;
    private double minElv;
    private double maxElv;

    private double[][] elvData;
    private double[][][] sunData;
    private double[][][] tempData;
    private double[][][] wetData;

    private InputHandler handler = new InputHandler();

    /**
     * Method to load and read the data into the terrain.
     * 
     * @param filepaths Array of file paths to read data from.
     */
    public void loadData(String[] filepaths) {
        this.elvData = this.handler.readElevation(filepaths[0]);
        this.sunData = this.handler.readAbiotic(filepaths[1]);
        this.tempData = this.handler.readAbiotic(filepaths[2]);
        this.wetData = this.handler.readAbiotic(filepaths[3]);

        this.dimX = this.handler.eMap.getDimX();
        this.dimY = this.handler.eMap.getDimY();
        this.gridSpacing = this.handler.eMap.getGridSpacing();

        this.maxElv = this.handler.eMap.maxElv;
        this.minElv = this.handler.eMap.minElv;
    }

    /**
     * Method to return the X dimensions.
     * 
     * @return X dimensions.
     */
    public int getDimX() {
        return this.dimX;
    }

    /**
     * Method to set the X dimensions.
     * 
     * @param dimX X dimensions to set.
     */
    public void setDimX(int dimX) {
        this.dimX = dimX;
    }

    /**
     * Method to return the Y dimensions.
     * 
     * @return Y dimensions.
     */
    public int getDimY() {
        return this.dimY;
    }

    /**
     * Method to set the Y dimensions.
     * 
     * @param dimY Y dimensions to set.
     */
    public void setDimY(int dimY) {
        this.dimY = dimY;
    }

    /**
     * Method to return the grid spacing.
     * 
     * @return The grid spacing between each coordinate.
     */
    public float getGridSpacing() {
        return this.gridSpacing;
    }

    /**
     * Method to set the grid spacing.
     * 
     * @param gridSpacing The grid spacing to set.
     */
    public void setGridSpacing(float gridSpacing) {
        this.gridSpacing = gridSpacing;
    }

    /**
     * Method to return the elevation data
     * 
     * @return elevation data per coordinate
     */
    public double[][] getElevationData() {
        return elvData;
    }

    /**
     * Method to return the elevation data.
     * 
     * @return Elevation data per coordinate.
     */
    public void setElevationData(double[][] elvData) {
        this.elvData = elvData;
    }

    /**
     * Metohd to return the sunlight data
     * 
     * @return sunlight data per month per coordinate
     */
    public double[][][] getSunlightData() {
        return sunData;
    }

    /**
     * Method to return the sunlight data.
     * 
     * @return Sunlight data per month per coordinate.
     */
    public void setSunlightData(double[][][] sunData) {
        this.sunData = sunData;
    }

    /**
     * Method to return the temperature data
     * 
     * @return temperature data per month per coordinate
     */
    public double[][][] getTemperatureData() {
        return tempData;
    }

    /**
     * Method to set the temperature data.
     * 
     * @param tempData Temperature data to set.
     */
    public void setTemperatureData(double[][][] tempData) {
        this.tempData = tempData;
    }

    /**
     * Method to return the moisture data
     * 
     * @return moisture data per month per coordinate
     */
    public double[][][] getWetData() {
        return wetData;
    }

    /**
     * Method to set the moisture data.
     * 
     * @param wetData Moisture data to set.
     */
    public void setWetData(double[][][] wetData) {
        this.wetData = wetData;
    }

    /**
     * Method to return the number of months.
     * 
     * @return Number of months.
     */
    public int getNumMonths() {
        return numMonths;
    }

    /**
     * Method to set the number of months.
     * 
     * @param m Number of months to set.
     */
    public void setNumMonths(int m) {
        this.numMonths = m;
    }

    public double getMinElv() {
        return minElv;
    }

    public double getMaxElv() {
        return maxElv;
    }
}