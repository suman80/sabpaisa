package in.sabpaisa.droid.sabpaisa;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import in.sabpaisa.droid.sabpaisa.Adapter.AllTransactionAdapter;
import in.sabpaisa.droid.sabpaisa.Model.AllTransactiongettersetter;
import in.sabpaisa.droid.sabpaisa.Model.Member_GetterSetter;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;

public class AllTransactionSummary extends AppCompatActivity {
String token;
String id,clientName,spTranscationId,transcationDate,paidAmount;
AllTransactionAdapter allTransactionAdapter;
ShimmerRecyclerView recycler_view_Txn;
String date1;
    ArrayList<AllTransactiongettersetter> allTransactiongettersetters;
    LinearLayout linearLayoutnoDataFound;
    Toolbar toolbar;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_all_transaction_summary);


        back = (ImageView) findViewById(R.id.bbck);
       /* toolbar.setTitle("Profile");
        toolbar.setNavigationIcon(R.drawable.ic_action_previousback);
       */
       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               onBackPressed();
           }
       });


        // Inflate the layout for this fragment
        linearLayoutnoDataFound = (LinearLayout) findViewById(R.id.noDataFound);
        recycler_view_Txn = (ShimmerRecyclerView) findViewById(R.id.txbnh);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view_Txn.addItemDecoration(new SimpleDividerItemDecoration(this));
        recycler_view_Txn.setLayoutManager(llm);

        SharedPreferences sharedPreferences1 = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        String response = sharedPreferences1.getString("response", "123");

        if(response!=null) {

            token = response;
            Log.d("alltexnsummtoken", " " + token);

            Log.d("alltexnsummt", " " + response);
        }


        alltxnsummary(token);
    }

    public void alltxnsummary(final  String token)
    {

        String url="https://portal.sabpaisa.in/SabPaisaResponseHandler/SPtranscationIds?token=";
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url + token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)  {

                Log.d("xyvhjkk.nk",""+response);
                //progressBar.setVisibility(View.GONE);
                JSONObject object = null;
                try {

                    allTransactiongettersetters = new ArrayList<>();

                    object = new JSONObject(response.toString());


                    String status = object.getString("status");
                    String responsee = object.getString("response");

                    Log.d("txnstatussum",""+status);
                    Log.d("txnstatussum",""+responsee);

                    if(status.equals("failure"))
                    {

                        linearLayoutnoDataFound.setVisibility(View.VISIBLE);
                        recycler_view_Txn.setVisibility(View.GONE);

                    }
                    JSONArray jArray3 = object.getJSONArray("response");

                    Log.d("txnarraysum",""+jArray3);

                   /* JsonObject jsonObject;
                    jsonObject = new JsonObject(responsee);

                    for(int i = 0; i < jArray3 .length(); i++)

*/

                    for(int i = 0; i < jArray3.length(); i++)
                    {
                        JSONObject object3 = jArray3.getJSONObject(i);

                        AllTransactiongettersetter allTransactiongettersetter=new AllTransactiongettersetter();
                        allTransactiongettersetter.setClientName(object3.getString("clientName"));
                        allTransactiongettersetter.setId(object3.getString("id"));
                        allTransactiongettersetter.setPaidAmount(object3.getString("paidAmount"));
                        allTransactiongettersetter.setSpTranscationId(object3.getString("spTranscationId"));
                        allTransactiongettersetter.setPaymentStatus(object3.getString("paymentStatus"));
                        String datatime=  object3.getString("transcationDate");
                        String x=  object3.getString("paymentStatus");
                        Log.d("abcgshy","dnjk"+datatime);
                      Log.d("abcgshy","dnjk"+x);

                        allTransactiongettersetter.setTranscationDate(getDate(Long.parseLong(datatime)));


                        allTransactiongettersetters.add(allTransactiongettersetter);


                        /*id   = object3.getString("id");
                        paidAmount = object3.getString("paidAmount");
                        transcationDate  = object3.getString("transcationDate");
                        clientName = object3.getString("clientName");
                        spTranscationId = object3.getString("spTranscationId");
                        Log.d("uhnsdhkahk",""+id);
                        Log.d("uhnsdhkahk",""+paidAmount);
                        //Log.d("uhnsdhkahk",""+spTranscationId);
                        Log.d("uhnsdhkahk",""+spTranscationId);
                        Log.d("uhnsdhkahk",""+clientName);
                        Log.d("uhnsdhkahk",""+transcationDate);
               */     }
                    Log.d("ArrayListAfterParse", " " + allTransactiongettersetters.get(0).getSpTranscationId());



                } catch (JSONException e) {
                    e.printStackTrace();
                }

                allTransactionAdapter = new AllTransactionAdapter(allTransactiongettersetters,getApplicationContext());
                recycler_view_Txn.setAdapter(allTransactionAdapter);
                allTransactionAdapter.notifyDataSetChanged();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

AppController.getInstance().addToRequestQueue(stringRequest);
    }


    private String getDate(long time) {


        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd/MM/yyyy HH:mm", cal).toString();
        date1 = DateFormat.format("dd/MM/yyyy ", cal).toString();
        Log.d("date11",""+date1);
        return date;
    }

}
