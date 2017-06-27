package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by abc on 21-06-2017.
 */

public class TransactionResultDialog extends AppCompatActivity {
    TextView txnAmount,userName,messageHead,messageBody;
    ImageView messageImage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.list_transaction_result);

        String name = getIntent().getStringExtra("Name");
        String amount = getIntent().getStringExtra("Amount");
        String upi = getIntent().getStringExtra("UPI");
        boolean result = getIntent().getBooleanExtra("Result",false);

        txnAmount = (TextView)findViewById(R.id.tv_txnAmount);
        userName = (TextView)findViewById(R.id.tv_userName);
        messageHead = (TextView)findViewById(R.id.tv_message_head);
        messageBody = (TextView)findViewById(R.id.tv_message_body);
        messageImage = (ImageView) findViewById(R.id.iv_message_image);

        messageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("Status", true);
                setResult(200, intent);
                finish();
            }
        });
    }
}
