package in.sabpaisa.droid.sabpaisa.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import in.sabpaisa.droid.sabpaisa.Adapter.InstitutionAdapter;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.MainActivity;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Model.Institution;
import in.sabpaisa.droid.sabpaisa.R;

/**
 * Created by abc on 14-06-2017.
 */

public class ProceedInstitiutionFragment extends Fragment {

    View rootView;
    RecyclerView recyclerViewInstitutions;
    InstitutionAdapter institutionAdapter;
    //ArrayList<Institution> institutions;
    ArrayList<Institution> clientArrayList ;


    String stateName,serviceName;

    public ProceedInstitiutionFragment() {

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_institutions, container, false);
        recyclerViewInstitutions = (RecyclerView) rootView.findViewById(R.id.recycler_view_institutions);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),10));
        recyclerViewInstitutions.setLayoutManager(llm);
        /*institutions = new ArrayList<>();
        institutionAdapter = new InstitutionAdapter(institutions);
        recyclerViewInstitutions.setAdapter(institutionAdapter);*/
        clientArrayList=new ArrayList<Institution>();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MainActivity.MYSHAREDPREF, Context.MODE_PRIVATE);
        stateName=sharedPreferences.getString("STATENAME","123");
        serviceName=sharedPreferences.getString("SERVICENAME","123");

        Log.d("stateName2222"," "+stateName);
        Log.d("serviceName2222"," "+serviceName);
        /*stateName=getArguments().getString("STATE_NAME");
        serviceName=getArguments().getString("SERVICE_NAME");

        Log.d("stateName"," "+stateName);
        Log.d("serviceName"," "+serviceName);*/



        getClientsList();
        /*Institution institution = new Institution("COA", "New Delhi");
        institutions.add(institution);
        institutions.add(new Institution("COA", "New Delhi"));
        institutions.add(new Institution("COA", "New Delhi"));
        institutions.add(new Institution("COA", "New Delhi"));
        institutions.add(new Institution("COA", "New Delhi"));*/
       /* Bundle arguments = getArguments();
        if(arguments==null)
        {

        }
        else {
            stateName = getArguments().getString("stateName");
            serviceName = getArguments().getString("serviceName");
        }*/
        return rootView;
    }

    private void getClientsList() {

        String  tag_string_req = "req_clients";

        StringRequest request=new StringRequest(Request.Method.GET, AppConfig.URL_ServiceBasedOnState+stateName+"&service="+serviceName, new Response.Listener<String>(){

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


                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        Institution institution = new Institution();

                        /*institution.setOrganizationId(jsonObject1.getInt("organizationId"));
                        institution.setOrganization_name(jsonObject1.getString("clientName"));
                        institution.setRelationship_type(jsonObject1.getString("relationship_type"));
                        institution.setOrgLogo(jsonObject1.getString("clientImage"));
                        institution.setOrgWal(jsonObject1.getString("clientLogo"));
                        institution.setOrgAddress(jsonObject1.getString("state"));
                        institution.setOrgDesc(jsonObject1.getString("orgDesc"));
*/

                        //institution.set(jsonObject1.getInt("id"));
                        //institution.(jsonObject1.getString("clientId"));
                       institution.setOrganization_name(jsonObject1.getString("clientName"));
                       /* institution.setClientCode(jsonObject1.getString("clientCode"));
                        institution.setClientContact(jsonObject1.getString("clientContact"));
                        institution.setClientEmail(jsonObject1.getString("clientEmail"));*/
                        institution.setOrgLogo(jsonObject1.getString("clientLogoPath"));
                        institution.setOrgWal(jsonObject1.getString("clientImagePath"));

                       /* institution.setClientLink(jsonObject1.getString("clientLink"));
                        institution.setClientAuthenticationType(jsonObject1.getString("clientAuthenticationType"));
                        institution.setPaymentMode(jsonObject1.getString("paymentMode"));
                        institution.setBid(jsonObject1.getString("bid"));
                        institution.setState(jsonObject1.getString("state"));*/
                        institution.setOrgAddress(jsonObject1.getString("state"));
                    /*    institution.setProductName(jsonObject1.getString("productName"));
                        institution.setService(jsonObject1.getString("service"));
                        institution.setSuccessUrl(jsonObject1.getString("successUrl"));
                        institution.setFailedUrl(jsonObject1.getString("failedUrl"));
                        institution.setClientLogoPath(jsonObject1.getString("clientLogoPath"));
                        institution.setLandingPage(jsonObject1.getString("landingPage"));*/

                        clientArrayList.add(institution);
                    }

                    institutionAdapter = new InstitutionAdapter(clientArrayList);
                    recyclerViewInstitutions.setAdapter(institutionAdapter);

                    Log.d("clientArrayList2222"," "+clientArrayList.get(0).getOrganization_name());


                }
                catch(JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage()==null ||error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme).create();

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




































     /*   String urlJsonObj = AppConfiguration.MAIN_URL + "/getAllClientDetails/0" ;
        urlJsonObj = urlJsonObj.trim().replace(" ", "%20");

        JsonArrayRequest request = new JsonArrayRequest(urlJsonObj, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0;i<response.length();i++){
                    try {


                      *//*  organizationId: 1,
                                organization_name: "SRS",
                                relationship_type: null,
                                orgLogo: null,
                                orgWal: null,
                                orgAddress: null,
                                orgDesc: null,
                                conPersonName: null,
                                conPersonMobNO: null,
                                conPersonEmail: null,
                                createdDate: 1511354799086
                    },*//*









                        JSONObject obj =response.getJSONObject(i);
                        Institution institution = new Institution();
                        institution.setOrganizationId(obj.getInt("organizationId"));
                        institution.setOrganization_name(obj.getString("organization_name"));
                        institution.setRelationship_type(obj.getString("relationship_type"));
                        institution.setOrgLogo(obj.getString("orgLogo"));
                        institution.setOrgWal(obj.getString("orgWal"));
                        institution.setOrgAddress(obj.getString("orgAddress"));
                        institution.setOrgDesc(obj.getString("orgDesc"));
                        institution.setConPersonName(obj.getString("conPersonName"));
//                        institution.setConPersonMobNO(obj.getLong("conPersonMobNO"));
                        institution.setConPersonEmail(obj.getString("conPersonEmail"));
                        institution.setCreatedDate(obj.getLong("createdDate"));

                        institutions.add(institution);
                        institutionAdapter.notifyDataSetChanged();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }*/
    }
}