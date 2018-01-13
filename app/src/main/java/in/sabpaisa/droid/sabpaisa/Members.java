package in.sabpaisa.droid.sabpaisa;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import in.sabpaisa.droid.sabpaisa.Adapter.MemberAdapter;
import in.sabpaisa.droid.sabpaisa.Fragments.ProceedFeedsFragments;
import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.Model.Member_GetterSetter;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;


/**
 * A simple {@link Fragment} subclass.
 */
public class Members extends Fragment {
    private static final String TAG = Members.class.getSimpleName();
    View rootView;
    public static String clientId;
    ArrayList<Member_GetterSetter> member_getterSetterArrayList;
    MemberAdapter memberAdapter;
    ShimmerRecyclerView recycler_view_Member;

    public Members() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_members, container, false);

        recycler_view_Member = (ShimmerRecyclerView) rootView.findViewById(R.id.recycler_view_Member);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view_Member.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        recycler_view_Member.setLayoutManager(llm);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FullViewOfClientsProceed.MySharedPrefOnFullViewOfClientProceed, Context.MODE_PRIVATE);
        clientId=sharedPreferences.getString("clientId","abc");
        Log.d("clientId_MEMBERS",""+clientId);

        memberData(clientId);

        return rootView;
    }


    public void memberData (final String clientId)
    {
        String tag_string_req = "req_register";
        String url = AppConfig.URL_Show_Member+"clientId="+clientId;

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>(){

            // Takes the response from the JSON request
            @Override
            public void onResponse(String response) {
                try {

                    Log.d(TAG, "Member: " + response);
                    //swipeRefreshLayout.setRefreshing(false);
                    member_getterSetterArrayList = new ArrayList<>();

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    String response1 = jsonObject.getString("response");

                    if (status.equals("failure")) {

                        //Toast.makeText(getContext(),"No Result Found",Toast.LENGTH_SHORT).show();

                    }else {
                        JSONArray jsonArray = jsonObject.getJSONArray("response");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            Member_GetterSetter member_getterSetter = new Member_GetterSetter();
                            member_getterSetter.setDeviceId(jsonObject1.getString("deviceId"));
                            member_getterSetter.setEmailId(jsonObject1.getString("emailId"));
                            member_getterSetter.setGroupId(jsonObject1.getString("groupId"));
                            member_getterSetter.setId(jsonObject1.getString("id"));
                            member_getterSetter.setPhoneNumber(jsonObject1.getString("phoneNumber"));
                            member_getterSetter.setStatus(jsonObject1.getString("status"));
                            member_getterSetter.setTimestampOfJoining(jsonObject1.getString("timestampOfJoining"));
                            member_getterSetter.setUin(jsonObject1.getString("uin"));
                            member_getterSetter.setUserId(jsonObject1.getString("userId"));
                            member_getterSetter.setUserImageUrl(jsonObject1.getString("userImageUrl"));
                            member_getterSetter.setFullName(jsonObject1.getString("fullName"));


                            member_getterSetterArrayList.add(member_getterSetter);

                        }
                        Log.d("ArrayListAfterParse", " " + member_getterSetterArrayList.get(0).getFullName());

                            memberAdapter = new MemberAdapter(member_getterSetterArrayList);
                            recycler_view_Member.setAdapter(memberAdapter);

                    }

                }
                // Try and catch are included to handle any errors due to JSON
                catch (JSONException e) {
                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();

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

                        Log.e("Member", "FeedError"+error);
                    }
                }
        );
        // Adds the JSON array request "arrayreq" to the request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);
    }



}
