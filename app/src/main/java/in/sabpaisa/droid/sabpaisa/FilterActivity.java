package in.sabpaisa.droid.sabpaisa;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import com.santalu.respinner.ReSpinner;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.autofill.AutofillValue;
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
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;

public class FilterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = FilterActivity.class.getSimpleName();

    ReSpinner  stateSpinner, serviceSpinner,clientsSpinner;;



    Button proceed,skip;


    //Declaring Arraylists
    ArrayList<String> stateArrayList;
    ArrayList<String> serviceArrayList;
    ArrayList<String> clientArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
               setContentView(R.layout.activity_filter);

        proceed = (Button)findViewById(R.id.proceed);
        skip = (Button) findViewById(R.id.skip);

        stateSpinner =      (  ReSpinner ) findViewById(R.id.StateSpinner);
        serviceSpinner =    (  ReSpinner ) findViewById(R.id.ServiceSpinner);
        clientsSpinner = (  ReSpinner ) findViewById(R.id.ClientSpinner);

        stateArrayList = new ArrayList<>();

       getStateData();


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
                if( stateSpinner.getSelectedItemPosition()!=0 && serviceSpinner.getSelectedItemPosition()!=0){

                    String serviceName=serviceSpinner.getSelectedItem().toString();
                    Log.d("serviceName"," "+serviceName);
                    String stateName=stateSpinner.getSelectedItem().toString();
                    Log.d("stateName"," "+stateName);

                    Intent intent = new Intent(FilterActivity.this,MainActivity.class);
                    intent.putExtra("STATENAME",stateName);
                    intent.putExtra("SERVICENAME",serviceName);
                    startActivity(intent);
                }
                else
                {
                   // Toast.makeText(FilterActivity.this, "Select any Services", Toast.LENGTH_SHORT).show();
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

                        if (i == 0) {
                            stateArrayList.add("Select State");
                        }
                        stateArrayList.add(getterSetter.getStateName().toString());
                        ArrayAdapter<String> bankAdapter = new ArrayAdapter<String>(FilterActivity.this, android.R.layout.simple_spinner_item, stateArrayList);
                        bankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                       // stateSpinner.setPrompt("Select");
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

                            @SuppressLint("ResourceAsColor")
                            public void onItemSelected(AdapterView<?> parentView,
                                                       View selectedItemView, int position, long id) {

                                ((TextView) selectedItemView).setTextColor(R.color.tw__blue_default);
                               // ((TextView) selectedItemView).setAllCaps(true);
                                ((TextView) selectedItemView).getGravity();
                                ((TextView) selectedItemView).getText();





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

                try {
                    clientArrayList=new ArrayList<>();
                    jsonObject = new JSONObject(response1);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");

                    Log.d("JSONARRAY","-->"+jsonArray);
                    clientArrayList=new ArrayList<String>();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        ClientData clientData = new ClientData();
                        /*clientData.setId(jsonObject1.getInt("id"));
                        clientData.setClientId(jsonObject1.getString("clientId"));*/
                        clientData.setClientName(jsonObject1.getString("clientName"));
                       /* clientData.setClientCode(jsonObject1.getString("clientCode"));
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
*/
                        if (i==0){
                            clientArrayList.add("Select");
                        }

                        clientArrayList.add(clientData.getClientName().toString());
                    }
/*
                    clientNameArrayList.add(jsonObject1.getString("clientName").toString());
*/
                    Log.d("clientArrayList",""+clientArrayList.get(0));

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item, clientArrayList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    clientsSpinner.setPrompt("Select");
                    clientsSpinner.setSelection(0);
                    clientsSpinner.setAdapter(adapter);
                    clientsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            ((TextView) view).setTextColor(R.color.tw__blue_default);
                            /*((TextView) view).setAllCaps(true);
                            ((TextView) view).getGravity();*/

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

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


                 if (error instanceof NetworkError) {
                     checkNetworkConnection();
                 }


            }


        }) ;

        AppController.getInstance().addToRequestQueue(request,tag_string_req);

    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selected=parent.getItemAtPosition(position).toString();
        ((TextView) view).setTextColor(R.color.tw__blue_default);
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