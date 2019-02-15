package in.sabpaisa.droid.sabpaisa.Model;

public class MemberSpaceModel {

    String memberId,roleId,roleName,userId,fullName,address,contactNumber,dateOfBirth,
            emailId,password,accessTokenFb,accessTokenGoogle,deviceId,userAccessToken,
            timestampOfRegistration,status,dobString,userImageUrl;

    boolean isSelected;

    public MemberSpaceModel(String memberId, String roleId, String roleName, String userId, String fullName, String address, String contactNumber, String dateOfBirth, String emailId, String password, String accessTokenFb, String accessTokenGoogle, String deviceId, String userAccessToken, String timestampOfRegistration, String status, String dobString, String userImageUrl, boolean isSelected) {
        this.memberId = memberId;
        this.roleId = roleId;
        this.roleName = roleName;
        this.userId = userId;
        this.fullName = fullName;
        this.address = address;
        this.contactNumber = contactNumber;
        this.dateOfBirth = dateOfBirth;
        this.emailId = emailId;
        this.password = password;
        this.accessTokenFb = accessTokenFb;
        this.accessTokenGoogle = accessTokenGoogle;
        this.deviceId = deviceId;
        this.userAccessToken = userAccessToken;
        this.timestampOfRegistration = timestampOfRegistration;
        this.status = status;
        this.dobString = dobString;
        this.userImageUrl = userImageUrl;
        this.isSelected = isSelected;
    }

    public MemberSpaceModel() {
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccessTokenFb() {
        return accessTokenFb;
    }

    public void setAccessTokenFb(String accessTokenFb) {
        this.accessTokenFb = accessTokenFb;
    }

    public String getAccessTokenGoogle() {
        return accessTokenGoogle;
    }

    public void setAccessTokenGoogle(String accessTokenGoogle) {
        this.accessTokenGoogle = accessTokenGoogle;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserAccessToken() {
        return userAccessToken;
    }

    public void setUserAccessToken(String userAccessToken) {
        this.userAccessToken = userAccessToken;
    }

    public String getTimestampOfRegistration() {
        return timestampOfRegistration;
    }

    public void setTimestampOfRegistration(String timestampOfRegistration) {
        this.timestampOfRegistration = timestampOfRegistration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDobString() {
        return dobString;
    }

    public void setDobString(String dobString) {
        this.dobString = dobString;
    }


    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
