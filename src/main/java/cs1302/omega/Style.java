package cs1302.omega;

/**
 * Represents the style of the image to be outputted by the request of the {@code Moon} object.
 */
public class Style {
    String moonStyle;
    String backgroundStyle;
    String backgroundColor;
    String headingColor;
    String textColor;

    /**
     * Constructs a {@code Style} object with preset values for the desired image
     * given by AstronomyAPI.
     */
    public Style() {
        moonStyle = "default";
        backgroundStyle = "stars";
        backgroundColor = "black";
        headingColor = "white";
        textColor = "red";
    }
}
