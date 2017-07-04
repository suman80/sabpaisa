package in.sabpaisa.droid.sabpaisa;

/**
 * Created by SabPaisa on 03-07-2017.
 */

public class FeedData {
    public String getFeed_Name() {
        return feed_Name;
    }

    public void setFeed_Name(String feed_Name) {
        this.feed_Name = feed_Name;
    }

    public String getFeedText() {
        return feedText;
    }

    public void setFeedText(String feedText) {
        this.feedText = feedText;
    }

    String feed_Name;
    String feedText;

    public String getFeed_date() {
        return feed_date;
    }

    public void setFeed_date(String feed_date) {
        this.feed_date = feed_date;
    }

    String feed_date;

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    String feedId;
}
