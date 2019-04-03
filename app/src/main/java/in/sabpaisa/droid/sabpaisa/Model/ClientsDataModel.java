package in.sabpaisa.droid.sabpaisa.Model;

public class ClientsDataModel {

    String clientId,clientName,userAccessToken,uinNo,cobCustId,clientImageUrl;

    public ClientsDataModel() {
    }


    public ClientsDataModel(String clientId, String clientName, String userAccessToken, String uinNo, String cobCustId, String clientImageUrl) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.userAccessToken = userAccessToken;
        this.uinNo = uinNo;
        this.cobCustId = cobCustId;
        this.clientImageUrl = clientImageUrl;
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

    public String getUserAccessToken() {
        return userAccessToken;
    }

    public void setUserAccessToken(String userAccessToken) {
        this.userAccessToken = userAccessToken;
    }

    public String getUinNo() {
        return uinNo;
    }

    public void setUinNo(String uinNo) {
        this.uinNo = uinNo;
    }

    public String getCobCustId() {
        return cobCustId;
    }

    public void setCobCustId(String cobCustId) {
        this.cobCustId = cobCustId;
    }

    public String getClientImageUrl() {
        return clientImageUrl;
    }

    public void setClientImageUrl(String clientImageUrl) {
        this.clientImageUrl = clientImageUrl;
    }
}
