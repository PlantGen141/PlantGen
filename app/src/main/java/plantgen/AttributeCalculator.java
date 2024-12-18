package plantgen;

import java.util.SplittableRandom;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class to calculate the attributes of a placed plant
 */
public class AttributeCalculator {

    private static double viabilityThreshold;
    private static double[][] cohortAge; // maximum age of a plant, extracted from drawing
    private Species type;

    private int lifespan;
    private int maxHeightOpen;
    private int maxHeightClosed;
    private float radiusMultiplierOpen;
    private float radiusMultiplierClosed;
    private int q; // tuning param

    /**
     * Constructor to initialize the AttributeCalculator with a given species.
     * 
     * @param species the species of the plant
     */
    public AttributeCalculator(Species species) {
        this.type = species;
        this.lifespan = this.type.getLifespan();
        this.maxHeightOpen = this.type.getMaxHeightOpen();
        this.maxHeightClosed = this.type.getMaxHeightClosed();
        this.radiusMultiplierOpen = this.type.getRadiusMultipierOpen();
        this.radiusMultiplierClosed = this.type.getRadiusMultipierClosed();
        this.q = this.type.getQ();
    }

    /**
     * Method to calculate the attributes of a plant to be placed.
     * 
     * @param vigour      the viability of the plant
     * @param plant       the plant itself
     * @param placed      the list of plants that have been placed already
     * @param gridSpacing the grid spacing
     * @param seed        the seed for random number generation
     */
    public void calculateAttributes(double vigour, Plant plant, CopyOnWriteArrayList<Plant> placed, double gridSpacing,
            int seed) {
        int maxHeight = maxHeightOpen;
        float radiusMultiplier = radiusMultiplierOpen;

        if (vigour > viabilityThreshold) { // If closed, the max height and radius multiplier change to the closed
                                           // values
            maxHeight = maxHeightClosed;
            radiusMultiplier = radiusMultiplierClosed;
        }

        // Calculate age
        int maxAge;
        if (cohortAge != null) {
            int cohAge = (int) cohortAge[Math.round(plant.getPosition().getX())][Math
                    .round(plant.getPosition().getY())];
            if (cohAge == -1) {
                cohAge = lifespan;
            }
            maxAge = Math.min(lifespan, cohAge);
        } else {
            maxAge = Math.min(lifespan, 350);
        }

        int maxUndergrowthAge = (int) Math.round(Math.max(0.4 * maxAge, 1));

        SplittableRandom r = new SplittableRandom(seed);

        int age;
        if (plant.getCanopy()) {
            age = (int) Math.ceil(r.nextInt(maxUndergrowthAge + 1, maxAge + 1) * vigour);
        } else {
            age = (int) Math.ceil(r.nextInt(1, maxUndergrowthAge + 1) * vigour);
        }

        // Calculate height
        double eExp = ((double) age / (double) lifespan) * q;
        double divisor = 1 + Math.pow(Math.E, eExp);
        double height = ((2 / divisor) - 1) * maxHeight;

        // Calculate canopy radius
        double canopyRadius = height * radiusMultiplier;

        plant.loadAttributes(age, height, canopyRadius);
    }

    /**
     * Method to get the distance between one plant and another.
     * 
     * @param p           current plant
     * @param other       plant from list
     * @param gridSpacing the grid spacing
     * @return the distance between the plants
     */
    public double getDistance(Plant p, Plant other, double gridSpacing) {
        double distance = (Math
                .sqrt(Math.pow(((double) p.getPosition().getX()) - ((double) other.getPosition().getX()), 2.0)
                        + Math.pow(((double) p.getPosition().getY()) - ((double) other.getPosition().getY()), 2.0)))
                * gridSpacing;

        return distance;
    }

    /**
     * Method to set the cohort age.
     * 
     * @param cohortAge the cohort age array
     */
    public static void setCohortAge(double[][] cohortAge) {
        AttributeCalculator.cohortAge = cohortAge;
    }

    /**
     * Method to set the viability threshold.
     * 
     * @param viabilityThreshold the viability threshold
     */
    public static void setViabilityThreshold(double viabilityThreshold) {
        AttributeCalculator.viabilityThreshold = viabilityThreshold;
    }
}
