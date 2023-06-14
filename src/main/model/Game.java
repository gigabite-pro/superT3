package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.util.*;


// Represents a game controller that handles the game
public class Game {

    Position []singlePosInit = new Position[] {
            new Position(0,0),new Position(0,1),new Position(0,2),
            new Position(1,0),new Position(1,1),new Position(1,2),
            new Position(2,0),new Position(2,1),new Position(2,2)};

    List<Position> singlePos = Arrays.asList(singlePosInit);

    private LargeGrid grid;

    public Game() {
//        runGame(); // uncomment this to run CLI game
    }

    private Scanner input;

    private Player p1;
    private Player p2;

    private boolean runState = true;
    private int activeGrid = 0;
    private String activePlayerSymbol;
    private ArrayList<Integer> wonGrids = new ArrayList<>();

    /*
     * EFFECTS: starts a game and shows a game menu
     */
    private void runGame() {
        String command;
        init();

        while (runState) {
            showMenu();
            command = input.next().toLowerCase();
            if (command.equals("q")) {
                handleQuit();
            } else if (command.equals("p")) {
                handlePlay();
            } else {
                System.out.println("Invalid Input");
            }
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: initializes players and initiates Scanner
     */
    private void init() {
        p1 = new Player("X");
        p2 = new Player("0");
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    /*
     * EFFECTS: shows the main game menu
     */
    private void showMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tp -> play game");
        System.out.println("\tq -> quit game");
    }

    /*
     * EFFECTS: starts a new game or load a saved game
     */
    private void handlePlay() {
        String command;
        showGameMenu();
        command = input.next().toLowerCase();
        if (command.equals("n")) {
            play();
        } else if (command.equals("l")) {
            showLoadMenu();
        } else {
            System.out.println("Invalid Input");
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: saves or exits the game based on user input
     */
    private void handleQuit() {
        String command;
        System.out.println("Do you want to save the game before quitting?");
        System.out.println("\ty -> yes, save the game");
        System.out.println("\tn -> no, quit the game");
        command = input.next().toLowerCase();
        if (command.equals("y")) {
            save();
        } else if (command.equals("n")) {
            System.out.println("Thank you for playing");
            runState = false;
        } else {
            System.out.println("Invalid Input");
        }
        runState = false;
    }

    /*
     * EFFECTS: shows the inner menu
     */
    private void showGameMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tn -> new game");
        System.out.println("\tl -> load game");
    }

    /*
     * EFFECTS: reads a JSON file to show a menu of saved games
     */
    private void showLoadMenu() {
        String command;
        JsonReader jsonReader = new JsonReader();
        try {
            String dataString = jsonReader.read("./data/data.json");
            if (!dataString.isEmpty()) {
                JSONObject data = new JSONObject(dataString);
                System.out.println("Please input the name of the saved game you want to continue: ");
                System.out.printf("| %-20s | %-20s |%n", "Saved Name", "Saved Time");
                System.out.printf("-----------------------------------------------%n");
                for (int i = 0; i < data.names().length(); i++) {
                    String key = data.names().getString(i);
                    Object date = data.getJSONObject(key).get("date");
                    System.out.printf("| %-20s | %-20s |%n", key, date);
                }
            } else {
                System.out.println("There are no saved games");
                handlePlay();
            }
        } catch (IOException e) {
            System.out.println("File cannot be read");
        }

        command = input.next();
        load(command);
    }

    /*
     * MODIFIES: this
     * EFFECTS: initializes a large grid and asks
     * for input from the user
     */
    private void play() {
        grid = new LargeGrid();
        showGrid();
        getInput(p1);
    }

    /*
     * EFFECTS: loads the selected game
     */
    private void load(String savedName) {
        JsonReader jsonReader = new JsonReader();
        try {
            grid = new LargeGrid();
            String dataString = jsonReader.read("./data/data.json");
            JSONObject data = new JSONObject(dataString);
            JSONArray dataArray  = (JSONArray) data.getJSONObject(savedName).get("game");
            loadDataToGrids(dataArray, data, savedName);
        } catch (IOException e) {
            System.out.println("File cannot be opened");
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
                for (int j = 0; j < dataArray.getJSONObject(i).length(); j++) {
                    JSONArray keys = (JSONArray) dataArray.getJSONObject(i).get("keys");
                    JSONArray values = (JSONArray) dataArray.getJSONObject(i).get("values");
                    if (keys.length() != 0 || values.length() != 0) {
                        positions.put(keys.getInt(j), values.getString(j));
                    }
                }
                grid.getSmallGrid(i + 1).loadSmallGrid(positions);
            }
            showGrid();
            activeGrid = data.getJSONObject(savedName).getInt("activeGrid");
            if (data.getJSONObject(savedName).getString("activePlayerSymbol").equals(p1.getSymbol())) {
                getInput(p1);
            } else {
                getInput(p2);
            }
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: saves the game
     */
    private void save() {
        String command;
        System.out.println("Save game as: ");
        command = input.next();
        JsonWriter jsonWriter = new JsonWriter();
        boolean added = jsonWriter.addGame(command, grid.getGrid(), activeGrid, activePlayerSymbol, true, wonGrids);
        if (!added) {
            promptConfirmName(command, jsonWriter);
        }
        jsonWriter.write();
        System.out.println("Thank you for playing");
        runState = false;
    }

    /*
     * EFFECTS: if saved name exists,
     * shows a prompt to change the name or
     * update existing data
     */
    private void promptConfirmName(String name, JsonWriter jsonWriter) {
        String command;
        System.out.println("A game with " + name + " already exists");
        System.out.println("u -> update existing data");
        System.out.println("c -> change the name");
        command = input.next().toLowerCase();

        if (command.equals("u")) {
            jsonWriter.addGame(name, grid.getGrid(), activeGrid, activePlayerSymbol, false, wonGrids);
        } else if (command.equals("c")) {
            save();
        }
    }

    /*
     * EFFECTS: prints the grid on the console
     */
    private void showGrid() {
        System.out.println(grid.toString());
    }

    /*
     * MODIFIES: this
     * EFFECTS: asks the user to select the grid to play in
     */
    private void getInput(Player p) {
        int commandGrid;
        if (activeGrid == 0) {
            System.out.println("Which grid do you want to play in?");
            commandGrid = input.nextInt();
            activeGrid = commandGrid;
        } else if (wonGrids.contains(activeGrid)) {
            activeGrid = 0;
            getInput(p);
        }
        System.out.println("Active Grid: " + activeGrid);
        playPrompt(p);
    }

    /*
     * EFFECTS: asks for the position to play in and plays in the position
     */
    private void playPrompt(Player p) {
        int commandPos;
        System.out.println("\nWrite the position number where you want to place " + "(" + p.getSymbol() + ")"
                + "\n0 -> quit to main menu");
        commandPos = input.nextInt();

        if (commandPos == 0) {
            runGame();
        } else {
            addPosition(p,commandPos);
        }
    }

    /*
     * MODIFIES: Small Grid
     * EFFECTS: adds a position to the small grid
     */
    private void addPosition(Player p, Integer commandPos) {
        Position position = numberToPos(commandPos);
        boolean addState = grid.getSmallGrid(activeGrid).add(p.getSymbol(),position.getRow(),position.getColumn());
        if (addState) {
            if (hasSmallWon(p.getSymbol())) {
                grid.getSmallGrid(activeGrid).makeWin(p.getSymbol());
                wonGrids.add(activeGrid);
            } else if (hasSmallTied(p.getSymbol())) {
                grid.getSmallGrid(activeGrid).makeTie();
                wonGrids.add(activeGrid);
            }
            activeGrid = commandPos;

            handleWin(p);
        } else {
            System.out.println("This position has already been played");
            showGrid();
            getInput(p);
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: initializes a large grid and asks
     * for input from the user
     */
    private void handleWin(Player p) {
        if (hasLargeWon()) {
            System.out.println("Congratulations! " + activePlayerSymbol + " has won the game");
            savePrompt();
        } else if (!hasLargeWon() && wonGrids.size() == 9) {
            System.out.println("The game has tied!");
            savePrompt();
        } else {
            turn(p);
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: asks the user to save the game or not
     */
    private void savePrompt() {
        String command;
        System.out.println("Do you want to save this game?");
        System.out.println("y -> yes");
        System.out.println("n -> no");
        command = input.next().toLowerCase();

        if (command.equals("y")) {
            save();
        } else {
            runState = false;
        }
        runGame();
    }

    /*
     * MODIFIES: this
     * EFFECTS: changes the turn of who plays the next move
     */
    private void turn(Player p) {
        if (p.getSymbol().equals(p1.getSymbol())) {
            p = p2;
        } else {
            p = p1;
        }
        activePlayerSymbol = p.getSymbol();
        getInput(p);
    }

    private boolean hasLargeWon() {
        return grid.isAligned(activePlayerSymbol);
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
