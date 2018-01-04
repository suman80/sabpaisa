package in.sabpaisa.droid.sabpaisa;

/**
 * Created by SabPaisa on 03-07-2017.
 */

public class FeedData {
    public String feedId;
    String clientId;
    public String feedText;
    public String feedName;
    public String createdDate;
    String imagePath;
    String logoPath;

    public FeedData() {
    }

    public FeedData(String feedId, String clientId, String feedText, String feedName, String createdDate, String imagePath, String logoPath) {
        this.feedId = feedId;
        this.clientId = clientId;
        this.feedText = feedText;
        this.feedName = feedName;
        this.createdDate = createdDate;
        this.imagePath = imagePath;
        this.logoPath = logoPath;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getFeedText() {
        return feedText;
    }

    public void setFeedText(String feedText) {
        this.feedText = feedText;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
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
