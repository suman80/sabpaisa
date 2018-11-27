package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class SharedGroupFragmentAdapter extends RecyclerView.Adapter<SharedGroupFragmentAdapter.MyViewHolder> {
    Context mContext;
    private List<GroupListData> countryList;
    public Button joinmember;
    ArrayList<String> selectedArrayList = new ArrayList<>();

    String popup = "Group";

    static FlagCallback flagCallback;

    public SharedGroupFragmentAdapter(List<GroupListData> countryList, Context context) {
        this.countryList = countryList;
        this.mContext = context;
        this.flagCallback = (FlagCallback) context;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final GroupListData c = countryList.get(position);
        holder.groupName.setText(c.getGroupName());

        Glide.with(mContext)
                .load(c.getLogoPath())
                .error(R.drawable.image_not_found)
                .into(holder.groupLogo);


        if (c.getMemberStatus().equals("Approved")) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()) {
                    Log.d("checkBox.isChecked()", " " + holder.checkBox.isChecked());

                    if (selectedArrayList.size() < 3) {

                        selectedArrayList.add(c.getGroupId());
                        for (String val : selectedArrayList) {
                            Log.d("val_selectedArrayList", " " + val);
                        }
                        flagCallback.onSharedFragmentSetGroups(selectedArrayList);
                    }else {
                        Toast.makeText(mContext,"You cannot select more than 3 feeds/groups",Toast.LENGTH_SHORT).show();
                        holder.checkBox.setChecked(false);
                    }


                } else {
                    selectedArrayList.remove(c.getGroupId());
                }

                if (selectedArrayList.isEmpty()) {
                    flagCallback.onSharedFragmentSetGroups(selectedArrayList);
                }


            }
        });


    }
    /*END Method to change data when put query in searchBar*/

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_shared_group_layout, parent, false);
        return new MyViewHolder(v);
    }

    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView groupName;

        public ImageView groupLogo;
        public CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);

            groupName = (TextView) view.findViewById(R.id.groupName);
            groupLogo = (ImageView) view.findViewById(R.id.groupLogo);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);

        }
    }
}