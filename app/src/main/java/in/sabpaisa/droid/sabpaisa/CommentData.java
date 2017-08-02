package in.sabpaisa.droid.sabpaisa;



public class CommentData {

    public CommentData(String commentText) {
        this.commentText = commentText;
    }

    public CommentData() {

    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    String commentText;

    public String getComment_date() {
        return comment_date;
    }

    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }

    String comment_date;
}
