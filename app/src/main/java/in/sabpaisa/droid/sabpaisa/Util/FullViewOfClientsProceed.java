package in.sabpaisa.droid.sabpaisa.Util;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.ViewPagerAdapter;
import in.sabpaisa.droid.sabpaisa.FeedData;
import in.sabpaisa.droid.sabpaisa.FeedsFragments;
import in.sabpaisa.droid.sabpaisa.FilterActivity;
import in.sabpaisa.droid.sabpaisa.FormFragment;
//import in.sabpaisa.droid.sabpaisa.Fragments.FeedFragments1;
import in.sabpaisa.droid.sabpaisa.Fragments.InstitutionFragment;
import in.sabpaisa.droid.sabpaisa.Fragments.ProceedFeedsFragments;
import in.sabpaisa.droid.sabpaisa.Fragments.ProceedGroupsFragments;
import in.sabpaisa.droid.sabpaisa.Fragments.ProceedInstitiutionFragment;
import in.sabpaisa.droid.sabpaisa.GroupListData;
import in.sabpaisa.droid.sabpaisa.GroupsFragments;
import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.MainActivity;
import in.sabpaisa.droid.sabpaisa.Members;
import in.sabpaisa.droid.sabpaisa.Model.ContactList;
import in.sabpaisa.droid.sabpaisa.PayFeeFragment;
import in.sabpaisa.droid.sabpaisa.PayFragments;
import in.sabpaisa.droid.sabpaisa.R;

import static in.sabpaisa.droid.sabpaisa.LogInActivity.PREFS_NAME;

public class FullViewOfClientsProceed extends AppCompatActivity implements OnFragmentInteractionListener,NavigationView.OnNavigationItemSelectedListener, AppBarLayout.OnOffsetChangedListener {
    ImageView clientImagePath;
    String clientName,state,landingPage;
    public static String ClientId;
    public static String clientImageURLPath=null;
    private ViewPager viewPager;
    ActionBarDrawerToggle toggle;
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    AppBarLayout appBarLayout;

    private TabLayout tabLayout;

    public static String MySharedPrefOnFullViewOfClientProceed="mySharedPrefForId";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_nagationfullview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setHomeAsUpIndicator(R.drawable.ic_drawer);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sabpaisa");
        mCollapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitleEnabled(false);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        appBarLayout.addOnOffsetChangedListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewpagerproceed);
        setupViewPager(viewPager);

        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = getIntent();

        landingPage =getIntent().getStringExtra("landingPage");
        Log.d("page",""+landingPage);

        /*Bundle bundle = new Bundle();
        bundle.putString("landingPage", "From Activity");
// set Fragmentclass Arguments
        PayFragments fragobj = new PayFragments();
        fragobj.setArguments(bundle);*/

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

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.coa_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        //noinspection SimplifiableIfStatement
        /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        android.app.Fragment newFragment = null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Profile) {
            Intent intent=new Intent(FullViewOfClientsProceed.this, ProfileNavigationActivity.class);

            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_Chat) {

        } else if (id == R.id.nav_Settings) {
            Intent intent=new Intent(FullViewOfClientsProceed.this, SettingsNavigationActivity.class);

            startActivity(intent);

        }
        else  if(id == R.id.nav_ChangePassword)
        {
            Intent intent=new Intent(FullViewOfClientsProceed.this, ForgotActivity.class);

            startActivity(intent);
        }
        else  if(id == R.id.nav_Privacy_Policy)
        {
            Intent intent=new Intent(FullViewOfClientsProceed.this, PrivacyPolicyActivity.class);

            startActivity(intent);
        }
        else if (id == R.id.nav_Settings) {
            Intent intent=new Intent(FullViewOfClientsProceed.this, SettingsNavigationActivity.class);

            startActivity(intent);



        }

        else if (id == R.id.nav_logout) {

            AlertDialog.Builder builder=new AlertDialog.Builder(FullViewOfClientsProceed.this); //Home is name of the activity
            builder.setMessage("Do you want to Logout?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.remove("logged");
                    editor.commit();
                    finish();
                    Intent intent=new Intent(FullViewOfClientsProceed.this, LogInActivity.class);

                    startActivity(intent);



                }
            });

            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog alert=builder.create();
            alert.show();



        }else if (id == R.id.nav_share) {
            /*Intent intent=new Intent(MainActivity.this, ShareActivity.class);

            startActivity(intent);*/
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Sabpaisa App");
                String sAux = "\n Let me recommend you this application .\n this is the easy way to pay your fee\n It is very cool app try it once ,download it from the below link given... \n \n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=Orion.Soft \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Complete action using "));
            } catch (Exception e) {
                //e.toString();
            }


        } else if (id == R.id.nav_rate) {
            Intent intent=new Intent(FullViewOfClientsProceed.this, RateActivity.class);

            startActivity(intent);

/*
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.tt, newFragment)
                    .commit();*/
            /*newFragment = new Ratefragment();
            transaction.replace(R.id.tt, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();*/

            /*Fragment fragment = new Fragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.tt, fragment, fragment.getTag()).commit();
        */
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        adapter.addFragment(new PayFragments(),"Payment");
        adapter.addFragment(new Members(),"Members");
        viewPager.setAdapter(adapter);


        //in.beginTransaction().replace(R.id.activity_main_rfab, instituteFragment).commit();

    }


}
