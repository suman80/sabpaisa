package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Adapter;
import android.widget.Toast;

import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.List;

import in.sabpaisa.droid.sabpaisa.Adapter.ChatAdapter;
import in.sabpaisa.droid.sabpaisa.Model.ChatModel;

public class ChatDetail extends AppCompatActivity implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

    RecyclerView recyclerView;
    ArrayList<ChatModel> chatModelArrayList;
    ChatAdapter adapter;
    Toolbar toolbar;

    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaBtn;
    private RapidFloatingActionHelper rfabHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        //rfaLayout = (RapidFloatingActionLayout)findViewById(R.id.activity_main_rfal);
        rfaBtn = (RapidFloatingActionButton)findViewById(R.id.activity_main_rfab);
        FabButtonCreate();

        recyclerView = (RecyclerView)findViewById(R.id.rv_chat);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Chat");
        setSupportActionBar(toolbar);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),10));
        recyclerView.setLayoutManager(llm);
        chatModelArrayList = new ArrayList<>();
        LoadChatData();
        adapter = new ChatAdapter(this,chatModelArrayList);
        recyclerView.setAdapter(adapter);

    }

    private void FabButtonCreate() {
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
//        rfaContent.setOnRapidFloatingActionContentListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setResId(R.mipmap.ic_pay_white)
                .setLabel("Through VPA")
                .setIconNormalColor(getResources().getColor(R.color.bg_orange))
                .setIconPressedColor(getResources().getColor(R.color.bg_orange))
                .setWrapper(1)
        );
        items.add(new RFACLabelItem<Integer>()
                .setResId(R.mipmap.ic_pay_white)
                .setLabel("Through Account+IFSC")
                .setIconNormalColor(getResources().getColor(R.color.bg_orange))
                .setIconPressedColor(getResources().getColor(R.color.bg_orange))
                .setWrapper(2)
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
        if (position==0){
            Intent intent = new Intent(ChatDetail.this,SendMoneyDialog.class);
            intent.putExtra("Dialog",true);
            startActivityForResult(intent,100);
        }else if (position==1){
            Intent intent = new Intent(ChatDetail.this,SendMoneyAcIFSCDialog.class);
            startActivityForResult(intent,100);
        }
        rfabHelper.toggleContent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if(resultCode == 200) {
                chatModelArrayList.add(new ChatModel("Money Send Successfully",true,0));
                adapter.notifyItemInserted(chatModelArrayList.size() - 1);
                recyclerView.scrollToPosition(chatModelArrayList.size() - 1);
            }
        }
    }

    private void LoadChatData() {
        chatModelArrayList.add(new ChatModel("Hey",false,1));
        chatModelArrayList.add(new ChatModel("Hi",true,1));
        chatModelArrayList.add(new ChatModel("Send me money for coffee",false,1));
        chatModelArrayList.add(new ChatModel("Ok, will give u when we meet",true,1));
        chatModelArrayList.add(new ChatModel("Send it from the app",false,1));
        chatModelArrayList.add(new ChatModel("Now you can send it from the app itself",false,1));
        chatModelArrayList.add(new ChatModel("Wow, great",true,1));
        chatModelArrayList.add(new ChatModel("How you want your money? VPA?Bank Account?",true,1));
        chatModelArrayList.add(new ChatModel("My VPA is 0000000000@sabpaisa",false,1));
        chatModelArrayList.add(new ChatModel("Ok, will send you",true,1));
    }

}
