package in.sabpaisa.droid.sabpaisa.Util;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.uncopt.android.widget.text.justify.JustifiedEditText;

import in.sabpaisa.droid.sabpaisa.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);

        setContentView(R.layout.activity_privacy_policy);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
