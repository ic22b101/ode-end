package fhtw;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Controller-Klasse für die RSS-Reader JavaFX-Anwendung.
 * <p>
 * Diese Klasse handhabt die Interaktionen innerhalb der Benutzeroberfläche der RSS-Reader-Anwendung.
 * Sie ermöglicht das Einstellen des Aktualisierungsintervalls für die RSS-Feeds und deren Anzeige.
 * </p>
 *
 * @since 1.0
 */

public class RSSReaderController {

    @FXML
    private ComboBox<String> intervalComboBox;

    @FXML
    private TextArea rssFeedTextArea;

    private Timer timer;

    /**
     * Startet die regelmäßige Aktualisierung der RSS-Feeds.
     * <p>
     * Diese Methode liest das ausgewählte Aktualisierungsintervall, plant regelmäßige Aktualisierungen
     * und aktualisiert die Textbereichskomponente der Benutzeroberfläche mit den neuesten Feeds.
     * Bei Fehlern wird ein Alert-Dialog mit einer Fehlermeldung angezeigt.
     * </p>
     */

    public void startFeedUpdate() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();

        try {
            String selectedInterval = intervalComboBox.getValue();
            if (selectedInterval != null) {
                long interval = Long.parseLong(selectedInterval) * 60 * 60 * 1000;

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            AdvancedFeedParser parser = new AdvancedFeedParser("https://www.derstandard.at/rss");
                            String rssText = parser.getFeedsForDisplay();
                            Platform.runLater(() -> rssFeedTextArea.setText(rssText));
                            parser.saveFeedsToCSV();
                        } catch (Exception e) {
                            Platform.runLater(() -> showAlert("Fehler beim Aktualisieren der Feeds: " + e.getMessage()));
                        }
                    }
                }, 0, interval);
            } else {
                System.out.println("Bitte wählen Sie ein Aktualisierungsintervall aus.");
            }
        } catch (NumberFormatException e) {
            showAlert("Ungültiges Format für das Aktualisierungsintervall: " + e.getMessage());
        }
    }

    /**
     * Zeigt einen Alert-Dialog mit einer spezifischen Nachricht.
     * <p>
     * Diese Hilfsmethode wird verwendet, um Fehlermeldungen in einem Dialogfenster anzuzeigen.
     * </p>
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
}
