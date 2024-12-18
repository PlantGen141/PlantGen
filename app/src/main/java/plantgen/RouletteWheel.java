package plantgen;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.SplittableRandom;

/**
 * Class for the Roulette Wheel
 */
public class RouletteWheel {
    private static List<String> speciesName = new ArrayList<>(
            Arrays.asList("Boxwood", "Snowy Mespilus", "Mountain Pine", "Silve Fir",
                    "Silver Birch", "Sessile Oak", "European Beech"));

    /**
     * Method to calculate the cumulative viabilities
     * 
     * @param validViabilities    List of valid viabilities
     * @param cumulativeViability Array to store cumulative viabilities
     */
    public void getCumulativeViability(ArrayList<Double> validViabilities, double[] cumulativeViability) {
        cumulativeViability[0] = 0;

        // Calculate cumulative viabilities
        for (int i = 1; i < cumulativeViability.length; i++) {
            cumulativeViability[i] = cumulativeViability[i - 1] + validViabilities.get(i - 1);
        }
    }

    /**
     * Method to randomly select a plant or nothing to place at a coordinate
     * depending on the viabilities
     * 
     * @param c    Coordinate object containing viabilities
     * @param seed Seed for random number generator
     * @return Plant object or null
     */
    public Plant spinWheel(Coordinate c, int seed) {
        ArrayList<String> validSpecies = new ArrayList<>();
        ArrayList<Double> validViabilities = new ArrayList<>();

        SplittableRandom r = new SplittableRandom(seed);

        getValidViabilites(c.getViabilities(), validSpecies, validViabilities);

        double[] cumulativeViability = new double[validViabilities.size() + 1];

        getCumulativeViability(validViabilities, cumulativeViability);

        Plant potentialPlant = null;
        double pViability = -1;
        // If there are no valid species, return null
        if (cumulativeViability[cumulativeViability.length - 1] == 0)
            return null;

        double randomNum = r.nextDouble(0, cumulativeViability[cumulativeViability.length - 1]);

        // Determine which plant to potentially select
        for (int i = 0; i < cumulativeViability.length; i++) {
            if (randomNum < cumulativeViability[i]) {
                potentialPlant = new Plant(validSpecies.get(i - 1), c);
                pViability = cumulativeViability[i] - cumulativeViability[i - 1];
                break;
            }
        }

        randomNum = r.nextDouble(0, 1);
        // If the random number is less than the potential viability, return the plant
        if (randomNum < pViability)
            return potentialPlant;
        return null;
    }

    /**
     * Method to test the spin wheel method
     * 
     * @param c          Coordinate object containing viabilities
     * @param randomNum1 Random number for selecting plant
     * @param randomNum2 Random number for selecting plant
     * @return Plant object or null
     */

    public Plant testSpinWheel(Coordinate c, double randomNum1, double randomNum2) {
        ArrayList<String> validSpecies = new ArrayList<>();
        ArrayList<Double> validViabilities = new ArrayList<>();

        getValidViabilites(c.getViabilities(), validSpecies, validViabilities);

        double[] cumulativeViability = new double[validViabilities.size() + 1];

        getCumulativeViability(validViabilities, cumulativeViability);

        Plant potentialPlant = null;
        double pViability = -1;
        // If there are no valid species, return null
        if (cumulativeViability[cumulativeViability.length - 1] == 0)
            return null;
        // Determine which plant to potentially select
        for (int i = 0; i < cumulativeViability.length; i++) {
            if (randomNum1 < cumulativeViability[i]) {
                potentialPlant = new Plant(validSpecies.get(i - 1), c);
                pViability = cumulativeViability[i] - cumulativeViability[i - 1];
                break;
            }
        }

        // If the random number is less than the potential viability, return the plant

        if (randomNum2 < pViability)
            return potentialPlant;
        return null;
    }

    /**
     * Method to get the valid viabilities
     * 
     * @param viabilities      List of viabilities
     * @param validSpecies     List of valid species
     * @param validViabilities List of valid viabilities
     */

    public void getValidViabilites(ArrayList<Double> viabilities, ArrayList<String> validSpecies,
            ArrayList<Double> validViabilities) {
        for (int i = 0; i < viabilities.size(); i++) { // ensures that there are no 0 viabilities in the list
            if (viabilities.get(i) > 0) {
                validViabilities.add(viabilities.get(i));
                validSpecies.add(RouletteWheel.speciesName.get(i));
            }
        }
    }
}
