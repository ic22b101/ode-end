package fhtw;

public class RSSFeedServer {
    private RSSFeedTCPServer server;
    private final int port;

    public RSSFeedServer(int port) {
        this.port = port;
        this.server = new RSSFeedTCPServer("https://www.derstandard.at/rss");
    }

    public void startServer() {
        new Thread(() -> server.start(port)).start();
    }

    public void stopServer() {
        server.stop();
    }
}
