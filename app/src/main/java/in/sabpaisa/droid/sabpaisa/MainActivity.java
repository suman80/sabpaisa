package in.sabpaisa.droid.sabpaisa;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.braunster.androidchatsdk.firebaseplugin.firebase.BChatcatNetworkAdapter;
import com.braunster.chatsdk.Utils.helper.ChatSDKUiHelper;
import com.braunster.chatsdk.activities.ChatSDKLoginActivity;
import com.braunster.chatsdk.network.BNetworkManager;
import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.sabpaisa.droid.sabpaisa.Adapter.ViewPagerAdapter;
import in.sabpaisa.droid.sabpaisa.Fragments.InstitutionFragment;
import in.sabpaisa.droid.sabpaisa.Fragments.ProceedInstitiutionFragment;
import in.sabpaisa.droid.sabpaisa.Model.ClientData;
import in.sabpaisa.droid.sabpaisa.Model.FetchUserImageGetterSetter;
import in.sabpaisa.droid.sabpaisa.Model.TransactionreportModelClass;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;
import in.sabpaisa.droid.sabpaisa.Util.CustomSliderView;
import in.sabpaisa.droid.sabpaisa.Util.CustomViewPager;
import in.sabpaisa.droid.sabpaisa.Util.ForgotActivity;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;
import in.sabpaisa.droid.sabpaisa.Util.LogoutNavigationActivity;
import in.sabpaisa.droid.sabpaisa.Util.PrivacyPolicyActivity;
import in.sabpaisa.droid.sabpaisa.Util.ProfileNavigationActivity;
import in.sabpaisa.droid.sabpaisa.Util.RateActivity;
import in.sabpaisa.droid.sabpaisa.Util.SettingsNavigationActivity;

import static android.view.View.GONE;
import static in.sabpaisa.droid.sabpaisa.CommentAdapterDatabase.context;
import static in.sabpaisa.droid.sabpaisa.LogInActivity.PREFS_NAME;

public class MainActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener,NavigationView.OnNavigationItemSelectedListener,BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    private SliderLayout mHeaderSlider;
    ArrayList<Integer> headerList = new ArrayList<>();
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    AppBarLayout appBarLayout;
    private CustomViewPager viewPager;
    private TabLayout tabLayout;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    ImageView niv;
    TextView usernameniv,mailIdniv;
    Toolbar toolbar;
    String n,m;
    private static int CODE = 1; //declare as FIELD
    private FirebaseAnalytics firebaseAnalytics;
    NetworkImageView nav;
    String userImageUrl=null;
    String response,response1,userAccessToken;
    ImageView sendMoney, requestMoney,socialPayment,transaction,profile,bank,UpibankList,mPinInfo,mPinInfo2;
    LinearLayout paymentButton,chatButton,memberButton;
    int isMpinSet=1;
    FloatingActionButton fab;
    ActionBarDrawerToggle toggle;
 //public  static String userImageUrl=null;
    HashMap<String,String> Hash_file_maps;
    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaBtn;
    private RapidFloatingActionHelper rfabHelper;
    Bundle bundle;
    String x;
    String stateName,serviceName,ClientId;
    String stateName1,serviceName1,ClientId1;

    public  static  String MYSHAREDPREF="mySharedPref";

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

////Testing
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //checking
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);

//nav=(NetworkImageView)findViewById(R.id.profile_image);

//        requestW
// indowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_navigation);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Sabpaisa");

       // toggle.setHomeAsUpIndicator(R.drawable.ic_drawer);



        SharedPreferences sharedPreferences2 = getApplication().getSharedPreferences(UIN.MYSHAREDPREFUIN, Context.MODE_PRIVATE);
        n=sharedPreferences2.getString("m","xyz");
        m=n;
        ClientId1=sharedPreferences2.getString("clientId", "123");

        ClientId=ClientId1;


        SharedPreferences sharedPreferences1 = getApplication().getSharedPreferences(FilterActivity.MySharedPreffilter, Context.MODE_PRIVATE);
        stateName1=sharedPreferences1.getString("selectedstate", "abc");
        stateName=stateName1;
        serviceName1=sharedPreferences1.getString("selectedservice", "123");
        serviceName=serviceName1;
        //ClientId1=sharedPreferences1.getString("stateId", "123");
      /*  ClientId1=sharedPreferences1.getString("clientId", "123");

        ClientId=ClientId1;*/

        Log.d("Sharedprefrencemat","-."+ClientId1+stateName1+serviceName1);
        Log.d("Sharedprefrencemat","-."+ClientId+stateName+serviceName);
        Log.d("Sharedprefrencemat","-."+ClientId+stateName+serviceName+n+m);


        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        response = sharedPreferences.getString("response", "123");

        if(response!=null) {

            userAccessToken = response;
            Log.d("AccessToken111", " " + userAccessToken);

            Log.d("FFResponse11111", " " + response);
        }else {
            try{

            }catch (NullPointerException e){
                // [START log_and_report]
                FirebaseCrash.logcat(Log.ERROR, "MainActivity", "NPE caught");
                FirebaseCrash.report(e);
            }
        }

        //getUserIm(userAccessToken);
        //new DownloadImageTask(nav).execute(userImageUrl);
/*
        Picasso.with(this).load(userImageUrl).into(nav);
*/

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
       // ClientId=getIntent().getStringExtra("clientId");
       // userImageUrl=getIntent().getStringExtra("userImageUrl");

        /*Log.d("stateName11111"," "+stateName);
        Log.d("serviceName1111"," "+serviceName);*/

        Log.d("CLIENTID(MainActivity)","-->"+ClientId);
        Log.d("userImageUrl(MainAhjhkn","-->"+userImageUrl);
      /*  Intent intent=new Intent(MainActivity.this, FullViewOfClientsProceed.class);
        intent.putExtra("userImageUrl",userImageUrl);*/


      //Firebase Analytics
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        ClientData clientData=new ClientData();
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, clientData.getClientId());
       // bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, clientData.getClientName());

        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        //Sets whether analytics collection is enabled for this app on this device.
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);

        //Sets the minimum engagement time required before starting a session. The default value is 10000 (10 seconds). Let's make it 20 seconds just for the fun
        firebaseAnalytics.setMinimumSessionDuration(20000);

        //Sets the duration of inactivity that terminates the current session. The default value is 1800000 (30 minutes).
        firebaseAnalytics.setSessionTimeoutDuration(500);


        //Sets the user ID property.
        firebaseAnalytics.setUserId(String.valueOf(clientData.getClientId()));

        //Sets a user property to a given value.
        //firebaseAnalytics.setUserProperty("Client",clientData.getClientName());


        // Firebase push notification

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    //txtMessage.setText(message);
                }
            }
        };

        displayFirebaseRegId();




        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
       //View header = navigationView.inflateHeaderView(R.layout.nav_header_main_activity_navigation);
        //nav = (NetworkImageView) header.findViewById(R.id.profile_image);

         niv = (ImageView)navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        usernameniv = (TextView)navigationView.getHeaderView(0).findViewById(R.id.username_nav);
        usernameniv.setText("");

        mailIdniv = (TextView)navigationView.getHeaderView(0).findViewById(R.id.email_nav);
        // View header = navigationView.getHeaderView(0);
      // NetworkImageView niv = (NetworkImageView) header.findViewById(R.id.profile_image);

        //if(url.length() > 0)
            //niv.setImageUrl(userImageUrl, imageLoader);
       // niv.setDefaultImageResId(R.drawable.sabpaisa);
        //niv.setErrorImageResId(R.drawable.
        //error);
        //nav.setImageUrl(userImageUrl);

      //  new MainActivity.DownloadImageTask(niv).execute(userImageUrl);

        sendMoney = (ImageView)findViewById(R.id.ll_send);
        requestMoney = (ImageView)findViewById(R.id.ll_request);
        //socialPayment = (ImageView)findViewById(R.id.ll_social_payment);
        transaction = (ImageView)findViewById(R.id.ll_transactions);
        profile = (ImageView)findViewById(R.id.ll_profile);
        bank = (ImageView)findViewById(R.id.ll_bank);
        //UpibankList = (ImageView)findViewById(R.id.ll_Upibank);
        paymentButton = (LinearLayout)findViewById(R.id.payment_button);
        chatButton = (LinearLayout)findViewById(R.id.chat);
        memberButton = (LinearLayout)findViewById(R.id.members);
        rfaLayout = (RapidFloatingActionLayout)findViewById(R.id.activity_main_rfal);
        rfaBtn = (RapidFloatingActionButton)findViewById(R.id.activity_main_rfab);
        FabButtonCreate();
        //fab = (FloatingActionButton)findViewById(R.id.fab_dashboard);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sabpaisa");
        toolbar.setNavigationIcon(R.drawable.ic_navigation);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
onBackPressed();            }
        });

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
   /*     ClientId=getIntent().getStringExtra("clientId");
        userImageUrl=getIntent().getStringExtra("userImageUrl");

        *//*Log.d("stateName11111"," "+stateName);
        Log.d("serviceName1111"," "+serviceName);*//*

        Log.d("CLIENTID(MainActivity)","-->"+ClientId);
        Log.d("userImageUrl(MainAhjhkn","-->"+userImageUrl);
*/

       //new MainActivity.DownloadImageTask(nav).execute(userImageUrl);
        SharedPreferences.Editor editor = getSharedPreferences(MYSHAREDPREF,MODE_PRIVATE).edit();
        editor.putString("clientId",ClientId);
        //editor.putString("userImageUrl",userImageUrl);
        editor.commit();
        LoadHeaderImageList();
        setHeaderImageList();

        sendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (isMpinSet==0) {             *//*TODO check if mpin is set or not, for now i am hardcoding it*//*
                    Intent intent = new Intent(MainActivity.this, AccountInfoActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
                }else {
                    Intent intent = new Intent(MainActivity.this, SendMoneyActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
                }*/

                Toast.makeText(getApplicationContext(), "Coming Soon !", Toast.LENGTH_SHORT).show();

            }
        });


        requestMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(MainActivity.this, RequestMoney.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);*/

                Toast.makeText(getApplicationContext(), "Coming Soon !", Toast.LENGTH_SHORT).show();

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(MainActivity.this, UPI_UserAccounts.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);*/
                Toast.makeText(getApplicationContext(), "Coming Soon !", Toast.LENGTH_SHORT).show();
            }
        });

        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(MainActivity.this, AstatccountInfoActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);*/
                Toast.makeText(getApplicationContext(), "Coming Soon !", Toast.LENGTH_SHORT).show();
            }
        });

      /*  UpibankList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UPIBankList.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        });
*/

        transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(MainActivity.this, TransactionsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);*/
                Toast.makeText(getApplicationContext(), "Coming Soon !", Toast.LENGTH_SHORT).show();
            }
        });
        /*socialPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SocialPayment.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        });*/

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int value=1;

                Intent intent = new Intent(MainActivity.this,ChatSDKLoginActivity.class);

                intent.putExtra("userImageUrl",userImageUrl);
                intent.putExtra("VALUE",value);

                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        });
        getUserIm(userAccessToken);
        showProfileData();
        String y=x;
/*g.d("xmailidmain",""+y);
Log.d("xmailidmain",""+x);*/
        //mailIdniv.setText(x);

        usernameniv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ProfileNavigationActivity.class);
                intent.putExtra("ClientId",ClientId);
                startActivity(intent);

            }
        });

        niv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ProfileNavigationActivity.class);
                intent.putExtra("ClientId",ClientId);
                startActivity(intent);

            }
        });

        mailIdniv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ProfileNavigationActivity.class);
                intent.putExtra("ClientId",ClientId);
                startActivity(intent);

            }
        });

    }


    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Log.d("Fbid", "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId)) {
            //Toast.makeText(this, "Firebase Reg Id: " + regId, Toast.LENGTH_SHORT).show();
        }

            //txtRegId.setText("Firebase Reg Id: " + regId);
        else {

            //Toast.makeText(this, "Firebase Reg Id is not received yet!" + regId, Toast.LENGTH_SHORT).show();
        }
        //txtRegId.setText("Firebase Reg Id is not received yet!");*/
    }

    @Override
    public void onBackPressed() {
       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } *//*else if(m=="abc") {
            super.onBackPressed();
            Intent intent=new Intent(MainActivity.this,UIN.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);

        */


            finish();
            moveTaskToBack(true);
            System.exit(0);


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
        //adapter.addFragment(new FormFragment(),"Other Clients");
        //adapter.addFragment(new InstitutionFragment(),"Groups");
        viewPager.setAdapter(adapter);
        stateName=getIntent().getStringExtra("STATENAME");
        serviceName=getIntent().getStringExtra("SERVICENAME");

        FragmentManager in=getSupportFragmentManager();
        Fragment instituteFragment=new InstitutionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("stateName", stateName);
        bundle.putString("serviceName",serviceName);
        //bundle.putString("userImageUrl",userImageUrl);
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


    Hash_file_maps.put("Sabpaisa Digitizing Cash", AppConfig.Base_Url+"/Docs/Images/HomeImage/sabpaisa.png");
        Hash_file_maps.put("Payment & Transfer", AppConfig.Base_Url+"/Docs/Images/HomeImage/UPI_2.png");
        Hash_file_maps.put("The Future Of Payments", AppConfig.Base_Url+"/Docs/Images/HomeImage/UPI_image.jpg");
        Hash_file_maps.put("UPI", AppConfig.Base_Url+"/Docs/Images/HomeImage/UPI_1.svg.png");
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
        getMenuInflater().inflate(R.menu.main_activity_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

       /* case R.id.action_profile:
        intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
        return true;
        case R.id.action_payment:
        intent = new Intent(MainActivity.this, Payment.class);
        startActivity(intent);
        //Toast.makeText(this, "Feature Coming soon", Toast.LENGTH_SHORT).show();
        return true;
        case R.id.action_chat:
        intent = new Intent(MainActivity.this, ChatSDKLoginActivity.class);
        startActivity(intent);
        return true;*/


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
            intent.putExtra("ClientId",ClientId);
            startActivity(intent);

            //startActivity(intent,CODE);
            // Handle the camera action
        } /*else if (id == R.id.nav_Chat) {

        } *//*else if (id == R.id.nav_Settings) {
            Intent intent=new Intent(MainActivity.this, SettingsNavigationActivity.class);

            startActivity(intent);

        }*/

        else if(id==R.id.nav_TransactionReport){
            Intent intent=new Intent(MainActivity.this, TransactionReportNav.class);
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
        /*else if (id == R.id.nav_Settings) {
            Intent intent=new Intent(MainActivity.this, SettingsNavigationActivity.class);

            startActivity(intent);



        }*/

        else if (id == R.id.nav_logout) {

            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this); //Home is name of the activity
            builder.setMessage("Do you want to Logout?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    SharedPreferences settings1 = getSharedPreferences(UIN.MYSHAREDPREFUIN, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = settings1.edit();
                    editor1.remove("m");
                    editor1.commit();
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
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Sabpaisa App");
                String sAux = "\n Let me recommend you this application .\n this is the easy way to pay your fee\n It is very cool app try it once ,download it from the below link given... \n \n";
                sAux = sAux + "https://www.sabpaisa.in/QwikCollect/sabpaisa \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Complete action using "));
            } catch (Exception e) {


                //e.toString();
            }


        }
        else if(id==R.id.nav_clean_data)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this); //Home is name of the activity
            builder.setMessage("For Selecting any other client .Press OK ");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {


                    SharedPreferences settings = getSharedPreferences(UIN.MYSHAREDPREFUIN, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.remove("m");
                    editor.remove("selectedstate");
                    editor.remove("selectedservice");
                    editor.remove("logged");
                    editor.clear();
                    editor.commit();
                    finish();

                    Intent intent=new Intent( MainActivity.this, FilterActivity.class);

                    startActivity(intent);

                    /*SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.remove("logged");
                    editor.commit();
                    finish();
                    Intent intent=new Intent(MainActivity.this, LogInActivity.class);

                    startActivity(intent);
*/


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

        }
            else if (id == R.id.nav_rate) {
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

/*
    private void getUserIm(final  String token) {

        String  tag_string_req = "req_clients";

        StringRequest request=new StringRequest(Request.Method.GET,AppConfig.URL_UserProfilephot   +token, new Response.Listener<String>(){

            @Override
            public void onResponse(String response1)
            {

                Log.d("Particularclientimage","-->"+response1);
                //parsing Json
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response1.toString());
                    String response = jsonObject.getString("response");
                    String status = jsonObject.getString("status");
                    Log.d("responsus",""+response);
                    Log.d("statsus",""+status);
                    JSONObject jsonObject1 = new JSONObject(response);
                    FetchUserImageGetterSetter fetchUserImageGetterSetter=new FetchUserImageGetterSetter();fetchUserImageGetterSetter.setUserImageUrl(jsonObject1.getString("userImageUrl"));
                    userImageUrl=fetchUserImageGetterSetter.getUserImageUrl().toString();
                    Log.d("userImageUrlactivity",""+userImageUrl);
                 */
/*   ClientData clientData=new ClientData();
                    clientData.setClientLogoPath(jsonObject1.getString("clientLogoPath"));
                    clientData.setClientImagePath(jsonObject1.getString("clientImagePath"));
                    clientData.setClientName(jsonObject1.getString("clientName"));

                    clientLogoPath=clientData.getClientLogoPath().toString();
                    clientImagePath=clientData.getClientImagePath().toString();
                    clientname11=clientData.getClientName().toString();
                    // clientname=clientData.getClientName().toString();
                    Log.d("clientlogooooo","-->"+clientLogoPath );
                    Log.d("clientimageooo","-->"+clientImagePath );
                    Log.d("clientiooo","-->"+clientname11 );*//*



                }
                catch(JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage()==null ||error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getApplication(), R.style.MyDialogTheme).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Network/Connection Error");

                    // Setting Dialog Message
                    alertDialog.setMessage("Internet Connection is poor OR The Server is taking too long to respond.Please try again later.Thank you.");

                    // Setting Icon to Dialog
                    //  alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                    //Log.e(TAG, "Registration Error: " + error.getMessage());

                } else if (error instanceof AuthFailureError) {

                    //TODO
                } else if (error instanceof ServerError) {

                    //TODO
                } else if (error instanceof NetworkError) {

                    //TODO
                } else if (error instanceof ParseError) {

                    //TODO
                }


            }


        }) ;

        AppController.getInstance().addToRequestQueue(request,tag_string_req);


    }
*/



    private class DownloadLogoTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loading.show();
        }

        public DownloadLogoTask(ImageView bmImage) {
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


    //Firebase Notification


    // Fetches reg id from shared preferences
    // and displays on the screen
    /*private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.d("Fbid", "Firebase reg id: " + regId);

        *//*if (!TextUtils.isEmpty(regId))
            //txtRegId.setText("Firebase Reg Id: " + regId);
        else
            //txtRegId.setText("Firebase Reg Id is not received yet!");*//*
    }*/

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

   /* private void subscribeUserToParse() {
        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        if (TextUtils.isEmpty(deviceToken)) {
            Intent intent = new Intent(this, MyFirebaseInstanceIDService.class);
            startService(intent);
            return;
        }

        User user = UserUtil.retrieveUserFromDB(mRealm);
        String objectId = use




        r.getParseObjectId();
        if (!TextUtils.isEmpty(objectId)) {
            ParseUtils.subscribeWithUsersObjectId(objectId, deviceToken);
        }
    }*/



    private void getUserIm(final  String token) {

        String  tag_string_req = "req_clients";

        StringRequest request=new StringRequest(Request.Method.GET, AppConfig.Base_Url+AppConfig.App_api+AppConfig.URL_Show_UserProfileImage+"?token="+token, new Response.Listener<String>(){

            @Override
            public void onResponse(String response1)
            {

                Log.d("Particularclientimage","-->"+response1);
                //parsing Json
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response1.toString());
                    String response = jsonObject.getString("response");
                    String status = jsonObject.getString("status");
                    Log.d("responsefilter",""+response);
                    Log.d("statusfilter",""+status);
                    JSONObject jsonObject1 = new JSONObject(response);
                    FetchUserImageGetterSetter fetchUserImageGetterSetter=new FetchUserImageGetterSetter();fetchUserImageGetterSetter.setUserImageUrl(jsonObject1.getString("userImageUrl"));
                    userImageUrl=fetchUserImageGetterSetter.getUserImageUrl().toString();

                    Log.d("userImageUrlfilter",""+userImageUrl);
                    //Glide.with(MainActivity.this).load(userImageUrl).error(R.drawable.default_users).into(niv);

                    Glide
                            .with(MainActivity.this)
                            .load(userImageUrl)
                            .error(R.drawable.default_users)
                            .into(niv);

                    Log.d("Skip",""+userImageUrl);
                 /*   ClientData clientData=new ClientData();
                    clientData.setClientLogoPath(jsonObject1.getString("clientLogoPath"));
                    clientData.setClientImagePath(jsonObject1.getString("clientImagePath"));
                    clientData.setClientName(jsonObject1.getString("clientName"));

                    clientLogoPath=clientData.getClientLogoPath().toString();
                    clientImagePath=clientData.getClientImagePath().toString();
                    clientname11=clientData.getClientName().toString();
                    // clientname=clientData.getClientName().toString();
                    Log.d("clientlogooooo","-->"+clientLogoPath );
                    Log.d("clientimageooo","-->"+clientImagePath );
                    Log.d("clientiooo","-->"+clientname11 );*/


                }
                catch(JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage()==null ||error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getApplication(), R.style.MyDialogTheme).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Network/Connection Error");

                    // Setting Dialog Message
                    alertDialog.setMessage("Internet Connection is poor OR The Server is taking too long to respond.Please try again later.Thank you.");

                    // Setting Icon to Dialog
                    //  alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    // Showing Alert Message
                    // alertDialog.show();
                    //Log.e(TAG, "Registration Error: " + error.getMessage());

                } else if (error instanceof AuthFailureError) {

                    //TODO
                } else if (error instanceof ServerError) {

                    //TODO
                } else if (error instanceof NetworkError) {

                    //TODO
                } else if (error instanceof ParseError) {

                    //TODO
                }


            }


        }) ;

        AppController.getInstance().addToRequestQueue(request,tag_string_req);


    }
    private void showProfileData() {

        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.Base_Url+AppConfig.App_api+AppConfig.URL_Show_UserProfile+"?token="+userAccessToken, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                Log.d("SKipusernamenav", "Register Response: " + response1.toString());

                try {
                    //progressBar.setVisibility(View.GONE);
                    JSONObject object = new JSONObject(response1);
                    String response = object.getString("response");
                    String status =object.getString("status");
                    x = object.getJSONObject("response").getString("emailId").toString();
if (x.equals("null"))
{
    usernameniv.setText(object.getJSONObject("response").getString("fullName").toString());

    mailIdniv.setText("");
}
                   else if (status.equals("success")) {


                        usernameniv.setText(object.getJSONObject("response").getString("fullName").toString());
                        //mNumber.setText(object.getJSONObject("response").getString("contactNumber").toString());

                      mailIdniv.setText(x);

                        /*if(x==null) {
                            mailIdniv.setText(" ");
                            Log.d("mainusername111", "" +x );
                        }
                        else
                        {

                            mailIdniv.setText(x);
                        Log.d("mainusername11", "userName" +x );
                        }*/
                        /// et_address.setText(object.getJSONObject("response").getString("address").toString());
                        //  et_UserName.setText(object.getJSONObject("response").getString("fullName").toString());
                        Log.d("mainusername", "userName" + usernameniv);
                        Log.d("mainusermailid", "Mail" + mailIdniv);

                    }else {
                        // Toast.makeText(getApplicationContext(),"Could  not able to load data",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    // Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

               /* if (error.getMessage()==null ||error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ProfileNavigationActivity.this, R.style.MyDialogTheme).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Network/Connection Error");

                    // Setting Dialog Message
                    alertDialog.setMessage("Internet Connection is poor OR The Server is taking too long to respond.Please try again later.Thank you.");

                    // Setting Icon to Dialog
                    //  alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed
                            // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                    Log.e(TAG, "Update Error: " + error.getMessage());

                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                } else if (error instanceof NetworkError) {
                    //TODO
                } else if (error instanceof ParseError) {
                    //TODO
                }
*/
            }
        }); /*{

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userAccessToken", userAccessToken);

                return params;
            }

        };
*/
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

}

