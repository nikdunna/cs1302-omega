package cs1302.omega;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Base64;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Shows the user a picture of the moon/moon-phase on the user's birthday and where they were born.
 */
public class OmegaApp extends Application {
    /**
     * Class representing the objects of the return JSON from PositionStack API.
     */
    static class PosStack {
        public Data[] data;

        /**
         * Class representing the data object of the PositionStack return JSON.
         */
        static class Data {
            public double latitude;
            public double longitude;
        } // Data
    } // PosStack

    /**
     * Class representing the objects of the return JSON from AstronomyAPI.
     */
    static class Astro {
        public Results data;

        /**
         * Class representing the data/results object of the AstronomyAPI return JSON.
         */
        static class Results {
            String imageUrl;
        } // Results
    } // Astro

    Image preset;
    ImageView astroView;

    TextField bdayPrompt;
    TextField locationPrompt;
    Button go;

    HBox prompts;
    VBox root;
    Scene scene;

    private static final String POSITION_STACK_ENDPOINT = "http://api.positionstack.com/v1/forward";
    private static final String ASTRO_APP_ID = "48849e67-39a9-4db4-b6d7-ab2e0bdd901f";
    private static final String ASTRO_SECRET = "d9c990db6aebe47958551e826b5ff0f411"
                        + "18ccd7fcbfda7d85b20119bbdcc9e0efe60604"
                        + "77620af84e04b6c7d29bfc91fee909f62392242941049a2652"
                        + "d92405dacf23cd5913ae05d4e717f3c0f612efa5d"
                        + "b930cddfa02d732879ba68a72f50feaae79e0f448430d060fec91f884a3f4";
    private static final String ASTRO_PRE_ENCODE = ASTRO_APP_ID + ":" + ASTRO_SECRET;
    private static final String ASTRO_HASH =
        new String(Base64.getEncoder().encodeToString(ASTRO_PRE_ENCODE.getBytes()));

    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    /**
     * Constructs an {@code OmegaApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public OmegaApp() {
        preset = new Image("file:resources/space.jpg");
        astroView = new ImageView(preset);

        bdayPrompt = new TextField("Birthday YYYY-MM-DD");
        locationPrompt = new TextField("Birthplace (City)");
        go = new Button("Take me to my moon!");

        prompts = new HBox(bdayPrompt, locationPrompt, go);
        root = new VBox(prompts, astroView);
        scene = new Scene(root);
    } // OmegaApp

    /** {@inheritDoc} */
    @Override
    public void init() {
        HBox.setHgrow(bdayPrompt, Priority.ALWAYS);
        HBox.setHgrow(locationPrompt, Priority.ALWAYS);
        HBox.setHgrow(go, Priority.ALWAYS);
        HBox.setHgrow(astroView, Priority.ALWAYS);
        astroView.setFitWidth(preset.getWidth());
        astroView.setFitHeight(380);

        go.setOnAction((event) -> {
            try {
                getMoon();
            } catch (Exception e) {
                errorPopUp(e);
            }
        });
    } // init

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        stage.setTitle("OmegaApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();
    } // start

    /**
     * Helper method that calls the PositionStack API to Geocode the coordinates of the user's
     * inputted city.
     * @return double array of size 2 that contains the latitude and longitude of the user's city.
     */
    public double[] getCoords() {
        double[] coords = new double[2];
        String city = locationPrompt.getText();
        String rawJson = "";
        PosStack parsedJson = new PosStack();
        URI uri = URI.create(POSITION_STACK_ENDPOINT
                  + "?access_key=632f77608bd75ddaefb8a9601f373ff5"
                  + "&query=" + city
                  + "&limit=1"
                  + "&fields=results.latitude,results.longitude");
        try {
            rawJson = get(uri);
            parsedJson = GSON.fromJson(rawJson, OmegaApp.PosStack.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        coords[0] = parsedJson.data[0].latitude;
        coords[1] = parsedJson.data[0].longitude;
        return coords;
    } // getCoords

    /**
     * Uses the coordinates given by the PositionStack API and the date given by the user to
     * request a generated image of the moon on that night and place through AstronomyAPI. Sets
     * the ImageView in the application to the generated image.
     */
    public void getMoon() {
        double[] coords = new double[2];
        try {
            coords = getCoords();
        } catch (Exception e) {
            errorPopUp(e);
        }
        String rawJson = "";
        Moon moon = new Moon(coords[0], coords[1], bdayPrompt.getText());
        Astro parsedJson = new Astro();

        String moonJson = GSON.toJson(moon);
        System.out.println(moonJson);

        HttpClient moonClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.astronomyapi.com/api/v2/studio/moon-phase"))
            .header("Authorization", "Basic " + ASTRO_HASH)
            .POST(HttpRequest.BodyPublishers.ofString(moonJson))
            .build();

        try {
            HttpResponse<String> response = moonClient.send(request, BodyHandlers.ofString());
            System.out.println("STATUS CODE " + response.statusCode());
            System.out.println(response.body());
            rawJson = response.body();
            parsedJson = GSON.fromJson(rawJson, OmegaApp.Astro.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorPopUp(e);
        }
        astroView.setImage(new Image(parsedJson.data.imageUrl));
    }

    /**
     * Helper method that makes an HTTP GET request to the specified URI.
     * @param uri the uri to make the call to.
     * @return string representation of the resulting JSON.
     */
    public static String get(URI uri) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(uri)
            .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        return response.body();
    }

    /**
     * Produces an error message based on the exception passed in.
     * @param cause the exception to be diagnosed.
     */
    public static void errorPopUp(Throwable cause) {
        TextArea text = new TextArea(cause.toString());
        text.setEditable(false);
        Alert alert = new Alert(AlertType.ERROR);
        alert.getDialogPane().setContent(text);
        alert.setResizable(true);
        alert.showAndWait();
    }
} // OmegaApp
