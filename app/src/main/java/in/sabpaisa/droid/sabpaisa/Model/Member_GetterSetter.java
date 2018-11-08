package in.sabpaisa.droid.sabpaisa.Model;

/**
 * Created by rajdeeps on 12/28/17.
 */

public class Member_GetterSetter {
   public String id,userId,groupId,timestampOfJoining,status,fullName,
            emailId,phoneNumber,deviceId,userImageUrl,uin,CONTACT_NUMBER,userAccessToken,uin_Status;

   boolean isSelected ;

   boolean checked;

   public String uin_Role,roleId,roleName;

    public Member_GetterSetter() {
    }


    public Member_GetterSetter(String id, String userId, String groupId, String timestampOfJoining, String status, String fullName, String emailId, String phoneNumber, String deviceId, String userImageUrl, String uin, String CONTACT_NUMBER, String userAccessToken, String uin_Status, boolean isSelected, boolean checked, String uin_Role, String roleId, String roleName) {
        this.id = id;
        this.userId = userId;
        this.groupId = groupId;
        this.timestampOfJoining = timestampOfJoining;
        this.status = status;
        this.fullName = fullName;
        this.emailId = emailId;
        this.phoneNumber = phoneNumber;
        this.deviceId = deviceId;
        this.userImageUrl = userImageUrl;
        this.uin = uin;
        this.CONTACT_NUMBER = CONTACT_NUMBER;
        this.userAccessToken = userAccessToken;
        this.uin_Status = uin_Status;
        this.isSelected = isSelected;
        this.checked = checked;
        this.uin_Role = uin_Role;
        this.roleId = roleId;
        this.roleName = roleName;
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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTimestampOfJoining() {
        return timestampOfJoining;
    }

    public void setTimestampOfJoining(String timestampOfJoining) {
        this.timestampOfJoining = timestampOfJoining;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getUin() {
        return uin;
    }

    public void setUin(String uin) {
        this.uin = uin;
    }

    public String getCONTACT_NUMBER() {
        return CONTACT_NUMBER;
    }

    public void setCONTACT_NUMBER(String CONTACT_NUMBER) {
        this.CONTACT_NUMBER = CONTACT_NUMBER;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getUserAccessToken() {
        return userAccessToken;
    }

    public void setUserAccessToken(String userAccessToken) {
        this.userAccessToken = userAccessToken;
    }


    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }


    public String getUin_Role() {
        return uin_Role;
    }

    public void setUin_Role(String uin_Role) {
        this.uin_Role = uin_Role;
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


    public String getUin_Status() {
        return uin_Status;
    }

    public void setUin_Status(String uin_Status) {
        this.uin_Status = uin_Status;
    }
}
