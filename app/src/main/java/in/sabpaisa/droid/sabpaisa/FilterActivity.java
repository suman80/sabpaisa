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
        final LinearLayout BankClient, ClientSpinner, InstituteSpinner,HospitalSpinner;
        Button proceed = (Button)findViewById(R.id.proceed);
        final Spinner bankSpinner, clientSpinner,institutespinner, hospitalspinner;

        BankUser =    (AppCompatCheckBox) findViewById(R.id.rb_bankuser);
        PrivateUser = (AppCompatCheckBox) findViewById(R.id.rbPrivate);

        BankClient =      (LinearLayout) findViewById(R.id.llBankUser);
        ClientSpinner =   (LinearLayout)findViewById(R.id.llClientSpinner);
        InstituteSpinner =(LinearLayout)findViewById(R.id.InstituteSpinner);
        HospitalSpinner = (LinearLayout) findViewById(R.id.HospitalSpinner);

        bankSpinner =      (Spinner) findViewById(R.id.spinnerBank).findViewById(R.id.spinner2);
        clientSpinner =    (Spinner) findViewById(R.id.spinnerClient).findViewById(R.id.spinner2);
        institutespinner = (Spinner) findViewById(R.id.spinnerInsttitute).findViewById(R.id.spinner2);


        BankUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                    if (isChecked) {
                                                        BankClient.setVisibility(View.VISIBLE);
                                                        TextView bankTxt, clientTxt, institute;
                                                        bankTxt = (TextView) findViewById(R.id.spinnerBank).findViewById(R.id.textName);
                                                        clientTxt = (TextView) findViewById(R.id.spinnerClient).findViewById(R.id.textName);
                                                        institute = (TextView) findViewById(R.id.InstituteSpinner).findViewById(R.id.textName);


                                                        // FOr State Name
                                                        bankTxt.setText("State Name");
                                                        // For Services
                                                        clientTxt.setText("Select Services");
                                                        // For Institute
                                                        institute.setText("Select Institute");
                                                        ArrayList<String> banks = new ArrayList<>();
                                                        final ArrayList<String> clients = new ArrayList<>();
                                                        final ArrayList<String> institute1 = new ArrayList<>();
                                                        final ArrayList<String> institute2 =new ArrayList<>();
                                                        banks.add("Select State");
                    /*banks.add("Allahabad Bank");
                    banks.add("DCB Bank");*/
                                                        banks.add("Andaman and Nicobar Islands");
                                                        banks.add("Andhra Pradesh");
                                                        banks.add("Arunachal Pradesh");
                                                        banks.add("Assam");
                                                        banks.add("Bihar");
                                                        banks.add("Chandigarh");
                                                        banks.add("Chhattisgarh");
                                                        banks.add("Dadra and Nagar Haveli");
                                                        banks.add("Daman and Diu");
                                                        banks.add("Delhi");
                                                        banks.add("Goa");
                                                        banks.add("Gujarat");
                                                        banks.add("Haryana");
                                                        banks.add("Himachal Pradesh");
                                                        banks.add("Jammu and Kashmir");
                                                        banks.add("Jharkhand");
                                                        banks.add("Karnataka");
                                                        banks.add("Kerala");
                                                        banks.add("Lakshadweep");
                                                        banks.add("Madhya Pradesh");
                                                        banks.add("Maharashtra");
                                                        banks.add("Manipur");
                                                        banks.add("Meghalaya");
                                                        banks.add("Mizoram");
                                                        banks.add("Nagaland");
                                                        banks.add("Orissa");
                                                        banks.add("Pondicherry");
                                                        banks.add("Punjab");
                                                        banks.add("Rajasthan");
                                                        banks.add("Sikkim");
                                                        banks.add("Tamil Nadu");
                                                        banks.add("Telangana");
                                                        banks.add("Tripura");
                                                        banks.add("Uttaranchal");
                                                        banks.add("Uttar Pradesh");
                                                        banks.add("West Bengal");


                                                        //banks.add("")
                                                        clients.add("Select Services");
                                                        clients.add("Institute");
                                                        clients.add("Hospital");
                                                        clients.add("Others");
                                                        //clients.add("IES");


                                                        institute1.add("Select Institute");
                                                        institute1.add("BSEB ,Patna");

                                                        institute2.add("Select Hospital");
                                                        institute2.add("E-Health,KGMU");

                                                        ArrayAdapter<String> bankAdapter = new ArrayAdapter<String>(FilterActivity.this, android.R.layout.simple_spinner_item, banks);
                                                        bankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        bankSpinner.setAdapter(bankAdapter);

                                                        bankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                            @Override
                                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                                if (position != 0) {
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
                                                                    }, 200);
                                                                }
                                                            }

                                                            @Override
                                                            public void onNothingSelected(AdapterView<?> parent) {

                                                            }
                                                        });

                                                        ArrayAdapter<String> clientAdapter = new ArrayAdapter<String>(FilterActivity.this, android.R.layout.simple_spinner_item, clients);
                                                        clientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        clientSpinner.setAdapter(clientAdapter);

                                                        clientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                            @Override
                                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                                if (position ==1) {
                                                                    final ProgressDialog pd = new ProgressDialog(FilterActivity.this);
                                                                    pd.setMessage("Loading Services");
                                                                    pd.show();
                                                                    Handler handler = new Handler();
                                                                    handler.postDelayed(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            pd.dismiss();
                                                                            InstituteSpinner.setVisibility(View.VISIBLE);
                                                                            ArrayAdapter<String> InstituteAdapter = new ArrayAdapter<String>(FilterActivity.this, android.R.layout.simple_spinner_item, institute1);
                                                                            InstituteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                                            institutespinner.setAdapter(InstituteAdapter);
                                                                        }
                                                                    }, 200);
                                                                }


                                                                 else if(
                                                                         position ==2
                                                                        ) {
                                                                    final ProgressDialog pd = new ProgressDialog(FilterActivity.this);
                                                                    pd.setMessage("Loading Services");
                                                                    pd.show();
                                                                    Handler handler = new Handler();
                                                                    handler.postDelayed(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            pd.dismiss();
                                                                            InstituteSpinner.setVisibility(View.VISIBLE);
                                                                            ArrayAdapter<String> InstituteAdapter = new ArrayAdapter<String>(FilterActivity.this, android.R.layout.simple_spinner_item, institute2);
                                                                            InstituteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                                            institutespinner.setAdapter(InstituteAdapter);
                                                                        }
                                                                    }, 200);
                                                                }
                                                            }

                                                            @Override
                                                            public void onNothingSelected(AdapterView<?> parent) {

                                                            }
                                                        });





                                                    } else {
                                                        ClientSpinner.setVisibility(View.GONE);
                                                        BankClient.setVisibility(View.GONE);
                                                        InstituteSpinner.setVisibility(View.GONE);
                                                    }

                                                    }



/////////////////////////////////////// 14 /09/2017




                /////////////////////////////////////////////////////////////////////////////

               /* clientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0) {
                            final ProgressDialog pd = new ProgressDialog(FilterActivity.this);
                            pd.setMessage("Loading Clients");
                            pd.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pd.dismiss();
                                    InstituteSpinner.setVisibility(View.VISIBLE);

                                    ArrayAdapter<String> instituteAdapter = new ArrayAdapter<String>(FilterActivity.this, android.R.layout.simple_spinner_item,    );
                                    instituteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    institutespinner.setAdapter(instituteAdapter);
                                }
                            }, 200);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
                *//*else

            {
                ClientSpinner.setVisibility(View.GONE);
                BankClient.setVisibility(View.GONE);
            }*/



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
                }
                else
                {
                    Toast.makeText(FilterActivity.this, "Select any Services", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
