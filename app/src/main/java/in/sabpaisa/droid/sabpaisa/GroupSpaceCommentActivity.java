package in.sabpaisa.droid.sabpaisa;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
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
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;
import in.sabpaisa.droid.sabpaisa.Util.VolleyMultipartRequest;

import static in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments.TABLE_GROUP_COMMENTS;

public class GroupSpaceCommentActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{


    String date1,i;

    public static String GroupsNm , GroupsDiscription , GroupsImg,groupLogo;
    public static String memberGroupRole;
    String  GroupId, userAccessToken, response;
    ArrayList<CommentData> arrayList, feedArrayList;
    SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;
    ScrollView scrollView;

    ArrayList<CommentData> commentArrayList;
    int count = 1;
    ProgressBar progress;
    SpinKitView spin_kit;

    CircleButton imageView2;
    EditText group_details_text_view = null;

    private EndlessScrollListener scrollListener;


    FrameLayout shareViewFrameLayout, ImageViewFrameLayout, DocViewFrameLayout;
    ImageView attachmentFile, selectedImg, closeSelectedImage, selectedDoc, closeSelectedDoc, attachment_Image_Video_File;
    TextView selectedDocName;


    LinearLayout shareDocument, shareImage;

    String userImageUrl;

    Bitmap commentFile;

    File fileDoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_space_comment);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        GroupId = getIntent().getStringExtra("groupId");
        GroupsNm = getIntent().getStringExtra("groupName");
        GroupsDiscription = getIntent().getStringExtra("groupText");
        GroupsImg = getIntent().getStringExtra("groupImage");
        groupLogo = getIntent().getStringExtra("groupLogo");
        memberGroupRole = getIntent().getStringExtra("memberGroupRole");

        Log.d("NamemGSCA", "" + GroupsNm);
        Log.d("DiscriptionGSCA", "" + GroupsDiscription);
        Log.d("GroupImgGSCA", "" + GroupsImg);
        Log.d("GroupID_GSCA", "" + GroupId);
        Log.d("memberGroupRole_GSCA", "" + memberGroupRole);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(GroupsNm);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_previousback);
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
        //imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView2 = (CircleButton) findViewById(R.id.imageView2);


        commentArrayList = new ArrayList<CommentData>();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        response = sharedPreferences.getString("response", "123");

        userAccessToken = response;

        Log.d("PGFAccessToken", " " + userAccessToken);


        if (getIntent().getStringExtra("userAccessTokenFromNotification") != null) {
            userAccessToken = getIntent().getStringExtra("userAccessTokenFromNotification");
        }

        if(userAccessToken == null || userAccessToken.equals("123")) {
            Intent intent = new Intent(GroupSpaceCommentActivity.this, LogInActivity.class);
            startActivity(intent);
        }

        arrayList = new ArrayList<>();

        if (isOnline()) {
            progress.setVisibility(View.VISIBLE);
            //API
            callGetCommentList(GroupId,userAccessToken);
        }else {
            //Todo offline
        }


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
                Toast.makeText(GroupSpaceCommentActivity.this, "Documents", Toast.LENGTH_SHORT).show();

                if (isStoragePermissionGranted()) {
                    showFileChooser();
                } else {
                    Toast.makeText(GroupSpaceCommentActivity.this, "Permission Denied !", Toast.LENGTH_SHORT).show();
                }

            }
        });

        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(GroupSpaceCommentActivity.this, "Pick Image", Toast.LENGTH_SHORT).show();
                pickImage();
            }
        });

        attachment_Image_Video_File.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent intent = new Intent(GroupSpaceCommentActivity.this, DisplayActivity.class);
                intent.putExtra("CAMVALUE", 2);
                intent.putExtra("groupId", GroupId);
                intent.putExtra("groupName", GroupsNm);
                intent.putExtra("groupText", GroupsDiscription);
                startActivity(intent);
                finish();
*/
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




    }


    @Override
    protected void onResume() {
        super.onResume();

        // UI Update Code

    }


    private void loadCommentListView(ArrayList<CommentData> arrayList) {
        final RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view_feed_details_comment);
        final CommentAdapter ca = new CommentAdapter(arrayList, getApplicationContext(), toolbar);

        rv.setAdapter(ca);

        LinearLayoutManager llm = new LinearLayoutManager(this);
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
        i = StringEscapeUtils.escapeJava(commentText.trim());
        Log.d("commentText3", "67667767 " + i);

        // showpDialog(view);
        if (i.trim().length() == 0 && commentFile == null && fileDoc == null) {

            Log.d("commentText2", " " + commentText);
            AlertDialog.Builder builder = new AlertDialog.Builder(GroupSpaceCommentActivity.this);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(GroupSpaceCommentActivity.this);
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
                Log.d("SSSS ", selectedimg + "");
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
                    Toast.makeText(GroupSpaceCommentActivity.this, "Invalid File Format", Toast.LENGTH_SHORT).show();
                }


            }


            if (requestCode == 1) {
                if (resultCode == Activity.RESULT_OK) {


                    Uri uri = data.getData();
                    Log.d("PGFfileDoc1", "File Uri: " + uri.toString());
                    // Get the path
                    String path = getPath(this, uri);
                    Log.d("PGFfileDoc2", "File Path: " + path);

                    fileDoc = new File(path);


                    if (fileDoc != null && !(checkFileExtension(fileDoc))) {
                        shareViewFrameLayout.setVisibility(View.GONE);
                        ImageViewFrameLayout.setVisibility(View.GONE);
                        closeSelectedDoc.setVisibility(View.GONE);
                        Toast.makeText(GroupSpaceCommentActivity.this, "Invalid File Format", Toast.LENGTH_SHORT).show();
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




    private void callCommentService(final String GroupId, final String userAccessToken, final String comment_text) {

        ImageViewFrameLayout.setVisibility(View.GONE);
        DocViewFrameLayout.setVisibility(View.GONE);
        shareViewFrameLayout.setVisibility(View.GONE);
        closeSelectedImage.setVisibility(View.GONE);
        closeSelectedDoc.setVisibility(View.GONE);

        Log.d("GSCA", "userAccessToken_" + userAccessToken);
        //our custom volley request

        Log.d("comment_text <<<<<<<<< ", i + " <<<< " + URLEncoder.encode(i.trim()));

        String url = AppConfig.Base_Url + AppConfig.App_api + "/addGroupsComments?group_id=" + GroupId + "&userAccessToken=" + userAccessToken + "&comment_text=" + URLEncoder.encode(i.trim());

        Log.d("URL_AT_GSCA", "url__ " + url);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d("GSCA", "Res__" + response);


                        try {
                            // Parsing json object response
                            // response will be a json object
                            JSONObject obj = new JSONObject(new String(response.data));
                            String status = obj.getString("status");
                            String returnResponse = obj.getString("response");
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
                                callGetCommentList(GroupId,userAccessToken);


                            } else if (status.equals("failed")) {
                                commentFile = null;
                                fileDoc = null;
                                imageView2.setVisibility(View.VISIBLE);
                                spin_kit.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), returnResponse, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

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
                if (commentFile != null) {
                    params.put("commentFile", new DataPart(imagename + ".jpeg", getFileDataFromDrawable(commentFile)));

                    Log.d("Image_At_PGF", params.get("commentFile") + "");
                }


                if (fileDoc != null) {


                    byte[] fileContent = readBytesFromFile(fileDoc.getAbsolutePath());

                    params.put("commentFile", new DataPart(fileDoc.getName(), fileContent));


                }


                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);


    }

    public void callGetCommentList(final String GropuId,final String userAccessToken) {


        String tag_string_req = "req_register";

        String urlJsonObj = AppConfig.Base_Url + AppConfig.App_api + "getPageGroupsComments?group_id=" + GropuId + "&pageNo=" + count + "&rowLimit=25"+"&token="+userAccessToken;

        Log.d("URL_AT_GSCA", " " + urlJsonObj);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                urlJsonObj, new Response.Listener<String>() {

            // Takes the response from the JSON request
            @Override
            public void onResponse(String response) {
                try {
                    swipeRefreshLayout.setRefreshing(false);


                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("jsonobject1", "" + jsonObject);


                    JSONArray jsonArray = null;
                    Object obj = jsonObject.get("response");

                    String status = jsonObject.getString("status");

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

                            String userImageUrl = jsonObject1.getString("userImageUrl");


                            Log.d("dataTimePFF", " " + dataTime);
                            Log.d("dataimageurlgroup1111", " " + userImageUrl);


                            JSONObject jsonObject2 = new JSONObject(userImageUrl);
                            groupData.setUserImageUrl(jsonObject2.getString("userImageUrl"));
                            groupData.setUserId(jsonObject2.getString("userId"));
                            String image = groupData.getUserImageUrl().toString();
                            Log.d("imageuser", " " + image);


                            try {
                                groupData.setComment_date(getDate(Long.parseLong(dataTime)));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            commentArrayList.add(groupData);


                        }

                        loadCommentListView(commentArrayList);

                    } else if (status.equals("failure") && obj.equals("Not A Member")){

                        AlertDialog.Builder builder = new AlertDialog.Builder(GroupSpaceCommentActivity.this);
                        builder.setTitle("Comment Service");
                        builder.setMessage(obj.toString());
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                finish();
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        alertDialog.show();

                    }else {
                        progress.setVisibility(View.GONE);

                        if (count > 1 ){
                            progress.setVisibility(View.GONE);

                          Toast.makeText(GroupSpaceCommentActivity.this, "No More Record Found !", Toast.LENGTH_SHORT).show();
                        }


                    }

                    Log.d("GroupSpaceCmttActivity", "  " + obj.toString());


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

                        Log.e("GroupSpaceCmttActivity", "Error");
                    }
                }
        );
        // Adds the JSON array request "arrayreq" to the request queue
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
            callGetCommentList(GroupId,userAccessToken);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(GroupSpaceCommentActivity.this, "Seems that you are not connected to the internet \n Please connect your internet to load more data", Toast.LENGTH_SHORT).show();
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
            Log.v("GroupSpaceCmttActivity", "Internet Connection Not Present");
            return false;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_space_menu, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.groupSpaceMembers:
                Intent intent = new Intent(GroupSpaceCommentActivity.this, MembersOfAGroupSpace.class);
                intent.putExtra("GroupId", GroupId);
                //intent.putExtra("memberGroupRole", memberGroupRole);
                startActivity(intent);
                return true;


            case R.id.editGroupSpaceMenu:
                Intent intent2 = new Intent(GroupSpaceCommentActivity.this, EditGroupSpace.class);
                intent2.putExtra("groupName", GroupsNm);
                intent2.putExtra("groupText", GroupsDiscription);
                intent2.putExtra("groupImage", GroupsImg);
                intent2.putExtra("groupLogo", groupLogo);
                intent2.putExtra("groupId", GroupId);
                startActivity(intent2);
                return true;



            default:
                return super.onOptionsItemSelected(item);
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
    public void onBackPressed() {

        if (shareViewFrameLayout.getVisibility() == View.VISIBLE) {
            shareViewFrameLayout.setVisibility(View.GONE);
        } else {


            finish();

        }
    }




}
