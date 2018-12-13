package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.Model.PrivateMemberListModel;
import in.sabpaisa.droid.sabpaisa.NumberOfGroups;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.UIN;

public class PrivateFeedMemberListAdapter extends RecyclerView.Adapter<PrivateFeedMemberListAdapter.MyViewHolder> {

    private List<PrivateMemberListModel> arrayList;
    Context context;
    String userAccessToken;

    public PrivateFeedMemberListAdapter(List<PrivateMemberListModel> arrayList , Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView memberImg;
        TextView memberName,textViewAdmin;
        ImageView imgPopUpMenu;

        public MyViewHolder(View view) {
            super(view);
            memberImg = (ImageView) itemView.findViewById(R.id.memberImg);
            memberName = (TextView) itemView.findViewById(R.id.memberName);
            imgPopUpMenu = (ImageView) itemView.findViewById(R.id.imgPopUpMenu);
            textViewAdmin = (TextView)itemView.findViewById(R.id.textViewAdmin);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.member_custom, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        final PrivateMemberListModel privateMemberListModel = arrayList.get(position);

        SharedPreferences sharedPreferencesRole = context.getSharedPreferences(UIN.SHARED_PREF_FOR_CHECK_USER, Context.MODE_PRIVATE);

        final String roleValue = sharedPreferencesRole.getString("USER_ROLE", "abc");

        Glide.with(context)
                .load(privateMemberListModel.getUserImageUrl())
                .error(R.drawable.default_users)
                .into(holder.memberImg);

        holder.memberName.setText(privateMemberListModel.getFullName());


        if (roleValue.equals("1")) {

            holder.imgPopUpMenu.setVisibility(View.VISIBLE);
        }

        SharedPreferences sharedPreferences1 = context.getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        userAccessToken = sharedPreferences1.getString("response", "123");

        if (userAccessToken.equals(privateMemberListModel.getUserAccessToken())){
            holder.memberName.setText("You");

        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}
