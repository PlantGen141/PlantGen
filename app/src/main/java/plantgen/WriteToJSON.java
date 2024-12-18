package plantgen;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Utility class for writing plant data and elevation data to a JSON file.
 */
public class WriteToJSON {

    /**
     * Writes the given plant data and elevation data to a JSON file.
     *
     * @param placedPlants  List of Plant objects to be written to the JSON file.
     * @param elevationData 2D array of elevation data.
     * @param gridSpacing   Spacing of the grid.
     * @param filePath      Path to the output JSON file.
     */
    public static void write(List<Plant> placedPlants, double[][] elevationData, float gridSpacing, String filePath) {
        StringBuilder toWrite = new StringBuilder();
        toWrite.append("{\n");
        toWrite.append(" \"plants\": [\n");

        // Loop through each plant and append its data to the JSON string
        for (int i = 0; i < placedPlants.size(); i++) { // output placed plants
            Plant plant = placedPlants.get(i);
            toWrite.append("  {\n");
            toWrite.append("    \"type\": \"").append(plant.getSpecies()).append("\",\n");
            toWrite.append("    \"isCanopy\": ").append(plant.getCanopy()).append(",\n");
            toWrite.append("    \"position\": \"").append(plant.getPosition()).append("\",\n");
            toWrite.append("    \"age\": ").append(plant.getAge()).append(",\n");
            toWrite.append("    \"height\": ").append(plant.getHeight()).append(",\n");
            toWrite.append("    \"canopy radius\": ").append(plant.getCanopyRadius()).append("\n");
            toWrite.append("  }");

            // Add a comma if it's not the last plant
            if (i < placedPlants.size() - 1) {
                toWrite.append(",");
            }

            toWrite.append("\n");
        }

        toWrite.append(" ],\n");

        toWrite.append(" \"elevationData\": [\n");

        // Loop through each row of elevation data and append it to the JSON string
        for (int i = 0; i < elevationData.length; i++) {
            toWrite.append("  [");
            for (int j = 0; j < elevationData[i].length; j++) {
                toWrite.append(elevationData[i][j]);
                if (j != elevationData[i].length - 1) {
                    toWrite.append(", ");
                }
            }
            if (i != elevationData.length - 1) {
                toWrite.append("],\n");
            } else {
                toWrite.append("]\n");
            }
        }

        toWrite.append(" ],\n");

        // Append grid spacing and number of plants to the JSON string
        toWrite.append(" \"gridSpacing\": ").append(gridSpacing).append(",\n");
        toWrite.append(" \"numberOfPlants\": ").append(placedPlants.size()).append("\n");
        toWrite.append("}");

        // Write the JSON string to the specified file
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(toWrite.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
