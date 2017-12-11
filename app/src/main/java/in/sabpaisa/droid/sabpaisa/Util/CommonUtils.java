package in.sabpaisa.droid.sabpaisa.Util;

import android.app.Activity;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by archana on 9/12/17.
 */

public class CommonUtils {

    public static void setFullScreen(Context context) {
        ( (Activity) context).requestWindowFeature(Window.FEATURE_NO_TITLE);
        ( (Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


    }

}