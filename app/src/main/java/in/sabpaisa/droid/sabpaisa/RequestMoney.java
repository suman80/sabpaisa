package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.ContactsAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.SavedUPIAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.ViewPagerAdapter;
import in.sabpaisa.droid.sabpaisa.Fragments.AccountIFSCFragment;
import in.sabpaisa.droid.sabpaisa.Fragments.ContactListFragment;
import in.sabpaisa.droid.sabpaisa.Fragments.SavedUPIFragment;
import in.sabpaisa.droid.sabpaisa.Fragments.TransactionAllFragment;
import in.sabpaisa.droid.sabpaisa.Fragments.TransactionPendingFragment;
import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.Model.ContactList;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;

/**
 * Created by SabPaisa on 21-09-2017.
 */

public class RequestMoney extends AppCompatActivity implements SavedUPIAdapter.UPIResponse,ContactsAdapter.UPIResponse,
        OnFragmentInteractionListener,ContactListFragment.GetDataInterface{

    Toolbar mtoolbar;
    EditText uniqueId;
    TextView idVerify;
    ViewPager viewPager;
    TabLayout tabs;
    LinearLayout parent;
    ArrayList<ContactList> contactLists;
    ArrayList<ContactList> filteredContactList;
    ContactListFragment contactListFragment;
    LinearLayout ll_space;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_request_money);

        mtoolbar = (Toolbar)findViewById(R.id.toolbar);
        uniqueId = (EditText)findViewById(R.id.et_uniqueId);
        idVerify = (TextView)findViewById(R.id.tv_idVerify);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        parent = (LinearLayout)findViewById(R.id.ll_send_money);
        ll_space = (LinearLayout)findViewById(R.id.ll_space);
        setupViewPager(viewPager);
        tabs = (TabLayout)findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        mtoolbar.setTitle("Request Money");
        setSupportActionBar(mtoolbar);


        idVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uniqueId.getText().toString().isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(parent, "Please enter a UPI address", Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.bg_orange));
                    snackbar.show();
                }else {
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(RequestMoney.this);
                    View sheetView = getLayoutInflater().inflate(R.layout.bottomsheet_verifing_upi, null);
                    bottomSheetDialog.setContentView(sheetView);
                    bottomSheetDialog.setCancelable(false);
                    bottomSheetDialog.show();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bottomSheetDialog.cancel();
                            Intent intent = new Intent(RequestMoney.this, BeneficiaryDetail.class);
                            intent.putExtra("UPI", uniqueId.getText().toString());
                            startActivity(intent);
                        }
                    }, 5000);
                }
            }
        });

    }

    private ArrayList<ContactList> filterFeed(ArrayList<ContactList> contactLists, String s) {
        s = s.toLowerCase();

        ArrayList<ContactList> filteredList = new ArrayList<>();
        filteredList.clear();
        for (ContactList item : contactLists) {
            if (item.getNumber().toLowerCase().contains(s) || item.getName().toLowerCase().contains(s)) {
                filteredList.add(item);
            }
        }

        return filteredList;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SavedUPIFragment(),"Saved");

        contactListFragment = new ContactListFragment();
        adapter.addFragment(contactListFragment,"Contacts");
        adapter.addFragment(new AccountIFSCFragment(),"A/c+IFSC");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onUpiSelect(String upi) {
        uniqueId.setText(upi);
    }

    @Override
    public void onFragmentSetFeeds(ArrayList<FeedData> feedData) {

    }

    @Override
    public void onFragmentSetContacts(ArrayList<ContactList> contactLists) {
        this.contactLists = contactLists;
    }

    @Override
    public void onFragmentSetGroups(ArrayList<GroupListData> groupData) {

    }

    @Override
    public ArrayList<ContactList> getFeedDataList() {
        return filteredContactList;
    }
}
