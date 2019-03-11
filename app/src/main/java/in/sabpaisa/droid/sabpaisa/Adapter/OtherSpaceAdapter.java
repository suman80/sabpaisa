package in.sabpaisa.droid.sabpaisa.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
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
import in.sabpaisa.droid.sabpaisa.AppDB.NotificationDB;
import in.sabpaisa.droid.sabpaisa.EditSpace;
import in.sabpaisa.droid.sabpaisa.Model.MemberSpaceModel;
import in.sabpaisa.droid.sabpaisa.Model.PersonalSpaceModel;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.SkipClientDetailsScreen;

import static android.content.Context.MODE_PRIVATE;
import static in.sabpaisa.droid.sabpaisa.MainActivitySkip.SUPER_ADMIN_SHAREDFREF;

public class OtherSpaceAdapter extends RecyclerView.Adapter<OtherSpaceAdapter.MyViewHolder> {
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private ArrayList<PersonalSpaceModel> institutions;

    ArrayList<MemberSpaceModel> memberSpaceModelArrayList;

    Context context;

    ProgressDialog progressDialog;

    NotificationDB db;

    public OtherSpaceAdapter(ArrayList<PersonalSpaceModel> institutions,Context context) {
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


                //Storing value for user role and setting default to 1 with token
                SharedPreferences.Editor editor = context.getSharedPreferences(SUPER_ADMIN_SHAREDFREF, MODE_PRIVATE).edit();
                editor.putString("ROLE_VALUE", "3");
                editor.apply();



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



        db= new NotificationDB(context);

        int commentCounterF = 0;

        Cursor resF = db.getSpaceNotificationFeed(mainFeedData.getAppCid());
        if (resF.getCount() > 0) {
            StringBuffer stringBuffer = new StringBuffer();


            while (resF.moveToNext()) {
                stringBuffer.append(resF.getString(0) + " ");
                stringBuffer.append(resF.getString(1) + " ");
                stringBuffer.append(resF.getString(2) + " ");
                commentCounterF += Integer.parseInt(resF.getString(2));
                stringBuffer.append(resF.getString(3) + " ");
                stringBuffer.append(resF.getString(4) + " ");
                stringBuffer.append(resF.getString(5) + " ");
            }

            Log.d("SMCA", "Notification__F__ " + stringBuffer);



        }

        int commentCounterG = 0;


        Cursor resG = db.getSpaceNotificationGroup(mainFeedData.getAppCid());
        if (resG.getCount() > 0) {
            StringBuffer stringBufferG = new StringBuffer();


            while (resG.moveToNext()) {
                stringBufferG.append(resG.getString(0) + " ");
                stringBufferG.append(resG.getString(1) + " ");
                stringBufferG.append(resG.getString(2) + " ");
                commentCounterG += Integer.parseInt(resG.getString(2));
                stringBufferG.append(resG.getString(3) + " ");
                stringBufferG.append(resG.getString(4) + " ");
                stringBufferG.append(resG.getString(5) + " ");
            }

            Log.d("SMCA", "Notification__G__ " + stringBufferG);


        }


        int totalNotification = commentCounterF + commentCounterG;

        Log.d("totalNotification", "___" + totalNotification);


        if (totalNotification > 0) {
            holder.relativeLayoutNotification.setVisibility(View.VISIBLE);

            if (totalNotification <= 9) {
                holder.notificationText.setText(String.valueOf(totalNotification));
            } else {
                holder.notificationText.setText(String.valueOf("9+"));
            }
        }








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

        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_other_space_view, parent, false);
        return new MyViewHolder(v);
    }





}
