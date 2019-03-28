package in.sabpaisa.droid.sabpaisa;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
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

import static in.sabpaisa.droid.sabpaisa.Proceed_Group_FullScreen.memberGroupRole;

public class AddSpaceActivity extends AppCompatActivity {

    String imageUrl;
    String imageUrl1;

    Bitmap spaceImageBitmap,spaceLogoBitmap;

    EditText spaceNameEditText,spaceDescriptionEditText;

    ImageView spaceLogo,spaceImg;

    Button btn_Save;

    String userAccessToken;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_space);

        spaceImg = (ImageView)findViewById(R.id.spaceImg);
        spaceLogo = (ImageView)findViewById(R.id.spaceLogo);
        spaceNameEditText = (EditText) findViewById(R.id.spaceNameEditText);
        spaceDescriptionEditText = (EditText) findViewById(R.id.spaceDescriptionEditText);
        btn_Save = (Button)findViewById(R.id.btn_Save);

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        userAccessToken = sharedPreferences.getString("response", "123");

        progressDialog = new ProgressDialog(AddSpaceActivity.this,R.style.DialogTheme);

        spaceImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        spaceLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickLogo();
            }
        });


        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String spaceName = spaceNameEditText.getText().toString();
                String spaceDescription = spaceDescriptionEditText.getText().toString();

                if (spaceName == null || spaceName.isEmpty()){
                    spaceNameEditText.setError("Please entre the name for your space !");
                }else if (spaceDescription == null || spaceDescription.isEmpty()){
                    spaceDescriptionEditText.setError("Please enter the description of your space");
                }else if (!isOnline()){
                    AlertDialog alertDialog = new AlertDialog.Builder(AddSpaceActivity.this, R.style.MyDialogTheme).create();

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
                    uploadSpaceData(spaceImageBitmap,spaceLogoBitmap,spaceName.trim(),spaceDescription.trim(),userAccessToken);
                }

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
        intent.putExtra("userImageUrl", imageUrl1);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 300);


    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == 200 && resultCode == RESULT_OK && data != null) {

                Uri selectedimg = data.getData();

                Log.d("AddSpace", "selectedimg_ " + selectedimg);

                //android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                spaceImg.setImageBitmap(android.provider.MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));

                /*final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;

                Bitmap bm = BitmapFactory.decodeFile(String.valueOf(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg)),options);
                spaceImg.setImageBitmap(bm);*/

                spaceImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg);

                Log.d("spaceImageBitmap"," "+spaceImageBitmap);

                if (spaceImageBitmap == null){
                    Toast.makeText(AddSpaceActivity.this, "Invalid File Format", Toast.LENGTH_SHORT).show();
                    spaceImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_file_upload));
                }


            }else if (requestCode == 300 && resultCode == RESULT_OK && data != null){

                Uri selectedimg = data.getData();

                //android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                spaceLogo.setImageBitmap(android.provider.MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));

                spaceLogoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg);

                Log.d("spaceLogoBitmap"," "+spaceLogoBitmap);

                if (spaceLogoBitmap == null){
                    Toast.makeText(AddSpaceActivity.this, "Invalid File Format", Toast.LENGTH_SHORT).show();
                    spaceLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_file_upload));
                }


            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }


    }


    private void uploadSpaceData(final Bitmap spaceImageBitmap, final Bitmap spaceLogoBitmap, final String spaceName, final String spaceDescription, final String userAccessToken) {

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_addSpappclient + "?cname="+URLEncoder.encode(spaceName)+"&description="+URLEncoder.encode(spaceDescription)+"&userToken="+userAccessToken;

        Log.d("AddSpace","_URL "+url);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Log.d("AddSpace", "Res_" + response);

                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                try {

                    JSONObject obj = new JSONObject(new String(response.data));

                    Log.d("AddSpace", "ResJsonObj_" + obj);

                    final String status = obj.getString("status");
                    final String returnResponse = obj.getString("response");

                    if (status.equals("success")) {

                        Log.d("AddSpace","InIfPart");
                        finish();

                    }else if (status.equals("failure")){
                        Toast.makeText(AddSpaceActivity.this,returnResponse,Toast.LENGTH_SHORT).show();
                    }
                    else {

                        Log.d("AddSpace","InElsePart");

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
            Log.v("AddSpace", "Internet Connection Not Present");
            return false;
        }
    }





}
