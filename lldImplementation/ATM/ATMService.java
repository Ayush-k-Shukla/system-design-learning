package ATM;

import ATM.Types.TransactionType;
import ATM.state.ATMState;
import ATM.state.IdleATMState;

import java.util.List;

public class ATMService {

    private static ATMService instance;
    private final BankService bankService;

    private double availableAmount;
    private Card currentCard;
    private ATMState currentState;
    private List<Transaction> transactions;

    private ATMService(){
        bankService = new BankService();
        availableAmount = 1000;
        currentCard = null;
        currentState = new IdleATMState();
    }

    public ATMService getInstance(){
        if(instance==null){
            instance = new ATMService();
        }
        return instance;
    }

    public void setCurrentState(ATMState state){
        currentState = state;
    }

    public void setCurrentCard(Card card){
        currentCard = card;
    }

    public void insertCard(String cardNumber) {
        currentState.insertCard(this, cardNumber);
    }

    public void enterPin( String pin) {
        currentState.enterPin(this, pin);
    }

    public void performOperation(TransactionType type, double... args) {
       currentState.performOperation(this, type, args);
    }

    public double checkBalance(){
        pushTransactionLog(TransactionType.INQUIRY);
        return bankService.getBalance(currentCard);
    }

    public void deposit(double amount){
        pushTransactionLog(TransactionType.DEPOSIT, amount);
        bankService.deposit(currentCard.getCardNumber(), amount);
    }

    public boolean withdraw(double amount){
        pushTransactionLog(TransactionType.WITHDRAW, amount);
        if(!canDispense(amount)) return false;
        return bankService.withdraw(currentCard.getCardNumber(), amount);
    }

    public Card getCard(String cardName){

        return bankService.getCard(cardName);
    }

    public boolean validate(String pin){
        if(currentCard==null) return false;
        return bankService.validateCard(currentCard, pin);
    }

    private synchronized boolean canDispense(double amount){
        return availableAmount > amount;
    }

    private void pushTransactionLog(TransactionType type, double ...args){
        transactions.add(new Transaction(type, args[0]));
    }
}
