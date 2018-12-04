package in.sabpaisa.droid.sabpaisa.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.google.firebase.crash.FirebaseCrash;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.AppDB.AppDB;
import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.MainActivity;
import in.sabpaisa.droid.sabpaisa.MainActivitySkip;
import in.sabpaisa.droid.sabpaisa.Model.ProfileModel;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.RegisterActivity;
import in.sabpaisa.droid.sabpaisa.UIN;

import static in.sabpaisa.droid.sabpaisa.AppDB.AppDB.TABLE_USER_IMG;
import static in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments.TABLE_NAME_MEMBERS;
import static in.sabpaisa.droid.sabpaisa.R.color.black;

public class ProfileNavigationActivity extends AppCompatActivity {
    private static final String TAG = ProfileNavigationActivity.class.getSimpleName();
    ImageView userImage;
    Button numberEdit, mailIdEdit, addressEdit, tv_NameEdit;
    TextView userName, mNumber;
    EditText mailId, et_address, et_UserName;
    Toolbar toolbar;
    LinearLayout layout;
    String userAccessToken;
    String address, email, name;
    String x;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    String clientName, state, clientImageURLPath;
    String clientId;
    String userImageUrl;
    public static String MYSHAREDPREFPNA = "mySharedPrefPNA";
    ProgressDialog progressDialog;
    int val;
    Bitmap bitmap;
    /////////////DB///////////////////////

    AppDB db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_profile_navigation);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        layout = (LinearLayout) findViewById(R.id.ll_profile);
        userImage = (ImageView) findViewById(R.id.iv_userImage);
        userName = (TextView) findViewById(R.id.tv_userName);
        //uin = (TextView) findViewById(R.id.tv_uin);
        //userType = (TextView) findViewById(R.id.tv_userType);
        //numberEdit = (Button) findViewById(R.id.tv_numberEdit);
        mailIdEdit = (Button) findViewById(R.id.tv_mailIdEdit);
        //mailIdEdit.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
        tv_NameEdit = (Button) findViewById(R.id.tv_NameEdit);

        mNumber = (TextView) findViewById(R.id.et_mNumber);
        mailId = (EditText) findViewById(R.id.et_mailId);
        et_address = (EditText) findViewById(R.id.et_address);
        et_UserName = (EditText) findViewById(R.id.et_UserName);


        addressEdit = (Button) findViewById(R.id.tv_addressEdit);


        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        ///////////////////////DB/////////////////////////////////
        db = new AppDB(ProfileNavigationActivity.this);


        Intent intent = getIntent();
        clientName = intent.getStringExtra("clientName");
        state = intent.getStringExtra("state");
        clientImageURLPath = getIntent().getStringExtra("clientImagePath");

        Log.d("ProfileLOGs", "" + clientId + " " + "  " + clientImageURLPath + "  " + state);

        toolbar.setTitle("Profile");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_action_previousback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        //layout.setVisibility(View.GONE);

        sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        userAccessToken = sharedPreferences.getString("response", "123");

        Log.d(TAG, "userAccessToken " + userAccessToken);

        SharedPreferences.Editor editor = getSharedPreferences(MYSHAREDPREFPNA, MODE_PRIVATE).edit();
        editor.putString("response", userAccessToken);
        editor.commit();
        sharedPreferences = getApplication().getSharedPreferences(UIN.MYSHAREDPREFUIN, Context.MODE_PRIVATE);
        clientId = sharedPreferences.getString("clientId", "abc");
        Log.d("clintidprofile", "---" + clientId);
        val = getIntent().getIntExtra("valueProfile", 0);
        Log.d("vaalueeProfile", "" + val);


        mNumber.setEnabled(false);
        mailId.setEnabled(false);
        et_address.setEnabled(false);
        et_UserName.setEnabled(false);


       /* numberEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mailId.setFocusable(true);
                if (numberEdit.getText().toString().equals("Edit")) {
                    mNumber.setEnabled(true);
                    mNumber.requestFocus();
                    numberEdit.setText("Save");
                } else {
                    mNumber.setEnabled(false);
                    numberEdit.setText("Edit");
                }
            }
        });*/
//Added on 2nd Feb
        mailIdEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                mailId.setFocusable(true);
                if (mailIdEdit.getText().toString().equals("Edit")) {

                    mailId.setEnabled(true);
                    //mailId.setText(" ");
                    mailId.requestFocus();
                    mailIdEdit.setText("Save");
                } else if (mailIdEdit.getText().toString().equals("Save")) {
                    //Toast.makeText(getApplication(), "Please wait for a popup.Once, It will notify that data is updated", Toast.LENGTH_LONG).show();

                    if (isValidEmail(mailId.getText().toString().trim())) {
                        email = mailId.getText().toString().trim();
                        updateUserProfileEmail(userAccessToken, email);
                        mailId.setFocusable(false);
                    } else {
                        Toast.makeText(ProfileNavigationActivity.this, "Please enter correct email id", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    mailId.setEnabled(false);
                    mailIdEdit.setText("Edit");
                }
            }
        });


        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplication(), "Please wait for a popup.Once, It will notify that data is updated", Toast.LENGTH_LONG).show();


                pickImage();

            }
        });

        addressEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addressEdit.getText().toString().equals("Edit")) {

                    et_address.setEnabled(true);
                    //et_address.setText(" ");
                    et_address.requestFocus();
                    addressEdit.setText("Save");

                } else if (addressEdit.getText().toString().equals("Save")) {
                    //Toast.makeText(getApplication(), "Please wait for a popup.Once, It will notify that data is updated", Toast.LENGTH_LONG).show();

                    address = et_address.getText().toString();
                    updateUserProfileAddress(userAccessToken, address);
                    et_address.setFocusable(false);
                } else {
                    et_address.setEnabled(false);
                    addressEdit.setText("Edit");
                }


            }
        });


        tv_NameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tv_NameEdit.getText().toString().equals("Edit")) {

                    et_UserName.setEnabled(true);
                    et_UserName.requestFocus();
                    Log.d("Usernameedit", "-->");
                    tv_NameEdit.setText("Save");

                } else if (tv_NameEdit.getText().toString().equals("Save")) {
                    //Toast.makeText(getApplication(), "Please wait for a popup.Once, It will notify that data is updated", Toast.LENGTH_LONG).show();

                    name = et_UserName.getText().toString().trim();

                    if (!TextUtils.isEmpty(name)) {

                        Log.d("Usernameedit11", "-->");

                        updateUserProfileName(userAccessToken, name);

                        et_UserName.setFocusable(false);

                    } else {
                        Toast.makeText(ProfileNavigationActivity.this, "Please fill your name !", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    et_UserName.setEnabled(false);
                    Log.d("Usernameedit22", "-->");

                    tv_NameEdit.setText("Edit");
                }


            }
        });


        showProfileData();
        progressBar.setVisibility(View.VISIBLE);

        if (isOnline()) {
            showProfileImage();
        } else {
            progressBar.setVisibility(View.GONE);
            Cursor res = db.getParticularImageData(userAccessToken);

            if (res.getCount() > 0) {
                StringBuffer stringBuffer = new StringBuffer();
                while (res.moveToNext()) {
                    stringBuffer.append(res.getString(0) + " ");
                    stringBuffer.append(res.getString(1) + " ");
                    stringBuffer.append(res.getString(2) + " ");
                    loadImageFromStorage(res.getString(2));
                }
                Log.d("getImgFrmDB", "-->" + stringBuffer);

            } else {

                Log.d("In Else Part", "");
                //Toast.makeText(ProfileNavigationActivity.this,"In Else Part",Toast.LENGTH_SHORT).show();

            }
        }


    }


    @Override
    public void onBackPressed() {

        finish();
       /* if (val == 1) {
            Intent intent = new Intent(ProfileNavigationActivity.this, MainActivity.class);
            intent.putExtra("clientId", clientId);
            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (val == 2) {
            Intent intent = new Intent(ProfileNavigationActivity.this, MainActivitySkip.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        } else if (val == 3) {
            Intent intent = new Intent(ProfileNavigationActivity.this, FullViewOfClientsProceed.class);
            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            intent.putExtra("clientId", clientId);
            intent.putExtra("clientImagePath", clientImageURLPath);
            intent.putExtra("clientName", clientName);
            intent.putExtra("state", state);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }*/

    }

    public void pickImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("userImageUrl", userImageUrl);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 200);


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == 200 && resultCode == RESULT_OK && data != null) {


                //Get ImageURi and load with help of picasso
                //Uri selectedImageURI = data.getData();
//                    userImage.setImageDrawable(data.getData());
                Uri selectedimg = data.getData();

                //android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                userImage.setImageBitmap(android.provider.MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));
                Bitmap userImg = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg);

                uploadBitmap(userImg);

                progressDialog = new ProgressDialog(ProfileNavigationActivity.this);
                progressDialog.setMessage("Loading Please Wait ...");
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);

            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

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


    private void uploadBitmap(final Bitmap bitmap) {

        Log.d(TAG, "IMG_userAccessToken" + userAccessToken);
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_UserProfileImageUpdate + "?token=" + userAccessToken,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d(TAG, "IMG_Res" + response);
                        try {

                            JSONObject obj = new JSONObject(new String(response.data));
                            Log.d(TAG, "IMG_Res" + obj);
                            final String status = obj.getString("status");

                            if (status.equals("success")) {

                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                    progressDialog.setCanceledOnTouchOutside(true);
                                    progressDialog.setCancelable(true);
                                }


                                AlertDialog alertDialog = new AlertDialog.Builder(ProfileNavigationActivity.this, R.style.MyDialogTheme).create();

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

                                      /*  Intent intent = new Intent(ProfileNavigationActivity.this,ProfileNavigationActivity.class);
                                        startActivity(intent);*/
                                    }
                                });

                                // Showing Alert Message
                                alertDialog.show();

                            } else {
                                Toast.makeText(ProfileNavigationActivity.this, "Image Upload Failed !", Toast.LENGTH_SHORT).show();
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                    progressDialog.setCanceledOnTouchOutside(true);
                                    progressDialog.setCancelable(true);
                                }
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
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog.setCanceledOnTouchOutside(true);
                            progressDialog.setCancelable(true);
                        }
                        Toast.makeText(ProfileNavigationActivity.this, "Image Upload Failed !", Toast.LENGTH_SHORT).show();
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


    private void showProfileData() {


        Cursor res = db.getParticularData(userAccessToken);

        if (res.getCount() > 0) {
            StringBuffer stringBuffer = new StringBuffer();
            while (res.moveToNext()) {
                stringBuffer.append(res.getString(0));
                stringBuffer.append(res.getString(1));
                stringBuffer.append(res.getString(2));
                stringBuffer.append(res.getString(3));
                stringBuffer.append(res.getString(4));
                stringBuffer.append(res.getString(5));
                userName.setText(res.getString(1));
                et_UserName.setText(res.getString(1));
                mailId.setText(res.getString(2));
                et_address.setText(res.getString(3));
                mNumber.setText(res.getString(4));
            }
            Log.d("getParticularNum", "-->" + stringBuffer);

        } else {


            String tag_string_req = "req_register";

            StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_Show_UserProfile + "?token=" + userAccessToken, new Response.Listener<String>() {

                @Override
                public void onResponse(String response1) {
                    Log.d(TAG, "Register Response: " + response1.toString());

                    try {
                        //progressBar.setVisibility(View.GONE);
                        JSONObject object = new JSONObject(response1);
                        String response = object.getString("response");
                        String status = object.getString("status");

                        if (status.equals("success")) {
                            userName.setText(object.getJSONObject("response").getString("fullName").toString());
                            mNumber.setText(object.getJSONObject("response").getString("contactNumber").toString());
                            x = object.getJSONObject("response").getString("emailId").toString();
                            if (x.equals("null")) {
                                mailId.setText("");
                            } else {
                                mailId.setText(x);
                            }
                            String a = object.getJSONObject("response").getString("address").toString();
                            if (a.equals("null")) {
                                et_address.setText("");
                            } else {
                                et_address.setText(a);
                            }

                            et_UserName.setText(object.getJSONObject("response").getString("fullName").toString());
                            Log.d(TAG, "userName" + userName);

                            //////////////////////////////LOCAL DB//////////////////////////////////////
                            String Name = object.getJSONObject("response").getString("fullName").toString();
                            String Email = object.getJSONObject("response").getString("emailId").toString();
                            String Address = object.getJSONObject("response").getString("address").toString();
                            String number = object.getJSONObject("response").getString("contactNumber").toString();

                            boolean isInserted = db.insertData(Name, Email, Address, number, userAccessToken);

                            if (isInserted == true) {

                                //Toast.makeText(ProfileNavigationActivity.this, "Data  Inserted", Toast.LENGTH_SHORT).show();

                                Log.d("ProfileNavigation", "LocalDBInIfPart" + isInserted);

                            } else {
                                Log.d("ProfileNavigation", "LocalDBInElsePart" + isInserted);
                                //Toast.makeText(ProfileNavigationActivity.this, "Data  Not Inserted", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(getApplicationContext(), "Could  not able to load data", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    if (error.getMessage() == null || error instanceof TimeoutError || error instanceof NoConnectionError) {
                        AlertDialog alertDialog = new AlertDialog.Builder(ProfileNavigationActivity.this, R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Network/Connection Error");

                        // Setting Dialog Message
                        alertDialog.setMessage("Internet Connection is poor OR The Server is taking too long to respond.Please try again later.Thank you.");

                        // Setting Icon to Dialog
                        //  alertDialog.setIcon(R.drawable.tick);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog closed
                                // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();
                        Log.e(TAG, "Update Error: " + error.getMessage());

                    } else if (error instanceof AuthFailureError) {
                        //TODO
                    } else if (error instanceof ServerError) {
                        //TODO
                    } else if (error instanceof NetworkError) {
                        //TODO
                    } else if (error instanceof ParseError) {
                        //TODO
                    }

                }
            }); /*{

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userAccessToken", userAccessToken);

                return params;
            }

        };
*/
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }

    }


    private void showProfileImage() {


        boolean checkDb = db.isTableExists(TABLE_USER_IMG);

        Log.d("DbValuePNA", " " + checkDb);

        if (checkDb == true) {
            db.deleteAllImageData();
        }

        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_Show_UserProfileImage + "?token=" + userAccessToken, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                Log.d(TAG, "Register Response: " + response1.toString());

                try {
                    JSONObject object = new JSONObject(response1);
                    String response = object.getString("response");
                    String status = object.getString("status");

                    if (status.equals("success")) {
                        //Toast.makeText(getApplication(), "Please wait for a popup.Once, It will notify that data is updated", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        userImageUrl = object.getJSONObject("response").getString("userImageUrl");
                        //new DownloadImageTask(userImage).execute(userImageUrl);
                        Glide.with(ProfileNavigationActivity.this)
                                .load(userImageUrl)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .error(R.drawable.default_users)
                                .into(userImage);

                        new imageDownloader().execute(userImageUrl);


                    } else {
                        progressBar.setVisibility(View.GONE);
                        // Toast.makeText(getApplicationContext(), "Cannot able to load image!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                if (error.getMessage() == null || error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ProfileNavigationActivity.this, R.style.MyDialogTheme).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Network/Connection Error");

                    // Setting Dialog Message
                    alertDialog.setMessage("Internet Connection is poor OR The Server is taking too long to respond.Please try again later.Thank you.");

                    // Setting Icon to Dialog
                    //  alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed
                            // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                        }
                    });

                    //Added on 24 oct 2018

                    try {

                        //show dialog

                        // Showing Alert Message

                        alertDialog.show();

                    }catch (WindowManager.BadTokenException e){
                        Log.d("PNA","WindowManager "+e);
                    }



                    Log.e(TAG, "Update Error: " + error.getMessage());

                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                } else if (error instanceof NetworkError) {
                    //TODO
                } else if (error instanceof ParseError) {
                    //TODO
                }

            }
        }); /*{

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userAccessToken", userAccessToken);

                return params;
            }

        };
*/
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    private void updateUserProfileName(final String userAccessToken, final String name) {

        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_UserProfileUpdate + "?token=" + userAccessToken + "&" + "fullName=" + name.trim().replace(" ", "%20"), new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                Log.d(TAG, "Register Response: " + response1.toString());

                try {
                    //progressBar.setVisibility(View.GONE);
                    JSONObject object = new JSONObject(response1);
                    String response = object.getString("response");
                    String status = object.getString("status");

                    if (status != null && status.equals("success")) {

                        final ProfileModel profile = new ProfileModel();

                        profile.setFullName(response);

                        AlertDialog alertDialog = new AlertDialog.Builder(ProfileNavigationActivity.this, R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Name Update");

                        // Setting Dialog Message
                        alertDialog.setMessage("Your Name Has Been Updated");

                        // Setting Icon to Dialog
                        //  alertDialog.setIcon(R.drawable.tick);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ProfileNavigationActivity.this, ProfileNavigationActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });


                        // Showing Alert Message
                        alertDialog.show();

                        ////////////////Update in Local Db also/////////////////////////////////////
                        boolean isUpdated = db.updateNameStatus(userAccessToken, name);
                        if (isUpdated == true) {

                            //Toast.makeText(ProfileNavigationActivity.this, "Data  Inserted", Toast.LENGTH_SHORT).show();

                            Log.d("ProfileNavigation", "LocalDBInIfPart" + isUpdated);

                        } else {
                            Log.d("ProfileNavigation", "LocalDBInElsePart" + isUpdated);
                            //Toast.makeText(ProfileNavigationActivity.this, "Data  Not Inserted", Toast.LENGTH_SHORT).show();
                        }


                    } else if (status.equals("failure")) {
                        AlertDialog alertDialog = new AlertDialog.Builder(ProfileNavigationActivity.this, R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Name Update");

                        // Setting Dialog Message
                        alertDialog.setMessage("Your Name  is already exists!");

                        // Setting Icon to Dialog
                        //  alertDialog.setIcon(R.drawable.tick);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(ProfileNavigationActivity.this, ProfileNavigationActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Oops! Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ProfileNavigationActivity.this, R.style.MyDialogTheme).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Network/Connection Error");

                    // Setting Dialog Message
                    alertDialog.setMessage("Internet Connection is poor OR The Server is taking too long to respond.Please try again later.Thank you.");

                    // Setting Icon to Dialog
                    //  alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed
                            // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                    Log.e(TAG, "Update Error: " + error.getMessage());

                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                } else if (error instanceof NetworkError) {
                    //TODO
                } else if (error instanceof ParseError) {
                    //TODO
                }

            }
        });/*{

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("token",userAccessToken);
                params.put("address", address);

                return params;
            }

        };*/

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    private void updateUserProfileAddress(final String userAccessToken, final String address) {

        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_UserProfileUpdate + "?token=" + userAccessToken + "&" + "address=" + address.trim().replace(" ", "%20"), new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                Log.d(TAG, "Register Response: " + response1.toString());

                try {
                    //progressBar.setVisibility(View.GONE);
                    JSONObject object = new JSONObject(response1);
                    String response = object.getString("response");
                    String status = object.getString("status");

                    if (status != null && status.equals("success")) {

                        final ProfileModel profile = new ProfileModel();

                        profile.setAddress(response);

                        AlertDialog alertDialog = new AlertDialog.Builder(ProfileNavigationActivity.this, R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Address Update");

                        // Setting Dialog Message
                        alertDialog.setMessage("Your Address Has Been Updated");

                        // Setting Icon to Dialog
                        //  alertDialog.setIcon(R.drawable.tick);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ProfileNavigationActivity.this, ProfileNavigationActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();


                        ////////////////Update in Local Db also/////////////////////////////////////
                        boolean isUpdated = db.updateAddressStatus(userAccessToken, address);
                        if (isUpdated == true) {

                            //Toast.makeText(ProfileNavigationActivity.this, "Data  Inserted", Toast.LENGTH_SHORT).show();

                            Log.d("ProfileNavigation", "LocalDBInIfPart" + isUpdated);

                        } else {
                            Log.d("ProfileNavigation", "LocalDBInElsePart" + isUpdated);
                            //Toast.makeText(ProfileNavigationActivity.this, "Data  Not Inserted", Toast.LENGTH_SHORT).show();
                        }


                    } else if (status.equals("failure")) {
                        AlertDialog alertDialog = new AlertDialog.Builder(ProfileNavigationActivity.this, R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Address Update");

                        // Setting Dialog Message
                        alertDialog.setMessage("Your Address  is already exists!");

                        // Setting Icon to Dialog
                        //  alertDialog.setIcon(R.drawable.tick);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(ProfileNavigationActivity.this, ProfileNavigationActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Oops! Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ProfileNavigationActivity.this, R.style.MyDialogTheme).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Network/Connection Error");

                    // Setting Dialog Message
                    alertDialog.setMessage("Internet Connection is poor OR The Server is taking too long to respond.Please try again later.Thank you.");

                    // Setting Icon to Dialog
                    //  alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed
                            // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                    Log.e(TAG, "Update Error: " + error.getMessage());

                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                } else if (error instanceof NetworkError) {
                    //TODO
                } else if (error instanceof ParseError) {
                    //TODO
                }

            }
        });/*{

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("token",userAccessToken);
                params.put("address", address);

                return params;
            }

        };*/

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    private void updateUserProfileEmail(final String userAccessToken, final String email) {

        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_UserProfileUpdate + "?token=" + userAccessToken + "&" + "emailId=" + email, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                Log.d(TAG, "Register Response: " + response1.toString());

                try {
                    //progressBar.setVisibility(View.GONE);
                    JSONObject object = new JSONObject(response1);
                    String response = object.getString("response");
                    String status = object.getString("status");

                    if (status != null && status.equals("success")) {
                        //mailIdEdit.setText("Edit");
                        final ProfileModel profile = new ProfileModel();

                        profile.setAddress(response);


                        AlertDialog alertDialog = new AlertDialog.Builder(ProfileNavigationActivity.this, R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Email Update");

                        // Setting Dialog Message
                        alertDialog.setMessage("Your Email Has Been Updated");

                        // Setting Icon to Dialog
                        //  alertDialog.setIcon(R.drawable.tick);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ProfileNavigationActivity.this, ProfileNavigationActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();


                        ////////////////Update in Local Db also/////////////////////////////////////
                        boolean isUpdated = db.updateEmailStatus(userAccessToken, email);
                        if (isUpdated == true) {

                            //Toast.makeText(ProfileNavigationActivity.this, "Data  Inserted", Toast.LENGTH_SHORT).show();

                            Log.d("ProfileNavigation", "LocalDBInIfPart" + isUpdated);

                        } else {
                            Log.d("ProfileNavigation", "LocalDBInElsePart" + isUpdated);
                            //Toast.makeText(ProfileNavigationActivity.this, "Data  Not Inserted", Toast.LENGTH_SHORT).show();
                        }


                    } else if (status.equals("failure")) {
                        AlertDialog alertDialog = new AlertDialog.Builder(ProfileNavigationActivity.this, R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Email Update");

                        // Setting Dialog Message
                        alertDialog.setMessage("Your email id  is already exists!");

                        // Setting Icon to Dialog
                        //  alertDialog.setIcon(R.drawable.tick);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(ProfileNavigationActivity.this, ProfileNavigationActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Oops! Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage() == null || error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ProfileNavigationActivity.this, R.style.MyDialogTheme).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Network/Connection Error");

                    // Setting Dialog Message
                    alertDialog.setMessage("Internet Connection is poor OR The Server is taking too long to respond.Please try again later.Thank you.");

                    // Setting Icon to Dialog
                    //  alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed
                            // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                    Log.e(TAG, "Update Error: " + error.getMessage());

                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                } else if (error instanceof NetworkError) {
                    //TODO
                } else if (error instanceof ParseError) {
                    //TODO
                }

            }
        });/*{

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("token",userAccessToken);
                params.put("address", address);

                return params;
            }

        };*/

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    /*private String setUserName(ProfileModel profile) {
        String name = null;
        if(!profile.getFirstName().equals("null")){
            name = profile.getFirstName();
        }
        if (!profile.getLastName().equals("null")){
            name = name + " "+ profile.getLastName();
        }
        if (name.equals("null")){
            return "Not available";
        }
        return name;
    }*/


    //Code for fetching image from server
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loading.show();
        }

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
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


    public final static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    private class imageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... url) {

            String myUrl = url[0];


            try {
                InputStream inputStream = new URL(myUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                //image = getBytes(bitmap);

                //Log.d("ImgBytesInTry"," "+image.toString());

                saveToInternalStorage(userAccessToken, bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
//            logo = getBytes(bitmap);
            //userImage.setImageBitmap(bitmap);
            Log.d("ImgBitMapValue", " " + bitmap);
        }
    }


    private String saveToInternalStorage(String userAccessToken, Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, (System.currentTimeMillis() / 1000) + "img.jpg");

        Log.d("mypath", "mypath  " + mypath);


        //////////////////////////////LOCAL DB//////////////////////////////////////

        boolean isInserted = db.insertUserImageData(userAccessToken, mypath.toString());
        if (isInserted == true) {

            //Toast.makeText(MainActivity.this, "Data  Inserted", Toast.LENGTH_SHORT).show();

            Log.d("ProfileNavigationIMG", "LocalDBInIfPart" + isInserted);

        } else {
            Log.d("ProfileNavigationIMG", "LocalDBInElsePart" + isInserted);
            //Toast.makeText(MainActivity.this, "Data  Not Inserted", Toast.LENGTH_SHORT).show();
        }


        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private void loadImageFromStorage(String path) {

        try {
            //File f=new File(path, "profile.jpg");
            File f = new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            //ImageView img=(ImageView)findViewById(R.id.imgPicker);
            //.setImageBitmap(b);

            userImage.setImageBitmap(b);

            Log.d("b", " " + b);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v(TAG, "Internet Connection Not Present");
            return false;
        }
    }


}