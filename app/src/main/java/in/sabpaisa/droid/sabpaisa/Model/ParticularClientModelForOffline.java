package in.sabpaisa.droid.sabpaisa.Model;

public class ParticularClientModelForOffline {

    String clientId,clientName,state;
    String clientImagePath,clientLogoPath;


    public ParticularClientModelForOffline() {
    }

    public ParticularClientModelForOffline(String clientId, String clientName, String state, String clientImagePath, String clientLogoPath) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.state = state;
        this.clientImagePath = clientImagePath;
        this.clientLogoPath = clientLogoPath;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getClientImagePath() {
        return clientImagePath;
    }

    public void setClientImagePath(String clientImagePath) {
        this.clientImagePath = clientImagePath;
    }

    public String getClientLogoPath() {
        return clientLogoPath;
    }

    public void setClientLogoPath(String clientLogoPath) {
        this.clientLogoPath = clientLogoPath;
    }
}
