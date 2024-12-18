package gui;

/**
 * CohortAgeFetcher is a class that implements the DrawingListener interface.
 * It is responsible for fetching and storing cohort age data upon drawing
 * completion.
 */

public class CohortAgeFetcher implements DrawingListener {
    // 2D array to store cohort age data

    private double[][] cohortAge;

    /**
     * This method is called when the drawing is completed.
     * It sets the cohortAge field with the provided data.
     *
     * @param cohortAge 2D array containing cohort age data
     */

    @Override
    public void onDrawingCompletion(double[][] cohortAge) {
        this.cohortAge = cohortAge;
    }

    /**
     * Returns the cohort age data.
     *
     * @return 2D array containing cohort age data
     */
    public double[][] getCohortAge() {
        return cohortAge;
    }

    /**
     * Resets the cohort age data to null.
     */

    public void resetAges() {
        cohortAge = null;
    }
}
