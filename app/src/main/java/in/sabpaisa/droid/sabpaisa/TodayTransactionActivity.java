package in.sabpaisa.droid.sabpaisa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.Property;
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
import java.util.List;
import java.util.Locale;

import in.sabpaisa.droid.sabpaisa.Adapter.CustomTransactioReportAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.TodayTransactionAdapter;
import in.sabpaisa.droid.sabpaisa.Model.CustomTransactionReportgettersetter;
import in.sabpaisa.droid.sabpaisa.Model.TodayTransactiongettersetter;

import static in.sabpaisa.droid.sabpaisa.Model.TodayTransactiongettersetter.*;

public class TodayTransactionActivity extends AppCompatActivity   {

    private RecyclerView custom_transaction_recycleView;
    private Button viewCustomReport;
    private androidx.appcompat.widget.Toolbar toolbar;
    private ArrayList<TodayTransactiongettersetter> customTransactiongettersetters;
    private TodayTransactionAdapter customTransactioReportAdapter;
    ProgressDialog progressDialog;
    private LinearLayout noDataFound;
    private  List<TodayTransactiongettersetter> challenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_transaction_report);

        challenge  = this.getIntent().getExtras().getParcelableArrayList("Birds");
        //String secondTransList=getIntent().getStringExtra("ArrayList");
        Log.d("transalistdmkmkd",""+challenge);

        custom_transaction_recycleView=findViewById(R.id.custom_transaction);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        custom_transaction_recycleView.addItemDecoration(new SimpleDividerItemDecoration(this));
        custom_transaction_recycleView.setLayoutManager(llm);
        custom_transaction_recycleView.setNestedScrollingEnabled(false);
        customTransactiongettersetters = new ArrayList<>();


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
        customTransactioReportAdapter = new TodayTransactionAdapter(challenge, getApplicationContext());
        custom_transaction_recycleView.setAdapter(customTransactioReportAdapter);
        customTransactioReportAdapter.notifyDataSetChanged();
    }




}
