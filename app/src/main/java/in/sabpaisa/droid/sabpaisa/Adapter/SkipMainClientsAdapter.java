package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.github.chrisbanes.photoview.PhotoView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.Model.SkipClientData;
import in.sabpaisa.droid.sabpaisa.Util.SkipClientDetailsScreen;

//import com.elyeproj.loaderviewlibrary.LoaderImageView;
public class SkipMainClientsAdapter extends RecyclerView.Adapter<SkipMainClientsAdapter.MyViewHolder> {
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private ArrayList<SkipClientData> institutions;

    public SkipMainClientsAdapter(ArrayList<SkipClientData> institutions) {
        this.institutions = institutions;
    }

    /*START Method to change data when put query in searchBar*/
    public void setItems(ArrayList<SkipClientData> feedDatas) {
        this.institutions = feedDatas;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
          //final SkipClientData mainFeedData = mainClientDataList.get(position);

        //try {
            final SkipClientData mainFeedData = institutions.get(position);
            holder.instituteName.setText(mainFeedData.getOrganization_name());
            holder.instituteLocation.setText(mainFeedData.getOrgAddress());
            //holder.thumbnail.setImageIcon(Icon.createWithContentUri(mainFeedData.getOrgLogo()));
            holder.thumbnail.setImageUrl(mainFeedData.getOrgLogo(), imageLoader);
            holder.clinetbanner.setImageUrl(mainFeedData.getOrgWal(),imageLoader);

           /* holder.main_feed_group_name.setText(mainFeedData.getFeed_Name());
            holder.main_feed_group_description.setText(mainFeedData.getFeedText());
            holder.main_feed_group_time.setText(getDataFormate(mainFeedData.getFeed_date()));

            byte[] imgData = Base64.decode(mainFeedData.getFeedImage(),Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
            holder.main_feed_image.setImageBitmap(bmp);
            holder.main_feed_image.setVisibility(View.VISIBLE);*/
           /* holder.viewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(v.getContext(), "kjfsnfslk", Toast.LENGTH_SHORT).show();
                    if (holder.viewMore.getText().equals("View More")){
                        holder.viewMore.setText("View Less");
                        holder.main_feed_image.setVisibility(View.VISIBLE);
                    }else {
                        holder.viewMore.setText("View More");
                        holder.main_feed_image.setVisibility(View.GONE);
                    }
                }
            });*/




           holder.clinetbanner.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(v.getContext(), SkipClientDetailsScreen.class);
                   intent.putExtra("clientName", mainFeedData.getOrganization_name());
                   intent.putExtra("state", mainFeedData.getOrgAddress());
                   intent.putExtra("clientLogoPath", mainFeedData.getOrgLogo());
                   intent.putExtra("clientImagePath",mainFeedData.getOrgWal());
                   //intent.putExtra("FeedImage",mainFeedData.getFeedImage());
                   v.getContext().startActivity(intent);
               }
           });

            holder.instituteLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), SkipClientDetailsScreen.class);

                    intent.putExtra("clientName", mainFeedData.getOrganization_name());
                    intent.putExtra("state", mainFeedData.getOrgAddress());
                    intent.putExtra("clientLogoPath", mainFeedData.getOrgLogo());
                    intent.putExtra("clientImagePath",mainFeedData.getOrgWal());
                    //intent.putExtra("FeedImage",mainFeedData.getFeedImage());
                    v.getContext().startActivity(intent);
                }
            });

       /* } catch (ParseException e)
        {
            e.printStackTrace();
        }*/
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
        return institutions .size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView instituteLogo,institutePic;
        NetworkImageView thumbnail,clinetbanner;
        TextView instituteName,instituteLocation;
        public MyViewHolder(View itemView) {
            super(itemView);


            thumbnail = (NetworkImageView) itemView .findViewById(R.id.thumbnail);
            clinetbanner =(NetworkImageView) itemView.findViewById(R.id.clinetbanner);
            //instituteLogo = (ImageView)itemView.findViewById(R.id.iv_instituteLogo);
            //institutePic = (ImageView)itemView.findViewById(R.id.iv_institutePic);
            //thumbnail =(NetworkImageView)itemView.findViewById(R.id.thumbnail);

            instituteName = (TextView)itemView.findViewById(R.id.tv_instituteName);
            instituteLocation = (TextView)itemView.findViewById(R.id.tv_instituteLocation);

        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_institutions_tab, parent, false);
        return new MyViewHolder(v);
    }

    /**
     * View holder class
     */
   /* public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView main_feed_group_description, main_feed_group_name, main_feed_group_time,viewMore;
        NetworkImageView thumbnail,clinetbanner;
        TextView instituteName,instituteLocation;
        public PhotoView main_feed_image;
        public RelativeLayout relativeLayout;
        public LinearLayout linerar1;


        public MyViewHolder(View view) {
            super(view);
            main_feed_group_description = (TextView) view.findViewById(R.id.main_feed_group_description);
            main_feed_group_name = (TextView) view.findViewById(R.id.main_feed_group_name);
            main_feed_group_time = (TextView) view.findViewById(R.id.main_feed_creation_time);
            main_feed_image = (PhotoView) view.findViewById(R.id.iv_feedImage);
           // viewMore = (TextView)view.findViewById(R.id.tv_viewMore);
            relativeLayout = (RelativeLayout)view.findViewById(R.id.rl_view);
            linerar1 =(LinearLayout)view.findViewById(R.id.linerar1);
           // com.elyeproj.loaderviewlibrary.
             //       LoaderImageView.resetLoader();
        }
    }*/
}
