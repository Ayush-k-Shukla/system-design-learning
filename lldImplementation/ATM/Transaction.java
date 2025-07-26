package ATM;

import ATM.Types.TransactionType;

import java.util.Date;

public class Transaction {
    private TransactionType type;
    private double amount;
    private Long timestamp;

    public Transaction(TransactionType type, double amount){
        this.amount = amount;
        this.type = type;
        this.timestamp = new Date().getTime();
    }
}
