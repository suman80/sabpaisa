package in.sabpaisa.droid.sabpaisa.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.braunster.chatsdk.activities.ChatSDKLoginActivity;
import com.braunster.chatsdk.activities.ChatSDKMainActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import in.sabpaisa.droid.sabpaisa.AppController;

import in.sabpaisa.droid.sabpaisa.GroupListData;
import in.sabpaisa.droid.sabpaisa.Members;
import in.sabpaisa.droid.sabpaisa.MembersProfile;
import in.sabpaisa.droid.sabpaisa.Model.Member_GetterSetter;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;

/**
 * Created by rajdeeps on 12/28/17.
 */

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MyViewHolder> {
    int count;
    ArrayList<Member_GetterSetter> memberGetterSetterArrayList;
    Context mContext;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    @Override
    public MemberAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_custom, parent, false);
        return new MemberAdapter.MyViewHolder(v);
    }


    public MemberAdapter(ArrayList<Member_GetterSetter> memberGetterSetterArrayList , Context context) {
        this.memberGetterSetterArrayList = memberGetterSetterArrayList;
        this.mContext = context;
    }


    /*START Method to change data when put query in searchBar*/
    public void setItems(ArrayList<Member_GetterSetter> memberDatas) {
        this.memberGetterSetterArrayList = memberDatas;
    }



    @Override
    public void onBindViewHolder(MemberAdapter.MyViewHolder holder, final int position) {

        final Member_GetterSetter member_getterSetter = memberGetterSetterArrayList.get(position);
        holder.memberName.setText(member_getterSetter.getFullName());
        //holder.memberTimeStamp.setText(member_getterSetter.getTimestampOfJoining());
        //holder.memberImg.setImageUrl(member_getterSetter.getUserImageUrl(), imageLoader);
        Glide.with(mContext)
                .load(member_getterSetter.getUserImageUrl())
                .error(R.drawable.default_users)
                .into(holder.memberImg);
        holder.memberImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               /* Intent intent = new Intent(mContext,MembersProfile.class);

                intent.putExtra("",member_getterSetter.getFullName());
                mContext.startActivity(intent);*/

                Log.d("POsitikon-",""+memberGetterSetterArrayList.get(position).getFullName().toString());
                Log.d("POsitikon-",""+memberGetterSetterArrayList.get(position).getUserImageUrl().toString());
                Intent intent = new Intent(mContext,MembersProfile.class);
                intent.putExtra("name1",memberGetterSetterArrayList.get(position).getFullName().toString());
                intent.putExtra("image1",memberGetterSetterArrayList.get(position).getUserImageUrl().toString());
                intent.putExtra("emailid1",memberGetterSetterArrayList.get(position).getEmailId().toString());
                intent.putExtra("mobNo1",memberGetterSetterArrayList.get(position).getPhoneNumber().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                mContext.startActivity(intent);

            }
        });

        holder.memberName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent intent = new Intent(mContext,MembersProfile.class);
                mContext.startActivity(intent);
*/
                Log.d("POsitikon-",""+memberGetterSetterArrayList.get(position).getFullName().toString());
                Log.d("POsitikon-",""+memberGetterSetterArrayList.get(position).getUserImageUrl().toString());
                Intent intent = new Intent(mContext,MembersProfile.class);
                intent.putExtra("name1",memberGetterSetterArrayList.get(position).getFullName().toString());
                intent.putExtra("image1",memberGetterSetterArrayList.get(position).getUserImageUrl().toString());
                intent.putExtra("emailid1",memberGetterSetterArrayList.get(position).getEmailId().toString());
                intent.putExtra("mobNo1",memberGetterSetterArrayList.get(position).getPhoneNumber().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                mContext.startActivity(intent);

            }
        });


        holder.memberChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //21st March,2018
                SharedPreferences sharedPreferences = mContext.getSharedPreferences(FullViewOfClientsProceed.MySharedPrefOnFullViewOfClientProceed, Context.MODE_PRIVATE);
                String clientId=sharedPreferences.getString("clientId","abc");
                String clientName=sharedPreferences.getString("clientName","abc");
                String state=sharedPreferences.getString("state","abc");
                String clientImageURLPath=sharedPreferences.getString("clientImageURLPath","abc");

                int value=3;
                Intent intent = new Intent(mContext,ChatSDKLoginActivity.class);
                intent.putExtra("VALUE",value);
                intent.putExtra("CLIENTID",clientId);
                intent.putExtra("CLIENTNAME",clientName);
                intent.putExtra("STATE",state);
                intent.putExtra("CLIENTIMG",clientImageURLPath);
                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);

                ////21st March,2018
            }
        });

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView memberImg;
        Button memberChat;
        public TextView memberName;
        TextView memberTimeStamp;
        public MyViewHolder(View itemView) {
            super(itemView);


            memberImg = (ImageView) itemView .findViewById(R.id.memberImg);
            memberName = (TextView)itemView.findViewById(R.id.memberName);
            memberChat = (Button) itemView.findViewById(R.id.groupmmbrchat);
            //memberTimeStamp = (TextView)itemView.findViewById(R.id.memberTimeStamp);
        }
    }

    @Override
    public int getItemCount() {
        return memberGetterSetterArrayList.size();
    }
}
