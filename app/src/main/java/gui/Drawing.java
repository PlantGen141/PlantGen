package gui;

import javax.swing.*;
import javax.swing.border.LineBorder;

import plantgen.CohortAgeReader;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * The Drawing class represents a drawing panel where users can draw with
 * different colors and brush sizes.
 * It also allows users to save the drawing and associate colors with ages.
 */

public class Drawing extends JPanel {
    private DrawingListener drawingListener;

    private Color currentColour = Color.WHITE;
    private int brushSize = 5;
    private BufferedImage canvas;
    private Graphics2D g2d;
    private Point prevPoint = null;
    private boolean isEditable = true;

    private int dimX;
    private int dimY;
    private JButton clearCanvasButton;

    private HashMap<Color, Integer> colourAgeMap = new HashMap<>(); // Map to store color and its associated age
    private JPanel colourAgePanel; // Panel to display color-age entries

    private HashMap<Color, JTextField> colourAgeFieldMap = new HashMap<>();
    private double[][] cohortAges; // Array to store cohort ages

    /**
     * Constructor to initialize the drawing panel with specified dimensions.
     * 
     * @param dimX Width of the drawing canvas
     * @param dimY Height of the drawing canvas
     */

    public Drawing(int dimX, int dimY) {
        this.dimX = dimX;
        this.dimY = dimY;

        setLayout(new BorderLayout());

        // Initialize the canvas and graphics context
        canvas = new BufferedImage(dimX, dimY, BufferedImage.TYPE_INT_ARGB);
        g2d = canvas.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g2d.setColor(currentColour);
        g2d.setStroke(new BasicStroke(brushSize));

        DrawingCanvas drawingPanel = new DrawingCanvas();
        drawingPanel.setPreferredSize(new Dimension(dimX, dimY));
        drawingPanel.setBorder(new LineBorder(Color.BLACK, 2));

        // Create a panel to wrap the canvas to ensure proper layout
        JPanel drawingWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        drawingWrapper.add(drawingPanel);

        add(drawingWrapper, BorderLayout.CENTER);

        // Create a control panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        add(controlPanel, BorderLayout.EAST);

        // Colour picker
        JButton pickColourButton = new JButton("Pick Colour");
        pickColourButton.addActionListener(e -> pickColour());
        controlPanel.add(pickColourButton);

        // Brush size selector
        JLabel brushSizeLabel = new JLabel("Brush Size:");
        JSlider brushSizeSlider = new JSlider(1, 30, brushSize);
        brushSizeSlider.addChangeListener(e -> {
            brushSize = brushSizeSlider.getValue();
            g2d.setStroke(new BasicStroke(brushSize)); // Update brush size
        });
        controlPanel.add(brushSizeLabel);
        controlPanel.add(brushSizeSlider);

        // Clear canvas button
        clearCanvasButton = new JButton("Clear Canvas");
        clearCanvasButton.addActionListener(e -> clearCanvas());
        controlPanel.add(clearCanvasButton);

        // Done button to finish drawing
        JButton doneButton = new JButton("Done");
        doneButton.addActionListener(e -> {
            finishDrawing();
            doneButton.setEnabled(false);
            pickColourButton.setEnabled(false);
        });
        controlPanel.add(doneButton);

        // Panel to display color-age entries
        colourAgePanel = new JPanel();
        colourAgePanel.setLayout(new BoxLayout(colourAgePanel, BoxLayout.Y_AXIS));
        drawingWrapper.add(colourAgePanel, BorderLayout.SOUTH);

    }

    /**
     * Opens a color picker dialog to select a color for drawing.
     * If the selected color is new, prompts the user to enter an age for the color.
     */
    private void pickColour() {
        Color selectedColour = JColorChooser.showDialog(this, "Choose Colour", currentColour);
        if (selectedColour != null) {
            currentColour = selectedColour;
            g2d.setColor(currentColour); // Update the graphics context colour

            // Prompt for age if the colour is being used for the first time
            if (!colourAgeMap.containsKey(selectedColour)) {
                promptAgeForColor(selectedColour);
            }
        }
    }

    /**
     * Prompts the user to enter an age for the specified color.
     * 
     * @param colour The color for which the age is to be entered
     */
    private void promptAgeForColor(Color colour) {

        if (colour == null) {
            return;
        }
        // Prompt the user to enter an age for the selected colour
        while (true) {
            String ageInput = JOptionPane.showInputDialog(this, "Enter age for " + colourToHex(colour) + ":",
                    "Set Colour Age", JOptionPane.PLAIN_MESSAGE);
            if (ageInput != null) {
                try {
                    int age = Integer.parseInt(ageInput);
                    if (age > 0) {
                        // Store the age and create a JTextField for it
                        colourAgeMap.put(colour, age);
                        createAgeEntry(colour, age);
                        break; // Break the loop once a valid age is entered
                    } else {
                        JOptionPane.showMessageDialog(this, "Please enter a positive number for age.", "Invalid Input",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid number for age.", "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // User cancelled input; keep prompting until they enter a valid age
                JOptionPane.showMessageDialog(this, "Age is required for drawing with this colour.", "Input Required",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
        System.out.println(colourAgeMap.toString());
    }

    /**
     * Creates an entry in the color-age panel for the specified color and age.
     * 
     * @param colour The color for which the entry is to be created
     * @param age    The age associated with the color
     */
    private void createAgeEntry(Color colour, int age) {
        JPanel colourAgeEntry = new JPanel(new FlowLayout(FlowLayout.LEFT));
        colourAgeEntry.setBackground(colour);

        // Determine text colour based on background colour
        Color textColour = (colour.getRed() * 0.299 + colour.getGreen() * 0.587 + colour.getBlue() * 0.114) > 186
                ? Color.BLACK
                : Color.WHITE;

        JLabel colourLabel = new JLabel("Selected Colour: " + colourToHex(colour));
        colourLabel.setForeground(textColour);
        colourAgeEntry.add(colourLabel);

        // Create JTextField with a preferred size matching the canvas width
        JTextField colourAge = new JTextField(String.valueOf(age), 20);
        colourAge.setPreferredSize(new Dimension(dimX - 20, 25)); // Adjusting for padding
        colourAge.setMaximumSize(new Dimension(dimX, 25)); // Ensure it doesn't exceed canvas width
        colourAge.setEditable(false); // Make the text field non-editable
        colourAgeFieldMap.put(colour, colourAge);

        colourAgeEntry.add(colourAge);
        colourAgePanel.add(colourAgeEntry);
        colourAgePanel.revalidate();
        colourAgePanel.repaint();
    }

    /**
     * Finishes the drawing process by validating age fields, saving the drawing,
     * and notifying the listener.
     */

    private void finishDrawing() {
        // Check if all age fields are filled and valid
        if (!areAllAgeFieldsValid()) {
            JOptionPane.showMessageDialog(this, "Please ensure all age fields are filled with positive numbers.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return; // Stop if validation fails
        }

        // Set drawing mode to non-editable
        isEditable = false;
        disableAgeFields(); // Disable all age fields to prevent further editing

        clearCanvasButton.setEnabled(false);

        saveDrawing();

        if (drawingListener != null) {
            drawingListener.onDrawingCompletion(cohortAges);
        }

        JOptionPane.showMessageDialog(this, "Drawing is now finished. You can no longer edit.", "Finished",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Checks if all age fields are valid (positive numbers).
     * 
     * @return true if all age fields are valid, false otherwise
     */

    private boolean areAllAgeFieldsValid() {
        for (JTextField colourAgeField : colourAgeFieldMap.values()) {
            try {
                int age = Integer.parseInt(colourAgeField.getText());
                if (age <= 0) {
                    return false; // Invalid if age is not positive
                }
            } catch (NumberFormatException e) {
                return false; // Invalid if not a number
            }
        }
        return true; // All fields are valid
    }

    /**
     * Disables all age fields to prevent further editing.
     */

    private void disableAgeFields() {
        for (JTextField ageField : colourAgeFieldMap.values()) {
            ageField.setEditable(false); // Disable editing
        }
    }

    /**
     * Saves the current drawing to a file.
     */
    private void saveDrawing() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Drawing");

        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG Images", "png"));
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getAbsolutePath().endsWith(".png")) {
                fileToSave = new File(fileToSave + ".png");
            }

            try {
                ImageIO.write(canvas, "png", fileToSave);
                JOptionPane.showMessageDialog(this, "Image saved successfully to " + fileToSave.getAbsolutePath(),
                        "Save Successful", JOptionPane.INFORMATION_MESSAGE);

                processDrawing(fileToSave.getAbsolutePath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving image: " + e.getMessage(), "Save Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    /**
     * Processes the saved drawing to extract cohort ages.
     * 
     * @param file The file path of the saved drawing
     */
    private void processDrawing(String file) {
        // Placeholder for processing logic
        System.out.println("Extracting Cohort Ages");
        CohortAgeReader cohortAgeReader = new CohortAgeReader(file, colourAgeMap);
        this.cohortAges = cohortAgeReader.convertColourToAge();
        System.out.println("Cohort Ages Extracted");
    }

    /**
     * Converts a Color object to its hexadecimal representation.
     * 
     * @param color The color to be converted
     * @return The hexadecimal representation of the color
     */

    private String colourToHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Sets the listener for drawing completion events.
     * 
     * @param drawingListener The listener to be set
     */

    public void setListener(DrawingListener drawingListener) {
        this.drawingListener = drawingListener;
    }

    /**
     * Inner class representing the drawing canvas where users can draw.
     */

    private class DrawingCanvas extends JPanel {
        public DrawingCanvas() {
            setPreferredSize(new Dimension(canvas.getWidth(), canvas.getHeight()));

            setBackground(Color.WHITE);
            // setBorder(new LineBorder(Color.BLACK, 2));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (isEditable) {
                        prevPoint = e.getPoint(); // Set the starting point for drawing
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    prevPoint = null; // Reset the point when mouse is released
                }
            });

            // Mouse motion listener for drawing
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (isEditable && prevPoint != null) {
                        Point currentPoint = e.getPoint();
                        g2d.drawLine(prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
                        prevPoint = currentPoint; // Update the previous point
                        repaint(); // Repaint the panel to show the new line
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(canvas, 0, 0, null); // Draw the canvas on the panel
        }
    }

    // Method to clear the canvas
    public void clearCanvas() {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        repaint();

        colourAgeMap.clear();
        colourAgeFieldMap.clear();

        colourAgePanel.removeAll();
        colourAgePanel.revalidate();
        colourAgePanel.repaint();
    }

    /**
     * Restarts the drawing process, allowing the user to draw again.
     */

    public void restartDrawing() {
        isEditable = true;

        // Enable the buttons
        for (Component component : ((JPanel) getComponent(1)).getComponents()) {
            component.setEnabled(true);
        }
        // wait until the user finishes drawing
        while (isEditable) {
            try {
                Thread.sleep(100); // Check every 100 milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}