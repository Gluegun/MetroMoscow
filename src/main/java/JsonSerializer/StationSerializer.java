package JsonSerializer;

import Metro.Station;
import com.google.gson.*;

import java.lang.reflect.Type;

public class StationSerializer implements com.google.gson.JsonSerializer<Station> {


    @Override
    public JsonElement serialize(Station src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject result = new JsonObject();
        result.addProperty("name", src.getName());
        result.addProperty("line", src.getLine().getName());


        /*JsonArray lines = new JsonArray();
        result.add("lines", lines);
        for (Station station : src.getLine().getStations()) {
            lines.add(new JsonPrimitive(station.getLine().getNumber()));
            lines.add(new JsonPrimitive(station.getLine().getName()));
            lines.add(new JsonPrimitive(station.getLine().getColor()));
        }*/


        return result;
    }
}
