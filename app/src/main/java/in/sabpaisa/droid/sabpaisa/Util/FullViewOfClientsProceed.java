package in.sabpaisa.droid.sabpaisa.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

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
import in.sabpaisa.droid.sabpaisa.Model.*;
import in.sabpaisa.droid.sabpaisa.Model.SkipClientData;
import in.sabpaisa.droid.sabpaisa.PayFeeFragment;
import in.sabpaisa.droid.sabpaisa.PayFragments;
import in.sabpaisa.droid.sabpaisa.R;

import static in.sabpaisa.droid.sabpaisa.LogInActivity.PREFS_NAME;

public class FullViewOfClientsProceed extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,OnFragmentInteractionListener,ProceedFeedsFragments.GetDataInterface,ProceedGroupsFragments.GetDataInterface {
    ImageView clientImagePath;
    String clientName,state,landingPage;
    public static String ClientId;
    public static String clientImageURLPath=null;
    private ViewPager viewPager;
    TextView mSearchText;
    ActionBarDrawerToggle toggle;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    public static String userImageUrl=null;
    AppBarLayout appBarLayout;
    MaterialSearchView searchView;
    ArrayList<FeedData> feedData;
    ArrayList<FeedData> filteredfeedList;
    ArrayList<GroupListData> GroupData;
    ArrayList<GroupListData> filteredGroupList;
    ProceedFeedsFragments feedsFragments;
    ProceedGroupsFragments groupsFragments;
    Members membersFragment;

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
       // appBarLayout.addOnOffsetChangedListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewpagerproceed);
        setupViewPager(viewPager);

        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        landingPage =getIntent().getStringExtra("landingPage");
        Log.d("page",""+landingPage);

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.MYSHAREDPREF, MODE_PRIVATE);
        userImageUrl=sharedPreferences.getString("userImageUrl","abc");
        //Log.d("userImageUrlFrag","-->"+userImageUrl);
        Log.d("userImageUrl_FVOCL"," "+userImageUrl);

        Intent intent=getIntent();
        clientName = intent.getStringExtra("clientName");
        state = intent.getStringExtra("state");
        clientImageURLPath= getIntent().getStringExtra("clientImagePath");
        ClientId=getIntent().getStringExtra("clientId");
        //userImageUrl=getIntent().getStringExtra("userImageUrl");
        Log.d("ClientId_FVOCL"," "+ClientId);
        Log.d("userImageUrl_FVOCL"," "+userImageUrl);

        ImageView niv = (ImageView)navigationView.getHeaderView(0).findViewById(R.id.profile_image);

        Glide
                .with(FullViewOfClientsProceed.this)
                .load(userImageUrl)
                .error(R.drawable.default_users)
                .into(niv);

        SharedPreferences.Editor editor = getSharedPreferences(MySharedPrefOnFullViewOfClientProceed,MODE_PRIVATE).edit();
        editor.putString("clientId",ClientId);
        editor.commit();

        TextView clientNameTextView = (TextView) findViewById(R.id.particular_client_name_proceed);

        TextView stateTextView = (TextView) findViewById(R.id.particular_client_address_proceed);


        clientImagePath = (ImageView)findViewById(R.id.ClientImagePRoceed);
        clientNameTextView.setText( clientName);
        stateTextView.setText(state);
        new DownloadImageTask(clientImagePath).execute(clientImageURLPath);
        searchViewBar();


    }


    private void setupViewPager(ViewPager viewPager) {


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        feedsFragments = new ProceedFeedsFragments();
        adapter.addFragment(feedsFragments,"Feeds"); //changing here creating different frags

        groupsFragments = new ProceedGroupsFragments();
        adapter.addFragment(groupsFragments,"Groups");//changing here creating different frags

        adapter.addFragment(new PayFragments(),"Payment");

        membersFragment=new Members();
        adapter.addFragment(membersFragment,"Members");

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
                if (query.length()==0&& feedData!=null && GroupData!=null){
                    filteredfeedList = feedData;
                    filteredGroupList = GroupData;
                    feedsFragments.getDataFromActivity();
                    groupsFragments.getDataFromActivity();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
                if (query.length() > 0 && feedData!=null && GroupData!=null) {
                    filteredfeedList = filterFeed(feedData, query);
                    filteredGroupList = filterGroup(GroupData, query);

//                    filteredMemberList = filterMember(MemberData, newText);
                    Log.wtf("FilteredList", String.valueOf(filteredfeedList));
                    feedsFragments.getDataFromActivity();
                    groupsFragments.getDataFromActivity();
//                    memberFragment.getDataFromActivity();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length()==0&& feedData!=null && GroupData!=null){
                    filteredfeedList = feedData;
                    Log.wtf("filteredfeedList ", String.valueOf(filteredfeedList));
                    filteredGroupList = GroupData;
                    feedsFragments.getDataFromActivity();
                    groupsFragments.getDataFromActivity();
                }
                else if (newText.length() > 0 && feedData!=null && GroupData!=null) {
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
                    || item.createdDate.toLowerCase().contains(query)||item.groupId.toLowerCase().contains(query)) {
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
        menu.getItem(1).setIcon(dSearch);
        searchView.setMenuItem(menu.getItem(1));  //TODO searchView

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
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
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
            Intent intent=new Intent(FullViewOfClientsProceed.this, ProfileNavigationActivity.class);

            startActivity(intent);
            // Handle the camera action
        } /*else if (id == R.id.nav_Chat) {

        }*//* else if (id == R.id.nav_Settings) {
            Intent intent=new Intent(FullViewOfClientsProceed.this, SettingsNavigationActivity.class);

            startActivity(intent);

        }*/
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
       /* else if (id == R.id.nav_Settings) {
            Intent intent=new Intent(FullViewOfClientsProceed.this, SettingsNavigationActivity.class);

            startActivity(intent);



        }*/

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

    @Override
    public void onFragmentSetClients(ArrayList<SkipClientData> clientData) {

    }


}


    /*END methods for implementations*/