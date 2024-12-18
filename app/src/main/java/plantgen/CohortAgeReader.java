package plantgen;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.awt.Color;

/**
 * The CohortAgeReader class reads an image file and maps colors to ages.
 * It provides methods to print the image colors, print the cohort ages,
 * and convert colors in the image to ages based on a provided color-age map.
 */
public class CohortAgeReader {

    private double[][] cohortAge;
    private BufferedImage image;
    private HashMap<Color, Integer> colorAgeMap;

    /**
     * Constructs a CohortAgeReader with the specified image path and color-age map.
     *
     * @param path        the path to the image file
     * @param colorAgeMap a map of colors to ages
     */
    public CohortAgeReader(String path, HashMap<Color, Integer> colorAgeMap) {

        try {
            File imagePath = new File(path);
            BufferedImage image = ImageIO.read(imagePath);
            this.image = image;
            this.colorAgeMap = colorAgeMap;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Prints the color of each pixel in the image.
     */
    public void printImage() {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color color = new Color(image.getRGB(i, j));
                System.out.println(color);
            }
        }
    }

    /**
     * Prints the cohort age of each pixel in the image.
     */
    public void printCohortAge() {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                System.out.println(cohortAge[i][j]);
            }
        }
    }

    /**
     * Converts the colors in the image to ages based on the color-age map.
     * White pixels are assigned an age of -1.
     *
     * @return a 2D array representing the ages of each pixel in the image
     */
    public double[][] convertColourToAge() {

        // loop through the image and get the color of each pixel
        this.cohortAge = new double[image.getWidth()][image.getHeight()];
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color color = new Color(image.getRGB(i, j));

                // If the color is white, assign an age of -1
                if (color.equals(Color.WHITE)) {
                    this.cohortAge[i][j] = -1;
                } else {
                    // Otherwise, get the age from the colorAgeMap
                    this.cohortAge[i][j] = colorAgeMap.get(color);
                }
            }
        }
        return (cohortAge);

    }

}
