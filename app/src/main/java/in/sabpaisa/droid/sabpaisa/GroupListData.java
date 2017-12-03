package in.sabpaisa.droid.sabpaisa;

/**
 * Created by SabPaisa on 03-07-2017.
 */

public class GroupListData {
    int clientId,groupId;
    String groupName,groupText,createdDate,imagePath,logoPath;

    public GroupListData() {
    }

    public GroupListData(int clientId, int groupId, String groupName, String groupText, String createdDate, String imagePath, String logoPath) {
        this.clientId = clientId;
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupText = groupText;
        this.createdDate = createdDate;
        this.imagePath = imagePath;
        this.logoPath = logoPath;
    }


    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupText() {
        return groupText;
    }

    public void setGroupText(String groupText) {
        this.groupText = groupText;
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
}

