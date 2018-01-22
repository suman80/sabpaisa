package in.sabpaisa.droid.sabpaisa.Util;





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

import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.FilterActivity;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.RegisterActivity;
import in.sabpaisa.droid.sabpaisa.VerifyMobile;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static in.sabpaisa.droid.sabpaisa.Util.TelephonyInfo.getOutput;
@RuntimePermissions
public class LoginActivityWithoutSharedPreference extends AppCompatActivity {

    private static final String TAG = in.sabpaisa.droid.sabpaisa.LogInActivity.class.getSimpleName();
    public static String mobileNo,password;
    EditText et_phone_number,et_password;

    TextView forgotPassword,register,passwordShow;
    Button login;

    public static String MySharedPrefLogin="mySharedPrefForlogin";

    public static final String PREFS_NAME = "LoginPrefs";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_without_shared_preference);

        //CommonUtils.setFullScreen(this);





        // password = (EditText)findViewById(R.id.et_password);
        passwordShow = (TextView)findViewById(R.id.tv_password_show);
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

                startActivity(new Intent(LoginActivityWithoutSharedPreference.this, ForgotActivity.class));

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
                    final AlertDialog alertDialog = new AlertDialog.Builder(in.sabpaisa.droid.sabpaisa.Util.LoginActivityWithoutSharedPreference.this, R.style.MyDialogTheme).create();
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
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivityWithoutSharedPreference.this, R.style.MyDialogTheme).create();

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
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("logged", "logged");
                    editor.commit();
                   /* SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString("MobileNumber",mobileNo);
                    editor.putString("Password", password);
                    editor.commit();*/
                    //sdkHandShake();

                } else {

                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivityWithoutSharedPreference.this,R.style.MyDialogTheme).create();

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
                Intent intent = new Intent(LoginActivityWithoutSharedPreference.this, RegisterActivity.class);
                startActivity(intent);

                // LogInActivityPermissionsDispatcher.isDualSimOrNotWithCheck(LogInActivity.this);
            }
        });


        //tv_register

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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        moveTaskToBack(true);
        System.exit(0);

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

        Intent intent = new Intent(LoginActivityWithoutSharedPreference.this,VerifyMobile.class);
        intent.putExtra("SIM1",optName1);
        intent.putExtra("SIM2",optName2);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }



    private void registerUser(final String mobileNo , final String password) {


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

                        Intent intent = new Intent(LoginActivityWithoutSharedPreference.this,FilterActivity.class);

                        startActivity(intent);

                    }else {

                        final AlertDialog alertDialog = new AlertDialog.Builder(LoginActivityWithoutSharedPreference.this, R.style.MyDialogTheme).create();
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
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivityWithoutSharedPreference.this, R.style.MyDialogTheme).create();

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

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void launchMainScreen() {
        startActivity(new Intent(LoginActivityWithoutSharedPreference.this, FilterActivity.class));

    }

}