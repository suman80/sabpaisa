package in.sabpaisa.droid.sabpaisa;

import android.app.AlertDialog;
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

public class EditGroup extends AppCompatActivity {

    Toolbar toolbar;
    String GroupsNm,GroupId,GroupsDiscription,GroupsImg,groupLogo;
    EditText editText_GroupName,editText_GroupDescription;
    ImageView img_GroupImage,img_GroupLogo;

    String imageUrl;

    Bitmap groupImageBitMap,groupLogoBitMap;

    Button btn_Cancel,btn_Save;

    String userAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_edit_group);

        GroupsNm = getIntent().getStringExtra("groupName");
        GroupId = getIntent().getStringExtra("groupId");
        GroupsDiscription = getIntent().getStringExtra("groupText");
        GroupsImg = getIntent().getStringExtra("groupImage");
        groupLogo = getIntent().getStringExtra("groupLogo");

        Log.d("EditGroup","IntentValues_"+GroupsNm+" "+GroupId+" "+GroupsDiscription+" "+GroupsImg+" "+groupLogo);


        toolbar = (Toolbar)findViewById(R.id.toolbar);

        toolbar.setTitle(GroupsNm);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_action_previousback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        editText_GroupName = (EditText)findViewById(R.id.editText_GroupName);
        editText_GroupDescription = (EditText)findViewById(R.id.editText_GroupDescription);
        img_GroupImage = (ImageView)findViewById(R.id.img_GroupImage);
        img_GroupLogo = (ImageView)findViewById(R.id.img_GroupLogo);
        btn_Cancel = (Button)findViewById(R.id.btn_Cancel);
        btn_Save = (Button)findViewById(R.id.btn_Save);

        editText_GroupName.setText(GroupsNm);
        editText_GroupDescription.setText(GroupsDiscription);

        Glide.with(getApplicationContext())
                .load(GroupsImg)
                .error(R.drawable.appicon)
                .into(img_GroupImage);

        Glide.with(getApplicationContext())
                .load(groupLogo)
                .error(R.drawable.appicon)
                .into(img_GroupLogo);

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        userAccessToken = sharedPreferences.getString("response", "123");


        img_GroupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

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
                    AlertDialog alertDialog = new AlertDialog.Builder(EditGroup.this, R.style.MyDialogTheme).create();

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
                    updateGroup(groupImageBitMap,groupLogoBitMap,groupNm,groupDesc);
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
                img_GroupImage.setImageBitmap(android.provider.MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));

                groupImageBitMap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg);

                Log.d("feedImageBitMap"," "+groupImageBitMap);

                if (groupImageBitMap == null){
                    Toast.makeText(EditGroup.this, "Invalid File Format", Toast.LENGTH_SHORT).show();
                    img_GroupImage.setImageDrawable(getResources().getDrawable(R.drawable.appicon));
                }


            }else if (requestCode == 300 && resultCode == RESULT_OK && data != null){

                Uri selectedimg = data.getData();

                //android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                img_GroupLogo.setImageBitmap(android.provider.MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));

                groupLogoBitMap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg);

                Log.d("feedImageBitMap"," "+groupLogoBitMap);

                if (groupLogoBitMap == null){
                    Toast.makeText(EditGroup.this, "Invalid File Format", Toast.LENGTH_SHORT).show();
                    img_GroupLogo.setImageDrawable(getResources().getDrawable(R.drawable.appicon));
                }


            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }


    }


    private void updateGroup(final Bitmap group_image,final Bitmap group_logo,final String groupName,final String groupText) {

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_updateGroup + "?group_Id="+GroupId+"&group_text="+URLEncoder.encode(groupText)+"&group_name="+URLEncoder.encode(groupName)+"&admin="+userAccessToken;

        Log.d("EditGroup","_URL "+url);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Log.d("EditGroup", "Res_" + response);
                try {

                    JSONObject obj = new JSONObject(new String(response.data));

                    Log.d("EditGroup", "ResJsonObj_" + obj);

                    final String status = obj.getString("status");
                    final String returnResponse = obj.getString("response");

                    if (status.equals("success")) {

                        Log.d("EditGroup","InIfPart");

                        //Toast.makeText(EditGroup.this,"Group has been Edited",Toast.LENGTH_SHORT).show();

                        AlertDialog alertDialog = new AlertDialog.Builder(EditGroup.this, R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Edit Group");

                        // Setting Dialog Message
                        alertDialog.setMessage("Group has been Edited");


                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String clientImageURLPath = FullViewOfClientsProceed.clientImageURLPath;

                                Log.d("EditGroup","clientImageURLPath "+clientImageURLPath);

                                Intent intent = new Intent(EditGroup.this,FullViewOfClientsProceed.class);
                                intent.putExtra("clientImagePath",clientImageURLPath);
                                intent.putExtra("FRAGMENT_ID","1");
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                MainGroupAdapter1.isClicked = false;

                                startActivity(intent);
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();


                    } else if (status.equals("failed")){
                        Toast.makeText(EditGroup.this,returnResponse,Toast.LENGTH_SHORT).show();
                    }else {

                        Log.d("EditGroup","InElsePart");

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

                        //Toast.makeText(EditGroup.this, "Data Upload Failed !", Toast.LENGTH_SHORT).show();
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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
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
            Log.v("EditGroup", "Internet Connection Not Present");
            return false;
        }
    }






}
