package in.sabpaisa.droid.sabpaisa.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import com.bumptech.glide.Glide;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
//import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import in.sabpaisa.droid.sabpaisa.Adapter.ViewPagerAdapter;
import in.sabpaisa.droid.sabpaisa.AllContacts;
import in.sabpaisa.droid.sabpaisa.AllTransactionSummary;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.CommentAdapter;
import in.sabpaisa.droid.sabpaisa.CommentData;
import in.sabpaisa.droid.sabpaisa.FeedData;
import in.sabpaisa.droid.sabpaisa.FeedsFragments;
import in.sabpaisa.droid.sabpaisa.FilterActivity;
import in.sabpaisa.droid.sabpaisa.FilterActivity1;
import in.sabpaisa.droid.sabpaisa.Fragments.SkipFeedFragment;
import in.sabpaisa.droid.sabpaisa.Fragments.SkipGroupFragment;
import in.sabpaisa.droid.sabpaisa.Fragments.SkipMembersFragment;
import in.sabpaisa.droid.sabpaisa.GroupListData;
import in.sabpaisa.droid.sabpaisa.GroupsFragments;
import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.MainActivitySkip;
import in.sabpaisa.droid.sabpaisa.Model.*;
import in.sabpaisa.droid.sabpaisa.Model.SkipClientData;
import in.sabpaisa.droid.sabpaisa.PayFragments;
import in.sabpaisa.droid.sabpaisa.R;

import in.sabpaisa.droid.sabpaisa.Adapter.CommentAdapterDatabase;
import in.sabpaisa.droid.sabpaisa.SimpleDividerItemDecoration;
import in.sabpaisa.droid.sabpaisa.UIN;

import static android.support.v4.widget.SwipeRefreshLayout.*;
import static in.sabpaisa.droid.sabpaisa.LogInActivity.PREFS_NAME;

//This Activity has rolled back

/*implements SwipeRefreshLayout.OnRefreshListener*/

public class SkipClientDetailsScreen extends AppCompatActivity implements OnFragmentInteractionListener, OnRefreshListener, NavigationView.OnNavigationItemSelectedListener, SkipFeedFragment.GetDataInterface, SkipGroupFragment.GetDataInterface, SkipMembersFragment.GetDataInterface {

    public static String clientName, state, position;
    public static String clientImageURLPath = null;
    public static String clientLogoURLPath = null;
    public static String appCid;
    public static String MySharedPrefOnSkipClientDetailsScreenForAppCid = "MySharedPrefOnSkipClientDetailsScreenForAppCid";
    private ViewPager viewPager;

    private boolean isLoading = false;
    private TabLayout tabLayout;

    TextView particular_client_name_proceed, particular_client_address_proceed;
    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;

    //Fragments Attached
    SkipFeedFragment skipFeedFragment;
    SkipGroupFragment skipGroupFragment;
    SkipMembersFragment skipMembersFragment;

    ImageView ClientImagePRoceed;

    MaterialSearchView searchView;
    AppBarLayout appBarLayout;
    ArrayList<FeedData> feedData;
    ArrayList<FeedData> filteredfeedList;
    ArrayList<GroupListData> GroupData;
    ArrayList<GroupListData> filteredGroupList;
    ArrayList<MemberSpaceModel> memberData;
    ArrayList<MemberSpaceModel> filteredmemberData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // CommonUtils.setFullScreen(this);
        setContentView(R.layout.skip_clientdetails);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        clientName = intent.getStringExtra("clientName");
        state = intent.getStringExtra("state");
        appCid = intent.getStringExtra("appCid");
        clientImageURLPath = getIntent().getStringExtra("clientImagePath");
        clientLogoURLPath = getIntent().getStringExtra("clientLogoPath");
        Log.d("clientIdSCDS", "" + appCid);
        Log.d("clientImagePath", "" + clientImageURLPath);
        Log.d("clientLogoPath", "" + clientLogoURLPath);

        SharedPreferences.Editor editor = getSharedPreferences(MySharedPrefOnSkipClientDetailsScreenForAppCid, MODE_PRIVATE).edit();
        editor.putString("appCid", appCid);
        editor.commit();


        particular_client_name_proceed = (TextView) findViewById(R.id.particular_client_name_proceed);
        particular_client_name_proceed.setText(clientName);
        particular_client_address_proceed = (TextView) findViewById(R.id.particular_client_address_proceed);
        particular_client_address_proceed.setText(state);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);

        int[] tabIcons = {
                R.drawable.feeds,
                R.drawable.groups,
                R.drawable.payments,
                R.drawable.members,
        };

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[3]);

        position = getIntent().getStringExtra("FRAGMENT_ID");

        Log.d("SkipClientDetailScr", "FRAGMENT_ID__" + position);

        if (position != null) {
            viewPager.setCurrentItem(Integer.parseInt(position));
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setHomeAsUpIndicator(R.drawable.ic_drawer);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        toolbar.setNavigationIcon(R.drawable.ic_navigation);

        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_clean_data).setVisible(false);

        ClientImagePRoceed = (ImageView) findViewById(R.id.ClientImagePRoceed);

        Glide.with(SkipClientDetailsScreen.this)/*//Added on 1st Feb*/
                .load(clientLogoURLPath)
                .error(R.drawable.image_not_found)
                .into(ClientImagePRoceed);

        // Searching functionality
        searchViewBar();

    }

    @Override
    public void onRefresh() {

    }


    private void setupViewPager(ViewPager viewPager) {

        Bundle bundle = new Bundle();
        bundle.putString("clientName", clientName);
        bundle.putString("clientLogoPath", clientLogoURLPath);
        bundle.putString("clientImagePath", clientImageURLPath);
        bundle.putString("state", state);

        skipFeedFragment = new SkipFeedFragment();
        skipGroupFragment = new SkipGroupFragment();
        skipMembersFragment = new SkipMembersFragment();

        skipFeedFragment.setArguments(bundle);
        skipGroupFragment.setArguments(bundle);
        skipMembersFragment.setArguments(bundle);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(skipFeedFragment, "");
        adapter.addFragment(skipGroupFragment, "");
        adapter.addFragment(skipMembersFragment, "");
        //adapter.addFragment(new GroupsFragments(),"Groups");
        //adapter.addFragment(new PayFragments(),"Make Payment");
        viewPager.setAdapter(adapter);


    }


    /*START method to enable searchBar and define its action*/
    private void searchViewBar() { //TODO searchView
        searchView = (MaterialSearchView) findViewById(R.id.search_viewSP);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() == 0) {

                    if (feedData != null) {
                        filteredfeedList = feedData;
                        skipFeedFragment.getDataFromActivity();
                    } else {
                    }

                    if (GroupData != null) {
                        filteredGroupList = GroupData;
                        skipGroupFragment.getDataFromActivity();
                    } else {
                    }

                    if (memberData != null) {
                        filteredmemberData = memberData;
                        skipMembersFragment.getDataFromActivity();
                    } else {
                    }


                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
                if (query.length() > 0) {

                    if (feedData != null) {
                        filteredfeedList = filterFeed(feedData, query);
                        skipFeedFragment.getDataFromActivity();
                    } else {
                    }

                    if (GroupData != null) {
                        filteredGroupList = filterGroup(GroupData, query);
                        skipGroupFragment.getDataFromActivity();
                    } else {
                    }

                    if (memberData != null) {
                        filteredmemberData = filterMember(memberData, query);
                        skipMembersFragment.getDataFromActivity();
                    } else {
                    }

                    Log.wtf("FilteredList_FEED", String.valueOf(filteredfeedList));
                    Log.wtf("FilteredList_Group", String.valueOf(filteredGroupList));
                    Log.wtf("FilteredList_Member", String.valueOf(filteredmemberData));

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
                        skipFeedFragment.getDataFromActivity();
                    } else {
                    }

                    if (GroupData != null) {
                        filteredGroupList = GroupData;
                        skipGroupFragment.getDataFromActivity();
                    } else {
                    }



                    if (memberData != null) {
                        filteredmemberData = memberData;
                        skipMembersFragment.getDataFromActivity();
                    } else {
                    }

                    Log.wtf("FilteredList_FEED", String.valueOf(filteredfeedList));
                    Log.wtf("FilteredList_Group", String.valueOf(filteredGroupList));
                    Log.wtf("FilteredList_Member", String.valueOf(filteredmemberData));


                } else if (newText.length() > 0) {

                    if (feedData != null) {
                        filteredfeedList = filterFeed(feedData, newText);
                        skipFeedFragment.getDataFromActivity();
                    } else {
                    }

                    if (GroupData != null) {
                        filteredGroupList = filterGroup(GroupData, newText);
                        skipGroupFragment.getDataFromActivity();
                    } else {
                    }

                    if (memberData != null) {
                        filteredmemberData = filterMember(memberData, newText);
                        skipGroupFragment.getDataFromActivity();
                    } else {
                    }

                    Log.wtf("FilteredList_FEED", String.valueOf(filteredfeedList));
                    Log.wtf("FilteredList_Group", String.valueOf(filteredGroupList));
                    Log.wtf("FilteredList_Member", String.valueOf(filteredmemberData));


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
                    skipFeedFragment.getDataFromActivity();
                } else {
                }

                if (GroupData != null) {
                    filteredGroupList = GroupData;
                    skipGroupFragment.getDataFromActivity();
                } else {
                }

                if (memberData != null) {
                    filteredmemberData = memberData;
                    skipMembersFragment.getDataFromActivity();
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
    private ArrayList<MemberSpaceModel> filterMember(ArrayList<MemberSpaceModel> mList, String query) { //TODO searchView
        query = query.toLowerCase();

        ArrayList<MemberSpaceModel> filteredList = new ArrayList<>();
        filteredList.clear();
        for (MemberSpaceModel item : mList) {
            if (item.fullName.toLowerCase().contains(query)) {
                filteredList.add(item);
            }
        }

        return filteredList;
    }
    /*END method to search query in Group List*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);


        //Search
        Bitmap iconSearch = BitmapFactory.decodeResource(getResources(), R.drawable.search); //Converting drawable into bitmap
        Bitmap newIconSearch = resizeBitmapImageFn(iconSearch, (int) convertDpToPixel(20f, this)); //resizing the bitmap
        Drawable dSearch = new BitmapDrawable(getResources(), newIconSearch); //Converting bitmap into drawable
        // menu.getItem(1).setIcon(dSearch);
        searchView.setMenuItem(menu.getItem(0));  //TODO searchView

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        android.app.Fragment newFragment = null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Profile) {
            int value = 2;
            Intent intent = new Intent(SkipClientDetailsScreen.this, ProfileNavigationActivity.class);
            //   intent.putExtra("ClientId",ClientId);
            intent.putExtra("valueProfile", value);
            startActivity(intent);
            // Handle the camera action
        } /*else if (id == R.id.nav_Chat) {

        }*/ /*else if (id == R.id.nav_Settings) {
            Intent intent=new Intent(MainActivitySkip.this, SettingsNavigationActivity.class);

            startActivity(intent);

        }*/ else if (id == R.id.nav_ChangePassword) {
            Intent intent = new Intent(SkipClientDetailsScreen.this, ForgotActivity.class);

            startActivity(intent);
        } else if (id == R.id.nav_Privacy_Policy) {
            Intent intent = new Intent(SkipClientDetailsScreen.this, PrivacyPolicyActivity.class);

            startActivity(intent);
        } else if (id == R.id.nav_logout) {


            AlertDialog.Builder builder = new AlertDialog.Builder(SkipClientDetailsScreen.this); //Home is name of the activity
            builder.setMessage("Do you want to Exit the app?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.remove("logged");
                    editor.commit();
                    clearApplicationData();
                    finish();
                    finishAffinity();
                    /*Intent intent = new Intent(MainActivitySkip.this, LogInActivity.class);

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


            // onLogoutClick(view);
            /*SharedPreferences sp=getSharedPreferences("login",MODE_PRIVATE);
            SharedPreferences.Editor e=sp.edit();
            e.clear();
            e.commit();

            startActivity(new Intent(Home.this,MainActivity.class));
            finish();   //f

            SharedPreferences settings = getSharedPreferences("your_preference_name", 0);
            boolean isLoggedIn = settings.getBoolean("LoggedIn", false);

            if(isLoggedIn )
            {
                //Go directly to Homescreen.
            }
           */


        } else if (id == R.id.nav_txnhistory) {

            Intent intent = new Intent(SkipClientDetailsScreen.this, AllTransactionSummary.class);

            startActivity(intent);

        } else if (id == R.id.nav_Contacts) {


            Intent intent = new Intent(SkipClientDetailsScreen.this, AllContacts.class);

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


        }
//changes 25 april

        else if (id == R.id.nav_TransactionReport) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onFragmentSetFeeds(ArrayList<FeedData> feedData) {
        this.feedData = feedData;
    }

    @Override
    public void onFragmentSetGroups(ArrayList<GroupListData> groupData) {
        this.GroupData = groupData;
    }


    @Override
    public void onFragmentSetMembersSpace(ArrayList<MemberSpaceModel> memberSpaceData) {
        this.memberData = memberSpaceData;
    }


    @Override
    public ArrayList<FeedData> getFeedDataList() {
        return filteredfeedList;
    }


    @Override
    public ArrayList<GroupListData> getGroupDataList() {
        return filteredGroupList;
    }


    @Override
    public ArrayList<MemberSpaceModel> getMemberDataList() {
        return filteredmemberData;
    }


    @Override
    public void onFragmentSetClients(ArrayList<PersonalSpaceModel> clientData) {

    }


    @Override
    public void onFragmentSetMembers(ArrayList<Member_GetterSetter> memberData) {

    }


    @Override
    public void onFragmentSetContacts(ArrayList<ContactList> contactLists) {

    }


    public void shareIntentSpecificApps() {


        List<Intent> targetShareIntents = new ArrayList<Intent>();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        PackageManager pm = SkipClientDetailsScreen.this.getPackageManager();
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
                Toast.makeText(SkipClientDetailsScreen.this, "No app to share.", Toast.LENGTH_LONG).show();
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


    @Override
    public void onBackPressed() {


        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);
            Log.d("Drawer", "Closing_FVCP");
        }else {
            super.onBackPressed();
        }
    }
}