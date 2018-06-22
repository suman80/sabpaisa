package in.sabpaisa.droid.sabpaisa.Model;

public class ParticularClientModelForOffline {

    String clientId,clientName,state;
    byte [] clientImagePath,clientLogoPath;


    public ParticularClientModelForOffline() {
    }

    public ParticularClientModelForOffline(String clientId, String clientName, String state, byte[] clientImagePath, byte[] clientLogoPath) {
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

    public byte[] getClientImagePath() {
        return clientImagePath;
    }

    public void setClientImagePath(byte[] clientImagePath) {
        this.clientImagePath = clientImagePath;
    }

    public byte[] getClientLogoPath() {
        return clientLogoPath;
    }

    public void setClientLogoPath(byte[] clientLogoPath) {
        this.clientLogoPath = clientLogoPath;
    }
}
