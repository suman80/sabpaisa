package in.sabpaisa.droid.sabpaisa;

import android.content.pm.ActivityInfo;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.layer_net.stepindicator.StepIndicator;

import in.sabpaisa.droid.sabpaisa.Adapter.TestAdapter;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;

public class TestScrollPager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_test_scroll_pager);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ViewPager pager = (ViewPager)findViewById(R.id.viewpager);
        pager.setAdapter(new TestAdapter(getSupportFragmentManager()));
        //Bind the step indicator to the adapter
        StepIndicator stepIndicator = (StepIndicator) findViewById(R.id.step_indicator);
        stepIndicator.setupWithViewPager(pager);
    }
}
