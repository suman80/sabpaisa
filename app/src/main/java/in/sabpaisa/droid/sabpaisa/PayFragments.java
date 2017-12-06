package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import in.sabpaisa.droid.sabpaisa.Adapter.ViewPagerAdapter;

/**
 * Created by SabPaisa on 03-07-2017.
 */

public class PayFragments extends Fragment {

    ViewPager viewPager;
    TabLayout tabs;
    View rootView;

    public PayFragments() {
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
        rootView = inflater.inflate(R.layout.fragments_pay, container, false);
        /*viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);*/
        Button btn_pay = (Button) rootView.findViewById(R.id.btn_pay);

        tabs = (TabLayout) rootView.findViewById(R.id.tabs);
//        tabs.setupWithViewPager(viewPager);
        return rootView;
    }
//    Button btn_pay = (Button) rootView.findViewById(R.id.btn_pay);





    //utton13 = (Button) view.findViewById(R.id.button13);


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn_pay = (Button) rootView.findViewById(R.id.btn_pay);

        btn_pay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent i=new Intent(getContext(),WebViewActivity.class);

                /*Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://43.252.89.223:9191/QwikCollect/Canarabank.jsp"));
               */ startActivity(i);

                // do something
            }
        });

    }

 /*   private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
       // adapter.addFragment(new FormFragments(), "Form Name");
        //adapter.addFragment(new PaymentHistoryFragment(), "Payment History");
        viewPager.setAdapter(adapter);
    }*/

}
