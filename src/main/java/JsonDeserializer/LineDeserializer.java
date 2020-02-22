/*
package JsonDeserializer;

import Metro.Core.Line;
import com.google.gson.*;

import java.lang.reflect.Type;

public class LineDeserializer implements JsonDeserializer<Line> {
    @Override
    public Line deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        String data = json.getAsString();

        JsonObject object = json.getAsJsonObject();
        String lineName;
        String lineNumber;
        String lineColor;



        Line line = new Line(lineNumber, lineName, lineColor);

        return null;
    }
}
*/
