package JsonSerializer;

import Metro.Station;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;

import java.lang.reflect.Type;

public class StationSerializer implements com.google.gson.JsonSerializer <Station> {


    @Override
    public JsonElement serialize(Station station, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }
}
