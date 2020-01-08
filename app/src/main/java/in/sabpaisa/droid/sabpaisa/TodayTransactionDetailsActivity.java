package in.sabpaisa.droid.sabpaisa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import in.sabpaisa.droid.sabpaisa.Adapter.TodayTransactionsDetailsAdapter;
import in.sabpaisa.droid.sabpaisa.Model.TodayTransactiongettersetter;
import in.sabpaisa.droid.sabpaisa.Model.TodayTransactionlistgettersetter;

public class TodayTransactionDetailsActivity extends AppCompatActivity  implements  AdapterView.OnItemSelectedListener {
    private ShimmerRecyclerView recycler_view_Txn;
    private androidx.appcompat.widget.Toolbar toolbar;
    private List<TodayTransactionlistgettersetter> clientDetails;
    private TodayTransactionsDetailsAdapter todayTransactionAdapter;
    private LinearLayout noDataFound;
    private Button today_view_all_transactions;
    private Parcelable parcelable;
    private RelativeLayout total_transaction,client_list;
    private List<TodayTransactiongettersetter> transList=new ArrayList<>();
    int sum=0;
    private TextView today_transaction;
    private Spinner spinner;
    private Calendar cal;
    private Calendar cal1;
    private  ProgressDialog progressDialog;
    String getNumberoftransactions;
    private String strFromDate=null,strEndDate=null;
    private  LocalDate today;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_transaction_details);
        spinner = findViewById(R.id.spinner);


        List<String> categories = new ArrayList<String>();
        categories.add("Today");
        categories.add("Yesterday");
        categories.add("Last 7 Days");
        categories.add("Current Month");
        categories.add("Last Month");


        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);


        recycler_view_Txn = findViewById(R.id.today_transaction_detail_recycleview);
        noDataFound = findViewById(R.id.noDataFound);
        today_transaction = findViewById(R.id.today_transaction);
        today_view_all_transactions = findViewById(R.id.today_view_all_transactions);


        today_view_all_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TodayTransactionActivity.class);
                startActivity(intent);
            }
        });

        total_transaction = findViewById(R.id.total_transaction);
        client_list = findViewById(R.id.client_list);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Successful Transactions Summary");
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
        //recycler_view_Txn.addItemDecoration(new SimpleDividerItemDecoration(this));
        recycler_view_Txn.setLayoutManager(llm);
        spinner.setOnItemSelectedListener(this);
        cal = Calendar.getInstance();
        cal1 = Calendar.getInstance();

        Calendar aCalendar = Calendar.getInstance();
        // add -1 month to current month
        aCalendar.add(Calendar.MONTH, -1);
        // set DATE to 1, so first date of previous month
        aCalendar.set(Calendar.DATE, 1);

        aCalendar.set(Calendar.DATE,     aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        //read it
        Date lastDateOfPreviousMonth = aCalendar.getTime();

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String date1 = format1.format(lastDateOfPreviousMonth);

        Log.d("lastDateOfPreviousMonth",""+date1);

    }


    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

        if(position==0)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            strFromDate = sdf.format(new Date());
            strEndDate=sdf.format(new Date());
            getTodayTransactionReport(strFromDate,strEndDate);

        }

       else if(position==1)
        {
            Date mydate = new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String yestr = dateFormat.format(mydate);
            getTodayTransactionReport(yestr,yestr);

           /* DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
           // Log.d("today",dateFormat.format(cal.getTime()));


            cal.add(Calendar.DATE, -1);

            strFromDate=dateFormat.format(cal.getTime());
            strEndDate=dateFormat.format(cal.getTime());

            Log.d("yesterday",dateFormat.format(cal.getTime()));
            getTodayTransactionReport(strFromDate,strEndDate);
*/
        }

        else if(position==2)
        {

            Date mydate1 = new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24));
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

            String yestr = dateFormat1.format(mydate1);

            Date mydate2 = new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 168));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String yestr2 = dateFormat.format(mydate2);

            getTodayTransactionReport(yestr2,yestr);

        }
        else if(position==3)
        {

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, 0);
            cal.set(Calendar.DATE, 1);
            Date firstDateOfPreviousMonth = cal.getTime();

            Date lastDateOfPreviousMonth = cal.getTime();

            cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE)); // changed calendar to cal

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            strFromDate= format1.format(firstDateOfPreviousMonth);


            //end date

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            strEndDate=sdf.format(new Date());

            getTodayTransactionReport(strFromDate,strEndDate);


        }

        else if(position==4)
        {

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            cal.set(Calendar.DATE, 1);
            Date firstDateOfPreviousMonth = cal.getTime();

            Date lastDateOfPreviousMonth = cal.getTime();

            cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE)); // changed calendar to cal

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            strFromDate= format1.format(lastDateOfPreviousMonth);
            Log.d("previous_strat",""+strFromDate);

            //Last date
            Calendar aCalendar = Calendar.getInstance();
            // add -1 month to current month
            aCalendar.add(Calendar.MONTH, -1);
            // set DATE to 1, so first date of previous month
            aCalendar.set(Calendar.DATE, 1);

            aCalendar.set(Calendar.DATE,     aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            //read it
            Date lastDateOfPreviousMonth1 = aCalendar.getTime();

            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
            strEndDate= format2.format(lastDateOfPreviousMonth1);


            getTodayTransactionReport(strFromDate,strEndDate);


        }


       /* else if(position==2)
        {
            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Log.d("today",dateFormat1.format(cal.getTime()));
            Calendar cal1= Calendar.getInstance();
            cal1.add(Calendar.DATE, -1);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Log.d("today",dateFormat.format(cal.getTime()));

            cal.add(Calendar.DATE, -7);
            Log.d("yesterday_7",dateFormat.format(cal.getTime())+dateFormat.format(cal1.getTime()));

            getTodayTransactionReport(dateFormat.format(cal.getTime()),dateFormat1.format(cal1.getTime()));

        }
        else
        {

        }*/

        Log.d("position",""+position);
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }



    private void getTodayTransactionReport(final String currentDateandTime,final String yesterdayDate)
    {

        progressDialog =new ProgressDialog(TodayTransactionDetailsActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        String url="https://securepay.sabpaisa.in/SabPaisaRepository/trans/report/mobile";

        String url_localhost="https://sp2.sabpaisa.in/SabPaisaRepository/trans/report/mobile";

        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.POST, url,null,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                clientDetails=new ArrayList<TodayTransactionlistgettersetter>();

                strEndDate=null;
                strFromDate=null;

                cal.add(Calendar.DATE, 0);
                cal1.add(Calendar.DATE, 0);

                JSONObject jsonObject=null;
                transList=new ArrayList<>();

                client_list.setVisibility(View.VISIBLE);
                total_transaction.setVisibility(View.VISIBLE);
                today_transaction.setVisibility(View.GONE);
                progressDialog.dismiss();

                Log.d("json_object", "" + response);

                if(response.length()>0 &&response!=null) {
                   //today_view_all_transactions.setVisibility(View.VISIBLE);
                    Log.d("json_object_new", "" + response);


                    for(int i=0;i<response.length();i++)
                    {
                        try {
                            jsonObject = response.getJSONObject(i);
                            TodayTransactionlistgettersetter clientDetail = new TodayTransactionlistgettersetter();
                            clientDetail.setClientName(jsonObject.getString("clientName"));
                            clientDetail.setNumberoftransactions(jsonObject.getString("noOfTransaction"));
                            String transaction1 = jsonObject.getString("noOfTransaction");
                            clientDetails.add(clientDetail);

                            Log.d("transaction1", "" + transaction1);
                            getNumberoftransactions  =clientDetail.getNumberoftransactions();

                            int k= Integer.parseInt(getNumberoftransactions);
                            sum = sum +k;

                            TextView totaltransactions=findViewById(R.id.totaltransactions);
                            totaltransactions.setText(String.valueOf(sum));

                            Log.d("getNumberoftransactions",""+getNumberoftransactions);

                            Log.d("total_sum",""+sum);

                            ArrayList<TodayTransactiongettersetter> clientTransList=new ArrayList<>();
                     /*       todayTransactiongettersetters.add(todayTransactionlistgettersetter);*/

                            if(jsonObject.getString("transList").length()>0&&jsonObject.getString("transList")!=null)
                            {
                                JSONArray jsonArray=jsonObject.getJSONArray("transList");

                                Log.d("translIst",""+jsonArray);

                                for(int j=0;j<jsonArray.length();j++)
                                {

                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    TodayTransactiongettersetter s = new TodayTransactiongettersetter(jsonObject1.getString("amount"),jsonObject1.getString("txnId"),jsonObject1.getString("txnDate"),jsonObject1.getString("status"),jsonObject1.getString("payerName"));
                                    /*s.setTxnId(jsonObject1.getString("txnId"));
                                    s.setAmount(jsonObject1.getString("amount"));
                                    s.setTxnDate(jsonObject1.getString("txnDate"));
                                    s.setTxnStatus(jsonObject1.getString("status"));
                                    s.setPayerName(jsonObject1.getString("payerName"));*/
                                    clientTransList.add(s);
                                 //   Log.d("jsonamount",""+car.getAmount());
                                    ///todayTransactionlistgettersetter.setTodayTransactiongettersetters(todayTransactiongettersetters1);
                                }

                                clientDetail.setTodayTransactiongettersetters(clientTransList);

                                clientDetails.add(clientDetail);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    sum=0;
                    jsonObject=null;


                 /*   clientDetails.forEach(client->{
                        Log.d("client object",""+client);
                    });*/


                    todayTransactionAdapter = new TodayTransactionsDetailsAdapter(clientDetails, getApplicationContext());
                    recycler_view_Txn.setAdapter(todayTransactionAdapter);
                    todayTransactionAdapter.notifyDataSetChanged();


                }
                else
                {
                    noDataFound.setVisibility(View.VISIBLE);
                    recycler_view_Txn.setVisibility(View.GONE);
                    client_list.setVisibility(View.GONE);
                    total_transaction.setVisibility(View.GONE);
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

       /* {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> obj=new HashMap<>();
                obj.put("fromDate",currentDateandTime);
                obj.put("endDate",currentDateandTime);

                Log.d("fromdate_new",""+obj);

                return obj;
            }
        }
*/

       // ROM = "\"" + ROM + "\"";



        {
            @Override
            public byte[] getBody() {

                String body ="{"+ "\"fromDate\":"+ "\""+ currentDateandTime + "\"" +","+ "\"endDate\":"+ "\""+yesterdayDate +"\" " +"}";

               /* String body="{\n" +

                " \"fromDate\": "+currentDateandTime+" ,\n  " +
                        " \"endDate\":"+currentDateandTime+"\n" +
                        "}";*/
                Log.d("body_log",""+body);
                return body.getBytes();

            }
        }  ;

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(stringRequest);

    }




}
