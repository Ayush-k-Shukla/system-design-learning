package ATM;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BankService {
    private Map<String, Account> accounts;
    private Map<String, Account> cardToAccountMapping;
    private Map<String, Card> cards;


    public BankService(){
        accounts = new ConcurrentHashMap<>();
        cardToAccountMapping = new ConcurrentHashMap<>();
        cards = new ConcurrentHashMap<>();
    }

    public boolean createAccount(String accountNumber, double balance){
        Account newA = new Account(accountNumber, balance);
        accounts.put(accountNumber, newA);
        return true;
    }

    public void createCard(String cardNumber, String pin, String accountNumber){
        Card newCard = new Card(cardNumber, pin);
        accounts.get(accountNumber).addCard(newCard);
        cardToAccountMapping.put(cardNumber, accounts.get(accountNumber));
        cards.put(cardNumber, newCard);
    }

    public boolean validateCard(Card card, String pin){
        return card.getPin().equals(pin);
    }

    public Card getCard(String cardName){
        return cards.getOrDefault(cardName, null);
    }

    public double getBalance(Card card){
        return cardToAccountMapping.get(card.getCardNumber()).getBalance();
    }

    public void deposit(String cardNumber, double amount){
        Account ac = cardToAccountMapping.get(cardNumber);
        ac.deposit(amount);
    }

    public boolean withdraw(String cardNumber, double amount){
        Account ac = cardToAccountMapping.get(cardNumber);
        return ac.withdraw(amount);
    }
}
