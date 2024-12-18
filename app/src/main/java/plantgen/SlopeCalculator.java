package plantgen;

/**
 * Class to calculate the slope at each coordinate using the elevation data
 */
public class SlopeCalculator {
    private int dimX;
    private int dimY;

    private double[][] elevation;
    private double gridSpacing;

    /**
     * Constructs a SlopeCalculator with the given terrain data.
     *
     * @param terrain the terrain data containing elevation and grid spacing
     *                information
     */
    public SlopeCalculator(Terrain terrain) {
        this.dimX = terrain.getDimX();
        this.dimY = terrain.getDimY();

        this.elevation = terrain.getElevationData();
        this.gridSpacing = terrain.getGridSpacing();
    }

    public SlopeCalculator(double[][] elvData, double gridSpacing, int dimX, int dimY) {
        this.elevation = elvData;
        this.gridSpacing = gridSpacing;
        this.dimX = dimX;
        this.dimY = dimY;
    }

    /**
     * Derives the slope of each coordinate in the elevation data grid.
     *
     * @return a 2D double array containing the slope data at each coordinate in
     *         degrees
     */
    public double[][] deriveSlope() {
        System.out.println("Calculating slope...");
        double[][] slopes = new double[dimX][dimY];

        // Iterate over the grid, excluding the border points
        for (int x = 1; x < dimX - 1; x++) {
            for (int y = 1; y < dimY - 1; y++) {
                // Calculate the partial derivatives dz/dx and dz/dy
                double dzdx = (elevation[x + 1][y] - elevation[x - 1][y]) / (2 * gridSpacing);
                double dzdy = (elevation[x][y + 1] - elevation[x][y - 1]) / (2 * gridSpacing);

                // Calculate the slope in radians
                double slopeRadians = Math.atan(Math.sqrt(Math.pow(dzdx, 2) + Math.pow(dzdy, 2)));
                // Convert the slope to degrees

                double slopeDegrees = Math.toDegrees(slopeRadians);

                slopes[x][y] = slopeDegrees;
            }
        }
        return slopes;
    }
}
