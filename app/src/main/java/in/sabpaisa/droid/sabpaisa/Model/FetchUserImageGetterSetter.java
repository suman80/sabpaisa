package in.sabpaisa.droid.sabpaisa.Model;

/**
 * Created by archana on 4/1/18.
 */

public class FetchUserImageGetterSetter {

    String id,userId,userImageUrl;

    public FetchUserImageGetterSetter() {
    }

    public FetchUserImageGetterSetter(String id, String userId, String userImageUrl) {
        this.id = id;
        this.userId = userId;
        this.userImageUrl = userImageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }
}
