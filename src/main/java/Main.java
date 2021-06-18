import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        Document htmlFile = Jsoup.connect("https://www.moscowmap.ru/metro.html#lines")
                .maxBodySize(0).get();
        Metro mscMetro = new Metro();
        mscMetro.createJsonFile(htmlFile);
        mscMetro.printCountOfStationsAtLines();
        mscMetro.printStationsAndLines();
    }
}


