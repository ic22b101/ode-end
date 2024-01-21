package fhtw;

import java.io.*;
import java.net.*;

/**
 * Client-Klasse für die Kommunikation mit dem RSS-Feed-Server über TCP.
 * <p>
 * Diese Klasse stellt Methoden zum Herstellen einer Verbindung mit dem Server,
 * zum Empfangen der neuesten RSS-Feeds und zum Schließen der Verbindung bereit.
 * </p>
 *
 * @see RSSFeedTCPServer
 * @since 1.0
 */
public class RSSFeedTCPClient {
    private Socket clientSocket;
    private BufferedReader in;

    /**
     * Stellt eine Verbindung mit dem Server her.
     * <p>
     * Versucht, eine TCP-Verbindung zum angegebenen Server und Port herzustellen.
     * Gibt bei Erfolg {@code true} und bei Misserfolg {@code false} zurück.
     * </p>
     *
     * @param ip   Die IP-Adresse des Servers.
     * @param port Der Port des Servers.
     */

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Fehler beim Herstellen einer Verbindung: " + e.getMessage());
        }
    }

    /**
     * Ruft die neuesten RSS-Feeds vom Server ab.
     * <p>
     * Liest die Daten vom Server und gibt sie als formatierten String zurück.
     * Bei einem Fehler wird eine entsprechende Fehlermeldung zurückgegeben.
     * </p>
     *
     * @return Die empfangenen RSS-Feed-Daten als String oder eine Fehlermeldung.
     */

    public String getLatestFeeds() {
        try {
            StringBuilder feeds = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                feeds.append(line).append("\n");
            }
            return feeds.toString();
        } catch (IOException e) {
            System.err.println("Fehler beim Lesen der Feeds: " + e.getMessage());
            return "Fehler beim Lesen der Feeds";
        }
    }

    /**
     * Schließt die Verbindung zum Server.
     * <p>
     * Schließt den Inputstream und den Socket. Bei einem Fehler wird eine Fehlermeldung
     * auf der Standardfehlerausgabe ausgegeben.
     * </p>
     */

    public void stopConnection() {
        try {
            if (in != null) {
                in.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Schließen der Verbindung: " + e.getMessage());
        }
    }
}
