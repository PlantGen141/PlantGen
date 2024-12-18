import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import plantgen.AbioticMap;

/**
 * Test class for AbioticMap
 */

public class AbioticMapTest {
    @Test
    /*
     * Test the readData method of the AbioticMap class by comparing the expected
     * data with the actual data read from the file
     */
    public void testReadData() {
        AbioticMap abioMap = new AbioticMap();
        abioMap.setNumMonths(3);
        abioMap.setDimX(3);
        abioMap.setDimY(3);

        double[][][] abioExpData = {
                { { 9.4683, 10.4425, 10.9785 }, { 11.9607, 12.939, 13.8934 }, { 12.8804, 11.9212, 10.9962 } },
                { { 10.48, 9.50823, 8.5186 }, { 9.4683, 10.4425, 11.0031 }, { 11.9607, 12.939, 13.8934 } },
                { { 12.8803, 11.9211, 11.0191 }, { 10.4798, 9.50802, 8.52742 }, { 9.4683, 10.4425, 11.0244 } } };

        Path resourcePath = Paths.get("src", "test", "resources", "dummySun.txt");
        abioMap.readData(resourcePath.toString());

        for (int m = 0; m < 3; m++) {
            for (int x = 0; x < 3; x++) {
                assertArrayEquals(abioExpData[m][x], abioMap.getData()[m][x], 0.00001);
            }
        }
    }

    /*
     * Test the copyData method of the AbioticMap class by comparing the expected
     * data with the actual data copied from the original data
     */
    @Test
    public void testCopyData() {
        double[][][] abioExpData = {
                { { 9.4683, 10.4425, 4000.05 }, { 11.9607, 12.939, 13.8934 }, { 12.8804, 11.9212, 10.9962 } },
                { { 10.48, 9.50823, 8.5186 }, { 9.4683, 10.4425, 11.0031 }, { 11.9607, 12.939, 13.8934 } },
                { { 12.8803, 11.9211, 11.0191 }, { 10.4798, 9.50802, 8.52742 }, { 9.4683, 10.4425, 11.0244 } } };

        AbioticMap abioMap = new AbioticMap();
        abioMap.setDimX(3);
        abioMap.setDimY(3);
        abioMap.setNumMonths(3);
        Path resourcePath = Paths.get("src", "test", "resources", "dummySun.txt");
        abioMap.readData(resourcePath.toString());
        abioMap.copyData();

        abioMap.setData(abioExpData);

        double[][][] actAbioData = abioMap.getOriginalData();

        assertNotEquals(abioMap.getData()[0][0][2], actAbioData[0][0][2]);
        assertEquals(abioMap.getData()[1][0][2], actAbioData[1][0][2]);
    }
}
