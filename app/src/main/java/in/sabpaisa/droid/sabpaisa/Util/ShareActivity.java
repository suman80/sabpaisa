package in.sabpaisa.droid.sabpaisa.Util;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import in.sabpaisa.droid.sabpaisa.R;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_share);


    }
}
