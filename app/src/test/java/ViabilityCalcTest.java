import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import plantgen.Coordinate;
import plantgen.Species;
import plantgen.Terrain;
import plantgen.ViabilityCalculator;
/*
 * Test class for ViabilityCalculator class 
 */

public class ViabilityCalcTest {
    // Test data for the sun, temperature, wetness, and slope data
    private double[][][] sunData = new double[][][] {
            { { 9.4683, 10.4425, 10.9785 }, { 11.9607, 12.939, 13.8934 }, { 12.8804, 11.9212, 10.9962 } },
            { { 10.48, 9.50823, 8.5186 }, { 9.4683, 10.4425, 11.0031 }, { 11.9607, 12.939, 13.8934 } },
            { { 12.8803, 11.9211, 11.0191 }, { 10.4798, 9.50802, 8.52742 }, { 9.4683, 10.4425, 11.0244 } }
    };

    private double[][][] tempData = new double[][][] {
            { { 2.95672, 2.15672, 4.75672 }, { 7.55672, 11.0567, 15.1567 }, { 18.0567, 17.8567, 14.1567 } },
            { { 10.4567, 5.65672, 3.65672 }, { 2.95222, 2.15222, 4.75222 }, { 7.55223, 11.0522, 15.1522 } },
            { { 18.0522, 17.8522, 14.1522 }, { 10.4522, 5.65223, 3.65223 }, { 2.94832, 2.14832, 4.74832 } }
    };

    private double[][][] wetData = new double[][][] {
            { { 3.65304, 0, 0 }, { 5.20001, 27.9409, 34.669 }, { 43.947, 44.45, 48.628 } },
            { { 53.5561, 76.297, 36 }, { 3.19169, 0, 0 }, { 5.20003, 29.3251, 35.5917 } },
            { { 44.4084, 44.4501, 48.1667 }, { 52.6334, 76.7584, 36.0001 }, { 2.81736, 0, 0 } }
    };

    private double[][] slopeData = new double[][] {
            { 0, 0, 0 },
            { 0, 64.63344119, 0 },
            { 0, 0, 0 }
    };

    /*
     * Test the calculateDistance method of the ViabilityCalculator class
     */

    @Test
    public void testCalculateDistance() {
        // Create a mock terrain object
        Terrain mockTerrain = new Terrain();
        mockTerrain.setDimX(3);
        mockTerrain.setDimY(3);
        mockTerrain.setNumMonths(3);
        mockTerrain.setElevationData(slopeData);
        mockTerrain.setGridSpacing(0.9144f);
        mockTerrain.setSunlightData(sunData);
        mockTerrain.setTemperatureData(tempData);
        mockTerrain.setWetData(wetData);

        Path resourcePath = Paths.get("src", "test", "resources", "species-data.csv");

        Species.initialiseSpeciesData(resourcePath.toString());
        Species[] subsetSpecies = new Species[] { new Species("Boxwood"), new Species("Mountain Pine") };

        ViabilityCalculator vc = new ViabilityCalculator(mockTerrain, subsetSpecies, slopeData);

        // expected result
        double abiotic = 10.0;
        float cFactor = 11.75f;
        double expected = 1.75;

        double actual = vc.calculateDistance(abiotic, cFactor);

        assertEquals(expected, actual);
    }

    /*
     * Test the adaptationFunction method of the ViabilityCalculator class
     */
    @Test
    public void testAdaptationFunction() {
        // Create a mock terrain object
        Terrain mockTerrain = new Terrain();
        mockTerrain.setDimX(3);
        mockTerrain.setDimY(3);
        mockTerrain.setNumMonths(3);
        mockTerrain.setElevationData(slopeData);
        mockTerrain.setGridSpacing(0.9144f);
        mockTerrain.setSunlightData(sunData);
        mockTerrain.setTemperatureData(tempData);
        mockTerrain.setWetData(wetData);

        Path resourcePath = Paths.get("src", "test", "resources", "species-data.csv");

        Species.initialiseSpeciesData(resourcePath.toString());
        Species[] subsetSpecies = new Species[] { new Species("Boxwood"), new Species("Mountain Pine") };

        ViabilityCalculator vc = new ViabilityCalculator(mockTerrain, subsetSpecies, slopeData);

        double d = 1.75;
        float rFactor = 23.25f;
        double expected = 0.9999829933;

        double actual = vc.adaptationFunction(d, rFactor);

        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testViabilityCalc() {
        Terrain mockTerrain = new Terrain();
        mockTerrain.setDimX(3);
        mockTerrain.setDimY(3);
        mockTerrain.setNumMonths(3);
        mockTerrain.setElevationData(slopeData);
        mockTerrain.setGridSpacing(0.9144f);
        mockTerrain.setSunlightData(sunData);
        mockTerrain.setTemperatureData(tempData);
        mockTerrain.setWetData(wetData);

        Path resourcePath = Paths.get("src", "test", "resources", "species-data.csv");

        Species.initialiseSpeciesData(resourcePath.toString());
        Species[] subsetSpecies = new Species[] { new Species("Boxwood"), new Species("Mountain Pine") };

        ViabilityCalculator vc = new ViabilityCalculator(mockTerrain, subsetSpecies, slopeData);

        double temp = 0.8;
        double moist = 0.7;
        double sunlight = 0.81;
        double slope = 0.45;
        double expected = 0.45;

        double actual = vc.viabilityCalc(temp, moist, sunlight, slope);

        assertEquals(expected, actual);
    }

    @Test
    public void testCalculateViability() {
        Terrain mockTerrain = new Terrain();
        mockTerrain.setDimX(3);
        mockTerrain.setDimY(3);
        mockTerrain.setNumMonths(3);
        mockTerrain.setElevationData(slopeData);
        mockTerrain.setGridSpacing(0.9144f);
        mockTerrain.setSunlightData(sunData);
        mockTerrain.setTemperatureData(tempData);
        mockTerrain.setWetData(wetData);

        Path resourcePath = Paths.get("src", "test", "resources", "species-data.csv");

        Species.initialiseSpeciesData(resourcePath.toString());
        Species[] subsetSpecies = new Species[] { new Species("Boxwood"), new Species("Mountain Pine") };

        ViabilityCalculator vc = new ViabilityCalculator(mockTerrain, subsetSpecies, slopeData);

        Coordinate testCoord = new Coordinate(1, 1);

        ArrayList<Double> actualViability = vc.calculateAverageViability(testCoord);

        ArrayList<Double> expectedViability = new ArrayList<Double>();
        expectedViability.add(0.0);
        expectedViability.add(0.0);

        assertEquals(expectedViability.get(0), actualViability.get(0), 0.01);
        assertEquals(expectedViability.get(1), actualViability.get(1), 0.01);
    }
}