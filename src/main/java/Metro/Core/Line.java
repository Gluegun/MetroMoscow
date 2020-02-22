package Metro.Core;

import java.util.ArrayList;
import java.util.List;

public class Line implements Comparable<Line> {
    private String number;
    private String name;
    private List<Station> stations;
    private String color;
    private List<String> stationsName;

    public Line(String number, String name, String color) {
        this.number = number;
        this.name = name;
        this.color = color;
        this.stations = new ArrayList<>();
        this.stationsName = new ArrayList<>();
    }

    public String getNumber() {
        return number;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public void addStation(Station station) {
        stations.add(station);
        stationsName.add(station.getName());
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<String> getStationsName() {
        return stationsName;
    }

    @Override
    public int compareTo(Line line) {
        return this.getNumber().compareTo(line.getNumber());
    }

    @Override
    public boolean equals(Object obj) {
        return compareTo((Line) obj) == 0;
    }

    @Override
    public String toString() {
        return "Line{" +
                "number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
