import plantgen.Coordinate;
import plantgen.Cell;
import plantgen.AbioticsUpdater;
import plantgen.Grid;
import plantgen.Plant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 * Test class for AbioticsUpdater class
 */
public class AbioticsUpdaterTest {
        private Grid grid;
        private AbioticsUpdater updater;
        private Plant plant;
        private Coordinate position;
        private Cell cell;
        private double gridSpacing = 1.0;

        // Set up the grid, plant, and cell for testing
        @BeforeEach
        public void setUp() {
                grid = new Grid(10, 10);
                grid.divideGrid(gridSpacing);

                position = new Coordinate(5, 5);
                plant = new Plant("TestSpecies", position);
                cell = grid.findCell(5.0, 5.0);

                plant.setCanopyRadius(2.0);

                cell.canopyCoords = new ArrayList<>();
                cell.undergrowthCoords = new ArrayList<>();
                cell.neighbours = new HashMap<>();

                updater = new AbioticsUpdater(grid, gridSpacing);
        }

        /*
         * Test the updateSunlight method of the AbioticsUpdater class by comparing the
         * expected
         * data with the actual data updated
         */

        @Test
        public void testUpdateSunlight() {
                double[][][] sunlight = new double[12][10][10];
                for (int i = 0; i < 12; i++) {
                        for (int x = 0; x < 10; x++) {
                                for (int y = 0; y < 10; y++) {
                                        sunlight[i][x][y] = 1.0;
                                }
                        }
                }

                Coordinate coord = new Coordinate(5, 5);
                cell.canopyCoords.add(coord);

                double leafTransparency = 0.8;
                double[][][] updatedSunlight = updater.updateSunlight(sunlight, plant, leafTransparency);

                for (int m = 0; m < 12; m++) {
                        assertEquals(0.8, updatedSunlight[m][5][5]);
                }
        }

        /*
         * Test the updateMoisture method of the AbioticsUpdater class by comparing the
         * expected
         * data with the actual data updated
         */

        @Test
        public void testUpdateMoisture() {
                double[][][] moisture = new double[12][10][10];
                for (int i = 0; i < 12; i++) {
                        for (int x = 0; x < 10; x++) {
                                for (int y = 0; y < 10; y++) {
                                        moisture[i][x][y] = 1.0;
                                }
                        }
                }

                Coordinate coord = new Coordinate(5, 5);
                cell.canopyCoords.add(coord);

                double moistureAbsorption = 0.2;
                double[][][] updatedMoisture = updater.updateMoisture(moisture, plant, moistureAbsorption);

                for (int m = 0; m < 12; m++) {
                        assertEquals(0.8, updatedMoisture[m][5][5]);
                }
        }
        /*
         * Test the checkGoodDistance method of the AbioticsUpdater class by comparing
         * the expected
         * data with the actual data
         */

        @Test
        public void testCheckGoodDistance() {
                Coordinate current = new Coordinate(5, 5);
                Coordinate toUpdate = new Coordinate(6, 6);
                double updateDistance = 2.0;

                boolean result = updater.checkGoodDistance(current, toUpdate, updateDistance);
                assertTrue(result);

                toUpdate = new Coordinate(10, 10);
                result = updater.checkGoodDistance(current, toUpdate, updateDistance);
                assertFalse(result);
        }
}