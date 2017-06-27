package in.sabpaisa.droid.sabpaisa.Model;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by abc on 20-06-2017.
 */

public class ChatHistory {
    String userImage;
    String userName;

    public ChatHistory(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
