package in.sabpaisa.droid.sabpaisa.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.InstitutionAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.ProceedInstitutionFragmentOfflineAdapter;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments;
import in.sabpaisa.droid.sabpaisa.MainActivity;
import in.sabpaisa.droid.sabpaisa.MainFeedAdapter;
import in.sabpaisa.droid.sabpaisa.Model.ClientData;
import in.sabpaisa.droid.sabpaisa.Model.FeedDataForOffLine;
import in.sabpaisa.droid.sabpaisa.Model.ParticularClientModelForOffline;
import in.sabpaisa.droid.sabpaisa.PayFragments;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Model.Institution;
import in.sabpaisa.droid.sabpaisa.R;

import static android.content.Context.MODE_PRIVATE;


public class ProceedInstitiutionFragment extends Fragment {

    View rootView;
    LinearLayout linearLayoutnoDataFound;
    public  static  String MYSHAREDPREFProceed="mySharedPref11";
    RecyclerView recyclerViewInstitutions;
    InstitutionAdapter institutionAdapter;
    //ArrayList<Institution> institutions;
    ArrayList<Institution> clientArrayList ;

    ShimmerRecyclerView shimmerRecyclerView;
    String landing_page;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String stateName,serviceName,clientId,userImageUrl;

    /////////Local Db//////////
    AppDbComments db;
    ArrayList<ParticularClientModelForOffline> clientArrayListForOffline ;

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
        // ((MainActivity) getActivity()).initToolBar(clientId);
        linearLayoutnoDataFound = (LinearLayout)rootView.findViewById(R.id.noDataFound);
        shimmerRecyclerView=(ShimmerRecyclerView) rootView.findViewById(R.id.recycler_view_institutions);
        recyclerViewInstitutions = (RecyclerView) rootView.findViewById(R.id.recycler_view_institutions);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),10));
        recyclerViewInstitutions.setLayoutManager(llm);

        ///////////////////////DB/////////////////////////////////
        db = new AppDbComments(getContext());


        clientArrayList=new ArrayList<Institution>();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MainActivity.MYSHAREDPREF, MODE_PRIVATE);
        //stateName=sharedPreferences.getString("STATENAME","123");
        //serviceName=sharedPreferences.getString("SERVICENAME","123");
        clientId=sharedPreferences.getString("clientId","abc");
        userImageUrl=sharedPreferences.getString("userImageUrl","abc");
        Log.d("PIF_ClientId","-->"+clientId);
        Log.d("userImageUrlFrag","-->"+userImageUrl);
        recyclerViewInstitutions.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },2000);

        if (isOnline()) {
            db.deleteAllClientData();
            getClientsList(clientId.toString());

        }else {
            /////////////////////Retrive data from local DB///////////////////////////////

            Cursor res = db.getParticularClientData(clientId);
            clientArrayListForOffline = new ArrayList<>();
            if (res.getCount() > 0) {
                StringBuffer stringBuffer = new StringBuffer();
                while (res.moveToNext()) {
                    stringBuffer.append(res.getString(0) + " ");
                    stringBuffer.append(res.getString(1) + " ");
                    stringBuffer.append(res.getString(2) + " ");
                    stringBuffer.append(res.getString(3) + " ");
                    stringBuffer.append(res.getBlob(4) + " ");
                    stringBuffer.append(res.getBlob(5) + " ");


                    ParticularClientModelForOffline particularClientModelForOffline = new ParticularClientModelForOffline();
                    particularClientModelForOffline.setClientId(res.getString(1));
                    particularClientModelForOffline.setClientName(res.getString(2));
                    particularClientModelForOffline.setState(res.getString(3));
                    particularClientModelForOffline.setClientImagePath(res.getBlob(4));
                    particularClientModelForOffline.setClientLogoPath(res.getBlob(5));

                    clientArrayListForOffline.add(particularClientModelForOffline);

                }
                Log.d("getPIFOfflineData", "-->" + stringBuffer);

                ProceedInstitutionFragmentOfflineAdapter adapter = new ProceedInstitutionFragmentOfflineAdapter(getContext(),clientArrayListForOffline);
                recyclerViewInstitutions.setAdapter(adapter);



            } else {
                Log.d("PIFLocalDb", "In Else Part");
                Toast.makeText(getContext(), "No Data Found !", Toast.LENGTH_SHORT).show();
            }
        }


        return rootView;
    }

    private void getClientsList(final  String clientId) {

//        db.deleteAllClientData();

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
                    String status =jsonObject.getString("status");

                    if (status.equals("success")) {

                        String response = jsonObject.getString("response");

                        //Adding data to new jsonobject////////////////////////

                        JSONObject jsonObject1 = new JSONObject(response);

                        Institution institution = new Institution();

                        institution.setOrganizationId(jsonObject1.getString("clientId"));
                        Log.d("ClientIdjijiji", "-->" + institution.getOrganizationId());
                        institution.setOrganization_name(jsonObject1.getString("clientName"));
                        Log.d("Clientnamejijiji", "-->" + institution.getOrganization_name());

                        institution.setOrgLogo(jsonObject1.getString("clientLogoPath"));
                        Log.d("Clientimage", "-->" + institution.getOrgLogo());


                        institution.setOrgWal(jsonObject1.getString("clientImagePath"));

                        Log.d("ImageTest", "-->" + institution.getOrgLogo());
                        //Added on 1st Feb
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("lookupState");
                        institution.setOrgAddress(jsonObject2.getString("stateName"));
                        //Added on 1st Feb
                        institution.setOrgDesc(jsonObject1.getString("landingPage"));

                        Log.d("JSONobjectResp", "-->" + response);

                        Log.d("JSONobjectttt", "-->" + jsonObject);

                        clientArrayList.add(institution);
                        //////////////////////////////LOCAL DB//////////////////////////////////////

                        boolean isInserted = db.insertClientData(institution);
                        if (isInserted == true) {

                            //Toast.makeText(AllTransactionSummary.this, "Data  Inserted", Toast.LENGTH_SHORT).show();

                            Log.d("PFF", "LocalDBInIfPart" + isInserted);

                        } else {
                            Log.d("PFF", "LocalDBInElsePart" + isInserted);
                            //Toast.makeText(AllTransactionSummary.this, "Data  Not Inserted", Toast.LENGTH_SHORT).show();
                        }

                        institutionAdapter = new InstitutionAdapter(getContext(),clientArrayList);
                        recyclerViewInstitutions.setAdapter(institutionAdapter);

                        Log.d("clientArrayList2222", " " + clientArrayList.get(0).getOrganization_name());
                        Log.d("clientArrayList2222", " " + clientArrayList.get(0).getOrganizationId());
                        Log.d("clientArrayList3333", " " + clientArrayList.get(0).getOrgDesc());

                        landing_page = clientArrayList.get(0).getOrgDesc();

                        Log.d("xyz", "" + landing_page);

                        SharedPreferences.Editor editor1 = getContext().getSharedPreferences(MYSHAREDPREFProceed, MODE_PRIVATE).edit();
                        editor1.putString("landing_page", landing_page);
                        //editor.putString("SERVICENAME",serviceName);
                        editor1.commit();


                        PayFragments ldf = new PayFragments();
                        Bundle args = new Bundle();
                        args.putString("landing_page", "landing_page");
                        ldf.setArguments(args);


                    }else {
                        linearLayoutnoDataFound.setVisibility(View.VISIBLE);
                        shimmerRecyclerView.setVisibility(View.GONE);
                    }

                 /*   PayFragments ldf = new PayFragments();
                    Bundle args = new Bundle();
                    args.putString("landing_page", "landing_page");
                    ldf.setArguments(args);
*///Inflate the fragment
                    //getFragmentManager().beginTransaction().add(R.id.container, ldf).commit();

                   /* MakePa ldf = new YourNewFragment ();
                    Bundle args = new Bundle();
                    args.putString("landing_page", "landing_page");
                    ldf.setArguments(args);
*/
//
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


    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v("AllTransactionSummary", "Internet Connection Not Present");
            return false;
        }
    }


}


