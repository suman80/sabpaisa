package in.sabpaisa.droid.sabpaisa;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class SplashScreen extends AppCompatActivity {

   // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_splash);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        /*TextView   text =(TextView) findViewById(R.id.text);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                startActivity(intent);
            }
        });*/

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
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
                //Toast.makeText(SplashScreen.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(SplashScreen.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_PHONE_STATE/*, Manifest.permission.READ_SMS*/,/*Manifest.permission.ACCESS_COARSE_LOCATION,*/
                        Manifest.permission.READ_CONTACTS/*,Manifest.permission.READ_EXTERNAL_STORAGE*//*,Manifest.permission.WRITE_EXTERNAL_STORAGE*/
               /* ,Manifest.permission.CAMERA*/)
                .check();

    }



    }












