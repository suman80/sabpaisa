package in.sabpaisa.droid.sabpaisa;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Fragments.InstitutionFragment;
import in.sabpaisa.droid.sabpaisa.Model.ClientData;
import in.sabpaisa.droid.sabpaisa.Model.StateGetterSetter;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;

public class FilterActivity extends AppCompatActivity {

    private static final String TAG = FilterActivity.class.getSimpleName();

    Spinner stateSpinner, serviceSpinner,clientsSpinner, hospitalspinner;
    AppCompatCheckBox BankUser, PrivateUser;
    LinearLayout BankClient, ClientSpinner, InstituteSpinner,HospitalSpinner;
    Button proceed,skip;

    String state_position;

    //Declaring Arraylists
    ArrayList<String> stateArrayList;
    ArrayList<String> serviceArrayList;
    ArrayList<ClientData> clientArrayList;
    ArrayList<String> clientNameArrayList;

    String getclient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        proceed = (Button)findViewById(R.id.proceed);
        skip = (Button) findViewById(R.id.skip);



        BankUser =    (AppCompatCheckBox) findViewById(R.id.rb_bankuser);
        //PrivateUser = (AppCompatCheckBox) findViewById(R.id.rbPrivate);

        BankClient =      (LinearLayout) findViewById(R.id.llBankUser);
        ClientSpinner =   (LinearLayout)findViewById(R.id.llClientSpinner);
        InstituteSpinner =(LinearLayout)findViewById(R.id.InstituteSpinner);
        HospitalSpinner = (LinearLayout) findViewById(R.id.HospitalSpinner);

        stateSpinner =      (Spinner) findViewById(R.id.spinnerBank).findViewById(R.id.spinner2);
        serviceSpinner =    (Spinner) findViewById(R.id.spinnerClient).findViewById(R.id.spinner2);
        clientsSpinner = (Spinner) findViewById(R.id.spinnerInsttitute).findViewById(R.id.spinner2);

        //Initializing arrayLists
        stateArrayList = new ArrayList<>();
        clientNameArrayList = new ArrayList<>();
        getStateData();

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
                    institute.setText("Select Institute");  //Do change here

/*-------------------------------------------------------------------------------------------------------------------------------------------------*/
                                                        /*ArrayList<String> banks = new ArrayList<>();
                                                        final ArrayList<String> clients = new ArrayList<>();
                                                        final ArrayList<String> institute1 = new ArrayList<>();
                                                        final ArrayList<String> institute2 =new ArrayList<>();*/
                                                        /*banks.add("Select State");
                    *//*banks.add("Allahabad Bank");
                    banks.add("DCB Bank");*//*
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
*/

                                                       /* //banks.add("")
                                                        clients.add("Select Services");
                                                        clients.add("Institute");
                                                        clients.add("Hospital");
                                                        clients.add("Others");
                                                        //clients.add("IES");


                                                        institute1.add("Select Institute");
                                                        institute1.add("BSEB ,Patna");

                                                        institute2.add("Select Hospital");
                                                        institute2.add("E-Health,KGMU");*/

                    ArrayAdapter<String> bankAdapter = new ArrayAdapter<String>(FilterActivity.this, android.R.layout.simple_spinner_item, stateArrayList);
                    bankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    stateSpinner.setAdapter(bankAdapter);

                    stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            state_position=parent.getItemAtPosition(position).toString();

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

                                                       /* ArrayAdapter<String> clientadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, serviceArrayList);
                                                        clientadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        serviceSpinner.setAdapter(clientadapter);
*/
                    serviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override

                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // getting data from volley
                            getClientData(serviceSpinner.getSelectedItem().toString(),stateSpinner.getSelectedItem().toString());

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
                                                                            /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,clientArrayList );
                                                                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                                            clientsSpinner.setAdapter(adapter);*/


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

       /* BankUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PrivateUser.isChecked()) {
                    PrivateUser.setChecked(false);
                    BankUser.setChecked(true);
                }
            }
        });*/
        /*PrivateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BankUser.isChecked()) {
                    BankUser.setChecked(false);
                    PrivateUser.setChecked(true);
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
                if(BankUser.isChecked() && stateSpinner.getSelectedItemPosition()!=0 && serviceSpinner.getSelectedItemPosition()!=0){

                    String serviceName=serviceSpinner.getSelectedItem().toString();
                    Log.d("serviceName"," "+serviceName);
                    String stateName=stateSpinner.getSelectedItem().toString();
                    Log.d("stateName"," "+stateName);

                    Intent intent = new Intent(FilterActivity.this,MainActivity.class);
                    intent.putExtra("STATENAME",stateName);
                    intent.putExtra("SERVICENAME",serviceName);
                    startActivity(intent);
                }/*else if (PrivateUser.isChecked()){
                    startActivity(new Intent(FilterActivity.this,MainActivity.class));
                }*/
                else
                {
                    Toast.makeText(FilterActivity.this, "Select any Services", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

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

                        if(i==0)
                        {
                            stateArrayList.add("Select State");
                        }
                        stateArrayList.add(getterSetter.getStateName().toString());

                    }

                }catch(JSONException e){
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage()==null ||error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(FilterActivity.this, R.style.MyDialogTheme).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Network/Connection Error");

                    // Setting Dialog Message
                    alertDialog.setMessage("Internet Connection is poor OR The Server is taking too long to respond.Please try again later.Thank you.");

                    // Setting Icon to Dialog
                    //  alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                    Log.e(TAG, "Registration Error: " + error.getMessage());

                } else if (error instanceof AuthFailureError) {

                    //TODO
                } else if (error instanceof ServerError) {

                    //TODO
                } else if (error instanceof NetworkError) {

                    //TODO
                } else if (error instanceof ParseError) {

                    //TODO
                }



            }


        });

        AppController.getInstance().addToRequestQueue(request, tag_string_req);

    }


    // GetServiceData

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
                    Toast.makeText(FilterActivity.this,"No Data Found",Toast.LENGTH_SHORT).show();

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

                        ArrayAdapter<String> clientadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, serviceArrayList);
                        clientadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        serviceSpinner.setAdapter(clientadapter);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage()==null ||error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(FilterActivity.this, R.style.MyDialogTheme).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Network/Connection Error");

                    // Setting Dialog Message
                    alertDialog.setMessage("Internet Connection is poor OR The Server is taking too long to respond.Please try again later.Thank you.");

                    // Setting Icon to Dialog
                    //  alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                    Log.e(TAG, "Registration Error: " + error.getMessage());

                } else if (error instanceof AuthFailureError) {

                    //TODO
                } else if (error instanceof ServerError) {

                    //TODO
                } else if (error instanceof NetworkError) {

                    //TODO
                } else if (error instanceof ParseError) {

                    //TODO
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

                try {
                    jsonObject = new JSONObject(response1);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");

                    Log.d("JSONARRAY","-->"+jsonArray);
                    clientArrayList=new ArrayList<ClientData>();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        ClientData clientData = new ClientData();
                        clientData.setId(jsonObject1.getInt("id"));
                        clientData.setClientId(jsonObject1.getString("clientId"));
                        clientData.setClientName(jsonObject1.getString("clientName"));
                        clientData.setClientCode(jsonObject1.getString("clientCode"));
                        clientData.setClientContact(jsonObject1.getString("clientContact"));
                        clientData.setClientEmail(jsonObject1.getString("clientEmail"));
                        clientData.setClientImagePath(jsonObject1.getString("clientImagePath"));
                        clientData.setClientLink(jsonObject1.getString("clientLink"));
                        clientData.setClientAuthenticationType(jsonObject1.getString("clientAuthenticationType"));
                        clientData.setPaymentMode(jsonObject1.getString("paymentMode"));
                        clientData.setBid(jsonObject1.getString("bid"));
                        clientData.setState(jsonObject1.getString("state"));
                        clientData.setAddress(jsonObject1.getString("address"));
                        clientData.setProductName(jsonObject1.getString("productName"));
                        clientData.setService(jsonObject1.getString("service"));
                        clientData.setSuccessUrl(jsonObject1.getString("successUrl"));
                        clientData.setFailedUrl(jsonObject1.getString("failedUrl"));
                        clientData.setClientLogoPath(jsonObject1.getString("clientLogoPath"));
                        clientData.setLandingPage(jsonObject1.getString("landingPage"));
                        if(i==0)
                            clientNameArrayList.add("Select");
                        else
                            clientNameArrayList.add(clientArrayList.get(i-1).getClientName());
                        clientArrayList.add(clientData);
                    }
                    clientNameArrayList.add(clientArrayList.get(clientArrayList.size()-1).getClientName());
                    Log.d("clientArrayList",""+clientArrayList.get(0).getClientName());

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item, clientNameArrayList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    clientsSpinner.setPrompt("Select");
                    clientsSpinner.setSelection(0);
                    clientsSpinner.setAdapter(adapter);

                    clientsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            String selectedItem=parent.getItemAtPosition(position).toString();
                            Log.d("selectedItem","-->"+selectedItem);
                            if(position > 0) Log.d("CLient", " "+clientArrayList.get(position-1).getClientName());
                            //Toast.makeText(FilterActivity.this,""+selectedItem,Toast.LENGTH_SHORT).show();
                            getclient=selectedItem;
                            Log.d("getclient",""+getclient);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                }
                catch(JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage()==null ||error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(FilterActivity.this, R.style.MyDialogTheme).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Network/Connection Error");

                    // Setting Dialog Message
                    alertDialog.setMessage("Internet Connection is poor OR The Server is taking too long to respond.Please try again later.Thank you.");

                    // Setting Icon to Dialog
                    //  alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                    Log.e(TAG, "Registration Error: " + error.getMessage());

                } else if (error instanceof AuthFailureError) {

                    //TODO
                } else if (error instanceof ServerError) {

                    //TODO
                } else if (error instanceof NetworkError) {

                    //TODO
                } else if (error instanceof ParseError) {

                    //TODO
                }


            }


        }) ;

        AppController.getInstance().addToRequestQueue(request,tag_string_req);

    }



}