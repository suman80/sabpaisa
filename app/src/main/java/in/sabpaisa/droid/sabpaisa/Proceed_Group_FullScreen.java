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
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.braunster.androidchatsdk.firebaseplugin.firebase.BChatcatNetworkAdapter;
import com.braunster.chatsdk.Utils.helper.ChatSDKUiHelper;
import com.braunster.chatsdk.network.BNetworkManager;
import com.bumptech.glide.Glide;
import com.olive.upi.transport.model.lib.NameValuePair;
import com.parse.signpost.http.HttpResponse;
import com.rockerhieu.emojicon.EmojiconEditText;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import in.sabpaisa.droid.sabpaisa.Adapter.GroupsCommentOfflineAdapter;
import in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments;
import in.sabpaisa.droid.sabpaisa.Model.FeedCommentsOfflineModel;
import in.sabpaisa.droid.sabpaisa.Model.GroupsCommentOfflineModel;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.AppConfiguration;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;
import retrofit2.http.HTTP;


import java.sql.Timestamp;
public class Proceed_Group_FullScreen extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,View.OnKeyListener {

    TextView groupsName,group_description_details;
    ImageView groupImage;
    String encodedUrl;
    CommentsDB dbHelper;
    private int TOTAL_PAGES = 3;
    String date1;
    String i,Gts;
    Timestamp Groupts;
    public static String MySharedPRoceedGroupFullScreen="mySharedPrefForTime";

    String GroupsNm,GroupsDiscription,GroupsImg,GroupId,userAccessToken,response;
    private EndlessScrollListener scrollListener;
    ArrayList<CommentData> arrayList,feedArrayList;
    Button button1;
    SwipeRefreshLayout swipeRefreshLayout;

    View collapsingLayout;
    Toolbar toolbar;
    Button prvtfeeds;
    ScrollView scrollView;

    /////////Local Db//////////
    AppDbComments db;
    ArrayList<GroupsCommentOfflineModel> arrayListForOffline;

    ArrayList<CommentData> commentArrayList;
    int count = 1;
//Again//Again
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Emojiconize.activity(this).go();
        //  CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_proceed_group_full_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        collapsingLayout = findViewById(R.id.collapsingLayout);
//        this.getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        groupsName = (TextView) findViewById(R.id.groupsName);
        group_description_details = (TextView) findViewById(R.id.group_description_details);
        groupImage = (ImageView) findViewById(R.id.groupImage);
        button1 = (Button) findViewById(R.id.b1);
        prvtfeeds = (Button) findViewById(R.id.b2);
        // emojIcon = new EmojIconActions(this);\
        // This is used for the app custom toast and activity transition
        ChatSDKUiHelper.initDefault();

// Init the network manager
        BNetworkManager.init(getApplicationContext());

// Create a new adapter
        BChatcatNetworkAdapter adapter = new BChatcatNetworkAdapter(getApplicationContext());

// Set the adapter
        BNetworkManager.sharedManager().setNetworkAdapter(adapter);

        commentArrayList = new ArrayList<CommentData>();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Proceed_Group_FullScreen.this, NumberOfGroups.class);
                intent.putExtra("GroupId", GroupId);

                startActivity(intent);

            }
        });

        prvtfeeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Proceed_Group_FullScreen.this, PrivateGroupFeeds.class);
                intent.putExtra("GroupId", GroupId);
                startActivity(intent);
            }
        });
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        response = sharedPreferences.getString("response", "123");

        userAccessToken = response;

        Log.d("PGFAccessToken", " " + userAccessToken);

        Log.d("PGFResponse", " " + response);

        GroupId = getIntent().getStringExtra("groupId");
        GroupsNm = getIntent().getStringExtra("groupName");
        GroupsDiscription = getIntent().getStringExtra("groupText");
        GroupsImg = getIntent().getStringExtra("groupImage");
        Log.d("NamemPGFS", "" + GroupsNm);
        Log.d("DiscriptionPGFS", "" + GroupsDiscription);
        Log.d("GroupImgPGFS", "" + GroupsImg);
        Log.d("GroupID_PGFS", "" + GroupId);


        groupsName.setText(GroupsNm);
        group_description_details.setText(GroupsDiscription);
        // new DownloadImageTask(groupImage).execute(GroupsImg);

        Glide.with(getApplicationContext())
                .load(GroupsImg)
                .error(R.drawable.image_not_found)
                .into(groupImage);

        arrayList = new ArrayList<>();
        toolbar.setNavigationIcon(R.drawable.previousmoresmall);

        toolbar.setTitle(GroupsNm);
        toolbar.setTitleMargin(11, 11, 11, 11);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                        //Toast.makeText(MainActivity.this, "clicking the toolbar!", Toast.LENGTH_SHORT).show();
                    }
                }

        );

        ///////////////////////DB/////////////////////////////////
        db = new AppDbComments(Proceed_Group_FullScreen.this);


        if (isOnline()) {
            //API
            callGetCommentList(GroupId);
        } else {

            Log.d("ProceedGroupFullSCR", "No Internet");

            /////////////////////Retrive data from local DB///////////////////////////////

            Cursor res = db.getParticularGroupComments(GroupId);
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

                    GroupsCommentOfflineModel feedCommentsOfflineModel = new GroupsCommentOfflineModel();
                    feedCommentsOfflineModel.setGroupId(res.getString(1));
                    feedCommentsOfflineModel.setCommentId(res.getInt(2));
                    feedCommentsOfflineModel.setCommentText(res.getString(3));
                    feedCommentsOfflineModel.setCommentByName(res.getString(4));
                    feedCommentsOfflineModel.setCommentDate(res.getString(5));

                    arrayListForOffline.add(feedCommentsOfflineModel);


                }
                Log.d("GroupCommentsOffline", "-->" + stringBuffer);

                final RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view_feed_details_comment);
                final GroupsCommentOfflineAdapter ca = new GroupsCommentOfflineAdapter(arrayListForOffline);

                rv.setAdapter(ca);

                LinearLayoutManager llm = new LinearLayoutManager(this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);


                llm.setReverseLayout(true);
                scrollListener=new EndlessScrollListener(llm) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        callGetCommentList(GroupId);
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
                }, 10L);

            }else {
                Log.d("PGFLocalDb", "In Else Part");
                Toast.makeText(Proceed_Group_FullScreen.this, "No Data Found !", Toast.LENGTH_SHORT).show();
            }

        }

    }





    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {

        TextView responseText = (TextView) findViewById(R.id.responseText);
        EditText myEditText = (EditText) view;

        if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                keyCode == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

            if (!event.isShiftPressed()) {
                Log.v("AndroidEnterKeyActivity","Enter Key Pressed!");
                switch (view.getId()) {
                    case R.id.commentadd:
                        responseText
                                .setText("Just pressed the ENTER key, " +
                                        "focus was on Text Box1. " +
                                        "You typed:\n" + myEditText.getText());
                        break;
                    /*case R.id.editText2:
                        responseText
                                .setText("Just pressed the ENTER key, " +
                                        "focus was on Text Box2. " +
                                        "You typed:\n" + myEditText.getText());
                        break;*/
                }
                return true;
            }

        }
        return false; // pass on to other listeners.

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
//        scrollListener=new EndlessScrollListener(llm) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount) {
//                callGetCommentList(GroupId);
//            }
//        };
//        rv.addOnScrollListener(scrollListener);
        rv.addItemDecoration(new SimpleDividerItemDecoration(this));
        rv.setLayoutManager(llm);
        rv.setNestedScrollingEnabled(false);
//        scrollView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //replace this line to scroll up or down
//                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
//            }
//        }, 10L);
    }

    //EditText group_details_text_view = null;

    EditText group_details_text_view = null;

    public void onClickSendComment(View view) {
        group_details_text_view = (EditText) findViewById(R.id.commentadd);


        StringEscapeUtils.escapeJava(group_details_text_view.getText().toString());




        EditText etEmojiEditText = new EditText(this);
        etEmojiEditText.setText("TYPE SOMETHING IN EMOJI");

        String toServer = String.valueOf(group_details_text_view.getText());
        String toServerUnicodeEncoded = StringEscapeUtils.escapeJava(toServer);

        String serverResponse = "SOME RESPONSE FROM SERVER WITH UNICODE CHARACTERS";
        String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(serverResponse);

        String commentText = group_details_text_view.getText().toString();
        i=StringEscapeUtils.escapeJava(commentText);
        Log.d("commentText3","67667767 "+i);

        // showpDialog(view);
        if (i.trim().length()==0)
        {

            Log.d("commentText2"," "+commentText);
            AlertDialog.Builder builder =new AlertDialog.Builder(Proceed_Group_FullScreen.this);
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
            callCommentService(GroupId, userAccessToken, commentText);
        }


        else if(i.equals("&"))

        {
            commentText.replace("&", "%26");
            Log.e("ctctcc ", "CommentData2 " + commentText);
            callCommentService(GroupId, userAccessToken, commentText);

        }


        else if(i.length()>1999)
        {
            AlertDialog.Builder builder =new AlertDialog.Builder(Proceed_Group_FullScreen.this);
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
            //Toast.makeText(getApplicationContext(),"Hi Hello ",Toast.LENGTH_SHORT).show();

            callCommentService(GroupId, userAccessToken, commentText);
            Log.e("CommentDatafeeddetaida ", "CommentData1 " + commentText);
        }
        //callCommentService(GroupId, userAccessToken, commentText);
    }

    private void callCommentService(final String GroupId, final String userAccessToken, final String comment_text) {


        String urlJsonObj = AppConfig.Base_Url+AppConfig.App_api+ "/addGroupsComments?group_id=" + GroupId + "&userAccessToken=" + userAccessToken + "&comment_text=" +URLEncoder.encode(i)
                ;
        Log.d("242424", urlJsonObj);



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


                        ///////////RRRRRRRRRRRRRRRRRRRR?????????????????
                        if(response.getString("response").equals("Not A Member")){
                            Toast.makeText(getApplicationContext(),"You cannot able to comment because your request is in pending status",Toast.LENGTH_SHORT).show();
                        }
                        /////////////RRRRRRRRRRRRR??????????????????

                        group_details_text_view.setText("");
                        //Toast.makeText(Proceed_Group_FullScreen.this, "Group Comment has been save successfully.", Toast.LENGTH_SHORT).show();
                        callGetCommentList(GroupId);

                    }
                    else if (status.equals("failed"))
                    {
                        Toast.makeText(getApplicationContext(),"Please Join the Group",Toast.LENGTH_SHORT).show();
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




                /*VolleyLog.d("Group Details", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();*/
                // hide the progress dialog
                //hidepDialog();
            }





        });





        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    public void callGetCommentList(final String GropuId) {

        db.deleteAllGroupCommentData(GropuId);

        //String urlJsonObj = AppConfiguration.MAIN_URL + "/getGroupsComments/" + GroupId;
        String tag_string_req="req_register";
        //String urlJsonObj =AppConfig.Base_Url+AppConfig.App_api+ "getGroupsComments?group_id=" + GropuId;
        String urlJsonObj =AppConfig.Base_Url+AppConfig.App_api+ "getPageGroupsComments?group_id=" + GropuId+"&pageNo="+count+"&rowLimit=25";

        //StringEscapeUtils.unescapeJava(String text)


        //urlJsonObj.replaceAll("%", "%25");


        //////////////////////////////


/////////////////////////////////


        //urlJsonObj = urlJsonObj.trim().replace(" ", "%20");


        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                urlJsonObj, new Response.Listener<String>(){

            // Takes the response from the JSON request
            @Override
            public void onResponse(String response) {
                try {
                    swipeRefreshLayout.setRefreshing(false);


                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("jsonobject1",""+jsonObject);


                    //String status = jsonObject.getString("status");

                    // String response1 = jsonObject.getString("response");
                    ///////////////////////////////
                    JSONArray jsonArray = null;
                    Object obj = jsonObject.get("response");

                    if(obj instanceof JSONArray){
                        jsonArray = (JSONArray)obj;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            CommentData groupData = new CommentData();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            groupData.setCommentText(jsonObject1.getString("commentText"));

                            groupData.setCommentName(jsonObject1.getString("commentByName"));
                            groupData.setUserImageUrl(jsonObject1.getString("userImageUrl"));
                            String dataTime = jsonObject1.getString("commentDate");
                            groupData.setCommentId(jsonObject1.getInt("commentId"));


                            String str = jsonArray.getString(i);
                            Log.d("444", str);
                            String userImageUrl=jsonObject1.getString("userImageUrl");
                            //  String userImageUrl1=groupData.getUserImageUrl().toString();
                            //.split(" ")[1].replace(".0", "");

                            Log.d("dataTimePFF"," "+dataTime);
                            Log.d("dataimageurlgroup1111"," "+userImageUrl);


                            JSONObject jsonObject2=new JSONObject(userImageUrl);
                            groupData.setUserImageUrl(jsonObject2.getString("userImageUrl"));
                            String image=groupData.getUserImageUrl().toString();
                            Log.d("imageuser"," "+image);


                            // Log.d("dataimageurlgroup222222"," "+userImageUrl1);
                            try {
                                groupData.setComment_date(getDate(Long.parseLong(dataTime)));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            commentArrayList.add(groupData);

                            //////////////////////////////LOCAL DB//////////////////////////////////////

                            boolean isInserted = db.insertGroupComments(groupData,GropuId);
                            if (isInserted == true) {

                                //Toast.makeText(Proceed_Group_FullScreen.this, "Data  Inserted", Toast.LENGTH_SHORT).show();

                                Log.d("PGF_Scr", "LocalDBInIfPart" + isInserted);

                            } else {
                                Log.d("PGF_Scr", "LocalDBInElsePart" + isInserted);
                                //Toast.makeText(Proceed_Group_FullScreen.this, "Data  Not Inserted", Toast.LENGTH_SHORT).show();
                            }


/*
                            JSONArray jsonArray1 = jsonObject.getJSONArray("userImageUrl");
                            for (int j = 0; j < jsonArray.length(); j++) {

                                String str1 = jsonArray1.getString(j);
                                Log.d("1111111111111", str1);
                            }*/

                        }
                        loadCommentListView(commentArrayList);
                    }

                    else {
                        Toast.makeText(Proceed_Group_FullScreen.this,"No Record Found !",Toast.LENGTH_SHORT).show();
                    }

                    Log.d("PGF","  "+obj.toString());



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
                    // Handles erroronErrorResponses that occur due to Volley
                    public void onErrorResponse(VolleyError error) {

                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                // Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);
                            } catch (UnsupportedEncodingException e1) {
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                                // returned data is not JSONObject?
                                e2.printStackTrace();
                            }
                        }


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

     /*   Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm:ss.SS");
        String strDate = sdf.format(cal.getTime());
        Log.d("CurrentdateFormat: ","" + strDate);

        SimpleDateFormat sdf1 = new SimpleDateFormat();
        sdf1.applyPattern("dd/MM HH:mm");
        Date date = sdf1.parse(strDate);
        String string=sdf1.format(date);*/
      /*  Calendar cal = Calendar.getInstance(Locale.ENGLISH);
       // Calendar cal = Calendar.getInstance();

        cal.setTimeInMillis(time);
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS");
        String strDate = simpleDateFormat.format(cal.getTime());
        Date date = simpleDateFormat.parse(strDate);
        String string=simpleDateFormat.format(date);
        String date = DateFormat.format("DD:HH:mm", cal).toString();
        return date;*/
        //return string;
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd/MM HH:mm", cal).toString();
        date1 = DateFormat.format("dd/MM ", cal).toString();
        Log.d("date11",""+date1);
        return date;
    }

    @Override
    public void onRefresh() {
        count++;
        callGetCommentList(GroupId);
    }

/*public  void numberofgroupmember()
{
    Intent intent=new Intent(this,NumberOfGroups.class);
    startActivity(intent);
}*/



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































/*
public void privatefeeds(final String groupId)
{

    String Url=AppConfig.Base_Url+AppConfig.App_api+"privatefeed?groupId=";
    StringRequest stringRequest=new StringRequest(Request.Method.GET,Url+Groupid ,new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
           Log.d("Private_feeds",response);
        }

    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    });
}*/

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences preferences = getSharedPreferences(MySharedPRoceedGroupFullScreen, 0);
        preferences.edit().remove("Groupts").commit();

        // Store our shared preference
        SharedPreferences.Editor editor = getSharedPreferences("GroupTime",MODE_PRIVATE).edit();

       /* SharedPreferences sp = getSharedPreferences("OURINFO", MODE_PRIVATE);
        Editor ed = sp.edit();
       */ editor.putBoolean("active", true);
        Log.d("ARCOnStartgroup","----");
        editor.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();

        Date date= new Date();
        //getTime() returns current time in milliseconds
        long time = date.getTime();
        //Passed the milliseconds to constructor of Timestamp class
        Groupts = new Timestamp(time);
        Gts= String.valueOf(Groupts);
        System.out.println("Current Time Stamp: "+Gts);
        Log.d("ARCTimeGroup",""+time);
        Log.d("ARCTimeGroup",""+Gts);

        // Store our shared preference
        SharedPreferences sp = getSharedPreferences(MySharedPRoceedGroupFullScreen, MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("activeGroup", false);

        ed.putString("Groupts", String.valueOf(Long.valueOf(time)));
        Log.d("ARCTimeGroupts111",""+String.valueOf(Gts));
        Log.d("ARCOnStopGroup","--"+String.valueOf(Long.valueOf(time)));
        ed.commit();

      /*  // Store our shared preference
        SharedPreferences sp = getSharedPreferences(MySharedPRoceedGroupFullScreen, MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("active", false);
        ed.putString("Groupts", ts);
        Log.d("ARCOnStopGroup","--");

        ed.commit();
        Long tsLong = System.currentTimeMillis()/1000;
        ts = tsLong.toString();
        Log.d("ARCTimeGroup",""+ts);
*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Store our shared preference
        SharedPreferences sp = getSharedPreferences("OURINFO", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("active", false);
        Log.d("ARCOnResumeGroup" ,"----");

        ed.commit();

    }

}