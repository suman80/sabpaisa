package in.sabpaisa.droid.sabpaisa.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import in.sabpaisa.droid.sabpaisa.Fragments.TestFragment;

/**
 * Created by abc on 19-06-2017.
 */

public class TestAdapter extends FragmentPagerAdapter{


    public TestAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return TestFragment.newInstance(position + 1);
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "SECTION"+(position+1);
    }
}
