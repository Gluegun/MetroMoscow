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

    public static void main(String[] args) throws IOException {

        StationIndexUtil index = new StationIndexUtil();

        GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(Line.class, new LineSerializer())
                .registerTypeAdapter(Station.class, new StationSerializer());

        Gson gson = gsonBuilder.setPrettyPrinting().create();

        String json = gson.toJson(index);

        try {

            Path dirPath = Paths.get("data");
            Path filePath = Paths.get("data/mosmetro.json");
            if (!Files.exists(dirPath)) Files.createDirectory(dirPath);
            if (!Files.exists(filePath)) Files.createFile(filePath);
            Files.write(filePath, json.getBytes(), StandardOpenOption.WRITE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
