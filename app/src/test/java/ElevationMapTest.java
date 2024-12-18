import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import plantgen.ElevationMap;
/*
 * Test class for ElevationMap class 
 */

public class ElevationMapTest {
    /*
     * Test the readData method of the ElevationMap class by comparing the expected
     * data with the actual data read from the fileS
     */
    @Test
    public void testReadData() {
        ElevationMap elvMap = new ElevationMap();

        double[][] elvData = { { 446.505, 447.196, 447.797 }, { 448.394, 449.044, 449.653 },
                { 450.184, 450.842, 451.531 } };

        Path resourcePath = Paths.get("src", "test", "resources", "dummyElv.txt");
        elvMap.readData(resourcePath.toString());

        for (int i = 0; i < elvData.length; i++) {
            assertArrayEquals(elvData[i], elvMap.getData()[i], 0.00001);
        }
    }
}
