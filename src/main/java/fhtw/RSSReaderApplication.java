package fhtw;


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
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fhtw/rss_reader.fxml")));

            primaryStage.setTitle("RSS Reader App");

            Scene scene = new Scene(root, 1200, 800);

            primaryStage.setScene(scene);

            primaryStage.show();
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
        Thread rssThread = new Thread(RSSReaderApplication::startRSSApplication);
        rssThread.start();
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
    public static void startRSSApplication() {
        Thread serverThread = new Thread(() -> {
            RSSFeedTCPServer server = new RSSFeedTCPServer("https://www.derstandard.at/rss");
            server.start(6666);
        });
        serverThread.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.err.println("Warten unterbrochen: " + e.getMessage());
        }

        RSSFeedTCPClient client = new RSSFeedTCPClient();
        try {
            client.startConnection("127.0.0.1", 6666);
            String feedData = client.getLatestFeeds();
        } catch (IOException e) {
            System.err.println("Fehler beim Starten des Clients: " + e.getMessage());
        } finally {
            try {
                client.stopConnection();
            } catch (IOException e) {
                System.err.println("Fehler beim Schließen der Verbindung: " + e.getMessage());
            }
        }
    }
}
