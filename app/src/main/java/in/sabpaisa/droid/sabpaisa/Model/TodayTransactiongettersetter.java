package in.sabpaisa.droid.sabpaisa.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class TodayTransactiongettersetter {
    private  String amount;
    private String txnId;
    private  String txnDate;
    private  String txnStatus;
    private String payerName;

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }

    public String getTxnStatus() {
        return txnStatus;
    }

    public void setTxnStatus(String txnStatus) {
        this.txnStatus = txnStatus;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getTransactions() {
        return amount;
    }

    public void setTransactions(String transactions) {
        this.amount = transactions;
    }
}
