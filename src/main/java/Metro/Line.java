package Metro;

import java.util.ArrayList;
import java.util.List;

public class Line implements Comparable<Line> {
    private double number;
    private String name;
    //private List<Station> stations;
    private String color;

    public Line(double number, String name, String color) {
        this.number = number;
        this.name = name;
        this.color = color;
        //stations = new ArrayList<>();
    }

    public double getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    /*public void addStation(Station station) {
        stations.add(station);
    }

    public List<Station> getStations() {
        return stations;
    }*/

    @Override
    public int compareTo(Line line) {
        return Double.compare(number, line.getNumber());
    }

    @Override
    public boolean equals(Object obj) {
        return compareTo((Line) obj) == 0;
    }

}
