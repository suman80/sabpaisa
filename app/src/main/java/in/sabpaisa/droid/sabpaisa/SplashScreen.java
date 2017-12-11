package in.sabpaisa.droid.sabpaisa;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;


public class SplashScreen extends AppCompatActivity {

   // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_splash);


/*TextView   text =(TextView) findViewById(R.id.text);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashScreen.this,MainActivity.class);

                startActivity(intent);

            }
        });*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                // For MI user
                // Intent i = new Intent(SplashScreen.this, FilterActivity.class);
               Intent i = new Intent(SplashScreen.this, LogInActivity.class);
                startActivity(i);

                /*Intent i = new Intent(SplashScreen.this, UpiSdkHandshake.class);
                startActivity(i);*/

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }



    }












