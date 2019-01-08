package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import in.sabpaisa.droid.sabpaisa.Model.Bank;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;

public class SendAccountIFSC extends AppCompatActivity {

    Toolbar mtoolbar;
    TextView ifscBank;
    EditText ifscBranch,beneficiaryName,AccountNumber,cAccountNumber;
    ImageView bankLogo;
    Button send;
    RelativeLayout relativeLayout;
    ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_send_account_ifsc);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        String ifsc = getIntent().getStringExtra("IFSC");
        Bundle extras = getIntent().getExtras();
        byte[] b = extras.getByteArray("Logo");
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);

        mtoolbar = (Toolbar)findViewById(R.id.toolbar);
        ifscBank = (TextView)findViewById(R.id.tv_ifscBank);
        ifscBranch = (EditText) findViewById(R.id.et_ifscBranch);
        beneficiaryName = (EditText)findViewById(R.id.et_beneficiaryName);
        AccountNumber = (EditText)findViewById(R.id.et_AccountNumber);
        cAccountNumber = (EditText)findViewById(R.id.et_cAccountNumber);
        bankLogo = (ImageView)findViewById(R.id.iv_bankLogo);
        send = (Button)findViewById(R.id.btn_send);
        relativeLayout = (RelativeLayout)findViewById(R.id.rl_account_ifsc);
        scrollView = (ScrollView)findViewById(R.id.sc_account_ifsc);
        mtoolbar.setTitle("Send Money");
        setSupportActionBar(mtoolbar);

        ifscBank.setText(ifsc);
        bankLogo.setImageBitmap(bmp);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//TODO API - Validate Address
//                Intent intent = new Intent(SendAccountIFSC.this,BeneficiaryDetail.class);
//                intent.putExtra("UPI",AccountNumber.getText().toString());
//                startActivity(intent);
            }
        });
        relativeLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                relativeLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = relativeLayout.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    scrollView.post(new Runnable() {
                        public void run() {
//                            scrollView.scrollTo(0,100);
                            scrollView.scrollTo(0, scrollView.getBottom()/2);
                        }
                    });
                }
                else {// keyboard is closed
                }
            }
        });
    }


}
