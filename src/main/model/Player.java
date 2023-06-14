package model;

public final class Player {

    private final String symbol;


    public Player(String symbol) {
        this.symbol = symbol;
        EventLog.getInstance().logEvent(new Event("Created player " + symbol + "."));
    }


    public String getSymbol() {
        return symbol;
    }
}
