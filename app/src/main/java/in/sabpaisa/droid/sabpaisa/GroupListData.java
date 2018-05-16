package in.sabpaisa.droid.sabpaisa;

/**
 * Created by SabPaisa on 03-07-2017.
 */

public class GroupListData {

    public String groupId,groupText,groupName,clientId,createdDate,imagePath,logoPath,status,memberStatus;

    public GroupListData() {
    }

    public GroupListData(String groupId, String groupText, String groupName, String clientId, String createdDate, String imagePath, String logoPath, String status, String memberStatus) {
        this.groupId = groupId;
        this.groupText = groupText;
        this.groupName = groupName;
        this.clientId = clientId;
        this.createdDate = createdDate;
        this.imagePath = imagePath;
        this.logoPath = logoPath;
        this.status = status;
        this.memberStatus = memberStatus;
    }


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupText() {
        return groupText;
    }

    public void setGroupText(String groupText) {
        this.groupText = groupText;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }
}

