package in.sabpaisa.droid.sabpaisa;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.MemberAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.MemberOfflineAdapter;
import in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments;
import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.Model.MemberOfflineDataModel;
import in.sabpaisa.droid.sabpaisa.Model.Member_GetterSetter;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;

import static in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments.TABLE_NAME_MEMBERS;


/**
 * A simple {@link Fragment} subclass.
 */
public class Members extends Fragment {
    private static final String TAG = Members.class.getSimpleName();
    View rootView;
    LinearLayout linearLayoutnoDataFound;
    public static String clientId;
    ArrayList<Member_GetterSetter> member_getterSetterArrayList;
    MemberAdapter memberAdapter;
    ShimmerRecyclerView recycler_view_Member;
    /*START Interface for getting data from activity*/
    GetDataInterface sGetDataInterface;
    /*START Interface for getting data from activity*/

    /////////Local Db//////////
    AppDbComments db;
    ArrayList<MemberOfflineDataModel> memberOfflineDataModelArrayList;

    public Members() {
        // Required empty public cons
        // tructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_members, container, false);
        linearLayoutnoDataFound = (LinearLayout)rootView.findViewById(R.id.noDataFound);
        recycler_view_Member = (ShimmerRecyclerView) rootView.findViewById(R.id.recycler_view_Member);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view_Member.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        recycler_view_Member.setLayoutManager(llm);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FullViewOfClientsProceed.MySharedPrefOnFullViewOfClientProceed, Context.MODE_PRIVATE);
        clientId=sharedPreferences.getString("clientId","abc");
        Log.d("clientId_MEMBERS",""+clientId);

        ///////////////////////DB/////////////////////////////////
        db = new AppDbComments(getContext());

        if (isOnline()) {
            Log.d("RAJ", "DEEP");
            memberData(clientId);
        } else {

            /////////////////////Retrive data from local DB///////////////////////////////

            Cursor res = db.getParticularMembersData(clientId);
            memberOfflineDataModelArrayList = new ArrayList<>();
            if (res.getCount() > 0) {
                StringBuffer stringBuffer = new StringBuffer();
                while (res.moveToNext()) {
                    stringBuffer.append(res.getString(0) + " ");
                    stringBuffer.append(res.getString(1) + " ");
                    stringBuffer.append(res.getString(2) + " ");
                    stringBuffer.append(res.getString(3) + " ");

                    MemberOfflineDataModel memberOfflineDataModel = new MemberOfflineDataModel();
                    memberOfflineDataModel.setFullName(res.getString(2));
                    memberOfflineDataModel.setUserImageUrl(res.getString(3));
                    memberOfflineDataModelArrayList.add(memberOfflineDataModel);

                }
                Log.d("getMemberData", "-->" + stringBuffer);

                MemberOfflineAdapter adapter = new MemberOfflineAdapter(memberOfflineDataModelArrayList,getContext());
                recycler_view_Member.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                Log.d("MemberLocalDb", "In Else Part");
                Toast.makeText(getContext(), "No Data Found !", Toast.LENGTH_SHORT).show();
            }


        }

        return rootView;
    }


    public void memberData (final String clientId)
    {

        boolean checkDb = db.isTableExists(TABLE_NAME_MEMBERS);

        Log.d("DbValueMember"," "+checkDb);

        if (checkDb == true){
            db.deleteAllMembersData();
        }

        String tag_string_req = "req_register";
        String url = AppConfig.Base_Url+AppConfig.App_api+AppConfig.URL_Show_Member+"clientId="+clientId;

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

                        linearLayoutnoDataFound.setVisibility(View.VISIBLE);
                        recycler_view_Member.setVisibility(View.GONE);

                    }else {
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


                            member_getterSetterArrayList.add(member_getterSetter);


                            /////////////////////Saving To Internal Storage/////////////////////////////////////////

                            final MemberOfflineDataModel memberOfflineDataModel = new MemberOfflineDataModel();
                            memberOfflineDataModel.setFullName(jsonObject1.getString("fullName"));

                            Glide.with(getContext())
                                    .load(member_getterSetter.getUserImageUrl())
                                    .asBitmap()
                                    .into(new SimpleTarget<Bitmap>(100, 100) {
                                        @Override
                                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                            Log.d("LogoBitmap", " " + resource);
                                            //saveLogoToInternalStorage(resource,groupListData.getGroupId());

                                            ContextWrapper cw = new ContextWrapper(getContext());
                                            // path to /data/data/yourapp/app_data/imageDir
                                            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                                            // Create imageDir
                                            File mypath = new File(directory, member_getterSetter.getUserId() + "memberImg.jpg");

                                            Log.d("mypath", "mypath  " + mypath);

                                            String imgPath = mypath.toString();

                                            FileOutputStream fos = null;
                                            try {
                                                fos = new FileOutputStream(mypath);
                                                // Use the compress method on the BitMap object to write image to the OutputStream
                                                resource.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            } finally {
                                                try {
                                                    fos.close();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            memberOfflineDataModel.setUserImageUrl(imgPath);

                                        }
                                    });

                            //////////////////////////////LOCAL DB//////////////////////////////////////

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Do something after 3000ms

                                    Log.d("imagePath_Member", "IntoLocalDb " + memberOfflineDataModel.getUserImageUrl());

                                    boolean isInserted = db.insertMembersData(clientId,memberOfflineDataModel);

                                    if (isInserted == true) {

                                        //Toast.makeText(AllTransactionSummary.this, "Data  Inserted", Toast.LENGTH_SHORT).show();

                                        Log.d("Members", "LocalDBInIfPart" + isInserted);

                                    } else {
                                        Log.d("Members", "LocalDBInElsePart" + isInserted);
                                        //Toast.makeText(AllTransactionSummary.this, "Data  Not Inserted", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }, 2000);



                        }
                        Log.d("ArrayListAfterParse", " " + member_getterSetterArrayList.get(0).getFullName());

                        /*START listener for sending data to activity*/
                        OnFragmentInteractionListener listener = (OnFragmentInteractionListener) getActivity();
                        listener.onFragmentSetMembers(member_getterSetterArrayList);
                            /*END listener for sending data to activity*/
                            memberAdapter = new MemberAdapter(member_getterSetterArrayList,getContext());
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
            this.member_getterSetterArrayList = sGetDataInterface.getMemberDataList();
            memberAdapter.setItems(this.member_getterSetterArrayList);
            memberAdapter.notifyDataSetChanged();
        }else {
            Log.d("PGF_I&A"," "+sGetDataInterface+"&"+member_getterSetterArrayList);
        }

        //Log.d("PGF_I&A"," "+sGetDataInterface+"&"+member_getterSetterArrayList);
    }




    public interface GetDataInterface {
        ArrayList<Member_GetterSetter> getMemberDataList();
    }

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
