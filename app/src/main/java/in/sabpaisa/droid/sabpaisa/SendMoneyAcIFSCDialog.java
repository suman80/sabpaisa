package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;

/**
 * Created by abc on 21-06-2017.
 */

public class SendMoneyAcIFSCDialog extends AppCompatActivity {

    TextView ifscBank;
    EditText ifscBranch,beneficiaryName,AccountNumber,cAccountNumber;
    ImageView bankLogo;
    Button send;
    RelativeLayout relativeLayout;
    ScrollView scrollView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        /*supportRequestWindowFeature(Window.FEATURE_NO_TITLE);*/
        setContentView(R.layout.item_send_account_ifcs);
        /*etWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);*/

        ifscBank = (TextView)findViewById(R.id.tv_ifscBank);
        ifscBranch = (EditText) findViewById(R.id.et_ifscBranch);
        beneficiaryName = (EditText)findViewById(R.id.et_beneficiaryName);
        AccountNumber = (EditText)findViewById(R.id.et_AccountNumber);
        cAccountNumber = (EditText)findViewById(R.id.et_cAccountNumber);
        bankLogo = (ImageView)findViewById(R.id.iv_bankLogo);
        send = (Button)findViewById(R.id.btn_send);
        relativeLayout = (RelativeLayout)findViewById(R.id.rl_account_ifsc);
        scrollView = (ScrollView)findViewById(R.id.sc_account_ifsc);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//TODO API - Validate Address
                Intent intent = new Intent(SendMoneyAcIFSCDialog.this,SendMoneyDialog.class);
                intent.putExtra("UPI",AccountNumber.getText().toString());
                startActivityForResult(intent,100);
                setVisible(false);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if(resultCode == 200) {
                Intent intent = new Intent();
                intent.putExtra("Status", true);
                setResult(200, intent);
                finish();
            }
        }
    }
}
