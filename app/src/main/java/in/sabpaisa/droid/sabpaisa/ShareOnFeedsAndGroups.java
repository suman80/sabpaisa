package in.sabpaisa.droid.sabpaisa;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.FeedAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.GroupAdapter;
import in.sabpaisa.droid.sabpaisa.Interfaces.FlagCallback;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;

public class ShareOnFeedsAndGroups extends AppCompatActivity implements FlagCallback {

    Bitmap bitmap;
    ImageView imageView;
    String recievedText,appCid;
    Uri recievedUri;

    ArrayList<FeedData> feedDataArrayList;
    ArrayList<GroupListData> groupListDataArrayList;

    RecyclerView feedRecyclerView,groupRecyclerView;

    FeedAdapter feedAdapter;
    GroupAdapter groupAdapter;

    String userAccessToken;

    Toolbar toolbar;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_on_feeds_and_groups);

        //imageView = (ImageView)findViewById(R.id.img);

        recievedText = getIntent().getStringExtra("recievedText");
        appCid = getIntent().getStringExtra("appCid");
        recievedUri = Uri.parse(getIntent().getStringExtra("imageUri"));

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), recievedUri);
            //imageView.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbarSOFAG);
        toolbar.setTitle("Share: Feeds/Groups");

        feedRecyclerView = (RecyclerView)findViewById(R.id.feedRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        feedRecyclerView.setLayoutManager(mLayoutManager);
        feedRecyclerView.setItemAnimator(new DefaultItemAnimator());

        groupRecyclerView = (RecyclerView)findViewById(R.id.groupRecyclerView);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        groupRecyclerView.setLayoutManager(mLayoutManager1);
        groupRecyclerView.setItemAnimator(new DefaultItemAnimator());

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        userAccessToken = sharedPreferences.getString("response", "123");


        getFeed_Group_List(appCid,userAccessToken);



    }




    private void getFeed_Group_List(String appCid,String userAccessToken) {


        String tag_string_req = "req_register";

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_spappclientgroupandfeeds +"?appcid="+appCid+"&usertoken="+userAccessToken;

        Log.d("SOFAG", "url___" + url);

        JsonObjectRequest req = new JsonObjectRequest(/*Request.Method.GET,*/url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("SOFAG__","resp___"+response);

                        try {
                            String status = response.getString("status");

                            if (status.equals("success")){

                                feedDataArrayList = new ArrayList<>();
                                groupListDataArrayList = new ArrayList<>();
                                JSONObject res = response.getJSONObject("response");

                                Log.d("SOFAG__","respInter___"+res.toString());

                                if (!res.isNull("feeds")) {

                                    JSONArray jsonArray = res.optJSONArray("feeds");

                                    Log.d("SOFAG__","jsonArraySizeFEED_____"+jsonArray.length());

                                    for (int i = 0; i<jsonArray.length();i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        final FeedData feedData = new FeedData();
                                        feedData.setClientId(jsonObject.getString("clientId"));
                                        feedData.setFeedId(jsonObject.getString("feedId"));
                                        feedData.setFeedName(jsonObject.getString("feedName"));
                                        feedData.setFeedText(jsonObject.getString("feedText"));
                                        feedData.setCreatedDate(jsonObject.getString("createdDate"));
                                        feedData.setLogoPath(jsonObject.getString("logoPath"));
                                        //feedData.setImagePath(jsonObject1.getString("imagePath"));

                                        feedDataArrayList.add(feedData);

                                    }

                                    //todo
                                    feedAdapter = new FeedAdapter(feedDataArrayList,ShareOnFeedsAndGroups.this);
                                    feedRecyclerView.setAdapter(feedAdapter);


                                }


                                if (!res.isNull("groups")){
                                    JSONArray jsonArray = res.optJSONArray("groups");

                                    Log.d("SOFAG__","jsonArraySizeGRP_____"+jsonArray.length());

                                    for (int i = 0; i<jsonArray.length();i++){

                                        final GroupListData groupListData = new GroupListData();

                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        groupListData.setClientId(jsonObject.getString("clientId"));
                                        groupListData.setGroupId(jsonObject.getString("groupId"));
                                        Log.d("TTTTTTTTTTTTT","groupId___"+groupListData.getGroupId());
                                        groupListData.setGroupName(jsonObject.getString("groupName"));
                                        groupListData.setGroupText(jsonObject.getString("groupText"));
                                        groupListData.setCreatedDate(jsonObject.getString("createdDate"));

                                        groupListData.setLogoPath(jsonObject.getString("logoPath"));

                                        groupListDataArrayList.add(groupListData);
                                    }

                                    groupAdapter = new GroupAdapter(groupListDataArrayList,ShareOnFeedsAndGroups.this);
                                    groupRecyclerView.setAdapter(groupAdapter);

                                }




                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("","Error: "+error.getMessage());

            }
        });
        AppController.getInstance().addToRequestQueue(req, tag_string_req);
    }


    @Override
    public void onSharedFragmentSetFeeds(ArrayList<String> feedData) {
        Log.d("onSharedFragmentSetFeed", " " + feedData);

    }

    @Override
    public void onSharedFragmentSetGroups(ArrayList<String> groupData) {
        Log.d("onSharedFragmentSetGrp", " " + groupData);
    }
}
