package in.sabpaisa.droid.sabpaisa;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.SkipClientDetailsScreen;

/**
 * Created by SabPaisa on 03-07-2017.
 */

public class GroupsFragments extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    View rootView = null;
    private static final String TAG = GroupsFragments.class.getSimpleName();

    public static String clientId;

    String tag_string_req = "req_register";
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<GroupListData> groupArrayList;
    MainGroupAdapter1 mainGroupAdapter1;/*Globally Declared Adapter*/
    /*START Interface for getting data from activity*/
    GetDataInterface sGetDataInterface;


    /*END Interface for getting data from activity*/
    public GroupsFragments() {
        // Required empty public constructor
    }

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        *//*try {
            sGetDataInterface= (GetDataInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "must implement GetDataInterface Interface");
        }*//*
    }*/

   /* public void getDataFromActivity() {
        if(sGetDataInterface != null){
            this.groupArrayList = sGetDataInterface.getGroupDataList();
            //mainGroupAdapter1.setItems(this.groupArrayList);
            mainGroupAdapter1.notifyDataSetChanged();
        }
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragments_groups, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SkipClientDetailsScreen.MySharedPrefOnSkipClientDetailsScreenForAppCid, Context.MODE_PRIVATE);
        clientId=sharedPreferences.getString("clientId","abc");
        Log.d("clientId_GF",""+clientId);

        callGroupDataList(clientId);

        return rootView;
    }

    public void callGroupDataList(final String clientId ) {
        String urlJsonObj = AppConfig.Base_Url+AppConfig.App_api+"getParticularClientsGroups"+"?client_Id="+ clientId;
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                urlJsonObj, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                try {
                    groupArrayList = new ArrayList<GroupListData>();
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    String response1 = jsonObject.getString("response");

                    if (status.equals("success")&&response1.equals("No_Record_Found")) {

                        //Toast.makeText(getContext(),"No Result Found",Toast.LENGTH_SHORT).show();

                    }
                    else {
                        JSONArray jsonArray = jsonObject.getJSONArray("response");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            GroupListData groupListData = new GroupListData();
                            groupListData.setClientId(jsonObject1.getString("clientId"));
                            groupListData.setGroupId(jsonObject1.getString("groupId"));
                            groupListData.setGroupName(jsonObject1.getString("groupName"));
                            groupListData.setGroupText(jsonObject1.getString("groupText"));
                            groupListData.setCreatedDate(jsonObject1.getString("createdDate"));
                            groupListData.setImagePath(jsonObject1.getString("imagePath"));
                            groupListData.setLogoPath(jsonObject1.getString("logoPath"));
                            groupArrayList.add(groupListData);
                        }
                        Log.d("groupArrayList121211", " " + groupArrayList.get(0).getGroupId());
                       /*START listener for sending data to activity*/
                        OnFragmentInteractionListener listener = (OnFragmentInteractionListener) getActivity();
                        listener.onFragmentSetGroups(groupArrayList);
                            /*END listener for sending data to activity*/
                        //loadGroupListView(groupArrayList, (RecyclerView) rootView.findViewById(R.id.recycler_view_group));
                    }
                }
                    // Try and catch are included to handle any errors due to JSON
                catch(JSONException e){
                        // If an error occurs, this prints the error to the log
                        e.printStackTrace();
                        callGroupDataList(clientId);
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
                        callGroupDataList(clientId);
                        Log.e("Group fragments", "Group fragments Error");
                    }
                }
        );
        // Adds the JSON arra   y request "arrayreq" to the request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);
    }

    /*private void loadGroupListView(ArrayList<GroupListData> arrayList,final RecyclerView rv) {
        mainGroupAdapter1 = new MainGroupAdapter1(arrayList);

        rv.postDelayed(new Runnable() {
            @Override
            public void run() {
                rv.setAdapter(mainGroupAdapter1);

            }
        },2000);
        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), rv, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        *//*GroupListData groupListData = groupArrayList.get(position);
                        Intent intent = new Intent(((COA)getContext()), GroupDetails.class);
                        intent.putExtra("GroupId", groupListData.getGroupId());
                        intent.putExtra("GroupName", groupListData.getGroupName());
                        intent.putExtra("group_count",groupListData.getGroupCount());
                        intent.putExtra("GroupDescription", groupListData.getGroupDescription());
                        startActivity(intent);*//*
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        rv.setLayoutManager(llm);
    }*/

    /*START onRefresh() for SwipeRefreshLayout*/
    @Override
    public void onRefresh() {
        callGroupDataList(clientId);
    }

    public interface GetDataInterface {
        ArrayList<GroupListData> getGroupDataList();
    }
    /*END onRefresh() for SwipeRefreshLayout*/
}
