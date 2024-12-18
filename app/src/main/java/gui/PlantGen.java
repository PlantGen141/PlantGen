package gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import plantgen.Ecosystem;
import plantgen.WriteToJSON;

import com.formdev.flatlaf.FlatLightLaf;

/**
 * Main class for the PlantGen application.
 * This class sets up the GUI and handles the main workflow of the application.
 */

public class PlantGen {
    private static int timesRan = 1;

    /**
     * Main method to start the PlantGen application.
     * 
     * @param args Command line arguments
     */

    public static void main(String[] args) {

        try {
            // Load a custom theme
            UIManager.setLookAndFeel(new FlatLightLaf());

            // Customize UI properties
            UIManager.put("TabbedPane.background", new Color(144, 238, 144)); // Light green
            UIManager.put("TabbedPane.foreground", Color.gray);
            UIManager.put("TabbedPane.font", new Font("Serif", Font.BOLD, 14));
            UIManager.put("Panel.background", new Color(240, 255, 240)); // Honeydew
            UIManager.put("OptionPane.background", new Color(240, 255, 240)); // Honeydew
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create the main application window
        JFrame app = new JFrame("PlantGen - Team 141");
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        app.setSize(screenSize);
        app.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Create a tabbed pane for different panels
        JTabbedPane tabbedPane = new JTabbedPane();

        // Initialize file upload panel and input processor
        FileUploadPanel fileUploadPanel = new FileUploadPanel();
        InputsFetcher inputProcessor = new InputsFetcher();
        fileUploadPanel.setFileValidationListener(inputProcessor);

        // Add the file upload panel to the tabbed pane
        tabbedPane.addTab("File Upload and Inputs", fileUploadPanel);
        app.add(tabbedPane);
        app.setVisible(true);

        while (inputProcessor.getFileUploadData() == null) {
            try {
                Thread.sleep(100); // Check every 100 milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Retrieve file upload data
        FileUploadData fileUploadData = inputProcessor.getFileUploadData();
        fileUploadPanel.setVisible(false);

        // Extract data from file upload
        float[] samplerArgs = fileUploadData.getSamplerArgs();
        int seed = fileUploadData.getSeedValue();
        double viabilityThreshold = fileUploadData.getViabilityThresholdValue();
        String[] filePaths = fileUploadData.getFilepaths();

        // Initialize ecosystem with the uploaded data
        Ecosystem es = new Ecosystem(filePaths, samplerArgs, seed, viabilityThreshold);
        es.loadTerrainData();
        System.out.println("Terrain Data Loaded");

        System.out.println("Opening Drawing Panel");

        int dimX = es.getTerrain().getDimX();
        int dimY = es.getTerrain().getDimY();

        // Get dimensions for drawing panel
        Drawing drawingPanel = new Drawing(dimX, dimY);
        CohortAgeFetcher cohortAgeFetcher = new CohortAgeFetcher();
        drawingPanel.setListener(cohortAgeFetcher);

        // Add drawing panel to the tabbed pane
        tabbedPane.addTab("Cohort Age Drawer", drawingPanel);
        tabbedPane.setSelectedComponent(drawingPanel);

        // Wait for cohort age data
        while (cohortAgeFetcher.getCohortAge() == null) {
            try {
                Thread.sleep(100); // Check every 100 milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        double[][] cohortAges = cohortAgeFetcher.getCohortAge();

        es.setCohortAge(cohortAges);

        long startTime = System.currentTimeMillis(); // Start timer

        // Generate pink noise, derive slope, and assign plants
        es.generatePinkNoise();
        if (es.getPinkNoiseStatus()) {
            JOptionPane.showMessageDialog(null,
                    "Pink Noise generation failed. Please try again with valid parameters.");
            System.exit(0);

        }
        es.deriveSlope();
        es.assignPlants();

        long endTime = System.currentTimeMillis();

        JOptionPane.showMessageDialog(null, "Generation Time: " + (endTime - startTime) + " milliseconds.");

        System.out.println(es.getPlacedPlants().size()); // outputs number of plants

        System.out.println("Execution time: " + (endTime - startTime));

        // Save ecosystem data to JSON file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose Ecosystem JSON Save Location");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int userSelection = fileChooser.showSaveDialog(app);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            // Check if the selected file has the .json extension; if not, add it
            if (!filePath.toLowerCase().endsWith(".json")) {
                filePath += ".json";

                WriteToJSON.write(es.getPlacedPlants(), es.getTerrain().getElevationData(),
                        (float) es.getGridSpacing(),
                        filePath);
                JOptionPane.showMessageDialog(app, "File saved successfully at: " + filePath);
            }
        }

        PlotPinkNoise plotPinkNoise = new PlotPinkNoise(es.getCanopyCoords(), es.getUndergrowthCoords(),
                es.getTerrain().getElevationData(), es.getTerrain().getMinElv(), es.getTerrain().getMaxElv());

        tabbedPane.addTab("Pink Noise Plot " + 1, plotPinkNoise);
        tabbedPane.setSelectedComponent(plotPinkNoise);

        PlotPlantPlacement plotPlantPlacement = new PlotPlantPlacement(es.getPlacedPlants(),
                es.getTerrain().getElevationData(), es.getTerrain().getMinElv(), es.getTerrain().getMaxElv());

        tabbedPane.addTab("Plant Placement Plot " + 1, plotPlantPlacement);

        // Continuous reupload loop
        while (true) {

            timesRan = timesRan + 1;
            fileUploadPanel.reupload();
            // wait for user to reupload files (change old files to new files)
            // Retrieve existing file upload data
            String[] existingFiles = inputProcessor.getFileUploadData().getFilepaths();
            while (inputProcessor.getFileUploadData() == null
                    || existingFiles == inputProcessor.getFileUploadData().getFilepaths()) {
                try {
                    Thread.sleep(100); // Check every 100 milliseconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            FileUploadData fileUploadData2 = inputProcessor.getFileUploadData();

            samplerArgs = fileUploadData2.getSamplerArgs();
            seed = fileUploadData2.getSeedValue();
            filePaths = fileUploadData2.getFilepaths();

            Ecosystem es1 = new Ecosystem(filePaths, samplerArgs, seed, viabilityThreshold);
            es1.loadTerrainData();
            System.out.println("Terrain Data Loaded");

            int newDimX = es1.getSunlight()[0].length;
            int newDimY = es1.getSunlight()[0][0].length;

            if (newDimX != dimX || newDimY != dimY) {
                drawingPanel = new Drawing(newDimX, newDimY);
                drawingPanel.setListener(cohortAgeFetcher);
                tabbedPane.addTab("Cohort Age Drawer + " + timesRan, drawingPanel);
                tabbedPane.setSelectedComponent(drawingPanel);
                dimX = newDimX;
                dimY = newDimY;
            } else {
                tabbedPane.setSelectedComponent(drawingPanel);
                drawingPanel.restartDrawing();
            }

            cohortAgeFetcher.resetAges();
            cohortAges = cohortAgeFetcher.getCohortAge();

            while (cohortAgeFetcher.getCohortAge() == null) {
                try {
                    Thread.sleep(100); // Check every 100 milliseconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            es1.setCohortAge(cohortAges);

            startTime = System.currentTimeMillis(); // Start timer

            // Generate pink noise, derive slope, and assign plants
            es1.generatePinkNoise();
            if (es1.getPinkNoiseStatus()) {
                JOptionPane.showMessageDialog(null,
                        "Pink Noise generation failed. Please try again with valid parameters.");
                System.exit(0);

            }
            es1.deriveSlope();
            es1.assignPlants();

            endTime = System.currentTimeMillis();

            JOptionPane.showMessageDialog(null, "Generation Time: " + (endTime - startTime) + " milliseconds.");

            System.out.println(es1.getPlacedPlants().size()); // outputs number of plants

            System.out.println("Execution time: " + (endTime - startTime));

            // Save new ecosystem data to JSON file
            fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose Ecosystem JSON Save Location");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            userSelection = fileChooser.showSaveDialog(app);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();

                // Check if the selected file has the .json extension; if not, add it
                if (!filePath.toLowerCase().endsWith(".json")) {
                    filePath += ".json";

                    WriteToJSON.write(es1.getPlacedPlants(), es1.getTerrain().getElevationData(),
                            (float) es1.getGridSpacing(),
                            filePath);
                    JOptionPane.showMessageDialog(app, "File saved successfully at: " + filePath);
                }
            }

            // Plot pink noise and plant placement

            plotPinkNoise = new PlotPinkNoise(es1.getCanopyCoords(), es1.getUndergrowthCoords(),
                    es1.getTerrain().getElevationData(), es1.getTerrain().getMinElv(), es1.getTerrain().getMaxElv());

            tabbedPane.addTab("Pink Noise Plot " + timesRan, plotPinkNoise);
            tabbedPane.setSelectedComponent(plotPinkNoise);

            plotPlantPlacement = new PlotPlantPlacement(es1.getPlacedPlants(), es1.getTerrain().getElevationData(),
                    es1.getTerrain().getMinElv(), es1.getTerrain().getMaxElv());

            tabbedPane.addTab("Plant Placement Plot " + timesRan, plotPlantPlacement);

            app.setVisible(true);
        }
    }
}