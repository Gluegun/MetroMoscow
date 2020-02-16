package JsonSerializer;

import Metro.Line;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class LineSerializer implements JsonSerializer<Line> {
    @Override
    public JsonElement serialize(Line line, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }
}
