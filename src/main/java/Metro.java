import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Metro {

    private static final List<List<String>> listOfStations = new ArrayList<>();
    private final List<Line> listOfLines = new LinkedList<>();
    private final JSONObject objectMetro = new JSONObject();

    /** Создаем JSON файл согласно JSON стандарту */
    public void createJsonFile(Document htmlFile) throws IOException {
        Elements listLine = htmlFile.select("span[data-line]");
        for (Element e : listLine) {
            String numberLines = e.attr("data-line");
            String nameLines = e.text();
            listOfLines.add(new Line(numberLines, nameLines));
        }

        /** Список станций, в которых номера без символов(без букв) */
        List<String> listsOfStations;
        for (int i = 1; i <= 15; i++) {
            if (i != 13) {
                String numbers = String.valueOf(i);
                listsOfStations = htmlFile
                        .select("div[data-line=\"" + numbers + "\"]")
                        .select("span[class=\"name\"]")
                        .eachText();
                listOfStations.add(listsOfStations);
            }
        }

        /** Список станций, в которых номера содержат символы */
        List<String> symbols = Arrays.asList("11A", "D1", "D2");
        for (String s : symbols) {
            listsOfStations = htmlFile
                    .select("div[data-line=\"" + s + "\"]")
                    .select("span[class=\"name\"]")
                    .eachText();
            listOfStations.add(listsOfStations);
        }

        JSONObject objectStations = new JSONObject();
        JSONObject objectLines = new JSONObject();
        for (int i = 0, j = 0; j <= 15; i++, j++) {
            if (i == 12) {
                objectStations.put(i, listOfStations.get(j - 1));
            } else if (j == 14 || j == 15) {
                objectStations.put(i, listOfStations.get(j - 2));
            } else {
                objectStations.put(i + 1, listOfStations.get(j));
            }
        }

        /** Добавляем наши станции в JSONObject, которые содержат символы */
        objectStations.put("11A", listOfStations.get(14));
        objectStations.put("D1", listOfStations.get(15));
        objectStations.put("D2", listOfStations.get(16));

        /** Добавляем наши станции и линии в JSONObject, тем самым делаем массив в массиве */
        objectMetro.put("stations", objectStations);
        objectLines.put("lines", listOfLines);
        objectMetro.putAll(objectLines);

        /** Создаем JSON файл и красиво записываем в него наши линии и станции,
          * используя new DefaultPrettyPrinter() */
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(new File("data/mscmetro.json"), objectMetro);
    }

    /** Печатаем количество станций на каждой линии */
    public void printCountOfStationsAtLines() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < listOfLines.size(); i++) {
            if (i == 11) {
                s.append(listOfLines.get(i)).append(": ")
                        .append(listOfStations.get(i + 3).size()).append(" станции;\n");
            } else if (i == 12 || i == 13 || i == 14){
                s.append(listOfLines.get(i)).append(": ")
                        .append(listOfStations.get(i - 1).size()).append(" станций;\n");
            } else {
                s.append(listOfLines.get(i)).append(": ")
                        .append(listOfStations.get(i).size()).append(" станций;\n");
            }
        }
        System.out.println("\nКоличество станций на каждой линии:\n");
        System.out.println(s.toString());
    }

    /** Печатаем станции и линии метро в консоли согласно стандарту JSON */
    public void printStationsAndLines() {
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(objectMetro));
    }
}