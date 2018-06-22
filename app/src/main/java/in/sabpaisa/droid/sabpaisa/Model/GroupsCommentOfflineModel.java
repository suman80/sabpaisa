package in.sabpaisa.droid.sabpaisa.Model;

public class GroupsCommentOfflineModel {

    String groupId,commentText,commentByName,commentDate;
    int commentId;

    public GroupsCommentOfflineModel() {
    }


    public GroupsCommentOfflineModel(String groupId, String commentText, String commentByName, String commentDate, int commentId) {
        this.groupId = groupId;
        this.commentText = commentText;
        this.commentByName = commentByName;
        this.commentDate = commentDate;
        this.commentId = commentId;
    }


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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
