package persistence;

import org.json.JSONObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class JsonReader {

    private static JSONObject jsonObject;
    private static String jsonString;

    public JsonReader() {
        jsonObject = new JSONObject();
        jsonString = "";
    }

    public String read(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    public void parse(String key) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String game = jsonObject.getString(key);

        System.out.println("game: " + game);
    }
}
