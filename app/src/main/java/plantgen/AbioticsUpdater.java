package plantgen;

import java.util.ArrayList;

/**
 * Class to update the abiotics of the terrain once a plant is placed
 */
public class AbioticsUpdater {
    private Grid grid;
    private double gridSpacing;

    public AbioticsUpdater(Grid grid, double gridSpacing) {
        this.grid = grid;
        this.gridSpacing = gridSpacing;
    }

    /**
     * Method to update the sunlight abiotics of the terrain
     * For each coordinate within the calculated distance of the plant being placed
     * the sunlight abiotics data is multiplicatively updated
     * 
     * @param sunlight         the sunlight data of the terrain
     * @param p                the plant that's being placed
     * @param leafTransparency the species leaf transparency
     * @return the updated sunlight data
     */
    public double[][][] updateSunlight(double[][][] sunlight, Plant p, double leafTransparency) {
        double updateDistance = 1.5 * p.getCanopyRadius();

        Coordinate position = p.getPosition();
        Cell c = grid.findCell(Math.round(position.getX()), Math.round(position.getY()));

        ArrayList<Coordinate> coordsToCheck = new ArrayList<>();

        // Add coordinates from the cell's canopy and undergrowth
        coordsToCheck.addAll(c.canopyCoords);
        coordsToCheck.addAll(c.undergrowthCoords);

        // Add coordinates from neighboring cells' canopy and undergrowth
        for (Cell n : c.neighbours.values()) {
            coordsToCheck.addAll(n.canopyCoords);
            coordsToCheck.addAll(n.undergrowthCoords);
        }

        // Update sunlight data for coordinates within the update distance
        for (Coordinate coord : coordsToCheck) {
            if (coord.distanceFrom(position) < updateDistance) {
                int x = Math.round(coord.getX());
                int y = Math.round(coord.getY());
                for (int m = 0; m < 12; m++) {
                    sunlight[m][x][y] *= leafTransparency;
                }
            }
        }

        return sunlight;
    }

    /**
     * Method to update the moisture abiotics of the terrain
     * For each coordinate within the calculated distance of the plant being placed
     * the moisture abiotics data is subtractively updated
     * 
     * @param moisture           the moisture data of the terrain
     * @param p                  the plant that's being placed
     * @param moistureAbsorption the species moisture absorption data
     * @return the updated moisture data
     */
    public double[][][] updateMoisture(double[][][] moisture, Plant p, double moistureAbsorption) {
        double updateDistance = 1.5 * p.getCanopyRadius();

        Coordinate position = p.getPosition();
        Cell c = grid.findCell(Math.round(position.getX()), Math.round(position.getY()));

        ArrayList<Coordinate> coordsToCheck = new ArrayList<>();

        // Add coordinates from the cell's canopy and undergrowth
        coordsToCheck.addAll(c.canopyCoords);
        coordsToCheck.addAll(c.undergrowthCoords);

        // Add coordinates from neighboring cells' canopy and undergrowth
        for (Cell n : c.neighbours.values()) {
            coordsToCheck.addAll(n.canopyCoords);
            coordsToCheck.addAll(n.undergrowthCoords);
        }

        // Update moisture data for coordinates within the update distance
        for (Coordinate coord : coordsToCheck) {
            if (coord.distanceFrom(position) < updateDistance) {
                int x = Math.round(coord.getX());
                int y = Math.round(coord.getY());
                for (int m = 0; m < 12; m++) {
                    moisture[m][x][y] -= moistureAbsorption;
                }
            }
        }

        return moisture;
    }

    /**
     * Method to check whether or not a coordinate should be updated
     * If the distance is within the affected radius return true
     * 
     * @param current        the plant being placed's coordinates
     * @param toUpdate       the coordinates to potentially update
     * @param updateDistance the furthest distance that can be updated
     * @return
     */
    public boolean checkGoodDistance(Coordinate current, Coordinate toUpdate, double updateDistance) {
        double distance = Math.sqrt(Math.pow(((double) current.getX()) - ((double) toUpdate.getX()), 2.0)
                + Math.pow(((double) current.getY()) - ((double) toUpdate.getY()), 2.0));

        // Multiplied by grid space to account for the scale of the grid
        return distance * gridSpacing < updateDistance;
    }
}
