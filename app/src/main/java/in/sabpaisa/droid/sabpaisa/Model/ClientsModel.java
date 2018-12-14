package in.sabpaisa.droid.sabpaisa.Model;

public class ClientsModel {
    String clientId,client_Name,state,stateId,clientLogoPath,clientImagePath;
    long timeStampForParticularClient;

    public ClientsModel() {
    }

    public ClientsModel(String clientId, String client_Name, String state, String stateId, String clientLogoPath, String clientImagePath, long timeStampForParticularClient) {
        this.clientId = clientId;
        this.client_Name = client_Name;
        this.state = state;
        this.stateId = stateId;
        this.clientLogoPath = clientLogoPath;
        this.clientImagePath = clientImagePath;
        this.timeStampForParticularClient = timeStampForParticularClient;
    }


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClient_Name() {
        return client_Name;
    }

    public void setClient_Name(String client_Name) {
        this.client_Name = client_Name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getClientLogoPath() {
        return clientLogoPath;
    }

    public void setClientLogoPath(String clientLogoPath) {
        this.clientLogoPath = clientLogoPath;
    }

    public String getClientImagePath() {
        return clientImagePath;
    }

    public void setClientImagePath(String clientImagePath) {
        this.clientImagePath = clientImagePath;
    }

    public long getTimeStampForParticularClient() {
        return timeStampForParticularClient;
    }

    public void setTimeStampForParticularClient(long timeStampForParticularClient) {
        this.timeStampForParticularClient = timeStampForParticularClient;
    }
}
