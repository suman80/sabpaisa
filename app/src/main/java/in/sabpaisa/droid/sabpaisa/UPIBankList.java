package in.sabpaisa.droid.sabpaisa;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.olive.upi.OliveUpiManager;
import com.olive.upi.transport.OliveUpiEventListener;
import com.olive.upi.transport.api.Result;
import com.olive.upi.transport.api.UpiService;
import com.olive.upi.transport.model.Bank;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.BankListAdapter;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;

public class UPIBankList extends AppCompatActivity implements OliveUpiEventListener {

    GridView bankList;

    ArrayList<Bank> bankListArrayList;

    BankListAdapter bankListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_upibank_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        OliveUpiManager.getInstance(UPIBankList.this).setListener(this);
        OliveUpiManager.getInstance(UPIBankList.this).fetchListBanks();
        bankListArrayList=new ArrayList<>();
        bankList = (GridView) findViewById(R.id.bankList);


/*
        bankListAdapter = new BankListAdapter(UPIBankList.this,R.layout.custom_upibanklist,bankListArrayList);

        bankList.setAdapter(bankListAdapter);
*/

    }

    @Override
    public void onSuccessResponse(int reqType, Object data) {
        //Fetch List Banks(1)
        //This API is used to get the list of the UPI enabled banks.
        Log.d("reqTypeForBank",""+reqType);
        Log.d("dataForBank",""+data);
         if(reqType == UpiService.REQUEST_LIST_BANKS){

            Result<ArrayList<Bank>> banklist = (Result<ArrayList<Bank>>) data;
            if(banklist.getCode().equals("00")){
                Toast.makeText(this,"list Banks success",Toast.LENGTH_SHORT).show();

                for(Bank bank : banklist.getData()){
                    bank.getIin();
                    bank.getLogo();
                    bank.getName();
                    bankListArrayList.add(bank);
                }

                bankListAdapter = new BankListAdapter(UPIBankList.this,R.layout.custom_upibanklist,bankListArrayList);

                bankList.setAdapter(bankListAdapter);

                bankList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String bankpos = String.valueOf(bankListArrayList.get(position));
                        String requiredIin = bankpos.substring(bankpos.lastIndexOf(" ")+ 1 , bankpos.length());
                        Log.d("REQUIRED_IIN-->","REQUIRED_IIN"+requiredIin);
                        Toast.makeText(getApplicationContext(),"Bank"+bankpos+" "+requiredIin,Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(UPIBankList.this,FetchAccBasedOnIIN.class);
                        intent.putExtra("IIN",requiredIin);
                        startActivity(intent);

                    }
                });


                //bankListArrayList.add(banklist.toString());

                Log.d("BANK_LIST_ARRAYLIST"," "+bankListArrayList);



            }
        }
    }

    @Override
    public void onFailureResponse(int reqType, Object data) {
        Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show();
        Log.d("Failed"," "+bankListArrayList);
    }
}
