package ATM.state;

import ATM.ATMService;
import ATM.Card;
import ATM.Types.TransactionType;

public class CardInsertedATMState implements ATMState {
    @Override
    public void insertCard(ATMService atmService, String cardNumber) {
        System.out.println("Error: Card is already present.");
    }

    @Override
    public void enterPin(ATMService atmService, String pin) {
         boolean isAuth = atmService.validate(pin);
         if(!isAuth){
             ejectCard(atmService);
             return;
         }
         atmService.setCurrentState(new AuthenticatedATMState());
    }

    @Override
    public void performOperation(ATMService atmService, TransactionType type, double... args) {
         System.out.println("Error: Please insert a card first.");
    }

    @Override
    public void ejectCard(ATMService atmService) {
          atmService.setCurrentCard(null);
          atmService.setCurrentState(new IdleATMState());
    }
}
