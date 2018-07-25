package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Model.FeedDataForOffLine;
import in.sabpaisa.droid.sabpaisa.Proceed_Feed_FullScreen;
import in.sabpaisa.droid.sabpaisa.R;

public class ProceedFeedsFragmentsOfflineAdapter extends RecyclerView.Adapter<ProceedFeedsFragmentsOfflineAdapter.MyViewHolder> {

    ImageLoader imageLoader;
    Context context;

    ArrayList<FeedDataForOffLine> arrayList;


    public ProceedFeedsFragmentsOfflineAdapter(Context context, ArrayList<FeedDataForOffLine> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    /*START Method to change data when put query in searchBar*//*
public void setItems(ArrayList<FeedData> feedDatas) {
        this.mainFeedDataList = feedDatas;
        }*/

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        final FeedDataForOffLine feedDataForOffLine = arrayList.get(position);
        holder.main_feed_name.setText(feedDataForOffLine.getFeedName());
        /*holder.main_feed_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Proceed_Feed_FullScreen.class);
                    intent.putExtra("feedName", feedDataForOffLine.getFeedName());
                    intent.putExtra("feedText", feedDataForOffLine.getFeedText());
                    intent.putExtra("feedImage", feedDataForOffLine.getImagePath());
                    intent.putExtra("feedLogo", feedDataForOffLine.getLogoPath());
                    intent.putExtra("feedId", feedDataForOffLine.getFeedId());
                    v.getContext().startActivity(intent);
            }
        });*/
        holder.main_feed_description.setText(feedDataForOffLine.getFeedText());
        /*holder.main_feed_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Proceed_Feed_FullScreen.class);
                    intent.putExtra("feedName", feedDataForOffLine.getFeedName());
                    intent.putExtra("feedText", feedDataForOffLine.getFeedText());
                    intent.putExtra("feedImage", feedDataForOffLine.getImagePath());
                    intent.putExtra("feedLogo", feedDataForOffLine.getLogoPath());
                    intent.putExtra("feedId", feedDataForOffLine.getFeedId());
                    v.getContext().startActivity(intent);
            }
        });
        Log.d("client_Image123456", "" + feedDataForOffLine.getImagePath());*/

        //new DownloadImageTask(holder.client_Image).execute(mainFeedData.getImagePath());

//        holder.client_Image.setImageResource(R.drawable.offline);
//        holder.cilent_Logo.setImageResource(R.drawable.offline);


        Glide.with(context)
                .load(feedDataForOffLine.getImagePath())
                .error(R.drawable.offline)
                .into(holder.client_Image);


        Glide.with(context)
                .load(feedDataForOffLine.getLogoPath())
                .error(R.drawable.offline)
                .into(holder.cilent_Logo);




        /*holder.client_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Proceed_Feed_FullScreen.class);
                    intent.putExtra("feedName", feedDataForOffLine.getFeedName());
                    intent.putExtra("feedText", feedDataForOffLine.getFeedText());
                    intent.putExtra("feedImage", feedDataForOffLine.getImagePath());
                    intent.putExtra("feedLogo", feedDataForOffLine.getLogoPath());
                    intent.putExtra("feedId", feedDataForOffLine.getFeedId());
                    v.getContext().startActivity(intent);
            }
        });*/



        holder.linearLayout_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Proceed_Feed_FullScreen.class);
                intent.putExtra("feedName", feedDataForOffLine.getFeedName());
                intent.putExtra("feedText", feedDataForOffLine.getFeedText());
                intent.putExtra("feedImage", feedDataForOffLine.getImagePath());
                intent.putExtra("feedLogo", feedDataForOffLine.getLogoPath());
                intent.putExtra("feedId", feedDataForOffLine.getFeedId());
                view.getContext().startActivity(intent);
            }
        });



    }


//new DownloadLogoTask(holder.cilent_Logo).execute(mainFeedData.getLogoPath());



    /*END Method to change data when put query in searchBar*/


    @Override
    public int getItemCount() {
        return arrayList.size();
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
        public TextView main_feed_description, main_feed_name, main_feed_creation_time;
        ImageView client_Image, cilent_Logo;
        LinearLayout linearLayout_feed;

        public MyViewHolder(View view) {
            super(view);
            main_feed_description = (TextView) view.findViewById(R.id.main_feed_description);
            main_feed_name = (TextView) view.findViewById(R.id.main_feed_name);
            main_feed_creation_time = (TextView) view.findViewById(R.id.main_feed_creation_time);
            client_Image = (ImageView) view.findViewById(R.id.client_Image);
            cilent_Logo = (ImageView) view.findViewById(R.id.client_Logo);
            linearLayout_feed = (LinearLayout)view.findViewById(R.id.linearLayout_feed);
        }
    }



}
