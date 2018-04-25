package in.sabpaisa.droid.sabpaisa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.olive.upi.OliveUpiManager;
import com.olive.upi.transport.OliveUpiEventListener;
import com.olive.upi.transport.api.Result;
import com.olive.upi.transport.api.UpiService;
import com.olive.upi.transport.model.Account;
import com.olive.upi.transport.model.BeneVpa;
import com.olive.upi.transport.model.CustomerBankAccounts;
import com.olive.upi.transport.model.PendingReqVo;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;

public class Collect_Approve_Reject extends AppCompatActivity implements OliveUpiEventListener{

    TextView tv_vpa,tv_accountNumber;
    EditText et_amount,et_remark;
    Button btn_Approve,btn_Decline;
    Account account;
    Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_collect__approve__reject);

        OliveUpiManager.getInstance(Collect_Approve_Reject.this).setListener(this);
        OliveUpiManager.getInstance(Collect_Approve_Reject.this).fetchMyAccounts();

        mtoolbar = (Toolbar)findViewById(R.id.toolbar);
        tv_vpa=(TextView)findViewById(R.id.tv_vpa);
        tv_accountNumber=(TextView)findViewById(R.id.tv_accountNumber);
        et_amount=(EditText)findViewById(R.id.et_amount);
        et_remark=(EditText)findViewById(R.id.et_remark);
        btn_Approve=(Button)findViewById(R.id.btn_Approve);
        btn_Decline=(Button)findViewById(R.id.btn_Decline);

        mtoolbar.setTitle("Request Money");
        setSupportActionBar(mtoolbar);

        btn_Approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeneVpa vpa = new BeneVpa();
                vpa.setName("Aditya");
                vpa.setVpa("cde@dcb");//Given by aditya dcb
                vpa.setNickname("Nikki");
                String merchantvpa = "onkar@dcb";
                String merchantid = "131";
                String submerchantid = "550";
                String merchantchannelid = "121";
                String trantype = "P2P";
                String remarks=et_remark.getText().toString();

                String amount="";

                if (!et_amount.getText().toString().contains("."))
                {
                    amount=et_amount.getText().toString().concat(".00").trim();
                }
                else
                {
                    amount=et_amount.getText().toString().trim();
                }

                Log.d("amount_CAR",""+amount);


                String txnId = "AXI1234567890123456789";
                OliveUpiManager.getInstance(Collect_Approve_Reject.this).collectApprove(account, vpa, amount, remarks, txnId, "0517", merchantvpa, merchantid, submerchantid, merchantchannelid, trantype);

            }
        });

        btn_Decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Doubt what is PendingReqvo
                PendingReqVo pendingReqVo = new PendingReqVo();// Pass JSON Object here
                OliveUpiManager.getInstance(Collect_Approve_Reject.this).collectReject(pendingReqVo);
            }
        });


    }


    @Override
    public void onSuccessResponse(int reqType, Object data) {
        Log.d("Main", "onSuccessResponse: reqType " + reqType + " data " + data);

        //Fetch All Linked Accounts(5)
        if (reqType == UpiService.REQUEST_ALL_ACCOUNTS) {
            ArrayList<CustomerBankAccounts> bankAccounts = (ArrayList<CustomerBankAccounts>) data;
            Log.d("REQUEST_ALL_ACCOUNTS", "--->" + bankAccounts.get(0).getBankName());
            Log.d("REQUEST_ALL_ACCOUNTS", "--->" + bankAccounts.get(0).getBankCode());
            Log.d("REQUEST_ALL_ACCOUNTS", "--->" + bankAccounts.get(0).getAccounts());
            Log.d("REQUEST_ALL_ACCOUNTS", "--->" + bankAccounts.get(0).getInput());
            if (bankAccounts != null && bankAccounts.size() > 0) {
                account = (Account) bankAccounts.get(0).getAccounts().get(0);
                tv_accountNumber.setText(account.getMaskedAccnumber());
                Log.d("Val_Acc", "-->" + account.toString());


            }
        }

        //This API is used to approve the collect request.
        else if (reqType== UpiService. REQUEST_COLLECT_NOTIFICATION_PAY){

            Result<ArrayList<String>> requestCollect = (Result<ArrayList<String>>) data;

            if (requestCollect.getCode().equals("00")){
                Log.d("REQUEST_COLLECT","-->"+requestCollect);
            }

        }

//This API is used to decline the collect request.
        else if (reqType== UpiService.REQUEST_GET_REJECTED_NOTIFICATION){
            Result<ArrayList<String>> rejectCollect = (Result<ArrayList<String>>) data;
            Log.d("rejectCollect","--->"+rejectCollect.getData().size());
            if (rejectCollect.getCode().equals("00")){
                Log.d("rejectCollect","--->"+rejectCollect);
            }

        }



    }

    @Override
    public void onFailureResponse(int reqType, Object data) {
        Toast.makeText(Collect_Approve_Reject.this,"Fail" , Toast.LENGTH_SHORT).show();
    }



}
