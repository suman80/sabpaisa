package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;

/**
 * Created by abc on 21-06-2017.
 */

public class SendConfirmUPImPINDialog extends AppCompatActivity implements View.OnClickListener{
    TextView mdigit1,mdigit2,mdigit3,mdigit4,mdigit5,mdigit6;
    TextView num1,num2,num3,num4,num5,num6,num7,num8,num9,num0;
    TextView showHide,txnAmount,userName;
    ImageView back,forward;
    int flag =1;
    String upi;
    BottomSheetDialog bottomSheetDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.item_send_confirm_upi_mpin);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mdigit1=(TextView) findViewById(R.id.layout_mdigit1).findViewById(R.id.tv_pass);
        mdigit2=(TextView) findViewById(R.id.layout_mdigit2).findViewById(R.id.tv_pass);
        mdigit3=(TextView) findViewById(R.id.layout_mdigit3).findViewById(R.id.tv_pass);
        mdigit4=(TextView) findViewById(R.id.layout_mdigit4).findViewById(R.id.tv_pass);
        mdigit5=(TextView) findViewById(R.id.layout_mdigit5).findViewById(R.id.tv_pass);
        mdigit6=(TextView) findViewById(R.id.layout_mdigit6).findViewById(R.id.tv_pass);
        showHide = (TextView)findViewById(R.id.tv_showHide);
//        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        /*bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.layout_keyboard_onscreen, null);

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        bottomSheetDialog.show();*/

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

        showHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showHide.getText().equals("Show")) {
                    showHide.setText("Hide");
                    mdigit1.setInputType(InputType.TYPE_CLASS_NUMBER);
                    mdigit2.setInputType(InputType.TYPE_CLASS_NUMBER);
                    mdigit3.setInputType(InputType.TYPE_CLASS_NUMBER);
                    mdigit4.setInputType(InputType.TYPE_CLASS_NUMBER);
                    mdigit5.setInputType(InputType.TYPE_CLASS_NUMBER);
                    mdigit6.setInputType(InputType.TYPE_CLASS_NUMBER);
                }else{
                    showHide.setText("Show");
                    mdigit1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mdigit2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mdigit3.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mdigit4.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mdigit5.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mdigit6.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

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
                if (mdigit6.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Thou shall enter the passcode first", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void removeFlag() {
        if (flag == 1) {
            return;
        }
        if (flag == 7) {
            mdigit6.setText("");
        } else if (flag == 6) {
            mdigit5.setText("");
        } else if (flag == 5) {
            mdigit4.setText("");
        } else if (flag == 4) {
            mdigit3.setText("");
        } else if (flag == 3) {
            mdigit2.setText("");
        } else if (flag == 2) {
            mdigit1.setText("");
        }
        flag--;
    }

    private void setPass(String text) {
        if (flag == 7) {
            return;
        }
        if (flag == 1) {
            mdigit1.setText(text);
        } else if (flag == 2) {
            mdigit2.setText(text);
        } else if (flag == 3) {
            mdigit3.setText(text);
        } else if (flag == 4) {
            mdigit4.setText(text);
        } else if (flag == 5) {
            mdigit5.setText(text);
        } else if (flag == 6) {
            mdigit6.setText(text);
        }
        flag++;
        if (flag == 7) {
            final BottomSheetDialog bottomSheetDialog1 = new BottomSheetDialog(this);
            View sheetView = getLayoutInflater().inflate(R.layout.bottomsheet_verifing_upi, null);
            TextView message = (TextView)sheetView.findViewById(R.id.tv_message);
            message.setText("Transfering money");
            bottomSheetDialog1.setContentView(sheetView);
            bottomSheetDialog1.setCancelable(false);
            bottomSheetDialog1.show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() { //TODO ,API - ReqPay , RespPay
                    bottomSheetDialog1.cancel();
                    Intent intent = new Intent(SendConfirmUPImPINDialog.this,TransactionResultDialog.class);
                    intent.putExtra("Name","Harpreet");
                    intent.putExtra("Amount","500");
                    intent.putExtra("UPI","gaurav@upi");
                    intent.putExtra("Result",true);
                    startActivityForResult(intent,100);
                    setVisible(false);
                }
            }, 5000);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if(resultCode == 200) {
                Intent intent = new Intent();
                intent.putExtra("Status", true);
                setResult(200, intent);
                finish();
            }
        }
    }
}
