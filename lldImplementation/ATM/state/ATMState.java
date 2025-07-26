package ATM.state;

import ATM.ATMService;
import ATM.Card;
import ATM.Types.TransactionType;

public interface ATMState {
     void insertCard(ATMService atmService, String cardNumber);
     void enterPin(ATMService atmService, String pin);
     void performOperation(ATMService atmService, TransactionType type, double... args);
     void ejectCard(ATMService atmService);
}
