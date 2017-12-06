package in.sabpaisa.droid.sabpaisa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.AppConfiguration;

public class Proceed_Feed_FullScreen extends AppCompatActivity {

    TextView feedsName, feed_description_details;
    ImageView feedImage;

    String FeedsNm, feedsDiscription, feedImg, response, feed_id, userAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed__feed__full_screen);

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        response = sharedPreferences.getString("response", "123");

        userAccessToken = response;

        Log.d("AccessToken", " " + userAccessToken);

        Log.d("ClientId_FeedsFrag", " " + response);

        feedsName = (TextView) findViewById(R.id.feedsName);
        feed_description_details = (TextView) findViewById(R.id.feed_description_details);
        feedImage = (ImageView) findViewById(R.id.feedImage);

        FeedsNm = getIntent().getStringExtra("feedName");
        feedsDiscription = getIntent().getStringExtra("feedText");
        feedImg = getIntent().getStringExtra("feedImage");
        feed_id = getIntent().getStringExtra("feedId");
        Log.d("FeedsNmPFF", "" + feed_id);
        Log.d("FeedsNmPFF", "" + FeedsNm);
        Log.d("feedsDiscriptionPFF", "" + feedsDiscription);
        Log.d("feedImgPFF", "" + feedImg);

        feedsName.setText(FeedsNm);
        feed_description_details.setText(feedsDiscription);
        new DownloadImageTask(feedImage).execute(feedImg);
        callGetCommentList(feed_id);

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
        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view_group_details_comment);
        CommentAdapter ca = new CommentAdapter(arrayList);
        rv.setAdapter(ca);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.addItemDecoration(new SimpleDividerItemDecoration(this));
        rv.setLayoutManager(llm);
        rv.setNestedScrollingEnabled(false);
    }

    EditText group_details_text_view = null;

    public void onClickSendComment(View view) {
        group_details_text_view = (EditText) findViewById(R.id.commentadd);
        String commentText = group_details_text_view.getText().toString();
        // showpDialog(view);
        callCommentService(feed_id, userAccessToken, commentText);
    }

    private void callCommentService(final String feed_id, final String userAccessToken, final String comment_text) {
        String urlJsonObj = AppConfiguration.FeedAddComent + "/addFeedsComments?feed_id=" + feed_id + "&userAccessToken=" + userAccessToken + "&comment_text=" + comment_text;

        // String urlJsonObj = AppConfiguration.FeedAddComent + "/aaddFeedsComments/" +"?feed_id="+ feed_id+ "/" + 1 + "/" + commentText;
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
                        Toast.makeText(Proceed_Feed_FullScreen.this, "Group Comment has been save successfully.", Toast.LENGTH_SHORT).show();
                        callGetCommentList(feed_id);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                // hidepDialog();
            }
        }, new ErrorListener() {

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


    public void callGetCommentList(final String feed_id) {
        //String urlJsonObj = AppConfiguration.MAIN_URL + "/getGroupsComments/" + GroupId;
        String tag_string_req="req_register";
        String urlJsonObj = AppConfiguration.FeedAddComent + "/getFeedsComments/" + feed_id;

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

                        for (int i = 0; i < jsonArray.length(); i++) {
                            CommentData groupData = new CommentData();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            groupData.setCommentText(jsonObject1.getString("comment_text"));
                            String dataTime = jsonObject1.getString("comment_date");//.split(" ")[1].replace(".0", "");
                            groupData.setComment_date(dataTime);

                        }


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

    }


