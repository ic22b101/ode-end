package fhtw;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.io.IOException;


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
    private DatePicker startDatePicker,endDatePicker;

    @FXML
    private void handleFilterButtonAction(ActionEvent event) {
        loadFeeds();
    }

    @FXML
    private ComboBox<String> intervalComboBox;

    @FXML
    private TextArea rssFeedTextArea;

    private Timer timer;

    private List<String> favorites = new ArrayList<>();

    @FXML
    private ListView<String> feedListView; // Angenommen, die Feeds werden als Strings angezeigt

    @FXML
    private ListView<String> favoritesListView; // Liste der Favoriten



    public void initialize() {
        configureFavoritesListView();
        loadFavorites(); // Lädt die Feeds beim Start

        feedListView.setCellFactory(lv -> new ListCell<String>() {
            private final Button addToFavoritesButton = new Button("Zu Favoriten hinzufügen");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item); // Anzeige des Feed-Eintrags
                    addToFavoritesButton.setOnAction(e -> addFavorite(item)); // Hinzufügen des Feed-Eintrags zu Favoriten
                    setGraphic(addToFavoritesButton); // Button neben dem Feed-Eintrag anzeigen
                }
            }
        });
        loadFavorites(); // Lädt die Favoriten beim Start
    }

    private void loadFeeds() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);

        if (startDate == null || endDate == null) {
            return; // Beenden, wenn keine Datumsangaben vorhanden sind
        }

        try {
            AdvancedFeedParser parser = new AdvancedFeedParser("https://www.derstandard.at/rss");
            NodeList feedItems = parser.parse();

            feedListView.getItems().clear(); // Liste vor dem Hinzufügen neuer Elemente leeren
            StringBuilder textAreaContent = new StringBuilder(); // Für rssFeedTextArea

            for (int i = 0; i < feedItems.getLength(); i++) {
                Element item = (Element) feedItems.item(i);
                String pubDateStr = item.getElementsByTagName("pubDate").item(0).getTextContent();
                LocalDateTime pubDate = LocalDateTime.parse(pubDateStr, formatter);

                if (!pubDate.toLocalDate().isBefore(startDate) && !pubDate.toLocalDate().isAfter(endDate)) {
                    String title = item.getElementsByTagName("title").item(0).getTextContent();
                    String link = item.getElementsByTagName("link").item(0).getTextContent();

                    // Formatieren des anzuzeigenden Texts
                    String feedDisplayText = "Titel: " + title + "\nLink: " + link + "\nVeröffentlicht: " + pubDateStr + "\n\n";

                    feedListView.getItems().add(feedDisplayText); // Hinzufügen zum ListView
                    textAreaContent.append(feedDisplayText); // Hinzufügen zum TextArea
                }
            }

            rssFeedTextArea.setText(textAreaContent.toString()); // Aktualisieren der rssFeedTextArea
        } catch (Exception e) {
            showAlert("Fehler beim Laden der Feeds: " + e.getMessage());
        }
    }


    private void loadFavorites() {
        try {
            List<String> savedFavorites = Files.readAllLines(Paths.get("favorites.txt"), StandardCharsets.UTF_8);
            favoritesListView.getItems().setAll(savedFavorites);
        } catch (IOException e) {
            showAlert("Fehler beim Laden der Favoriten: " + e.getMessage());
        }
    }

    public void addFavorite(String feed) {
        if (!favorites.contains(feed)) {
            favorites.add(feed);
            saveFavorites();
            updateFavoritesListView(); // Aktualisieren der Favoritenliste
        }
    }

    private void configureFavoritesListView() {
        favoritesListView.setCellFactory(lv -> new ListCell<String>() {
            private final Button removeButton = new Button("Entfernen");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    removeButton.setOnAction(e -> removeFavorite(item));
                    setGraphic(removeButton);
                }
            }
        });
    }

    private void removeFavorite(String favorite) {
        favorites.remove(favorite); // Entfernen des Favoriten aus der Liste
        saveFavorites(); // Speichern der aktualisierten Favoritenliste
        updateFavoritesListView(); // Aktualisieren der ListView
    }


    private void saveFavorites() {
        try {
            Files.write(Paths.get("favorites.txt"), favorites, StandardCharsets.UTF_8);
        } catch (IOException e) {
            showAlert("Fehler beim Speichern der Favoriten: " + e.getMessage());
        }
    }

    private void updateFavoritesListView() {
        favoritesListView.getItems().setAll(favorites); // Aktualisieren der ListView mit den aktuellen Favoriten
    }


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
                showAlert("Bitte wählen Sie ein Aktualisierungsintervall aus.");
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
    public static void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}