package in.sabpaisa.droid.sabpaisa.Model;

public class UserImageModel {

    String userId,userImage;

    public UserImageModel() {
    }


    public UserImageModel(String userId, String userImage) {
        this.userId = userId;
        this.userImage = userImage;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
