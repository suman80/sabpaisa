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
import com.balysv.materialripple.MaterialRippleLayout;
//import com.github.chrisbanes.photoview.PhotoView;

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

            final SkipClientData mainFeedData = institutions.get(position);
            holder.instituteName.setText(mainFeedData.getOrganization_name());
            holder.instituteLocation.setText(mainFeedData.getOrgAddress());
            //holder.thumbnail.setImageIcon(Icon.createWithContentUri(mainFeedData.getOrgLogo()));

        if (mainFeedData.getOrgLogo()==null)
        {
            holder.thumbnail.setDefaultImageResId(R.drawable.image_not_found);
        }else {
            holder.thumbnail.setImageUrl(mainFeedData.getOrgLogo(), imageLoader);
        }

        if (mainFeedData.getOrgLogo()==null)
        {
            holder.clinetbanner.setDefaultImageResId(R.drawable.image_not_found);
        }else {
            holder.clinetbanner.setImageUrl(mainFeedData.getOrgWal(),imageLoader);
        }

/*
           holder.clinetbanner.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(v.getContext(), SkipClientDetailsScreen.class);
                   intent.putExtra("clientName", mainFeedData.getOrganization_name());
                   intent.putExtra("state", mainFeedData.getOrgAddress());
                   intent.putExtra("clientLogoPath", mainFeedData.getOrgLogo());
                   intent.putExtra("clientImagePath",mainFeedData.getOrgWal());
                   intent.putExtra("clientId",mainFeedData.getOrganizationId());
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
                    intent.putExtra("clientId",mainFeedData.getOrganizationId());
                    v.getContext().startActivity(intent);
                }
            });*/

        holder.rippleClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SkipClientDetailsScreen.class);

                intent.putExtra("clientName", mainFeedData.getOrganization_name());
                intent.putExtra("state", mainFeedData.getOrgAddress());
                intent.putExtra("clientLogoPath", mainFeedData.getOrgLogo());
                intent.putExtra("clientImagePath",mainFeedData.getOrgWal());
                intent.putExtra("clientId",mainFeedData.getOrganizationId());
                v.getContext().startActivity(intent);
            }
        });

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
        MaterialRippleLayout rippleClick;
        public MyViewHolder(View itemView) {
            super(itemView);


            thumbnail = (NetworkImageView) itemView .findViewById(R.id.thumbnail);
            clinetbanner =(NetworkImageView) itemView.findViewById(R.id.clinetbanner);
            //instituteLogo = (ImageView)itemView.findViewById(R.id.iv_instituteLogo);
            //institutePic = (ImageView)itemView.findViewById(R.id.iv_institutePic);
            //thumbnail =(NetworkImageView)itemView.findViewById(R.id.thumbnail);

            instituteName = (TextView)itemView.findViewById(R.id.tv_instituteName);
            instituteLocation = (TextView)itemView.findViewById(R.id.tv_instituteLocation);
            rippleClick = (MaterialRippleLayout)itemView.findViewById(R.id.rippleClick);

        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_institutions_tab, parent, false);
        return new MyViewHolder(v);
    }

}
