package in.sabpaisa.droid.sabpaisa.Adapter;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

import in.sabpaisa.droid.sabpaisa.GroupListData;
import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.MainGroupAdapter1;
import in.sabpaisa.droid.sabpaisa.Model.GroupDataForOffLine;
import in.sabpaisa.droid.sabpaisa.Proceed_Group_FullScreen;
import in.sabpaisa.droid.sabpaisa.R;

public class ProceedGroupsFragmentsOfflineAdapter extends RecyclerView.Adapter<ProceedGroupsFragmentsOfflineAdapter.MyViewHolder> {
    Context mContext;
    private List<GroupDataForOffLine> dataForOffLineList;
    //public Button joinmember;


    public ProceedGroupsFragmentsOfflineAdapter(List<GroupDataForOffLine> dataForOffLineList, Context context) {
        this.dataForOffLineList = dataForOffLineList;
        this.mContext = context;
    }

    /*START Method to change data when put query in searchBar*/
//    public void setItems(List<GroupListData> groupDatas) {
//        this.countryList = groupDatas;
//    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final GroupDataForOffLine groupDataForOffLine = dataForOffLineList.get(position);
        holder.Group_name.setText(groupDataForOffLine.getGroupName());
        holder.Group_description.setText(groupDataForOffLine.getGroupText());

        holder.Group_Image.setImageResource(R.drawable.offline);
        holder.Group_Logo.setImageResource(R.drawable.offline);

        holder.Group_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

                String token = sharedPreferences.getString("response", "123");

                String groupId = groupDataForOffLine.getGroupId().toString();

                Log.d("tokenGRPOffline", " " + token);
                Log.d("groupIdGRPOffline", " " + groupId);

                if (groupDataForOffLine.getMemberStatus().equals("Approved")) {

                    Intent intent = new Intent(v.getContext(), Proceed_Group_FullScreen.class);
                    intent.putExtra("groupName", groupDataForOffLine.getGroupName());
                    intent.putExtra("groupText", groupDataForOffLine.getGroupText());
                    //intent.putExtra("groupImage",groupDataForOffLine.getImagePath());
                    intent.putExtra("groupId", groupDataForOffLine.getGroupId());
                    v.getContext().startActivity(intent);
                }


            }
        });

        holder.Group_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

                String token = sharedPreferences.getString("response", "123");

                String groupId = groupDataForOffLine.getGroupId().toString();

                Log.d("tokenGRPOffline", " " + token);
                Log.d("groupIdGRPOffline", " " + groupId);

                if (groupDataForOffLine.getMemberStatus().equals("Approved")) {

                    Intent intent = new Intent(v.getContext(), Proceed_Group_FullScreen.class);
                    intent.putExtra("groupName", groupDataForOffLine.getGroupName());
                    intent.putExtra("groupText", groupDataForOffLine.getGroupText());
                    //intent.putExtra("groupImage",groupDataForOffLine.getImagePath());
                    intent.putExtra("groupId", groupDataForOffLine.getGroupId());
                    v.getContext().startActivity(intent);
                }

            }
        });

        holder.Group_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

                String token = sharedPreferences.getString("response", "123");

                String groupId = groupDataForOffLine.getGroupId().toString();

                Log.d("tokenGRPOffline", " " + token);
                Log.d("groupIdGRPOffline", " " + groupId);

                if (groupDataForOffLine.getMemberStatus().equals("Approved")) {

                    Intent intent = new Intent(v.getContext(), Proceed_Group_FullScreen.class);
                    intent.putExtra("groupName", groupDataForOffLine.getGroupName());
                    intent.putExtra("groupText", groupDataForOffLine.getGroupText());
                    //intent.putExtra("groupImage",groupDataForOffLine.getImagePath());
                    intent.putExtra("groupId", groupDataForOffLine.getGroupId());
                    v.getContext().startActivity(intent);
                }

            }
        });
        holder.Group_Logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

                String token = sharedPreferences.getString("response", "123");

                String groupId = groupDataForOffLine.getGroupId().toString();

                Log.d("tokenGRPOffline", " " + token);
                Log.d("groupIdGRPOffline", " " + groupId);

                if (groupDataForOffLine.getMemberStatus().equals("Approved")) {

                    Intent intent = new Intent(v.getContext(), Proceed_Group_FullScreen.class);
                    intent.putExtra("groupName", groupDataForOffLine.getGroupName());
                    intent.putExtra("groupText", groupDataForOffLine.getGroupText());
                    //intent.putExtra("groupImage",groupDataForOffLine.getImagePath());
                    intent.putExtra("groupId", groupDataForOffLine.getGroupId());
                    v.getContext().startActivity(intent);
                }


            }
        });


       /* holder.joinmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

                String token = sharedPreferences.getString("response", "123");

                String groupId = c.getGroupId().toString();

                Log.d("tokenGRP", " " + token);
                Log.d("groupIdGRP", " " + groupId);

                addMember(token, groupId, v, c);
            }
        });
*/



        Log.d("PGFOA"," "+groupDataForOffLine.getMemberStatus());

        holder.joinmember.setText(groupDataForOffLine.getMemberStatus());
        if (groupDataForOffLine.getMemberStatus().equals("Approved"))
            holder.joinmember.setVisibility(View.INVISIBLE);



        if (groupDataForOffLine.getMemberStatus().equals("Blocked")) {
            holder.linearLayoutGroupItemList.setEnabled(false);
            holder.linearLayoutGroupItemList.setAlpha(.5f);
        }


    }
    /*END Method to change data when put query in searchBar*/

    @Override
    public int getItemCount() {
        return dataForOffLineList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_item_list, parent, false);
        return new MyViewHolder(v);
    }

    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView Group_name;
        public TextView Group_description;
        public ImageView Group_Logo;
        public PhotoView Group_Image;
        public Button joinmember;
        public LinearLayout linearLayoutGroupItemList;

        public MyViewHolder(View view) {
            super(view);

            Group_name = (TextView) view.findViewById(R.id.Group_name);
            Group_description = (TextView) view.findViewById(R.id.Group_description);
            joinmember = (Button) view.findViewById(R.id.joinmember);
            Group_Logo = (ImageView) view.findViewById(R.id.Group_Logo);
            Group_Image = (PhotoView) view.findViewById(R.id.Group_Image);
            linearLayoutGroupItemList = (LinearLayout) view.findViewById(R.id.linearLayoutGroupItemList);

        }
    }
}