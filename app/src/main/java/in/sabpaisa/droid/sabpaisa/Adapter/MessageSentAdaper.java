package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import in.sabpaisa.droid.sabpaisa.Model.SentMessage;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.Util.SentMessageActivity;

public class MessageSentAdaper extends RecyclerView.Adapter<MessageSentAdaper.MyViewHolder>{

    Context context;
    ArrayList<SentMessage> message;
    public MessageSentAdaper(ArrayList<SentMessage> message, Context context){
        this.message = message ;
        this.context = context;
    }
    @Override
    public MessageSentAdaper.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.messagesent_adapter_layout, parent, false);
        return new MessageSentAdaper.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageSentAdaper.MyViewHolder holder, int position) {
        final SentMessage sentMessage = message.get(position);
        holder.userName.setText(sentMessage.getUserName());
        holder.mobileNumber.setText(sentMessage.getMobileNumber());
        String userImage=sentMessage.getUserImage();

        Glide.with(context)
                .load(userImage)
                .centerCrop()
                .placeholder(R.drawable.default_image)
                .into(holder.userImage);
    }

    @Override
    public int getItemCount() {
        return message.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        CircleImageView userImage;
        TextView mobileNumber;
        public MyViewHolder(View itemView) {
            super(itemView);
            userName = (TextView)itemView.findViewById(R.id.m_username);
            userImage =  itemView.findViewById(R.id.user_image);
            mobileNumber =itemView.findViewById(R.id.m_mobile_number);
        }
    }

}
