package in.sabpaisa.droid.sabpaisa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.tv.TvContract;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import in.sabpaisa.droid.sabpaisa.Fragments.InstitutionFragment;
import in.sabpaisa.droid.sabpaisa.Model.TransactionreportModelClass;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;

public class TransactionReportNav extends AppCompatActivity {
Button enter;
    //String RefId="0023212021811314838863760";
    EditText RefID_Et;
    RelativeLayout txnlayout;
    RelativeLayout txnlayoutcont;
String refID;
ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_transaction_report_nav);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
      RefID_Et=(EditText)findViewById(R.id.refid);
      back=(ImageView)findViewById(R.id.back4);
      enter=(Button)findViewById(R.id.Report);

/*
      refID = RefID_Et.getText().toString();*/


back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        onBackPressed();
    }
});

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                refID = RefID_Et.getText().toString();
                Log.d("refrenceid1",""+refID);

                if (RefID_Et.getText().toString().equals(""))

                {
                    AlertDialog alertDialog = new AlertDialog.Builder(TransactionReportNav.this, R.style.MyDialogTheme).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("");

                    // Setting Dialog Message
                    alertDialog.setMessage("Hey,you forgot to enter your Reference id");
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
                   /* Log.d("refrenceid131313",""+refID);
                    Intent intent = new Intent(TransactionReportNav.this, Reportt.class);
                    intent.putExtra("refID", refID);
                    startActivity(intent);*/
                } else {
                    Log.d("refrenceid131314",""+refID);
                    Intent intent = new Intent(TransactionReportNav.this, Reportt.class);
                    intent.putExtra("refID", RefID_Et.getText().toString());
                    Log.d("refrencud",""+refID);

                    startActivity(intent);
                    /*AlertDialog alertDialog = new AlertDialog.Builder(TransactionReportNav.this, R.style.MyDialogTheme).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("");

                    // Setting Dialog Message
                    alertDialog.setMessage("Hey,you forgot to enter your Reference id");
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
                    alertDialog.show();*/
                }



            }
        });
    }

}
