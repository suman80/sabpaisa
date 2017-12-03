package in.sabpaisa.droid.sabpaisa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Member;
import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.ViewPagerAdapter;
import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.Model.ContactList;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
/**
 * Created by SabPaisa on 03-07-2017.
 */





import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
public class COA extends AppCompatActivity implements OnFragmentInteractionListener, FeedsFragments.GetDataInterface, GroupsFragments.GetDataInterface {

    // Within which the entire activity is enclosed
    DrawerLayout mDrawerLayout;

    // ListView represents Navigation Drawer
    LinearLayout mDrawerLeftLayout;
    ListView mDrawerList;
    // ActionBarDrawerToggle indicates the presence of Navigation Drawer in the action bar
    ActionBarDrawerToggle mDrawerToggle;
    // Title of the action bar
    String mTitle = "Council Of Architecture";
    boolean idSideMenu = false;
    //BottomBar bottomBar;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    /*START Globally declaring variables*/
    AppBarLayout appBarLayout;
    TextView mSearchText;
    ProgressDialog loading = null;
    MaterialSearchView searchView;
    ArrayList<FeedData> feedData;
    ArrayList<FeedData> filteredfeedList;
    ArrayList<GroupListData> GroupData;
    ArrayList<GroupListData> filteredGroupList;
    FeedsFragments feedsFragments;
    GroupsFragments groupsFragments;
    PayFragments payFragments;
    LinearLayout paymentButton;
    LinearLayout chatButton;
    LinearLayout membersButton;
    Toolbar toolbar;
    private AHBottomNavigation bottomNavigation;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    /*END Globally declaring variables*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coa_main_screen);



        mCollapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitleEnabled(false);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);

        // This is used for the app custom toast and activity transition
       // ChatSDKUiHelper.initDefault();

// Init the network manager
        //BNetworkManager.init(getApplicationContext());

// Create a new adapter
        //BChatcatNetworkAdapter adapter = new BChatcatNetworkAdapter(getApplicationContext());

// Set the adapter
        //BNetworkManager.sharedManager().setNetworkAdapter(adapter);


        mCollapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitleEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        searchViewBar();
        paymentButton = (LinearLayout)findViewById(R.id.payment_button);
        chatButton = (LinearLayout)findViewById(R.id.chat);
        membersButton = (LinearLayout)findViewById(R.id.members);

        setUpBottomBar();
        loadHomeFragment();

    }

    private void loadHomeFragment() {


    }

    @Override
    protected void onStart() {
        super.onStart();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Council Of Architecture");

        setSupportActionBar(toolbar);
        loadSideMenu(toolbar);
    }

    /*START method to give handles to BottomBar Buttons*/
    private void setUpBottomBar() {
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
                /*Intent myIntent = new Intent(COA.this, Payment.class);
                COA.this.startActivity(myIntent);*/
                // Toast.makeText(MainActivity.this, "Payment Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        chatButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
               /* Intent myIntent = new Intent(COA.this, ChatSDKLoginActivity.class);
                COA.this.startActivity(myIntent);*/
            }


          /*  @Override
            public void onClick(View v) {
              //  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                //fab.setOnClickListener(new View.OnClickListener() {
                   // @Override
                    public void onClick(View view) {

                    }
                });*//*

                Toast.makeText(MainActivity.this, "Chat Clicked", Toast.LENGTH_SHORT).show();
            }*/
        });
        membersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3);
                /*Intent myIntent = new Intent(COA.this, Member.class);
                COA.this.startActivity(myIntent);*/
                // Toast.makeText(MainActivity.this, "Members clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /*END method to give handles to BottomBar Buttons*/

    /*START method to enable searchBar and define its action*/
    private void searchViewBar() { //TODO searchView
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;


            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    filteredfeedList = filterFeed(feedData, newText);
                    filteredGroupList = filterGroup(GroupData, newText);
//                    filteredMemberList = filterMember(MemberData, newText);
                    Log.wtf("FilteredList", String.valueOf(filteredfeedList));
                    feedsFragments.getDataFromActivity();
                    groupsFragments.getDataFromActivity();
//                    memberFragment.getDataFromActivity();
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

                filteredfeedList = feedData;
                feedsFragments.getDataFromActivity();
                filteredGroupList = GroupData;
                groupsFragments.getDataFromActivity();
//                filteredMemberList = MemberData;
//                memberFragment.getDataFromActivity();
            }
        });
    }
    /*END method to enable searchBar and define its action*/

    /*START method to search query in Feed List*/
    private ArrayList<FeedData> filterFeed(ArrayList<FeedData> mList, String query) { //TODO searchView
        query = query.toLowerCase();

        ArrayList<FeedData> filteredList = new ArrayList<>();
        filteredList.clear();
        /*for (FeedData item : mList) {
            if (item.feed_Name.toLowerCase().contains(query) || item.feedId.toLowerCase().contains(query)
                    || item.feedText.toLowerCase().contains(query) || item.feed_date.toLowerCase().contains(query)) {
                filteredList.add(item);
            }
        }*/

        return filteredList;
    }
    /*END method to search query in Feed List*/

    /*START method to search query in Groupu List*/
    private ArrayList<GroupListData> filterGroup(ArrayList<GroupListData> mList, String query) { //TODO searchView
        query = query.toLowerCase();

        ArrayList<GroupListData> filteredList = new ArrayList<>();
        filteredList.clear();
       /* for (GroupListData item : mList) {
            if (item.groupName.toLowerCase().contains(query) || item.groupDescription.toLowerCase().contains(query)
                    || item.groupId.toLowerCase().contains(query)*//*||item.group_count.toLowerCase().contains(query)*//*) {
                filteredList.add(item);
            }
        }*/

        return filteredList;
    }
    /*END method to search query in Group List*/


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        feedsFragments = new FeedsFragments();
        adapter.addFragment(feedsFragments,"Feeds");
        groupsFragments = new GroupsFragments();
        adapter.addFragment(groupsFragments,"Groups");
        payFragments = new PayFragments();
        adapter.addFragment(payFragments, "Pay");
        viewPager.setAdapter(adapter);
    }


    private void loadSideMenu(Toolbar toolbar) {
        mTitle = toolbar.getTitle().toString();

        // Getting reference to the DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerLeftLayout = (LinearLayout) findViewById(R.id.drawer_left_list_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);

        // Getting reference to the ActionBarDrawerToggle
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            /** Called when drawer is closed */
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);

                invalidateOptionsMenu();
                idSideMenu = false;
            }

            /** Called when a drawer is opened */
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu();
                idSideMenu = true;
            }
        };

        // Setting DrawerToggle on DrawerLayout
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Creating an ArrayAdapter to add items to the listview mDrawerList
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getBaseContext(),
                R.layout.drawer_list_item,
                getResources().getStringArray(R.array.left_side_menu)
        );

        // Setting the adapter on mDrawerList
        mDrawerList.setAdapter(adapter);

        // Enabling Home button
        getSupportActionBar().setHomeButtonEnabled(true);

        // Enabling Up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawer,
                getApplicationContext().getTheme());

        mDrawerToggle.setHomeAsUpIndicator(drawable);

        // Setting item click listener for the listview mDrawerList
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {

                onCallLeftSideMenu(position);
                // Closing the drawer
                mDrawerLayout.closeDrawer(mDrawerLeftLayout);
                mDrawerLayout.clearAnimation();
            }
        });

    }

    private void onCallLeftSideMenu(int position) {
        // Getting an array of rivers
        String[] menuItems = getResources().getStringArray(R.array.left_side_menu);

        //Currently selected river
        mTitle = menuItems[position];
        if (menuItems[position].equals(menuItems[0])) {
            /*Intent intent = new Intent(COA.this, ProfileActivity.class);
            //Intent intent = new Intent(MainActivity.this, Privacy.class);
            startActivity(intent);*/

            //mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else if (menuItems[position].equals(menuItems[1])) {
          /*  Intent intent = new Intent(COA.this, Payment.class);
            //Intent intent = new Intent(MainActivity.this, Privacy.class);
            startActivity(intent);*/
        } else if (menuItems[position].equals(menuItems[2])) {


          /*  Intent intent = new Intent(COA.this, ChatSDKLoginActivity.class);
            startActivity(intent);*/
        } else if (menuItems[position].equals(menuItems[3])) {



            Intent intent = new Intent(COA.this, FeedDetails.class);
            startActivity(intent);
        } else if (menuItems[position].equals(menuItems[4])) {
            //ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

            Log.e("SSS",menuItems[4]);
            groupsFragments = new GroupsFragments();
            //adapter.addFragment(groupsFragments,"Groups");

            Intent intent = new Intent(COA.this, GroupDetails.class);
            startActivity(intent);
        } else if (menuItems[position].equals(menuItems[5])) {
            /*Intent intent = new Intent(COA.this, Forms.class);
            startActivity(intent);*/
        }
        //Payment Due
        else if (menuItems[position].equals(menuItems[6])) {
            Intent intent = new Intent(COA.this, Member.class);
            startActivity(intent);
        } else if (menuItems[position].equals(menuItems[7])) {
           /* Intent intent = new Intent(COA.this, Setting.class);
            startActivity(intent);*/
            //Send Money
        } else  if (menuItems[position].equals(menuItems[8])) {
            /*Intent intent = new Intent(COA.this, Logout.class);
            startActivity(intent);*/
        }

        else  if (menuItems[position].equals(menuItems[9])) {
            /*Intent intent = new Intent(COA.this, Privacy.class);
            startActivity(intent);*/
        }
        //Setting
    /*    } else if (menuItems[position].equals(menuItems[9])) {
            Intent intent = new Intent(MainActivity.this, COAChatScreen.class);
            startActivity(intent);*//*
            //Send Feedback
         else if (menuItems[position].equals(menuItems[10])) {
            Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
            // Private Policy
        } else if (menuItems[position].equals(menuItems[11])) {
            Intent intent = new Intent(MainActivity.this, Logout.class);
            startActivity(intent);
            // Private Policy
        }*/

        // Logout
            /*SharedPref.putBoolean(getApplicationContext(), AppConfiguration.OPT_VARIFICATION, false);
            Intent intent = new Intent(MainActivity.this, SplashScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();*/
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.coa_menu, menu);

        //Notification
        Bitmap iconDot = BitmapFactory.decodeResource(getResources(), R.drawable.three_dot_menu); //Converting drawable into bitmap
        Bitmap newIconDot = resizeBitmapImageFn(iconDot, (int) convertDpToPixel(20f, this)); //resizing the bitmap
        Drawable dDot = new BitmapDrawable(getResources(), newIconDot); //Converting bitmap into drawable
        menu.getItem(2).setIcon(dDot);

        //Search
        Bitmap iconSearch = BitmapFactory.decodeResource(getResources(), R.drawable.search); //Converting drawable into bitmap
        Bitmap newIconSearch = resizeBitmapImageFn(iconSearch, (int) convertDpToPixel(20f, this)); //resizing the bitmap
        Drawable dSearch = new BitmapDrawable(getResources(), newIconSearch); //Converting bitmap into drawable
        menu.getItem(1).setIcon(dSearch);
        searchView.setMenuItem(menu.getItem(1));  //TODO searchView


        //Notification
        Bitmap iconNoti = BitmapFactory.decodeResource(getResources(), R.drawable.notification); //Converting drawable into bitmap
        Bitmap newIconNoti = resizeBitmapImageFn(iconNoti, (int) convertDpToPixel(20f, this)); //resizing the bitmap
        Drawable dNoti = new BitmapDrawable(getResources(), newIconNoti); //Converting bitmap into drawable
        menu.getItem(0).setIcon(dNoti);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    // The following callbacks are called for the SearchView.OnQueryChangeListener
    public boolean onQueryTextChange(String newText) {
        newText = newText.isEmpty() ? "" : "Query so far: " + newText;
        mSearchText.setText(newText);
        mSearchText.setTextColor(Color.GREEN);
        return true;
    }

//Search,Notification ends

    public boolean      onQueryTextSubmit      (String query) {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent = null;

        switch (item.getItemId()) {
           /* case R.id.action_notification:
                //intent = new Intent(this, COANotification.class);
                startActivity(intent);
                return true;*/
            case R.id.action_search:
                /*intent = new Intent(this, Search.class);
                startActivity(intent);*/
                return true;
            /*case R.id.three_dot_menu:

  //              onClickMenuPopup();

                return true;*/
            case R.id.action_home:

                //            onClickMenuPopup();

                return true;
            case R.id.action_profile:
                /*intent = new Intent(COA.this, ProfileActivity.class);
                startActivity(intent);*/
                return true;
            case R.id.action_payment:
               /* intent = new Intent(COA.this, Payment.class);
                startActivity(intent);*/
                //Toast.makeText(this, "Feature Coming soon", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_chat:
                /*intent = new Intent(COA.this, ChatSDKLoginActivity.class);
                startActivity(intent);*/
                return true;
          /*  case R.id.action_chats:

                intent = new Intent(MainActivity.this, COAChatScreen.class);
                startActivity(intent);

                return true;
            case R.id.action_send_money:

                onClickMenuPopup();

                return true;
            case R.id.action_members:

                // intent = new Intent(MainActivity.this, MembersScreen.class);
                startActivity(intent);

                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void hidepDialog() {
        if (loading != null) {
            loading.dismiss();
        }
    }

    private void showpDialog(View v) {
        loading = new ProgressDialog(v.getContext());
        loading.setCancelable(true);
        loading.setMessage("Loading....");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.show();
    }

    /*START method to handle searchView on back pressed, if it is open*/
    @Override
    public void onBackPressed() {  //TODO searchView
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }
    /*END method to handle searchView on back pressed, if it is open*/

    /*START methods for implementations*/
    @Override  //TODO searchView
    public void onFragmentSetFeeds(ArrayList<FeedData> feedData) {
        this.feedData = feedData;
    }

    @Override //TODO searchView
    public void onFragmentSetGroups(ArrayList<GroupListData> groupData) {
        this.GroupData = groupData;
    }

    @Override //TODO searchView
    public ArrayList<FeedData> getFeedDataList() {
        return filteredfeedList;
    }


    @Override //TODO searchView
    public ArrayList<GroupListData> getGroupDataList() {
        return filteredGroupList;
    }

    @Override
    public void onFragmentSetContacts(ArrayList<ContactList> contactLists) {

    }
    /*END methods for implementations*/

}
