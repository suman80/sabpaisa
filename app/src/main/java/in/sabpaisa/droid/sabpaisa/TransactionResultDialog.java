package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
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
        //CommonUtils.setFullScreen(this);
       // supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.list_transaction_result);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("Status", true);
        setResult(200, intent);
        finish();
    }

}
