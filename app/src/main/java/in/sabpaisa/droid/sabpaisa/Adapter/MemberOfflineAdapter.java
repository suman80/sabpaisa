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
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.MembersProfile;
import in.sabpaisa.droid.sabpaisa.Model.MemberOfflineDataModel;
import in.sabpaisa.droid.sabpaisa.Model.Member_GetterSetter;
import in.sabpaisa.droid.sabpaisa.Proceed_Feed_FullScreen;
import in.sabpaisa.droid.sabpaisa.R;


public class MemberOfflineAdapter extends RecyclerView.Adapter<MemberOfflineAdapter.MyViewHolder> {
    int count;
    ArrayList<MemberOfflineDataModel> memberGetterSetterArrayList;
    Context mContext;
    String x, name, mobNumber;
    String useracesstoken;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_custom, parent, false);
        return new MyViewHolder(v);
    }


    public MemberOfflineAdapter(ArrayList<MemberOfflineDataModel> memberGetterSetterArrayList, Context context) {
        this.memberGetterSetterArrayList = memberGetterSetterArrayList;
        this.mContext = context;
    }


    /*START Method to change data when put query in searchBar*/
    public void setItems(ArrayList<MemberOfflineDataModel> memberDatas) {
        this.memberGetterSetterArrayList = memberDatas;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final MemberOfflineDataModel memberOfflineDataModel = memberGetterSetterArrayList.get(position);
        holder.memberName.setText(memberOfflineDataModel.getFullName());
        //holder.memberTimeStamp.setText(member_getterSetter.getTimestampOfJoining());
        //holder.memberImg.setImageUrl(member_getterSetter.getUserImageUrl(), imageLoader);
        Glide.with(mContext)
                .load(memberOfflineDataModel.getUserImageUrl())
                .error(R.drawable.default_users)
                .into(holder.memberImg);
        holder.memberImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Log.d("POsitikon-", "" + memberGetterSetterArrayList.get(position).getFullName().toString());
                Log.d("POsitikon-", "" + memberGetterSetterArrayList.get(position).getUserImageUrl().toString());
                Intent intent = new Intent(mContext, MembersProfile.class);
                intent.putExtra("name1", memberGetterSetterArrayList.get(position).getFullName().toString());
                intent.putExtra("image1", memberGetterSetterArrayList.get(position).getUserImageUrl().toString());
                intent.putExtra("emailid1", memberGetterSetterArrayList.get(position).getEmailId().toString());
                intent.putExtra("mobNo1", memberGetterSetterArrayList.get(position).getPhoneNumber().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                mContext.startActivity(intent);
*/
            }
        });

        holder.memberName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Log.d("POsitikon-", "" + memberGetterSetterArrayList.get(position).getFullName().toString());
                Log.d("POsitikon-", "" + memberGetterSetterArrayList.get(position).getUserImageUrl().toString());
                Intent intent = new Intent(mContext, MembersProfile.class);
                intent.putExtra("name1", memberGetterSetterArrayList.get(position).getFullName().toString());
                intent.putExtra("image1", memberGetterSetterArrayList.get(position).getUserImageUrl().toString());
                intent.putExtra("emailid1", memberGetterSetterArrayList.get(position).getEmailId().toString());
                intent.putExtra("mobNo1", memberGetterSetterArrayList.get(position).getPhoneNumber().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                mContext.startActivity(intent);*/

            }
        });

        /*holder.memberChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"No Internet Connection !",Toast.LENGTH_SHORT).show();
            }
        });*/





    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView memberImg;
        Button memberChat;
        public TextView memberName;
        TextView memberTimeStamp;
        String response, userAccessToken;

        public MyViewHolder(View itemView) {
            super(itemView);
            memberImg = (ImageView) itemView.findViewById(R.id.memberImg);
            memberName = (TextView) itemView.findViewById(R.id.memberName);
            //memberChat = (Button) itemView.findViewById(R.id.groupmmbrchat);


        }
    }

    @Override
    public int getItemCount() {
        return memberGetterSetterArrayList.size();
    }
}