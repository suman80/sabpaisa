package in.sabpaisa.droid.sabpaisa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.media.Image;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
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
import in.sabpaisa.droid.sabpaisa.AppDB.AppDB;
import in.sabpaisa.droid.sabpaisa.Model.AllTransactiongettersetter;
import in.sabpaisa.droid.sabpaisa.Model.Member_GetterSetter;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;
import in.sabpaisa.droid.sabpaisa.Util.ProfileNavigationActivity;

public class AllTransactionSummary extends AppCompatActivity {
    String token;
    String id, clientName, spTranscationId, transcationDate, paidAmount;
    AllTransactionAdapter allTransactionAdapter;
    ShimmerRecyclerView recycler_view_Txn;
    String date1;
    ArrayList<AllTransactiongettersetter> allTransactiongettersetters;
    ArrayList<AllTransactiongettersetter> allTransactiongettersettersForLocalDb;
    LinearLayout linearLayoutnoDataFound;
    Toolbar toolbar;
    ImageView back;

    /////////////DB///////////////////////

    AppDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_all_transaction_summary);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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

        ///////////////////////DB/////////////////////////////////
        db = new AppDB(this);


        SharedPreferences sharedPreferences1 = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        String response = sharedPreferences1.getString("response", "123");

        if (response != null) {

            token = response;
            Log.d("alltexnsummtoken", " " + token);

            Log.d("alltexnsummt", " " + response);
        }


        if (isOnline()) {
            //API
            alltxnsummary(token);
        } else {

            AlertDialog alertDialog = new AlertDialog.Builder(AllTransactionSummary.this, R.style.MyDialogTheme).create();

            // Setting Dialog Title
            alertDialog.setTitle("No Internet Connection");

            // Setting Dialog Message
            alertDialog.setMessage("Please check internet connection and try again. Thank you.");

            // Setting Icon to Dialog
            //  alertDialog.setIcon(R.drawable.tick);

            // Setting OK Button
            alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                    // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                }
            });

            // Showing Alert Message
            alertDialog.show();
            Log.v("Home", "############################You are not online!!!!");

            /////////////////////Retrive data from local DB///////////////////////////////

            Cursor res = db.getTransactionData(token);
            allTransactiongettersettersForLocalDb = new ArrayList<>();
            if (res.getCount() > 0) {
                StringBuffer stringBuffer = new StringBuffer();
                while (res.moveToNext()) {
                    stringBuffer.append(res.getString(0));
                    stringBuffer.append(res.getString(1));
                    stringBuffer.append(res.getString(2));
                    stringBuffer.append(res.getString(3));
                    stringBuffer.append(res.getString(4));
                    stringBuffer.append(res.getString(5));

                    AllTransactiongettersetter allTransactiongettersetter = new AllTransactiongettersetter();
                    allTransactiongettersetter.setSpTranscationId(res.getString(1));
                    allTransactiongettersetter.setTranscationDate(res.getString(2));
                    allTransactiongettersetter.setPaidAmount(res.getString(3));
                    allTransactiongettersetter.setPaymentStatus(res.getString(4));
                    allTransactiongettersetter.setUserAcceessToken(res.getString(5));

                    allTransactiongettersettersForLocalDb.add(allTransactiongettersetter);


                }
                Log.d("getTransactionData", "-->" + stringBuffer);
                Log.d("TransactionForLocalDb", "-->" + allTransactiongettersettersForLocalDb.get(0).getSpTranscationId());
                Log.d("TransactionForLocalDb", "-->" + allTransactiongettersettersForLocalDb.get(0).getTranscationDate());
                Log.d("TransactionForLocalDb", "-->" + allTransactiongettersettersForLocalDb.get(0).getPaidAmount());
                Log.d("TransactionForLocalDb", "-->" + allTransactiongettersettersForLocalDb.get(0).getPaymentStatus());
                Log.d("TransactionForLocalDb", "-->" + allTransactiongettersettersForLocalDb.get(0).getUserAcceessToken());

                allTransactionAdapter = new AllTransactionAdapter(allTransactiongettersettersForLocalDb, getApplicationContext());
                recycler_view_Txn.setAdapter(allTransactionAdapter);
                allTransactionAdapter.notifyDataSetChanged();

            }else {
                Log.d("AllTransactionLocalDb", "In Else Part");
                Toast.makeText(AllTransactionSummary.this,"No Data Found !",Toast.LENGTH_SHORT).show();
            }




        }
    }

    public void alltxnsummary(final String token) {


        /*Cursor res = db.getTransactionData(token);
        allTransactiongettersettersForLocalDb = new ArrayList<>();
        if (res.getCount() > 0) {
            StringBuffer stringBuffer = new StringBuffer();
            while (res.moveToNext()) {
                stringBuffer.append(res.getString(0));
                stringBuffer.append(res.getString(1));
                stringBuffer.append(res.getString(2));
                stringBuffer.append(res.getString(3));
                stringBuffer.append(res.getString(4));
                stringBuffer.append(res.getString(5));

                AllTransactiongettersetter allTransactiongettersetter = new AllTransactiongettersetter();
                allTransactiongettersetter.setSpTranscationId(res.getString(1));
                allTransactiongettersetter.setTranscationDate(res.getString(2));
                allTransactiongettersetter.setPaidAmount(res.getString(3));
                allTransactiongettersetter.setPaymentStatus(res.getString(4));
                allTransactiongettersetter.setUserAcceessToken(res.getString(5));

                allTransactiongettersettersForLocalDb.add(allTransactiongettersetter);


            }
            Log.d("getTransactionData", "-->" + stringBuffer);
            Log.d("TransactionForLocalDb", "-->" + allTransactiongettersettersForLocalDb.get(0).getSpTranscationId());
            Log.d("TransactionForLocalDb", "-->" + allTransactiongettersettersForLocalDb.get(0).getTranscationDate());
            Log.d("TransactionForLocalDb", "-->" + allTransactiongettersettersForLocalDb.get(0).getPaidAmount());
            Log.d("TransactionForLocalDb", "-->" + allTransactiongettersettersForLocalDb.get(0).getPaymentStatus());
            Log.d("TransactionForLocalDb", "-->" + allTransactiongettersettersForLocalDb.get(0).getUserAcceessToken());

            allTransactionAdapter = new AllTransactionAdapter(allTransactiongettersettersForLocalDb, getApplicationContext());
            recycler_view_Txn.setAdapter(allTransactionAdapter);
            allTransactionAdapter.notifyDataSetChanged();

        } else {*/

        //clear table
        db.deleteAllTransactionData();

            String url = AppConfig.Base_Url + "/SabPaisaResponseHandler/" + AppConfig.URL_AllTransactionReport;
            // String url="https://portal.sabpaisa.in/SabPaisaResponseHandler/SPtranscationIds?token=";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url + token, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d("xyvhjkk.nk", "" + response);
                    //progressBar.setVisibility(View.GONE);
                    JSONObject object = null;
                    try {

                        allTransactiongettersetters = new ArrayList<>();

                        object = new JSONObject(response.toString());


                        String status = object.getString("status");
                        String responsee = object.getString("response");

                        Log.d("txnstatussum", "" + status);
                        Log.d("txnstatussum", "" + responsee);

                        if (status.equals("failure")) {

                            linearLayoutnoDataFound.setVisibility(View.VISIBLE);
                            recycler_view_Txn.setVisibility(View.GONE);

                        }
                        JSONArray jArray3 = object.getJSONArray("response");

                        Log.d("txnarraysum", "" + jArray3);

                   /* JsonObject jsonObject;
                    jsonObject = new JsonObject(responsee);

                    for(int i = 0; i < jArray3 .length(); i++)

*/

                        for (int i = 0; i < jArray3.length(); i++) {
                            JSONObject object3 = jArray3.getJSONObject(i);

                            AllTransactiongettersetter allTransactiongettersetter = new AllTransactiongettersetter();
                            allTransactiongettersetter.setClientName(object3.getString("clientName"));
                            allTransactiongettersetter.setId(object3.getString("id"));
                            allTransactiongettersetter.setPaidAmount(object3.getString("paidAmount"));
                            allTransactiongettersetter.setSpTranscationId(object3.getString("spTranscationId"));
                            allTransactiongettersetter.setPaymentStatus(object3.getString("paymentStatus"));
                            allTransactiongettersetter.setUserAcceessToken(object3.getString("userAcceessToken"));
                            String datatime = object3.getString("transcationDate");
                            String x = object3.getString("paymentStatus");
                            Log.d("abcgshy", "dnjk" + datatime);
                            Log.d("abcgshy", "dnjk" + x);

                            allTransactiongettersetter.setTranscationDate(getDate(Long.parseLong(datatime)));

                            //////////////////////////////LOCAL DB//////////////////////////////////////

                            boolean isInserted = db.insertDataForTransaction(allTransactiongettersetter);
                            if (isInserted == true) {

                                //Toast.makeText(AllTransactionSummary.this, "Data  Inserted", Toast.LENGTH_SHORT).show();

                                Log.d("AllTransactionSummary", "LocalDBInIfPart" + isInserted);

                            } else {
                                Log.d("AllTransactionSummary", "LocalDBInElsePart" + isInserted);
                                //Toast.makeText(AllTransactionSummary.this, "Data  Not Inserted", Toast.LENGTH_SHORT).show();
                            }


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
               */
                        }
                        Log.d("ArrayListAfterParse", " " + allTransactiongettersetters.get(0).getSpTranscationId());


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    allTransactionAdapter = new AllTransactionAdapter(allTransactiongettersetters, getApplicationContext());
                    recycler_view_Txn.setAdapter(allTransactionAdapter);
                    allTransactionAdapter.notifyDataSetChanged();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            AppController.getInstance().addToRequestQueue(stringRequest);

       // } SQLite

    }


    private String getDate(long time) {


        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd/MM/yyyy HH:mm", cal).toString();
        date1 = DateFormat.format("dd/MM/yyyy ", cal).toString();
        Log.d("date11", "" + date1);
        return date;
    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v("AllTransactionSummary", "Internet Connection Not Present");
            return false;
        }
    }


}
