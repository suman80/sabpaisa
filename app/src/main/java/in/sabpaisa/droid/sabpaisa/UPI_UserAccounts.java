package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.olive.upi.OliveUpiManager;
import com.olive.upi.transport.OliveUpiEventListener;
import com.olive.upi.transport.api.Result;
import com.olive.upi.transport.api.UpiService;
import com.olive.upi.transport.model.Account;
import com.olive.upi.transport.model.CustomerBankAccounts;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.UPI_UserAccountCustomAdapter;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;

public class UPI_UserAccounts extends AppCompatActivity implements OliveUpiEventListener{

    ShimmerRecyclerView userBankAccountsRecyclerView;
    ArrayList<CustomerBankAccounts> bankAccounts;
    ArrayList<Account> userBankAccountDetailsArrayList;
    Account account;
    Toolbar mtoolbar;
    TextView accountName,accountNumber,accountStatus,accountType,accountVPA,requestBalance;
    FrameLayout addNewBankAccount;
    Button accountRemove,setUpiPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_upi__user_accounts);


        OliveUpiManager.getInstance(UPI_UserAccounts.this).setListener(this);

        OliveUpiManager.getInstance(UPI_UserAccounts.this).fetchMyAccounts();
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        /*userBankAccountsRecyclerView = (ShimmerRecyclerView)findViewById(R.id.userBankAccountsRecyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        userBankAccountsRecyclerView.setLayoutManager(llm);*/

        accountName = (TextView)findViewById(R.id.accountName);
        accountNumber = (TextView)findViewById(R.id.accountNumber);
        //accountStatus = (TextView)itemView.findViewById(R.id.accountStatus);
        accountType = (TextView)findViewById(R.id.accountType);
        accountVPA = (TextView)findViewById(R.id.accountVPA);
        requestBalance = (TextView)findViewById(R.id.requestBalance);
        addNewBankAccount=(FrameLayout)findViewById(R.id.addNewBankAccount);
        accountRemove = (Button)findViewById(R.id.accountRemove);
        setUpiPin = (Button)findViewById(R.id.setUpiPin);
        mtoolbar.setTitle("User Account Details");
        setSupportActionBar(mtoolbar);

        requestBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account == null) {
                    Toast.makeText(UPI_UserAccounts.this, "No account selected.", Toast.LENGTH_SHORT).show();
                } else {
                    OliveUpiManager.getInstance(UPI_UserAccounts.this).checkBalance(account);
                }
            }
        });

        addNewBankAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UPI_UserAccounts.this,UPIBankList.class);
                startActivity(intent);

            }
        });

        accountRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OliveUpiManager.getInstance(UPI_UserAccounts.this). accountRemove(account);
            }
        });

        setUpiPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bankid=account.getIin().toString(); //here bankid is IIN number
                OliveUpiManager.getInstance(UPI_UserAccounts.this).changeMpin(bankid,account);
            }
        });

    }


    @Override
    public void onSuccessResponse(int reqType, Object data) {
        Log.d("Main", "onSuccessResponse: reqType "+reqType+" data "+data);

        //Fetch All Linked Accounts(5)
        if(reqType == UpiService.REQUEST_ALL_ACCOUNTS) {
            bankAccounts=new ArrayList<>();
            bankAccounts = (ArrayList<CustomerBankAccounts>) data;
            Log.d("REQUEST_ALL_ACCOUNTS","--->"+bankAccounts.get(0).getBankName());
            Log.d("REQUEST_ALL_ACCOUNTS","--->"+bankAccounts.get(0).getBankCode());
            Log.d("REQUEST_ALL_ACCOUNTS","--->"+bankAccounts.get(0).getAccounts());
            Log.d("REQUEST_ALL_ACCOUNTS","--->"+bankAccounts.get(0).getInput());
            if (bankAccounts != null && bankAccounts.size() > 0) {

                userBankAccountDetailsArrayList = new ArrayList<>();
                userBankAccountDetailsArrayList=bankAccounts.get(0).getAccounts();
                /*UPI_UserAccountCustomAdapter upi_userAccountCustomAdapter = new UPI_UserAccountCustomAdapter(userBankAccountDetailsArrayList);
                userBankAccountsRecyclerView.setAdapter(upi_userAccountCustomAdapter);*/

                account=(Account)bankAccounts.get(0).getAccounts().get(0);

                accountName.setText(userBankAccountDetailsArrayList.get(0).getName());
                accountNumber.setText(userBankAccountDetailsArrayList.get(0).getMaskedAccnumber());
                accountType.setText(userBankAccountDetailsArrayList.get(0).getType());
                accountVPA.setText(userBankAccountDetailsArrayList.get(0).getVpa());

                Log.d("Val_Acc","-->"+account.toString());


            }
        }


        //This API is used to check balance in the account based on Account details/VPA /Customer ID.(7)
        else if (reqType== UpiService. REQUEST_GET_BALANCE){

            Result<String> checkBalance = (Result<String>) data;
            Log.d("REQUEST_GET_BALANCE","--->"+checkBalance);
            requestBalance.setText(checkBalance.getData());

        }

        //This API is used to change the existing Mpin and add the new Mpin
        else if (reqType== UpiService. REQUEST_GET_CHANGE_MPIN){
            Result<ArrayList<Account>> changeMPin = (Result<ArrayList<Account>>) data;
            Log.d("REQUEST_GET_CHANGE_MPIN","-->"+changeMPin);
            Toast.makeText(this,"MPin Change Success",Toast.LENGTH_SHORT).show();
        }


        //This API is used to remove the particular account from the list of added accounts.
        else if (reqType== UpiService. REQUEST_GET_ACCOUNT_REMOVE){
            Result<String> accountRemove = (Result<String>) data;

            if (accountRemove.getCode().equals("00")){
                Log.d("accountRemove","--->"+accountRemove);
                Toast.makeText(this,"Account Removed Successfully",Toast.LENGTH_SHORT).show();
            }

        }


        else {
            Toast.makeText(this,"Not Success",Toast.LENGTH_SHORT).show();
        }






    }

    @Override
    public void onFailureResponse(int reqType, Object data) {
        Toast.makeText(UPI_UserAccounts.this,"Fail" , Toast.LENGTH_SHORT).show();
    }





}
