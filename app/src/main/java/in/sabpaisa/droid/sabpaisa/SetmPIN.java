package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SetmPIN extends AppCompatActivity implements View.OnClickListener {

    SeekBar stepSlider;
    @Nullable
    TextView accountNumber,digit1,digit2,digit3,digit4,digit5,digit6,cvv_digit1,cvv_digit2,cvv_digit3,cvv_digit4;
    TextView otpdigit1,otpdigit2,otpdigit3,otpdigit4,mdigit1,mdigit2,mdigit3,mdigit4,mdigit5,mdigit6,cmdigit1,cmdigit2,cmdigit3,cmdigit4,cmdigit5,cmdigit6;
    TextView num1,num2,num3,num4,num5,num6,num7,num8,num9,num0;
    TextView showHide,cshowHide;
    ImageView back,forward;
    String titleString;
    Toolbar toolbar;
    int flag =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_mpin);

        DataBinding();


        stepSlider.incrementProgressBy(50);
        stepSlider.setMax(100);
        stepSlider.setEnabled(false);
        titleString = getIntent().getStringExtra("Tag");

        if (titleString.equals("Setup Debit Card")){
            stepSlider.setProgress(0);
            findViewById(R.id.layout_setup_card).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_set_mpin).setVisibility(View.GONE);
            findViewById(R.id.confirm_mpin).setVisibility(View.GONE);
        }else if (titleString.equals("Setup UPI MPIN")){
            stepSlider.setProgress(50);
            findViewById(R.id.layout_setup_card).setVisibility(View.GONE);
            findViewById(R.id.layout_set_mpin).setVisibility(View.VISIBLE);
            findViewById(R.id.confirm_mpin).setVisibility(View.GONE);
        }else if (titleString.equals("Confirm UPI MPIN")){
            stepSlider.setProgress(100);
            findViewById(R.id.layout_setup_card).setVisibility(View.GONE);
            findViewById(R.id.layout_set_mpin).setVisibility(View.GONE);
            findViewById(R.id.confirm_mpin).setVisibility(View.VISIBLE);
        }
        toolbar.setTitle("Set UPI mPIN");
        setSupportActionBar(toolbar);


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
        cshowHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cshowHide.getText().equals("Show")) {
                    cshowHide.setText("Hide");
                    cmdigit1.setInputType(InputType.TYPE_CLASS_NUMBER);
                    cmdigit2.setInputType(InputType.TYPE_CLASS_NUMBER);
                    cmdigit3.setInputType(InputType.TYPE_CLASS_NUMBER);
                    cmdigit4.setInputType(InputType.TYPE_CLASS_NUMBER);
                    cmdigit5.setInputType(InputType.TYPE_CLASS_NUMBER);
                    cmdigit6.setInputType(InputType.TYPE_CLASS_NUMBER);
                }else{
                    cshowHide.setText("Show");
                    cmdigit1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    cmdigit2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    cmdigit3.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    cmdigit4.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    cmdigit5.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    cmdigit6.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    private void DataBinding() {
        stepSlider = (SeekBar)findViewById(R.id.sb_stepSlider);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        accountNumber = (TextView)findViewById(R.id.tv_accountNumber);
        digit1 = (TextView)findViewById(R.id.layout_digit1).findViewById(R.id.tv_pass);
        digit2 = (TextView)findViewById(R.id.layout_digit2).findViewById(R.id.tv_pass);
        digit3 = (TextView)findViewById(R.id.layout_digit3).findViewById(R.id.tv_pass);
        digit4 = (TextView)findViewById(R.id.layout_digit4).findViewById(R.id.tv_pass);
        digit5 = (TextView)findViewById(R.id.layout_digit5).findViewById(R.id.tv_pass);
        digit6 = (TextView)findViewById(R.id.layout_digit6).findViewById(R.id.tv_pass);
        cvv_digit1 = (TextView)findViewById(R.id.layout_cvv_digit1).findViewById(R.id.tv_pass);
        cvv_digit2 = (TextView)findViewById(R.id.layout_cvv_digit2).findViewById(R.id.tv_pass);
        cvv_digit3 = (TextView)findViewById(R.id.layout_cvv_digit3).findViewById(R.id.tv_pass);
        cvv_digit4 = (TextView)findViewById(R.id.layout_cvv_digit4).findViewById(R.id.tv_pass);
        otpdigit1 = (TextView)findViewById(R.id.layout_otpdigit1).findViewById(R.id.tv_pass);
        otpdigit2 = (TextView)findViewById(R.id.layout_otpdigit2).findViewById(R.id.tv_pass);
        otpdigit3 = (TextView)findViewById(R.id.layout_otpdigit3).findViewById(R.id.tv_pass);
        otpdigit4 = (TextView)findViewById(R.id.layout_otpdigit4).findViewById(R.id.tv_pass);
        mdigit1 = (TextView)findViewById(R.id.layout_mdigit1).findViewById(R.id.tv_pass);
        mdigit2 = (TextView)findViewById(R.id.layout_mdigit2).findViewById(R.id.tv_pass);
        mdigit3 = (TextView)findViewById(R.id.layout_mdigit3).findViewById(R.id.tv_pass);
        mdigit4 = (TextView)findViewById(R.id.layout_mdigit4).findViewById(R.id.tv_pass);
        mdigit5 = (TextView)findViewById(R.id.layout_mdigit5).findViewById(R.id.tv_pass);
        mdigit6 = (TextView)findViewById(R.id.layout_mdigit6).findViewById(R.id.tv_pass);
        cmdigit1 = (TextView)findViewById(R.id.layout_cmdigit1).findViewById(R.id.tv_pass);
        cmdigit2 = (TextView)findViewById(R.id.layout_cmdigit2).findViewById(R.id.tv_pass);
        cmdigit3 = (TextView)findViewById(R.id.layout_cmdigit3).findViewById(R.id.tv_pass);
        cmdigit4 = (TextView)findViewById(R.id.layout_cmdigit4).findViewById(R.id.tv_pass);
        cmdigit5 = (TextView)findViewById(R.id.layout_cmdigit5).findViewById(R.id.tv_pass);
        cmdigit6 = (TextView)findViewById(R.id.layout_cmdigit6).findViewById(R.id.tv_pass);
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

        cvv_digit1.setInputType(InputType.TYPE_CLASS_NUMBER);
        cvv_digit2.setInputType(InputType.TYPE_CLASS_NUMBER);
        cvv_digit3.setInputType(InputType.TYPE_CLASS_NUMBER);
        cvv_digit4.setInputType(InputType.TYPE_CLASS_NUMBER);

        showHide = (TextView)findViewById(R.id.tv_showHide);
        cshowHide = (TextView)findViewById(R.id.tv_cshowHide);
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
                break;
        }
    }

    private void setPass(String text) {
        if (titleString.equals("Setup Debit Card")) {
            if (flag == 11) {
                return;
            }
            if (flag == 1) {
                digit1.setText(text);
            } else if (flag == 2) {
                digit2.setText(text);
            } else if (flag == 3) {
                digit3.setText(text);
            } else if (flag == 4) {
                digit4.setText(text);
            } else if (flag == 5) {
                digit5.setText(text);
            } else if (flag == 6) {
                digit6.setText(text);
            } else if (flag == 7) {
                cvv_digit1.setText(text);
            } else if (flag == 8) {
                cvv_digit2.setText(text);
            } else if (flag == 9) {
                cvv_digit3.setText(text);
            } else if (flag == 10) {
                cvv_digit4.setText(text);
            }
            flag++;
            if (flag == 11) {
                Intent intent = new Intent(SetmPIN.this, SetmPIN.class);
                intent.putExtra("Tag", "Setup UPI MPIN");
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        }else if (titleString.matches("Setup UPI MPIN")){
            if (flag == 11) {
                return;
            }
            if (flag == 1) {
                otpdigit1.setInputType(InputType.TYPE_CLASS_NUMBER);
                otpdigit1.setText(text);
            } else if (flag == 2) {
                otpdigit2.setInputType(InputType.TYPE_CLASS_NUMBER);
                otpdigit2.setText(text);
            } else if (flag == 3) {
                otpdigit3.setInputType(InputType.TYPE_CLASS_NUMBER);
                otpdigit3.setText(text);
            } else if (flag == 4) {
                otpdigit4.setInputType(InputType.TYPE_CLASS_NUMBER);
                otpdigit4.setText(text);
            } else if (flag == 5) {
                mdigit1.setText(text);
            } else if (flag == 6) {
                mdigit2.setText(text);
            } else if (flag == 7) {
                mdigit3.setText(text);
            } else if (flag == 8) {
                mdigit4.setText(text);
            } else if (flag == 9) {
                mdigit5.setText(text);
            } else if (flag == 10) {
                mdigit6.setText(text);
            }
            flag++;
            if (flag == 11) {
                Intent intent = new Intent(SetmPIN.this, SetmPIN.class);
                intent.putExtra("Tag", "Confirm UPI MPIN");
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        }else{
            if (flag == 7) {
                return;
            }
            if (flag == 1) {
                cmdigit1.setText(text);
            } else if (flag == 2) {
                cmdigit2.setText(text);
            } else if (flag == 3) {
                cmdigit3.setText(text);
            } else if (flag == 4) {
                cmdigit4.setText(text);
            } else if (flag == 5) {
                cmdigit5.setText(text);
            } else if (flag == 6) {
                cmdigit6.setText(text);
            }
            flag++;
            if (flag == 7) { //TODO API - Set Credentials
                Intent intent = new Intent(SetmPIN.this, SuccessMPIN.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        }
    }

    private void removeFlag() {
        if (titleString.equals("Setup Debit Card")) {
            if (flag == 1) {
                return;
            }
            if (flag == 11) {
                cvv_digit4.setText("");
            } else if (flag == 10) {
                cvv_digit3.setText("");
            } else if (flag == 9) {
                cvv_digit2.setText("");
            } else if (flag == 8) {
                cvv_digit1.setText("");
            } else if (flag == 7) {
                digit6.setText("");
            } else if (flag == 6) {
                digit5.setText("");
            } else if (flag == 5) {
                digit4.setText("");
            } else if (flag == 4) {
                digit3.setText("");
            } else if (flag == 3) {
                digit2.setText("");
            } else if (flag == 2) {
                digit1.setText("");
            }
            flag--;
        }else if (titleString.equals("Setup UPI MPIN")){
            if (flag == 1) {
                return;
            }
            if (flag == 11) {
                mdigit6.setText("");
            } else if (flag == 10) {
                mdigit5.setText("");
            } else if (flag == 9) {
                mdigit4.setText("");
            } else if (flag == 8) {
                mdigit3.setText("");
            } else if (flag == 7) {
                mdigit2.setText("");
            } else if (flag == 6) {
                mdigit1.setText("");
            } else if (flag == 5) {
                otpdigit4.setText("");
            } else if (flag == 4) {
                otpdigit3.setText("");
            } else if (flag == 3) {
                otpdigit2.setText("");
            } else if (flag == 2) {
                otpdigit1.setText("");
            }
            flag--;
        }else {
            if (flag == 1) {
                return;
            }
            if (flag == 7) {
                cmdigit6.setText("");
            } else if (flag == 6) {
                cmdigit5.setText("");
            } else if (flag == 5) {
                cmdigit4.setText("");
            } else if (flag == 4) {
                cmdigit3.setText("");
            } else if (flag == 3) {
                cmdigit2.setText("");
            } else if (flag == 2) {
                cmdigit1.setText("");
            }
            flag--;
        }
    }
}
