package in.sabpaisa.droid.sabpaisa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.provider.Contacts;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.sabpaisa.droid.sabpaisa.Model.ClientData;
import in.sabpaisa.droid.sabpaisa.Model.FetchUserImageGetterSetter;
import in.sabpaisa.droid.sabpaisa.Model.Institution;
import in.sabpaisa.droid.sabpaisa.Model.StateGetterSetter;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;
import io.fabric.sdk.android.Fabric;

import static in.sabpaisa.droid.sabpaisa.LogInActivity.PREFS_NAME;

public class FilterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener ,ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String TAG = FilterActivity.class.getSimpleName();

    Spinner stateSpinner, serviceSpinner,clientsSpinner, hospitalspinner;
    AppCompatCheckBox BankUser, PrivateUser;
    LinearLayout BankClient, ClientSpinner;
           RelativeLayout InstituteSpinner,HospitalSpinner;
    Button proceed,skip;
    String userImageUrl;
    String userAccessToken,response;
    Map<String, Integer> stateMap;
    SharedPreferences sharedPreferences;
    public static String MySharedPreffilter="mySharedPrefForFilter";

    public static final String PREFS_NAME = "loginPrefs";



ImageView spinnerClick1,spinnerClick2,spinnerClick3;

    ArrayList<Institution> client1ArrayList;

     public static  String state_position,clientId;

    //Declaring Arraylists
    ArrayList<String> stateArrayList;
    ArrayList<String> serviceArrayList;
    ArrayList<String> clientArrayList;
    ArrayList<String> clientNameArrayList;
    ArrayList<String> clientIdArrayList;

    String clientLogoPath;
    String clientImagePath;
    String clientname11,m,n;
    int stateId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);


        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_filter);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        checkConnection();

        SharedPreferences sharedPreferences2 = getApplication().getSharedPreferences(UIN.MYSHAREDPREFUIN, Context.MODE_PRIVATE);
         n=sharedPreferences2.getString("m","xyz");
        m=n;
        if(m.equals("abc")){
            Intent intent=new Intent(FilterActivity.this,MainActivity.class);
           startActivity(intent);
        }

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
       /* if (settings.getString("logged1", "").toString().equals("logged")) {
            Intent intent = new Intent(FilterActivity.this,UIN.class);

            intent.putExtra("clientLogoPath", clientLogoPath);
            intent.putExtra("clientImagePath", clientImagePath);
            intent.putExtra("clientname", clientname11);
            startActivity(intent);
        }*/
        proceed = (Button)findViewById(R.id.proceed);
        skip = (Button) findViewById(R.id.skip);
       // SharedPreferences setting = getSharedPreferences(PREFS_NAME, 0);



        // BankUser =    (AppCompatCheckBox) findViewById(R.id.rb_bankuser);
        //PrivateUser = (AppCompatCheckBox) findViewById(R.id.rbPrivate);

        BankClient =      (LinearLayout) findViewById(R.id.llBankUser);
        ClientSpinner =   (LinearLayout)findViewById(R.id.llClientSpinner);
        InstituteSpinner =(RelativeLayout)findViewById(R.id.InstituteSpinner);
        //HospitalSpinner = (LinearLayout) findViewById(R.id.HospitalSpinner);

        stateSpinner =      (Spinner) findViewById(R.id.spinnerBank).findViewById(R.id.spinner2);
        serviceSpinner =    (Spinner) findViewById(R.id.spinnerClient).findViewById(R.id.spinner2);
        clientsSpinner =    (Spinner) findViewById(R.id.spinnerInsttitute).findViewById(R.id.spinner2);

        spinnerClick1 = (ImageView) findViewById(R.id.spinnerClick1);

        spinnerClick1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateSpinner.performClick();
            }
        });

        spinnerClick2 = (ImageView) findViewById(R.id.spinnerClick2);

        spinnerClick2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceSpinner.performClick();
            }
        });

        spinnerClick3 = (ImageView) findViewById(R.id.spinnerClick3);

        spinnerClick3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientsSpinner.performClick();
            }
        });


        //Initializing arrayLists
        stateArrayList = new ArrayList<>();
        clientNameArrayList = new ArrayList<>();

        stateMap = new HashMap<>();//Added on 1st Feb
        stateMap.put("Select State",-1);//Added on 1st Feb
        getStateData();
        final TextView bankTxt, clientTxt, institute;
        bankTxt = (TextView) findViewById(R.id.spinnerBank).findViewById(R.id.textName);
        clientTxt = (TextView) findViewById(R.id.spinnerClient).findViewById(R.id.textName);
        institute = (TextView) findViewById(R.id.InstituteSpinner).findViewById(R.id.textName);



        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        response = sharedPreferences.getString("response", "123");

        userAccessToken = response;


        Log.d("AccessTokensilter", " " + userAccessToken);

        Log.d("FFResfilter", " " + response);




        getUserIm(userAccessToken);
        Log.d("imagefilter", " " + userImageUrl);


        // FOr State Name
        bankTxt.setText("State Name");
        // For Services
        clientTxt.setText("Select Services");
        // For Institute
        institute.setText("Select Client");  //Do change here

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent intent=new Intent(FilterActivity.this, MainActivitySkip.class);
                intent.putExtra("userImageUrl",userImageUrl);

                startActivity(intent);
            }
        });


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Log.d("FA_clientId123435423412","---->"+clientId );
                    Log.d("FA_clientId123435423412","---->"+clientImagePath );
                    Log.d("FA_clientId123435423412","---->"+clientLogoPath );
                    Log.d("FA_clientId123435423412","---->"+clientname11);
                    // getParticularClientdata(clientId);

                    if (stateSpinner.getSelectedItemPosition() != 0 && serviceSpinner.getSelectedItemPosition() != 0 &&clientsSpinner.getSelectedItemPosition()!=0) {
                     /*   SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("filter", "filter");
                        editor.commit();*/

                        //String clientId=serviceSpinner.getSelectedItem().toString();
                        //Intent intent = new Intent(FilterActivity.this, UIN.class);

//                     SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                            Intent intent = new Intent(FilterActivity.this,UIN.class);

                            intent.putExtra("clientLogoPath", clientLogoPath);
                            intent.putExtra("clientImagePath", clientImagePath);
                            intent.putExtra("clientname", clientname11);
                            startActivity(intent);

                        SharedPreferences.Editor editor = getSharedPreferences(MySharedPreffilter,MODE_PRIVATE).edit();
                        editor.putString("clientId",clientId);
                        editor.putInt("stateId",stateId);
                        editor.commit();

                       // intent.putExtra("clientId", clientId);

                    } else {

                        AlertDialog.Builder builder =new AlertDialog.Builder(FilterActivity.this);
                        builder.setTitle("Select Client");
                        builder.setMessage("Please select one client to proceed further");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        //Toast.makeText(FilterActivity.this, "Select One Client", Toast.LENGTH_SHORT).show();
                    }
                
            }
        });

    }

    // GetServiceData


    private void getStateData (){

        String  tag_string_req = "req_state";

        StringRequest request=new StringRequest(Request.Method.GET, AppConfig.Base_Url+AppConfig.App_api+AppConfig.URL_StateList, new Response.Listener<String>(){

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
                        stateMap.put(getterSetter.getStateName().toString(),getterSetter.getStateId());
                        ArrayAdapter<String> bankAdapter = new ArrayAdapter<String>(FilterActivity.this, R.layout.spinner_item, stateArrayList);
                        bankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        stateSpinner.setAdapter(bankAdapter);

                        stateSpinner.setOnItemSelectedListener(FilterActivity.this);
                        Log.d("statearryli",""+stateArrayList);

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

        StringRequest request=new StringRequest(Request.Method.GET,AppConfig.Base_Url+AppConfig.App_api+AppConfig.URL_ClientBasedOnState+state, new Response.Listener<String>(){

            @Override
            public void onResponse(String response1)
            {
                // Log.d("Service_LIST","-->"+URL_ClientBasedOnState+state);

                Log.d("Service_LIST","-->"+response1);
                //parsing Json
                JSONObject jsonObject = null;

          /*  if (response1.equals("failure"))
            {
                    Toast.makeText(FilterActivity.this,"No Data Found",Toast.LENGTH_SHORT).show();


            }
*/
                    try {
                        serviceArrayList = new ArrayList<String>();

                        jsonObject = new JSONObject(response1);
                        String status = jsonObject.getString("status");
                        String response = jsonObject.getString("response");
                        Log.d("responsefilterser","servicelist=="+response);




                        if (((status.equals("success"))&& (response.length()>0))) {
                            JSONArray jsonArray = jsonObject.getJSONArray("response");

                            if(jsonArray.length() == 0){
                                Log.d("Array SIZE ", " sdfsdfsdfsdfdf"+jsonArray.length());
                                Log.d("{}","[]");
                                AlertDialog.Builder builder =new AlertDialog.Builder(FilterActivity.this);
                                builder.setTitle("");
                                builder.setMessage("No data is available for Selected State");
                                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                            else{
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    String str = jsonArray.getString(i);
                                    Log.d("fikolkk", str);

                                    if (i == 0) {
                                        serviceArrayList.add("Select Service");
                                    }
                                    serviceArrayList.add(str);
                                }

                                Log.d("serviceArrayList", "" + serviceArrayList);

                                ArrayAdapter<String> clientadapter = new ArrayAdapter<String>(FilterActivity.this, R.layout.spinner_item, serviceArrayList);
                                clientadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                serviceSpinner.setAdapter(clientadapter);
                                serviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                    public void onItemSelected(AdapterView<?> parentView,
                                                               View selectedItemView, int position, long id) {

                                        String selectedstate=parentView.getItemAtPosition(position).toString();
                                        SharedPreferences.Editor editor = getSharedPreferences(MySharedPreffilter,MODE_PRIVATE).edit();
                                        editor.putString("selectedstate",selectedstate);
                                        editor.commit();
                                        clientsSpinner.setAdapter(null);
                                        //  getClientData(serviceSpinner.getSelectedItem().toString(), stateSpinner.getSelectedItem().toString());
                                        getClientData(serviceSpinner.getSelectedItem().toString(), stateMap.get(stateSpinner.getSelectedItem().toString())+"");
                                    }

                                    public void onNothingSelected(AdapterView<?> arg0) {// do nothing
                                    }

                                });
                            }

//                            JSONArray jsonArray = jsonObject.getJSONArray("response");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//
//                                String str = jsonArray.getString(i);
//                                Log.d("fikolkk", str);
//
//                                if (i == 0) {
//                                    serviceArrayList.add("Select Service");
//                                }
//                                serviceArrayList.add(str);
//                            }
//
//                            Log.d("serviceArrayList", "" + serviceArrayList);
//
//                            ArrayAdapter<String> clientadapter = new ArrayAdapter<String>(FilterActivity.this, R.layout.spinner_item, serviceArrayList);
//                            clientadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            serviceSpinner.setAdapter(clientadapter);
//                            serviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//                                public void onItemSelected(AdapterView<?> parentView,
//                                                           View selectedItemView, int position, long id) {
//                                    clientsSpinner.setAdapter(null);
//                                  //  getClientData(serviceSpinner.getSelectedItem().toString(), stateSpinner.getSelectedItem().toString());
//                                    getClientData(serviceSpinner.getSelectedItem().toString(), stateMap.get(stateSpinner.getSelectedItem().toString())+"");
//                                }
//
//                                public void onNothingSelected(AdapterView<?> arg0) {// do nothing
//                                }
//
//                            });

                        }



                       // else if ((status.equals("failure")  && ((stateId>0) ))|| (((status.equals("success")&&response.equals("[]" ))&&stateId>0)))

                        else if ((status.equals("failure")  && ((stateId>0) ))) {

                            AlertDialog.Builder builder =new AlertDialog.Builder(FilterActivity.this);
                            builder.setTitle("");
                            builder.setMessage("No data is available for Selected State");
                            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();


                           // Toast.makeText(getApplicationContext(),"Select any other state",Toast.LENGTH_SHORT).show();


                            }

//                        else if((status.equals("success"))  && (response.length()<=0)){
//                            Log.d("{}","[]");
//                            AlertDialog.Builder builder =new AlertDialog.Builder(FilterActivity.this);
//                            builder.setTitle("");
//                            builder.setMessage("No data is available for Selected State");
//                            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            });
//                            AlertDialog alertDialog = builder.create();
//                            alertDialog.show();
//                        }


                    } catch(JSONException e){
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

        AppController.getInstance().addToRequestQueue(request,tag_string_req);

    }


    private void getClientData(final String serviceName, String state) {

        String  tag_string_req = "req_clients";


        StringRequest request=new StringRequest(Request.Method.GET, AppConfig.Base_Url+AppConfig.App_api+AppConfig.URL_ServiceBasedOnState+state+"&service="+serviceName.trim().replace(" ","%20"), new Response.Listener<String>(){


/*        StringRequest request=new StringRequest(Request.Method.GET, AppConfig.URL_ServiceBasedOnState+state+"&service="+serviceName, new Response.Listener<String>(){*/

            @Override
            public void onResponse(String response1)
            {

                Log.d("Service_LIST","-->"+response1);
                //parsing Json
                JSONObject jsonObject = null;

                clientArrayList=new ArrayList<String>();

                try {
                   jsonObject = new JSONObject(response1);
                    String status = jsonObject.getString("status");
                    String response = jsonObject.getString("response");

                    if (status.equals("success"))
                    {

                    JSONArray jsonArray = jsonObject.getJSONArray("response");

                    Log.d("JSONARRAY", "-->" + jsonArray);
                    clientArrayList = new ArrayList<String>();

                    clientIdArrayList=new ArrayList<String>();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        ClientData clientData = new ClientData();
                        //clientData.setId(jsonObject1.getInt("id"));
                        clientData.setClientId(jsonObject1.getString("clientId"));
                        clientData.setClientName(jsonObject1.getString("clientName"));

                        if (i == 0) {
                            clientArrayList.add("Select");
                            clientIdArrayList.add("0");

                        }
                     //   else
                        clientId=clientData.getClientId().toString();
                        Log.d("FA_ClientId",""+clientId);

                        clientArrayList.add(clientData.getClientName().toString());
                        clientIdArrayList.add(clientData.getClientId().toString());
                    }
    /*
                        clientNameArrayList.add(jsonObject1.getString("clientName").toString());
    */
                    Log.d("clientArrayList", "" + clientArrayList.get(0));



                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(FilterActivity.this, R.layout.spinner_item, clientArrayList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    clientsSpinner.setPrompt("Select");
                    clientsSpinner.setSelection(0);
                    clientsSpinner.setAdapter(adapter);

                    clientsSpinner.setAdapter(adapter);
                    clientsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            final  String selectedservice=parent.getItemAtPosition(position).toString();
                            //getParticularClientdata(selected1);
                            SharedPreferences.Editor editor = getSharedPreferences(MySharedPreffilter,MODE_PRIVATE).edit();
                            editor.putString("selectedservice",selectedservice);
                            editor.commit();
                            clientId= clientIdArrayList.get(position);

                            getClientsList(clientId);

                            Log.d("ClientIDCHECKING", "" + clientId);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {


                        }
                    });

                    }else if (status.equals("failure")&&response.equals("Client is not refgistered with us"))
                    {//Toast.makeText(getApplicationContext(),"Client is not refgistered with us",Toast.LENGTH_SHORT).show();

                        Log.d(TAG,"InElseIfPart");

                    }

                    else {Log.d(TAG,"InElsePart"+response);}

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
        Log.d("StateID"," "+selected);
         stateId = stateMap.get(selected);
        serviceSpinner.setAdapter(null);
        clientsSpinner.setAdapter(null);
       // getServiceData(selected);
        if(stateId==0) {


        }
        else {
            getServiceData("" + stateId);
        }
        SharedPreferences.Editor editor = getSharedPreferences(MySharedPreffilter,MODE_PRIVATE).edit();
        editor.putString("selected",selected);
        editor.putInt("stateId",stateId);
        editor.commit();
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
    private void getClientsList(final  String clientId) {

        String  tag_string_req = "req_clients";

        StringRequest request=new StringRequest(Request.Method.POST, AppConfig.Base_Url+AppConfig.App_api+AppConfig.URL_ClientBasedOnClientId+clientId, new Response.Listener<String>(){

            @Override
            public void onResponse(String response1)
            {

                Log.d("Particularclient","-->"+response1);
                //parsing Json
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response1.toString());
                    String response = jsonObject.getString("response");
                    String status = jsonObject.getString("status");
                         Log.d("responsus",""+response);
                         Log.d("statsus",""+status);

                         if (status.equals("success")) {

                             JSONObject jsonObject1 = new JSONObject(response);

                             ClientData clientData = new ClientData();
                             clientData.setClientLogoPath(jsonObject1.getString("clientLogoPath"));
                             clientData.setClientImagePath(jsonObject1.getString("clientImagePath"));
                             clientData.setClientName(jsonObject1.getString("clientName"));

                             clientLogoPath = clientData.getClientLogoPath().toString();
                             clientImagePath = clientData.getClientImagePath().toString();
                             clientname11 = clientData.getClientName().toString();
                             // clientname=clientData.getClientName().toString();
                             Log.d("clientlogooooo", "-->" + clientLogoPath);
                             Log.d("clientimageooo", "-->" + clientImagePath);
                             Log.d("clientiooo", "-->" + clientname11);

                         }else if (status.equals("failure"))
                         {Log.d(TAG,"InElseIfPart"+response);}
                         else {
                             Log.d(TAG,"InElsePart"+response);
                         }
                }
                catch(JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage()==null ||error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getApplication(), R.style.MyDialogTheme).create();

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
//                    alertDialog.show();
                    //Log.e(TAG, "Registration Error: " + error.getMessage());

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
    private void getUserIm(final  String token) {

        String  tag_string_req = "req_clients";

        StringRequest request=new StringRequest(Request.Method.GET, AppConfig.Base_Url+AppConfig.App_api+AppConfig.URL_Show_UserProfileImage+"?token="+token, new Response.Listener<String>(){

            @Override
            public void onResponse(String response1)
            {

                Log.d("Particularclientimage","-->"+response1);
                //parsing Json
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response1.toString());
                    String response = jsonObject.getString("response");
                    String status = jsonObject.getString("status");
                    Log.d("responsefilter",""+response);
                    Log.d("statusfilter",""+status);
                    JSONObject jsonObject1 = new JSONObject(response);
                    FetchUserImageGetterSetter fetchUserImageGetterSetter=new FetchUserImageGetterSetter();
                    fetchUserImageGetterSetter.setUserImageUrl(jsonObject1.getString("userImageUrl"));
                    userImageUrl=fetchUserImageGetterSetter.getUserImageUrl().toString();

                    Log.d("userImageUrlfilter",""+userImageUrl);
                 /*   ClientData clientData=new ClientData();
                    clientData.setClientLogoPath(jsonObject1.getString("clientLogoPath"));
                    clientData.setClientImagePath(jsonObject1.getString("clientImagePath"));
                    clientData.setClientName(jsonObject1.getString("clientName"));

                    clientLogoPath=clientData.getClientLogoPath().toString();
                    clientImagePath=clientData.getClientImagePath().toString();
                    clientname11=clientData.getClientName().toString();
                    // clientname=clientData.getClientName().toString();
                    Log.d("clientlogooooo","-->"+clientLogoPath );
                    Log.d("clientimageooo","-->"+clientImagePath );
                    Log.d("clientiooo","-->"+clientname11 );*/


                }
                catch(JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage()==null ||error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getApplication(), R.style.MyDialogTheme).create();

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
                   // alertDialog.show();
                    //Log.e(TAG, "Registration Error: " + error.getMessage());

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
    @Override
    public void onBackPressed() {
        //super.onBackPressed();


finish();
        moveTaskToBack(true);
        System.exit(0);



    }

    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        AppController.getInstance().setConnectivityListener(this);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }
/*
    public void saveSpinnerPosition(int position){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("spnCalorieRange",position);
        editor.apply();
    }*/
}
