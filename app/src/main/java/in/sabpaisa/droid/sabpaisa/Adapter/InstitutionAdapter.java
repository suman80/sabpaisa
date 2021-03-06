package in.sabpaisa.droid.sabpaisa.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.balysv.materialripple.MaterialRippleLayout;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.Model.Institution;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;

import static android.graphics.drawable.Icon.createWithContentUri;

/**
 * Created by abc on 14-06-2017.
 */

public class InstitutionAdapter extends RecyclerView.Adapter<InstitutionAdapter.MyViewHolder> {
    int count;
    ArrayList<Institution> institutions;
    Context context;

    public static ProgressDialog progressDialog;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_institutions_tab, parent, false);
        return new MyViewHolder(v);
    }

    public InstitutionAdapter(Context context,ArrayList<Institution> institutions) {
        this.institutions = institutions;
        this.context = context;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

            final Institution mainFeedData = institutions.get(position);

            holder.instituteName.setText(mainFeedData.getOrganization_name());
            holder.instituteLocation.setText(mainFeedData.getOrgAddress());
            //holder.thumbnail.setImageIcon(Icon.createWithContentUri(mainFeedData.getOrgLogo()));

            if (mainFeedData.getOrgLogo() == null) {
                holder.thumbnail.setDefaultImageResId(R.drawable.image_not_found);
            } else {
                holder.thumbnail.setImageUrl(mainFeedData.getOrgLogo(), imageLoader);
                Log.d("image1234", "adapter-->" + mainFeedData.getOrgLogo().toString());
            }

            if (mainFeedData.getOrgLogo() == null) {
                holder.clinetbanner.setDefaultImageResId(R.drawable.image_not_found);
            } else {
                holder.clinetbanner.setImageUrl(mainFeedData.getOrgWal(), imageLoader);
                Log.d("image1234", "adapter-->" + mainFeedData.getOrgWal().toString());

            }



       /* holder.linearLayout_ParticularClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FullViewOfClientsProceed.class);
                intent.putExtra("clientName", mainFeedData.getOrganization_name());
                intent.putExtra("state", mainFeedData.getOrgAddress());
                intent.putExtra("clientLogoPath", mainFeedData.getOrgLogo());
                intent.putExtra("clientImagePath", mainFeedData.getOrgWal());
                Log.d("clientId_banner", "" + mainFeedData.getOrganizationId());
                intent.putExtra("clientId", mainFeedData.getOrganizationId());
                intent.putExtra("landing_page", mainFeedData.getOrgDesc());
                view.getContext().startActivity(intent);
            }
        });*/

        holder.rippleClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.setMessage("Please wait !");
                progressDialog.show();

                Intent intent = new Intent(view.getContext(), FullViewOfClientsProceed.class);
                intent.putExtra("clientName", mainFeedData.getOrganization_name());
                intent.putExtra("state", mainFeedData.getOrgAddress());
                intent.putExtra("clientLogoPath", mainFeedData.getOrgLogo());
                intent.putExtra("clientImagePath", mainFeedData.getOrgWal());
                Log.d("clientId_banner", "" + mainFeedData.getOrganizationId());
                intent.putExtra("clientId", mainFeedData.getOrganizationId());
                intent.putExtra("landing_page", mainFeedData.getOrgDesc());
                view.getContext().startActivity(intent);


            }
        });

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView instituteLogo,institutePic;
        NetworkImageView thumbnail,clinetbanner;
        TextView instituteName,instituteLocation;
        LinearLayout linearLayout_ParticularClient;
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

            linearLayout_ParticularClient = (LinearLayout)itemView.findViewById(R.id.linearLayout_ParticularClient);

            rippleClick = (MaterialRippleLayout)itemView.findViewById(R.id.rippleClick);

            progressDialog = new ProgressDialog(context,R.style.DialogTheme);

        }
    }

    @Override
    public int getItemCount() {
        return institutions.size();
    }



}
