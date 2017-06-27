package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TransactionResult extends AppCompatActivity {

    Toolbar toolbar;
    TextView txnAmount,userName,messageHead,messageBody;
    ImageView messageImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_result);

        String name = getIntent().getStringExtra("Name");
        String amount = getIntent().getStringExtra("Amount");
        String upi = getIntent().getStringExtra("UPI");
        boolean result = getIntent().getBooleanExtra("Result",false);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txnAmount = (TextView)findViewById(R.id.tv_txnAmount);
        userName = (TextView)findViewById(R.id.tv_userName);
        messageHead = (TextView)findViewById(R.id.tv_message_head);
        messageBody = (TextView)findViewById(R.id.tv_message_body);
        messageImage = (ImageView) findViewById(R.id.iv_message_image);


        userName.setText(name);
        txnAmount.setText(amount);
        if (result) {
            toolbar.setTitle("Transaction Success");
            messageHead.setText("Congratulations !!!");
            messageBody.setText("You have succesfully send " + getResources().getString(R.string.Rs)+" "+amount + " to "+upi);
            messageImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_ic_check_circle_black_24dp));
        }else {
            toolbar.setTitle("Transaction Failed");
            messageHead.setText("Transaction Failed");
            messageBody.setText("Unable to send " + getResources().getString(R.string.Rs) + " "+amount+" to "+upi);
            messageImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_ic_cancel_black_24dp));
        }
        messageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BacktoMain();
            }
        });
        setSupportActionBar(toolbar);
    }

    private void BacktoMain() {
        Intent intent = new Intent(TransactionResult.this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_right_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BacktoMain();
    }
}
