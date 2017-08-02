package in.sabpaisa.droid.sabpaisa;

import in.sabpaisa.droid.sabpaisa.Model.Bank;

/**
 * Created by SabPaisa on 27-07-2017.
 */

class BankHistory{
    String formName;
    int amount;

    public BankHistory(String formName, int amount) {
        this.formName = formName;
        this.amount = amount;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
