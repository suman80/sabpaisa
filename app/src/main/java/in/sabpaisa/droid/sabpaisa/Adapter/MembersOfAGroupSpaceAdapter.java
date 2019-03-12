package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.MembersOfAGroupSpace;
import in.sabpaisa.droid.sabpaisa.MembersProfile;
import in.sabpaisa.droid.sabpaisa.Model.MemberSpaceModel;
import in.sabpaisa.droid.sabpaisa.Model.Member_GetterSetter;
import in.sabpaisa.droid.sabpaisa.NumberOfGroups;
import in.sabpaisa.droid.sabpaisa.PrivateFeedMembersList;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.UIN;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.NoOfGroupmemberAdapter;

import static in.sabpaisa.droid.sabpaisa.MainActivitySkip.SUPER_ADMIN_SHAREDFREF;

public class MembersOfAGroupSpaceAdapter extends RecyclerView.Adapter<MembersOfAGroupSpaceAdapter.MyViewHolder> {
    int count;
    ArrayList<Member_GetterSetter> memberGetterSetterArrayList;
    Context mContext;
    String name, image, mobNo;
    String useracesstoken, response, x, mobNumber;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    String userAccessToken;

    int countforAdmin = 0;

    String roleValue;

    public MembersOfAGroupSpaceAdapter(ArrayList<Member_GetterSetter> memberGetterSetterArrayList, Context context) {
        this.memberGetterSetterArrayList = memberGetterSetterArrayList;
        this.mContext = context;
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_custom, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {

        final Member_GetterSetter member_getterSetter = memberGetterSetterArrayList.get(i);

//        if (member_getterSetter.getUin_Role().equals("1")||member_getterSetter.getRoleId().equals("2")){
//            countforAdmin++;
//        }

        myViewHolder.memberName.setText(member_getterSetter.getFullName());

        name = member_getterSetter.getFullName();

        mobNo = member_getterSetter.getCONTACT_NUMBER();

        image = member_getterSetter.getUserImageUrl();


        Glide.with(mContext)
                .load(member_getterSetter.getUserImageUrl())
                .error(R.drawable.default_users)
                .into(myViewHolder.memberImg);

        SharedPreferences sharedPreferencesRole = mContext.getSharedPreferences(SUPER_ADMIN_SHAREDFREF, Context.MODE_PRIVATE);

        roleValue = sharedPreferencesRole.getString("ROLE_VALUE", "abc");

        Log.d("MOGSA","roleValue___"+roleValue);

        SharedPreferences sharedPreferences1 = mContext.getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        userAccessToken = sharedPreferences1.getString("response", "123");

        if (userAccessToken.equals(member_getterSetter.getUserAccessToken())){
            myViewHolder.memberName.setText("You");

        }


        if (roleValue.equals("1") && userAccessToken.equals(member_getterSetter.getUserAccessToken())){
            myViewHolder.textViewAdmin.setVisibility(View.VISIBLE);

            myViewHolder.textViewAdmin.setText("Admin");
        }else if ( !(member_getterSetter.getRoleId() == null || member_getterSetter.getRoleId().equals("null"))
                && member_getterSetter.getRoleId().equals("2")){
            myViewHolder.textViewAdmin.setVisibility(View.VISIBLE);

            myViewHolder.textViewAdmin.setText(member_getterSetter.getRoleName());
        }else{
            myViewHolder.textViewAdmin.setVisibility(View.GONE);

        }


        if (( roleValue.equals("1")) || (MembersOfAGroupSpace.memberGroupRole !=null && MembersOfAGroupSpace.memberGroupRole.equals("2"))) {

            myViewHolder.imgPopUpMenu.setVisibility(View.VISIBLE);
        }


        myViewHolder.imgPopUpMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu menu = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    menu = new PopupMenu(mContext,view, Gravity.CENTER);
                }

                menu.getMenu().add("Remove");

                Log.d("MOAGSA","RoleId__"+member_getterSetter.getRoleId());

                if(member_getterSetter.getRoleId() ==null || member_getterSetter.getRoleId().equals("null") || !member_getterSetter.getRoleId().equals("2"))
                {
                    if (/*!roleValue.equals("1")||*/!(userAccessToken.equals(member_getterSetter.getUserAccessToken()))){
                        menu.getMenu().add("Make a group admin");
                    }
                }




                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (menuItem.getTitle().equals("Remove")) {

                            if (isNetworkAvailable()) {

                                removeUserfromGroup(userAccessToken, member_getterSetter.getUserAccessToken(), member_getterSetter.getGroupId(), myViewHolder.getAdapterPosition());

                            }else {

                                Toast.makeText(mContext,"Please check internet connection and try again. Thank you.",Toast.LENGTH_SHORT).show();

                            }

                            }


                            if (menuItem.getTitle().equals("Make a group admin")){

                            }



                        return true;
                    }
                });

                menu.show();
            }
        });



    }





    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView memberImg;
        TextView memberName, memberTimeStamp,textViewAdmin;
        ImageView imgPopUpMenu;

        public MyViewHolder(View itemView) {
            super(itemView);


            memberImg = (ImageView) itemView.findViewById(R.id.memberImg);
            memberName = (TextView) itemView.findViewById(R.id.memberName);
            imgPopUpMenu = (ImageView) itemView.findViewById(R.id.imgPopUpMenu);
            textViewAdmin = (TextView)itemView.findViewById(R.id.textViewAdmin);


        }
    }

    @Override
    public int getItemCount() {
        return memberGetterSetterArrayList.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public void removeUserfromGroup(final String userAccessToken , final String token , final String groupId,final int pos){

        String tag_string_req = "req_clients";

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_removeUserfromGroup+ "?token=" + token +"&admin="+userAccessToken +"&groupId="+groupId;

        Log.d("MOAGSA", "URL-->" + url);

        StringRequest request = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {

                Log.d("MOAGSA", "-->" + response1);
                //parsing Json
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response1);
                    String response = jsonObject.getString("response");
                    String status = jsonObject.getString("status");
                    Log.d("MOAGSA", "Response__" + response);
                    Log.d("MOAGSA", "Status__" + status);

                    if (status.equals("success")){
                        Log.d("MOAGSA","InIfPart");
                        countforAdmin--;

                        memberGetterSetterArrayList.remove(pos);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, getItemCount() - pos);

                    }else {
                        Log.d("MOAGSA","InElsePart");
                        Toast.makeText(mContext,response,Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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


    public void makeGroupAdmin(String userAccessToken , String userId , final String groupId){

        String tag_string_req = "req_clients";

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_groupAdmin+ "?userId=" + userId +"&userToken="+userAccessToken +"&group_Id="+groupId;

        Log.d("MOAGSA", "URL-->" + url);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {

                Log.d("MOAGSA", "-->" + response1);
                //parsing Json
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response1);
                    String response = jsonObject.getString("response");
                    String status = jsonObject.getString("status");
                    Log.d("MOAGSAResp", "" + response);
                    Log.d("MOAGSAStatus", "" + status);

                    if (status.equals("success")){
                        Log.d("MOAGSA","InIfPart");
                        Intent intent = new Intent(mContext, MembersOfAGroupSpace.class);
                        intent.putExtra("GroupId",groupId);
                        intent.putExtra("memberGroupRole","");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);

                    }else {
                        Log.d("MOAGSA","InElsePart");
                        Toast.makeText(mContext,response,Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

