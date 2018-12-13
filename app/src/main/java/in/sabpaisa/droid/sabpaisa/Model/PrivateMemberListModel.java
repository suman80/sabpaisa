package in.sabpaisa.droid.sabpaisa.Model;

public class PrivateMemberListModel {

    String id,userId,userImageUrl,fullName,contactNumber,userAccessToken;

    public PrivateMemberListModel() {
    }

    public PrivateMemberListModel(String id, String userId, String userImageUrl, String fullName, String contactNumber, String userAccessToken) {
        this.id = id;
        this.userId = userId;
        this.userImageUrl = userImageUrl;
        this.fullName = fullName;
        this.contactNumber = contactNumber;
        this.userAccessToken = userAccessToken;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getUserAccessToken() {
        return userAccessToken;
    }

    public void setUserAccessToken(String userAccessToken) {
        this.userAccessToken = userAccessToken;
    }
}
