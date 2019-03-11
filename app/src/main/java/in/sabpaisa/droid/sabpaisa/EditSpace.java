package in.sabpaisa.droid.sabpaisa;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import java.util.HashMap;
import java.util.Map;

import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.VolleyMultipartRequest;

public class EditSpace extends AppCompatActivity {

    String spaceName,spaceDescription,spaceLogo,spaceImage,appCid;

    ImageView img_SpaceImage,img_SpaceLogo;

    EditText editText_SpaceName,editText_SpaceDescription;

    Button btn_Cancel,btn_Save;

    Toolbar toolbar;

    String imageUrl,userAccessToken;

    Bitmap spaceLogoBitmap,spaceImageBitmap;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_space);

        img_SpaceImage = (ImageView)findViewById(R.id.img_SpaceImage);
        img_SpaceLogo = (ImageView)findViewById(R.id.img_SpaceLogo);
        editText_SpaceName = (EditText) findViewById(R.id.editText_SpaceName);
        editText_SpaceDescription = (EditText) findViewById(R.id.editText_SpaceDescription);
        btn_Cancel = (Button) findViewById(R.id.btn_Cancel);
        btn_Save = (Button) findViewById(R.id.btn_Save);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        progressDialog = new ProgressDialog(EditSpace.this,R.style.DialogTheme);

        spaceName = getIntent().getStringExtra("SPACE_NAME");
        spaceDescription = getIntent().getStringExtra("SPACE_DESCRIPTION");
        spaceLogo = getIntent().getStringExtra("SPACE_LOGO");
        spaceImage = getIntent().getStringExtra("SPACE_IMAGE");
        appCid = getIntent().getStringExtra("SPACE_APP_CID");

        Log.d("EdtSpace","RecievedValue_Intent__"+spaceName+"\n"+spaceDescription+"\n"+spaceLogo+"\n"+spaceImage+"\n"+appCid);

        toolbar.setTitle(spaceName);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_action_previousback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        userAccessToken = sharedPreferences.getString("response", "123");

        editText_SpaceName.setText(spaceName);
        editText_SpaceDescription.setText(spaceDescription);



        Glide.with(getApplicationContext())
                .load(spaceLogo)
                .error(R.drawable.ic_file_upload)
                .into(img_SpaceLogo);

        Glide.with(getApplicationContext())
                .load(spaceImage)
                .error(R.drawable.ic_file_upload)
                .into(img_SpaceImage);


        img_SpaceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        img_SpaceLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickLogo();
            }
        });


        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String spaceName = editText_SpaceName.getText().toString();
                String spaceDescription = editText_SpaceDescription.getText().toString();

                if (spaceName == null || spaceName.equals("") || spaceName.isEmpty()){
                    editText_SpaceName.setError("Please enter the Space Name");
                }else if (spaceDescription == null || spaceDescription.equals("") || spaceDescription.isEmpty()){
                    editText_SpaceDescription.setError("Please enter the Space Description");
                }else if (!isOnline()){
                    AlertDialog alertDialog = new AlertDialog.Builder(EditSpace.this, R.style.MyDialogTheme).create();

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

                    uploadSpaceData(spaceImageBitmap,spaceLogoBitmap,spaceName,spaceDescription,userAccessToken,appCid);
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

                Log.d("EditSpace", "selectedimg_ " + selectedimg);

                //android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                img_SpaceImage.setImageBitmap(android.provider.MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));

                spaceImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg);

                Log.d("spaceImageBitmap"," "+spaceImageBitmap);

                if (spaceImageBitmap == null){
                    Toast.makeText(EditSpace.this, "Invalid File Format", Toast.LENGTH_SHORT).show();
                    img_SpaceImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_file_upload));
                }


            }else if (requestCode == 300 && resultCode == RESULT_OK && data != null){

                Uri selectedimg = data.getData();

                //android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                img_SpaceLogo.setImageBitmap(android.provider.MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));

                spaceLogoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg);

                Log.d("spaceLogoBitmap"," "+spaceLogoBitmap);

                if (spaceLogo == null){
                    Toast.makeText(EditSpace.this, "Invalid File Format", Toast.LENGTH_SHORT).show();
                    img_SpaceLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_file_upload));
                }


            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }


    }


    private void uploadSpaceData(final Bitmap spaceImageBitmap, final Bitmap spaceLogoBitmap, final String spaceName, final String spaceDescription, final String userAccessToken,final String appCid) {

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_updateSpappclient+"?appcid="+appCid + "&cname="+spaceName+"&description="+spaceDescription+"&userToken="+userAccessToken;

        Log.d("EditSpace","_URL "+url);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Log.d("EditSpace", "Res_" + response);

                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                try {

                    JSONObject obj = new JSONObject(new String(response.data));

                    Log.d("EditSpace", "ResJsonObj_" + obj);

                    final String status = obj.getString("status");
                    final String returnResponse = obj.getString("response");

                    if (status.equals("success")) {

                        Log.d("EditSpace","InIfPart");

                        AlertDialog alertDialog = new AlertDialog.Builder(EditSpace.this, R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Edit Space");

                        // Setting Dialog Message
                        alertDialog.setMessage(returnResponse);


                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                finish();

                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();


                    }else if (status.equals("failure")){
                        Toast.makeText(EditSpace.this,returnResponse,Toast.LENGTH_SHORT).show();
                    }
                    else {

                        Log.d("EditSpace","InElsePart");

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


                        //Toast.makeText(AddFeed.this, "Data Upload Failed !", Toast.LENGTH_SHORT).show();
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
                String spaceImage = "SabPaisa_SpaceImage";
                String spaceLogo = "SabPaisa_SpaceLogo";

                if (spaceImageBitmap != null)
                    params.put("cImage", new DataPart(spaceImage + ".jpeg", getFileDataFromDrawable(spaceImageBitmap)));
//                else{
//                    params.put("feed_image",null);
//                }
                if(spaceLogoBitmap != null)
                    params.put("clogo", new DataPart(spaceLogo + ".jpeg", getFileDataFromDrawable(spaceLogoBitmap)));
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
            Log.v("EditSpace", "Internet Connection Not Present");
            return false;
        }
    }



}
