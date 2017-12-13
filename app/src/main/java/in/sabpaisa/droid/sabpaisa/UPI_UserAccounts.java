package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_upi__user_accounts);
        OliveUpiManager.getInstance(UPI_UserAccounts.this).setListener(this);

        OliveUpiManager.getInstance(UPI_UserAccounts.this).fetchMyAccounts();
        userBankAccountsRecyclerView = (ShimmerRecyclerView)findViewById(R.id.userBankAccountsRecyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        userBankAccountsRecyclerView.setLayoutManager(llm);




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
                UPI_UserAccountCustomAdapter upi_userAccountCustomAdapter = new UPI_UserAccountCustomAdapter(userBankAccountDetailsArrayList);
                userBankAccountsRecyclerView.setAdapter(upi_userAccountCustomAdapter);

                account=(Account)bankAccounts.get(0).getAccounts().get(0);
                //tv_accountNumber.setText(account.getMaskedAccnumber());
                Log.d("Val_Acc","-->"+account.toString());


            }

            //This API is used to verify the virtual Payment Address is valid or not.
        }

        else{
            Toast.makeText(this,"Not Success",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailureResponse(int reqType, Object data) {
        Toast.makeText(UPI_UserAccounts.this,"Fail" , Toast.LENGTH_SHORT).show();
    }





}
