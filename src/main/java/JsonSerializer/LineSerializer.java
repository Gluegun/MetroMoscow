package JsonSerializer;

import Metro.Line;
import Metro.Station;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LineSerializer implements JsonSerializer<Line> {
    @Override
    public JsonElement serialize(Line src, Type type, JsonSerializationContext context) {

        JsonObject result = new JsonObject();
        result.addProperty("name", src.getName());
        result.addProperty("number", src.getNumber());
        result.addProperty("color", src.getColor());

        return result;
    }
}
