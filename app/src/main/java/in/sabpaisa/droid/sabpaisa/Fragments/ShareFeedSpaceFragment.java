package in.sabpaisa.droid.sabpaisa.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.SharedFeedFragmentAdapter;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.FeedData;
import in.sabpaisa.droid.sabpaisa.FeedSpaceCommentsActivity;
import in.sabpaisa.droid.sabpaisa.GroupSpaceCommentActivity;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.SimpleDividerItemDecoration;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.SkipClientDetailsScreen;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShareFeedSpaceFragment extends Fragment {

    static String clientName,clientLogoPath,clientImagePath,state,appCid;

    LinearLayout linearLayoutnoDataFound;
    ShimmerRecyclerView recyclerView;

    MaterialRippleLayout rippleClickAdd;

    FrameLayout framelayoutAddFeed;

    LinearLayout linearLayoutAddFeedWhenNoData;


    ArrayList<FeedData> feedArrayList = new ArrayList<FeedData>();

    SharedFeedFragmentAdapter sharedFeedFragmentAdapter;


    public ShareFeedSpaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_feed_space, container, false);



//        Log.d("FROM NOTIFIVATION ", FeedSpaceCommentsActivity.appCid);
//        Log.d("FROM_NOTIFIVATION ", "notificationFlag__"+FeedSpaceCommentsActivity.notificationFlag);

        if (GroupSpaceCommentActivity.notificationFlag || FeedSpaceCommentsActivity.notificationFlag){
            if(GroupSpaceCommentActivity.appCid != null){
                appCid = GroupSpaceCommentActivity.appCid;
                //appCid = FeedSpaceCommentsActivity.appCid;
            }
            if(FeedSpaceCommentsActivity.appCid != null){
                appCid = FeedSpaceCommentsActivity.appCid;
            }
            Log.d("clientId_MEMBERS","FromNotification"+appCid);
        }else {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SkipClientDetailsScreen.MySharedPrefOnSkipClientDetailsScreenForAppCid, Context.MODE_PRIVATE);
            appCid=sharedPreferences.getString("appCid","abc");
            Log.d("clientId_MEMBERS",""+appCid);
        }

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



        callFeedDataList(appCid);

        return view;
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


                        sharedFeedFragmentAdapter = new SharedFeedFragmentAdapter(feedArrayList, getContext());
                        recyclerView.setAdapter(sharedFeedFragmentAdapter);

                    } else {

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
