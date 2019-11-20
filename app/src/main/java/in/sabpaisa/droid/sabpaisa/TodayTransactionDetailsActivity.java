package in.sabpaisa.droid.sabpaisa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import in.sabpaisa.droid.sabpaisa.Adapter.TodayTransactionAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.TodayTransactionsDetailsAdapter;
import in.sabpaisa.droid.sabpaisa.Model.TodayTransactiongettersetter;
import in.sabpaisa.droid.sabpaisa.Model.TodayTransactionlistgettersetter;

public class TodayTransactionDetailsActivity extends AppCompatActivity {
    private ShimmerRecyclerView recycler_view_Txn;
    private androidx.appcompat.widget.Toolbar toolbar;
    private ArrayList<TodayTransactionlistgettersetter> todayTransactiongettersetters;
    private TodayTransactionsDetailsAdapter todayTransactionAdapter;
    private LinearLayout noDataFound;
    private Button today_view_all_transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_transaction_details);
        recycler_view_Txn=findViewById(R.id.today_transaction_detail_recycleview);

        noDataFound=findViewById(R.id.noDataFound);
        today_view_all_transactions=findViewById(R.id.today_view_all_transactions);

        today_view_all_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),TodayTransactionActivity.class);
                startActivity(intent);
            }
        });

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Today transactions");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_action_previousback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view_Txn.addItemDecoration(new SimpleDividerItemDecoration(this));
        recycler_view_Txn.setLayoutManager(llm);
        getTodayTransactionReport();

    }


    private void getTodayTransactionReport()
    {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        final String currentDateandTime = sdf.format(new Date());

        String url="https://sp2.sabpaisa.in/SabPaisaAdmin/REST/transaction/filterTransaction";

        String url_localhost="https://sp2.sabpaisa.in/SabPaisaRepository/trans/report/mobile"+"?"+"fromDate="+"2019-10-15   00:00:00"+"&"+"endDate="+"2019-10-15 23:55:45"+"&"+"clientCode="+"ABN";

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url_localhost,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                todayTransactiongettersetters=new ArrayList<>();


                if(response.length()>0 &&response!=null) {
                  //  today_view_all_transactions.setVisibility(View.VISIBLE);

                    try {

                        TodayTransactionlistgettersetter todayTransactionlistgettersetter=new TodayTransactionlistgettersetter();
                        todayTransactionlistgettersetter.setClientName(response.getString("clientName"));
                        todayTransactionlistgettersetter.setNumberoftransactions(response.getString("noOfTransaction"));
                        String clientName=response.getString("clientName");
                        todayTransactiongettersetters.add(todayTransactionlistgettersetter);

                        Log.d("clientName",""+clientName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    todayTransactionAdapter = new TodayTransactionsDetailsAdapter(todayTransactiongettersetters, getApplicationContext());
                    recycler_view_Txn.setAdapter(todayTransactionAdapter);
                    todayTransactionAdapter.notifyDataSetChanged();

                }
                else
                {
                    noDataFound.setVisibility(View.VISIBLE);
                    recycler_view_Txn.setVisibility(View.GONE);
                }


                Log.d("json_objectllllllllll1", "" + response);

            }
        }


                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volleyError",""+error);

            }
        })
        /*{
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> obj=new HashMap<>();
                obj.put("fromDate",currentDateandTime);
                obj.put("endDate",currentDateandTime);
                obj.put("clientCode","ABN");

                Log.d("fromdate_new",""+obj);

                return super.getParams();
            }
        }
*/
        /*{
            @Override
            public byte[] getBody() {
                String body="{\n" +
                        "  \"paymentStatus\": \"\",\n" +
                        "  \"statusFlag\": false,\n" +
                        "  \"paymentMode\": \"\",\n" +
                        "  \"clientCode\": \"\",\n" +
                        "  \"clientCodeFlag\": false,\n" +
                        "  \"paymentModeFlag\": false,\n" +
                        "  \"fromDate\": \"2019-10-10   10:55:45\",\n" +
                        "  \"fromDateFlag\": true,\n" +
                        "  \"endDate\": \"2019-10-16   10:55:45\",\n" +
                        "  \"endDateFlag\": true\n" +
                        " \n" +
                        "}";

                Log.d("body_log",""+body);
                return body.getBytes();

            }
        }*/;

        AppController.getInstance().addToRequestQueue(stringRequest);

    }

}
