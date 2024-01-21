package fhtw;

/**
 * Hauptklasse für die RSS-Feed-Client-Anwendung.
 * <p>
 * Diese Anwendungsklasse erstellt eine Instanz des RSSFeedTCPClient,
 * stellt eine Verbindung zum Server her und ruft die neuesten RSS-Feeds ab.
 * Nach dem Abrufen der Feeds wird die Verbindung zum Server geschlossen.
 * </p>
 *
 * @see RSSFeedTCPClient
 * @since 1.0
 */
public class RSSFeedClientApplication {

    /**
     * Hauptmethode zum Starten des RSS-Feed-Clients.
     * <p>
     * Diese Methode initiiert den Verbindungsaufbau zum RSS-Feed-Server,
     * fordert die aktuellen Feeds an und gibt diese auf der Konsole aus.
     * Schließlich wird die Verbindung zum Server geschlossen.
     * </p>
     *
     * @param args Kommandozeilenargumente, nicht verwendet.
     */
    public static void main(String[] args) {
        RSSFeedTCPClient client = new RSSFeedTCPClient();
        client.startConnection("127.0.0.1", 6666);
        String feedData = client.getLatestFeeds();
        System.out.println(feedData);
        client.stopConnection();
    }
}
