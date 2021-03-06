package in.sabpaisa.droid.sabpaisa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.ViewPagerAdapter;
import in.sabpaisa.droid.sabpaisa.Fragments.ShareFeedFragment;
import in.sabpaisa.droid.sabpaisa.Fragments.ShareFeedSpaceFragment;
import in.sabpaisa.droid.sabpaisa.Fragments.ShareGroupSpaceFragment;
import in.sabpaisa.droid.sabpaisa.Fragments.SharedGroupFragment;
import in.sabpaisa.droid.sabpaisa.Interfaces.FlagCallback;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;
import in.sabpaisa.droid.sabpaisa.Util.VolleyMultipartRequest;

import static in.sabpaisa.droid.sabpaisa.MainActivitySkip.AppDecideFlag;

public class SharingActivity extends AppCompatActivity implements FlagCallback {

    ArrayList<CommentData> commentDataArrayList;
    ShareFeedFragment feedsFragments;
    SharedGroupFragment groupsFragments;
    ShareFeedSpaceFragment shareFeedSpaceFragment;
    ShareGroupSpaceFragment shareGroupSpaceFragment;
    ArrayList<String> feedDataForShare;
    ArrayList<String> groupDataForShare;
    MenuItem item;
    Toolbar toolbar;
    String userAccessToken;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);

        feedDataForShare = new ArrayList<>();
        groupDataForShare = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Share");
        toolbar.setNavigationIcon(R.drawable.previousmoresmall);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpagerSelect);
        setupViewPager(viewPager);

        viewPager.setOffscreenPageLimit(2);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        int[] tabIcons = {
                R.drawable.feeds,
                R.drawable.groups,
        };

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);


        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        userAccessToken = sharedPreferences.getString("response", "123");


        commentDataArrayList = (ArrayList<CommentData>) getIntent().getExtras().getSerializable("SELECTED_LIST");

        //Log.d("commentDataArrayList"," "+commentDataArrayList);

        for (CommentData Item : commentDataArrayList) {

            Log.d("commentDataArrayList", " " + Item.getCommentText());


        }


    }


    private void setupViewPager(ViewPager viewPager) {

        Log.d("setupViewPager","Method Started");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Log.d("AppDecideFlag","Value__"+AppDecideFlag);
        Log.d("AppDecideFlagFeed","appCidValue__"+FeedSpaceCommentsActivity.appCid);
        Log.d("AppDecideFlagGroup","appCidValue__"+GroupSpaceCommentActivity.appCid);

        if (AppDecideFlag || FeedSpaceCommentsActivity.appCid != null || GroupSpaceCommentActivity.appCid != null){

            shareFeedSpaceFragment = new ShareFeedSpaceFragment();
            adapter.addFragment(shareFeedSpaceFragment,"");

            shareGroupSpaceFragment = new ShareGroupSpaceFragment();
            adapter.addFragment(shareGroupSpaceFragment,"");

            viewPager.setAdapter(adapter);

        }else {
            Log.d("AppDecideFlagInsideElse","Here");
            feedsFragments = new ShareFeedFragment();
            adapter.addFragment(feedsFragments, ""); //changing here creating different frags

            groupsFragments = new SharedGroupFragment();
            adapter.addFragment(groupsFragments, "");//changing here creating different frags

            viewPager.setAdapter(adapter);
        }

    }


    @Override
    public void onSharedFragmentSetFeeds(ArrayList<String> feedData) {
        Log.d("onSharedFragmentSetFeed", " " + feedData);

        feedDataForShare = feedData;
        if (feedDataForShare.size() + groupDataForShare.size() > 3 ){
            item.setVisible(false);
            Toast.makeText(SharingActivity.this,"Sharing limit exceeds ! You can share upto 3 limit ",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SharingActivity.this,"Sharing limit exceeds ! You can share upto 3 limit ",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SharingActivity.this,"Sharing limit exceeds ! You can share upto 3 limit ",Toast.LENGTH_SHORT).show();
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

                String selectedComment = null;
                for (CommentData Item : commentDataArrayList) {

                    Log.d("commentDataArrayList", " " + Item.getCommentText());
                    selectedComment = Item.getCommentText();
                    String sharedUrl = Item.getCommentImage();
                    Log.d("sharedUrl"," "+sharedUrl);
                    if(sharedUrl == null || sharedUrl.equals("null") || sharedUrl.trim().isEmpty())
                        sharedUrl = null;

                    if (feedDataForShare.size() > 0) {

                        for (String feedId : feedDataForShare) {
                            sendFeedComments(feedId, userAccessToken, selectedComment,sharedUrl);
                        }

                    }
                    if (groupDataForShare.size() > 0) {

                        for (String groupId : groupDataForShare) {
                            sendGroupComments(groupId, userAccessToken, selectedComment,sharedUrl);
                        }


                    }
                }


                /*if (groupDataForShare.size() + feedDataForShare.size() > 0) {
                    Intent intent = new Intent(SharingActivity.this, FullViewOfClientsProceed.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    MainFeedAdapter.isClicked=false;
                    MainGroupAdapter1.isClicked=false;
                    startActivity(intent);
                }*/

                if (!AppDecideFlag){
                    if (groupDataForShare.size() + feedDataForShare.size() > 0) {
                        Intent intent = new Intent(SharingActivity.this, FullViewOfClientsProceed.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        MainFeedAdapter.isClicked=false;
                        MainGroupAdapter1.isClicked=false;
                        startActivity(intent);
                    }
                }else {
                    finish();
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
                        Log.d("PFF", "IMG_Res" + response);
                        try {

                            JSONObject obj = new JSONObject(new String(response.data));
                            Log.d("PFF", "IMG_Res" + obj);
                            final String status = obj.getString("status");

                            if (status.equals("success")) {

                                Toast.makeText(SharingActivity.this, "Comment has been post successfully.", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(SharingActivity.this, "Comment has not been posted.", Toast.LENGTH_SHORT).show();

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

                        Toast.makeText(SharingActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
            /*@Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                String imagename = "SabPaisa_CommentImage";
                if(commentFile != null){
                    params.put("commentFile", new DataPart(imagename + ".jpeg", getFileDataFromDrawable(commentFile)));

                    Log.d("Image",params.get("commentFile")+"");
                }
                return params;
            }*/
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
                        Log.d("PGF", "IMG_Res" + response);


                        try {
                            // Parsing json object response
                            // response will be a json object
                            JSONObject obj = new JSONObject(new String(response.data));
                            String status = obj.getString("status");
                            if (status.equals("success")) {
                                Toast.makeText(SharingActivity.this, "Comment has been post successfully.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("Failure : ", obj.getString("response"));
                                Toast.makeText(SharingActivity.this, "Comment has not been posted.", Toast.LENGTH_SHORT).show();
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
            /*@Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                String imagename = "SabPaisa_CommentImage";
                if(commentFile != null){
                    params.put("commentFile", new DataPart(imagename + ".jpeg", getFileDataFromDrawable(commentFile)));

                    Log.d("Image",params.get("commentFile")+"");
                }
                return params;
            }*/
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);


    }


}
