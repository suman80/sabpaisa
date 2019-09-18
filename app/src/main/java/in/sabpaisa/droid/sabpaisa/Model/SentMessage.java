package in.sabpaisa.droid.sabpaisa.Model;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by abc on 16-06-2017.
 */

public class SentMessage{
    String userName;
    String mobileNumber;
    String userImage;

    public SentMessage() {

    }
    public SentMessage(String userName, String mobileNumber,String userImage){
        this.userName = userName;
        this.mobileNumber = mobileNumber;
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}

