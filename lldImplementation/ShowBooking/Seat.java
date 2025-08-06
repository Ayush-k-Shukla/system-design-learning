package ShowBooking;

import ShowBooking.Types.SeatStatus;
import ShowBooking.Types.SeatType;

public class Seat {
    private int row;
    private int column;
    private SeatStatus status;
    private SeatType type;

    public Seat(int row, int column, SeatStatus status, SeatType type){
        this.row = row;
        this.column = column;
        this.status = status;
        this.type = type;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public SeatType getType() {
        return type;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public synchronized void setStatus(SeatStatus status) {
        this.status = status;
    }
}
