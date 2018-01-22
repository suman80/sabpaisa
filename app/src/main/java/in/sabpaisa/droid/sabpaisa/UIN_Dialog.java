package in.sabpaisa.droid.sabpaisa;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import in.sabpaisa.droid.sabpaisa.Util.AppConfig;

public class UIN_Dialog extends Dialog  {

    public Context c;
    public Button yes,no;
    String clientId,userAccessToken;

    public UIN_Dialog(Context context) {
        super(context);
        c = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_uni_veryfi_error);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(UIN.MYSHAREDPREFUIN, Context.MODE_PRIVATE);
        clientId=sharedPreferences.getString("clientId","123");
        userAccessToken=sharedPreferences.getString("userAccessToken","123");


        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//UIN uin=new UIN();
                RequestUINNumber(clientId,userAccessToken);
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
    public void RequestUINNumber( String clientId, String userAccessToken) {
        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_RequestUin+clientId+"&aceesToken="+userAccessToken, new Response.Listener<String>()  {

            @Override
            public void onResponse(String response) {


                Log.d("RequestUIN", response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    JSONObject jObj = new JSONObject(response);

                    String response1 = jObj.getString("response");
                    //Log.d(TAG, "Register Response1: " + response);
                    String status = jObj.getString("status");
                    //String response1 = response.getString("response");
                    // Log.i("status_UIN", "status=" + status);
                    Log.i("response_ReuestUIN", "Repsomse_RequestUIN=" + response1);
                    if (response1.equals("Request Sent")) {

                        AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Sent");

                        // Setting Dialog Message
                        alertDialog.setMessage("Request Sent");
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


                    } else if (response1.equals("User_Not_Registered")){
                        AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme).create();

                        // Setting Dialog TiRehtle
                        alertDialog.setTitle("USer  Not Registered");

                        // Setting Dialog Message
                        alertDialog.setMessage("USer  Not Registered");

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
                    /*Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();*/
                }
                //  hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage()==null ||error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme).create();

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

                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                } else if (error instanceof NetworkError) {
                    //TODO
                } else if (error instanceof ParseError) {
                    //TODO
                }
                VolleyLog.d("eclipse", "Error: " + error.getMessage());
               /* Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();*/

                // hide the progress dialog
                //      hidepDialog();
            }
        });
        // Adds the JSON array request "arrayreq" to the request queue
        AppController.getInstance().addToRequestQueue(strReq,tag_string_req);

        // AppController.getInstance().addToRequestQueue(jsonObjReq);
    }



}