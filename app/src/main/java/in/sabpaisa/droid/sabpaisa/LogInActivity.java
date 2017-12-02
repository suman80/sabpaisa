package in.sabpaisa.droid.sabpaisa;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.ForgotActivity;
import in.sabpaisa.droid.sabpaisa.Util.TelephonyInfo;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static in.sabpaisa.droid.sabpaisa.Util.TelephonyInfo.getOutput;

@RuntimePermissions
public class LogInActivity extends AppCompatActivity {
    private static final String TAG = LogInActivity.class.getSimpleName();
    String mobileNo;
    EditText et_phone_number;
    EditText password;
    TextView forgotPassword,register,passwordShow;
    Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
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

                    String status =jObj.getString("status");

                    if (status!=null && status.equals("success")){
                        launchMainScreen();
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

}
