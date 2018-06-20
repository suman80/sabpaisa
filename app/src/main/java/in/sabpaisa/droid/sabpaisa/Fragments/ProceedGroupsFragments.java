package in.sabpaisa.droid.sabpaisa.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.ProceedFeedsFragmentsOfflineAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.ProceedGroupsFragmentsOfflineAdapter;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments;
import in.sabpaisa.droid.sabpaisa.FeedsFragments;
import in.sabpaisa.droid.sabpaisa.GroupListData;
import in.sabpaisa.droid.sabpaisa.GroupsFragments;
import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.MainGroupAdapter1;
import in.sabpaisa.droid.sabpaisa.Model.FeedDataForOffLine;
import in.sabpaisa.droid.sabpaisa.Model.GroupDataForOffLine;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.RecyclerItemClickListener;
import in.sabpaisa.droid.sabpaisa.SimpleDividerItemDecoration;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;
import in.sabpaisa.droid.sabpaisa.Util.SkipClientDetailsScreen;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProceedGroupsFragments extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    View rootView = null;
    private static final String TAG = ProceedGroupsFragments.class.getSimpleName();

    public static String clientId;
    LinearLayout linearLayoutnoDataFound;
    ShimmerRecyclerView groupList;

    SharedPreferences sharedPreferences1;

    String token;

    String tag_string_req = "req_register";
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<GroupListData> groupArrayList;
    ArrayList<GroupDataForOffLine> groupArrayListForOffline;
    MainGroupAdapter1 mainGroupAdapter1;

    /////////Local Db//////////
    AppDbComments db;

    /*Globally Declared Adapter*/
    /*START Interface for getting data from activity*/
    GetDataInterface sGetDataInterface;
    /*START Interface for getting data from activity*/

    public ProceedGroupsFragments() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragments_groups, container, false);
        linearLayoutnoDataFound = (LinearLayout)rootView.findViewById(R.id.noDataFound);
        groupList=(ShimmerRecyclerView)rootView.findViewById(R.id.groupList);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        groupList.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        groupList.setLayoutManager(llm);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FullViewOfClientsProceed.MySharedPrefOnFullViewOfClientProceed, Context.MODE_PRIVATE);
        clientId=sharedPreferences.getString("clientId","abc");
        Log.d("clientId_PGF",""+clientId);

        // Inflate the layout for this fragment


        //  swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        //  swipeRefreshLayout.setOnRefreshListener(this);

        sharedPreferences1 = getActivity().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        token = sharedPreferences1.getString("response", "123");

        Log.d("TokenInPGF"," "+token);


        ///////////////////////DB/////////////////////////////////
        db = new AppDbComments(getContext());

        if (isOnline()) {
            callGroupDataList(token, clientId);
        }else {

            /////////////////////Retrive data from local DB///////////////////////////////

            Cursor res = db.getParticularGroupData(clientId);
            groupArrayListForOffline = new ArrayList<>();
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
                    stringBuffer.append(res.getBlob(7) + " ");
                    stringBuffer.append(res.getBlob(8) + " ");

                    GroupDataForOffLine groupDataForOffLine = new GroupDataForOffLine();
                    groupDataForOffLine.setClientId(res.getString(1));
                    groupDataForOffLine.setGroupId(res.getString(2));
                    groupDataForOffLine.setGroupName(res.getString(3));
                    groupDataForOffLine.setGroupText(res.getString(4));
                    groupDataForOffLine.setMemberStatus(res.getString(5));
                    groupDataForOffLine.setImagePath(res.getBlob(7));
                    groupDataForOffLine.setLogoPath(res.getBlob(8));
                    groupArrayListForOffline.add(groupDataForOffLine);

                }
                Log.d("getGroupData", "-->" + stringBuffer);

                ProceedGroupsFragmentsOfflineAdapter adapter = new ProceedGroupsFragmentsOfflineAdapter(groupArrayListForOffline,getContext());
                groupList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                Log.d("PGFLocalDb", "In Else Part");
                Toast.makeText(getContext(), "No Data Found !", Toast.LENGTH_SHORT).show();
            }


        }

        return rootView;
    }

    public void callGroupDataList(final String token,final String clientId ) {

        db.deleteAllGroupData();

        String urlJsonObj = AppConfig.Base_Url+AppConfig.App_api+"memberStatusWithGroup"+"?token="+ token+"&clientId="+clientId;
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                urlJsonObj, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                try {
                    groupArrayList = new ArrayList<GroupListData>();
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d(TAG, "PGD_RESP: " + response);

                    String status = jsonObject.getString("status");

                    String response1 = jsonObject.getString("response");

                    JSONArray jsonArray = null;
                    Object obj = jsonObject.get("response");
                    if(obj instanceof JSONArray){
                        jsonArray = (JSONArray)obj;

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObjectX = jsonArray.getJSONObject(i);
                            JSONObject jsonObject1 = jsonObjectX.getJSONObject("clientGroup");
                            GroupListData groupListData = new GroupListData();
                            groupListData.setClientId(jsonObject1.getString("clientId"));
                            groupListData.setGroupId(jsonObject1.getString("groupId"));
                            groupListData.setGroupName(jsonObject1.getString("groupName"));
                            groupListData.setGroupText(jsonObject1.getString("groupText"));
                            groupListData.setCreatedDate(jsonObject1.getString("createdDate"));
                            groupListData.setImagePath(jsonObject1.getString("imagePath"));
                            groupListData.setLogoPath(jsonObject1.getString("logoPath"));
                            groupListData.setMemberStatus(jsonObjectX.getString("memberStatus"));
                            groupArrayList.add(groupListData);

                            //////////////////////////////LOCAL DB//////////////////////////////////////

                            boolean isInserted = db.insertGroupData(groupListData,token);
                            if (isInserted == true) {

                                //Toast.makeText(AllTransactionSummary.this, "Data  Inserted", Toast.LENGTH_SHORT).show();

                                Log.d("PGF", "LocalDBInIfPart" + isInserted);

                            } else {
                                Log.d("PGF", "LocalDBInElsePart" + isInserted);
                                //Toast.makeText(AllTransactionSummary.this, "Data  Not Inserted", Toast.LENGTH_SHORT).show();
                            }


                        }
                        Log.d("groupArrayList1212", " " + groupArrayList.get(0).getGroupName());
                        /*START listener for sending data to activity*/
                        OnFragmentInteractionListener listener = (OnFragmentInteractionListener) getActivity();
                        listener.onFragmentSetGroups(groupArrayList);
                        /*END listener for sending data to activity*/

                        mainGroupAdapter1 = new MainGroupAdapter1(groupArrayList,getContext());
                        groupList.setAdapter(mainGroupAdapter1);

                    }else {
                        linearLayoutnoDataFound.setVisibility(View.VISIBLE);
                        groupList.setVisibility(View.GONE);
                    }
                }
                // Try and catch are included to handle any errors due to JSON
                catch(JSONException e){
                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();
                    callGroupDataList(token,clientId);
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
                        callGroupDataList(token,clientId);
                        Log.e("Group fragments", "Group fragments Error");
                    }
                }
        );
        // Adds the JSON arra   y request "arrayreq" to the request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);
    }


    /*START onRefresh() for SwipeRefreshLayout*/
    @Override
    public void onRefresh() {
        callGroupDataList(token,clientId);
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
            this.groupArrayList = sGetDataInterface.getGroupDataList();
            mainGroupAdapter1.setItems(this.groupArrayList);
            mainGroupAdapter1.notifyDataSetChanged();
        }

        Log.d("PGF_I&A"," "+sGetDataInterface+"&"+groupArrayList);
    }




    public interface GetDataInterface {
        ArrayList<GroupListData> getGroupDataList();
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
            Log.v("PGF", "Internet Connection Not Present");
            return false;
        }
    }


}
