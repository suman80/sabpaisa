package in.sabpaisa.droid.sabpaisa;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
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
import android.widget.Button;
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

import java.util.List;

import in.sabpaisa.droid.sabpaisa.AppDB.NotificationDB;
import in.sabpaisa.droid.sabpaisa.Model.GroupNotificationModel;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;

import static in.sabpaisa.droid.sabpaisa.AppDB.NotificationDB.TABLE_FEEDNOTIFICATION;
import static in.sabpaisa.droid.sabpaisa.AppDB.NotificationDB.TABLE_GROUPNOTIFICATION;
import static in.sabpaisa.droid.sabpaisa.ConstantsForUIUpdates.GROUP_ARRAYLIST;

public class MainGroupAdapter1 extends RecyclerView.Adapter<MainGroupAdapter1.MyViewHolder> {

    Context mContext;
    private List<GroupListData> countryList;
    public Button joinmember;

    String popup = "Group";

    String userAccessToken;

    public static boolean isClicked=false;

    //MyViewHolder globalHolder;

    NotificationDB db;

    BroadcastReceiver broadcastReceiver;

    int notificationCount;

    //public static ProgressDialog progressDialog;


    public MainGroupAdapter1() {
    }

    public MainGroupAdapter1(List<GroupListData> countryList, Context context) {
        this.countryList = countryList;
        this.mContext = context;
    }

    /*START Method to change data when put query in searchBar*/
    public void setItems(List<GroupListData> groupDatas) {
        this.countryList = groupDatas;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        //globalHolder=holder;

        final GroupListData c = countryList.get(position);
        holder.Group_name.setText(c.getGroupName());
        holder.Group_description.setText(c.getGroupText());

        Glide.with(mContext)
                .load(c.getLogoPath())
                .error(R.drawable.image_not_found)
                .into(holder.Group_Logo);

        // Commenting on 29th Nov 2018 for banner image
       /* Glide.with(mContext)
                .load(c.getImagePath())
                .error(R.drawable.image_not_found)
                .into(holder.Group_Image);*/

        if (c.getMemberStatus().equals("Blocked")) {

            Log.d("RajBhai", "Checking fade effect");
            holder.linearLayoutGroupItemList.setEnabled(false);
            holder.linearLayoutGroupItemList.setAlpha(.5f);

        }


        holder.rippleClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

                String token = sharedPreferences.getString("response", "123");

                String groupId = c.getGroupId().toString();

                Log.d("tokenGRP", " " + token);
                Log.d("groupIdGRP", " " + groupId);

                /*progressDialog.setMessage("Please wait !");
                progressDialog.show();*/

                //addMember(token, groupId, view, c, holder);
                //holder.joinmember.setText("Pending");

                /////////////////////////////////////////////////////////////////////

                //////////////Notification db//////////////////////////
                //db = new NotificationDB(mContext);
                if (db.isTableExists(TABLE_GROUPNOTIFICATION)){

                    Cursor res = db.getParticularGroupNotificationData(groupId);
                    if (res.getCount() > 0) {
                        StringBuffer stringBuffer = new StringBuffer();

//                                while (res.moveToNext()) {
//                                    stringBuffer.append(res.getString(0) + " ");
//                                    stringBuffer.append(res.getString(1) + " ");
//                                    stringBuffer.append(res.getString(2) + " ");
//                                    stringBuffer.append(res.getString(3) + " ");
//                                    stringBuffer.append(res.getString(4) + " ");
//                                    stringBuffer.append(res.getString(5) + " ");
//                                }

                        //Log.d("PGF_Notification","stringBuffer___ "+stringBuffer);

                        boolean isUpdated = db.updateGroupNotificationData(groupId,0,0, System.currentTimeMillis(),true);
                        if (isUpdated == true){
                            Log.d("PGF_Notification","Updated "+isUpdated);
                            holder.relativeLayoutNotification.setVisibility(View.GONE);
                        }else {
                            Log.d("PGF_Notification","NotUpdated "+isUpdated);
                        }
                    }
                    //////////////////////////////////////////////////////
                    else{
                        GroupNotificationModel groupNotificationModel = new GroupNotificationModel();
                        groupNotificationModel.setGroupId(groupId);
                        groupNotificationModel.setGroupNotificationCount(0);
                        groupNotificationModel.setGroupRecentCommentTimeStamp(0);
                        groupNotificationModel.setGroupRecentOpenCommentTimeStamp(System.currentTimeMillis());
                        groupNotificationModel.setGroupOpen(true);

                        groupNotificationModel.setAppCid(null);
                        groupNotificationModel.setClientId(c.getClientId());

                        boolean isInserted = db.insertGroupNotificationData(groupNotificationModel);
                        if (isInserted == true) {


                            Log.d("PGF_Notification", "Notification Insert : " + isInserted);

                        } else {

                            Log.d("PGF_Notification", "Notification Insert : " + isInserted);

                        }

                    }
                    ///////////////////////////////////////////////////////

//                            boolean isUpdated = db.updateGroupNotificationData(groupListData.getGroupId(),0,0, System.currentTimeMillis(),true);
//                            if (isUpdated == true){
//                                Log.d("PGF_Notification","Updated "+isUpdated);
//                                holder.relativeLayoutNotification.setVisibility(View.GONE);
//                            }else {
//                                Log.d("PGF_Notification","NotUpdated "+isUpdated);
//                            }



                }




                popup = "Groups";
                Intent intent = new Intent(view.getContext(), Proceed_Group_FullScreen.class);
                intent.putExtra("popup", popup);
                intent.putExtra("groupName", c.getGroupName());
                intent.putExtra("groupText", c.getGroupText());
                intent.putExtra("groupImage", c.getImagePath());
                intent.putExtra("groupLogo", c.getLogoPath());
                intent.putExtra("groupId", c.getGroupId());
                intent.putExtra("memberGroupRole", c.getMemberGroupRole());

                Log.d("MainGRPADA","isClicked_ "+isClicked);

                if (!isClicked) {

                    isClicked = !isClicked;

                    view.getContext().startActivity(intent);

                }
        /////////////////////////////////////////////////////////////////////////////////////////////


            }
        });


        holder.joinmember.setText(c.getMemberStatus());
        if (c.getMemberStatus().equals("Approved")) {
            holder.joinmember.setVisibility(View.GONE);
        }

/*        holder.joinmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

                String token = sharedPreferences.getString("response", "123");

                String groupId = c.getGroupId().toString();

                Log.d("tokenGRP", " " + token);
                Log.d("groupIdGRP", " " + groupId);

                addMember(token, groupId, v, c, holder);

            }
        });*/

        SharedPreferences sharedPreferencesRole = mContext.getSharedPreferences(UIN.SHARED_PREF_FOR_CHECK_USER, Context.MODE_PRIVATE);

        String roleValue = sharedPreferencesRole.getString("USER_ROLE", "abc");

        if ((roleValue.equals("1") || c.getMemberGroupRole().equals("2")) && c.getMemberStatus().equals("Approved")) {
            Log.d(roleValue, "  "+c.getMemberGroupRole()+" >>>>>>>>>>>>> ");
            holder.imgPopUpMenu.setVisibility(View.VISIBLE);
        }

        holder.imgPopUpMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences1 = mContext.getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

                userAccessToken = sharedPreferences1.getString("response", "123");

                final String groupId = c.getGroupId();
                final String groupText = c.getGroupText();

//                Toast.makeText(mContext,groupId,Toast.LENGTH_SHORT).show();
//                Toast.makeText(mContext,groupText,Toast.LENGTH_SHORT).show();

                Log.d("MainFeedAdapter1","groupIdIs: "+groupId);

                PopupMenu menu = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    menu = new PopupMenu(mContext,view,Gravity.CENTER);
                }

                menu.getMenu().add("Delete");
                menu.getMenu().add("Add Member");
                //menu.getMenu().add("Pending List");

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (menuItem.getTitle().equals("Delete")){

                            /*progressDialog.setMessage("Please wait !");
                            progressDialog.show();*/

                            if (isNetworkAvailable()) {

                                deleteGroupDialog(groupId,holder.getAdapterPosition());

                               // deleteGroupData(groupId, userAccessToken, holder.getAdapterPosition());
                            }else {
                                Toast.makeText(mContext,"Please check internet connection and try again. Thank you.",Toast.LENGTH_SHORT).show();
                            }
                        }

                        if (menuItem.getTitle().equals("Add Member")){
                            Intent intent = new Intent(mContext,AddMemberTo_A_Group.class);
                            intent.putExtra("groupId",groupId);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mContext.startActivity(intent);
                        }


                        /*if (menuItem.getTitle().equals("Pending List")){
                            Intent intent = new Intent(mContext,PendingListOfUsers.class);
                            intent.putExtra("groupId",groupId);
                            mContext.startActivity(intent);
                        }*/

                        return true;
                    }
                });

                menu.show();

            }
        });


        db= new NotificationDB(mContext);
        if (db.isTableExists(TABLE_FEEDNOTIFICATION)) {

            Cursor resCount = db.getParticularPrivateFeedNotificationData(c.getGroupId());
            if (resCount.getCount() > 0) {
                StringBuffer stringBuffer = new StringBuffer();

                while (resCount.moveToNext()) {
                    stringBuffer.append(resCount.getString(0) + " ");
                    stringBuffer.append(resCount.getString(1) + " ");
                    stringBuffer.append(resCount.getInt(2) + " ");
                    stringBuffer.append(resCount.getString(3) + " ");

                    notificationCount += resCount.getInt(2);
//                    break;

                }

                Log.d("PGF_Notification", "ForPrivateFeedStringBuffer___ " + stringBuffer);
                Log.d("PGF_Notification", "notificationCount " + notificationCount);



            }
        }



        Cursor res = db.getParticularGroupNotificationData(c.getGroupId());
        if (res.getCount() > 0) {
            StringBuffer stringBuffer = new StringBuffer();

            int commentCounter = 0;
            while (res.moveToNext()) {
                stringBuffer.append(res.getString(0) + " ");
                stringBuffer.append(res.getString(1) + " ");
                stringBuffer.append(res.getString(2) + " ");
                commentCounter = Integer.parseInt(res.getString(2))+notificationCount;
                stringBuffer.append(res.getString(3) + " ");
                stringBuffer.append(res.getString(4) + " ");
                stringBuffer.append(res.getString(5) + " ");
            }

            Log.d("MainGrpAdapt","Notification "+stringBuffer);

            if(commentCounter > 0) {
                holder.relativeLayoutNotification.setVisibility(View.VISIBLE);

                if (commentCounter <= 9) {
                    holder.notificationText.setText(String.valueOf(commentCounter));
                }else {
                    holder.notificationText.setText(String.valueOf("9+"));
                }
            }


        }




        /*//////////////////////////Broadcast reciever for UI update/////////////////////////////

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String groupId = intent.getStringExtra("GROUP_ID");

                Log.d("MGA_GRP","broadcastVal__"+groupId);

                if (intent.getAction().equals(ConstantsForUIUpdates.GROUP_UI)){

                    db= new NotificationDB(mContext);
                    Cursor res = db.getParticularGroupNotificationData(c.getGroupId());
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
                        }

                        Log.d("MainGrpAdapt","Notification "+stringBuffer);

                        if(commentCounter > 0) {
                            holder.relativeLayoutNotification.setVisibility(View.VISIBLE);

                            if (commentCounter <= 9) {
                                holder.notificationText.setText(String.valueOf(commentCounter));
                            }else {
                                holder.notificationText.setText(String.valueOf("9+"));
                            }
                        }


                    }


                }


            }
        };

        LocalBroadcastManager.getInstance(mContext).registerReceiver(broadcastReceiver,new IntentFilter(ConstantsForUIUpdates.GROUP_UI));

*/











    }


    /*END Method to change data when put query in searchBar*/

    @Override
    public int getItemCount() {
        return countryList.size();
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
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_item_list, parent, false);
        return new MyViewHolder(v);
    }

    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView Group_name;
        public TextView Group_description;
        public ImageView Group_Logo;
        public ImageView Group_Image;
        public TextView joinmember;
        public LinearLayout linearLayoutGroupItemList;
        MaterialRippleLayout rippleClick;
        ImageView imgPopUpMenu;

        RelativeLayout relativeLayoutNotification;
        TextView notificationText;

        public MyViewHolder(View view) {
            super(view);

            Group_name = (TextView) view.findViewById(R.id.Group_name);
            Group_description = (TextView) view.findViewById(R.id.Group_description);
            joinmember = (TextView) view.findViewById(R.id.joinmember);
            Group_Logo = (ImageView) view.findViewById(R.id.Group_Logo);
            //Group_Image = (ImageView) view.findViewById(R.id.Group_Image);
            linearLayoutGroupItemList = (LinearLayout) view.findViewById(R.id.linearLayoutGroupItemList);
            rippleClick = (MaterialRippleLayout) view.findViewById(R.id.rippleClick);
            imgPopUpMenu = (ImageView)view.findViewById(R.id.imgPopUpMenu);

            relativeLayoutNotification = (RelativeLayout) view.findViewById(R.id.relativeLayoutNotification);
            notificationText = (TextView) view.findViewById(R.id.notificationText);

            //progressDialog = new ProgressDialog(mContext,R.style.DialogTheme);

        }



    }


    private void deleteGroupData(String groupId, String userAccessToken,final int pos) {

        String tag_string_req = "req_clients";

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_deleteGroup+ "?groupId=" + groupId +"&admin="+userAccessToken;

        Log.d("MainFeedAdapter1", "URL-->" + url);

        StringRequest request = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {

                Log.d("MainFeedAdapter1", "-->" + response1);
                //parsing Json
                JSONObject jsonObject = null;

                /*if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }*/

                try {

                    jsonObject = new JSONObject(response1);
                    String response = jsonObject.getString("response");
                    String status = jsonObject.getString("status");
                    Log.d("MainFeedAdapter1Resp", "" + response);
                    Log.d("MainFeedAdapter1Status", "" + status);

                    if (status.equals("success")){
                        Log.d("MainFeedAdapter1","InIfPart");

                        /*String clientImageURLPath = FullViewOfClientsProceed.clientImageURLPath;

                        Log.d("MainFeedAdapter1","clientImageURLPath "+clientImageURLPath);

                        Intent intent = new Intent(mContext,FullViewOfClientsProceed.class);
                        intent.putExtra("clientImagePath",clientImageURLPath);
                        intent.putExtra("FRAGMENT_ID","1");
                        mContext.startActivity(intent);*/

                        countryList.remove(pos);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, getItemCount() - pos);

                        //Update UI When No data found
                        if (getItemCount() == 0){
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(GROUP_ARRAYLIST));
                        }


                    }else {
                        Log.d("MainFeedAdapter1","InElsePart");
                        Toast.makeText(mContext,response,Toast.LENGTH_SHORT).show();
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
                    android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(mContext, R.style.MyDialogTheme).create();

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


    public void addMember(final String token, final String groupId, final View view, final GroupListData groupListData, final MyViewHolder holder) {

// Tag used to cancel the request
        String tag_string_req = "req_register";

        //pDialog.setMessage("Registering ...");
        //showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_ADD_Member + "token=" + token + "&" + "groupId=" + groupId, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {

//                hideDialog();

                /*if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }*/

                try {
                    JSONObject jObj = new JSONObject(response1);

                    final String response = jObj.getString("response");
                    ////////16th  feb////////response==Member added successfully
                    Log.d("MemberResponse", " " + response);

                    String status = jObj.getString("status");

                    if (status != null && status.equals("success")) {

                        final AlertDialog alertDialog1 = new AlertDialog.Builder(view.getContext(), R.style.MyDialogTheme).create();
                        // Setting Dialog Title
                        alertDialog1.setTitle("Group Member");

                        // Setting Dialog Message
                        alertDialog1.setMessage("Hey,Your request sent for approval ");

                        alertDialog1.setCanceledOnTouchOutside(false);

                        // Setting OK Button
                        alertDialog1.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog closed

                                holder.joinmember.setText("Pending");
//                                Intent intent = new Intent(view.getContext(),Proceed_Group_FullScreen.class);
//                                intent.putExtra("groupName",groupListData.getGroupName());
//                                intent.putExtra("groupText",groupListData.getGroupText());
//                                intent.putExtra("groupImage",groupListData.getImagePath());
//                                intent.putExtra("groupId",groupListData.getGroupId());
//                                view.getContext().startActivity(intent);

                            }
                        });

                        // Showing Alert Message
                        alertDialog1.show();


                    } else if (response.equals("User already a member of the client with Status Approved")) {
                        //joinmember.setVisibility(View.GONE);


                        //////////////Notification db//////////////////////////
                        //db = new NotificationDB(mContext);
                        if (db.isTableExists(TABLE_GROUPNOTIFICATION)){

                            Cursor res = db.getParticularGroupNotificationData(groupListData.getGroupId());
                            if (res.getCount() > 0) {
                                StringBuffer stringBuffer = new StringBuffer();

//                                while (res.moveToNext()) {
//                                    stringBuffer.append(res.getString(0) + " ");
//                                    stringBuffer.append(res.getString(1) + " ");
//                                    stringBuffer.append(res.getString(2) + " ");
//                                    stringBuffer.append(res.getString(3) + " ");
//                                    stringBuffer.append(res.getString(4) + " ");
//                                    stringBuffer.append(res.getString(5) + " ");
//                                }

                                //Log.d("PGF_Notification","stringBuffer___ "+stringBuffer);

                                boolean isUpdated = db.updateGroupNotificationData(groupListData.getGroupId(),0,0, System.currentTimeMillis(),true);
                                if (isUpdated == true){
                                    Log.d("PGF_Notification","Updated "+isUpdated);
                                    holder.relativeLayoutNotification.setVisibility(View.GONE);
                                }else {
                                    Log.d("PGF_Notification","NotUpdated "+isUpdated);
                                }
                            }
                            //////////////////////////////////////////////////////
                            else{
                                GroupNotificationModel groupNotificationModel = new GroupNotificationModel();
                                groupNotificationModel.setGroupId(groupListData.getGroupId());
                                groupNotificationModel.setGroupNotificationCount(0);
                                groupNotificationModel.setGroupRecentCommentTimeStamp(0);
                                groupNotificationModel.setGroupRecentOpenCommentTimeStamp(System.currentTimeMillis());
                                groupNotificationModel.setGroupOpen(true);

                                groupNotificationModel.setAppCid(null);
                                groupNotificationModel.setClientId(groupListData.getClientId());


                                boolean isInserted = db.insertGroupNotificationData(groupNotificationModel);
                                if (isInserted == true) {


                                    Log.d("PGF_Notification", "Notification Insert : " + isInserted);

                                } else {

                                    Log.d("PGF_Notification", "Notification Insert : " + isInserted);

                                }

                            }
                            ///////////////////////////////////////////////////////

//                            boolean isUpdated = db.updateGroupNotificationData(groupListData.getGroupId(),0,0, System.currentTimeMillis(),true);
//                            if (isUpdated == true){
//                                Log.d("PGF_Notification","Updated "+isUpdated);
//                                holder.relativeLayoutNotification.setVisibility(View.GONE);
//                            }else {
//                                Log.d("PGF_Notification","NotUpdated "+isUpdated);
//                            }



                        }




                        popup = "Groups";
                        Intent intent = new Intent(view.getContext(), Proceed_Group_FullScreen.class);
                        intent.putExtra("popup", popup);
                        intent.putExtra("groupName", groupListData.getGroupName());
                        intent.putExtra("groupText", groupListData.getGroupText());
                        intent.putExtra("groupImage", groupListData.getImagePath());
                        intent.putExtra("groupLogo", groupListData.getLogoPath());
                        intent.putExtra("groupId", groupListData.getGroupId());
                        intent.putExtra("memberGroupRole", groupListData.getMemberGroupRole());

                        Log.d("MainGRPADA","isClicked_ "+isClicked);

                        if (!isClicked) {

                            isClicked = !isClicked;

                            view.getContext().startActivity(intent);

                        }


                    } else if (response.equals("User already a member of the client with Status Blocked")) {
                        AlertDialog alertDialog = new AlertDialog.Builder(view.getContext(), R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("");

                        // Setting Dialog Message
                        alertDialog.setMessage("Hey,looks like you are not Authorised to enter this group. Sorry!!!");

                        // Setting Icon to Dialog
                        //  alertDialog.setIcon(R.drawable.tick);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();
                    } else if (response.equals("User already a member of the client with Status Pending")) {
                        AlertDialog alertDialog = new AlertDialog.Builder(view.getContext(), R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("");

                        // Setting Dialog Message
                        alertDialog.setMessage("Hey,Your request is pending.Wait for approval.");

                        // Setting Icon to Dialog
                        //  alertDialog.setIcon(R.drawable.tick);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();
                    }

                    ///////////////////////////////////////////////
                    // Write your code here to execute after dialog closed
//                                Intent intent = new Intent(view.getContext(),Proceed_Group_FullScreen.class);
//                                intent.putExtra("groupName",groupListData.getGroupName());
//                                intent.putExtra("groupText",groupListData.getGroupText());
//                                intent.putExtra("groupImage",groupListData.getImagePath());
//                                intent.putExtra("groupId",groupListData.getGroupId());
//                                view.getContext().startActivity(intent);


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                /*if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }*/

                if (error.getMessage() == null || error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(view.getContext(), R.style.MyDialogTheme).create();

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
        }); /*{

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", token);
                params.put("groupId", groupId);



                return params;
            }

        };*/

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    public void statusGroup(final String clientId, final String token) {
        StringRequest stringRequest = new
                StringRequest(Request.Method.GET, AppConfig.Base_Url + AppConfig.App_api + "/memberStatusWithGroup?clientId=" + clientId + "&token=" + token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("StatusActiveornot", "" + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest);

    }



/*

    public void addMember(final String token, final String groupId, final View view, final GroupListData groupListData, final MyViewHolder holder) {

// Tag used to cancel the request
        String tag_string_req = "req_register";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_ADD_Member + "token=" + token + "&" + "groupId=" + groupId, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {

//                hideDialog();

                */
/*if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }*//*


                try {
                    JSONObject jObj = new JSONObject(response1);

                    final String response = jObj.getString("response");
                    ////////16th  feb////////response==Member added successfully
                    Log.d("MemberResponse", " " + response);

                    String status = jObj.getString("status");

              if (status != null && response.equals("User already a member of the client with Status Approved")) {
                        //joinmember.setVisibility(View.GONE);


                        //////////////Notification db//////////////////////////
                        //db = new NotificationDB(mContext);
                        if (db.isTableExists(TABLE_GROUPNOTIFICATION)){

                            Cursor res = db.getParticularGroupNotificationData(groupListData.getGroupId());
                            if (res.getCount() > 0) {
                                StringBuffer stringBuffer = new StringBuffer();

//                                while (res.moveToNext()) {
//                                    stringBuffer.append(res.getString(0) + " ");
//                                    stringBuffer.append(res.getString(1) + " ");
//                                    stringBuffer.append(res.getString(2) + " ");
//                                    stringBuffer.append(res.getString(3) + " ");
//                                    stringBuffer.append(res.getString(4) + " ");
//                                    stringBuffer.append(res.getString(5) + " ");
//                                }

                                //Log.d("PGF_Notification","stringBuffer___ "+stringBuffer);

                                boolean isUpdated = db.updateGroupNotificationData(groupListData.getGroupId(),0,0, System.currentTimeMillis(),true);
                                if (isUpdated == true){
                                    Log.d("PGF_Notification","Updated "+isUpdated);
                                    holder.relativeLayoutNotification.setVisibility(View.GONE);
                                }else {
                                    Log.d("PGF_Notification","NotUpdated "+isUpdated);
                                }
                            }
                            //////////////////////////////////////////////////////
                            else{
                                GroupNotificationModel groupNotificationModel = new GroupNotificationModel();
                                groupNotificationModel.setGroupId(groupListData.getGroupId());
                                groupNotificationModel.setGroupNotificationCount(0);
                                groupNotificationModel.setGroupRecentCommentTimeStamp(0);
                                groupNotificationModel.setGroupRecentOpenCommentTimeStamp(System.currentTimeMillis());
                                groupNotificationModel.setGroupOpen(true);

                                boolean isInserted = db.insertGroupNotificationData(groupNotificationModel);
                                if (isInserted == true) {


                                    Log.d("PGF_Notification", "Notification Insert : " + isInserted);

                                } else {

                                    Log.d("PGF_Notification", "Notification Insert : " + isInserted);

                                }

                            }


                        }




                        popup = "Groups";
                        Intent intent = new Intent(view.getContext(), Proceed_Group_FullScreen.class);
                        intent.putExtra("popup", popup);
                        intent.putExtra("groupName", groupListData.getGroupName());
                        intent.putExtra("groupText", groupListData.getGroupText());
                        intent.putExtra("groupImage", groupListData.getImagePath());
                        intent.putExtra("groupLogo", groupListData.getLogoPath());
                        intent.putExtra("groupId", groupListData.getGroupId());
                        intent.putExtra("memberGroupRole", groupListData.getMemberGroupRole());

                        Log.d("MainGRPADA","isClicked_ "+isClicked);

                        if (!isClicked) {

                            isClicked = !isClicked;

                            view.getContext().startActivity(intent);

                        }


                    } else {


                  AlertDialog alertDialog = new AlertDialog.Builder(view.getContext(), R.style.MyDialogTheme).create();

                  // Setting Dialog Title
                  alertDialog.setTitle("Groups");

                  // Setting Dialog Message
                  alertDialog.setMessage(response);

                  // Setting Icon to Dialog
                  //  alertDialog.setIcon(R.drawable.tick);

                  // Setting OK Button
                  alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int which) {

                      }
                  });

                  // Showing Alert Message
                  alertDialog.show();


              }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                */
/*if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }*//*


                if (error.getMessage() == null || error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(view.getContext(), R.style.MyDialogTheme).create();

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
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
*/


    public void deleteGroupDialog(final String groupId,final int position){

        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(mContext);
        alertDialogBuilder.setMessage("Do you want to delete this group ?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteGroupData(groupId, userAccessToken,position);


                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



}
