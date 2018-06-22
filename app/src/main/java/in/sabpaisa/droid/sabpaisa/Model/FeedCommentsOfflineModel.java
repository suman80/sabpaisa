package in.sabpaisa.droid.sabpaisa.Model;

public class FeedCommentsOfflineModel {

    String feedId,commentText,commentByName,commentDate;
    int commentId;

    public FeedCommentsOfflineModel() {
    }


    public FeedCommentsOfflineModel(String feedId, String commentText, String commentByName, String commentDate, int commentId) {
        this.feedId = feedId;
        this.commentText = commentText;
        this.commentByName = commentByName;
        this.commentDate = commentDate;
        this.commentId = commentId;
    }


    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getCommentByName() {
        return commentByName;
    }

    public void setCommentByName(String commentByName) {
        this.commentByName = commentByName;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }
}
