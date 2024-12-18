package plantgen;

/**
 * Plant class which holds the placed plant's attributes
 */
public class Plant {
    private String species;
    private Coordinate position;

    // Calculated attributes
    private int age;
    private double height;
    private double canopyRadius;
    private boolean isCanopy = false;

    /**
     * Constructor to initialize the Plant object with species and position
     * 
     * @param species  The species of the plant
     * @param position The position of the plant
     */
    public Plant(String species, Coordinate position) {
        this.species = species;
        this.position = position;
    }

    /**
     * Method to load the attributes of the plant
     * 
     * @param age
     * @param height
     * @param canopyRadius
     */
    public void loadAttributes(int age, double height, double canopyRadius) {
        this.age = age;
        this.height = height;
        this.canopyRadius = canopyRadius;
    }

    /**
     * Method to return the species of the plant
     * 
     * @return species of plant
     */
    public String getSpecies() {
        return this.species;
    }

    /**
     * Method to return the age of the plant
     * 
     * @return age of plant
     */
    public int getAge() {
        return this.age;
    }

    /**
     * Method to get the height of the plant
     * 
     * @return height of plant
     */
    public double getHeight() {
        return this.height;
    }

    /**
     * Method to return the canopy radius of the plant
     * 
     * @return canopy radius
     */
    public double getCanopyRadius() {
        return this.canopyRadius;
    }

    /**
     * Method to return the coordinates of the plant
     * 
     * @return coordinates of the plant
     */
    public Coordinate getPosition() {
        return this.position;
    }

    /**
     * Method to return a string representation of the plant
     */
    public String toString() {
        return "Type: " + this.getSpecies() + "\nPosition: " + this.getPosition() + "\nAge: " + this.getAge() +
                "\nHeight: " + this.getHeight() + "\nCanopy Radius: " + this.getCanopyRadius();
    }

    /**
     * Method to change whether or not the plant is a canopy or undergrowth plant
     */
    public void setIsCanopy() {
        this.isCanopy = true;
    }

    /**
     * Method to return whether or not a plant is a canopy plant
     * 
     * @return boolean of whether or not the plant is a canopy plant
     */
    public boolean getCanopy() {
        return this.isCanopy;
    }

    /**
     * Method to set the canopy radius of the plant
     * 
     * @param canopyRadius the canopy radius of the plant
     */
    public void setCanopyRadius(double canopyRadius) {
        this.canopyRadius = canopyRadius;
    }
}
