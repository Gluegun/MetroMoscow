package Metro.Util;

import Metro.Core.Line;
import Metro.Core.Station;
import Metro.StationIndex;
import TestAndOther.ColorUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.IOException;

public class MetroUtil {

    private static String url = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";


    public static StationIndex createIndex() throws IOException {

        StationIndex index = new StationIndex();

        Document doc = Jsoup.connect(url).maxBodySize(0).get();

        Elements tables = doc.select("table.standard.sortable tr");

        tables.remove(0);

        Line line;
        Station station;

        for (Element table : tables) {

            String stationName = table.child(1).text();
            String lineName = table.select("img[alt]").first().attr("alt");
            String lineNumber = table.child(0).child(0).select("span").text();
            String color;
            String hexValue = table.child(0).attr("style");

            if (!hexValue.isEmpty()) color = colorConverter(hexValue);
            else color = "gray";

            line = new Line(lineNumber, lineName, color);
            index.addLine(line);

            if (stationName.equals("Некрасовка")) break;
        }


        for (Element table : tables) {

            String stationName = table.child(1).text();
            String lineNumber = table.child(0).child(0).select("span").text();

            station = new Station(stationName, index.getLine(lineNumber));
            index.addStation(station);
            index.getLine(lineNumber).addStation(station);

            if (stationName.equals("Некрасовка")) break;

        }


        return index;
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
