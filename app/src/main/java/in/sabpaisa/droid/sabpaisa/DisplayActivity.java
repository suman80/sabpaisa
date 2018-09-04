package in.sabpaisa.droid.sabpaisa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.VolleyMultipartRequest;

public class DisplayActivity extends AppCompatActivity {

    ImageView sendBtn;
    ImageView imageView;
    EditText commentEditText;
    ProgressBar progress_bar;
    Bitmap photo;
    private static final int CAMERA_REQUEST = 1888;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private String mCurrentPhotoPath;

    int camVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        imageView = (ImageView) findViewById(R.id.imageView1);
        sendBtn = (ImageView) findViewById(R.id.sendBtn);
        commentEditText = (EditText) findViewById(R.id.commentEditText);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        camVal = getIntent().getIntExtra("CAMVALUE",0);

        Log.d("camVal"," "+camVal);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress_bar.setVisibility(View.VISIBLE);

                SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

                final String userAccessToken = sharedPreferences.getString("response", "123");

                if (camVal==1) {

                    SharedPreferences prefs = getSharedPreferences(Proceed_Feed_FullScreen.MY_PREFS_FOR_FEED_ID, MODE_PRIVATE);
                    final String feedId = prefs.getString("feedId", null);

                    //////////////////////////////////////////////////////////////////////////
                    StringEscapeUtils.escapeJava(commentEditText.getText().toString());
                    EditText etEmojiEditText = new EditText(DisplayActivity.this);
                    etEmojiEditText.setText("TYPE SOMETHING IN EMOJI");

                    String toServer = String.valueOf(commentEditText.getText());
                    String toServerUnicodeEncoded = StringEscapeUtils.escapeJava(toServer);

                    String serverResponse = "SOME RESPONSE FROM SERVER WITH UNICODE CHARACTERS";
                    String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(serverResponse);

                    String commentText = commentEditText.getText().toString();
                    String i= StringEscapeUtils.escapeJava(commentText);
                    Log.d("commentText3","67667767 "+i);
//        rv.smoothScrollBy(100,100);

                    if (i.trim().length()==0)
                    {

                        Log.d("commentText2"," "+commentText);
                        AlertDialog.Builder builder =new AlertDialog.Builder(DisplayActivity.this);
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
                        sendCommentFeed(feedId, userAccessToken, i,photo);
                    }


                    else if(i.equals("&"))

                    {
                        commentText.replace("&", "%26");
                        Log.e("ctctcc ", "CommentData2 " + commentText);
                        sendCommentFeed(feedId, userAccessToken, i,photo);

                    }




                    else if(i.trim().length()>1999)
                    {
                        AlertDialog.Builder builder =new AlertDialog.Builder(DisplayActivity.this);
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



                        sendCommentFeed(feedId, userAccessToken, i,photo);
                        Log.e("CommentDatafeeddetaida ", "CommentData " + commentText);
                    }

                    Log.d("commentText3"," "+commentText);
                    /////////////////////////////////////////////////////////////////////////

                    //sendCommentFeed(feedId, userAccessToken, comment, photo);
                }
                if (camVal == 2){

                    SharedPreferences prefs = getSharedPreferences(Proceed_Group_FullScreen.MY_PREFS_FOR_GROUP_ID, MODE_PRIVATE);
                    final String groupId = prefs.getString("groupId", null);


                    StringEscapeUtils.escapeJava(commentEditText.getText().toString());
                    EditText etEmojiEditText = new EditText(DisplayActivity.this);
                    etEmojiEditText.setText("TYPE SOMETHING IN EMOJI");

                    String toServer = String.valueOf(commentEditText.getText());
                    String toServerUnicodeEncoded = StringEscapeUtils.escapeJava(toServer);

                    String serverResponse = "SOME RESPONSE FROM SERVER WITH UNICODE CHARACTERS";
                    String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(serverResponse);

                    String commentText = commentEditText.getText().toString();
                    String i= StringEscapeUtils.escapeJava(commentText);
                    Log.d("commentText3","67667767 "+i);
//        rv.smoothScrollBy(100,100);

                    if (i.trim().length()==0)
                    {

                        Log.d("commentText2"," "+commentText);
                        AlertDialog.Builder builder =new AlertDialog.Builder(DisplayActivity.this);
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
                        sendCommentGroup(groupId, userAccessToken, i,photo);
                    }


                    else if(i.equals("&"))

                    {
                        commentText.replace("&", "%26");
                        Log.e("ctctcc ", "CommentData2 " + commentText);
                        sendCommentGroup(groupId, userAccessToken, i,photo);

                    }




                    else if(i.trim().length()>1999)
                    {
                        AlertDialog.Builder builder =new AlertDialog.Builder(DisplayActivity.this);
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



                        sendCommentGroup(groupId, userAccessToken, i,photo);
                        Log.e("CommentDatafeeddetaida ", "CommentData " + commentText);
                    }

                    Log.d("commentText3"," "+commentText);
                    /////////////////////////////////////////////////////////////////////////


//                    String comment = commentEditText.getText().toString();
//                    sendCommentGroup(groupId,userAccessToken,comment,photo);
                }

            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                        MY_CAMERA_PERMISSION_CODE);
            } else {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.i("DisplayActivity", "IOException");
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {

                        Uri photoURI=null;

                        try {
                             photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".my.package.name.provider", createImageFile());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);

                    }

                }

            }
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



    protected void sendCommentFeed(final String feed_id, final String userAccessToken, final String comment_text ,final Bitmap image) {

        Log.d("PVRA", "IMG_userAccessToken" + userAccessToken);
        //our custom volley request

        Log.d("PVRAcomment_text"  ,"->"+  URLEncoder.encode(comment_text));
        String url = AppConfig.Base_Url+AppConfig.App_api+ "addFeedsComments?feed_id=" + feed_id + "&userAccessToken=" + userAccessToken + "&comment_text="  +  URLEncoder.encode(comment_text.trim());

        Log.d("URL_AT_PVRA"," "+url);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d("PVRA", "IMG_Res" + response);
                        try {

                            JSONObject obj = new JSONObject(new String(response.data));
                            Log.d("PVRA", "IMG_Res" + obj);
                            final String status = obj.getString("status");

                            if (status.equals("success")) {

                                Log.d("CommentPVRA","Success");

                                commentEditText.setText("");

                                progress_bar.setVisibility(View.GONE);

                                Intent intent = new Intent(DisplayActivity.this,Proceed_Feed_FullScreen.class);
                                intent.putExtra("feedId",feed_id);
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            } else {

                                Log.d("CommentPVRA","Failed");
                                //Toast.makeText(Proceed_Feed_FullScreen.this, "Image Upload Failed !", Toast.LENGTH_SHORT).show();

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

                params.put("commentFile", new DataPart(imagename + ".jpeg", getFileDataFromDrawable(image)));

                Log.d("Image_At_PVRA",params.get("commentFile")+"");

                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }



    private void sendCommentGroup(final String GroupId, final String userAccessToken, final String comment_text,final Bitmap image) {

        Log.d("PVRA", "IMG_userAccessToken" + userAccessToken);

        Log.d("PVRAcomment_text"  ,"->"+  URLEncoder.encode(comment_text));

        String url = AppConfig.Base_Url + AppConfig.App_api + "/addGroupsComments?group_id=" + GroupId + "&userAccessToken=" + userAccessToken + "&comment_text=" + URLEncoder.encode(comment_text.trim());

        Log.d("URL_AT_PVRAG"," "+url);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d("PVRAG", "IMG_Res" + response);


                        try {
                            // Parsing json object response
                            // response will be a json object
                            JSONObject obj = new JSONObject(new String(response.data));
                            String status = obj.getString("status");
                            if (status.equals("success")) {

                                Log.d("CommentPVRAG","Success");

                                commentEditText.setText("");

                                progress_bar.setVisibility(View.GONE);

                                Intent intent = new Intent(DisplayActivity.this,Proceed_Group_FullScreen.class);
                                intent.putExtra("groupId",GroupId);

                                startActivity(intent);
                                finish();


                            } else {
                                Log.d("CommentPVRAG","Failed");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: KASKADKASKDASKCNSKACKASNVKNASKANka" + e.getMessage(),
                                    Toast.LENGTH_LONG).show();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error In Upoload", error.toString());
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

                params.put("commentFile", new DataPart(imagename + ".jpeg", getFileDataFromDrawable(image)));

                Log.d("Image_At_PVRAG",params.get("commentFile")+"");


                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);



    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }


    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            try {
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                imageView.setImageBitmap(photo);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
