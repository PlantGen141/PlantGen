package gui;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import plantgen.Coordinate;

/**
 * Class to plot the pink noise data
 */
public class PlotPinkNoise extends JPanel {

    private ArrayList<Coordinate> canopyCoords;
    private ArrayList<Coordinate> undergrowthCoords;
    private double[][] elvData;
    private double minElv;
    private double maxElv;
    private int dimX;
    private int dimY;

    /**
     * Constructor for PlotPinkNoise.
     *
     * @param canopyCoords      List of coordinates representing the canopy.
     * @param undergrowthCoords List of coordinates representing the undergrowth.
     * @param elvData           2D array of elevation data.
     * @param minElv            Minimum elevation value.
     * @param maxElv            Maximum elevation value.
     */
    public PlotPinkNoise(ArrayList<Coordinate> canopyCoords, ArrayList<Coordinate> undergrowthCoords,
            double[][] elvData, double minElv, double maxElv) {
        this.canopyCoords = canopyCoords;
        this.undergrowthCoords = undergrowthCoords;
        this.elvData = elvData;
        this.maxElv = maxElv;
        this.minElv = minElv;
        this.dimX = elvData.length;
        this.dimY = elvData[0].length;
        initialise();
    }

    /**
     * Creates a BufferedImage representing the elevation data.
     *
     * @return BufferedImage of the elevation data.
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
     * 
     * @return XYDataset containing the series for canopy and undergrowth
     *         coordinates
     */
    private XYDataset createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries series1 = new XYSeries("canopy");
        XYSeries series2 = new XYSeries("undergrowth");

        // Add canopy coordinates to the series
        for (int i = 0; i < canopyCoords.size(); i++) {
            series1.add(canopyCoords.get(i).getX(), canopyCoords.get(i).getY());
        }

        dataset.addSeries(series1);

        // Add undergrowth coordinates to the series
        for (int i = 0; i < undergrowthCoords.size(); i++) {
            series2.add(undergrowthCoords.get(i).getX(), undergrowthCoords.get(i).getY());
        }

        dataset.addSeries(series2);
        return dataset;
    }

    /**
     * Method to set the data of the plot
     */
    public void initialise() {
        XYDataset dataset = createDataset();

        // Create a scatter plot with the dataset
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Pink Noise Sampling Preview",
                "X-Coordinates", "Y-Coordinates", dataset);

        // Configure the renderer to display shapes without lines
        XYItemRenderer renderer = new XYLineAndShapeRenderer(false, true); // false for no lines, true for shapes

        // Configure the plot settings
        XYPlot plot = (XYPlot) chart.getPlot();
        BufferedImage elvImage = createElvImage();

        plot.setBackgroundImage(elvImage);
        plot.setBackgroundImageAlpha(0.5f);

        plot.setRenderer(renderer);
        renderer.setSeriesPaint(0, Color.green); // Color for series1 ("canopy")
        renderer.setSeriesPaint(1, Color.yellow); // Color for series2 ("undergrowth")
        plot.setRenderer(renderer);

        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis(); // X-axis
        domainAxis.setRange(0.0, dimX); // Manually set the x-axis range

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis(); // Y-axis
        rangeAxis.setRange(0.0, dimY); // Manually set the y-axis range

        plot.setDomainAxis(domainAxis);
        plot.setRangeAxis(rangeAxis);

        chart.getLegend().setPosition(RectangleEdge.RIGHT);

        // Create a chart panel and add it to the JPanel
        ChartPanel panel = new ChartPanel(chart);
        this.add(panel);

    }
}
