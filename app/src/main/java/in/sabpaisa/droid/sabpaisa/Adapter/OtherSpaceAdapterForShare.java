package in.sabpaisa.droid.sabpaisa.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.balysv.materialripple.MaterialRippleLayout;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.Model.MemberSpaceModel;
import in.sabpaisa.droid.sabpaisa.Model.PersonalSpaceModel;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.ShareFromOtherApp;
import in.sabpaisa.droid.sabpaisa.ShareOnFeedsAndGroups;

public class OtherSpaceAdapterForShare extends RecyclerView.Adapter<OtherSpaceAdapterForShare.MyViewHolder> {
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private ArrayList<PersonalSpaceModel> institutions;

    ArrayList<MemberSpaceModel> memberSpaceModelArrayList;

    Context context;

    public static ProgressDialog progressDialog;



    public OtherSpaceAdapterForShare(ArrayList<PersonalSpaceModel> institutions,Context context) {
        this.institutions = institutions;
        this.context=context;
    }

    //START Method to change data when put query in searchBar
    public void setItems(ArrayList<PersonalSpaceModel> feedDatas) {
        this.institutions = feedDatas;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final PersonalSpaceModel mainFeedData = institutions.get(position);
        holder.instituteName.setText(mainFeedData.getAppCname());
        holder.instituteLocation.setText(mainFeedData.getAddress());
        //holder.thumbnail.setImageIcon(Icon.createWithContentUri(mainFeedData.getOrgLogo()));

        if (mainFeedData.getClientImagePath()==null)
        {
            holder.thumbnail.setDefaultImageResId(R.drawable.image_not_found);
        }else {
            holder.thumbnail.setImageUrl(mainFeedData.getClientLogoPath(), imageLoader);
        }

        if (mainFeedData.getClientLogoPath()==null)
        {
            holder.clinetbanner.setDefaultImageResId(R.drawable.image_not_found);
        }else {
            holder.clinetbanner.setImageUrl(mainFeedData.getClientImagePath(),imageLoader);
        }

        holder.rippleClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //API to check member data



                Intent intent = new Intent(context, ShareOnFeedsAndGroups.class);
                intent.putExtra("recievedText", ShareFromOtherApp.recievedText);
                if (ShareFromOtherApp.recievedImageUri!=null) {
                    intent.putExtra("imageUri", ShareFromOtherApp.recievedImageUri.toString());
                }
                intent.putExtra("appCid", mainFeedData.getAppCid());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                //Storing value for user role and setting default to 1 with token
                /*SharedPreferences.Editor editor = context.getSharedPreferences(SUPER_ADMIN_SHAREDFREF, MODE_PRIVATE).edit();
                editor.putString("ROLE_VALUE", "3");
                editor.apply();*/



                context.startActivity(intent);



            }
        });

        holder.imgPopUpMenu.setVisibility(View.GONE);



    }


    @Override
    public int getItemCount() {
        if (institutions != null && !institutions.isEmpty()) {
            return institutions.size();
        }
        else return 0;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView instituteLogo,institutePic;
        NetworkImageView thumbnail,clinetbanner;
        TextView instituteName,instituteLocation;
        MaterialRippleLayout rippleClick;
        ImageView imgPopUpMenu;

        RelativeLayout relativeLayoutNotification;
        TextView notificationText;

        public MyViewHolder(View itemView) {
            super(itemView);


            thumbnail = (NetworkImageView) itemView .findViewById(R.id.thumbnail);
            clinetbanner =(NetworkImageView) itemView.findViewById(R.id.clinetbanner);

            instituteName = (TextView)itemView.findViewById(R.id.tv_instituteName);
            instituteLocation = (TextView)itemView.findViewById(R.id.tv_instituteLocation);
            rippleClick = (MaterialRippleLayout)itemView.findViewById(R.id.rippleClick);
            imgPopUpMenu = (ImageView)itemView.findViewById(R.id.imgPopUpMenu);

            relativeLayoutNotification = (RelativeLayout) itemView.findViewById(R.id.relativeLayoutNotification);
            notificationText = (TextView) itemView.findViewById(R.id.notificationText);
            progressDialog = new ProgressDialog(context,R.style.DialogTheme);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_other_space_view, parent, false);
        return new MyViewHolder(v);
    }



}
