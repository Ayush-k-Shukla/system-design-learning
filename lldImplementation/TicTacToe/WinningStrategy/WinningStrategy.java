package TicTacToe.WinningStrategy;

import TicTacToe.Board;
import TicTacToe.Game;
import TicTacToe.Types.Symbol;

public interface WinningStrategy {
    boolean checkWin(Board board, Symbol lastMove);
}