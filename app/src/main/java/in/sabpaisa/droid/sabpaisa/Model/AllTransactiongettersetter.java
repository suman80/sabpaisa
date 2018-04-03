package in.sabpaisa.droid.sabpaisa.Model;

/**
 * Created by archana on 21/3/18.
 */

public class AllTransactiongettersetter {

    String userAcceessToken;
    String id;
    String paidAmount;
    String transcationDate;
    String clientName;
    String spTranscationId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status;

    public AllTransactiongettersetter(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentStatus() {

        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    String paymentStatus;

    public String getUserAcceessToken() {
        return userAcceessToken;
    }

    public void setUserAcceessToken(String userAcceessToken) {
        this.userAcceessToken = userAcceessToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getTranscationDate() {
        return transcationDate;
    }

    public String setTranscationDate(String transcationDate) {
        this.transcationDate = transcationDate;
        return transcationDate;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getSpTranscationId() {
        return spTranscationId;
    }

    public void setSpTranscationId(String spTranscationId) {
        this.spTranscationId = spTranscationId;
    }

    public AllTransactiongettersetter() {

    }

    public AllTransactiongettersetter(String userAcceessToken, String id, String paidAmount, String transcationDate, String clientName, String spTranscationId) {

        this.userAcceessToken = userAcceessToken;
        this.id = id;
        this.paidAmount = paidAmount;
        this.transcationDate = transcationDate;
        this.clientName = clientName;
        this.spTranscationId = spTranscationId;
    }
}
