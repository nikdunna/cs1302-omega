package cs1302.omega;

/**
 * Represents the location and place of the observer of the {@code Moon} object.
 */
public class Observer {
    double latitude;
    double longitude;
    String date = "";

    /**
     * Constructs an {@code Observer} object.
     * @param latitude given by the PositionStack API.
     * @param longitude given by the PositionStack API.
     * @param date given by the user as their birthdate.
     */
    public Observer(double latitude, double longitude, String date) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
    }
}
