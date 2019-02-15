package in.sabpaisa.droid.sabpaisa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

import in.sabpaisa.droid.sabpaisa.Adapter.MembersOfAGroupSpaceAdapter;
import in.sabpaisa.droid.sabpaisa.Model.MemberSpaceModel;
import in.sabpaisa.droid.sabpaisa.Model.Member_GetterSetter;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.NoOfGroupmemberAdapter;
import in.sabpaisa.droid.sabpaisa.Util.SkipClientDetailsScreen;

public class MembersOfAGroupSpace extends AppCompatActivity {

    LinearLayout linearLayoutnoDataFound;
    ArrayList<Member_GetterSetter> member_getterSetterArrayList;
    MembersOfAGroupSpaceAdapter memberAdapter;
    ShimmerRecyclerView recycler_view_Member;

    String GroupId;
    Toolbar toolbar;

    String appCid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_of_agroup_space);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Inflate the layout for this fragment
        linearLayoutnoDataFound = (LinearLayout) findViewById(R.id.noDataFound);
        recycler_view_Member = (ShimmerRecyclerView) findViewById(R.id.recycler_view_Member);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view_Member.addItemDecoration(new SimpleDividerItemDecoration(this));
        recycler_view_Member.setLayoutManager(llm);

        Intent intent = getIntent();
        GroupId = intent.getStringExtra("GroupId");

        Log.d("NumerOFGroup", "" + GroupId);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SkipClientDetailsScreen.MySharedPrefOnSkipClientDetailsScreenForAppCid, Context.MODE_PRIVATE);
        appCid=sharedPreferences.getString("appCid","abc");
        Log.d("clientId_MEMBERS",""+appCid);

        membersOfAGropup(appCid,GroupId,"Approved");

    }



    public void membersOfAGropup(final String appCid, final String Groupid, final String status) {

        String tag_string_req = "req_register";

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_NoOFGroupMembersSpace + appCid + "&groupId=" + Groupid + "&status=" + status;

        Log.d("MembersOfAGroupSpace","url__ "+url);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            // Takes the response from the JSON request
            @Override
            public void onResponse(String response) {
                try {

                    Log.d("NoOfGRPMMBr", "Member " + response);
                    //swipeRefreshLayout.setRefreshing(false);
                    member_getterSetterArrayList = new ArrayList<>();

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    String response1 = jsonObject.getString("response");

                    if (status.equals("failure")) {

                        //Toast.makeText(getContext(),"No Result Found",Toast.LENGTH_SHORT).show();

                        linearLayoutnoDataFound.setVisibility(View.VISIBLE);
                        recycler_view_Member.setVisibility(View.GONE);

                    } else {
                        JSONArray jsonArray = jsonObject.getJSONArray("response");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            final Member_GetterSetter member_getterSetter = new Member_GetterSetter();
                            member_getterSetter.setDeviceId(jsonObject1.getString("deviceId"));
                            member_getterSetter.setEmailId(jsonObject1.getString("emailId"));
                            member_getterSetter.setGroupId(jsonObject1.getString("groupId"));
                            member_getterSetter.setId(jsonObject1.getString("id"));
                            member_getterSetter.setPhoneNumber(jsonObject1.getString("phoneNumber"));
                            member_getterSetter.setStatus(jsonObject1.getString("status"));
                            member_getterSetter.setTimestampOfJoining(jsonObject1.getString("timestampOfJoining"));
                            //member_getterSetter.setUin(jsonObject1.getString("uin"));
                            member_getterSetter.setUserId(jsonObject1.getString("userId"));
                            member_getterSetter.setUserImageUrl(jsonObject1.getString("userImageUrl"));
                            member_getterSetter.setFullName(jsonObject1.getString("fullName"));
                            member_getterSetter.setUserAccessToken(jsonObject1.getString("userAccessToken"));
                            member_getterSetter.setUin_Role(jsonObject1.getString("uin_Role"));
                            member_getterSetter.setUin_Status(jsonObject1.getString("uin_Status"));



                            member_getterSetterArrayList.add(member_getterSetter);

                        }
                        Log.d("ArrayListAfterParse", " " + member_getterSetterArrayList.get(0).getFullName());


                        memberAdapter = new MembersOfAGroupSpaceAdapter(member_getterSetterArrayList, MembersOfAGroupSpace.this);
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
                    public void onErrorResponse(VolleyError error) {

                    }


                }
        );
        // Adds the JSON array request "arrayreq" to the request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }



}
