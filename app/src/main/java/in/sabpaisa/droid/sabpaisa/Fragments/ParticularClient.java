package in.sabpaisa.droid.sabpaisa.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.InstitutionAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.ProceedInstitutionFragmentOfflineAdapter;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments;
import in.sabpaisa.droid.sabpaisa.HttpsTrustManager;
import in.sabpaisa.droid.sabpaisa.MainActivity;
import in.sabpaisa.droid.sabpaisa.Model.Institution;
import in.sabpaisa.droid.sabpaisa.Model.ParticularClientModelForOffline;
import in.sabpaisa.droid.sabpaisa.PayFragments;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;

import static android.content.Context.MODE_PRIVATE;
import static in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments.TABLE_Particular_Client;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParticularClient extends Fragment {


    View rootView;
    LinearLayout linearLayoutnoDataFound;
    public static String MYSHAREDPREFProceed = "mySharedPref11";
    InstitutionAdapter institutionAdapter;
    ArrayList<Institution> clientArrayList;

    ShimmerRecyclerView shimmerRecyclerView;
    String landing_page;
    String clientId, userImageUrl;

    public static String MYSHAREDPREF = "mySharedPref";
    /////////Local Db//////////
    AppDbComments db;

    ArrayList<ParticularClientModelForOffline> clientArrayListForOffline;


    public ParticularClient() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_particular_client, container, false);

        linearLayoutnoDataFound = (LinearLayout) rootView.findViewById(R.id.noDataFound);
        shimmerRecyclerView = (ShimmerRecyclerView) rootView.findViewById(R.id.recycler_view_institutions);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        shimmerRecyclerView.setLayoutManager(llm);

        ///////////////////////DB/////////////////////////////////
        db = new AppDbComments(getContext());

        clientArrayList = new ArrayList<Institution>();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MainActivity.MYSHAREDPREF, MODE_PRIVATE);

        clientId = sharedPreferences.getString("clientId", "abc");
        userImageUrl = sharedPreferences.getString("userImageUrl", "abc");
        Log.d("PIF_ClientId", "-->" + clientId);
        Log.d("userImageUrlFrag", "-->" + userImageUrl);
        /*recyclerViewInstitutions.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 2000);
*/
        if (isOnline()) {
            //db.deleteAllClientData();
            getClientsList(clientId.toString());

        } else {
            /////////////////////Retrive data from local DB///////////////////////////////

            Cursor res = db.getParticularClientData(clientId);
            clientArrayListForOffline = new ArrayList<>();
            if (res.getCount() > 0) {
                StringBuffer stringBuffer = new StringBuffer();
                while (res.moveToNext()) {
                    stringBuffer.append(res.getString(0) + " ");
                    stringBuffer.append(res.getString(1) + " ");
                    stringBuffer.append(res.getString(2) + " ");
                    stringBuffer.append(res.getString(3) + " ");
                    stringBuffer.append(res.getString(4) + " ");
                    stringBuffer.append(res.getString(5) + " ");
                    ParticularClientModelForOffline particularClientModelForOffline = new ParticularClientModelForOffline();
                    particularClientModelForOffline.setClientId(res.getString(1));
                    particularClientModelForOffline.setClientName(res.getString(2));
                    particularClientModelForOffline.setState(res.getString(3));
                    particularClientModelForOffline.setClientLogoPath(res.getString(4));
                    particularClientModelForOffline.setClientImagePath(res.getString(5));
                    clientArrayListForOffline.add(particularClientModelForOffline);

                }
                Log.d("getPIFOfflineData", "-->" + stringBuffer);

                ProceedInstitutionFragmentOfflineAdapter adapter = new ProceedInstitutionFragmentOfflineAdapter(getContext(), clientArrayListForOffline);
                shimmerRecyclerView.setAdapter(adapter);


            } else {
                Log.d("PIFLocalDb", "In Else Part");
                Toast.makeText(getContext(), "No Data Found !", Toast.LENGTH_SHORT).show();
            }
        }


        return rootView;
    }

    private void getClientsList(final String clientId) {

//Added for SSL (17th Sep 2018)
        HttpsTrustManager.allowAllSSL();

        boolean checkDb = db.isTableExists(TABLE_Particular_Client);

        Log.d("DbValuePC", " " + checkDb);

        if (checkDb == true) {
            db.deleteAllClientData();
        }

        String tag_string_req = "req_clients";

        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_ClientBasedOnClientId + clientId, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {

                Log.d("Particularclient", "-->" + response1);
                //parsing Json
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response1.toString());
                    String status = jsonObject.getString("status");

                    if (status.equals("success")) {

                        final String response = jsonObject.getString("response");

                        //Adding data to new jsonobject////////////////////////

                        JSONObject jsonObject1 = new JSONObject(response);

                        final Institution institution = new Institution();

                        institution.setOrganizationId(jsonObject1.getString("clientId"));
                        Log.d("ClientIdjijiji", "-->" + institution.getOrganizationId());
                        institution.setOrganization_name(jsonObject1.getString("clientName"));
                        Log.d("Clientnamejijiji", "-->" + institution.getOrganization_name());

                        institution.setOrgLogo(jsonObject1.getString("clientLogoPath"));
                        Log.d("OrgLogo", "-->" + institution.getOrgLogo());


                        institution.setOrgWal(jsonObject1.getString("clientImagePath"));

                        Log.d("OrgWal", "-->" + institution.getOrgWal());
                        //Added on 1st Feb
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("lookupState");
                        institution.setOrgAddress(jsonObject2.getString("stateName"));
                        //Added on 1st Feb
                        institution.setOrgDesc(jsonObject1.getString("landingPage"));

                        Log.d("JSONobjectResp", "-->" + response);

                        Log.d("JSONobjectttt", "-->" + jsonObject);

                        clientArrayList.add(institution);
                        /////////////////////Saving To Internal Storage/////////////////////////////////////////

                        final ParticularClientModelForOffline particularClientModelForOffline = new ParticularClientModelForOffline();
                        particularClientModelForOffline.setClientId(jsonObject1.getString("clientId"));
                        particularClientModelForOffline.setClientName(jsonObject1.getString("clientName"));
                        particularClientModelForOffline.setState(jsonObject2.getString("stateName"));

                        Glide.with(getContext())
                                .load(institution.getOrgLogo())
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>(100, 100) {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                        Log.d("LogoBitmap", " " + resource);

                                        ContextWrapper cw = new ContextWrapper(getContext());
                                        // path to /data/data/yourapp/app_data/imageDir
                                        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                                        // Create imageDir
                                        File mypath = new File(directory, (System.currentTimeMillis() / 1000) + "logo.jpg");

                                        Log.d("mypath", "mypath  " + mypath);

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
                                        particularClientModelForOffline.setClientLogoPath(logoPath);

                                    }
                                });


                        Glide.with(getContext())
                                .load(institution.getOrgWal())
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>(100, 100) {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                        Log.d("ImgBitmap", " " + resource);


                                        ContextWrapper cw = new ContextWrapper(getContext());
                                        // path to /data/data/yourapp/app_data/imageDir
                                        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                                        // Create imageDir
                                        File mypath = new File(directory, (System.currentTimeMillis() / 1000) + "image.jpg");

                                        Log.d("mypathImg", "mypathImg  " + mypath);

                                        String imagePath = mypath.toString();

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
                                        particularClientModelForOffline.setClientImagePath(imagePath);

                                    }
                                });

                        //////////////////////////////LOCAL DB//////////////////////////////////////
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Do something after 1000ms

                                boolean isInserted = db.insertClientData(particularClientModelForOffline);
                                if (isInserted == true) {

                                    //Toast.makeText(AllTransactionSummary.this, "Data  Inserted", Toast.LENGTH_SHORT).show();

                                    Log.d("PIF_Data", "LocalDBInIfPart" + isInserted);

                                } else {
                                    Log.d("PIF_Data", "LocalDBInElsePart" + isInserted);
                                    //Toast.makeText(AllTransactionSummary.this, "Data  Not Inserted", Toast.LENGTH_SHORT).show();
                                }


                            }
                        }, 1000);

                        //new saveToInternalStorage().doInBackground(jsonObject1);

                        institutionAdapter = new InstitutionAdapter(getContext(), clientArrayList);
                        shimmerRecyclerView.setAdapter(institutionAdapter);

                        Log.d("clientArrayList2222", " " + clientArrayList.get(0).getOrganization_name());
                        Log.d("clientArrayList2222", " " + clientArrayList.get(0).getOrganizationId());
                        Log.d("clientArrayList3333", " " + clientArrayList.get(0).getOrgDesc());

                        landing_page = clientArrayList.get(0).getOrgDesc();

                        Log.d("xyz", "" + landing_page);

//                        SharedPreferences.Editor editor1 = getContext().getSharedPreferences(MYSHAREDPREFProceed, MODE_PRIVATE).edit();
//                        editor1.putString("landing_page", landing_page);
//                        //editor.putString("SERVICENAME",serviceName);
//                        editor1.commit();


                    } else {
                        linearLayoutnoDataFound.setVisibility(View.VISIBLE);
                        shimmerRecyclerView.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage() == null || error instanceof TimeoutError || error instanceof NoConnectionError) {

                    if (getActivity()!=null) {

                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme).create();

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

                    }


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


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v("AllTransactionSummary", "Internet Connection Not Present");
            return false;
        }
    }



    /*public class saveToInternalStorage extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... jsonObjects) {

            final ParticularClientModelForOffline particularClientModelForOffline = new ParticularClientModelForOffline();

            for (JSONObject jsonArray: jsonObjects) {
                try {
                    particularClientModelForOffline.setClientId(jsonArray.getString("clientId"));
                    particularClientModelForOffline.setClientName(jsonArray.getString("clientName"));
                    particularClientModelForOffline.setState(jsonArray.getString("stateName"));
                    particularClientModelForOffline.setClientLogoPath(jsonArray.getString("clientLogoPath"));
                    Log.d("OrgLogo11", "-->" + particularClientModelForOffline.getClientLogoPath());
                    particularClientModelForOffline.setClientImagePath(jsonArray.getString("clientImagePath"));
                    Log.d("OrgWal11", "-->" + particularClientModelForOffline.getClientImagePath());

                    Glide.with(getContext())
                            .load(particularClientModelForOffline.getClientLogoPath())
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>(100, 100) {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                    Log.d("LogoBitmap", " " + resource);

                                    ContextWrapper cw = new ContextWrapper(getContext());
                                    // path to /data/data/yourapp/app_data/imageDir
                                    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                                    // Create imageDir
                                    File mypath = new File(directory, (System.currentTimeMillis() / 1000) + "logo.jpg");

                                    Log.d("mypath", "mypath  " + mypath);

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
//                                    particularClientModelForOffline.setClientLogoPath(logoPath);

                                }
                            });


                    Glide.with(getContext())
                            .load(particularClientModelForOffline.getClientImagePath())
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>(100, 100) {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                    Log.d("ImgBitmap", " " + resource);


                                    ContextWrapper cw = new ContextWrapper(getContext());
                                    // path to /data/data/yourapp/app_data/imageDir
                                    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                                    // Create imageDir
                                    File mypath = new File(directory, (System.currentTimeMillis() / 1000) + "image.jpg");

                                    Log.d("mypathImg", "mypathImg  " + mypath);

                                    String imagePath = mypath.toString();

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
                                    //particularClientModelForOffline.setClientImagePath(imagePath);

                                }
                            });

                    //////////////////////////////LOCAL DB//////////////////////////////////////
                    *//*final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 1000ms*//*

                            boolean isInserted = db.insertClientData(particularClientModelForOffline);
                            if (isInserted == true) {

                                //Toast.makeText(AllTransactionSummary.this, "Data  Inserted", Toast.LENGTH_SHORT).show();

                                Log.d("PIF_Data", "LocalDBInIfPart" + isInserted);

                            } else {
                                Log.d("PIF_Data", "LocalDBInElsePart" + isInserted);
                                //Toast.makeText(AllTransactionSummary.this, "Data  Not Inserted", Toast.LENGTH_SHORT).show();
                            }

*//*
                        }
                    }, 1000);*//*



                }catch (Exception e){
                    Log.d("saveToInternalStorage"," "+e.getMessage());
                }

            }



            return null;
        }
    }
*/
}
