package in.sabpaisa.droid.sabpaisa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.sabpaisa.droid.sabpaisa.Adapter.NotificationAdapter;
import in.sabpaisa.droid.sabpaisa.Model.NotificationModelClass;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;

public class NotificationPopUPActivity extends AppCompatActivity {
    NotificationAdapter notificationAdapter;
    NotificationModelClass notificationModelClass;
    static  ArrayList<NotificationModelClass> notificationModelClassArrayList;
    LinearLayout linearLayoutnoDataFound;

    public static Map<String, NotificationModelClass> groupMap=new HashMap<>();
    public static Map<String, NotificationModelClass> feedMap=new HashMap<>();
    LinearLayoutManager llm;
    RecyclerView recyclerView;
    String clientid,token,timegroup,feedtime;
    TextView text;
    String posttime1,posttime,posttimeFeed,posttimeGroup;
    //int popup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_pop_up);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
       // text=(TextView)findViewById(R.id.text123);
        linearLayoutnoDataFound = (LinearLayout) findViewById(R.id.noDataFound);

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width =dm.widthPixels;
        int heigth= dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(heigth*.8));
        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.gravity= Gravity.CENTER;
        params.x=0;
        params.y=-20;
        getWindow().setAttributes(params);
        int   popup=1;
        Intent intent=getIntent();
        clientid=intent.getStringExtra("clientId");
        token=intent.getStringExtra("useraccesstoken");
      //  timegroup=intent.getStringExtra("grouptime");

      // feedtime=intent.getStringExtra("feedtime");

   //     groupMap = new HashMap<>();
   //     feedMap = new HashMap<>();
//        notificationModelClassArrayList=new ArrayList<NotificationModelClass>();

        SharedPreferences sharedPreferences113 = getApplication().getSharedPreferences(FullViewOfClientsProceed.MySharedPrefOnFullViewOfClientProceed, Context.MODE_PRIVATE);

        posttime = sharedPreferences113.getString("ts", "123");
        final String timefull=posttime;

        SharedPreferences sharedPreferences11 = getApplication().getSharedPreferences(Proceed_Feed_FullScreen.MySharedPrefProceedFeedFullScreen, Context.MODE_PRIVATE);

        posttimeFeed = sharedPreferences11.getString("ts", "123");
        feedtime=posttimeFeed;
        SharedPreferences sharedPreferences112 = getApplication().getSharedPreferences(Proceed_Group_FullScreen.MySharedPRoceedGroupFullScreen, Context.MODE_PRIVATE);
        posttimeGroup = sharedPreferences112.getString("Groupts", "123");
        timegroup=posttimeGroup;

        long tGroup = Long.parseLong(timegroup);
        long tFeeds = Long.parseLong(feedtime);
        Log.d("ArcPosttimeFeed",""+posttimeFeed);
        Log.d("ArcPosttimeGroup",""+posttimeGroup);
        Log.d("ArcPosttimeGroup11",""+timegroup);
        Log.d("ArcPosttimeGroup11",""+timefull);
        Log.d("popuactity",""+timegroup +" "+feedtime+ " "+token+" "+clientid);

     if(tGroup > tFeeds) {
         posttime1 = timegroup;
         NotificationCount(clientid, timegroup, token);
     }
     else {
         posttime1 = feedtime;
         NotificationCount(clientid, feedtime, token);
     }
    }

    public void NotificationCount(final String client_Id,final String postTime,final String token){
        StringRequest stringRequest=new StringRequest(Request.Method.GET, AppConfig.Base_Url + AppConfig.App_api + "notifications?client_Id=" + client_Id + "&postTime=" + postTime + "&token=" + token, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("NotifactionCount", "-->" + s);
                JSONObject object=null;
                String result;
                JSONArray feeds = null;
                JSONObject data =null ;
                JSONObject data1=null;
                JSONArray groups=null;
                String groupname,groupdescription,grouimage,countfeeds;
                int sum,sumgroup = 0,sumfeed = 0;
                String name,image,description,id,groupid,groupcount = null;


                try{
                    object=new JSONObject(s);
                    String response = object.getString("response");
                    String status = object.getString("status");
                    Log.d("CountResponse", "-->" + response);
                    Log.d("Countstatus", "-->" + status);

                    JSONObject object1 = new JSONObject(response);

     notificationModelClassArrayList  =new ArrayList<NotificationModelClass>();
                    if(!(object1.isNull("groups")) ) {

                           groups = object1.getJSONArray("groups");
                        Log.d("CountGroups", "-->" + groups);
                        for (int i = 0; i < groups.length(); i++) {
                            notificationModelClass=new NotificationModelClass();
                            data1 = groups.getJSONObject(i);
                            grouimage= data1.getString("imagePath");
                            groupid= data1.getString("id");
                            groupcount=   data1.getString("count");
                            sumgroup=sumgroup+Integer.parseInt(groupcount);
                            groupname=data1.getString("name");
                            groupdescription=data1.getString("description");
                            Log.d("countgroupsSum", "" + grouimage + "--" + groupdescription +" -----"+groupname+"---"+groupid);
                            notificationModelClass.setCount(data1.getString("count"));
                            notificationModelClass.setName(data1.getString("name"));
                            notificationModelClass.setIdentify("Group");
                            notificationModelClass.setId(data1.getString("id"));
                            notificationModelClass.setDescription(data1.getString("description"));
                            notificationModelClass.setImagePath(data1.getString("imagePath"));
                            notificationModelClassArrayList.add(notificationModelClass);
                            }

                        Log.d("CountresultGroup", "-->" + groups);
                    }
                 if(!(object1.isNull("feeds"/*"groups"*/))){
                           feeds = object1.getJSONArray("feeds");
                        Log.d("CountFeeds", "-->" + feeds);
                        for (int i = 0; i < feeds.length(); i++) {
                            notificationModelClass=new NotificationModelClass();

                            data = feeds.getJSONObject(i);
                            countfeeds = data.getString("count");
                            sumfeed=sumfeed+Integer.parseInt(countfeeds);
                            name = data.getString("name");
                            Log.d("countfeedsSum", "" +  countfeeds+"--" + name);
                            Log.d("Countdata", "" + data.getString("id") + "=" + data.getString("count"));
                            notificationModelClass.setCount(data.getString("count"));
                            notificationModelClass.setName(data.getString("name"));
                            notificationModelClass.setIdentify("Feed");
                            notificationModelClass.setDescription(data.getString("description"));
                            notificationModelClass.setId(data.getString("id"));

                            notificationModelClass.setImagePath(data.getString("imagePath"));

                            notificationModelClassArrayList.add(notificationModelClass);

                        }

                        Log.d("notificationMogrp",""+notificationModelClassArrayList.size());
                        Log.d("CountGroupselse", "-->" + notificationModelClassArrayList);

                    }

                    Log.d("notificationMogrp",""+notificationModelClassArrayList.size());
                    if(notificationModelClassArrayList.size()>0){
                        sum=sumfeed+sumgroup;
                        Log.d("SCOUNRHKD","=="+ sum+ "  "+sumfeed+"  "+sumgroup);
                        notificationAdapter = new NotificationAdapter(notificationModelClassArrayList,NotificationPopUPActivity.this);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(notificationAdapter);

                        notificationAdapter.notifyDataSetChanged();
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    }

                    else{
                        recyclerView.setVisibility(View.INVISIBLE);
                        text.setVisibility(View.VISIBLE);
                        linearLayoutnoDataFound.setVisibility(View.VISIBLE);

                    }

                }


                catch (Exception e)
                {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        Log.d("Strngrqst",""+stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
//Notification
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(NotificationPopUPActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }
}
