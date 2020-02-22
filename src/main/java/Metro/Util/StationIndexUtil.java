package Metro.Util;

import Metro.Core.Line;
import Metro.StationIndex;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StationIndexUtil {

    static StationIndex index;

    static {
        try {
            index = MetroUtil.createIndex();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, List<String>> stations = createTestIndex();

    private List<Line> lines = index.getAllLines();


    private static Map<String, List<String>> createTestIndex() {

        TreeMap<String, List<String>> stations = new TreeMap<>();

        index.getAllLines().forEach(line -> stations.put(line.getName(), line.getStationsName()));

        return stations;
    }

    public Map<String, List<String>> getStations() {
        return stations;
    }

    public List<Line> getLines() {
        return lines;
    }
}
