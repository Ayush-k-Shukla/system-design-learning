package TicTacToe.WinningStrategy;

import TicTacToe.Board;
import TicTacToe.Types.Symbol;

public class RowWinningStrategy implements WinningStrategy {
    @Override
    public boolean checkWin(Board board, Symbol lastMove){
        for (int i = 0; i < board.getSize(); i++) {
            boolean allMatch = true;
            for (int j = 0; j < board.getSize(); j++) {
                if (!board.getCell(i, j).getValue().equals(lastMove.getValue())) {
                    allMatch = false;
                    break;
                }
            }
            if (allMatch) return true;
        }
        return false;
    }
}