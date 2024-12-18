package gui;

/**
 * The FileUploadData class holds data related to file uploads,
 * including sampler arguments, seed value, viability threshold value, and file
 * paths.
 */

public class FileUploadData {
    private float[] samplerArgs;
    private int seedValue;
    private double viabilityThresholdValue;
    private String[] filepaths;

    /**
     * Constructs a new FileUploadData object with the specified parameters.
     *
     * @param samplerArgs             an array of floats representing sampler
     *                                arguments
     * @param seedValue               an integer representing the seed value
     * @param viabilityThresholdValue a double representing the viability threshold
     *                                value
     * @param filepaths               an array of strings representing file paths
     */

    public FileUploadData(float[] samplerArgs, int seedValue, double viabilityThresholdValue, String[] filepaths) {
        this.samplerArgs = samplerArgs;
        this.seedValue = seedValue;
        this.viabilityThresholdValue = viabilityThresholdValue;
        this.filepaths = filepaths;
    }

    /**
     * Returns the sampler arguments.
     *
     * @return an array of floats representing the sampler arguments
     */

    public float[] getSamplerArgs() {
        return samplerArgs;
    }

    /**
     * Returns the seed value.
     *
     * @return an integer representing the seed value
     */

    public int getSeedValue() {
        return seedValue;
    }

    /**
     * Returns the file paths.
     *
     * @return an array of strings representing the file paths
     */

    public String[] getFilepaths() {
        return filepaths;
    }

    /**
     * Returns the viability threshold value.
     *
     * @return a double representing the viability threshold value
     */

    public double getViabilityThresholdValue() {
        return viabilityThresholdValue;
    }
}
