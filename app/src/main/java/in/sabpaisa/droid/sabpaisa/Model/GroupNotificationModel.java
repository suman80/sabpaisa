package in.sabpaisa.droid.sabpaisa.Model;

public class GroupNotificationModel {

    String groupId;
    int groupNotificationCount;
    long groupRecentCommentTimeStamp;
    long groupRecentOpenCommentTimeStamp;
    boolean isGroupOpen;

    public GroupNotificationModel() {
    }


    public GroupNotificationModel(String groupId, int groupNotificationCount, long groupRecentCommentTimeStamp, long groupRecentOpenCommentTimeStamp, boolean isGroupOpen) {
        this.groupId = groupId;
        this.groupNotificationCount = groupNotificationCount;
        this.groupRecentCommentTimeStamp = groupRecentCommentTimeStamp;
        this.groupRecentOpenCommentTimeStamp = groupRecentOpenCommentTimeStamp;
        this.isGroupOpen = isGroupOpen;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getGroupNotificationCount() {
        return groupNotificationCount;
    }

    public void setGroupNotificationCount(int groupNotificationCount) {
        this.groupNotificationCount = groupNotificationCount;
    }

    public long getGroupRecentCommentTimeStamp() {
        return groupRecentCommentTimeStamp;
    }

    public void setGroupRecentCommentTimeStamp(long groupRecentCommentTimeStamp) {
        this.groupRecentCommentTimeStamp = groupRecentCommentTimeStamp;
    }

    public long getGroupRecentOpenCommentTimeStamp() {
        return groupRecentOpenCommentTimeStamp;
    }

    public void setGroupRecentOpenCommentTimeStamp(long groupRecentOpenCommentTimeStamp) {
        this.groupRecentOpenCommentTimeStamp = groupRecentOpenCommentTimeStamp;
    }


    public boolean isGroupOpen() {
        return isGroupOpen;
    }

    public void setGroupOpen(boolean groupOpen) {
        isGroupOpen = groupOpen;
    }
}
