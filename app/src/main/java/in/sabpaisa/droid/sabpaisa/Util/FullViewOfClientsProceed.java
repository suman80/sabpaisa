package in.sabpaisa.droid.sabpaisa.Util;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.ViewPagerAdapter;
import in.sabpaisa.droid.sabpaisa.FeedData;
import in.sabpaisa.droid.sabpaisa.FeedsFragments;
import in.sabpaisa.droid.sabpaisa.FormFragment;
//import in.sabpaisa.droid.sabpaisa.Fragments.FeedFragments1;
import in.sabpaisa.droid.sabpaisa.Fragments.InstitutionFragment;
import in.sabpaisa.droid.sabpaisa.Fragments.ProceedFeedsFragments;
import in.sabpaisa.droid.sabpaisa.Fragments.ProceedGroupsFragments;
import in.sabpaisa.droid.sabpaisa.Fragments.ProceedInstitiutionFragment;
import in.sabpaisa.droid.sabpaisa.GroupListData;
import in.sabpaisa.droid.sabpaisa.GroupsFragments;
import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.Model.ContactList;
import in.sabpaisa.droid.sabpaisa.PayFeeFragment;
import in.sabpaisa.droid.sabpaisa.PayFragments;
import in.sabpaisa.droid.sabpaisa.R;

public class FullViewOfClientsProceed extends AppCompatActivity implements OnFragmentInteractionListener{
    ImageView clientImagePath;
    String clientName,state;
    public static String ClientId;
    public static String clientImageURLPath=null;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public static String MySharedPrefOnFullViewOfClientProceed="mySharedPrefForId";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_full_view_of_clients_proceed2);


        viewPager = (ViewPager) findViewById(R.id.viewpagerproceed);
        setupViewPager(viewPager);

        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = getIntent();


        clientName = intent.getStringExtra("clientName");
        state = intent.getStringExtra("state");
        clientImageURLPath= getIntent().getStringExtra("clientImagePath");
        ClientId=getIntent().getStringExtra("clientId");
        Log.d("ClientId_FVOCL"," "+ClientId);

        SharedPreferences.Editor editor = getSharedPreferences(MySharedPrefOnFullViewOfClientProceed,MODE_PRIVATE).edit();
        editor.putString("clientId",ClientId);
        editor.commit();

        TextView clientNameTextView = (TextView) findViewById(R.id.particular_client_name_proceed);

        TextView stateTextView = (TextView) findViewById(R.id.particular_client_address_proceed);


        clientImagePath = (ImageView)findViewById(R.id.ClientImagePRoceed);
        clientNameTextView.setText( clientName);
        stateTextView.setText(state);
        new DownloadImageTask(clientImagePath).execute(clientImageURLPath);



    }

 /*   @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }*/

    @Override
    public void onFragmentSetFeeds(ArrayList<FeedData> feedData) {

    }

    @Override
    public void onFragmentSetContacts(ArrayList<ContactList> contactLists) {

    }

    @Override
    public void onFragmentSetGroups(ArrayList<GroupListData> groupData) {

    }

    //Code for fetching image from server
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loading.show();
        }

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);

            //loading.dismiss();
        }

    }

    private void setupViewPager(ViewPager viewPager) {


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProceedFeedsFragments(),"Feeds"); //changing here creating different frags
        adapter.addFragment(new ProceedGroupsFragments(),"Groups");//changing here creating different frags
        adapter.addFragment(new PayFragments(),"Make Payment");
        viewPager.setAdapter(adapter);


        //in.beginTransaction().replace(R.id.activity_main_rfab, instituteFragment).commit();

    }


}
