import JsonSerializer.LineSerializer;
import JsonSerializer.StationSerializer;
import Metro.Core.Line;
import Metro.Core.Station;
import Metro.Util.StationIndexUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class Main {

    static String url = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";

    public static void main(String[] args) throws IOException {

        StationIndexUtil testIndex = new StationIndexUtil();

        GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(Line.class, new LineSerializer())
                .registerTypeAdapter(Station.class, new StationSerializer());

        Gson gson = gsonBuilder.setPrettyPrinting().create();

        String json = gson.toJson(testIndex);


        try {

            Path dirPath = Paths.get("data");
            Path filePath = Paths.get("data/mosmetro.json");
            if (!Files.exists(dirPath)) Files.createDirectory(dirPath);
            if (!Files.exists(filePath)) Files.createFile(filePath);
            Files.write(filePath, json.getBytes(), StandardOpenOption.WRITE);

        } catch (
                Exception ex) {
            ex.printStackTrace();
        }
    }
}
