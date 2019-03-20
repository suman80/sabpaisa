package in.sabpaisa.droid.sabpaisa.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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

import in.sabpaisa.droid.sabpaisa.Adapter.SharedGroupFragmentAdapter;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.GroupListData;
import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.Proceed_Feed_FullScreen;
import in.sabpaisa.droid.sabpaisa.Proceed_Group_FullScreen;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.SimpleDividerItemDecoration;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;

/**
 * A simple {@link Fragment} subclass.
 */
public class SharedGroupFragment extends Fragment {

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

    SharedGroupFragmentAdapter sharedGroupFragmentAdapter;

    public SharedGroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_shared_group, container, false);

        linearLayoutnoDataFound = (LinearLayout) rootView.findViewById(R.id.noDataFound);
        groupList = (ShimmerRecyclerView) rootView.findViewById(R.id.groupList);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        groupList.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        groupList.setLayoutManager(llm);

        if (Proceed_Feed_FullScreen.notificationFlag || Proceed_Group_FullScreen.notificationFlag) {

            if (Proceed_Feed_FullScreen.clientId != null) {
                clientId = Proceed_Feed_FullScreen.clientId;
            }

            if (Proceed_Group_FullScreen.clientId != null){
                clientId = Proceed_Group_FullScreen.clientId;
            }

        }else {

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FullViewOfClientsProceed.MySharedPrefOnFullViewOfClientProceed, Context.MODE_PRIVATE);
            clientId = sharedPreferences.getString("clientId", "abc");
            Log.d("clientId_PGF", "" + clientId);

        }

        // Inflate the layout for this fragment


        //  swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        //  swipeRefreshLayout.setOnRefreshListener(this);

        sharedPreferences1 = getActivity().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        token = sharedPreferences1.getString("response", "123");

        Log.d("TokenInPGF", " " + token);

        callGroupDataList(token, clientId);


        return rootView;
    }


    public void callGroupDataList(final String token, final String clientId) {

        String urlJsonObj = AppConfig.Base_Url + AppConfig.App_api + "memberStatusWithGroup" + "?token=" + token + "&clientId=" + clientId;
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                urlJsonObj, new Response.Listener<String>() {

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
                    if (obj instanceof JSONArray) {
                        jsonArray = (JSONArray) obj;

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObjectX = jsonArray.getJSONObject(i);
                            JSONObject jsonObject1 = jsonObjectX.getJSONObject("clientGroup");
                            final GroupListData groupListData = new GroupListData();
                            groupListData.setClientId(jsonObject1.getString("clientId"));
                            groupListData.setGroupId(jsonObject1.getString("groupId"));
                            groupListData.setGroupName(jsonObject1.getString("groupName"));
                            groupListData.setGroupText(jsonObject1.getString("groupText"));
                            groupListData.setCreatedDate(jsonObject1.getString("createdDate"));
                            groupListData.setImagePath(jsonObject1.getString("imagePath"));
                            groupListData.setLogoPath(jsonObject1.getString("logoPath"));
                            groupListData.setMemberStatus(jsonObjectX.getString("memberStatus"));
                            if(groupListData.getMemberStatus().equals("Approved"))
                                    groupArrayList.add(groupListData);

                        }
                        //Log.d("groupArrayList1212", " " + groupArrayList.get(0).getGroupName());

                        sharedGroupFragmentAdapter = new SharedGroupFragmentAdapter(groupArrayList, getContext());
                        groupList.setAdapter(sharedGroupFragmentAdapter);

                    } else {
                        linearLayoutnoDataFound.setVisibility(View.VISIBLE);
                        groupList.setVisibility(View.GONE);
                    }
                }
                // Try and catch are included to handle any errors due to JSON
                catch (JSONException e) {
                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();
                    callGroupDataList(token, clientId);
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
                        callGroupDataList(token, clientId);
                        Log.e("SharedGroup fragments", "Group fragments Error");
                    }
                }
        );
        // Adds the JSON arra   y request "arrayreq" to the request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);
    }


}
