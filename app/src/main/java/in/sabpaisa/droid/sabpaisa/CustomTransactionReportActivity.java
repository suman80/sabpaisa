package in.sabpaisa.droid.sabpaisa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.sabpaisa.droid.sabpaisa.Adapter.CustomTransactioReportAdapter;
import in.sabpaisa.droid.sabpaisa.Model.CustomTransactionReportgettersetter;

public class CustomTransactionReportActivity extends AppCompatActivity {

    private RecyclerView custom_transaction_recycleView;
    private Button viewCustomReport;
    private androidx.appcompat.widget.Toolbar toolbar;
    private ArrayList<CustomTransactionReportgettersetter> customTransactiongettersetters;
    private CustomTransactioReportAdapter customTransactioReportAdapter;
    ProgressDialog progressDialog;
    private LinearLayout noDataFound;
    private List<CustomTransactionReportgettersetter> challenge;

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

        challenge  = this.getIntent().getExtras().getParcelableArrayList("Birds");

        Log.d("birdsData",""+challenge);


        progressDialog=new ProgressDialog(CustomTransactionReportActivity.this);
        noDataFound=findViewById(R.id.noDataFound);
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

        viewCustomReport=findViewById(R.id.viewCustomReport);
        customTransactioReportAdapter = new CustomTransactioReportAdapter(challenge, getApplicationContext());
        custom_transaction_recycleView.setAdapter(customTransactioReportAdapter);
        customTransactioReportAdapter.notifyDataSetChanged();


    }



}
