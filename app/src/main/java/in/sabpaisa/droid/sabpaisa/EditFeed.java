package in.sabpaisa.droid.sabpaisa;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;
import in.sabpaisa.droid.sabpaisa.Util.VolleyMultipartRequest;

public class EditFeed extends AppCompatActivity {

    Toolbar toolbar;
    String FeedsNm,feedsDiscription,feedImg,feedLogo,feed_id;

    EditText editText_FeedName,editText_FeedDescription;

    ImageView img_FeedImage,img_FeedLogo;

    String imageUrl;

    Bitmap feedImageBitMap,feedLogoBitMap;

    String userAccessToken;

    Button btn_Cancel,btn_Save;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_edit_feed);

        FeedsNm = getIntent().getStringExtra("feedName");
        feedsDiscription = getIntent().getStringExtra("feedText");
        feedImg = getIntent().getStringExtra("feedImage");
        feedLogo = getIntent().getStringExtra("feedLogo");
        feed_id = getIntent().getStringExtra("feedId");

        Log.d("EditFeed","IntentValues_"+FeedsNm+" "+feedsDiscription+" "+feedImg+" "+feedLogo+" "+feed_id);

        toolbar = (Toolbar)findViewById(R.id.toolbar);

        progressDialog = new ProgressDialog(EditFeed.this,R.style.DialogTheme);

        toolbar.setTitle(FeedsNm);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_action_previousback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        editText_FeedName = (EditText)findViewById(R.id.editText_FeedName);
        editText_FeedDescription = (EditText)findViewById(R.id.editText_FeedDescription);
        //img_FeedImage = (ImageView)findViewById(R.id.img_FeedImage);
        img_FeedLogo = (ImageView)findViewById(R.id.img_FeedLogo);
        btn_Save = (Button)findViewById(R.id.btn_Save);
        btn_Cancel = (Button)findViewById(R.id.btn_Cancel);

        editText_FeedName.setText(FeedsNm);
        editText_FeedDescription.setText(feedsDiscription);

        /*Glide.with(getApplicationContext())
                .load(feedImg)
                .error(R.drawable.appicon)
                .into(img_FeedImage);*/

        Glide.with(getApplicationContext())
                .load(feedLogo)
                .error(R.drawable.ic_file_upload)
                .into(img_FeedLogo);


        /*img_FeedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });*/

        img_FeedLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickLogo();
            }
        });

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        userAccessToken = sharedPreferences.getString("response", "123");

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String feedNm = editText_FeedName.getText().toString().trim();
                String feedDesc = editText_FeedDescription.getText().toString().trim();

                if (feedNm == null || feedNm.equals("") || feedNm.isEmpty()){
                    editText_FeedName.setError("Please enter the Feed Name");
                }else if (feedDesc == null || feedDesc.equals("") || feedDesc.isEmpty()){
                    editText_FeedDescription.setError("Please enter the Feed Description");
                }else if (!isOnline()){
                    AlertDialog alertDialog = new AlertDialog.Builder(EditFeed.this, R.style.MyDialogTheme).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("No Internet Connection");

                    // Setting Dialog Message
                    alertDialog.setMessage("Please check internet connection and try again. Thank you.");


                    alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                }else {

                    progressDialog.setMessage("Please wait !");
                    progressDialog.show();

                    updateFeed(feedImageBitMap,feedLogoBitMap,feedNm,feedDesc);
                }

            }
        });

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });






    }


    public void pickImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("userImageUrl", imageUrl);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 200);


    }

    public void pickLogo() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("userImageUrl", imageUrl);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 300);


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == 200 && resultCode == RESULT_OK && data != null) {

                Uri selectedimg = data.getData();

                Log.d("EditFeed", "selectedimg_ " + selectedimg);

                //android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                img_FeedImage.setImageBitmap(android.provider.MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));

                feedImageBitMap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg);

                Log.d("feedImageBitMap"," "+feedImageBitMap);

                if (feedImageBitMap == null){
                    Toast.makeText(EditFeed.this, "Invalid File Format", Toast.LENGTH_SHORT).show();
                    img_FeedImage.setImageDrawable(getResources().getDrawable(R.drawable.appicon));
                }


            }else if (requestCode == 300 && resultCode == RESULT_OK && data != null){

                Uri selectedimg = data.getData();

                //android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                img_FeedLogo.setImageBitmap(android.provider.MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));

                feedLogoBitMap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg);

                Log.d("feedImageBitMap"," "+feedLogoBitMap);

                if (feedLogoBitMap == null){
                    Toast.makeText(EditFeed.this, "Invalid File Format", Toast.LENGTH_SHORT).show();
                    img_FeedLogo.setImageDrawable(getResources().getDrawable(R.drawable.appicon));
                }


            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }


    }


    private void updateFeed(final Bitmap feed_image,final Bitmap feed_logo,final String feedName,final String feedText) {

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_updateFeed + "?feed_Id="+feed_id+"&feed_text="+URLEncoder.encode(feedText)+"&feed_name="+URLEncoder.encode(feedName)+"&admin="+userAccessToken;

        Log.d("EditFeed","_URL "+url);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Log.d("EditFeed", "Res_" + response);

                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }


                try {

                    JSONObject obj = new JSONObject(new String(response.data));

                    Log.d("EditFeed", "ResJsonObj_" + obj);

                    final String status = obj.getString("status");
                    final String returnResponse = obj.getString("response");

                    if (status.equals("success")) {

                        Log.d("EditFeed", "InIfPart");

                        //Toast.makeText(EditFeed.this,"Feed has been Edited",Toast.LENGTH_SHORT).show();

                        AlertDialog alertDialog = new AlertDialog.Builder(EditFeed.this, R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Edit Feed");

                        // Setting Dialog Message
                        alertDialog.setMessage("Feed has been Edited");

                        alertDialog.setCancelable(false);

                        alertDialog.setCanceledOnTouchOutside(false);

                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                if (PrivateGroupFeeds.FLAG != null) {


                                    Intent intent = new Intent(EditFeed.this,PrivateGroupFeeds.class);
                                    intent.putExtra("GroupId",PrivateGroupFeeds.GroupId);
                                    intent.putExtra("memberGroupRole",PrivateGroupFeeds.memberGroupRole);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    MainGroupAdapter1.isClicked = false;
                                    MainFeedAdapter.isClicked = false;
                                    startActivity(intent);


                                } else {


                                    String clientImageURLPath = FullViewOfClientsProceed.clientImageURLPath;

                                    Log.d("EditFeed", "clientImageURLPath " + clientImageURLPath);

                                    Intent intent = new Intent(EditFeed.this, FullViewOfClientsProceed.class);
                                    intent.putExtra("clientImagePath", clientImageURLPath);
                                    intent.putExtra("FRAGMENT_ID", "0");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    MainGroupAdapter1.isClicked = false;
                                    MainFeedAdapter.isClicked = false;
                                    startActivity(intent);
                                }

                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();



                    }else if (status.equals("failure")){
                        Toast.makeText(EditFeed.this,returnResponse,Toast.LENGTH_SHORT).show();
                    }
                    else {

                        Log.d("EditFeed","InElsePart");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error In Upoload", error.toString());

                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }


                        //Toast.makeText(EditFeed.this, "Data Upload Failed !", Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            /*@Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tags", tags);
                return params;
            }*/

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                String feedImage = "SabPaisa_FeedImage";
                String feedLogo = "SabPaisa_FeedLogo";

                if (feed_image != null)
                    params.put("feed_image", new DataPart(feedImage + ".jpeg", getFileDataFromDrawable(feed_image)));
//                else{
//                    params.put("feed_image",null);
//                }
                if(feed_logo != null)
                    params.put("feed_logo", new DataPart(feedLogo + ".jpeg", getFileDataFromDrawable(feed_logo)));
//                else{
//                    params.put("feed_logo",null);
//                }
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }


    /*
     * The method is taking Bitmap as an argument
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


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v("EditFeed", "Internet Connection Not Present");
            return false;
        }
    }






}
