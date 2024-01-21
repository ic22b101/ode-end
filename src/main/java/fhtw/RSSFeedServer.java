package fhtw;

/**
 * Die Klasse RSSFeedServer dient zum Starten und Stoppen eines Serverdienstes, der RSS-Feeds bereitstellt.
 * <p>
 * Diese Klasse kapselt die Funktionalitäten des RSSFeedTCPServers und ermöglicht es,
 * den Server auf einem bestimmten Port zu starten und zu stoppen. Sie wird verwendet,
 * um eine kontinuierliche Bereitstellung von RSS-Feed-Daten über eine TCP-Verbindung zu ermöglichen.
 * </p>
 *
 * @since 1.0
 */
public class RSSFeedServer {
    private final RSSFeedTCPServer server;
    private final int port;

    /**
     * Konstruktor, der einen neuen RSSFeedServer auf einem bestimmten Port initialisiert.
     *
     * @param port Der Port, auf dem der Server lauschen soll.
     */
    public RSSFeedServer(int port) {
        this.port = port;
        this.server = new RSSFeedTCPServer("https://www.derstandard.at/rss");
    }

    /**
     * Startet den RSS-Feed-Server in einem neuen Thread.
     * <p>
     * Diese Methode initialisiert und startet den RSS-Feed-Server, der auf dem zuvor
     * im Konstruktor festgelegten Port auf eingehende Verbindungen wartet.
     * </p>
     */
    public void startServer() {
        new Thread(() -> server.start(port)).start();
    }

    /**
     * Stoppt den RSS-Feed-Server.
     * <p>
     * Diese Methode sendet ein Signal an den Server, um den laufenden Dienst zu beenden.
     * Dadurch wird der Server nicht mehr auf neue Verbindungen reagieren und bestehende
     * Verbindungen ordnungsgemäß schließen.
     * </p>
     */
    public void stopServer() {
        server.stop();
    }
}
