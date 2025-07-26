package ATM.state;

import ATM.ATMService;
import ATM.Types.TransactionType;

public class AuthenticatedATMState implements ATMState {
    @Override
    public void insertCard(ATMService atmService, String cardNumber) {
        System.out.println("Error: Card is already present.");
    }

    @Override
    public void enterPin(ATMService atmService, String pin) {
        System.out.println("Error: PIN is already present.");
    }

    @Override
    public void performOperation(ATMService atmService, TransactionType type, double... args) {
         switch (type){
             case DEPOSIT -> atmService.deposit(args[0]);
             case INQUIRY -> atmService.checkBalance();
             case WITHDRAW -> atmService.withdraw(args[0]);
         }
    }

    @Override
    public void ejectCard(ATMService atmService) {
          atmService.setCurrentCard(null);
          atmService.setCurrentState(new IdleATMState());
    }
}
