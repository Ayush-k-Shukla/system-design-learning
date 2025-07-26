package ATM.state;

import ATM.ATMService;
import ATM.Card;
import ATM.Types.TransactionType;

public class IdleATMState implements ATMState {
    @Override
    public void insertCard(ATMService atmService, String cardNumber) {
          Card card = atmService.getCard(cardNumber);
          if(card == null){
              System.out.println("Error: Please enter a valid card first.");
              return;
          }
          atmService.setCurrentCard(card);
    }

    @Override
    public void enterPin(ATMService atmService, String pin) {
         System.out.println("Error: Please insert a card first.");
    }

    @Override
    public void performOperation(ATMService atmService, TransactionType type, double... args) {
         System.out.println("Error: Please insert a card first.");
    }

    @Override
    public void ejectCard(ATMService atmService) {
          atmService.setCurrentCard(null);
    }
}
