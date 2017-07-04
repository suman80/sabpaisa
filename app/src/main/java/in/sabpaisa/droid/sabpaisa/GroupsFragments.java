package in.sabpaisa.droid.sabpaisa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;

/**
 * Created by SabPaisa on 03-07-2017.
 */

public class GroupsFragments extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    View rootView = null;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<GroupListData> groupArrayList = new ArrayList<GroupListData>();
    MainGroupAdapter1 ca;/*Globally Declared Adapter*/
    /*START Interface for getting data from activity*/
    GetDataInterface sGetDataInterface;


    /*END Interface for getting data from activity*/
    public GroupsFragments() {
        // Required empty public constructor
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
            this.groupArrayList = sGetDataInterface.getGroupDataList();
            ca.setItems(this.groupArrayList);
            ca.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragments_groups, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);

        callGroupDataList();

        return rootView;
    }

    public void callGroupDataList() {
        String urlJsonObj = AppConfiguration.MAIN_URL + "/getGroupsBasedonOrg/SRS";
        urlJsonObj = urlJsonObj.trim().replace(" ", "%20");
        // Creating the JsonArrayRequest class called arrayreq, passing the required parameters
        //JsonURL is the URL to be fetched from
        JsonArrayRequest arrayreq = new JsonArrayRequest(urlJsonObj,
                // The second parameter Listener overrides the method onResponse() and passes
                //JSONArray as a parameter
                new Response.Listener<JSONArray>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            swipeRefreshLayout.setRefreshing(false);
                            groupArrayList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject colorObj = response.getJSONObject(i);
//                              JSONObject colorObjBean = colorObj.getJSONObject("grpBean");
                                GroupListData groupData = new GroupListData();
                                groupData.setGroupId(colorObj.getString("groupId"));
                                groupData.setGroupName(colorObj.getString("groupName"));
                                groupData.setGroupDescription(colorObj.getString("groupDescription"));
                                groupData.setGroupCount(colorObj.getString("group_count"));
                                //groupData.setJoin(colorObj.getString("join"));
                                //groupData.setGroupCount(colorObj.getString("group_count"));
                                groupArrayList.add(groupData);

                            }
                            /*START listener for sending data to activity*/
                            OnFragmentInteractionListener listener = (OnFragmentInteractionListener) getActivity();
                            listener.onFragmentSetGroups(groupArrayList);
                            /*END listener for sending data to activity*/
                            loadGroupListView(groupArrayList, (RecyclerView) rootView.findViewById(R.id.recycler_view_group));
                        }
                        // Try and catch are included to handle any errors due to JSON
                        catch (JSONException e) {
                            // If an error occurs, this prints the error to the log
                            e.printStackTrace();
                            callGroupDataList();
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
                        callGroupDataList();
                        Log.e("Group fragments", "Group fragments Error");
                    }
                }
        );
        // Adds the JSON arra   y request "arrayreq" to the request queue
        AppController.getInstance().addToRequestQueue(arrayreq);
    }

    private void loadGroupListView(ArrayList<GroupListData> arrayList, RecyclerView rv) {
        ca = new MainGroupAdapter1(arrayList);
        rv.setAdapter(ca);
        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), rv, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        GroupListData groupListData = groupArrayList.get(position);
                        Intent intent = new Intent(((COA)getContext()), GroupDetails.class);
                        intent.putExtra("GroupId", groupListData.getGroupId());
                        intent.putExtra("GroupName", groupListData.getGroupName());
                        intent.putExtra("group_count",groupListData.getGroupCount());
                        intent.putExtra("GroupDescription", groupListData.getGroupDescription());

                        startActivity(intent);
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
    }

    /*START onRefresh() for SwipeRefreshLayout*/
    @Override
    public void onRefresh() {
        callGroupDataList();
    }

    public interface GetDataInterface {
        ArrayList<GroupListData> getGroupDataList();
    }
    /*END onRefresh() for SwipeRefreshLayout*/
}
