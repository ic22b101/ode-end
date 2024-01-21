package fhtw;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.net.URL;
import java.net.MalformedURLException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <p>Die Klasse Frequency dient zum periodischen Abrufen und Anzeigen von RSS-Feed-Daten.
 * Weitere Informationen über RSS-Feeds finden Sie unter
 * <a href="https://www.w3schools.com/xml/xml_rss.asp">RSS Feeds W3Schools</a>.</p>
 *
 * @see <a href="https://www.derstandard.at/rss">RSS URL</a>
 * @since 1.0
 */
public class Frequency {

    private static final String RSS_URL = "https://www.derstandard.at/rss";
    private static final long UPDATE_INTERVAL = 60 * 60 * 1000;

    /**
     * Hauptmethode zum Initialisieren und Starten des TimerTasks zur RSS-Feed-Aktualisierung.
     * <p>
     * Diese Methode initialisiert einen Timer, der den RssFeedTask in regelmäßigen Abständen ausführt.
     * </p>
     * @param args Kommandozeilenargumente (werden nicht verwendet).
     */
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new RssFeedTask(), 0, UPDATE_INTERVAL);
    }

    /**
     * TimerTask zur Durchführung der RSS-Feed-Aktualisierung.
     * <p>
     * Diese innere Klasse ruft den RSS-Feed von der angegebenen URL ab und gibt die Titel und Links
     * der Feed-Elemente auf der Konsole aus. Sie behandelt auch eventuelle Fehler bei der Abfrage
     * und beim Parsen der RSS-Feeds.
     * </p>
     */
    static class RssFeedTask extends TimerTask {
        @Override
        public void run() {
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(new URL(RSS_URL).openStream());

                NodeList itemList = doc.getElementsByTagName("item");
                for (int i = 0; i < itemList.getLength(); i++) {
                    Element item = (Element) itemList.item(i);
                    String title = item.getElementsByTagName("title").item(0).getTextContent();
                    String link = item.getElementsByTagName("link").item(0).getTextContent();
                    System.out.println("Title: " + title);
                    System.out.println("Link: " + link);
                    System.out.println();
                }
            } catch (MalformedURLException e) {
                System.err.println("Fehlerhafte URL: " + e.getMessage());
            } catch (ParserConfigurationException e) {
                System.err.println("Fehler bei der Konfiguration des XML-Parsers: " + e.getMessage());
            } catch (SAXException e) {
                System.err.println("Fehler beim Parsen des XML-Dokuments: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("IO-Fehler beim Abrufen des RSS-Feeds: " + e.getMessage());
            }
        }
    }
}