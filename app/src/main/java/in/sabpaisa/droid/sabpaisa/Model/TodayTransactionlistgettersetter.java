package in.sabpaisa.droid.sabpaisa.Model;

import java.util.ArrayList;
import java.util.List;

public class TodayTransactionlistgettersetter {
    private  String clientName;
    private String numberoftransactions;
    private List<TodayTransactiongettersetter> todayTransactiongettersetters;


    public List<TodayTransactiongettersetter> getTodayTransactiongettersetters() {
        return todayTransactiongettersetters;
    }

    public void setTodayTransactiongettersetters(List<TodayTransactiongettersetter> todayTransactiongettersetters) {
        this.todayTransactiongettersetters = todayTransactiongettersetters;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getNumberoftransactions() {
        return numberoftransactions;
    }

    public void setNumberoftransactions(String numberoftransactions) {
        this.numberoftransactions = numberoftransactions;
    }
}
