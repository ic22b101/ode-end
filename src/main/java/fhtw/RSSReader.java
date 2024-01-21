package fhtw;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.net.URL;
import java.net.MalformedURLException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.IOException;

/**
 * Hauptklasse zum Auslesen und Anzeigen von RSS-Feed-Daten.
 * <p>
 * Diese Klasse dient zum Auslesen eines RSS-Feeds von einer angegebenen URL und
 * zum Ausgeben der Feed-Elemente wie Titel, Link und Veröffentlichungsdatum auf der Konsole.
 * </p>
 *
 * @since 1.0
 */
public class RSSReader {

    /**
     * Hauptmethode zum Abrufen und Anzeigen von RSS-Feed-Daten.
     * <p>
     * Diese Methode lädt RSS-Feed-Daten von einer angegebenen URL und gibt die
     * Details der einzelnen Feed-Elemente wie Titel, Link und Veröffentlichungsdatum auf der Konsole aus.
     * Bei Auftreten von Fehlern werden entsprechende Fehlermeldungen ausgegeben.
     * </p>
     *
     * @param args Kommandozeilenargumente, nicht verwendet.
     */
    public static void main(String[] args) {
        try {
            String rssUrl = "https://www.derstandard.at/rss";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new URL(rssUrl).openStream());

            NodeList itemList = doc.getElementsByTagName("item");
            for (int i = 0; i < itemList.getLength(); i++) {
                Element item = (Element) itemList.item(i);
                String title = item.getElementsByTagName("title").item(0).getTextContent();
                String link = item.getElementsByTagName("link").item(0).getTextContent();
                String pubDate = item.getElementsByTagName("pubDate").item(0).getTextContent();
                System.out.println("Title: " + title);
                System.out.println("Link: " + link);
                System.out.println("Published: " + pubDate);
                System.out.println();
            }
        } catch (MalformedURLException e) {
            System.err.println("Fehlerhafte URL: " + e.getMessage());
        } catch (ParserConfigurationException e) {
            System.err.println("Fehler bei der Konfiguration des XML-Parsers: " + e.getMessage());
        } catch (SAXException e) {
            System.err.println("Fehler beim Parsen des XML-Dokuments: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO-Fehler beim Lesen des RSS-Feeds: " + e.getMessage());
        }
    }
}