package TicTacToe.WinningStrategy;

import TicTacToe.Board;
import TicTacToe.Types.Symbol;

public class DiagonalWinningStrategy implements WinningStrategy {
    @Override
    public boolean checkWin(Board board, Symbol lastMove){
        boolean mainDiagonalMatch = true;
        for (int i = 0; i < board.getSize(); i++) {
            if (!board.getCell(i, i).getValue().equals(lastMove.getValue())) {
                mainDiagonalMatch = false;
                break;
            }
        }
        if (mainDiagonalMatch) return true;

        boolean antiDiagonalMatch = true;
        for (int i = 0; i < board.getSize(); i++) {

            if (!board.getCell(i, board.getSize() - 1 - i).getValue().equals(lastMove.getValue())) {
                antiDiagonalMatch = false;
                break;
            }
        }
        return antiDiagonalMatch;
    }
}