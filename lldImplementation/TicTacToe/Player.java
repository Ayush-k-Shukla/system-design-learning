package TicTacToe;

import TicTacToe.Types.Symbol;

import java.util.UUID;

public class Player {
    private final String name;
    private final String id;
    private final Symbol symbol;

    public Player(String name, Symbol symbol) {
        this.name = name;
        this.symbol = symbol;
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Symbol getSymbol() {
        return symbol;
    }
}
