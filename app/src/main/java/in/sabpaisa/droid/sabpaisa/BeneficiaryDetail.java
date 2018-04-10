package in.sabpaisa.droid.sabpaisa;

import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.olive.upi.OliveUpiManager;
import com.olive.upi.transport.OliveUpiEventListener;
import com.olive.upi.transport.api.Result;
import com.olive.upi.transport.api.UpiService;
import com.olive.upi.transport.model.Account;
import com.olive.upi.transport.model.BeneVpa;
import com.olive.upi.transport.model.CustomerBankAccounts;

import java.text.NumberFormat;
import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;

public class BeneficiaryDetail extends AppCompatActivity implements OliveUpiEventListener{

    Toolbar mtoolbar;
    String vpaString;
    TextView upiName,upi,tv_accountNumber;
    EditText amount,remark;
    CheckBox save;
    Button send;
    LinearLayout beneficiayDetail;
    ScrollView svBeneficiary;
    Account account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);

        setContentView(R.layout.activity_beneficiary_detail);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mtoolbar = (Toolbar)findViewById(R.id.toolbar);
        upiName = (TextView)findViewById(R.id.tv_upiName);
        upi = (TextView)findViewById(R.id.tv_upi);
        amount = (EditText)findViewById(R.id.et_amount);
        remark = (EditText)findViewById(R.id.et_remark);
        save = (CheckBox)findViewById(R.id.cb_save);
        send = (Button)findViewById(R.id.btn_send);
        beneficiayDetail = (LinearLayout)findViewById(R.id.ll_beneficiary_detail);
        svBeneficiary = (ScrollView)findViewById(R.id.sv_beneficiary);
        tv_accountNumber = (TextView)findViewById(R.id.tv_accountNumber);

        OliveUpiManager.getInstance(BeneficiaryDetail.this).setListener(this);

        OliveUpiManager.getInstance(BeneficiaryDetail.this).fetchMyAccounts();


        vpaString = getIntent().getStringExtra("VPA");



        boolean isDialog = getIntent().getBooleanExtra("Dialog",false);
        upi.setText(vpaString);
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
                //else if (Integer.parseInt(amount.getText().toString())<=10000) {
                    /*Intent intent = new Intent(BeneficiaryDetail.this, SendConfirmUPImPIN.class);
                    intent.putExtra("Name", upiName.getText().toString());
                    intent.putExtra("Amount", amount.getText().toString());
                    intent.putExtra("UPI", upi.getText().toString());
                    startActivity(intent);*/


                    if (account == null) {
                        Toast.makeText(BeneficiaryDetail.this, "No account selected.", Toast.LENGTH_SHORT).show();
                    }

                    else

                    {

                        String amount1="";

                        if (!amount.getText().toString().contains("."))
                        {
                             amount1=amount.getText().toString().concat(".00").trim();
                        }
                        else
                        {
                             amount1=amount.getText().toString().trim();
                        }

                        Log.d("amount1",""+amount1);

                        BeneVpa vpa = new BeneVpa();
                        vpa.setName("Aditya");
                        vpa.setVpa("aditya@dcb");//Given by aditya dcb
                        vpa.setNickname("Nikki");
                        String merchantvpa = "onkar@dcb";
                        String merchantid = "131";
                        String submerchantid = "550";
                        String merchantchannelid = "121";
                        String trantype = "P2P";
                        String remarks=remark.getText().toString();

                        OliveUpiManager.getInstance(BeneficiaryDetail.this).initiatePay(account, vpa, amount1, remarks, merchantvpa, merchantid, submerchantid, merchantchannelid, trantype);

                    }



                }/*else {
                    Snackbar snackbar = Snackbar
                            .make(beneficiayDetail, "Please enter a amount less than or equal to 10,000", Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.bg_orange));
                    snackbar.show();
//                    Toast.makeText(BeneficiaryDetail.this, "Please enter a amount less than or equal to 10,000", Toast.LENGTH_SHORT).show();
                }
            }*/
        });
        /*amount.addTextChangedListener(new TextWatcher() {
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
        });*/

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
    public void onSuccessResponse(int reqType, Object data) {
        Log.d("Main", "onSuccessResponse: reqType "+reqType+" data "+data);

        //Fetch All Linked Accounts(5)
        if(reqType == UpiService.REQUEST_ALL_ACCOUNTS) {
            ArrayList<CustomerBankAccounts> bankAccounts = (ArrayList<CustomerBankAccounts>) data;
            Log.d("REQUEST_ALL_ACCOUNTS","--->"+bankAccounts.get(0).getBankName());
            Log.d("REQUEST_ALL_ACCOUNTS","--->"+bankAccounts.get(0).getBankCode());
            Log.d("REQUEST_ALL_ACCOUNTS","--->"+bankAccounts.get(0).getAccounts());
            Log.d("REQUEST_ALL_ACCOUNTS","--->"+bankAccounts.get(0).getInput());
            if (bankAccounts != null && bankAccounts.size() > 0) {
                account=(Account)bankAccounts.get(0).getAccounts().get(0);
                tv_accountNumber.setText(account.getMaskedAccnumber());
                Log.d("Val_Acc","-->"+account.toString());


            }
        }




          /*
This API is used to transfer fund from payer account to payee account. Fund transfer
        is processed based on the VPA/ account details of the both parties.(6)
*/
         if(reqType==UpiService.REQUEST_PAY){

            Result<String> PayDataArrayList = (Result<String>) data;
            Log.d("PayDataArrayList1","-->"+PayDataArrayList);
            if(PayDataArrayList.getCode().equals("00")){

                Toast.makeText(this,"Pay Success",Toast.LENGTH_SHORT).show();

                Log.d("PayDataArrayList2","-->"+PayDataArrayList);

            }


        }


        else{
            //Toast.makeText(this,"Pay Not Success",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailureResponse(int reqType, Object data) {
        Toast.makeText(BeneficiaryDetail.this,"Fail" , Toast.LENGTH_SHORT).show();
    }





}
