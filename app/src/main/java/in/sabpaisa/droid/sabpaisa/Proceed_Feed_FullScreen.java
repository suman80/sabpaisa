package in.sabpaisa.droid.sabpaisa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.AppConfiguration;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;

public class Proceed_Feed_FullScreen extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    TextView feedsName, feed_description_details;
    ImageView feedImage;
    CommentsDB dbHelper;
    private int TOTAL_PAGES = 3;
    ArrayList<CommentData> arrayList;
    String commentText;
    String date1;
    String FeedsNm, feedsDiscription, feedImg, response, feed_id, userAccessToken;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rv;
    Toolbar toolbar;
    ScrollView scrollView;
    EndlessScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
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

        callGetCommentList(feed_id);
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

/*
    private void loadCommentListView(ArrayList<CommentData> arrayList) {
         rv = (RecyclerView) findViewById(R.id.recycler_view_group_details_comment);
        final CommentAdapter ca = new CommentAdapter(arrayList);
        rv.setAdapter(ca);
       // rv.addFocusables(onNavigateUp(),arrayList,);setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        //llm.setReverseLayout(false);
        llm.setStackFromEnd(true);
       // rv.smoothScrollBy(100,100);
        rv.setLayoutManager(llm);
        rv.addItemDecoration(new SimpleDividerItemDecoration(this));
*/
/*
        rv.addItemDecoration(new RecyclerView.ItemDecoration() {

            private int textSize = 50;
            private int groupSpacing = 100;
            private int itemsInGroup = 3;

            private Paint paint = new Paint();
            {
                paint.setTextSize(textSize);
            }


            @Override
            public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View view = parent.getChildAt(i);

                    int position = parent.getChildAdapterPosition(view);
                    if (position % itemsInGroup == 0) {
                       // c.drawText("Group " + (position / itemsInGroup + 1), view.getLeft(),
                         //       view.getTop() - groupSpacing / 2 + textSize / 3, paint);
                        c.drawText("Group " + date1, view.getLeft(),
                                view.getTop() - groupSpacing / 2 + textSize / 3, paint);
                    }
                }
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) % itemsInGroup == 0) {
                    outRect.set(0, groupSpacing, 0, 0);
                }
            }
        });*//*


        rv.setLayoutManager(llm);

        rv.smoothScrollToPosition(ca.getItemCount());
       rv.setNestedScrollingEnabled(false);

     */
/*   rv.post(new Runnable() {
            @Override
            public void run() {
                // Call smooth scroll
                rv.smoothScrollToPosition(ca.getItemCount() - 1);
            } });*//*

    }
*/


    //EditText group_details_text_view = null;



    EditText group_details_text_view ;

    public void onClickSendComment(View view) {
        group_details_text_view = (EditText) findViewById(R.id.commentadd);
        commentText = group_details_text_view.getText().toString();
//        rv.smoothScrollBy(100,100);

        /*if (group_details_text_view.trim().length()==0)*/
        if (commentText.trim().length()==0 )
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
        else if(commentText.trim().length()>1999)
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

/*
        if (commentText=="null")
            Log.d("commentText1"," "+commentText);
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

        }*/


        Log.d("commentText3"," "+commentText);

    }

    private void callCommentService(final String feed_id, final String userAccessToken, final String comment_text) {
        String urlJsonObj = AppConfig.Base_Url+AppConfig.App_api+ "addFeedsComments?feed_id=" + feed_id + "&userAccessToken=" + userAccessToken + "&comment_text=" + comment_text;

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
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                // hidepDialog();
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                /*VolleyLog.d("Group Details", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(Proceed_Feed_FullScreen.this, "error!", Toast.LENGTH_SHORT).show();
*/
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        Toast.makeText(Proceed_Feed_FullScreen.this, "error11!", Toast.LENGTH_SHORT).show();

                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                        Toast.makeText(Proceed_Feed_FullScreen.this, "error22!", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e2) {
                        Toast.makeText(Proceed_Feed_FullScreen.this, "error33!", Toast.LENGTH_SHORT).show();

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

                            String dataTime = jsonObject1.getString("commentDate");//.split(" ")[1].replace(".0", "");
                            Log.d("dataTimePFF"," "+dataTime);

                            /*String str = jsonArray.getString(i);
                            Log.d("444feed", str);
                            ///String userImageUrl=jsonObject1.getString("userImageUrl");
                             String userImageUrl1=groupData.getUserImageUrl().toString();
                            //.split(" ")[1].replace(".0", "");
                           // Log.d("dataimageurlfeed1111"," "+userImageUrl);


                            JSONObject jsonObject2=new JSONObject(userImageUrl1);
                            groupData.setUserImageUrl(jsonObject2.getString("userImageUrl"));
                            String image=groupData.getUserImageUrl().toString();
                            Log.d("imageuserfeed"," "+image);
                            Log.d("imageuserfeed11"," "+userImageUrl1);


*/

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
                                groupData.setComment_date(getDate(Long.parseLong(dataTime)));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
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
                        /*error.printStackTrace();

                        Log.e("Feed", "FeedError");*/

                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                Toast.makeText(Proceed_Feed_FullScreen.this, "error1!", Toast.LENGTH_SHORT).show();

                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                // Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);
                            } catch (UnsupportedEncodingException e1) {
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                                Toast.makeText(Proceed_Feed_FullScreen.this, "error2!", Toast.LENGTH_SHORT).show();

                            } catch (JSONException e2) {
                                Toast.makeText(Proceed_Feed_FullScreen.this, "error3!", Toast.LENGTH_SHORT).show();

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
    private String getDate(long time) throws ParseException {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd/MM HH:mm", cal).toString();
         date1 = DateFormat.format("dd/MM ", cal).toString();
        Log.d("date11",""+date1);
        return date;

       /* Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm:ss.SS");
        String strDate = sdf.format(cal.getTime());
        Log.d("CurrentdateFormat: ","" + strDate);

        SimpleDateFormat sdf1 = new SimpleDateFormat()
        Date d = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(date);

        Date date = sdf1.parse(strDate);
        String string=sdf1.format(date);
        return string;

      //  Calendar cal = Calendar.getInstance();
        sdf1.applyPattern("dd/MM HH:mm");

        cal.setTime(d);
        int month = cal.get(Calendar.MONTH);

        return month + 1;*/
    }

    @Override
    public void onRefresh() {
        callGetCommentList(feed_id);
    }




}


