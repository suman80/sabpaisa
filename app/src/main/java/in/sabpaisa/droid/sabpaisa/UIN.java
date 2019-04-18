package in.sabpaisa.droid.sabpaisa;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Contacts;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.InstitutionAdapter;
import in.sabpaisa.droid.sabpaisa.AppDB.ClientsDB;
import in.sabpaisa.droid.sabpaisa.Fragments.ClientFilterFragment;
import in.sabpaisa.droid.sabpaisa.Model.ClientData;
import in.sabpaisa.droid.sabpaisa.Model.ClientsDataModel;
import in.sabpaisa.droid.sabpaisa.Model.FetchUserImageGetterSetter;
import in.sabpaisa.droid.sabpaisa.Model.Institution;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;


public class UIN extends AppCompatActivity {
    String userAccessToken, response;
    String clientId;

    EditText uinnnumber;
    String userImageUrl;
    //TextView clientname;
    String clientname;
    String m;
    TextView clientNAmeTextview;
    ImageView clientImagePath, clientlogopath;
    public static String MYSHAREDPREFUIN = "mySharedPref11";
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public static String clientImageURLPath = null;
    public static String clientLogoPath = null;

    public static String SHARED_PREF_FOR_CHECK_USER = "checkUserForAdmin";
    public static String SHARED_PREF_UIN_STATUS = "sharedPrefUinStatus";

    ClientsDB clientsDB;

    String cobLoginId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_uin_verification);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTheme(R.style.popupTheme);
        //getClientsList(clientId.toString());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        uinnnumber = (EditText) findViewById(R.id.uiinnum);
        clientImagePath = (ImageView) findViewById(R.id.institutepic);
        clientlogopath = (ImageView) findViewById(R.id.institutelogo);


        //clientId=getIntent().getStringExtra("clientId");
        // clientname=getIntent().getStringExtra("clientname");
        // clientImageURLPath=getIntent().getStringExtra("clientImagePath");
        //  clientLogoURLPath=getIntent().getStringExtra("clientLogoPath");

        clientNAmeTextview = (TextView) findViewById(R.id.InstitueNAme);

        SharedPreferences sharedPreferences1 = getApplication().getSharedPreferences(ClientFilterFragment.MySharedPreffilter, Context.MODE_PRIVATE);
        clientId = sharedPreferences1.getString("clientId", "abc");
        getClientsList(clientId);

        // clientNAmeTextview.setText(clientname);
        Log.d("ClientIduin", "__>" + clientId);
        Log.d("clientImageURLPathuin", "__>" + clientImageURLPath);
        Log.d("clientImageURLPathuin", "__>" + clientLogoPath);
        Log.d("clientImageURLPathuin", "__>" + clientname);
        Log.d("clientImageURLPathuin", "__>" + clientNAmeTextview);
        // new UIN.DownloadImageTask(clientImagePath).execute(clientImageURLPath);
        // new UIN.DownloadImageTask(clientlogopath).execute(clientLogoURLPath);
       /* Glide
                .with(UIN.this)
                .load(clientLogoPath)
                .error(R.drawable.sabpaisa)
                .into(clientlogopath);
        Glide
                .with(UIN.this)
                .load(clientImageURLPath)
                .error(R.drawable.sabpaisa)
                .into(clientImagePath);
*/
        //clientName.setText(R.id.InstitueNAme);

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        response = sharedPreferences.getString("response", "123");
         cobLoginId = sharedPreferences.getString("cobLoginId", null);
        Log.d("cobLoginId", "______ " + cobLoginId);
        userAccessToken = response;

        Log.d("AccessToken111", " " + userAccessToken);

        Log.d("FFResponse11111", " " + response);

        getUserImage(userAccessToken);
        m = "abc";
        SharedPreferences.Editor editor = getSharedPreferences(MYSHAREDPREFUIN, MODE_PRIVATE).edit();
        editor.putString("clientId", clientId);
        editor.putString("m", m);
        editor.putString("userAccessToken", userAccessToken);
        editor.commit();

        checkUserForAdmin(userAccessToken, clientId);

        clientsDB = new ClientsDB(UIN.this);

    }


    public void onVerifyBtn(View view) {
        if (!isOnline()) {

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
        } else if (uinnnumber.getText().toString().equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(UIN.this);
            builder.setTitle("Empty UIN");
            builder.setMessage("Please Enter UIN Number");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            //Toast.makeText(UIN.this, "Enter Uin Number", Toast.LENGTH_LONG).show();

        } else if (!uinnnumber.getText().toString().equals("")) {

            showpDialog(view);
            callVerifyUINNumber(uinnnumber.getText().toString(), clientId, userAccessToken);


        } else {
            Toast.makeText(UIN.this, "Enter Uin Number", Toast.LENGTH_LONG).show();
        }
    }

    public void callVerifyUINNumber(final String uinnnumber, final String clientId, final String userAccessToken) {
        String tag_string_req = "req_register";
        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_VerifyUin + uinnnumber + "&client_Id=" + clientId + "&aceesToken=" + userAccessToken;

        if(cobLoginId != null && !cobLoginId.equals("null"))
            url +="&cobLoginId="+cobLoginId;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

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
                    if (status.equals("success") && response1.equals("UIN verified")) {

                        Log.d("InIfPArt", "UINVerifu");


                        SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREF_UIN_STATUS, MODE_PRIVATE).edit();
                        editor.putString("UIN_STATUS", "UIN_VERIFIED");
                        editor.putString("UIN_NUMBER", uinnnumber);
                        editor.commit();


                        ///////////////////////Client Db//////////////////////////////////////

                        Cursor res = clientsDB.getParticularClientData1(clientId,userAccessToken);
                        if (res.getCount() > 0) {
                            Log.d("Data","Already Exists");

                            StringBuffer stringBuffer = new StringBuffer();


                            while (res.moveToNext()){
                                stringBuffer.append(res.getString(0) + " ");
                                stringBuffer.append(res.getString(1) + " ");
                                /*stringBuffer.append(res.getString(2) + " ");
                                stringBuffer.append(res.getString(3) + " ");
                                stringBuffer.append(res.getString(4) + " ");
                                stringBuffer.append(res.getString(5) + " ");
                                stringBuffer.append(res.getString(6) + " ");*/

                            }

                            Log.d("GETTING_ALL","StrBuffer__"+stringBuffer);
                            callMainScreen();
                        }else {

                            final ClientsDataModel clientsDataModel = new ClientsDataModel();
                            clientsDataModel.setClientId(clientId);
                            Log.d("clientNameUIN", "____" + clientname);
                            clientsDataModel.setClientName(clientname);
                            clientsDataModel.setCobCustId(null);
                            clientsDataModel.setUinNo(uinnnumber);
                            clientsDataModel.setUserAccessToken(userAccessToken);

                            Log.d("clientLogoPathUIN", "___" + clientLogoPath);

                            Glide.with(getApplicationContext())
                                    .load(clientLogoPath)
                                    .asBitmap()
                                    .into(new SimpleTarget<Bitmap>(100, 100) {
                                        @Override
                                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                            Log.d("LogoBitmap", " " + resource);
                                            //saveLogoToInternalStorage(resource , feedData.getFeedId());


                                            ContextWrapper cw = new ContextWrapper(UIN.this);
                                            // path to /data/data/yourapp/app_data/imageDir
                                            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                                            // Create imageDir
                                            File mypath = new File(directory, clientsDataModel.getClientId() + "clientImg.jpg");

                                            Log.d("mypathjzbjhzb", "mypath  " + mypath);

                                            String logoPath = mypath.toString();


                                            FileOutputStream fos = null;
                                            try {
                                                fos = new FileOutputStream(mypath);
                                                // Use the compress method on the BitMap object to write image to the OutputStream
                                                resource.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            } finally {
                                                try {
                                                    fos.close();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            Log.d("UIN_LogoPath", "__" + logoPath);
                                            clientsDataModel.setClientImageUrl(logoPath);

                                        }
                                    });


                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do something after 5s = 5000ms
                                    boolean isInserted = clientsDB.insertClientData(clientsDataModel);

                                    if (isInserted) {
                                        Log.d("UIN", "LocalDBInIfPart____" + isInserted);
                                        callMainScreen();
                                    } else {
                                        Log.d("UIN", "LocalDBInElsePart____" + isInserted);
                                    }

                                }
                            }, 2000);

                        }


                    } else if (status.equals("success") && response1.equals("Invalid UIN number")) {

                        Log.d("InelseIf1PArt", "UINVerifu");

                        AlertDialog alertDialog = new AlertDialog.Builder(UIN.this, R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("UIN Does not match");

                        // Setting Dialog Message
                        alertDialog.setMessage("Please Enter Correct UIN Number");

                        // Setting Icon to Dialog
                        //  alertDialog.setIcon(R.drawable.tick);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                   /* SharedPreferences settings = getSharedPreferences(UIN.MYSHAREDPREFUIN, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.remove("m");
                                    editor.remove("selectedstate");
                                    editor.remove("selectedservice");
                                    editor.remove("logged");
                                    editor.clear();
                                    editor.commit();
                                    finish();

                                    Intent intent=new Intent( UIN.this, FilterActivity.class);

                                    startActivity(intent);
*/
                                // Write your code here to execute after dialog closed
                                // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();

                    } else if (status.equals("failed") && response1.equals("User is Blocked")) {

                        Log.d("InElsePart", "UINVerifu");

                        AlertDialog alertDialog = new AlertDialog.Builder(UIN.this, R.style.MyDialogTheme).create();

                        alertDialog.setTitle("UIN");

                        alertDialog.setMessage(response1);

                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();

                    } else {
                        callErrorPopup();
                        Log.d("InelsePArt", "UINVerifu");

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

                if (error.getMessage() == null || error instanceof TimeoutError || error instanceof NoConnectionError) {
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
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        // AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        //Log.v(TAG, "Internet Connection Not Present");
        return cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected();
    }

    private void callErrorPopup() {
        UIN_Dialog uniErrorPopUp = new UIN_Dialog(this);
        uniErrorPopUp.show();
    }

    private void callMainScreen() {
        Intent intent = new Intent(UIN.this, MainActivity.class);
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


    private void getUserImage(final String token) {

        String tag_string_req = "req_clients";

        StringRequest request = new StringRequest(Request.Method.GET, AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_Show_UserProfileImage + "?token=" + token, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {

                Log.d("Particularclientimage", "-->" + response1);
                //parsing Json
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response1.toString());
                    String response = jsonObject.getString("response");
                    String status = jsonObject.getString("status");
                    Log.d("responsus", "" + response);
                    Log.d("statsus", "" + status);
                    JSONObject jsonObject1 = new JSONObject(response);
                    FetchUserImageGetterSetter fetchUserImageGetterSetter = new FetchUserImageGetterSetter();
                    fetchUserImageGetterSetter.setUserImageUrl(jsonObject1.getString("userImageUrl"));
                    userImageUrl = fetchUserImageGetterSetter.getUserImageUrl().toString();

                    Log.d("userImageUrlactivity", "" + userImageUrl);
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


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage() == null || error instanceof TimeoutError || error instanceof NoConnectionError) {
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

                    try {

                        //show dialog

                        // Showing Alert Message

                        alertDialog.show();

                    } catch (WindowManager.BadTokenException e) {
                        Log.d("UIN", "WindowManager " + e);
                    }

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

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        SharedPreferences settings = getSharedPreferences(UIN.MYSHAREDPREFUIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("m");
        editor.remove("selectedstate");
        editor.remove("selectedservice");
        editor.remove("logged");
        editor.clear();
        editor.commit();
        //finish();

        //Added on 5th dec 2018

        SharedPreferences settings1 = getSharedPreferences(ClientFilterFragment.MySharedPreffilter, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = settings1.edit();
        editor1.remove("clientId");
        editor1.remove("stateId");
        editor1.clear();
        editor1.commit();


        /*Intent intent = new Intent(UIN.this, FilterActivity1.class);

        startActivity(intent);*/

        finish();


    }

    private void getClientsList(final String clientId) {

        String tag_string_req = "req_clients";

        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_ClientBasedOnClientId + clientId, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {

                Log.d("Particularclient", "-->" + response1);
                //parsing Json
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response1.toString());
                    String response = jsonObject.getString("response");
                    String status = jsonObject.getString("status");
                    Log.d("responsus", "" + response);
                    Log.d("statsus", "" + status);

                    if (status.equals("success")) {

                        JSONObject jsonObject1 = new JSONObject(response);

                        ClientData clientData = new ClientData();
                        clientData.setClientLogoPath(jsonObject1.getString("clientLogoPath"));
                        clientData.setClientImagePath(jsonObject1.getString("clientImagePath"));
                        clientData.setClientName(jsonObject1.getString("clientName"));

                        clientLogoPath = clientData.getClientLogoPath().toString();
                        clientImageURLPath = clientData.getClientImagePath().toString();
                        clientNAmeTextview.setText(clientData.getClientName().toString());
                        clientname = clientData.getClientName();
                        Glide
                                .with(UIN.this)
                                .load(clientLogoPath)
                                .error(R.drawable.sabpaisa)
                                .into(clientlogopath);
                        Glide
                                .with(UIN.this)
                                .load(clientImageURLPath)
                                .error(R.drawable.sabpaisa)
                                .into(clientImagePath);

                        // clientname=clientData.getClientName().toString();
                        Log.d("clientlogooooo", "-->" + clientLogoPath);
                        Log.d("clientimageooo", "-->" + clientImagePath);
                        //  Log.d("clientiooo", "-->" + clientname11);

                    } else if (status.equals("failure")) {
                        Log.d("UIn", "InElseIfPart" + response);
                    } else {
                        Log.d("UIn", "InElsePart" + response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage() == null || error instanceof TimeoutError || error instanceof NoConnectionError) {
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


        });

        AppController.getInstance().addToRequestQueue(request, tag_string_req);


    }


    private void checkUserForAdmin(String userAccessToken, String clientId) {

        String tag_string_req = "req_clients";

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_UserRole + "?token=" + userAccessToken + "&clientId=" + clientId;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {

                Log.d("checkUserForAdmin", "-->" + response1);
                //parsing Json
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response1);
                    String response = jsonObject.getString("response");
                    String status = jsonObject.getString("status");
                    Log.d("checkUserForAdminResp", "" + response);
                    Log.d("checkUserForAdminStatus", "" + status);

                    if (status.equals("success")) {


                        Log.d("UIN_CheckForAdmin", "InIfPart");

                        SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREF_FOR_CHECK_USER, MODE_PRIVATE).edit();
                        editor.putString("USER_ROLE", response);
                        editor.commit();

                    } else {
                        Log.d("UIN_CheckForAdmin", "InElsePart");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage() == null || error instanceof TimeoutError || error instanceof NoConnectionError) {
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


        });

        AppController.getInstance().addToRequestQueue(request, tag_string_req);

    }


}


