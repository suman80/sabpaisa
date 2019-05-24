package in.sabpaisa.droid.sabpaisa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sabpaisa.droid.sabpaisa.Adapter.FeedAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.GroupAdapter;
import in.sabpaisa.droid.sabpaisa.Interfaces.FlagCallback;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;
import in.sabpaisa.droid.sabpaisa.Util.VolleyMultipartRequest;

import static in.sabpaisa.droid.sabpaisa.MainActivitySkip.AppDecideFlag;

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

    ArrayList<String> feedDataForShare;
    ArrayList<String> groupDataForShare;
    MenuItem item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_on_feeds_and_groups);

        //imageView = (ImageView)findViewById(R.id.img);

        recievedText = getIntent().getStringExtra("recievedText");
        appCid = getIntent().getStringExtra("appCid");

        //recievedUri = Uri.parse(getIntent().getStringExtra("imageUri"));
        String image = getIntent().getStringExtra("imageUri");

        if (image != null){
            recievedUri = Uri.parse(image);
        }


        if (recievedUri != null) {

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), recievedUri);
                //imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        toolbar = (Toolbar) findViewById(R.id.toolbarSOFAG);
        toolbar.setTitle("Share: Feeds/Groups");
        toolbar.setNavigationIcon(R.drawable.previousmoresmall);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

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


        feedDataForShare = new ArrayList<>();
        groupDataForShare = new ArrayList<>();

        if (isOnline()) {

            getFeed_Group_List(appCid, userAccessToken);

        }else {
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
        }


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

        feedDataForShare = feedData;
        if (feedDataForShare.size() + groupDataForShare.size() > 3 ){
            item.setVisible(false);
            Toast.makeText(ShareOnFeedsAndGroups.this,"Sharing limit exceeds ! You can share upto 3 limit ",Toast.LENGTH_SHORT).show();
        }
        else if (feedDataForShare.size() > 0 || groupDataForShare.size() > 0) {
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }

    }

    @Override
    public void onSharedFragmentSetGroups(ArrayList<String> groupData) {
        Log.d("onSharedFragmentSetGrp", " " + groupData);

        groupDataForShare = groupData;
        if (feedDataForShare.size() + groupDataForShare.size() > 3 ){
            item.setVisible(false);
            Toast.makeText(ShareOnFeedsAndGroups.this,"Sharing limit exceeds ! You can share upto 3 limit ",Toast.LENGTH_SHORT).show();
        }
        else if (feedDataForShare.size() > 0 || groupDataForShare.size() > 0) {
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sharing_menu, menu);
        item = menu.findItem(R.id.shareOk);

        if (feedDataForShare.size() + groupDataForShare.size() > 3 ){
            item.setVisible(false);
            Toast.makeText(ShareOnFeedsAndGroups.this,"Sharing limit exceeds ! You can share upto 3 limit ",Toast.LENGTH_SHORT).show();
        }
        else if (feedDataForShare.size() > 0 || groupDataForShare.size() > 0) {
            item.setVisible(true);
        }else {
            item.setVisible(false);
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.shareOk:

                if (isOnline()) {

                    if (feedDataForShare.size() > 0) {

                        for (String feedId : feedDataForShare) {
                            if (recievedText!=null ) {
                                sendFeedComments(feedId, userAccessToken, recievedText, null);
                            }else {
                                sendFeedComments(feedId, userAccessToken, "", null);
                            }
                        }

                    }
                    if (groupDataForShare.size() > 0) {

                        for (String groupId : groupDataForShare) {
                            if (recievedText != null) {
                                sendGroupComments(groupId, userAccessToken, recievedText, null);
                            }else {
                                sendGroupComments(groupId, userAccessToken, "", null);
                            }
                        }


                    }

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            finishAffinity();

                            Intent intent = new Intent("finish_activity");
                            sendBroadcast(intent);

                        }
                    }, 3000);




                }else {
                    Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                }



                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    private void sendFeedComments(final String feed_id, final String userAccessToken, final String commentData , final String sharedPath) {


        String url = AppConfig.Base_Url + AppConfig.App_api + "addFeedsComments?feed_id=" + feed_id + "&userAccessToken=" + userAccessToken + "&comment_text=" + URLEncoder.encode(commentData);
        if(sharedPath != null)
            url += "&sharedPath="+sharedPath;

        Log.d("urlSendFeed"," "+url);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d("SOFAG", "Res____" + response);
                        try {

                            JSONObject obj = new JSONObject(new String(response.data));
                            Log.d("PFF", "IMG_Res" + obj);
                            final String status = obj.getString("status");

                            if (status.equals("success")) {

                                Toast.makeText(ShareOnFeedsAndGroups.this, "Comment has been post successfully.", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ShareOnFeedsAndGroups.this, "Comment has not been posted.", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error In Upoload", error.toString());

                        Toast.makeText(ShareOnFeedsAndGroups.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }) {


            /** If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
           /* @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tags", tags);
                return params;
            }*/


            /** Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                String imagename = "SabPaisa_CommentImage";
                if(bitmap != null){
                    params.put("commentFile", new DataPart(imagename + ".jpeg", getFileDataFromDrawable(bitmap)));

                    Log.d("Image",params.get("commentFile")+"");
                }
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);

    }

    private void sendGroupComments(final String GroupId, final String userAccessToken, final String commentData , final String sharedPath) {

        String url = AppConfig.Base_Url + AppConfig.App_api + "/addGroupsComments?group_id=" + GroupId + "&userAccessToken=" + userAccessToken + "&comment_text=" + URLEncoder.encode(commentData);

        if(sharedPath != null)
            url += "&sharedPath="+sharedPath;

        Log.d("urlSendGrp"," "+url);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d("SOFAG", "Res____" + response);


                        try {
                            // Parsing json object response
                            // response will be a json object
                            JSONObject obj = new JSONObject(new String(response.data));
                            String status = obj.getString("status");
                            if (status.equals("success")) {
                                Toast.makeText(ShareOnFeedsAndGroups.this, "Comment has been post successfully.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("Failure : ", obj.getString("response"));
                                Toast.makeText(ShareOnFeedsAndGroups.this, "Comment has not been posted.", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("InCatch", " " + e.getMessage());

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error In Upoload", error.toString());
                        //Toast.makeText(Proceed_Feed_FullScreen.this, "Image Upload Failed !", Toast.LENGTH_SHORT).show();
                    }
                }) {


            /** If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
           /* @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tags", tags);
                return params;
            }*/


            /** Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                String imagename = "SabPaisa_CommentImage";
                if(bitmap != null){
                    params.put("commentFile", new DataPart(imagename + ".jpeg", getFileDataFromDrawable(bitmap)));

                    Log.d("Image",params.get("commentFile")+"");
                }
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);


    }



    /**
     * The method is taking Bitmap as an argument
     * then it will return the byte[] array for the given bitmap
     * and we will send this array to the server
     * here we are using JPEG Compression with 80% quality
     * you can give quality between 0 to 100
     * 0 means worse quality
     * 100 means best quality
     */
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
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
