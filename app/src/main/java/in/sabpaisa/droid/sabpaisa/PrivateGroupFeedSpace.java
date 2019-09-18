package in.sabpaisa.droid.sabpaisa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.balysv.materialripple.MaterialRippleLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import in.sabpaisa.droid.sabpaisa.AppDB.NotificationDB;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.SkipClientDetailsScreen;

import static in.sabpaisa.droid.sabpaisa.AppDB.NotificationDB.TABLE_FEEDNOTIFICATION;
import static in.sabpaisa.droid.sabpaisa.MainActivitySkip.SUPER_ADMIN_SHAREDFREF;

public class PrivateGroupFeedSpace extends AppCompatActivity {

    public static String GroupId;
    ArrayList<FeedData> feedArrayList = new ArrayList<FeedData>();
    Toolbar toolbar;

    FrameLayout framelayoutAddPrivateFeed;
    MaterialRippleLayout rippleClickAdd;

    public static String roleValue,memberGroupRole;

    String appCid;
    public static String FLAG ;

    BroadcastReceiver broadcastReceiver;

    static boolean active = false;

    NotificationDB notificationDB;

    String userAccessToken;

    PrivateGroupFeedSpaceAdapter privateGroupFeedSpaceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_group_feed_space);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FLAG = null;
                active = false;

                onBackPressed();

            }
        });


        framelayoutAddPrivateFeed = (FrameLayout)findViewById(R.id.framelayoutAddPrivateFeed);
        rippleClickAdd = (MaterialRippleLayout) findViewById(R.id.rippleClickAdd);

        SharedPreferences sharedPreferences1 = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        userAccessToken = sharedPreferences1.getString("response", "123");


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SkipClientDetailsScreen.MySharedPrefOnSkipClientDetailsScreenForAppCid, Context.MODE_PRIVATE);
        appCid=sharedPreferences.getString("appCid","abc");
        Log.d("PGFS",""+appCid);

        SharedPreferences sharedPreferencesRole = getApplicationContext().getSharedPreferences(SUPER_ADMIN_SHAREDFREF, Context.MODE_PRIVATE);

        roleValue = sharedPreferencesRole.getString("ROLE_VALUE", "abc");

        GroupId=getIntent().getStringExtra("GroupId");

        if (roleValue.equals("1") || memberGroupRole.equals("2")) {

            framelayoutAddPrivateFeed.setVisibility(View.VISIBLE);
        }

        FLAG = "PrivateGroupFeedSpace";

        rippleClickAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("AddPrivateFeed","Clicked");
                Intent intent = new Intent(PrivateGroupFeedSpace.this,AddSpaceFeed.class);
                intent.putExtra("FLAG",FLAG);
                intent.putExtra("GROUP_ID",GroupId);
                intent.putExtra("appCid",appCid);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        //Notification db
        notificationDB= new NotificationDB(PrivateGroupFeedSpace.this);



        callFeedDataList(GroupId,userAccessToken);


    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume","onResumeCalled");
        active = true;

        // Update UI
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String feedId = intent.getStringExtra("FEED_ID");

                Log.d("PFF_FEED","broadcastVal__"+feedId);

                //API

                if (active == true) {

                    callFeedDataList(GroupId,userAccessToken);
                }

            }
        };

        LocalBroadcastManager.getInstance(PrivateGroupFeedSpace.this).registerReceiver(broadcastReceiver,new IntentFilter(ConstantsForUIUpdates.IS_FEED_FRAG_OPEN));

    }



    public void callFeedDataList(final String groupId , final  String userAccessToken) {
        String urlJsonObj = AppConfig.Base_Url+AppConfig.App_api+"privatefeed"+"?groupId="+groupId;

        if(!roleValue.equals("1")){
            urlJsonObj += "&userToken="+userAccessToken;
        }

        Log.d("PrivateFeed_URL","__"+urlJsonObj);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                urlJsonObj, new Response.Listener<String>(){

            // Takes the response from the JSON request
            @Override
            public void onResponse(String response) {
                try {

                    Log.d( "Private_Feeds: " , response);
                    //swipeRefreshLayout.setRefreshing(false);
                    feedArrayList = new ArrayList<FeedData>();

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    String response1 = jsonObject.getString("response");

                    if (status.equals("success")&&response1.equals("No Record Found")) {

                        framelayoutAddPrivateFeed.setVisibility(View.GONE);

                        if (roleValue.equals("1") || memberGroupRole.equals("2")){
                            android.app.AlertDialog.Builder builder =new android.app.AlertDialog.Builder(PrivateGroupFeedSpace.this);
                            builder.setTitle("");
                            builder.setMessage("No feed generated for this group till now. \n Press add button to add feed");
                            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    /*onBackPressed();*/
                                    Intent intent = new Intent(PrivateGroupFeedSpace.this,AddSpaceFeed.class);
                                    intent.putExtra("FLAG",FLAG);
                                    intent.putExtra("GROUP_ID",GroupId);
                                    intent.putExtra("appCid",appCid);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);


                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    onBackPressed();
                                }
                            });

                            android.app.AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }else {

                            android.app.AlertDialog.Builder builder =new android.app.AlertDialog.Builder(PrivateGroupFeedSpace.this);
                            builder.setTitle("");
                            builder.setMessage("No feed generated for this group till now.");
                            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    onBackPressed();


                                }
                            });


                            android.app.AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        }



                        //Toast.makeText(getApplicationContext(),"No Result Found",Toast.LENGTH_SHORT).show();

                    }else {
                        JSONArray jsonArray = jsonObject.getJSONArray("response");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            FeedData feedData = new FeedData();
                            feedData.setClientId(jsonObject1.getString("clientId"));
                            feedData.setFeedId(jsonObject1.getString("feedId"));
                            feedData.setFeedName(jsonObject1.getString("feedName"));
                            feedData.setFeedText(jsonObject1.getString("feedText"));
                            feedData.setCreatedDate(jsonObject1.getString("createdDate"));
                            feedData.setImagePath(jsonObject1.getString("imagePath"));
                            feedData.setLogoPath(jsonObject1.getString("logoPath"));
                            feedArrayList.add(feedData);


                            //////////////Notification db//////////////////////////

                            if (notificationDB.isTableExists(TABLE_FEEDNOTIFICATION)) {

                                Cursor res = notificationDB.getParticularFeedNotificationData(feedData.getFeedId());
                                if (res.getCount() > 0) {
                                    StringBuffer stringBuffer = new StringBuffer();

                                    while (res.moveToNext()) {
                                        stringBuffer.append(res.getString(0) + " ");
                                        stringBuffer.append(res.getString(1) + " ");
                                        stringBuffer.append(res.getString(2) + " ");
                                        stringBuffer.append(res.getString(3) + " ");
                                        feedData.setFeedRecentCommentTime(Long.parseLong(res.getString(3)));
                                        stringBuffer.append(res.getString(4) + " ");
                                    }

                                    Log.d("PFF_Notification", "stringBuffer___ " + stringBuffer);
                                    //Log.d("PGF_Notification", "grpListDataVal____ " + groupListData.getGroupRecentCommentTime());

                                }

                            }

                            Log.d("PFF_Notification", "feedListDataVal____ " + feedData.getFeedRecentCommentTime());






                        }
                        Log.d("feedArrayListAfterParse", " " + feedArrayList.get(0).getFeedName());
                        Log.d("feedClintId", " " + feedArrayList.get(0).getClientId());
                        Log.d("feedClintId", " " + feedArrayList.get(0).getFeedId());
                        /*START listener for sending data to activity*/
                /*        OnFragmentInteractionListener listener = (OnFragmentInteractionListener) getActivity();
                        listener.onFragmentSetFeeds(feedArrayList);*/
                        /*END listener for sending data to activity*/




                        Collections.sort(feedArrayList, new Comparator<FeedData>() {
                            @Override
                            public int compare(FeedData feedData, FeedData t1) {

                                if (feedData.getFeedRecentCommentTime() > t1.getFeedRecentCommentTime()){
                                    return -1;
                                }
                                else if (feedData.getFeedRecentCommentTime() < t1.getFeedRecentCommentTime()){
                                    return 1;
                                }
                                else return 0;
                            }
                        });





                        loadFeedListView(feedArrayList, (RecyclerView) findViewById(R.id.recycler_view_feeds));
                    }

                }
                // Try and catch are included to handle any errors due to JSON
                catch (JSONException e) {
                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();
                    callFeedDataList(groupId,userAccessToken);
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
                        callFeedDataList(groupId,userAccessToken);
                        Log.e("Feed", "FeedError");
                    }
                }
        );
        // Adds the JSON array request "arrayreq" to the request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    private void loadFeedListView(ArrayList<FeedData> arrayList, final RecyclerView recyclerView) {

        privateGroupFeedSpaceAdapter = new PrivateGroupFeedSpaceAdapter(arrayList,PrivateGroupFeedSpace.this);
        recyclerView.setAdapter(privateGroupFeedSpaceAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        recyclerView.setLayoutManager(llm);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FLAG = null;

        active = false;

    }



}
