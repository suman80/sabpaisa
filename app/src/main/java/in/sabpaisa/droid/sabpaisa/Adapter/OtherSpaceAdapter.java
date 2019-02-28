package in.sabpaisa.droid.sabpaisa.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.balysv.materialripple.MaterialRippleLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.EditSpace;
import in.sabpaisa.droid.sabpaisa.Model.MemberSpaceModel;
import in.sabpaisa.droid.sabpaisa.Model.PersonalSpaceModel;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.SkipClientDetailsScreen;

public class OtherSpaceAdapter extends RecyclerView.Adapter<OtherSpaceAdapter.MyViewHolder> {
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private ArrayList<PersonalSpaceModel> institutions;

    ArrayList<MemberSpaceModel> memberSpaceModelArrayList;

    Context context;

    ProgressDialog progressDialog;

    public OtherSpaceAdapter(ArrayList<PersonalSpaceModel> institutions,Context context) {
        this.institutions = institutions;
        this.context=context;
    }

    /*START Method to change data when put query in searchBar*/
    /*public void setItems(ArrayList<PersonalSpaceModel> feedDatas) {
        this.institutions = feedDatas;
    }
*/
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
            holder.thumbnail.setImageUrl(mainFeedData.getClientImagePath(), imageLoader);
        }

        if (mainFeedData.getClientLogoPath()==null)
        {
            holder.clinetbanner.setDefaultImageResId(R.drawable.image_not_found);
        }else {
            holder.clinetbanner.setImageUrl(mainFeedData.getClientLogoPath(),imageLoader);
        }

        holder.rippleClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //API to check member data
                progressDialog = new ProgressDialog(context,R.style.DialogTheme);
                progressDialog.setMessage("Please Wait");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();

                Intent intent = new Intent(context, SkipClientDetailsScreen.class);
                intent.putExtra("clientName", mainFeedData.getAppCname());
                intent.putExtra("state", mainFeedData.getAddress());
                intent.putExtra("clientLogoPath", mainFeedData.getClientLogoPath());
                intent.putExtra("clientImagePath", mainFeedData.getClientImagePath());
                intent.putExtra("appCid", mainFeedData.getAppCid());
                context.startActivity(intent);



            }
        });

        holder.imgPopUpMenu.setVisibility(View.GONE);

        /*holder.imgPopUpMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                PopupMenu menu = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    menu = new PopupMenu(view.getContext(),view, Gravity.CENTER);
                }

                menu.getMenu().add("Edit");


                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (menuItem.getTitle().equals("Edit")){

                            Intent intent = new Intent(view.getContext(), EditSpace.class);
                            intent.putExtra("SPACE_NAME",mainFeedData.getAppCname());
                            intent.putExtra("SPACE_LOGO",mainFeedData.getClientLogoPath());
                            intent.putExtra("SPACE_IMAGE",mainFeedData.getClientImagePath());
                            intent.putExtra("SPACE_APP_CID",mainFeedData.getAppCid());
                            intent.putExtra("SPACE_DESCRIPTION",mainFeedData.getDescription());
                            view.getContext().startActivity(intent);


                        }


                        return true;
                    }
                });

                menu.show();

            }
        });
*/



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
        ImageView imgPopUpMenu;
        public MyViewHolder(View itemView) {
            super(itemView);


            thumbnail = (NetworkImageView) itemView .findViewById(R.id.thumbnail);
            clinetbanner =(NetworkImageView) itemView.findViewById(R.id.clinetbanner);

            instituteName = (TextView)itemView.findViewById(R.id.tv_instituteName);
            instituteLocation = (TextView)itemView.findViewById(R.id.tv_instituteLocation);
            rippleClick = (MaterialRippleLayout)itemView.findViewById(R.id.rippleClick);
            imgPopUpMenu = (ImageView)itemView.findViewById(R.id.imgPopUpMenu);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_other_space_view, parent, false);
        return new MyViewHolder(v);
    }





}
