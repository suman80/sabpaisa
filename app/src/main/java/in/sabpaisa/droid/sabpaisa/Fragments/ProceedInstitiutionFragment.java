package in.sabpaisa.droid.sabpaisa.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.InstitutionAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.ProceedInstitutionFragmentOfflineAdapter;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments;
import in.sabpaisa.droid.sabpaisa.AppDB.AppDbImage;
import in.sabpaisa.droid.sabpaisa.MainActivity;
import in.sabpaisa.droid.sabpaisa.MainFeedAdapter;
import in.sabpaisa.droid.sabpaisa.Model.ClientData;
import in.sabpaisa.droid.sabpaisa.Model.FeedDataForOffLine;
import in.sabpaisa.droid.sabpaisa.Model.ParticularClientModelForOffline;
import in.sabpaisa.droid.sabpaisa.PayFragments;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Model.Institution;
import in.sabpaisa.droid.sabpaisa.R;

import static android.content.Context.MODE_PRIVATE;
import static in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments.TABLE_NAME_MEMBERS;
import static in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments.TABLE_Particular_Client;
import static in.sabpaisa.droid.sabpaisa.AppDB.AppDbImage.TABLE_ParticularClientImage;


public class ProceedInstitiutionFragment extends Fragment {

    View rootView;
    LinearLayout linearLayoutnoDataFound;
    public static String MYSHAREDPREFProceed = "mySharedPref11";
    RecyclerView recyclerViewInstitutions;
    InstitutionAdapter institutionAdapter;
    //ArrayList<Institution> institutions;
    ArrayList<Institution> clientArrayList;

    ShimmerRecyclerView shimmerRecyclerView;
    String landing_page;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String stateName, serviceName, clientId, userImageUrl;

    Bitmap bitmap;

    String logoPath, imagePath;
    public static String MYSHAREDPREF = "mySharedPref";
    /////////Local Db//////////
    AppDbComments db;
    AppDbImage imgDb;
    ArrayList<ParticularClientModelForOffline> clientArrayListForOffline;

    public ProceedInstitiutionFragment() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_institutions, container, false);
        // ((MainActivity) getActivity()).initToolBar(clientId);
        linearLayoutnoDataFound = (LinearLayout) rootView.findViewById(R.id.noDataFound);
        shimmerRecyclerView = (ShimmerRecyclerView) rootView.findViewById(R.id.recycler_view_institutions);
        recyclerViewInstitutions = (RecyclerView) rootView.findViewById(R.id.recycler_view_institutions);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        //        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),10));
        recyclerViewInstitutions.setLayoutManager(llm);

        ///////////////////////DB/////////////////////////////////
        db = new AppDbComments(getContext());
        imgDb = new AppDbImage(getContext());

        clientArrayList = new ArrayList<Institution>();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MainActivity.MYSHAREDPREF, MODE_PRIVATE);
        //stateName=sharedPreferences.getString("STATENAME","123");
        //serviceName=sharedPreferences.getString("SERVICENAME","123");
        clientId = sharedPreferences.getString("clientId", "abc");
        userImageUrl = sharedPreferences.getString("userImageUrl", "abc");
        Log.d("PIF_ClientId", "-->" + clientId);
        Log.d("userImageUrlFrag", "-->" + userImageUrl);
        recyclerViewInstitutions.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 2000);

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
                    ParticularClientModelForOffline particularClientModelForOffline = new ParticularClientModelForOffline();
                    particularClientModelForOffline.setClientId(res.getString(1));
                    particularClientModelForOffline.setClientName(res.getString(2));
                    particularClientModelForOffline.setState(res.getString(3));
                    clientArrayListForOffline.add(particularClientModelForOffline);

                }
                Log.d("getPIFOfflineData", "-->" + stringBuffer);

                ProceedInstitutionFragmentOfflineAdapter adapter = new ProceedInstitutionFragmentOfflineAdapter(getContext(), clientArrayListForOffline);
                recyclerViewInstitutions.setAdapter(adapter);


                Cursor resForImage = imgDb.getParticularClientImageData(clientId);

                Log.d("resForImage Size : ",resForImage.getCount()+"");
                if (resForImage.getCount() > 0) {
                    StringBuffer stringBuffer1 = new StringBuffer();
                    while (resForImage.moveToNext()) {

                        Log.d("INSIDE : "," for clientImages");
                        stringBuffer1.append(resForImage.getString(0) + " ");
                        stringBuffer1.append(resForImage.getString(1) + " ");
                        stringBuffer1.append(resForImage.getString(2) + " ");
                        stringBuffer1.append(resForImage.getString(3) + " ");

//                        loadLogoFromStorage(resForImage.getString(2));
//                        loadImageFromStorage(resForImage.getString(3));

                        SharedPreferences.Editor editorImg = getContext().getSharedPreferences(MYSHAREDPREF, MODE_PRIVATE).edit();
                        editorImg.putString("logo_path", resForImage.getString(2));
                        editorImg.putString("image_path", resForImage.getString(3));
                        editorImg.commit();

                    }
                    Log.d("getImgFrmDBPIF", "-->" + stringBuffer1);

                } else {

                    Log.d("In Else Part", "");
                    //Toast.makeText(ProfileNavigationActivity.this,"In Else Part",Toast.LENGTH_SHORT).show();

                }


            } else {
                Log.d("PIFLocalDb", "In Else Part");
                Toast.makeText(getContext(), "No Data Found !", Toast.LENGTH_SHORT).show();
            }
        }


        return rootView;
    }

    private void getClientsList(final String clientId) {


        boolean checkDb = db.isTableExists(TABLE_Particular_Client);

        Log.d("DbValuePIF"," "+checkDb);

        if (checkDb == true){
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

                        Institution institution = new Institution();

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
                        //////////////////////////////LOCAL DB//////////////////////////////////////

                        boolean isInserted = db.insertClientData(institution);
                        if (isInserted == true) {

                            //Toast.makeText(AllTransactionSummary.this, "Data  Inserted", Toast.LENGTH_SHORT).show();

                            Log.d("PIF_Data", "LocalDBInIfPart" + isInserted);

                        } else {
                            Log.d("PIF_Data", "LocalDBInElsePart" + isInserted);
                            //Toast.makeText(AllTransactionSummary.this, "Data  Not Inserted", Toast.LENGTH_SHORT).show();
                        }

                        /////////////////////Saving To Internal Storage/////////////////////////////////////////
                        ////Deleting path From Db//////
                        boolean checkDb = imgDb.isTableExists(TABLE_ParticularClientImage);

                        Log.d("DbValuePIF_Img"," "+checkDb);

                        if (checkDb == true){
                            imgDb.deleteAllImageData();
                        }



                        Glide.with(getContext())
                                .load(institution.getOrgLogo())
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>(100, 100) {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                        Log.d("LogoBitmap", " " + resource);
                                        saveLogoToInternalStorage(resource);
                                    }
                                });


                        Glide.with(getContext())
                                .load(institution.getOrgWal())
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>(100, 100) {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                        Log.d("ImgBitmap", " " + resource);
                                        saveImageToInternalStorage(resource);
                                    }
                                });

                        ///////////////////////Handler For images path For local db/////////////////////////////
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Do something after 5000ms
                                saveImagesIntoLocalDb(logoPath, imagePath);

                            }
                        }, 1000);



                        institutionAdapter = new InstitutionAdapter(getContext(), clientArrayList);
                        recyclerViewInstitutions.setAdapter(institutionAdapter);

                        Log.d("clientArrayList2222", " " + clientArrayList.get(0).getOrganization_name());
                        Log.d("clientArrayList2222", " " + clientArrayList.get(0).getOrganizationId());
                        Log.d("clientArrayList3333", " " + clientArrayList.get(0).getOrgDesc());

                        landing_page = clientArrayList.get(0).getOrgDesc();

                        Log.d("xyz", "" + landing_page);

                        SharedPreferences.Editor editor1 = getContext().getSharedPreferences(MYSHAREDPREFProceed, MODE_PRIVATE).edit();
                        editor1.putString("landing_page", landing_page);
                        //editor.putString("SERVICENAME",serviceName);
                        editor1.commit();


                        PayFragments ldf = new PayFragments();
                        Bundle args = new Bundle();
                        args.putString("landing_page", "landing_page");
                        ldf.setArguments(args);




                    } else {
                        linearLayoutnoDataFound.setVisibility(View.VISIBLE);
                        shimmerRecyclerView.setVisibility(View.GONE);
                    }

                 /*   PayFragments ldf = new PayFragments();
                    Bundle args = new Bundle();
                    args.putString("landing_page", "landing_page");
                    ldf.setArguments(args);
*///Inflate the fragment
                    //getFragmentManager().beginTransaction().add(R.id.container, ldf).commit();

                   /* MakePa ldf = new YourNewFragment ();
                    Bundle args = new Bundle();
                    args.putString("landing_page", "landing_page");
                    ldf.setArguments(args);
*/
//
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage() == null || error instanceof TimeoutError || error instanceof NoConnectionError) {
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

    private String saveLogoToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, (System.currentTimeMillis() / 1000) + "logo.jpg");

        Log.d("mypath", "mypath  " + mypath);

        logoPath = mypath.toString();


        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }


    private String saveImageToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, (System.currentTimeMillis() / 1000) + "image.jpg");

        Log.d("mypathImg", "mypathImg  " + mypath);

        imagePath = mypath.toString();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }


    private void saveImagesIntoLocalDb(String logoPath, String imagePath) {
        Log.d("logoPath_PIF", "IntoLocalDb " + logoPath+",   imagePath : " + imagePath);
        Log.d("ClientID_PIF", "IntoLocalDb " + clientId);

        boolean isInserted = imgDb.insertClientImageData(clientId, logoPath, imagePath);
        if (isInserted == true) {
            Log.d("PIF_Image", "LocalDBInIfPart" + isInserted);
        } else {
            Log.d("PIF_Image", "LocalDBInElsePart" + isInserted);
        }

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


}