package in.sabpaisa.droid.sabpaisa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.ViewPagerAdapter;
import in.sabpaisa.droid.sabpaisa.Fragments.ProceedFeedsFragment;
import in.sabpaisa.droid.sabpaisa.Fragments.ProceedGroupsFragments;
import in.sabpaisa.droid.sabpaisa.Fragments.ShareFeedFragment;
import in.sabpaisa.droid.sabpaisa.Fragments.SharedGroupFragment;
import in.sabpaisa.droid.sabpaisa.Interfaces.FlagCallback;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;

public class SharingActivity extends AppCompatActivity implements FlagCallback {

    ArrayList<CommentData> commentDataArrayList;
    ShareFeedFragment feedsFragments;
    SharedGroupFragment groupsFragments;
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
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
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


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        feedsFragments = new ShareFeedFragment();
        adapter.addFragment(feedsFragments, ""); //changing here creating different frags

        groupsFragments = new SharedGroupFragment();
        adapter.addFragment(groupsFragments, "");//changing here creating different frags

        viewPager.setAdapter(adapter);


    }


    @Override
    public void onSharedFragmentSetFeeds(ArrayList<String> feedData) {
        Log.d("onSharedFragmentSetFeed", " " + feedData);

        feedDataForShare = feedData;
        if (feedDataForShare.size() > 0 || groupDataForShare.size() > 0) {
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }
    }

    @Override
    public void onSharedFragmentSetGroups(ArrayList<String> groupData) {
        Log.d("onSharedFragmentSetGrp", " " + groupData);

        groupDataForShare = groupData;

        if (feedDataForShare.size() > 0 || groupDataForShare.size() > 0) {
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


        if (feedDataForShare.size() > 0 || groupDataForShare.size() > 0) {
            item.setVisible(true);
        } else {
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
                    if (feedDataForShare.size() > 0) {

                        for (String feedId : feedDataForShare) {
                            sendFeedComments(feedId, userAccessToken, selectedComment);
                        }

                    }
                    if (groupDataForShare.size() > 0) {

                        for (String groupId : groupDataForShare) {
                            sendGroupComments(groupId, userAccessToken, selectedComment);
                        }


                    }
                }


                if(groupDataForShare.size() + feedDataForShare.size()>0)
                {
                    Intent intent = new Intent(SharingActivity.this, FullViewOfClientsProceed.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void sendFeedComments(final String feed_id, final String userAccessToken, final String commentData) {

        String urlJsonObj = AppConfig.Base_Url + AppConfig.App_api + "addFeedsComments?feed_id=" + feed_id + "&userAccessToken=" + userAccessToken + "&comment_text=" + URLEncoder.encode(commentData);

        urlJsonObj = urlJsonObj.trim().replace(" ", "%20");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Group Details", response.toString());
                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = response.getString("status");
                    if (status.equals("success")) {

                        Toast.makeText(SharingActivity.this, "Comment has been post successfully.", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(SharingActivity.this, "Comment has not been posted.", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                   /* Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();*/
                }
                // hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        //Toast.makeText(Proceed_Feed_FullScreen.this, "error11!", Toast.LENGTH_SHORT).show();

                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                        // Toast.makeText(Proceed_Feed_FullScreen.this, "error22!", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e2) {
                        // Toast.makeText(Proceed_Feed_FullScreen.this, "error33!", Toast.LENGTH_SHORT).show();

                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
                // hide the progress dialog
                //hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void sendGroupComments(final String GroupId, final String userAccessToken, final String commentData) {

        String urlJsonObj = AppConfig.Base_Url + AppConfig.App_api + "/addGroupsComments?group_id=" + GroupId + "&userAccessToken=" + userAccessToken + "&comment_text=" + URLEncoder.encode(commentData);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                urlJsonObj, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d("a", response.toString());


                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = response.getString("status");
                    if (status.equals("success")) {

                        Toast.makeText(SharingActivity.this, "Comment has been post successfully.", Toast.LENGTH_SHORT).show();


                    } else if (status.equals("failed")) {

                        Toast.makeText(SharingActivity.this, "Comment has not been posted.", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: KASKADKASKDASKCNSKACKASNVKNASKANka" + e.getMessage(),
                            Toast.LENGTH_LONG).show();

                }
                // hidepDialog();
            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        String obj = new String(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }


            }


        });


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


}
