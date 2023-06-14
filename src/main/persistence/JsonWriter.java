package persistence;

import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Array;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class JsonWriter {

    private static final int TAB = 4;
    private static JSONObject jsonObject;
    private PrintWriter writer;

    public JsonWriter() {
        JsonReader jsonReader = new JsonReader();
        try {
            String dataString = jsonReader.read("./data/data.json");
            if (dataString.equals("")) {
                jsonObject = new JSONObject();
            } else {
                jsonObject = new JSONObject(dataString);
            }
        } catch (IOException e) {
            System.out.println("File cannot be read");
        }
    }

    public void write() {
        try {
            writer = new PrintWriter("./data/data.json");
            writer.print(jsonObject.toString(TAB));
            writer.close();
            System.out.println("Saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean addGame(String key, ArrayList<LinkedHashMap<Integer, String>> grid,
                           int activeGrid, String activePlayerSymbol, boolean state, ArrayList<Integer> wonGrids) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        JSONObject nestedJson = new JSONObject();

        nestedJson.put("date", dtf.format(now));
        nestedJson.put("activeGrid", activeGrid);
        nestedJson.put("activePlayerSymbol", activePlayerSymbol);
        nestedJson.put("game", parseHashMap(grid));
        nestedJson.put("wonGrids", wonGrids);
        if (jsonObject.has(key) && state) {
            return false;
        } else {
            jsonObject.put(key, nestedJson);
            return true;
        }
    }

    private ArrayList<Object> parseHashMap(ArrayList<LinkedHashMap<Integer, String>> grid) {
        ArrayList<Object> combinedPositions = new ArrayList<>();
        for (LinkedHashMap<Integer, String> hm : grid) {
            ArrayList<Integer> keys = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();
            hm.forEach((k, value) -> {
                keys.add(k);
                values.add(value);
            });
            JSONObject combinedKeyVal = new JSONObject();
            combinedKeyVal.put("keys", keys);
            combinedKeyVal.put("values", values);
            combinedPositions.add(combinedKeyVal);
        }

        return combinedPositions;
    }
}
