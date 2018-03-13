package in.sabpaisa.droid.sabpaisa.Fragments;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.InstitutionAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.SkipMainClientsAdapter;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.GroupListData;
import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.Model.Institution;
import in.sabpaisa.droid.sabpaisa.Model.SkipClientData;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;


public class InstitutionSkipFragment extends Fragment {
    private static final String TAG = InstitutionSkipFragment.class.getSimpleName();
    View rootView;
    LinearLayout linearLayoutnoDataFound;
    RecyclerView recyclerViewInstitutions;

    SkipMainClientsAdapter skipMainClientsAdapter;
    //InstitutionAdapter institutionAdapter;
    ArrayList<SkipClientData> institutions;
    /*START Interface for getting data from activity*/
    GetDataInterface sGetDataInterface;

    public InstitutionSkipFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_institution_skip, container, false);
        recyclerViewInstitutions = (RecyclerView) rootView.findViewById(R.id.recycler_view_institutions);
        linearLayoutnoDataFound = (LinearLayout)rootView.findViewById(R.id.noDataFound);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),10));
        recyclerViewInstitutions.setLayoutManager(llm);

        recyclerViewInstitutions.getRecycledViewPool().setMaxRecycledViews(0, 0);


        //institutionAdapter = new InstitutionAdapter(institutions);
        //recyclerViewInstitutions.setAdapter(institutionAdapter);
        recyclerViewInstitutions.postDelayed(new Runnable() {
            @Override
            public void run() {


            }
        },1000);
        Log.d("sGetDataInterface",""+sGetDataInterface);
        getClientsList();

        return rootView;
    }

    private void getClientsList() {


        String tag_string_req = "req_register";



        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.Base_Url+AppConfig.App_api+AppConfig.url_clientsall, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    institutions = new ArrayList<SkipClientData>();
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    String response1 = jsonObject.getString("response");

                    if (status.equals("success")&&response1.equals("No_Record_Found")) {

                        //Toast.makeText(getContext(),"No Result Found",Toast.LENGTH_SHORT).show();

                        linearLayoutnoDataFound.setVisibility(View.VISIBLE);
                        recyclerViewInstitutions.setVisibility(View.GONE);
                    }
                    else {
                        JSONArray jsonArray = jsonObject.getJSONArray("response");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            SkipClientData institution = new SkipClientData();

                            institution.setOrganizationId(jsonObject1.getString("clientId"));
                            institution.setOrganization_name(jsonObject1.getString("clientName"));
                            institution.setOrgLogo(jsonObject1.getString("clientLogoPath"));
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("lookupState");
                            institution.setOrgAddress(jsonObject2.getString("stateName"));

                            institution.setOrgWal(jsonObject1.getString("clientImagePath"));
                            Log.d("institutionskip1","121");
                            institutions.add(institution);
                        }

                       /*START listener for sending data to activity*/
                        OnFragmentInteractionListener listener = (OnFragmentInteractionListener) getActivity();
                        listener.onFragmentSetClients(institutions);
                            /*END listener for sending data to activity*/
                        //loadGroupListView(groupArrayList, (RecyclerView) rootView.findViewById(R.id.recycler_view_group));
                        skipMainClientsAdapter =new SkipMainClientsAdapter(institutions);
                        recyclerViewInstitutions.setAdapter(skipMainClientsAdapter);
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            sGetDataInterface= (GetDataInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "must implement GetDataInterface Interface");
        }
    }

    public void getDataFromActivity() {
        if(sGetDataInterface != null){
            this.institutions = sGetDataInterface.getClientDataList();
            skipMainClientsAdapter.setItems(this.institutions);
            skipMainClientsAdapter.notifyDataSetChanged();
        }

        Log.d("Institution_I&A"," "+sGetDataInterface+"&"+institutions);
    }
    /*END Interface for getting data from activity*/

    public interface GetDataInterface {
        ArrayList<SkipClientData> getClientDataList();
    }





}


