package fhtw;


import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
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

    private RSSFeedServer feedServer;
    private RSSFeedClient feedClient;
    private RSSReaderController controller;

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fhtw/rss_reader.fxml"));
            Parent root = loader.load();

            // Zugriff auf den Controller
            controller = loader.getController();

            primaryStage.setTitle("RSS Reader App");

            Scene scene = new Scene(root, 1200, 800);

            primaryStage.setScene(scene);

            primaryStage.show();
            startRSSServices();
        } catch (Exception e) {
            RSSReaderController.showAlert("Ein Fehler ist aufgetreten: " + e.getMessage());
        }
    }



    /**
     * Hauptmethode zum Starten der Anwendung.
     * <p>
     * Diese Methode ist der Einstiegspunkt für die JavaFX-Anwendung. Sie startet
     * den Prozess zur Initialisierung der Benutzeroberfläche und der Hintergrunddienste.
     * </p>
     *
     * @param args Kommandozeilenargumente, die an die Anwendung übergeben werden.
     */
    public static void main(String[] args) {
        launch(args);
    }


    /**
     * Startet den RSS-Feed-Server und den RSS-Feed-Client.
     * <p>
     * Diese Methode initialisiert und startet sowohl den RSS-Feed-Server als auch den -Client.
     * Der Server wird in einem separaten Thread gestartet und auf eine bestimmte Portnummer
     * eingestellt, um Client-Anfragen entgegenzunehmen. Der Client verbindet sich mit dem Server
     * und ruft die neuesten RSS-Feeds ab.
     * </p>
     */
    private void startRSSServices() {
        feedServer = new RSSFeedServer(6666);
        feedServer.startServer();

        feedClient = new RSSFeedClient("127.0.0.1", 6666);
        new Thread(() -> {
            try {
                String feedData = feedClient.fetchFeeds();
                Platform.runLater(() -> {
                    controller.updateFeedListView(feedData);
                });
            } catch (IOException e) {
                Platform.runLater(() -> RSSReaderController.showAlert("Fehler beim Abrufen der Feeds: " + e.getMessage()));
            }
        }).start();
    }
}
