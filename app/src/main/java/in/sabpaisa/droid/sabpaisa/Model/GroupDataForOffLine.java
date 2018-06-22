package in.sabpaisa.droid.sabpaisa.Model;

public class GroupDataForOffLine {

    public String groupId,groupText,groupName,clientId,memberStatus;
    byte[] imagePath,logoPath;


    public GroupDataForOffLine() {
    }


    public GroupDataForOffLine(String groupId, String groupText, String groupName, String clientId, String memberStatus, byte[] imagePath, byte[] logoPath) {
        this.groupId = groupId;
        this.groupText = groupText;
        this.groupName = groupName;
        this.clientId = clientId;
        this.memberStatus = memberStatus;
        this.imagePath = imagePath;
        this.logoPath = logoPath;
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

    public byte[] getImagePath() {
        return imagePath;
    }

    public void setImagePath(byte[] imagePath) {
        this.imagePath = imagePath;
    }

    public byte[] getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(byte[] logoPath) {
        this.logoPath = logoPath;
    }
}
