package in.sabpaisa.droid.sabpaisa.Adapter;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.COA;
import in.sabpaisa.droid.sabpaisa.Model.Institution;
import in.sabpaisa.droid.sabpaisa.R;

import static android.graphics.drawable.Icon.createWithContentUri;

/**
 * Created by abc on 14-06-2017.
 */

public class InstitutionProceedAdapter extends RecyclerView.Adapter<InstitutionProceedAdapter.MyViewHolder> {
    int count;
    ArrayList<Institution> institutions;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
   @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_institutions_tab_proceed, parent, false);
        return new MyViewHolder(v);
    }


    public InstitutionProceedAdapter(ArrayList<Institution> institutions) {
        this.institutions = institutions;
    }




    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {



        Institution mainFeedData = institutions.get(position);
        holder.instituteName.setText(mainFeedData.getOrganization_name());
        holder.instituteLocation.setText(mainFeedData.getOrgAddress());
        //holder.thumbnail.setImageIcon(Icon.createWithContentUri(mainFeedData.getOrgLogo()));
        holder.thumbnail.setImageUrl(mainFeedData.getOrgLogo(), imageLoader);
        holder.clinetbanner.setImageUrl(mainFeedData.getOrgWal(),imageLoader);
        //holder.thumbnail.setImageIcon(Icon.createWithContentUri(mainFeedData.getOrgLogo()));
//        holder.instituteLocation.setText(mainFeedData.getOrgDesc());
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), COA.class);
                v.getContext().startActivity(intent);
            }
        });
        holder.instituteLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), COA.class);
                v.getContext().startActivity(intent);
            }
        });
        holder.instituteName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), COA.class);
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


            thumbnail = (NetworkImageView) itemView .findViewById(R.id.iv_instituteLogo);
            clinetbanner =(NetworkImageView) itemView.findViewById(R.id.iv_institutePic);
            //instituteLogo = (ImageView)itemView.findViewById(R.id.iv_instituteLogo);
            //institutePic = (ImageView)itemView.findViewById(R.id.iv_institutePic);
            //thumbnail =(NetworkImageView)itemView.findViewById(R.id.thumbnail);

            instituteName = (TextView)itemView.findViewById(R.id.tv_instituteName);
            instituteLocation = (TextView)itemView.findViewById(R.id.tv_instituteLocation);
        }
    }







    @Override
    public int getItemCount() {
        return institutions.size();
    }
}
