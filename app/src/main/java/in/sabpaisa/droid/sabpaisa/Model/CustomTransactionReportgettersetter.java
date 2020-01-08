package in.sabpaisa.droid.sabpaisa.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomTransactionReportgettersetter implements Parcelable {

    private String customTxnId;
    private  String customTxnDate;
    private  String customTxnAmount;
    private  String customTxnStatus;
    private  String payer_name;


    public  CustomTransactionReportgettersetter(String amount,String txnId,String txnDate,String txnStatus,String payerName) {
        this.customTxnAmount = amount;
        this.customTxnId = txnId;
        this.customTxnDate = txnDate;
        this.customTxnStatus = txnStatus;
        this.payer_name = payerName;

    }


    protected CustomTransactionReportgettersetter(Parcel in) {
        customTxnId = in.readString();
        customTxnDate = in.readString();
        customTxnAmount = in.readString();
        customTxnStatus = in.readString();
        payer_name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(customTxnId);
        dest.writeString(customTxnDate);
        dest.writeString(customTxnAmount);
        dest.writeString(customTxnStatus);
        dest.writeString(payer_name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CustomTransactionReportgettersetter> CREATOR = new Creator<CustomTransactionReportgettersetter>() {
        @Override
        public CustomTransactionReportgettersetter createFromParcel(Parcel in) {
            return new CustomTransactionReportgettersetter(in);
        }

        @Override
        public CustomTransactionReportgettersetter[] newArray(int size) {
            return new CustomTransactionReportgettersetter[size];
        }
    };

    public String getPayer_name() {
        return payer_name;
    }

    public void setPayer_name(String payer_name) {
        this.payer_name = payer_name;
    }

    public String getCustomTxnId() {
        return customTxnId;
    }

    public void setCustomTxnId(String customTxnId) {
        this.customTxnId = customTxnId;
    }

    public String getCustomTxnDate() {
        return customTxnDate;
    }

    public void setCustomTxnDate(String customTxnDate) {
        this.customTxnDate = customTxnDate;
    }

    public String getCustomTxnAmount() {
        return customTxnAmount;
    }

    public void setCustomTxnAmount(String customTxnAmount) {
        this.customTxnAmount = customTxnAmount;
    }

    public String getCustomTxnStatus() {
        return customTxnStatus;
    }

    public void setCustomTxnStatus(String customTxnStatus) {
        this.customTxnStatus = customTxnStatus;
    }
    @Override
    public String toString() {
        return "{" +
                "amount='" + customTxnAmount + '\'' +
                ", txnId='" + customTxnId + '\'' +
                ", txnDate='" + customTxnDate + '\'' +
                ", txnStatus='" + customTxnStatus + '\'' +
                ", payerName='" + payer_name+ '\'' +
                '}';
    }

}
