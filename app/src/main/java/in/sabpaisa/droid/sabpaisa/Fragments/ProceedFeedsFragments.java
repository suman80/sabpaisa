package in.sabpaisa.droid.sabpaisa.Fragments;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.AllTransactionAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.ProceedFeedsFragmentsOfflineAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.ProceedInstitutionFragmentOfflineAdapter;
import in.sabpaisa.droid.sabpaisa.AllTransactionSummary;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.AppDB.AppDB;
import in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments;
import in.sabpaisa.droid.sabpaisa.AppDB.AppDbImage;
import in.sabpaisa.droid.sabpaisa.FeedData;
import in.sabpaisa.droid.sabpaisa.FeedDetails;
import in.sabpaisa.droid.sabpaisa.FeedsFragments;
import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.MainFeedAdapter;
import in.sabpaisa.droid.sabpaisa.Model.AllTransactiongettersetter;
import in.sabpaisa.droid.sabpaisa.Model.FeedDataForOffLine;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.RecyclerItemClickListener;
import in.sabpaisa.droid.sabpaisa.SimpleDividerItemDecoration;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;
import in.sabpaisa.droid.sabpaisa.Util.SkipClientDetailsScreen;

import static android.content.Context.MODE_PRIVATE;
import static in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments.TABLE_NAME;
import static in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments.TABLE_NAME_MEMBERS;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProceedFeedsFragments extends Fragment {
    private static final String TAG = ProceedFeedsFragments.class.getSimpleName();
    View rootView;
    LinearLayout linearLayoutnoDataFound;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    String tag_string_req = "req_register";
    ArrayList<FeedData> feedArrayList = new ArrayList<FeedData>();

    ArrayList<FeedDataForOffLine> feedArrayListForLocalDb;

    MainFeedAdapter mainFeedAdapter;/*Globally Declared Adapter*/

    public static String clientId;

    /*START Interface for getting data from activity*/
    GetDataInterface sGetDataInterface;

    /////////Local Db//////////
    AppDbComments db;


    public ProceedFeedsFragments() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_proceed_feeds_fragments, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_feeds);
        linearLayoutnoDataFound = (LinearLayout) rootView.findViewById(R.id.noDataFound);

        ///////////////////////DB/////////////////////////////////
        db = new AppDbComments(getContext());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FullViewOfClientsProceed.MySharedPrefOnFullViewOfClientProceed, Context.MODE_PRIVATE);
        clientId = sharedPreferences.getString("clientId", "abc");
        Log.d("clientId_PFF", "" + clientId);
        /*swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);*/


        if (isOnline()) {
            callFeedDataList(clientId);
        } else {

            /////////////////////Retrive data from local DB///////////////////////////////

            Cursor res = db.getParticularFeedData(clientId);
            feedArrayListForLocalDb = new ArrayList<>();
            if (res.getCount() > 0) {
                StringBuffer stringBuffer = new StringBuffer();
                while (res.moveToNext()) {
                    stringBuffer.append(res.getString(0) + " ");
                    stringBuffer.append(res.getString(1) + " ");
                    stringBuffer.append(res.getString(2) + " ");
                    stringBuffer.append(res.getString(3) + " ");
                    stringBuffer.append(res.getString(4) + " ");
                    stringBuffer.append(res.getString(5) + " ");
                    stringBuffer.append(res.getString(6) + " ");

                    FeedDataForOffLine feedData = new FeedDataForOffLine();
                    feedData.setClientId(res.getString(1));
                    feedData.setFeedId(res.getString(2));
                    feedData.setFeedName(res.getString(3));
                    feedData.setFeedText(res.getString(4));
                    feedData.setImagePath(res.getString(5));
                    feedData.setLogoPath(res.getString(6));
                    feedArrayListForLocalDb.add(feedData);

                }
                Log.d("getFeedData", "-->" + stringBuffer);

                ProceedFeedsFragmentsOfflineAdapter adapter = new ProceedFeedsFragmentsOfflineAdapter(getContext(),feedArrayListForLocalDb);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();



            } else {
                Log.d("getFeedDataLocalDb", "In Else Part");
                Toast.makeText(getContext(), "No Data Found !", Toast.LENGTH_SHORT).show();
            }


        }

        Log.d("sGetDataInterface", "" + sGetDataInterface);

        return rootView;
    }


    public void callFeedDataList(final String clientId) {


        boolean checkDb = db.isTableExists(TABLE_NAME);

        Log.d("DbValuePFF"," "+checkDb);

        if (checkDb == true){
            db.deleteAllFeedData();
        }

        String urlJsonObj = AppConfig.Base_Url + AppConfig.App_api + "getParticularClientsFeeds/" + "?client_Id=" + clientId;


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                urlJsonObj, new Response.Listener<String>() {

            // Takes the response from the JSON request
            @Override
            public void onResponse(String response) {
                try {
                    //    swipeRefreshLayout.setRefreshing(false);
                    Log.d(TAG, "profeed1: " + response);
                    //swipeRefreshLayout.setRefreshing(false);
                    feedArrayList = new ArrayList<FeedData>();

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    String response1 = jsonObject.getString("response");

                    JSONArray jsonArray = null;
                    Object obj = jsonObject.get("response");
                    if (obj instanceof JSONArray) {
                        jsonArray = (JSONArray) obj;

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            final FeedData feedData = new FeedData();
                            feedData.setClientId(jsonObject1.getString("clientId"));
                            feedData.setFeedId(jsonObject1.getString("feedId"));
                            feedData.setFeedName(jsonObject1.getString("feedName"));
                            feedData.setFeedText(jsonObject1.getString("feedText"));
                            feedData.setCreatedDate(jsonObject1.getString("createdDate"));
                            feedData.setLogoPath(jsonObject1.getString("logoPath"));
                            feedData.setImagePath(jsonObject1.getString("imagePath"));
                            feedArrayList.add(feedData);


                            /////////////////////Saving To Internal Storage/////////////////////////////////////////

                            final FeedDataForOffLine feedDataForOffLine = new FeedDataForOffLine();
                            feedDataForOffLine.setClientId(jsonObject1.getString("clientId"));
                            feedDataForOffLine.setFeedId(jsonObject1.getString("feedId"));
                            feedDataForOffLine.setFeedName(jsonObject1.getString("feedName"));
                            feedDataForOffLine.setFeedText(jsonObject1.getString("feedText"));

                            Glide.with(getContext())
                                    .load(feedData.getLogoPath())
                                    .asBitmap()
                                    .into(new SimpleTarget<Bitmap>(100, 100) {
                                        @Override
                                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                            Log.d("LogoBitmap", " " + resource);
                                            //saveLogoToInternalStorage(resource , feedData.getFeedId());


                                            ContextWrapper cw = new ContextWrapper(getContext());
                                            // path to /data/data/yourapp/app_data/imageDir
                                            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                                            // Create imageDir
                                            File mypath = new File(directory, feedData.getFeedId() + "feedLogo.jpg");

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

                                            feedDataForOffLine.setLogoPath(logoPath);

                                        }
                                    });


                            Glide.with(getContext())
                                    .load(feedData.getImagePath())
                                    .asBitmap()
                                    .into(new SimpleTarget<Bitmap>(100, 100) {
                                        @Override
                                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                            Log.d("ImgBitmap", " " + resource);
                                            //saveImageToInternalStorage(resource , feedData.getFeedId());


                                            ContextWrapper cw = new ContextWrapper(getContext());
                                            // path to /data/data/yourapp/app_data/imageDir
                                            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                                            // Create imageDir
                                            File mypath = new File(directory, feedData.getFeedId() + "feedImage.jpg");

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

                                            feedDataForOffLine.setImagePath(imagePath);
                                        }
                                    });


                            //////////////////////////////LOCAL DB//////////////////////////////////////
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Do something after 3000ms

                                    Log.d("logoPath_PFF", "IntoLocalDb " + feedDataForOffLine.getLogoPath());
                                    Log.d("imagePath_PFF", "IntoLocalDb " + feedDataForOffLine.getImagePath());


                                    boolean isInserted = db.insertFeedData(feedDataForOffLine);
                                    if (isInserted == true) {

                                        //Toast.makeText(AllTransactionSummary.this, "Data  Inserted", Toast.LENGTH_SHORT).show();

                                        Log.d("PFF", "LocalDBInIfPart" + isInserted);

                                    } else {
                                        Log.d("PFF", "LocalDBInElsePart" + isInserted);
                                        //Toast.makeText(AllTransactionSummary.this, "Data  Not Inserted", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            }, 2000);



                        }
                        Log.d("feedArrayListAfterParse", " " + feedArrayList.get(0).getFeedName());
                        //*START listener for sending data to activity*//*
                        OnFragmentInteractionListener listener = (OnFragmentInteractionListener) getActivity();
                        listener.onFragmentSetFeeds(feedArrayList);


                        //*END listener for sending data to activity*//*
                        loadFeedListView(feedArrayList, recyclerView);

                    } else {
                        linearLayoutnoDataFound.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }

                }
                // Try and catch are included to handle any errors due to JSON
                catch (JSONException e) {
                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();
                    callFeedDataList(clientId);
                }
            }
        },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        callFeedDataList(clientId);
                        Log.e("Feed", "FeedError");
                    }
                }
        );
        // Adds the JSON array request "arrayreq" to the request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);
    }

    private void loadFeedListView(ArrayList<FeedData> arrayList, final RecyclerView recyclerView) {

        mainFeedAdapter = new MainFeedAdapter(arrayList, getContext());
        // recyclerView.setAdapter(mainFeedAdapter);
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(mainFeedAdapter);

            }
        }, 1000);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent((getContext()), FeedDetails.class);
                       /* intent.putExtra("FeedId", feedArrayList.get(position).getFeedId());
                        intent.putExtra("FeedName", feedArrayList.get(position).getFeedName());
                        intent.putExtra("FeedDeatils", feedArrayList.get(position).getFeedText());
                        startActivity(intent);*/
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        recyclerView.setLayoutManager(llm);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            sGetDataInterface = (GetDataInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "must implement GetDataInterface Interface");
        }
    }

    public void getDataFromActivity() {
        if (sGetDataInterface != null) {
            this.feedArrayList = sGetDataInterface.getFeedDataList();
            mainFeedAdapter.setItems(this.feedArrayList);
            mainFeedAdapter.notifyDataSetChanged();
        }

        Log.d("PFF_I&A", " " + sGetDataInterface + "&" + feedArrayList);
    }


    /*END Interface for getting data from activity*/




   /* @Override
    public void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(this);
    }*/


    public interface GetDataInterface {
        ArrayList<FeedData> getFeedDataList();
    }
    /*END onRefresh() for SwipeRefreshLayout*/


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v("PFF", "Internet Connection Not Present");
            return false;
        }
    }


}