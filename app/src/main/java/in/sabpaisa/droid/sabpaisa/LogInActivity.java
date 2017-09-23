package in.sabpaisa.droid.sabpaisa;

import android.Manifest;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import in.sabpaisa.droid.sabpaisa.Util.TelephonyInfo;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static in.sabpaisa.droid.sabpaisa.Util.TelephonyInfo.getOutput;

@RuntimePermissions
public class LogInActivity extends AppCompatActivity {

    EditText phoneNumber,password;
    TextView forgotPassword,register,passwordShow;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DataBinding();
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogInActivityPermissionsDispatcher.isDualSimOrNotWithCheck(LogInActivity.this);
            }
        });
        passwordShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordShow.getText().toString().equals("Show")){
                    passwordShow.setText("Hide");
                    //password.setInputType(InputType.TYPE_CLASS_TEXT);
                }else
                {
                    passwordShow.setText("Show");
                   // password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    private void DataBinding() {
        phoneNumber = (EditText)findViewById(R.id.et_phone_number);
        password = (EditText)findViewById(R.id.et_password);
        passwordShow = (TextView)findViewById(R.id.tv_password_show);
        forgotPassword = (TextView)findViewById(R.id.tv_forgot_password);
        register = (TextView)findViewById(R.id.tv_register);
        login = (Button)findViewById(R.id.btn_login);
    }

    @NeedsPermission(Manifest.permission.READ_PHONE_STATE)
    public void isDualSimOrNot(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = this.getLayoutInflater().inflate(R.layout.bottomdialog_getting_sim_info, null);
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
        TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);
        String optName1 = getOutput(this, "SimOperatorName", 0);
        String optName2 = getOutput(this, "SimOperatorName", 1);
        String optName3 = getOutput(this, "SimOperatorName", 2);
        bottomSheetDialog.cancel();

        Intent intent = new Intent(LogInActivity.this,VerifyMobile.class);
        intent.putExtra("SIM1",optName1);
        intent.putExtra("SIM2",optName2);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }
}
