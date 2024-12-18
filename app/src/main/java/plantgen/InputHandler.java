package plantgen;

/**
 * Class to handle the abiotics file inputs
 */
public class InputHandler {
    protected ElevationMap eMap = new ElevationMap();
    protected AbioticMap aMap = new AbioticMap();

    /**
     * Method that reads and returns formatted elevation data
     * @param filepath
     * @return the elevation data
     */

    public double[][] readElevation(String filepath) {
        this.eMap.readData(filepath);
        this.aMap.setDimX(this.eMap.getDimX());
        this.aMap.setDimY(this.eMap.getDimY());
        return this.eMap.getData();
    }

    /**
     * Method that reads and formats abiotic data
     * 
     * @param filepath
     */

    public double[][][] readAbiotic(String filepath) {
        this.aMap.readData(filepath);
        return this.aMap.getData();
    }
}
