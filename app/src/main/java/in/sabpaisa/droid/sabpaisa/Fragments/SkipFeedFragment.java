package in.sabpaisa.droid.sabpaisa.Fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import in.sabpaisa.droid.sabpaisa.Adapter.SkipFeedFragmentAdapter;
import in.sabpaisa.droid.sabpaisa.AddFeed;
import in.sabpaisa.droid.sabpaisa.AddSpaceFeed;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.FeedData;
import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.MainFeedAdapter;
import in.sabpaisa.droid.sabpaisa.Model.FeedDataForOffLine;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.SimpleDividerItemDecoration;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.SkipClientDetailsScreen;

import static in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments.TABLE_NAME;
import static in.sabpaisa.droid.sabpaisa.AppDB.NotificationDB.TABLE_FEEDNOTIFICATION;
import static in.sabpaisa.droid.sabpaisa.ConstantsForUIUpdates.FEED_ARRAYLIST;

public class SkipFeedFragment extends Fragment {


    //Values get from SkipClient details screen
    static String clientName,clientLogoPath,clientImagePath,state,appCid;

    LinearLayout linearLayoutnoDataFound;
    ShimmerRecyclerView recyclerView;

    MaterialRippleLayout rippleClickAdd;

    FrameLayout framelayoutAddFeed;

    LinearLayout linearLayoutAddFeedWhenNoData;


    ArrayList<FeedData> feedArrayList = new ArrayList<FeedData>();

    SkipFeedFragmentAdapter skipFeedFragmentAdapter;

    BroadcastReceiver broadcastReceiver,receiver;

    public SkipFeedFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clientName = getArguments().getString("clientName");
        clientLogoPath = getArguments().getString("clientLogoPath");
        clientImagePath = getArguments().getString("clientImagePath");
        state = getArguments().getString("state");

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SkipClientDetailsScreen.MySharedPrefOnSkipClientDetailsScreenForAppCid, Context.MODE_PRIVATE);
        appCid=sharedPreferences.getString("appCid","abc");
        Log.d("clientId_MEMBERS",""+appCid);

        Log.d("SkipFeedsFrag","Recieved_Val_"+clientName+" "+clientLogoPath+" "+clientImagePath+" "+state+" "+appCid);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_skip_feed, container, false);

        recyclerView = (ShimmerRecyclerView) view.findViewById(R.id.recycler_view_feeds);

        rippleClickAdd = (MaterialRippleLayout) view.findViewById(R.id.rippleClickAdd);

        framelayoutAddFeed = (FrameLayout) view.findViewById(R.id.framelayoutAddFeed);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        recyclerView.setLayoutManager(llm);
        recyclerView.setMotionEventSplittingEnabled(false);

        linearLayoutnoDataFound = (LinearLayout) view.findViewById(R.id.noDataFound);

        linearLayoutAddFeedWhenNoData = (LinearLayout) view.findViewById(R.id.linearLayoutAddFeedWhenNoData);

        framelayoutAddFeed.setVisibility(View.VISIBLE);

        rippleClickAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddSpaceFeed.class);
                intent.putExtra("appCid",appCid);
                intent.putExtra("clientName",clientName);
                intent.putExtra("clientLogoPath",clientLogoPath);
                intent.putExtra("clientImagePath",clientImagePath);
                intent.putExtra("state",state);
                getActivity().startActivity(intent);
            }
        });

        linearLayoutAddFeedWhenNoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddSpaceFeed.class);
                intent.putExtra("appCid",appCid);
                intent.putExtra("clientName",clientName);
                intent.putExtra("clientLogoPath",clientLogoPath);
                intent.putExtra("clientImagePath",clientImagePath);
                intent.putExtra("state",state);
                getActivity().startActivity(intent);
            }
        });

        callFeedDataList(appCid);


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(FEED_ARRAYLIST)){
                    callFeedDataList(appCid);
                }

            }
        };

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,new IntentFilter(FEED_ARRAYLIST));


    }


    @Override
    public void onDestroy() {

        //LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);

        super.onDestroy();
    }

    public void callFeedDataList(final String appCid) {


        String tag_string_req = "req_register";

        String urlJsonObj = AppConfig.Base_Url + AppConfig.App_api + "getParticularClientsFeeds/" + "?appcid=" + appCid;

        Log.d("SkipFeedFragment","URL___"+urlJsonObj);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                urlJsonObj, new Response.Listener<String>() {

            // Takes the response from the JSON request
            @Override
            public void onResponse(String response) {
                try {
                    //    swipeRefreshLayout.setRefreshing(false);
                    Log.d("SkipFeedFragment", "response__: " + response);
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
                            //feedData.setImagePath(jsonObject1.getString("imagePath"));
                            feedArrayList.add(feedData);


                        }


                        skipFeedFragmentAdapter = new SkipFeedFragmentAdapter(feedArrayList, getContext());
                        recyclerView.setAdapter(skipFeedFragmentAdapter);

                    } else {
                        //
                        linearLayoutAddFeedWhenNoData.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        framelayoutAddFeed.setVisibility(View.GONE);
                        linearLayoutnoDataFound.setVisibility(View.GONE);
                        /*if (roleValue.equals("1")){
                            linearLayoutAddFeedWhenNoData.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            framelayoutAddFeed.setVisibility(View.GONE);
                            linearLayoutnoDataFound.setVisibility(View.GONE);
                        }else {
                            linearLayoutnoDataFound.setVisibility(View.VISIBLE);
                            framelayoutAddFeed.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            linearLayoutAddFeedWhenNoData.setVisibility(View.GONE);
                        }*/

                    }

                }
                // Try and catch are included to handle any errors due to JSON
                catch (JSONException e) {
                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();
                    callFeedDataList(appCid);
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
                        callFeedDataList(appCid);
                        Log.e("Feed", "FeedError");
                    }
                }
        );
        // Adds the JSON array request "arrayreq" to the request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);
    }




}
