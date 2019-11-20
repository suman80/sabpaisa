package in.sabpaisa.droid.sabpaisa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import in.sabpaisa.droid.sabpaisa.Adapter.CustomTransactioReportAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.TodayTransactionAdapter;
import in.sabpaisa.droid.sabpaisa.Model.CustomTransactionReportgettersetter;
import in.sabpaisa.droid.sabpaisa.Model.TodayTransactiongettersetter;

public class TodayTransactionActivity extends AppCompatActivity {

    private RecyclerView custom_transaction_recycleView;
    private Button viewCustomReport;
    private androidx.appcompat.widget.Toolbar toolbar;
    private ArrayList<TodayTransactiongettersetter> customTransactiongettersetters;
    private TodayTransactionAdapter customTransactioReportAdapter;
    ProgressDialog progressDialog;
    private LinearLayout noDataFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_transaction_report);

        custom_transaction_recycleView=findViewById(R.id.custom_transaction);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        custom_transaction_recycleView.addItemDecoration(new SimpleDividerItemDecoration(this));
        custom_transaction_recycleView.setLayoutManager(llm);
        custom_transaction_recycleView.setNestedScrollingEnabled(false);


        progressDialog=new ProgressDialog(TodayTransactionActivity.this);
        noDataFound=findViewById(R.id.noDataFound);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Today transactions");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_action_previousback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        viewCustomReport=findViewById(R.id.viewCustomReport);

        setCustomTransactionReport();

    }

    private void setCustomTransactionReport()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        final String currentDateandTime = sdf.format(new Date());

        String url="https://sp2.sabpaisa.in/SabPaisaAdmin/REST/transaction/filterTransaction";
        String url_localhost="https://sp2.sabpaisa.in/SabPaisaRepository/trans/report/mobile"+"?"+"fromDate="+"2019-10-15   00:00:00"+"&"+"endDate="+"2019-10-15 23:55:45"+"&"+"clientCode="+"ABN";

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url_localhost,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();

                Log.d("transaction_list", "" + response);
                if(response.length()>0&&response!=null)
                {
                    customTransactiongettersetters = new ArrayList<>();

                    try {
                        JSONArray jsonArray=response.getJSONArray("transList");

                        if(jsonArray.length()>0&&jsonArray!=null)
                        {
                            Log.d("jsonArray",""+jsonArray);
                            for (int i = 0; i <jsonArray.length(); i++) {

                                Log.d("jsonArray_txn","fdjifjdkdjkjkd");


                                try {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    TodayTransactiongettersetter s = new TodayTransactiongettersetter();
                                    s.setTxnId(jsonObject.getString("txnId"));
                                    s.setAmount(jsonObject.getString("amount"));
                                    s.setTxnDate(jsonObject.getString("txnDate"));
                                    s.setTxnStatus(jsonObject.getString("status"));
                                    s.setPayerName(jsonObject.getString("payerName"));

                                    customTransactiongettersetters.add(s);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                customTransactioReportAdapter = new TodayTransactionAdapter(customTransactiongettersetters, getApplicationContext());
                                custom_transaction_recycleView.setAdapter(customTransactioReportAdapter);
                                customTransactioReportAdapter.notifyDataSetChanged();

                            }
                        }
                        else
                        {

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else

                {
                    noDataFound.setVisibility(View.VISIBLE);

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("volleyError",""+error);

            }
        })
       /* {
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

            }}*/;

        AppController.getInstance().addToRequestQueue(stringRequest);

    }


}
