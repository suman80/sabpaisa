package in.sabpaisa.droid.sabpaisa;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.AppConfiguration;
import in.sabpaisa.droid.sabpaisa.Util.OtpDialog;
import in.sabpaisa.droid.sabpaisa.Util.SharedPref;
import in.sabpaisa.droid.sabpaisa.Util.SmsListener;
import in.sabpaisa.droid.sabpaisa.Util.SmsReceiver;

/**
 * Created by SabPaisa on 26-10-2017.
 */

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    EditText et_phone_number,et_FullName,et_password;
    private Button btn_register;
    private TextView send_Otp;
    private ProgressDialog pDialog;
    private EditText et_otp;
    String number;
    String otpTag = "Please Use this OTP to verify your Mobile on SabPaisa App";
    Handler handler = new Handler();
    EditText optEditText = null;
    TextView timerTextView = null;
    Button resentButton = null; /*Type is changed from TextView to Button, also name is refracted*/
    TextView waitTextView = null;
    ProgressDialog progressBar = null;
    CountDownTimer countDownTimer = null;
    BottomSheetDialog mBottomSheetDialog;





    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_register = (Button) findViewById(R.id.btn_register);
        send_Otp = (TextView) findViewById(R.id.send_Otp);
        optEditText = (EditText) findViewById(R.id.optEditText);

        /*btn_name_next1 = (Button) findViewById(R.id.btn_name_next1);
        btn_name_next2 = (Button) findViewById(R.id.btn_name_next2);*/
        //emailid = (EditText) findViewById(R.id.emailid);


        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        et_FullName = (EditText) findViewById(R.id.et_FullName);
        //et_otp =(EditText) findViewById(R.id.et_otp);
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



        send_Otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = et_phone_number.getText().toString();
                if (number.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter Phone Number!", Toast.LENGTH_LONG).show();
                } else if (isOnline()){

                    sendOTP(v, number);
                    // Toast.makeText(OTPVarify.this, "first name field is empty", Toast.LENGTH_LONG).show();
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

            }
        });








        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //      String id = emailid.getText().toString();
                String contactNumber =  et_phone_number.getText().toString();
                String fullName = et_FullName.getText().toString();

               // String dob =  et_phone_number.getText().toString();
                String mobile =  et_phone_number.getText().toString();
               // String otp =  et_otp.getText().toString();
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




    private void veryfiOTP(String number, String otp) {
        String urlJsonObj = "http://192.168.0.49:8089/sabPaisaApp/verifyOtp?otp="+otp+"&mobile_no="+ number;

        //   progressBarShow();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("OTP1", response.toString());


                try {
                    JSONObject jObj = new JSONObject(String.valueOf(response));
                    // Parsing json object response
                    // response will be a json object
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        //SharedPref.putBoolean(RegisterActivity.this, AppConfiguration.OPT_VARIFICATION, true);
                        launchAgeScreen();
                        //callUINVarificationScreen();
                        //Toast.makeText(OTPVarify.this, "OTP Verified ", Toast.LENGTH_LONG).show();
                    } else if (status.equals("failure")) {
                        // Toast.makeText(OTPVarify.this, "Unable To send OTP varify", Toast.LENGTH_LONG).show();
                        // Error occurred in registration. Get the error
                        // message
                       /* AlertDialog alertDialog = new AlertDialog.Builder();

                        // Setting Dialog Title
                        alertDialog.setTitle("OTP send error");

                        // Setting Dialog Message
                        alertDialog.setMessage("Due to Server response .Unable to send OTP .Please try again");

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
                        alertDialog.show();*/

                        //  String errorMsg = jObj.getString("error_msg");
                        //Toast.makeText(context,errorMsg, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                progressBarDismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("OTP", "Error: " + error.getMessage());
                Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                progressBarDismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    private void progressBarDismiss() {
        if (progressBar != null) {
            progressBar.dismiss();
        }
    }




    OtpDialog otpDialog = null;

    public void Show_Dialog(Context v, String number) {
        otpDialog = new OtpDialog(RegisterActivity.this, number);
        Bundle bundle = new Bundle();
        otpDialog.onCreate(bundle); /*otpDialog.show(); was creating the problem with dialog, instead we called onCreate method to start bottomSheetDialog*/
        //otpDialog.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (otpDialog != null) {
            otpDialog.dismiss();
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



    private void sendOTP(View v, final String number) {


        String urlJsonObj = "http://192.168.0.49:8089/sabPaisaApp/SendOTP/" +"?mobile_no="+ number;
//        String urlJsonObj = "http://205.147.103.27:6060/SabPaisaAppApi/SendOTP/" +"?mobile_no="+ number;

        showpDialog(v);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Send OTP", response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    JSONObject jObj = new JSONObject(String.valueOf(response));
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        Show_Dialog(getApplicationContext(), number);
                        //Toast.makeText(getApplicationContext(), "OTP sent", Toast.LENGTH_LONG).show();


                        SmsReceiver.bindListener(new SmsListener() {
                            @Override
                            public void messageReceived(String messageText) {
//                Toast.makeText(context, "Message: " + messageText, Toast.LENGTH_LONG).show();
                                Log.i("OTP", "messageText=" + messageText);

                                final String optSplit[] = messageText.split(":");
                                if (optSplit[0].trim().equalsIgnoreCase(otpTag)) {
                                    optEditText.setText(optSplit[1]);
                                    handler.postAtTime(new Runnable() {
                                        @Override
                                        public void run() {

                                            if (countDownTimer != null) {
                                                countDownTimer.cancel();
                                            }

                                            veryfiOTP(number, optSplit[1]);

                                            //  callVaryfyOTP(optSplit[1]);
                                        }
                                    }, 1000);
                                }
                            }
                        });

                        if (!optEditText.getText().toString().equals("")) {
                            String optText = optEditText.getText().toString();
                           /* if (countDownTimer != null) {
                                countDownTimer.cancel();
                            }*/

                            veryfiOTP(number, optText);

                            //callVaryfyOTP(optText);
                        }
                        //  callTimerCoundown();


                        Show_Dialog(getApplicationContext(), number);
                        //Toast.makeText(getApplicationContext(), "OTP sent", Toast.LENGTH_LONG).show();

                    }
                    else if (status.equals("failure")){

                        AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this, R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("OTP send error");

                        // Setting Dialog Message
                        alertDialog.setMessage("Due to Server response .Unable to send OTP .Please try again");

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
                        // Error occurred in registration. Get the error
                        // message
                       /* String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();*/
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                hidepDialog();
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



             /*   VolleyLog.d("eclipse", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();*/
                //Show_Dialog(getApplicationContext(), number);
                // hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
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

+96                        String userId = jObj.getString("userId");
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
    ProgressDialog loading = null;

    private void showpDialog(View v) {
        loading = new ProgressDialog(v.getContext());
        loading = new ProgressDialog(v.getContext());
        loading.setCancelable(true);
        loading.setMessage("Please wait for OTP.....");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.show();
    }



    private void hidepDialog() {
        if (loading != null) {
            loading.dismiss();
        }
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
        startActivity(new Intent(RegisterActivity.this, FilterActivity.class));

    }





}