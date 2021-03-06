package in.sabpaisa.droid.sabpaisa;

import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

import in.sabpaisa.droid.sabpaisa.Adapter.MemberAdapter;

public class MembersProfile extends AppCompatActivity {
String name,image,emailid,mobno;

TextView name_Tv,email_Tv,mobNo_tv,name11;
Toolbar toolbar;
ImageView userimage,coverpic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_profile);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("Sabpaisa");
        //toolbar.setNavigationIcon(R.drawable.ic_navigation);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(" cvxcfgbfhg","549");
                MemberAdapter.isClicked =false;
                onBackPressed();
            }
        });

        name_Tv=(TextView)findViewById(R.id.UserName);
        name11=(TextView)findViewById(R.id.tv_userName);
        mobNo_tv=(TextView)findViewById(R.id.et_mNumber);
        email_Tv=(TextView)findViewById(R.id.emailId);
        userimage=(ImageView) findViewById(R.id.iv_userImage);
        coverpic=(ImageView) findViewById(R.id.coverr);


        //  toolbar.setTitle("Profile");
        //toolbar.setTitle("Profile");
        //toolbar.setNavigationIcon(R.drawable.ic_action_previousback);
       /// toolbar.setNavigationOnClickListener(new View.OnClickListener(){
         /*   @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });*/



        name=getIntent().getStringExtra("name1");
        image=getIntent().getStringExtra("image1");
        emailid=getIntent().getStringExtra("emailid1");
        mobno=getIntent().getStringExtra("mobNo1");
        Log.d("MembersProfilename",""+name);
        Log.d("MembersProfilename",""+image);
        Log.d("MembersProfilename",""+mobno);

        mobNo_tv.setText(mobno);
        name_Tv.setText(name);
        name11.setText(name);
        email_Tv.setText(emailid);
        Glide.with(MembersProfile.this)
                .load(image)
                .error(R.drawable.default_users)
                .into(userimage);
        Glide.with(MembersProfile.this)
                .load(image)
                .error(R.drawable.default_users)
                .into(coverpic);
    }

    @Override
    public void onBackPressed() {
        Log.d("onBackPressed","549");
        MemberAdapter.isClicked =false;
        super.onBackPressed();
    }
}
