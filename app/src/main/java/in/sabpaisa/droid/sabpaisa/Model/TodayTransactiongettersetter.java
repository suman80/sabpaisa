package in.sabpaisa.droid.sabpaisa.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class TodayTransactiongettersetter implements Parcelable{
    private  String amount;
    private String txnId;
    private  String txnDate;
    private  String txnStatus;
    private String payerName;


    public  TodayTransactiongettersetter(String amount,String txnId,String txnDate,String txnStatus,String payerName) {
        this.amount = amount;
        this.txnId = txnId;
        this.txnDate = txnDate;
        this.txnStatus = txnStatus;
        this.payerName = payerName;

    }



     public TodayTransactiongettersetter(Parcel in) {
         amount = in.readString();
         txnId = in.readString();
         txnDate = in.readString();
         txnStatus = in.readString();
         payerName = in.readString();
     }

     public static final Creator<TodayTransactiongettersetter> CREATOR = new Creator<TodayTransactiongettersetter>() {
         @Override
         public TodayTransactiongettersetter createFromParcel(Parcel in) {
             return new TodayTransactiongettersetter(in);
         }

         @Override
         public TodayTransactiongettersetter[] newArray(int size) {
             return new TodayTransactiongettersetter[size];
         }
     };

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

    @Override
    public String toString() {
        return "{" +
                "amount='" + amount + '\'' +
                ", txnId='" + txnId + '\'' +
                ", txnDate='" + txnDate + '\'' +
                ", txnStatus='" + txnStatus + '\'' +
                ", payerName='" + payerName + '\'' +
                '}';
    }

 @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(amount);
        dest.writeString(txnId);
        dest.writeString(txnDate);
        dest.writeString(txnStatus);
        dest.writeString(payerName);
    }
}
