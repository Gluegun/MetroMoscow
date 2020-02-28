package Metro.Util;

import Metro.Core.Line;
import Metro.Core.Station;
import Metro.StationIndex;
import com.sun.source.tree.Tree;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class StationIndexUtil {

    static StationIndex index;

    static {
        try {
            index = MetroUtil.createIndex();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, List<String>> stations = createStations();

    private Map<String, Line> lines = createLines();

    private Map<String, Set<Station>> connectedStations = createConnections();

    private Map<String, Set<Station>> createConnections() {

        Map<String, Set<Station>> connectedStations = new TreeMap<>();

        for (Line line : index.getAllLines()) {

            for (Station station : line.getStations()) {

                Set<Station> connectedStationsSet = index.getConnectedStations(station);
                if (!connectedStationsSet.isEmpty()) {
                    connectedStations.put(station.getName(), connectedStationsSet);
                }
            }
        }

        return connectedStations;

    }


    private static Map<String, List<String>> createStations() {

        TreeMap<String, List<String>> stations = new TreeMap<>();

        index.getAllLines().forEach(line -> stations.put(line.getName(), line.getStationsName()));

        return stations;
    }

    private Map<String, Line> createLines() {

        Map<String, Line> lines = new TreeMap<>();

        for (Line line : index.getAllLines()) {
            lines.put(line.getNumber(), line);
        }
        return lines;
    }

    public Map<String, List<String>> getStations() {
        return stations;
    }

    public Map<String, Line> getLines() {
        return lines;
    }
}
