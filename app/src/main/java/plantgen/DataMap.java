package plantgen;

/**
 * Parent class: DataMap.
 * Used as a parent for the abiotics and elevation data. 
 */
public class DataMap {
    protected int dimX;
    protected int dimY;

    /**
     * Return the x dimensions of the map
     * @return X dimensions of the map
     */
    public int getDimX() {
        return this.dimX;
    }

    /**
     * Return the y dimensions of the map
     * @return Y dimensions of the map
     */
    public int getDimY() {
        return this.dimY;
    }

    /**
     * Method to set the X dimensions
     * @param x X dimension
     */
    public void setDimX(int x) {
        this.dimX = x;
    }

    /**
     * Method to set the Y dimensions
     * @param y Y dimension
     */
    public void setDimY(int y) {
        this.dimY = y;
    }
    
    /**
     * Abstract method to read the data.
     * To be used by the inheriting classes
     * @param filepath Filepath to read from
     */
    public void readData(String filepath) {}
}