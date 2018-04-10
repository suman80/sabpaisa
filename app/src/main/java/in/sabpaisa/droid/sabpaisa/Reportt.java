package in.sabpaisa.droid.sabpaisa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

import in.sabpaisa.droid.sabpaisa.Model.TransactionreportModelClass;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;

/**
 * Created by archana on 12/3/18.
 */

public class Reportt extends AppCompatActivity {

    TextView t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,clientname;
String date1;
Toolbar toolbar;
ImageView back;
String refId=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        setContentView(R.layout.content_txn_report);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        t1=(TextView)findViewById(R.id.t11);
        t2=(TextView)findViewById(R.id.t22);
        t3=(TextView)findViewById(R.id.t33);
        t4=(TextView)findViewById(R.id.t44);
        back=(ImageView) findViewById(R.id.back3);
        t5=(TextView)findViewById(R.id.t55);
        t6=(TextView)findViewById(R.id.t66);
        t7=(TextView)findViewById(R.id.t77);
        t8=(TextView)findViewById(R.id.t88);
        t9=(TextView)findViewById(R.id.t99);
//        toolbar=(Toolbar)findViewById(R.id.toolbacontr);
        t10=(TextView)findViewById(R.id.t101);
        clientname=(TextView)findViewById(R.id.clientname);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationIcon(R.drawable.ic_action_previousback);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }*/

        //Intent i=getIntent();

        refId= getIntent().getExtras().getString("refID");
        Log.d("Refid","---"+refId);

        TransactionReport(refId);

    }


    public void TransactionReport(final String Referenceid){

        final String tag_string_req = "req_Report";

        String url= AppConfig.Base_Url+"/SabPaisaResponseHandler/"+AppConfig.URL_TransactionReport;

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url + Referenceid, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TRNCTNReport1",""+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);


                    String status = jsonObject.getString("status");

                    String response1 = jsonObject.getString("response");

                    Log.d("TRNCTNReport2","status--"+status);
                    Log.d("TRNCTNReport3","response--"+response1);


                    if(status.equals("success")) {


                        //Toast.makeText(getApplication(), "TransactionReport", Toast.LENGTH_LONG).show();

                        JSONObject jsonObject1 = new JSONObject(response1);


                        TransactionreportModelClass TRMC=new TransactionreportModelClass();
                        TRMC.setStatus(jsonObject1.getString("status"));
                        String a=TRMC.getStatus().toString();
                        Log.d("sttus","--"+a);

                        TRMC.setPayeeFirstName(jsonObject1.getString("payeeFirstName"));
                        TRMC.setPayeeLstName(jsonObject1.getString("payeeLstName"));
                      ///  TRMC.setPayeeLstName(jsonObject1.getString("payeeLstName"));
                        TRMC.setPayeeAmount(jsonObject1.getString("payeeAmount"));
                        TRMC.setPaidAmount(jsonObject1.getString("paidAmount"));
                        TRMC.setPayeeMob(jsonObject1.getString("payeeMob"));
                        TRMC.setConvcharges(jsonObject1.getString("convcharges"));
                        TRMC.setPayeeEmail(jsonObject1.getString("payeeEmail"));
                        TRMC.setTxnId(jsonObject1.getString("txnId"));
                        TRMC.setStatus(jsonObject1.getString("status"));
                        TRMC.setClientName(jsonObject1.getString("clientName"));
                        TRMC.setActAmount(jsonObject1.getString("actAmount"));
                        TRMC.setAgrProfitshare(jsonObject1.getString("agrProfitshare"));
                        TRMC.setBankTxnId(jsonObject1.getString("bankTxnId"));
                        TRMC.setApplicationFailurePath(jsonObject1.getString("applicationFailurePath"));
                        TRMC.setPgname(jsonObject1.getString("pgname"));
                        TRMC.setConvifee(jsonObject1.getString("convifee"));
                        TRMC.setPgtxnId(jsonObject1.getString("pgtxnId"));
                        TRMC.setPaymentMode(jsonObject1.getString("paymentMode"));
                        TRMC.setTransDate(jsonObject1.getString("transDate"));//.split(" ")[1].replace(".0", "");
                        String dataTime ;
                        dataTime=TRMC.getTransDate().toString();
                        clientname.setText(TRMC.getClientName().toString());
                        try {
                            t1.setText(getDate(Long.parseLong(dataTime)));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        t10.setText(TRMC.getStatus().toString());
                      t9.setText(TRMC.getPayeeAmount().toString());
                       t8.setText(TRMC.getPayeeMob().toString());
                        t7.setText(TRMC.getPayeeEmail().toString());
                        t6.setText(TRMC.getTxnId().toString());
                        t5.setText(TRMC.getPayeeFirstName().toString());
                     //   t4.setText(TRMC.getPayeeFirstName().toString()+" "+TRMC.getPayeeLstName().toString());
                       t4.setText(TRMC.getPaymentMode().toString());
                       t3.setText(TRMC.getPaidAmount().toString());
                     //   t2.setText(TRMC.getPaidAmount().toString());
                       // t1.setText(TRMC.getTransDate().toString());
                        String a1 = TRMC.getBankTxnId().toString();
                        Log.d("aaaaaar", "" + jsonObject.getJSONObject(TRMC.getPayeeFirstName().toString()));
                        Log.d("BANKID", "___" + a1);





    /*jsonObject = new JSONObject(response1.toString());
    String response = jsonObject.getString("response");
    String status = jsonObject.getString("status");
    Log.d("responsus",""+response);
    Log.d("statsus",""+status);
    JSONObject jsonObject1 = new JSONObject(response);
    FetchUserImageGetterSetter fetchUserImageGetterSetter=new FetchUserImageGetterSetter();
    fetchUserImageGetterSetter.setUserImageUrl(jsonObject1.getString("userImageUrl"));
    userImageUrl=fetchUserImageGetterSetter.getUserImageUrl().toString();*/

                    }

                    else if(status.equals("failure")){
                    clientname.setVisibility(View.GONE);

                        t10.setVisibility(View.GONE);
                        t9.setVisibility(View.GONE);
                        t8.setVisibility(View.GONE);
                        t7.setVisibility(View.GONE);
                        t6.setVisibility(View.GONE);
                        t5.setVisibility(View.GONE);
                        //   t4.setText(TRMC.getPayeeFirstName().toString()+" "+TRMC.getPayeeLstName().toString());
                        t4.setVisibility(View.GONE);
                        t3.setVisibility(View.GONE);
                        t1.setVisibility(View.GONE);

                        AlertDialog alertDialog = new AlertDialog.Builder(Reportt.this, R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("");

                        // Setting Dialog Message
                        alertDialog.setMessage("Hey,you have enter wrong Reference ID.Try again with correct Reference Id");
                        // Setting Icon to Dialog
                        //  alertDialog.setIcon(R.drawable.tick);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(Reportt.this,TransactionReportNav.class);
i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                               // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                                // Write your code here to execute after dialog closed
                                // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();

                    }
                    else
                    {
                        Log.d("reportID", "ELSEprt");

                    }
                }

                catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        } ,new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);

    }

    private String getDate(long time) throws ParseException {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd/MM HH:mm", cal).toString();
        date1 = DateFormat.format("dd/MM ", cal).toString();
        Log.d("date11", "" + date1);
        return date;
    }
}
