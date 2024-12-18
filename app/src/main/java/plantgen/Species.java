package plantgen;

/**
 * Species class to hold species specific data
 */
public class Species {
    private static CSVReader reader;
    private static String[][] speciesData;

    private String name;
    private int lifespan;
    private int maxHeightOpen;
    private int maxHeightClosed;
    private int q;

    private float radiusMultiplierOpen;
    private float radiusMultiplierClosed;

    private double leafTransparency;
    private double moistureAbsorption;

    private float[] sunValues = new float[2];
    private float[] wetValues = new float[2];
    private float[] tempValues = new float[2];
    private float[] slopeValues = new float[2];

    /**
     * Constructor to initialize a Species object with a given name
     * 
     * @param name the name of the species
     */
    public Species(String name) {
        this.name = name;
        this.setValues();
    }

    public Species() {
    }

    /**
     * Method to call the readCSV method to read in the species data and its
     * parameters
     * 
     * @return a 2D array containing species data
     */
    private static String[][] readSpeciesData() {
        return reader.readCSV();
    }

    /**
     * Method to set the parameters of each species from the CSV
     */
    public void setValues() {
        String[] data = new String[20];

        // Switch case to assign species data based on the species name
        switch (name) {
            case "Boxwood":
                data = speciesData[1];
                break;
            case "Snowy Mespilus":
                data = speciesData[2];
                break;
            case "Mountain Pine":
                data = speciesData[3];
                break;
            case "Silve Fir":
                data = speciesData[4];
                break;
            case "Silver Birch":
                data = speciesData[5];
                break;
            case "Sessile Oak":
                data = speciesData[6];
                break;
            case "European Beech":
                data = speciesData[7];
        }

        // Parsing and setting the species parameters from the data array
        this.lifespan = Integer.parseInt(data[4]);
        this.maxHeightOpen = Integer.parseInt(data[5]);
        this.maxHeightClosed = Integer.parseInt(data[6]);
        this.q = Integer.parseInt(data[7]);

        this.radiusMultiplierOpen = Float.parseFloat(data[8]);
        this.radiusMultiplierClosed = Float.parseFloat(data[9]);

        this.leafTransparency = Float.parseFloat(data[10]);
        this.moistureAbsorption = Integer.parseInt(data[11]);

        this.sunValues[0] = Float.parseFloat(data[12]);
        this.sunValues[1] = Float.parseFloat(data[13]);

        this.wetValues[0] = Float.parseFloat(data[14]);
        this.wetValues[1] = Float.parseFloat(data[15]);

        this.tempValues[0] = Float.parseFloat(data[16]);
        this.tempValues[1] = Float.parseFloat(data[17]);

        this.slopeValues[0] = Float.parseFloat(data[18]);
        this.slopeValues[1] = Float.parseFloat(data[19]);
    }

    /**
     * Method to return the name of the species
     * 
     * @return the name of the species
     */
    public String getName() {
        return this.name;
    }

    /**
     * Method to return the lifespan of the species
     * 
     * @return the lifespan of the species
     */
    public int getLifespan() {
        return this.lifespan;
    }

    /**
     * Method to return the maximum height (open) of the species
     * 
     * @return maximum height (open)
     */
    public int getMaxHeightOpen() {
        return this.maxHeightOpen;
    }

    /**
     * Method to return the maximum height (closed) of the species
     * 
     * @return maximum height (closed)
     */
    public int getMaxHeightClosed() {
        return this.maxHeightClosed;
    }

    /**
     * Method to return the tuning parameter of the species
     * 
     * @return the turning parameter
     */
    public int getQ() {
        return this.q;
    }

    /**
     * Method to return the radius multiplier (open)
     * 
     * @return the radius multiplier (open)
     */
    public float getRadiusMultipierOpen() {
        return this.radiusMultiplierOpen;
    }

    /**
     * Method to return the radius multiplier (closed)
     * 
     * @return the radius multiplier (closed)
     */
    public float getRadiusMultipierClosed() {
        return this.radiusMultiplierClosed;
    }

    /**
     * Method to return the leaf transparency of the species
     * 
     * @return leaf transparency
     */
    public double getLeafTransparency() {
        return this.leafTransparency;
    }

    /**
     * Method to return the moisture absorption of the species
     * 
     * @return moisture absorption
     */
    public double getMoistureAbsorption() {
        return this.moistureAbsorption;
    }

    /**
     * Method to return the c and r sun values of the species
     * 
     * @return the sun values: {c, r}
     */
    public float[] getSunValues() {
        return this.sunValues;
    }

    /**
     * Method to return the c and r temp values of the species
     * 
     * @return the temp values: {c, r}
     */
    public float[] getTempValues() {
        return this.tempValues;
    }

    /**
     * Method to return the c and r moisture values of the species
     * 
     * @return the moisture values: {c, r}
     */
    public float[] getWetValues() {
        return this.wetValues;
    }

    /**
     * Method to return the c and r slope values
     * 
     * @return the slope values: {c, r}
     */
    public float[] getSlopeValues() {
        return this.slopeValues;
    }

    /**
     * Method to read and format the csv data into the species
     * 
     * @param filepath
     */
    public static void initialiseSpeciesData(String filepath) {
        Species.reader = new CSVReader(filepath);
        Species.speciesData = Species.readSpeciesData();
    }

    // Setters for testing

    public void setQ(int q) {
        this.q = q;
    }

    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
    }

    public void setMaxHeightOpen(int maxHeightOpen) {
        this.maxHeightOpen = maxHeightOpen;
    }

    public void setMaxHeightClosed(int maxHeightClosed) {
        this.maxHeightClosed = maxHeightClosed;
    }

    public void setRadiusMultiplierOpen(float radiusMultiplierOpen) {
        this.radiusMultiplierOpen = radiusMultiplierOpen;
    }

    public void setRadiusMultiplierClosed(float radiusMultiplierClosed) {
        this.radiusMultiplierClosed = radiusMultiplierClosed;
    }
}
