package fhtw;

import java.io.IOException;

/**
 * Die Klasse RSSFeedClient dient zum Abrufen von RSS-Feeds von einem Server über eine TCP-Verbindung.
 * <p>
 * Diese Klasse verwendet einen RSSFeedTCPClient, um eine Verbindung zu einem Server aufzubauen,
 * die neuesten RSS-Feeds abzurufen und dann die Verbindung zu schließen. Sie ist verantwortlich für
 * die Verwaltung der Netzwerkinteraktionen beim Abrufen der Feeds.
 * </p>
 *
 * @since 1.0
 */

public class RSSFeedClient {
    private final RSSFeedTCPClient client;
    private final String ip;
    private final int port;


    /**
     * Konstruktor, der einen neuen RSSFeedClient initialisiert.
     * <p>
     * Dieser Konstruktor erstellt einen RSSFeedClient, der sich mit einem Server mit
     * einer bestimmten IP-Adresse und einem Port verbindet.
     * </p>
     *
     * @param ip   Die IP-Adresse des Servers, zu dem eine Verbindung hergestellt werden soll.
     * @param port Der Port des Servers, zu dem eine Verbindung hergestellt werden soll.
     */
    public RSSFeedClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.client = new RSSFeedTCPClient();
    }

    /**
     * Ruft die neuesten RSS-Feeds vom Server ab.
     * <p>
     * Diese Methode stellt eine Verbindung zum Server her, fordert die neuesten RSS-Feeds an
     * und schließt anschließend die Verbindung. Bei Fehlern während des Verbindungsprozesses
     * oder der Datenabfrage wird eine IOException ausgelöst.
     * </p>
     *
     * @return Eine Zeichenkette, die die abgerufenen RSS-Feed-Daten enthält.
     * @throws IOException Wenn beim Aufbau der Verbindung oder beim Abrufen der Daten ein Fehler auftritt.
     */
    public String fetchFeeds() throws IOException {
        client.startConnection(ip, port);
        String feedData = client.getLatestFeeds();
        client.stopConnection();
        return feedData;
    }
}
