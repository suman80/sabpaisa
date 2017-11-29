package in.sabpaisa.droid.sabpaisa.Util;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.CommentAdapter;
import in.sabpaisa.droid.sabpaisa.CommentData;
import in.sabpaisa.droid.sabpaisa.FeedDetails;
import in.sabpaisa.droid.sabpaisa.Model.*;
import in.sabpaisa.droid.sabpaisa.Model.SkipClientData;
import in.sabpaisa.droid.sabpaisa.R;

import in.sabpaisa.droid.sabpaisa.Adapter.CommentAdapterDatabase;
import in.sabpaisa.droid.sabpaisa.SimpleDividerItemDecoration;


/*implements SwipeRefreshLayout.OnRefreshListener*/
public class SkipClientDetailsScreen extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    ArrayList<SkipClientData> institutions;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    String FeedId,FeedName,FeedDesc,FeedTime,FeedImage,clientName,state,clientImagePath,clientLogoPath;
    TextView feedDeatilsTextView, feedNameTextView,feedTime;
    ImageView feedImage;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    EditText commentadd = null;
    ProgressDialog loading = null;
    ArrayList<CommentData> commentArrayList;
    int lastSeq=0;
    boolean loadMore=true;
    ShimmerRecyclerView rv;
    CommentAdapterDatabase ca;
    ProgressBar progressBar;
    NestedScrollView nestedScroll;
    int lastCommentId;
    int totalComments = 0;
    // Indicates if footer ProgressBar is shown (i.e. next page is loading)
    private boolean isLoading = false;
    // If current page is the last page (Pagination will stop after this page load)
    private boolean isLastPage = false;
    private int currentPage = 1;
///////testRajDeep
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skip_clientdetails);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setTitle("Feed Details");
        mCollapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitleEnabled(false);
        Intent intent = getIntent();




        clientName = intent.getStringExtra("clientName");
        state = intent.getStringExtra("state");
        //FeedDesc = intent.getStringExtra("FeedDeatils");
        clientImagePath= getIntent().getStringExtra("clientImagePath");
        clientLogoPath = getIntent().getStringExtra("clientLogoPath");


         TextView clientNameTextView = (TextView) findViewById(R.id.group_name_details);

        TextView stateTextView = (TextView) findViewById(R.id.group_description_details);

         NetworkImageView clientImagePath = (NetworkImageView)findViewById(R.id.iv_feedImage);
        ImageView clientLogoPath  = (ImageView)findViewById(R.id.group_created_date_details);




//       callFeedDeatilsByFeedId();
        clientNameTextView.setText( clientName);
        stateTextView.setText(state);
        clientImagePath.setImageUrl(String.valueOf(clientImagePath), imageLoader);


//        feedTime.setText(FeedTime);
        //clientImagePath.setImageResource(R.drawable.group);
        //clientLogoPath.setImageUrl(R.id.);
       /* byte[] imgData = Base64.decode(FeedImage,Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
        feedImage.setImageBitmap(bmp);*/

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);

        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        nestedScroll = (NestedScrollView) findViewById(R.id.nestedScroll);

        rv = (ShimmerRecyclerView) findViewById(R.id.recycler_view_feed_details_comment);
//        rv.showShimmerAdapter();

        commentArrayList = new ArrayList<CommentData>();
        ca = new CommentAdapterDatabase(this);
        rv.setAdapter(ca);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.addItemDecoration(new SimpleDividerItemDecoration(this));
        rv.setLayoutManager(llm);
        rv.setNestedScrollingEnabled(false);

        /*Paginate.with(rv, callbacks)
                .setLoadingTriggerThreshold(1)
                .addLoadingListItem(loadMore?true:false)
                .setLoadingListItemCreator(new CustomLoadingListItemCreator())
                .build();*/
        currentPage=1;
        //loadComments();
    }



    private void loadComments(final String feed_id,final  String userAccessToken, final String comment_text) {
        String urlJsonObj = AppConfig.SAb_Api + "/getFeedsComments/" + "feed_id=1";
        final JsonArrayRequest request = new JsonArrayRequest(urlJsonObj, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.wtf("Response", String.valueOf(response));
                try {
                    swipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                    final ArrayList<CommentData> commentArrayList = new ArrayList<CommentData>();
                    /*if (i!=0) {
                        ca.removeLoadingFooter();
                    }*/
                    for (int i = 0; i < response.length(); i++) {
                        totalComments++;
                        JSONObject colorObj = response.getJSONObject(i);
//                                JSONObject colorObjBean = colorObj.getJSONObject("grpBean");
                        CommentData groupData = new CommentData();
                        groupData.setCommentText(colorObj.getString("comment_text"));
                        Long dataTime = (colorObj.getLong("createdDate"));//.split(" ")[1].replace(".0", "");
                        Timestamp timestamp = new Timestamp(dataTime);
                        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM HH:mm:ss");
                        fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
                        Date date = new Date(timestamp.getTime());
                        String time = String.valueOf(fmt.format(timestamp));
                        groupData.setComment_date(time);
                        groupData.setCommentId(colorObj.getInt("commentId"));
                        groupData.setCommentName(colorObj.getString("comment_by_name"));
                        commentArrayList.add(groupData);

                    }
                    loadCommentListView(commentArrayList);
                    ca.addAll(commentArrayList);
                    if (commentArrayList.size() == 10)
                        ca.addLoadingFooter();
                    /*else {
                        isLastPage = true;
                        int j;
                        for (j=0;j<commentArrayList.size();j++){
                        }
                        lastCommentId= commentArrayList.get(j-1).getCommentId();
//                        ca.removeLoadingFooter();
                    }*/

                   /* nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {

                                if (!isLastPage){
                                    isLoading = true;
                                    int i;
                                    for (i=0;i<commentArrayList.size();i++){
                                    }
                                    int lastId = commentArrayList.get(i-1).getCommentId();
//                                    currentPage += 1;
                                    loadComments(String.valueOf(1));
//                                    loadNextPage(getCommentList(dbHelper, currentPage));
                                }
                            }
                        }
                    });*/
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

            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("feed_id", feed_id);
                params.put("userAccessToken", userAccessToken);
                params.put("comment_text",comment_text);


                return params;
            }


        };


        AppController.getInstance().addToRequestQueue(request);
    }

    private String getTimeFormat(long timeStamp) {
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void callGetCommentList(final String feed_id) {

        String urlJsonObj = AppConfig.SAb_Api + "/getFeedsComments/" + "feed_id=1";
        urlJsonObj = urlJsonObj.trim().replace(" ", "%20");
        // Creating the JsonArrayRequest class called arrayreq, passing the required parameters
        //JsonURL is the URL to be fetched from
        JsonArrayRequest arrayreq = new JsonArrayRequest(urlJsonObj,
                // The second parameter Listener overrides the method onResponse() and passes
                //JSONArray as a parameter
                new Response.Listener<JSONArray>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.wtf("FeedResponse", response.toString());
                        try {
                            swipeRefreshLayout.setRefreshing(false);

                            if (response.length() < 10) {
                                loadMore = false;
                            }
                            int i;
                            for (i = 0; i < response.length(); i++) {
                                JSONObject colorObj = response.getJSONObject(i);
//                                JSONObject colorObjBean = colorObj.getJSONObject("grpBean");
                                CommentData groupData = new CommentData();
                                groupData.setCommentText(colorObj.getString("comment_text"));
//                                String dataTime = colorObj.getString("comment_date").split(" ")[1].replace(".0", "");
//                                groupData.setComment_date(dataTime);

                                commentArrayList.add(groupData);
                                lastSeq = colorObj.getInt("commentId");
                            }
                            ca.notifyItemChanged(commentArrayList.size() - 1);
                            rv.scrollToPosition(commentArrayList.size() - 1);
                            totalComments++;
//                            loadCommentListView(commentArrayList);

                        }
                        // Try and catch are included to handle any errors due to JSON
                        catch (JSONException e) {
                            // If an error occurs, this prints the error to the log
                            e.printStackTrace();
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
//                        callGroupDataList(listClick, GropuName);
                        Log.e("CommentData ", "CommentData Error");
                    }

                })


        {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("feed_id", feed_id);


                return params;
            }


        };

//        // Adds the JSON array request "arrayreq" to the request queue
        AppController.getInstance().addToRequestQueue(arrayreq);

        }

    private void loadCommentListView(ArrayList<CommentData> arrayList) {
        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view_feed_details_comment);
        CommentAdapter ca = new CommentAdapter(arrayList);
        rv.setAdapter(ca);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.addItemDecoration(new SimpleDividerItemDecoration(this));
        rv.setLayoutManager(llm);
        rv.setNestedScrollingEnabled(false);
    }

    public void onClickSendComment(View view) {
        commentadd = (EditText) findViewById(R.id.commentadd);
        String commentText = commentadd.getText().toString();
        showpDialog(view);
        callCommentService(commentText);
    }

    private void callCommentService(final String commentText) {
        String urlJsonObj = AppConfig.SAb_Api + "/addFeedsComments/" +
                "feed_id=1" + "/" + "userAccessToken=47DCC2AB8F1FEE94182E4426522C85D127A37404BE91FF13979B5DED7934EB49"+ "/" + "commentText=hi";
        urlJsonObj = urlJsonObj.trim().replace(" ", "%20");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("AddCommentsToFeedByUSer", response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = response.getString("status");
                    if (status.equals("success"))
                    {
                        commentadd.setText("");
                        Toast.makeText(SkipClientDetailsScreen.this, "Comment done.", Toast.LENGTH_SHORT).show();
//                        ca.cleanUpAdapter();
                        loadlatestComment();
//                        loadComments(0);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Feed Details", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void loadlatestComment() {
        ca.cleanUpAdapter();
        //loadComments(String.valueOf(1));
    }

    private void hidepDialog() {
        if (loading != null) {
            loading.dismiss();
        }
    }

    private void showpDialog(View v) {
        loading = new ProgressDialog(v.getContext());
        loading.setCancelable(true);
        //loading.setMessage("OTP Checking....");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.show();
    }

    /*Start onRefresh() for SwipeRefreshLayout*/
    @Override
    public void onRefresh() {
        ca.cleanUpAdapter();
//        loadComments(String.valueOf(1));
    }
    /*End onRefresh() for SwipeRefreshLayout*/
}