package gui;

/**
 * Interface representing a listener for file upload events.
 * Implement this interface to handle the completion of file upload validation.
 */

public interface FileUploadListener {

    /**
     * Called when the file upload validation is complete.
     *
     * @param data The data related to the file upload.
     */
    void onValidationComplete(FileUploadData data);
}
