/*
package TestAndOther;

import Metro.Core.Line;
import Metro.Core.Station;
import Metro.StationIndex;
import Metro.Util.StationIndexUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

class TestClass {

    private static String url = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";

    public static void main(String[] args) throws IOException {

        StationIndex index = new StationIndex();

        Document doc = Jsoup.connect(url).maxBodySize(0).get();

        Elements table1 = doc.select("table").get(3).select("tr");
        Elements table2 = doc.select("table").get(4).select("tr");
        table2.remove(0);
        Elements table3 = doc.select("table").get(5).select("tr");
        table3.remove(0);

        addLineToIndex(table1, index);
        addLineToIndex(table2, index);
        addLineToIndex(table3, index);

        addStationToLine(table1, index);
        addStationToLine(table2, index);
        addStationToLine(table3, index);

        parseConnections(table1, index);
        parseConnections(table2, index);
        parseConnections(table3, index);


        for (Station station : index.getLine("06").getStations()) {
            Set<Station> connectedStations = index.getConnectedStations(station);
            connectedStations.add(station);
            System.out.println(connectedStations);
            System.out.println();
        }
    }

    public static void addLineToIndex(Elements tables, StationIndex index) {
        tables.remove(0);
        Line line;

        for (Element table : tables) {

            String stationName = table.child(1).child(0).select("a[href]").text();
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

            String stationName = table.child(1).child(0).select("a[href]").text();
            if (stationName.equals("Название станции")) continue;
            String lineNumber = table.child(0).child(0).select("span").text();
            station = new Station(stationName, index.getLine(lineNumber));
            index.addStation(station);
            index.getLine(lineNumber).addStation(station);
        }
    }

    public static void parseConnections(Elements tables, StationIndex index) {

        for (Element table : tables) {

            Station transitedStation = null;
            Line transitLine = null;


            List<Station> connectedStations = new ArrayList<>();


            String stationName = table.child(1).child(0).select("a[href]").text();
            String lineNumber = table.child(0).child(0).select("span").text();
            Station station = index.getStation(stationName, lineNumber);
            Line line = station.getLine();

            String transitLineNumber = table.child(3).text();

            if (transitLineNumber.contains("8А11")) {
                transitLineNumber = transitLineNumber.replaceAll("8А11", "8А 11");
            }

            if (transitLineNumber.isEmpty()) {
                continue;

            } else if (transitLineNumber.contains(" ")) {

                String[] lineNumbers = transitLineNumber.split(" ");

                for (String lineNumberFromArray : lineNumbers) {
                    transitLineNumber = lineNumberFromArray;

                    transitLine = index.getLine(transitLineNumber);

                    Elements stationsToBeTransited = table.child(3).select("span[title]");
                    for (Element stationToBeTransited : stationsToBeTransited) {
                        String transitedStationName = stationToBeTransited.select("span").attr("title");
                        transitedStationName = transitedStationName.replaceAll("Переход на станцию ", "");

                        List<Station> stationsFromCurrentLine = transitLine.getStations();
                        for (Station stationFromCurrentLine : stationsFromCurrentLine) {

                            if (transitedStationName.contains(stationFromCurrentLine.getName())) {
                                transitedStation = stationFromCurrentLine;
                            }
                        }
                    }
                    connectedStations.add(transitedStation);
                    connectedStations.add(station);
                }


            } else {
                transitLine = index.getLine(transitLineNumber);
                Elements stationsToBeTransited = table.child(3).select("span[title]");
                for (Element stationToBeTransited : stationsToBeTransited) {
                    String transitedStationName = stationToBeTransited.select("span").attr("title");
                    transitedStationName = transitedStationName.replaceAll("Переход на станцию ", "");

                    List<Station> stationsFromCurrentLine = transitLine.getStations();
                    for (Station stationFromCurrentLine : stationsFromCurrentLine) {

                        if (transitedStationName.contains(stationFromCurrentLine.getName())) {
                            transitedStation = stationFromCurrentLine;
                        }
                    }
                }
                connectedStations.add(transitedStation);
                connectedStations.add(station);
            }
            index.addConnection(connectedStations);
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

    private static String extractStation(String transitionStation, String cutTransitionStation) {
        String stationName;
        int firstIndex = transitionStation.indexOf("станцию") + 8;

        if (transitionStation.contains("Московского центрального кольца")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Московского центрального кольца"));
            return stationName.trim();
        } else if (transitionStation.contains("Кольцевой линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Кольцевой линии"));
            return stationName.trim();
        } else if (transitionStation.contains("Калужско-Рижской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Калужско-Рижской линии"));
            return stationName.trim();
        } else if (transitionStation.contains("Таганско-Краснопресненской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Таганско-Краснопресненской линии"));
            return stationName.trim();
        } else if (transitionStation.contains("Люблинско-Дмитровской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Люблинско-Дмитровской линии"));
            return stationName.trim();
        } else if (transitionStation.contains("Замоскворецкой линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Замоскворецкой линии"));
            if (stationName.contains("Театральная")) {
                stationName = "Театральная";
            }
            return stationName.trim();
        } else if (transitionStation.contains("Арбатско-Покровской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Арбатско-Покровской линии"));
            return stationName.trim();
        } else if (transitionStation.contains("Филёвской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Филёвской линии"));
            if (stationName.contains("Александровский сад")) {
                stationName = "Александровский сад";
            }
            return stationName.trim();
        } else if (transitionStation.contains("Серпуховско-Тимирязевской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Серпуховско-Тимирязевской линии"));
            return stationName.trim();
        } else if (transitionStation.contains("Большой кольцевой линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Большой кольцевой линии"));
            return stationName.trim();
        } else if (transitionStation.contains("Калининской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Калининской линии"));
            return stationName.trim();
        } else if (transitionStation.contains("Каховской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Каховской линии"));
            return stationName.trim();
        } else if (transitionStation.contains("Солнцевской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Солнцевской линии"));
            return stationName.trim();
        } else if (transitionStation.contains("Бутовской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Бутовской линии"));
            return stationName.trim();
        } else if (transitionStation.contains("Московского монорельса")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Московского монорельса"));
            return stationName.trim();
        } else if (transitionStation.contains("Некрасовской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Некрасовской линии"));
            return stationName.trim();
        } else if (transitionStation.contains("Сокольнической линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Сокольнической линии"));
            return stationName.trim();
        } else return null;
    }
}*/
