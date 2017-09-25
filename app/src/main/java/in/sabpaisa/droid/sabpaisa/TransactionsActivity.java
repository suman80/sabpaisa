package in.sabpaisa.droid.sabpaisa;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import in.sabpaisa.droid.sabpaisa.Adapter.ViewPagerAdapter;
import in.sabpaisa.droid.sabpaisa.Fragments.InstitutionFragment;
import in.sabpaisa.droid.sabpaisa.Fragments.TransactionAllFragment;
import in.sabpaisa.droid.sabpaisa.Fragments.TransactionPendingFragment;

public class TransactionsActivity extends AppCompatActivity {

    Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Payment Transactions");
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TransactionAllFragment(),"Transfer Money");
        adapter.addFragment(new TransactionPendingFragment(),"My Transactions");
        viewPager.setAdapter(adapter);
    }
}
//changes done
