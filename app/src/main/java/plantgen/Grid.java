package plantgen;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Set;
import java.util.SplittableRandom;

/**
 * The Grid class represents a grid structure composed of cells.
 * It provides methods to divide the grid into cells, find cell neighbors,
 * assign colors to cells, and group cells by color.
 */
public class Grid {
    int width, height;

    private ArrayList<Cell> cells;
    private ArrayList<Double> xCellBounds; // Store the x boundaries of cells
    private ArrayList<Double> yCellBounds; // Store the y boundaries of cells

    private final Map<Double, Set<Cell>> hashTable = new HashMap<>();

    public Map<Integer, List<Cell>> cellsByColour;

    int numRows, numCols;

    /**
     * Constructs a Grid with the specified dimensions.
     *
     * @param dimensions the dimensions of the grid
     */
    public Grid(int dimensions) {
        this.width = this.height = dimensions - 1;

        this.cells = new ArrayList<>();
        this.xCellBounds = new ArrayList<>();
        this.yCellBounds = new ArrayList<>();
    }

    /**
     * Constructs a Grid with the specified width and height.
     *
     * @param width  the width of the grid
     * @param height the height of the grid
     */
    public Grid(int width, int height) {
        this.width = width - 1;
        this.height = height - 1;

        this.cells = new ArrayList<>();
        this.xCellBounds = new ArrayList<>();
        this.yCellBounds = new ArrayList<>();
    }

    /**
     * Divides the grid into cells of size d x d.
     *
     * @param d the size of each cell
     * @return the list of cells created
     */
    public ArrayList<Cell> divideGrid(double d) {
        int numCellsX = (int) Math.floor(width / d);
        int numCellsY = (int) Math.floor(height / d);

        double currentX = 0;
        for (int i = 0; i < numCellsX; i++) {
            double nextX = (i == numCellsX - 1) ? width : currentX + d;
            xCellBounds.add(nextX);
            currentX = nextX;
        }

        double currentY = 0;
        for (int j = 0; j < numCellsY; j++) {
            double nextY = (j == numCellsY - 1) ? height : currentY + d;
            yCellBounds.add(nextY);
            currentY = nextY;
        }

        // Create cells and add them to the grid

        for (int i = 0; i < numCellsY; i++) {
            for (int j = 0; j < numCellsX; j++) {
                int cellStartX = (int) (j == 0 ? 0 : xCellBounds.get(j - 1));
                int cellStartY = (int) (i == 0 ? 0 : yCellBounds.get(i - 1));
                int cellEndX = (int) Math.floor(xCellBounds.get(j));
                int cellEndY = (int) Math.floor(yCellBounds.get(i));

                Cell c = new Cell(cellStartX, cellStartY, cellEndX, cellEndY);

                cells.add(c);
                addCell(c);
            }
        }

        this.numRows = yCellBounds.size();
        this.numCols = xCellBounds.size();

        return cells;
    }

    /**
     * Retrieves the cell at the specified row and column.
     *
     * @param row the row index
     * @param col the column index
     * @return the cell at the specified row and column
     * @throws IndexOutOfBoundsException if the row or column is out of bounds
     */
    public Cell getCell(int row, int col) {
        if (row < 0 || row > numRows - 1 || col < 0 || col > numCols - 1) {
            throw new IndexOutOfBoundsException("Invalid cell coordinates");
        }

        int index = row * numCols + col;
        return this.cells.get(index);
    }

    /**
     * Retrieves the position of the specified cell in the grid.
     *
     * @param cell the cell whose position is to be found
     * @return an array containing the row and column of the cell
     * @throws IllegalArgumentException if the cell is not found in the grid
     */

    public int[] getCellPosition(Cell cell) {
        int index = this.cells.indexOf(cell);
        if (index == -1) {
            throw new IllegalArgumentException("Cell not found in the grid");
        }

        int row = index / numCols;
        int col = index % numCols;

        return new int[] { row, col };
    }

    /**
     * Finds and assigns the neighbors of the specified cell.
     *
     * @param c the cell whose neighbors are to be found
     */
    public void findCellNeighbours(Cell c) {
        int[] cellPosition = getCellPosition(c);
        int row = cellPosition[0];
        int col = cellPosition[1];

        if (col != 0) {
            c.neighbours.put("left", getCell(row, col - 1));
            if (row != 0) {
                c.neighbours.put("top-left", getCell(row - 1, col - 1));
            }
            if (row != numRows - 1) {
                c.neighbours.put("bottom-left", getCell(row + 1, col - 1));
            }
        }

        if (row != 0) {
            c.neighbours.put("top", getCell(row - 1, col));
        }

        if (row != numRows - 1) {
            c.neighbours.put("bottom", getCell(row + 1, col));
        }

        if (col != numCols - 1) {
            c.neighbours.put("right", getCell(row, col + 1));
            if (row != 0) {
                c.neighbours.put("top-right", getCell(row - 1, col + 1));
            }
            if (row != numRows - 1) {
                c.neighbours.put("bottom-right", getCell(row + 1, col + 1));
            }
        }

    }

    /**
     * Assigns a color to the specified cell based on its position.
     *
     * @param c the cell to be colored
     */
    public void assignCellColour(Cell c) {
        int[] cellPosition = getCellPosition(c);
        int row = cellPosition[0];
        int col = cellPosition[1];

        c.setColour((row % 2) * 4 + (col % 2) * 2 + ((row + col) % 2));
    }

    /**
     * Groups cells by their color.
     */
    public void groupCellsbyColour() {
        cellsByColour = cells.stream().collect(Collectors.groupingBy(Cell::getColour));
    }

    /**
     * Retrieves the list of cells in the grid.
     *
     * @return the list of cells
     */
    public ArrayList<Cell> getCells() {
        return this.cells;
    }

    /**
     * Computes a hash value for the given x and y coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the hash value
     */
    private double hash(double x, double y) {
        return 31 * x + y; // Simple hash function
    }

    /**
     * Adds a cell to the hash table for efficient lookup.
     *
     * @param cell the cell to be added
     */
    public void addCell(Cell cell) {
        // Calculate the bucket indices for the cell's bounding box
        int minXBucket = (int) Math.floor(cell.getStartX());
        int maxXBucket = (int) Math.floor(cell.getEndX());
        int minYBucket = (int) Math.floor(cell.getStartY());
        int maxYBucket = (int) Math.floor(cell.getEndY());

        // Add the cell to all buckets it overlaps
        for (int x = minXBucket; x <= maxXBucket; x++) {
            for (int y = minYBucket; y <= maxYBucket; y++) {
                double hash = hash(x, y);
                hashTable.computeIfAbsent(hash, k -> new HashSet<>()).add(cell);
            }
        }
    }

    /**
     * Finds the cell that contains the specified x and y coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the cell that contains the coordinates, or null if no cell is found
     */
    public Cell findCell(double x, double y) {
        double hash = hash(x, y);
        Set<Cell> candidateCells = hashTable.get(hash);

        if (candidateCells != null) {
            for (Cell cell : candidateCells) {
                if (cell.contains(x, y)) {
                    return cell;
                }
            }
        }
        return null; // or handle the case where no cell is found
    }

    /**
     * Initializes the grid with the specified cell size, number of control points,
     * number of uncertainty points, and random number generator.
     *
     * @param cellSize   the size of each cell
     * @param numCPoints the number of control points
     * @param numUPoints the number of uncertainty points
     * @param random     the random number generator
     */

    public void initialise(double cellSize, int numCPoints, int numUPoints, SplittableRandom random) {
        ArrayList<Cell> cells = divideGrid(cellSize);

        int baseCpoints = numCPoints / cells.size();
        int baseUpoints = numUPoints / cells.size();

        int cPointVariance = baseCpoints / 20;
        int uPointVariance = baseUpoints / 40;

        ArrayList<Integer> cPointsPerCell = new ArrayList<>();
        ArrayList<Integer> uPointsPerCell = new ArrayList<>();

        int remainingCpoints = numCPoints;
        int remainingUpoints = numUPoints;

        // Assign control and uncertainty points to each cell
        for (int i = 0; i < cells.size(); i++) {
            int cPointValue = Math.max(0, baseCpoints + random.nextInt(0 - cPointVariance, cPointVariance + 1));
            int uPointValue = Math.max(0, baseUpoints + random.nextInt(0 - uPointVariance, uPointVariance + 1));

            cPointsPerCell.add(cPointValue);
            uPointsPerCell.add(uPointValue);

            remainingCpoints -= cPointValue;
            remainingUpoints -= uPointValue;
        }

        // Distribute remaining points randomly

        if (remainingCpoints > 0) {
            for (int i = 0; i < remainingCpoints; i++) {
                int index = random.nextInt(0, cells.size());
                cPointsPerCell.set(index, cPointsPerCell.get(index) + 1);
            }
        }

        if (remainingCpoints < 0) {
            for (int i = 0; i < remainingCpoints * -1; i++) {
                int index = random.nextInt(0, cells.size());
                cPointsPerCell.set(index, cPointsPerCell.get(index) - 1);
            }
        }

        if (remainingUpoints > 0) {
            for (int i = 0; i < remainingUpoints; i++) {
                int index = random.nextInt(0, cells.size());
                uPointsPerCell.set(index, uPointsPerCell.get(index) + 1);
            }
        }

        if (remainingUpoints < 0) {
            for (int i = 0; i < remainingUpoints * -1; i++) {
                int index = random.nextInt(0, cells.size());
                uPointsPerCell.set(index, uPointsPerCell.get(index) - 1);
            }
        }

        // Assign points to cells

        for (int i = 0; i < cells.size(); i++) {
            Cell c = cells.get(i);

            assignCellColour(c);
            findCellNeighbours(c);

            c.setNumCPoints(cPointsPerCell.get(i));
            c.setNumUPoints(uPointsPerCell.get(i));
        }

        groupCellsbyColour();
    }

}
