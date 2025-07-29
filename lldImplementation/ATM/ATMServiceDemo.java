package ATM;

import ATM.Types.TransactionType;

public class ATMServiceDemo {

    public static void run(){
        ATMService atmService = ATMService.getInstance();

        atmService.insertCard("abc123");
        atmService.enterPin("123");
        atmService.performOperation(TransactionType.INQUIRY);


        System.out.println(atmService.getTransactions().toArray().length);
    }
}
