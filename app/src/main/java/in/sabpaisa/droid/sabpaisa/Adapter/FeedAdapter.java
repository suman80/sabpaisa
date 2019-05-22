package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
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

import in.sabpaisa.droid.sabpaisa.FeedData;
import in.sabpaisa.droid.sabpaisa.Interfaces.FlagCallback;
import in.sabpaisa.droid.sabpaisa.R;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder> {

    private List<FeedData> feedDataList;
    Context context;
    static FlagCallback flagCallback;
    ArrayList<String> selectedArrayList = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView feedName;
        public ImageView feedImg;
        public CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);
            feedName = (TextView) view.findViewById(R.id.feedName);
            feedImg = (ImageView) view.findViewById(R.id.feedImg);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        }
    }


    public FeedAdapter(List<FeedData> feedDataList, Context context) {
        this.feedDataList = feedDataList;
        this.context = context;
        this.flagCallback = (FlagCallback) context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_feed_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final FeedData feedData = feedDataList.get(position);
        holder.feedName.setText(feedData.getFeedName());
        Glide.with(context)
                .load(feedData.getLogoPath())
                .error(R.drawable.image_not_found)
                .into(holder.feedImg);


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()){
                    Log.d("checkBox.isChecked()"," "+holder.checkBox.isChecked());

                    if (selectedArrayList.size() < 3) {

                        selectedArrayList.add(feedData.getFeedId());

                        for (String val : selectedArrayList) {
                            Log.d("val_selectedArrayList", " " + val);
                        }

                        flagCallback.onSharedFragmentSetFeeds(selectedArrayList);

                    }else {
                        Toast.makeText(context,"You cannot select more than 3 feeds/groups",Toast.LENGTH_SHORT).show();
                        holder.checkBox.setChecked(false);
                    }

                }else {
                    selectedArrayList.remove(feedData.getFeedId());
                }
                if(selectedArrayList.isEmpty()){
                    flagCallback.onSharedFragmentSetFeeds(selectedArrayList);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return feedDataList.size();
    }
}
