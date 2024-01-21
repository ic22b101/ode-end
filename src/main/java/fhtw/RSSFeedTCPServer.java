package fhtw;

import java.io.*;
import java.net.*;

/**
 * Server-Klasse für die Bereitstellung von RSS-Feeds über eine TCP-Verbindung.
 * <p>
 * Diese Klasse implementiert einen Server, der auf einem bestimmten Port läuft und
 * auf eingehende Client-Anfragen wartet. Für jede erfolgreiche Verbindung wird
 * der aktuelle RSS-Feed an den Client gesendet.
 * </p>
 * @since 1.0
 */

public class RSSFeedTCPServer {
    private ServerSocket serverSocket;
    private final AdvancedFeedParser feedParser;

    /**
     * Konstruktor für RSSFeedTCPServer.
     * <p>
     * Initialisiert den Server mit einem Feed-Parser, der auf einer spezifischen URL basiert.
     * </p>
     * @param feedUrl Die URL des RSS-Feeds, der vom Server bereitgestellt wird.
     */

    public RSSFeedTCPServer(String feedUrl) {
        this.feedParser = new AdvancedFeedParser(feedUrl);
    }

    /**
     * Startet den Server auf einem bestimmten Port.
     * <p>
     * Diese Methode startet den Server und wartet auf eingehende TCP-Verbindungen. Bei jeder
     * erfolgreichen Verbindung werden die RSS-Feed-Daten an den Client gesendet.
     * </p>
     * @param port Der Port, auf dem der Server lauschen soll.
     */

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("RSS Feed Server is running on port " + port);

            while (!serverSocket.isClosed()) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    String feedData = feedParser.getFeedsForDisplay();
                    out.println(feedData);
                } catch (IOException e) {
                    if (!serverSocket.isClosed()) {
                        System.err.println("Fehler bei der Kommunikation mit dem Client: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Server konnte nicht auf Port " + port + " gestartet werden: " + e.getMessage());
        } finally {
            stop();
        }
    }

    /**
     * Stoppt den Server und schließt den ServerSocket.
     * <p>
     * Diese Methode schließt den ServerSocket und beendet damit den Server.
     * </p>
     */
    public void stop() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Fehler beim Schließen des Servers: " + e.getMessage());
            }
        }
    }
}
