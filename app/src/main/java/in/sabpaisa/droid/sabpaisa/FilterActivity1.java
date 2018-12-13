package in.sabpaisa.droid.sabpaisa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import in.sabpaisa.droid.sabpaisa.Adapter.AutoCompleteClientAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.SkipMainClientsAdapter;
import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.Model.FilterClientModel;
import in.sabpaisa.droid.sabpaisa.Model.SkipClientData;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;

public class FilterActivity1 extends AppCompatActivity {

    ArrayList<FilterClientModel> clientList;

    AutoCompleteTextView clientAutoCompleteTextView;

    AutoCompleteClientAdapter autoCompleteClientAdapter;

    Button btn_Proceed;

    public static String MySharedPreffilter="mySharedPrefForFilter";

    String state,clientId,clientLogo,clientImage,clientName,stateId;
    String m,n;

    //Test//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter1);

        SharedPreferences sharedPreferences2 = getApplication().getSharedPreferences(UIN.MYSHAREDPREFUIN, Context.MODE_PRIVATE);
        n=sharedPreferences2.getString("m","xyz");
        m=n;
        if(m.equals("abc")){
            Intent intent=new Intent(FilterActivity1.this,MainActivity.class);
            startActivity(intent);
        }


        clientAutoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.clientAutoCompleteTextView);
        btn_Proceed = (Button) findViewById(R.id.btn_Proceed);

        getClientsList();


        btn_Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (clientAutoCompleteTextView.getText().toString() == null || clientAutoCompleteTextView.getText().toString().isEmpty() || clientAutoCompleteTextView.getText().toString().equals("")){
                    clientAutoCompleteTextView.setError("Please enter the client name !");
                }else if (!checkArrayList(clientAutoCompleteTextView.getText().toString())){
                    clientAutoCompleteTextView.setError("No Record Found !");
                }else if (!isOnline()){
                    AlertDialog alertDialog = new AlertDialog.Builder(FilterActivity1.this, R.style.MyDialogTheme).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("No Internet Connection");

                    // Setting Dialog Message
                    alertDialog.setMessage("Please check internet connection and try again. Thank you.");


                    alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                }else {


                    Log.d("FilterActivity1", "ProceedBtn_Val___" + "State: " + state + "ClientId: " + clientId + "ClientLogo: " + clientLogo + "ClientImage: " + clientImage + "ClientName: " + clientName + "StateId: " + stateId);


                    Intent intent = new Intent(FilterActivity1.this, UIN.class);

                    intent.putExtra("clientLogoPath", clientLogo);
                    intent.putExtra("clientImagePath", clientImage);
                    intent.putExtra("clientname", clientName);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    clientAutoCompleteTextView.setText("");

                    startActivity(intent);

                    SharedPreferences.Editor editor = getSharedPreferences(MySharedPreffilter, MODE_PRIVATE).edit();
                    editor.putString("clientId", clientId);
                    editor.putInt("stateId", Integer.parseInt(stateId));
                    editor.commit();
                }

            }
        });


    }


    private void getClientsList() {


        String tag_string_req = "req_register";

        String url = AppConfig.Base_Url+AppConfig.App_api+AppConfig.url_clientsall;

        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d("FA1","__"+response);

                try {

                    clientList = new ArrayList<FilterClientModel>();

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    String response1 = jsonObject.getString("response");

                    if (status.equals("success")&&response1.equals("No_Record_Found")) {

                        Toast.makeText(FilterActivity1.this,"No Result Found",Toast.LENGTH_SHORT).show();

                    }
                    else {
                        JSONArray jsonArray = jsonObject.getJSONArray("response");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            FilterClientModel filterClientModel = new FilterClientModel();

                            filterClientModel.setOrganizationId(jsonObject1.getString("clientId"));
                            filterClientModel.setOrganization_name(jsonObject1.getString("clientName"));
                            filterClientModel.setOrgLogo(jsonObject1.getString("clientLogoPath"));
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("lookupState");
                            filterClientModel.setOrgAddress(jsonObject2.getString("stateName"));
                            filterClientModel.setStateId(jsonObject2.getString("stateId"));
                            filterClientModel.setOrgWal(jsonObject1.getString("clientImagePath"));
                            Log.d("institutionskip1","121");
                            clientList.add(filterClientModel);
                        }


                        autoCompleteClientAdapter = new AutoCompleteClientAdapter(FilterActivity1.this,clientList);
                        clientAutoCompleteTextView.setAdapter(autoCompleteClientAdapter);

                        clientAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                FilterClientModel selected = (FilterClientModel) adapterView.getItemAtPosition(i);

                                Log.d("FilterActivity1","selected_ "+selected);

                                Log.d("FilterActivity1","state_"+selected.getOrgAddress());
                                Log.d("FilterActivity1","ID_"+selected.getOrganizationId());
                                Log.d("FilterActivity1","logo_"+selected.getOrgLogo());
                                Log.d("FilterActivity1","image_"+selected.getOrgWal());
                                Log.d("FilterActivity1","name_"+selected.getOrganization_name());
                                Log.d("FilterActivity1","stateId_"+selected.getStateId());

                                state = selected.getOrgAddress();
                                clientId = selected.getOrganizationId();
                                clientLogo = selected.getOrgLogo();
                                clientImage = selected.getOrgWal();
                                clientName = selected.getOrganization_name();
                                stateId = selected.getStateId();



                            }
                        });


                    }
                }
                // Try and catch are included to handle any errors due to JSON
                catch(JSONException e){
                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();

                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FilterActivity1.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
    }



    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v("EditGroup", "Internet Connection Not Present");
            return false;
        }
    }


    public boolean checkArrayList(String clientName){
        for (FilterClientModel filterClientModel:clientList) {

            if (filterClientModel.getOrganization_name().equals(clientName)){
                return true;
            }

        }

        return false;
    }


}
