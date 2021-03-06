package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.AppController;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.proceed_inst_offline_adapter, parent, false);
        return new MyViewHolder(v);
    }

    public ProceedInstitutionFragmentOfflineAdapter(Context context,ArrayList<ParticularClientModelForOffline> offlineArrayList) {
        this.offlineArrayList = offlineArrayList;
        this.context = context;
    }

    public ProceedInstitutionFragmentOfflineAdapter() {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final ParticularClientModelForOffline particularClientModelForOffline = offlineArrayList.get(position);

        holder.instituteName.setText(particularClientModelForOffline.getClientName());
        holder.instituteLocation.setText(particularClientModelForOffline.getState());

        Glide.with(context)
                .load(particularClientModelForOffline.getClientLogoPath())
                .error(R.drawable.offline)
                .into(holder.thumbnail);


        Glide.with(context)
                .load(particularClientModelForOffline.getClientImagePath())
                .error(R.drawable.offline)
                .into(holder.clinetbanner);


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
        ImageView thumbnail,clinetbanner;
        TextView instituteName,instituteLocation;
        public MyViewHolder(View itemView) {
            super(itemView);


            thumbnail = (ImageView) itemView .findViewById(R.id.thumbnail);
            clinetbanner =(ImageView) itemView.findViewById(R.id.clinetbanner);
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


}
