package in.sabpaisa.droid.sabpaisa;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;

public class UpiSdkHandshake extends AppCompatActivity implements OliveUpiEventListener {


    int mode;
    public final int PERMISSION_REQUEST = 100;

    int REQUEST_SEND_SMS = 101;
    int REQUEST_MOBILE_NO = 102;
    int REQUEST_FINE_LOCATION=103;
    int REQUEST_COARSE_LOCATION=104;
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

Button HandShake_Btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_upi_sdk_handshake);

        OliveUpiManager.getInstance(UpiSdkHandshake.this).setListener(this);
        getResponse();

        HandShake_Btn=(Button)findViewById(R.id.HandShake_Btn);
        HandShake_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceId();
            }
        });

    }

    //getResponse method is used for getting response using volley lib from merchant server
    private void getResponse() {

        // Tag used to cancel the request
        String tag_string_req = "req_register";
        final JSONObject jsonObject = new JSONObject();
        //sending parameters to merchant server
        try {
            jsonObject.put("mcccode", mcccode);

            jsonObject.put("mobilenumber", mobilenumber);

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

                Toast.makeText(UpiSdkHandshake.this, "Response-->" + response.toString(), Toast.LENGTH_SHORT).show();

                try {

                    JSONObject jObj = new JSONObject(response.toString());

                    String code=response.getString("code");

                    if (code.equals("00")) {
                        //extracting merchantauthtoken from json response
                        responseMerchantauthtoken = (jObj.getJSONObject("data").get("merchantauthtoken").toString());

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
                Toast.makeText(UpiSdkHandshake.this, "Server is down !", Toast.LENGTH_SHORT).show();

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


    public void getDeviceId() {

        if (!checkForPermission(Manifest.permission.SEND_SMS, REQUEST_SEND_SMS)) {
            // Log.d("", "getMobileNumberRequest: return1");
            return;
        }
        if (!checkForPermission(Manifest.permission.READ_PHONE_STATE, REQUEST_MOBILE_NO))  {
            // Log.d("", "getMobileNumberRequest: return2");
            return;
        }

        if (!checkForPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_FINE_LOCATION))  {
            // Log.d("", "getMobileNumberRequest: return2");
            return;
        }

        if (!checkForPermission(Manifest.permission.ACCESS_COARSE_LOCATION, REQUEST_COARSE_LOCATION))  {
            // Log.d("", "getMobileNumberRequest: return2");
            return;
        }

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
        sdkHandshake.setDeviceid(deviceId);   //Not done or test value
        sdkHandshake.setEmailId("divve@olivecrypto.com");    //Not done or test value
        sdkHandshake.setMcccode(mcccode);
        sdkHandshake.setMerchanttoken(responseMerchantauthtoken);
        sdkHandshake.setMerchChanId(merchChanId);
        sdkHandshake.setMerchId(merchId);
        sdkHandshake.setMobileNumber(mobilenumber);
        sdkHandshake.setOrderid("SabPaisa");                    //Not done or test value
        sdkHandshake.setSubmerchantid(submerchantid);
        sdkHandshake.setUnqCustId("918096449293");                  //Not done or test value
        sdkHandshake.setUnqTxnId("Sabpaisa" + new Date().getTime());                   //Not done or test value

/*------------------------------------------------------------------------------------------------------------------------*/

        OliveUpiManager.getInstance(UpiSdkHandshake.this).initiateSDK(sdkHandshake);




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
                Toast.makeText(UpiSdkHandshake.this,"Success" , Toast.LENGTH_SHORT).show();

            }

        }
        else{
            Toast.makeText(this,"Not Success",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailureResponse(int i, Object o) {

    }


    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:sss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


    public void permissionGranted(int mode) {

        if (mode == REQUEST_MOBILE_NO || mode == REQUEST_SEND_SMS) {
            getDeviceId();
        }
    }

    public boolean checkForPermission(String permission, int mode) {
        this.mode = mode;
        int permissionCheck = ContextCompat.checkSelfPermission(this, permission);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            //if(com.olive.upi.transport.TransportConstants.appRelease) Log.d("", "checkForPermission: 1");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    permission)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {

                // No explanation needed, we can request the permission.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{permission}, PERMISSION_REQUEST);
                }

            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        //if(com.olive.upi.transport.TransportConstants.appRelease) Log.d("", "onRequestPermissionsResult: 1");
        switch (requestCode) {
            case PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.

                //if(com.olive.upi.transport.TransportConstants.appRelease) Log.d("", "onRequestPermissionsResult: 2");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    //if(com.olive.upi.transport.TransportConstants.appRelease) Log.d("", "onRequestPermissionsResult: 3");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    permissionGranted(mode);

                } else {

                    // permission denied, Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }



}
