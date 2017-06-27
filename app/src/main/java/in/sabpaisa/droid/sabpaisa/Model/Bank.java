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

public class Bank{
    String name;
    String ifsc;
    Drawable logo;

    public Bank(String name, Drawable logo,String ifsc){
        this.name = name;
        this.logo = logo;
        this.ifsc = ifsc;
    }

    public String getName() {
        return name;
    }

    public Drawable getLogo() {
        return logo;
    }

    public String getIfsc() {
        return ifsc;
    }
}
