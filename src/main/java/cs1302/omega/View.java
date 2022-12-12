package cs1302.omega;

/**
 * Class that defines the look of the image outputted by AstronomyAPI.
 */
public class View {
    String type;
    String orientation;

    /**
     * Constructor for the {@code View} object with preset values for the look-and-feel of the image
     * produced by AstronomyAPI.
     */
    public View() {
        type = "landscape-simple";
        orientation = "south-up";
    }


}
