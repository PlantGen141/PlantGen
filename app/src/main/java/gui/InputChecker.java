package gui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class InputChecker {

    // File paths for different input files
    private String elvPath;
    private String sunPath;
    private String tempPath;
    private String moisturePath;
    private String speciesPath;

    // Dimensions of the elevation data
    int dimX;
    int dimY;

    /**
     * Constructor to initialize file paths
     * 
     * @param elv      Path to the elevation file
     * @param sun      Path to the sunlight file
     * @param temp     Path to the temperature file
     * @param moisture Path to the moisture file
     * @param species  Path to the species file
     */
    public InputChecker(String elv, String sun, String temp, String moisture, String species) {

        this.elvPath = elv;
        this.sunPath = sun;
        this.tempPath = temp;
        this.moisturePath = moisture;
        this.speciesPath = species;
    }

    /**
     * Checks if the elevation file is valid
     * 
     * @return true if the elevation file is valid, false otherwise
     */
    private boolean checkElv() {
        if (elvPath == null || !elvPath.endsWith(".elv")) {
            return false;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(elvPath))) {
            String params = br.readLine();
            String[] param = params.split(" ");
            if (param.length != 4) {
                return false;
            }
            this.dimX = Integer.parseInt(param[0]);
            this.dimY = Integer.parseInt(param[1]);
            if (dimX <= 0 || dimY <= 0) {
                return false;
            }
            if (Double.parseDouble(param[2]) <= 0) {
                return false;
            }
            Double latitude = Double.parseDouble(param[3]);
            if (latitude < -90 || latitude > 90) {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Checks if the sunlight file is valid
     * 
     * @return true if the sunlight file is valid, false otherwise
     */
    private boolean checkSun() {
        if (sunPath == null || !sunPath.endsWith(".txt")) {
            return false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(sunPath))) {
            String params = br.readLine();
            String[] param = params.split(" ");
            if (param.length != 3) {
                return false;
            }
            int sunDimX = Integer.parseInt(param[0]);
            int sunDimY = Integer.parseInt(param[1]);

            if (sunDimX != this.dimX || sunDimY != this.dimY) {
                return false;
            }

            if (Double.parseDouble(param[2]) <= 0) {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Checks if the temperature file is valid
     * 
     * @return true if the temperature file is valid, false otherwise
     */
    private boolean checkTemp() {
        if (tempPath == null || !tempPath.endsWith(".txt")) {
            return false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(tempPath))) {
            String params = br.readLine();
            String[] param = params.split(" ");
            if (param.length != 2) {
                return false;
            }
            int tempDimX = Integer.parseInt(param[0]);
            int tempDimY = Integer.parseInt(param[1]);

            if (tempDimX != this.dimX || tempDimY != this.dimY) {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Checks if the moisture file is valid
     * 
     * @return true if the moisture file is valid, false otherwise
     */
    private boolean checkMoisture() {
        if (moisturePath == null || !moisturePath.endsWith(".txt")) {
            return false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(tempPath))) {
            String params = br.readLine();
            String[] param = params.split(" ");
            if (param.length != 2) {
                return false;
            }
            int moistDimX = Integer.parseInt(param[0]);
            int moistDimY = Integer.parseInt(param[1]);

            if (moistDimX != this.dimX || moistDimY != this.dimY) {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    /**
     * Checks if the species file is valid
     * 
     * @return true if the species file is valid, false otherwise
     */
    private boolean checkSpecies() {
        if (speciesPath == null || !speciesPath.endsWith(".csv")) {
            return false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(speciesPath))) {
            String params = br.readLine();
            String[] param = params.split(",");
            if (param.length != 20) {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Checks if all the input files are valid
     * 
     * @return true if all input files are valid, false otherwise
     */
    public boolean checkFile() {

        return checkElv() && checkSun() && checkTemp() && checkMoisture() && checkSpecies();

    }

}
