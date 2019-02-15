package in.sabpaisa.droid.sabpaisa.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
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

import in.sabpaisa.droid.sabpaisa.AddMemberTo_A_SpaceGroup;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.GroupListData;
import in.sabpaisa.droid.sabpaisa.GroupSpaceCommentActivity;
import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.UIN;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;

import static in.sabpaisa.droid.sabpaisa.ConstantsForUIUpdates.GROUP_ARRAYLIST;

public class SkipGroupFragmentAdapter extends RecyclerView.Adapter<SkipGroupFragmentAdapter.MyViewHolder> {

    Context mContext;
    private List<GroupListData> countryList;
    public Button joinmember;

    String popup = "Group";

    String userAccessToken;



// test //

    public SkipGroupFragmentAdapter() {
    }

    public SkipGroupFragmentAdapter(List<GroupListData> countryList, Context context) {
        this.countryList = countryList;
        this.mContext = context;
    }

    /*START Method to change data when put query in searchBar*/
    /*public void setItems(List<GroupListData> groupDatas) {
        this.countryList = groupDatas;
    }*/

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final GroupListData c = countryList.get(position);
        holder.Group_name.setText(c.getGroupName());
        holder.Group_description.setText(c.getGroupText());

        Glide.with(mContext)
                .load(c.getLogoPath())
                .error(R.drawable.image_not_found)
                .into(holder.Group_Logo);



        holder.rippleClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent = new Intent(view.getContext(), GroupSpaceCommentActivity.class);
                intent.putExtra("groupName", c.getGroupName());
                intent.putExtra("groupText", c.getGroupText());
                intent.putExtra("groupImage", c.getImagePath());
                intent.putExtra("groupLogo", c.getLogoPath());
                intent.putExtra("groupId", c.getGroupId());
                intent.putExtra("memberGroupRole", c.getMemberGroupRole());
                view.getContext().startActivity(intent);



            }
        });


        holder.joinmember.setText(c.getMemberStatus());
        if (c.getMemberStatus().equals("Approved")) {
            holder.joinmember.setVisibility(View.GONE);
        }

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



                Log.d("MainFeedAdapter1","groupIdIs: "+groupId);

                PopupMenu menu = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    menu = new PopupMenu(mContext,view, Gravity.CENTER);
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

                                deleteGroupData(groupId, userAccessToken, holder.getAdapterPosition());
                            }else {
                                Toast.makeText(mContext,"Please check internet connection and try again. Thank you.",Toast.LENGTH_SHORT).show();
                            }
                        }

                        if (menuItem.getTitle().equals("Add Member")){

                            //TODO Add member
                            Intent intent = new Intent(mContext, AddMemberTo_A_SpaceGroup.class);
                            intent.putExtra("groupId",groupId);
                            mContext.startActivity(intent);
                        }



                        return true;
                    }
                });

                menu.show();

            }
        });



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
                .inflate(R.layout.custom_skip_group_fragment, parent, false);
        return new MyViewHolder(v);
    }


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





    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



}
