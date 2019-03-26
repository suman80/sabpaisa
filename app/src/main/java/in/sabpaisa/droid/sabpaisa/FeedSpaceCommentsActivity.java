package in.sabpaisa.droid.sabpaisa;

import android.Manifest;
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
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import at.markushi.ui.CircleButton;
import in.sabpaisa.droid.sabpaisa.AppDB.NotificationDB;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;
import in.sabpaisa.droid.sabpaisa.Util.SkipClientDetailsScreen;
import in.sabpaisa.droid.sabpaisa.Util.VolleyMultipartRequest;

import static in.sabpaisa.droid.sabpaisa.MainActivitySkip.SUPER_ADMIN_SHAREDFREF;

public class FeedSpaceCommentsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    EditText group_details_text_view;
    String dataTime1;
    String value;
    String i;
    ArrayList<CommentData> arrayList;
    String commentText;
    String date1;
    String   response, userAccessToken;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rv;
    Toolbar toolbar;
    ScrollView scrollView;
    ArrayList<CommentData> commentArrayList;
    int count = 1;
    ProgressBar progress;
    SpinKitView spin_kit;
    CircleButton imageView2;
    FrameLayout shareViewFrameLayout, ImageViewFrameLayout, DocViewFrameLayout;
    ImageView attachmentFile, selectedImg, closeSelectedImage, selectedDoc, closeSelectedDoc, attachment_Image_Video_File;
    TextView selectedDocName;
    LinearLayout shareDocument, shareImage;
    String userImageUrl;
    Bitmap commentFile;
    File fileDoc;
    String memberGroupRole;

    //Getting Values from feedskipfrag
    public static String feedId,feedName,feedText,feedLogo,feedImage;

    NotificationDB notificationDB;

    BroadcastReceiver broadcastReceiver;

    String roleValue;

    public static String SHARED_PREF_FEED_ID_VALUE = "sharedPrefFeedID";
    public static String appCid ;
    public static boolean notificationFlag ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_space_comments);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Getting Values from feedskipfrag
        feedId = getIntent().getStringExtra("feedId");
        feedName = getIntent().getStringExtra("feedName");
        feedText = getIntent().getStringExtra("feedText");
        feedLogo = getIntent().getStringExtra("feedLogo");
        feedImage = getIntent().getStringExtra("feedImage");

        if (getIntent().getStringExtra("appCid")!=null) {

            appCid = getIntent().getStringExtra("appCid");
            notificationFlag = getIntent().getBooleanExtra("FIREBASE_NOTI_FLAG",false);
            Log.d("FSCA123","appCid_Notification__"+appCid);
            Log.d("FSCA123","notificationFlag"+notificationFlag);
        }

        Log.d("FSCA","recieved_feedId__"+feedId);

        /*if (GroupSpaceCommentActivity.appCid != null){
            GroupSpaceCommentActivity.appCid = null;
        }*/

        Proceed_Feed_FullScreen.clientId=null;
        Proceed_Group_FullScreen.clientId=null;


        commentArrayList = new ArrayList<CommentData>();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);

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

        scrollView = (ScrollView) findViewById(R.id.scrollView);

        progress = (ProgressBar) findViewById(R.id.progress);

        spin_kit = (SpinKitView) findViewById(R.id.spin_kit);
        imageView2 = (CircleButton) findViewById(R.id.imageView2);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(feedName);
        setSupportActionBar(toolbar);

        arrayList = new ArrayList<>();
        toolbar.setNavigationIcon(R.drawable.ic_action_previousback);

        rv = (RecyclerView) findViewById(R.id.recycler_view_feed_details_comment);

        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        SkipClientDetailsScreen.isFragmentOpen = true;

                        boolean isUpdated = notificationDB.updateFeedNotificationData(feedId,0,0, System.currentTimeMillis(),false);

                        if (isUpdated == true){
                            Log.d("FSC_NotiDB","Updated "+isUpdated);
                        }else {
                            Log.d("FSC_NotiDB","NotUpdated "+isUpdated);
                        }

                        onBackPressed();
                        //Toast.makeText(MainActivity.this, "clicking the toolbar!", Toast.LENGTH_SHORT).show();
                    }
                }

        );


        //User Access Token

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        response = sharedPreferences.getString("response", "123");

        userAccessToken = response;

        if (getIntent().getStringExtra("userAccessTokenFromNotification") != null) {
            userAccessToken = getIntent().getStringExtra("userAccessTokenFromNotification");
        }

        if(userAccessToken == null || userAccessToken.equals("123")) {
            Intent intent = new Intent(FeedSpaceCommentsActivity.this, LogInActivity.class);
            startActivity(intent);
        }


        SharedPreferences sharedPreferencesRole = getApplicationContext().getSharedPreferences(SUPER_ADMIN_SHAREDFREF, Context.MODE_PRIVATE);

        roleValue = sharedPreferencesRole.getString("ROLE_VALUE", "abc");



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
                Toast.makeText(FeedSpaceCommentsActivity.this, "Documents", Toast.LENGTH_SHORT).show();

                if (isStoragePermissionGranted()) {
                    showFileChooser();
                } else {
                    Toast.makeText(FeedSpaceCommentsActivity.this, "Permission Denied !", Toast.LENGTH_SHORT).show();
                }

            }
        });

        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FeedSpaceCommentsActivity.this, "Pick Image", Toast.LENGTH_SHORT).show();
                pickImage();
            }
        });


        attachment_Image_Video_File.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //todo camera integration
                Intent intent = new Intent(FeedSpaceCommentsActivity.this, DisplayActivity.class);
                intent.putExtra("CAMVALUE", 3);
                intent.putExtra("feedId", feedId);
                intent.putExtra("feedName", feedName);
                intent.putExtra("feedText", feedText);
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

//Api Call
        if (isOnline()) {
            progress.setVisibility(View.VISIBLE);

            if (PrivateGroupFeedSpace.FLAG != null){
                callGetCommentListForPrivateFeeds(feedId,userAccessToken);
            }else {
                callGetCommentList(feedId);
            }


            }else {
            //Offline feature
            Toast.makeText(FeedSpaceCommentsActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
        }

        if (PrivateGroupFeedSpace.FLAG == null){
            GroupSpaceCommentActivity.memberGroupRole = null;
        }

        memberGroupRole = GroupSpaceCommentActivity.memberGroupRole;

        Log.d("PFF_memberGroupRole","After"+Proceed_Group_FullScreen.memberGroupRole);



        notificationDB = new NotificationDB(FeedSpaceCommentsActivity.this);

        ///////////////////////////////////////////////////////////////////////////////////////

        SkipClientDetailsScreen.isFragmentOpen = false;

        boolean isUpdated = notificationDB.updateFeedNotificationData(feedId,0,0, System.currentTimeMillis(),true);
        if (isUpdated == true){
            Log.d("FSC_Notification","Updated "+isUpdated);
        }else {
            Log.d("FSC_Notification","NotUpdated "+isUpdated);
        }


        SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREF_FEED_ID_VALUE, MODE_PRIVATE).edit();
        editor.putString("feedId", feedId);
        editor.apply();

        SharedPreferences.Editor editor1 = getSharedPreferences(GroupSpaceCommentActivity.SHARED_PREF_GROUP_ID_VALUE, MODE_PRIVATE).edit();
        editor1.clear();
        editor1.commit();

    }



    @Override
    protected void onResume() {
        super.onResume();

        // UI Update Code

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
                feedId = feedId;
                Log.d("PFF_FEED","broadcastVal__"+feedId +" RR "+feedId);


                if (PrivateGroupFeedSpace.FLAG != null){
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
        LocalBroadcastManager.getInstance(FeedSpaceCommentsActivity.this).registerReceiver(broadcastReceiver,new IntentFilter(ConstantsForUIUpdates.FEED_UI));


    }



    protected void loadCommentListView(ArrayList<CommentData> arrayList) {
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
            AlertDialog.Builder builder = new AlertDialog.Builder(FeedSpaceCommentsActivity.this);
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
            callCommentService(feedId, userAccessToken, commentText);
        } else if (i.equals("&"))

        {
            commentText.replace("&", "%26");
            Log.e("ctctcc ", "CommentData2 " + commentText);
            callCommentService(feedId, userAccessToken, commentText);

        } else if (i.trim().length() > 1999) {
            AlertDialog.Builder builder = new AlertDialog.Builder(FeedSpaceCommentsActivity.this);
            builder.setTitle("Comment");
            builder.setMessage("Hey ,It looks like you exceeded the text limit");
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
            callCommentService(feedId, userAccessToken, commentText);
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
                    Toast.makeText(FeedSpaceCommentsActivity.this, "Invalid File Format", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(FeedSpaceCommentsActivity.this, "Invalid File Format", Toast.LENGTH_SHORT).show();
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
        Log.d("FSCA", "IMG_userAccessToken" + userAccessToken);
        //our custom volley request

        Log.d("comment_text <<<<<<<<< ", i + " <<<< " + URLEncoder.encode(i));
        String url = AppConfig.Base_Url + AppConfig.App_api + "addFeedsComments?feed_id=" + feed_id + "&userAccessToken=" + userAccessToken + "&comment_text=" + URLEncoder.encode(i.trim());

        Log.d("FSCA", "URL__" + url);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d("FSCA", "Response__" + response);
                        try {

                            JSONObject obj = new JSONObject(new String(response.data));

                            Log.d("FSCA", "Object_Res__" + obj);

                            final String status = obj.getString("status");

                            if (status.equals("success")) {

                                Log.d("FSCA_Comment", "Success");


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


                                if (PrivateGroupFeedSpace.FLAG != null){
                                    callGetCommentListForPrivateFeeds(feed_id,userAccessToken);
                                }else {
                                    callGetCommentList(feed_id);
                                }



                            } else {

                                imageView2.setVisibility(View.VISIBLE);
                                spin_kit.setVisibility(View.GONE);

                                Log.d("FSCA_Comment", "Failed");
                                //Toast.makeText(Proceed_Feed_FullScreen.this, "Image Upload Failed !", Toast.LENGTH_SHORT).show();
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


        String tag_string_req = "req_register";

        String urlJsonObj = AppConfig.Base_Url + AppConfig.App_api + "/getPageFeedsComments?feed_id=" + feed_id + "&pageNo=" + count + "&rowLimit=25";

        Log.d("callGetCommentList_","url:__"+urlJsonObj);
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
                    Log.d("callGetCommentList_", "Resp___" + response1);
                    Log.d("callGetCommentList_", "Status___" + status);

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


                        }

                        /////Added By RAJ/////////
                        Log.d("SIZZZZZZZZZZZZZZZZ : ", commentArrayList.size() + "");
                        //if(count==1)
                        /////////////////

                        loadCommentListView(commentArrayList);
                    } else {
                        progress.setVisibility(View.GONE);

                        if (count > 1){
                            Toast.makeText(FeedSpaceCommentsActivity.this, "No More Record Found !", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Toast.makeText(FeedSpaceCommentsActivity.this, response1, Toast.LENGTH_SHORT).show();
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



        String tag_string_req = "req_register";

        String urlJsonObj = AppConfig.Base_Url + AppConfig.App_api + "/getPageFeedsComments?feed_id=" + feed_id + "&pageNo=" + count + "&rowLimit=25"+"&token="+userAccessToken;

        Log.d("urlComment__Feed__", urlJsonObj);
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

                        }

                        loadCommentListView(commentArrayList);
                    } else if (status.equals("failed")) {

                        progress.setVisibility(View.GONE);

                        if (response1 != null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(FeedSpaceCommentsActivity.this);
                            builder.setTitle("Comment Service");
                            builder.setMessage(response1.toString());
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                /*Intent feedUiRefresh = new Intent(ConstantsForUIUpdates.REFRESH_FEED_FRAGMENT);
                                LocalBroadcastManager.getInstance(Proceed_Feed_FullScreen.this).sendBroadcast(feedUiRefresh);*/

                                    Intent intent = new Intent(FeedSpaceCommentsActivity.this, PrivateGroupFeedSpace.class);
                                    intent.putExtra("memberGroupRole", PrivateGroupFeedSpace.memberGroupRole);
                                    intent.putExtra("GroupId", PrivateGroupFeedSpace.GroupId);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    startActivity(intent);

                                    SkipClientDetailsScreen.isFragmentOpen = true;

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
                            Toast.makeText(FeedSpaceCommentsActivity.this, "No More Record Found !", Toast.LENGTH_SHORT).show();

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
            callGetCommentList(feedId);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(FeedSpaceCommentsActivity.this, "Seems that you are not connected to the internet \n Please connect your internet to load more data", Toast.LENGTH_SHORT).show();
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
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.feed_space_menu, menu);

        MenuItem menuItem4 = menu.findItem(R.id.editFeedSpaceMenu);

        if (!notificationFlag) {

            if (roleValue.equals("1") || (memberGroupRole != null && memberGroupRole.equals("2"))) {
                menuItem4.setVisible(true);
            } else {
                menuItem4.setVisible(false);
            }

        }else {
            menuItem4.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.editFeedSpaceMenu:

                //todo edit feed

                Intent intent = new Intent(FeedSpaceCommentsActivity.this, EditFeedSpace.class);
                intent.putExtra("feedName", feedName);
                intent.putExtra("feedText", feedText);
                intent.putExtra("feedLogo", feedLogo);
                intent.putExtra("feedId", feedId);
                startActivity(intent);
                return true;

            case R.id.privateFeedMembers:

                Intent intent1 = new Intent(FeedSpaceCommentsActivity.this, PrivateFeedMembersList.class);
                intent1.putExtra("feedId", feedId);
                startActivity(intent1);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if (shareViewFrameLayout.getVisibility() == View.VISIBLE) {
            shareViewFrameLayout.setVisibility(View.GONE);
        } else {


            SkipClientDetailsScreen.isFragmentOpen = true;

        boolean isUpdated = notificationDB.updateFeedNotificationData(feedId,0,0, System.currentTimeMillis(),false);

        if (isUpdated == true){
            Log.d("FSC_NotiDB","Updated "+isUpdated);
        }else {
            Log.d("FSC_NotiDB","NotUpdated "+isUpdated);
        }

        finish();

            notificationFlag = false;

    }

    }

}
