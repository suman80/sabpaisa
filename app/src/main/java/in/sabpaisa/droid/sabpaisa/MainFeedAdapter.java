package in.sabpaisa.droid.sabpaisa;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.sabpaisa.droid.sabpaisa.Model.FeedDataForOffLine;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;



public class MainFeedAdapter extends RecyclerView.Adapter<MainFeedAdapter.MyViewHolder> {

    private ArrayList<FeedData> mainFeedDataList;
    ImageLoader imageLoader;
    Context context;
    String popup="Feeds";


    public MainFeedAdapter(ArrayList<FeedData> countryList, Context context) {
        this.mainFeedDataList = countryList;
        this.context = context;
    }


    /*START Method to change data when put query in searchBar*/
    public void setItems(ArrayList<FeedData> feedDatas) {
        this.mainFeedDataList = feedDatas;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final FeedData mainFeedData = mainFeedDataList.get(position);

        Glide.with(context)
                .load(mainFeedData.getLogoPath())
                .error(R.drawable.image_not_found)
                .into(holder.cilent_Logo);

        Glide.with(context)
                .load(mainFeedData.getImagePath())
                .error(R.drawable.image_not_found)
                .into(holder.client_Image);



        holder.main_feed_name.setText(mainFeedData.getFeedName());
        /*holder.main_feed_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Proceed_Feed_FullScreen.class);
                intent.putExtra("feedName", mainFeedData.getFeedName());
                intent.putExtra("feedText", mainFeedData.getFeedText());
                intent.putExtra("popup",popup);
                intent.putExtra("feedImage", mainFeedData.getImagePath());
                intent.putExtra("feedLogo", mainFeedData.getLogoPath());
                intent.putExtra("feedId", mainFeedData.getFeedId());
                v.getContext().startActivity(intent);
            }
        });*/
        holder.main_feed_description.setText(mainFeedData.getFeedText());
        /*holder.main_feed_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Proceed_Feed_FullScreen.class);
                intent.putExtra("feedName", mainFeedData.getFeedName());
                intent.putExtra("popup",popup);

                intent.putExtra("feedText", mainFeedData.getFeedText());
                intent.putExtra("feedImage", mainFeedData.getImagePath());
                intent.putExtra("feedLogo", mainFeedData.getLogoPath());
                intent.putExtra("feedId", mainFeedData.getFeedId());
                v.getContext().startActivity(intent);
            }
        });
        Log.d("client_Image123456", "" + mainFeedData.getImagePath());*/


        /*holder.client_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Proceed_Feed_FullScreen.class);
                intent.putExtra("feedName", mainFeedData.getFeedName());
                intent.putExtra("popup",popup);

                intent.putExtra("feedText", mainFeedData.getFeedText());
                intent.putExtra("feedImage", mainFeedData.getImagePath());
                intent.putExtra("feedLogo", mainFeedData.getLogoPath());
                intent.putExtra("feedId", mainFeedData.getFeedId());
                v.getContext().startActivity(intent);
            }
        });*/

        /*holder.linearLayout_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(view.getContext(), Proceed_Feed_FullScreen.class);
                intent.putExtra("feedName", mainFeedData.getFeedName());
                intent.putExtra("popup",popup);

                intent.putExtra("feedText", mainFeedData.getFeedText());
                intent.putExtra("feedImage", mainFeedData.getImagePath());
                intent.putExtra("feedLogo", mainFeedData.getLogoPath());
                intent.putExtra("feedId", mainFeedData.getFeedId());
                view.getContext().startActivity(intent);
            }
        });*/

        holder.rippleClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(view.getContext(), Proceed_Feed_FullScreen.class);
                intent.putExtra("feedName", mainFeedData.getFeedName());
                intent.putExtra("popup",popup);

                intent.putExtra("feedText", mainFeedData.getFeedText());
                intent.putExtra("feedImage", mainFeedData.getImagePath());
                intent.putExtra("feedLogo", mainFeedData.getLogoPath());
                intent.putExtra("feedId", mainFeedData.getFeedId());
                view.getContext().startActivity(intent);
            }
        });




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
        public TextView main_feed_description, main_feed_name, main_feed_creation_time;
        ImageView client_Image, cilent_Logo;
        LinearLayout linearLayout_feed;
        MaterialRippleLayout rippleClick;

        public MyViewHolder(View view) {
            super(view);
            main_feed_description = (TextView) view.findViewById(R.id.main_feed_description);
            main_feed_name = (TextView) view.findViewById(R.id.main_feed_name);
            main_feed_creation_time = (TextView) view.findViewById(R.id.main_feed_creation_time);
            client_Image = (ImageView) view.findViewById(R.id.client_Image);
            cilent_Logo = (ImageView) view.findViewById(R.id.client_Logo);
            linearLayout_feed = (LinearLayout)view.findViewById(R.id.linearLayout_feed);
            rippleClick = (MaterialRippleLayout)view.findViewById(R.id.rippleClick);
        }
    }


}
