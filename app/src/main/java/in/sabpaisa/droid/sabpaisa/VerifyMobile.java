package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;

public class VerifyMobile extends AppCompatActivity {

    LinearLayout layoutSim1,layoutSim2;
    TextView tvSim1,tvSim2;
    AppCompatCheckBox checkBoxSim1, checkBoxSim2;
    Button sendSMS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_verify_mobile);

        String optName1 = getIntent().getStringExtra("SIM1");
        String optName2 = getIntent().getStringExtra("SIM2");

        DataBinding();

        setSIMLayout(optName1, optName2);

        setCheckBox();

        sendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkBoxSim1.isChecked() && !checkBoxSim2.isChecked()){
                    Toast.makeText(VerifyMobile.this, "Please Select a SIM to continue", Toast.LENGTH_SHORT).show();
                }else{
                    showBottomSheet();
                }
            }
        });
    }

    private void setCheckBox() {
        checkBoxSim1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxSim2.isChecked()){
                    checkBoxSim2.setChecked(false);
                    checkBoxSim1.setChecked(true);
                }
            }
        });
        checkBoxSim2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxSim1.isChecked()){
                    checkBoxSim1.setChecked(false);
                    checkBoxSim2.setChecked(true);
                }
            }
        });
    }

    private void setSIMLayout(String optName1, String optName2) {
        if (optName1.isEmpty()){
            layoutSim1.setVisibility(View.GONE);
        }else {
            tvSim1.setText(optName1);
        }
        if (optName2.isEmpty()){
            layoutSim2.setVisibility(View.GONE);
        }else {
            tvSim2.setText(optName2);
        }
    }

    private void showBottomSheet() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = this.getLayoutInflater().inflate(R.layout.bottomdialog_verify, null);
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
        bottomSheetDialog.setCancelable(false);

        final TextView _tv = (TextView) sheetView.findViewById( R.id.tv_countdownTimer );
        new CountDownTimer(10000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                Long min = TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished);
                Long sec = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
                if (sec>=10) {
                    _tv.setText("0" + min + ":" + sec);
                }else {
                    _tv.setText("0" + min + ":" + "0"+sec);
                }
            }

            public void onFinish() {
                bottomSheetDialog.cancel();  //TODO API - List Account
                Intent intent = new Intent(VerifyMobile.this,FilterActivity.class);
                intent.putExtra("Title","Register Passcode");
                intent.putExtra("activity",0);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        }.start();
    }

    private void DataBinding() {
        layoutSim1 = (LinearLayout)findViewById(R.id.LL_sim1);
        layoutSim2 = (LinearLayout)findViewById(R.id.LL_sim2);
        tvSim1 = (TextView)findViewById(R.id.tv_sim1);
        tvSim2 = (TextView)findViewById(R.id.tv_sim2);
        checkBoxSim1 = (AppCompatCheckBox) findViewById(R.id.cb_sim1);
        checkBoxSim2 = (AppCompatCheckBox) findViewById(R.id.cb_sim2);
        sendSMS = (Button)findViewById(R.id.btn_sendSMS);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_right_out);
    }

}
