package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.ChatDetail;
import in.sabpaisa.droid.sabpaisa.Model.ChatHistory;
import in.sabpaisa.droid.sabpaisa.Model.TransactionDetail;
import in.sabpaisa.droid.sabpaisa.R;

/**
 * Created by abc on 20-06-2017.
 */

public class SocialPaymentChatAdapter extends RecyclerView.Adapter<SocialPaymentChatAdapter.MyViewHolder> {
    Context context;
    ArrayList<ChatHistory> chatHistories;

    public SocialPaymentChatAdapter(Context context, ArrayList<ChatHistory> chatHistories) {
        this.context = context;
        this.chatHistories = chatHistories;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_social_payment, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ChatHistory chatHistory = chatHistories.get(position);
        holder.userName.setText(chatHistory.getUserName());

        if (position%2!=0) {
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.main_screen_bottom_color));
        }
        else{
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        holder.background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatDetail.class);
                context.startActivity(intent);
//                context.overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatHistories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userName;
        RelativeLayout background;
        public MyViewHolder(View itemView) {
            super(itemView);
            background = (RelativeLayout)itemView.findViewById(R.id.rv_background);
            userImage = (ImageView)itemView.findViewById(R.id.iv_personImage);
            userName = (TextView)itemView.findViewById(R.id.tv_userName);
        }
    }
}
