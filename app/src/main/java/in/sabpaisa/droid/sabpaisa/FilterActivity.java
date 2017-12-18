package in.sabpaisa.droid.sabpaisa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Model.ClientData;
import in.sabpaisa.droid.sabpaisa.Model.StateGetterSetter;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;

public class FilterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = FilterActivity.class.getSimpleName();

    Spinner stateSpinner, serviceSpinner,clientsSpinner, hospitalspinner;
    AppCompatCheckBox BankUser, PrivateUser;
    LinearLayout BankClient, ClientSpinner, InstituteSpinner,HospitalSpinner;
    Button proceed,skip;

     public static  String state_position,clientId;

    //Declaring Arraylists
    ArrayList<String> stateArrayList;
    ArrayList<String> serviceArrayList;
    ArrayList<String> clientArrayList;
    ArrayList<String> clientNameArrayList;
    ArrayList<String> clientIdArrayList;

    String getclient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        proceed = (Button)findViewById(R.id.proceed);
        skip = (Button) findViewById(R.id.skip);



        // BankUser =    (AppCompatCheckBox) findViewById(R.id.rb_bankuser);
        //PrivateUser = (AppCompatCheckBox) findViewById(R.id.rbPrivate);

        BankClient =      (LinearLayout) findViewById(R.id.llBankUser);
        ClientSpinner =   (LinearLayout)findViewById(R.id.llClientSpinner);
        InstituteSpinner =(LinearLayout)findViewById(R.id.InstituteSpinner);
        //HospitalSpinner = (LinearLayout) findViewById(R.id.HospitalSpinner);

        stateSpinner =      (Spinner) findViewById(R.id.spinnerBank).findViewById(R.id.spinner2);
        serviceSpinner =    (Spinner) findViewById(R.id.spinnerClient).findViewById(R.id.spinner2);
        clientsSpinner = (Spinner) findViewById(R.id.spinnerInsttitute).findViewById(R.id.spinner2);

        //Initializing arrayLists
        stateArrayList = new ArrayList<>();
        clientNameArrayList = new ArrayList<>();
        getStateData();


        TextView bankTxt, clientTxt, institute;
        bankTxt = (TextView) findViewById(R.id.spinnerBank).findViewById(R.id.textName);
        clientTxt = (TextView) findViewById(R.id.spinnerClient).findViewById(R.id.textName);
        institute = (TextView) findViewById(R.id.InstituteSpinner).findViewById(R.id.textName);


        // FOr State Name
        bankTxt.setText("State Name");
        // For Services
        clientTxt.setText("Select Services");
        // For Institute
        institute.setText("Select Client");  //Do change here

                   /* ArrayAdapter<String> bankAdapter = new ArrayAdapter<String>(FilterActivity.this, android.R.layout.simple_spinner_item, stateArrayList);
                    bankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    stateSpinner.setAdapter(bankAdapter);

                    stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            state_position = parent.getItemAtPosition(position).toString();

                            getServiceData(state_position);

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
                                        ArrayAdapter<String> clientAdapter = new ArrayAdapter<String>(FilterActivity.this, android.R.layout.simple_spinner_item, serviceArrayList);
                                        clientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        serviceSpinner.setAdapter(clientAdapter);
                                    }
                                }, 200);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {


                        }
                    });

                                                       *//* ArrayAdapter<String> clientadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, serviceArrayList);
                                                        clientadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        serviceSpinner.setAdapter(clientadapter);
*//*
                    serviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override

                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // getting data from volley
                            getClientData(serviceSpinner.getSelectedItem().toString(), stateSpinner.getSelectedItem().toString());

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
                                                                            *//*ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,clientArrayList );
                                                                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                                            clientsSpinner.setAdapter(adapter);*//*


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




        });*/



        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(FilterActivity.this, MainActivitySkip.class));
            }
        });


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stateSpinner.getSelectedItemPosition() != 0 && serviceSpinner.getSelectedItemPosition() != 0) {

                    Log.d("FA_clientId123435423412","---->"+clientId );
                    // getParticularClientdata(clientId);

                    if (stateSpinner.getSelectedItemPosition() != 0 && serviceSpinner.getSelectedItemPosition() != 0 &&clientsSpinner.getSelectedItemPosition()!=0) {


                        //String clientId=serviceSpinner.getSelectedItem().toString();
                        Intent intent = new Intent(FilterActivity.this, MainActivity.class);
                        intent.putExtra("clientId", clientId);
                        startActivity(intent);
                    } else {
                        Toast.makeText(FilterActivity.this, "Select any Services", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    // GetServiceData


    private void getStateData (){

        String  tag_string_req = "req_state";

        StringRequest request=new StringRequest(Request.Method.GET, AppConfig.URL_StateList, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {

                //   Toast.makeText(MainActivity.this,String.valueOf(response), Toast.LENGTH_SHORT).show();

                Log.d("STATE_LIST","-->"+response);
                //parsing Json
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");

                    Log.d("JSONARRAY","-->"+jsonArray);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        // Log.d("jsonObject1",""+jsonObject1.getString("stateName"));
                        StateGetterSetter getterSetter = new StateGetterSetter();

                        getterSetter.setStateId(jsonObject1.getInt("stateId"));
                        getterSetter.setStateCode(jsonObject1.getString("stateCode").toString());
                        getterSetter.setStateName(jsonObject1.getString("stateName").toString());

                        if (i == 0) {
                            stateArrayList.add("Select State");
                        }
                        stateArrayList.add(getterSetter.getStateName().toString());
                        ArrayAdapter<String> bankAdapter = new ArrayAdapter<String>(FilterActivity.this, android.R.layout.simple_spinner_item, stateArrayList);
                        bankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        stateSpinner.setAdapter(bankAdapter);

                        stateSpinner.setOnItemSelectedListener(FilterActivity.this);

                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    checkNetworkConnection();
                }



            }


        });

        AppController.getInstance().addToRequestQueue(request, tag_string_req);

    }

    public void getServiceData(final String state ){


        String  tag_string_req = "req_service";

        StringRequest request=new StringRequest(Request.Method.GET,AppConfig.URL_ClientBasedOnState+state, new Response.Listener<String>(){

            @Override
            public void onResponse(String response1)
            {
                // Log.d("Service_LIST","-->"+URL_ClientBasedOnState+state);

                Log.d("Service_LIST","-->"+response1);
                //parsing Json
                JSONObject jsonObject = null;

                if (response1==null){
                    //
                    // Toast.makeText(FilterActivity.this,"No Data Found",Toast.LENGTH_SHORT).show();

                }else {

                    try {
                        serviceArrayList = new ArrayList<String>();

                        jsonObject = new JSONObject(response1);
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            String str = jsonArray.getString(i);
                            Log.d("1", str);

                            if (i == 0) {
                                serviceArrayList.add("Select Service");
                            }
                            serviceArrayList.add(str);
                        }

                        Log.d("serviceArrayList", "" + serviceArrayList);

                        ArrayAdapter<String> clientadapter = new ArrayAdapter<String>(FilterActivity.this, R.layout.support_simple_spinner_dropdown_item, serviceArrayList);
                        clientadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        serviceSpinner.setAdapter(clientadapter);
                        serviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            public void onItemSelected(AdapterView<?> parentView,
                                                       View selectedItemView, int position, long id) {

                                getClientData(serviceSpinner.getSelectedItem().toString(),stateSpinner.getSelectedItem().toString());

                            }

                            public void onNothingSelected(AdapterView<?> arg0) {// do nothing
                            }

                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NetworkError) {
                    checkNetworkConnection();
                }




            }


        });

        AppController.getInstance().addToRequestQueue(request,tag_string_req);

    }


    private void getClientData(final String serviceName, String state) {

        String  tag_string_req = "req_clients";

        StringRequest request=new StringRequest(Request.Method.GET, AppConfig.URL_ServiceBasedOnState+state+"&service="+serviceName, new Response.Listener<String>(){

            @Override
            public void onResponse(String response1)
            {
                //Toast.makeText(MainActivity.this,String.valueOf(response1), Toast.LENGTH_SHORT).show();

                Log.d("Service_LIST","-->"+response1);
                //parsing Json
                JSONObject jsonObject = null;

                //try {
                clientArrayList=new ArrayList<String>();
                try {
                    jsonObject = new JSONObject(response1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    // if(jsonObject.get("status").equals("failure")) {
                    // Toast.makeText(FilterActivity.this,"no no",Toast.LENGTH_SHORT).show();
                    //  }
                    //else {

                    JSONArray jsonArray = jsonObject.getJSONArray("response");

                    Log.d("JSONARRAY", "-->" + jsonArray);
                    clientArrayList = new ArrayList<String>();

                    clientIdArrayList=new ArrayList<String>();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        ClientData clientData = new ClientData();
                        clientData.setId(jsonObject1.getInt("id"));
                        clientData.setClientId(jsonObject1.getString("clientId"));
                        clientData.setClientName(jsonObject1.getString("clientName"));

                        if (i == 0) {
                            clientArrayList.add("Select");
                            clientIdArrayList.add("0");

                        }
                        clientId=clientData.getClientId().toString();
                        Log.d("FA_ClientId",""+clientId);

                        clientArrayList.add(clientData.getClientName().toString());
                        clientIdArrayList.add(clientData.getClientId().toString());
                    }
    /*
                        clientNameArrayList.add(jsonObject1.getString("clientName").toString());
    */
                    Log.d("clientArrayList", "" + clientArrayList.get(0));

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, clientArrayList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    clientsSpinner.setPrompt("Select");
                    clientsSpinner.setSelection(0);
                    clientsSpinner.setAdapter(adapter);
                    clientsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            final  String selected1=parent.getItemAtPosition(position).toString();
                            //getParticularClientdata(selected1);
                            clientId= clientIdArrayList.get(position);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    // }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                if (error instanceof NetworkError) {
                    checkNetworkConnection();
                }


            }


        }) ;

        AppController.getInstance().addToRequestQueue(request,tag_string_req);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selected=parent.getItemAtPosition(position).toString();
        //  Toast.makeText(this,selected,Toast.LENGTH_SHORT).show();
        getServiceData(selected);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void checkNetworkConnection(){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
