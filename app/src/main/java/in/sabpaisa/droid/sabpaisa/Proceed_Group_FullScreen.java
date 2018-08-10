package in.sabpaisa.droid.sabpaisa;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.braunster.androidchatsdk.firebaseplugin.firebase.BChatcatNetworkAdapter;
import com.braunster.chatsdk.Utils.helper.ChatSDKUiHelper;
import com.braunster.chatsdk.network.BNetworkManager;
import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
import com.olive.upi.transport.model.lib.NameValuePair;
import com.parse.signpost.http.HttpResponse;
import com.rockerhieu.emojicon.EmojiconEditText;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.HashMap;
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
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;
import in.sabpaisa.droid.sabpaisa.Util.VolleyMultipartRequest;
import retrofit2.http.HTTP;


import java.sql.Timestamp;
import java.util.Map;

import static in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments.TABLE_FEED_COMMENTS;
import static in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments.TABLE_GROUP_COMMENTS;

public class Proceed_Group_FullScreen extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnKeyListener {

    public static String MySharedPRoceedGroupFullScreen = "mySharedPrefForTime";
    TextView groupsName, group_description_details;
    CommentsDB dbHelper;
    String date1;
    String i, Gts;
    Timestamp Groupts;
    String GroupsNm, GroupsDiscription, GroupsImg, GroupId, userAccessToken, response;
    ArrayList<CommentData> arrayList, feedArrayList;
    SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;
    ScrollView scrollView;
    /////////Local Db//////////
    AppDbComments db;
    ArrayList<GroupsCommentOfflineModel> arrayListForOffline;
    ArrayList<CommentData> commentArrayList;
    int count = 1;
    ProgressBar progress;
    SpinKitView spin_kit;
    ImageView imageView2;
    EditText group_details_text_view = null;
    private int TOTAL_PAGES = 3;
    private EndlessScrollListener scrollListener;


    FrameLayout shareViewFrameLayout,ImageViewFrameLayout,DocViewFrameLayout;
    ImageView attachmentFile,selectedImg,closeSelectedImage,selectedDoc,closeSelectedDoc;
    TextView selectedDocName;


    LinearLayout shareDocument,shareImage;

    String userImageUrl;

    Bitmap commentFile;

    File fileDoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Emojiconize.activity(this).go();
        //  CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_proceed_group_full_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        GroupId = getIntent().getStringExtra("groupId");
        GroupsNm = getIntent().getStringExtra("groupName");
        GroupsDiscription = getIntent().getStringExtra("groupText");
        GroupsImg = getIntent().getStringExtra("groupImage");
        Log.d("NamemPGFS", "" + GroupsNm);
        Log.d("DiscriptionPGFS", "" + GroupsDiscription);
        Log.d("GroupImgPGFS", "" + GroupsImg);
        Log.d("GroupID_PGFS", "" + GroupId);


        toolbar = (Toolbar) findViewById(R.id.toolbarPGF);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationIcon(R.drawable.previousmoresmall);
        toolbar.setTitleMargin(11, 11, 11, 11);
        getSupportActionBar().setTitle(GroupsNm);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                        //Toast.makeText(MainActivity.this, "clicking the toolbar!", Toast.LENGTH_SHORT).show();
                    }
                }

        );


        scrollView = (ScrollView) findViewById(R.id.scrollView);

        progress = (ProgressBar) findViewById(R.id.progress);

        spin_kit = (SpinKitView) findViewById(R.id.spin_kit);
        imageView2 = (ImageView) findViewById(R.id.imageView2);

        commentArrayList = new ArrayList<CommentData>();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);


       /* ChatSDKUiHelper.initDefault();

// Init the network manager
        BNetworkManager.init(getApplicationContext());

// Create a new adapter
        BChatcatNetworkAdapter adapter = new BChatcatNetworkAdapter(getApplicationContext());

// Set the adapter
        BNetworkManager.sharedManager().setNetworkAdapter(adapter);*/


        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        response = sharedPreferences.getString("response", "123");

        userAccessToken = response;

        Log.d("PGFAccessToken", " " + userAccessToken);

        Log.d("PGFResponse", " " + response);



        arrayList = new ArrayList<>();

        ///////////////////////DB/////////////////////////////////
        db = new AppDbComments(Proceed_Group_FullScreen.this);


        if (isOnline()) {
            progress.setVisibility(View.VISIBLE);
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
                scrollListener = new EndlessScrollListener(llm) {
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

            } else {
                Log.d("PGFLocalDb", "In Else Part");
                Toast.makeText(Proceed_Group_FullScreen.this, "No Data Found !", Toast.LENGTH_SHORT).show();
            }

        }


        /////////////Code for file/image upload in comments///////////////////////////////////////////////////
        shareViewFrameLayout = (FrameLayout) findViewById(R.id.shareViewFrameLayout);
        attachmentFile = (ImageView)findViewById(R.id.attachmentFile);
        shareDocument = (LinearLayout)findViewById(R.id.shareDocument);
        shareImage = (LinearLayout)findViewById(R.id.shareImage);
        ImageViewFrameLayout = (FrameLayout)findViewById(R.id.ImageViewFrameLayout);
        DocViewFrameLayout = (FrameLayout)findViewById(R.id.DocViewFrameLayout);
        selectedImg = (ImageView)findViewById(R.id.selectedImg);
        selectedDoc = (ImageView)findViewById(R.id.selectedDoc);
        selectedDocName = (TextView) findViewById(R.id.selectedDocName);
        closeSelectedImage = (ImageView)findViewById(R.id.closeSelectedImage);
        closeSelectedDoc = (ImageView)findViewById(R.id.closeSelectedDoc);

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
                Toast.makeText(Proceed_Group_FullScreen.this,"Documents",Toast.LENGTH_SHORT).show();

                if (isStoragePermissionGranted()){
                    showFileChooser();
                }else {
                    Toast.makeText(Proceed_Group_FullScreen.this,"Permission Denied !",Toast.LENGTH_SHORT).show();
                }

            }
        });

        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Proceed_Group_FullScreen.this,"Pick Image",Toast.LENGTH_SHORT).show();
                pickImage();
            }
        });


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
                Log.v("AndroidEnterKeyActivity", "Enter Key Pressed!");
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

    private void loadCommentListView(ArrayList<CommentData> arrayList) {
        final RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view_feed_details_comment);
        final CommentAdapter ca = new CommentAdapter(arrayList, getApplicationContext(),toolbar);

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
/*        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                //replace this line to scroll up or down
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 10L);

        */
        progress.setVisibility(View.GONE);
    }

    //EditText group_details_text_view = null;

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
        i = StringEscapeUtils.escapeJava(commentText);
        Log.d("commentText3", "67667767 " + i);

        // showpDialog(view);
        if (i.trim().length() == 0 && commentFile == null && fileDoc == null) {

            Log.d("commentText2", " " + commentText);
            AlertDialog.Builder builder = new AlertDialog.Builder(Proceed_Group_FullScreen.this);
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
            callCommentService(GroupId, userAccessToken, commentText);
        } else if (i.equals("&"))

        {
            commentText.replace("&", "%26");
            Log.e("ctctcc ", "CommentData2 " + commentText);
            callCommentService(GroupId, userAccessToken, commentText);

        } else if (i.length() > 1999) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Proceed_Group_FullScreen.this);
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

            imageView2.setVisibility(View.GONE);
            spin_kit.setVisibility(View.VISIBLE);

            callCommentService(GroupId, userAccessToken, commentText);
            Log.e("CommentDatafeeddetaida ", "CommentData1 " + commentText);
        }
        //callCommentService(GroupId, userAccessToken, commentText);
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
                Log.d("SSSS ", selectedimg+"");

                shareViewFrameLayout.setVisibility(View.GONE);
                ImageViewFrameLayout.setVisibility(View.VISIBLE);
                closeSelectedImage.setVisibility(View.VISIBLE);

                selectedImg.setImageBitmap(android.provider.MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));


                commentFile = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg);
                Log.d("STTTTTTS ", selectedimg+"");


            }


            if (requestCode == 1) {
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedFileURI = data.getData();
                    fileDoc = new File(selectedFileURI.getPath().toString());
                    Log.d("PFF", "File : " + fileDoc.getName());
//                uploadedFileName = file.getName().toString();
//                tokens = new StringTokenizer(uploadedFileName, ":");
//                first = tokens.nextToken();
//                file_1 = tokens.nextToken().trim();
//                txt_file_name_1.setText(file_1);

                    shareViewFrameLayout.setVisibility(View.GONE);
                    DocViewFrameLayout.setVisibility(View.VISIBLE);
                    closeSelectedDoc.setVisibility(View.VISIBLE);
                    selectedDocName.setText(fileDoc.getName());


                }
            }


        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong : "+e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

    }



    /** The method is taking Bitmap as an argument
     * then it will return the byte[] array for the given bitmap
     * and we will send this array to the server
     * here we are using JPEG Compression with 80% quality
     * you can give quality between 0 to 100
     * 0 means worse quality
     * 100 means best quality
     * */
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }




    private void callCommentService(final String GroupId, final String userAccessToken, final String comment_text) {

        ImageViewFrameLayout.setVisibility(View.GONE);
        DocViewFrameLayout.setVisibility(View.GONE);
        shareViewFrameLayout.setVisibility(View.GONE);
        closeSelectedImage.setVisibility(View.GONE);
        closeSelectedDoc.setVisibility(View.GONE);

        Log.d("PGF", "IMG_userAccessToken" + userAccessToken);
        //our custom volley request

        Log.d("comment_text <<<<<<<<< "  ,i+" <<<< "+  URLEncoder.encode(i));

        String url = AppConfig.Base_Url + AppConfig.App_api + "/addGroupsComments?group_id=" + GroupId + "&userAccessToken=" + userAccessToken + "&comment_text=" + URLEncoder.encode(i);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d("PGF", "IMG_Res" + response);


                        try {
                            // Parsing json object response
                            // response will be a json object
                            JSONObject obj = new JSONObject(new String(response.data));
                            String status = obj.getString("status");
                            if (status.equals("success")) {

                                imageView2.setVisibility(View.VISIBLE);
                                spin_kit.setVisibility(View.GONE);

                                commentFile = null;
                                fileDoc = null;

                                ///////////RRRRRRRRRRRRRRRRRRRR?????????????????
                                if (obj.getString("response").equals("Not A Member")) {
                                    Toast.makeText(getApplicationContext(), "You cannot able to comment because your request is in pending status", Toast.LENGTH_SHORT).show();
                                }
                                /////////////RRRRRRRRRRRRR??????????????????

                                group_details_text_view.setText("");
                                //Toast.makeText(Proceed_Group_FullScreen.this, "Group Comment has been save successfully.", Toast.LENGTH_SHORT).show();
                                commentArrayList.clear();
                                count = 1;
                                callGetCommentList(GroupId);

                            } else if (status.equals("failed")) {
                                commentFile = null;
                                fileDoc = null;
                                imageView2.setVisibility(View.VISIBLE);
                                spin_kit.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Please Join the Group", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: KASKADKASKDASKCNSKACKASNVKNASKANka" + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            imageView2.setVisibility(View.VISIBLE);
                            spin_kit.setVisibility(View.GONE);
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
                if(commentFile != null){
                    params.put("commentFile", new DataPart(imagename + ".jpeg", getFileDataFromDrawable(commentFile)));

                    Log.d("Image",params.get("commentFile")+"");
                }


                if (fileDoc != null){



                    byte [] fileContent = readBytesFromFile(fileDoc.getAbsolutePath());

                    params.put("commentFile", new DataPart(fileDoc.getName(),fileContent));



                }


                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);



    }

    public void callGetCommentList(final String GropuId) {


        boolean checkDb = db.isTableExists(TABLE_GROUP_COMMENTS);

        Log.d("DbValuePGF", " " + checkDb);

        if (checkDb == true) {
            db.deleteAllGroupCommentData(GropuId);
        }

        //String urlJsonObj = AppConfiguration.MAIN_URL + "/getGroupsComments/" + GroupId;
        String tag_string_req = "req_register";
        //String urlJsonObj =AppConfig.Base_Url+AppConfig.App_api+ "getGroupsComments?group_id=" + GropuId;
        String urlJsonObj = AppConfig.Base_Url + AppConfig.App_api + "getPageGroupsComments?group_id=" + GropuId + "&pageNo=" + count + "&rowLimit=25";


        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                urlJsonObj, new Response.Listener<String>() {

            // Takes the response from the JSON request
            @Override
            public void onResponse(String response) {
                try {
                    swipeRefreshLayout.setRefreshing(false);


                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("jsonobject1", "" + jsonObject);


                    //String status = jsonObject.getString("status");

                    // String response1 = jsonObject.getString("response");
                    ///////////////////////////////
                    JSONArray jsonArray = null;
                    Object obj = jsonObject.get("response");

                    if (obj instanceof JSONArray) {
                        jsonArray = (JSONArray) obj;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            CommentData groupData = new CommentData();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            groupData.setCommentText(jsonObject1.getString("commentText"));

                            groupData.setCommentName(jsonObject1.getString("commentByName"));
                            groupData.setUserImageUrl(jsonObject1.getString("userImageUrl"));
                            groupData.setCommentImage(jsonObject1.getString("filePath"));
                            String dataTime = jsonObject1.getString("commentDate");
                            groupData.setCommentId(jsonObject1.getInt("commentId"));


                            String str = jsonArray.getString(i);
                            Log.d("444", str);
                            String userImageUrl = jsonObject1.getString("userImageUrl");
                            //  String userImageUrl1=groupData.getUserImageUrl().toString();
                            //.split(" ")[1].replace(".0", "");

                            Log.d("dataTimePFF", " " + dataTime);
                            Log.d("dataimageurlgroup1111", " " + userImageUrl);


                            JSONObject jsonObject2 = new JSONObject(userImageUrl);
                            groupData.setUserImageUrl(jsonObject2.getString("userImageUrl"));
                            String image = groupData.getUserImageUrl().toString();
                            Log.d("imageuser", " " + image);


                            // Log.d("dataimageurlgroup222222"," "+userImageUrl1);
                            try {
                                groupData.setComment_date(getDate(Long.parseLong(dataTime)));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            commentArrayList.add(groupData);

                            //////////////////////////////LOCAL DB//////////////////////////////////////

                            boolean isInserted = db.insertGroupComments(groupData, GropuId);
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
                    } else {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(Proceed_Group_FullScreen.this, "No Record Found !", Toast.LENGTH_SHORT).show();
                    }

                    Log.d("PGF", "  " + obj.toString());


                }
                // Try and catch are included to handle any errors due to JSON
                catch (JSONException e) {
                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();
                    progress.setVisibility(View.GONE);
                }
            }
        },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles erroronErrorResponses that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        progress.setVisibility(View.GONE);
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
        Log.d("date11", "" + date1);
        return date;
    }

    @Override
    public void onRefresh() {

        if (isOnline()) {

            count++;
            callGetCommentList(GroupId);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(Proceed_Group_FullScreen.this, "Seems that you are not connected to the internet \n Please connect your internet to load more data", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences preferences = getSharedPreferences(MySharedPRoceedGroupFullScreen, 0);
        preferences.edit().remove("Groupts").commit();

        // Store our shared preference
        SharedPreferences.Editor editor = getSharedPreferences("GroupTime", MODE_PRIVATE).edit();

       /* SharedPreferences sp = getSharedPreferences("OURINFO", MODE_PRIVATE);
        Editor ed = sp.edit();
       */
        editor.putBoolean("active", true);
        Log.d("ARCOnStartgroup", "----");
        editor.commit();
    }

/*public  void numberofgroupmember()
{
    Intent intent=new Intent(this,NumberOfGroups.class);
    startActivity(intent);
}*/

    @Override
    protected void onStop() {
        super.onStop();

        Date date = new Date();
        //getTime() returns current time in milliseconds
        long time = date.getTime();
        //Passed the milliseconds to constructor of Timestamp class
        Groupts = new Timestamp(time);
        Gts = String.valueOf(Groupts);
        System.out.println("Current Time Stamp: " + Gts);
        Log.d("ARCTimeGroup", "" + time);
        Log.d("ARCTimeGroup", "" + Gts);

        // Store our shared preference
        SharedPreferences sp = getSharedPreferences(MySharedPRoceedGroupFullScreen, MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("activeGroup", false);

        ed.putString("Groupts", String.valueOf(Long.valueOf(time)));
        Log.d("ARCTimeGroupts111", "" + String.valueOf(Gts));
        Log.d("ARCOnStopGroup", "--" + String.valueOf(Long.valueOf(time)));
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
    protected void onResume() {
        super.onResume();

        // Store our shared preference
        SharedPreferences sp = getSharedPreferences("OURINFO", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("active", false);
        Log.d("ARCOnResumeGroup", "----");

        ed.commit();

    }

    //Code for fetching image from server
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loading.show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.groupMembers:
                Intent intent = new Intent(Proceed_Group_FullScreen.this, NumberOfGroups.class);
                intent.putExtra("GroupId", GroupId);
                startActivity(intent);
                return true;



            case R.id.groupFeeds:
                Intent intent1 = new Intent(Proceed_Group_FullScreen.this, PrivateGroupFeeds.class);
                intent1.putExtra("GroupId", GroupId);
                startActivity(intent1);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    private static byte[] readBytesFromFile(String filePath) {

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



    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("PFF","Permission is granted");
                return true;
            } else {

                Log.v("PFF","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("PFF","Permission is granted");
            return true;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v("PFF","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }





}