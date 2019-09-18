package in.sabpaisa.droid.sabpaisa.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

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

import in.sabpaisa.droid.sabpaisa.Adapter.SkipMemberAdapter;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.Model.MemberSpaceModel;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.SimpleDividerItemDecoration;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.SkipClientDetailsScreen;

import static in.sabpaisa.droid.sabpaisa.MainActivitySkip.SUPER_ADMIN_SHAREDFREF;

/**
 * A simple {@link Fragment} subclass.
 */
public class SkipMembersFragment extends Fragment {

    ArrayList<MemberSpaceModel> memberSpaceModelArrayList;
    String appCid;
    LinearLayout linearLayoutnoDataFound;
    ShimmerRecyclerView recycler_view_Member;
    MaterialRippleLayout rippleClickAdd;
    FrameLayout framelayoutAdd;

    LinearLayout linearLayoutAddMemberWhenNoData;

    SkipMemberAdapter skipMemberAdapter;

    //Values get from SkipClient details screen
    String clientName,clientLogoPath,clientImagePath,state;

    /*START Interface for getting data from activity*/
    GetDataInterface sGetDataInterface;
    /*START Interface for getting data from activity*/

    String roleValue;

    public SkipMembersFragment() {
        // Required empty public constructor
    }






    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clientName = getArguments().getString("clientName");
        clientLogoPath = getArguments().getString("clientLogoPath");
        clientImagePath = getArguments().getString("clientImagePath");
        state = getArguments().getString("state");

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SkipClientDetailsScreen.MySharedPrefOnSkipClientDetailsScreenForAppCid, Context.MODE_PRIVATE);
        appCid=sharedPreferences.getString("appCid","abc");
        Log.d("clientId_MEMBERS",""+appCid);

        Log.d("SkipMembersFrag","Recieved_Val_"+clientName+" "+clientLogoPath+" "+clientImagePath+" "+state+" "+appCid);

        SharedPreferences sharedPreferencesRole = getContext().getSharedPreferences(SUPER_ADMIN_SHAREDFREF, Context.MODE_PRIVATE);

        roleValue = sharedPreferencesRole.getString("ROLE_VALUE", "abc");

        Log.d("roleValueSkipMemFrag"," "+roleValue);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_skip_members, container, false);

        linearLayoutnoDataFound = (LinearLayout)view.findViewById(R.id.noDataFound);
        recycler_view_Member = (ShimmerRecyclerView) view.findViewById(R.id.recycler_view_Member);
        rippleClickAdd = (MaterialRippleLayout)view.findViewById(R.id.rippleClickAdd);

        framelayoutAdd = (FrameLayout)view.findViewById(R.id.framelayoutAdd);

        linearLayoutAddMemberWhenNoData = (LinearLayout)view.findViewById(R.id.linearLayoutAddMemberWhenNoData);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view_Member.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        recycler_view_Member.setLayoutManager(llm);
        recycler_view_Member.setMotionEventSplittingEnabled(false);

        rippleClickAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                AddMemberToSpaceDialogFragment1 addMemberToSpaceDialogFragment = new AddMemberToSpaceDialogFragment1();
                addMemberToSpaceDialogFragment.show(fragmentManager, "AddMemberDialogFragment");

                Bundle bundle = new Bundle();
                bundle.putString("appCid",appCid);
                bundle.putString("clientName",clientName);
                bundle.putString("clientLogoPath",clientLogoPath);
                bundle.putString("clientImagePath",clientImagePath);
                bundle.putString("state",state);
                addMemberToSpaceDialogFragment.setArguments(bundle);




            }
        });


        if (roleValue.equals("1")){
            framelayoutAdd.setVisibility(View.VISIBLE);
        }


        if (isOnline()) {
            loadMemberData(appCid);
        } else {
            //Todo offline
            Toast.makeText(getContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
        }


        return view;
    }


    public void loadMemberData (final String appCid)
    {


        String tag_string_req = "req_register";
        String url = AppConfig.Base_Url+AppConfig.App_api+AppConfig.URL_membersOfSPappclient+"?appcid="+appCid;

        Log.d("SkipMemberFragment","url___"+url);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>(){

            // Takes the response from the JSON request
            @Override
            public void onResponse(String response) {
                try {

                    Log.d("SMCA", "Member: " + response);
                    //swipeRefreshLayout.setRefreshing(false);
                    memberSpaceModelArrayList = new ArrayList<>();

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    String response1 = jsonObject.getString("response");

                    if (status.equals("failure")) {

                        Log.d("SMF", "InFail: "+response1);



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
                            if (jsonObject1.has("userImageUrl") && !jsonObject1.isNull("userImageUrl")) {
                                memberSpaceModel.setUserImageUrl(jsonObject1.getJSONObject("userImageUrl").getString("userImageUrl"));
                            }
                            memberSpaceModel.setStatus(jsonObject1.getString("status"));//Outer status
                            memberSpaceModelArrayList.add(memberSpaceModel);

                        }

                        Log.d("SMF_size___","memberSpaceModelArrayList___"+memberSpaceModelArrayList.size());



                            /*START listener for sending data to activity*/
                        if (memberSpaceModelArrayList !=null && !memberSpaceModelArrayList.isEmpty()) {
                            Log.d("SMF_1","memberSpaceModelArrayList___"+memberSpaceModelArrayList.size());

                            OnFragmentInteractionListener listener = (OnFragmentInteractionListener) getActivity();
                            listener.onFragmentSetMembersSpace(memberSpaceModelArrayList);
                            /*END listener for sending data to activity*/
                        }


                        skipMemberAdapter = new SkipMemberAdapter(memberSpaceModelArrayList,getContext());
                        recycler_view_Member.setAdapter(skipMemberAdapter);



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

                        Log.e("SMF", "onErrorResponse"+error);
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
            this.memberSpaceModelArrayList = sGetDataInterface.getMemberDataList();

                skipMemberAdapter.setItems(this.memberSpaceModelArrayList);
                skipMemberAdapter.notifyDataSetChanged();

            Log.d("SMF_I&A_InIf"," "+sGetDataInterface+"&"+memberSpaceModelArrayList.size());
        }else {
            Log.d("SMF_I&A_InElse"," "+sGetDataInterface+"&"+memberSpaceModelArrayList.size());
        }

    }


    public interface GetDataInterface {
        ArrayList<MemberSpaceModel> getMemberDataList();
    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v("SMF", "Internet Connection Not Present");
            return false;
        }
    }


}
