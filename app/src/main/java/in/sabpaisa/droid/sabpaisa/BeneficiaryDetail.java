package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.graphics.Rect;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;

public class BeneficiaryDetail extends AppCompatActivity {

    Toolbar mtoolbar;
    String upiString;
    TextView upiName,upi;
    EditText amount,remark;
    CheckBox save;
    Button send;
    LinearLayout beneficiayDetail;
    ScrollView svBeneficiary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_beneficiary_detail);
        upiString = getIntent().getStringExtra("UPI");

        DataBinding();

        boolean isDialog = getIntent().getBooleanExtra("Dialog",false);
        upi.setText(upiString);
        mtoolbar.setTitle("Beneficiary Account");
        setSupportActionBar(mtoolbar);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount.getText().toString().isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(beneficiayDetail, "Please enter some amount", Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.bg_orange));
                    snackbar.show();
                }
                else if (Integer.parseInt(amount.getText().toString())<=10000) {
                    Intent intent = new Intent(BeneficiaryDetail.this, SendConfirmUPImPIN.class);
                    intent.putExtra("Name", upiName.getText().toString());
                    intent.putExtra("Amount", amount.getText().toString());
                    intent.putExtra("UPI", upi.getText().toString());
                    startActivity(intent);
                }else {
                    Snackbar snackbar = Snackbar
                            .make(beneficiayDetail, "Please enter a amount less than or equal to 10,000", Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.bg_orange));
                    snackbar.show();
//                    Toast.makeText(BeneficiaryDetail.this, "Please enter a amount less than or equal to 10,000", Toast.LENGTH_SHORT).show();
                }
            }
        });
        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!amount.getText().toString().isEmpty()&&Integer.parseInt(amount.getText().toString())>10000){
                    Snackbar snackbar = Snackbar
                            .make(beneficiayDetail, "Amount should be less than or equal to 10,000", Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.bg_orange));

                    snackbar.show();
//                    Toast.makeText(BeneficiaryDetail.this, "Amount should be less than or equal to 10,000", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        beneficiayDetail.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                beneficiayDetail.getWindowVisibleDisplayFrame(r);
                int screenHeight = beneficiayDetail.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    svBeneficiary.post(new Runnable() {
                        public void run() {
                            svBeneficiary.scrollTo(0, svBeneficiary.getBottom());
                        }
                    });
                }
                else {// keyboard is closed
                }
            }
        });
    }

    private void DataBinding() {
        mtoolbar = (Toolbar)findViewById(R.id.toolbar);
        upiName = (TextView)findViewById(R.id.tv_upiName);
        upi = (TextView)findViewById(R.id.tv_upi);
        amount = (EditText)findViewById(R.id.et_amount);
        remark = (EditText)findViewById(R.id.et_remark);
        save = (CheckBox)findViewById(R.id.cb_save);
        send = (Button)findViewById(R.id.btn_send);
        beneficiayDetail = (LinearLayout)findViewById(R.id.ll_beneficiary_detail);
        svBeneficiary = (ScrollView)findViewById(R.id.sv_beneficiary);
    }
}
