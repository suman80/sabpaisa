package in.sabpaisa.droid.sabpaisa;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by SabPaisa on 27-07-2017.
 */

public class ApplicationNoFragment extends Fragment {


    View rootView;
    RecyclerView recyclerView;
    ApplicationAdapter adapter;
    ArrayList<String> formNames;

    public ApplicationNoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_application, container, false);

        TextView serviceTxt,stateTxt,institudeTxt;
        Spinner serviceSpinner,stateSpinner;

        serviceTxt = (TextView)rootView.findViewById(R.id.serviceLayout).findViewById(R.id.textName);
//        stateTxt = (TextView)rootView.findViewById(R.id.stateLayout).findViewById(R.id.textName);
  //      institudeTxt = (TextView)rootView.findViewById(R.id.institudeLayout).findViewById(R.id.textName);

        serviceSpinner = (Spinner) rootView.findViewById(R.id.serviceLayout).findViewById(R.id.spinner2);
//        stateSpinner = (Spinner)rootView.findViewById(R.id.stateLayout).findViewById(R.id.spinner2);


        serviceTxt.setText("Client");
    //    stateTxt.setText("State Name");
      //  institudeTxt.setText("Institution Name");

        ArrayList<String> services = new ArrayList<>();
  //      ArrayList<String> states = new ArrayList<>();


        services.add("Select Client");
        services.add("Allahabad Bank");
        services.add("Corporation Bank");
      /*  states.add("Select State");
        states.add("dummy21");
        states.add("dummy21");
        states.add("dummy22");
        states.add("dummy23");*/


        ArrayAdapter<String> serviceAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,services);
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceSpinner.setAdapter(serviceAdapter);

        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(getContext(),R.array.india_states, android.R.layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //stateSpinner.setAdapter(stateAdapter);

        /*stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0){
                    final ProgressDialog pd = new ProgressDialog(getContext());
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
                            Spinner institudeSpinner = (Spinner)rootView.findViewById(R.id.institudeLayout).findViewById(R.id.spinner2);
                            ArrayAdapter<String> instituteAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,institutions);
                            instituteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            institudeSpinner.setAdapter(instituteAdapter);
                        }
                    }, 200);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        return rootView;
    }
}


