package in.sabpaisa.droid.sabpaisa.Util;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import in.sabpaisa.droid.sabpaisa.R;

public class RateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        TextView textView=(TextView)findViewById(R.id.Temporary);
    }
}
