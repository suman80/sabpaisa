package in.sabpaisa.droid.sabpaisa.Util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.R;


/*extends BottomSheetDialog instead of Dialog*/
public class OtpDialog extends BottomSheetDialog {


    public Context context;
    public Button yes;
    String number;
    String otpTag = "is your OTP to verify your Mobile on SabPaisa. This is important for safety of your account and must be done before you proceed";
    Handler handler = new Handler();
    EditText optEditText = null;
    TextView timerTextView = null;
    // Button resentButton = null; /*Type is changed from TextView to Button, also name is refracted*/
    TextView waitTextView = null;
    ProgressDialog progressBar = null;
    public CountDownTimer countDownTimer = null;
    public BottomSheetDialog mBottomSheetDialog;

    public OtpDialog(Context context, String number) {
        super(context);
        this.context = context;
        this.number = number;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_otp);

        /*START Initiallizing BottomSheetDialog and giving its view in sheetView*/
        mBottomSheetDialog = new BottomSheetDialog(getContext());
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View sheetView = inflater.inflate(R.layout.popup_otp, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();

        /*END Initiallizing BottomSheetDialog and giving its view in sheetView*/

        //yes = (Button) sheetView.findViewById(R.id.btn_yes);
        //yes.setOnClickListener(this);
        // optEditText = (EditText) sheetVi
        // ew.findViewById(R.id.otp_enter_edittext);
        timerTextView = (TextView) sheetView.findViewById(R.id.timer_text_view);

        //resentButton = (Button) sheetView.findViewById(R.id.resend_button);
//        waitTextView = (TextView) findViewById(R.id.wait_text_view);
//        waitTextView.setOnClickListener(this);
        /*SmsReceiver.bindListener(new SmsListener() {
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
                            callVaryfyOTP(optSplit[1]);
                        }
                    }, 1000);
                }
            }
        });*/

        callTimerCoundown();

    }

/*   @Override
    public void onClick(View v) {
        if (v == yes) {

            if (!optEditText.getText().toString().equals("")) {
                String optText = optEditText.getText().toString();
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                callVaryfyOTP(optText);
            }*//* else {
                Toast.makeText(context, "Please enter OTP!", Toast.LENGTH_SHORT).show();
            }*//*
        } else if (v == resentButton) {
            sendOTP(number);
        } /*//*else if (v.getId() == R.id.wait_text_view) {
            callTimerCoundown();
        }*/



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
                dismiss();
//                resentButton.setClickable(true);
//                waitTextView.setClickable(true);
            }
        }.start();

    }

  /*  private void callVaryfyOTP(String otpText) {
        veryfiOTP(number, otpText);
        dismiss();
    }*/

    /*private void veryfiOTP(String number, String otp) {
        String urlJsonObj = "http://205.147.103.27:6060/SabPaisaPay/verifyOtp/" + otp + "/" + number;

//        progressBarShow();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("OTP", response.toString());

                try {
                    JSONObject jObj = new JSONObject(String.valueOf(response));
                    // Parsing json object response
                    // response will be a json object
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        SharedPref.putBoolean(context, AppConfiguration.OPT_VARIFICATION, true);

                        callUINVarificationScreen();
                        Toast.makeText(context,"OTP Verified ",Toast.LENGTH_LONG).show() ;
                    }
                    else if(status.equals("failure")) {
                        Toast.makeText(context,"Unable To send OTP Dialog", Toast.LENGTH_LONG).show();
                        // Error occurred in registration. Get the error
                        // message
                       *//* AlertDialog alertDialog = new AlertDialog.Builder();

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
                        alertDialog.show();*//*

                      //  String errorMsg = jObj.getString("error_msg");
                        //Toast.makeText(context,errorMsg, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                progressBarDismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("OTP", "Error: " + error.getMessage());
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                progressBarDismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
*/
   /* private void callUINVarificationScreen() {
        mBottomSheetDialog.dismiss();
        SharedPref.putBoolean(context, AppConfiguration.OPT_VARIFICATION, true);
        //Intent i = new Intent(getContext(), UIN.class);
        getContext().startActivity(i); //The method startActivity(Intent) is undefined for the type CustomDialog
        ((OTPVarify) context).finish();
    }*/

    private void sendOTP(final String number) {
//      String urlJsonObj = "http://43.252.89.122:6060/SabPaisaPay/SendOTP/" + number;

        String urlJsonObj = AppConfig.Base_Url+AppConfig.App_api+"SendOTP/" + number;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("OTP", response.toString());

                Toast.makeText(context, "OTP Request has been sent Successfully", Toast.LENGTH_LONG).show();
                callTimerCoundown();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("OTP", "Error: " + error.getMessage());
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void progressBarShow() {
        progressBar = new ProgressDialog(getContext());
        progressBar.setCancelable(true);
        progressBar.setMessage("File downloading ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
    }






}