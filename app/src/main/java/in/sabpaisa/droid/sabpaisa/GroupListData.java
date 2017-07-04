package in.sabpaisa.droid.sabpaisa;

/**
 * Created by SabPaisa on 03-07-2017.
 */

public class GroupListData {
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    String groupName;

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    String groupDescription;



    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    String groupId;


    public String getGroupCount(){return group_count; }

    public  void setGroupCount(String group_count)
    {
        this.group_count = group_count;

    }

    String group_count;


}

