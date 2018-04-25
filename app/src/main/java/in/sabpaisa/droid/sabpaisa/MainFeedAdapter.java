package in.sabpaisa.droid.sabpaisa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;


public class MainFeedAdapter extends RecyclerView.Adapter<MainFeedAdapter.MyViewHolder> {

    private ArrayList<FeedData> mainFeedDataList;
    ImageLoader imageLoader;
    Context context;

    public MainFeedAdapter(ArrayList<FeedData> countryList,Context context) {
        this.mainFeedDataList = countryList;
        this.context=context;
    }

    /*START Method to change data when put query in searchBar*/
    public void setItems(ArrayList<FeedData> feedDatas) {
        this.mainFeedDataList = feedDatas;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final FeedData mainFeedData = mainFeedDataList.get(position);
        holder.main_feed_name.setText(mainFeedData.getFeedName());
        holder.main_feed_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Proceed_Feed_FullScreen.class);
                intent.putExtra("feedName",mainFeedData.getFeedName());
                intent.putExtra("feedText",mainFeedData.getFeedText());
                intent.putExtra("feedImage",mainFeedData.getImagePath());
                intent.putExtra("feedLogo",mainFeedData.getLogoPath());
                intent.putExtra("feedId",mainFeedData.getFeedId());
                v.getContext().startActivity(intent);
            }
        });
        holder.main_feed_description.setText(mainFeedData.getFeedText());
        holder.main_feed_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Proceed_Feed_FullScreen.class);
                intent.putExtra("feedName",mainFeedData.getFeedName());
                intent.putExtra("feedText",mainFeedData.getFeedText());
                intent.putExtra("feedImage",mainFeedData.getImagePath());
                intent.putExtra("feedLogo",mainFeedData.getLogoPath());
                intent.putExtra("feedId",mainFeedData.getFeedId());
                v.getContext().startActivity(intent);
            }
        });
        Log.d("client_Image123456",""+mainFeedData.getImagePath());

        //new DownloadImageTask(holder.client_Image).execute(mainFeedData.getImagePath());

        Glide.with(context)
                .load(mainFeedData.getImagePath())
                .error(R.drawable.image_not_found)
                .into(holder.client_Image);


        holder.client_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Proceed_Feed_FullScreen.class);
                intent.putExtra("feedName",mainFeedData.getFeedName());
                intent.putExtra("feedText",mainFeedData.getFeedText());
                intent.putExtra("feedImage",mainFeedData.getImagePath());
                intent.putExtra("feedLogo",mainFeedData.getLogoPath());
                intent.putExtra("feedId",mainFeedData.getFeedId());
                v.getContext().startActivity(intent);
            }
        });


        Glide.with(context)
                .load(mainFeedData.getLogoPath())
                .error(R.drawable.image_not_found)
                .into(holder.cilent_Logo);



        //new DownloadLogoTask(holder.cilent_Logo).execute(mainFeedData.getLogoPath());
    }
    /*END Method to change data when put query in searchBar*/

    /*private String getDataFormate(String dateText) throws ParseException {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM");
        Date date = inputFormat.parse(dateText);
        return outputFormat.format(date);
    }*/

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
                ImageView client_Image,cilent_Logo;

        public MyViewHolder(View view) {
            super(view);
            main_feed_description = (TextView) view.findViewById(R.id.main_feed_description);
            main_feed_name = (TextView) view.findViewById(R.id.main_feed_name);
            main_feed_creation_time = (TextView) view.findViewById(R.id.main_feed_creation_time);
            client_Image = (ImageView) view.findViewById(R.id.client_Image);
            cilent_Logo = (ImageView) view.findViewById(R.id.client_Logo);
        }
    }


    //Code for fetching image from server
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loading.show();
        }

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            //loading.dismiss();
        }

    }

    //Code for fetching image from server
    private class DownloadLogoTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loading.show();
        }

        public DownloadLogoTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            //loading.dismiss();
        }

    }


}
