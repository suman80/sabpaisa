package in.sabpaisa.droid.sabpaisa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Model.Member_GetterSetter;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;
import in.sabpaisa.droid.sabpaisa.Util.NoOfGroupmemberAdapter;

public class NumberOfGroups extends AppCompatActivity {

    public static String clientId;
    LinearLayout linearLayoutnoDataFound;
    ArrayList<Member_GetterSetter> member_getterSetterArrayList;
    NoOfGroupmemberAdapter memberAdapter;
    ShimmerRecyclerView recycler_view_Member;

    String GroupId;
    Toolbar toolbar;

    public static String memberGroupRole;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_number_of_groups);
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


        SharedPreferences sharedPreferences = getSharedPreferences(FullViewOfClientsProceed.MySharedPrefOnFullViewOfClientProceed, Context.MODE_PRIVATE);
        clientId = sharedPreferences.getString("clientId", "abc");
        Log.d("NumerOFGroup", "" + clientId);
        Intent intent = getIntent();
        GroupId = intent.getStringExtra("GroupId");

        Log.d("NumerOFGroup", "" + GroupId);

        memberGroupRole = intent.getStringExtra("memberGroupRole");

        Log.d("NumerOFGroup", "memberGroupRole" + memberGroupRole);

        NoOfGRPMembers(clientId, GroupId, "Approved");

    }

    public void NoOfGRPMembers(final String clientid, final String Groupid, final String status) {
        //StringRequest stringRequest=ne
        //
        // w StringRequest(Method.GET, AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_NoOFGroupMembeers+clientId+"&groupId="+Groupid+"&status="+status, new Response.Listener<String>() {

        String tag_string_req = "req_register";
        ///String url = AppConfig.Base_Url+AppConfig.App_api+AppConfig.URL_NoOFGroupMembeers+clientId+"&groupId="+Groupid+"&status="+status;

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_NoOFGroupMembeers + clientid + "&groupId=" + Groupid + "&status=" + status, new Response.Listener<String>() {

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
                            Member_GetterSetter member_getterSetter = new Member_GetterSetter();
                            member_getterSetter.setUserImageUrl(jsonObject1.getString("userImageUrl"));
                            member_getterSetter.setFullName(jsonObject1.getString("fullName"));
                            member_getterSetter.setPhoneNumber(jsonObject1.getString("phoneNumber"));
                            member_getterSetter.setEmailId(jsonObject1.getString("emailId"));
                            member_getterSetter.setUserAccessToken(jsonObject1.getString("userAccessToken"));
                            member_getterSetter.setGroupId(jsonObject1.getString("groupId"));
                            member_getterSetter.setUserId(jsonObject1.getString("userId"));

                            if (!jsonObject1.getString("lookupRole").equals("null")) {

                                member_getterSetter.setUin_Role(jsonObject1.getString("uin_Role"));
                                member_getterSetter.setRoleId(jsonObject1.getJSONObject("lookupRole").optString("roleId"));
                                member_getterSetter.setRoleName(jsonObject1.getJSONObject("lookupRole").optString("roleName"));

//                            Log.d("MMBRNAME", "" + jsonObject1.getString("fullName"));
//                            Log.d("MMBRIMAGE", "" + jsonObject1.getString("userImageUrl"));
                                Log.d("NOG_", "uin_Role" + jsonObject1.getString("uin_Role"));
                                Log.d("NOG_", "roleId" + jsonObject1.getJSONObject("lookupRole").optString("roleId"));
                                Log.d("NOG_", "roleName" + jsonObject1.getJSONObject("lookupRole").optString("roleName"));
                            }

                            member_getterSetterArrayList.add(member_getterSetter);

                        }
                        Log.d("ArrayListAfterParse", " " + member_getterSetterArrayList.get(0).getFullName());

                        /*START listener for sending data to activity*/
                       /* OnFragmentInteractionListener listener = (OnFragmentInteractionListener) getApplication();
                        listener.onFragmentSetMembers(member_getterSetterArrayList);
                      */
                        /*END listener for sending data to activity*/
                        memberAdapter = new NoOfGroupmemberAdapter(member_getterSetterArrayList, getApplicationContext());
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (member_getterSetterArrayList != null && !member_getterSetterArrayList.isEmpty()) {

            member_getterSetterArrayList.clear();

        }
    }
}