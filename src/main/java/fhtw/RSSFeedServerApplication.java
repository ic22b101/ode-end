package fhtw;

/**
 * Hauptklasse für die RSS-Feed-Server-Anwendung.
 * <p>
 * Diese Anwendungsklasse startet einen RSSFeedTCPServer, der auf einem bestimmten Port läuft
 * und RSS-Feed-Daten von einer bestimmten URL zur Verfügung stellt.
 * </p>
 *
 * @see RSSFeedTCPServer
 * @since 1.0
 */
public class RSSFeedServerApplication {

    /**
     * Hauptmethode zum Starten des RSS-Feed-Servers.
     * <p>
     * Diese Methode erstellt eine Instanz des RSSFeedTCPServer und startet ihn auf dem
     * angegebenen Port. Der Server hört auf eingehende Client-Anfragen und liefert
     * RSS-Feed-Daten.
     * </p>
     *
     * @param args Kommandozeilenargumente, nicht verwendet.
     */
    public static void main(String[] args) {
        RSSFeedTCPServer server = new RSSFeedTCPServer("https://www.derstandard.at/rss");
        server.start(6666);
    }
}
