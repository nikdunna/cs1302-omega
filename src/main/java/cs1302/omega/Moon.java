package cs1302.omega;

/**
 * Represents a Moon object to be parsed into JSON and delivered as the request to AstronomyAPI.
 */
public class Moon {
    String format;
    Style style;
    Observer observer;
    View view;

    /**
     * Constructs a {@code Moon} object.
     * @param latitude given by the PositionStack API.
     * @param longitude given by the PositionStack API.
     * @param date given by the user as their birthdate.
     */
    public Moon(double latitude, double longitude, String date) {
        format = "png";
        style = new Style();
        observer = new Observer(latitude, longitude, date);
        view = new View();
    }
}
