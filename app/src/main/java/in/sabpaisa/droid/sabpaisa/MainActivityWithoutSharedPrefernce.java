package in.sabpaisa.droid.sabpaisa;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;
//import com.wangjie.androidbucket.utils.ABTextUtil;
//import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
//import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
//import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
//import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
//import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.sabpaisa.droid.sabpaisa.Adapter.ViewPagerAdapter;
import in.sabpaisa.droid.sabpaisa.Fragments.InstitutionFragment;
import in.sabpaisa.droid.sabpaisa.Fragments.ProceedInstitiutionFragment;
import in.sabpaisa.droid.sabpaisa.Model.FetchUserImageGetterSetter;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;
import in.sabpaisa.droid.sabpaisa.Util.CustomSliderView;
import in.sabpaisa.droid.sabpaisa.Util.CustomViewPager;
import in.sabpaisa.droid.sabpaisa.Util.ForgotActivity;
import in.sabpaisa.droid.sabpaisa.Util.PrivacyPolicyActivity;
import in.sabpaisa.droid.sabpaisa.Util.ProfileNavigationActivity;
import in.sabpaisa.droid.sabpaisa.Util.RateActivity;

import static android.view.View.GONE;
import static in.sabpaisa.droid.sabpaisa.LogInActivity.PREFS_NAME;

public class MainActivityWithoutSharedPrefernce extends AppCompatActivity implements  /*AppBarLayout.OnOffsetChangedListener, RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener,*/NavigationView.OnNavigationItemSelectedListener,BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
    private SliderLayout mHeaderSlider;
    ArrayList<Integer> headerList = new ArrayList<>();
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    AppBarLayout appBarLayout;
    String i;
    RequestQueue requestQueue;
    int MY_SOCKET_TIMEOUT_MS =100000;
    private CustomViewPager viewPager;
    private TabLayout tabLayout;
    Toolbar toolbar;
    NavigationView navigationView;String x;
    NetworkImageView nav;
    String userImageUrl;
    String name,mobNumber;
    String useracesstoken;
    String response,response1,userAccessToken;
    ImageView sendMoney, requestMoney,socialPayment,transaction,profile,bank,UpibankList,mPinInfo,mPinInfo2;
    LinearLayout paymentButton,chatButton,memberButton;
    int isMpinSet=1;
    FloatingActionButton fab;
    ActionBarDrawerToggle toggle;
    //public  static String userImageUrl=null;
    //public  static String userImageUrl=null;error
    HashMap<String,String> Hash_file_maps;

    ImageView niv;
    TextView usernameniv,mailIdniv;
//    private RapidFloatingActionLayout rfaLayout;
//    private RapidFloatingActionButton rfaBtn;
//    private RapidFloatingActionHelper rfabHelper;
    Bundle bundle;
    String stateName,serviceName,ClientId;
//    public  static  String MYSHAREDPREF="mySharedPref";

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //CommonUtils.setFullScreen(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main_navigation);

        // This is used for the app custom toast and activity transition
        ChatSDKUiHelper.initDefault();

// Init the network manager
        BNetworkManager.init(getApplicationContext());

// Create a new adapter
        BChatcatNetworkAdapter adapter = new BChatcatNetworkAdapter(getApplicationContext());

// Set the adapter
        BNetworkManager.sharedManager().setNetworkAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Sabpaisa");

               /* SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

                response = sharedPreferences.getString("response", "123");

                userAccessToken = response;

                Log.d("AccessToken111", " " + userAccessToken);

                Log.d("FFResponse11111", " " + response);*/
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
        builder.firebaseRootPath("p
        rod");

// Initialize the Chat SDK
        ChatSDK.initialize(builder.build());
        UserInterfaceModule.activate(context);

// Activate the Firebase module
        FirebaseModule.activate();

// File storage is needed for profile image upload and image messages
        FirebaseFileStorageModule.activate();


*/

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        response = sharedPreferences.getString("response", "123");

        useracesstoken = response;

        Log.d("FFResfilter", " " + response);


        SharedPreferences sharedPreferences1 = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        response = sharedPreferences1.getString("response", "123");

        userAccessToken = response;




        Log.d("AccessTokenwithout", " " + userAccessToken);

        Log.d("Reswithout", " " + response);



        getUserIm(userAccessToken);
        final String userImageUrl1=getIntent().getStringExtra("userImageUrl");

        Log.d("ujhuolvbluhkl","-->"+userImageUrl1);


        //mDrawerToggle=(ActionBarDrawerToggle)findViewById(R.id.nav)
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawer,
                getApplicationContext().getTheme());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setHomeAsUpIndicator(R.drawable.ic_drawer);
        toggle.syncState();
        ClientId=getIntent().getStringExtra("clientId");
        // userImageUrl=getIntent().getStringExtra("userImageUrl");
       /* SharedPreferences sharedPreferences = getApplication().getSharedPreferences(FilterActivity.PREFS_NAME1, Context.MODE_PRIVATE);

        userImageUrl = sharedPreferences.getString("imageusrl", "123");
*/
        // userImageUrl = response;
        /*Log.d("stateName11111"," "+stateName);
        Log.d("serviceName1111"," "+serviceName);*/

        Log.d("Test","-->"+ClientId);
      /*  Intent intent=new Intent(MainActivity.this, FullViewOfClientsProceed.class);
        intent.putExtra("userImageUrl",userImageUrl);*/


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //View header = navigationView.inflateHeaderView(R.layout.nav_header_main_activity_navigation);
        //nav = (NetworkImageView) header.findViewById(R.id.profile_image);

        // View header = navigationView.getHeaderView(0);
        // NetworkImageView niv = (NetworkImageView) header.findViewById(R.id.profile_image);

/*
        //initialize and create the image loader logic
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }

    *//*
    @Override
    public Drawable placeholder(Context ctx) {
        return super.placeholder(ctx);
    }

    @Override
    public Drawable placeholder(Context ctx, String tag) {
        return super.placeholder(ctx, tag);
    }
    *//*
        });

      */          //if(url.length() > 0)
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
//        rfaLayout = (RapidFloatingActionLayout)findViewById(R.id.activity_main_rfal);
//        rfaBtn = (RapidFloatingActionButton)findViewById(R.id.activity_main_rfab);
       // FabButtonCreate();
        //fab = (FloatingActionButton)findViewById(R.id.fab_dashboard);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("SPApp");
        toolbar.setNavigationIcon(R.drawable.ic_navigation);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(MainActivity.this, "clicking the toolbar!", Toast.LENGTH_SHORT).show();
                    }
                }

        );
        mCollapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitleEnabled(false);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        //appBarLayout.addOnOffsetChangedListener(this);

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

            /*    //new MainActivity.DownloadImageTask(nav).execute(userImageUrl);
                *//*SharedPreferences.Editor editor = getSharedPreferences(MYSHAREDPREF,MODE_PRIVATE).edit();
                editor.putString("clientId",ClientId);
                editor.putString("userImageUrl",userImageUrl);
                editor.commit();*/
        LoadHeaderImageList();
        setHeaderImageList();
        niv = (ImageView)navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        usernameniv = (TextView)navigationView.getHeaderView(0).findViewById(R.id.username_nav);
        mailIdniv = (TextView)navigationView.getHeaderView(0).findViewById(R.id.email_nav);
        showProfileData();

        //mailIdniv.setText(x);

        sendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (isMpinSet==0) {             *//*TODO check if mpin is set or not, for now i am hardcoding it*//*
                    Intent intent = new Intent( MainActivityWithoutSharedPrefernce.this, AccountInfoActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
                }else {
                    Intent intent = new Intent( MainActivityWithoutSharedPrefernce.this, SendMoneyActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
                }*/
                Toast.makeText(getApplicationContext(), "Coming Soon !", Toast.LENGTH_SHORT).show();
            }
        });


        requestMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent( MainActivityWithoutSharedPrefernce.this, RequestMoney.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);*/
                Toast.makeText(getApplicationContext(), "Coming Soon !", Toast.LENGTH_SHORT).show();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent( MainActivityWithoutSharedPrefernce.this, UPI_UserAccounts.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);*/
                Toast.makeText(getApplicationContext(), "Coming Soon !", Toast.LENGTH_SHORT).show();
            }
        });

        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent( MainActivityWithoutSharedPrefernce.this, AccountInfoActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);*/
                Toast.makeText(getApplicationContext(), "Coming Soon !", Toast.LENGTH_SHORT).show();
            }
        });

       /* UpibankList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivityWithoutSharedPrefernce.this, UPIBankList.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        });*/


        transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent( MainActivityWithoutSharedPrefernce.this, TransactionsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);*/
                Toast.makeText(getApplicationContext(), "Coming Soon !", Toast.LENGTH_SHORT).show();
            }
        });
               /* socialPayment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent( MainActivityWithoutSharedPrefernce.this,SocialPayment.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
                    }
                });*/

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        /*Intent intent = new Intent( MainActivityWithoutSharedPrefernce.this,ChatSDKLoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);*/


                int value=1;

                Intent intent = new Intent(MainActivityWithoutSharedPrefernce.this,ChatSDKLoginActivity.class);

                intent.putExtra("userImageUrl",userImageUrl);
                intent.putExtra("VALUE",value);
                intent.putExtra("CLIENTID",ClientId);

                intent.putExtra("userImageUrlMaim",userImageUrl);
                intent.putExtra("usernameniv",usernameniv.getText().toString().trim());
                //intent.putExtra("VALUE",value);
                intent.putExtra("xxxxx",mailIdniv.getText().toString().trim());
                intent.putExtra("mobNumber",mobNumber);


                /////////10-april-2018////////////////////////


                intent.putExtra("userImageUrlMaim",userImageUrl);
                intent.putExtra("usernameniv",name);
                //  intent.putExtra("VALUE",value);
                intent.putExtra("xxxxx",x);
                intent.putExtra("mobNumber",mobNumber);

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
                /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    super.onBackPressed();
                    finish();

                }*/

        moveTaskToBack(true);
        System.exit(0);
        finish();
    }

  /*  private void FabButtonCreate() {
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
            if (isMpinSet==0) {             *//*TODO check if mpin is set or not, for now i am hardcoding it*//*
                Intent intent = new Intent( MainActivityWithoutSharedPrefernce.this, AccountInfoActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }else {
                Intent intent = new Intent( MainActivityWithoutSharedPrefernce.this, SendMoneyActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        }else if (position==1){
            Toast.makeText(this, "Request Clicked", Toast.LENGTH_SHORT).show();
        }else if (position==0){
            Toast.makeText(this, "Transactions Clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent( MainActivityWithoutSharedPrefernce.this, TransactionsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
        }
        rfabHelper.toggleContent();
    }*/

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

    /*@Override
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
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.coa_menu1, menu);




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
            Intent intent=new Intent( MainActivityWithoutSharedPrefernce.this, ProfileNavigationActivity.class);

            startActivity(intent);
            // Handle the camera action
        } /*else if (id == R.id.nav_Chat) {

        } *//*else if (id == R.id.nav_Settings) {
            Intent intent=new Intent(MainActivity.this, SettingsNavigationActivity.class);

            startActivity(intent);

        }*/
        else  if(id == R.id.nav_ChangePassword)
        {
            Intent intent=new Intent( MainActivityWithoutSharedPrefernce.this, ForgotActivity.class);

            startActivity(intent);
        }
        else  if(id == R.id.nav_Privacy_Policy)
        {
            Intent intent=new Intent( MainActivityWithoutSharedPrefernce.this, PrivacyPolicyActivity.class);

            startActivity(intent);
        }
       /* else if(id==R.id.nav_TransactionReport){
            Intent intent=new Intent(MainActivityWithoutSharedPrefernce.this, TransactionReportNav.class);
            startActivity(intent);
        }*/
        /*else if (id == R.id.nav_Settings) {
            Intent intent=new Intent(MainActivity.this, SettingsNavigationActivity.class);

            startActivity(intent);



        }*/

        else if (id == R.id.nav_logout) {

            AlertDialog.Builder builder=new AlertDialog.Builder( MainActivityWithoutSharedPrefernce.this); //Home is name of the activity
            builder.setMessage("Do you want to Logout?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.remove("logged");
                    editor.commit();
                    finish();
                    Intent intent=new Intent( MainActivityWithoutSharedPrefernce.this, LogInActivity.class);

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



        }

      /*  else if(id==R.id.nav_Contacts){


            Intent intent=new Intent( MainActivityWithoutSharedPrefernce.this, AllContacts.class);

            startActivity(intent);

        }*/
        else if (id == R.id.nav_share) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "SPApp");
                String sAux = "\n Let me recommend you this application .\n this is the easy way to pay your fee\n It is very cool app try it once ,download it from the below link given... \n \n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=in.sabpaisa.droid.sabpaisa";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Complete action using "));
            } catch (Exception e) {
                //e.toString();
            }


        }

        else if(id==R.id.nav_txnhistory){

            Intent intent=new Intent( MainActivityWithoutSharedPrefernce.this, AllTransactionSummary.class);

            startActivity(intent);

        }/*else if (id == R.id.nav_rate) {
            Intent intent=new Intent( MainActivityWithoutSharedPrefernce.this, RateActivity.class);

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
        else if(id==R.id.nav_clean_data)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivityWithoutSharedPrefernce.this); //Home is name of the activity
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

                    Intent intent=new Intent( MainActivityWithoutSharedPrefernce.this, FilterActivity.class);
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

        toolbar.setNavigationIcon(R.drawable.ic_navigation);
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
    private String getUserIm(final  String token) {

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
                    i= userImageUrl;
                    Log.d("check123",""+i);
                    ImageView niv = (ImageView)navigationView.getHeaderView(0).findViewById(R.id.profile_image);

                    Glide.with( MainActivityWithoutSharedPrefernce.this)
                            .load(i)
                            .error(R.drawable.default_users)
                            .into(niv);
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


        return tag_string_req;
    }

    private void showProfileData() {

        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.Base_Url+AppConfig.App_api+AppConfig.URL_Show_UserProfile+"?token="+useracesstoken, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                Log.d("SKipusernamenav", "Register Response: " + response1.toString());

                try {
                    //progressBar.setVisibility(View.GONE);
                    JSONObject object = new JSONObject(response1);
                    String response = object.getString("response");
                    String status =object.getString("status");
                    x = object.getJSONObject("response").getString("emailId").toString();

                    name=object.getJSONObject("response").getString("fullName").toString();



                    /////////10-april-2018////////////////////////

                    mobNumber = object.getJSONObject("response").getString("contactNumber").toString();

                    if(x.equals("null"))
{
    usernameniv.setText(object.getJSONObject("response").getString("fullName").toString());
mailIdniv.setText("");
}
                  else   if (status.equals("success")) {


                        /////////10-april-2018////////////////////////


                        name=object.getJSONObject("response").getString("fullName").toString();

                        usernameniv.setText(object.getJSONObject("response").getString("fullName").toString());
                        //mNumber.setText(object.getJSONObject("response").getString("contactNumber").toString());

          mailIdniv.setText(x);
                        //mailIdniv.setText(object.getJSONObject("response").getString("emailId").toString());
                        /// et_address.setText(object.getJSONObject("response").getString("address").toString());
                        //  et_UserName.setText(object.getJSONObject("response").getString("fullName").toString());
                        Log.d("skipusername", "userName" + usernameniv);
                        Log.d("skipusermailid", "Mail" + mailIdniv);

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

}
