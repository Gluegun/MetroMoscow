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


        ArrayList<Set<Station>> listOfSets = new ArrayList<>();
        Set<Station> connectedStations;

        for (Line line : index.getAllLines()) {
            System.out.println(line.getName());
            for (Station station : line.getStations()) {
                connectedStations = index.getConnectedStations(station);
                if (!connectedStations.isEmpty()) {
                    connectedStations.add(station);
                }
                else continue;
                System.out.println(connectedStations);
                listOfSets.add(connectedStations);
            }
            System.out.println("==================\n");
        }

        System.out.println(listOfSets.size());

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

}
*/
