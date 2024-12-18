package gui;

import java.util.Arrays;

/**
 * The InputsFetcher class implements the FileUploadListener interface
 * and handles the validated file data.
 */

public class InputsFetcher implements FileUploadListener {
    private FileUploadData fileUploadData;

    /**
     * This method is called when the file validation is complete.
     * It handles the validated file data by printing the sampler arguments,
     * seed value, and file paths to the console, and stores the data in the
     * fileUploadData field.
     *
     * @param data The validated file data.
     */

    @Override
    public void onValidationComplete(FileUploadData data) {
        // Handle the validated file data
        System.out.println("Files validated successfully.");
        System.out.println("Sampler Args: " + Arrays.toString(data.getSamplerArgs()));
        System.out.println("Seed Value: " + data.getSeedValue());
        System.out.println("File Paths: " + Arrays.toString(data.getFilepaths()));

        // Store the validated file data
        this.fileUploadData = data;
    }

    /**
     * Returns the validated file data.
     *
     * @return The validated file data.
     */

    public FileUploadData getFileUploadData() {
        return fileUploadData;
    }
}
