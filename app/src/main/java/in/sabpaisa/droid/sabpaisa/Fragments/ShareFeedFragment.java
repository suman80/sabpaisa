package in.sabpaisa.droid.sabpaisa.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.SharedFeedFragmentAdapter;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.FeedData;
import in.sabpaisa.droid.sabpaisa.Model.FeedDataForOffLine;
import in.sabpaisa.droid.sabpaisa.Proceed_Feed_FullScreen;
import in.sabpaisa.droid.sabpaisa.Proceed_Group_FullScreen;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.SimpleDividerItemDecoration;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShareFeedFragment extends Fragment {

    View rootView = null;
    private static final String TAG = ShareFeedFragment.class.getSimpleName();

    LinearLayout linearLayoutnoDataFound;
    ShimmerRecyclerView recyclerView;


    ArrayList<FeedData> feedArrayList = new ArrayList<FeedData>();

    ArrayList<FeedDataForOffLine> feedArrayListForLocalDb;

    SharedFeedFragmentAdapter sharedFeedFragmentAdapter;/*Globally Declared Adapter*/

    public static String clientId;



    public ShareFeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_share_feed, container, false);

        recyclerView = (ShimmerRecyclerView) rootView.findViewById(R.id.recycler_view_feeds);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        recyclerView.setLayoutManager(llm);


        linearLayoutnoDataFound = (LinearLayout) rootView.findViewById(R.id.noDataFound);

        if (Proceed_Feed_FullScreen.notificationFlag || Proceed_Group_FullScreen.notificationFlag) {

            if (Proceed_Feed_FullScreen.clientId != null) {
                clientId = Proceed_Feed_FullScreen.clientId;
                Log.d("clientId_PFF_FrmNOti", "" + clientId);
            }

            if (Proceed_Group_FullScreen.clientId != null){
                clientId = Proceed_Group_FullScreen.clientId;
                Log.d("clientId_PFF_FrmNOti", "" + clientId);
            }

        }else {

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FullViewOfClientsProceed.MySharedPrefOnFullViewOfClientProceed, Context.MODE_PRIVATE);
            clientId = sharedPreferences.getString("clientId", "abc");
            Log.d("clientId_PFF", "" + clientId);

        }

        callFeedDataList(clientId);

        return rootView;
    }



    public void callFeedDataList(final String clientId) {

        String tag_string_req = "req_register";

        String urlJsonObj = AppConfig.Base_Url + AppConfig.App_api + "getParticularClientsFeeds/" + "?client_Id=" + clientId;


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                urlJsonObj, new Response.Listener<String>() {

            // Takes the response from the JSON request
            @Override
            public void onResponse(String response) {
                try {
                    //    swipeRefreshLayout.setRefreshing(false);
                    Log.d(TAG, "RESP" + response);
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

                        }
                        Log.d("feedArrayListAfterParse", " " + feedArrayList.get(0).getFeedName());

                        sharedFeedFragmentAdapter = new SharedFeedFragmentAdapter(feedArrayList, getContext());
                        recyclerView.setAdapter(sharedFeedFragmentAdapter);

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


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v("SFF", "Internet Connection Not Present");
            return false;
        }
    }



}
