package in.sabpaisa.droid.sabpaisa.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
