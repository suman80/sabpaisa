package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;

public class SuccessMPIN extends AppCompatActivity {

    LinearLayout goToHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_success_mpin);

        goToHome = (LinearLayout)findViewById(R.id.ll_goToHome);
        goToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SuccessMPIN.this,MainActivity.class));
                overridePendingTransition(R.anim.anim_right_in, R.anim.anim_right_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SuccessMPIN.this,MainActivity.class));
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_right_out);
    }
}
