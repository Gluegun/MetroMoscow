package Metro;

import com.google.gson.annotations.SerializedName;

import java.util.*;
import java.util.stream.Collectors;

public class StationIndex {
    @SerializedName("Metro.Station")
    HashMap<String, Line> number2line;
    TreeSet<Station> stations;
    TreeMap<Station, TreeSet<Station>> connections; // связи станций?


    public StationIndex() {
        number2line = new HashMap<>();
        stations = new TreeSet<>();
        connections = new TreeMap<>();

    }

    public void addStation(Station station) {
        stations.add(station);
    }

    public void addLine(Line line) {
        number2line.put(line.getNumber(), line); // добавляем в hashmap номер линии и линию, например: "(1, "первая")"
    }

    public void addConnection(List<Station> stations) {
        for (Station station : stations) {
            if (!connections.containsKey(station)) { // если TreeMap connections не содержит ключ(станция)
                connections.put(station, new TreeSet<>()); // добавляем ключ(станция) в TreeMap connections с новым TreeSet'ом
            }
            TreeSet<Station> connectedStations = connections.get(station); // новый трисэт connectedStations = connections.get(ключ(станция))
            connectedStations.addAll(stations.stream() //добавляем все полученные станции в список станций
                    .filter(s -> !s.equals(station)).collect(Collectors.toList()));
        }
    }

    public Line getLine(int number) {
        return number2line.get(number); // получаем значение по ключу
    }

    public Station getStation(String name) {
        for (Station station : stations) {
            if (station.getName().equalsIgnoreCase(name)) {
                return station;
            }
        }
        return null;
    }

    public Station getStation(String name, int lineNumber) {
        Station query = new Station(name, getLine(lineNumber));
        Station station = stations.ceiling(query); // ceiling возвращает ближайший элемент, но больше, чем текущий. Как это работает с Object?
        return station.equals(query) ? station : null;
    }

    public Set<Station> getConnectedStations(Station station) {
        if (connections.containsKey(station)) {
            return connections.get(station);
        }
        return new TreeSet<>();
    }
}
