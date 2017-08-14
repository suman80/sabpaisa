package in.sabpaisa.droid.sabpaisa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import static java.security.AccessController.getContext;

public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);



Button proceed =(Button) findViewById(R.id.proceed);


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });


        TextView serviceTxt,stateTxt,institudeTxt;
        Spinner serviceSpinner,stateSpinner;

        serviceTxt = (TextView)findViewById(R.id.serviceLayout).findViewById(R.id.textName);
        stateTxt = (TextView)findViewById(R.id.stateLayout).findViewById(R.id.textName);
        institudeTxt = (TextView)findViewById(R.id.institudeLayout).findViewById(R.id.textName);

        serviceSpinner = (Spinner) findViewById(R.id.serviceLayout).findViewById(R.id.spinner2);
        stateSpinner = (Spinner)findViewById(R.id.stateLayout).findViewById(R.id.spinner2);


        serviceTxt.setText("Bank");
        stateTxt.setText("Client");
        institudeTxt.setText("Private user");

        ArrayList<String> services = new ArrayList<>();
        ArrayList<String> states = new ArrayList<>();


        services.add("Select Bank");
        services.add("Allahabad Bank");
        services.add("DCB Bank");
        states.add("Select Client");
        states.add("BSEB");
        states.add("COA");
        states.add("CCS");
        states.add("IES");



        ArrayAdapter<String> serviceAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,services);
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceSpinner.setAdapter(serviceAdapter);




        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.india_client, android.R.layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(stateAdapter);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0){
                    final ProgressDialog pd = new ProgressDialog(getApplicationContext());
                    pd.setMessage("Loading Institudes");
                    pd.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            ArrayList<String> institutions = new ArrayList<>();
                            institutions.add("Select Institute");
                            Random random = new Random();
                            int i = random.nextInt(10);
                            for (int t=0;t<i;t++){
                                institutions.add("institude"+t);
                            }
                            Spinner institudeSpinner = (Spinner)findViewById(R.id.institudeLayout).findViewById(R.id.spinner2);
                            ArrayAdapter<String> instituteAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,institutions);
                            instituteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            institudeSpinner.setAdapter(instituteAdapter);
                        }
                    }, 200);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }
}
