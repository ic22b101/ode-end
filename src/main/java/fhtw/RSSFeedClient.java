package fhtw;

import java.io.IOException;

public class RSSFeedClient {
    private RSSFeedTCPClient client;
    private String ip;
    private int port;

    public RSSFeedClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.client = new RSSFeedTCPClient();
    }

    public String fetchFeeds() throws IOException {
        client.startConnection(ip, port);
        String feedData = client.getLatestFeeds();
        client.stopConnection();
        return feedData;
    }
}
