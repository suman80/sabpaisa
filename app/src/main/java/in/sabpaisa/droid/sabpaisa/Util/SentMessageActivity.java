package in.sabpaisa.droid.sabpaisa.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.sabpaisa.droid.sabpaisa.Adapter.AddMemberTo_A_GroupAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.ChatAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.MessageSentAdaper;
import in.sabpaisa.droid.sabpaisa.AddMemberTo_A_Group;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.Fragments.ClientFilterFragment;
import in.sabpaisa.droid.sabpaisa.Fragments.SkipFeedFragment;
import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.Model.Member_GetterSetter;
import in.sabpaisa.droid.sabpaisa.Model.SentMessage;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.SimpleDividerItemDecoration;

public class SentMessageActivity extends AppCompatActivity {

    RecyclerView recycler_view_sentmessage;
    public static String clientId;
    private int commentId;
    private RelativeLayout linlaHeaderProgress;

    MessageSentAdaper message_sent_adapterr;
    Toolbar toolbar;
    String status="Approved";

    ArrayList<SentMessage> sentMessageArraylist;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  CommonUtils.setFullScreen(this);
        setContentView(R.layout.sent_message_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        recycler_view_sentmessage = (RecyclerView)findViewById(R.id.recycler_message_sent);

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view_sentmessage.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        recycler_view_sentmessage.setLayoutManager(llm);
        recycler_view_sentmessage.setMotionEventSplittingEnabled(false);
        toolbar=findViewById(R.id.toolbar_sent);
        linlaHeaderProgress=findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.VISIBLE);

        toolbar.setTitle("User List");
        setSupportActionBar(toolbar);

        commentId=getIntent().getIntExtra("commentId",0);
        Log.d("comment_id_new",""+commentId);



        SharedPreferences sharedPreferences1 = getApplication().getSharedPreferences(ClientFilterFragment.MySharedPreffilter, Context.MODE_PRIVATE);
        String clientId = sharedPreferences1.getString("clientId", null);
        Log.d("clientId_MEMBERS12",""+clientId);

        SharedPreferences sharedPreferences =getApplicationContext().getSharedPreferences(SkipClientDetailsScreen.MySharedPrefOnSkipClientDetailsScreenForAppCid, Context.MODE_PRIVATE);
        String appCid=sharedPreferences.getString("appCid",null);
        Log.d("clientId_MEMBERS_app",""+appCid);

        sentMessageList(clientId,appCid,commentId,status);


        sentMessageArraylist = new ArrayList<>();


    }



    public void sentMessageList(final String clientId,final String appCid, final int commentId, final String approved)
    {

        String tag_string_req = "req_register";
        String url = AppConfig.Base_Url+AppConfig.App_api+"groupcommentInfo?commentId="+commentId+"&status="+approved;

        if(clientId != null){
            url = url + "&clientId="+clientId;
        }

        else if(appCid != null){
            url = url + "&appcid="+appCid;
        }
        Log.d("AddMemberToaGrp", "Member_URL: " + url);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>(){

            // Takes the response from the JSON request
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("response_message",""+response);
                    linlaHeaderProgress.setVisibility(View.GONE);

                    //swipeRefreshLayout.setRefreshing(false);
                    sentMessageArraylist = new ArrayList<>();

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    String response1 = jsonObject.getString("response");
                    Log.d("respose_message",""+response1);
                    JSONObject jObj = new JSONObject(response1);
                    String commentId=jObj.getString("seenBy");
                    Log.d("commentId_new",""+commentId);
                    JSONArray jsonArray = jObj.getJSONArray("seenBy");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        final SentMessage sentMessage = new SentMessage();
                        sentMessage.setUserName(jsonObject1.getString("fullName"));
                        Log.d("commentId_new1",""+jsonObject1.getString("fullName"));
                        sentMessage.setMobileNumber(jsonObject1.getString("phoneNumber"));
                        sentMessage.setUserImage(jsonObject1.getString("userImageUrl"));

                        sentMessageArraylist.add(sentMessage);

                    }
//                        Log.d("ArrayListAfterParse", " " + member_getterSetterArrayList.get(0).getFullName());
                    message_sent_adapterr = new MessageSentAdaper(sentMessageArraylist,SentMessageActivity.this);
                    recycler_view_sentmessage.setAdapter(message_sent_adapterr);



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
                        linlaHeaderProgress.setVisibility(View.GONE);

                        Log.e("AddMemberToaGrp", "FeedError"+error);
                    }
                }
        )
        { @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<>();
            params.put("clientId",clientId);
            params.put("commentId",String.valueOf(commentId));
            params.put("status",approved);
            Log.d("parameter",""+params);
            return params;

        }
        }

                ;
        // Adds the JSON array request "arrayreq" to the request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);
    }

}
