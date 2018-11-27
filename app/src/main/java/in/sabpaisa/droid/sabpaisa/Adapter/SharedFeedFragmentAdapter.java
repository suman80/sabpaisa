package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.FeedData;
import in.sabpaisa.droid.sabpaisa.GroupListData;
import in.sabpaisa.droid.sabpaisa.Interfaces.FlagCallback;
import in.sabpaisa.droid.sabpaisa.MainFeedAdapter;
import in.sabpaisa.droid.sabpaisa.Proceed_Feed_FullScreen;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.SharingActivity;

public class SharedFeedFragmentAdapter extends RecyclerView.Adapter<SharedFeedFragmentAdapter.MyViewHolder> {

    private ArrayList<FeedData> mainFeedDataList;
    ImageLoader imageLoader;
    Context context;
    String popup="Feeds";
    static FlagCallback flagCallback;

    ArrayList<String> selectedArrayList = new ArrayList<>();

    public SharedFeedFragmentAdapter(ArrayList<FeedData> countryList, Context context) {
        this.mainFeedDataList = countryList;
        this.context = context;
        this.flagCallback = (FlagCallback) context;
    }

    public SharedFeedFragmentAdapter() {
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final FeedData mainFeedData = mainFeedDataList.get(position);

        Glide.with(context)
                .load(mainFeedData.getLogoPath())
                .error(R.drawable.image_not_found)
                .into(holder.feedLogo);

        holder.feedName.setText(mainFeedData.getFeedName());

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()){
                    Log.d("checkBox.isChecked()"," "+holder.checkBox.isChecked());

                    if (selectedArrayList.size() < 3) {

                        selectedArrayList.add(mainFeedData.getFeedId());

                        for (String val : selectedArrayList) {
                            Log.d("val_selectedArrayList", " " + val);
                        }

                        flagCallback.onSharedFragmentSetFeeds(selectedArrayList);

                    }else {
                        Toast.makeText(context,"You cannot select more than 3 feeds/groups",Toast.LENGTH_SHORT).show();
                        holder.checkBox.setChecked(false);
                    }

                }else {
                    selectedArrayList.remove(mainFeedData.getFeedId());
                }
                if(selectedArrayList.isEmpty()){
                    flagCallback.onSharedFragmentSetFeeds(selectedArrayList);
                }
            }
        });



    }


    @Override
    public int getItemCount() {
        return mainFeedDataList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_shared_feed_layout, parent, false);
        return new MyViewHolder(v);
    }

    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  feedName;
        ImageView  feedLogo;
        CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);

            feedName = (TextView) view.findViewById(R.id.feedName);
            feedLogo = (ImageView) view.findViewById(R.id.feedLogo);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);

        }
    }


}
