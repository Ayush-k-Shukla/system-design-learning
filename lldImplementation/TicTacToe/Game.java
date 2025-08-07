package TicTacToe;

import TicTacToe.Types.GameStatus;
import TicTacToe.WinningStrategy.WinningStrategy;

import java.util.List;
import java.util.UUID;

public class Game {
    private final String id;
    private final Board board;
    private final List<Player> players;
    private int activePlayerIndex;
    private GameStatus status;
    private List<WinningStrategy> strategies;

    public Game(Board board, List<Player> players, List<WinningStrategy> strategies){
       this.id = UUID.randomUUID().toString();
       this.board = board;
       this.players = players;
       activePlayerIndex = 0;
       status = GameStatus.IN_PROGRESS;
       this.strategies = strategies;
    }

    public synchronized boolean playMove(int row, int col){

        boolean isPlayable = board.isPlayable(row, col);
        if(!isPlayable){
            return false;
        }

        if(activePlayerIndex>=players.size()){
            activePlayerIndex = 0;
        }

        Player activePlayer = players.get(activePlayerIndex);
        board.playMove(row, col, activePlayer.getSymbol());

        boolean isWin = false;
        for(WinningStrategy strategy : strategies){
            if (strategy.checkWin(board, activePlayer.getSymbol())) {
                isWin = true;
                break;
            }
        }

        if(isWin){
            System.out.println("Winner is "+ activePlayer.getName());
            status = GameStatus.WIN;
            return true;
        }

        if(board.isBoardFull()){
            System.out.println("Draw it is");
            status = GameStatus.DRAW;
            return true;
        }
        activePlayerIndex++;

        return true;
    }

    public void print(){
        board.print();
    }

    public String getId() {
        return id;
    }

    public GameStatus getStatus() {
        return status;
    }
}
