package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.Model.Institution;
import in.sabpaisa.droid.sabpaisa.Model.ParticularClientModelForOffline;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;

public class ProceedInstitutionFragmentOfflineAdapter extends RecyclerView.Adapter<ProceedInstitutionFragmentOfflineAdapter.MyViewHolder> {
    int count;
    ArrayList<ParticularClientModelForOffline> offlineArrayList;
    Context context;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_institutions_tab, parent, false);
        return new MyViewHolder(v);
    }

    public ProceedInstitutionFragmentOfflineAdapter(Context context,ArrayList<ParticularClientModelForOffline> offlineArrayList) {
        this.offlineArrayList = offlineArrayList;
        this.context = context;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final ParticularClientModelForOffline particularClientModelForOffline = offlineArrayList.get(position);

        holder.instituteName.setText(particularClientModelForOffline.getClientName());
        holder.instituteLocation.setText(particularClientModelForOffline.getState());
        //holder.thumbnail.setImageIcon(Icon.createWithContentUri(mainFeedData.getOrgLogo()));

        /*if (particularClientModelForOffline.getClientLogoPath() == null) {
            holder.thumbnail.setDefaultImageResId(R.drawable.image_not_found);
        } else {
            holder.thumbnail.setImageBitmap(getImage(particularClientModelForOffline.getClientLogoPath()));
            Log.d("Offline_Logo", "adapter-->" + particularClientModelForOffline.getClientLogoPath().toString());
        }

        if (particularClientModelForOffline.getClientImagePath() == null) {
            holder.clinetbanner.setDefaultImageResId(R.drawable.image_not_found);
        } else {
            holder.clinetbanner.setImageBitmap(getImage(particularClientModelForOffline.getClientImagePath()));
            Log.d("Offline_Image", "adapter-->" + particularClientModelForOffline.getClientImagePath().toString());

        }*/

        holder.thumbnail.setDefaultImageResId(R.drawable.offline);
        holder.clinetbanner.setDefaultImageResId(R.drawable.offline);


        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FullViewOfClientsProceed.class);
                intent.putExtra("clientName", particularClientModelForOffline.getClientName());
                intent.putExtra("state", particularClientModelForOffline.getState());
                intent.putExtra("clientLogoPath", particularClientModelForOffline.getClientLogoPath());
                intent.putExtra("clientImagePath", particularClientModelForOffline.getClientImagePath());
                intent.putExtra("clientId", particularClientModelForOffline.getClientId());
//                intent.putExtra("landing_page", mainFeedData.getOrgDesc());

//                Log.d("clientId_thumbnail", "" + mainFeedData.getOrganizationId());
//                Log.d("land", "" + mainFeedData.getOrgDesc());

                v.getContext().startActivity(intent);
            }
        });
        holder.instituteLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FullViewOfClientsProceed.class);
                v.getContext().startActivity(intent);
                intent.putExtra("clientName", particularClientModelForOffline.getClientName());
                intent.putExtra("state", particularClientModelForOffline.getState());
                intent.putExtra("clientLogoPath", particularClientModelForOffline.getClientLogoPath());
                intent.putExtra("clientImagePath", particularClientModelForOffline.getClientImagePath());
                intent.putExtra("clientId", particularClientModelForOffline.getClientId());
//                intent.putExtra("landing_page", mainFeedData.getOrgDesc());
//                Log.d("clientId_location", "" + mainFeedData.getOrganizationId());
            }
        });
        holder.instituteName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FullViewOfClientsProceed.class);
                intent.putExtra("clientName", particularClientModelForOffline.getClientName());
                intent.putExtra("state", particularClientModelForOffline.getState());
                intent.putExtra("clientLogoPath", particularClientModelForOffline.getClientLogoPath());
                intent.putExtra("clientImagePath", particularClientModelForOffline.getClientImagePath());
                intent.putExtra("clientId", particularClientModelForOffline.getClientId());
//                intent.putExtra("landing_page", mainFeedData.getOrgDesc());
//                Log.d("urllll", "" + mainFeedData.getOrgDesc());
                v.getContext().startActivity(intent);
            }
        });
        holder.clinetbanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FullViewOfClientsProceed.class);
                intent.putExtra("clientName", particularClientModelForOffline.getClientName());
                intent.putExtra("state", particularClientModelForOffline.getState());
                intent.putExtra("clientLogoPath", particularClientModelForOffline.getClientLogoPath());
                intent.putExtra("clientImagePath", particularClientModelForOffline.getClientImagePath());
                intent.putExtra("clientId", particularClientModelForOffline.getClientId());
//                intent.putExtra("landing_page", mainFeedData.getOrgDesc());
//                Log.d("urllll", "" + mainFeedData.getOrgDesc());
                v.getContext().startActivity(intent);

            }
        });

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
    public int getItemCount() {
        return offlineArrayList.size();
    }


    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v("AllTransactionSummary", "Internet Connection Not Present");
            return false;
        }
    }


}