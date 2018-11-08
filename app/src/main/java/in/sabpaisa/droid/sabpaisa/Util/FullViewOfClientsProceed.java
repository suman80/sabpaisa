package in.sabpaisa.droid.sabpaisa.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.android.volley.toolbox.StringRequest;
import com.braunster.androidchatsdk.firebaseplugin.firebase.BChatcatNetworkAdapter;
import com.braunster.chatsdk.Utils.helper.ChatSDKUiHelper;
import com.braunster.chatsdk.activities.ChatSDKLoginActivity;
import com.braunster.chatsdk.network.BNetworkManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import in.sabpaisa.droid.sabpaisa.Adapter.ViewPagerAdapter;
import in.sabpaisa.droid.sabpaisa.AllContacts;
import in.sabpaisa.droid.sabpaisa.AllTransactionSummary;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.AppDB.AppDB;
import in.sabpaisa.droid.sabpaisa.FeedData;
import in.sabpaisa.droid.sabpaisa.FilterActivity;
//import in.sabpaisa.droid.sabpaisa.Fragments.FeedFragments1;
import in.sabpaisa.droid.sabpaisa.Fragments.ProceedFeedsFragment;
import in.sabpaisa.droid.sabpaisa.Fragments.ProceedGroupsFragments;
import in.sabpaisa.droid.sabpaisa.GroupListData;
import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.MainActivity;
import in.sabpaisa.droid.sabpaisa.Members;
import in.sabpaisa.droid.sabpaisa.Model.*;
import in.sabpaisa.droid.sabpaisa.Model.SkipClientData;
import in.sabpaisa.droid.sabpaisa.PayFragments;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.UIN;

import static in.sabpaisa.droid.sabpaisa.LogInActivity.PREFS_NAME;

public class FullViewOfClientsProceed extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener, ProceedFeedsFragment.GetDataInterface, ProceedGroupsFragments.GetDataInterface, Members.GetDataInterface {
    ImageView clientImagePath;
    String clientName, state, landingPage;
    public static String ClientId;
    public static String clientImageURLPath = null;
    private ViewPager viewPager;
    TextView mSearchText;
    String name, mobNumber;
    static boolean active = false;
    String Fts;
    Timestamp Fullviewts;
    ActionBarDrawerToggle toggle;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    public static String userImageUrl = null;
    AppBarLayout appBarLayout;
    TextView usernameniv, mailIdniv;
    LinearLayout paymentButton, chatButton, memberButton;
    MaterialSearchView searchView;
    ArrayList<FeedData> feedData;
    NavigationView navigationView;
    String x;
    String i, useracesstoken, response, response1;
    ImageView niv;
    ArrayList<FeedData> filteredfeedList;
    ArrayList<GroupListData> GroupData;
    ArrayList<GroupListData> filteredGroupList;
    ProceedFeedsFragment feedsFragments;
    ProceedGroupsFragments groupsFragments;
    Members membersFragment;
    ArrayList<Member_GetterSetter> memberData;
    ArrayList<Member_GetterSetter> filteredmemberData;

    private TabLayout tabLayout;

    DrawerLayout drawer;

    public static String MySharedPrefOnFullViewOfClientProceed = "mySharedPrefForId";

    AppDB appDB;


    String position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_nagationfullview);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        paymentButton = (LinearLayout) findViewById(R.id.payment_button);
        //chatButton = (LinearLayout)findViewById(R.id.chat);
        memberButton = (LinearLayout) findViewById(R.id.members);


       /* //ChatSDKUiHelper.initDefault();

        ChatSDKUiHelper.initDefault();

        // Init the network manager
        BNetworkManager.init(getApplicationContext());

// Create a new adapter
        BChatcatNetworkAdapter adapter = new BChatcatNetworkAdapter(getApplicationContext());

// Set the adapter
        BNetworkManager.sharedManager().setNetworkAdapter(adapter);
*/

       //24th oct 2018
       //https://stackoverflow.com/questions/33627106/how-to-open-a-specific-fragment-page-in-viewpager-from-another-activity-button-c
        //position = getIntent().getStringExtra("deleteFrmGrp");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setHomeAsUpIndicator(R.drawable.ic_drawer);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sabpaisa");
        toolbar.setNavigationIcon(R.drawable.ic_navigation);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitleEnabled(false);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        // appBarLayout.addOnOffsetChangedListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewpagerproceed);
        setupViewPager(viewPager);

        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //////////////////Code for adding image and text on tab bar ///////////////////////////////////////////////

        int[] tabIcons = {
                R.drawable.feeds,
                R.drawable.groups,
                R.drawable.payments,
                R.drawable.members,
        };

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);

        position = getIntent().getStringExtra("FRAGMENT_ID");

        Log.d("FVOCP_pos","FRAGMENT_ID__"+position);

        if (position != null){
            viewPager.setCurrentItem(Integer.parseInt(position));
        }

        landingPage = getIntent().getStringExtra("landingPage");
        Log.d("page", "" + landingPage);
/*
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.MYSHAREDPREF, MODE_PRIVATE);
        userImageUrl=sharedPreferences.getString("userImageUrl","abc");
        //Log.d("userImageUrlFrag","-->"+userImageUrl);
        Log.d("userImageUrl_FVOCL"," "+userImageUrl);*/

        Intent intent = getIntent();
        clientName = intent.getStringExtra("clientName");
        state = intent.getStringExtra("state");
        clientImageURLPath = getIntent().getStringExtra("clientImagePath");

        ClientId = getIntent().getStringExtra("clientId");
        //userImageUrl=getIntent().getStringExtra("userImageUrl");
        Log.d("ClientId_FVOCL", " " + ClientId);
        Log.d("userImageUrl_FVOCL", " " + userImageUrl);
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        response = sharedPreferences.getString("response", "123");

        useracesstoken = response;

        Log.d("FFResfilter", " " + response);

        // Added on 3rd feb
        SharedPreferences sharedPreferences12 = getApplication().getSharedPreferences(UIN.MYSHAREDPREFUIN, Context.MODE_PRIVATE);

        response1 = sharedPreferences12.getString("clientId", "123");

        ClientId = response1;

        Log.d("fullviewclientid", "" + ClientId);


        niv = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        usernameniv = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username_nav);
        mailIdniv = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email_nav);


        mailIdniv.setText(x);
        usernameniv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = 3;

                Intent intent = new Intent(FullViewOfClientsProceed.this, ProfileNavigationActivity.class);
                intent.putExtra("ClientId", ClientId);
                intent.putExtra("state", state);
                intent.putExtra("clientName", clientName);


                intent.putExtra("clientImagePath", clientImageURLPath);
                intent.putExtra("valueProfile", value);

                startActivity(intent);

            }
        });

        niv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = 3;

                Intent intent = new Intent(FullViewOfClientsProceed.this, ProfileNavigationActivity.class);
                intent.putExtra("ClientId", ClientId);
                intent.putExtra("state", state);
                intent.putExtra("clientName", clientName);


                intent.putExtra("clientImagePath", clientImageURLPath);
                intent.putExtra("valueProfile", value);

                startActivity(intent);

            }
        });

        mailIdniv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = 3;

                Intent intent = new Intent(FullViewOfClientsProceed.this, ProfileNavigationActivity.class);
                intent.putExtra("ClientId", ClientId);
                intent.putExtra("state", state);
                intent.putExtra("clientName", clientName);


                intent.putExtra("clientImagePath", clientImageURLPath);
                intent.putExtra("valueProfile", value);

                startActivity(intent);

            }
        });

        appDB = new AppDB(getApplicationContext());


        if (isOnline()) {

            getUserImage(useracesstoken);

        } else {

            Cursor res = appDB.getParticularImageData(useracesstoken);

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
                Log.d("getImgFrmDBMainFVOCP", "-->" + stringBuffer);

            } else {

                Log.d("In Else Part", "");
                //Toast.makeText(ProfileNavigationActivity.this,"In Else Part",Toast.LENGTH_SHORT).show();

            }

        }


        if (isOnline()) {
            showProfileData();
        } else {

            Cursor res = appDB.getParticularData(useracesstoken);

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
                Log.d("FVOCP", "In Else Part");
            }

        }


/*
        Glide.with(FullViewOfClientsProceed.this)
                .load(userImageUrl)
                .error(R.drawable.default_users)
                .into(niv);
*/

        SharedPreferences.Editor editor = getSharedPreferences(MySharedPrefOnFullViewOfClientProceed, MODE_PRIVATE).edit();
        editor.putString("clientId", ClientId);
        editor.putString("clientName", clientName);  //21st March,2018
        editor.putString("state", state);    //21st March,2018
        editor.putString("clientImageURLPath", clientImageURLPath);  //21st March,2018
        editor.commit();

        TextView clientNameTextView = (TextView) findViewById(R.id.particular_client_name_proceed);

        TextView stateTextView = (TextView) findViewById(R.id.particular_client_address_proceed);


        clientImagePath = (ImageView) findViewById(R.id.ClientImagePRoceed);
        clientNameTextView.setText(clientName);
        stateTextView.setText(state);
        //new DownloadImageTask(clientImagePath).execute(clientImageURLPath);
        Glide.with(FullViewOfClientsProceed.this)
                .load(clientImageURLPath)
                .error(R.drawable.default_users)
                .into(clientImagePath);

        Glide.with(FullViewOfClientsProceed.this)/*//Added on 1st Feb*/
                .load(clientImageURLPath)
                .error(R.drawable.image_not_found)
                .into(clientImagePath);
        searchViewBar();


        /*chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value=3;
                Intent intent = new Intent(FullViewOfClientsProceed.this,ChatSDKLoginActivity.class);
                intent.putExtra("VALUE",value);
                intent.putExtra("CLIENTID",ClientId);
                intent.putExtra("CLIENTNAME",clientName);
                intent.putExtra("STATE",state);
                intent.putExtra("CLIENTIMG",clientImageURLPath);
                intent.putExtra("userImageUrlMaim",userImageUrl);
                intent.putExtra("usernameniv",usernameniv.getText().toString().trim());
                //intent.putExtra("VALUE",value);
                intent.putExtra("xxxxx",mailIdniv.getText().toString().trim());
                intent.putExtra("mobNumber",mobNumber);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        });*/


    }


    private void setupViewPager(ViewPager viewPager) {


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        /*feedsFragments = new ProceedFeedsFragments();
        adapter.addFragment(feedsFragments,"Feeds"); //changing here creating different frags

        groupsFragments = new ProceedGroupsFragments();
        adapter.addFragment(groupsFragments,"Groups");//changing here creating different frags

        adapter.addFragment(new PayFragments(),"Payment");

        membersFragment=new Members();
        adapter.addFragment(membersFragment,"Members");*/

        feedsFragments = new ProceedFeedsFragment();
        adapter.addFragment(feedsFragments, ""); //changing here creating different frags

        groupsFragments = new ProceedGroupsFragments();
        adapter.addFragment(groupsFragments, "");//changing here creating different frags

        adapter.addFragment(new PayFragments(), "");

        membersFragment = new Members();
        adapter.addFragment(membersFragment, "");

        viewPager.setAdapter(adapter);


    }





 /*   @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }*/


    /*START method to enable searchBar and define its action*/
    private void searchViewBar() { //TODO searchView
        searchView = (MaterialSearchView) findViewById(R.id.search_viewSP);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() == 0) {

                    if (feedData != null) {
                        filteredfeedList = feedData;
                        feedsFragments.getDataFromActivity();
                    } else {
                    }

                    if (GroupData != null) {
                        filteredGroupList = GroupData;
                        groupsFragments.getDataFromActivity();
                    } else {
                    }

                    if (memberData != null) {
                        filteredmemberData = memberData;
                        membersFragment.getDataFromActivity();
                    } else {
                    }


                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
                if (query.length() > 0) {

                    if (feedData != null) {
                        filteredfeedList = filterFeed(feedData, query);
                        feedsFragments.getDataFromActivity();
                    } else {
                    }

                    if (GroupData != null) {
                        filteredGroupList = filterGroup(GroupData, query);
                        groupsFragments.getDataFromActivity();
                    } else {
                    }

                    if (memberData != null) {
                        filteredmemberData = filterMember(memberData, query);
                        membersFragment.getDataFromActivity();
                    } else {
                    }

                    Log.wtf("FilteredList", String.valueOf(filteredfeedList));

                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {

                    if (feedData != null) {
                        filteredfeedList = feedData;
                        feedsFragments.getDataFromActivity();
                    } else {
                    }

                    if (GroupData != null) {
                        filteredGroupList = GroupData;
                        groupsFragments.getDataFromActivity();
                    } else {
                    }

                    Log.wtf("filteredfeedList ", String.valueOf(filteredfeedList));

                    if (memberData != null) {
                        filteredmemberData = memberData;
                        membersFragment.getDataFromActivity();
                    } else {
                    }


                } else if (newText.length() > 0) {

                    if (feedData != null) {
                        filteredfeedList = filterFeed(feedData, newText);
                        feedsFragments.getDataFromActivity();
                    } else {
                    }

                    if (GroupData != null) {
                        filteredGroupList = filterGroup(GroupData, newText);
                        groupsFragments.getDataFromActivity();
                    } else {
                    }

                    if (memberData != null) {
                        filteredmemberData = filterMember(memberData, newText);
                        membersFragment.getDataFromActivity();
                    } else {
                    }

                    Log.wtf("FilteredList", String.valueOf(filteredfeedList));

                }
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            int temp;

            @Override
            public void onSearchViewShown() {
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
                TypedValue tv = new TypedValue();
                getApplicationContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
                int actionBarHeight = getResources().getDimensionPixelSize(tv.resourceId);
                temp = params.height;
                params.height = actionBarHeight; // COLLAPSED_HEIGHT

                appBarLayout.setLayoutParams(params);
                appBarLayout.setExpanded(true, false);
                searchView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSearchViewClosed() {
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
                params.height = temp; // COLLAPSED_HEIGHT

                appBarLayout.setLayoutParams(params);
                appBarLayout.setExpanded(true, false);//Do some magic

                if (feedData != null) {
                    filteredfeedList = feedData;
                    feedsFragments.getDataFromActivity();
                } else {
                }

                if (GroupData != null) {
                    filteredGroupList = GroupData;
                    groupsFragments.getDataFromActivity();
                } else {
                }

                if (memberData != null) {
                    filteredmemberData = memberData;
                    membersFragment.getDataFromActivity();
                } else {
                }

            }
        });
    }
    /*END method to enable searchBar and define its action*/

    /*START method to search query in Feed List*/
    private ArrayList<FeedData> filterFeed(ArrayList<FeedData> mList, String query) { //TODO searchView
        query = query.toLowerCase();

        ArrayList<FeedData> filteredList = new ArrayList<>();
        filteredList.clear();
        for (FeedData item : mList) {
            if (item.feedName.toLowerCase().contains(query) || item.feedId.toLowerCase().contains(query)
                    || item.feedText.toLowerCase().contains(query) || item.createdDate.toLowerCase().contains(query)) {
                filteredList.add(item);
            }
        }

        return filteredList;
    }
    /*END method to search query in Feed List*/

    /*START method to search query in Groupu List*/
    private ArrayList<GroupListData> filterGroup(ArrayList<GroupListData> mList, String query) { //TODO searchView
        query = query.toLowerCase();

        ArrayList<GroupListData> filteredList = new ArrayList<>();
        filteredList.clear();
        for (GroupListData item : mList) {
            if (item.groupName.toLowerCase().contains(query) || item.groupText.toLowerCase().contains(query)
                    || item.createdDate.toLowerCase().contains(query) || item.groupId.toLowerCase().contains(query)) {
                filteredList.add(item);
            }
        }

        return filteredList;
    }
    /*END method to search query in Group List*/

    /*START method to search query in member List*/
    private ArrayList<Member_GetterSetter> filterMember(ArrayList<Member_GetterSetter> mList, String query) { //TODO searchView
        query = query.toLowerCase();

        ArrayList<Member_GetterSetter> filteredList = new ArrayList<>();
        filteredList.clear();
        for (Member_GetterSetter item : mList) {
            if (item.fullName.toLowerCase().contains(query) /*|| item.phoneNumber.toLowerCase().contains(query)
                    || item.createdDate.toLowerCase().contains(query)||item.groupId.toLowerCase().contains(query)*/) {
                filteredList.add(item);
            }
        }

        return filteredList;
    }
    /*END method to search query in Group List*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.coa_menu, menu);


        //Search
        Bitmap iconSearch = BitmapFactory.decodeResource(getResources(), R.drawable.search); //Converting drawable into bitmap
        Bitmap newIconSearch = resizeBitmapImageFn(iconSearch, (int) convertDpToPixel(20f, this)); //resizing the bitmap
        Drawable dSearch = new BitmapDrawable(getResources(), newIconSearch); //Converting bitmap into drawable
        // menu.getItem(1).setIcon(dSearch);
        searchView.setMenuItem(menu.getItem(0));  //TODO searchView

        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    // The following callbacks are called for the SearchView.OnQueryChangeListener
    /*public boolean onQueryTextChange(String newText) {
        newText = newText.isEmpty() ? "" : "Query so far: " + newText;
        mSearchText.setText(newText);
        mSearchText.setTextColor(Color.GREEN);
        return true;
    }
*/
//Search,Notification ends

    public boolean onQueryTextSubmit(String query) {
        //Toast.makeText(this, "Searching for: " + query + "...", Toast.LENGTH_SHORT).show();
        mSearchText.setText("Searching for: " + query + "...");
        mSearchText.setTextColor(Color.RED);
        return true;
    }

    public float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
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
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);
            Log.d("Drawer", "Closing_FVCP");
        } else {
            super.onBackPressed();

            Intent intent = new Intent(FullViewOfClientsProceed.this, MainActivity.class);
            intent.putExtra("clientId", ClientId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }
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
        if (id == R.id.action_searchSP) {
            return true;
        }

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
            int value = 3;

            Intent intent = new Intent(FullViewOfClientsProceed.this, ProfileNavigationActivity.class);
            intent.putExtra("ClientId", ClientId);
            intent.putExtra("state", state);
            intent.putExtra("clientName", clientName);


            intent.putExtra("clientImagePath", clientImageURLPath);
            intent.putExtra("valueProfile", value);

            startActivity(intent);
            // Handle the camera action
        } /*else if (id == R.id.nav_Chat) {

        }*//* else if (id == R.id.nav_Settings) {
            Intent intent=new Intent(FullViewOfClientsProceed.this, SettingsNavigationActivity.class);

            startActivity(intent);

        }*/ else if (id == R.id.nav_TransactionReport) {

        } else if (id == R.id.nav_ChangePassword) {
            Intent intent = new Intent(FullViewOfClientsProceed.this, ForgotActivity.class);

            startActivity(intent);
        } else if (id == R.id.nav_Privacy_Policy) {
            Intent intent = new Intent(FullViewOfClientsProceed.this, PrivacyPolicyActivity.class);

            startActivity(intent);
        }
       /* else if (id == R.id.nav_Settings) {
            Intent intent=new Intent(FullViewOfClientsProceed.this, SettingsNavigationActivity.class);

            startActivity(intent);



        }*/

        else if (id == R.id.nav_logout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(FullViewOfClientsProceed.this); //Home is name of the activity
            builder.setMessage("Do you want to Exit the app?");
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

                    clearApplicationData();

                    finish();
                    finishAffinity();
                    /*Intent intent = new Intent(FullViewOfClientsProceed.this, LogInActivity.class);

                    startActivity(intent);
*/

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



        } else if (id == R.id.nav_txnhistory) {

            Intent intent = new Intent(FullViewOfClientsProceed.this, AllTransactionSummary.class);

            startActivity(intent);

        } else if (id == R.id.nav_clean_data) {
            AlertDialog.Builder builder = new AlertDialog.Builder(FullViewOfClientsProceed.this); //Home is name of the activity
            builder.setMessage("For selecting other Institute/client.Press OK. ");
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

                    Intent intent = new Intent(FullViewOfClientsProceed.this, FilterActivity.class);

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

            AlertDialog alert = builder.create();
            alert.show();

        } else if (id == R.id.nav_Contacts) {


            Intent intent = new Intent(FullViewOfClientsProceed.this, AllContacts.class);

            startActivity(intent);

        } else if (id == R.id.nav_share) {
            /*Intent intent=new Intent(MainActivity.this, ShareActivity.class);

            startActivity(intent);*/
            try {
                /*Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "SPApp");
                String sAux = "\n Let me recommend you this application .\n this is the easy way to pay your fee\n It is very cool app try it once ,download it from the below link given... \n \n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=in.sabpaisa.droid.sabpaisa";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Complete action using "));*/

                shareIntentSpecificApps();


            } catch (Exception e) {
                //e.toString();

            }


        } /*else if (id == R.id.nav_rate) {
            Intent intent=new Intent(FullViewOfClientsProceed.this, RateActivity.class);

            startActivity(intent);

///////////////

        }
*/

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


    /* @Override
     public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

     }*/
    @Override
    public void onFragmentSetFeeds(ArrayList<FeedData> feedData) {
        this.feedData = feedData;
    }

    @Override
    public void onFragmentSetGroups(ArrayList<GroupListData> groupData) {
        this.GroupData = groupData;
    }

    @Override
    public void onFragmentSetMembers(ArrayList<Member_GetterSetter> memberData) {
        this.memberData = memberData;
    }


    @Override
    public ArrayList<FeedData> getFeedDataList() {
        return filteredfeedList;
    }


    @Override //TODO searchView
    public ArrayList<GroupListData> getGroupDataList() {
        return filteredGroupList;
    }


    @Override
    public ArrayList<Member_GetterSetter> getMemberDataList() {
        return filteredmemberData;
    }

    @Override
    public void onFragmentSetContacts(ArrayList<ContactList> contactLists) {

    }

    @Override
    public void onFragmentSetClients(ArrayList<SkipClientData> clientData) {

    }

    private String getUserImage(final String token) {

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
                    i = userImageUrl;
                    Log.d("check123", "" + i);
                    ImageView niv = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.profile_image);

                    Glide.with(getApplicationContext())
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

        AppController.getInstance().addToRequestQueue(request, tag_string_req);


        return tag_string_req;
    }

    private void showProfileData() {

        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_Show_UserProfile + "?token=" + useracesstoken, new Response.Listener<String>() {

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
                        Log.d("namemainFull", "" + name);
                        Log.d("namemainFull", "" + mobNumber);

                        usernameniv.setText(object.getJSONObject("response").getString("fullName").toString());
                        //mNumber.setText(object.getJSONObject("response").getString("contactNumber").toString());

                        mailIdniv.setText(x);
                        //mailIdniv.setText(object.getJSONObject("response").getString("emailId").toString());
                        /// et_address.setText(object.getJSONObject("response").getString("address").toString());
                        //  et_UserName.setText(object.getJSONObject("response").getString("fullName").toString());
                        Log.d("skipusername", "userName" + usernameniv);
                        Log.d("skipusermailid", "Mail" + mailIdniv);

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
                    //TODOonb
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

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences(MySharedPrefOnFullViewOfClientProceed, 0);
        preferences.edit().remove("Groupts").commit();

        // Store our shared preference
        SharedPreferences.Editor editor = getSharedPreferences("FullTime", MODE_PRIVATE).edit();

       /* SharedPreferences sp = getSharedPreferences("OURINFO", MODE_PRIVATE);
        Editor ed = sp.edit();
       */
        editor.putBoolean("active", true);
        Log.d("ARCOnStartgroup", "----");
        editor.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();

        Date date = new Date();
        //getTime() returns current time in milliseconds
        long time = date.getTime();
        //Passed the milliseconds to constructor of Timestamp class
        Fullviewts = new Timestamp(time);
        Fts = String.valueOf(Fullviewts);
        System.out.println("Current Time Stamp: " + Fts);
        Log.d("ARCTimefull", "" + time);
        Log.d("ARCTimeFullts1", "" + Fts);

        // Store our shared preference
        SharedPreferences sp = getSharedPreferences(MySharedPrefOnFullViewOfClientProceed, MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("active", false);
        ed.putString("FullViewts", String.valueOf(Long.valueOf(time)));
        Log.d("ARCTimeFullts111", "" + String.valueOf(Fts));
        Log.d("ARCOnStopFull", "--" + String.valueOf(Long.valueOf(time)));
        ed.commit();

      /*  // Store our shared preference
        SharedPreferences sp = getSharedPreferences(MySharedPRoceedGroupFullScreen, MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("active", false);
        ed.putString("Groupts", ts);
        Log.d("ARCOnStopGroup","--");

        ed.commit();
        Long tsLong = System.currentTimeMillis()/1000;
        ts = tsLong.toString();
        Log.d("ARCTimeGroup",""+ts);
*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Store our shared preference
        SharedPreferences sp = getSharedPreferences("OURINFO", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("active", false);
        Log.d("ARCOnResumFullViewClnt", "----");

        ed.commit();

        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }

    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v("FVOCP", "Internet Connection Not Present");
            return false;
        }
    }


    public void shareIntentSpecificApps() {


        List<Intent> targetShareIntents = new ArrayList<Intent>();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        PackageManager pm = FullViewOfClientsProceed.this.getPackageManager();
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
                Toast.makeText(FullViewOfClientsProceed.this, "No app to share.", Toast.LENGTH_LONG).show();
            }
        }
//Check

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


/*END methods for implementations*/