package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterPasscode extends AppCompatActivity implements View.OnClickListener {

    TextView title;
    TextView pass1,pass2,pass3,pass4;
    TextView num1,num2,num3,num4,num5,num6,num7,num8,num9,num0;
    ImageView back,forward;
    int flag =1;
    int activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_register_passcode);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        DataBinding();

        String titleString = getIntent().getStringExtra("Title");
        activity = getIntent().getIntExtra("activity",0);
        title.setText(titleString);

        num0.setOnClickListener(this);
        num1.setOnClickListener(this);
        num2.setOnClickListener(this);
        num3.setOnClickListener(this);
        num4.setOnClickListener(this);
        num5.setOnClickListener(this);
        num6.setOnClickListener(this);
        num7.setOnClickListener(this);
        num8.setOnClickListener(this);
        num9.setOnClickListener(this);
        back.setOnClickListener(this);
        forward.setOnClickListener(this);
    }

    private void DataBinding() {
        title = (TextView)findViewById(R.id.tv_title);
        pass1 = (TextView) findViewById(R.id.ll_pass1).findViewById(R.id.tv_pass);
        pass2 = (TextView) findViewById(R.id.ll_pass2).findViewById(R.id.tv_pass);
        pass3 = (TextView) findViewById(R.id.ll_pass3).findViewById(R.id.tv_pass);
        pass4 = (TextView) findViewById(R.id.ll_pass4).findViewById(R.id.tv_pass);

        num0 = (TextView)findViewById(R.id.text0);
        num1 = (TextView)findViewById(R.id.text1);
        num2 = (TextView)findViewById(R.id.text2);
        num3 = (TextView)findViewById(R.id.text3);
        num4 = (TextView)findViewById(R.id.text4);
        num5 = (TextView)findViewById(R.id.text5);
        num6 = (TextView)findViewById(R.id.text6);
        num7 = (TextView)findViewById(R.id.text7);
        num8 = (TextView)findViewById(R.id.text8);
        num9 = (TextView)findViewById(R.id.text9);

        back = (ImageView)findViewById(R.id.textBack);
        forward = (ImageView)findViewById(R.id.textOk);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_right_out);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text0:
                setPass("0");
                break;
            case R.id.text1:
                setPass("1");
                break;
            case R.id.text2:
                setPass("2");
                break;
            case R.id.text3:
                setPass("3");
                break;
            case R.id.text4:
                setPass("4");
                break;
            case R.id.text5:
                setPass("5");
                break;
            case R.id.text6:
                setPass("6");
                break;
            case R.id.text7:
                setPass("7");
                break;
            case R.id.text8:
                setPass("8");
                break;
            case R.id.text9:
                setPass("9");
                break;
            case R.id.textBack:
                removeFlag();
                break;
            case R.id.textOk:
                if (activity==0) {
                    if (pass4.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Thou shall enter the passcode first", Toast.LENGTH_SHORT).show();
                    } else {
                        String passcode = pass1.getText().toString() + "" + pass2.getText().toString() + "" + pass3.getText().toString() + "" + pass4.getText().toString();
                        Intent intent = new Intent(RegisterPasscode.this, RegisterPasscode.class);
                        intent.putExtra("Title", "Confirm Passcode");
                        intent.putExtra("activity",1);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
                    }
                }
                break;
        }
    }

    private void removeFlag() {
        if (flag==1){
            return;
        }
        if (flag==5){
            pass4.setText("");
        }else if (flag==4){
            pass3.setText("");
        }else if (flag==3){
            pass2.setText("");
        }else if (flag==2){
            pass1.setText("");
        }
        flag--;
    }

    private void setPass(String text) {
        if (flag==5) {
            return;
        }
        if (flag == 1) {
            pass1.setText(text);
        } else if (flag == 2) {
            pass2.setText(text);
        } else if (flag == 3) {
            pass3.setText(text);
        } else if (flag == 4) {
            pass4.setText(text);
        }
        flag++;
        if (activity==1 && flag==5){
            //TODO CHECK FOR THE PASSCODE TO BE SAME
            Intent intent = new Intent(RegisterPasscode.this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
        }
    }
}
