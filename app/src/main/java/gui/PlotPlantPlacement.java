package gui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;

import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import plantgen.Plant;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class to plot the plant placement
 */
public class PlotPlantPlacement extends JPanel {
    private CopyOnWriteArrayList<Plant> plantPlacements;
    private double[][] elvData;
    private double minElv;
    private double maxElv;
    private int dimX;
    private int dimY;

    /**
     * Constructor to initialize the PlotPlantPlacement panel.
     * 
     * @param plantPlacements List of plant placements.
     * @param elvData         Elevation data.
     * @param minElv          Minimum elevation.
     * @param maxElv          Maximum elevation.
     */

    public PlotPlantPlacement(CopyOnWriteArrayList<Plant> plantPlacements, double[][] elvData, double minElv,
            double maxElv) {
        this.plantPlacements = plantPlacements;
        this.elvData = elvData;
        this.maxElv = maxElv;
        this.minElv = minElv;
        this.dimX = elvData.length;
        this.dimY = elvData[0].length;
        setLayout(new BorderLayout());
        set();
    }

    /**
     * Creates an elevation image from the elevation data.
     * 
     * @return BufferedImage representing the elevation data.
     */

    private BufferedImage createElvImage() {
        BufferedImage image = new BufferedImage(dimX, dimY, BufferedImage.TYPE_BYTE_GRAY);

        for (int x = 0; x < dimX; x++) {
            for (int y = 0; y < dimY; y++) {
                int gray = (int) (255 * (elvData[x][y] - minElv) / (maxElv - minElv));
                int rgb = (gray << 16) | (gray << 8) | gray;
                image.setRGB(x, dimY - y - 1, rgb);
            }
        }

        return image;
    }

    /**
     * Method to create the dataset for the plot.
     * Each plant has an undergrowth and canopy series.
     * 
     * @return XYDataset containing the plant placement data.
     */
    private XYDataset createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries boxwoodUndergrowth = new XYSeries("boxwoodUndergrowth");
        XYSeries snowUndergrowth = new XYSeries("snowUndergrowth");
        XYSeries mountainUndergrowth = new XYSeries("mountainUndergrowth");
        XYSeries silvUndergrowth = new XYSeries("silvUndergrowth");
        XYSeries birchUndergrowth = new XYSeries("birchUndergrowth");
        XYSeries oakUndergrowth = new XYSeries("oakUndergrowth");
        XYSeries beechUndergrowth = new XYSeries("beechUndergrowth");

        XYSeries boxwoodCanopy = new XYSeries("boxwoodCanopy");
        XYSeries snowCanopy = new XYSeries("snowCanopy");
        XYSeries mountainCanopy = new XYSeries("mountainCanopy");
        XYSeries silvCanopy = new XYSeries("silvCanopy");
        XYSeries birchCanopy = new XYSeries("birchCanopy");
        XYSeries oakCanopy = new XYSeries("oakCanopy");
        XYSeries beechCanopy = new XYSeries("beechCanopy");

        int undergrowthCount = 0;
        int canopyCount = 0;

        // Iterate through plant placements and add to respective series
        for (int i = 0; i < plantPlacements.size(); i++) {
            Plant plant = plantPlacements.get(i);
            String plantName = plant.getSpecies();
            boolean canopy = plant.getCanopy();

            // switch statement to check what plant and plant type the coordinate relates to
            switch (plantName) {
                case "Boxwood":
                    if (canopy) {
                        boxwoodCanopy.add(plant.getPosition().getX(), plant.getPosition().getY());
                        canopyCount++;
                        break;
                    }
                    boxwoodUndergrowth.add(plant.getPosition().getX(), plant.getPosition().getY());
                    undergrowthCount++;
                    break;

                case "Snowy Mespilus":
                    if (canopy) {
                        snowCanopy.add(plant.getPosition().getX(), plant.getPosition().getY());
                        canopyCount++;
                        break;
                    }
                    snowUndergrowth.add(plant.getPosition().getX(), plant.getPosition().getY());
                    undergrowthCount++;
                    break;

                case "Mountain Pine":
                    if (canopy) {
                        mountainCanopy.add(plant.getPosition().getX(), plant.getPosition().getY());
                        canopyCount++;
                        break;
                    }
                    mountainUndergrowth.add(plant.getPosition().getX(), plant.getPosition().getY());
                    undergrowthCount++;
                    break;

                case "Silve Fir":
                    if (canopy) {
                        silvCanopy.add(plant.getPosition().getX(), plant.getPosition().getY());
                        canopyCount++;
                        break;
                    }
                    silvUndergrowth.add(plant.getPosition().getX(), plant.getPosition().getY());
                    undergrowthCount++;
                    break;

                case "Silver Birch":
                    if (canopy) {
                        birchCanopy.add(plant.getPosition().getX(), plant.getPosition().getY());
                        canopyCount++;
                        break;
                    }
                    birchUndergrowth.add(plant.getPosition().getX(), plant.getPosition().getY());
                    undergrowthCount++;
                    break;

                case "Sessile Oak":
                    if (canopy) {
                        oakCanopy.add(plant.getPosition().getX(), plant.getPosition().getY());
                        canopyCount++;
                        break;
                    }
                    oakUndergrowth.add(plant.getPosition().getX(), plant.getPosition().getY());
                    undergrowthCount++;
                    break;

                case "European Beech":
                    if (canopy) {
                        beechCanopy.add(plant.getPosition().getX(), plant.getPosition().getY());
                        canopyCount++;
                        break;
                    }
                    beechUndergrowth.add(plant.getPosition().getX(), plant.getPosition().getY());
                    undergrowthCount++;
                    break;
            }
        }

        // Add series to dataset
        dataset.addSeries(boxwoodUndergrowth);
        dataset.addSeries(boxwoodCanopy);

        dataset.addSeries(snowUndergrowth);
        dataset.addSeries(snowCanopy);

        dataset.addSeries(mountainUndergrowth);
        dataset.addSeries(mountainCanopy);

        dataset.addSeries(silvUndergrowth);
        dataset.addSeries(silvCanopy);

        dataset.addSeries(birchUndergrowth);
        dataset.addSeries(birchCanopy);

        dataset.addSeries(oakUndergrowth);
        dataset.addSeries(oakCanopy);

        dataset.addSeries(beechUndergrowth);
        dataset.addSeries(beechCanopy);

        // Add statistics panel
        addStatsPanel(plantPlacements.size(), undergrowthCount, canopyCount);

        return dataset;
    }

    /**
     * Method to set up the chart and panels.
     */
    public void set() {
        XYDataset dataset = createDataset();

        // Create scatter plot
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Plant Placement Plot",
                "X-Coordinates", "Y-Coordinates", dataset);

        XYItemRenderer renderer = new XYLineAndShapeRenderer(false, true); // false for no lines, true for shapes

        // Define colors for each plant type
        Color boxwoodColour = new Color(47, 79, 79);
        Color snowColour = new Color(127, 0, 0);
        Color mountainColour = new Color(0, 100, 0);
        Color silvColour = new Color(255, 140, 0);
        Color birchColour = new Color(222, 184, 135);
        Color oakColour = new Color(221, 160, 221);
        Color beechColour = new Color(0, 0, 205);

        // Set colors for each series
        renderer.setSeriesPaint(0, boxwoodColour);
        renderer.setSeriesPaint(1, boxwoodColour);
        renderer.setSeriesPaint(2, snowColour);
        renderer.setSeriesPaint(3, snowColour);
        renderer.setSeriesPaint(4, mountainColour);
        renderer.setSeriesPaint(5, mountainColour);
        renderer.setSeriesPaint(6, silvColour);
        renderer.setSeriesPaint(7, silvColour);
        renderer.setSeriesPaint(8, birchColour);
        renderer.setSeriesPaint(9, birchColour);
        renderer.setSeriesPaint(10, oakColour);
        renderer.setSeriesPaint(11, oakColour);
        renderer.setSeriesPaint(12, beechColour);
        renderer.setSeriesPaint(13, beechColour);

        // Set shapes for each series
        renderer.setSeriesShape(0, new java.awt.geom.Rectangle2D.Double(-3, -3, 6, 6)); // Boxwood Undergrowth (square)
        renderer.setSeriesShape(1, new java.awt.geom.Ellipse2D.Double(-3, -3, 6, 6)); // Boxwood Canopy (circle)

        renderer.setSeriesShape(2, new java.awt.geom.Rectangle2D.Double(-3, -3, 6, 6)); // Snow Undergrowth (square)
        renderer.setSeriesShape(3, new java.awt.geom.Ellipse2D.Double(-3, -3, 6, 6)); // Snow Canopy (circle)

        renderer.setSeriesShape(4, new java.awt.geom.Rectangle2D.Double(-3, -3, 6, 6)); // Mountain Undergrowth (square)
        renderer.setSeriesShape(5, new java.awt.geom.Ellipse2D.Double(-3, -3, 6, 6)); // Mountain Canopy (circle)

        renderer.setSeriesShape(6, new java.awt.geom.Rectangle2D.Double(-3, -3, 6, 6)); // Silv Undergrowth (square)
        renderer.setSeriesShape(7, new java.awt.geom.Ellipse2D.Double(-3, -3, 6, 6)); // Silv Canopy (circle)

        renderer.setSeriesShape(8, new java.awt.geom.Rectangle2D.Double(-3, -3, 6, 6)); // Birch Undergrowth (square)
        renderer.setSeriesShape(9, new java.awt.geom.Ellipse2D.Double(-3, -3, 6, 6)); // Birch Canopy (circle)

        renderer.setSeriesShape(10, new java.awt.geom.Rectangle2D.Double(-3, -3, 6, 6)); // Oak Undergrowth (square)
        renderer.setSeriesShape(11, new java.awt.geom.Ellipse2D.Double(-3, -3, 6, 6)); // Oak Canopy (circle)

        renderer.setSeriesShape(12, new java.awt.geom.Rectangle2D.Double(-3, -3, 6, 6)); // Beech Undergrowth (square)
        renderer.setSeriesShape(13, new java.awt.geom.Ellipse2D.Double(-3, -3, 6, 6)); // Beech Canopy (circle)

        XYPlot plot = (XYPlot) chart.getPlot();

        BufferedImage elvImage = createElvImage();

        plot.setBackgroundImage(elvImage);
        plot.setBackgroundImageAlpha(0.5f);

        plot.setRenderer(renderer);

        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis(); // X-axis
        domainAxis.setRange(0.0, dimX); // Manually set the x-axis range

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis(); // Y-axis
        rangeAxis.setRange(0.0, dimY); // Manually set the y-axis range

        plot.setDomainAxis(domainAxis);
        plot.setRangeAxis(rangeAxis);

        chart.getLegend().setPosition(RectangleEdge.RIGHT);

        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

        JPanel plotPanel = new JPanel();
        plotPanel.setLayout(new BorderLayout());

        ChartPanel chartPanel = new ChartPanel(chart);
        plotPanel.add(chartPanel, BorderLayout.CENTER);

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));

        wrapperPanel.add(plotPanel);

        this.add(wrapperPanel, BorderLayout.CENTER);
    }

    /**
     * Method to add a statistics panel to the plot.
     * 
     * @param total       Total number of plants.
     * @param undergrowth Number of undergrowth plants.
     * @param canopy      Number of canopy plants.
     */
    private void addStatsPanel(int total, int undergrowth, int canopy) {
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));

        JLabel totalLabel = new JLabel("Total Plants: " + total);
        JLabel undergrowthLabel = new JLabel("Number of Undergrowth Plants: " + undergrowth);
        JLabel canopyLabel = new JLabel("Number of Canopy Plants: " + canopy);

        statsPanel.add(totalLabel);
        statsPanel.add(undergrowthLabel);
        statsPanel.add(canopyLabel);

        this.add(statsPanel, BorderLayout.EAST);
    }
}
