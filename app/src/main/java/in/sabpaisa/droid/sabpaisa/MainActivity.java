package in.sabpaisa.droid.sabpaisa;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.media.tv.TvContract;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.res.ResourcesCompat;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.braunster.androidchatsdk.firebaseplugin.firebase.BChatcatNetworkAdapter;
import com.braunster.chatsdk.Utils.helper.ChatSDKUiHelper;
import com.braunster.chatsdk.activities.ChatSDKLoginActivity;
import com.braunster.chatsdk.network.BNetworkManager;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.androidbucket.utils.imageprocess.ABShape;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.sabpaisa.droid.sabpaisa.Adapter.ViewPagerAdapter;
import in.sabpaisa.droid.sabpaisa.Fragments.InstitutionFragment;
import in.sabpaisa.droid.sabpaisa.Fragments.ProceedInstitiutionFragment;
import in.sabpaisa.droid.sabpaisa.Model.Institution;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;
import in.sabpaisa.droid.sabpaisa.Util.CustomSliderView;
import in.sabpaisa.droid.sabpaisa.Util.CustomViewPager;
import in.sabpaisa.droid.sabpaisa.Util.ForgotActivity;
import in.sabpaisa.droid.sabpaisa.Util.LogoutNavigationActivity;
import in.sabpaisa.droid.sabpaisa.Util.PrivacyPolicyActivity;
import in.sabpaisa.droid.sabpaisa.Util.ProfileNavigationActivity;
import in.sabpaisa.droid.sabpaisa.Util.RateActivity;
import in.sabpaisa.droid.sabpaisa.Util.SettingsNavigationActivity;
import in.sabpaisa.droid.sabpaisa.Util.ShareActivity;

import static android.view.View.GONE;
import static in.sabpaisa.droid.sabpaisa.LogInActivity.PREFS_NAME;

public class MainActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener,NavigationView.OnNavigationItemSelectedListener,BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    private SliderLayout mHeaderSlider;
    ArrayList<Integer> headerList = new ArrayList<>();
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    AppBarLayout appBarLayout;
    private CustomViewPager viewPager;
    private TabLayout tabLayout;
    Toolbar toolbar;
    ImageView sendMoney, requestMoney,socialPayment,transaction,profile,bank,UpibankList,mPinInfo,mPinInfo2;
    LinearLayout paymentButton,chatButton,memberButton;
    int isMpinSet=1;
    FloatingActionButton fab;
    ActionBarDrawerToggle toggle;

    HashMap<String,String> Hash_file_maps;
    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaBtn;
    private RapidFloatingActionHelper rfabHelper;
    Bundle bundle;
    String stateName,serviceName,ClientId;
    public  static  String MYSHAREDPREF="mySharedPref";


////Testing
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //checking
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);



//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_navigation);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Sabpaisa");


/*
        Context context = getApplicationContext();

// Create a new configuration
        Configuration.Builder builder = new Configuration.Builder(context);

// Perform any configuration steps (optional)
        builder.firebaseRootPath("prod");

// Initialize the Chat SDK
        ChatSDK.initialize(builder.build());
        UserInterfaceModule.activate(context);

// Activate the Firebase module
        FirebaseModule.activate();

// File storage is needed for profile image upload and image messages
        FirebaseFileStorageModule.activate();


*/



        // This is used for the app custom toast and activity transition
        ChatSDKUiHelper.initDefault();

// Init the network manager
        BNetworkManager.init(getApplicationContext());

// Create a new adapter
        BChatcatNetworkAdapter adapter = new BChatcatNetworkAdapter(getApplicationContext());

// Set the adapter
        BNetworkManager.sharedManager().setNetworkAdapter(adapter);




        //mDrawerToggle=(ActionBarDrawerToggle)findViewById(R.id.nav)
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawer,
                getApplicationContext().getTheme());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setHomeAsUpIndicator(R.drawable.ic_drawer);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        sendMoney = (ImageView)findViewById(R.id.ll_send);
        requestMoney = (ImageView)findViewById(R.id.ll_request);
        socialPayment = (ImageView)findViewById(R.id.ll_social_payment);
        transaction = (ImageView)findViewById(R.id.ll_transactions);
        profile = (ImageView)findViewById(R.id.ll_profile);
        bank = (ImageView)findViewById(R.id.ll_bank);
        UpibankList = (ImageView)findViewById(R.id.ll_Upibank);
        paymentButton = (LinearLayout)findViewById(R.id.payment_button);
        chatButton = (LinearLayout)findViewById(R.id.chat);
        memberButton = (LinearLayout)findViewById(R.id.members);
        rfaLayout = (RapidFloatingActionLayout)findViewById(R.id.activity_main_rfal);
        rfaBtn = (RapidFloatingActionButton)findViewById(R.id.activity_main_rfab);
        FabButtonCreate();
        //fab = (FloatingActionButton)findViewById(R.id.fab_dashboard);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sabpaisa");
        mCollapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitleEnabled(false);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        appBarLayout.addOnOffsetChangedListener(this);

        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.disableScroll(true);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setSupportActionBar(toolbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitleEnabled(false);


        mHeaderSlider = (SliderLayout)findViewById(R.id.slider);

        /*stateName=getIntent().getStringExtra("STATENAME");
        serviceName=getIntent().getStringExtra("SERVICENAME");*/
        ClientId=getIntent().getStringExtra("clientId");

        /*Log.d("stateName11111"," "+stateName);
        Log.d("serviceName1111"," "+serviceName);*/

        Log.d("CLIENTID(MainActivity)","-->"+ClientId);

        SharedPreferences.Editor editor = getSharedPreferences(MYSHAREDPREF,MODE_PRIVATE).edit();
        //editor.putString("STATENAME",stateName);
        //editor.putString("SERVICENAME",serviceName);
        editor.putString("clientId",ClientId);
        editor.commit();
        LoadHeaderImageList();
        setHeaderImageList();

        sendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMpinSet==0) {             /*TODO check if mpin is set or not, for now i am hardcoding it*/
                    Intent intent = new Intent(MainActivity.this, AccountInfoActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
                }else {
                    Intent intent = new Intent(MainActivity.this, SendMoneyActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
                }
            }
        });

        requestMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RequestMoney.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UPI_UserAccounts.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        });

        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AccountInfoActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        });

        UpibankList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UPIBankList.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        });


        transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TransactionsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        });
        socialPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SocialPayment.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ChatSDKLoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        });

        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void FabButtonCreate() {
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
//        rfaContent.setOnRapidFloatingActionContentListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        Bitmap send = BitmapFactory.decodeResource(getResources(),
                R.drawable.send_icon);
        items.add(new RFACLabelItem<Integer>()
                .setResId(R.mipmap.ic_transactions_icon)
                .setIconNormalColor(getResources().getColor(R.color.bg_orange))
                .setIconPressedColor(getResources().getColor(R.color.bg_orange))
                .setWrapper(1)
        );
        items.add(new RFACLabelItem<Integer>()
                .setResId(R.mipmap.ic_request_icon)
                .setIconNormalColor(getResources().getColor(R.color.bg_orange))
                .setIconPressedColor(getResources().getColor(R.color.bg_orange))
                .setWrapper(2)
        );
        items.add(new RFACLabelItem<Integer>()
                .setResId(R.mipmap.ic_send_icon)
                .setIconNormalColor(getResources().getColor(R.color.bg_orange))
                .setIconPressedColor(getResources().getColor(R.color.bg_orange))
                .setWrapper(3)
        );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(ABTextUtil.dip2px(this, 5))
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(ABTextUtil.dip2px(this, 5))
        ;
        rfabHelper = new RapidFloatingActionHelper(
                this,
                rfaLayout,
                rfaBtn,
                rfaContent
        ).build();
    }
    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        Toast.makeText(this, "clicked label: " + position, Toast.LENGTH_SHORT).show();
        rfabHelper.toggleContent();
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        if (position==2){
            Toast.makeText(this, "Send Clicked", Toast.LENGTH_SHORT).show();
            if (isMpinSet==0) {             /*TODO check if mpin is set or not, for now i am hardcoding it*/
                Intent intent = new Intent(MainActivity.this, AccountInfoActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }else {
                Intent intent = new Intent(MainActivity.this, SendMoneyActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        }else if (position==1){
            Toast.makeText(this, "Request Clicked", Toast.LENGTH_SHORT).show();
        }else if (position==0){
            Toast.makeText(this, "Transactions Clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, TransactionsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
        }
        rfabHelper.toggleContent();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProceedInstitiutionFragment(),"Clients");
        adapter.addFragment(new FormFragment(),"Other Clients");
        //adapter.addFragment(new InstitutionFragment(),"Groups");
        viewPager.setAdapter(adapter);
        stateName=getIntent().getStringExtra("STATENAME");
        serviceName=getIntent().getStringExtra("SERVICENAME");

        FragmentManager in=getSupportFragmentManager();
        Fragment instituteFragment=new InstitutionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("stateName", stateName);
        bundle.putString("serviceName",serviceName);
        instituteFragment.setArguments(bundle);
        //in.beginTransaction().replace(R.id.activity_main_rfab, instituteFragment).commit();

    }

    private void setHeaderImageList() {
        for(int i=0;i<headerList.size();i++){
            CustomSliderView customSliderView = new CustomSliderView(this);
            // initialize a SliderLayout
            customSliderView
                    .image(headerList.get(i))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            //TODO URL
                        }
                    });
            mHeaderSlider.addSlider(customSliderView);
        }
        mHeaderSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mHeaderSlider.setBackgroundColor(getResources().getColor(R.color.main_screen_bottom_color));
        mHeaderSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mHeaderSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
//        mHeaderSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
    }

    private void LoadHeaderImageList() {



        Hash_file_maps = new HashMap<String, String>();


        Hash_file_maps.put("Sabpaisa", "http://205.147.103.27:6060/Docs/Images/HomeImage/sabpaisa.png");
        Hash_file_maps.put("Sabpaisa.", "http://205.147.103.27:6060/Docs/Images/HomeImage/UPI_2.png");
        Hash_file_maps.put("Sabpaisa..", "http://205.147.103.27:6060/Docs/Images/HomeImage/UPI_image.jpg");
        Hash_file_maps.put("Sabpaisa...", "http://205.147.103.27:6060/Docs/Images/HomeImage/UPI_1.svg.png");
        for(String name : Hash_file_maps .keySet())
        {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(Hash_file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mHeaderSlider.addSlider(textSliderView);
        }
 /*       headerList.add(R.drawable.test_header600240);
        headerList.add(R.drawable.test_header600241);
        headerList.add(R.drawable.test_header600242);
        headerList.add(R.drawable.test_header600243);*/
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0)
        {
            rfaLayout.setVisibility(GONE);
            //fab.setVisibility(View.GONE);// Collapsed
        }
        else
        {
            rfaLayout.setVisibility(View.VISIBLE);
            //fab.setVisibility(View.VISIBLE);// Not collapsed
        }
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
            Intent intent=new Intent(MainActivity.this, ProfileNavigationActivity.class);

            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_Chat) {

        } else if (id == R.id.nav_Settings) {
            Intent intent=new Intent(MainActivity.this, SettingsNavigationActivity.class);

            startActivity(intent);

        }
        else  if(id == R.id.nav_ChangePassword)
        {
            Intent intent=new Intent(MainActivity.this, ForgotActivity.class);

            startActivity(intent);
        }
        else  if(id == R.id.nav_Privacy_Policy)
        {
            Intent intent=new Intent(MainActivity.this, PrivacyPolicyActivity.class);

            startActivity(intent);
        }
        else if (id == R.id.nav_Settings) {
            Intent intent=new Intent(MainActivity.this, SettingsNavigationActivity.class);

            startActivity(intent);



        }

        else if (id == R.id.nav_logout) {

            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this); //Home is name of the activity
            builder.setMessage("Do you want to Logout?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.remove("logged");
                    editor.commit();
                    finish();
                    Intent intent=new Intent(MainActivity.this, LogInActivity.class);

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
            Intent intent=new Intent(MainActivity.this, RateActivity.class);

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

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void initToolBar(String title) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);

        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.threelines);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(MainActivity.this, "clicking the toolbar!", Toast.LENGTH_SHORT).show();
                    }
                }

        );
    }
}
