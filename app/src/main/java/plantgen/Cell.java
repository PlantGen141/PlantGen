package plantgen;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a cell in a plant generation grid.
 * A cell has a start and end coordinate, a color, and can contain canopy and
 * undergrowth coordinates.
 * It also keeps track of its neighboring cells.
 */

public class Cell {
    private int startX, startY;
    private int endX, endY;

    private int colour;
    private int numUPoints;
    private int numCPoints;
    public HashMap<String, Cell> neighbours;

    public ArrayList<Coordinate> canopyCoords;
    public ArrayList<Coordinate> undergrowthCoords;

    /**
     * Constructs a Cell with specified start and end coordinates.
     * Initializes the neighbours, canopyCoords, and undergrowthCoords.
     *
     * @param startX the starting X coordinate
     * @param startY the starting Y coordinate
     * @param endX   the ending X coordinate
     * @param endY   the ending Y coordinate
     */

    public Cell(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;

        neighbours = new HashMap<>();

        canopyCoords = new ArrayList<>();
        undergrowthCoords = new ArrayList<>();
    }

    /**
     * Checks if a point (x, y) is within the cell's boundaries.
     *
     * @param x the X coordinate of the point
     * @param y the Y coordinate of the point
     * @return true if the point is within the cell, false otherwise
     */
    public boolean contains(double x, double y) {
        if (x >= startX && x <= endX &&
                y >= startY && y <= endY) {
            return true;
        }

        return false;
    }

    /**
     * Gets the starting X coordinate of the cell.
     *
     * @return the starting X coordinate
     */
    public int getStartX() {
        return this.startX;
    }

    /**
     * Gets the starting Y coordinate of the cell.
     *
     * @return the starting Y coordinate
     */
    public int getStartY() {
        return this.startY;
    }

    /**
     * Gets the ending X coordinate of the cell.
     *
     * @return the ending X coordinate
     */
    public int getEndX() {
        return this.endX;
    }

    /**
     * Gets the ending Y coordinate of the cell.
     *
     * @return the ending Y coordinate
     */
    public int getEndY() {
        return this.endY;
    }

    /**
     * Gets the colour of the cell.
     *
     * @return the colour of the cell
     */
    public int getColour() {
        return this.colour;
    }

    /**
     * Gets the number of canopy points in the cell.
     *
     * @return the number of canopy points
     */
    public int getNumCPoints() {
        return this.numCPoints;
    }

    /**
     * Gets the number of undergrowth points in the cell.
     *
     * @return the number of undergrowth points
     */
    public int getNumUPoints() {
        return this.numUPoints;
    }

    /**
     * Sets the number of canopy points in the cell.
     *
     * @param numCPoints the number of canopy points to set
     */
    public void setNumCPoints(int numCPoints) {
        this.numCPoints = numCPoints;
    }

    /**
     * Sets the number of undergrowth points in the cell.
     *
     * @param numUPoints the number of undergrowth points to set
     */
    public void setNumUPoints(int numUPoints) {
        this.numUPoints = numUPoints;
    }

    /**
     * Sets the colour of the cell.
     *
     * @param colour the colour to set
     */
    public void setColour(int colour) {
        this.colour = colour;
    }

    /**
     * Checks if this cell is equal to another cell based on their coordinates.
     *
     * @param other the other cell to compare with
     * @return true if the cells have the same coordinates, false otherwise
     */
    public boolean equals(Cell other) {
        if (this.startX == other.startX && this.startY == other.startY &&
                this.endX == other.endX && this.endY == other.endY) {
            return true;
        }

        return false;
    }

    /**
     * Returns a string representation of the cell.
     *
     * @return a string representing the cell's coordinates
     */
    public String toString() {
        return "Cell: (" + this.startX + "," + this.startY + ") - " +
                "(" + this.endX + "," + this.endY + ")";
    }
}
