package gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * A panel for uploading and validating various environmental data files.
 */

public class FileUploadPanel extends JPanel {

    // Buttons for uploading files
    private JButton elevationButton, sunButton, moistureButton, temperatureButton, validateButton, speciesButton;

    // Labels to display the status of file uploads
    private JLabel elevationLabel, sunLabel, moistureLabel, temperatureLabel, speciesLabel;

    // Spinners for user inputs
    private JSpinner numCanopyPoints, numUndergrowthPoints, minCanopyDistance, minUndergrowthDistance,
            minCanUnderDistance, seed, viabilityThreshold;

    // Files selected by the user
    private File elevationFile, sunFile, moistureFile, temperatureFile, speciesFile;

    // private Drawing drawingPanel;
    private File lastDirectory;

    // Values for user inputs
    private float numCanPointsVal;
    private float numUndergrowthPVal;
    private float minCanopyDistVal;
    private float minUndergrowthDistVal;
    private float minCanUnderDistVal;

    // Arguments for the sampler
    private float[] sampler_args;
    private int seedValue;
    private float viabilityThresholdValue;
    private String[] filepaths;

    // Listener for file validation
    private FileUploadListener validationListener;

    /**
     * Constructs a new FileUploadPanel.
     */
    public FileUploadPanel() {
        setLayout(new BorderLayout());

        // Panel for buttons and labels on the left
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 2)); // 4 rows, 2 columns

        // Create buttons and labels
        elevationButton = new JButton("Upload Elevation File");
        sunButton = new JButton("Upload Sun File");
        moistureButton = new JButton("Upload Moisture File");
        temperatureButton = new JButton("Upload Temperature File");
        speciesButton = new JButton("Upload Species File");

        elevationLabel = new JLabel("No file uploaded");
        sunLabel = new JLabel("No file uploaded");
        moistureLabel = new JLabel("No file uploaded");
        temperatureLabel = new JLabel("No file uploaded");
        speciesLabel = new JLabel("No file uploaded");

        // Add action listeners to handle file selection
        elevationButton.addActionListener(e -> updateFileLabel(elevationLabel, "Elevation"));
        sunButton.addActionListener(e -> updateFileLabel(sunLabel, "Sun"));
        moistureButton.addActionListener(e -> updateFileLabel(moistureLabel, "Moisture"));
        temperatureButton.addActionListener(e -> updateFileLabel(temperatureLabel, "Temperature"));
        speciesButton.addActionListener(e -> updateFileLabel(speciesLabel, "Species"));

        // Add buttons and labels to the panel
        buttonPanel.add(elevationButton);
        buttonPanel.add(elevationLabel);
        buttonPanel.add(sunButton);
        buttonPanel.add(sunLabel);
        buttonPanel.add(temperatureButton);
        buttonPanel.add(temperatureLabel);
        buttonPanel.add(moistureButton);
        buttonPanel.add(moistureLabel);
        buttonPanel.add(speciesButton);
        buttonPanel.add(speciesLabel);

        // Add the button panel to the left side of the frame
        add(buttonPanel, BorderLayout.WEST);

        // Panel for text fields on the right
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(7, 1)); // 6 rows, 1 column for text fields

        // Create text fields for user inputs
        numCanopyPoints = new JSpinner(new SpinnerNumberModel(0, 0, 100000.0, 1000.0));
        numUndergrowthPoints = new JSpinner(new SpinnerNumberModel(0, 0, 200000.0, 1000.0));
        minCanopyDistance = new JSpinner(new SpinnerNumberModel(0, 0, 30.0, 0.5));
        minUndergrowthDistance = new JSpinner(new SpinnerNumberModel(0, 0, 30.0, 0.5));
        minCanUnderDistance = new JSpinner(new SpinnerNumberModel(0, 0, 30.0, 0.5));
        seed = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        viabilityThreshold = new JSpinner(new SpinnerNumberModel(0, 0, 1.0, 0.01));

        // Add text fields to the input panel
        inputPanel.add(new JLabel("No. Canopy Points:"));
        inputPanel.add(numCanopyPoints);
        inputPanel.add(new JLabel("No. Undergrowth Points:"));
        inputPanel.add(numUndergrowthPoints);
        inputPanel.add(new JLabel("Minimum Canopy-Canopy Distance:"));
        inputPanel.add(minCanopyDistance);
        inputPanel.add(new JLabel("Minimum Undergrowth-Undergrowth Distance:"));
        inputPanel.add(minUndergrowthDistance);
        inputPanel.add(new JLabel("Minimum Canopy-Undergrowth Distance:"));
        inputPanel.add(minCanUnderDistance);
        inputPanel.add(new JLabel("Seed:"));
        inputPanel.add(seed);
        inputPanel.add(new JLabel("Viability Threshold:"));
        inputPanel.add(viabilityThreshold);

        // Add the input panel to the right side of the frame
        add(inputPanel, BorderLayout.EAST);

        // Add process button at the bottom
        validateButton = new JButton("Validate Files");
        validateButton.addActionListener(e -> validateFiles());
        add(validateButton, BorderLayout.SOUTH);
    }

    /**
     * Updates the label with the selected file's name and stores the file.
     *
     * @param label    The label to update.
     * @param fileType The type of file being uploaded.
     */
    private void updateFileLabel(JLabel label, String fileType) {
        File selectedFile = selectFile();
        if (selectedFile != null) {
            label.setText(selectedFile.getName());
            switch (fileType) {
                case "Elevation":
                    elevationFile = selectedFile;
                    break;
                case "Sun":
                    sunFile = selectedFile;
                    break;
                case "Moisture":
                    moistureFile = selectedFile;
                    break;
                case "Temperature":
                    temperatureFile = selectedFile;
                    break;
                case "Species":
                    speciesFile = selectedFile;
                    break;
            }
        }
    }

    /**
     * Opens a file chooser dialog for the user to select a file.
     *
     * @return The selected file, or null if no file was selected.
     */

    private File selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (lastDirectory != null) {
            fileChooser.setCurrentDirectory(lastDirectory);
        }
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            lastDirectory = fileChooser.getCurrentDirectory();
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    /**
     * Checks if the selected files are valid.
     *
     * @return true if all files are valid, false otherwise.
     */
    private boolean checkFiles() {
        InputChecker checkValidFile = new InputChecker(elevationFile.getAbsolutePath(), sunFile.getAbsolutePath(),
                temperatureFile.getAbsolutePath(), moistureFile.getAbsolutePath(), speciesFile.getAbsolutePath());
        return checkValidFile.checkFile();
    }

    /**
     * Re-enables the file upload and input fields for re-uploading files.
     */
    public void reupload() {
        validateButton.setEnabled(true);
        elevationButton.setEnabled(true);
        moistureButton.setEnabled(true);
        temperatureButton.setEnabled(true);
        speciesButton.setEnabled(true);
        sunButton.setEnabled(true);

        numCanopyPoints.setEnabled(true);
        numUndergrowthPoints.setEnabled(true);
        minCanUnderDistance.setEnabled(true);
        minCanopyDistance.setEnabled(true);
        minUndergrowthDistance.setEnabled(true);
        seed.setEnabled(true);
        viabilityThreshold.setEnabled(true);

        validateButton.addActionListener(e -> validateFiles());
        checkFiles();

    }

    /**
     * Validates the selected files and user inputs.
     */
    private void validateFiles() {
        if (!checkFiles()) {
            JOptionPane.showMessageDialog(this,
                    "One or more files are invalid. Please check your file selections.",
                    "Invalid Files", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Check if all inputs are valid and positive
        if (((Double) numCanopyPoints.getValue()).floatValue() <= 0
                || ((Double) numUndergrowthPoints.getValue()).floatValue() <= 0
                || ((Double) minCanopyDistance.getValue()).floatValue() <= 0
                || ((Double) minUndergrowthDistance.getValue()).floatValue() <= 0
                || ((Double) minCanUnderDistance.getValue()).floatValue() <= 0
                || ((Double) viabilityThreshold.getValue()).floatValue() <= 0) {

            JOptionPane.showMessageDialog(this,
                    "One or more inputs are invalid. Please check your input selections.",
                    "Invalid Inputs", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if all files are selected and fields are filled
        if (elevationFile != null && sunFile != null && moistureFile != null && temperatureFile != null) {
            // Disable the file upload and input fields
            validateButton.setEnabled(false);

            elevationButton.setEnabled(false);
            moistureButton.setEnabled(false);
            temperatureButton.setEnabled(false);
            speciesButton.setEnabled(false);
            sunButton.setEnabled(false);

            numCanopyPoints.setEnabled(false);
            numUndergrowthPoints.setEnabled(false);
            minCanUnderDistance.setEnabled(false);
            minCanopyDistance.setEnabled(false);
            minUndergrowthDistance.setEnabled(false);
            seed.setEnabled(false);
            viabilityThreshold.setEnabled(false);

            // Get values from the input fields
            this.numCanPointsVal = ((Double) numCanopyPoints.getValue()).floatValue();
            this.numUndergrowthPVal = ((Double) numUndergrowthPoints.getValue()).floatValue();
            this.minCanopyDistVal = ((Double) minCanopyDistance.getValue()).floatValue();
            this.minUndergrowthDistVal = ((Double) minUndergrowthDistance.getValue()).floatValue();
            this.minCanUnderDistVal = ((Double) minCanUnderDistance.getValue()).floatValue();

            this.sampler_args = new float[] { numCanPointsVal, numUndergrowthPVal, minCanopyDistVal,
                    minUndergrowthDistVal, minCanUnderDistVal };

            this.seedValue = (Integer) seed.getValue();

            this.viabilityThresholdValue = ((Double) viabilityThreshold.getValue()).floatValue();

            this.filepaths = new String[] { elevationFile.getAbsolutePath(), sunFile.getAbsolutePath(),
                    temperatureFile.getAbsolutePath(), moistureFile.getAbsolutePath(), speciesFile.getAbsolutePath() };
            // Notify the listener that validation is complete
            if (validationListener != null) {
                FileUploadData data = new FileUploadData(sampler_args, seedValue, viabilityThresholdValue, filepaths);
                validationListener.onValidationComplete(data);
            }

        } else {
            JOptionPane.showMessageDialog(this, "Please upload all files and enter all inputs before processing.");
        }
    }

    /**
     * Sets the listener for file validation.
     *
     * @param listener The listener to set.
     */
    public void setFileValidationListener(FileUploadListener listener) {
        this.validationListener = listener;
    }

}
