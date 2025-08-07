package TicTacToe;

import TicTacToe.Types.GameStatus;
import TicTacToe.WinningStrategy.WinningStrategy;

import java.util.*;

public class TicTacToeGame {
    private static TicTacToeGame instance;

    private Map<String, Player> playerMap;
    private Map<String, Game> games;
    private List<WinningStrategy> strategies;


    private TicTacToeGame(){
        playerMap = new HashMap<>();
        games = new HashMap<>();
        strategies = new ArrayList<>();
    }

    // Singleton
    public static TicTacToeGame getInstance(){
        if(instance==null){
            instance = new TicTacToeGame();
        }
        return instance;
    }

    public String startGame(List<Player> players, int size){
        Board board = new Board(size);
        Game game = new Game(board, players, strategies);
        games.put(game.getId(), game);
        return game.getId();
    }

    public void playMove(String gameId, int row, int col){
        games.get(gameId).playMove(row, col);
    }

    public void printGame(String gameId){
        games.get(gameId).print();
    }

    public GameStatus getStatus(String gameId){
      return games.get(gameId).getStatus();
    }

    public void setStrategies(List<WinningStrategy> strategies) {
        this.strategies = strategies;
    }
}
