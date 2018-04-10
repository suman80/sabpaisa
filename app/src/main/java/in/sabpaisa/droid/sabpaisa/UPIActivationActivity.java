package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.olive.upi.OliveUpiManager;
import com.olive.upi.transport.OliveUpiEventListener;
import com.olive.upi.transport.api.Result;
import com.olive.upi.transport.api.UpiService;
import com.olive.upi.transport.model.Account;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;

public class UPIActivationActivity extends AppCompatActivity implements OliveUpiEventListener{

    EditText et_cardNo,et_ExpNoMM,et_ExpNoYY;
    Button btnAcctivate;
    public static Account account;
    ArrayList<Account> accountArrayList;
     String expiryDateNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_upiactivation);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        OliveUpiManager.getInstance(UPIActivationActivity.this).setListener(this);
        et_cardNo = (EditText)findViewById(R.id.et_cardNo);
        et_ExpNoMM = (EditText)findViewById(R.id.et_ExpNoMM);
        et_ExpNoYY = (EditText)findViewById(R.id.et_ExpNoYY);
        btnAcctivate = (Button)findViewById(R.id.btnAcctivate);
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences(FetchAccBasedOnIIN.MYSHAREDPREFFORACCOUNT, MODE_PRIVATE);
        String accountdata=sharedPreferences.getString("ACCOUNT","");
        account=gson.fromJson(accountdata,Account.class);

        Log.d("VALUE_ACC",""+account);


        Log.d("account"," "+account);

        btnAcctivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardNo = et_cardNo.getText().toString();
                Log.d("cardNo",""+cardNo);

                if (Integer.parseInt(et_ExpNoMM.getText().toString())<=12){
                    expiryDateNo=et_ExpNoMM.getText().toString().concat(et_ExpNoYY.getText().toString());
                    Log.d("expiryDateNo",""+expiryDateNo);
                }else {
                    Toast.makeText(getApplicationContext(),"Invalid",Toast.LENGTH_SHORT).show();
                }



                if (account == null) {
                    Toast.makeText(UPIActivationActivity.this, "No account selected.", Toast.LENGTH_SHORT).show();
                } else {

                    OliveUpiManager.getInstance(UPIActivationActivity.this).activateAccount(account.getIin(), account, cardNo, expiryDateNo);
                }

            }
        });


    }


    @Override
    public void onSuccessResponse(int reqType, Object data) {

        Log.d("reqTypeForBank",""+reqType);


        //Account Activation
        /*Activate Account
        This API is used to validate the card details and Activate the account.
                This API requests OTP from the customer issuer bank which is used to set/Re-set Mpin.
                The MPIN is Used for transaction authorization.*/

         if (reqType == UpiService. REQUEST_ACCOUNT_MOBILE_REG){
            Result<ArrayList<String>> accountMoblieReg = (Result<ArrayList<String>>) data;

            if (accountMoblieReg.getCode().equals("00")){
                Log.d("ACCOUNT_MOBILE_REG","-->"+accountMoblieReg.getCode());
                Log.d("ACCOUNT_MOBILE_REG","-->"+accountMoblieReg.getData());
                Log.d("ACCOUNT_MOBILE_REG","-->"+accountMoblieReg.getResult());
                Toast.makeText(getApplicationContext(),"Activation Success",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UPIActivationActivity.this,MainActivity.class);
                startActivity(intent);
            }

        }else {
             Toast.makeText(getApplicationContext(),"Activation Failed",Toast.LENGTH_SHORT).show();
         }




    }

    @Override
    public void onFailureResponse(int reqType, Object data) {
        Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show();

    }



}
