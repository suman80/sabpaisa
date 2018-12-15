package in.sabpaisa.droid.sabpaisa.Model;

public class FeedNotificatonModel {

    String feedId;
    int feedNotificationCount;
    long feedRecentCommentTimeStamp;
    long feedRecentOpenCommentTimeStamp;
    boolean feedIsOpen;
    String feedMode,feedGroupId;


    public FeedNotificatonModel() {
    }

    public FeedNotificatonModel(String feedId, int feedNotificationCount, long feedRecentCommentTimeStamp, long feedRecentOpenCommentTimeStamp, boolean feedIsOpen, String feedMode, String feedGroupId) {
        this.feedId = feedId;
        this.feedNotificationCount = feedNotificationCount;
        this.feedRecentCommentTimeStamp = feedRecentCommentTimeStamp;
        this.feedRecentOpenCommentTimeStamp = feedRecentOpenCommentTimeStamp;
        this.feedIsOpen = feedIsOpen;
        this.feedMode = feedMode;
        this.feedGroupId = feedGroupId;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public int getFeedNotificationCount() {
        return feedNotificationCount;
    }

    public void setFeedNotificationCount(int feedNotificationCount) {
        this.feedNotificationCount = feedNotificationCount;
    }

    public long getFeedRecentCommentTimeStamp() {
        return feedRecentCommentTimeStamp;
    }

    public void setFeedRecentCommentTimeStamp(long feedRecentCommentTimeStamp) {
        this.feedRecentCommentTimeStamp = feedRecentCommentTimeStamp;
    }

    public long getFeedRecentOpenCommentTimeStamp() {
        return feedRecentOpenCommentTimeStamp;
    }

    public void setFeedRecentOpenCommentTimeStamp(long feedRecentOpenCommentTimeStamp) {
        this.feedRecentOpenCommentTimeStamp = feedRecentOpenCommentTimeStamp;
    }


    public boolean isFeedIsOpen() {
        return feedIsOpen;
    }

    public void setFeedIsOpen(boolean feedIsOpen) {
        this.feedIsOpen = feedIsOpen;
    }


    public String getFeedMode() {
        return feedMode;
    }

    public void setFeedMode(String feedMode) {
        this.feedMode = feedMode;
    }

    public String getFeedGroupId() {
        return feedGroupId;
    }

    public void setFeedGroupId(String feedGroupId) {
        this.feedGroupId = feedGroupId;
    }
}
