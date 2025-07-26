package ATM;

import java.util.UUID;

public class Card {
    private String id;
    private String cardNumber;
    private String pin;

    public Card(String cardNumber, String pin){
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.id = UUID.randomUUID().toString();
    }

    public String getCardNumber(){
        return cardNumber;
    }

    public String getPin(){
        return pin;
    }
}
