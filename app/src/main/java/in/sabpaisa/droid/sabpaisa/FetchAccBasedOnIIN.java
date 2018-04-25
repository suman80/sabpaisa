package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.olive.upi.OliveUpiManager;
import com.olive.upi.transport.OliveUpiEventListener;
import com.olive.upi.transport.api.Result;
import com.olive.upi.transport.api.UpiService;
import com.olive.upi.transport.model.Account;
import com.olive.upi.transport.model.Bank;
import com.olive.upi.transport.model.CustomerBankAccounts;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.BankListAdapter;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;

public class FetchAccBasedOnIIN extends AppCompatActivity implements OliveUpiEventListener{

    Account account;
    Toolbar mtoolbar;
    TextView accountName,accountNumber,accountStatus,accountType,accountVPA,requestBalance;
    Button btn_LinkVpa,btn_AccActivation;
    TextView editVPA;
    EditText text_VPA;
    LinearLayout llAddVpa;


    ArrayList<CustomerBankAccounts> accountArrayList;

    public  static  String MYSHAREDPREFFORACCOUNT="mySharedPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_fetch_acc_based_on_iin);

        Intent intent = getIntent();
        final String iin = intent.getStringExtra("IIN");//DCB IIN
        Log.d("IIN", "---->"+ iin);

        OliveUpiManager.getInstance(FetchAccBasedOnIIN.this).setListener(this);

        OliveUpiManager.getInstance(FetchAccBasedOnIIN.this).FetchAccountonIIN(iin);//Pass here bank IIN

        if (account == null)
        {
            OliveUpiManager.getInstance(FetchAccBasedOnIIN.this).fetchMyAccounts();
        }


        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        accountName = (TextView)findViewById(R.id.accountName);
        accountNumber = (TextView)findViewById(R.id.accountNumber);

        accountType = (TextView)findViewById(R.id.accountType);
        //accountVPA = (TextView)findViewById(R.id.accountVPA);
        btn_LinkVpa = (Button) findViewById(R.id.btn_LinkVpa);

        llAddVpa = (LinearLayout)findViewById(R.id.llAddVpa);
        text_VPA = (EditText) findViewById(R.id.text_VPA);



        btn_LinkVpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account == null) {

                    Toast.makeText(FetchAccBasedOnIIN.this, "No account selected.", Toast.LENGTH_SHORT).show();
                } else {
                     final String vpa = text_VPA.getText().toString();
                    OliveUpiManager.getInstance(FetchAccBasedOnIIN.this).updateVPA(vpa, account);//srs is the vpa
                    Intent intent1 = new Intent(FetchAccBasedOnIIN.this, UPIActivationActivity.class);
                    startActivity(intent1);
                }
            }
        });



        mtoolbar.setTitle("User Account");
        setSupportActionBar(mtoolbar);


    }


    @Override
    public void onSuccessResponse(int reqType, Object data) {

        Log.d("reqTypeForBank", "" + reqType);

        //This API is used to add bank accounts based on the customer Id and Bank IIN.(2)
        if (reqType == UpiService.REQUEST_FETCH_ACCOUNT) {

            Result<ArrayList<Account>> fetchAccountList = (Result<ArrayList<Account>>) data;
            Log.d("FETCH_ACCOUNT_ON_IIN_1", "---->" + fetchAccountList);

            if (fetchAccountList.getCode().equals("00") && fetchAccountList != null && fetchAccountList.getData().size() > 0) {
                Toast.makeText(getApplicationContext(), "Fetch Account Success", Toast.LENGTH_SHORT).show();
                account = (Account) fetchAccountList.getData().get(0);
                Log.d("FETCH_ACCOUNT_ON_IIN_2", "---->" + account.toString());

                SharedPreferences.Editor editor = getSharedPreferences(MYSHAREDPREFFORACCOUNT, MODE_PRIVATE).edit();
                Gson gson = new Gson();
                String accountdata = gson.toJson(account);
                editor.putString("ACCOUNT", accountdata);
                editor.commit();

                accountName.setText(account.getName());
                accountNumber.setText(account.getMaskedAccnumber());
                accountType.setText(account.getType());


            } else {
                Toast.makeText(getApplicationContext(), "Fetch Account Details Failed", Toast.LENGTH_SHORT).show();
            }
        } else if (reqType == UpiService.REQUEST_SAVE_VPA) {

            Result<String> linkVPA = (Result<String>) data;
            Log.d("LINK_VPA1111", "-->" + linkVPA);

            if (linkVPA.getCode().equals("00"))

            {
                Toast.makeText(this, linkVPA.getData().toString(), Toast.LENGTH_SHORT).show();
                Log.d("LINK_VPA", "-->" + linkVPA.getCode());
                Log.d("LINK_VPA", "-->" + linkVPA.getResult());
                Log.d("LINK_VPA", "-->" + linkVPA.getData());
                Toast.makeText(this, "VPA Linked Success", Toast.LENGTH_SHORT).show();



            } else {
                Toast.makeText(getApplicationContext(), "VPA Linked Failed", Toast.LENGTH_SHORT).show();
            }


        }

        //Fetch All Linked Accounts(5)
        else if (reqType == UpiService.REQUEST_ALL_ACCOUNTS) {
            Toast.makeText(getApplicationContext(), "Fetch Account Success", Toast.LENGTH_SHORT).show();
            accountArrayList = new ArrayList<>();
            accountArrayList = (ArrayList<CustomerBankAccounts>) data;
            Log.d("REQUEST_ALL_ACCOUNTS", "--->" + accountArrayList.get(0).getBankName());
            Log.d("REQUEST_ALL_ACCOUNTS", "--->" + accountArrayList.get(0).getBankCode());
            Log.d("REQUEST_ALL_ACCOUNTS", "--->" + accountArrayList.get(0).getAccounts());
            Log.d("REQUEST_ALL_ACCOUNTS", "--->" + accountArrayList.get(0).getInput());
            if (accountArrayList != null && accountArrayList.size() > 0) {

                account = (Account) accountArrayList.get(0).getAccounts().get(0);

                accountName.setText(account.getName());
                accountNumber.setText(account.getMaskedAccnumber());
                accountType.setText(account.getType());

                SharedPreferences.Editor editor = getSharedPreferences(MYSHAREDPREFFORACCOUNT, MODE_PRIVATE).edit();
                Gson gson = new Gson();
                String accountdata = gson.toJson(account);
                editor.putString("ACCOUNT", accountdata);
                editor.commit();

                Log.d("Val_Acc", "-->" + account.toString());


            }
        }

        else
            {

                Toast.makeText(getApplicationContext(), "Fetch Account Details Failed", Toast.LENGTH_SHORT).show();
            }



    }


    @Override
    public void onFailureResponse(int reqType, Object data) {
        Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show();

    }


}



