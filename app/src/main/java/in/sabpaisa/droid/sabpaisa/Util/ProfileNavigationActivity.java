package in.sabpaisa.droid.sabpaisa.Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.Model.ProfileModel;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.RegisterActivity;

public class ProfileNavigationActivity extends AppCompatActivity {
    private static final String TAG = ProfileNavigationActivity.class.getSimpleName();
    ImageView userImage;
    TextView userName,uin, userType, numberEdit, mailIdEdit,addressEdit;
    EditText mNumber, mailId,et_address;
    Toolbar toolbar;
    LinearLayout layout;
    String userAccessToken;
    String address;    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_profile_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        layout = (LinearLayout)findViewById(R.id.ll_profile);
        userImage = (ImageView) findViewById(R.id.iv_userImage);
        userName = (TextView)findViewById(R.id.tv_userName);
        //uin = (TextView) findViewById(R.id.tv_uin);
        //userType = (TextView) findViewById(R.id.tv_userType);
        numberEdit = (TextView) findViewById(R.id.tv_numberEdit);
        mailIdEdit = (TextView) findViewById(R.id.tv_mailIdEdit);
        mNumber = (EditText) findViewById(R.id.et_mNumber);
        mailId = (EditText) findViewById(R.id.et_mailId);
        et_address = (EditText)findViewById(R.id.et_address);
        addressEdit = (TextView) findViewById(R.id.tv_addressEdit);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);

        toolbar.setTitle("Profile");

        //layout.setVisibility(View.GONE);

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        userAccessToken = sharedPreferences.getString("response", "123");

        Log.d(TAG,"userAccessToken "+userAccessToken);

        mNumber.setEnabled(false);
        mailId.setEnabled(false);

        numberEdit.setOnClickListener(new View.OnClickListener() {
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
        });

        mailIdEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mailId.setFocusable(true);
                if (mailIdEdit.getText().toString().equals("Edit")) {
                    mailId.setEnabled(true);
                    mailId.requestFocus();
                    mailIdEdit.setText("Save");
                } else {
                    mailId.setEnabled(false);
                    mailIdEdit.setText("Edit");
                }
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        showProfileData();
        showProfileImage();
/*
        addressEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mailId.setFocusable(true);
                if (addressEdit.getText().toString().equals("Edit")) {
                    et_address.setEnabled(true);
                    et_address.requestFocus();
                    addressEdit.setText("Save");
                } else {
                    et_address.setEnabled(false);
                    addressEdit.setText("Edit");
                }
            }
        });*/

        addressEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfileData(userAccessToken,address);
            }
        });

    }



    public void pickImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 200);

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (resultCode == RESULT_OK) {
                if (requestCode == 200) {

                    //Get ImageURi and load with help of picasso
                    //Uri selectedImageURI = data.getData();
//                    userImage.setImageDrawable(data.getData());
                    Uri selectedimg = data.getData();
                    userImage.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));
                }

            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }


    private void showProfileData() {

        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.URL_Show_UserProfile+"?token="+userAccessToken, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                Log.d(TAG, "Register Response: " + response1.toString());

                try {
                    //progressBar.setVisibility(View.GONE);
                    JSONObject object = new JSONObject(response1);
                    String response = object.getString("response");
                    String status =object.getString("status");

                    if (status.equals("success")) {
                        userName.setText(object.getJSONObject("response").getString("fullName").toString());
                        mNumber.setText(object.getJSONObject("response").getString("contactNumber").toString());
                        mailId.setText(object.getJSONObject("response").getString("emailId").toString());
                        et_address.setText(object.getJSONObject("response").getString("address").toString());
                        Log.d(TAG, "userName" + userName);

                    }else {
                        Toast.makeText(getApplicationContext(),"Could  not able to load data",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage()==null ||error instanceof TimeoutError || error instanceof NoConnectionError) {
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



    private void showProfileImage() {

        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.URL_Show_UserProfileImage+"?token="+userAccessToken, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                Log.d(TAG, "Register Response: " + response1.toString());

                try {
                    progressBar.setVisibility(View.GONE);
                    JSONObject object = new JSONObject(response1);
                    String response = object.getString("response");
                    String status =object.getString("status");

                    if (status.equals("success")) {

                        String image =object.getJSONObject("response").getString("userImageUrl");
                        new DownloadImageTask(userImage).execute(image);

                    }else {
                        Toast.makeText(getApplicationContext(),"Cannot able to load image!",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage()==null ||error instanceof TimeoutError || error instanceof NoConnectionError) {
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





    //private void updateProfileData(final String userAccessToken,final String address,final String userImage,final String contactNumber ) {
    private void updateProfileData(final String  userAccessToken, final String address ) {

        String tag_string_req = "req_register";



        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UserProfileUpdate, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                Log.d(TAG, "Register Response: " + response1.toString());

                try {
                    //progressBar.setVisibility(View.GONE);
                    JSONObject object = new JSONObject(response1);
                    String response = object.getString("response");
                    String status =object.getString("status");

                    if (status!=null && status.equals("success")) {

                        ProfileModel profile =new ProfileModel();
                        profile.setContactNumber(object.getString("contactNumber"));
                        profile.setAddress(object.getString("address"));
                        String imageByteCode = object.getString("userImage");

                        mNumber.setText(profile.getContactNumber());
                        et_address.setText(profile.getAddress());

                        byte[] imgData = Base64.decode(imageByteCode, Base64.DEFAULT);
                        Bitmap bmp = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
                        userImage.setImageBitmap(bmp);
                        layout.setVisibility(View.VISIBLE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage()==null ||error instanceof TimeoutError || error instanceof NoConnectionError) {
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
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("token",userAccessToken);
                params.put("address", address);

                return params;
            }

        };

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





}
