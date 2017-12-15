package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.olive.upi.OliveUpiManager;
import com.olive.upi.transport.OliveUpiEventListener;
import com.olive.upi.transport.api.Result;
import com.olive.upi.transport.api.UpiService;
import com.olive.upi.transport.model.Account;
import com.olive.upi.transport.model.Bank;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.BankListAdapter;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;

public class FetchAccBasedOnIIN extends AppCompatActivity implements OliveUpiEventListener{

    Account account;
    Toolbar mtoolbar;
    TextView accountName,accountNumber,accountStatus,accountType,accountVPA,requestBalance;
    Button btn_LinkVpa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_fetch_acc_based_on_iin);

        Intent intent = getIntent();
        final String iin = intent.getStringExtra("IIN");//DCB IIN
        Log.d("IIN", "---->"+ iin);

        OliveUpiManager.getInstance(FetchAccBasedOnIIN.this).setListener(this);

        OliveUpiManager.getInstance(FetchAccBasedOnIIN.this).FetchAccountonIIN(iin);//Pass here bank IIN



        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        accountName = (TextView)findViewById(R.id.accountName);
        accountNumber = (TextView)findViewById(R.id.accountNumber);
        //accountStatus = (TextView)itemView.findViewById(R.id.accountStatus);
        accountType = (TextView)findViewById(R.id.accountType);
        accountVPA = (TextView)findViewById(R.id.accountVPA);
        btn_LinkVpa = (Button) findViewById(R.id.btn_LinkVpa);

        btn_LinkVpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account == null) {

                    Toast.makeText(FetchAccBasedOnIIN.this, "No account selected.", Toast.LENGTH_SHORT).show();
                } else {

                    OliveUpiManager.getInstance(FetchAccBasedOnIIN.this).updateVPA("srs", account);

                }
            }
        });

        mtoolbar.setTitle("User Account");
        setSupportActionBar(mtoolbar);


    }


    @Override
    public void onSuccessResponse(int reqType, Object data) {

        //This API is used to get the list of the UPI enabled banks.
        Log.d("reqTypeForBank",""+reqType);
        //This API is used to add bank accounts based on the customer Id and Bank IIN.(2)
         if (reqType == UpiService. REQUEST_FETCH_ACCOUNT){

            Result<ArrayList<Account>> fetchAccountList = (Result<ArrayList<Account>>) data;
            Log.d("FETCH_ACCOUNT_ON_IIN_1","---->"+fetchAccountList);

            if (fetchAccountList.getCode().equals("00") && fetchAccountList!=null && fetchAccountList.getData().size()>0){
                account= (Account)fetchAccountList.getData().get(0);
                Log.d("FETCH_ACCOUNT_ON_IIN_2","---->"+account.toString());


                accountName.setText(account.getName());
                accountNumber.setText(account.getMaskedAccnumber());
                accountType.setText(account.getType());
                accountVPA.setText(account.getVpa());



            }else {
                Toast.makeText(getApplicationContext(),"Not Success",Toast.LENGTH_SHORT).show();
            }
        }

         else if (reqType == UpiService. REQUEST_SAVE_VPA){

             Result<String> linkVPA = (Result<String>) data;


             if (linkVPA.getCode().equals("00")){
                 Toast.makeText(this,linkVPA.getData().toString(),Toast.LENGTH_SHORT).show();
                 Log.d("LINK_VPA","-->"+linkVPA.getCode());
                 Log.d("LINK_VPA","-->"+linkVPA.getResult());
                 Log.d("LINK_VPA","-->"+linkVPA.getData());



             }


         }

    }

    @Override
    public void onFailureResponse(int reqType, Object data) {
        Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show();

    }


}



