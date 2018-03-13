package in.sabpaisa.droid.sabpaisa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import in.sabpaisa.droid.sabpaisa.Model.TransactionreportModelClass;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;

/**
 * Created by archana on 12/3/18.
 */

public class Report extends AppCompatActivity {

    TextView t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,clientname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        setContentView(R.layout.content_txn_report);


        t1=(TextView)findViewById(R.id.t11);
        t2=(TextView)findViewById(R.id.t22);
        t3=(TextView)findViewById(R.id.t33);
        t4=(TextView)findViewById(R.id.t44);
        t5=(TextView)findViewById(R.id.t55);
        t6=(TextView)findViewById(R.id.t66);
        t7=(TextView)findViewById(R.id.t77);
        t8=(TextView)findViewById(R.id.t88);
        t9=(TextView)findViewById(R.id.t99);
        t10=(TextView)findViewById(R.id.t101);
        clientname=(TextView)findViewById(R.id.clientname);


        TransactionReport("0023212021811314838863760");

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
                        Log.d("archna","--"+a);

                        TRMC.setPayeeFirstName(jsonObject1.getString("payeeFirstName"));
                        TRMC.setPayeeLstName(jsonObject1.getString("payeeLstName"));
                        TRMC.setPaidAmount(jsonObject1.getString("payeeLstName"));
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
//                        clientname.setText(TRMC.getClientName().toString());

                        t10.setText(TRMC.getStatus().toString());
                        t9.setText(TRMC.getPayeeAmount().toString());
                        t8.setText(TRMC.getPayeeMob().toString());
                        t7.setText(TRMC.getPayeeEmail().toString());
                        t6.setText(TRMC.getTxnId().toString());
                        t5.setText(TRMC.getPayeeAmount().toString());
                        t4.setText(TRMC.getPayeeFirstName().toString()+" "+TRMC.getPayeeLstName().toString());
                        t3.setText(TRMC.getPaymentMode().toString());
                        t2.setText(TRMC.getPaidAmount().toString());
                        t1.setText(TRMC.getTransDate().toString());
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


}
