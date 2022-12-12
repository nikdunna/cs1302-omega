package cs1302.omega;

public class Moon {
    String format;
    Style style;
    Observer observer;
    View view;

    public Moon(double latitude, double longitude, String date) {
        format = "png";
        style = new Style();
        observer = new Observer(latitude, longitude, date);
        view = new View();
    }
}
