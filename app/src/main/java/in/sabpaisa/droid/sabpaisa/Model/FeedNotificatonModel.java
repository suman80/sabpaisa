package in.sabpaisa.droid.sabpaisa.Model;

public class FeedNotificatonModel {

    String feedId;
    int feedNotificationCount;
    long feedRecentCommentTimeStamp;
    long feedRecentOpenCommentTimeStamp;


    public FeedNotificatonModel() {
    }

    public FeedNotificatonModel(String feedId, int feedNotificationCount, long feedRecentCommentTimeStamp, long feedRecentOpenCommentTimeStamp) {
        this.feedId = feedId;
        this.feedNotificationCount = feedNotificationCount;
        this.feedRecentCommentTimeStamp = feedRecentCommentTimeStamp;
        this.feedRecentOpenCommentTimeStamp = feedRecentOpenCommentTimeStamp;
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
}
