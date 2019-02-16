package in.sabpaisa.droid.sabpaisa.Fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import in.sabpaisa.droid.sabpaisa.Adapter.SkipGroupFragmentAdapter;
import in.sabpaisa.droid.sabpaisa.AddGroup;
import in.sabpaisa.droid.sabpaisa.AddGroupSpace;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.GroupListData;
import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.MainGroupAdapter1;
import in.sabpaisa.droid.sabpaisa.Model.GroupDataForOffLine;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.SimpleDividerItemDecoration;
import in.sabpaisa.droid.sabpaisa.UIN;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.SkipClientDetailsScreen;

import static in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments.TABLE_NAME_GROUPS;
import static in.sabpaisa.droid.sabpaisa.AppDB.NotificationDB.TABLE_GROUPNOTIFICATION;
import static in.sabpaisa.droid.sabpaisa.ConstantsForUIUpdates.GROUP_ARRAYLIST;
import static in.sabpaisa.droid.sabpaisa.ConstantsForUIUpdates.REFRESH_GROUP_FRAGMENT;
import static in.sabpaisa.droid.sabpaisa.MainActivitySkip.SUPER_ADMIN_SHAREDFREF;

public class SkipGroupFragment extends Fragment {

    String appCid,userAcessToken;

    LinearLayout linearLayoutnoDataFound;
    ShimmerRecyclerView groupList;
    ArrayList<GroupListData> groupArrayList ;
    MaterialRippleLayout rippleClickAdd;

    FrameLayout framelayoutAddGroup;
    LinearLayout linearLayoutAddGrpWhenNoData;


    //Values get from SkipClient details screen
    String clientName,clientLogoPath,clientImagePath,state;

    SkipGroupFragmentAdapter skipGroupFragmentAdapter;

    BroadcastReceiver broadcastReceiver,receiver;

    String roleValue;

    public SkipGroupFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clientName = getArguments().getString("clientName");
        clientLogoPath = getArguments().getString("clientLogoPath");
        clientImagePath = getArguments().getString("clientImagePath");
        state = getArguments().getString("state");

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SkipClientDetailsScreen.MySharedPrefOnSkipClientDetailsScreenForAppCid, Context.MODE_PRIVATE);
        appCid=sharedPreferences.getString("appCid","abc");
        Log.d("clientId_MEMBERS",""+appCid);

        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        userAcessToken = sharedPreferences1.getString("response", "123");

        Log.d("userAcessToken", " " + userAcessToken);

        Log.d("SkipGroupFrag","Recieved_Val_"+clientName+" "+clientLogoPath+" "+clientImagePath+" "+state+" "+appCid);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_skip_group, container, false);

        linearLayoutnoDataFound = (LinearLayout) view.findViewById(R.id.noDataFound);
        groupList = (ShimmerRecyclerView) view.findViewById(R.id.groupList);

        rippleClickAdd = (MaterialRippleLayout) view.findViewById(R.id.rippleClickAdd);
        framelayoutAddGroup = (FrameLayout) view.findViewById(R.id.framelayoutAddGroup);

        linearLayoutAddGrpWhenNoData = (LinearLayout) view.findViewById(R.id.linearLayoutAddGrpWhenNoData);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        groupList.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        groupList.setLayoutManager(llm);
        groupList.setMotionEventSplittingEnabled(false);

        if (isOnline()) {
            callGroupDataList(userAcessToken, appCid);
        } else {
            //Todo offline
        }



        SharedPreferences sharedPreferencesRole = getContext().getSharedPreferences(SUPER_ADMIN_SHAREDFREF, Context.MODE_PRIVATE);

        roleValue = sharedPreferencesRole.getString("ROLE_VALUE", "abc");

        if (roleValue.equals("1")) {

            framelayoutAddGroup.setVisibility(View.VISIBLE);
        }

        rippleClickAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddGroupSpace.class);
                intent.putExtra("appCid",appCid);
                getActivity().startActivity(intent);
            }
        });

        linearLayoutAddGrpWhenNoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),AddGroupSpace.class);
                intent.putExtra("appCid",appCid);
                getActivity().startActivity(intent);
            }
        });


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(GROUP_ARRAYLIST)){
                    callGroupDataList(userAcessToken, appCid);
                }

            }
        };

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,new IntentFilter(GROUP_ARRAYLIST));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,new IntentFilter(REFRESH_GROUP_FRAGMENT));




        return view;
    }





    public void callGroupDataList(final String token, final String appCid) {

        String tag_string_req = "tag_string_req";

        String urlJsonObj = AppConfig.Base_Url + AppConfig.App_api + "memberStatusWithGroup" + "?token=" + token + "&appcid=" + appCid;

        Log.d("SkipGroupSpace","url___"+urlJsonObj);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                urlJsonObj, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    groupArrayList = new ArrayList<GroupListData>();

                    JSONObject jsonObject = new JSONObject(response);

                    Log.d("SkipGroupFrag", "PGD_RESP: " + response);

                    String status = jsonObject.getString("status");

                    String response1 = jsonObject.getString("response");

                    JSONArray jsonArray = null;
                    Object obj = jsonObject.get("response");
                    if (obj instanceof JSONArray) {
                        jsonArray = (JSONArray) obj;

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObjectX = jsonArray.getJSONObject(i);
                            JSONObject jsonObject1 = jsonObjectX.getJSONObject("clientGroup");
                            final GroupListData groupListData = new GroupListData();
                            groupListData.setClientId(jsonObject1.getString("clientId"));
                            groupListData.setGroupId(jsonObject1.getString("groupId"));
                            groupListData.setGroupName(jsonObject1.getString("groupName"));
                            groupListData.setGroupText(jsonObject1.getString("groupText"));
                            groupListData.setCreatedDate(jsonObject1.getString("createdDate"));
                            //groupListData.setImagePath(jsonObject1.getString("imagePath"));
                            groupListData.setLogoPath(jsonObject1.getString("logoPath"));
                            groupListData.setMemberStatus(jsonObjectX.getString("memberStatus"));
                            groupListData.setMemberGroupRole(jsonObjectX.getString("memberGroupRole"));

                            Log.d("ProceedGroupFragmGR"," "+jsonObjectX.getString("memberGroupRole"));

                            if (groupListData.getMemberStatus().contains("Approved")) {

                                groupArrayList.add(groupListData);

                            }

                        }
                        Log.d("groupArrayList1212", " " + groupArrayList.get(0).getGroupName());


                        /*START listener for sending data to activity*/
                        /*OnFragmentInteractionListener listener = (OnFragmentInteractionListener) getActivity();
                        listener.onFragmentSetGroups(arrayListForApproved);*/
                        /*END listener for sending data to activity*/


                        skipGroupFragmentAdapter = new SkipGroupFragmentAdapter(groupArrayList, getContext());
                        groupList.setAdapter(skipGroupFragmentAdapter);

                    } else {


                        if (roleValue.equals("1")) {

                            linearLayoutAddGrpWhenNoData.setVisibility(View.VISIBLE);
                            framelayoutAddGroup.setVisibility(View.GONE);
                            groupList.setVisibility(View.GONE);
                            linearLayoutnoDataFound.setVisibility(View.GONE);

                        }else {
                            linearLayoutnoDataFound.setVisibility(View.VISIBLE);
                            framelayoutAddGroup.setVisibility(View.GONE);
                            groupList.setVisibility(View.GONE);
                            linearLayoutAddGrpWhenNoData.setVisibility(View.GONE);
                        }

                    }
                }
                // Try and catch are included to handle any errors due to JSON
                catch (JSONException e) {
                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();
                    callGroupDataList(token, appCid);
                }

            }

        },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        callGroupDataList(token, appCid);
                        Log.e("Group fragments", "Group fragments Error");
                    }
                }
        );
        // Adds the JSON arra   y request "arrayreq" to the request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);
    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v("SGF", "Internet Connection Not Present");
            return false;
        }
    }




}
