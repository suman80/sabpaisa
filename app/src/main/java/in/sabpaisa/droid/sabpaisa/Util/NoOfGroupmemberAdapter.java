package in.sabpaisa.droid.sabpaisa.Util;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.braunster.chatsdk.activities.ChatSDKMainActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.MemberAdapter;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.Model.Member_GetterSetter;
import in.sabpaisa.droid.sabpaisa.R;

/**
 * Created by archana on 7/3/18.
 */

public class NoOfGroupmemberAdapter extends RecyclerView.Adapter<NoOfGroupmemberAdapter.MyViewHolder> {
        int count;
        ArrayList<Member_GetterSetter> memberGetterSetterArrayList;
        Context mContext;
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
@Override
public NoOfGroupmemberAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_custom, parent, false);
        return new NoOfGroupmemberAdapter.MyViewHolder(v);
        }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        Member_GetterSetter member_getterSetter = memberGetterSetterArrayList.get(i);
        myViewHolder.memberName.setText(member_getterSetter.getFullName());
        //holder.memberTimeStamp.setText(member_getterSetter.getTimestampOfJoining());
        //holder.memberImg.setImageUrl(member_getterSetter.getUserImageUrl(), imageLoader);
        Glide.with(mContext)
                .load(member_getterSetter.getUserImageUrl())
                .error(R.drawable.default_users)
                .into(myViewHolder.memberImg);
        myViewHolder.memberChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent=new Intent(, ChatSDKMainActivity.class);

                v.getContext().startActivity(new Intent(mContext,ChatSDKMainActivity.class));

            }
        });
    }


    public NoOfGroupmemberAdapter(ArrayList<Member_GetterSetter> memberGetterSetterArrayList , Context context) {
        this.memberGetterSetterArrayList = memberGetterSetterArrayList;
        this.mContext = context;
        }


    /*START Method to change data when put query in searchBar*/
public void setItems(ArrayList<Member_GetterSetter> memberDatas) {
        this.memberGetterSetterArrayList = memberDatas;
        }



/*
@Override
public void onBindViewHolder(MemberAdapter.MyViewHolder holder, int position) {

        Member_GetterSetter member_getterSetter = memberGetterSetterArrayList.get(position);
        holder.memberName.setText(member_getterSetter.getFullName());
        //holder.memberTimeStamp.setText(member_getterSetter.getTimestampOfJoining());
        //holder.memberImg.setImageUrl(member_getterSetter.getUserImageUrl(), imageLoader);
        Glide.with(mContext)
        .load(member_getterSetter.getUserImageUrl())
        .error(R.drawable.default_users)
        .into(holder.memberImg);

        }
*/

public class MyViewHolder extends RecyclerView.ViewHolder {
Button memberChat;
    ImageView memberImg;
    TextView memberName,memberTimeStamp;
    public MyViewHolder(View itemView) {
        super(itemView);


        memberImg = (ImageView) itemView .findViewById(R.id.memberImg);
        memberName = (TextView)itemView.findViewById(R.id.memberName);
        memberChat = (Button)itemView.findViewById(R.id.groupmmbrchat);
        //memberTimeStamp = (TextView)itemView.findViewById(R.id.memberTimeStamp);
    }
}

    @Override
    public int getItemCount() {
        return memberGetterSetterArrayList.size();
    }
}

