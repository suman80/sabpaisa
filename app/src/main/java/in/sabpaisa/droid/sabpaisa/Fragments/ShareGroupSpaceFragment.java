package in.sabpaisa.droid.sabpaisa.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.SharedGroupFragmentAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.SkipGroupFragmentAdapter;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.GroupListData;
import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.SimpleDividerItemDecoration;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.SkipClientDetailsScreen;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShareGroupSpaceFragment extends Fragment {

    String appCid,userAcessToken;

    LinearLayout linearLayoutnoDataFound;
    ShimmerRecyclerView groupList;
    ArrayList<GroupListData> groupArrayList ;
    MaterialRippleLayout rippleClickAdd;

    FrameLayout framelayoutAddGroup;
    LinearLayout linearLayoutAddGrpWhenNoData;

    SharedGroupFragmentAdapter sharedGroupFragmentAdapter;


    //Values get from SkipClient details screen
    String clientName,clientLogoPath,clientImagePath,state;


    public ShareGroupSpaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_share_group_space, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SkipClientDetailsScreen.MySharedPrefOnSkipClientDetailsScreenForAppCid, Context.MODE_PRIVATE);
        appCid=sharedPreferences.getString("appCid","abc");
        Log.d("clientId_MEMBERS",""+appCid);

        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        userAcessToken = sharedPreferences1.getString("response", "123");

        Log.d("userAcessToken", " " + userAcessToken);


        linearLayoutnoDataFound = (LinearLayout) view.findViewById(R.id.noDataFound);
        groupList = (ShimmerRecyclerView) view.findViewById(R.id.groupList);

        rippleClickAdd = (MaterialRippleLayout) view.findViewById(R.id.rippleClickAdd);
        framelayoutAddGroup = (FrameLayout) view.findViewById(R.id.framelayoutAddGroup);

        linearLayoutAddGrpWhenNoData = (LinearLayout) view.findViewById(R.id.linearLayoutAddGrpWhenNoData);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        groupList.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        groupList.setLayoutManager(llm);
        groupList.setMotionEventSplittingEnabled(false);

        if (isOnline()) {
            callGroupDataList(userAcessToken, appCid);
        } else {
            //Todo offline
        }



        return view;
    }


    public void callGroupDataList(final String token, final String appCid) {

        String tag_string_req = "tag_string_req";

        String urlJsonObj = AppConfig.Base_Url + AppConfig.App_api + "memberStatusWithGroup" + "?token=" + token + "&appcid=" + appCid;

        Log.d("SkipGroupSpace","url___"+urlJsonObj);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                urlJsonObj, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    groupArrayList = new ArrayList<GroupListData>();

                    JSONObject jsonObject = new JSONObject(response);

                    Log.d("SkipGroupFrag", "PGD_RESP: " + response);

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
                            //groupListData.setImagePath(jsonObject1.getString("imagePath"));
                            groupListData.setLogoPath(jsonObject1.getString("logoPath"));
                            groupListData.setMemberStatus(jsonObjectX.getString("memberStatus"));
                            groupListData.setMemberGroupRole(jsonObjectX.getString("memberGroupRole"));

                            Log.d("ProceedGroupFragmGR"," "+jsonObjectX.getString("memberGroupRole"));

                            if (groupListData.getMemberStatus().contains("Approved")) {

                                groupArrayList.add(groupListData);

                            }

                        }
                        Log.d("groupArrayList1212", " " + groupArrayList.get(0).getGroupName());


                        sharedGroupFragmentAdapter = new SharedGroupFragmentAdapter(groupArrayList, getContext());
                        groupList.setAdapter(sharedGroupFragmentAdapter);

                    } else {


                    }
                }
                // Try and catch are included to handle any errors due to JSON
                catch (JSONException e) {
                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();
                    callGroupDataList(token, appCid);
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
                        callGroupDataList(token, appCid);
                        Log.e("Group fragments", "Group fragments Error");
                    }
                }
        );
        // Adds the JSON arra   y request "arrayreq" to the request queue
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
            Log.v("SGF", "Internet Connection Not Present");
            return false;
        }
    }


}
