package in.sabpaisa.droid.sabpaisa.Model;

public class MemberOfflineDataModel {

    public String fullName,userImageUrl;

    public MemberOfflineDataModel() {
    }


    public MemberOfflineDataModel(String fullName, String userImageUrl) {
        this.fullName = fullName;
        this.userImageUrl = userImageUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }
}
