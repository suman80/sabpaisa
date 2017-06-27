package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AccountInfoActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView bankLogo;
    TextView accountNumber,accountType,accountIFSC,changeMPIN,resetMPIN,requestBalance;
    LinearLayout mpinInfo,mpinInfo2;
    Button setmPIN;
    int isMpinSet=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_account);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        bankLogo = (ImageView)findViewById(R.id.iv_bankLogo);
        accountNumber = (TextView)findViewById(R.id.tv_accountNumber);
        accountType = (TextView)findViewById(R.id.tv_accountType);
        accountIFSC = (TextView)findViewById(R.id.tv_accountIFSC);
        mpinInfo = (LinearLayout)findViewById(R.id.ll_mpinInfo); /*TODO Hide it if the pin is set*/
        mpinInfo2 = (LinearLayout)findViewById(R.id.ll_mpinInfo2);
        setmPIN = (Button)findViewById(R.id.btn_setmPIN);
        changeMPIN = (TextView)findViewById(R.id.tv_changeMPIN);
        resetMPIN = (TextView)findViewById(R.id.tv_resetMPIN);
        requestBalance = (TextView)findViewById(R.id.tv_requestBalance);
        if (isMpinSet==0){
            mpinInfo2.setVisibility(View.GONE);
            mpinInfo.setVisibility(View.VISIBLE);
            setmPIN.setVisibility(View.VISIBLE);
        }else{
            mpinInfo2.setVisibility(View.VISIBLE);
            mpinInfo.setVisibility(View.GONE);
            setmPIN.setVisibility(View.GONE);
        }

        setmPIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountInfoActivity.this,SetmPIN.class);
                intent.putExtra("Tag","Setup Debit Card");
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        });

        toolbar.setTitle("Account Information");
        setSupportActionBar(toolbar);

        changeMPIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO API - Set Credentials
            }
        });
        resetMPIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO API - Set Credentials
            }
        });
        requestBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO API - Balance-Enquiry
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_right_out);
    }
}
