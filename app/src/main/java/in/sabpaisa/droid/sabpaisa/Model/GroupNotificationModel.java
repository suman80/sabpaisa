package in.sabpaisa.droid.sabpaisa.Model;

public class GroupNotificationModel {

    String groupId;
    int groupNotificationCount;
    long groupRecentCommentTimeStamp;
    long groupRecentOpenCommentTimeStamp;

    public GroupNotificationModel() {
    }


    public GroupNotificationModel(String groupId, int groupNotificationCount, long groupRecentCommentTimeStamp, long groupRecentOpenCommentTimeStamp) {
        this.groupId = groupId;
        this.groupNotificationCount = groupNotificationCount;
        this.groupRecentCommentTimeStamp = groupRecentCommentTimeStamp;
        this.groupRecentOpenCommentTimeStamp = groupRecentOpenCommentTimeStamp;
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
}
