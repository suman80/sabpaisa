package in.sabpaisa.droid.sabpaisa;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.InstitutionAdapter;
import in.sabpaisa.droid.sabpaisa.Model.FetchUserImageGetterSetter;
import in.sabpaisa.droid.sabpaisa.Model.Institution;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;


public class UIN extends AppCompatActivity {
String clientId,userAccessToken,response;
    EditText uinnnumber;
    //TextView clientname;
    String clientname;
    String userImageUrl;
    //TextView clientNAmeTextview;
    ImageView clientImagePath,clientlogopath;
    public  static  String MYSHAREDPREFUIN="mySharedPref11";
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public static String clientImageURLPath=null;
    public static String clientLogoURLPath=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_uin_verification);
        setTheme(R.style.popupTheme);
        //getClientsList(clientId.toString());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         uinnnumber = (EditText) findViewById(R.id.uiinnum);
        clientImagePath=(ImageView)findViewById(R.id.institutepic);
clientlogopath=(ImageView)findViewById(R.id.institutelogo);


        clientId=getIntent().getStringExtra("clientId");
        clientname=getIntent().getStringExtra("clientname");
        clientImageURLPath=getIntent().getStringExtra("clientImagePath");
        clientLogoURLPath=getIntent().getStringExtra("clientLogoPath");

       TextView clientNAmeTextview=(TextView)findViewById(R.id.InstitueNAme);


        clientNAmeTextview.setText(clientname);
Log.d("ClientIduin","__>"+clientId);
Log.d("clientImageURLPathuin","__>"+clientImageURLPath);
Log.d("clientImageURLPathuin","__>"+clientLogoURLPath);
Log.d("clientImageURLPathuin","__>"+clientname);
        new UIN.DownloadImageTask(clientImagePath).execute(clientImageURLPath);
        new UIN.DownloadImageTask(clientlogopath).execute(clientLogoURLPath);

        //clientName.setText(R.id.InstitueNAme);

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        response = sharedPreferences.getString("response", "123");

        userAccessToken = response;

        Log.d("AccessToken111", " " + userAccessToken);

        Log.d("FFResponse11111", " " + response);

        getUserIm(userAccessToken);

        SharedPreferences.Editor editor = getSharedPreferences(MYSHAREDPREFUIN,MODE_PRIVATE).edit();
        editor.putString("clientId",clientId);
        editor.commit();

    }

    public void onVerifyBtn(View view) {
        if (!isOnline()){

            AlertDialog alertDialog = new AlertDialog.Builder(UIN.this, R.style.MyDialogTheme).create();

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

            // Toast.makeText(OTPVarify.this, "first name field is empty", Toast.LENGTH_LONG).show();
        }


        else if(uinnnumber.getText().toString().equals("")){
             AlertDialog.Builder builder =new AlertDialog.Builder(UIN.this);
            builder.setTitle("Empty UIN");
            builder.setMessage("Please Enter UIN Number");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show(); Toast.makeText(UIN.this, "Enter Uin Number", Toast.LENGTH_LONG).show();

        }

       else if (!uinnnumber.getText().toString().equals("")) {

            showpDialog(view);
            callVerifyUINNumber(uinnnumber.getText().toString(),clientId,userAccessToken);


        }


        else {
            Toast.makeText(UIN.this, "Enter Uin Number", Toast.LENGTH_LONG).show();
        }
    }

    public void callVerifyUINNumber(String uinnnumber, String clientId, String userAccessToken) {
        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_VerifyUin+uinnnumber +"&client_Id="+clientId+"&aceesToken="+userAccessToken, new Response.Listener<String>()  {

            @Override
            public void onResponse(String response) {


                Log.d("Verify UINnhhhh", response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    JSONObject jObj = new JSONObject(response);

                    String response1 = jObj.getString("response");
                    //Log.d(TAG, "Register Response1: " + response);
                    String status = jObj.getString("status");
                    //String response1 = response.getString("response");
                   // Log.i("status_UIN", "status=" + status);
                    Log.i("response_UIN", "Repsomse_UIN=" + response1);
                    if (response1.equals("UIN verified")) {
                        callMainScreen();
                    } else {
                        if (response1.equals("Invalid UIN number")) {
                            AlertDialog alertDialog = new AlertDialog.Builder(UIN.this, R.style.MyDialogTheme).create();

                            // Setting Dialog Title
                            alertDialog.setTitle("UIN Does not match");

                            // Setting Dialog Message
                            alertDialog.setMessage("Please Enter Correct UNI Number");

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
                            callErrorPopup();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage()==null ||error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(UIN.this, R.style.MyDialogTheme).create();

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
               /* VolleyLog.d("eclipse", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();*/

                // hide the progress dialog
                hidepDialog();
            }
        });
        // Adds the JSON array request "arrayreq" to the request queue
        AppController.getInstance().addToRequestQueue(strReq,tag_string_req);

       // AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            //Log.v(TAG, "Internet Connection Not Present");
            return false;
        }
    }
    private void callErrorPopup() {
        UIN_Dialog uniErrorPopUp = new UIN_Dialog(this);
        uniErrorPopUp.show();
    }


    private void callMainScreen() {
        Intent intent = new Intent(UIN.this,MainActivity.class);
        intent.putExtra("clientId", clientId);
        intent.putExtra("userImageUrl", userImageUrl);

        startActivity(intent);
        UIN.this.finish();
    }


    ProgressDialog loading = null;

    private void hidepDialog() {
        if (loading != null) {
            loading.dismiss();
        }
    }

    private void showpDialog(View v) {
        loading = new ProgressDialog(v.getContext());
        loading.setCancelable(true);
        loading.setMessage("UIN Verifying....");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.show();
    }

    private class DownloadLogoTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loading.show();
        }

        public DownloadLogoTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            //loading.dismiss();
        }

    }


    //Code for fetching image from server
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loading.show();
        }

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            //loading.dismiss();
        }

    }

    private void getUserIm(final  String token) {

        String  tag_string_req = "req_clients";

        StringRequest request=new StringRequest(Request.Method.GET,AppConfig.URL_UserProfilephot   +token, new Response.Listener<String>(){

            @Override
            public void onResponse(String response1)
            {

                Log.d("Particularclientimage","-->"+response1);
                //parsing Json
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response1.toString());
                    String response = jsonObject.getString("response");
                    String status = jsonObject.getString("status");
                    Log.d("responsus",""+response);
                    Log.d("statsus",""+status);
                    JSONObject jsonObject1 = new JSONObject(response);
                    FetchUserImageGetterSetter fetchUserImageGetterSetter=new FetchUserImageGetterSetter();fetchUserImageGetterSetter.setUserImageUrl(jsonObject1.getString("userImageUrl"));
                    userImageUrl=fetchUserImageGetterSetter.getUserImageUrl().toString();

                    Log.d("userImageUrlactivity",""+userImageUrl);
                 /*   ClientData clientData=new ClientData();
                    clientData.setClientLogoPath(jsonObject1.getString("clientLogoPath"));
                    clientData.setClientImagePath(jsonObject1.getString("clientImagePath"));
                    clientData.setClientName(jsonObject1.getString("clientName"));

                    clientLogoPath=clientData.getClientLogoPath().toString();
                    clientImagePath=clientData.getClientImagePath().toString();
                    clientname11=clientData.getClientName().toString();
                    // clientname=clientData.getClientName().toString();
                    Log.d("clientlogooooo","-->"+clientLogoPath );
                    Log.d("clientimageooo","-->"+clientImagePath );
                    Log.d("clientiooo","-->"+clientname11 );*/


                }
                catch(JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage()==null ||error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getApplication(), R.style.MyDialogTheme).create();

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
                    //Log.e(TAG, "Registration Error: " + error.getMessage());

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


        }) ;

        AppController.getInstance().addToRequestQueue(request,tag_string_req);


    }

}
