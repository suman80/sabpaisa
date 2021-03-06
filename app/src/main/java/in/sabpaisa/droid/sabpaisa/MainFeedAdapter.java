package in.sabpaisa.droid.sabpaisa;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.AppDB.NotificationDB;
import in.sabpaisa.droid.sabpaisa.Model.FeedNotificatonModel;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;

import static in.sabpaisa.droid.sabpaisa.AppDB.NotificationDB.TABLE_FEEDNOTIFICATION;
import static in.sabpaisa.droid.sabpaisa.ConstantsForUIUpdates.FEED_ARRAYLIST;


public class MainFeedAdapter extends RecyclerView.Adapter<MainFeedAdapter.MyViewHolder>  {

    private ArrayList<FeedData> mainFeedDataList;
    ImageLoader imageLoader;
    Context context;
    String popup="Feeds";

    public static boolean isClicked=false;

    String userAccessToken;

    NotificationDB db;

    //public static ProgressDialog progressDialog;


    public MainFeedAdapter(ArrayList<FeedData> countryList, Context context) {
        this.mainFeedDataList = countryList;
        this.context = context;
        //progressDialog = new ProgressDialog(context,R.style.DialogTheme);
    }


    /*START Method to change data when put query in searchBar*/
    public void setItems(ArrayList<FeedData> feedDatas) {
        this.mainFeedDataList = feedDatas;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final FeedData mainFeedData = mainFeedDataList.get(position);

        Glide.with(context)
                .load(mainFeedData.getLogoPath())
                .error(R.drawable.image_not_found)
                .into(holder.cilent_Logo);

        //Commenting on 29th Nov 2018 for banner image

        /*Glide.with(context)
                .load(mainFeedData.getImagePath())
                .error(R.drawable.image_not_found)
                .into(holder.client_Image);*/



        holder.main_feed_name.setText(mainFeedData.getFeedName());

        holder.main_feed_description.setText(mainFeedData.getFeedText());


        db= new NotificationDB(context);

        Cursor res = db.getParticularFeedNotificationData(mainFeedData.getFeedId());
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

        holder.rippleClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* progressDialog.setMessage("Please wait !");
                progressDialog.show();*/

                //////////////Notification db//////////////////////////
                //db = new NotificationDB(context);
                if (db.isTableExists(TABLE_FEEDNOTIFICATION)){

                    Cursor res = db.getParticularFeedNotificationData(mainFeedData.getFeedId());
                    if (res.getCount() > 0) {
                        StringBuffer stringBuffer = new StringBuffer();

//                        while (res.moveToNext()) {
//                            stringBuffer.append(res.getString(0) + " ");
//                            stringBuffer.append(res.getString(1) + " ");
//                            stringBuffer.append(res.getString(2) + " ");
//                            stringBuffer.append(res.getString(3) + " ");
//                            stringBuffer.append(res.getString(4) + " ");
//                        }

//                        Log.d("PFF_Notification","stringBuffer___ "+stringBuffer);
                        boolean isUpdated = db.updateFeedNotificationData(mainFeedData.getFeedId(),0,0, System.currentTimeMillis(),true);
                        if (isUpdated == true){
                            Log.d("PFF_Notification","Updated "+isUpdated);
                            holder.relativeLayoutNotification.setVisibility(View.GONE);
                        }else {
                            Log.d("PFF_Notification","NotUpdated "+isUpdated);
                        }
                    }
                    else{

                        FeedNotificatonModel feedNotificatonModel = new FeedNotificatonModel();
                        feedNotificatonModel.setFeedId(mainFeedData.getFeedId());
                        feedNotificatonModel.setFeedNotificationCount(0);
                        feedNotificatonModel.setFeedRecentCommentTimeStamp(0);
                        feedNotificatonModel.setFeedRecentOpenCommentTimeStamp(System.currentTimeMillis());
                        feedNotificatonModel.setFeedIsOpen(true);
                        if (PrivateGroupFeeds.FLAG != null) {
                            feedNotificatonModel.setFeedMode("Private");
                            feedNotificatonModel.setFeedGroupId(PrivateGroupFeeds.GroupId);
                        }else {
                            feedNotificatonModel.setFeedMode("Public");
                            feedNotificatonModel.setFeedGroupId(null);
                        }

                        feedNotificatonModel.setAppCid(null);
                        feedNotificatonModel.setClientId(mainFeedData.getClientId());

                        boolean isInserted = db.insertFeedNotificationData(feedNotificatonModel);
                        if (isInserted == true) {


                            Log.d("PFF_Notification", "Notification Insert : " + isInserted);

                        } else {

                            Log.d("PFF_Notification", "Notification Insert : " + isInserted);

                        }





                    }

//                    boolean isUpdated = db.updateFeedNotificationData(mainFeedData.getFeedId(),0,0, System.currentTimeMillis(),true);
//                    if (isUpdated == true){
//                        Log.d("PFF_Notification","Updated "+isUpdated);
//                        holder.relativeLayoutNotification.setVisibility(View.GONE);
//                    }else {
//                        Log.d("PFF_Notification","NotUpdated "+isUpdated);
//                    }



                }



                Intent intent = new Intent(view.getContext(), Proceed_Feed_FullScreen.class);
                intent.putExtra("feedName", mainFeedData.getFeedName());
                intent.putExtra("popup",popup);
                intent.putExtra("feedText", mainFeedData.getFeedText());
                intent.putExtra("feedImage", mainFeedData.getImagePath());
                intent.putExtra("feedLogo", mainFeedData.getLogoPath());
                intent.putExtra("feedId", mainFeedData.getFeedId());
//                intent.putExtra("memberGroupRole", PrivateGroupFeeds.memberGroupRole);

                Log.d("MainFeedADAClick"," "+isClicked);

                if (!isClicked) {


                    isClicked = !isClicked;
                    view.getContext().startActivity(intent);

                }
            }
        });

        SharedPreferences sharedPreferencesRole = context.getSharedPreferences(UIN.SHARED_PREF_FOR_CHECK_USER, Context.MODE_PRIVATE);

        String roleValue = sharedPreferencesRole.getString("USER_ROLE", "abc");

        String memberGroupRole = PrivateGroupFeeds.memberGroupRole;


        if (roleValue.equals("1") || (PrivateGroupFeeds.FLAG != null && (memberGroupRole != null) && memberGroupRole.equals("2"))) {

            holder.imgPopUpMenu.setVisibility(View.VISIBLE);
        }

        holder.imgPopUpMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences1 = context.getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

                userAccessToken = sharedPreferences1.getString("response", "123");

                final String feedId = mainFeedData.getFeedId();
                //Toast.makeText(context,feedId,Toast.LENGTH_SHORT).show();

                Log.d("MainFeedAdapter","FeedIdIs: "+feedId);


                PopupMenu menu = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    menu = new PopupMenu(context,view,Gravity.CENTER);
                }

                menu.getMenu().add("Delete");

                if (PrivateGroupFeeds.FLAG != null) {

                    menu.getMenu().add("Add Member");

                }

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (menuItem.getTitle().equals("Delete")){

                           /* progressDialog.setMessage("Please wait !");
                            progressDialog.show();*/
                           if (isNetworkAvailable()) {
                               deleteFeedData(feedId, userAccessToken, holder.getAdapterPosition());
                           }else {
                               Toast.makeText(context,"Please check internet connection and try again. Thank you.",Toast.LENGTH_SHORT).show();

                           }
                        }


                        if (menuItem.getTitle().equals("Add Member")){
                            /*if (progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }*/
                            Intent intent = new Intent(context,AddMemberTo_A_PrivateFeed.class);
                            intent.putExtra("feedId",feedId);
                            intent.putExtra("GroupId",PrivateGroupFeeds.GroupId);
                            context.startActivity(intent);
                        }


                        return true;
                    }
                });

                menu.show();


            }
        });



    }


    @Override
    public int getItemCount() {
        return mainFeedDataList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_feed_list_item, parent, false);
        return new MyViewHolder(v);
    }

    /**
     * View holder class
     */
    public  class MyViewHolder extends RecyclerView.ViewHolder  {
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

        Log.d("MainFeedAdapter", "URL-->" + url);

        StringRequest request = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {

                Log.d("MainFeedAdapter", "-->" + response1);
                //parsing Json
                JSONObject jsonObject = null;

                /*if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }*/


                try {

                    jsonObject = new JSONObject(response1);
                    String response = jsonObject.getString("response");
                    String status = jsonObject.getString("status");
                    Log.d("MainFeedAdapterResp", "" + response);
                    Log.d("MainFeedAdapterStatus", "" + status);

                    if (status.equals("success")){
                        Log.d("MainFeedAdapter","InIfPart");


                        /*if (PrivateGroupFeeds.FLAG != null){

                            Intent intent = new Intent(context,PrivateGroupFeeds.class);
                            intent.putExtra("GroupId",PrivateGroupFeeds.GroupId);
                            intent.putExtra("",PrivateGroupFeeds.memberGroupRole);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            MainGroupAdapter1.isClicked = false;
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                        }else {


                            String clientImageURLPath = FullViewOfClientsProceed.clientImageURLPath;

                            Log.d("MainFeedAdapter", "clientImageURLPath " + clientImageURLPath);

                            Intent intent = new Intent(context, FullViewOfClientsProceed.class);
                            intent.putExtra("clientImagePath", clientImageURLPath);
                            intent.putExtra("FRAGMENT_ID", "0");
                            context.startActivity(intent);
                        }*/


                        mainFeedDataList.remove(pos);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, getItemCount() - pos);

                        //Update UI When No data found
                        if (getItemCount() == 0){
                            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(FEED_ARRAYLIST));
                        }



                    }else {
                        Log.d("MainFeedAdapter","InElsePart");
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




//    public void callFeedDataList(final String clientId) {
//
//        String tag_string_req = "req_register";
//
//        String urlJsonObj = AppConfig.Base_Url + AppConfig.App_api + "getParticularClientsFeeds/" + "?client_Id=" + clientId;
//
//
//        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
//                urlJsonObj, new Response.Listener<String>() {
//
//            // Takes the response from the JSON request
//            @Override
//            public void onResponse(String response) {
//                try {
//                    //    swipeRefreshLayout.setRefreshing(false);
//                    Log.d("MainFeedAdapter", "profeed1: " + response);
//                    //swipeRefreshLayout.setRefreshing(false);
//                    ArrayList<FeedData> feedArrayList = new ArrayList<FeedData>();
//
//                    JSONObject jsonObject = new JSONObject(response);
//
//                    String status = jsonObject.getString("status");
//
//                    String response1 = jsonObject.getString("response");
//
//                    JSONArray jsonArray = null;
//                    Object obj = jsonObject.get("response");
//                    if (obj instanceof JSONArray) {
//                        jsonArray = (JSONArray) obj;
//
//                        for (int i = 0; i < jsonArray.length(); i++) {
//
//                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                            final FeedData feedData = new FeedData();
//                            feedData.setClientId(jsonObject1.getString("clientId"));
//                            feedData.setFeedId(jsonObject1.getString("feedId"));
//                            feedData.setFeedName(jsonObject1.getString("feedName"));
//                            feedData.setFeedText(jsonObject1.getString("feedText"));
//                            feedData.setCreatedDate(jsonObject1.getString("createdDate"));
//                            feedData.setLogoPath(jsonObject1.getString("logoPath"));
//                            feedData.setImagePath(jsonObject1.getString("imagePath"));
//                            feedArrayList.add(feedData);
//
//
//                        }
//                        Log.d("feedArrayListAfterParse", " " + feedArrayList.get(0).getFeedName());
//                        //*START listener for sending data to activity*//*
//                        /*OnFragmentInteractionListener listener = (OnFragmentInteractionListener) getActivity();
//                        listener.onFragmentSetFeeds(feedArrayList);
//
//*/
//                        //*END listener for sending data to activity*//*
//
//                        MainFeedAdapter mainFeedAdapter = new MainFeedAdapter(feedArrayList, getContext());
//                        recyclerView.setAdapter(mainFeedAdapter);
//
//                    } else {
//                        linearLayoutnoDataFound.setVisibility(View.VISIBLE);
//                        recyclerView.setVisibility(View.GONE);
//                    }
//
//                }
//                // Try and catch are included to handle any errors due to JSON
//                catch (JSONException e) {
//                    // If an error occurs, this prints the error to the log
//                    e.printStackTrace();
//                    callFeedDataList(clientId);
//                }
//            }
//        },
//                // The final parameter overrides the method onErrorResponse() and passes VolleyError
//                //as a parameter
//                new Response.ErrorListener() {
//                    @Override
//                    // Handles errors that occur due to Volley
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                        callFeedDataList(clientId);
//                        Log.e("Feed", "FeedError");
//                    }
//                }
//        );
//        // Adds the JSON array request "arrayreq" to the request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);
//    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
