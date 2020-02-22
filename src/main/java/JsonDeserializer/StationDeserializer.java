package JsonDeserializer;

import Metro.Core.Station;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class StationDeserializer implements JsonDeserializer<Station> {
    @Override
    public Station deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        String stationName;





        return null;
    }
}
