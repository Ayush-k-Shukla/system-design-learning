package ATM;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Account {
    private String accountNumber;
    private Map<String, Card> cards;
    private double balance;

    public Account(String accountNumber, double balance) {
        this.balance = balance;
        this.accountNumber = accountNumber;
        cards = new ConcurrentHashMap<>();
    }

    public double getBalance() {
        return balance;
    }

    public synchronized void deposit(double amount) {
        balance += amount;
    }

    public synchronized boolean withdraw(double amount) {
        if (balance < amount) {
            return false;
        }
        balance -= amount;
        return true;
    }

    public Map<String, Card> getCards() {
        return cards;
    }

    public synchronized void addCard(Card card) {
        cards.put(card.getCardNumber(), card);
    }
}
