package in.sabpaisa.droid.sabpaisa.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.FeedData;
import in.sabpaisa.droid.sabpaisa.FeedDetails;
import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.MainFeedAdapter;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.RecyclerItemClickListener;
import in.sabpaisa.droid.sabpaisa.SimpleDividerItemDecoration;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;

public class SkipClient_FeedsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = SkipClient_FeedsFragment.class.getSimpleName();
    View rootView;
    SwipeRefreshLayout swipeRefreshLayout;
    String tag_string_req = "req_register";
    ArrayList<FeedData> feedArrayList = new ArrayList<FeedData>();
    MainFeedAdapter mainFeedAdapter;/*Globally Declared Adapter*/

    public static String ClientId;

    /*START Interface for getting data from activity*/
   // FeedsFragments.GetDataInterface sGetDataInterface;

    public SkipClient_FeedsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* try {
            sGetDataInterface= (GetDataInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "must implement GetDataInterface Interface");
        }*/
    }

    /*public void getDataFromActivity() {
        if(sGetDataInterface != null){
            this.feedArrayList = sGetDataInterface.getFeedDataList();
            mainFeedAdapter.setItems(this.feedArrayList);
            mainFeedAdapter.notifyDataSetChanged();
        }
    }*/
    /*END Interface for getting data from activity*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_skip_client__feeds, container, false);

        /*SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SkipClientDetailsScreen.MySharedPrefForIdSkip, Context.MODE_PRIVATE);

        ClientId=sharedPreferences.getString("ClientId","123");

        Log.d("ClientId_FeedsFrag"," "+ClientId);*/

        /*final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
        frameLayout.setBackgroundColor();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dummyfrag_scrollableview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        DessertAdapter adapter = new DessertAdapter(getContext());
        recyclerView.setAdapter(adapter);*/

        // swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        //swipeRefreshLayout.setOnRefreshListener(this);

        Log.d("feedArrayList"," "+feedArrayList);

        callFeedDataList(Integer.parseInt(ClientId));
        return rootView;
    }

    public void callFeedDataList(final int ClientId) {
        String urlJsonObj = AppConfig.Base_Url+ AppConfig.App_api+"getParticularClientsFeeds/"+"?client_Id="+ ClientId;



        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                urlJsonObj, new Response.Listener<String>(){

            // Takes the response from the JSON request
            @Override
            public void onResponse(String response) {
                try {

                    Log.d(TAG, "profeed1: " + response);
                    //swipeRefreshLayout.setRefreshing(false);
                    feedArrayList = new ArrayList<FeedData>();

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    String response1 = jsonObject.getString("response");

                    if (status.equals("success")&&response1.equals("No_Record_Found")) {

                        Toast.makeText(getContext(),"No Result Found",Toast.LENGTH_SHORT).show();

                    }else {
                        JSONArray jsonArray = jsonObject.getJSONArray("response");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            FeedData feedData = new FeedData();
                            feedData.setClientId(jsonObject1.getString("clientId"));
                            feedData.setFeedId(jsonObject1.getString("feedId"));
                            feedData.setFeedName(jsonObject1.getString("feedName"));
                            feedData.setFeedText(jsonObject1.getString("feedText"));
                            feedData.setCreatedDate(jsonObject1.getString("createdDate"));
                            feedData.setImagePath(jsonObject1.getString("imagePath"));
                            feedData.setLogoPath(jsonObject1.getString("logoPath"));
                            feedArrayList.add(feedData);

                        }
                        Log.d("feedAfterParse", " " + feedArrayList.get(0).getFeedName());
                            /*START listener for sending data to activity*/
                        OnFragmentInteractionListener listener = (OnFragmentInteractionListener) getActivity();
                        listener.onFragmentSetFeeds(feedArrayList);
                            /*END listener for sending data to activity*/
                        loadFeedListView(feedArrayList, (RecyclerView) rootView.findViewById(R.id.recycler_view_feeds));
                    }

                }
                // Try and catch are included to handle any errors due to JSON
                catch (JSONException e) {
                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();
                    callFeedDataList(ClientId);
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
                        callFeedDataList(ClientId);
                        Log.e("Feed", "FeedError");
                    }
                }
        );
        // Adds the JSON array request "arrayreq" to the request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);
    }

    private void loadFeedListView(ArrayList<FeedData> arrayList, RecyclerView recyclerView) {

        mainFeedAdapter = new MainFeedAdapter(arrayList,getContext());
        recyclerView.setAdapter(mainFeedAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(( getContext()), FeedDetails.class);
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

    /*START onRefresh() for SwipeRefreshLayout*/
    @Override
    public void onRefresh() {
        //callFeedDataList(Id);
    }

    public interface GetDataInterface {
        ArrayList<FeedData> getFeedDataList();
    }
    /*END onRefresh() for SwipeRefreshLayout*/
}
