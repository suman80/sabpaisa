package in.sabpaisa.droid.sabpaisa.Util;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.AppSignatureHelper;
import in.sabpaisa.droid.sabpaisa.GoogleApiHelper;
import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.MainActivity;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.RegisterActivity;

import android.app.Activity;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.telephony.TelephonyManager;

import com.android.volley.toolbox.Volley;
import com.goodiebag.pinview.Pinview;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import in.sabpaisa.droid.sabpaisa.TLSSocketFactory;

import static in.sabpaisa.droid.sabpaisa.ConstantsForUIUpdates.OTP_NOT_RECIEVED;
import static in.sabpaisa.droid.sabpaisa.ConstantsForUIUpdates.SEND_OTP;

public class ForgotActivity extends AppCompatActivity {
    private static final String TAG = ForgotActivity.class.getSimpleName();
    private Button send_Otp;
    //private EditText et_otp;
    private Pinview et_otp;
    Handler handler = new Handler();
    int MY_SOCKET_TIMEOUT_MS =100000;
    String deviceId;
    String otp11;
    String otpTag = "Please Use this OTP to verify your Mobile on SabPaisa App";
    //EditText optEditText = null;
    Pinview optEditText = null;
    CountDownTimer countDownTimer = null;
    TextView timerTextView = null;
    ProgressDialog progressBar = null;
    RequestQueue requestQueue;

    //////////////
    static  int testcount = 1;
    ///////////////

    EditText password;
    BottomSheetDialog mBottomSheetDialog;
    private static final int REQUEST_READ_PERMISSION = 123;

    String globalNumber;

    public static final int RESOLVE_HINT = 1;

    EditText et_phone_number;
    public static ProgressDialog progressDialog;

    String Flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_forgot);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //final EditText et_currentPassword =(EditText) findViewById(R.id.et_currentPassword);
        et_phone_number = (EditText)findViewById(R.id.et_phone_number);
        password = (EditText)findViewById(R.id.et_password);
        Button btn_save = (Button)findViewById(R.id.btn_save);
        send_Otp = (Button) findViewById(R.id.send_Otp);
        optEditText = (Pinview) findViewById(R.id.optEditText);
        et_otp=(Pinview)findViewById(R.id.optEditText);
        /*START Initiallizing BottomSheetDialog and giving its view in sheetView*/
        mBottomSheetDialog = new BottomSheetDialog(ForgotActivity.this);
        LayoutInflater inflater = (LayoutInflater) ForgotActivity.this.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View sheetView = inflater.inflate(R.layout.popup_otp, null);
        mBottomSheetDialog.setContentView(sheetView);
        timerTextView = (TextView) sheetView.findViewById(R.id.timer_text_view);

        progressDialog = new ProgressDialog(ForgotActivity.this,R.style.DialogTheme);


        Flag = getIntent().getStringExtra("FLAG");

        Log.d("ForgotFlagVal","FLAG__"+Flag);


//Code Added for visible and invisible of send_Otp
        et_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().trim().length() < 9) {
                    send_Otp.setVisibility(View.INVISIBLE);
                } else {
                    send_Otp.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        send_Otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = et_phone_number.getText().toString();
                if (number.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter Phone Number!", Toast.LENGTH_LONG).show();
                } else if (isOnline()) {

                    //Commenting on 2nd Jan 2019
                    /*mBottomSheetDialog.setCancelable(true);//Added on 2nd Feb
                    mBottomSheetDialog.setCanceledOnTouchOutside(true);//Added on 2nd Feb
                    mBottomSheetDialog.show();
                    callTimerCoundown();*/

                    progressDialog.setMessage("Please wait for OTP Verification");
                    progressDialog.show();
                    /*progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);*/


                    Log.d("Button : Test count : ", testcount++ +"");
                    send_Otp.setVisibility(View.GONE);
                    sendOTPAPI(v, number);
                    // Toast.makeText(OTPVarify.this, "first name field is empty", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(ForgotActivity.this, R.style.MyDialogTheme).create();
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

        if (CheckPermission(this, Manifest.permission.READ_PHONE_STATE)) {
            deviceId();
        } else {
            RequestPermission(ForgotActivity.this, Manifest.permission.READ_PHONE_STATE, REQUEST_READ_PERMISSION);
        }


        optEditText.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                //Make api calls here or what not
                //Toast.makeText(SignUpActivity.this, pinview.getValue(), Toast.LENGTH_SHORT).show();
                if (et_phone_number.getText().length() == 0 || et_phone_number.getText().length() < 10){
                    //et_phone_number.setError("Please Fill The Phone No. ");
                    Toast.makeText(ForgotActivity.this,"Please Fill The Phone No.",Toast.LENGTH_SHORT).show();
                }else {
                    String number = et_phone_number.getText().toString();
                    Log.d(number + "  : ", pinview.getValue());

                    veryfiOTP(number, pinview.getValue());
                }
            }
        });





        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactNumber = et_phone_number.getText().toString();
                String newPassword =password.getText().toString();
                //String  currentPassword = et_currentPassword.getText().toString();
                Log.e(TAG, "response: " + contactNumber);
                if ((et_phone_number.length() == 0)  ||(et_phone_number.length()<10)) {
                    et_phone_number.setError("Please make sure that You have entered 10 digit number");
                }
                if(newPassword.length()==0)
                {
                    password.setError("Please enter password ");
                }
                else if(et_otp.getValue().toString().isEmpty()){

                    //et_otp.setError("Please click on the send otp");
                    Toast.makeText(ForgotActivity.this,"Please click on the send otp",Toast.LENGTH_SHORT).show();
                }else if (!(contactNumber.equals(globalNumber))){
                    Toast.makeText(ForgotActivity.this,"Otp is not verified with this number",Toast.LENGTH_SHORT).show();
                }
                else if(isOnline())
                {
                    registerUser(contactNumber,newPassword);
                }
                else {

                    AlertDialog alertDialog = new AlertDialog.Builder(ForgotActivity.this, R.style.MyDialogTheme).create();
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



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        googleSmsRetrievalApi();


        LocalBroadcastManager.getInstance(ForgotActivity.this).registerReceiver(broadcastReceiver,new IntentFilter(SEND_OTP));
        LocalBroadcastManager.getInstance(ForgotActivity.this).registerReceiver(broadcastReceiverOtpRecieved,new IntentFilter(OTP_NOT_RECIEVED));




    }




    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(SEND_OTP)){

                String otp = intent.getStringExtra("OTP");

                Log.d("Register","otpRecieved___"+otp);

                optEditText.setValue(otp);

            }

        }
    };

    BroadcastReceiver broadcastReceiverOtpRecieved = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(OTP_NOT_RECIEVED)){

                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                AlertDialog alertDialog = new AlertDialog.Builder(ForgotActivity.this, R.style.MyDialogTheme).create();

                alertDialog.setTitle("OTP Error!");

                alertDialog.setMessage("OTP not recieved yet, kindly try again");

                alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        send_Otp.setVisibility(View.VISIBLE);
                    }
                });

                alertDialog.show();


            }

        }
    };



    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiverOtpRecieved);
        super.onDestroy();
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




    private void registerUser(final String contactNumber,final String newPassword ) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        //pDialog.setMessage("Registering ...");
        //showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.Base_Url+AppConfig.App_api+AppConfig.URL_forgotpasswprd, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                Log.d(TAG, "Register Response: " + response1.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response1);
                    String status = jObj.getString("status");
                    String response = jObj.getString("response");
                    Log.d("Registerreposy",""+response) ;
                    Log.d("Registerstts",""+status) ;

                    if(status.equals("success") && et_otp.getValue().toString().equals(otp11)) {

                        /*Intent intent = new Intent(ForgotActivity.this, LoginActivityWithoutSharedPreference.class);
                        startActivity(intent);*/


                        Log.d(TAG, "status: " + status);

                        if (Flag !=null && !Flag.isEmpty() && Flag.equals("MainActivity")){
                            finish();
                        }else {
                            Intent intent = new Intent(ForgotActivity.this, LogInActivity.class);
                            startActivity(intent);

                            finish();
                        }


                    }
                    //Log.e(TAG, "response2163123: " + userId);
                    else if(status.equals("failure")&&et_otp.getValue().toString().equals(otp11))
                    {

                        AlertDialog alertDialog = new AlertDialog.Builder(ForgotActivity.this, R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("SPApp");

                        // Setting Dialog Message
                        alertDialog.setMessage("Hey,Its look like you are not registered with us.");
                        // Setting Icon to Dialog
                        //  alertDialog.setIcon(R.drawable.tick);
                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog closed
                                // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();


                                Intent intent=new Intent(ForgotActivity.this,RegisterActivity.class);
                                startActivity(intent);
                            }
                        });
                        // Showing Alert Message
                        alertDialog.show();
                    }
                    else if(!et_otp.getValue().toString().equals(otp11))
                    {


                        AlertDialog alertDialog = new AlertDialog.Builder(ForgotActivity.this, R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("SPApp");

                        // Setting Dialog Message
                        alertDialog.setMessage("Hey, its look like OTP is incorrect or trying with other mobile device or you are not Registered with us");
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
                    else {
                        AlertDialog alertDialog = new AlertDialog.Builder(ForgotActivity.this, R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("");
                        // Setting Dialog Message
                        alertDialog.setMessage("Your password is successfully changed");
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
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage()==null ||error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ForgotActivity.this, R.style.MyDialogTheme).create();

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
                params.put("newPassword", newPassword);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void launchMainScreen() {
        startActivity(new Intent(ForgotActivity.this, MainActivity.class));

    }
    private void callTimerCoundown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                int sec = (int) millisUntilFinished / 1000;
                if (sec < 10) {
                    timerTextView.setText("00:0" + sec);
                } else

                {
                    timerTextView.setText("00:" + sec);

                }
//                resentButton.setClickable(false);
//                waitTextView.setClickable(false);
            }

            public void onFinish() {
                timerTextView.setText("00:00");
                mBottomSheetDialog.hide();

//                resentButton.setClickable(true);
//                waitTextView.setClickable(true);
            }
        }.start();

    }
    private void veryfiOTP(final String number, String otp) {
        String urlJsonObj = AppConfig.Base_Url+AppConfig.App_api+"verifyOtp?otp="+otp+"&mobile_no="+ number;

        //   progressBarShow();
        Log.d("Verify Otp url : ",urlJsonObj);
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


                    String  verifireponse= response.getString("response");

                    Log.d("Archana1111111",""+status);

                    Log.d("Archana211111111",""+verifireponse);

                    if (status.equals("success")) {

                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }

                        otp11 = response.optString("otp");
                        Log.d("otp11","Value "+otp11);
                        globalNumber = number;
                        send_Otp.setVisibility(View.INVISIBLE);
                        Log.d("verifireponsePass"," "+verifireponse);
                    Toast.makeText(ForgotActivity.this,verifireponse,Toast.LENGTH_SHORT).show();
                    } else if (status.equals("failed")) {

                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }

                        Log.d("verifireponseFail"," "+verifireponse);
                        Toast.makeText(ForgotActivity.this,verifireponse,Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    e.printStackTrace();
                    //Toast.makeText(ForgotActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                //progressBarDismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                VolleyLog.d("OTP", "Error: " + error.getMessage());
               // Toast.makeText(ForgotActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                //progressBarDismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void sendOTPAPI(View v, final String number) {

        Log.d(" sendOTP :Test count : ", testcount++ +"");
        String urlJsonObj =  AppConfig.Base_Url+AppConfig.App_api+"SendOTP/" +"?mobile_no="+ number;
//        String urlJsonObj = "http://205.147.103.27:6060/SabPaisaAppApi/SendOTP/" +"?mobile_no="+ number;

        //showpDialog(v);


        Log.d(" sendOTP :URL : ", urlJsonObj);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Send OTP11", response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    JSONObject jObj = new JSONObject(String.valueOf(response));

                    String status = response.getString("status");
                    if (status.equals("success")) {

                        //Toast.makeText(getApplicationContext(), "OTP sent", Toast.LENGTH_LONG).show();

                        //Commenting on 2nd Jan 2019

//                        SmsReceiver.bindListener(new SmsListener() {
//                            @Override
//                            public void messageReceived(String messageText) {
////                Toast.makeText(context, "Message: " + messageText, Toast.LENGTH_LONG).show();
//                                Log.i("OTP", "messageText=" + messageText);
//
//                                final String optSplit[] = messageText.split(":");
//                                if (optSplit[0].trim().equalsIgnoreCase(otpTag)) {
//                                    optEditText.setValue(optSplit[1]);
//                                    handler.postAtTime(new Runnable() {
//                                        @Override
//                                        public void run() {
//
//                                            /*if (countDownTimer != null) {
//                                                countDownTimer.cancel();
//                                            }*/
//
//                                            mBottomSheetDialog.hide();
//                                            //veryfiOTP(number, optSplit[1]);
//
//                                            //  callVaryfyOTP(optSplit[1]);
//                                        /*OtpDialog dialog = null;
//                                        dialog = new OtpDialog(RegisterActivity.this,number);
//                                        Bundle bundle = new Bundle();
//                                        dialog.onCreate(bundle);
//                                        dialog.dismiss();*/
//                                        }
//                                    }, 1000);
//                                }
//                            }
//                        });


                       /* if (!optEditText.getText().toString().equals("")) {
                            String optText = optEditText.getText().toString();
                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                            }

                            veryfiOTP(number, optText);

                            //callVaryfyOTP(optText);
                        }*/
                        //  callTimerCoundown();
                        //Toast.makeText(getApplicationContext(), "OTP sent", Toast.LENGTH_LONG).show();

                    }
                    else if (status.equals("failure")){
                        AlertDialog alertDialog = new AlertDialog.Builder(ForgotActivity.this, R.style.MyDialogTheme).create();

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
                                send_Otp.setVisibility(View.VISIBLE);
                            }
                        });

                        alertDialog.show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                   /* Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();*/
                }
                //hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage()==null ||error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ForgotActivity.this, R.style.MyDialogTheme).create();

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
                //hidepDialog();
            }
        });



        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            try {
                ProviderInstaller.installIfNeeded(getApplicationContext());
            } catch (GooglePlayServicesRepairableException e) {
                // Indicates that Google Play services is out of date, disabled, etc.
                // Prompt the user to install/update/enable Google Play services.
                GooglePlayServicesUtil.showErrorNotification(e.getConnectionStatusCode(), getApplicationContext());
                // Notify the SyncManager that a soft error occurred.
                //final SyncResult syncResult = null;
                //syncResult.stats.numIOExceptions++;

                // Toast.makeText(getApplicationContext(), "Sync", Toast.LENGTH_LONG).show();
                return;
            } catch (GooglePlayServicesNotAvailableException e) {
                // Indicates a non-recoverable error; the ProviderInstaller is not able
                // to install an up-to-date Provider.
                // Notify the SyncManager that a hard error occurred.
                //syncResult.stats.numAuthExceptions++;
                //Toast.makeText(getApplicationContext(), "Sync12", Toast.LENGTH_LONG).show();
                return;
            }

            HttpStack stack = null;
            try {
                stack = new HurlStack(null, new TLSSocketFactory());
            } catch (KeyManagementException e) {
                e.printStackTrace();
                Log.d("Your Wrapper Class", "Could not create new stack for TLS v1.2");
                stack = new HurlStack();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                Log.d("Your Wrapper Class", "Could not create new stack for TLS v1.2");
                stack = new HurlStack();
            }

            // AppController.getInstance().addToRequestQueue(getApplicationContext(),stack);

            requestQueue = Volley.newRequestQueue(getApplicationContext(), stack);
        } else {

            requestQueue = Volley.newRequestQueue(getApplicationContext());
            //AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }





        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void deviceId() {
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        deviceId = deviceUuid.toString();

        Log.e(TAG, "Device Id: " + deviceId);
    }
    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        switch (permsRequestCode) {

            case REQUEST_READ_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    deviceId();
                } else {
                    //ShowToast(getString(R.string.permission_needed_sms));
                }
                return;
            }
        }
    }

    public void RequestPermission(Activity thisActivity, String Permission, int Code) {
        if (ContextCompat.checkSelfPermission(thisActivity,
                Permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    Permission)) {
            } else {
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{Permission},
                        Code);
            }
        }
    }

    public boolean CheckPermission(Context context, String Permission) {
        return ContextCompat.checkSelfPermission(context,
                Permission) == PackageManager.PERMISSION_GRANTED;
    }



    public void googleSmsRetrievalApi(){


        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(ForgotActivity.this);
        appSignatureHelper.getAppSignatures();


        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();



        GoogleApiClient apiClient = new GoogleApiClient.Builder(ForgotActivity.this)
                .addApi(Auth.CREDENTIALS_API).enableAutoManage(ForgotActivity.this, GoogleApiHelper
                        .getSafeAutoManageId(), new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.e("RegisterActivity", "Client connection failed: " + connectionResult.getErrorMessage());
                    }
                }).build();




        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(apiClient, hintRequest);

        try {
            startIntentSenderForResult(intent.getIntentSender(), RESOLVE_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }


        // Get an instance of SmsRetrieverClient, used to start listening for a matching
// SMS message.
        SmsRetrieverClient client = SmsRetriever.getClient(this /* context */);

// Starts SmsRetriever, which waits for ONE matching SMS message until timeout
// (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
// action SmsRetriever#SMS_RETRIEVED_ACTION.
        Task<Void> task = client.startSmsRetriever();

// Listen for success/failure of the start Task. If in a background thread, this
// can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                // ...

                Log.d("onSuccess","__"+aVoid);

            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
                // ...
            }
        });

    }



    // Obtain the phone number from the result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                credential.getId();  //<-- will need to process phone number string
                Log.d("onActivityResult","__"+credential.getId());


                if (credential.getId().length() > 10){
                    String number = credential.getId().substring(credential.getId().length()-10);
                    et_phone_number.setText(number);
                    send_Otp.setVisibility(View.VISIBLE);
                }else {
                    et_phone_number.setText(credential.getId().length());
                    send_Otp.setVisibility(View.VISIBLE);
                }

            }
        }
    }




}