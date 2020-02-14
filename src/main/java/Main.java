import Metro.Line;
import Metro.Station;
import Metro.StationIndex;
import TestAndOther.ColorUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class Main {

    static String url = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";

    public static void main(String[] args) throws IOException {

        Document doc = Jsoup.connect(url).maxBodySize(0).get();

        StationIndex index = new StationIndex();

        Elements tables = doc.select("table.standard.sortable tr");

        tables.remove(0);

        for (Element table : tables) {

            String station = table.child(1).text();
            String lineName = table.select("img[alt]").first().attr("alt");
            String lineNumber = table.child(0).child(0).select("span").text();
            String color;
            String hexValue = table.child(0).attr("style");
            System.out.println("Станция: " + station);
            System.out.println("Линия №" + lineNumber + " " + lineName);


            if (!hexValue.isEmpty()) color = colorConverter(hexValue);
            else continue;
            index.addStation(new Station(station, new Line(lineNumber, lineName, color)));
            index.addLine(new Line(lineNumber, lineName, color));
            if (station.equals("Некрасовка")) break;

        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {

            Path dirPath = Paths.get("data");
            Path filePath = Paths.get("data/mosmetro.json");
            if (!Files.exists(dirPath)) Files.createDirectory(dirPath);
            if (!Files.exists(filePath)) Files.createFile(filePath);
            Files.write(filePath, gson.toJson(index).getBytes(), StandardOpenOption.WRITE);


        } catch (
                Exception ex) {
            ex.printStackTrace();
        }

    }

    public static String colorConverter(String hexValue) {

        String cutHexValue = hexValue.substring(hexValue.indexOf("#"), hexValue.indexOf("#") + 7);
        ColorUtils colorUtils = new ColorUtils();
        Color decode = Color.decode(cutHexValue);
        int r = decode.getRed();
        int g = decode.getGreen();
        int b = decode.getBlue();
        String color = colorUtils.getColorNameFromRgb(r, g, b);
        return color;
    }
}
