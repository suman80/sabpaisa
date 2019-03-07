package in.sabpaisa.droid.sabpaisa.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import in.sabpaisa.droid.sabpaisa.Adapter.InstitutionAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.OtherSpaceAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.SkipMainClientsAdapter;
import in.sabpaisa.droid.sabpaisa.AddSpaceActivity;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.AppDB.NotificationDB;
import in.sabpaisa.droid.sabpaisa.FeedData;
import in.sabpaisa.droid.sabpaisa.GroupListData;
import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.Model.Institution;
import in.sabpaisa.droid.sabpaisa.Model.PersonalSpaceModel;
import in.sabpaisa.droid.sabpaisa.Model.SkipClientData;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;

import static in.sabpaisa.droid.sabpaisa.AppDB.NotificationDB.TABLE_FEEDNOTIFICATION;
import static in.sabpaisa.droid.sabpaisa.AppDB.NotificationDB.TABLE_GROUPNOTIFICATION;


public class InstitutionSkipFragment extends Fragment {
    private static final String TAG = InstitutionSkipFragment.class.getSimpleName();
    View rootView;
    LinearLayout linearLayoutnoDataFound;
    RecyclerView recyclerViewInstitutions;

    SkipMainClientsAdapter skipMainClientsAdapter;
    //InstitutionAdapter institutionAdapter;
    ArrayList<PersonalSpaceModel> institutions;
    ArrayList<PersonalSpaceModel> institutions1;
    /*START Interface for getting data from activity*/
    GetDataInterface sGetDataInterface;

    //FrameLayout framelayoutAddFeed;

    LinearLayout linearLayoutAddFeedWhenNoData;

    String userAccessToken;

    RecyclerView recyclerViewOtherSpace;

    OtherSpaceAdapter OtherSpaceAdapter;

    RelativeLayout relativeLayoutOther;

    NotificationDB notificationDB;

    String appCid;

    public InstitutionSkipFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notificationDB = new NotificationDB(getContext());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_institution_skip, container, false);
        recyclerViewInstitutions = (RecyclerView) rootView.findViewById(R.id.recycler_view_institutions);
        recyclerViewOtherSpace = (RecyclerView) rootView.findViewById(R.id.recyclerViewOtherSpace);

        //framelayoutAddFeed = (FrameLayout) rootView.findViewById(R.id.framelayoutAddFeed);
        linearLayoutnoDataFound = (LinearLayout) rootView.findViewById(R.id.noDataFound);
        linearLayoutAddFeedWhenNoData = (LinearLayout) rootView.findViewById(R.id.linearLayoutAddFeedWhenNoData);


        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),10));
        recyclerViewInstitutions.setLayoutManager(llm);

        recyclerViewInstitutions.getRecycledViewPool().setMaxRecycledViews(0, 0);

        //Other recyclerview

        LinearLayoutManager llm1 = new LinearLayoutManager(getContext());
        llm1.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),10));
        recyclerViewOtherSpace.setLayoutManager(llm1);

        recyclerViewOtherSpace.getRecycledViewPool().setMaxRecycledViews(0, 0);

        relativeLayoutOther = (RelativeLayout)rootView.findViewById(R.id.relativeLayoutOther);



        Log.d("sGetDataInterface", "" + sGetDataInterface);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        userAccessToken = sharedPreferences.getString("response", "123");

        if (isOnline()) {

            getClientsList();
            getOtherSpce();




        }else {
            Toast.makeText(getContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
        }

        linearLayoutAddFeedWhenNoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddSpaceActivity.class);
                startActivity(intent);
            }
        });


        return rootView;
    }

    private void getClientsList() {


        String tag_string_req = "req_register";

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_spAppClient + "?admin=" + userAccessToken;

        Log.d("ISF", "url___" + url);

        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    institutions = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d("ISF__", "response___" + response);

                    String status = jsonObject.getString("status");

                    String response1 = jsonObject.getString("response");

                    Log.d("ISF__", "status__" + status);
                    Log.d("ISF__", "response1__" + response1);

                    if (status.equals("success") && response1.equals("No Record Found")) {

                        //Toast.makeText(getContext(),"No Result Found",Toast.LENGTH_SHORT).show();

                        //linearLayoutnoDataFound.setVisibility(View.VISIBLE);
                        recyclerViewInstitutions.setVisibility(View.GONE);
                        linearLayoutAddFeedWhenNoData.setVisibility(View.VISIBLE);
                    } else {
                        JSONArray jsonArray = jsonObject.getJSONArray("response");

                        JsonParser parser = new JsonParser();

                        Gson gson = new Gson();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            //PersonalSpaceModel personalSpaceModel = new PersonalSpaceModel();


                            JsonElement mJson =  parser.parse(jsonObject1.toString());

                            PersonalSpaceModel object = gson.fromJson(mJson, PersonalSpaceModel.class);

                            institutions.add(object);

                        }

                       /* *//*START listener for sending data to activity*//*
                        OnFragmentInteractionListener listener = (OnFragmentInteractionListener) getActivity();
                        listener.onFragmentSetClients(institutions);
                        *//*END listener for sending data to activity*/
                        //loadGroupListView(groupArrayList, (RecyclerView) rootView.findViewById(R.id.recycler_view_group));
                        skipMainClientsAdapter = new SkipMainClientsAdapter(institutions,getContext());
                        recyclerViewInstitutions.setAdapter(skipMainClientsAdapter);
                    }
                }
                // Try and catch are included to handle any errors due to JSON
                catch (JSONException e) {
                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();

                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }



    private void getOtherSpce() {


        String tag_string_req = "req_register";

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_spAppClients + "?token=" + userAccessToken;

        Log.d("ISF_Other", "url___" + url);

        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    institutions1 = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d("ISF_Other", "response___" + response);

                    String status = jsonObject.getString("status");

                    String response1 = jsonObject.getString("response");

                    Log.d("ISF_Other", "status__" + status);
                    Log.d("ISF_Other", "response1__" + response1);

                    if (status.equals("failure") && response1.equals("No Record Found")) {

                        Toast.makeText(getContext(),"No Result Found",Toast.LENGTH_SHORT).show();

                        //linearLayoutnoDataFound.setVisibility(View.VISIBLE);

                        relativeLayoutOther.setVisibility(View.GONE);

                    } else {
                        JSONArray jsonArray = jsonObject.getJSONArray("response");

                        JsonParser parser = new JsonParser();

                        Gson gson = new Gson();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            //PersonalSpaceModel personalSpaceModel = new PersonalSpaceModel();


                            JsonElement mJson =  parser.parse(jsonObject1.toString());

                            PersonalSpaceModel object = gson.fromJson(mJson, PersonalSpaceModel.class);

                            appCid = object.getAppCid();

                            institutions1.add(object);



                            if (notificationDB.isTableExists(TABLE_FEEDNOTIFICATION)) {

                                Cursor res = notificationDB.getSpaceNotificationFeed(object.getAppCid());
                                if (res.getCount() > 0) {
                                    StringBuffer stringBuffer = new StringBuffer();

                                    while (res.moveToNext()) {
                                        stringBuffer.append(res.getString(0) + " ");
                                        stringBuffer.append(res.getString(1) + " ");
                                        stringBuffer.append(res.getString(2) + " ");
                                        stringBuffer.append(res.getString(3) + " ");
                                        //feedData.setFeedRecentCommentTime(Long.parseLong(res.getString(3)));
                                        stringBuffer.append(res.getString(4) + " ");
                                        stringBuffer.append(res.getString(5) + " ");
                                        stringBuffer.append(res.getString(6) + " ");
                                        stringBuffer.append(res.getString(7) + " ");
                                        stringBuffer.append(res.getString(8) + " ");
                                        stringBuffer.append(res.getString(9) + " ");
                                    }

                                    Log.d("ISF_Notification", "stringBufferFEED___ " + stringBuffer);
                                    //Log.d("PGF_Notification", "grpListDataVal____ " + groupListData.getGroupRecentCommentTime());

                                }

                            }




                            if (notificationDB.isTableExists(TABLE_GROUPNOTIFICATION)) {

                                Cursor res = notificationDB.getSpaceNotificationGroup(object.getAppCid());
                                if (res.getCount() > 0) {
                                    StringBuffer stringBuffer = new StringBuffer();

                                    while (res.moveToNext()) {
                                        stringBuffer.append(res.getString(0) + " ");
                                        stringBuffer.append(res.getString(1) + " ");
                                        stringBuffer.append(res.getString(2) + " ");
                                        stringBuffer.append(res.getString(3) + " ");
                                        //feedData.setFeedRecentCommentTime(Long.parseLong(res.getString(3)));
                                        stringBuffer.append(res.getString(4) + " ");
                                        stringBuffer.append(res.getString(5) + " ");
                                        stringBuffer.append(res.getString(6) + " ");
                                        stringBuffer.append(res.getString(7) + " ");

                                    }

                                    Log.d("ISF_Notification", "stringBufferGRP___ " + stringBuffer);
                                    //Log.d("PGF_Notification", "grpListDataVal____ " + groupListData.getGroupRecentCommentTime());

                                }

                            }




                        }

                        /*START listener for sending data to activity*/
                        OnFragmentInteractionListener listener = (OnFragmentInteractionListener) getActivity();
                        listener.onFragmentSetClients(institutions1);
                        /*END listener for sending data to activity*/

                        // Comparing timings with notification db

                        if (notificationDB.isTableExists(TABLE_FEEDNOTIFICATION) && notificationDB.isTableExists(TABLE_GROUPNOTIFICATION)){
                            Cursor resF,resG;
                            resF = notificationDB.getMaxSpaceNotificationTime_Feed(appCid);

                            resG = notificationDB.getMaxSpaceNotificationTime_Group(appCid);

                            if (resF.getCount()>0 || resG.getCount()>0){

                                long time_F = Long.parseLong(resF.getString(3));
                                long time_G = Long.parseLong(resG.getString(3));

                                //Collections.sort();


                            }



                        }

                        OtherSpaceAdapter = new OtherSpaceAdapter(institutions1,getContext());
                        recyclerViewOtherSpace.setAdapter(OtherSpaceAdapter);
                    }
                }
                // Try and catch are included to handle any errors due to JSON
                catch (JSONException e) {
                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();

                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }






    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            sGetDataInterface = (GetDataInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "must implement GetDataInterface Interface");
        }
    }

    public void getDataFromActivity() {
        if (sGetDataInterface != null) {
            this.institutions1 = sGetDataInterface.getClientDataList();
            OtherSpaceAdapter.setItems(this.institutions1);
            OtherSpaceAdapter.notifyDataSetChanged();
        }

        Log.d("Institution_I&A", " " + sGetDataInterface + "&" + institutions);
    }
    /*END Interface for getting data from activity*/

    public interface GetDataInterface {
        ArrayList<PersonalSpaceModel> getClientDataList();
    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v("MainActivitySkip", "Internet Connection Not Present");
            return false;
        }
    }


}


