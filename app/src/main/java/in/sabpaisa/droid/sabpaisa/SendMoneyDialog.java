package in.sabpaisa.droid.sabpaisa;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;

/**
 * Created by abc on 21-06-2017.
 */

public class SendMoneyDialog extends AppCompatActivity {

    String upiString;
    TextView upiName,upi;
    EditText amount,remark;
    CheckBox save;
    Button send;
    LinearLayout beneficiayDetail;
    ScrollView svBeneficiary;
    Toolbar mtoolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_beneficiary_detail);
        //getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        DataBinding();



        mtoolbar.setVisibility(View.GONE);
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
                    Intent intent = new Intent(SendMoneyDialog.this, SendConfirmUPImPINDialog.class);
                    intent.putExtra("Name", upiName.getText().toString());
                    intent.putExtra("Amount", amount.getText().toString());
                    intent.putExtra("UPI", upi.getText().toString());
                    startActivityForResult(intent,100);
                    setVisible(false);
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

    private void DataBinding() {
        upiName = (TextView)findViewById(R.id.tv_upiName);
        upi = (TextView)findViewById(R.id.tv_upi);
        amount = (EditText)findViewById(R.id.et_amount);
        remark = (EditText)findViewById(R.id.et_remark);
        save = (CheckBox)findViewById(R.id.cb_save);
        send = (Button)findViewById(R.id.btn_send);
        beneficiayDetail = (LinearLayout)findViewById(R.id.ll_beneficiary_detail);
        svBeneficiary = (ScrollView)findViewById(R.id.sv_beneficiary);
        mtoolbar = (Toolbar)findViewById(R.id.toolbar);
    }
}
