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
import java.util.Set;
import java.util.TreeSet;

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


        for (Station station : index.getLine("01").getStations()) {
            System.out.println("Станция: " + station.getName());
            Set<Station> connectedStations = index.getConnectedStations(station);
            System.out.println("Переход: " + connectedStations);
        }


//        for (Element table : table1) {
//
//
//            String stationName = table.child(1).text();
//            if (stationName.equals("Название станции")) continue;
//
//            String lineNumber = table.child(0).child(0).select("span").text();
//            Line line = index.getLine(lineNumber);
//
//
//            System.out.println("Станция: " + stationName + " Линия: " + line.getNumber() + " " + line.getName());
//            String numberOfTransitLine = table.child(3).text();
//            if (numberOfTransitLine.isEmpty()) {
//                System.out.println("Перехода нет");
//                System.out.println();
//                continue;
//            }
//
//            Line transitLine;
//
//            Elements transfer = table.select("td:nth-of-type(4)").select("span[title]");
//
////            for (int i = 1; i < transfer.size(); i++) {
////
////                String transitionStation = transfer.get(i - 1).select("span").attr("title");
////                String cutTransitionStation = transitionStation.substring(transitionStation.indexOf("станцию") + 8);
////                String transitStationName = extractStation(transitionStation, cutTransitionStation);
////
////
////                if (numberOfTransitLine.contains("8А11")) {
////                    numberOfTransitLine = numberOfTransitLine.replaceAll("8А11", "8А 11");
////                }
////
////                if (numberOfTransitLine.contains(" ")) {
////                    String[] lineNumbersArray = numberOfTransitLine.split(" ");
////
////                    /*for (int i = 1; i < lineNumbersArray.length; i++) {
////
////                        transitLine = index.getLine(lineNumbersArray[i]);
////
////                        if (lineNumbersArray[i].equals(lineNumbersArray[i - 1])) {
////                            continue;
////                        }
////                        System.out.println("Переход на станцию: " + transitStationName + " Линия: " + transitLine.getNumber() + " " + transitLine.getName());
////
////
////                    }
////                    System.out.println();*/
////
////                    for (String lineNumberString : lineNumbersArray) {
////
////                        transitLine = index.getLine(lineNumberString);
////
////                        System.out.println("TEST TEST ============================= TEST TEST");
////                        System.out.println(transfer.get(i).select("span").attr("title"));
////                        System.out.println(transfer.get(i-1).select("span").attr("title"));
////                        System.out.println(transitStationName);
////                        System.out.println("TEST TEST ============================= TEST TEST");
////
////                        if (transfer.get(i).select("span").attr("title").equals(transfer.get(i - 1).select("span").attr("title")))
////                            continue;
////                        else {
////                            System.out.println("Переход на станцию: " + transitStationName + " Линия: " + transitLine.getNumber() + " " + transitLine.getName());
////                        }
////                    }
////                    System.out.println();
////
////                } else {
////                    transitLine = index.getLine(numberOfTransitLine);
////                    System.out.println("Переход на станцию: " + transitStationName + " Линия: " + transitLine.getNumber() + " " + transitLine.getName());
////                    System.out.println();
////                }
////            }
//            ArrayList<String> stations = new ArrayList<>();
//
//            for (Element singleTransfer : transfer) {
//
//                String transitionStation = singleTransfer.select("span").attr("title");
//                String cutTransitionStation = transitionStation.substring(transitionStation.indexOf("станцию") + 8);
//                String transitStationName = extractStation(transitionStation, cutTransitionStation);
//                System.out.println("Переход на станцию: " + transitStationName);
//
//                //stations = new ArrayList<>();
//                stations.add(transitStationName);
//
//                if (numberOfTransitLine.contains("8А11")) {
//                    numberOfTransitLine = numberOfTransitLine.replaceAll("8А11", "8А 11");
//                }
//
//
//                /*else if (numberOfTransitLine.isEmpty()) {
//                    System.out.println("Перехода нет\n");
//                    continue;
//                }*/
//
//                if (numberOfTransitLine.contains(" ")) {
//                    String[] lineNumbersArray = numberOfTransitLine.split(" ");
//
//                    /*for (int i = 1; i < lineNumbersArray.length; i++) {
//
//                        transitLine = index.getLine(lineNumbersArray[i]);
//
//                        if ()) {
//                            continue;
//                        }
//                        System.out.println("Переход на станцию: " + transitStationName + " Линия: " + transitLine.getNumber() + " " + transitLine.getName());
//
//
//                    }
//                    System.out.println();*/
//
//                    for (String lineNumberString : lineNumbersArray) {
//                        transitLine = index.getLine(lineNumberString);
////                        System.out.println("Переход на станцию: " + transitStationName + " Линия: " + transitLine.getNumber() + " " + transitLine.getName());
//                    }
//                    //System.out.println();
//
//                } else {
//                    transitLine = index.getLine(numberOfTransitLine);
////                    System.out.println("Переход на станцию: " + transitStationName + " Линия: " + transitLine.getNumber() + " " + transitLine.getName());
//                    // System.out.println();
//                }
//            }
//            System.out.println(stations.size() + stations.toString());
//
//
//
//            /*String numberOfTransitLine = table.child(3).text();
//            System.out.println(numberOfTransitLine);
//            if (numberOfTransitLine.contains("8А11")) {
//                numberOfTransitLine = numberOfTransitLine.replaceAll("8А11", "8А 11");
//            }
//            if (numberOfTransitLine.isEmpty()) {
//                System.out.println("Станция: " + stationName);
//                System.out.println("Перехода нет\n");
//            }
//            else if (numberOfTransitLine.contains(" ")) {
//                String[] lineNumbersArray = numberOfTransitLine.split(" ");
//                for (String lineNmber : lineNumbersArray) {
//                    Line transitLine = index.getLine(lineNmber);
//                    System.out.println("Станция: " + stationName);
//                    System.out.println("Переход на линию: " + transitLine.getNumber() + " " + transitLine.getName());
//                    System.out.println();
//                }
//            }
//            else {
//                Line transitLine = index.getLine(numberOfTransitLine);
//                System.out.println("Станция: " + stationName);
//                System.out.println("Переход на линию: " + transitLine.getNumber() + " " + transitLine.getName());
//            }*/ // Здесь переходы на линии, не удалять
//
//        }


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
            String stationName = table.child(1).child(0).select("a[href]").text();
            String lineNumber = table.child(0).child(0).select("span").text();
            Line line = index.getLine(lineNumber);

            System.out.println("Станция: " + stationName + " Линия: " + line.getNumber() + " " + line.getName());
            String numberOfTransitLine = table.child(3).text();

            if (numberOfTransitLine.isEmpty()) {
                System.out.println("Перехода нет");
                System.out.println();
                continue;
            }

            Elements transfer = table.select("td:nth-of-type(4)").select("span[title]");

            ArrayList<Station> stations = new ArrayList<>();

            for (Element singleTransfer : transfer) {

                String transitionStation = singleTransfer.select("span").attr("title");
                String cutTransitionStation = transitionStation.substring(transitionStation.indexOf("станцию") + 8);
                String transitStationName = extractStation(transitionStation, cutTransitionStation);
                System.out.println("Переход на станцию: " + transitStationName);

                Station transitStation = index.getStation(transitStationName);

                stations.add(transitStation);

                if (numberOfTransitLine.contains("8А11")) {
                    numberOfTransitLine = numberOfTransitLine.replaceAll("8А11", "8А 11");
                }
            }

            int size = stations.size();

            if (size == 1) {
                System.out.println(size + " переход " + stations.toString() + "\n");
            }
            if (size == 2 || size == 3) {
                System.out.println(size + " перехода " + stations.toString() + "\n");
            } else {
                continue;
            }

            index.addConnection(stations);

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
}