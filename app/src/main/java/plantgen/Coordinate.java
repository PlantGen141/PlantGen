package plantgen;

import java.util.ArrayList;

/**
 * Class representing a coordinate with x and y values and a list of
 * viabilities.
 */
public class Coordinate {
    private float x;
    private float y;

    private ArrayList<Double> viabilities;

    /**
     * Constructor to initialize the coordinate with x and y values.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Coordinate(float x, float y) {
        this.x = x;
        this.y = y;

        this.viabilities = new ArrayList<>();
    }

    /**
     * Calculates the distance from this coordinate to another coordinate.
     * 
     * @param other the other coordinate
     * @return the distance between the two coordinates
     */
    public double distanceFrom(Coordinate other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    /**
     * Gets the x-coordinate.
     * 
     * @return the x-coordinate
     */
    public float getX() {
        return x;
    }

    /**
     * Gets the y-coordinate.
     * 
     * @return the y-coordinate
     */
    public float getY() {
        return y;
    }

    /**
     * Gets the list of viabilities.
     * 
     * @return the list of viabilities
     */
    public ArrayList<Double> getViabilities() {
        return this.viabilities;
    }

    /**
     * Sets the list of viabilities.
     * 
     * @param v the new list of viabilities
     */
    public void setViabilities(ArrayList<Double> v) {
        this.viabilities = v;
    }

    /**
     * Returns a string representation of the coordinate.
     * 
     * @return a string in the format "(x,y)"
     */
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    /**
     * Checks if this coordinate is equal to another coordinate.
     * 
     * @param other the other coordinate
     * @return true if the coordinates are equal, false otherwise
     */
    public boolean equals(Coordinate other) {
        if (this.x == other.x && this.y == other.y)
            return true;
        return false;
    }
}
