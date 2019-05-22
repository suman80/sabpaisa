package in.sabpaisa.droid.sabpaisa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import in.sabpaisa.droid.sabpaisa.Adapter.OtherSpaceAdapter;

import in.sabpaisa.droid.sabpaisa.Adapter.OtherSpaceAdapterForShare;
import in.sabpaisa.droid.sabpaisa.Adapter.SkipMainClientsAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.SkipMainClientsAdapterForShare;
import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.Model.PersonalSpaceModel;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;

import static in.sabpaisa.droid.sabpaisa.AppDB.NotificationDB.TABLE_FEEDNOTIFICATION;
import static in.sabpaisa.droid.sabpaisa.AppDB.NotificationDB.TABLE_GROUPNOTIFICATION;

public class ShareFromOtherApp extends AppCompatActivity {

    ImageView imageView;

    ////////////////////////////////////////////////

    LinearLayout linearLayoutnoDataFound;
    RecyclerView recyclerViewInstitutions;
    RecyclerView recyclerViewOtherSpace;


    //InstitutionAdapter institutionAdapter;
    ArrayList<PersonalSpaceModel> institutions;
    ArrayList<PersonalSpaceModel> institutions1;

    LinearLayout linearLayoutAddFeedWhenNoData;

    String userAccessToken;


    OtherSpaceAdapterForShare OtherSpaceAdapter;
    SkipMainClientsAdapterForShare skipMainClientsAdapter;

    RelativeLayout relativeLayoutOther;

    String appCid;

    public Bitmap bitmap;
    public static String recievedText;
    public static Uri recievedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_from_other_app);

        //imageView = (ImageView)findViewById(R.id.imageView);


        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }

//////////////////////////////////////////////////////////////////////////////////////////////////////////

        linearLayoutnoDataFound = (LinearLayout) findViewById(R.id.noDataFound);
        linearLayoutAddFeedWhenNoData = (LinearLayout) findViewById(R.id.linearLayoutAddFeedWhenNoData);

        recyclerViewInstitutions = (RecyclerView) findViewById(R.id.recycler_view_institutions);
        recyclerViewOtherSpace = (RecyclerView) findViewById(R.id.recyclerViewOtherSpace);

        LinearLayoutManager llm = new LinearLayoutManager(ShareFromOtherApp.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewInstitutions.addItemDecoration(new DividerItemDecoration(ShareFromOtherApp.this,DividerItemDecoration.VERTICAL));
        recyclerViewInstitutions.setLayoutManager(llm);

        recyclerViewInstitutions.getRecycledViewPool().setMaxRecycledViews(0, 0);

        //////////////////other Space////////////////////////////////////////////////////////

        LinearLayoutManager llm1 = new LinearLayoutManager(ShareFromOtherApp.this);
        llm1.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),10));
        recyclerViewOtherSpace.setLayoutManager(llm1);

        recyclerViewOtherSpace.getRecycledViewPool().setMaxRecycledViews(0, 0);

        recyclerViewOtherSpace.setNestedScrollingEnabled(false);

        SharedPreferences sharedPreferences = getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        userAccessToken = sharedPreferences.getString("response", "123");


        if (isOnline()) {

            getClientsList();
            getOtherSpce();


        }else {
            Toast.makeText(ShareFromOtherApp.this,"No Internet Connection",Toast.LENGTH_SHORT).show();

        }




    }



    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            Log.d("sharedText","sharedText____"+sharedText);

            recievedText = sharedText;

        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            // Update UI to reflect image being shared
            Log.d("imageUri","imageUri_____"+imageUri);

            recievedImageUri = imageUri;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                //imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
        }
    }



    private void getClientsList() {


        String tag_string_req = "req_register";

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_spAppClient + "?admin=" + userAccessToken;

        Log.d("ISF", "url___" + url);

        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    institutions = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d("ISF__", "response___" + response);

                    String status = jsonObject.getString("status");

                    String response1 = jsonObject.getString("response");

                    Log.d("ISF__", "status__" + status);
                    Log.d("ISF__", "response1__" + response1);

                    if (status.equals("success") && response1.equals("No Record Found")) {

                        //Toast.makeText(getContext(),"No Result Found",Toast.LENGTH_SHORT).show();

                        //linearLayoutnoDataFound.setVisibility(View.VISIBLE);
                        recyclerViewInstitutions.setVisibility(View.GONE);
                        linearLayoutAddFeedWhenNoData.setVisibility(View.VISIBLE);
                    } else {
                        JSONArray jsonArray = jsonObject.getJSONArray("response");

                        JsonParser parser = new JsonParser();

                        Gson gson = new Gson();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            //PersonalSpaceModel personalSpaceModel = new PersonalSpaceModel();


                            JsonElement mJson =  parser.parse(jsonObject1.toString());

                            PersonalSpaceModel object = gson.fromJson(mJson, PersonalSpaceModel.class);

                            institutions.add(object);

                        }

                        /* *//*START listener for sending data to activity*//*
                        OnFragmentInteractionListener listener = (OnFragmentInteractionListener) getActivity();
                        listener.onFragmentSetClients(institutions);
                        *//*END listener for sending data to activity*/
                        //loadGroupListView(groupArrayList, (RecyclerView) rootView.findViewById(R.id.recycler_view_group));
                        skipMainClientsAdapter = new SkipMainClientsAdapterForShare(institutions,ShareFromOtherApp.this);
                        recyclerViewInstitutions.setAdapter(skipMainClientsAdapter);




                    }
                }
                // Try and catch are included to handle any errors due to JSON
                catch (JSONException e) {
                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();

                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }



    private void getOtherSpce() {


        String tag_string_req = "req_register";

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_spAppClients + "?token=" + userAccessToken;

        Log.d("ISF_Other", "url___" + url);

        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    institutions1 = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d("ISF_Other", "response___" + response);

                    String status = jsonObject.getString("status");

                    String response1 = jsonObject.getString("response");

                    Log.d("ISF_Other", "status__" + status);
                    Log.d("ISF_Other", "response1__" + response1);

                    if (status.equals("failure") && response1.equals("No Record Found")) {

                        //Toast.makeText(getContext(),"No Result Found",Toast.LENGTH_SHORT).show();

                        recyclerViewOtherSpace.setVisibility(View.GONE);


                    } else {


                        JSONArray jsonArray = jsonObject.getJSONArray("response");

                        JsonParser parser = new JsonParser();

                        Gson gson = new Gson();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            //PersonalSpaceModel personalSpaceModel = new PersonalSpaceModel();


                            JsonElement mJson =  parser.parse(jsonObject1.toString());

                            PersonalSpaceModel object = gson.fromJson(mJson, PersonalSpaceModel.class);

                            appCid = object.getAppCid();

                            institutions1.add(object);


                        }



                        OtherSpaceAdapter = new OtherSpaceAdapterForShare(institutions1,ShareFromOtherApp.this);
                        recyclerViewOtherSpace.setAdapter(OtherSpaceAdapter);
                    }
                }
                // Try and catch are included to handle any errors due to JSON
                catch (JSONException e) {
                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();

                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v("MainActivitySkip", "Internet Connection Not Present");
            return false;
        }
    }




}
