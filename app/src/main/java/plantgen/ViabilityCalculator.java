package plantgen;

import java.util.ArrayList;

/**
 * Class to calculate the viabilites
 */
public class ViabilityCalculator {
    private Terrain terrain;
    private double[][] slopeData;
    final private double maxStressVal = 0.2;
    private Species[] plants;

    /**
     * Constructor to initialize the ViabilityCalculator.
     * 
     * @param t         Terrain object containing environmental data.
     * @param plants    Array of Species objects representing different plant
     *                  species.
     * @param slopeData 2D array containing slope data for the terrain.
     */
    public ViabilityCalculator(Terrain t, Species[] plants, double[][] slopeData) {
        this.terrain = t;
        this.plants = plants;
        this.slopeData = slopeData;
    }

    /**
     * Calculate the distance from the ideal value for a given factor.
     * 
     * @param abiotic The actual abiotic factor value.
     * @param cFactor The ideal value for the factor.
     * @return The absolute value of the distance.
     */
    public double calculateDistance(double abiotic, float cFactor) {
        return Math.abs(abiotic - cFactor);
    }

    /**
     * Calculate the adaptation value for a given factor.
     * 
     * @param d       The distance from the ideal value.
     * @param rFactor The response factor for the species.
     * @return The adaptation function value.
     */
    public double adaptationFunction(double d, float rFactor) {
        double eExp = Math.pow((d / rFactor), 4.5) * Math.log(0.2);
        return ((1 + maxStressVal) * Math.pow(Math.E, eExp)) - maxStressVal;
    }

    /**
     * Calculate the viability based on multiple factors.
     * 
     * @param temp     Adaptation value for temperature.
     * @param moist    Adaptation value for moisture.
     * @param sunlight Adaptation value for sunlight.
     * @param slope    Adaptation value for slope.
     * @return The minimum adaptation value among the factors.
     */
    public double viabilityCalc(double temp, double moist, double sunlight, double slope) {
        return Math.min(Math.min(temp, moist), Math.min(sunlight, slope));
    }

    /**
     * Method to calculate the average year viability at a particular coordinate,
     * per plant
     * 
     * @param c coordinate for which to calculate the viability
     * @return An ArrayList of the viabilities for each plant at that coordinate
     */
    public ArrayList<Double> calculateAverageViability(Coordinate c) {
        int xCoord = Math.round(c.getX());
        int yCoord = Math.round(c.getY());

        double slope = this.slopeData[xCoord][yCoord];

        int numMonths = terrain.getNumMonths();

        double[][] viability = new double[plants.length][12];

        for (int j = 0; j < plants.length; j++) { // per plant
            float[] sunFact = plants[j].getSunValues();
            float[] moistFact = plants[j].getWetValues();
            float[] tempFact = plants[j].getTempValues();
            float[] slopeFact = plants[j].getSlopeValues();

            for (int i = 0; i < numMonths; i++) { // per month
                double temp = terrain.getTemperatureData()[i][xCoord][yCoord];
                double moist = terrain.getWetData()[i][xCoord][yCoord];
                double sunlight = terrain.getSunlightData()[i][xCoord][yCoord];

                double tempDist = calculateDistance(temp, tempFact[0]);
                double tempAdapt = adaptationFunction(tempDist, tempFact[1]);

                double moistDist = calculateDistance(moist, moistFact[0]);
                double moistAdapt = adaptationFunction(moistDist, moistFact[1]);

                double sunDist = calculateDistance(sunlight, sunFact[0]);
                double sunAdapt = adaptationFunction(sunDist, sunFact[1]);

                double slopeDist = calculateDistance(slope, slopeFact[0]);
                double slopeAdapt = adaptationFunction(slopeDist, slopeFact[1]);

                // store in viability list
                viability[j][i] = Math.max(viabilityCalc(tempAdapt, moistAdapt, sunAdapt, slopeAdapt), 0);

            }

        }

        ArrayList<Double> viabilityAvg = new ArrayList<Double>();
        for (int i = 0; i < plants.length; i++) {
            viabilityAvg.add(0.0);
        }

        // get avg of each plant's year viability
        for (int i = 0; i < plants.length; i++) {
            for (int j = 0; j < numMonths; j++) {
                viabilityAvg.set(i, viabilityAvg.get(i) + viability[i][j]);
            }
            viabilityAvg.set(i, viabilityAvg.get(i) / numMonths);
        }

        return viabilityAvg;

    }
}
