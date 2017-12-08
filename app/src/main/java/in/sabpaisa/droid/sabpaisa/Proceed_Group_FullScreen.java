package in.sabpaisa.droid.sabpaisa;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import in.sabpaisa.droid.sabpaisa.Util.AppConfiguration;

public class Proceed_Group_FullScreen extends AppCompatActivity {

    TextView groupsName,group_description_details;
    ImageView groupImage;
    CommentsDB dbHelper;
    private int TOTAL_PAGES = 3;
    String GroupsNm,GroupsDiscription,GroupsImg,GroupId,userAccessToken,response;

    ArrayList<CommentData> arrayList;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed_group_full_screen);

        groupsName=(TextView)findViewById(R.id.groupsName);
        group_description_details=(TextView)findViewById(R.id.group_description_details);
        groupImage=(ImageView)findViewById(R.id.groupImage);

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        response = sharedPreferences.getString("response", "123");

        userAccessToken = response;

        Log.d("PGFAccessToken", " " + userAccessToken);

        Log.d("PGFResponse", " " + response);

        GroupId=getIntent().getStringExtra("groupId");
        GroupsNm=getIntent().getStringExtra("groupName");
        GroupsDiscription=getIntent().getStringExtra("groupText");
        GroupsImg=getIntent().getStringExtra("groupImage");
        Log.d("NamemPGFS",""+GroupsNm);
        Log.d("DiscriptionPGFS",""+GroupsDiscription);
        Log.d("GroupImgPGFS",""+GroupsImg);
        Log.d("GroupID_PGFS",""+GroupId);


        groupsName.setText(GroupsNm);
        group_description_details.setText(GroupsDiscription);
        new DownloadImageTask(groupImage).execute(GroupsImg);

        callGetCommentList(GroupId);
        arrayList = new ArrayList<>();


    }


    //Code for fetching image from server
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loading.show();
        }

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            //loading.dismiss();
        }

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

    //EditText group_details_text_view = null;

    EditText group_details_text_view = null;

    public void onClickSendComment(View view) {
        group_details_text_view = (EditText) findViewById(R.id.commentadd);
        String commentText = group_details_text_view.getText().toString();
        // showpDialog(view);
        callCommentService(GroupId, userAccessToken, commentText);
    }

    private void callCommentService(final String GroupId, final String userAccessToken, final String comment_text) {
        String urlJsonObj = AppConfiguration.GroupAddComment + "/addGroupsComments?group_id=" + GroupId + "&userAccessToken=" + userAccessToken + "&comment_text=" + comment_text;

        // String urlJsonObj = AppConfiguration.FeedAddComent + "/aaddFeedsComments/" +"?feed_id="+ feed_id+ "/" + 1 + "/" + commentText;
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
                        group_details_text_view.setText("");
                        Toast.makeText(Proceed_Group_FullScreen.this, "Group Comment has been save successfully.", Toast.LENGTH_SHORT).show();
                        callGetCommentList(GroupId);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                // hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Group Details", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                //hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    public void callGetCommentList(final String GropuId) {
        //String urlJsonObj = AppConfiguration.MAIN_URL + "/getGroupsComments/" + GroupId;
        String tag_string_req="req_register";
        String urlJsonObj = AppConfiguration.GroupAddComment + "/getGroupsComments?group_id=" + GropuId;

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                urlJsonObj, new Response.Listener<String>(){

            // Takes the response from the JSON request
            @Override
            public void onResponse(String response) {
                try {

                    ArrayList<CommentData> commentArrayList = new ArrayList<CommentData>();

                    JSONObject jsonObject = new JSONObject(response);

                    //String status = jsonObject.getString("status");

                    // String response1 = jsonObject.getString("response");

                    JSONArray jsonArray = jsonObject.getJSONArray("response");

                    // new LoadDBfromAPI().execute(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        CommentData groupData = new CommentData();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        groupData.setCommentText(jsonObject1.getString("commentText"));
                        groupData.setCommentName(jsonObject1.getString("commentByName"));

                        String dataTime = jsonObject1.getString("commentDate");//.split(" ")[1].replace(".0", "");
                        Log.d("dataTimePFF"," "+dataTime);
                        groupData.setComment_date(getDate(Long.parseLong(dataTime)));
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

                        Log.e("Feed", "FeedError");
                    }
                }
        );
        // Adds the JSON array request "arrayreq" to the request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);
    }

    public class LoadDBfromAPI extends AsyncTask<JSONArray, Void, Void> {

        @Override
        protected Void doInBackground(JSONArray... params) {
            try {
                dbHelper = new CommentsDB(getApplicationContext());
                dbHelper.refreshTable();

                for (int i = 0; i < params[0].length(); i++) {
                    JSONObject colorObj = params[0].getJSONObject(i);
                    CommentData groupData = new CommentData();
                    // "commentDate": 1511248345000, "commentText": "test2"


                    groupData.setCommentText(colorObj.getString("commentText"));
                    String dataTime = colorObj.getString("commentDate");//.split(" ")[1].replace(".0", "");
                    groupData.setComment_date(dataTime);
// groupData.setPage(Integer.valueOf(String.valueOf(i/10+1)));

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
            loadCommentListView(arrayList);
        }


    }


    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("HH:mm", cal).toString();
        return date;
    }





}
