package in.sabpaisa.droid.sabpaisa;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;

import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.SpinKitView;


import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import at.markushi.ui.CircleButton;
import in.sabpaisa.droid.sabpaisa.Adapter.FeedsCommentOfflineAdapter;
import in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments;
import in.sabpaisa.droid.sabpaisa.AppDB.NotificationDB;
import in.sabpaisa.droid.sabpaisa.Interfaces.NotificationInterface;
import in.sabpaisa.droid.sabpaisa.Model.FeedCommentsOfflineModel;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;

import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;
import in.sabpaisa.droid.sabpaisa.Util.VolleyMultipartRequest;

public class Proceed_Feed_FullScreen extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,NotificationInterface {

    TextView feedsName, feed_description_details;
    ImageView feedImage;
    EditText group_details_text_view;
    CommentsDB dbHelper;
    private int TOTAL_PAGES = 3;
    String ts1;
    String popup;
    Timestamp ts;

    String dataTime1;
    int mHour;
    String value;
    int mMinute;
    String i;
    ArrayList<CommentData> arrayList;
    String commentText;
    String date1;
    String   response, feed_id, userAccessToken;
    public static String FeedsNm , feedsDiscription , feedImg , feedLogo;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rv;
    Toolbar toolbar;
    String dataTime;
    ScrollView scrollView;
    EndlessScrollListener scrollListener;
    public static String MySharedPrefProceedFeedFullScreen = "mySharedPrefFortime";

    /////////Local Db//////////
    AppDbComments db;
    ArrayList<FeedCommentsOfflineModel> arrayListForOffline;

    /////////////////START : by RaJ/////////////////
    ArrayList<CommentData> commentArrayList;
    /////////////////END : by RaJ/////////////////
    boolean isScrolling = false;
    int currentItems, totalItems, scrolledOutItems;
    int count = 1;

    ProgressBar progress;
    SpinKitView spin_kit;
    //ImageView imageView2;
    CircleButton imageView2;

    FrameLayout shareViewFrameLayout, ImageViewFrameLayout, DocViewFrameLayout;
    ImageView attachmentFile, selectedImg, closeSelectedImage, selectedDoc, closeSelectedDoc, attachment_Image_Video_File;
    TextView selectedDocName;

    LinearLayout shareDocument, shareImage;

    String userImageUrl;

    Bitmap commentFile;

    File fileDoc;

    public static String MY_PREFS_FOR_FEED_ID = "mySharedPrefForFeedId";

    public static boolean notificationFlag;
    public static String clientId;

    String roleValue;

    BroadcastReceiver broadcastReceiver;

    NotificationDB notificationDB;

    String memberGroupRole;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_proceed__feed__full_screen);
        // this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


//        /////////////////////////////////
        commentArrayList = new ArrayList<CommentData>();
        /////////////////////////


        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        response = sharedPreferences.getString("response", "123");

        userAccessToken = response;

        Log.d("Activity:", " PFF ");
        Log.d("Activity:", " PFF_cid"+getIntent().getStringExtra("clientId"));
        if (getIntent().getStringExtra("userAccessTokenFromNotification") != null) {
            userAccessToken = getIntent().getStringExtra("userAccessTokenFromNotification");
        }

        if (getIntent().getStringExtra("clientId")!=null) {

            clientId = getIntent().getStringExtra("clientId");
            notificationFlag = getIntent().getBooleanExtra("FIREBASE_NOTI_FLAG",false);
            Log.d("PFF123_Feed","clientId_Val_Notification__"+clientId);
            Log.d("PFF123_Feed","notificationFlag"+notificationFlag);
        }

        /*if (Proceed_Group_FullScreen.clientId!=null){
            Proceed_Group_FullScreen.clientId = null;
        }*/

        FeedSpaceCommentsActivity.appCid = null;
        GroupSpaceCommentActivity.appCid = null;
        MainActivitySkip.AppDecideFlag = false;

        if(userAccessToken == null || userAccessToken.equals("123")) {
            Intent intent = new Intent(Proceed_Feed_FullScreen.this, LogInActivity.class);
            startActivity(intent);
        }


        SharedPreferences sharedPreferencesRole = getApplicationContext().getSharedPreferences(UIN.SHARED_PREF_FOR_CHECK_USER, Context.MODE_PRIVATE);

        roleValue = sharedPreferencesRole.getString("USER_ROLE", "abc");

        scrollView = (ScrollView) findViewById(R.id.scrollView);

        Log.d("AccessToken", " " + userAccessToken);

        Log.d("FFResponse", " " + response);

        progress = (ProgressBar) findViewById(R.id.progress);

        spin_kit = (SpinKitView) findViewById(R.id.spin_kit);
        //imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView2 = (CircleButton) findViewById(R.id.imageView2);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);

        FeedsNm = getIntent().getStringExtra("feedName");
        feedsDiscription = getIntent().getStringExtra("feedText");
        value = getIntent().getStringExtra("value");
        feedImg = getIntent().getStringExtra("feedImage");
        feedLogo = getIntent().getStringExtra("feedLogo");
        feed_id = getIntent().getStringExtra("feedId");
        popup = getIntent().getStringExtra("popup");
        //memberGroupRole = getIntent().getStringExtra("memberGroupRole");
        Log.d("FeedsID", "" + feed_id);
        Log.d("ValUeAT FEED", "" + popup);
        Log.d("FeedsNmPFF", "" + FeedsNm);
        Log.d("feedsDiscriptionPFF", "" + feedsDiscription);
        Log.d("feedImgPFF", "" + feedImg);
        //Log.d("memberGroupRole", "memberGroupRole_____ " + memberGroupRole);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(FeedsNm);
        setSupportActionBar(toolbar);

        /*feedsName.setText(FeedsNm);
        feed_description_details.setText(feedsDiscription);
        //new DownloadImageTask(feedImage).execute(feedImg);

        Glide.with(getApplicationContext())
                .load(feedImg)
                .error(R.drawable.image_not_found)
                .into(feedImage);
*/
        ///////////////////////DB/////////////////////////////////
        db = new AppDbComments(Proceed_Feed_FullScreen.this);


        Log.d("Feedidproceed", "" + feed_id);
        arrayList = new ArrayList<>();
        toolbar.setNavigationIcon(R.drawable.ic_action_previousback);

        rv = (RecyclerView) findViewById(R.id.recycler_view_feed_details_comment);

        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        MainFeedAdapter.isClicked=false;
                        FullViewOfClientsProceed.isFragmentOpen = true;
                        /*if (MainFeedAdapter.progressDialog.isShowing()){
                            MainFeedAdapter.progressDialog.dismiss();
                        }*/

                        boolean isUpdated = notificationDB.updateFeedNotificationData(feed_id,0,0, System.currentTimeMillis(),false);

                        if (isUpdated == true){
                            Log.d("PFF_NotiDB","Updated "+isUpdated);
                        }else {
                            Log.d("PFF_NotiDB","NotUpdated "+isUpdated);
                        }

                        onBackPressed();
                        //Toast.makeText(MainActivity.this, "clicking the toolbar!", Toast.LENGTH_SHORT).show();
                    }
                }

        );


        if (isOnline()) {
            progress.setVisibility(View.VISIBLE);


            if (PrivateGroupFeeds.FLAG != null){
                callGetCommentListForPrivateFeeds(feed_id,userAccessToken);
            }else {
                callGetCommentList(feed_id);
            }

            /*if (getIntent().getStringExtra("userAccessTokenFromNotification") != null) {
                //API Call HttpGet
                new getFeedComments().execute();
            }*/

        } else {

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

                final RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view_feed_details_comment);
                final FeedsCommentOfflineAdapter ca = new FeedsCommentOfflineAdapter(arrayListForOffline);
                rv.setAdapter(ca);

                LinearLayoutManager llm = new LinearLayoutManager(this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);

                llm.setReverseLayout(true);
                scrollListener = new EndlessScrollListener(llm) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        //callGetCommentList(feed_id);

                        if (PrivateGroupFeeds.FLAG != null){
                            callGetCommentListForPrivateFeeds(feed_id,userAccessToken);
                        }else {
                            callGetCommentList(feed_id);
                        }

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


            } else {
                Log.d("PGFLocalDb", "In Else Part");
                Toast.makeText(Proceed_Feed_FullScreen.this, "No Data Found !", Toast.LENGTH_SHORT).show();
            }

        }

        /////////////Code for file/image upload in comments///////////////////////////////////////////////////

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_FOR_FEED_ID, MODE_PRIVATE).edit();
        editor.putString("feedId", feed_id);
        editor.apply();

        SharedPreferences.Editor editor1 = getSharedPreferences(Proceed_Group_FullScreen.MY_PREFS_FOR_GROUP_ID, MODE_PRIVATE).edit();
        editor1.clear();
        editor1.commit();

        Log.d("editor1"," "+editor1);

        shareViewFrameLayout = (FrameLayout) findViewById(R.id.shareViewFrameLayout);
        attachmentFile = (ImageView) findViewById(R.id.attachmentFile);
        shareDocument = (LinearLayout) findViewById(R.id.shareDocument);
        shareImage = (LinearLayout) findViewById(R.id.shareImage);
        ImageViewFrameLayout = (FrameLayout) findViewById(R.id.ImageViewFrameLayout);
        DocViewFrameLayout = (FrameLayout) findViewById(R.id.DocViewFrameLayout);
        selectedImg = (ImageView) findViewById(R.id.selectedImg);
        selectedDoc = (ImageView) findViewById(R.id.selectedDoc);
        selectedDocName = (TextView) findViewById(R.id.selectedDocName);
        closeSelectedImage = (ImageView) findViewById(R.id.closeSelectedImage);
        closeSelectedDoc = (ImageView) findViewById(R.id.closeSelectedDoc);
        attachment_Image_Video_File = (ImageView) findViewById(R.id.attachment_Image_Video_File);

        attachmentFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (shareViewFrameLayout.getVisibility() == View.GONE) {
                    shareViewFrameLayout.setVisibility(View.VISIBLE);
                } else {
                    shareViewFrameLayout.setVisibility(View.GONE);
                }


            }
        });

        closeSelectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageViewFrameLayout.setVisibility(View.GONE);
                commentFile = null;

            }
        });


        closeSelectedDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocViewFrameLayout.setVisibility(View.GONE);
                fileDoc = null;

            }
        });


        shareDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Proceed_Feed_FullScreen.this, "Documents", Toast.LENGTH_SHORT).show();

                if (isStoragePermissionGranted()) {
                    showFileChooser();
                } else {
                    Toast.makeText(Proceed_Feed_FullScreen.this, "Permission Denied !", Toast.LENGTH_SHORT).show();
                }

            }
        });

        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Proceed_Feed_FullScreen.this, "Pick Image", Toast.LENGTH_SHORT).show();
                pickImage();
            }
        });


        attachment_Image_Video_File.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Proceed_Feed_FullScreen.this, DisplayActivity.class);
                intent.putExtra("CAMVALUE", 1);
                intent.putExtra("feedId", feed_id);
                intent.putExtra("feedName", FeedsNm);
                intent.putExtra("feedText", feedsDiscription);
                startActivity(intent);
                finish();

            }
        });

        shareViewFrameLayout.bringToFront();
        ImageViewFrameLayout.bringToFront();
        DocViewFrameLayout.bringToFront();

        group_details_text_view = (EditText) findViewById(R.id.commentadd);

        group_details_text_view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                shareViewFrameLayout.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //Log.d("PFF_memberGroupRole","Before"+Proceed_Group_FullScreen.memberGroupRole);

        if (PrivateGroupFeeds.FLAG == null){
            Proceed_Group_FullScreen.memberGroupRole = null;
        }

        memberGroupRole = Proceed_Group_FullScreen.memberGroupRole;

        Log.d("PFF_memberGroupRole","After"+Proceed_Group_FullScreen.memberGroupRole);


        FullViewOfClientsProceed.isFragmentOpen = false;



        notificationDB = new NotificationDB(Proceed_Feed_FullScreen.this);

        ///////////////////////////////////////////////////////////////////////////////////////

        boolean isUpdated = notificationDB.updateFeedNotificationData(feed_id,0,0, System.currentTimeMillis(),true);
        if (isUpdated == true){
            Log.d("PFF_Notification","Updated "+isUpdated);
        }else {
            Log.d("PFF_Notification","NotUpdated "+isUpdated);
        }




    }




    @Override
    protected void onResume() {
        super.onResume();


        // Update UI
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String feedId = intent.getStringExtra("FEED_ID");

//                ArrayList<CommentData> arr = new ArrayList<CommentData>();

                try{

                    JSONObject jsonObject = new JSONObject(intent.getStringExtra("FEED_JSON"));
                    Log.d("RecievedJSONData",""+jsonObject);
                    CommentData groupData = new CommentData();
                    groupData.setCommentText(jsonObject.getString("commentText"));
                    Log.d("RecievedBody",""+jsonObject.getString("commentText"));
                    groupData.setCommentName(jsonObject.getString("commentByName"));
                    groupData.setCommentImage(jsonObject.getString("filePath"));
                    Log.d("FILE_PATH_At_PFF1", " " + jsonObject.getString("filePath"));
                    groupData.setCommentId(jsonObject.getInt("commentId"));

                    dataTime1 = jsonObject.getString("commentDate");
                    Log.d("dataTimePFF1", " " + dataTime1);

                    /*String userImageUrl*/ JSONObject jsonObject2 = jsonObject.getJSONObject("userImageUrl")/*.getString("userImageUrl")*/;

//                    JSONObject jsonObject2 = new JSONObject(userImageUrl);
                    groupData.setUserId(jsonObject2.getString("userId"));
                    groupData.setUserImageUrl(jsonObject2.getString("userImageUrl"));

                    try {
                        groupData.setComment_date(getDate(Long.parseLong(dataTime1)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    /*arr.add(groupData);*/
//                    arr.addAll(commentArrayList);
                    commentArrayList.add(0, groupData);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                feed_id = feedId;
                Log.d("PFF_FEED","broadcastVal__"+feedId +" RR "+feed_id);

                //////////////////////////////////////////////////////////////////////////////////
//                    commentArrayList.clear();
                    //count = 1;
                //API

                //callGetCommentList(feed_id);

                if (PrivateGroupFeeds.FLAG != null){
                    //callGetCommentListForPrivateFeeds(feed_id,userAccessToken);
                    loadCommentListView(commentArrayList);
                }else {

//                    commentArrayList.addAll(arr);
                    Log.d("ARR  ", " "+commentArrayList.size());
                    loadCommentListView(commentArrayList);
//                    callGetCommentList(feed_id);
                }

            }
        };
        LocalBroadcastManager.getInstance(Proceed_Feed_FullScreen.this).registerReceiver(broadcastReceiver,new IntentFilter(ConstantsForUIUpdates.FEED_UI));

    }

    /*private*/protected void loadCommentListView(ArrayList<CommentData> arrayList) {
        final RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view_feed_details_comment);
        final CommentAdapter ca = new CommentAdapter(arrayList, getApplicationContext(), toolbar);
        rv.setAdapter(ca);

        final LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        llm.setReverseLayout(true);
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



        progress.setVisibility(View.GONE);

    }


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
        i = StringEscapeUtils.escapeJava(commentText.trim());
        Log.d("commentText3", "67667767 " + i + " "+commentText.trim().length());
//        rv.smoothScrollBy(100,100);

        if (i.trim().length() == 0 && commentFile == null && fileDoc == null) {

            Log.d("commentText2", " " + commentText);
            AlertDialog.Builder builder = new AlertDialog.Builder(Proceed_Feed_FullScreen.this);
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

        } else if (i.equals("%"))

        {
            commentText.replace("%", "%25");
            Log.e("ctctcc ", "CommentData" + commentText);
            callCommentService(feed_id, userAccessToken, commentText);
        } else if (i.equals("&"))

        {
            commentText.replace("&", "%26");
            Log.e("ctctcc ", "CommentData2 " + commentText);
            callCommentService(feed_id, userAccessToken, commentText);

        } else if (i.trim().length() > 1999) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Proceed_Feed_FullScreen.this);
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

            imageView2.setVisibility(View.GONE);
            spin_kit.setVisibility(View.VISIBLE);
            callCommentService(feed_id, userAccessToken, commentText);
            Log.e("CommentDatafeeddetaida ", "CommentData " + commentText);

        }

        Log.d("commentText3", " " + commentText);



    }


    public void pickImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("userImageUrl", userImageUrl);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 200);


    }


    private void showFileChooser() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    1);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == 200 && resultCode == RESULT_OK && data != null) {


                Uri selectedimg = data.getData();
                Log.d("SSSSselectedimg ", selectedimg + "");

                Log.d("SSSSSelectedFile ", selectedimg.getPath() + "");

                shareViewFrameLayout.setVisibility(View.GONE);
                ImageViewFrameLayout.setVisibility(View.VISIBLE);
                closeSelectedImage.setVisibility(View.VISIBLE);

                selectedImg.setImageBitmap(android.provider.MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));


                commentFile = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg);
                Log.d("STTTTTTS ", selectedimg + "");
                Log.d("ChkCommentFile ", commentFile + "");

                if (commentFile == null) {
                    shareViewFrameLayout.setVisibility(View.GONE);
                    ImageViewFrameLayout.setVisibility(View.GONE);
                    closeSelectedImage.setVisibility(View.GONE);
                    Toast.makeText(Proceed_Feed_FullScreen.this, "Invalid File Format", Toast.LENGTH_SHORT).show();
                }


            }


            if (requestCode == 1) {
                if (resultCode == Activity.RESULT_OK) {
                    /*Uri selectedFileURI = data.getData();
                    fileDoc = new File(selectedFileURI.getPath().toString());*/

                    Uri uri = data.getData();
                    Log.d("PFFfileDoc1", "File Uri: " + uri.toString());
                    // Get the path
                    String path = getPath(this, uri);
                    Log.d("PFFfileDoc2", "File Path: " + path);

                    fileDoc = new File(path);

                    /*Log.d("PFFfileDoc1", "File : " + selectedFileURI);
                    Log.d("PFFfileDoc2", "File : " + fileDoc);
                    Log.d("PFFfileDoc3", "File : " + fileDoc.getName());
                    Log.d("PFFfileDoc4", "File : " + fileDoc.getAbsolutePath());*/

                    if (fileDoc != null && !(checkFileExtension(fileDoc))) {
                        shareViewFrameLayout.setVisibility(View.GONE);
                        ImageViewFrameLayout.setVisibility(View.GONE);
                        closeSelectedDoc.setVisibility(View.GONE);
                        Toast.makeText(Proceed_Feed_FullScreen.this, "Invalid File Format", Toast.LENGTH_SHORT).show();
                        fileDoc = null;
                    } else {

                        shareViewFrameLayout.setVisibility(View.GONE);
                        DocViewFrameLayout.setVisibility(View.VISIBLE);
                        closeSelectedDoc.setVisibility(View.VISIBLE);
                        selectedDocName.setText(fileDoc.getName());

                    }
                }
            }


        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong : " + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

    }


    /**
     * The method is taking Bitmap as an argument
     * then it will return the byte[] array for the given bitmap
     * and we will send this array to the server
     * here we are using JPEG Compression with 80% quality
     * you can give quality between 0 to 100
     * 0 means worse quality
     * 100 means best quality
     */
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    protected void callCommentService(final String feed_id, final String userAccessToken, final String comment_text) {

        ImageViewFrameLayout.setVisibility(View.GONE);
        DocViewFrameLayout.setVisibility(View.GONE);
        shareViewFrameLayout.setVisibility(View.GONE);
        closeSelectedImage.setVisibility(View.GONE);
        closeSelectedDoc.setVisibility(View.GONE);
        Log.d("PFF", "IMG_userAccessToken" + userAccessToken);
        //our custom volley request

        Log.d("comment_text <<<<<<<<< ", i + " <<<< " + URLEncoder.encode(i));
        String url = AppConfig.Base_Url + AppConfig.App_api + "addFeedsComments?feed_id=" + feed_id + "&userAccessToken=" + userAccessToken + "&comment_text=" + URLEncoder.encode(i.trim());

        Log.d("URL_AT_PFF", " " + url);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d("PFF", "IMG_Res" + response);
                        try {

                            JSONObject obj = new JSONObject(new String(response.data));
                            Log.d("PFF", "IMG_Res" + obj);
                            final String status = obj.getString("status");
                            final String returnResp = obj.getString("response");

                            if (status.equals("success")) {

                                Log.d("Comment", "Success");


                                imageView2.setVisibility(View.VISIBLE);
                                spin_kit.setVisibility(View.GONE);

                                //group_details_text_view = (EditText) findViewById(R.id.commentadd);
                                group_details_text_view.setText("");

                                //Toast.makeText(Proceed_Feed_FullScreen.this, "Image Upload Success !", Toast.LENGTH_SHORT).show();

                                commentFile = null;
                                fileDoc = null;

                                commentArrayList.clear();
                                count = 1;
                                //callGetCommentList(feed_id);

                                if (PrivateGroupFeeds.FLAG != null){
                                    callGetCommentListForPrivateFeeds(feed_id,userAccessToken);
                                }else {
                                    callGetCommentList(feed_id);
                                }


                            } else {

                                imageView2.setVisibility(View.VISIBLE);
                                spin_kit.setVisibility(View.GONE);

                                Log.d("Comment", "Failed");
                                Toast.makeText(Proceed_Feed_FullScreen.this,returnResp , Toast.LENGTH_SHORT).show();
                                commentFile = null;
                                fileDoc = null;
                            }

                        } catch (JSONException e) {

                            imageView2.setVisibility(View.VISIBLE);
                            spin_kit.setVisibility(View.GONE);

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error In Upoload", error.toString());

                        imageView2.setVisibility(View.VISIBLE);
                        spin_kit.setVisibility(View.GONE);

                        //Toast.makeText(Proceed_Feed_FullScreen.this, "Image Upload Failed !", Toast.LENGTH_SHORT).show();
                    }
                }) {


            /** If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
           /* @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tags", tags);
                return params;
            }*/


            /** Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                String imagename = "SabPaisa_CommentImage";
                if (commentFile != null) {
                    params.put("commentFile", new DataPart(imagename + ".jpeg", getFileDataFromDrawable(commentFile)));

                    Log.d("Image_At_PFF", params.get("commentFile") + "");
                }

                if (fileDoc != null) {


                    byte[] fileContent = readBytesFromFile(fileDoc.getAbsolutePath());

                    if (fileContent == null || fileContent.length == 0)
                        //Toast.makeText(Proceed_Feed_FullScreen.this, "File Path not choose from file Manager", Toast.LENGTH_SHORT).show();

                        Log.d("Doc_At_PFF", " File_Path_not_choose");

                    params.put("commentFile", new DataPart(fileDoc.getName(), fileContent));

                    Log.d("Doc_At_PFF", params.get("commentFile") + "");

                }

                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }


    public void callGetCommentList(final String feed_id) {

        db.deleteAllFeedCommentData();

        String tag_string_req = "req_register";

        String urlJsonObj = AppConfig.Base_Url + AppConfig.App_api + "/getPageFeedsComments?feed_id=" + feed_id + "&pageNo=" + count + "&rowLimit=25";

        Log.d("urlJsonObj  : ", urlJsonObj);
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                urlJsonObj, new Response.Listener<String>() {

            // Takes the response from the JSON request
            @Override
            public void onResponse(String response) {
                Log.d("In_callGetCommentList", "RESPONSE");
                try {
                    swipeRefreshLayout.setRefreshing(false);

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    String response1 = jsonObject.getString("response");
                    Log.d("Re[spnsere", " " + response1);
                    Log.d("Re[spnsere", " " + status);

                    if (status.equals("success")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("response");


                        for (int i = 0; i < jsonArray.length(); i++) {
                            final CommentData groupData = new CommentData();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String a = jsonObject1.getString("commentText");
                            Log.d("CTadhkacka", " " + a);
                            groupData.setCommentText(jsonObject1.getString("commentText"));
                            groupData.setCommentName(jsonObject1.getString("commentByName"));
                            groupData.setCommentImage(jsonObject1.getString("filePath"));
                            Log.d("FILE_PATH_At_PFF", " " + jsonObject1.getString("filePath"));
                            groupData.setCommentId(jsonObject1.getInt("commentId"));

                            dataTime1 = jsonObject1.getString("commentDate");
                            Log.d("dataTimePFF", " " + dataTime1);

                            String str = jsonArray.getString(i);
                            Log.d("444feed", str);
                            String userImageUrl = jsonObject1.getString("userImageUrl");

                            JSONObject jsonObject2 = new JSONObject(userImageUrl);
                            groupData.setUserId(jsonObject2.getString("userId"));
                            groupData.setUserImageUrl(jsonObject2.getString("userImageUrl"));
                            String image = groupData.getUserImageUrl().toString();
                            Log.d("imageuserfeed", " " + image);
                            Log.d("imageuserfeed11", " " + userImageUrl);

                            try {
                                groupData.setComment_date(getDate(Long.parseLong(dataTime1)));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            commentArrayList.add(groupData);

                            //////////////////////////////LOCAL DB//////////////////////////////////////



                                boolean isInserted = db.insertFeedComments(groupData, feed_id);
                                if (isInserted == true) {

                                    //Toast.makeText(AllTransactionSummary.this, "Data  Inserted", Toast.LENGTH_SHORT).show();

                                    Log.d("PFF", "LocalDBInIfPart" + isInserted);

                                } else {
                                    Log.d("PFF", "LocalDBInElsePart" + isInserted);
                                    //Toast.makeText(AllTransactionSummary.this, "Data  Not Inserted", Toast.LENGTH_SHORT).show();
                                }

                                


                        }




                        /////Added By RAJ/////////
                        Log.d("SIZZZZZZZZZZZZZZZZ : ", commentArrayList.size() + "");
                        //if(count==1)
                        /////////////////

                        loadCommentListView(commentArrayList);
                    } else {
                        progress.setVisibility(View.GONE);

                        if (count > 1){
                            Toast.makeText(Proceed_Feed_FullScreen.this, "No More Record Found !", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Toast.makeText(Proceed_Feed_FullScreen.this, response1, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                // Try and catch are included to handle any errors due to JSON
                catch (JSONException e) {

                    Log.d("In_callGetCommentList", "Exception");
                    progress.setVisibility(View.GONE);

                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();

                }
            }
        },

                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {

                        Log.d("In_callGetCommentList", "EROORListener");
                        progress.setVisibility(View.GONE);
                        /*error.printStackTrace();

                        Log.e("Feed", "FeedError");*/

                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                //Toast.makeText(Proceed_Feed_FullScreen.this, "error1!", Toast.LENGTH_SHORT).show();

                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                // Now you can use any deserializer to make sense of data
                                Log.d("EROOR RES : ", res);
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

//        Log.d("TTTTTTT","ASASAS");
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);
    }





    public void callGetCommentListForPrivateFeeds(final String feed_id,final String userAccessToken) {

        db.deleteAllFeedCommentData();

        String tag_string_req = "req_register";

        String urlJsonObj = AppConfig.Base_Url + AppConfig.App_api + "/getPageFeedsComments?feed_id=" + feed_id + "&pageNo=" + count + "&rowLimit=25"+"&token="+userAccessToken;

        Log.d("urlJsonObj  : ", urlJsonObj);
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                urlJsonObj, new Response.Listener<String>() {

            // Takes the response from the JSON request
            @Override
            public void onResponse(String response) {
                Log.d("In_callGetCommentList", "RESPONSE");
                try {
                    swipeRefreshLayout.setRefreshing(false);

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    String response1 = jsonObject.getString("response");
                    Log.d("Re[spnsere", " " + response1);
                    Log.d("Re[spnsere", " " + status);

                    if (status.equals("success")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("response");


                        for (int i = 0; i < jsonArray.length(); i++) {
                            final CommentData groupData = new CommentData();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String a = jsonObject1.getString("commentText");
                            Log.d("CTadhkacka", " " + a);
                            groupData.setCommentText(jsonObject1.getString("commentText"));
                            groupData.setCommentName(jsonObject1.getString("commentByName"));
                            groupData.setCommentImage(jsonObject1.getString("filePath"));
                            Log.d("FILE_PATH_At_PFF", " " + jsonObject1.getString("filePath"));
                            groupData.setCommentId(jsonObject1.getInt("commentId"));

                            dataTime1 = jsonObject1.getString("commentDate");
                            Log.d("dataTimePFF", " " + dataTime1);

                            String str = jsonArray.getString(i);
                            Log.d("444feed", str);
                            String userImageUrl = jsonObject1.getString("userImageUrl");

                            JSONObject jsonObject2 = new JSONObject(userImageUrl);
                            groupData.setUserId(jsonObject2.getString("userId"));
                            groupData.setUserImageUrl(jsonObject2.getString("userImageUrl"));
                            String image = groupData.getUserImageUrl().toString();
                            Log.d("imageuserfeed", " " + image);
                            Log.d("imageuserfeed11", " " + userImageUrl);

                            try {
                                groupData.setComment_date(getDate(Long.parseLong(dataTime1)));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            commentArrayList.add(groupData);

                            //////////////////////////////LOCAL DB//////////////////////////////////////



                            boolean isInserted = db.insertFeedComments(groupData, feed_id);
                            if (isInserted == true) {

                                //Toast.makeText(AllTransactionSummary.this, "Data  Inserted", Toast.LENGTH_SHORT).show();

                                Log.d("PFF", "LocalDBInIfPart" + isInserted);

                            } else {
                                Log.d("PFF", "LocalDBInElsePart" + isInserted);
                                //Toast.makeText(AllTransactionSummary.this, "Data  Not Inserted", Toast.LENGTH_SHORT).show();
                            }




                        }




                        /////Added By RAJ/////////
                        Log.d("SIZZZZZZZZZZZZZZZZ : ", commentArrayList.size() + "");
                        //if(count==1)
                        /////////////////

                        loadCommentListView(commentArrayList);
                    } else if (status.equals("failure")) {

                        progress.setVisibility(View.GONE);

                        if (response1.equals("Not A Member") || response1.equals("Feed is deactivated")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Proceed_Feed_FullScreen.this);
                        builder.setTitle("Comment Service");
                        builder.setMessage(response1.toString());
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainFeedAdapter.isClicked = false;
                                MainGroupAdapter1.isClicked = false;

                                /*Intent feedUiRefresh = new Intent(ConstantsForUIUpdates.REFRESH_FEED_FRAGMENT);
                                LocalBroadcastManager.getInstance(Proceed_Feed_FullScreen.this).sendBroadcast(feedUiRefresh);*/

                                Intent intent = new Intent(Proceed_Feed_FullScreen.this, PrivateGroupFeeds.class);
                                intent.putExtra("memberGroupRole", PrivateGroupFeeds.memberGroupRole);
                                intent.putExtra("GroupId", PrivateGroupFeeds.GroupId);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                startActivity(intent);

                                FullViewOfClientsProceed.isFragmentOpen = true;

                                finish();
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        alertDialog.show();

                    }
                    }else {
                        progress.setVisibility(View.GONE);

                        if (count > 1){
                            progress.setVisibility(View.GONE);
                            Toast.makeText(Proceed_Feed_FullScreen.this, "No More Record Found !", Toast.LENGTH_SHORT).show();

                        }

                        //Toast.makeText(Proceed_Feed_FullScreen.this, "No Record Found !", Toast.LENGTH_SHORT).show();

                    }
                }
                // Try and catch are included to handle any errors due to JSON
                catch (JSONException e) {

                    Log.d("In_callGetCommentList", "Exception");
                    progress.setVisibility(View.GONE);

                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();

                }
            }
        },

                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {

                        Log.d("In_callGetCommentList", "EROORListener");
                        progress.setVisibility(View.GONE);
                        /*error.printStackTrace();

                        Log.e("Feed", "FeedError");*/

                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                //Toast.makeText(Proceed_Feed_FullScreen.this, "error1!", Toast.LENGTH_SHORT).show();

                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                // Now you can use any deserializer to make sense of data
                                Log.d("EROOR RES : ", res);
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

//        Log.d("TTTTTTT","ASASAS");
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

                    Log.d("ARCdatatime", "" + dataTime);

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
            Log.d("ARcOnPReexecute", "----");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            swipeRefreshLayout.setRefreshing(false);
            Log.d("ARcOnPReexecute", "----");
            loadCommentListView(arrayList);
        }


    }

    private String getDate(long time) throws ParseException {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd/MM HH:mm", cal).toString();
        date1 = DateFormat.format("dd/MM ", cal).toString();
        Log.d("date11", "" + date1);
        return date;
    }

    @Override
    public void onRefresh() {

        if (isOnline()) {
            count++;
            callGetCommentList(feed_id);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(Proceed_Feed_FullScreen.this, "Seems that you are not connected to the internet \n Please connect your internet to load more data", Toast.LENGTH_SHORT).show();
        }
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


    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    private static byte[] readBytesFromFile(String filePath) {

        Log.d("PFFfileDoc4", "filePathInRBFF " + filePath);

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {

            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return bytesArray;

    }


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("PFF", "Permission is granted");
                return true;
            } else {

                Log.v("PFF", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("PFF", "Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("PFF", "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
        }

    }


    public boolean checkFileExtension(File file) {
        if (file.getAbsolutePath().endsWith(".doc") || file.getAbsolutePath().endsWith(".docx")
                || file.getAbsolutePath().endsWith(".pdf") || file.getAbsolutePath().endsWith(".xls")
                || file.getAbsolutePath().endsWith(".xlsx")) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {

        if (shareViewFrameLayout.getVisibility() == View.VISIBLE) {
            shareViewFrameLayout.setVisibility(View.GONE);
        } else {

            MainFeedAdapter.isClicked=false;
            /*if (MainFeedAdapter.progressDialog.isShowing()){
                MainFeedAdapter.progressDialog.dismiss();
            }*/
            FullViewOfClientsProceed.isFragmentOpen = true;

            boolean isUpdated = notificationDB.updateFeedNotificationData(feed_id,0,0, System.currentTimeMillis(),false);

            if (isUpdated == true){
                Log.d("PFF_NotiDB","Updated "+isUpdated);
            }else {
                Log.d("PFF_NotiDB","NotUpdated "+isUpdated);
            }

            finish();

        }
    }


    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_menu, menu);

        MenuItem menuItem4 = menu.findItem(R.id.editFeedMenu);
        if (roleValue.equals("1") || ( memberGroupRole != null && memberGroupRole.equals("2"))) {
            menuItem4.setVisible(true);
        }else {
            menuItem4.setVisible(false);
        }

        MenuItem menuItem5 = menu.findItem(R.id.privateFeedMembers);
        if ((PrivateGroupFeeds.FLAG != null)) {
            menuItem5.setVisible(true);
        }else {
            menuItem5.setVisible(false);
        }

        MenuItem menuItem = menu.findItem(R.id.editGroupMenu);
        menuItem.setVisible(false);

        MenuItem menuItem1 = menu.findItem(R.id.groupMembers);
        menuItem1.setVisible(false);

        MenuItem menuItem2 = menu.findItem(R.id.groupFeeds);
        menuItem2.setVisible(false);

        MenuItem menuItem3 = menu.findItem(R.id.blockedUser);
        menuItem3.setVisible(false);

        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.editFeedMenu:

                Intent intent = new Intent(Proceed_Feed_FullScreen.this, EditFeed.class);
                intent.putExtra("feedName", FeedsNm);
                intent.putExtra("feedText", feedsDiscription);
                intent.putExtra("feedImage", feedImg);
                intent.putExtra("feedLogo", feedLogo);
                intent.putExtra("feedId", feed_id);
                startActivity(intent);
                return true;


            case R.id.privateFeedMembers:

                Intent intent1 = new Intent(Proceed_Feed_FullScreen.this, PrivateFeedMembersList.class);
                /*intent1.putExtra("feedName", FeedsNm);
                intent1.putExtra("feedText", feedsDiscription);
                intent1.putExtra("feedImage", feedImg);
                intent1.putExtra("feedLogo", feedLogo);*/
                intent1.putExtra("feedId", feed_id);
                startActivity(intent1);
                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void setMemberData(String notificationData) {

        Log.d("FeedNotification","setMemberData_ "+notificationData);

        if (notificationData != null) {




        }


    }




}

