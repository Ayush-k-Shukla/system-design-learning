package TicTacToe;

import TicTacToe.Types.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Board {
    private final int size;
    private List<List<Symbol>> cells;
    private final AtomicInteger movesCount = new AtomicInteger(0);

    public Board(int size){
        this.size = size;
        cells = new ArrayList<>();
        initializeBoard();
    }

    public synchronized boolean isPlayable(int row, int col){
        if(row>= size || col>=size) {
            return false;
        }
        Symbol cell = cells.get(row).get(col);
        // Already played
        return cell.getValue().equals(" ");
    }

    public synchronized void playMove(int row, int col, Symbol symbol){
        if(!isPlayable(row, col)) throw new Error("Not valid");
        Symbol cell = cells.get(row).get(col);
        cells.get(row).set(col, symbol);
        movesCount.incrementAndGet();
    }

    public boolean isBoardFull(){
        return movesCount.get() == (size*size);
    }

    public void print(){
        for(List<Symbol> li: cells){
            for(Symbol ele : li){
                System.out.print(ele.getValue() + " ");
            }
            System.out.println();
        }
    }

    public int getSize() {
        return size;
    }

    public Symbol getCell(int row, int col) {
        return cells.get(row).get(col);
    }
    private void initializeBoard(){
        for(int i = 0; i < size; i++){
            List<Symbol> row = new ArrayList<>();
            for(int j = 0; j < size; j++){
                row.add(new Symbol(" "));
            }
            cells.add(row);
        }
    }
}
