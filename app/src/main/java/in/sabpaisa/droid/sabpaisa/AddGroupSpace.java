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
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
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
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.SkipClientDetailsScreen;
import in.sabpaisa.droid.sabpaisa.Util.VolleyMultipartRequest;

public class AddGroupSpace extends AppCompatActivity {

    String appCid;

    EditText editText_GroupName,editText_GroupDescription;
    ImageView img_GroupImage,img_GroupLogo;
    Button btn_Cancel,btn_Save;

    String imageUrl;

    String userAccessToken;

    Bitmap groupImage,groupLogo;

    Toolbar toolbar;

    ProgressDialog progressDialog;

    String clientName,clientLogoPath,clientImagePath,state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_space);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        appCid = getIntent().getStringExtra("appCid");
        clientName = getIntent().getStringExtra("clientName");
        clientLogoPath = getIntent().getStringExtra("clientLogoPath");
        clientImagePath = getIntent().getStringExtra("clientImagePath");
        state = getIntent().getStringExtra("state");

        editText_GroupName = (EditText)findViewById(R.id.editText_GroupName);
        editText_GroupDescription = (EditText)findViewById(R.id.editText_GroupDescription);
        //img_GroupImage = (ImageView)findViewById(R.id.img_GroupImage);
        img_GroupLogo = (ImageView)findViewById(R.id.img_GroupLogo);
        btn_Cancel = (Button)findViewById(R.id.btn_Cancel);
        btn_Save = (Button)findViewById(R.id.btn_Save);


        toolbar = (Toolbar)findViewById(R.id.toolbar);

        progressDialog = new ProgressDialog(AddGroupSpace.this,R.style.DialogTheme);

        toolbar.setTitle("Add Groups");
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

        img_GroupLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickLogo();
            }
        });


        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String groupNm = editText_GroupName.getText().toString().trim();
                String groupDesc = editText_GroupDescription.getText().toString().trim();

                if (groupNm == null || groupNm.equals("") || groupNm.isEmpty()){
                    editText_GroupName.setError("Please enter the Group Name");
                }else if (groupDesc == null || groupDesc.equals("") || groupDesc.isEmpty()){
                    editText_GroupDescription.setError("Please enter the Group Description");
                }else if (!isOnline()){
                    AlertDialog alertDialog = new AlertDialog.Builder(AddGroupSpace.this, R.style.MyDialogTheme).create();

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

                    uploadGroupData(groupImage,groupLogo,groupNm,groupDesc);
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

                Uri selectedGrpImg = data.getData();

                //android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                img_GroupImage.setImageBitmap(android.provider.MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedGrpImg));

                groupImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedGrpImg);


                if (groupImage == null){
                    Toast.makeText(AddGroupSpace.this, "Invalid File Format", Toast.LENGTH_SHORT).show();
                    img_GroupImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_file_upload));
                }


            }else if (requestCode == 300 && resultCode == RESULT_OK && data != null){

                Uri selectedGrpLogo = data.getData();

                //android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                img_GroupLogo.setImageBitmap(android.provider.MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedGrpLogo));

                groupLogo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedGrpLogo);

                if (groupLogo == null){
                    Toast.makeText(AddGroupSpace.this, "Invalid File Format", Toast.LENGTH_SHORT).show();
                    img_GroupLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_file_upload));
                }

            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }


    }


    private void uploadGroupData(final Bitmap group_image,final Bitmap group_logo,final String groupName,final String groupText) {

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_addParticularClientsGroups + "?app_cid="+appCid+"&group_text="+ URLEncoder.encode(groupText)+"&group_name="+URLEncoder.encode(groupName)+"&admin="+userAccessToken;

        Log.d("AddgroupSpace","_URL "+url);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Log.d("AddgroupSpace", "Res_" + response);

                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                try {

                    JSONObject obj = new JSONObject(new String(response.data));

                    Log.d("AddgroupSpace", "ResJsonObj_" + obj);

                    final String status = obj.getString("status");
                    final String returnResponse = obj.getString("response");

                    if (status.equals("success")) {

                        Log.d("AddgroupSpace","InIfPart");

                        Toast.makeText(AddGroupSpace.this,"Group has been added",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AddGroupSpace.this, SkipClientDetailsScreen.class);
                        intent.putExtra("clientName",clientName);
                        intent.putExtra("state",state);
                        intent.putExtra("appCid",appCid);
                        intent.putExtra("clientImagePath",clientImagePath);
                        intent.putExtra("clientLogoPath",clientLogoPath);
                        intent.putExtra("FRAGMENT_ID","1");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);



                    } else if (status.equals("failure")){
                        Toast.makeText(AddGroupSpace.this,returnResponse,Toast.LENGTH_SHORT).show();
                    }else {

                        Log.d("AddgroupSpace","InElsePart");

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

                        //Toast.makeText(AddGroup.this, "Data Upload Failed !", Toast.LENGTH_SHORT).show();
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
                String groupImage = "SabPaisa_GroupImage";
                String groupLogo = "SabPaisa_GroupLogo";

                if (group_image != null)
                    params.put("group_image", new DataPart(groupImage + ".jpeg", getFileDataFromDrawable(group_image)));
//                else{
//                    params.put("feed_image",null);
//                }
                if(group_logo != null)
                    params.put("group_logo", new DataPart(groupLogo + ".jpeg", getFileDataFromDrawable(group_logo)));
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
            Log.v("AddgroupSpace", "Internet Connection Not Present");
            return false;
        }
    }




}
