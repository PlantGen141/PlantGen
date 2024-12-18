import org.junit.jupiter.api.Test;

import plantgen.Coordinate;
import plantgen.Plant;
import plantgen.RouletteWheel;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
/*
 * Test class for RouletteWheel class 
 */

public class RouletteWheelTest {
    /*
     * Test the getValidViabilities method of the RouletteWheel class
     */

    @Test
    public void testGetValidViabilities() {
        ArrayList<String> validSpecies = new ArrayList<>();
        ArrayList<Double> validViabilities = new ArrayList<>();

        ArrayList<Double> viabilities = new ArrayList<>();
        viabilities.add(-0.1); // Boxwood
        viabilities.add(-0.3); // Snowy Mespilus
        viabilities.add(0.2); // Mountain Pine
        viabilities.add(0.05); // Silve Fir
        viabilities.add(-0.02); // Silver Birch
        viabilities.add(0.0); // Sessile Oak
        viabilities.add(0.18); // European Beech

        // Initialise expected results
        ArrayList<String> expectedSpecies = new ArrayList<>();
        expectedSpecies.add("Mountain Pine");
        expectedSpecies.add("Silve Fir");
        expectedSpecies.add("European Beech");

        ArrayList<Double> expectedViabilities = new ArrayList<>();
        expectedViabilities.add(0.2);
        expectedViabilities.add(0.05);
        expectedViabilities.add(0.18);

        // Method call
        RouletteWheel wheel = new RouletteWheel();
        wheel.getValidViabilites(viabilities, validSpecies, validViabilities);

        // Comparison
        assertEquals(expectedSpecies, validSpecies);
        assertEquals(expectedViabilities, validViabilities);
    }
    /*
     * Test the getCumulativeViability method of the RouletteWheel class
     */

    @Test
    public void testGetCumulativeViability() {
        // Initialise parameters
        ArrayList<Double> validViabilities = new ArrayList<>();
        validViabilities.add(0.2);
        validViabilities.add(0.05);
        validViabilities.add(0.18);

        double[] cumulativeViability = new double[validViabilities.size() + 1];

        // Initialise expected results
        double[] expectedCumulativeViability = { 0, 0.2, 0.25, 0.43 };

        // Method call
        RouletteWheel wheel = new RouletteWheel();
        wheel.getCumulativeViability(validViabilities, cumulativeViability);

        // Comparison
        assertArrayEquals(expectedCumulativeViability, cumulativeViability);
    }

    /*
     * Test the spinWheel method of the RouletteWheel class
     */
    @Test
    public void testSpinWheel() {
        // Initialise parameters
        ArrayList<Double> viabilities = new ArrayList<>();
        viabilities.add(-0.1); // Boxwood
        viabilities.add(-0.3); // Snowy Mespilus
        viabilities.add(0.2); // Mountain Pine
        viabilities.add(0.05); // Silve Fir
        viabilities.add(-0.02); // Silver Birch
        viabilities.add(0.0); // Sessile Oak
        viabilities.add(0.18); // European Beech

        Coordinate c = new Coordinate(0, 0);
        c.setViabilities(viabilities);

        // Initialise expected results
        String mountainPine = "Mountain Pine";
        String silveFir = "Silve Fir";
        String europeanBeech = "European Beech";

        // Method Call
        RouletteWheel wheel = new RouletteWheel();

        double r1 = 0.1d; // For species selection, between 0 and max cumulative viability
        double r2 = 0d; // For if a plant is placed, between 0 and 1

        Plant plant = wheel.testSpinWheel(c, r1, r2); // Same method as spin wheel, except "random numbers" are passed
                                                      // in
        assertEquals(mountainPine, plant.getSpecies());

        r1 = 0.2d;
        plant = wheel.testSpinWheel(c, r1, r2);
        assertEquals(silveFir, plant.getSpecies());

        r1 = 0.4d;
        plant = wheel.testSpinWheel(c, r1, r2);
        assertEquals(europeanBeech, plant.getSpecies());

        r2 = 0.9d;
        plant = wheel.testSpinWheel(c, r1, r2);
        assertEquals(null, plant);
    }
}
