package in.sabpaisa.droid.sabpaisa;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import in.sabpaisa.droid.sabpaisa.Util.AppConfig;

import static in.sabpaisa.droid.sabpaisa.R.string.status;

/**
 * Created by SabPaisa on 26-10-2017.
 */

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    EditText et_phone_number,et_otp,et_FullName,et_password;
    private Button btn_register;
    private ProgressDialog pDialog;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_register = (Button) findViewById(R.id.btn_register);
        /*btn_name_next1 = (Button) findViewById(R.id.btn_name_next1);
        btn_name_next2 = (Button) findViewById(R.id.btn_name_next2);*/
        //emailid = (EditText) findViewById(R.id.emailid);


        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        et_FullName = (EditText) findViewById(R.id.et_FullName);
        et_otp =(EditText) findViewById(R.id.et_otp);
        et_password = (EditText) findViewById(R.id.et_password);
        //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        final String deviceId = deviceUuid.toString();

        Log.e(TAG, "Device Id: " + deviceId);
        // Progress dialog
        //pDialog = new ProgressDialog(this);
        //pDialog.setCancelable(false);




        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //      String id = emailid.getText().toString();
                String contactNumber =  et_phone_number.getText().toString();
                String fullName = et_FullName.getText().toString();

               // String dob =  et_phone_number.getText().toString();
                String mobile =  et_phone_number.getText().toString();
                String otp =  et_otp.getText().toString();
                String password =   et_password.getText().toString();


                if ((contactNumber.length() == 0) ||(contactNumber.length()<10)) {

                    et_phone_number.setError("Please make sure that You have entered 10 digit number ");

                }
             else   if (fullName.length() == 0) {

                    et_FullName.setError("Please Enter your Name");

                }

                else   if (password.length() == 0) {

                    et_password.setError("Please Enter Password");

                }



                else if(isOnline())
                {
                    registerUser(contactNumber,fullName,password,deviceId);
                    //launchAgeScreen();



                }


                else {

                    AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this, R.style.MyDialogTheme).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("No Internet Connection");

                    // Setting Dialog Message
                    alertDialog.setMessage("Please check internet connection and try again. Thank you.");

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
                    Log.v("Home", "############################You are not online!!!!");
                }
                //launchAgeScreen();



            }

        });






        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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



    private void registerUser(final String contactNumber,final String fullName,final String password, final String deviceId ) {


        // Tag used to cancel the request
        String tag_string_req = "req_register";

        //pDialog.setMessage("Registering ...");
        //showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                Log.d(TAG, "Register Response: " + response1.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response1);
                    String status = jObj.getString("status");
                    String response = jObj.getString("response");
                    //boolean error = jObj.getBoolean("e623+rror");
                    status =jObj.getString("status");

                       // response = jObj.getString("response");

                        launchAgeScreen();
                        /*Intent intent1 = new Intent(RegisterActivity.this, MainActivity.class);
                        //intent1.putExtra("response", response);

                        startActivity(intent1);
*/
                        Log.e(TAG,"123"+fullName );
                        Log.e(TAG, "status: " + status);
                    Log.e(TAG, "paswword: " + password);

                    Log.e(TAG, "response2163123: " + response);








///////////////////////////
             /*       if (success) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        //String uid = jObj.getString("uid");

                        String userId = jObj.getString("userId");
                        String token = jObj.getString("token");

                        db.addUser(token, userId);
                        //db.addUser(userId);
                        // sending token & userId to signup
                        Intent intent1 = new Intent(Name.this, Gender.class);
                        intent1.putExtra("userId", userId);
                        intent1.putExtra("token", token);
                        startActivity(intent1);
                        Log.e(TAG, "token: " + token);
                        Log.e(TAG, "userId: " + userId);

                       *//* Toast toast = Toast.makeText(getApplicationContext(), "Good to know your Name.Some information need to provide better result", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();*//*
                        //Toast.makeText(getApplicationContext(), "Good to know your Name.Some information need to provide better result", Toast.LENGTH_LONG).show();


                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        return;
                      *//*  String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();*//*
                    }
             */       ///////////////////////////////








/*

                    else if (status=="failed"){
                        response = jObj.getString("response");
                        if(response=="Duplicate_Mail_ID") {
                            AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this, R.style.MyDialogTheme).create();

                            // Setting Dialog Title
                            alertDialog.setTitle("Registreation Error");

                            // Setting Dialog Message
                            alertDialog.setMessage("Email Id is already registered.");

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

                        }
                        // Error occurred in registration. Get the error
                        // message
                        return;
                      *//*  String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();*//*
                    }*/





                   /* else {

                        // Error occurred in registration. Get the error
                        // message
                        return;
                       *//*String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();*//*
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage()==null ||error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this, R.style.MyDialogTheme).create();

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
                    Log.e(TAG, "Registration Error: " + error.getMessage());
                    /*Toast.makeText(context,
                            context.getString(R.string.error_network_timeout),
                            Toast.LENGTH_LONG).show();*/
                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                } else if (error instanceof NetworkError) {
                    //TODO
                } else if (error instanceof ParseError) {
                    //TODO
                }



                /*Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();*/
                //hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("contactNumber", contactNumber);
                params.put("fullName", fullName);

                params.put("password", password);
                // params.put("password", password);
                params.put("deviceId", deviceId);
               // params.put("dob", dob );


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

   /* private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }*/

  /*  private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }*/

    private void launchAgeScreen() {
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));

    }





}
