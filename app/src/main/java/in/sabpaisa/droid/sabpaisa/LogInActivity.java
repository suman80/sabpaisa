package in.sabpaisa.droid.sabpaisa;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.olive.upi.OliveUpiManager;
import com.olive.upi.transport.OliveUpiEventListener;
import com.olive.upi.transport.api.Result;
import com.olive.upi.transport.api.UpiService;
import com.olive.upi.transport.model.sdk.SDKHandshake;

import org.json.JSONException;
import org.json.JSONObject;

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

import static in.sabpaisa.droid.sabpaisa.Util.TelephonyInfo.getOutput;

@RuntimePermissions
public class LogInActivity extends AppCompatActivity implements OliveUpiEventListener {
    private static final String TAG = LogInActivity.class.getSimpleName();
    public static String mobileNo;
    EditText et_phone_number;
    EditText password;
    TextView forgotPassword,register,passwordShow;
    Button login;
    public static String MySharedPrefLogin="mySharedPrefForlogin";
    /////////////////////////////////////////////////////////////////////
    private String deviceId;
    SDKHandshake sdkHandshake;

    /*STEP:1
 These variables or parameters are used for Merchant server API to get token
*/
    private static String URL = "http://205.147.103.27:6060/SabPaisaAppApi/getAccessToken"; //merchant server

    private String mcccode = "123456";

    private String mobilenumber = "9711978706";

    private String unqCustId = "918096449293";

    private String merchChanId = "121";

    private String emailId = "divve@olivecrypto.com";

    private String submerchantid = "550";

    private String merchId = "131";

    // private String timestamp ;

    private String unqTxnId = "831258945263";

    private String responseMerchantauthtoken = "";  //responseMerchantauthtoken extracted from response and store here.




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);

        setContentView(R.layout.activity_login);

        OliveUpiManager.getInstance(LogInActivity.this).setListener(this);
        //getResponse();


        // password = (EditText)findViewById(R.id.et_password);
        //passwordShow = (TextView)findViewById(R.id.tv_password_show);
        forgotPassword = (TextView)findViewById(R.id.tv_forgot_password);
        et_phone_number = (EditText)findViewById(R.id.et_phone_number);
        register = (TextView)findViewById(R.id.tv_register);
        login = (Button)findViewById(R.id.btn_login);
        //DataBinding();
//        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LogInActivity.this, ForgotActivity.class));

            }
        });









        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 mobileNo = et_phone_number.getText().toString();
                Log.e(TAG, "response: " + mobileNo);

                if ((et_phone_number.length() == 0)  ||(et_phone_number.length()<10)) {

                    et_phone_number.setError("Please make sure that You have entered 10 digit number");

                } else if(isOnline())
                {
                    //Intent intent21 = new Intent(Name.this, Gender.class);

                    //startActivity(intent21);
                    //launchAgeScreen();
                    registerUser(mobileNo);
                    //sdkHandShake();

                }
                else {

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
                Intent intent = new Intent(LogInActivity.this,RegisterActivity.class);
                startActivity(intent);

                // LogInActivityPermissionsDispatcher.isDualSimOrNotWithCheck(LogInActivity.this);
            }
        });



        //tv_register

        /*passwordShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordShow.getText().toString().equals("Show")){
                    passwordShow.setText("Hide");
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                }else
                {
                    passwordShow.setText("Show");
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });*/
    }


    public void sdkHandShake(){
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        deviceId = telephonyManager.getDeviceId();


                    /* STEP:2
        This API used to activate the session and onboards the customer into SDK.
                This API verifies the mobile number for new customers.*/

                   /*All parameters are necessary for handShake and parameters are provided by Sabpaisa*/

        //creating object of SDKHandshake
        sdkHandshake = new SDKHandshake();
        //setting values for SDKHandshake
        sdkHandshake.setAppid("com.olive.upi.sdk");      //Not done or test value
        sdkHandshake.setCustName("Divve");   //Not done or test value
        sdkHandshake.setDeviceid(deviceId.toString());   //Not done or test value
        sdkHandshake.setEmailId("divve@olivecrypto.com");    //Not done or test value
        sdkHandshake.setMcccode(mcccode);
        sdkHandshake.setMerchanttoken(responseMerchantauthtoken);
        sdkHandshake.setMerchChanId(merchChanId);
        sdkHandshake.setMerchId(merchId);
        sdkHandshake.setMobileNumber(mobileNo);
        sdkHandshake.setOrderid("SabPaisa");                    //Not done or test value
        sdkHandshake.setSubmerchantid(submerchantid);
        sdkHandshake.setUnqCustId("918096449293");                  //Not done or test value
        sdkHandshake.setUnqTxnId("Sabpaisa" + new Date().getTime());                   //Not done or test value

/*------------------------------------------------------------------------------------------------------------------------*/

        OliveUpiManager.getInstance(LogInActivity.this).initiateSDK(sdkHandshake);



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
        forgotPassword = (TextView)findViewById(R.id.tv_forgot_password);
        register = (TextView)findViewById(R.id.tv_register);
        login = (Button)findViewById(R.id.btn_login);
    }

    @NeedsPermission(Manifest.permission.READ_PHONE_STATE)
    public void isDualSimOrNot(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = this.getLayoutInflater().inflate(R.layout.bottomdialog_getting_sim_info, null);
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
        TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);
        String optName1 = getOutput(this, "SimOperatorName", 0);
        String optName2 = getOutput(this, "SimOperatorName", 1);
        String optName3 = getOutput(this, "SimOperatorName", 2);
        bottomSheetDialog.cancel();

        Intent intent = new Intent(LogInActivity.this,VerifyMobile.class);
        intent.putExtra("SIM1",optName1);
        intent.putExtra("SIM2",optName2);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }



    private void registerUser(final String mobileNo ) {


        // Tag used to cancel the request
        String tag_string_req = "req_register";

        //pDialog.setMessage("Registering ...");
        //showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

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
                        //sdkHandShake();
                        getResponse();
                        //launchMainScreen();
                    }else {

                        final AlertDialog alertDialog = new AlertDialog.Builder(LogInActivity.this, R.style.MyDialogTheme).create();
                        // Setting Dialog Title
                        alertDialog.setTitle("Sign Error");

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



                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void launchMainScreen() {
        startActivity(new Intent(LogInActivity.this, FilterActivity.class));

    }


    //getResponse method is used for getting response using volley lib from merchant server
    private void getResponse() {

        // Tag used to cancel the request
        String tag_string_req = "req_register";
        final JSONObject jsonObject = new JSONObject();
        //sending parameters to merchant server
        try {
            jsonObject.put("mcccode", mcccode);

            jsonObject.put("mobilenumber", mobileNo);

            jsonObject.put("unqCustId", unqCustId);

            jsonObject.put("merchChanId", merchChanId);

            jsonObject.put("emailId", emailId);

            jsonObject.put("submerchantid", submerchantid);

            jsonObject.put("merchId", merchId);

            jsonObject.put("timestamp", getDateTime());

            jsonObject.put("unqTxnId", unqTxnId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Make request for JSONObject
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //Toast.makeText(LogInActivity.this, "Response-->" + response.toString(), Toast.LENGTH_SHORT).show();

                try {

                    JSONObject jObj = new JSONObject(response.toString());

                    String code=response.getString("code");

                    if (code.equals("00")) {
                        //extracting merchantauthtoken from json response
                        responseMerchantauthtoken = (jObj.getJSONObject("data").get("merchantauthtoken").toString());
                        sdkHandShake();
                    }else if (code.equals("OM01")){
                        Toast.makeText(getApplicationContext(),"MERCHANT ID NOT AVAILABLE",Toast.LENGTH_SHORT).show();
                    }else if (code.equals("OM02")){
                        Toast.makeText(getApplicationContext(),"MOBILE NUMBER NOT AVAILABLE",Toast.LENGTH_SHORT).show();
                    }else if (code.equals("OM03")){
                        Toast.makeText(getApplicationContext(),"MERCHANT NOT REGISTRED",Toast.LENGTH_SHORT).show();
                    }else if (code.equals("OM04")){
                        Toast.makeText(getApplicationContext(),"TECHNICAL ERROR",Toast.LENGTH_SHORT).show();
                    }else if (code.equals("OM05")){
                        Toast.makeText(getApplicationContext(),"MERCHANT KEY NOT AVAILABLE",Toast.LENGTH_SHORT).show();
                    }else if (code.equals("OM06")){
                        Toast.makeText(getApplicationContext(),"MERCHANT IP NOT AVAILABLE",Toast.LENGTH_SHORT).show();
                    }else if (code.equals("OM07")){
                        Toast.makeText(getApplicationContext(),"INVALID MERCHANT IP",Toast.LENGTH_SHORT).show();
                    }else if (code.equals("OM08")){
                        Toast.makeText(getApplicationContext(),"CHECKSUM NOT AVAILABLE",Toast.LENGTH_SHORT).show();
                    }else if (code.equals("OM09")){
                        Toast.makeText(getApplicationContext(),"INVALID MERCHANT DATA",Toast.LENGTH_SHORT).show();
                    }else if (code.equals("OM10")){
                        Toast.makeText(getApplicationContext(),"MANDATORY FIELDS NOT PRESENT",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(MainActivity.this, "Error-->" + error.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(LogInActivity.this, "Server is down !", Toast.LENGTH_SHORT).show();

                error.printStackTrace();
            }


        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:sss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


    @Override
    public void onSuccessResponse(int reqType, Object data) {
        Log.d("Main", "onSuccessResponse: reqType "+reqType+" data "+data);
        if(reqType == UpiService.REQUEST_GET_MOBILE) {

            Log.d("entered into", "Request Mobile");
            Result<String> result = (Result<String>) data;

            if (result.getCode().equals("00")) {
                Toast.makeText(this, result.getResult(), Toast.LENGTH_SHORT).show();
                Log.d("MOBILE NUMBER", "" + result.getData().toString());
                Log.d("+++", "" + result.getResult());


            }
        }
        else if(reqType==UpiService.REQUEST_SDK_HANDSHAKE){
            Result<String> sdkHandShake = (Result<String>) data;
            Log.d("sdkHandShake"," --> "+sdkHandShake);
            if (sdkHandShake.getCode().equals("00")){
                Toast.makeText(LogInActivity.this,"Success" , Toast.LENGTH_SHORT).show();
                launchMainScreen();
            }

        }
        else{
            Toast.makeText(this,"Not Success",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailureResponse(int reqType, Object data) {
        Toast.makeText(LogInActivity.this,"Fail" , Toast.LENGTH_SHORT).show();
    }





}