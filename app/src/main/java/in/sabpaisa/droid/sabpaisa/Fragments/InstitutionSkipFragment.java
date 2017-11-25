package in.sabpaisa.droid.sabpaisa.Fragments;


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
import in.sabpaisa.droid.sabpaisa.Model.SkipClientData;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;


public class InstitutionSkipFragment extends Fragment {
    private static final String TAG = InstitutionSkipFragment.class.getSimpleName();
    View rootView;
    RecyclerView recyclerViewInstitutions;

    SkipMainClientsAdapter skipMainClientsAdapter;
    //InstitutionAdapter institutionAdapter;
    ArrayList<SkipClientData> institutions;

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
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),10));
        recyclerViewInstitutions.setLayoutManager(llm);
        institutions = new ArrayList<>();
        skipMainClientsAdapter =new SkipMainClientsAdapter(institutions);
        //institutionAdapter = new InstitutionAdapter(institutions);
        //recyclerViewInstitutions.setAdapter(institutionAdapter);
        recyclerViewInstitutions.setAdapter(skipMainClientsAdapter);

        getClientsList();
        /*SkipClientData institution = new SkipClientData("COA", "New Delhi");
        institutions.add(institution);
        institutions.add(new SkipClientData("COA", "New Delhi"));
        institutions.add(new SkipClientData("COA", "New Delhi"));
        institutions.add(new SkipClientData("COA", "New Delhi"));
        institutions.add(new SkipClientData("COA", "New Delhi"));*/

        return rootView;
    }

    private void getClientsList() {


        String tag_string_req = "req_register";



        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.url_clientsall, new Response.Listener<String>() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response2) {

                Log.d(TAG, "Register Response: " + response2.toString());
//                hideDialog();

                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    String status = jObj.getString("status");

                    //status =jObj.getString("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JSONArray    jarr1 = null;
                try {
                    jarr1 = jObj.getJSONArray("response");

                    String status =jObj.getString("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < jarr1.length(); i++) {
                    try {

                        JSONObject jsonObject = jarr1.getJSONObject(i);
                    //    JSONArray jarr = new JSONArray(response2);
                        //String status = jObj.getString("status");
                        //String response = jarr.getString("response1");
                        //boolean error = jObj.getBoolean("e623+rror");
                        //status = jObj.getString("status");


                        SkipClientData institution = new SkipClientData();

                        //institution.setOrganizationId(Integer.parseInt(jarr1.getString(Integer.parseInt("id"))));
                        institution.setOrganization_name(jsonObject.getString("clientName"));
                       // institution.setRelationship_type(jsonObject.getString("clientName"));
                        institution.setOrgLogo(jsonObject.getString("clientLogoPath"));
                        institution.setOrgAddress(jsonObject.getString("state"));
                        institution.setOrgWal(jsonObject.getString("clientImagePath"));


                        institutions.add(institution);
                        skipMainClientsAdapter.notifyDataSetChanged();
                        //institutionAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
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

}


