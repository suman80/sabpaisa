package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.List;

import in.sabpaisa.droid.sabpaisa.Adapter.ViewPagerAdapter;
import in.sabpaisa.droid.sabpaisa.Fragments.InstitutionFragment;
import in.sabpaisa.droid.sabpaisa.Fragments.OtherClientFragment;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.Util.CustomSliderView;
import in.sabpaisa.droid.sabpaisa.Util.CustomViewPager;

import static android.view.View.GONE;

public class MainActivitySkip extends AppCompatActivity  implements AppBarLayout.OnOffsetChangedListener, RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

    private SliderLayout mHeaderSlider;
    ArrayList<Integer> headerList = new ArrayList<>();
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    AppBarLayout appBarLayout;
    private CustomViewPager viewPager;
    private TabLayout tabLayout;
    Toolbar toolbar;
    ImageView sendMoney, requestMoney,socialPayment,transaction,profile,bank,mPinInfo,mPinInfo2;
    LinearLayout paymentButton,chatButton,memberButton;
    int isMpinSet=1;
    FloatingActionButton fab;

    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaBtn;
    private RapidFloatingActionHelper rfabHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        sendMoney = (ImageView)findViewById(R.id.ll_send);
        requestMoney = (ImageView)findViewById(R.id.ll_request);
        socialPayment = (ImageView)findViewById(R.id.ll_social_payment);
        transaction = (ImageView)findViewById(R.id.ll_transactions);
        profile = (ImageView)findViewById(R.id.ll_profile);
        bank = (ImageView)findViewById(R.id.ll_bank);
        paymentButton = (LinearLayout)findViewById(R.id.payment_button);
        chatButton = (LinearLayout)findViewById(R.id.chat);
        memberButton = (LinearLayout)findViewById(R.id.members);
        rfaLayout = (RapidFloatingActionLayout)findViewById(R.id.activity_main_rfal);
        rfaBtn = (RapidFloatingActionButton)findViewById(R.id.activity_main_rfab);
        FabButtonCreate();
        //fab = (FloatingActionButton)findViewById(R.id.fab_dashboard);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("HOME");
        mCollapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitleEnabled(false);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        appBarLayout.addOnOffsetChangedListener(this);

        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.disableScroll(true);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setSupportActionBar(toolbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitleEnabled(false);

        mHeaderSlider = (SliderLayout)findViewById(R.id.slider);

        LoadHeaderImageList();

        setHeaderImageList();

        sendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMpinSet==0) {             /*TODO check if mpin is set or not, for now i am hardcoding it*/
                    Intent intent = new Intent(MainActivitySkip.this, AccountInfoActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
                }else {
                    Intent intent = new Intent(MainActivitySkip.this, SendMoneyActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
                }
            }
        });

        requestMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivitySkip.this, RequestMoney.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        });

        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivitySkip.this, AccountInfoActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        });
        transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivitySkip.this, TransactionsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        });
        socialPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivitySkip.this,SocialPayment.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        });
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivitySkip.this,SocialPayment.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        });
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

    private void FabButtonCreate() {
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
//        rfaContent.setOnRapidFloatingActionContentListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        Bitmap send = BitmapFactory.decodeResource(getResources(),
                R.drawable.send_icon);
        items.add(new RFACLabelItem<Integer>()
                .setResId(R.mipmap.ic_transactions_icon)
                .setIconNormalColor(getResources().getColor(R.color.bg_orange))
                .setIconPressedColor(getResources().getColor(R.color.bg_orange))
                .setWrapper(1)
        );
        items.add(new RFACLabelItem<Integer>()
                .setResId(R.mipmap.ic_request_icon)
                .setIconNormalColor(getResources().getColor(R.color.bg_orange))
                .setIconPressedColor(getResources().getColor(R.color.bg_orange))
                .setWrapper(2)
        );
        items.add(new RFACLabelItem<Integer>()
                .setResId(R.mipmap.ic_send_icon)
                .setIconNormalColor(getResources().getColor(R.color.bg_orange))
                .setIconPressedColor(getResources().getColor(R.color.bg_orange))
                .setWrapper(3)
        );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(ABTextUtil.dip2px(this, 5))
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(ABTextUtil.dip2px(this, 5))
        ;
        rfabHelper = new RapidFloatingActionHelper(
                this,
                rfaLayout,
                rfaBtn,
                rfaContent
        ).build();
    }
    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        Toast.makeText(this, "clicked label: " + position, Toast.LENGTH_SHORT).show();
        rfabHelper.toggleContent();
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        if (position==2){
            Toast.makeText(this, "Send Clicked", Toast.LENGTH_SHORT).show();
            if (isMpinSet==0) {             /*TODO check if mpin is set or not, for now i am hardcoding it*/
                Intent intent = new Intent(MainActivitySkip.this, AccountInfoActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }else {
                Intent intent = new Intent(MainActivitySkip.this, SendMoneyActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        }else if (position==1){
            Toast.makeText(this, "Request Clicked", Toast.LENGTH_SHORT).show();
        }else if (position==0){
            Toast.makeText(this, "Transactions Clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivitySkip.this, TransactionsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
        }
        rfabHelper.toggleContent();
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new InstitutionFragment(),"Clients");
        adapter.addFragment(new OtherClientFragment(),"Other Clients");
        //adapter.addFragment(new FormFragment(),"Forms");
        //adapter.addFragment(new InstitutionFragment(),"Groups");
        viewPager.setAdapter(adapter);
    }

    private void setHeaderImageList() {
        for(int i=0;i<headerList.size();i++){
            CustomSliderView customSliderView = new CustomSliderView(this);
            // initialize a SliderLayout
            customSliderView
                    .image(headerList.get(i))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            //TODO URL
                        }
                    });
            mHeaderSlider.addSlider(customSliderView);
        }
        mHeaderSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mHeaderSlider.setBackgroundColor(getResources().getColor(R.color.main_screen_bottom_color));
        mHeaderSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mHeaderSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
//        mHeaderSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
    }

    private void LoadHeaderImageList() {



        headerList.add(R.drawable.test_header600240);
        headerList.add(R.drawable.test_header600241);
        headerList.add(R.drawable.test_header600242);
        headerList.add(R.drawable.test_header600243);
    }


        /*url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");






        for(String name :  url_maps.keySet()){
        TextSliderView textSliderView = new TextSliderView(this);
        // initialize a SliderLayout
        textSliderView
                .description(name)
                .image(file_maps.get(name))
                .setScaleType(BaseSliderView.ScaleType.Fit)
                .setOnSliderClickListener(this);

        //add your extra information
        textSliderView.bundle(new Bundle());
        textSliderView.getBundle()
                .putString("extra",name);

        mDemoSlider.addSlider(textSliderView);
    }

*/

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0)
        {
            rfaLayout.setVisibility(GONE);
            //fab.setVisibility(View.GONE);// Collapsed
        }
        else
        {
            rfaLayout.setVisibility(View.VISIBLE);
            //fab.setVisibility(View.VISIBLE);// Not collapsed
        }
    }







    }


