package in.sabpaisa.droid.sabpaisa;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.sabpaisa.droid.sabpaisa.Adapter.ViewPagerAdapter;

/**
 * Created by SabPaisa on 27-07-2017.
 */

public class FormFragment extends Fragment {

    ViewPager viewPager;
    TabLayout tabs;
    View rootView;

    public FormFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragments_form, container, false);

        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabs = (TabLayout) rootView.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        //adapter.addFragment(new PayFeeFragment(), "Pay Fee");
        //adapter.addFragment(new ApplicationNoFragment(), "Application No.");
        //adapter.addFragment(new BanksFragment(),"Banks");
        viewPager.setAdapter(adapter);
    }

}
