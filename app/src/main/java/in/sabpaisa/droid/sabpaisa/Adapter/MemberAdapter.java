package in.sabpaisa.droid.sabpaisa.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.AppController;

import in.sabpaisa.droid.sabpaisa.Model.Member_GetterSetter;
import in.sabpaisa.droid.sabpaisa.R;

/**
 * Created by rajdeeps on 12/28/17.
 */

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MyViewHolder> {
    int count;
    ArrayList<Member_GetterSetter> memberGetterSetterArrayList;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    @Override
    public MemberAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_custom, parent, false);
        return new MemberAdapter.MyViewHolder(v);
    }


    public MemberAdapter(ArrayList<Member_GetterSetter> memberGetterSetterArrayList) {
        this.memberGetterSetterArrayList = memberGetterSetterArrayList;
    }




    @Override
    public void onBindViewHolder(MemberAdapter.MyViewHolder holder, int position) {

        Member_GetterSetter member_getterSetter = memberGetterSetterArrayList.get(position);
        holder.memberName.setText(member_getterSetter.getFullName());
        holder.memberTimeStamp.setText(member_getterSetter.getTimestampOfJoining());
        //holder.memberImg.setImageUrl(member_getterSetter.getUserImageUrl(), imageLoader);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView memberImg;
        TextView memberName,memberTimeStamp;
        public MyViewHolder(View itemView) {
            super(itemView);


            memberImg = (ImageView) itemView .findViewById(R.id.memberImg);
            memberName = (TextView)itemView.findViewById(R.id.memberName);
            memberTimeStamp = (TextView)itemView.findViewById(R.id.memberTimeStamp);
        }
    }

    @Override
    public int getItemCount() {
        return memberGetterSetterArrayList.size();
    }
}