import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import plantgen.Coordinate;
import plantgen.Sampler;
/*
 * Test class for Sampler class 
 */

public class SamplerTest {
    @Test
    public void testGeneratePinkNoise() {
        Sampler s = Sampler.getTestInstance();
        float gridSpacing = 0.9144f;

        s.generatePinkNoise();
        ArrayList<Coordinate> cPoints = s.getCanopyCoords();
        ArrayList<Coordinate> uPoints = s.getUndergrowthCoords();

        double distance;

        // Canopy-Canopy distance
        for (int i = 0; i < cPoints.size(); i++) {
            for (int j = 0; j < cPoints.size(); j++) {
                if (i == j)
                    continue;
                distance = Math.sqrt(Math.pow(cPoints.get(i).getX() - cPoints.get(j).getX(), 2)
                        + Math.pow(cPoints.get(i).getY() - cPoints.get(j).getY(), 2));
                assertEquals(true, distance * gridSpacing > 2);
            }
        }

        // Canopy-Undergrowth distance
        for (int i = 0; i < cPoints.size(); i++) {
            for (int j = 0; j < uPoints.size(); j++) {
                distance = Math.sqrt(Math.pow(cPoints.get(i).getX() - uPoints.get(j).getX(), 2)
                        + Math.pow(cPoints.get(i).getY() - uPoints.get(j).getY(), 2));
                assertEquals(true, distance * gridSpacing > 1.5);
            }
        }

        // Undergrowth-Undergrowth distance
        for (int i = 0; i < uPoints.size(); i++) {
            for (int j = 0; j < uPoints.size(); j++) {
                if (i == j)
                    continue;
                distance = Math.sqrt(Math.pow(uPoints.get(i).getX() - uPoints.get(j).getX(), 2)
                        + Math.pow(uPoints.get(i).getY() - uPoints.get(j).getY(), 2));
                assertEquals(true, distance * gridSpacing > 1);
            }
        }
    }
}
