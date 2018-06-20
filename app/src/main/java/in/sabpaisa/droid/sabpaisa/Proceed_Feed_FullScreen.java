package in.sabpaisa.droid.sabpaisa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.rockerhieu.emojicon.EmojiconEditText;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import in.sabpaisa.droid.sabpaisa.Adapter.FeedsCommentOfflineAdapter;
import in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments;
import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.Model.FeedCommentsOfflineModel;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.AppConfiguration;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;

public class Proceed_Feed_FullScreen extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    TextView feedsName, feed_description_details;
    ImageView feedImage;
    CommentsDB dbHelper;
    private int TOTAL_PAGES = 3;
    String ts1;
    Timestamp ts;

    String dataTime1;
    int mHour;
    int mMinute;
    String i;
    ArrayList<CommentData> arrayList;
    String commentText;
    String date1;
    String FeedsNm, feedsDiscription, feedImg, response, feed_id, userAccessToken;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rv;
    Toolbar toolbar;
    String dataTime;
    ScrollView scrollView;
    EndlessScrollListener scrollListener;
    public static String MySharedPrefProceedFeedFullScreen="mySharedPrefFortime";

    /////////Local Db//////////
    AppDbComments db;
    ArrayList<FeedCommentsOfflineModel> arrayListForOffline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_proceed__feed__full_screen);
       // this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        response = sharedPreferences.getString("response", "123");

        userAccessToken = response;
        scrollView=(ScrollView)findViewById(R.id.scrollView);

        Log.d("AccessToken", " " + userAccessToken);

        Log.d("FFResponse", " " + response);

        feedsName = (TextView) findViewById(R.id.feedsName);
        feed_description_details = (TextView) findViewById(R.id.feed_description_details);
        feedImage = (ImageView) findViewById(R.id.feedImage);
        swipeRefreshLayout= (SwipeRefreshLayout)
                findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);

        FeedsNm = getIntent().getStringExtra("feedName");
        feedsDiscription = getIntent().getStringExtra("feedText");
        feedImg = getIntent().getStringExtra("feedImage");
        feed_id = getIntent().getStringExtra("feedId");
        Log.d("FeedsID", "" + feed_id);
        Log.d("FeedsNmPFF", "" + FeedsNm);
        Log.d("feedsDiscriptionPFF", "" + feedsDiscription);
        Log.d("feedImgPFF", "" + feedImg);

        feedsName.setText(FeedsNm);
        feed_description_details.setText(feedsDiscription);
        //new DownloadImageTask(feedImage).execute(feedImg);

        Glide.with(getApplicationContext())
                .load(feedImg)
                .error(R.drawable.image_not_found)
                .into(feedImage);

        ///////////////////////DB/////////////////////////////////
        db = new AppDbComments(Proceed_Feed_FullScreen.this);


        Log.d("Feedidproceed",""+feed_id);
        arrayList = new ArrayList<>();
        toolbar.setNavigationIcon(R.drawable.previousmoresmall);

        toolbar.setTitle(FeedsNm);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                        //Toast.makeText(MainActivity.this, "clicking the toolbar!", Toast.LENGTH_SHORT).show();
                    }
                }

        );


        if (isOnline()) {

            callGetCommentList(feed_id);
        }else {

            Log.d("ProceedFeedFullSCR", "No Internet");

            /////////////////////Retrive data from local DB///////////////////////////////

            Cursor res = db.getParticularFeedComments(feed_id);
            arrayListForOffline = new ArrayList<>();
            if (res.getCount() > 0) {
                StringBuffer stringBuffer = new StringBuffer();
                while (res.moveToNext()) {
                    stringBuffer.append(res.getString(0) + " ");
                    stringBuffer.append(res.getString(1) + " ");
                    stringBuffer.append(res.getString(2) + " ");
                    stringBuffer.append(res.getString(3) + " ");
                    stringBuffer.append(res.getString(4) + " ");
                    stringBuffer.append(res.getString(5) + " ");

                    FeedCommentsOfflineModel feedCommentsOfflineModel = new FeedCommentsOfflineModel();
                    feedCommentsOfflineModel.setFeedId(res.getString(1));
                    feedCommentsOfflineModel.setCommentId(res.getInt(2));
                    feedCommentsOfflineModel.setCommentText(res.getString(3));
                    feedCommentsOfflineModel.setCommentByName(res.getString(4));
                    feedCommentsOfflineModel.setCommentDate(res.getString(5));

                    arrayListForOffline.add(feedCommentsOfflineModel);


                }
                Log.d("FeedsCommentsOffline", "-->" + stringBuffer);

//                ProceedGroupsFragmentsOfflineAdapter adapter = new ProceedGroupsFragmentsOfflineAdapter(groupArrayListForOffline, getContext());
//                groupList.setAdapter(adapter);
//                adapter.notifyDataSetChanged();

                final RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view_feed_details_comment);
                final FeedsCommentOfflineAdapter ca = new FeedsCommentOfflineAdapter(arrayListForOffline);
                rv.setAdapter(ca);

                LinearLayoutManager llm = new LinearLayoutManager(this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);

                llm.setReverseLayout(true);
                scrollListener = new EndlessScrollListener(llm) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        callGetCommentList(feed_id);
                    }
                };
                rv.addOnScrollListener(scrollListener);
                rv.addItemDecoration(new SimpleDividerItemDecoration(this));
                rv.setLayoutManager(llm);
                rv.setNestedScrollingEnabled(false);
                scrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //replace this line to scroll up or down
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }, 100L);


            }else {
                Log.d("PGFLocalDb", "In Else Part");
                Toast.makeText(Proceed_Feed_FullScreen.this, "No Data Found !", Toast.LENGTH_SHORT).show();
            }

        }


        //new LoadDBfromAPI().execute(response);
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
            String urldisplay = urls[0];            Bitmap bitmap = null;
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
        final RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view_feed_details_comment);
        final CommentAdapter ca = new CommentAdapter(arrayList);
        rv.setAdapter(ca);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

       llm.setReverseLayout(true);
       scrollListener=new EndlessScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                callGetCommentList(feed_id);
            }
        };
        rv.addOnScrollListener(scrollListener);
        rv.addItemDecoration(new SimpleDividerItemDecoration(this));
        rv.setLayoutManager(llm);
        rv.setNestedScrollingEnabled(false);
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                //replace this line to scroll up or down
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 100L);
    }

    EditText group_details_text_view ;

    public void onClickSendComment(View view) {
        group_details_text_view = (EditText) findViewById(R.id.commentadd);
        StringEscapeUtils.escapeJava(group_details_text_view.getText().toString());
        EditText etEmojiEditText = new EditText(this);
        etEmojiEditText.setText("TYPE SOMETHING IN EMOJI");

        String toServer = String.valueOf(group_details_text_view.getText());
        String toServerUnicodeEncoded = StringEscapeUtils.escapeJava(toServer);

        String serverResponse = "SOME RESPONSE FROM SERVER WITH UNICODE CHARACTERS";
        String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(serverResponse);

        commentText = group_details_text_view.getText().toString();
        i= StringEscapeUtils.escapeJava(commentText);
        Log.d("commentText3","67667767 "+i);
//        rv.smoothScrollBy(100,100);

        if (i.trim().length()==0 )
        {

            Log.d("commentText2"," "+commentText);
            AlertDialog.Builder builder =new AlertDialog.Builder(Proceed_Feed_FullScreen.this);
            builder.setTitle("Comment");
            builder.setMessage("Hey,looks like you forgot to enter text.");
            builder.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }

        else if(i.equals("%"))

        {
            commentText.replace("%", "%25");
            Log.e("ctctcc ", "CommentData" + commentText);
            callCommentService(feed_id, userAccessToken, commentText);
        }


        else if(i.equals("&"))

        {
            commentText.replace("&", "%26");
            Log.e("ctctcc ", "CommentData2 " + commentText);
            callCommentService(feed_id, userAccessToken, commentText);

        }




        else if(i.trim().length()>1999)
        {
            AlertDialog.Builder builder =new AlertDialog.Builder(Proceed_Feed_FullScreen.this);
            builder.setTitle("Comment");
            builder.setMessage("Hey folk,It looks like you exceeded the text limit");
            builder.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }

            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

            // showpDialog(view);
        else {
            callCommentService(feed_id, userAccessToken, commentText);
            Log.e("CommentDatafeeddetaida ", "CommentData " + commentText);
        }

        Log.d("commentText3"," "+commentText);

    }

    private void callCommentService(final String feed_id, final String userAccessToken, final String comment_text) {
        String urlJsonObj = AppConfig.Base_Url+AppConfig.App_api+ "addFeedsComments?feed_id=" + feed_id + "&userAccessToken=" + userAccessToken + "&comment_text="  + URLEncoder.encode(i);

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
                    if (status.equals("success")&&group_details_text_view!=null) {
                        group_details_text_view.setText("");
                       // Toast.makeText(Proceed_Feed_FullScreen.this, "Group Comment has been save successfully.", Toast.LENGTH_SHORT).show();
                        callGetCommentList(feed_id);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                   /* Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();*/
                }
                // hidepDialog();
            }
        }, new ErrorListener() {

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


    public void callGetCommentList(final String feed_id) {

        db.deleteAllFeedCommentData();

        //String urlJsonObj = AppConfiguration.MAIN_URL + "/getGroupsComments/" + GroupId;
        String tag_string_req="req_register";
        String urlJsonObj = AppConfig.Base_Url+AppConfig.App_api+ "getFeedsComments?feed_id=" + feed_id;

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                urlJsonObj, new Response.Listener<String>(){

            // Takes the response from the JSON request
            @Override
            public void onResponse(String response) {
                try {
                    swipeRefreshLayout.setRefreshing(false);
                    ArrayList<CommentData> commentArrayList = new ArrayList<CommentData>();

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    String response1 = jsonObject.getString("response");
                    Log.d("Re[spnsere"," "+response1);
                    Log.d("Re[spnsere"," "+status);


                    JSONArray jsonArray = jsonObject.getJSONArray("response");

                   // new LoadDBfromAPI().execute(response);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            CommentData groupData = new CommentData();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String a= jsonObject1.getString("commentText");
                            Log.d("CTadhkacka"," "+a);
                            groupData.setCommentText(jsonObject1.getString("commentText"));
                            groupData.setCommentName(jsonObject1.getString("commentByName"));
                            groupData.setCommentId(jsonObject1.getInt("commentId"));

                            dataTime1 = jsonObject1.getString("commentDate");//.split(" ")[1].replace(".0", "");
                            Log.d("dataTimePFF"," "+dataTime1);

                              String str = jsonArray.getString(i);
                            Log.d("444feed", str);
                            String userImageUrl=jsonObject1.getString("userImageUrl");
                            // String userImageUrl1=groupData.getUserImageUrl().toString();
                            //.split(" ")[1].replace(".0", "");

                           // Log.d("dataimageurlfeed1111"," "+userImageUrl);


                            JSONObject jsonObject2=new JSONObject(userImageUrl);
                            groupData.setUserImageUrl(jsonObject2.getString("userImageUrl"));
                            String image=groupData.getUserImageUrl().toString();
                            Log.d("imageuserfeed"," "+image);
                            Log.d("imageuserfeed11"," "+userImageUrl);

                            try {
                                groupData.setComment_date(getDate(Long.parseLong(dataTime1)));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            commentArrayList.add(groupData);

                            //////////////////////////////LOCAL DB//////////////////////////////////////

                            boolean isInserted = db.insertFeedComments(groupData,feed_id);
                            if (isInserted == true) {

                                //Toast.makeText(AllTransactionSummary.this, "Data  Inserted", Toast.LENGTH_SHORT).show();

                                Log.d("PFF", "LocalDBInIfPart" + isInserted);

                            } else {
                                Log.d("PFF", "LocalDBInElsePart" + isInserted);
                                //Toast.makeText(AllTransactionSummary.this, "Data  Not Inserted", Toast.LENGTH_SHORT).show();
                            }



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
                        /*error.printStackTrace();

                        Log.e("Feed", "FeedError");*/

                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                //Toast.makeText(Proceed_Feed_FullScreen.this, "error1!", Toast.LENGTH_SHORT).show();

                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                // Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);
                            } catch (UnsupportedEncodingException e1) {
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                               // Toast.makeText(Proceed_Feed_FullScreen.this, "error2!", Toast.LENGTH_SHORT).show();

                            } catch (JSONException e2) {
                                //Toast.makeText(Proceed_Feed_FullScreen.this, "error3!", Toast.LENGTH_SHORT).show();

                                // returned data is not JSONObject?
                                e2.printStackTrace();
                            }
                        }
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
                     dataTime = colorObj.getString("commentDate");//.split(" ")[1].replace(".0", "");
                    groupData.setComment_date(dataTime);

                    Log.d("ARCdatatime",""+dataTime);

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
            Log.d("ARcOnPReexecute","----");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            swipeRefreshLayout.setRefreshing(false);
             Log.d("ARcOnPReexecute","----");
            loadCommentListView(arrayList);
        }


    }
    private String getDate(long time) throws ParseException {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd/MM HH:mm", cal).toString();
         date1 = DateFormat.format("dd/MM ", cal).toString();
        Log.d("date11",""+date1);
        return date;
        }

    @Override
    public void onRefresh() {
        callGetCommentList(feed_id);
    }
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences(Proceed_Feed_FullScreen.MySharedPrefProceedFeedFullScreen, 0);
        preferences.edit().remove("ts").commit();
        // Store our shared preference
        SharedPreferences.Editor editor = getSharedPreferences("OURINFO",MODE_PRIVATE).edit();

       /* SharedPreferences sp = getSharedPreferences("OURINFO", MODE_PRIVATE);
        Editor ed = sp.edit();
       */ editor.putBoolean("active", true);
        Log.d("ARCOnStartFeed","----");
        editor.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Date object
        Date date= new Date();
        //getTime() returns current time in milliseconds
        long time = date.getTime();
        //Passed the milliseconds to constructor of Timestamp class
        ts = new Timestamp(time);
        ts1= String.valueOf(ts);
        System.out.println("Current Time Stamp: "+ts);
        Log.d("ARCTimeFeed",""+time);
        Log.d("ARCTimeFeedts1",""+ts1);

        // Store our shared preference
        SharedPreferences sp = getSharedPreferences(MySharedPrefProceedFeedFullScreen, MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("active", false);
        ed.putString("ts", String.valueOf(Long.valueOf(time)));
        Log.d("ARCTimeFeedts111",""+String.valueOf(ts));
        Log.d("ARCOnStopFeed","--"+String.valueOf(Long.valueOf(time)));
        ed.commit();
         }

    @Override
    protected void onResume() {
        super.onResume();

        // Store our shared preference
        SharedPreferences sp = getSharedPreferences("OURINFO", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("active", false);
        Log.d("ARCOnResumeFeed" ,"----");
        ed.commit();

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


