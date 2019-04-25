package in.sabpaisa.droid.sabpaisa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.balysv.materialripple.MaterialRippleLayout;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.miguelcatalan.materialsearchview.SearchAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import in.sabpaisa.droid.sabpaisa.AppDB.NotificationDB;
import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;

import static in.sabpaisa.droid.sabpaisa.AppDB.NotificationDB.TABLE_FEEDNOTIFICATION;
import static in.sabpaisa.droid.sabpaisa.ConstantsForUIUpdates.REFRESH_FEED_FRAGMENT;
import static in.sabpaisa.droid.sabpaisa.ConstantsForUIUpdates.REFRESH_GROUP_FRAGMENT;

public class PrivateGroupFeeds extends AppCompatActivity {

    public static String GroupId;
    ArrayList<FeedData> feedArrayList /*= new ArrayList<FeedData>()*/;
    MainFeedAdapter mainFeedAdapter;/*Globally Declared Adapter*/

    Toolbar toolbar;

    FrameLayout framelayoutAddPrivateFeed;
    MaterialRippleLayout rippleClickAdd;

    public static String roleValue,memberGroupRole;

    String clientId;
    public static String FLAG /*= "PrivateGroupFeeds"*/;

    BroadcastReceiver broadcastReceiver;

    static boolean active = false;

    NotificationDB notificationDB;

    String userAccessToken;

    MaterialSearchView searchView;

    TextView noResultTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_group_feeds);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        noResultTextView = (TextView)findViewById(R.id.noResultTextView);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Private Feeds");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FLAG = null;
                active = false;
                MainGroupAdapter1.isClicked=false;

                onBackPressed();

            }
        });

        Log.d("Before : ",FLAG+"");
        FLAG = "PrivateGroupFeeds";
        Log.d("After : ",FLAG);
        framelayoutAddPrivateFeed = (FrameLayout)findViewById(R.id.framelayoutAddPrivateFeed);
        rippleClickAdd = (MaterialRippleLayout) findViewById(R.id.rippleClickAdd);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        Log.d("framePrivateFeed", framelayoutAddPrivateFeed+"");


        SharedPreferences sharedPreferences1 = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        userAccessToken = sharedPreferences1.getString("response", "123");


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(FullViewOfClientsProceed.MySharedPrefOnFullViewOfClientProceed, Context.MODE_PRIVATE);
        clientId = sharedPreferences.getString("clientId", "abc");
        Log.d("clientId_PFF", "" + clientId);

        SharedPreferences sharedPreferencesRole = getApplicationContext().getSharedPreferences(UIN.SHARED_PREF_FOR_CHECK_USER, Context.MODE_PRIVATE);

        roleValue = sharedPreferencesRole.getString("USER_ROLE", "abc");

        memberGroupRole = getIntent().getStringExtra("memberGroupRole");


        GroupId=getIntent().getStringExtra("GroupId");
        if (GroupId != null)
        Log.d("PrivateGroupFeedsId",GroupId);



        if (roleValue.equals("1") || memberGroupRole.equals("2")) {

            framelayoutAddPrivateFeed.setVisibility(View.VISIBLE);
        }


        rippleClickAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("AddPrivateFeed","Clicked");
                Intent intent = new Intent(PrivateGroupFeeds.this,AddFeed.class);
                intent.putExtra("FLAG",FLAG);
                intent.putExtra("GROUP_ID",GroupId);
                intent.putExtra("CLIENT_ID",clientId);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        //Notification db
        notificationDB= new NotificationDB(PrivateGroupFeeds.this);

        active = true;


        if (isOnline()) {


            callFeedDataList(GroupId, userAccessToken);




            searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
                @Override
                public void onSearchViewShown() {

                }

                @Override
                public void onSearchViewClosed() {

                    //if closed then listview will return default

                    if (!(feedArrayList == null || feedArrayList.isEmpty())) {
                        loadFeedListView(feedArrayList, (RecyclerView) findViewById(R.id.recycler_view_feeds));
                    } else {
//                    Toast.makeText(ClientList.this,"No Data Found !",Toast.LENGTH_SHORT).show();

                        Log.d("Search", "No Data");

                    }

                }
            });

            searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    if (newText != null && !newText.isEmpty()) {

                        ArrayList<FeedData> newArraylist = new ArrayList<>();

                        for (FeedData items : feedArrayList) {

                            if (items.feedName.contains(newText.toLowerCase()) || items.feedText.contains(newText.toLowerCase())) {
                                newArraylist.add(items);
                            }

                        }

                        if (newArraylist != null && !newArraylist.isEmpty()) {

                            loadFeedListView(newArraylist, (RecyclerView) findViewById(R.id.recycler_view_feeds));
                            noResultTextView.setVisibility(View.GONE);
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_feeds);
                            recyclerView.setVisibility(View.VISIBLE);

                        } else {
                            Log.d("SearchInPGF_____", "No data found");
                            loadFeedListView(newArraylist, (RecyclerView) findViewById(R.id.recycler_view_feeds));
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_feeds);
                            recyclerView.setVisibility(View.GONE);
                            noResultTextView.setVisibility(View.VISIBLE);
                            Log.d("SearchInPGFNewText_____", " "+newText);
                            noResultTextView.setText("No Result For :"+newText);
                        }


                    } else {
                        // if search text is null then return default
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_feeds);
                        recyclerView.setVisibility(View.VISIBLE);
                        noResultTextView.setVisibility(View.GONE);
                        loadFeedListView(feedArrayList, recyclerView);
                    }

                    return true;
                }
            });

        }else {

            Toast.makeText(PrivateGroupFeeds.this,"No Internet Connection !",Toast.LENGTH_SHORT).show();
        }



    }




    @Override
    protected void onResume() {
            super.onResume();
            Log.d("onResume", "onResumeCalled");
            active = true;

            // Update UI
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String feedId = intent.getStringExtra("FEED_ID");

                    Log.d("PFF_FEED", "broadcastVal__" + feedId);

                    //API

                    if (active == true) {

                        callFeedDataList(GroupId, userAccessToken);
                    }

                }
            };

            LocalBroadcastManager.getInstance(PrivateGroupFeeds.this).registerReceiver(broadcastReceiver, new IntentFilter(ConstantsForUIUpdates.IS_FEED_FRAG_OPEN));




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
                    //feedArrayList = new ArrayList<FeedData>();
                    Log.d( "Private_Feeds: " , response);
                    //swipeRefreshLayout.setRefreshing(false);
                    feedArrayList = new ArrayList<FeedData>();

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    String response1 = jsonObject.getString("response");

                    if (status.equals("success")&&response1.equals("No Record Found")) {

                        framelayoutAddPrivateFeed.setVisibility(View.GONE);

                        if (roleValue.equals("1") || memberGroupRole.equals("2")){
                            android.app.AlertDialog.Builder builder =new android.app.AlertDialog.Builder(PrivateGroupFeeds.this);
                            builder.setTitle("");
                            builder.setMessage("No feed generated for this group till now. \n Press add button to add feed");
                            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    /*onBackPressed();*/
                                    Intent intent = new Intent(PrivateGroupFeeds.this,AddFeed.class);
                                    intent.putExtra("FLAG",FLAG);
                                    intent.putExtra("GROUP_ID",GroupId);
                                    intent.putExtra("CLIENT_ID",clientId);
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

                            android.app.AlertDialog.Builder builder =new android.app.AlertDialog.Builder(PrivateGroupFeeds.this);
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

        mainFeedAdapter = new MainFeedAdapter(arrayList,PrivateGroupFeeds.this);
        recyclerView.setAdapter(mainFeedAdapter);
        /*recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(mainFeedAdapter);

            }
        },1000);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplication(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(( getApplicationContext()), FeedDetails.class);
                       *//* intent.putExtra("FeedId", feedArrayList.get(position).getFeedId());
                        intent.putExtra("FeedName", feedArrayList.get(position).getFeedName());
                        intent.putExtra("FeedDeatils", feedArrayList.get(position).getFeedText());
                        startActivity(intent);*//*
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );*/
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        recyclerView.setLayoutManager(llm);
    }


    @Override
    public void onBackPressed() {

        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        }else {

            super.onBackPressed();
            FLAG = null;
            MainGroupAdapter1.isClicked = false;
            active = false;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);


        return true;
    }




    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v("PFF", "Internet Connection Not Present");
            return false;
        }
    }





}
