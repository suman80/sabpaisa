package in.sabpaisa.droid.sabpaisa;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainFeedAdapter extends RecyclerView.Adapter<MainFeedAdapter.MyViewHolder> {

    private ArrayList<FeedData> mainFeedDataList;

    public MainFeedAdapter(ArrayList<FeedData> countryList) {
        this.mainFeedDataList = countryList;
    }

    /*START Method to change data when put query in searchBar*/
    public void setItems(ArrayList<FeedData> feedDatas) {
        this.mainFeedDataList = feedDatas;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FeedData mainFeedData = mainFeedDataList.get(position);

        try {
            holder.main_feed_group_name.setText(mainFeedData.getFeed_Name());
            holder.main_feed_group_description.setText(mainFeedData.getFeedText());
            holder.main_feed_group_time.setText(getDataFormate(mainFeedData.getFeed_date()));
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
    }
    /*END Method to change data when put query in searchBar*/

    private String getDataFormate(String dateText) throws ParseException {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM");
        Date date = inputFormat.parse(dateText);
        return outputFormat.format(date);
    }

    @Override
    public int getItemCount() {
        return mainFeedDataList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_feed_list_item, parent, false);
        return new MyViewHolder(v);
    }

    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView main_feed_group_description, main_feed_group_name, main_feed_group_time;

        public MyViewHolder(View view) {
            super(view);
            main_feed_group_description = (TextView) view.findViewById(R.id.main_feed_group_description);
            main_feed_group_name = (TextView) view.findViewById(R.id.main_feed_group_name);
            main_feed_group_time = (TextView) view.findViewById(R.id.main_feed_creation_time);
        }
    }
}
