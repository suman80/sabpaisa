package in.sabpaisa.droid.sabpaisa.Model;

/**
 * Created by abc on 17-06-2017.
 */

public class TransactionDetail {
    boolean status;
    String userName;
    String userUpi;
    int amount;
    String date;
    String time;

    public TransactionDetail(boolean status, String userName, String userUpi, int amount, String date, String time) {
        this.status = status;
        this.userName = userName;
        this.userUpi = userUpi;
        this.amount = amount;
        this.date = date;
        this.time = time;
    }

    public boolean isStatus() {
        return status;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserUpi() {
        return userUpi;
    }

    public int getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
