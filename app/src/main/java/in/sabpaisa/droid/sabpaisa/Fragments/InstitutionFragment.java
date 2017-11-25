package in.sabpaisa.droid.sabpaisa.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.InstitutionAdapter;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.Util.AppConfiguration;
import in.sabpaisa.droid.sabpaisa.Model.Institution;
import in.sabpaisa.droid.sabpaisa.R;

/**
 * Created by abc on 14-06-2017.
 */

public class InstitutionFragment extends Fragment {

    View rootView;
    RecyclerView recyclerViewInstitutions;
    InstitutionAdapter institutionAdapter;
    ArrayList<Institution> institutions;



    String stateName,serviceName;

    public InstitutionFragment() {

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
        institutions = new ArrayList<>();
        institutionAdapter = new InstitutionAdapter(institutions);
        recyclerViewInstitutions.setAdapter(institutionAdapter);

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
        Bundle arguments = getArguments();
        if(arguments==null)
        {

        }
        else {
            stateName = getArguments().getString("stateName");
            serviceName = getArguments().getString("serviceName");
        }
        return rootView;
    }

    private void getClientsList() {
        String urlJsonObj = AppConfiguration.MAIN_URL + "/getAllClientDetails/0" ;
        urlJsonObj = urlJsonObj.trim().replace(" ", "%20");

        JsonArrayRequest request = new JsonArrayRequest(urlJsonObj, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0;i<response.length();i++){
                    try {


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
    }
}
