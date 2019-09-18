package in.sabpaisa.droid.sabpaisa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
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

//import neeraj_sabpaisa.com.coa.Adapters.CommentAdapterDatabase;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.AppConfiguration;

/*implements SwipeRefreshLayout.OnRefreshListener*/
public class GroupDetails extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    // Index from which pagination should start (1 is 1st page in our case)
    private static final int PAGE_START = 1;
    String GroupName, GroupDescription, GroupId;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    /*START Declaring Varibles Globally*/
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rv;
    CommentAdapterDatabase ca;
    LinearLayoutManager llm;
    NestedScrollView nestedScroll;
    CommentsDB dbHelper;
    int page=1;
    ProgressBar progressBar;
    ArrayList<CommentData> commentData;
    EditText group_details_text_view = null;
    ProgressDialog loading = null;
    // Indicates if footer ProgressBar is shown (i.e. next page is loading)
    private boolean isLoading = false;
    // If current page is the last page (Pagination will stop after this page load)
    private boolean isLastPage = false;
    // total no. of pages to load. Initial load is page 0, after which 2 more pages will load.
    private int TOTAL_PAGES = 3;
    // indicates the current page which Pagination is fetching.
    private int currentPage = PAGE_START;
    private int previousTotal = 0;
    private int visibleThreshold = 5;

//    ArrayList<> commentArrayList = null;

    /*END Declaring Varibles Globally*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  CommonUtils.setFullScreen(this);
        setContentView(R.layout.coa_group_details_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Group Details");
        Intent intent = getIntent();

        mCollapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitleEnabled(false);

        GroupId = intent.getStringExtra("GroupId");
        GroupDescription = intent.getStringExtra("GroupDescription");
        TextView textView = (TextView) findViewById(R.id.group_name_details);
        textView.setText(GroupName);
        textView = (TextView) findViewById(R.id.group_description_details);
        textView.setText(GroupDescription);

        progressBar = (ProgressBar) findViewById(R.id.main_progress); /*Progress Bar to show before list*/
        nestedScroll = (NestedScrollView) findViewById(R.id.nestedScroll);/*Nested Scroll to know when the end of screen is reached*/
        rv = (RecyclerView) findViewById(R.id.recycler_view_group_details_comment);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);

        callGetCommentList();
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
    /*END AsyncTask for saving Data in Database in background*/

    public void callGetCommentList() {
        String urlJsonObj = AppConfig.Base_Url+AppConfiguration.MAIN_URL + "/getGroupsComments/" + GroupId;

        // Creating the JsonArrayRequest class called arrayreq, passing the required parameters
        //JsonURL is the URL to be fetched from
        JsonArrayRequest arrayreq = new JsonArrayRequest(urlJsonObj,
                // The second parameter Listener overrides the method onResponse() and passes
                //JSONArray as a parameter
                new Response.Listener<JSONArray>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.wtf("Response", String.valueOf(response));
                        new LoadDBfromAPI().execute(response); /*AsyncTask for saving Data in Database in background*/
                        /*try {
                            ArrayList<CommentData> commentArrayList = new ArrayList<CommentData>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject colorObj = response.getJSONObject(i);
//                                JSONObject colorObjBean = colorObj.getJSONObject("grpBean");
                                CommentData groupData = new CommentData();
                                groupData.setCommentText(colorObj.getString("comment_text"));
                                String dataTime = colorObj.getString("comment_date");//.split(" ")[1].replace(".0", "");
                                groupData.setComment_date(dataTime);

                                commentArrayList.add(groupData);

                            }
                            loadCommentListView(commentArrayList);
                        }
                        // Try and catch are included to handle any errors due to JSON
                        catch (JSONException e) {
                            // If an error occurs, this prints the error to the log
                            e.printStackTrace();
                        }*/
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
                        Log.e("Volley", "Error");
                    }
                }
        );
//        // Adds the JSON array request "arrayreq" to the request queue
        AppController.getInstance().addToRequestQueue(arrayreq);

    }
    /*END Helper Method to load comment list in pagination*/

    /*START Helper Method to load comment list in pagination*/
    private void loadCommentsListView(final CommentsDB dbHelper){

        currentPage=1;
        commentData = new ArrayList<>();
        ca = new CommentAdapterDatabase(this);
        rv.setAdapter(ca);

        rv.setItemAnimator(new DefaultItemAnimator());


        llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.addItemDecoration(new SimpleDividerItemDecoration(this));

        rv.setLayoutManager(llm);

        rv.setNestedScrollingEnabled(false);

        loadFirstPage(getCommentList(dbHelper,1));

        nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {

                    if (currentPage==TOTAL_PAGES){
                        isLoading=false;
                        isLastPage=true;
                    }else {
                        isLoading = true;
                        currentPage += 1;
                        loadNextPage(getCommentList(dbHelper, currentPage));
                    }
                }
            }
        });


    }

    /*START Method to get comments from datase in pagination*/
    private void loadFirstPage(ArrayList<CommentData> commentList){
        ArrayList<CommentData> results = commentList;
        progressBar.setVisibility(View.GONE);
        ca.addAll(results);

        if (currentPage <= TOTAL_PAGES && results.size()==10) ca.addLoadingFooter();
        else isLastPage = true;

    }

    private void loadNextPage(ArrayList<CommentData> commentList){
        ca.removeLoadingFooter();
        isLoading = false;

        ArrayList<CommentData> results = commentList;
        ca.addAll(results);

        if (currentPage != TOTAL_PAGES) ca.addLoadingFooter();
        else isLastPage = true;
    }
    /*Method to get comments from datase in pagination*/

    private ArrayList<CommentData> getCommentList(CommentsDB commentDB, int page) {
        Cursor cursor = commentDB.getCommentFromPage(page++);
        ArrayList<CommentData> list = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                CommentData data = new CommentData();
                data.setComment_date(String.valueOf(cursor.getString(cursor.getColumnIndex("comment_ts"))));
                data.setCommentText(cursor.getString(cursor.getColumnIndex("comment_desc")));

                list.add(data);
            } while (cursor.moveToNext());
        }
        return list;
    }
    /*END onRefresh() for swipeRefreshLayout*/

    /*START onRefresh() for swipeRefreshLayout*/
    @Override
    public void onRefresh() {
        ca.cleanUpAdapter();
        callGetCommentList();
    }

    private void loadCommentListView(ArrayList<CommentData> arrayList) {
        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view_group_details_comment);
        CommentAdapter ca = new CommentAdapter(arrayList,getApplicationContext());
        rv.setAdapter(ca);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.addItemDecoration(new SimpleDividerItemDecoration(this));
        rv.setLayoutManager(llm);
        rv.setNestedScrollingEnabled(false);
    }

    public void onClickSendComment(View view) {
        group_details_text_view = (EditText) findViewById(R.id.commentadd);
        String commentText = group_details_text_view.getText().toString();
        showpDialog(view);
        callCommentService(commentText);
    }

    private void callCommentService(final String commentText) {
        String urlJsonObj = AppConfiguration.MAIN_URL + "/addCommentsToGroupByUser/" + GroupId + "/" + 1 + "/" + commentText;
        urlJsonObj = urlJsonObj.trim().replace(" ", "%20");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Group Details", response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        group_details_text_view.setText("");
                        Toast.makeText(GroupDetails.this, "Group Comment has been save successfully.", Toast.LENGTH_SHORT).show();
                        ca.cleanUpAdapter();
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
                VolleyLog.d("Group Details", "Error: " + error.getMessage());
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
        loading.setMessage("Adding comment");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.show();
    }

    /*START AsyncTask for saving Data in Database in background*/
    public class LoadDBfromAPI extends AsyncTask<JSONArray, Void, Void> {

        @Override
        protected Void doInBackground(JSONArray... params) {
            try {
                dbHelper = new CommentsDB(getApplicationContext());
                dbHelper.refreshTable();

                for (int i = 0; i < params[0].length(); i++) {
                    JSONObject colorObj = params[0].getJSONObject(i);
                    CommentData groupData = new CommentData();
                    groupData.setCommentName(colorObj.getString("comment_name"));
                    groupData.setCommentText(colorObj.getString("comment_text"));
                    String dataTime = colorObj.getString("comment_date");//.split(" ")[1].replace(".0", "");
                    groupData.setComment_date(dataTime);
//                    groupData.setPage(Integer.valueOf(String.valueOf(i/10+1)));

                    dbHelper.insertComment(colorObj.getString("commentId"), colorObj.getString("comment_text"), colorObj.getString("comment_date"), colorObj.getString("comment_by"), colorObj.getString("group_id"), Integer.valueOf(String.valueOf(i / 10 + 1)));
                    TOTAL_PAGES = Integer.valueOf(String.valueOf(i / 10 + 1));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            swipeRefreshLayout.setRefreshing(false);
            loadCommentsListView(dbHelper);
        }
    }
}