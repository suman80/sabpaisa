package in.sabpaisa.droid.sabpaisa;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;
import com.olive.upi.OliveUpiManager;
import com.olive.upi.transport.OliveUpiEventListener;
import com.olive.upi.transport.api.Result;
import com.olive.upi.transport.api.UpiService;
import com.olive.upi.transport.model.sdk.SDKHandshake;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;
import in.sabpaisa.droid.sabpaisa.Util.ForgotActivity;
import in.sabpaisa.droid.sabpaisa.Util.TelephonyInfo;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static in.sabpaisa.droid.sabpaisa.Util.TelephonyInfo.getOutput;

@RuntimePermissions
public class LogInActivity extends AppCompatActivity {
    private static final String TAG = LogInActivity.class.getSimpleName();
    public static String mobileNo, password;
    EditText et_phone_number, et_password;
    RequestQueue requestQueue;
    TextView forgotPassword, register;
    Button login;
    Button passwordShow;

    int MY_SOCKET_TIMEOUT_MS = 100000;
    public static String MySharedPrefLogin = "mySharedPrefForlogin";

    public static final String PREFS_NAME = "LoginPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        CommonUtils.setFullScreen(this);

        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        /*
         * Check if we successfully logged in before.
         * If we did, redirect to home page
         */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getString("logged", "").toString().equals("logged")) {
            Log.d("NSDM "," TTTTTTTTT");
            Intent intent = new Intent(LogInActivity.this, FilterActivity1.class);
            startActivity(intent);

        }
        // password = (EditText)findViewById(R.id.et_password);
        passwordShow = (Button) findViewById(R.id.tv_password_show);
        forgotPassword = (TextView) findViewById(R.id.tv_forgot_password);
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        et_password = (EditText) findViewById(R.id.et_password);
        register = (TextView) findViewById(R.id.tv_register);
        login = (Button) findViewById(R.id.btn_login);
        //DataBinding();
//        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LogInActivity.this, ForgotActivity.class));

            }
        });


        final SharedPreferences sharedpreferences = getSharedPreferences("LoginData", Context.MODE_PRIVATE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mobileNo = et_phone_number.getText().toString();
                password = et_password.getText().toString();
                Log.e(TAG, "response: " + mobileNo);


                if ((et_phone_number.length() == 0) || (et_phone_number.length() < 10)) {

//                        et_phone_number.setError("Please make sure that You have entered 10 digit number");
                    final AlertDialog alertDialog = new AlertDialog.Builder(LogInActivity.this, R.style.MyDialogTheme).create();
                    // Setting Dialog Title
                    alertDialog.setTitle("Sign In Error");

                    // Setting Dialog Message
                    alertDialog.setMessage("Please Check Your Credentials");

                    alertDialog.setCanceledOnTouchOutside(false);

                    // Setting OK Button
                    alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();

                } else if ((et_password.length() == 0)) {
                    AlertDialog alertDialog = new AlertDialog.Builder(LogInActivity.this, R.style.MyDialogTheme).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Missing Information");

                    // Setting Dialog Message
                    alertDialog.setMessage("Please enter your Correct Information to proceed further.");

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

                } else if ((et_phone_number.length() == 10) && (et_password != null) && isOnline())

                {
                    //Intent intent21 = new Intent(Name.this, Gender.class);

                    //startActivity(intent21);
                    //launchAgeScreen();
                    registerUser(mobileNo, password);

                   /* SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString("MobileNumber",mobileNo);
                    editor.putString("Password", password);
                    editor.commit();*/
                    //sdkHandShake();

                } else {

                    AlertDialog alertDialog = new AlertDialog.Builder(LogInActivity.this, R.style.MyDialogTheme).create();

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


                //LogInActivityPermissionsDispatcher.isDualSimOrNotWithCheck(LogInActivity.this);
            }


        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
                startActivity(intent);

                // LogInActivityPermissionsDispatcher.isDualSimOrNotWithCheck(LogInActivity.this);
            }
        });


        //tv_register

        passwordShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordShow.getText().toString().equals("Show")) {
                    passwordShow.setText("Hide");

                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    passwordShow.setText("Show");
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
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

    private void DataBinding() {
        //phoneNumber = (EditText)findViewById(R.id.et_phone_number);
        //password = (EditText)findViewById(R.id.et_password);
        //passwordShow = (TextView)findViewById(R.id.tv_password_show);
        forgotPassword = (TextView) findViewById(R.id.tv_forgot_password);
        register = (TextView) findViewById(R.id.tv_register);
        login = (Button) findViewById(R.id.btn_login);
    }

    @NeedsPermission(Manifest.permission.READ_PHONE_STATE)
    public void isDualSimOrNot() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = this.getLayoutInflater().inflate(R.layout.bottomdialog_getting_sim_info, null);
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
        TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);
        String optName1 = getOutput(this, "SimOperatorName", 0);
        String optName2 = getOutput(this, "SimOperatorName", 1);
        String optName3 = getOutput(this, "SimOperatorName", 2);
        bottomSheetDialog.cancel();

        Intent intent = new Intent(LogInActivity.this, VerifyMobile.class);
        intent.putExtra("SIM1", optName1);
        intent.putExtra("SIM2", optName2);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }

    private void registerUser(final String mobileNo, final String password) {

        //Added for SSL (17th Sep 2018)
        // HttpsTrustManager.allowAllSSL();

        // Tag used to cancel the request
        String tag_string_req = "req_register";

        //pDialog.setMessage("Registering ...");
        //showDialog();

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_LOGIN;
        Log.d("LoginURL", " " + url);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response1) {
                Log.d(TAG, "Register Response: " + response1.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response1);

                    String response = jObj.getString("response");
                    Log.d(TAG, "Register Response1: " + response);

                    SharedPreferences.Editor editor = getSharedPreferences(MySharedPrefLogin, MODE_PRIVATE).edit();
                    editor.putString("response", response);
                    editor.commit();


                    String status = jObj.getString("status");

                    if (status != null && status.equals("success")) {

                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor1 = settings.edit();
                        editor1.putString("logged", "logged");
                        editor1.commit();

//                        displayFirebaseRegId(response);

                        Intent intent = new Intent(LogInActivity.this, FilterActivity1.class);

                        startActivity(intent);

                    } else if ((status.equals("failed") && response.equals("UserName or Current password is WRONG"))) {


                        final AlertDialog alertDialog = new AlertDialog.Builder(LogInActivity.this, R.style.MyDialogTheme).create();
                        // Setting Dialog Title
                        alertDialog.setTitle("Sign in Error");

                        // Setting Dialog Message
                        alertDialog.setMessage("Please Check Your Credentials");

                        alertDialog.setCanceledOnTouchOutside(false);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog closed
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();
                    } else if (response.equals("Deactivated User")) {


                        final AlertDialog alertDialog = new AlertDialog.Builder(LogInActivity.this, R.style.MyDialogTheme).create();
                        // Setting Dialog Title
                        alertDialog.setTitle("Sign in Error");

                        // Setting Dialog Message
                        alertDialog.setMessage("You are blocked.Kindly contact at app@sabpaisa.in");

                        alertDialog.setCanceledOnTouchOutside(false);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog closed
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();
                    } else {

                        final AlertDialog alertDialog = new AlertDialog.Builder(LogInActivity.this, R.style.MyDialogTheme).create();
                        // Setting Dialog Title
                        alertDialog.setTitle("Sign in Error");

                        // Setting Dialog Message
                        alertDialog.setMessage("Please Check Your Credentials");

                        alertDialog.setCanceledOnTouchOutside(false);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog closed
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

                if (error.getMessage() == null || error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(LogInActivity.this, R.style.MyDialogTheme).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Network/Connection Error");

                    // Setting Dialog Message
                    alertDialog.setMessage("Internet Connection is poor OR The Server is taking too long to respond.Please try again later.Thank you.");

                    // Setting Icon to Dialog
                    //  alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

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


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobileNo", mobileNo);
                params.put("password", password);


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
            Log.d("LoginTLS", "In If Part");
            requestQueue = Volley.newRequestQueue(getApplicationContext(), stack);

        } else {
            Log.d("LoginTLS", "In Else Part");
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            //AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


/*
    private void registerUser(final String mobileNo , final String password) {


        // Tag used to cancel the request
        String tag_string_req = "req_register";

        //pDialog.setMessage("Registering ...");
        //showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.Base_Url+AppConfig.App_api+  AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                Log.d(TAG, "Register Response: " + response1.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response1);

                    String response = jObj.getString("response");
                    Log.d(TAG, "Register Response1: " + response);

                    SharedPreferences.Editor editor = getSharedPreferences(MySharedPrefLogin,MODE_PRIVATE).edit();
                    editor.putString("response",response);
                    editor.commit();


                    String status =jObj.getString("status");

                    if (status!=null && status.equals("success")){

                        Intent intent = new Intent(LogInActivity.this,FilterActivity.class);

                        startActivity(intent);

                    }else {

                        final AlertDialog alertDialog = new AlertDialog.Builder(LogInActivity.this, R.style.MyDialogTheme).create();
                        // Setting Dialog Title
                        alertDialog.setTitle("Sign in Error");

                        // Setting Dialog Message
                        alertDialog.setMessage("Please Check Your Credentials");

                        alertDialog.setCanceledOnTouchOutside(false);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog closed
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
                    AlertDialog alertDialog = new AlertDialog.Builder(LogInActivity.this, R.style.MyDialogTheme).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Network/Connection Error");

                    // Setting Dialog Message
                    alertDialog.setMessage("Internet Connection is poor OR The Server is taking too long to respond.Please try again later.Thank you.");

                    // Setting Icon to Dialog
                    //  alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

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



            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobileNo", mobileNo);
                params.put("password", password);



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
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
*/

    private void launchMainScreen() {
        startActivity(new Intent(LogInActivity.this, FilterActivity.class));

    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        moveTaskToBack(true);
        System.exit(0);

    }


    private void displayFirebaseRegId(String response) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        String isRegIdSaved = pref.getString("isRegIdSaved", null);
        Log.d("Fbid", "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId) && (isRegIdSaved == null)) {
            //Toast.makeText(this, "Firebase Reg Id: " + regId, Toast.LENGTH_SHORT).show();
            sendFCMTokenToDb(regId, response);

            SharedPreferences.Editor editor = pref.edit();
            //editor.putString("isRegIdSaved", "1");
            editor.commit();
        } else {
            Log.d("FCM(regId)", "Is Empty");
            //Toast.makeText(this, "Firebase Reg Id is not received yet!" + regId, Toast.LENGTH_SHORT).show();
        }
    }




    private void sendFCMTokenToDb(final String fcmToken, final String userAccessToken) {

        //Added for SSL (17th Sep 2018)
//        HttpsTrustManager.allowAllSSL();

        String tag_string_req = "req_clients";

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URl_FCM_TOKEN + "userToken=" + userAccessToken + "&fcmToken=" + fcmToken;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d("MainActivityFCMTocken", "--> " + response);

                try {

                    JSONObject jsonObject = new JSONObject(response.toString());
                    String status = jsonObject.getString("status");
                    String returnResponse = jsonObject.getString("response");

                    if (status.equals("success")) {


                        Log.d("MainActvtyFCMTokenInIF", "Fcm Token Has Updated " + returnResponse);

                    } else {

                        Log.d("MainActvtyFCMTokenInEls", "Fcm Token Has Not Updated " + returnResponse);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage() == null || error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Log.d("MainActivityFCMTocken", "onErrorResponse " + error.getMessage());

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

        AppController.getInstance().addToRequestQueue(request, tag_string_req);
    }






}