/*
import java.awt.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class TestClass {

    static String url = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";

    public static void main(String[] args) throws IOException {

        Document doc = Jsoup.connect(url).maxBodySize(0).get();

        Elements tables = doc.select("table.standard.sortable tr");
        tables.remove(0);

        for (Element table : tables) {

            String station = table.child(1).text();
            String lineName = table.select("img[alt]").first().attr("alt");
            double lineNumber = Double.parseDouble(table.select("td").attr("data-sort-value"));
            System.out.println("Станция: " + station);
            System.out.println("Линия №:" + lineNumber + " " + lineName);
            String hexValue = table.child(0).attr("style");
            if (!hexValue.isEmpty()) {
                System.out.println("Цвет:" + colorConverter(hexValue));
            } else continue;
            if (station.equals("Некрасовка")) break;

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
*/
