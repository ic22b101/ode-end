package fhtw;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.net.URL;

public class RssReader {

    public static void main(String[] args) {
        try {
            String rssUrl = "https://example.com/rss-feed"; // Ersetzen Sie dies durch den tatsächlichen APA RSS-Feed-URL
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new URL(rssUrl).openStream());

            NodeList itemList = doc.getElementsByTagName("item");
            for (int i = 0; i < itemList.getLength(); i++) {
                Element item = (Element) itemList.item(i);
                String title = item.getElementsByTagName("title").item(0).getTextContent();
                String link = item.getElementsByTagName("link").item(0).getTextContent();
                System.out.println("Title: " + title);
                System.out.println("Link: " + link);
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
