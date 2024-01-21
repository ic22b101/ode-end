package fhtw;

import java.io.*;
import java.net.*;

/**
 * Client-Klasse für die Kommunikation mit einem RSS-Feed-Server über TCP.
 * <p>
 * Diese Klasse ermöglicht es, eine TCP-Verbindung zu einem Server aufzubauen, um die neuesten
 * RSS-Feeds abzurufen und die Verbindung anschließend wieder zu schließen.
 * </p>
 * @since 1.0
 */
public class RSSFeedTCPClient {
    private Socket clientSocket;
    private BufferedReader in;

    /**
     * Stellt eine Verbindung zum RSS-Feed-Server her.
     * <p>
     * Diese Methode baut eine Verbindung zu einem Server unter der angegebenen IP-Adresse
     * und Portnummer auf.
     * </p>
     * @param ip Die IP-Adresse des Servers.
     * @param port Der Port des Servers.
     * @throws IOException Wenn beim Aufbau der Verbindung ein Fehler auftritt.
     */

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    /**
     * Ruft die neuesten RSS-Feeds vom Server ab.
     * <p>
     * Diese Methode liest die Daten, die vom Server gesendet werden, und gibt sie als formatierten
     * String zurück.
     * </p>
     * @return Ein String, der die empfangenen RSS-Feed-Daten enthält.
     * @throws IOException Wenn beim Lesen der Daten ein Fehler auftritt.
     */
    public String getLatestFeeds() throws IOException {
        StringBuilder feeds = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            feeds.append(line).append("\n");
        }
        return feeds.toString();
    }

    /**
     * Schließt die Verbindung zum Server.
     * <p>
     * Diese Methode schließt den Inputstream und den Socket, um die Verbindung zum Server
     * ordnungsgemäß zu beenden.
     * </p>
     * @throws IOException Wenn beim Schließen der Verbindung ein Fehler auftritt.
     */
    public void stopConnection() throws IOException {
        if (in != null) in.close();
        if (clientSocket != null) clientSocket.close();
    }
}
