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
import java.util.ArrayList;

public class MetroUtil {

    private static String url = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";

    public static StationIndex createIndex() throws IOException {

        StationIndex index = new StationIndex();

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

        parseConnections(table1, index);
        parseConnections(table2, index);
        parseConnections(table3, index);

        return index;
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

    public static void parseConnections(Elements tables, StationIndex index) {

        for (Element table : tables) {
            String stationName = table.child(1).text();
            if (stationName.equals("Название станции")) continue;

            String lineNumber = table.child(0).child(0).select("span").text();
            Line line = index.getLine(lineNumber);

//            System.out.println("Станция: " + stationName + " Линия: " + line.getNumber() + " " + line.getName());
            String numberOfTransitLine = table.child(3).text();
            if (numberOfTransitLine.isEmpty()) {
//                System.out.println("Перехода нет");
//                System.out.println();
                continue;
            }

            Elements transfer = table.select("td:nth-of-type(4)").select("span[title]");

            ArrayList<Station> stations = new ArrayList<>();

            for (Element singleTransfer : transfer) {

                String transitionStation = singleTransfer.select("span").attr("title");
                String cutTransitionStation = transitionStation.substring(transitionStation.indexOf("станцию") + 8);
                String transitStationName = extractStation(transitionStation, cutTransitionStation);
//                System.out.println("Переход на станцию: " + transitStationName);

                Station transitStation = index.getStation(transitStationName);

                stations.add(transitStation);

                if (numberOfTransitLine.contains("8А11")) {
                    numberOfTransitLine = numberOfTransitLine.replaceAll("8А11", "8А 11");
                }
            }


            index.addConnection(stations);

        }
    }

    private static String extractStation(String transitionStation, String cutTransitionStation) {
        String stationName;
        int firstIndex = transitionStation.indexOf("станцию") + 8;

        if (transitionStation.contains("Московского центрального кольца")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Московского центрального кольца"));
            return stationName.trim();
        } else if (transitionStation.contains("Кольцевой линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Кольцевой линии"));
            if (stationName.contains("Парк культуры")) {
                stationName = "Парк культуры Парк Культуры имени Горького (до 1960-х)";
            }
            if (stationName.contains("Проспект Мира")) {
                stationName = "Проспект Мира Ботанический сад (до 1966)";
            }
            if (stationName.contains("Добрынинская")) {
                stationName = "Добрынинская Серпуховская (до 1961)";
            }
            return stationName.trim();
        } else if (transitionStation.contains("Калужско-Рижской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Калужско-Рижской линии"));
            if (stationName.contains("Проспект Мира")) {
                stationName = "Проспект Мира Ботанический сад (до 1966)";
            }
            if (stationName.contains("Китай-город")) {
                stationName = "Китай-город Площадь Ногина (до 1990)";
            }
            if (stationName.contains("Новоясеневская")) {
                stationName = "Новоясеневская Битцевский парк (до 2008)";
            }
            if (stationName.contains("ВДНХ")){
                stationName = "ВДНХ ВСХВ (до 1959)";
            }
            return stationName.trim();
        } else if (transitionStation.contains("Таганско-Краснопресненской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Таганско-Краснопресненской линии"));
            if (stationName.contains("Китай-город")) {
                stationName = "Китай-город Площадь Ногина (до 1990)";
            }
            return stationName.trim();
        } else if (transitionStation.contains("Люблинско-Дмитровской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Люблинско-Дмитровской линии"));
            return stationName.trim();
        } else if (transitionStation.contains("Замоскворецкой линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Замоскворецкой линии"));
            if (stationName.contains("Театральная")) {
                stationName = "Театральная Площадь Свердлова (до 1990)";
            }
            if (stationName.contains("Тверская")) {
                stationName = "Тверская Горьковская (до 1990)";
            }
            return stationName.trim();
        } else if (transitionStation.contains("Арбатско-Покровской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Арбатско-Покровской линии"));
            if (stationName.contains("Партизанская")) {
                stationName = "Партизанская Измайловский парк культуры и отдыха имени Сталина (до 1947) Измайловская (до 1963) Измайловский парк (до 2005)";
            }
            return stationName.trim();
        } else if (transitionStation.contains("Филёвской линии")) {
            stationName = transitionStation.substring(firstIndex, transitionStation.indexOf("Филёвской линии"));
            if (stationName.contains("Александровский сад")) {
                stationName = "Александровский сад Улица Коминтерна (до 1946) Калининская (до 1990)";
            }
            if (stationName.contains("Выставочная")) {
                stationName = "Выставочная Деловой центр (до 2008)";
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
            if (stationName.contains("Каховская")) {
                stationName = "Каховская (закрыта с 30 марта 2019)";
            }
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
            if (stationName.contains("Охотный Ряд")) {
                stationName = "Охотный Ряд Имени Л. М. Кагановича (с 1955 до 1957) Проспект Маркса (с 1961 до 1990)";
            }
            if (stationName.contains("Парк культуры")) {
                stationName = "Парк культуры Парк Культуры имени Горького (до 1960-х)";
            }
            if (stationName.contains("Чистые пруды")) {
                stationName = "Чистые пруды Кировская (до 1990)";
            }
            if (stationName.contains("Лубянка")) {
                stationName = "Лубянка Дзержинская (до 1990)";
            }
            return stationName.trim();
        } else return null;
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
