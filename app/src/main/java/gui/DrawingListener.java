package gui;

/**
 * The DrawingListener interface should be implemented by any class
 * that wants to be notified when a drawing operation is completed.
 */

public interface DrawingListener {

    /**
     * This method is called when a drawing operation is completed.
     *
     * @param cohortAge a 2D array representing the cohort age data
     */
    void onDrawingCompletion(double[][] cohortAge);
}
