package fhtw;

//import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.Objects;

/**
 * Hauptklasse für die RSS-Feed-Anwendung.
 * <p>
 * Diese Klasse dient als Einstiegspunkt für die Anwendung und enthält die
 * Methoden zum Starten des RSS-Feed-Servers, RSS-Feed-Clients und zur Darstellung
 * der Benutzeroberfläche für den RSS-Reader.
 * </p>
 *
 * @since 1.0
 */

public class RSSReaderApplication extends javafx.application.Application {

    /**
     * Startet die primäre Stage (Fenster) der Anwendung.
     * <p>
     * Diese Methode lädt die FXML-Datei für die Benutzeroberfläche des RSS-Readers
     * und zeigt das Hauptfenster an. Bei Fehlern im Ladeprozess wird ein Alert-Dialog
     * mit einer Fehlermeldung angezeigt.
     * </p>
     *
     * @param primaryStage Das primäre Stage-Objekt bereitgestellt von JavaFX.
     */

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load UI for RSS Reader
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fhtw/rss_reader.fxml")));

            // Set the window title
            primaryStage.setTitle("RSS Reader App");

            // Create a scene and set its dimensions
            Scene scene = new Scene(root, 1200, 800);

            // Set the scene for the primary stage
            primaryStage.setScene(scene);

            // Show the primary stage
            primaryStage.show();
        } catch (Exception e) {
            // Show an error dialog if an exception occurs
            showAlert("Ein Fehler ist aufgetreten: " + e.getMessage());
        }
    }

    /**
     * Zeigt einen Alert-Dialog mit einer spezifischen Nachricht.
     *
     * @param message Die anzuzeigende Nachricht.
     */

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Hauptmethode zum Starten der Anwendung.
     *
     * @param args Kommandozeilenargumente, die an die Anwendung übergeben werden.
     */

    public static void main(String[] args) {
        // Create a new thread to start RSS application
        Thread rssThread = new Thread(RSSReaderApplication::startRSSApplication); //method reference

        // Start the RSS application concurrently
        rssThread.start();

        // Start the JavaFX application
        launch(args);
    }

    /**
     * Startet den RSS-Feed-Server und den RSS-Feed-Client.
     */

    public static void startRSSApplication() {
        // Start RSS Feed Server
        RSSFeedTCPServer server = new RSSFeedTCPServer("https://www.derstandard.at/rss");
        server.start(6666);

        // Start RSS Feed Client
        RSSFeedTCPClient client = new RSSFeedTCPClient();
        client.startConnection("127.0.0.1", 6666);
        String feedData = client.getLatestFeeds();

        // Update JavaFX UI from the JavaFX application thread
        Platform.runLater(() -> System.out.println(feedData));

        client.stopConnection();
    }

}
