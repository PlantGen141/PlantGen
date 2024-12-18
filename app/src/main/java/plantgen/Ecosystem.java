package plantgen;

import java.util.SplittableRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Ecosystem class to store the ecosystem data and manage plant placement and
 * attributes.
 */
public class Ecosystem {
    // Species Data
    private Species[] speciesData;

    private int numCPoints;
    private int numUPoints;
    private float dCanopy;

    Grid grid;
    private SplittableRandom random;
    private HashMap<String, AttributeCalculator> nameCalculatorMap = null;

    // System input
    private String[] filepaths;
    private float[] samplerArgs;
    private double[][] cohortAges;
    private double viabilityThreshold;

    private Terrain terrain = new Terrain(); // Stores terrain data

    // Handlers
    private Sampler sampler = null;
    private SlopeCalculator slopeCalculator = null;
    private ViabilityCalculator viabilityCalculator = null;
    private AbioticsUpdater abioticsUpdater = null;
    private RouletteWheel wheel = new RouletteWheel();

    // Derived attributes
    private double[][] slopeData = null;
    private ArrayList<Coordinate> canopyCoords = null;
    private ArrayList<Coordinate> undergrowthCoords = null;
    private CopyOnWriteArrayList<Plant> placedPlants = new CopyOnWriteArrayList<>();

    public Ecosystem(String[] filepaths, float[] samplerArgs, int seed, double viabilityThreshold) {
        this.random = new SplittableRandom(seed);

        this.numCPoints = (int) samplerArgs[0];
        this.numUPoints = (int) samplerArgs[1];
        this.dCanopy = samplerArgs[2];
        this.viabilityThreshold = viabilityThreshold;

        Species.initialiseSpeciesData(filepaths[4]);

        Species[] data = {
                new Species("Boxwood"),
                new Species("Snowy Mespilus"),
                new Species("Mountain Pine"),
                new Species("Silve Fir"),
                new Species("Silver Birch"),
                new Species("Sessile Oak"),
                new Species("European Beech")
        };

        this.speciesData = data;

        this.filepaths = filepaths;
        this.samplerArgs = samplerArgs;
        this.createHashMap();
    }

    /**
     * Method to load the canopy coords
     */
    private void loadCanopyCoords() {
        this.canopyCoords = this.sampler.getCanopyCoords();
    }

    /**
     * Method to load the undergrowth coords
     */
    private void loadUndergrowthCoords() {
        this.undergrowthCoords = this.sampler.getUndergrowthCoords();
    }

    /**
     * Method to load the terrain data
     */
    public void loadTerrainData() {
        terrain.loadData(this.filepaths);
        this.initialiseGrid();
        this.updateHandlers();
    }

    /**
     * Method to generate the pink noise
     */
    public void generatePinkNoise() {
        this.sampler.generatePinkNoise();
        this.loadCanopyCoords();
        this.loadUndergrowthCoords();
    }

    /**
     * Method to get the pink noise status
     * 
     * @param status the status of the pink noise
     */
    public boolean getPinkNoiseStatus() {
        return this.sampler.getNotgeneratePinkNoise();
    }

    /**
     * Method to print the pink noise
     */
    public void printPinkNoise() {
        System.out.println(this.sampler.getCanopyCoords().toString());
        System.out.println(this.sampler.getUndergrowthCoords().toString());
    }

    /**
     * Method to derive the slope
     */
    public void deriveSlope() {
        this.slopeData = this.slopeCalculator.deriveSlope();
        this.updateHandlers();
    }

    /**
     * Method to get the slope data
     * 
     * @return the slope data
     */
    public double[][] getSlopeData() {
        return this.slopeData;
    }

    /**
     * Method to update the handlers such that they're always instantiated
     */
    public void updateHandlers() {
        if (this.sampler == null && this.terrain != null) {
            this.sampler = new Sampler(this.terrain, this.grid, this.samplerArgs, this.random);
        }

        if (this.slopeCalculator == null && this.terrain != null) {
            this.slopeCalculator = new SlopeCalculator(this.terrain);
        }

        if (this.viabilityCalculator == null && (this.slopeData != null && this.canopyCoords != null)) {
            this.viabilityCalculator = new ViabilityCalculator(terrain, speciesData, this.slopeData);
        }

        if (this.abioticsUpdater == null && this.terrain != null) {
            this.abioticsUpdater = new AbioticsUpdater(this.grid, this.getGridSpacing());
        }
    }

    /**
     * Method that calls the viability calculator for a particular coordinate.
     * 
     * @param c Coordinate to calculate viabilities for.
     */
    public void calculateViabilities(Coordinate c) {
        c.setViabilities(viabilityCalculator.calculateAverageViability(c));
    }

    /**
     * Method to assign a plant to a coordinate.
     * Uses the roulette wheel to decide what plant (if any) to place.
     * 
     * @param c     Coordinate in the grid.
     * @param wheel Roulette wheel for plant selection.
     * @param seed  Seed for random number generation.
     * @return the plant placed
     */
    public Plant placePlant(Coordinate c, RouletteWheel wheel, int seed) {
        Plant plant = wheel.spinWheel(c, seed);
        if (plant != null)
            placedPlants.add(plant);
        return plant;
    }

    /**
     * Method to create a hash map such that each species has its own attribute
     * calculator
     * Since each species has different parameters
     */
    public void createHashMap() {
        AttributeCalculator.setCohortAge(cohortAges);
        AttributeCalculator.setViabilityThreshold(viabilityThreshold);
        nameCalculatorMap = new HashMap<>();
        for (int i = 0; i < speciesData.length; i++) {
            nameCalculatorMap.put(speciesData[i].getName(), new AttributeCalculator(speciesData[i]));
        }
    }

    /**
     * Method to calculate the attributes of the plant placed.
     * Uses the hashmap to get the plant placed's attribute calculator.
     * 
     * @param p    Plant placed.
     * @param seed Seed for random number generation.
     */
    public void calculateAttributes(Plant p, int seed) {
        String name = p.getSpecies();
        double vigour = 0;
        // Get the vigour of the plant
        for (int i = 0; i < speciesData.length; i++) {
            if (speciesData[i].getName() == name) {
                vigour = p.getPosition().getViabilities().get(i);
            }
        }

        nameCalculatorMap.get(name).calculateAttributes(vigour, p, this.placedPlants, this.getGridSpacing(), seed);
    }

    /**
     * Method to call the update abiotics methods for sun and moisture data
     * Uses the plant's parameters to call the methods
     * 
     * @param p plant
     */
    public void updateAbiotics(Plant p) {
        double leafTransparency = 0;
        double moistureAbsorption = 0;
        // Get the leaf transparency and moisture absorption of the plant
        for (int i = 0; i < speciesData.length; i++) {
            if (p.getSpecies() == speciesData[i].getName()) {
                leafTransparency = speciesData[i].getLeafTransparency();
                moistureAbsorption = speciesData[i].getMoistureAbsorption();
            }
        }

        this.abioticsUpdater.updateSunlight(getSunlight(), p, leafTransparency);
        this.abioticsUpdater.updateMoisture(getSunlight(), p, moistureAbsorption);
    }

    /**
     * Method to return the grid spacing
     * 
     * @return grid spacing
     */
    public double getGridSpacing() {
        return this.terrain.getGridSpacing();
    }

    /**
     * Method to return the sunlight data
     * 
     * @return sunlight abiotic data
     */
    public double[][][] getSunlight() {
        return this.terrain.getSunlightData();
    }

    /**
     * Method to set the cohort ages to the provided ones
     * @param cohortAge provided cohort ages
     */
    public void setCohortAge(double[][] cohortAge) {
        this.cohortAges = cohortAge;
    }

    /**
     * Method to return the ArrayList of placed plants
     * 
     * @return placed plants
     */
    public CopyOnWriteArrayList<Plant> getPlacedPlants() {
        return this.placedPlants;
    }

    /**
     * Method to return an ArrayList of potential canopy plant coordinates
     * 
     * @return canopy coordinates
     */
    public ArrayList<Coordinate> getCanopyCoords() {
        return this.canopyCoords;
    }

    /**
     * Method to return an ArrayList of potential undergrowth plant coordinates
     * 
     * @return undergrowth coordinates
     */
    public ArrayList<Coordinate> getUndergrowthCoords() {
        return this.undergrowthCoords;
    }

    /**
     * Method to initialize the grid.
     */
    public void initialiseGrid() {
        grid = new Grid(terrain.getDimX(), terrain.getDimY());
        grid.initialise(dCanopy * 5, numCPoints, numUPoints, random);
    }

    /**
     * Method to assign a plant to a coordinate.
     * 
     * @param c      Coordinate to assign the plant to.
     * @param canopy Boolean indicating if the plant is a canopy plant.
     * @param seed   Seed for random number generation.
     */
    public void assignPlant(Coordinate c, boolean canopy, int seed) {
        calculateViabilities(c);
        Plant p = placePlant(c, wheel, seed);
        // If no plant is placed, return
        if (p == null)
            return;
        if (canopy)
            p.setIsCanopy();

        calculateAttributes(p, ++seed);
        updateAbiotics(p);
    }

    /**
     * Method to assign plants to the ecosystem.
     */
    public void assignPlants() {
        System.out.println("Assigning Plants...");
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        // Assign canopy plants first to ensure they are placed before undergrowth
        for (int colour = 0; colour < 8; colour++) {
            List<Cell> cells = grid.cellsByColour.getOrDefault(colour, new ArrayList<>());
            for (Cell cell : cells) {
                int cellSeed = random.nextInt();
                ;
                executor.submit(() -> {
                    SplittableRandom r = new SplittableRandom(cellSeed);
                    int coordSeed = r.nextInt();
                    for (Coordinate coord : cell.canopyCoords) {
                        assignPlant(coord, true, coordSeed);
                    }
                });
            }

            // Assign undergrowth plants
            executor.shutdown();
            try {
                executor.awaitTermination(60, TimeUnit.HOURS);
                executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Assign undergrowth plants
        for (int colour = 0; colour < 8; colour++) {
            List<Cell> cells = grid.cellsByColour.getOrDefault(colour, new ArrayList<>());
            for (Cell cell : cells) {
                int cellSeed = random.nextInt();
                executor.submit(() -> {
                    SplittableRandom r = new SplittableRandom(cellSeed);
                    int coordSeed = r.nextInt();
                    for (Coordinate coord : cell.undergrowthCoords) {
                        assignPlant(coord, false, coordSeed);
                    }
                });
            }
            executor.shutdown();
            try {
                executor.awaitTermination(60, TimeUnit.HOURS);
                executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
    }

    /**
     * Method to get the terrain data.
     * 
     * @return Terrain data.
     */
    public Terrain getTerrain() {
        return this.terrain;
    }
}
