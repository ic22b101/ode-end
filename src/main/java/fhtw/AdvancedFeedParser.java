package fhtw;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Erweiterte Implementierung eines Feed-Parsers zum Abrufen und Speichern von RSS-Feeds.
 * <p>
 * Diese Klasse ermöglicht das Parsen von RSS-Feeds von einer gegebenen URL und
 * stellt Funktionen zum Anzeigen und Speichern dieser Feeds bereit.
 * Weitere Informationen zu RSS-Feeds finden Sie unter
 * <a href="https://www.w3schools.com/xml/xml_rss.asp">RSS-Tutorials auf W3Schools</a>.
 * </p>
 *
 * @see BaseFeedParser  Verweist auf die Basisklasse, von der diese Klasse erbt.
 * @since 1.0
 */
public class AdvancedFeedParser extends BaseFeedParser {

    /**
     * Konstruktor für AdvancedFeedParser.
     * <p>
     * Dieser Konstruktor initialisiert den Parser mit einer spezifizierten URL des RSS-Feeds.
     * </p>
     *
     * @param feedUrl Die URL des RSS-Feeds, der verarbeitet werden soll.
     */
    public AdvancedFeedParser(String feedUrl) {
        super(feedUrl);
    }

    /**
     * Holt die Feeds und bereitet sie für die Anzeige auf.
     * <p>
     * Diese Methode ruft RSS-Feed-Daten ab und formatiert sie in einem für die Anzeige
     * geeigneten Format. Falls beim Abrufen oder Parsen der Daten ein Fehler auftritt,
     * wird eine Benutzerdialogbox mit einer Fehlermeldung angezeigt.
     * </p>
     *
     * @return Eine formatierte Zeichenkette, die die RSS-Feed-Daten enthält.
     *         Gibt eine Fehlermeldung zurück, falls ein Fehler auftritt.
     */
    public String getFeedsForDisplay() {
        try {
            NodeList itemList = parse();
            if (itemList == null) return "Keine Feeds gefunden";

            StringBuilder displayContent = new StringBuilder();
            for (int i = 0; i < itemList.getLength(); i++) {
                Element item = (Element) itemList.item(i);
                String title = item.getElementsByTagName("title").item(0).getTextContent();
                String link = item.getElementsByTagName("link").item(0).getTextContent();
                String pubDate = item.getElementsByTagName("pubDate").item(0).getTextContent();

                displayContent.append("Title: ").append(title)
                        .append("\nLink: ").append(link)
                        .append("\nPublished: ").append(pubDate)
                        .append("\n\n");
            }
            return displayContent.toString();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Fehler beim Abrufen der RSS-Feeds.");
            return "Fehler beim Abrufen der Feeds";
        }
    }

    /**
     * Speichert die RSS-Feed-Daten in einer CSV-Datei.
     * <p>
     * Diese Methode speichert die aktuell geladenen RSS-Feed-Daten in einer CSV-Datei.
     * Bei einem IO-Fehler wird eine Benutzerdialogbox mit einer Fehlermeldung angezeigt.
     * </p>
     *
     */
    public void saveFeedsToCSV() {
        try {
            NodeList itemList = parse();
            if (itemList == null) throw new IOException("Keine Feeds zum Speichern");

            StringBuilder csvContent = new StringBuilder("Title,Link,Published Date\n");
            for (int i = 0; i < itemList.getLength(); i++) {
                Element item = (Element) itemList.item(i);
                String title = item.getElementsByTagName("title").item(0).getTextContent();
                String link = item.getElementsByTagName("link").item(0).getTextContent();
                String pubDate = item.getElementsByTagName("pubDate").item(0).getTextContent();

                csvContent.append("\"").append(title.replace("\"", "\"\"")).append("\",")
                        .append("\"").append(link).append("\",")
                        .append("\"").append(pubDate).append("\"\n");
            }
            try (PrintWriter writer = new PrintWriter(new FileWriter("rss_feeds.csv"))) {
                writer.print(csvContent);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Fehler beim Speichern der RSS-Feeds in CSV.");
        } catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }
    }
}