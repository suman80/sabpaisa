package in.sabpaisa.droid.sabpaisa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.Util.AppConfiguration;

/**
 * Created by SabPaisa on 03-07-2017.
 */
/*implements SwipeRefreshLayout.OnRefreshListener*/
public class FeedsFragments extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    View rootView;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<FeedData> feedArrayList = new ArrayList<FeedData>();
    MainFeedAdapter ca;/*Globally Declared Adapter*/

    /*START Interface for getting data from activity*/
    GetDataInterface sGetDataInterface;

    public FeedsFragments() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            sGetDataInterface= (GetDataInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "must implement GetDataInterface Interface");
        }
    }

    public void getDataFromActivity() {
        if(sGetDataInterface != null){
            this.feedArrayList = sGetDataInterface.getFeedDataList();
            ca.setItems(this.feedArrayList);
            ca.notifyDataSetChanged();
        }
    }
    /*END Interface for getting data from activity*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragments_feeds, container, false);

        /*final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
        frameLayout.setBackgroundColor();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dummyfrag_scrollableview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        DessertAdapter adapter = new DessertAdapter(getContext());
        recyclerView.setAdapter(adapter);*/

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);

        callFeedDataList();
        return rootView;
    }

    public void callFeedDataList() {
        String urlJsonObj = AppConfiguration.MAIN_URL + "/getFeedsDetailsBasedOnOrganizations/SRS";
        urlJsonObj = urlJsonObj.trim().replace(" ", "%20");
        // Creating the JsonArrayRequest class called arrayreq, passing the required parameters
        //JsonURL is the URL to be fetched from
        JsonArrayRequest arrayreq = new JsonArrayRequest(urlJsonObj,
                // The second parameter Listener overrides the method onResponse() and passes
                //JSONArray as a parameter
                new Response.Listener<JSONArray>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            swipeRefreshLayout.setRefreshing(false);
                            feedArrayList = new ArrayList<FeedData>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject colorObj = response.getJSONObject(i);
                                FeedData feedData = new FeedData();
                                feedData.setFeedText(colorObj.getString("feedText"));
                                feedData.setFeed_Name(colorObj.getString("feed_Name"));
                                feedData.setFeed_date(colorObj.getString("feed_date"));
                                feedData.setFeedId(colorObj.getString("feedId"));
                                feedArrayList.add(feedData);

                            }
                            /*START listener for sending data to activity*/
                            OnFragmentInteractionListener listener = (OnFragmentInteractionListener) getActivity();
                            listener.onFragmentSetFeeds(feedArrayList);
                            /*END listener for sending data to activity*/
                            loadFeedListView(feedArrayList, (RecyclerView) rootView.findViewById(R.id.recycler_view_feeds));
                        }
                        // Try and catch are included to handle any errors due to JSON
                        catch (JSONException e) {
                            // If an error occurs, this prints the error to the log
                            e.printStackTrace();
                            callFeedDataList();
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
                        callFeedDataList();
                        Log.e("Feed", "FeedError");
                    }
                }
        );
        // Adds the JSON array request "arrayreq" to the request queue
        AppController.getInstance().addToRequestQueue(arrayreq);
    }

    private void loadFeedListView(ArrayList<FeedData> arrayList, RecyclerView recyclerView) {

        ca = new MainFeedAdapter(arrayList);
        recyclerView.setAdapter(ca);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(((COA) getContext()), FeedDetails.class);
                        intent.putExtra("FeedId", feedArrayList.get(position).getFeedId());
                        intent.putExtra("FeedName", feedArrayList.get(position).getFeed_Name());
                        intent.putExtra("FeedDeatils", feedArrayList.get(position).getFeedText());
                        startActivity(intent);
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

    /*START onRefresh() for SwipeRefreshLayout*/
    @Override
    public void onRefresh() {
        callFeedDataList();
    }

    public interface GetDataInterface {
        ArrayList<FeedData> getFeedDataList();
    }
    /*END onRefresh() for SwipeRefreshLayout*/
}
