package in.sabpaisa.droid.sabpaisa;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
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
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;
import in.sabpaisa.droid.sabpaisa.Util.LoginActivityWithoutSharedPreference;
import in.sabpaisa.droid.sabpaisa.Util.SmsListener;
import in.sabpaisa.droid.sabpaisa.Util.SmsReceiver;

public class ChangePassword extends AppCompatActivity {
    private static final String TAG = ChangePassword.class.getSimpleName();
    private Button send_Otp;
    private EditText et_otp;
    Handler handler = new Handler();
    String deviceId;
    String otpTag = "Please Use this OTP to verify your Mobile on SabPaisa App";
    EditText optEditText = null;
    CountDownTimer countDownTimer = null;
    TextView timerTextView = null;
    ProgressDialog progressBar = null;
    String otp11;
    BottomSheetDialog mBottomSheetDialog;
    private static final int REQUEST_READ_PERMISSION = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_forgot);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final EditText et_phone_number = (EditText)findViewById(R.id.et_phone_number);
        final EditText password = (EditText)findViewById(R.id.et_currentpass);
        final EditText curentpwd = (EditText)findViewById(R.id.et_mailId);
        Button btn_save = (Button)findViewById(R.id.btn_save);
        send_Otp = (Button) findViewById(R.id.send_Otp);
        optEditText = (EditText) findViewById(R.id.optEditText);
        et_otp=(EditText)findViewById(R.id.optEditText);
        /*START Initiallizing BottomSheetDialog and giving its view in sheetView*/
        mBottomSheetDialog = new BottomSheetDialog(ChangePassword.this);
        LayoutInflater inflater = (LayoutInflater) ChangePassword.this.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View sheetView = inflater.inflate(R.layout.popup_otp, null);
        mBottomSheetDialog.setContentView(sheetView);

        timerTextView = (TextView) sheetView.findViewById(R.id.timer_text_view);



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
                    mBottomSheetDialog.setCancelable(false);//Added on 2nd Feb
                    mBottomSheetDialog.setCanceledOnTouchOutside(false);//Added on 2nd Feb
                    mBottomSheetDialog.show();
                    callTimerCoundown();
                    sendOTP(v, number);

                    } else {

                    AlertDialog alertDialog = new AlertDialog.Builder(ChangePassword.this, R.style.MyDialogTheme).create();

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


        if (CheckPermission(this, android.Manifest.permission.READ_PHONE_STATE)) {
            deviceId();
        } else {
            RequestPermission(ChangePassword.this, android.Manifest.permission.READ_PHONE_STATE, REQUEST_READ_PERMISSION);
        }




        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String contactNumber = et_phone_number.getText().toString();
                String newPassword =password.getText().toString();
                String  currentPassword = curentpwd.getText().toString();
                Log.e(TAG, "response: " + contactNumber);

                if ((et_phone_number.length() == 0)  ||(et_phone_number.length()<10)) {

                    et_phone_number.setError("Please make sure that You have entered 10 digit number");

                }

                else if(et_otp.length()==0){

                    et_otp.setError("Please click on the send otp");
                } else if(curentpwd.length()==0){

                    curentpwd.setError("Please enter your Current Password");
                }
                else if(isOnline())
                {

                    registerUser(contactNumber,newPassword,currentPassword);
                    }
                else {

                    AlertDialog alertDialog = new AlertDialog.Builder(ChangePassword.this, R.style.MyDialogTheme).create();

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


    private void registerUser(final String contactNumber,final String newPassword, final String Curentpwd ) {


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

                    if(status.equals("success")&&et_otp.getText().toString().equals(otp11)) {

                        Intent intent = new Intent(ChangePassword.this, LoginActivityWithoutSharedPreference.class);
                        startActivity(intent);
                        Log.e(TAG, "status: " + status);
                        finish();
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
                    AlertDialog alertDialog = new AlertDialog.Builder(ChangePassword.this, R.style.MyDialogTheme).create();

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
                 params.put("currentPassword", Curentpwd);
                //params.put("deviceId", deviceId);
                // params.put("dob", dob );


                return params;
            }




        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void launchMainScreen() {
        startActivity(new Intent(ChangePassword.this, MainActivity.class));

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
                    otp11 = response.getString("otp");
                    String  verifireponse= response.getString("response");

                    Log.d("Archana1111111",""+status);
                    Log.d("Archana111111111",""+otp11);
                    Log.d("Archana211111111",""+verifireponse);

                    if (status.equals("success")) {
                        send_Otp.setVisibility(View.INVISIBLE);

                    } else if (status.equals("failure")) {
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ChangePassword.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                //progressBarDismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("OTP", "Error: " + error.getMessage());
                Toast.makeText(ChangePassword.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                //progressBarDismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void sendOTP(View v, final String number) {


        String urlJsonObj =  AppConfig.Base_Url+AppConfig.App_api+"SendOTP/" +"?mobile_no="+ number;


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
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

                        SmsReceiver.bindListener(new SmsListener() {
                            @Override
                            public void messageReceived(String messageText) {
                                Log.i("OTP", "messageText=" + messageText);

                                final String optSplit[] = messageText.split(":");
                                if (optSplit[0].trim().equalsIgnoreCase(otpTag)) {
                                    optEditText.setText(optSplit[1]);
                                    handler.postAtTime(new Runnable() {
                                        @Override
                                        public void run() {


                                            mBottomSheetDialog.hide();

                                            veryfiOTP(number, optSplit[1]);

                                        }
                                    }, 1000);
                                }
                            }
                        });


                    }
                    else if (status.equals("failure")){

                        AlertDialog alertDialog = new AlertDialog.Builder(ChangePassword.this, R.style.MyDialogTheme).create();

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

                        alertDialog.show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                //hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage()==null ||error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ChangePassword.this, R.style.MyDialogTheme).create();

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


            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
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


}

