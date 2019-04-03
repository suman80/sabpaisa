package in.sabpaisa.droid.sabpaisa.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.AutoCompleteClientAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.HorizontalClientsRecyclerViewAdapter;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.AppDB.ClientsDB;
import in.sabpaisa.droid.sabpaisa.FilterActivity1;
import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.Model.ClientsDataModel;
import in.sabpaisa.droid.sabpaisa.Model.FilterClientModel;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.UIN;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;

import static android.content.Context.MODE_PRIVATE;
import static in.sabpaisa.droid.sabpaisa.AppDB.ClientsDB.TABLE_Client;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClientFilterFragment extends Fragment {


    ArrayList<FilterClientModel> clientList;

    AutoCompleteTextView clientAutoCompleteTextView;

    AutoCompleteClientAdapter autoCompleteClientAdapter;

    Button btn_Proceed;

    public static String MySharedPreffilter="mySharedPrefForFilter";

    String state,clientId,clientLogo,clientImage,clientName,stateId;
    String m,n;
    private boolean _hasLoadedOnce= false; // your boolean field

    ClientsDB clientsDB;

    public ClientFilterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_client_filter, container, false);



        clientAutoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.clientAutoCompleteTextView);
        btn_Proceed = (Button)view.findViewById(R.id.btn_Proceed);

        /*if (!_hasLoadedOnce) {

            if (isOnline()) {

                getClientsList();
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme).create();

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
            }

        }
*/
        btn_Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (clientAutoCompleteTextView.getText().toString() == null || clientAutoCompleteTextView.getText().toString().isEmpty() || clientAutoCompleteTextView.getText().toString().equals("")){
                    clientAutoCompleteTextView.setError("Please enter the client name !");
                }else if (!checkArrayList(clientAutoCompleteTextView.getText().toString())){
                    clientAutoCompleteTextView.setError("No Record Found !");
                }else if (!isOnline()){
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme).create();

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


                    Intent intent = new Intent(getContext(), UIN.class);

                    intent.putExtra("clientLogoPath", clientLogo);
                    intent.putExtra("clientImagePath", clientImage);
                    intent.putExtra("clientname", clientName);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    clientAutoCompleteTextView.setText("");

                    startActivity(intent);

                    SharedPreferences.Editor editor = getContext().getSharedPreferences(MySharedPreffilter, MODE_PRIVATE).edit();
                    editor.putString("clientId", clientId);
                    editor.putInt("stateId", Integer.parseInt(stateId));
                    editor.commit();
                }


            }
        });


        SharedPreferences sharedPreferences = getContext().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        String userAccessToken = sharedPreferences.getString("response", "123");


        clientsDB = new ClientsDB(getContext());

        ArrayList<ClientsDataModel> clientsDataModelArrayList = new ArrayList<>();
        TextView Or = (TextView)view.findViewById(R.id.Or);
        RecyclerView recyclerView = view.findViewById(R.id.clientsRecyclerView);


        if (clientsDB.isTableExists(TABLE_Client)) {

            Cursor res = clientsDB.getParticularClientData(userAccessToken);
            if (res.getCount() > 0) {
                StringBuffer stringBuffer = new StringBuffer();

                while (res.moveToNext()) {
                    stringBuffer.append(res.getString(0) + " ");
                    stringBuffer.append(res.getString(1) + " ");
                    stringBuffer.append(res.getString(2) + " ");
                    stringBuffer.append(res.getString(3) + " ");
                    stringBuffer.append(res.getString(4) + " ");
                    stringBuffer.append(res.getString(5) + " ");
                    stringBuffer.append(res.getString(6) + " ");

                    ClientsDataModel clientsDataModel = new ClientsDataModel();
                    clientsDataModel.setClientId(res.getString(1));
                    clientsDataModel.setClientName(res.getString(2));
                    clientsDataModel.setClientImageUrl(res.getString(3));
                    clientsDataModel.setUserAccessToken(res.getString(4));
                    clientsDataModel.setUinNo(res.getString(5));
                    clientsDataModel.setCobCustId(res.getString(6));
                    clientsDataModelArrayList.add(clientsDataModel);
                }
                Log.d("UIN_Notification", "stringBuffer___ " + stringBuffer);
                //Log.d("PGF_Notification", "grpListDataVal____ " + groupListData.getGroupRecentCommentTime());



                if (clientsDataModelArrayList !=null || !clientsDataModelArrayList.isEmpty()) {

                    Or.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    HorizontalClientsRecyclerViewAdapter adapter = new HorizontalClientsRecyclerViewAdapter(getContext(), clientsDataModelArrayList);
                    recyclerView.setAdapter(adapter);

                }else {
                    Or.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }


            }

        }





        return view;
    }



    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);


        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (isFragmentVisible_ && !_hasLoadedOnce) {

                getClientsList();

                _hasLoadedOnce = true;
            }
        }
    }



    private void getClientsList() {


        String tag_string_req = "req_register";

        String url = AppConfig.Base_Url+AppConfig.App_api+AppConfig.url_clientsall;

        Log.d("CFF_URL","__"+url);

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

                        Toast.makeText(getContext(),"No Result Found",Toast.LENGTH_SHORT).show();

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
                            //Log.d("institutionskip1","121");
                            clientList.add(filterClientModel);
                        }


                        autoCompleteClientAdapter = new AutoCompleteClientAdapter(getContext(),clientList);
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
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }



    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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

        if (clientList != null && !clientList.isEmpty()) {

            for (FilterClientModel filterClientModel : clientList) {

                if (filterClientModel.getOrganization_name().equals(clientName)) {
                    return true;
                }

            }
        }

        return false;
    }




}
