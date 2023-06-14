package model;

import java.util.*;

// Represents a small grid with 9 positions in it.
public class SmallGrid {

    // colors
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\033[0;33m";

    Position []singlePosInit = new Position[] {
            new Position(0,0),new Position(0,1),new Position(0,2),
            new Position(1,0),new Position(1,1),new Position(1,2),
            new Position(2,0),new Position(2,1),new Position(2,2)};

    List<Position> singlePos = Arrays.asList(singlePosInit);

    private final int rows = 3;

    private final int columns = rows;

    private String[][] grid;
    int[][] pos = {{1,2,3},{4,5,6},{7,8,9}};

    private final ArrayList<Position> positionAligned;

    private LinkedHashMap<Integer, String> positionsPlayed = new LinkedHashMap<>();

    private boolean hasWon = false;
    private boolean hasTied = false;
    private String playerSymbol = "";

    /*
     * EFFECTS: initiates a grid that is a list
     * of strings
     */
    public SmallGrid() {
        grid = initGrid();
        positionAligned = new ArrayList<>();
    }

    public LinkedHashMap<Integer, String> getSmallGrid() {
        return positionsPlayed;
    }

    /*
     * REQUIRES: a linkedHashMap with Integral keys and String values
     * MODIFIES: this
     * EFFECTS: loads the grid to the grid array
     */
    public void loadSmallGrid(LinkedHashMap<Integer, String> positions) {
        grid = initGrid();
        positionsPlayed = positions;
        positions.forEach((key, value) -> {
            Position p = numberToPos(key);
            grid[p.getRow()][p.getColumn()] = value;
        });
        if (isAligned("X")) {
            makeWin("X");
        } else if (isAligned("O")) {
            makeWin("O");
        } else if (!isAligned("X") && positionsPlayed.size() == 9) {
            makeTie();
        }
    }

    /*
     * EFFECTS: adds strings to an initial grid
     */
    private String[][] initGrid() {
        String[][] matrix = new String[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = Integer.toString(pos[i][j]);
            }
        }
        EventLog.getInstance().logEvent(new Event("Created a small grid."));
        return matrix;
    }

    /*
     * REQUIRES: the symbol that has to be added,
     * x and y position where it has to be added
     * MODIFIES: this
     * EFFECTS: adds the symbol to the defined position
     * in the grid
     */
    public boolean add(String symbol, int x, int y) {
        if (!(grid[x][y].equals("X") || grid[x][y].equals("O"))) {
            grid[x][y] = symbol;
            positionsPlayed.put((3 * x) + (1 + y), symbol);
            return true;
        } else {
            return false;
        }
    }

    /*
     * REQUIRES: a symbol X or O
     * MODIFIES: this
     * EFFECTS: makes this grid win for a symbol
     */
    public void makeWin(String symbol) {
        hasWon = true;
        playerSymbol = symbol;
    }

    /*
     * MODIFIES: this
     * EFFECTS: makes this grid tie
     */
    public void makeTie() {
        hasTied = true;
    }

    public boolean hasTied() {
        return hasTied;
    }

    public boolean hasSmallWon() {
        return hasWon;
    }

    public String getWinnerSymbol() {
        return playerSymbol;
    }

    /*
     * REQUIRES: a symbol X or O
     * EFFECTS: checks if the grid has been
     * aligned with 3 occurences of the symbol
     */
    public boolean isAligned(String symbol) {
        if (isRowAligned(symbol)) {
            return true;
        } else if (isColumnAligned(symbol)) {
            return true;
        } else {
            return isDiagonalAligned(symbol);
        }
    }

    /*
     * REQUIRES: a symbol X or O
     * EFFECTS: checks if a row of symbols
     *  has been aligned in the small grid
     */
    private boolean isRowAligned(String symbol) {
        int times = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j <= columns - 3; j++) {
                for (int k = 0; k < 3; k++) {
                    if (grid[i][j + k].equals(symbol)) {
                        times++;
                    }
                }
                if (times == 3) {
                    for (int k = 0; k < 3; k++) {
                        positionAligned.add(new Position(i,j + k));
                    }
                    return true;
                } else {
                    times = 0;
                }

            }
        }
        return false;
    }

    /*
     * REQUIRES: a symbol X or O
     * EFFECTS: checks if a column of symbols
     *  has been aligned in the small grid
     */
    private boolean isColumnAligned(String symbol) {
        int times = 0;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j <= rows - 3; j++) {
                for (int k = 0; k < 3; k++) {
                    if (grid[j + k][i].equals(symbol)) {
                        times++;
                    }
                }
                if (times == 3) {
                    for (int k = 0; k < 3; k++) {
                        positionAligned.add(new Position(j + k,i));
                    }
                    return true;
                } else {
                    times = 0;
                }
            }

        }
        return false;
    }

    /*
     * REQUIRES: a symbol X or O
     * EFFECTS: checks if the diagonals of symbols
     *  has been aligned in the small grid
     */
    private boolean isDiagonalAligned(String symbol) {
        return checkLeftDiagonal(symbol) || checkRightDiagonal(symbol);
    }

    /*
     * REQUIRES: a symbol X or O
     * EFFECTS: checks if the left of symbols
     *  has been aligned in the small grid
     */
    private boolean checkLeftDiagonal(String symbol) {
        int times = 0;
        for (int i = 0; i <= rows - 3; i++) {
            for (int j = 0; j <= columns - 3; j++) {
                for (int k = 0; k < 3; k++) {
                    if (grid[i + k][j + k].equals(symbol)) {
                        times++;
                    }
                }
                if (times == 3) {
                    for (int k = 0; k < 3; k++) {
                        positionAligned.add(new Position(i + k,j + k));
                    }
                    return true;
                } else {
                    times = 0;
                }


            }
        }
        return false;
    }

    /*
     * REQUIRES: a symbol X or O
     * EFFECTS: checks if the right of symbols
     *  has been aligned in the small grid
     */
    private boolean checkRightDiagonal(String symbol) {
        int times = 0;
        int row = rows - 1;
        for (int i = 0; i <= rows - 3; i++) {
            for (int j = 0; j <= columns - 3; j++) {
                for (int k = 0; k < 3; k++) {
                    if (grid[row - k][j + k].equals(symbol)) {
                        times++;
                    }
                }
                if (times == 3) {
                    for (int k = 0; k < 3; k++) {
                        positionAligned.add(new Position(row - k,j + k));
                    }
                    return true;
                } else {
                    times = 0;
                }
            }
            row--;
        }
        return false;
    }

    /*
     * EFFECTS: converts the grid list
     * to a string
     */
    public String toString() {
        String s = "\n  ";

        if (hasWon) {
            s = printLarge(playerSymbol, "      ");
        } else if (hasTied) {
            s = printLarge("tie", "     ");
        } else {
            for (int i = 0; i < rows; i++) {
                s += "\n";
                for (int j = 0; j < columns; j++) {
                    if (positionAligned.contains(new Position(i,j))) {
                        s += "  "  + grid[i][j]  + "  ";
                    } else {
                        s += ANSI_YELLOW +  "  "  + grid[i][j]  + "  " + ANSI_RESET;
                    }
                }
            }
        }
        return s;
    }

    /*
     * REQUIRES: a string that will be placed in the
     * center of the box, spaces around the string
     * EFFECTS: converts a won/tied grid to a box
     */
    private String printLarge(String center, String space) {
        String s = "\n";
        int heightInLines = 5;

        for (int i = 1; i <= heightInLines; i++) {
            if (i == 1 || i == heightInLines) {
                for (int j = 1; j <= 15; j++) {
                    if (j == 1 || j == 15) {
                        s += "+";
                    } else {
                        s += "-";
                    }
                }
                s += "\n";
            } else if (i % 2 == 0) {
                s += "|             |";
                s += "\n";
            } else {
                s += "+" + space + center + space + "+";
                s += "\n";
            }
        }
        return s;
    }

    private Position numberToPos(int num) {
        return (singlePos.get(num - 1));
    }
}
