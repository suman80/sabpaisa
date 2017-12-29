package in.sabpaisa.droid.sabpaisa.Model;

/**
 * Created by rajdeeps on 12/28/17.
 */

public class Member_GetterSetter {
    String id,userId,groupId,timestampOfJoining,status,fullName,
            emailId,phoneNumber,deviceId,userImageUrl,uin,CONTACT_NUMBER;

    public Member_GetterSetter() {
    }

    public Member_GetterSetter(String id, String userId, String groupId, String timestampOfJoining, String status, String fullName, String emailId, String phoneNumber, String deviceId, String userImageUrl, String uin, String CONTACT_NUMBER) {
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
}
