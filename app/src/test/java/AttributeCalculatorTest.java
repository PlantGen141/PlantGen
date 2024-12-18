import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import plantgen.AttributeCalculator;
import plantgen.Plant;
import plantgen.Species;
import plantgen.Coordinate;

import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for AttributeCalculator
 */

public class AttributeCalculatorTest {

    private Species species;
    private Plant plant;
    private CopyOnWriteArrayList<Plant> placedPlants;
    private AttributeCalculator attributeCalculator;

    // Set up the species, plant, and placedPlants for testing
    @BeforeEach
    public void setUp() {
        species = new Species();
        species.setLifespan(100);
        species.setMaxHeightOpen(10);
        species.setMaxHeightClosed(10);
        species.setRadiusMultiplierOpen(1.0f);
        species.setRadiusMultiplierClosed(2f);
        species.setQ(-1);

        plant = new Plant("TestSpecies", new Coordinate(1, 1));
        plant.setIsCanopy();

        placedPlants = new CopyOnWriteArrayList<>();

        attributeCalculator = new AttributeCalculator(species);
    }

    /*
     * Test the calculateAttributes method of the AttributeCalculator class by
     * comparing the expected data with the actual data calculated
     * when the plant is a canopy
     */
    @Test
    public void testCalculateAttributesOpen() {
        double vigour = 0.2;
        double gridSpacing = 1.0;
        int seed = 0;

        attributeCalculator.calculateAttributes(vigour, plant, placedPlants, gridSpacing, seed);

        int expectedAge = 20;
        double expectedHeight = 0.996;
        double expectedCanopyRadius = 1.993;

        assertEquals(expectedAge, plant.getAge(), 0.01);
        assertEquals(expectedHeight, plant.getHeight(), 0.01);
        assertEquals(expectedCanopyRadius, plant.getCanopyRadius(), 0.01);
    }

    /*
     * Test the calculateAttributes method of the AttributeCalculator class by
     * comparing the expected data with the actual data calculated
     * when the plant is not a canopy
     */
    @Test
    public void testCalculateAttributesClosed() {
        double vigour = 0.2;
        double gridSpacing = 1.0;
        int seed = 42;

        attributeCalculator.calculateAttributes(vigour, plant, placedPlants, gridSpacing, seed);

        int expectedAge = 11;
        double expectedHeight = 0.549;
        double expectedCanopyRadius = 1.098;

        assertEquals(expectedAge, plant.getAge());
        assertEquals(expectedHeight, plant.getHeight(), 0.01);
        assertEquals(expectedCanopyRadius, plant.getCanopyRadius(), 0.01);
    }

    /*
     * Test the getDistance method of the AttributeCalculator class by comparing the
     * expected data with the actual data calculated
     */

    @Test
    public void testGetDistance() {
        Plant otherPlant = new Plant("testSpecies", new Coordinate(4, 5));
        double gridSpacing = 1.0;

        double distance = attributeCalculator.getDistance(plant, otherPlant, gridSpacing);

        double expectedDistance = 5.0;

        assertEquals(expectedDistance, distance);
    }
}