package in.sabpaisa.droid.sabpaisa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import in.sabpaisa.droid.sabpaisa.Adapter.CustomTransactionDetailAdapter;
import in.sabpaisa.droid.sabpaisa.Model.CustomTransactionReportgettersetter;
import in.sabpaisa.droid.sabpaisa.Model.CustomTransactionlistgettersetter;

public class MyBusinessActivity extends AppCompatActivity {
    private Button todayReport,customReport;
    androidx.appcompat.widget.Toolbar toolbar;
    private ImageView down_arrow,up_arrow;
    private LinearLayout todayClient_layout;
    private TextView today_transaction,datewisetransactio;
    private CardView paymenttransaction;
    private TextView today_transaction_forward;
    private EditText enterfourdigit;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_business);
        todayReport=findViewById(R.id.today_report);
        customReport=findViewById(R.id.custom_report);
        down_arrow=findViewById(R.id.down_arrow);
        paymenttransaction=findViewById(R.id.paymenttransaction);
        today_transaction_forward=findViewById(R.id.today_transaction_forward);

      /*  paymenttransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todayClient_layout.setVisibility(View.VISIBLE);

            }
        });
        */
        progressDialog=new ProgressDialog(MyBusinessActivity.this);

        enterfourdigit=findViewById(R.id.enterfourdigit);
        up_arrow=findViewById(R.id.up_arrow);
        todayClient_layout=findViewById(R.id.todayClient_layout);
        today_transaction=findViewById(R.id.today_transaction_layout);
        datewisetransactio=findViewById(R.id.datewise_transaction_layout);

        today_transaction_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(enterfourdigit.getText().length()==1)
                {
                    Toast.makeText(getApplicationContext(),"Please enter 4-digit number",Toast.LENGTH_SHORT).show();

                }
                else if(enterfourdigit.getText().length()==2)
                {
                    Toast.makeText(getApplicationContext(),"Please enter 4-digit number",Toast.LENGTH_SHORT).show();

                }

                else if(enterfourdigit.getText().length()==3)
                {
                    Toast.makeText(getApplicationContext(),"Please enter 4-digit number",Toast.LENGTH_SHORT).show();

                }
                else if(enterfourdigit.getText().length()==4)
                {
                    getTodayTransactionReport(enterfourdigit.getText().toString());
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please enter 4-digit number",Toast.LENGTH_SHORT).show();
                }

            }
        });

        datewisetransactio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(getApplicationContext(),CustomTransactionDetailActivity.class);
                startActivity(intent);

            }
        });




        down_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todayClient_layout.setVisibility(View.VISIBLE);
                //up_arrow.setVisibility(View.VISIBLE);
                down_arrow.setVisibility(View.GONE);

            }
        });


        up_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todayClient_layout.setVisibility(View.GONE);
                up_arrow.setVisibility(View.GONE);
                down_arrow.setVisibility(View.VISIBLE);

            }
        });

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Monitor Paisa");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_action_previousback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });




        todayReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent=new Intent(getApplicationContext(),TodayTransactionDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);*/

            }
        });

        customReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),CustomTransactionReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
    }

    private void getTodayTransactionReport(String code )
    {

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

         String url_localhost="https://securepay.sabpaisa.in//SabPaisaRepository/verifyCode"+"?"+"code="+code;

         JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url_localhost,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("json_objectllllllllll1", "" + response);

                try {
                    String verify=response.getString("verify");
                    Log.d("json_", "" + verify);

                    if(verify.equalsIgnoreCase("true"))
                    {
                        progressDialog.dismiss();
                        Intent intent=new Intent(getApplicationContext(),TodayTransactionDetailsActivity.class);
                        startActivity(intent);
                    }
                    else
                    {/*
                        Intent intent=new Intent(getApplicationContext(),TodayTransactionDetailsActivity.class);
                        startActivity(intent);*/

                        progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(),"You are not authorised person",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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
*/;
        AppController.getInstance().addToRequestQueue(stringRequest);

    }

}
