package fhtw;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.net.URL;
import java.net.MalformedURLException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.IOException;

/**
 * Basisklasse für das Parsen von RSS-Feeds.
 * <p>
 * Diese Klasse bietet grundlegende Funktionalitäten zum Laden und Parsen von RSS-Feeds
 * von einer angegebenen URL. Sie dient als Grundlage für erweiterte Parser-Klassen.
 * Weitere Informationen zu XML und RSS finden Sie auf der
 * <a href="https://www.w3schools.com/xml/xml_rss.asp">W3Schools RSS-Seite</a>.
 * </p>
 * @since 1.0
 */
public class BaseFeedParser {
    protected String feedUrl;

    /**
     * Konstruktor für BaseFeedParser.
     * <p>
     * Initialisiert einen neuen Feed-Parser mit der angegebenen Feed-URL.
     * </p>
     *
     * @param feedUrl Die URL des RSS-Feeds, der verarbeitet werden soll.
     */
    public BaseFeedParser(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    /**
     * Parst den RSS-Feed und gibt die resultierenden Elemente zurück.
     * <p>
     * Diese Methode versucht, den RSS-Feed von der vorgegebenen URL zu laden und zu parsen.
     * Bei Auftreten von Fehlern während des Lade- oder Parse-Vorgangs werden entsprechende
     * Fehlermeldungen ausgegeben und null zurückgegeben.
     * </p>
     *
     * @return Eine NodeList, die die Elemente des RSS-Feeds enthält, oder null bei Fehlern.
     * @throws MalformedURLException Wenn das URL-Format ungültig ist.
     * @throws ParserConfigurationException Wenn ein Fehler bei der Konfiguration des Parsers auftritt.
     * @throws SAXException Wenn ein Fehler beim Parsen des XML-Dokuments auftritt.
     * @throws IOException Wenn ein IO-Fehler beim Lesen des RSS-Feeds auftritt.
     */


        public NodeList parse() throws MalformedURLException, ParserConfigurationException, SAXException, IOException {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new URL(feedUrl).openStream());
            return doc.getElementsByTagName("item");
        }

    }

