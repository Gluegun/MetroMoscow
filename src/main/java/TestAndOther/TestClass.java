package TestAndOther;

import Metro.Core.Line;
import Metro.Core.Station;
import Metro.StationIndex;
import Metro.Util.StationIndexUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.TreeSet;

class TestClass {
    private static StationIndex index;
    private static String url = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";

    public static void main(String[] args) throws IOException {

        index = new StationIndex();

        Document doc = Jsoup.connect(url).maxBodySize(0).get();

        Elements table1 = doc.select("table").get(3).select("tr");
        Elements table2 = doc.select("table").get(4).select("tr");
        Elements table3 = doc.select("table").get(5).select("tr");

        addLineToIndex(table1, index);
        addLineToIndex(table2, index);
        addLineToIndex(table3, index);

        addStationToLine(table1, index);
        addStationToLine(table2, index);
        addStationToLine(table3, index);

        parseConnections(table1);
        parseConnections(table2);
        parseConnections(table3);

        /*for (Element table : table1) {

            String transitionStation = "";
            String stationName = table.child(1).text();
            if (stationName.equals("Название станции")) continue;
            String lineName = table.select("img[alt]").first().attr("alt");
            String lineNumber = table.child(0).child(0).select("span").text();

            Elements transitStationsElements = table.select("td").select("span").select("a").select("img[alt]");

            System.out.println("\tСтанция: " + stationName + "\n\tЛиния: " + lineName + "\n\tПереход: " + transitStationsElements + "\n==========");


            for (int i = 0; i < transitStationsElements.size(); i++) {

                if (!transitStationsElements.get(i).toString().isEmpty()) {

                    String s = transitStationsElements.get(i).select("img").attr("alt");
                    System.out.println("\tСтанция: " + stationName + "\n\tЛиния: " + lineName + "\n\tПереход: "
                            + s + "\n==========");
                    if (s.contains("Переход") || s.contains("Кросс")) {

//                        System.out.println("\t\nСтанция: " + stationName + "\n\tЛиния: " + lineName
//                                + "\n\t\tПереход: " + s);
                        transitionStation = s;
                    }
//                    else System.out.println("\t\nСтанция: " + stationName + "\n\tЛиния: " + lineName
//                            + "\nСтанция без переходов");
                }
                if (!transitionStation.isEmpty()) {
                    //System.out.println(transitionStation);
                    String cutTransitionStation = transitionStation.substring(transitionStation.indexOf("станцию") + 8);
//                    System.out.println("Переход: " + cutTransitionStation);
                    //extractStation(transitionStation, cutTransitionStation);
                }
            }

        }*/
    }


    public static void addLineToIndex(Elements tables, StationIndex index) {
        tables.remove(0);
        Line line;

        for (Element table : tables) {

            String stationName = table.child(1).text();
            if (stationName.equals("Название станции")) continue;
            String lineName = table.select("img[alt]").first().attr("alt");
            String lineNumber = table.child(0).child(0).select("span").text();
            String hexValue = table.child(0).attr("style");
            String color;
            if (!hexValue.isEmpty()) color = colorConverter(hexValue);
            else color = "Gray";
            line = new Line(lineNumber, lineName, color);
            index.addLine(line);
        }
    }

    public static void addStationToLine(Elements tables, StationIndex index) {

        Station station;

        for (Element table : tables) {

            String stationName = table.child(1).text();
            if (stationName.equals("Название станции")) continue;
            String lineNumber = table.child(0).child(0).select("span").text();
            station = new Station(stationName, index.getLine(lineNumber));
            index.addStation(station);
            index.getLine(lineNumber).addStation(station);
        }
    }

    public static void parseConnections(Elements tables) {

        for (Element table : tables) {
            String stationName = table.child(1).text();
            String lineName;
            String lineNumber;
            if (stationName.equals("Название станции")) continue;

            String lineToBeChanged = table.child(3).text();

            if (lineToBeChanged.isEmpty()) {
                System.out.println("Стация: " + stationName + " \nПереходов нет\n");
            } else {
                if (lineToBeChanged.contains("8А11")) {
                    lineToBeChanged = lineToBeChanged.replace("8А11", "8А 11");
                }
                String[] stations = lineToBeChanged.split("\\s");

                for (String station : stations) {
                    lineName = index.getLine(station).getName();
                    lineNumber = index.getLine(station).getNumber();
                    System.out.println("=============\nСтанция: " + stationName + "\nПереход на линию: " + lineNumber + " " + lineName + "\n=============\n");
                }
            }

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

    private static void extractStation(String transitionStation, String cutTransitionStation) {
        Station station = null;
        String stationName;
        int firstIndex = transitionStation.indexOf("станцию") + 8;

        if (transitionStation.contains("Московского центрального кольца")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Московского центрального кольца"));
            System.out.println("Переход: " + stationName);

        } else if (transitionStation.contains("Кольцевой линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Кольцевой линии"));
            System.out.println("Переход: " + stationName);

        } else if (transitionStation.contains("Калужско-Рижской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Калужско-Рижской линии"));
            System.out.println("Переход: " + stationName);

        } else if (transitionStation.contains("Таганско-Краснопресненской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Таганско-Краснопресненской линии"));
            System.out.println("Переход: " + stationName);

        } else if (transitionStation.contains("Люблинско-Дмитровской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Люблинско-Дмитровской линии"));
            System.out.println("Переход: " + stationName);

        } else if (transitionStation.contains("Замоскворецкой линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Замоскворецкой линии"));
            System.out.println("Переход: " + stationName);

        } else if (transitionStation.contains("Арбатско-Покровской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Арбатско-Покровской линии"));
            System.out.println("Переход: " + stationName);

        } else if (transitionStation.contains("Филёвской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Филёвской линии"));
            System.out.println("Переход: " + stationName);

        } else if (transitionStation.contains("Серпуховско-Тимирязевской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Серпуховско-Тимирязевской линии"));
            System.out.println("Переход: " + stationName);

        } else if (transitionStation.contains("Большой кольцевой линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Большой кольцевой линии"));
            System.out.println("Переход: " + stationName);

        } else if (transitionStation.contains("Калининской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Калининской линии"));
            System.out.println("Переход: " + stationName);

        } else if (transitionStation.contains("Каховской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Каховской линии"));
            System.out.println("Переход: " + stationName);

        } else if (transitionStation.contains("Солнцевской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Солнцевской линии"));
            System.out.println("Переход: " + stationName);

        } else if (transitionStation.contains("Бутовской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Бутовской линии"));
            System.out.println("Переход: " + stationName);

        } else if (transitionStation.contains("Московского монорельса")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Московского монорельса"));
            System.out.println("Переход: " + stationName);

        } else if (transitionStation.contains("Некрасовской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Некрасовской линии"));
            System.out.println("Переход: " + stationName);

        } else System.out.println("Переход: " + cutTransitionStation);
    }

}