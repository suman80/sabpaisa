package in.sabpaisa.droid.sabpaisa;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.SocialPaymentChatAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.ViewPagerAdapter;
import in.sabpaisa.droid.sabpaisa.Fragments.TransactionAllFragment;
import in.sabpaisa.droid.sabpaisa.Fragments.TransactionPendingFragment;
import in.sabpaisa.droid.sabpaisa.Model.ChatHistory;

public class SocialPayment extends AppCompatActivity {
    Toolbar toolbar;
    private RecyclerView recyclerView;
    private SocialPaymentChatAdapter adapter;
    ArrayList<ChatHistory> chatHistories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_payment);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Social Payment");
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView)findViewById(R.id.rv_chatDetails);
        chatHistories = new ArrayList<>();
        LoadChatHistory();
        adapter = new SocialPaymentChatAdapter(this,chatHistories);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),10));
        recyclerView.setLayoutManager(llm);

        recyclerView.setAdapter(adapter);
    }

    private void LoadChatHistory() {
        chatHistories.add(new ChatHistory("Gaurav"));
        chatHistories.add(new ChatHistory("Neeraj"));
        chatHistories.add(new ChatHistory("Mukesh"));
        chatHistories.add(new ChatHistory("Vishal"));
        chatHistories.add(new ChatHistory("Harpreet"));
        chatHistories.add(new ChatHistory("Gaurav1"));
        chatHistories.add(new ChatHistory("Neeraj1"));
        chatHistories.add(new ChatHistory("Mukesh1"));
        chatHistories.add(new ChatHistory("Vishal1"));
        chatHistories.add(new ChatHistory("Harpreet1"));

    }
}
