package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.util.*;


// Represents a game controller that handles the GUI game
public class GuiGame {

    Position []singlePosInit = new Position[] {
            new Position(0,0),new Position(0,1),new Position(0,2),
            new Position(1,0),new Position(1,1),new Position(1,2),
            new Position(2,0),new Position(2,1),new Position(2,2)};

    List<Position> singlePos = Arrays.asList(singlePosInit);

    private LargeGrid grid;

    /*
     * MODIFIES: this
     * EFFECTS: initializes players and sets the active player
     */
    public GuiGame() {
        p1 = new Player("X");
        p2 = new Player("O");
        activePlayer = p1;
    }

    private final Player p1;
    private final Player p2;

    private int activeGrid = 0;
    Player activePlayer;
    boolean hasGameStarted = false;
    private final ArrayList<Integer> wonGrids = new ArrayList<>();

    /*
     * EFFECTS: returns the active grid
     */
    public int getActiveGrid() {
        return this.activeGrid;
    }

    /*
     * REQUIRES: Integer activeGrid to set
     * MODIFIES: this.activeGrid
     * EFFECTS: returns the active grid
     */
    public void setActiveGrid(int activeGrid) {
        this.activeGrid = activeGrid;
        EventLog.getInstance().logEvent(new Event("Active grid set to " + activeGrid + "."));
    }

    /*
     * EFFECTS: returns the active player
     */
    public Player getActivePlayer() {
        return this.activePlayer;
    }

    /*
     * EFFECTS: returns the large grid
     */
    public LargeGrid getGrid() {
        return grid;
    }

    /*
     * EFFECTS: returns the won grid list
     */
    public ArrayList<Integer> getWonGrids() {
        return wonGrids;
    }

    /*
     * EFFECTS: starts the game
     */
    public void play() {
        grid = new LargeGrid();
        hasGameStarted = true;
    }

    public boolean isHasGameStarted() {
        return hasGameStarted;
    }

    /*
     * MODIFIES: Small Grid
     * EFFECTS: adds a position to the small grid
     */
    public int addPosition(Integer commandPos) {
        Position position = numberToPos(commandPos);
        grid.getSmallGrid(activeGrid).add(activePlayer.getSymbol(),position.getRow(),position.getColumn());
        EventLog.getInstance().logEvent(new
                Event("Added a " + activePlayer.getSymbol() + " to grid " + activeGrid));
        if (hasSmallWon(activePlayer.getSymbol())) {
            grid.getSmallGrid(activeGrid).makeWin(activePlayer.getSymbol());
            wonGrids.add(activeGrid);
            EventLog.getInstance().logEvent(new Event(activePlayer.getSymbol() + " won grid " + activeGrid));
        } else if (hasSmallTied(activePlayer.getSymbol())) {
            grid.getSmallGrid(activeGrid).makeTie();
            wonGrids.add(activeGrid);
            EventLog.getInstance().logEvent(new Event("Grid " + activeGrid + " has been tied."));
        }
        activeGrid = commandPos;

        return handleWin(activePlayer);
    }

    /*
     * MODIFIES: this
     * EFFECTS: initializes a large grid and asks
     * for input from the user
     */
    public int handleWin(Player p) {
        if (hasLargeWon()) {
            EventLog.getInstance().logEvent(new Event(p.getSymbol() + " has won the game."));
            return 1;
        } else if (!hasLargeWon() && wonGrids.size() == 9) {
            EventLog.getInstance().logEvent(new Event("The game has been tied."));
            return 2;
        } else {
            turn(p);
            return 3;
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: changes the turn of who plays the next move
     */
    private void turn(Player p) {
        if (p.getSymbol().equals(p1.getSymbol())) {
            activePlayer = p2;
        } else {
            activePlayer = p1;
        }
    }

    /*
     * EFFECTS: returns keys of played positions in a small grid
     */
    public ArrayList<Integer> getSmallPlayedPositionKeys(int i) {
        ArrayList<Integer> positionKeys = new ArrayList<>();

        grid.getSmallGrid(i).getSmallGrid().forEach((key, value) -> positionKeys.add(key));

        return positionKeys;
    }

    /*
     * EFFECTS: reads a JSON file to show a menu of saved games
     */
    public ArrayList<Object[]> loadDataFromFile() {
        JsonReader jsonReader = new JsonReader();
        try {
            String dataString = jsonReader.read("./data/data.json");
            if (dataString.length() > 0) {
                JSONObject data = new JSONObject(dataString);
                ArrayList<Object[]> newData = new ArrayList<>();
                for (int i = 0; i < data.names().length(); i++) {
                    String key = data.names().getString(i);
                    Object activePlayer = data.getJSONObject(key).get("activePlayerSymbol");
                    Object activeGrid = data.getJSONObject(key).get("activeGrid");
                    Object date = data.getJSONObject(key).get("date");
                    newData.add(new Object[] {key, activePlayer, activeGrid, date});
                }
                return newData;
            } else {
                return new ArrayList<>();
            }

        } catch (IOException e) {
            System.out.println("File cannot be read");
            return new ArrayList<>();
        }
    }

    /*
     * EFFECTS: loads the selected game
     */
    public boolean load(String savedName) {
        JsonReader jsonReader = new JsonReader();
        try {
            grid = new LargeGrid();
            String dataString = jsonReader.read("./data/data.json");
            JSONObject data = new JSONObject(dataString);
            JSONArray dataArray  = (JSONArray) data.getJSONObject(savedName).get("game");
            loadDataToGrids(dataArray, data, savedName);
            return true;
        } catch (IOException e) {
            System.out.println("File cannot be opened");
            return false;
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: adds game data to each grid in the game
     */
    private void loadDataToGrids(JSONArray dataArray, JSONObject data, String savedName) {
        if (dataArray.length() != 0) {
            for (int i = 0; i < dataArray.length(); i++) {
                LinkedHashMap<Integer, String> positions = new LinkedHashMap<>();
                JSONArray keys = (JSONArray) dataArray.getJSONObject(i).get("keys");
                JSONArray values = (JSONArray) dataArray.getJSONObject(i).get("values");
                for (int j = 0; j < keys.length(); j++) {
                    if (keys.length() != 0 || values.length() != 0) {
                        positions.put(keys.getInt(j), values.getString(j));
                    }
                }
                grid.getSmallGrid(i + 1).loadSmallGrid(positions);
            }
            activeGrid = data.getJSONObject(savedName).getInt("activeGrid");
            for (int i = 0; i < data.getJSONObject(savedName).getJSONArray("wonGrids").length(); i++) {
                wonGrids.add(data.getJSONObject(savedName).getJSONArray("wonGrids").getInt(i));
            }
            setAndCheckActivePlayerWin(data, savedName);
            EventLog.getInstance().logEvent(new Event("Data loaded from file."));
        }
    }

    private void setAndCheckActivePlayerWin(JSONObject data, String savedName) {
        if (data.getJSONObject(savedName).getString("activePlayerSymbol").equals(p1.getSymbol())) {
            activePlayer = p1;
            handleWin(p2);
        } else {
            activePlayer = p2;
            handleWin(p1);
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: saves the game
     */
    public boolean save(String s) {
        JsonWriter jsonWriter = new JsonWriter();
        boolean added = jsonWriter.addGame(s, grid.getGrid(), activeGrid, activePlayer.getSymbol(), true, wonGrids);
        if (!added) {
            return false;
        }
        jsonWriter.write();
        EventLog.getInstance().logEvent(new Event("Data written to file."));
        return true;
    }

    /*
     * EFFECTS: updates the existing game
     */
    public void update(String s) {
        JsonWriter jsonWriter = new JsonWriter();
        jsonWriter.addGame(s, grid.getGrid(), activeGrid, activePlayer.getSymbol(), false, wonGrids);
        jsonWriter.write();
        EventLog.getInstance().logEvent(new Event("Data updated in file."));
    }

    /*
     * MODIFIES: this
     * EFFECTS: prints all the logs in the events
     */
    public void printLogs() {
        for (Event next: EventLog.getInstance()) {
            System.out.println(next.getDate() + " : " + next.getDescription());
        }
    }

    private boolean hasLargeWon() {
        return grid.isAligned(activePlayer.getSymbol());
    }

    private boolean hasSmallWon(String s) {
        return grid.getSmallGrid(activeGrid).isAligned(s);
    }

    private boolean hasSmallTied(String s) {
        SmallGrid sg = grid.getSmallGrid(activeGrid);
        return !sg.isAligned(s) && sg.getSmallGrid().size() == 9;
    }

    private Position numberToPos(int num) {
        return (singlePos.get(num - 1));
    }
}
