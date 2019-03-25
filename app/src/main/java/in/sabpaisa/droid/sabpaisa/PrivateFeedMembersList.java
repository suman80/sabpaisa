package in.sabpaisa.droid.sabpaisa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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

import in.sabpaisa.droid.sabpaisa.Adapter.PrivateFeedMemberListAdapter;
import in.sabpaisa.droid.sabpaisa.Model.Member_GetterSetter;
import in.sabpaisa.droid.sabpaisa.Model.PrivateMemberListModel;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.NoOfGroupmemberAdapter;

public class PrivateFeedMembersList extends AppCompatActivity {

    public static String feedId;
    LinearLayout linearLayoutnoDataFound;
    ShimmerRecyclerView recycler_view_Member;
    Toolbar toolbar;
    ArrayList<Member_GetterSetter> privateMemberArrayList;
    PrivateFeedMemberListAdapter privateFeedMemberListAdapter;
    public NoOfGroupmemberAdapter memberAdapter;
    ProgressDialog progressDialog;
    public static String Flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_feed_members_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        feedId = getIntent().getStringExtra("feedId");
        Log.d("PrivateFeedMembersList","feedId__"+feedId);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressDialog = new ProgressDialog(PrivateFeedMembersList.this);
        progressDialog.setMessage("Please wait...");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                privateMemberArrayList.clear();
                Flag = null;
                onBackPressed();
            }
        });

        Flag = "PrivateFeedMembersList";

        linearLayoutnoDataFound = (LinearLayout) findViewById(R.id.noDataFound);
        recycler_view_Member = (ShimmerRecyclerView) findViewById(R.id.recycler_view_Member);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view_Member.addItemDecoration(new SimpleDividerItemDecoration(this));
        recycler_view_Member.setLayoutManager(llm);


        if (isOnline()) {
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            privateFeedMembers(feedId);
        }else {
            Toast.makeText(PrivateFeedMembersList.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
        }


    }



    public void privateFeedMembers(final String feedId) {

        String tag_string_req = "req_register";

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_privateFeedMember +"?feed_id="+feedId;

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            // Takes the response from the JSON request
            @Override
            public void onResponse(String response) {
                try {

                    Log.d("NoOfGRPMMBr", "Member " + response);
                    //swipeRefreshLayout.setRefreshing(false);
                    privateMemberArrayList = new ArrayList<>();

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    String response1 = jsonObject.getString("response");

                    if (status.equals("failure")) {

                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }

                        Toast.makeText(PrivateFeedMembersList.this,response1,Toast.LENGTH_SHORT).show();

                        linearLayoutnoDataFound.setVisibility(View.VISIBLE);
                        recycler_view_Member.setVisibility(View.GONE);

                    } else {

                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }

                        JSONArray jsonArray = jsonObject.getJSONArray("response");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            /*PrivateMemberListModel privateMemberListModel = new PrivateMemberListModel();
                            if (jsonObject1.getJSONObject("userImageUrl") != null)
                            privateMemberListModel.setUserImageUrl(jsonObject1.getJSONObject("userImageUrl").getString("userImageUrl"));
                            if (jsonObject1.getJSONObject("usersMasterByUserId") != null)
                            privateMemberListModel.setFullName(jsonObject1.getJSONObject("usersMasterByUserId").getString("fullName"));
                            privateMemberListModel.setUserId(jsonObject1.getJSONObject("usersMasterByUserId").getString("userId"));
                            privateMemberListModel.setUserAccessToken(jsonObject1.getJSONObject("usersMasterByUserId").getString("userAccessToken"));
                            privateMemberListModel.setContactNumber(jsonObject1.getJSONObject("usersMasterByUserId").getString("contactNumber"));
*/

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


                            privateMemberArrayList.add(member_getterSetter);


                        }

                        /*privateFeedMemberListAdapter = new PrivateFeedMemberListAdapter(privateMemberArrayList, PrivateFeedMembersList.this);
                        recycler_view_Member.setAdapter(privateFeedMemberListAdapter);*/

                        memberAdapter = new NoOfGroupmemberAdapter(privateMemberArrayList, PrivateFeedMembersList.this);
                        recycler_view_Member.setAdapter(memberAdapter);


                    }

                }
                // Try and catch are included to handle any errors due to JSON
                catch (JSONException e) {
                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
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
        privateMemberArrayList.clear();
        Flag = null;
    }




    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v("PFF", "Internet Connection Not Present");
            return false;
        }
    }


}
