package in.sabpaisa.droid.sabpaisa.Util;

import android.content.pm.ActivityInfo;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.uncopt.android.widget.text.justify.JustifiedTextView;

import in.sabpaisa.droid.sabpaisa.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    JustifiedTextView privacy_content;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);

        setContentView(R.layout.activity_privacy_policy);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        privacy_content = (JustifiedTextView) findViewById(R.id.privacy_content);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        progressBar.setVisibility(View.VISIBLE);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                privacy_content.setText(getString(R.string.privacy));

            }
        }, 2000);


    }
}
