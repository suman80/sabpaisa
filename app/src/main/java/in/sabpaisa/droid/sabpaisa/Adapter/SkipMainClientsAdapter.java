package in.sabpaisa.droid.sabpaisa.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
//import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.sabpaisa.droid.sabpaisa.AddMemberToSpaceActivity;
import in.sabpaisa.droid.sabpaisa.AddMemberTo_A_Group;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.AppDB.NotificationDB;
import in.sabpaisa.droid.sabpaisa.EditSpace;
import in.sabpaisa.droid.sabpaisa.Fragments.AddMemberToSpaceDialogFragment;
import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.Model.MemberOfflineDataModel;
import in.sabpaisa.droid.sabpaisa.Model.MemberSpaceModel;
import in.sabpaisa.droid.sabpaisa.Model.Member_GetterSetter;
import in.sabpaisa.droid.sabpaisa.Model.PersonalSpaceModel;
import in.sabpaisa.droid.sabpaisa.Model.UserImageModel;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.Model.SkipClientData;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.SkipClientDetailsScreen;

import static android.content.Context.MODE_PRIVATE;
import static in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments.TABLE_NAME_MEMBERS;
import static in.sabpaisa.droid.sabpaisa.MainActivitySkip.SUPER_ADMIN_SHAREDFREF;

//import com.elyeproj.loaderviewlibrary.LoaderImageView;
public class SkipMainClientsAdapter extends RecyclerView.Adapter<SkipMainClientsAdapter.MyViewHolder> {
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private ArrayList<PersonalSpaceModel> institutions;

    ArrayList<MemberSpaceModel> memberSpaceModelArrayList;

    Context context;

    ProgressDialog progressDialog;

    NotificationDB db;


    public SkipMainClientsAdapter(ArrayList<PersonalSpaceModel> institutions,Context context) {
        this.institutions = institutions;
        this.context=context;
    }

    /*START Method to change data when put query in searchBar*/
    public void setItems(ArrayList<PersonalSpaceModel> feedDatas) {
        this.institutions = feedDatas;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

            final PersonalSpaceModel mainFeedData = institutions.get(position);
            holder.instituteName.setText(mainFeedData.getAppCname());
            holder.instituteLocation.setText(mainFeedData.getDescription());
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

                checkMemberData(mainFeedData.getAppCid(),mainFeedData);


            }
        });


        holder.imgPopUpMenu.setOnClickListener(new View.OnClickListener() {
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





        db= new NotificationDB(context);

        int commentCounterF = 0;

        Cursor resF = db.getSpaceNotificationFeed(mainFeedData.getAppCid());
        if (resF.getCount() > 0) {
            StringBuffer stringBuffer = new StringBuffer();


            while (resF.moveToNext()) {
                stringBuffer.append(resF.getString(0) + " ");
                stringBuffer.append(resF.getString(1) + " ");
                stringBuffer.append(resF.getString(2) + " ");
                commentCounterF = Integer.parseInt(resF.getString(2));
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
                commentCounterG = Integer.parseInt(resG.getString(2));
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_space_data, parent, false);
        return new MyViewHolder(v);
    }


    public void checkMemberData (final String appCid,final PersonalSpaceModel personalSpaceModel)
    {


        String tag_string_req = "req_register";
        String url = AppConfig.Base_Url+AppConfig.App_api+AppConfig.URL_membersOfSPappclient+"?appcid="+appCid;

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>(){

            // Takes the response from the JSON request
            @Override
            public void onResponse(String response) {
                try {

                    Log.d("SMCA", "Member: " + response);
                    //swipeRefreshLayout.setRefreshing(false);
                    memberSpaceModelArrayList = new ArrayList<>();

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    String response1 = jsonObject.getString("response");

                    if (status.equals("failure")) {

                        Log.d("SMCA", "InFail: "+response1);

                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }

                    }else {


                        JSONArray jsonArray = jsonObject.getJSONArray("response");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            final MemberSpaceModel memberSpaceModel = new MemberSpaceModel();
                            memberSpaceModel.setMemberId(jsonObject1.getString("memberId"));
                            memberSpaceModelArrayList.add(memberSpaceModel);

                }

                Log.d("SMCA","memberSpaceModelArrayList___"+memberSpaceModelArrayList.size());


                        if (memberSpaceModelArrayList.size()>1) {

                            if (progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }

                            Intent intent = new Intent(context, SkipClientDetailsScreen.class);
                            intent.putExtra("clientName", personalSpaceModel.getAppCname());
                            intent.putExtra("state", personalSpaceModel.getAddress());
                            intent.putExtra("clientLogoPath", personalSpaceModel.getClientLogoPath());
                            intent.putExtra("clientImagePath", personalSpaceModel.getClientImagePath());
                            intent.putExtra("appCid", personalSpaceModel.getAppCid());


                            //Storing value for user role and setting default to 1
                            SharedPreferences.Editor editor = context.getSharedPreferences(SUPER_ADMIN_SHAREDFREF, MODE_PRIVATE).edit();
                            editor.putString("ROLE_VALUE", "1");
                            editor.apply();



                            context.startActivity(intent);

                        }else {

                           /* FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            AddMemberToSpaceDialogFragment addMemberToSpaceDialogFragment = new AddMemberToSpaceDialogFragment();

                            addMemberToSpaceDialogFragment.show(fragmentManager, "MemberDialogFragment");*/


                            /*Bundle bundle = new Bundle();
                            bundle.putString("appCid", personalSpaceModel.getAppCid());
                            bundle.putString("state", personalSpaceModel.getAddress());
                            bundle.putString("clientLogoPath", personalSpaceModel.getClientLogoPath());
                            bundle.putString("clientImagePath", personalSpaceModel.getClientImagePath());
                            bundle.putString("clientName", personalSpaceModel.getAppCname());
                            addMemberToSpaceDialogFragment.setArguments(bundle);*/

                            if (progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }

                            Intent intent = new Intent(context, AddMemberToSpaceActivity.class);
                            intent.putExtra("appCid", personalSpaceModel.getAppCid());
                            intent.putExtra("state", personalSpaceModel.getAddress());
                            intent.putExtra("clientLogoPath", personalSpaceModel.getClientLogoPath());
                            intent.putExtra("clientImagePath", personalSpaceModel.getClientImagePath());
                            intent.putExtra("clientName", personalSpaceModel.getAppCname());

                            //Storing value for user role and setting default to 1 with token
                            SharedPreferences.Editor editor = context.getSharedPreferences(SUPER_ADMIN_SHAREDFREF, MODE_PRIVATE).edit();
                            editor.putString("ROLE_VALUE", "1");
                            editor.apply();


                            context.startActivity(intent);



                        }



                    }

                }
                // Try and catch are included to handle any errors due to JSON
                catch (JSONException e) {
                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();

                }
            }
        },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {

                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }

                        error.printStackTrace();

                        Log.e("SMCA", "onErrorResponse"+error);
                    }
                }
        );
        // Adds the JSON array request "arrayreq" to the request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);
    }




}
