package in.sabpaisa.droid.sabpaisa;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
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
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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

import android.Manifest;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;
import in.sabpaisa.droid.sabpaisa.Util.LoginActivityWithoutSharedPreference;

import static in.sabpaisa.droid.sabpaisa.ConstantsForUIUpdates.FEED_ARRAYLIST;
import static in.sabpaisa.droid.sabpaisa.ConstantsForUIUpdates.OTP_NOT_RECIEVED;
import static in.sabpaisa.droid.sabpaisa.ConstantsForUIUpdates.SEND_OTP;


/**
 * Created by Rajdeep Singh on 26-10-2017.
 */

public class RegisterActivity extends AppCompatActivity  {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    EditText et_phone_number, et_FullName, et_password,et_Email;
    private Button btn_register,sentOtpTest;
    private Button send_Otp;
    int MY_SOCKET_TIMEOUT_MS =100000;

    private ProgressDialog pDialog;
    //private EditText et_otp;
    private Pinview et_otp;
    String number;
    String otpTag = "Please Use this OTP to verify your Mobile on SabPaisa App";
    Handler handler = new Handler();
    String otp11;
    //EditText optEditText = null;
    Pinview optEditText = null;
    RequestQueue requestQueue;


    Button resentButton = null; /*Type is changed from TextView to Button, also name is refracted*/
    TextView waitTextView = null;

    CountDownTimer countDownTimer = null;
    TextView timerTextView = null;
    ProgressDialog progressBar = null;
    BottomSheetDialog mBottomSheetDialog;
    private static final int REQUEST_READ_PERMISSION = 123;
    String deviceId;
    Button passwordShow;

    public static final int RESOLVE_HINT = 1;

    public static ProgressDialog progressDialog;




    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_register);

        btn_register = (Button) findViewById(R.id.btn_register);
        send_Otp = (Button) findViewById(R.id.send_Otp);
        //optEditText = (EditText) findViewById(R.id.optEditText);
        optEditText = (Pinview) findViewById(R.id.optEditText);

        /*btn_name_next1 = (Button) findViewById(R.id.btn_name_next1);
        btn_name_next2 = (Button) findViewById(R.id.btn_name_next2);*/
        //emailid = (EditText) findViewById(R.id.emailid);


        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        et_FullName = (EditText) findViewById(R.id.et_FullName);
        //et_otp =(EditText) findViewById(R.id.et_otp);
        et_password = (EditText) findViewById(R.id.et_password);
        et_Email = (EditText) findViewById(R.id.et_Email);
        //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        passwordShow=(Button) findViewById(R.id.tv_password_show1);
        //et_otp=(EditText)findViewById(R.id.optEditText);
        et_otp=(Pinview)findViewById(R.id.optEditText);
///////////////////////////////////////////////////////////////////////////////////////////////////
        /*START Initiallizing BottomSheetDialog and giving its view in sheetView*/
        mBottomSheetDialog = new BottomSheetDialog(RegisterActivity.this);
        LayoutInflater inflater = (LayoutInflater) RegisterActivity.this.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View sheetView = inflater.inflate(R.layout.popup_otp, null);
        mBottomSheetDialog.setContentView(sheetView);

        timerTextView = (TextView) sheetView.findViewById(R.id.timer_text_view);

        progressDialog = new ProgressDialog(RegisterActivity.this,R.style.DialogTheme);




////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;


        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        final String deviceId = deviceUuid.toString();

        Log.e(TAG, "Device Id: " + deviceId);*/
        // Progress dialog
        //pDialog = new ProgressDialog(this);
        //pDialog.setCancelable(false);

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
                //Toast.makeText(getApplicationContext(), "Please enter Phone Number121313!", Toast.LENGTH_LONG).show();
                String number = et_phone_number.getText().toString();
                if (number.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter Phone Number!", Toast.LENGTH_LONG).show();
                } else if (isOnline()) {

                    //Commenting on 2nd Jan 2019
                    /*mBottomSheetDialog.show();
                    mBottomSheetDialog.setCanceledOnTouchOutside(true);
                    mBottomSheetDialog.setCancelable(true);
                    callTimerCoundown();*/

                    progressDialog.setMessage("Please wait for OTP Verification");
                    progressDialog.show();
                    /*progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);*/

                    send_Otp.setVisibility(View.GONE);
                    sendOTP(v, number);
                    // Toast.makeText(OTPVarify.this, "first name field is empty", Toast.LENGTH_LONG).show();
                } else {

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


        if (CheckPermission(this, Manifest.permission.READ_PHONE_STATE)) {
            deviceId();
        } else {
            RequestPermission(RegisterActivity.this, Manifest.permission.READ_PHONE_STATE, REQUEST_READ_PERMISSION);
        }


        optEditText.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                //Make api calls here or what not
                //Toast.makeText(SignUpActivity.this, pinview.getValue(), Toast.LENGTH_SHORT).show();
                if (et_phone_number.getText().length() == 0 || et_phone_number.getText().length() < 10){
                    //et_phone_number.setError("Please Fill The Phone No. ");
                    Toast.makeText(RegisterActivity.this,"Please Fill The Phone No.",Toast.LENGTH_SHORT).show();
                }else {
                    String number = et_phone_number.getText().toString();
                    Log.d(number + "  : ", pinview.getValue());

                    veryfiOTP(number, pinview.getValue());
                }
            }
        });



        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(), "Registeration ", Toast.LENGTH_LONG).show();

                //      String id = emailid.getText().toString();

                String contactNumber = et_phone_number.getText().toString();
                String fullName = et_FullName.getText().toString();
                String email = et_Email.getText().toString();
                String otp=et_otp.getValue().toString();

                // String dob =  et_phone_number.getText().toString();

                // String otp =  et_otp.getText().toString();
                String password = et_password.getText().toString();

                if ((contactNumber.length() == 0) || (contactNumber.length() < 10)) {

                    et_phone_number.setError("Please make sure that You have entered 10 digit number ");

                }

                else if(et_otp.getValue().toString().trim().isEmpty()){

                    //et_otp.setError("Please click on the send otp");
                    Toast.makeText(RegisterActivity.this,"Please click on the send otp",Toast.LENGTH_SHORT).show();
                }
                else if (fullName.length() == 0) {

                    et_FullName.setError("Please Enter your Name");

                } else if (password.length() == 0) {

                    et_password.setError("Please set your Password");

                }else if (email.length()!=0 && isValidEmail(email)) {

                    et_Email.setError("Please enter the correct email-id");

                } else if (isOnline()) {
                    Log.e(TAG, "otp11 " +otp11);
                    if(!et_otp.getValue().toString().equals(otp11))

                    {


                        AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this, R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("SPApp");

                        // Setting Dialog Message
                        alertDialog.setMessage("Hey, its look like OTP is incorrect or you are trying to Register with other mobile device");
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
                    else   {
                        registerUser(contactNumber, fullName, password, deviceId,email);

                    }



                } else {
                    //registerUser(contactNumber, fullName, password, deviceId);
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


        passwordShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordShow.getText().toString().equals("Show")){
                    passwordShow.setText("Hide");

                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else
                {
                    passwordShow.setText("Show");
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        googleSmsRetrievalApi();


        LocalBroadcastManager.getInstance(RegisterActivity.this).registerReceiver(broadcastReceiver,new IntentFilter(SEND_OTP));
        LocalBroadcastManager.getInstance(RegisterActivity.this).registerReceiver(broadcastReceiverOtpRecieved,new IntentFilter(OTP_NOT_RECIEVED));




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

                AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this, R.style.MyDialogTheme).create();

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
        if (ContextCompat.checkSelfPermission(context,
                Permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    private void veryfiOTP(final String number, String otp) {
        String urlJsonObj = AppConfig.Base_Url+AppConfig.App_api+"verifyOtp?otp="+otp+"&mobile_no="+ number;

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



                    String  verifireponse= response.getString("response");

                    Log.d("Archana1111111",""+status);

                    Log.d("Archana211111111",""+verifireponse);
                    if (status.equals("success")) {

                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }

                        otp11 = response.getString("otp");
                        Log.d("otp11","Value "+otp11);
                        send_Otp.setVisibility(View.INVISIBLE);
                        Toast.makeText(RegisterActivity.this,String.valueOf(verifireponse),Toast.LENGTH_SHORT).show();
                    } else if (status.equals("failed")){

                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }

                        Toast.makeText(RegisterActivity.this,String.valueOf(verifireponse),Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                   // Toast.makeText(RegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                //progressBarDismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                //VolleyLog.d("OTP", "Error: " + error.getMessage());
              //  Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                //progressBarDismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


   /* private void progressBarDismiss() {
        if (progressBar != null) {
            progressBar.dismiss();
        }
    }




    OtpDialog otpDialog = null;

    public void Show_Dialog(Context v, String number) {
        otpDialog = new OtpDialog(RegisterActivity.this, number);
        Bundle bundle = new Bundle();
        otpDialog.onCreate(bundle); *//*otpDialog.show(); was creating the problem with dialog, instead we called onCreate method to start bottomSheetDialog*//*
        //otpDialog.show();
    }*/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(RegisterActivity.this,LoginActivityWithoutSharedPreference.class);
        startActivity(intent);
        this.finish();


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


        String urlJsonObj = AppConfig.Base_Url+AppConfig.App_api+"SendOTP/" +"?mobile_no="+ number;
//        String urlJsonObj = "http://205.147.103.27:6060/SabPaisaAppApi/SendOTP/" +"?mobile_no="+ number;

        //showpDialog(v);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Send OTP", response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    JSONObject jObj = new JSONObject(String.valueOf(response));

                    String status = response.getString("status");
                    if (status.equals("success")) {

                        otp11=response.getString("otp");
                        Log.d("otp11","AfterSuccessResp"+otp11);
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
//
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
                 //   Log.e(TAG, "OTP Error: " + error.getMessage());
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
    }




    private void registerUser(final String contactNumber,final String fullName,final String password, final String deviceId ,final String email) {


        // Tag used to cancel the request
        String tag_string_req = "req_register";

        //pDialog.setMessage("Registering ...");
        //showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.Base_Url+AppConfig.App_api+AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                Log.d(TAG, "Register Response: " + response1.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response1);
                    String response = jObj.getString("response");
                    String status =jObj.getString("status");

                    if (status!=null && status.equals("success")) {

                        launchAgeScreen();

                        Log.e(TAG, "123" + fullName);
                        Log.e(TAG, "status: " + status);
                        Log.e(TAG, "paswword: " + password);

                        Log.e(TAG, "response2163123: " + response);

                    }else if (status!=null && status.equals("failed") && response.equals("Duplicate_Phone_No")){
                        AlertDialog.Builder builder =new AlertDialog.Builder(RegisterActivity.this);
                        builder.setTitle("Registration Error");
                        builder.setMessage("You have already registered with this number. Please click Okay to Login");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(RegisterActivity.this,LoginActivityWithoutSharedPreference.class);
                                startActivity(intent);
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        //Toast.makeText(RegisterActivity.this, "", Toast.LENGTH_LONG).show();



                        /*

                        AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this, R.style.MyDialogTheme).create();
                        // Setting Dialog Title
                        alertDialog.setTitle("Registration Error");

                        // Setting Dialog Message
                        alertDialog.setMessage("You have already registered with this number. Please click Okay to Login");

                        alertDialog.setCanceledOnTouchOutside(false);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog closed
                                Intent intent = new Intent(RegisterActivity.this,LogInActivity.class);
                                startActivity(intent);
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();

*/

                    }else if (status!=null && status.equals("failed") && response.equals("Duplicate_Mail_ID")){

                        AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this, R.style.MyDialogTheme).create();
                        // Setting Dialog Title
                        alertDialog.setTitle("Registration Error");

                        // Setting Dialog Message
                        alertDialog.setMessage("You have already registered with this Email. Please click Okay to Login");

                        alertDialog.setCanceledOnTouchOutside(false);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog closed
                                launchAgeScreen();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
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
                   // Log.e(TAG, "Registration Error: " + error.getMessage());
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
                params.put("emailId", email);
                // params.put("dob", dob );


                return params;
            }

        };



        strReq.setRetryPolicy(new DefaultRetryPolicy(
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

                //Toast.makeText(getApplicationContext(), "Sync", Toast.LENGTH_LONG).show();
                return;
            } catch (GooglePlayServicesNotAvailableException e) {
                // Indicates a non-recoverable error; the ProviderInstaller is not able
                // to install an up-to-date Provider.
                // Notify the SyncManager that a hard error occurred.
                //syncResult.stats.numAuthExceptions++;
               // Toast.makeText(getApplicationContext(), "Sync12", Toast.LENGTH_LONG).show();
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

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);




        // Adding request to request queue
      //
        //
        //
        //
        //
        // AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    /*ProgressDialog loading = null;

    private void showpDialog(View v) {
        loading = new ProgressDialog(v.getContext());
        loading = new ProgressDialog(v.getContext());
        loading.setCancelable(true);
        loading.setMessage("Please wait for OTP.....");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.show();
    }*/



    /* private void hidepDialog() {
         if (loading != null) {
             loading.dismiss();
         }
     }

    *//* private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }*//*

     *//*  private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }*//*
     */
    private void launchAgeScreen() {
        startActivity(new Intent(RegisterActivity.this, LoginActivityWithoutSharedPreference.class));

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
//                resentButton.setClickable(true);
//                waitTextView.setClickable(true);
            }
        }.start();

    }




    public void googleSmsRetrievalApi(){


        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(RegisterActivity.this);
        appSignatureHelper.getAppSignatures();


        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();



        GoogleApiClient apiClient = new GoogleApiClient.Builder(RegisterActivity.this)
                .addApi(Auth.CREDENTIALS_API).enableAutoManage(RegisterActivity.this, GoogleApiHelper
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


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


}