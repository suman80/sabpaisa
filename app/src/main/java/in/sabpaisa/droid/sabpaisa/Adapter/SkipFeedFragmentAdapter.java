package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.AppDB.NotificationDB;
import in.sabpaisa.droid.sabpaisa.FeedData;
import in.sabpaisa.droid.sabpaisa.FeedSpaceCommentsActivity;
import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.Model.FeedNotificatonModel;
import in.sabpaisa.droid.sabpaisa.PrivateGroupFeeds;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.SkipClientDetailsScreen;

import static in.sabpaisa.droid.sabpaisa.AppDB.NotificationDB.TABLE_FEEDNOTIFICATION;
import static in.sabpaisa.droid.sabpaisa.ConstantsForUIUpdates.FEED_ARRAYLIST;

public class SkipFeedFragmentAdapter extends RecyclerView.Adapter<SkipFeedFragmentAdapter.MyViewHolder> {

    private List<FeedData> feedDataList;
    Context context;
    NotificationDB db;


    public SkipFeedFragmentAdapter(List<FeedData> arrayList, Context context) {
        this.feedDataList = arrayList;
        this.context = context;
    }

    /*START Method to change data when put query in searchBar*/
    public void setItems(ArrayList<FeedData> feedDatas) {
        this.feedDataList = feedDatas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_skip_feed, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final FeedData feedData = feedDataList.get(position);

        Glide.with(context)
                .load(feedData.getLogoPath())
                .error(R.drawable.image_not_found)
                .into(holder.cilent_Logo);


        holder.main_feed_name.setText(feedData.getFeedName());

        holder.main_feed_description.setText(feedData.getFeedText());




//        if (memberSpaceModel.getRoleId().equals("1")){
        holder.imgPopUpMenu.setVisibility(View.VISIBLE);
        // }



        db= new NotificationDB(context);

        Cursor res = db.getParticularFeedNotificationData(feedData.getFeedId());
        if (res.getCount() > 0) {
            StringBuffer stringBuffer = new StringBuffer();

            int commentCounter = 0;
            while (res.moveToNext()) {
                stringBuffer.append(res.getString(0) + " ");
                stringBuffer.append(res.getString(1) + " ");
                stringBuffer.append(res.getString(2) + " ");
                commentCounter = Integer.parseInt(res.getString(2));
                stringBuffer.append(res.getString(3) + " ");
                stringBuffer.append(res.getString(4) + " ");
                stringBuffer.append(res.getString(5) + " ");
            }

            Log.d("MainFeedAdapt", "Notification " + stringBuffer);

            if (commentCounter > 0) {
                holder.relativeLayoutNotification.setVisibility(View.VISIBLE);

                if (commentCounter <= 9) {
                    holder.notificationText.setText(String.valueOf(commentCounter));
                } else {
                    holder.notificationText.setText(String.valueOf("9+"));
                }
            }


        }




        holder.imgPopUpMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                PopupMenu menu = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    menu = new PopupMenu(context,view, Gravity.CENTER);
                }

                menu.getMenu().add("Remove");


                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (menuItem.getTitle().equals("Remove")){

                            SharedPreferences sharedPreferences = context.getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

                            String userAccessToken = sharedPreferences.getString("response", "123");

                            //Todo Api Integration
                            deleteFeedData(feedData.getFeedId(),userAccessToken,holder.getAdapterPosition());

                        }




                        return true;
                    }
                });

                menu.show();


            }
        });



        holder.rippleClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (db.isTableExists(TABLE_FEEDNOTIFICATION)){

                    Cursor res = db.getParticularFeedNotificationData(feedData.getFeedId());
                    if (res.getCount() > 0) {
                        StringBuffer stringBuffer = new StringBuffer();

                        while (res.moveToNext()) {
                            stringBuffer.append(res.getString(0) + " ");
                            stringBuffer.append(res.getString(1) + " ");
                            stringBuffer.append(res.getString(2) + " ");
                            stringBuffer.append(res.getString(3) + " ");
                            stringBuffer.append(res.getString(4) + " ");
                        }

                        Log.d("PFF_Notification","stringBuffer___ "+stringBuffer);

                        boolean isUpdated = db.updateFeedNotificationData(feedData.getFeedId(),0,0, System.currentTimeMillis(),true);
                        if (isUpdated == true){
                            Log.d("SFFA_Notification","Updated "+isUpdated);
                            holder.relativeLayoutNotification.setVisibility(View.GONE);
                        }else {
                            Log.d("SFFA_Notification","NotUpdated "+isUpdated);
                        }
                    }
                    else{

                        FeedNotificatonModel feedNotificatonModel = new FeedNotificatonModel();
                        feedNotificatonModel.setFeedId(feedData.getFeedId());
                        feedNotificatonModel.setFeedNotificationCount(0);
                        feedNotificatonModel.setFeedRecentCommentTimeStamp(0);
                        feedNotificatonModel.setFeedRecentOpenCommentTimeStamp(System.currentTimeMillis());
                        feedNotificatonModel.setFeedIsOpen(true);
                        /*if (PrivateGroupFeeds.FLAG != null) {
                            feedNotificatonModel.setFeedMode("Private");
                            feedNotificatonModel.setFeedGroupId(PrivateGroupFeeds.GroupId);
                        }else {*/
                            feedNotificatonModel.setFeedMode("Public");
                            feedNotificatonModel.setFeedGroupId(null);
                        //}

                        SharedPreferences sharedPreferences = context.getSharedPreferences(SkipClientDetailsScreen.MySharedPrefOnSkipClientDetailsScreenForAppCid, Context.MODE_PRIVATE);
                        String appCid=sharedPreferences.getString("appCid","abc");
                        Log.d("SFFA",""+appCid);

                        feedNotificatonModel.setAppCid(appCid);
                        feedNotificatonModel.setClientId(null);

                        boolean isInserted = db.insertFeedNotificationData(feedNotificatonModel);
                        if (isInserted == true) {


                            Log.d("SFFA_Notification", "Notification Insert : " + isInserted);

                        } else {

                            Log.d("SFFA_Notification", "Notification Insert : " + isInserted);

                        }





                    }


                }



                Intent intent = new Intent(context, FeedSpaceCommentsActivity.class);
                intent.putExtra("feedName",feedData.getFeedName());
                intent.putExtra("feedText",feedData.getFeedText());
                intent.putExtra("feedId",feedData.getFeedId());
                intent.putExtra("feedImage",feedData.getImagePath());
                intent.putExtra("feedLogo",feedData.getLogoPath());
                intent.putExtra("clientId",feedData.getClientId());
                context.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return feedDataList.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView main_feed_description, main_feed_name, main_feed_creation_time;
        ImageView client_Image, cilent_Logo;
        LinearLayout linearLayout_feed;
        MaterialRippleLayout rippleClick;
        ImageView imgPopUpMenu;
        RelativeLayout relativeLayoutNotification;
        TextView notificationText;

        public MyViewHolder(View view) {
            super(view);
            main_feed_description = (TextView) view.findViewById(R.id.main_feed_description);
            main_feed_name = (TextView) view.findViewById(R.id.main_feed_name);
            main_feed_creation_time = (TextView) view.findViewById(R.id.main_feed_creation_time);
            //client_Image = (ImageView) view.findViewById(R.id.client_Image);
            cilent_Logo = (ImageView) view.findViewById(R.id.client_Logo);
            linearLayout_feed = (LinearLayout)view.findViewById(R.id.linearLayout_feed);
            rippleClick = (MaterialRippleLayout)view.findViewById(R.id.rippleClick);
            imgPopUpMenu = (ImageView)view.findViewById(R.id.imgPopUpMenu);
            relativeLayoutNotification = (RelativeLayout) view.findViewById(R.id.relativeLayoutNotification);
            notificationText = (TextView) view.findViewById(R.id.notificationText);

        }
    }



    private void deleteFeedData(String feedId, String userAccessToken,final int pos) {

        String tag_string_req = "req_clients";

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_deleteFeed + "?feed_Id=" + feedId +"&admin="+userAccessToken;

        Log.d("SkipFeedFragmentAdapter", "URL-->" + url);

        StringRequest request = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {

                Log.d("SkipFeedFragmentAdapter", "-->" + response1);
                //parsing Json
                JSONObject jsonObject = null;

                /*if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }*/


                try {

                    jsonObject = new JSONObject(response1);
                    String response = jsonObject.getString("response");
                    String status = jsonObject.getString("status");
                    Log.d("SkipFeedFragAdptrResp", "" + response);
                    Log.d("SkipFeedFragAdptrStatus", "" + status);

                    if (status.equals("success")){
                        Log.d("SkipFeedFragAdptr","InIfPart");

                        feedDataList.remove(pos);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, getItemCount() - pos);

                        //Update UI When No data found
                        if (getItemCount() == 0){
                            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(FEED_ARRAYLIST));
                        }



                    }else {
                        Log.d("SkipFeedFragAdptr","InElsePart");
                        Toast.makeText(context,response,Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                /*if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }*/


                if (error.getMessage() == null || error instanceof TimeoutError || error instanceof NoConnectionError) {
                    android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context, R.style.MyDialogTheme).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Network/Connection Error");

                    // Setting Dialog Message
                    alertDialog.setMessage("Internet Connection is poor OR The Server is taking too long to respond.Please try again later.Thank you.");

                    // Setting Icon to Dialog
                    //  alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                    //Log.e(TAG, "Registration Error: " + error.getMessage());

                } else if (error instanceof AuthFailureError) {

                    //TODO
                } else if (error instanceof ServerError) {

                    //TODO
                } else if (error instanceof NetworkError) {

                    //TODO
                } else if (error instanceof ParseError) {

                    //TODO
                }


            }


        });

        AppController.getInstance().addToRequestQueue(request, tag_string_req);


    }




}
