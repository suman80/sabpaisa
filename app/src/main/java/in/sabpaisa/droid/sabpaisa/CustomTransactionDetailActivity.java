package in.sabpaisa.droid.sabpaisa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import in.sabpaisa.droid.sabpaisa.Adapter.CustomTransactionDetailAdapter;
import in.sabpaisa.droid.sabpaisa.Model.CustomTransactionlistgettersetter;

public class CustomTransactionDetailActivity extends AppCompatActivity  {
    private ShimmerRecyclerView recycler_view_Txn;
    private androidx.appcompat.widget.Toolbar toolbar;
    private ArrayList<CustomTransactionlistgettersetter> todayTransactiongettersetters;
    private CustomTransactionDetailAdapter todayTransactionAdapter;
    private LinearLayout noDataFound;
    private  ImageView img_FromDate,img_ToDate;
    private EditText fromDateEditText,toDateEditText;
    private  long fromDateTimeStamp ,toDateTimeStamp ;
    private Button viewCustomReport;
    private ProgressDialog progressDialog;
    private Button show_all_transactions;
    private LinearLayout linear_layout;
    private FrameLayout startDate,endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_transaction_detail);
        progressDialog=new ProgressDialog(CustomTransactionDetailActivity.this);
        linear_layout=findViewById(R.id.linear_layout);
        show_all_transactions=findViewById(R.id.show_all_transactions);

        show_all_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),CustomTransactionReportActivity.class);
                startActivity(intent);
            }
        });


        recycler_view_Txn=findViewById(R.id.custom_transaction_detail);

        img_FromDate = (ImageView)findViewById(R.id.img_FromDate1);
        img_ToDate = (ImageView)findViewById(R.id.img_ToDate1);
        viewCustomReport=findViewById(R.id.viewCustomReport);

       // todayTransactionAdapter.setClickListener(this);



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Datewise transactions");
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

        viewCustomReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fromDateEditText.getText().length()<=0)
                {
                    Toast.makeText(getApplicationContext(),"Please select From date",Toast.LENGTH_LONG).show();

                }

                else if(toDateEditText.getText().length()<=0)
                {
                    Toast.makeText(getApplicationContext(),"Please select To date",Toast.LENGTH_LONG).show();

                }
                else
                {
                    progressDialog.setMessage("please wait......");
                    progressDialog.show();
                    getTodayTransactionReport(fromDateEditText.getText().toString(),toDateEditText.getText().toString(),"123");

                }

            }
        });
        fromDateEditText = (EditText)findViewById(R.id.fromDateEditText1);
        toDateEditText = (EditText)findViewById(R.id.toDateEditText1);
        fromDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);


                Log.d("RESOLVE ", day+" DAY "+ month+" MONTH "+year + " Year");
                Log.d("RESOLVE ", " MONTH BY SYTEM "+ calendar.get(Calendar.MONTH));

                DatePickerDialog datePickerDialog = new DatePickerDialog(CustomTransactionDetailActivity.this, com.example.rajdeeps.sabpaisatimecaptureapp.R.style.DatePicker ,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Log.d("onDateSet"," "+dayOfMonth+"/"+month+"/"+year);

                        fromDateEditText.setText(dayOfMonth+"/"+(month+1)+"/"+year);

                        Date date = new Date((month+1)+"/"+dayOfMonth+"/"+year);

                        fromDateTimeStamp = date.getTime();

                        Log.d("DATE ", date.getTime()+"  "+date);
//                        Timestamp ts=new Timestamp(date.getTime());

                    }
                },year,month,day);

                datePickerDialog.show();



            }
        });


        img_FromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);


                Log.d("RESOLVE ", day+" DAY "+ month+" MONTH "+year + " Year");
                Log.d("RESOLVE ", " MONTH BY SYTEM "+ calendar.get(Calendar.MONTH));

                DatePickerDialog datePickerDialog = new DatePickerDialog(CustomTransactionDetailActivity.this, com.example.rajdeeps.sabpaisatimecaptureapp.R.style.DatePicker ,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Log.d("onDateSet"," "+dayOfMonth+"/"+month+"/"+year);

                        fromDateEditText.setText(dayOfMonth+"/"+(month+1)+"/"+year);

                        Date date = new Date((month+1)+"/"+dayOfMonth+"/"+year);

                        fromDateTimeStamp = date.getTime();

                        Log.d("DATE ", date.getTime()+"  "+date);
//                        Timestamp ts=new Timestamp(date.getTime());

                    }
                },year,month,day);

                datePickerDialog.show();




            }
        });


        toDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                Log.d("RESOLVE ", day+" DAY "+ month+" MONTH "+year + " Year");
                Log.d("RESOLVE ", " MONTH BY SYTEM "+ calendar.get(Calendar.MONTH));

                DatePickerDialog datePickerDialog = new DatePickerDialog(CustomTransactionDetailActivity.this, com.example.rajdeeps.sabpaisatimecaptureapp.R.style.DatePicker ,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Log.d("onDateSet"," "+dayOfMonth+"/"+month+"/"+year);

                        toDateEditText.setText(dayOfMonth+"/"+(month+1)+"/"+year);

                        Date date = new Date((month+1)+"/"+dayOfMonth+"/"+year);

                        toDateTimeStamp = date.getTime();

                        Log.d("DATE ", date.getTime()+"  "+date);

                    }
                },year,month,day);

                datePickerDialog.show();


            }
        });


        img_ToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                Log.d("RESOLVE ", day+" DAY "+ month+" MONTH "+year + " Year");
                Log.d("RESOLVE ", " MONTH BY SYTEM "+ calendar.get(Calendar.MONTH));

                DatePickerDialog datePickerDialog = new DatePickerDialog(CustomTransactionDetailActivity.this, com.example.rajdeeps.sabpaisatimecaptureapp.R.style.DatePicker ,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Log.d("onDateSet"," "+dayOfMonth+"/"+month+"/"+year);

                        toDateEditText.setText(dayOfMonth+"/"+(month+1)+"/"+year);

                        Date date = new Date((month+1)+"/"+dayOfMonth+"/"+year);

                        toDateTimeStamp = date.getTime();

                        Log.d("DATE ", date.getTime()+"  "+date);

                    }
                },year,month,day);

                datePickerDialog.show();


            }
        });
    }

    private void getTodayTransactionReport(final String fromDate,final String toDate,final  String clientCode)
    {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        final String currentDateandTime = sdf.format(new Date());

        String url="https://sp2.sabpaisa.in/SabPaisaAdmin/REST/transaction/filterTransaction";

        String url_localhost="https://sp2.sabpaisa.in/SabPaisaRepository/trans/report/mobile";

        JSONArray stringRequest = new JSONArray(Request.Method.POST, url_localhost,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.dismiss();
                todayTransactiongettersetters=new ArrayList<>();
                recycler_view_Txn.setVisibility(View.VISIBLE);
                linear_layout.setVisibility(View.VISIBLE);
                // show_all_transactions.setVisibility(View.VISIBLE);

                if(response.length()>0 &&response!=null) {

                    for(int i=0;i<response.length();i++)
                    {
                        JSONObject jsonObject= null;
                        try {
                            jsonObject = response.getJSONObject(i);
                            CustomTransactionlistgettersetter todayTransactionlistgettersetter=new CustomTransactionlistgettersetter();
                            todayTransactionlistgettersetter.setClientName(jsonObject.getString("clientName"));
                            todayTransactionlistgettersetter.setNooftransactions(jsonObject.getString("noOfTransaction"));
                            String clientName=jsonObject.getString("clientName");
                            todayTransactiongettersetters.add(todayTransactionlistgettersetter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    try {

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    todayTransactionAdapter = new CustomTransactionDetailAdapter(todayTransactiongettersetters, getApplicationContext());
                    recycler_view_Txn.setAdapter(todayTransactionAdapter);
                    todayTransactionAdapter.notifyDataSetChanged();


                }
                else
                {
                    noDataFound.setVisibility(View.VISIBLE);
                    recycler_view_Txn.setVisibility(View.GONE);
                }


                Log.d("custom_report", "" + response);

            }
        }


                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                Log.d("volleyError",""+error);

            }
        })

        {
            @Override
            public byte[] getBody() {
                String body="{\n" +
                        " \"clientCodeList\":[\"ABN\",\"SSNC2\"],\n" +
                        " \"fromDate\": \"2019-10-10 00:00:00\",\n" +
                        " \"endDate\":\"2019-10-10 16:01:52\"\n" +
                        "}";

                Log.d("body_log",""+body);
                return body.getBytes();

            }
        }

        ;

        AppController.getInstance().addToRequestQueue(stringRequest);

    }

   /* @Override
    public void onClick(View view, int position) {
        Intent intent=new Intent(getApplicationContext(),CustomTransactionReportActivity.class);
        startActivity(intent);

    }*/
}
