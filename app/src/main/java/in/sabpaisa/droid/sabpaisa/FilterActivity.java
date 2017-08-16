package in.sabpaisa.droid.sabpaisa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import static java.security.AccessController.getContext;

public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        final AppCompatCheckBox BankUser, PrivateUser;
        final LinearLayout BankClient, ClientSpinner;
        Button proceed = (Button)findViewById(R.id.proceed);
        final Spinner bankSpinner, clientSpinner;

        BankUser = (AppCompatCheckBox) findViewById(R.id.rb_bankuser);
        PrivateUser = (AppCompatCheckBox) findViewById(R.id.rbPrivate);
        BankClient = (LinearLayout) findViewById(R.id.llBankUser);
        ClientSpinner = (LinearLayout)findViewById(R.id.llClientSpinner);

        bankSpinner = (Spinner) findViewById(R.id.spinnerBank).findViewById(R.id.spinner2);
        clientSpinner = (Spinner) findViewById(R.id.spinnerClient).findViewById(R.id.spinner2);

        BankUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    BankClient.setVisibility(View.VISIBLE);

                    TextView bankTxt, clientTxt;


                    bankTxt = (TextView) findViewById(R.id.spinnerBank).findViewById(R.id.textName);
                    clientTxt = (TextView) findViewById(R.id.spinnerClient).findViewById(R.id.textName);



                    bankTxt.setText("Bank");
                    clientTxt.setText("Client");

                    ArrayList<String> banks = new ArrayList<>();
                    final ArrayList<String> clients = new ArrayList<>();

                    banks.add("Select Bank");
                    banks.add("Allahabad Bank");
                    banks.add("DCB Bank");
                    clients.add("Select Client");
                    clients.add("BSEB");
                    clients.add("COA");
                    clients.add("CCS");
                    clients.add("IES");

                    ArrayAdapter<String> bankAdapter = new ArrayAdapter<String>(FilterActivity.this, android.R.layout.simple_spinner_item, banks);
                    bankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    bankSpinner.setAdapter(bankAdapter);

                    bankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position!=0){
                                final ProgressDialog pd = new ProgressDialog(FilterActivity.this);
                                pd.setMessage("Loading Clients");
                                pd.show();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        pd.dismiss();
                                        ClientSpinner.setVisibility(View.VISIBLE);
                                        ArrayAdapter<String> clientAdapter = new ArrayAdapter<String>(FilterActivity.this, android.R.layout.simple_spinner_item, clients);
                                        clientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        clientSpinner.setAdapter(clientAdapter);
                                    }
                                },200);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else {
                    ClientSpinner.setVisibility(View.GONE);
                    BankClient.setVisibility(View.GONE);
                }
            }
        });

        BankUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PrivateUser.isChecked()) {
                    PrivateUser.setChecked(false);
                    BankUser.setChecked(true);
                }
            }
        });
        PrivateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BankUser.isChecked()) {
                    BankUser.setChecked(false);
                    PrivateUser.setChecked(true);
                }
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BankUser.isChecked() && bankSpinner.getSelectedItemPosition()!=0 && clientSpinner.getSelectedItemPosition()!=0){
                    startActivity(new Intent(FilterActivity.this,MainActivity.class));
                }else if (PrivateUser.isChecked()){
                    startActivity(new Intent(FilterActivity.this,MainActivity.class));
                }else {
                    Toast.makeText(FilterActivity.this, "Select the Usability Type", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
