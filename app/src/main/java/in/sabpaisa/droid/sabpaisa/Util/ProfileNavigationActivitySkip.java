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
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.MainActivitySkip;
import in.sabpaisa.droid.sabpaisa.Model.ProfileModel;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.UIN;

public class ProfileNavigationActivitySkip extends AppCompatActivity {
    private static final String TAG = ProfileNavigationActivitySkip.class.getSimpleName();
    ImageView userImage;
    Button numberEdit, mailIdEdit,addressEdit;
    TextView userName;
    EditText mNumber, mailId,et_address;
    Toolbar toolbar;
    LinearLayout layout;
    String userAccessToken;
    String address,email;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    String clientId;
    String userImageUrl;
    public static String MYSHAREDPREFPNA="mySharedPrefPNA";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_profile_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        layout = (LinearLayout)findViewById(R.id.ll_profile);
        userImage = (ImageView) findViewById(R.id.iv_userImage);
        userName = (TextView) findViewById(R.id.tv_userName);
        //uin = (TextView) findViewById(R.id.tv_uin);
        //userType = (TextView) findViewById(R.id.tv_userType);
        numberEdit = (Button) findViewById(R.id.tv_numberEdit);
        mailIdEdit = (Button) findViewById(R.id.tv_mailIdEdit);
        mailIdEdit.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
        mNumber = (EditText) findViewById(R.id.et_mNumber);
        mailId = (EditText) findViewById(R.id.et_mailId);
        et_address = (EditText)findViewById(R.id.et_address);

        addressEdit = (Button) findViewById(R.id.tv_addressEdit);


        progressBar = (ProgressBar)findViewById(R.id.progress_bar);

        toolbar.setTitle("Profile");

        //layout.setVisibility(View.GONE);

        sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        userAccessToken = sharedPreferences.getString("response", "123");

        Log.d(TAG,"userAccessToken "+userAccessToken);


        sharedPreferences = getApplication().getSharedPreferences(UIN.MYSHAREDPREFUIN, Context.MODE_PRIVATE);
        clientId=sharedPreferences.getString("clientId","abc");


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
                    mailId.setText(" ");
                    mailId.requestFocus();
                    mailIdEdit.setText("Save");
                }else if(mailIdEdit.getText().toString().equals("Save")) {
                    email=mailId.getText().toString();
                    updateUserProfileEmail(userAccessToken,email);
                    mailId.setFocusable(false);
                }
                else {
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

        addressEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addressEdit.getText().toString().equals("Edit")) {
                    et_address.setEnabled(true);
                    et_address.setText(" ");
                    et_address.requestFocus();
                    addressEdit.setText("Save");

                } else if(addressEdit.getText().toString().equals("Save")){
                    address=et_address.getText().toString();
                    updateUserProfileAddress(userAccessToken,address);
                    et_address.setFocusable(false);
                }

                else{
                    et_address.setEnabled(false);
                    addressEdit.setText("Edit");
                }


            }
        });

        showProfileData();
        showProfileImage();


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

Intent intent=new Intent(ProfileNavigationActivitySkip.this, MainActivitySkip.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        /*intent.putExtra("clientId", clientId);
        intent.putExtra("userImageUrl", userImageUrl);
        startActivity(intent);*/
        this.finish();
    }

    public void pickImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("userImageUrl",userImageUrl);
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
                userImage.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));
                Bitmap userImg=MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg);

                uploadBitmap(userImg);
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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    private void uploadBitmap(final Bitmap bitmap) {

        Log.d(TAG,"IMG_userAccessToken"+userAccessToken);
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, AppConfig.URL_UserProfileImageUpdate+"?token="+userAccessToken,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d(TAG,"IMG_Res"+response);
                        try {

                            JSONObject obj = new JSONObject(new String(response.data));
                            Log.d(TAG,"IMG_Res"+obj);
                            final String status = obj.getString("status");
                            if (status.equals("success")) {

                                AlertDialog alertDialog = new AlertDialog.Builder(ProfileNavigationActivitySkip.this, R.style.MyDialogTheme).create();

                                // Setting Dialog Title
                                alertDialog.setTitle("User Image Update");

                                // Setting Dialog Message
                                alertDialog.setMessage("Your Image Has Been Updated successfully !");

                                // Setting Icon to Dialog
                                //  alertDialog.setIcon(R.drawable.tick);

                                // Setting OK Button
                                alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent=new Intent();
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);

                                      /*  Intent intent = new Intent(ProfileNavigationActivitySkip.this,ProfileNavigationActivitySkip.class);
                                        startActivity(intent);*/
                                    }
                                });

                                // Showing Alert Message
                                alertDialog.show();

                            }else {
                                Toast.makeText(getApplicationContext(),"Image Upload Failed !",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                    AlertDialog alertDialog = new AlertDialog.Builder(ProfileNavigationActivitySkip.this, R.style.MyDialogTheme).create();

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

                        userImageUrl =object.getJSONObject("response").getString("userImageUrl");
                        new ProfileNavigationActivitySkip.DownloadImageTask(userImage).execute(userImageUrl);

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
                    AlertDialog alertDialog = new AlertDialog.Builder(ProfileNavigationActivitySkip.this, R.style.MyDialogTheme).create();

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


    private void updateUserProfileAddress(final String  userAccessToken, final String address ) {

        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UserProfileUpdate+"?token="+userAccessToken+"&"+"address="+address, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                Log.d(TAG, "Register Response: " + response1.toString());

                try {
                    //progressBar.setVisibility(View.GONE);
                    JSONObject object = new JSONObject(response1);
                    String response = object.getString("response");
                    String status =object.getString("status");

                    if (status!=null && status.equals("success")) {

                        final ProfileModel profile =new ProfileModel();

                        profile.setAddress(response);

                        AlertDialog alertDialog = new AlertDialog.Builder(ProfileNavigationActivitySkip.this, R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Address Update");

                        // Setting Dialog Message
                        alertDialog.setMessage("Your Address Has Been Updated");

                        // Setting Icon to Dialog
                        //  alertDialog.setIcon(R.drawable.tick);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ProfileNavigationActivitySkip.this, ProfileNavigationActivitySkip.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();

                    }else {
                        Toast.makeText(getApplicationContext(),"Oops! Something Went Wrong",Toast.LENGTH_SHORT).show();
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
                    AlertDialog alertDialog = new AlertDialog.Builder(ProfileNavigationActivitySkip.this, R.style.MyDialogTheme).create();

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
        }) ;/*{

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


    private void updateUserProfileEmail(final String  userAccessToken, final String email ) {

        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UserProfileUpdate+"?token="+userAccessToken+"&"+"emailId="+email, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                Log.d(TAG, "Register Response: " + response1.toString());

                try {
                    //progressBar.setVisibility(View.GONE);
                    JSONObject object = new JSONObject(response1);
                    String response = object.getString("response");
                    String status =object.getString("status");

                    if (status!=null && status.equals("success")) {
                        //mailIdEdit.setText("Edit");
                        final ProfileModel profile =new ProfileModel();

                        profile.setAddress(response);



                        AlertDialog alertDialog = new AlertDialog.Builder(ProfileNavigationActivitySkip.this, R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Email Update");

                        // Setting Dialog Message
                        alertDialog.setMessage("Your Email Has Been Updated");

                        // Setting Icon to Dialog
                        //  alertDialog.setIcon(R.drawable.tick);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ProfileNavigationActivitySkip.this, ProfileNavigationActivitySkip.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();

                    }else if (status.equals("failure")){
                        AlertDialog alertDialog = new AlertDialog.Builder(ProfileNavigationActivitySkip.this, R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Email Update");

                        // Setting Dialog Message
                        alertDialog.setMessage("Your email id  is already exists!");

                        // Setting Icon to Dialog
                        //  alertDialog.setIcon(R.drawable.tick);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(ProfileNavigationActivitySkip.this, ProfileNavigationActivitySkip.class);
                                startActivity(intent);
                                finish();

                            }
                        });
                    }else {
                        Toast.makeText(getApplicationContext(),"Oops! Something Went Wrong",Toast.LENGTH_SHORT).show();
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
                    AlertDialog alertDialog = new AlertDialog.Builder(ProfileNavigationActivitySkip.this, R.style.MyDialogTheme).create();

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
        }) ;/*{

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



}

