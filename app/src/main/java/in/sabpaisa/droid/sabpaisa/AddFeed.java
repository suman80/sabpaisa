package in.sabpaisa.droid.sabpaisa;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.ProfileNavigationActivity;
import in.sabpaisa.droid.sabpaisa.Util.VolleyMultipartRequest;

public class AddFeed extends AppCompatActivity {

    EditText editText_FeedName,editText_FeedDescription;
    ImageView img_FeedImage,img_FeedLogo;
    Button btn_Cancel,btn_Save;

    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feed);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        editText_FeedName = (EditText)findViewById(R.id.editText_FeedName);
        editText_FeedDescription = (EditText)findViewById(R.id.editText_FeedDescription);
        img_FeedImage = (ImageView)findViewById(R.id.img_FeedImage);
        img_FeedLogo = (ImageView)findViewById(R.id.img_FeedLogo);
        btn_Cancel = (Button)findViewById(R.id.btn_Cancel);
        btn_Save = (Button)findViewById(R.id.btn_Save);

        img_FeedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        img_FeedLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickLogo();
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

                //android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                img_FeedImage.setImageBitmap(android.provider.MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));
                Bitmap userImg = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg);

                uploadBitmap(userImg);

            }else if (requestCode == 300 && resultCode == RESULT_OK && data != null){

                Uri selectedimg = data.getData();

                    //android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                img_FeedLogo.setImageBitmap(android.provider.MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));
                Bitmap userImg = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg);

                uploadBitmap(userImg);

            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }


    }


    private void uploadBitmap(final Bitmap bitmap) {

        //Log.d("AddFeed", "IMG_userAccessToken" + userAccessToken);
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_UserProfileImageUpdate + "?token=" /*+ userAccessToken*/,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d("AddFeed", "IMG_Res" + response);
                        try {

                            JSONObject obj = new JSONObject(new String(response.data));
                            Log.d("AddFeed", "IMG_Res" + obj);
                            final String status = obj.getString("status");

                            if (status.equals("success")) {


                                AlertDialog alertDialog = new AlertDialog.Builder(AddFeed.this, R.style.MyDialogTheme).create();

                                // Setting Dialog Title
                                alertDialog.setTitle("User Image Update");

                                // Setting Dialog Message
                                alertDialog.setMessage("Your Image Has Been Updated successfully !");

                                // Setting Icon to Dialog
                                //  alertDialog.setIcon(R.drawable.tick);

                                // Setting OK Button
                                alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent();
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                      /*  Intent intent = new Intent(AddFeed.this,AddFeed.class);
                                        startActivity(intent);*/
                                    }
                                });

                                // Showing Alert Message
                                alertDialog.show();

                            } else {
                                Toast.makeText(AddFeed.this, "Image Upload Failed !", Toast.LENGTH_SHORT).show();

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

                        Toast.makeText(AddFeed.this, "Image Upload Failed !", Toast.LENGTH_SHORT).show();
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
                String imagename = "SabPaisa_UserImage";
                params.put("userImage", new DataPart(imagename + ".jpeg", getFileDataFromDrawable(bitmap)));
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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


}
