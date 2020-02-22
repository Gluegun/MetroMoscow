package JsonSerializer;

import Metro.Core.Line;
import com.google.gson.*;

import java.lang.reflect.Type;

public class LineSerializer implements JsonSerializer<Line> {
    @Override
    public JsonElement serialize(Line src, Type type, JsonSerializationContext context) {

        JsonObject result = new JsonObject();
        result.addProperty("name", src.getName());
        result.addProperty("number", src.getNumber());
        result.addProperty("color", src.getColor());

        /*JsonArray arrayOfStations = new JsonArray();
        result.add("stations", arrayOfStations);

        for (Station station : src.getStations()) {
            arrayOfStations.add(new JsonPrimitive(station.getName()));
        }*/


        return result;
    }
}
