package in.sabpaisa.droid.sabpaisa.Model;

public class FeedDataForOffLine {

    public String feedId;
    public String clientId;
    public String feedText;
    public String feedName;
    public byte [] imagePath;
    public byte [] logoPath;

    public FeedDataForOffLine() {
    }


    public FeedDataForOffLine(String feedId, String clientId, String feedText, String feedName, byte[] imagePath, byte[] logoPath) {
        this.feedId = feedId;
        this.clientId = clientId;
        this.feedText = feedText;
        this.feedName = feedName;
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
