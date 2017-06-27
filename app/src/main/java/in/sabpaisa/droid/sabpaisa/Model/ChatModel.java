package in.sabpaisa.droid.sabpaisa.Model;

/**
 * Created by abc on 21-06-2017.
 */

public class ChatModel {
    String message;
    String imageUrl;
    boolean isMine;
    int messageType; //1 is conversation, and 0 is info like payment done

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public ChatModel(String message, boolean isMine, int messageType) {
        this.message = message;
        this.isMine = isMine;
        this.messageType = messageType;
    }

}
