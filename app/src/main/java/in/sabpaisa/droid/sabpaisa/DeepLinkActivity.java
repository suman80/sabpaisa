package in.sabpaisa.droid.sabpaisa;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class DeepLinkActivity extends AppCompatActivity implements
        View.OnClickListener{

    private static final String TAG = DeepLinkActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_link);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check for link in intent
        if (getIntent() != null && getIntent().getData() != null) {
            Uri data = getIntent().getData();

            Log.d(TAG, "data:" + data);
            ((TextView) findViewById(R.id.deep_link_text))
                    .setText(getString(R.string.deep_link_fmt, data.toString()));
        }
    }


    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.button_ok) {
            finish();
        }
    }

    }
