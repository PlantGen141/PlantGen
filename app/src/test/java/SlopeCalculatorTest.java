import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import plantgen.SlopeCalculator;
/*
 * Test class for SlopeCalculator class 
 */

public class SlopeCalculatorTest {

    /*
     * Test the deriveSlope method of the SlopeCalculator class
     */
    @Test
    public void testDeriveSlope() {
        double[][] elvData = { { 446.505, 447.196, 447.797 },
                { 448.394, 449.044, 449.653 },
                { 450.184, 450.842, 451.531 } };

        SlopeCalculator slopeCalc = new SlopeCalculator(elvData, 0.9144, 3, 3);

        double expSlope = 64.63344119;
        double[][] actSlope = slopeCalc.deriveSlope();

        assertEquals(expSlope, actSlope[1][1], 0.0000001);
    }
}