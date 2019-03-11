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
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import in.sabpaisa.droid.sabpaisa.Adapter.ProceedGroupsFragmentsOfflineAdapter;
import in.sabpaisa.droid.sabpaisa.AddGroup;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments;
import in.sabpaisa.droid.sabpaisa.AppDB.NotificationDB;
import in.sabpaisa.droid.sabpaisa.ConstantsForUIUpdates;
import in.sabpaisa.droid.sabpaisa.GroupListData;
import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.MainGroupAdapter1;
import in.sabpaisa.droid.sabpaisa.Model.GroupDataForOffLine;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.SimpleDividerItemDecoration;
import in.sabpaisa.droid.sabpaisa.UIN;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;

import static in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments.TABLE_NAME_GROUPS;
import static in.sabpaisa.droid.sabpaisa.AppDB.NotificationDB.TABLE_GROUPNOTIFICATION;
import static in.sabpaisa.droid.sabpaisa.ConstantsForUIUpdates.FEED_ARRAYLIST;
import static in.sabpaisa.droid.sabpaisa.ConstantsForUIUpdates.GROUP_ARRAYLIST;
import static in.sabpaisa.droid.sabpaisa.ConstantsForUIUpdates.REFRESH_GROUP_FRAGMENT;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProceedGroupsFragments extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    View rootView = null;
    private static final String TAG = ProceedGroupsFragments.class.getSimpleName();

    public static String clientId;
    LinearLayout linearLayoutnoDataFound;
    ShimmerRecyclerView groupList;

    SharedPreferences sharedPreferences1;

    String token;

    String tag_string_req = "req_register";
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<GroupListData> groupArrayList ;
    ArrayList<GroupDataForOffLine> groupArrayListForOffline;
    MainGroupAdapter1 mainGroupAdapter1;

    /////////Local Db//////////
    AppDbComments db;
    /*Globally Declared Adapter*/
    /*START Interface for getting data from activity*/
    GetDataInterface sGetDataInterface;
    /*START Interface for getting data from activity*/

    public ProceedGroupsFragments() {
        // Required empty public constructor
    }

    MaterialRippleLayout rippleClickAdd;

    FrameLayout framelayoutAddGroup;
    LinearLayout linearLayoutAddGrpWhenNoData;
    String roleValue;

    ArrayList<GroupListData> arrayListForApproved ;

    NotificationDB notificationDB;

    BroadcastReceiver broadcastReceiver,receiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragments_groups, container, false);
        linearLayoutnoDataFound = (LinearLayout) rootView.findViewById(R.id.noDataFound);
        groupList = (ShimmerRecyclerView) rootView.findViewById(R.id.groupList);

        rippleClickAdd = (MaterialRippleLayout) rootView.findViewById(R.id.rippleClickAdd);
        framelayoutAddGroup = (FrameLayout) rootView.findViewById(R.id.framelayoutAddGroup);

        linearLayoutAddGrpWhenNoData = (LinearLayout) rootView.findViewById(R.id.linearLayoutAddGrpWhenNoData);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        groupList.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        groupList.setLayoutManager(llm);
        groupList.setMotionEventSplittingEnabled(false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FullViewOfClientsProceed.MySharedPrefOnFullViewOfClientProceed, Context.MODE_PRIVATE);
        clientId = sharedPreferences.getString("clientId", "abc");
        Log.d("clientId_PGF", "" + clientId);

        // Inflate the layout for this fragment


        //  swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        //  swipeRefreshLayout.setOnRefreshListener(this);

        sharedPreferences1 = getActivity().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        token = sharedPreferences1.getString("response", "123");

        Log.d("TokenInPGF", " " + token);


        ///////////////////////DB/////////////////////////////////
        db = new AppDbComments(getContext());

        if (isOnline()) {
            callGroupDataList(token, clientId);
        } else {

            /////////////////////Retrive data from local DB///////////////////////////////

            Cursor res = db.getParticularGroupData(clientId);
            groupArrayListForOffline = new ArrayList<>();
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
                    stringBuffer.append(res.getString(7) + " ");
                    stringBuffer.append(res.getString(8) + " ");

                    GroupDataForOffLine groupDataForOffLine = new GroupDataForOffLine();
                    groupDataForOffLine.setClientId(res.getString(1));
                    groupDataForOffLine.setGroupId(res.getString(2));
                    groupDataForOffLine.setGroupName(res.getString(3));
                    groupDataForOffLine.setGroupText(res.getString(4));
                    groupDataForOffLine.setMemberStatus(res.getString(5));
                    groupDataForOffLine.setGroupImage(res.getString(7));
                    groupDataForOffLine.setGroupLogo(res.getString(8));
                    groupArrayListForOffline.add(groupDataForOffLine);

                }
                Log.d("getGroupData", "-->" + stringBuffer);

                ProceedGroupsFragmentsOfflineAdapter adapter = new ProceedGroupsFragmentsOfflineAdapter(groupArrayListForOffline, getContext());
                groupList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                Log.d("PGFLocalDb", "In Else Part");
                Toast.makeText(getContext(), "No Data Found !", Toast.LENGTH_SHORT).show();
            }


        }

        SharedPreferences sharedPreferencesRole = getContext().getSharedPreferences(UIN.SHARED_PREF_FOR_CHECK_USER, Context.MODE_PRIVATE);

        roleValue = sharedPreferencesRole.getString("USER_ROLE", "abc");

        if (roleValue.equals("1")) {

            framelayoutAddGroup.setVisibility(View.VISIBLE);
        }


        rippleClickAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),AddGroup.class);
                intent.putExtra("CLIENT_ID",clientId);
                getActivity().startActivity(intent);
            }
        });

        linearLayoutAddGrpWhenNoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),AddGroup.class);
                intent.putExtra("CLIENT_ID",clientId);
                getActivity().startActivity(intent);
            }
        });


        //Notification db
        notificationDB= new NotificationDB(getContext());


         receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(GROUP_ARRAYLIST)){
                    callGroupDataList1(token, clientId,context);
                }

                if (intent.getAction().equals(REFRESH_GROUP_FRAGMENT)){
                    callGroupDataList1(token, clientId,context);
                }


            }
        };

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,new IntentFilter(GROUP_ARRAYLIST));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,new IntentFilter(REFRESH_GROUP_FRAGMENT));




        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String groupId = intent.getStringExtra("GROUP_ID");

                Log.d("BROADCAST_PGF","broadcastVal__"+groupId);

                if (intent.getAction().equals(ConstantsForUIUpdates.IS_GROUP_FRAG_OPEN) && FullViewOfClientsProceed.isFragmentOpen) {
                    groupArrayList.clear();
                    arrayListForApproved.clear();
                    callGroupDataList1(token, clientId,context);

                }


            }

        };

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver,new IntentFilter(ConstantsForUIUpdates.IS_GROUP_FRAG_OPEN));


    }

    public void callGroupDataList(final String token, final String clientId) {


        boolean checkDb = db.isTableExists(TABLE_NAME_GROUPS);

        Log.d("DbValuePGF"," "+checkDb);

        if (checkDb == true){
            db.deleteAllGroupData();
        }


            String urlJsonObj = AppConfig.Base_Url + AppConfig.App_api + "memberStatusWithGroup" + "?token=" + token + "&clientId=" + clientId;

        Log.d("PGF_url","____"+urlJsonObj);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                urlJsonObj, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    groupArrayList = new ArrayList<GroupListData>();
                    arrayListForApproved = new ArrayList<GroupListData>();
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d(TAG, "PGD_RESP: " + response);

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

                            groupArrayList.add(groupListData);


                            /////////////////////Saving To Internal Storage/////////////////////////////////////////


                            final GroupDataForOffLine groupDataForOffLine = new GroupDataForOffLine();
                            groupDataForOffLine.setClientId(jsonObject1.getString("clientId"));
                            groupDataForOffLine.setGroupId(jsonObject1.getString("groupId"));
                            groupDataForOffLine.setGroupName(jsonObject1.getString("groupName"));
                            groupDataForOffLine.setGroupText(jsonObject1.getString("groupText"));
                            groupDataForOffLine.setMemberStatus(jsonObjectX.getString("memberStatus"));


                            Glide.with(getContext())
                                    .load(groupListData.getLogoPath())
                                    .asBitmap()
                                    .into(new SimpleTarget<Bitmap>(100, 100) {
                                        @Override
                                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                            Log.d("LogoBitmap", " " + resource);
                                            //saveLogoToInternalStorage(resource,groupListData.getGroupId());

                                            ContextWrapper cw = new ContextWrapper(getContext());
                                            // path to /data/data/yourapp/app_data/imageDir
                                            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                                            // Create imageDir
                                            File mypath = new File(directory, groupDataForOffLine.getGroupId() + "groupLogo.jpg");

                                            Log.d("mypath", "mypath  " + mypath);

                                            String logoPath = mypath.toString();

                                            FileOutputStream fos = null;
                                            try {
                                                fos = new FileOutputStream(mypath);
                                                // Use the compress method on the BitMap object to write image to the OutputStream
                                                resource.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            } finally {
                                                try {
                                                    fos.close();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            groupDataForOffLine.setGroupLogo(logoPath);

                                        }
                                    });

                            // Commenting on 29th Nov 2018 for banner image
/*

                            Glide.with(getContext())
                                    .load(groupListData.getImagePath())
                                    .asBitmap()
                                    .into(new SimpleTarget<Bitmap>(100, 100) {
                                        @Override
                                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                            Log.d("ImgBitmap", " " + resource);
                                            //saveImageToInternalStorage(resource,groupListData.getGroupId());

                                            ContextWrapper cw = new ContextWrapper(getContext());
                                            // path to /data/data/yourapp/app_data/imageDir
                                            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                                            // Create imageDir
                                            File mypath = new File(directory, groupDataForOffLine.getGroupId() + "groupImage.jpg");

                                            Log.d("mypathImg", "mypathImg  " + mypath);

                                            String imagePath = mypath.toString();

                                            FileOutputStream fos = null;
                                            try {
                                                fos = new FileOutputStream(mypath);
                                                // Use the compress method on the BitMap object to write image to the OutputStream
                                                resource.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            } finally {
                                                try {
                                                    fos.close();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            groupDataForOffLine.setGroupImage(imagePath);

                                        }
                                    });

*/

                            //////////////////////////////LOCAL DB//////////////////////////////////////

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Do something after 3000ms

                                    Log.d("logoPath_PGF", "IntoLocalDb " + groupDataForOffLine.getGroupLogo());
                                    //Log.d("imagePath_PGF", "IntoLocalDb " + groupDataForOffLine.getGroupImage());

                                    if (groupDataForOffLine.getMemberStatus().equals("Approved")) {

                                        boolean isInserted = db.insertGroupData(groupDataForOffLine, token);

                                        if (isInserted == true) {

                                            //Toast.makeText(AllTransactionSummary.this, "Data  Inserted", Toast.LENGTH_SHORT).show();

                                            Log.d("PGF", "LocalDBInIfPart" + isInserted);

                                        } else {
                                            Log.d("PGF", "LocalDBInElsePart" + isInserted);
                                            //Toast.makeText(AllTransactionSummary.this, "Data  Not Inserted", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }
                            }, 2000);



                            //////////////Notification db//////////////////////////

                            if (notificationDB.isTableExists(TABLE_GROUPNOTIFICATION)) {

                                Cursor res = notificationDB.getParticularGroupNotificationData(groupListData.getGroupId());
                                if (res.getCount() > 0) {
                                    StringBuffer stringBuffer = new StringBuffer();

                                    while (res.moveToNext()) {
                                        stringBuffer.append(res.getString(0) + " ");
                                        stringBuffer.append(res.getString(1) + " ");
                                        stringBuffer.append(res.getString(2) + " ");
                                        stringBuffer.append(res.getString(3) + " ");
                                        groupListData.setGroupRecentCommentTime(Long.parseLong(res.getString(3)));
                                        stringBuffer.append(res.getString(4) + " ");
                                    }

                                    Log.d("PGF_Notification", "stringBuffer___ " + stringBuffer);
                                    //Log.d("PGF_Notification", "grpListDataVal____ " + groupListData.getGroupRecentCommentTime());

                                }

                            }

                            Log.d("PGF_Notification", "grpListDataVal____ " + groupListData.getGroupRecentCommentTime());


                            }
                        Log.d("groupArrayList1212", " " + groupArrayList.get(0).getGroupName());


                        for (GroupListData approvedValue:groupArrayList) {
                            if (approvedValue.getMemberStatus().contains("Approved")){

                                arrayListForApproved.add(approvedValue);

                            }
                        }



                        if(!(arrayListForApproved == null || arrayListForApproved.isEmpty())){
                        Collections.sort(arrayListForApproved, new Comparator<GroupListData>() {
                            @Override
                            public int compare(GroupListData groupListData, GroupListData t1) {

                                if (groupListData.getGroupRecentCommentTime() > t1.getGroupRecentCommentTime()){
                                    return -1;
                                }
                                else if (groupListData.getGroupRecentCommentTime() < t1.getGroupRecentCommentTime()){
                                    return 1;
                                }
                                else return 0;
                            }
                        });

                        /*for (GroupListData approvedValue:groupArrayList) {
                            if (approvedValue.getMemberStatus().contains("Pending")){

                                arrayListForApproved.add(approvedValue);

                            }
                        }

                        for (GroupListData approvedValue:groupArrayList) {
                            if (approvedValue.getMemberStatus().contains("Blocked")){

                                arrayListForApproved.add(approvedValue);

                            }
                        }

                        for (GroupListData approvedValue:groupArrayList) {
                            if (approvedValue.getMemberStatus().contains("Not joined")){

                                arrayListForApproved.add(approvedValue);

                            }
                        }*/


                        /*START listener for sending data to activity*/
                        OnFragmentInteractionListener listener = (OnFragmentInteractionListener) getActivity();
                        listener.onFragmentSetGroups(arrayListForApproved);
                        /*END listener for sending data to activity*/

                        //mainGroupAdapter1 = new MainGroupAdapter1(groupArrayList, getContext());
                        mainGroupAdapter1 = new MainGroupAdapter1(arrayListForApproved, getContext());
                        groupList.setAdapter(mainGroupAdapter1);

                        }

                        else if (roleValue.equals("1")) {

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
                    callGroupDataList(token, clientId);
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
                        callGroupDataList(token, clientId);
                        Log.e("Group fragments", "Group fragments Error");
                    }
                }
        );
        // Adds the JSON arra   y request "arrayreq" to the request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);
    }




    public void callGroupDataList1(final String token, final String clientId , final Context context) {


        boolean checkDb = db.isTableExists(TABLE_NAME_GROUPS);

        Log.d("DbValuePGF"," "+checkDb);

        if (checkDb == true){
            db.deleteAllGroupData();
        }


        String urlJsonObj = AppConfig.Base_Url + AppConfig.App_api + "memberStatusWithGroup" + "?token=" + token + "&clientId=" + clientId;
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                urlJsonObj, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    groupArrayList = new ArrayList<GroupListData>();
                    arrayListForApproved = new ArrayList<GroupListData>();
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d(TAG, "PGD_RESP: " + response);

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

                            groupArrayList.add(groupListData);


                            /////////////////////Saving To Internal Storage/////////////////////////////////////////


                            final GroupDataForOffLine groupDataForOffLine = new GroupDataForOffLine();
                            groupDataForOffLine.setClientId(jsonObject1.getString("clientId"));
                            groupDataForOffLine.setGroupId(jsonObject1.getString("groupId"));
                            groupDataForOffLine.setGroupName(jsonObject1.getString("groupName"));
                            groupDataForOffLine.setGroupText(jsonObject1.getString("groupText"));
                            groupDataForOffLine.setMemberStatus(jsonObjectX.getString("memberStatus"));


                            Glide.with(context)
                                    .load(groupListData.getLogoPath())
                                    .asBitmap()
                                    .into(new SimpleTarget<Bitmap>(100, 100) {
                                        @Override
                                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                            Log.d("LogoBitmap", " " + resource);
                                            //saveLogoToInternalStorage(resource,groupListData.getGroupId());

                                            ContextWrapper cw = new ContextWrapper(context);
                                            // path to /data/data/yourapp/app_data/imageDir
                                            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                                            // Create imageDir
                                            File mypath = new File(directory, groupDataForOffLine.getGroupId() + "groupLogo.jpg");

                                            Log.d("mypath", "mypath  " + mypath);

                                            String logoPath = mypath.toString();

                                            FileOutputStream fos = null;
                                            try {
                                                fos = new FileOutputStream(mypath);
                                                // Use the compress method on the BitMap object to write image to the OutputStream
                                                resource.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            } finally {
                                                try {
                                                    fos.close();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            groupDataForOffLine.setGroupLogo(logoPath);

                                        }
                                    });


                            // Commenting on 29th Nov 2018 for banner image
      /*
                            Glide.with(context)
                                    .load(groupListData.getImagePath())
                                    .asBitmap()
                                    .into(new SimpleTarget<Bitmap>(100, 100) {
                                        @Override
                                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                            Log.d("ImgBitmap", " " + resource);
                                            //saveImageToInternalStorage(resource,groupListData.getGroupId());

                                            ContextWrapper cw = new ContextWrapper(context);
                                            // path to /data/data/yourapp/app_data/imageDir
                                            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                                            // Create imageDir
                                            File mypath = new File(directory, groupDataForOffLine.getGroupId() + "groupImage.jpg");

                                            Log.d("mypathImg", "mypathImg  " + mypath);

                                            String imagePath = mypath.toString();

                                            FileOutputStream fos = null;
                                            try {
                                                fos = new FileOutputStream(mypath);
                                                // Use the compress method on the BitMap object to write image to the OutputStream
                                                resource.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            } finally {
                                                try {
                                                    fos.close();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            groupDataForOffLine.setGroupImage(imagePath);

                                        }
                                    });

*/
                            //////////////////////////////LOCAL DB//////////////////////////////////////

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Do something after 3000ms

                                    Log.d("logoPath_PGF", "IntoLocalDb " + groupDataForOffLine.getGroupLogo());
                                    //Log.d("imagePath_PGF", "IntoLocalDb " + groupDataForOffLine.getGroupImage());

                                    if (groupDataForOffLine.getMemberStatus().equals("Approved")){

                                    boolean isInserted = db.insertGroupData(groupDataForOffLine, token);

                                    if (isInserted == true) {

                                        //Toast.makeText(AllTransactionSummary.this, "Data  Inserted", Toast.LENGTH_SHORT).show();

                                        Log.d("PGF", "LocalDBInIfPart" + isInserted);

                                    } else {
                                        Log.d("PGF", "LocalDBInElsePart" + isInserted);
                                        //Toast.makeText(AllTransactionSummary.this, "Data  Not Inserted", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                }
                            }, 2000);



                            //////////////Notification db//////////////////////////

                            if (notificationDB.isTableExists(TABLE_GROUPNOTIFICATION)) {

                                Cursor res = notificationDB.getParticularGroupNotificationData(groupListData.getGroupId());
                                if (res.getCount() > 0) {
                                    StringBuffer stringBuffer = new StringBuffer();

                                    while (res.moveToNext()) {
                                        stringBuffer.append(res.getString(0) + " ");
                                        stringBuffer.append(res.getString(1) + " ");
                                        stringBuffer.append(res.getString(2) + " ");
                                        stringBuffer.append(res.getString(3) + " ");
                                        groupListData.setGroupRecentCommentTime(Long.parseLong(res.getString(3)));
                                        stringBuffer.append(res.getString(4) + " ");
                                    }

                                    Log.d("PGF_Notification", "stringBuffer___ " + stringBuffer);
                                    //Log.d("PGF_Notification", "grpListDataVal____ " + groupListData.getGroupRecentCommentTime());

                                }

                            }

                            Log.d("PGF_Notification", "grpListDataVal____ " + groupListData.getGroupRecentCommentTime());


                        }
                        Log.d("groupArrayList1212", " " + groupArrayList.get(0).getGroupName());


                        for (GroupListData approvedValue:groupArrayList) {
                            if (approvedValue.getMemberStatus().contains("Approved")){

                                arrayListForApproved.add(approvedValue);

                            }
                        }

                        Collections.sort(arrayListForApproved, new Comparator<GroupListData>() {
                            @Override
                            public int compare(GroupListData groupListData, GroupListData t1) {

                                if (groupListData.getGroupRecentCommentTime() > t1.getGroupRecentCommentTime()){
                                    return -1;
                                }
                                else if (groupListData.getGroupRecentCommentTime() < t1.getGroupRecentCommentTime()){
                                    return 1;
                                }
                                else return 0;
                            }
                        });


                        /*for (GroupListData approvedValue:groupArrayList) {
                            if (approvedValue.getMemberStatus().contains("Pending")){

                                arrayListForApproved.add(approvedValue);

                            }
                        }

                        for (GroupListData approvedValue:groupArrayList) {
                            if (approvedValue.getMemberStatus().contains("Blocked")){

                                arrayListForApproved.add(approvedValue);

                            }
                        }

                        for (GroupListData approvedValue:groupArrayList) {
                            if (approvedValue.getMemberStatus().contains("Not joined")){

                                arrayListForApproved.add(approvedValue);

                            }
                        }
*/

                        /*START listener for sending data to activity*/
                        /*OnFragmentInteractionListener listener = (OnFragmentInteractionListener) getActivity();
                        listener.onFragmentSetGroups(arrayListForApproved);*/
                        /*END listener for sending data to activity*/

                        //mainGroupAdapter1 = new MainGroupAdapter1(groupArrayList, getContext());
                        mainGroupAdapter1 = new MainGroupAdapter1(arrayListForApproved, context);
                        groupList.setAdapter(mainGroupAdapter1);

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
                    callGroupDataList(token, clientId);
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
                        callGroupDataList(token, clientId);
                        Log.e("Group fragments", "Group fragments Error");
                    }
                }
        );
        // Adds the JSON arra   y request "arrayreq" to the request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);
    }


    @Override
    public void onDestroy() {

        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);

        super.onDestroy();
    }

    /*START onRefresh() for SwipeRefreshLayout*/
    @Override
    public void onRefresh() {
        callGroupDataList(token, clientId);
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
            this.arrayListForApproved = sGetDataInterface.getGroupDataList();
            mainGroupAdapter1.setItems(this.arrayListForApproved);
            mainGroupAdapter1.notifyDataSetChanged();
        }

        Log.d("PGF_I&A", " " + sGetDataInterface + "&" + groupArrayList);
    }


    public interface GetDataInterface {
        ArrayList<GroupListData> getGroupDataList();
    }
    /*END onRefresh() for SwipeRefreshLayout*/


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v("PGF", "Internet Connection Not Present");
            return false;
        }
    }


}