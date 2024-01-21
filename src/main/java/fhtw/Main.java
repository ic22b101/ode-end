package fhtw;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.Objects;

/**
 * Hauptklasse der JavaFX-Anwendung für den RSS-Reader.
 * <p>
 * Diese Klasse startet die JavaFX-Anwendung und lädt die Benutzeroberfläche aus einer FXML-Datei.
 * Bei Fehlern während des Ladens wird ein Dialogfenster mit einer Fehlermeldung angezeigt.
 * </p>
 * @see Application
 * @since 1.0
 */

public class Main extends Application {

    /**
     * Startet die primäre Stage (Fenster) der Anwendung.
     * <p>
     * Diese Methode lädt die FXML-Datei für die Benutzeroberfläche und zeigt das Hauptfenster an.
     * Bei Fehlern im Ladeprozess wird ein Alert-Dialog mit einer Fehlermeldung angezeigt.
     * </p>
     *
     * @param primaryStage Das primäre Stage-Objekt bereitgestellt von JavaFX.
     */

    @Override
    public void start(Stage primaryStage) {
        try {

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fhtw/rss_reader.fxml")));


            primaryStage.setTitle("RSS Reader App");


            primaryStage.setScene(new Scene(root, 1200, 800));


            primaryStage.show();
        } catch (Exception e) {

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
        launch(args);
    }
}
