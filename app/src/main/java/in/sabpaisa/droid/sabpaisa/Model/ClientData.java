package in.sabpaisa.droid.sabpaisa.Model;

/**
 * Created by rajdeeps on 11/24/17.
 */



public class ClientData {

    int id;
    String clientId,clientName,clientCode,clientContact,clientEmail,
            clientImagePath,clientLink,clientAuthenticationType,paymentMode,
            bid,state,address,productName,service,successUrl,failedUrl,clientLogoPath,landingPage;

    public ClientData() {
    }

    public ClientData(int id, String clientId, String clientName, String clientCode, String clientContact, String clientEmail, String clientImagePath, String clientLink, String clientAuthenticationType, String paymentMode, String bid, String state, String address, String productName, String service, String successUrl, String failedUrl, String clientLogoPath, String landingPage) {
        this.id = id;
        this.clientId = clientId;
        this.clientName = clientName;
        this.clientCode = clientCode;
        this.clientContact = clientContact;
        this.clientEmail = clientEmail;
        this.clientImagePath = clientImagePath;
        this.clientLink = clientLink;
        this.clientAuthenticationType = clientAuthenticationType;
        this.paymentMode = paymentMode;
        this.bid = bid;
        this.state = state;
        this.address = address;
        this.productName = productName;
        this.service = service;
        this.successUrl = successUrl;
        this.failedUrl = failedUrl;
        this.clientLogoPath = clientLogoPath;
        this.landingPage = landingPage;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getClientContact() {
        return clientContact;
    }

    public void setClientContact(String clientContact) {
        this.clientContact = clientContact;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getClientImagePath() {
        return clientImagePath;
    }

    public void setClientImagePath(String clientImagePath) {
        this.clientImagePath = clientImagePath;
    }

    public String getClientLink() {
        return clientLink;
    }

    public void setClientLink(String clientLink) {
        this.clientLink = clientLink;
    }

    public String getClientAuthenticationType() {
        return clientAuthenticationType;
    }

    public void setClientAuthenticationType(String clientAuthenticationType) {
        this.clientAuthenticationType = clientAuthenticationType;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getFailedUrl() {
        return failedUrl;
    }

    public void setFailedUrl(String failedUrl) {
        this.failedUrl = failedUrl;
    }

    public String getClientLogoPath() {
        return clientLogoPath;
    }

    public void setClientLogoPath(String clientLogoPath) {
        this.clientLogoPath = clientLogoPath;
    }

    public String getLandingPage() {
        return landingPage;
    }

    public void setLandingPage(String landingPage) {
        this.landingPage = landingPage;
    }
}

