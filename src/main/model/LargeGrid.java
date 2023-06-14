package model;

import java.util.ArrayList;
import java.util.LinkedHashMap;

// Represents a large grid containing 9 small grids.
public class LargeGrid {

    private final ArrayList<SmallGrid> largeGrid = new ArrayList<>();

    /*
     * EFFECTS: initializes 9 small grids
     * and adds them to a large grid array
     */
    public LargeGrid() {
        EventLog.getInstance().logEvent(new Event("Created large grid."));
        for (int i = 0; i < 9; i++) {
            SmallGrid sg = new SmallGrid();
            largeGrid.add(sg);
        }
    }

    /*
     * REQUIRES: a linked hash map of the positions
     * EFFECTS: returns the positions of each small grid
     */
    public ArrayList<LinkedHashMap<Integer, String>> getGrid() {
        ArrayList<LinkedHashMap<Integer, String>> positions = new ArrayList<>();
        for (SmallGrid sg : largeGrid) {
            positions.add(sg.getSmallGrid());
        }
        return positions;
    }

    public SmallGrid getSmallGrid(int index) {
        return largeGrid.get(index - 1);
    }

    /*
     * REQUIRES: a symbol X or O
     * EFFECTS: checks if X or O has aligned
     *  3 times in the large grid
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
     * EFFECTS: checks if a row of small grids
     *  has been aligned in the large grid
     */
    private boolean isRowAligned(String symbol) {
        int times = 0;
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 3; k++) {
                if (largeGrid.get(((3 * i) + (k + 1)) - 1).hasSmallWon()
                        && largeGrid.get(((3 * i) + (k + 1)) - 1).getWinnerSymbol().equals(symbol)) {
                    times++;
                }
            }
            if (times == 3) {
                return true;
            } else {
                times = 0;
            }
        }
        return false;
    }

    /*
     * REQUIRES: a symbol X or O
     * EFFECTS: checks if a column of small grids
     *  has been aligned in the large grid
     */
    private boolean isColumnAligned(String symbol) {
        int times = 0;
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 3; k++) {
                if (largeGrid.get(((3 * k) + (i + 1)) - 1).hasSmallWon()
                        && largeGrid.get(((3 * k) + (i + 1)) - 1).getWinnerSymbol().equals(symbol)) {
                    times++;
                }
            }
            if (times == 3) {
                return true;
            } else {
                times = 0;
            }
        }
        return false;
    }

    /*
     * REQUIRES: a symbol X or O
     * EFFECTS: checks if the diagonals of small grids
     *  has been aligned in the large grid
     */
    private boolean isDiagonalAligned(String symbol) {
        return checkLeftDiagonal(symbol) || checkRightDiagonal(symbol);
    }

    /*
     * REQUIRES: a symbol X or O
     * EFFECTS: checks if a left diagonal of small grids
     *  has been aligned in the large grid
     */
    private boolean checkLeftDiagonal(String symbol) {
        int times = 0;
        for (int i = 0; i < 3; i++) {
            if (largeGrid.get(((3 * i) + (i + 1)) - 1).hasSmallWon()
                    && largeGrid.get(((3 * i) + (i + 1)) - 1).getWinnerSymbol().equals(symbol)) {
                times++;
            }
        }
        if (times == 3) {
            return true;
        } else {
            times = 0;
        }
        return false;
    }

    /*
     * REQUIRES: a symbol X or O
     * EFFECTS: checks if a right diagonal of small grids
     *  has been aligned in the large grid
     */
    private boolean checkRightDiagonal(String symbol) {
        int times = 0;
        int row = 2;
        for (int i = 0; i < 3; i++) {
            if (largeGrid.get(((3 * (row - i)) + (i + 1)) - 1).hasSmallWon()
                    && largeGrid.get(((3 * (row - i)) + (i + 1)) - 1).getWinnerSymbol().equals(symbol)) {
                times++;
            }
        }
        if (times == 3) {
            return true;
        } else {
            times = 0;
        }

        return false;
    }

    /*
     * EFFECTS: converts the grid to String
     */
    @Override
    public String toString() {
        String s = "\n  ";

        for (int i = 0; i < 9; i++) {
            s += "\n";
            if (i % 3 == 0) {
                s += "---------------";
            }
            s += " " + largeGrid.get(i).toString() + " ";
        }
        return s;
    }
}