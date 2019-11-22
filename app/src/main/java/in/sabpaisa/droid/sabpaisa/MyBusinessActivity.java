package in.sabpaisa.droid.sabpaisa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyBusinessActivity extends AppCompatActivity {
    private Button todayReport,customReport;
    androidx.appcompat.widget.Toolbar toolbar;
    private ImageView down_arrow,up_arrow;
    private LinearLayout todayClient_layout;
    private TextView today_transaction,datewisetransactio;
    private CardView paymenttransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_business);
        todayReport=findViewById(R.id.today_report);
        customReport=findViewById(R.id.custom_report);
        down_arrow=findViewById(R.id.down_arrow);
        paymenttransaction=findViewById(R.id.paymenttransaction);

      /*  paymenttransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todayClient_layout.setVisibility(View.VISIBLE);

            }
        });
        */
        up_arrow=findViewById(R.id.up_arrow);
        todayClient_layout=findViewById(R.id.todayClient_layout);
        today_transaction=findViewById(R.id.today_transaction_layout);
        datewisetransactio=findViewById(R.id.datewise_transaction_layout);

        today_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),TodayTransactionDetailsActivity.class);
                startActivity(intent);

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
                up_arrow.setVisibility(View.VISIBLE);
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
        toolbar.setTitle("Monitor paisa");
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
}
