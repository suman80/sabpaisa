package in.sabpaisa.droid.sabpaisa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.InputStream;
import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Util.AppConfiguration;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;

public class Proceed_Group_FullScreen extends AppCompatActivity {

    public static String MySharedPrefForGroup = "mySharedPrefForGroup";
    String userAccessToken;
    TextView groupsName, group_description_details;
    ImageView groupImage;
    String group_id;

    String GroupsNm, GroupsDiscription, GroupsImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed__group__full_screen);
        String response;
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        response = sharedPreferences.getString("response", "123");

        Log.d("ClientId_FeedsFrag", " " + response);

        String userAccessToken = response;

        Log.d("AccessToken", " " + userAccessToken);
        groupsName = (TextView) findViewById(R.id.groupsName);
        group_description_details = (TextView) findViewById(R.id.group_description_details);
        groupImage = (ImageView) findViewById(R.id.groupImage);
        String group_id =getIntent().getStringExtra("group_id");
        Log.d("group_id", "" + group_id);
        GroupsNm = getIntent().getStringExtra("groupName");
        GroupsDiscription = getIntent().getStringExtra("groupText");
        GroupsImg = getIntent().getStringExtra("groupImage");
        Log.d("FeedsNmPGFFS", "" + GroupsNm);
        Log.d("feedsDiscriptionPGFS", "" + GroupsDiscription);
        Log.d("feedImgPGFS", "" + GroupsImg);

        groupsName.setText(GroupsNm);
        group_description_details.setText(GroupsDiscription);
        new DownloadImageTask(groupImage).execute(GroupsImg);

       // callGetCommentList(group_id);

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

//    ArrayList<> commentArrayList = null;
/*

    public void callGetCommentList(final String group_id) {
        String urlJsonObj = AppConfiguration.Group_URL + "/getGroupsComments/" + group_id;

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
                        Log.e("Volley", "Error");
                    }
                }
        );
//        // Adds the JSON array request "arrayreq" to the request queue
        AppController.getInstance().addToRequestQueue(arrayreq);

    }
*/

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
        showpDialog(view);
        //callCommentService(group_id,userAccessToken,commentText);
    }

   /* private void callCommentService(final String group_id,final String userAccessToken, final String comment_text) {


        String urlJsonObj = AppConfiguration.Group_URL + "/addGroupsComments?group_id="+group_id+ "&userAccessToken="+userAccessToken+"&comment_text="+comment_text;
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
                        callGetCommentList(group_id);

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
*/
    ProgressDialog loading = null;

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
//}


    //Code for fetching image from server
    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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

}

//}
