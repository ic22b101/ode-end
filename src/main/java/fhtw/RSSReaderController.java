package fhtw;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


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
    private void handleFilterButtonAction() {
        loadFeeds();
    }

    @FXML
    private ComboBox<String> intervalComboBox;

    @FXML
    private TextArea rssFeedTextArea;

    private Timer timer;

    private final List<String> favorites = new ArrayList<>();

    @FXML
    private ListView<String> feedListView;

    @FXML
    private ListView<String> favoritesListView;


    /**
     * Initialisiert die Benutzeroberfläche und lädt gespeicherte Favoriten.
     * <p>
     * Diese Methode konfiguriert die ListView für Favoriten und lädt bestehende Favoriten aus einer Datei.
     * Sie richtet auch die ListView für Feed-Anzeigen ein und fügt Hinzufügen-Buttons zu jeder Zeile hinzu.
     * </p>
     */
    public void initialize() {
        configureFavoritesListView();

        feedListView.setCellFactory(lv -> new ListCell<>() {
            private final Button addToFavoritesButton = new Button("Zu Favoriten hinzufügen");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    addToFavoritesButton.setOnAction(e -> addFavorite(item));
                    setGraphic(addToFavoritesButton);
                }
            }
        });

    }

    /**
     * Lädt Feeds basierend auf ausgewählten Datumsangaben.
     * <p>
     * Diese Methode filtert die Feeds nach dem ausgewählten Datumsbereich und aktualisiert die Anzeige
     * entsprechend. Wenn keine Daten ausgewählt sind, werden keine Aktionen ausgeführt.
     * </p>
     */
    private void loadFeeds() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);

        if (startDate == null || endDate == null) {
            showAlert("Bitte wählen Sie ein Start- und Enddatum aus.");
            return;
        }

        if (endDate.isBefore(startDate)) {
            showAlert("Das Enddatum muss nach dem Startdatum liegen.");
            return;
        }

        try {
            AdvancedFeedParser parser = new AdvancedFeedParser("https://www.derstandard.at/rss");
            NodeList feedItems = parser.parse();

            feedListView.getItems().clear();
            StringBuilder textAreaContent = new StringBuilder();

            for (int i = 0; i < feedItems.getLength(); i++) {
                Element item = (Element) feedItems.item(i);
                String pubDateStr = item.getElementsByTagName("pubDate").item(0).getTextContent();
                LocalDateTime pubDate = LocalDateTime.parse(pubDateStr, formatter);

                if (!pubDate.toLocalDate().isBefore(startDate) && !pubDate.toLocalDate().isAfter(endDate)) {
                    String title = item.getElementsByTagName("title").item(0).getTextContent();
                    String link = item.getElementsByTagName("link").item(0).getTextContent();

                    String feedDisplayText = "Titel: " + title + "\nLink: " + link + "\nVeröffentlicht: " + pubDateStr + "\n\n";

                    feedListView.getItems().add(feedDisplayText);
                    textAreaContent.append(feedDisplayText);
                }
            }

            rssFeedTextArea.setText(textAreaContent.toString());
        } catch (Exception e) {
            showAlert("Fehler beim Laden der Feeds: " + e.getMessage());
        }
    }



    /**
     * Fügt einen ausgewählten Feed zu den Favoriten hinzu.
     * <p>
     * Diese Methode fügt einen ausgewählten Feed zur Favoritenliste hinzu und speichert diese Liste
     * in einer Datei. Sie aktualisiert auch die Anzeige der Favoritenliste.
     * </p>
     *
     * @param feed Der Feed, der zu den Favoriten hinzugefügt werden soll.
     */
    public void addFavorite(String feed) {
        if (!favorites.contains(feed)) {
            favorites.add(feed);
            saveFavorites();
            updateFavoritesListView();
        }
    }

    /**
     * Konfiguriert die ListView für die Anzeige von Favoriten mit einem Entfernen-Button.
     * <p>
     * Diese Methode setzt eine CellFactory für die favoritesListView, um für jeden
     * Favoriten einen "Entfernen"-Button bereitzustellen. Beim Klick auf diesen Button
     * wird der entsprechende Favorit aus der Liste entfernt.
     * </p>
     */
    private void configureFavoritesListView() {
        favoritesListView.setCellFactory(lv -> new ListCell<>() {
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

    /**
     * Entfernt einen Favoriten aus der Liste und aktualisiert die Anzeige.
     * <p>
     * Diese Methode entfernt einen angegebenen Favoriten aus der internen Liste der Favoriten,
     * speichert die aktualisierte Liste in einer Datei und aktualisiert die ListView-Anzeige.
     * </p>
     *
     * @param favorite Der zu entfernende Favorit.
     */
    private void removeFavorite(String favorite) {
        favorites.remove(favorite);
        saveFavorites();
        updateFavoritesListView();
    }


    /**
     * Speichert die aktuelle Liste der Favoriten in einer Datei.
     * <p>
     * Diese Methode speichert die aktuelle Liste der Favoriten in einer Datei namens "favorites.txt".
     * Bei Fehlern beim Speichern wird ein Alert-Dialog mit einer Fehlermeldung angezeigt.
     * </p>
     */
    private void saveFavorites() {
        try {
            Files.write(Paths.get("favorites.txt"), favorites, StandardCharsets.UTF_8);
        } catch (IOException e) {
            showAlert("Fehler beim Speichern der Favoriten: " + e.getMessage());
        }
    }

    /**
     * Aktualisiert die ListView, die die Favoriten anzeigt.
     * <p>
     * Diese Methode aktualisiert die favoritesListView mit den aktuellen Favoriten aus der internen Liste.
     * Sie wird aufgerufen, nachdem Änderungen an der Favoritenliste vorgenommen wurden, um die Anzeige zu synchronisieren.
     * </p>
     */
    private void updateFavoritesListView() {
        favoritesListView.getItems().setAll(favorites);
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
                            Platform.runLater(() -> {
                                rssFeedTextArea.setText(rssText);
                                if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
                                    updateFeedListView(rssText);
                                } else {
                                    loadFeeds();
                                }
                            });
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


    private void updateFeedListView(String rssText) {
        List<String> feeds = new ArrayList<>();

        String[] lines = rssText.split("\n");

        for (String line : lines) {
            if (line.contains("Title:") && line.contains("Link:") && line.contains("Published:")) {
                feeds.add(line);
            }
        }

        feedListView.getItems().setAll(feeds);
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