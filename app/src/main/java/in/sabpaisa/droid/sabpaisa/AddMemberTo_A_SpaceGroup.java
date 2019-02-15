package in.sabpaisa.droid.sabpaisa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.AddMemberToSpaceGroupAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.AddMemberTo_A_GroupAdapter;
import in.sabpaisa.droid.sabpaisa.Interfaces.AddMemberCallBack;
import in.sabpaisa.droid.sabpaisa.Model.MemberSpaceModel;
import in.sabpaisa.droid.sabpaisa.Model.Member_GetterSetter;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;
import in.sabpaisa.droid.sabpaisa.Util.SkipClientDetailsScreen;

public class AddMemberTo_A_SpaceGroup extends AppCompatActivity implements AddMemberCallBack {

    ShimmerRecyclerView recycler_view_Member;
    ArrayList<MemberSpaceModel> member_getterSetterArrayList;
    AddMemberToSpaceGroupAdapter memberToSpaceGroupAdapter;

    String groupId;

    Toolbar toolbar;

    MenuItem item;

    ArrayList<String> memberNumberArraylist;

    String userAccessToken;

    String appCid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member_to__a__space_group);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        recycler_view_Member = (ShimmerRecyclerView)findViewById(R.id.recycler_view_Member);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Member");

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view_Member.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        recycler_view_Member.setLayoutManager(llm);
        recycler_view_Member.setMotionEventSplittingEnabled(false);

        SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        userAccessToken = sharedPreferences1.getString("response", "123");

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SkipClientDetailsScreen.MySharedPrefOnSkipClientDetailsScreenForAppCid, Context.MODE_PRIVATE);
        appCid=sharedPreferences.getString("appCid","abc");
        Log.d("clientId_MEMBERS",""+appCid);


        groupId = getIntent().getStringExtra("groupId");

        Log.d("AddMemberToGRP","GRP_ID "+groupId);
        memberNumberArraylist = new ArrayList<>();

        memberData(appCid,groupId);

    }



    public void memberData (final String appCid,final String groupId)
    {

        String tag_string_req = "req_register";
        String url = AppConfig.Base_Url+AppConfig.App_api+AppConfig.URL_addMemberList+"?appcid="+appCid+"&groupId="+groupId;

        Log.d("AddMemberToaGrp", "Member_URL: " + url);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>(){

            // Takes the response from the JSON request
            @Override
            public void onResponse(String response) {
                try {

                    Log.d("AddMemberToaGrp", "Member: " + response);
                    //swipeRefreshLayout.setRefreshing(false);
                    member_getterSetterArrayList = new ArrayList<>();

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    String response1 = jsonObject.getString("response");

                    if (status.equals("success")&&response1.equals("[]")) {

                        Toast.makeText(AddMemberTo_A_SpaceGroup.this,"No Record Found",Toast.LENGTH_SHORT).show();

                        recycler_view_Member.setVisibility(View.GONE);

                    }else {
                        JSONArray jsonArray = jsonObject.getJSONArray("response");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            final MemberSpaceModel memberSpaceModel = new MemberSpaceModel();
                            memberSpaceModel.setMemberId(jsonObject1.getString("memberId"));
                            memberSpaceModel.setRoleId(jsonObject1.getJSONObject("lookupRole").getString("roleId"));
                            memberSpaceModel.setRoleName(jsonObject1.getJSONObject("lookupRole").getString("roleName"));
                            memberSpaceModel.setUserId(jsonObject1.getJSONObject("usersMaster").getString("userId"));
                            memberSpaceModel.setFullName(jsonObject1.getJSONObject("usersMaster").getString("fullName"));
                            memberSpaceModel.setContactNumber(jsonObject1.getJSONObject("usersMaster").getString("contactNumber"));
                            memberSpaceModel.setUserAccessToken(jsonObject1.getJSONObject("usersMaster").getString("userAccessToken"));
                            memberSpaceModel.setEmailId(jsonObject1.getJSONObject("usersMaster").getString("emailId"));
                            memberSpaceModel.setUserImageUrl(jsonObject1.getJSONObject("userImageUrl").getString("userImageUrl"));
                            memberSpaceModel.setStatus(jsonObject1.getString("status"));//Outer status

                            member_getterSetterArrayList.add(memberSpaceModel);


                        }

                        memberToSpaceGroupAdapter = new AddMemberToSpaceGroupAdapter(member_getterSetterArrayList,AddMemberTo_A_SpaceGroup.this);
                        recycler_view_Member.setAdapter(memberToSpaceGroupAdapter);

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

                        Log.e("AddMemberToaGrp", "FeedError"+error);
                    }
                }
        );
        // Adds the JSON array request "arrayreq" to the request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);
    }


    @Override
    public void setMemberData(ArrayList<String> memberData) {
        memberNumberArraylist = memberData;
        for (String val:memberNumberArraylist) {
            Log.d("AddMemToAGRP","memberNumberArraylist "+val);
        }


        Log.d("AddMemberToGrp_Menu","memberNumberArraylist_"+memberNumberArraylist.size());

        if (memberNumberArraylist.size() > 0)
        {
            item.setVisible(true);
        }else {


            item.setVisible(false);
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sharing_menu, menu);
        item = menu.findItem(R.id.shareOk);

        Log.d("AddMemberToGrp_Menu","memberNumberArraylist_"+memberNumberArraylist.size());

        if (memberNumberArraylist.size() > 0)
        {
            item.setVisible(true);
        }else {
            item.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.shareOk:

                if (memberNumberArraylist.size() > 0 ) {

                    for (String num: memberNumberArraylist) {

                        addMemberData(groupId,userAccessToken,num);

                    }

                }





                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }






    private void addMemberData(final String groupId,final String userAccessToken,final String mobileNo) {

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_addMember + "?groupId="+groupId+"&admin="+userAccessToken+"&mobileNo="+mobileNo;

        Log.d("AddMemberToAGRP","_URL "+url);

        String tag_string_req = "req_clients";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {

                Log.d("AddMemberToAGRP", "-->" + response1);
                //parsing Json
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response1);
                    String response = jsonObject.getString("response");
                    String status = jsonObject.getString("status");
                    Log.d("checkUserForAdminResp", "" + response);
                    Log.d("checkUserForAdminStatus", "" + status);

                    if (status.equals("success")){


                        Log.d("AddMemberToGRP","InIfPart");

                        String clientName = SkipClientDetailsScreen.clientName;
                        String clientImageURLPath = SkipClientDetailsScreen.clientImageURLPath;
                        String clientLogoURLPath = SkipClientDetailsScreen.clientLogoURLPath;
                        String appCid = SkipClientDetailsScreen.appCid;
                        String state = SkipClientDetailsScreen.state;

                        Log.d("AddMemToSpcGrp","RecievedVals__ "+clientImageURLPath+" "+clientName+" "+appCid+" "+state);

                        Intent intent = new Intent(AddMemberTo_A_SpaceGroup.this,SkipClientDetailsScreen.class);
                        intent.putExtra("clientName",clientName);
                        intent.putExtra("clientImageURLPath",clientImageURLPath);
                        intent.putExtra("clientLogoURLPath",clientLogoURLPath);
                        intent.putExtra("appCid",appCid);
                        intent.putExtra("state",state);
                        intent.putExtra("FRAGMENT_ID","1");
                        startActivity(intent);


                        Toast.makeText(AddMemberTo_A_SpaceGroup.this,"Member Added Successfully",Toast.LENGTH_SHORT).show();



                    }else {
                        Log.d("AddMemberToGRP","InElsePart");
                        Toast.makeText(AddMemberTo_A_SpaceGroup.this,"Member doesn't Added",Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage() == null || error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext(), R.style.MyDialogTheme).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Network/Connection Error");

                    // Setting Dialog Message
                    alertDialog.setMessage("Internet Connection is poor OR The Server is taking too long to respond.Please try again later.Thank you.");

                    // Setting Icon to Dialog
                    //  alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                    //Log.e(TAG, "Registration Error: " + error.getMessage());

                } else if (error instanceof AuthFailureError) {

                    //TODO
                } else if (error instanceof ServerError) {

                    //TODO
                } else if (error instanceof NetworkError) {

                    //TODO
                } else if (error instanceof ParseError) {

                    //TODO
                }


            }


        });

        AppController.getInstance().addToRequestQueue(request, tag_string_req);



    }


}
