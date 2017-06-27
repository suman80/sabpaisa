package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Model.ChatHistory;
import in.sabpaisa.droid.sabpaisa.Model.ChatModel;
import in.sabpaisa.droid.sabpaisa.R;

/**
 * Created by abc on 21-06-2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    ArrayList<ChatModel> chats;

    public ChatAdapter(Context context, ArrayList<ChatModel> chats) {
        this.context = context;
        this.chats = chats;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case 0:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_left, parent, false);
                return new MyChatViewHolder(v);
            case 1:
                View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_right, parent, false);
                return new MyChatViewHolder(v1);
            case 2:
                View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
                return new MessageViewHolder(v2);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case 2:
                ChatModel chatModel1 = chats.get(position);
                MessageViewHolder messageViewHolder = (MessageViewHolder)holder;
                messageViewHolder.message.setText(chatModel1.getMessage());
                break;
            default:
                ChatModel chatModel = chats.get(position);
                MyChatViewHolder viewHolder0 = (MyChatViewHolder) holder;
                viewHolder0.userMessage.setText(chatModel.getMessage());
                break;
        }

    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatModel chatModel = chats.get(position);
        boolean isMine = chatModel.isMine();
        if(chatModel.getMessageType()==0){
            return 2;
        }
        if (isMine){
            return 0;
        }
        return 1;
    }

    public class MyChatViewHolder extends RecyclerView.ViewHolder {
        LinearLayout background;
        ImageView userImage;
        TextView userMessage;
        public MyChatViewHolder(View itemView) {
            super(itemView);
            background = (LinearLayout)itemView.findViewById(R.id.background);
            userImage = (ImageView)itemView.findViewById(R.id.iv_userImage);
            userMessage = (TextView)itemView.findViewById(R.id.tv_userMessage);
        }
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView message;
        public MessageViewHolder(View itemView) {
            super(itemView);
            message = (TextView)itemView.findViewById(R.id.tv_chatMessage);
        }
    }
}
