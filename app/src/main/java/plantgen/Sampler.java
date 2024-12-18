package plantgen;

import java.util.List;
import java.util.ArrayList;
import java.util.SplittableRandom;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;

/**
 * The Sampler class is responsible for generating canopy and undergrowth
 * coordinates
 * based on a given terrain and grid. It uses pink noise generation to
 * distribute points.
 */
public class Sampler {
    private float gridSpacing;

    private float dCanopy;
    private float dMid;
    private float dUndergrowth;

    private SplittableRandom random;

    private Grid grid;

    private static boolean NotgeneratePinkNoise = false;

    /**
     * Constructs a Sampler object.
     *
     * @param terrain     The terrain object containing grid spacing information.
     * @param grid        The grid object containing cells.
     * @param samplerArgs Array of sampler arguments.
     * @param random      The random number generator.
     */
    public Sampler(Terrain terrain, Grid grid, float[] samplerArgs, SplittableRandom random) {
        this.gridSpacing = terrain.getGridSpacing();

        this.grid = grid;

        this.dCanopy = samplerArgs[2];
        this.dUndergrowth = samplerArgs[3];
        this.dMid = samplerArgs[4];

        this.random = random;
    }

    /**
     * Constructs a Sampler object for testing purposes.
     */

    private Sampler() {
        gridSpacing = 0.9144f;
        grid = new Grid(256);
        grid.initialise(10, 4000, 8000, new SplittableRandom());

        dCanopy = 2;
        dUndergrowth = 1;
        dMid = 1.5f;

        random = new SplittableRandom();
    }

    /**
     * Gets a test instance of the Sampler class.
     *
     * @return A test instance of the Sampler class.
     */

    public static Sampler getTestInstance() {
        return new Sampler();
    }

    /**
     * Generates pink noise for canopy and undergrowth points.
     */

    public void generatePinkNoise() {
        System.out.println("Generating pink noise...");
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        // Generate canopy points for each cell in the grid using multithreading
        for (int colour = 0; colour < 8; colour++) {
            List<Cell> cells = grid.cellsByColour.getOrDefault(colour, new ArrayList<>());
            for (Cell c : cells) {
                int seed = random.nextInt();
                executor.submit(() -> generateCanopy(c, seed));
            }
            executor.shutdown();
            try {
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    throw new InterruptedException();
                }
                executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            } catch (InterruptedException e) {
                e.printStackTrace();
                NotgeneratePinkNoise = true;
                return;
            }
        }

        // Generate undergrowth points
        for (int colour = 0; colour < 8; colour++) {
            List<Cell> cells = grid.cellsByColour.getOrDefault(colour, new ArrayList<>());
            for (Cell c : cells) {
                int seed = random.nextInt();
                executor.submit(() -> generateUndergrowth(c, seed));
            }
            executor.shutdown();
            try {
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    throw new InterruptedException();
                }
                ;
                executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            } catch (InterruptedException e) {
                e.printStackTrace();
                NotgeneratePinkNoise = true;
                return;
            }
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                throw new InterruptedException();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            NotgeneratePinkNoise = true;
            return;
        }

    }

    /**
     * Generates canopy points for a given cell.
     *
     * @param cell The cell for which canopy points are generated.
     * @param seed The seed for random number generation.
     */
    public void generateCanopy(Cell cell, int seed) {
        // Get all coordinates to check for proximity
        ArrayList<Coordinate> coordsToCheck = new ArrayList<>();
        for (Cell n : cell.neighbours.values()) {
            coordsToCheck.addAll(n.canopyCoords);
        }

        SplittableRandom r = new SplittableRandom(seed);
        int pointsPlaced = 0;
        while (pointsPlaced < cell.getNumCPoints()) {
            boolean invalid = false;
            Coordinate newPoint = new Coordinate(r.nextFloat(cell.getStartX(), cell.getEndX()),
                    r.nextFloat(cell.getStartY(), cell.getEndY()));

            for (Coordinate coord : coordsToCheck) {
                if (newPoint.distanceFrom(coord) * gridSpacing < dCanopy) {
                    invalid = true;
                    break;
                }
            }

            if (!invalid) {
                cell.canopyCoords.add(newPoint);
                coordsToCheck.add(newPoint);
                pointsPlaced++;
            }
        }
    }

    /**
     * Generates undergrowth points for a given cell.
     *
     * @param cell The cell for which undergrowth points are generated.
     * @param seed The seed for random number generation.
     */
    public void generateUndergrowth(Cell cell, int seed) {
        ArrayList<Coordinate> cCoordsToCheck = new ArrayList<>();
        ArrayList<Coordinate> uCoordsToCheck = new ArrayList<>();
        // Get all coordinates to check for proximity
        for (Cell n : cell.neighbours.values()) {
            cCoordsToCheck.addAll(cell.canopyCoords);
            cCoordsToCheck.addAll(n.canopyCoords);
            uCoordsToCheck.addAll(n.undergrowthCoords);
        }

        SplittableRandom r = new SplittableRandom(seed);

        int pointsPlaced = 0;

        while (pointsPlaced < cell.getNumUPoints()) {
            boolean invalid = false;
            Coordinate newPoint = new Coordinate(r.nextFloat(cell.getStartX(), cell.getEndX()),
                    r.nextFloat(cell.getStartY(), cell.getEndY()));

            for (Coordinate coord : cCoordsToCheck) {
                if (newPoint.distanceFrom(coord) * gridSpacing < dMid) {
                    invalid = true;
                    break;
                }
            }

            if (invalid)
                continue;

            for (Coordinate coord : uCoordsToCheck) {
                if (newPoint.distanceFrom(coord) * gridSpacing < dUndergrowth) {
                    invalid = true;
                    break;
                }
            }

            if (!invalid) {
                cell.undergrowthCoords.add(newPoint);
                uCoordsToCheck.add(newPoint);
                pointsPlaced++;
            }
        }
    }

    /**
     * Gets all canopy coordinates from the grid.
     *
     * @return A list of canopy coordinates.
     */

    public ArrayList<Coordinate> getCanopyCoords() {
        ArrayList<Coordinate> coords = new ArrayList<>();

        for (Cell c : grid.getCells()) {
            coords.addAll(c.canopyCoords);
        }

        return coords;
    }

    /**
     * Gets all undergrowth coordinates from the grid.
     *
     * @return A list of undergrowth coordinates.
     */

    public ArrayList<Coordinate> getUndergrowthCoords() {
        ArrayList<Coordinate> coords = new ArrayList<>();

        for (Cell c : grid.getCells()) {
            coords.addAll(c.undergrowthCoords);
        }

        return coords;
    }

    public boolean getNotgeneratePinkNoise() {
        return NotgeneratePinkNoise;
    }
}
