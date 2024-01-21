package fhtw;

import java.io.*;
import java.net.*;

/**
 * Server-Klasse für die Bereitstellung von RSS-Feeds über TCP.
 * <p>
 * Diese Klasse implementiert einen Server, der auf einem bestimmten Port läuft und
 * Client-Anfragen annimmt. Für jeden Client wird ein neuer Thread gestartet, um
 * RSS-Feeds zu senden.
 * </p>
 *
 * @see AdvancedFeedParser
 * @since 1.0
 */
public class RSSFeedTCPServer {
    private ServerSocket serverSocket;
    private final AdvancedFeedParser feedParser;

    /**
     * Konstruktor für RSSFeedTCPServer.
     * <p>
     * Initialisiert den Server mit einem RSS-Feed-Parser, der auf einer spezifischen URL basiert.
     * </p>
     *
     * @param feedUrl Die URL des RSS-Feeds, der vom Server bereitgestellt wird.
     */

    public RSSFeedTCPServer(String feedUrl) {
        this.feedParser = new AdvancedFeedParser(feedUrl);
    }

    /**
     * Startet den Server auf einem bestimmten Port.
     * <p>
     * Der Server lauscht auf eingehende Verbindungen und erstellt für jede Verbindung
     * einen neuen ClientHandler-Thread.
     * </p>
     *
     * @param port Der Port, auf dem der Server lauschen soll.
     */
    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("RSS Feed Server is running on port " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    new ClientHandler(clientSocket, feedParser).start();
                } catch (IOException e) {
                    System.err.println("Fehler bei der Kommunikation mit dem Client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server konnte nicht auf Port " + port + " gestartet werden: " + e.getMessage());
        }
    }

    /**
     * Handler-Klasse für Client-Verbindungen.
     * <p>
     * Diese Klasse behandelt die Client-Anfragen in einem separaten Thread und sendet
     * die RSS-Feed-Daten.
     * </p>
     */
    private static class ClientHandler extends Thread {
        private final Socket clientSocket;
        private PrintWriter out;
        private final AdvancedFeedParser feedParser;

        public ClientHandler(Socket socket, AdvancedFeedParser feedParser) {
            this.clientSocket = socket;
            this.feedParser = feedParser;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                String feedData = feedParser.getFeedsForDisplay();
                out.println(feedData);
            } catch (IOException e) {
                System.err.println("Fehler beim Senden der Daten: " + e.getMessage());
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Fehler beim Schließen der Ressourcen: " + e.getMessage());
                }
            }
        }
    }
}