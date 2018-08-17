package in.sabpaisa.droid.sabpaisa;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;

import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.text.format.DateFormat;

import android.util.Log;

import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
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
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import in.sabpaisa.droid.sabpaisa.Adapter.FeedsCommentOfflineAdapter;
import in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments;
import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.Model.FeedCommentsOfflineModel;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;

import in.sabpaisa.droid.sabpaisa.Util.VolleyMultipartRequest;

public class Proceed_Feed_FullScreen extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    TextView feedsName, feed_description_details;
    ImageView feedImage;
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

    /////////////////START : by RaJ/////////////////
    ArrayList<CommentData> commentArrayList;
    /////////////////END : by RaJ/////////////////
    boolean isScrolling = false;
    int currentItems,totalItems,scrolledOutItems;
    int count = 1;

    ProgressBar progress;
    SpinKitView spin_kit;
    ImageView imageView2;

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
        scrollView=(ScrollView)findViewById(R.id.scrollView);

        Log.d("AccessToken", " " + userAccessToken);

        Log.d("FFResponse", " " + response);

        progress = (ProgressBar) findViewById(R.id.progress);

        spin_kit = (SpinKitView) findViewById(R.id.spin_kit);
        imageView2 = (ImageView) findViewById(R.id.imageView2);

        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);

        FeedsNm = getIntent().getStringExtra("feedName");
        feedsDiscription = getIntent().getStringExtra("feedText");
        value=getIntent().getStringExtra("value");
        feedImg = getIntent().getStringExtra("feedImage");
        feed_id = getIntent().getStringExtra("feedId");
        popup=getIntent().getStringExtra("popup");
        Log.d("FeedsID", "" + feed_id);
        Log.d("ValUeAT FEED", "" + popup);
        Log.d("FeedsNmPFF", "" + FeedsNm);
        Log.d("feedsDiscriptionPFF", "" + feedsDiscription);
        Log.d("feedImgPFF", "" + feedImg);
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


        Log.d("Feedidproceed",""+feed_id);
        arrayList = new ArrayList<>();
        toolbar.setNavigationIcon(R.drawable.previousmoresmall);

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
            progress.setVisibility(View.VISIBLE);
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
                Toast.makeText(Proceed_Feed_FullScreen.this,"Documents",Toast.LENGTH_SHORT).show();

                if (isStoragePermissionGranted()){
                    showFileChooser();
                }else {
                    Toast.makeText(Proceed_Feed_FullScreen.this,"Permission Denied !",Toast.LENGTH_SHORT).show();
                }

            }
        });

        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Proceed_Feed_FullScreen.this,"Pick Image",Toast.LENGTH_SHORT).show();
                pickImage();
            }
        });


    }




    private void loadCommentListView(ArrayList<CommentData> arrayList) {
        final RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view_feed_details_comment);
        final CommentAdapter ca = new CommentAdapter(arrayList,getApplicationContext(),toolbar);
        rv.setAdapter(ca);

        final LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        llm.setReverseLayout(true);

//       scrollListener=new EndlessScrollListener(llm) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount) {
//                callGetCommentList(feed_id);
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
//        }, 100L);
        //////////////////Rajdeep///////////////////////////////////////////////
//        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
//                    isScrolling = true;
//                }
//
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                currentItems = llm.getChildCount();
//                totalItems =   llm.getItemCount();
//                scrolledOutItems = llm.findFirstVisibleItemPosition();
//
//                Log.d("onScrolled_WeGet"," "+currentItems+" "+totalItems+" "+scrolledOutItems);
//
//                if (isScrolling &&(currentItems + scrolledOutItems == totalItems)){
//                    //Data Fetch
//                    isScrolling = false;
//                    //FetchData
//                    //count++;
//                    Log.d("Hellio","TESTTTTTTTTTTTTTTTTTTTTTT");
//                    callGetCommentList(feed_id);
//                }
//
//            }
  //      });

        progress.setVisibility(View.GONE);

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

        if (i.trim().length()==0 && commentFile == null && fileDoc == null)
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

        }else if (!(checkFileExtension(fileDoc))){
            Toast.makeText(Proceed_Feed_FullScreen.this,"Invalid File Format",Toast.LENGTH_SHORT).show();
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

            imageView2.setVisibility(View.GONE);
            spin_kit.setVisibility(View.VISIBLE);

            callCommentService(feed_id, userAccessToken, commentText);
            Log.e("CommentDatafeeddetaida ", "CommentData " + commentText);
        }

        Log.d("commentText3"," "+commentText);

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



    protected void callCommentService(final String feed_id, final String userAccessToken, final String comment_text) {

        ImageViewFrameLayout.setVisibility(View.GONE);
        DocViewFrameLayout.setVisibility(View.GONE);
        shareViewFrameLayout.setVisibility(View.GONE);
        closeSelectedImage.setVisibility(View.GONE);
        closeSelectedDoc.setVisibility(View.GONE);
        Log.d("PFF", "IMG_userAccessToken" + userAccessToken);
        //our custom volley request

        Log.d("comment_text <<<<<<<<< "  ,i+" <<<< "+  URLEncoder.encode(i));
        String url = AppConfig.Base_Url+AppConfig.App_api+ "addFeedsComments?feed_id=" + feed_id + "&userAccessToken=" + userAccessToken + "&comment_text="  +  URLEncoder.encode(i.trim());


        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d("PFF", "IMG_Res" + response);
                        try {

                            JSONObject obj = new JSONObject(new String(response.data));
                            Log.d("PFF", "IMG_Res" + obj);
                            final String status = obj.getString("status");

                            if (status.equals("success")) {

                                Log.d("Comment","Success");


                                imageView2.setVisibility(View.VISIBLE);
                                spin_kit.setVisibility(View.GONE);

                                group_details_text_view = (EditText) findViewById(R.id.commentadd);
                                group_details_text_view.setText("");

                                //Toast.makeText(Proceed_Feed_FullScreen.this, "Image Upload Success !", Toast.LENGTH_SHORT).show();

                                commentFile = null;
                                fileDoc = null;

                                fileDoc = null;

                                commentArrayList.clear();
                                count=1;
                                callGetCommentList(feed_id);


                            } else {

                                imageView2.setVisibility(View.VISIBLE);
                                spin_kit.setVisibility(View.GONE);

                                Log.d("Comment","Failed");
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


    public void callGetCommentList(final String feed_id) {

        db.deleteAllFeedCommentData();

        String tag_string_req="req_register";

        String urlJsonObj = AppConfig.Base_Url+AppConfig.App_api+"/getPageFeedsComments?feed_id="+feed_id+"&pageNo="+count+"&rowLimit=25";

        Log.d("urlJsonObj  : ",urlJsonObj);
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                urlJsonObj, new Response.Listener<String>(){

            // Takes the response from the JSON request
            @Override
            public void onResponse(String response) {
                try {
                    swipeRefreshLayout.setRefreshing(false);

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    String response1 = jsonObject.getString("response");
                    Log.d("Re[spnsere"," "+response1);
                    Log.d("Re[spnsere"," "+status);

                    if (status.equals("success")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("response");


                        for (int i = 0; i < jsonArray.length(); i++) {
                            CommentData groupData = new CommentData();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String a = jsonObject1.getString("commentText");
                            Log.d("CTadhkacka", " " + a);
                            groupData.setCommentText(jsonObject1.getString("commentText"));
                            groupData.setCommentName(jsonObject1.getString("commentByName"));
                            groupData.setCommentImage(jsonObject1.getString("filePath"));
                            groupData.setCommentId(jsonObject1.getInt("commentId"));

                            dataTime1 = jsonObject1.getString("commentDate");
                            Log.d("dataTimePFF", " " + dataTime1);

                            String str = jsonArray.getString(i);
                            Log.d("444feed", str);
                            String userImageUrl = jsonObject1.getString("userImageUrl");

                            JSONObject jsonObject2 = new JSONObject(userImageUrl);
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
                    }else {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(Proceed_Feed_FullScreen.this,"No Record Found !",Toast.LENGTH_SHORT).show();
                    }
                }
                // Try and catch are included to handle any errors due to JSON
                catch (JSONException e) {
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

        if (isOnline()) {
            count++;
            callGetCommentList(feed_id);
        }else {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(Proceed_Feed_FullScreen.this,"Seems that you are not connected to the internet \n Please connect your internet to load more data",Toast.LENGTH_SHORT).show();
        }
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
        ed.putBoolean("activeFeed", false);
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
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
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


    public boolean checkFileExtension (File file) {
        if (file.getAbsolutePath().endsWith(".doc") || file.getAbsolutePath().endsWith(".docx")
                || file.getAbsolutePath().endsWith(".pdf") || file.getAbsolutePath().endsWith(".xls")
                || file.getAbsolutePath().endsWith(".xlsx")){
            return true;
        }
        return false;
    }




}
