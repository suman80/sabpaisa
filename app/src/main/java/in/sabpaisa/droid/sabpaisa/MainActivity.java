package in.sabpaisa.droid.sabpaisa;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
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
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crashlytics.android.Crashlytics;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;
import com.google.firebase.analytics.FirebaseAnalytics;
//import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
//import com.wangjie.androidbucket.utils.ABTextUtil;
//import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
//import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
//import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
//import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
//import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import in.sabpaisa.droid.sabpaisa.Adapter.NotificationAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.ViewPagerAdapter;
import in.sabpaisa.droid.sabpaisa.AppDB.AppDB;
import in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments;
import in.sabpaisa.droid.sabpaisa.Fragments.InstitutionFragment;
import in.sabpaisa.droid.sabpaisa.Fragments.ParticularClient;
import in.sabpaisa.droid.sabpaisa.Model.ClientData;
import in.sabpaisa.droid.sabpaisa.Model.DynamicImagesModel;
import in.sabpaisa.droid.sabpaisa.Model.FetchUserImageGetterSetter;
import in.sabpaisa.droid.sabpaisa.Model.Institution;
import in.sabpaisa.droid.sabpaisa.Model.NotificationModelClass;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.CustomSliderView;
import in.sabpaisa.droid.sabpaisa.Util.CustomViewPager;
import in.sabpaisa.droid.sabpaisa.Util.ForgotActivity;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;
import in.sabpaisa.droid.sabpaisa.Util.PrivacyPolicyActivity;
import in.sabpaisa.droid.sabpaisa.Util.ProfileNavigationActivity;
import io.fabric.sdk.android.Fabric;
import me.grantland.widget.AutofitTextView;

import static com.mikepenz.materialize.util.UIUtils.convertDpToPixel;
import static in.sabpaisa.droid.sabpaisa.ConstantsForUIUpdates.PROFILE_IMAGE;
import static in.sabpaisa.droid.sabpaisa.LogInActivity.PREFS_NAME;

public class MainActivity extends AppCompatActivity implements /*AppBarLayout.OnOffsetChangedListener,*/ /*RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener,*/ NavigationView.OnNavigationItemSelectedListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private SliderLayout mHeaderSlider;
    ArrayList<Integer> headerList = new ArrayList<>();
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    AppBarLayout appBarLayout;
    RecyclerView recyclerView;

    private CustomViewPager viewPager;

    private TabLayout tabLayout;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    ImageView niv, bell, bell1;
    TextView notificationNumber, notificationNumberFeed;

    String time;
    View menu_bell, menu_bell_feed;

    String img, imglogo, orgname, state;
    RequestQueue requestQueue;

    String ts;
    Context context;

    String count, countfeeds, clntname;
    AutofitTextView usernameniv, mailIdniv;
    Toolbar toolbar;
    String n, m, name, mobNumber;
    private static int CODE = 1; //declare as FIELD
    private FirebaseAnalytics firebaseAnalytics;
    NetworkImageView nav;
    String userImageUrl = null;
    String response, response1, userAccessToken;
    ImageView sendMoney, requestMoney, socialPayment, transaction, profile, bank, UpibankList, mPinInfo, mPinInfo2;
    LinearLayout paymentButton, chatButton, memberButton;

    FloatingActionButton fab;
    int MY_SOCKET_TIMEOUT_MS = 100000;
    ActionBarDrawerToggle toggle;
    //public  static String userImageUrl=null;
    HashMap<String, String> Hash_file_maps;
    //    private RapidFloatingActionLayout rfaLayout;
//    private RapidFloatingActionButton rfaBtn;
//    private RapidFloatingActionHelper rfabHelper;
    Bundle bundle;
    String x;
    String stateName, serviceName, ClientId;
    String stateName1, serviceName1, ClientId1;
    String posttimel;
    Handler mHandler;
    public static String MYSHAREDPREF = "mySharedPref";

    AppDB appDB;
    AppDbComments appDbComments;

    DrawerLayout drawer;

    NavigationView navigationView;

    String uinStatus;


    ////Testing
    @SuppressLint("MissingPermission")
    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //checking
        super.onCreate(savedInstanceState);
        // CommonUtils.setFullScreen(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//nav=(NetworkImageView)findViewById(R.id.profile_image);

//        request
// indowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_navigation);
        this.mHandler = new Handler();
        /*  this.mHandler.postDelayed(m_Runnable,5000);*/
        Fabric.with(this, new Crashlytics());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Sabpaisa");
        // toggle.setHomeAsUpIndicator(R.drawable.ic_drawer);
        SharedPreferences sharedPreferences2 = getApplication().getSharedPreferences(UIN.MYSHAREDPREFUIN, Context.MODE_PRIVATE);
        n = sharedPreferences2.getString("m", "xyz");
        m = n;
        ClientId1 = sharedPreferences2.getString("clientId", "123");

        ClientId = ClientId1;

        context = this;

        //Checking UIN Status

        SharedPreferences sharedPreferencesCheckUIN = getApplication().getSharedPreferences(UIN.SHARED_PREF_UIN_STATUS, Context.MODE_PRIVATE);
        uinStatus = sharedPreferencesCheckUIN.getString("UIN_STATUS", "abc");

        if (!uinStatus.equals("UIN_VERIFIED")){

            SharedPreferences settings = getSharedPreferences(UIN.MYSHAREDPREFUIN, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove("m");
            editor.remove("selectedstate");
            editor.remove("selectedservice");
            editor.remove("logged");
            editor.clear();
            editor.commit();


            //Added on 5th dec 2018

            SharedPreferences settings1 = getSharedPreferences(FilterActivity1.MySharedPreffilter, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = settings1.edit();
            editor1.remove("clientId");
            editor1.remove("stateId");
            editor1.clear();
            editor1.commit();


            finish();

            Intent intent = new Intent(MainActivity.this, FilterActivity1.class);

            startActivity(intent);


        }


        SharedPreferences sharedPreferences1 = getApplication().getSharedPreferences(FilterActivity1.MySharedPreffilter, Context.MODE_PRIVATE);
        stateName1 = sharedPreferences1.getString("selectedstate", "abc");
        stateName = stateName1;
        serviceName1 = sharedPreferences1.getString("selectedservice", "123");
        serviceName = serviceName1;
        //ClientId1=sharedPreferences1.getString("stateId", "123");
      /*  ClientId1=sharedPreferences1.getString("clientId", "123");

        ClientId=ClientId1;*/

        Log.d("Sharedprefrencemat", "-." + ClientId1 + stateName1 + serviceName1);
        Log.d("Sharedprefrencemat", "-." + ClientId + stateName + serviceName);
        Log.d("Sharedprefrencemat", "-." + ClientId + stateName + serviceName + n + m);

//////////////////////////////////Initializing Sqlite Db////////////////////////////////
        appDB = new AppDB(getApplicationContext());
        appDbComments = new AppDbComments(getApplicationContext());
        Log.d("SQLITE_DB_MAIN", " " + appDB + " " + appDbComments);
////////////////////////////////////////////////////////////////////////////////////////


        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);
        response = sharedPreferences.getString("response", "123");
        if (response != null) {

            userAccessToken = response;
            Log.d("AccessToken111", " " + userAccessToken);

            Log.d("FFResponse11111", " " + response);
        } else {
            try {

            } catch (NullPointerException e) {
                // [START log_and_report]
                //   FirebaseCrash.logcat(Log.ERROR, "MainActivity", "NPE caught");
                Crashlytics.log(Log.ERROR, "MainActivity", "NPE caught");
                // FirebaseCrash.report(e);
                Crashlytics.logException(e);
            }
        }
        Long tsLong = System.currentTimeMillis() / 1000;
        ts = tsLong.toString();
        Log.d("timemainactiviu", "" + ts);

       /* // This is used for the app custom toast and activity transition
        ChatSDKUiHelper.initDefault();
// Init the network manager
        BNetworkManager.init(getApplicationContext());
        // Create a new adapter
        BChatcatNetworkAdapter adapter = new BChatcatNetworkAdapter(getApplicationContext());
// Set the adapter
        BNetworkManager.sharedManager().setNetworkAdapter(adapter);
*/
        //mDrawerToggle=(ActionBarDrawerToggle)findViewById(R.id.nav)
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawer,
                getApplicationContext().getTheme());

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setHomeAsUpIndicator(R.drawable.ic_drawer);
        toggle.syncState();
        // ClientId=getIntent().getStringExtra("clientId");
        Log.d("CLIENTID(MainActivity)", "-->" + ClientId);
        Log.d("userImageUrl(MainAhjhkn", "-->" + userImageUrl);
        //Firebase Analytics
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        ClientData clientData = new ClientData();
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

        niv = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        usernameniv = (AutofitTextView) navigationView.getHeaderView(0).findViewById(R.id.username_nav);
        usernameniv.setText("");
        mailIdniv = (AutofitTextView) navigationView.getHeaderView(0).findViewById(R.id.email_nav);

        sendMoney = (ImageView) findViewById(R.id.ll_send);
        requestMoney = (ImageView) findViewById(R.id.ll_request);
        //socialPayment = (ImageView)findViewById(R.id.ll_social_payment);
        transaction = (ImageView) findViewById(R.id.ll_transactions);
        profile = (ImageView) findViewById(R.id.ll_profile);
        bank = (ImageView) findViewById(R.id.ll_bank);
        //UpibankList = (ImageView)findViewById(R.id.ll_Upibank);
        paymentButton = (LinearLayout) findViewById(R.id.payment_button);
        //chatButton = (LinearLayout) findViewById(R.id.chat);
        memberButton = (LinearLayout) findViewById(R.id.members);
        /*rfaLayout = (RapidFloatingActionLayout) findViewById(R.id.activity_main_rfal);
        rfaBtn = (RapidFloatingActionButton) findViewById(R.id.activity_main_rfab);*/
        //FabButtonCreate();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("SPApp");
        toolbar.setNavigationIcon(R.drawable.ic_navigation);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitleEnabled(false);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        //appBarLayout.addOnOffsetChangedListener(this);

        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.disableScroll(true);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.clients);
//////////////////Code for adding image and text on tab bar ////////////////////////////////////////////////
       /* TextView newTab = (TextView) LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_tab_layout, null);
        newTab.setText("Clients");
        newTab.setTypeface(null, Typeface.BOLD);
        newTab.setCompoundDrawablesWithIntrinsicBounds(R.drawable.clients, 0, 0, 0);
        tabLayout.getTabAt(0).setCustomView(newTab);
        */
        setSupportActionBar(toolbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitleEnabled(false);
        mHeaderSlider = (SliderLayout) findViewById(R.id.slider);
        //new MainActivity.DownloadImageTask(nav).execute(userImageUrl);
        SharedPreferences.Editor editor = getSharedPreferences(MYSHAREDPREF, MODE_PRIVATE).edit();
        editor.putString("clientId", ClientId);
        //editor.putString("userImageUrl",userImageUrl);
        editor.commit();


        LoadHeaderImageList();

        //getDynamicScreenImages();

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

        transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(MainActivity.this, TransactionsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);*/
                Toast.makeText(getApplicationContext(), "Coming Soon !", Toast.LENGTH_SHORT).show();
            }
        });


       /* chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int value = 1;

                Intent intent = new Intent(MainActivity.this, ChatSDKLoginActivity.class);

                intent.putExtra("userImageUrlMaim", userImageUrl);
                intent.putExtra("usernameniv", usernameniv.getText().toString().trim());
                intent.putExtra("VALUE", value);
                intent.putExtra("xxxxx", mailIdniv.getText().toString().trim());
                intent.putExtra("mobNumber", mobNumber);

                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        });*/

        ////////API CAlls////////////////////////////

        if (isOnline()) {
            getUserImage(userAccessToken);
        } else {

            Cursor res = appDB.getParticularImageData(userAccessToken);

            if (res.getCount() > 0) {
                StringBuffer stringBuffer = new StringBuffer();
                while (res.moveToNext()) {
                    stringBuffer.append(res.getString(0) + " ");
                    stringBuffer.append(res.getString(1) + " ");
                    stringBuffer.append(res.getString(2) + " ");

                    Glide.with(getApplicationContext())
                            .load(res.getString(2))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.drawable.default_users)
                            .into(niv);

                }
                Log.d("getImgFrmDBMain", "-->" + stringBuffer);

            } else {

                Log.d("In Else Part", "");
                //Toast.makeText(ProfileNavigationActivity.this,"In Else Part",Toast.LENGTH_SHORT).show();

            }

        }

        if (isOnline()) {
            showProfileData();
        } else {

            Cursor res = appDB.getParticularData(userAccessToken);

            if (res.getCount() > 0) {
                StringBuffer stringBuffer = new StringBuffer();
                while (res.moveToNext()) {
                    stringBuffer.append(res.getString(0));
                    stringBuffer.append(res.getString(1));
                    stringBuffer.append(res.getString(2));
                    stringBuffer.append(res.getString(3));
                    stringBuffer.append(res.getString(4));
                    stringBuffer.append(res.getString(5));
                    usernameniv.setText(res.getString(1));
                    mailIdniv.setText(res.getString(2));
                }
                Log.d("getParticularNum", "-->" + stringBuffer);

            } else {
                Log.d("MainActivity", "In Else Part");
            }

        }
        getClientsList(ClientId1);
        String y = x;

        usernameniv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = 1;
                Intent intent = new Intent(MainActivity.this, ProfileNavigationActivity.class);
                intent.putExtra("ClientId", ClientId);
                intent.putExtra("valueProfile", value);
                startActivity(intent);

            }
        });

        niv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = 1;

                Intent intent = new Intent(MainActivity.this, ProfileNavigationActivity.class);
                intent.putExtra("ClientId", ClientId);
                intent.putExtra("valueProfile", value);

                startActivity(intent);

            }
        });

        mailIdniv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = 1;

                Intent intent = new Intent(MainActivity.this, ProfileNavigationActivity.class);
                intent.putExtra("ClientId", ClientId);
                intent.putExtra("valueProfile", value);

                startActivity(intent);

            }
        });



        FullViewOfClientsProceed.isFragmentOpen = false;

        // setting false for clicks in feeds and groups

        MainGroupAdapter1.isClicked=false;
        MainFeedAdapter.isClicked=false;

        //Updating UI

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(PROFILE_IMAGE)){
                    getUserImage(userAccessToken);
                }
            }
        };

        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(receiver,new IntentFilter(ConstantsForUIUpdates.PROFILE_IMAGE));

    }


    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        String isRegIdSaved = pref.getString("isRegIdSaved", null);
        Log.d("Fbid", "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId) && (isRegIdSaved == null)) {
            //Toast.makeText(this, "Firebase Reg Id: " + regId, Toast.LENGTH_SHORT).show();
            sendFCMTokenToDb(regId, userAccessToken);

            SharedPreferences.Editor editor = pref.edit();
            editor.putString("isRegIdSaved", "1");
            editor.commit();
        } else {
            Log.d("FCM(regId)", "Is Empty");
            //Toast.makeText(this, "Firebase Reg Id is not received yet!" + regId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {

        if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);
            Log.d("Drawer", "Closing");
        } else {
            Log.d("Drawer", "Closed");
            /*finish();
            moveTaskToBack(true);
            System.exit(0);
            super.onBackPressed();

            return;
*/
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
            startActivity(intent);
            finish();
            System.exit(0);
        }


    }

   /* private void FabButtonCreate() {
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
*/
    /*@Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        Toast.makeText(this, "clicked label: " + position, Toast.LENGTH_SHORT).show();
        rfabHelper.toggleContent();
    }*/

   /* @TargetApi(Build.VERSION_CODES.ECLAIR)
    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        if (position == 2) {
            Toast.makeText(this, "Send Clicked", Toast.LENGTH_SHORT).show();
            if (isMpinSet == 0) {             *//*TODO check if mpin is set or not, for now i am hardcoding it*//*
                Intent intent = new Intent(MainActivity.this, AccountInfoActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            } else {
                Intent intent = new Intent(MainActivity.this, SendMoneyActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        } else if (position == 1) {
            Toast.makeText(this, "Request Clicked", Toast.LENGTH_SHORT).show();
        } else if (position == 0) {
            Toast.makeText(this, "Transactions Clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, TransactionsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
        }
        rfabHelper.toggleContent();
    }*/

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ParticularClient(), "");//Clients
        //adapter.addFragment(new FormFragment(),"Other Clients");
        //adapter.addFragment(new InstitutionFragment(),"Groups");
        viewPager.setAdapter(adapter);
        stateName = getIntent().getStringExtra("STATENAME");
        serviceName = getIntent().getStringExtra("SERVICENAME");

        FragmentManager in = getSupportFragmentManager();
        Fragment instituteFragment = new InstitutionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("stateName", stateName);
        bundle.putString("serviceName", serviceName);
        //bundle.putString("userImageUrl",userImageUrl);
        instituteFragment.setArguments(bundle);
        //in.beginTransaction().replace(R.id.activity_main_rfab, instituteFragment).commit();

    }

    private void setHeaderImageList() {
        for (int i = 0; i < headerList.size(); i++) {
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


        Hash_file_maps.put("SPApp Digitizing Cash", AppConfig.Base_Url + "/Docs/Images/HomeImage/sabpaisa.png");
        Hash_file_maps.put("Payment & Transfer", AppConfig.Base_Url + "/Docs/Images/HomeImage/UPI_2.png");
        Hash_file_maps.put("The Future Of Payments", AppConfig.Base_Url + "/Docs/Images/HomeImage/UPI_image.jpg");
        Hash_file_maps.put("UPI", AppConfig.Base_Url + "/Docs/Images/HomeImage/UPI_1.svg.png");
        for (String name : Hash_file_maps.keySet()) {
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
                    .putString("extra", name);

            mHeaderSlider.addSlider(textSliderView);


        }

    }

/*
    private void getDynamicScreenImages() {

        //Added for SSL (17th Sep 2018)
//        HttpsTrustManager.allowAllSSL();

        String tag_string_req = "req_clients";

        String url = AppConfig.Base_Url+AppConfig.URL_DynamicImages;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d("MainActivity_DI", "DynamicImages___" + response);

                try {
                    Hash_file_maps = new HashMap<String, String>();

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0 ; i<jsonArray.length() ; i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        DynamicImagesModel dynamicImagesModel = new DynamicImagesModel();

                        dynamicImagesModel.setEventSliedr(jsonObject.getString("eventSliedr"));
                        dynamicImagesModel.setEventName(jsonObject.getString("eventName"));
                        dynamicImagesModel.setEventDescription(jsonObject.getString("eventDescription"));
                        dynamicImagesModel.setEventImagePath(jsonObject.getString("eventImagePath"));

                        if (dynamicImagesModel.getEventSliedr().equals("Y")) {
                            Hash_file_maps.put(dynamicImagesModel.getEventName(), AppConfig.Base_Url+"/"+dynamicImagesModel.getEventImagePath());
                            *//*name=dynamicImagesModel.getEventName();*//*
                            *//*for (String name : Hash_file_maps.keySet()) {*//*
                                TextSliderView textSliderView = new TextSliderView(MainActivity.this);
                                // initialize a SliderLayout
                                textSliderView
                                        .description(name)
                                        .image(Hash_file_maps.get(name))
                                        .setScaleType(BaseSliderView.ScaleType.Fit)
                                        .setOnSliderClickListener(MainActivity.this);

                                //add your extra information
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle()
                                        .putString("extra", name);

                                mHeaderSlider.addSlider(textSliderView);


//                            }

                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                if (error.getMessage() == null || error instanceof TimeoutError || error instanceof NoConnectionError) {
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


        });


        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            try {
                ProviderInstaller.installIfNeeded(getApplicationContext());
            } catch (GooglePlayServicesRepairableException e) {
                // Indicates that Google Play services is out of date, disabled, etc.
                // Prompt the user to install/update/enable Google Play services.
                GooglePlayServicesUtil.showErrorNotification(e.getConnectionStatusCode(), getApplicationContext());
                // Notify the SyncManager that a soft error occurred.
                //final SyncResult syncResult = null;
                //syncResult.stats.numIOExceptions++;

                // Toast.makeText(getApplicationContext(), "Sync", Toast.LENGTH_LONG).show();
                return;
            } catch (GooglePlayServicesNotAvailableException e) {
                // Indicates a non-recoverable error; the ProviderInstaller is not able
                // to install an up-to-date Provider.
                // Notify the SyncManager that a hard error occurred.
                //syncResult.stats.numAuthExceptions++;
                //Toast.makeText(getApplicationContext(), "Sync12", Toast.LENGTH_LONG).show();
                return;
            }

            HttpStack stack = null;
            try {
                stack = new HurlStack(null, new TLSSocketFactory());
            } catch (KeyManagementException e) {
                e.printStackTrace();
                Log.d("Your Wrapper Class", "Could not create new stack for TLS v1.2");
                stack = new HurlStack();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                Log.d("Your Wrapper Class", "Could not create new stack for TLS v1.2");
                stack = new HurlStack();
            }

            // AppController.getInstance().addToRequestQueue(getApplicationContext(),stack);

            requestQueue = Volley.newRequestQueue(getApplicationContext(), stack);
        } else {

            requestQueue = Volley.newRequestQueue(getApplicationContext());
            //AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
        AppController.getInstance().addToRequestQueue(request, tag_string_req);
    }*/


    /*@Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            rfaLayout.setVisibility(GONE);
            //fab.setVisibility(View.GONE);// Collapsed
        } else {
            rfaLayout.setVisibility(View.VISIBLE);
            //fab.setVisibility(View.VISIBLE);// Not collapsed
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_navigation, menu);

        //Notification
        Bitmap iconNoti = BitmapFactory.decodeResource(getResources(), R.drawable.notification); //Converting drawable into bitmap
        Bitmap newIconNoti = resizeBitmapImageFn(iconNoti, (int) convertDpToPixel(20f, this)); //resizing the bitmap
        Drawable dNoti = new BitmapDrawable(getResources(), newIconNoti); //Converting bitmap into drawable
        //  menu.getItem(0).setIcon(dNoti);
        menu_bell = menu.findItem(R.id.bbell).getActionView();
        menu_bell_feed = menu.findItem(R.id.bbellFeed).getActionView();

        bell = (ImageView) menu_bell.findViewById(R.id.notification_bell);
        bell1 = (ImageView) menu_bell_feed.findViewById(R.id.notification_bell);
        notificationNumber = (TextView) menu_bell.findViewById(R.id.notifyNum);

        menu_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  Intent intent=new Intent(MainActivity.this,NotificationPopUPActivity.class);

                intent.putExtra("clientId",ClientId1);
                intent.putExtra("useraccesstoken",userAccessToken);
                intent.putExtra("grouptime",timeGroup);
                intent.putExtra("feedtime",timefeed);
                intent.putExtra("clientImagePath",img);
                intent.putExtra("state",state);
                intent.putExtra("clientName",clntname);
                startActivity(intent);*/

            }
        });
        return true;
    }


    private Bitmap resizeBitmapImageFn(
            Bitmap bmpSource, int maxResolution) {
        int iWidth = bmpSource.getWidth();
        int iHeight = bmpSource.getHeight();
        int newWidth = iWidth;
        int newHeight = iHeight;
        float rate = 0.0f;

        if (iWidth > iHeight) {
            if (maxResolution < iWidth) {
                rate = maxResolution / (float) iWidth;
                newHeight = (int) (iHeight * rate);
                newWidth = maxResolution;
            }
        } else {
            if (maxResolution < iHeight) {
                rate = maxResolution / (float) iHeight;
                newWidth = (int) (iWidth * rate);
                newHeight = maxResolution;
            }
        }

        return Bitmap.createScaledBitmap(
                bmpSource, newWidth, newHeight, true);
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
            int value = 1;

            Intent intent = new Intent(MainActivity.this, ProfileNavigationActivity.class);
            intent.putExtra("ClientId", ClientId);
            intent.putExtra("valueProfile", value);
            startActivity(intent);

        }
//Changes 25 april
        else if (id == R.id.nav_TransactionReport) {
        }

        // end
        else if (id == R.id.nav_ChangePassword) {
            Intent intent = new Intent(MainActivity.this, ForgotActivity.class);

            startActivity(intent);
        } else if (id == R.id.nav_Privacy_Policy) {
            Intent intent = new Intent(MainActivity.this, PrivacyPolicyActivity.class);

            startActivity(intent);
        } else if (id == R.id.nav_logout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this); //Home is name of the activity
            builder.setMessage("Do you want to Exit the app?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    Log.d("Mainctivity "," ***Logout******* ");
                    SharedPreferences settings1 = getSharedPreferences(UIN.MYSHAREDPREFUIN, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = settings1.edit();
                    editor1.remove("m");
                    editor1.commit();

                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.remove("logged");

                    editor.commit();
                    clearApplicationData();
                    finish();

                    finishAffinity();
                    /*Intent intent = new Intent(MainActivity.this, LogInActivity.class);


                    startActivity(intent);*/

                    System.exit(0);



                }
            });


            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();


        } else if (id == R.id.nav_share) {
//https://stackoverflow.com/questions/9730243/how-to-filter-specific-apps-for-action-send-intent-and-set-a-different-text-for
            try {
                /*Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "SPApp");
                String sAux = "\n Let me recommend you this application .\n this is the easy way to pay your fee\n It is very cool app try it once ,download it from the below link given... \n \n";
                sAux = sAux + "\n" + "https://play.google.com/store/apps/details?id=in.sabpaisa.droid.sabpaisa";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Share via"));*/


                shareIntentSpecificApps();


            } catch (Exception e) {


                //e.toString();
            }

        } else if (id == R.id.nav_Contacts) {


            Intent intent = new Intent(MainActivity.this, AllContacts.class);

            startActivity(intent);

        } else if (id == R.id.nav_txnhistory) {

            Intent intent = new Intent(MainActivity.this, AllTransactionSummary.class);

            startActivity(intent);

        } else if (id == R.id.nav_clean_data) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this); //Home is name of the activity
            builder.setMessage("For selecting other Institute/client.Press OK.");
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

                    //Added on 5th dec 2018

                    SharedPreferences settings1 = getSharedPreferences(FilterActivity1.MySharedPreffilter, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = settings1.edit();
                    editor1.remove("clientId");
                    editor1.remove("stateId");
                    editor1.clear();
                    editor1.commit();

                    Intent intent = new Intent(MainActivity.this, FilterActivity1.class);

                    startActivity(intent);


                }
            });

            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();

        }
            /*else if (id == R.id.nav_rate) {
            Intent intent=new Intent(MainActivity.this, RateActivity.class);

            startActivity(intent);

*//*
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.tt, newFragment)
                    .commit();*//*
         *//*newFragment = new Ratefragment();
            transaction.replace(R.id.tt, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();*//*

         *//*Fragment fragment = new Fragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.tt, fragment, fragment.getTag()).commit();
        *//*
        }
*/
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

        //Api For userName and email at navigation header
        showProfileData();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());


        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }


    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void getUserImage(final String token) {

        //Added for SSL (17th Sep 2018)
//        HttpsTrustManager.allowAllSSL();

        String tag_string_req = "req_clients";

        StringRequest request = new StringRequest(Request.Method.GET, AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_Show_UserProfileImage + "?token=" + token, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {

                Log.d("Particularclientimage", "-->" + response1);
                //parsing Json
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response1.toString());
                    String response = jsonObject.getString("response");
                    String status = jsonObject.getString("status");
                    Log.d("responsefilter", "" + response);
                    Log.d("statusfilter", "" + status);
                    JSONObject jsonObject1 = new JSONObject(response);
                    FetchUserImageGetterSetter fetchUserImageGetterSetter = new FetchUserImageGetterSetter();
                    fetchUserImageGetterSetter.setUserImageUrl(jsonObject1.getString("userImageUrl"));
                    userImageUrl = fetchUserImageGetterSetter.getUserImageUrl().toString();

                    Log.d("userImageUrlfilter", "" + userImageUrl);
                    //Glide.with(MainActivity.this).load(userImageUrl).error(R.drawable.default_users).into(niv);

                    Glide.with(getApplicationContext())
                            .load(userImageUrl)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.drawable.default_users)
                            .into(niv);

                    Log.d("Skip", "" + userImageUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                if (error.getMessage() == null || error instanceof TimeoutError || error instanceof NoConnectionError) {
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


        });


        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            try {
                ProviderInstaller.installIfNeeded(getApplicationContext());
            } catch (GooglePlayServicesRepairableException e) {
                // Indicates that Google Play services is out of date, disabled, etc.
                // Prompt the user to install/update/enable Google Play services.
                GooglePlayServicesUtil.showErrorNotification(e.getConnectionStatusCode(), getApplicationContext());
                // Notify the SyncManager that a soft error occurred.
                //final SyncResult syncResult = null;
                //syncResult.stats.numIOExceptions++;

                // Toast.makeText(getApplicationContext(), "Sync", Toast.LENGTH_LONG).show();
                return;
            } catch (GooglePlayServicesNotAvailableException e) {
                // Indicates a non-recoverable error; the ProviderInstaller is not able
                // to install an up-to-date Provider.
                // Notify the SyncManager that a hard error occurred.
                //syncResult.stats.numAuthExceptions++;
                //Toast.makeText(getApplicationContext(), "Sync12", Toast.LENGTH_LONG).show();
                return;
            }

            HttpStack stack = null;
            try {
                stack = new HurlStack(null, new TLSSocketFactory());
            } catch (KeyManagementException e) {
                e.printStackTrace();
                Log.d("Your Wrapper Class", "Could not create new stack for TLS v1.2");
                stack = new HurlStack();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                Log.d("Your Wrapper Class", "Could not create new stack for TLS v1.2");
                stack = new HurlStack();
            }

            // AppController.getInstance().addToRequestQueue(getApplicationContext(),stack);

            requestQueue = Volley.newRequestQueue(getApplicationContext(), stack);
        } else {

            requestQueue = Volley.newRequestQueue(getApplicationContext());
            //AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
        AppController.getInstance().addToRequestQueue(request, tag_string_req);
    }

    private void showProfileData() {

        //Added for SSL (17th Sep 2018)
//        HttpsTrustManager.allowAllSSL();

        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_Show_UserProfile + "?token=" + userAccessToken, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                Log.d("SKipusernamenav", "Register Response: " + response1.toString());

                try {
                    //progressBar.setVisibility(View.GONE);
                    JSONObject object = new JSONObject(response1);
                    String response = object.getString("response");
                    String status = object.getString("status");
                    x = object.getJSONObject("response").getString("emailId").toString();
                    mobNumber = object.getJSONObject("response").getString("contactNumber").toString();
                    if (x.equals("null")) {

                        usernameniv.setText(object.getJSONObject("response").getString("fullName").toString());

                        mailIdniv.setText("");
                    } else if (status.equals("success")) {
                        name = object.getJSONObject("response").getString("fullName").toString();
                        Log.d("namemain", "" + name);

                        usernameniv.setText(object.getJSONObject("response").getString("fullName").toString());
                        //mNumber.setText(object.getJSONObject("response").getString("contactNumber").toString());

                        mailIdniv.setText(x);

                        Log.d("mainusername", "userName" + usernameniv);
                        Log.d("mainusermailid", "Mail" + mailIdniv);

                    } else {
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
        });
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            try {
                ProviderInstaller.installIfNeeded(getApplicationContext());
            } catch (GooglePlayServicesRepairableException e) {
                // Indicates that Google Play services is out of date, disabled, etc.
                // Prompt the user to install/update/enable Google Play services.
                GooglePlayServicesUtil.showErrorNotification(e.getConnectionStatusCode(), getApplicationContext());
                // Notify the SyncManager that a soft error occurred.
                //final SyncResult syncResult = null;
                //syncResult.stats.numIOExceptions++;

                // Toast.makeText(getApplicationContext(), "Sync", Toast.LENGTH_LONG).show();
                return;
            } catch (GooglePlayServicesNotAvailableException e) {
                // Indicates a non-recoverable error; the ProviderInstaller is not able
                // to install an up-to-date Provider.
                // Notify the SyncManager that a hard error occurred.
                //syncResult.stats.numAuthExceptions++;
                //Toast.makeText(getApplicationContext(), "Sync12", Toast.LENGTH_LONG).show();
                return;
            }

            HttpStack stack = null;
            try {
                stack = new HurlStack(null, new TLSSocketFactory());
            } catch (KeyManagementException e) {
                e.printStackTrace();
                Log.d("Your Wrapper Class", "Could not create new stack for TLS v1.2");
                stack = new HurlStack();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                Log.d("Your Wrapper Class", "Could not create new stack for TLS v1.2");
                stack = new HurlStack();
            }

            // AppController.getInstance().addToRequestQueue(getApplicationContext(),stack);

            requestQueue = Volley.newRequestQueue(getApplicationContext(), stack);
        } else {

            requestQueue = Volley.newRequestQueue(getApplicationContext());
            //AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    private final Runnable m_Runnable = new Runnable() {
        public void run()

        {
            // Toast.makeText(MainActivity.this,"in runnable",Toast.LENGTH_SHORT).show();
            //           MainActivity.this.mHandler.postDelayed(m_Runnable, 5000);
        }

    };//runnable


    private void getClientsList(final String clientId) {

        //Added for SSL (17th Sep 2018)
//        HttpsTrustManager.allowAllSSL();

        String tag_string_req = "req_clients";

        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_ClientBasedOnClientId + clientId, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {

                Log.d("ARcParticularclient", "-->" + response1);
                //parsing Json
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response1.toString());
                    String status = jsonObject.getString("status");

                    if (status.equals("success")) {

                        String response = jsonObject.getString("response");

                        //Adding data to new jsonobject////////////////////////

                        JSONObject jsonObject1 = new JSONObject(response);

                        Institution institution = new Institution();

                        institution.setOrganizationId(jsonObject1.getString("clientId"));
                        Log.d("ARCClientIdjij", "-->" + institution.getOrganizationId());
                        institution.setOrganization_name(jsonObject1.getString("clientName"));
                        Log.d("ARcClientnamejij", "-->" + institution.getOrganization_name());

                        institution.setOrgLogo(jsonObject1.getString("clientLogoPath"));
                        Log.d("ARCClientimage", "-->" + institution.getOrgLogo());
                        institution.setOrgWal(jsonObject1.getString("clientImagePath"));
                        Log.d("ARCImageTest", "-->" + institution.getOrgLogo());
                        //Added on 1st Feb
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("lookupState");
                        institution.setOrgAddress(jsonObject2.getString("stateName"));
                        //Added on 1st Feb
                        institution.setOrgDesc(jsonObject1.getString("landingPage"));
                        imglogo = institution.getOrgLogo();
                        orgname = institution.getOrganization_name();
                        img = institution.getOrgWal();
                        state = institution.getOrgAddress();
                        clntname = institution.getOrganization_name();
                        Log.d("ARCJSONobjectResp", "-->" + response);
                        Log.d("ARCkeiopp", "-->" + img + "--" + imglogo + "--" + orgname + "---" + state);
                        Log.d("ARCJSONobjectttt", "-->" + jsonObject);
                    } else {
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage() == null || error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme).create();

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


        });

        AppController.getInstance().addToRequestQueue(request, tag_string_req);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v("MainActivity", "Internet Connection Not Present");
            return false;
        }
    }


    public void shareIntentSpecificApps() {


        List<Intent> targetShareIntents = new ArrayList<Intent>();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        PackageManager pm = MainActivity.this.getPackageManager();
        List<ResolveInfo> resInfos = pm.queryIntentActivities(shareIntent, 0);
        if (!resInfos.isEmpty()) {
            System.out.println("Have package");
            for (ResolveInfo resInfo : resInfos) {
                String packageName = resInfo.activityInfo.packageName;
                Log.i("Package Name", packageName);

                if (packageName.contains("com.twitter.android") || packageName.contains("com.facebook.katana")
                        || packageName.contains("com.whatsapp") || packageName.contains("com.google.android.apps.plus")
                        || packageName.contains("com.google.android.talk") || packageName.contains("com.slack")
                        || packageName.contains("com.google.android.gm") || packageName.contains("com.facebook.orca")
                        || packageName.contains("com.yahoo.mobile") || packageName.contains("com.skype.raider")
                        || packageName.contains("com.android.mms") || packageName.contains("com.linkedin.android")
                        || packageName.contains("com.google.android.apps.messaging")) {
                    Intent intent = new Intent();

                    String sAux = "\n Let me recommend you this application .\n this is the easy way to pay your fee\n It is very cool app try it once ,download it from the below link given... \n \n";
                    sAux = sAux + "\n" + "https://play.google.com/store/apps/details?id=in.sabpaisa.droid.sabpaisa";

                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                    intent.putExtra("AppName", resInfo.loadLabel(pm).toString());
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, sAux);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "SPApp");
                    intent.setPackage(packageName);
                    targetShareIntents.add(intent);
                }
            }
            if (!targetShareIntents.isEmpty()) {
                Collections.sort(targetShareIntents, new Comparator<Intent>() {
                    @Override
                    public int compare(Intent o1, Intent o2) {
                        return o1.getStringExtra("AppName").compareTo(o2.getStringExtra("AppName"));
                    }
                });
                Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0), "Select app to share");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
                startActivity(chooserIntent);
            } else {
                Toast.makeText(MainActivity.this, "No app to share.", Toast.LENGTH_LONG).show();
            }
        }
//Check

    }


    private void sendFCMTokenToDb(final String fcmToken, final String userAccessToken) {

        //Added for SSL (17th Sep 2018)
//        HttpsTrustManager.allowAllSSL();

        String tag_string_req = "req_clients";

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URl_FCM_TOKEN + "userToken=" + userAccessToken + "&fcmToken=" + fcmToken;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d("MainActivityFCMTocken", "--> " + response);

                try {

                    JSONObject jsonObject = new JSONObject(response.toString());
                    String status = jsonObject.getString("status");
                    String returnResponse = jsonObject.getString("response");

                    if (status.equals("success")) {


                        Log.d("MainActvtyFCMTokenInIF", "Fcm Token Has Updated " + returnResponse);

                    } else {

                        Log.d("MainActvtyFCMTokenInEls", "Fcm Token Has Not Updated " + returnResponse);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage() == null || error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Log.d("MainActivityFCMTocken", "onErrorResponse " + error.getMessage());

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


        });

        AppController.getInstance().addToRequestQueue(request, tag_string_req);
    }



    public void clearApplicationData() {
        File cacheDirectory = getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        if (applicationDirectory.exists()) {
            String[] fileNames = applicationDirectory.list();
            for (String fileName : fileNames) {
                if (!fileName.equals("lib")) {
                    deleteFile(new File(applicationDirectory, fileName));
                }
            }
        }
    }

    public static boolean deleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (int i = 0; i < children.length; i++) {
                    deletedAll = deleteFile(new File(file, children[i])) && deletedAll;
                }
            } else {
                deletedAll = file.delete();
            }
        }

        return deletedAll;
    }




}


