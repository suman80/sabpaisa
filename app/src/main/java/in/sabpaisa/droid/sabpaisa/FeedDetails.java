package in.sabpaisa.droid.sabpaisa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/*implements SwipeRefreshLayout.OnRefreshListener*/
public class FeedDetails extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    String FeedId;
    TextView feedDeatilsTextView, feedNameTextView;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    EditText commentadd = null;
    ProgressDialog loading = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coa_feed_details_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setTitle("Feed Details");
        mCollapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitleEnabled(false);
        Intent intent = getIntent();

        FeedId = intent.getStringExtra("FeedId");

        feedNameTextView = (TextView) findViewById(R.id.group_name_details);

        feedDeatilsTextView = (TextView) findViewById(R.id.group_description_details);

//       callFeedDeatilsByFeedId();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);

        callGetCommentList();

    }

//    ArrayList<> commentArrayList = null;

    private void callFeedDeatilsByFeedId() {
        String urlJsonObj = AppConfiguration.MAIN_URL + "/getFeedsDetailsByID/" + FeedId;
        urlJsonObj = urlJsonObj.trim().replace(" ", "%20");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Feed Details By ID ", response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String feed_Name = response.getString("feed_Name");
                    feedNameTextView.setText(feed_Name);
                    String feedText = response.getString("feedText");
                    feedDeatilsTextView.setText(feedText);

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
                VolleyLog.d("COAFeedError", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
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

    public void callGetCommentList() {

        String urlJsonObj = AppConfiguration.MAIN_URL + "/getFeedsComments/" + FeedId;
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
                        try {
                            swipeRefreshLayout.setRefreshing(false);
                            ArrayList<CommentData> commentArrayList = new ArrayList<CommentData>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject colorObj = response.getJSONObject(i);
//                                JSONObject colorObjBean = colorObj.getJSONObject("grpBean");
                                CommentData groupData = new CommentData();
                                groupData.setCommentText(colorObj.getString("comment_text"));
                                String dataTime = colorObj.getString("comment_date").split(" ")[1].replace(".0", "");
                                groupData.setComment_date(dataTime);

                                commentArrayList.add(groupData);

                            }
                            loadCommentListView(commentArrayList);
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
                }
        );
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
        String urlJsonObj = AppConfiguration.MAIN_URL + "/addCommentsToFeedByUser/" +
                FeedId + "/" + 1 + "/" + commentText;
        urlJsonObj = urlJsonObj.trim().replace(" ", "%20");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
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
                        Toast.makeText(FeedDetails.this, "Comment done.", Toast.LENGTH_SHORT).show();
                        callGetCommentList();
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
        callGetCommentList();
    }
    /*End onRefresh() for SwipeRefreshLayout*/
}