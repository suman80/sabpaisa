package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import in.sabpaisa.droid.sabpaisa.GroupListData;
import in.sabpaisa.droid.sabpaisa.Interfaces.FlagCallback;
import in.sabpaisa.droid.sabpaisa.R;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> {

    private List<GroupListData> groupListDataList;
    Context context;

    static FlagCallback flagCallback;
    ArrayList<String> selectedArrayList = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView groupName;
        public ImageView groupImg;
        public CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);
            groupName = (TextView) view.findViewById(R.id.groupName);
            groupImg = (ImageView) view.findViewById(R.id.groupImg);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        }
    }


    public GroupAdapter(List<GroupListData> groupListDataList , Context context) {
        this.groupListDataList = groupListDataList;
        this.context = context;
        this.flagCallback = (FlagCallback) context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_group_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final GroupListData groupListData = groupListDataList.get(position);

        Log.d("GA__","GRP_NAME___"+groupListData.getGroupName());
        holder.groupName.setText(groupListData.getGroupName());

        Glide.with(context)
                .load(groupListData.getLogoPath())
                .error(R.drawable.image_not_found)
                .into(holder.groupImg);


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()) {
                    Log.d("checkBox.isChecked()", " " + holder.checkBox.isChecked());

                    if (selectedArrayList.size() < 3) {

                        selectedArrayList.add(groupListData.getGroupId());
                        for (String val : selectedArrayList) {
                            Log.d("val_selectedArrayList", " " + val);
                        }
                        flagCallback.onSharedFragmentSetGroups(selectedArrayList);
                    }else {
                        Toast.makeText(context,"You cannot select more than 3 feeds/groups",Toast.LENGTH_SHORT).show();
                        holder.checkBox.setChecked(false);
                    }


                } else {
                    selectedArrayList.remove(groupListData.getGroupId());
                }

                if (selectedArrayList.isEmpty()) {
                    flagCallback.onSharedFragmentSetGroups(selectedArrayList);
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return groupListDataList.size();
    }
}
