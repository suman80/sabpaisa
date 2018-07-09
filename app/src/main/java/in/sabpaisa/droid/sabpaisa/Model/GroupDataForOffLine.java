package in.sabpaisa.droid.sabpaisa.Model;

public class GroupDataForOffLine {

    public String groupId,groupText,groupName,clientId,memberStatus,groupImage,groupLogo;



    public GroupDataForOffLine() {
    }

    public GroupDataForOffLine(String groupId, String groupText, String groupName, String clientId, String memberStatus, String groupImage, String groupLogo) {
        this.groupId = groupId;
        this.groupText = groupText;
        this.groupName = groupName;
        this.clientId = clientId;
        this.memberStatus = memberStatus;
        this.groupImage = groupImage;
        this.groupLogo = groupLogo;
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

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public String getGroupLogo() {
        return groupLogo;
    }

    public void setGroupLogo(String groupLogo) {
        this.groupLogo = groupLogo;
    }
}
