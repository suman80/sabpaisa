package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.olive.upi.OliveUpiManager;
import com.olive.upi.transport.OliveUpiEventListener;
import com.olive.upi.transport.api.Result;
import com.olive.upi.transport.api.UpiService;
import com.olive.upi.transport.model.Bank;

import java.util.ArrayList;

public class UPIBankList extends AppCompatActivity implements OliveUpiEventListener {

    ListView bankList;

    ArrayList<Bank> bankListArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upibank_list);

        OliveUpiManager.getInstance(UPIBankList.this).setListener(this);
        OliveUpiManager.getInstance(UPIBankList.this).fetchListBanks();

        bankList = (ListView)findViewById(R.id.bankList);
        bankListArrayList=new ArrayList<>();

        ArrayAdapter<Bank> itemsAdapter = new ArrayAdapter<Bank>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,bankListArrayList );

        bankList.setAdapter(itemsAdapter);


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
