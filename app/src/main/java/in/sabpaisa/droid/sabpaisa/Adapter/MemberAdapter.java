package in.sabpaisa.droid.sabpaisa.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.balysv.materialripple.MaterialRippleLayout;
import com.braunster.chatsdk.activities.ChatSDKLoginActivity;
import com.braunster.chatsdk.activities.ChatSDKMainActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sabpaisa.droid.sabpaisa.AddMemberTo_A_Group;
import in.sabpaisa.droid.sabpaisa.AppController;

import in.sabpaisa.droid.sabpaisa.GroupListData;
import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.Members;
import in.sabpaisa.droid.sabpaisa.MembersProfile;
import in.sabpaisa.droid.sabpaisa.Model.Member_GetterSetter;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.TLSSocketFactory;
import in.sabpaisa.droid.sabpaisa.UIN;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;
import me.grantland.widget.AutofitTextView;

/**
 * Created by rajdeeps on 12/28/17.
 */

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MyViewHolder> {
    int count;
    ArrayList<Member_GetterSetter> memberGetterSetterArrayList;
    Context mContext;
    String x, name, mobNumber;
    String useracesstoken;
    RequestQueue requestQueue;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public static boolean isClicked=false;

    String userAccessToken,clientId;

    ProgressDialog progressDialog;

    @Override
    public MemberAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_custom, parent, false);
        return new MemberAdapter.MyViewHolder(v);
    }


    public MemberAdapter(ArrayList<Member_GetterSetter> memberGetterSetterArrayList, Context context) {
        this.memberGetterSetterArrayList = memberGetterSetterArrayList;
        this.mContext = context;
    }


    /*START Method to change data when put query in searchBar*/
    public void setItems(ArrayList<Member_GetterSetter> memberDatas) {
        this.memberGetterSetterArrayList = memberDatas;
    }


    @Override
    public void onBindViewHolder(final MemberAdapter.MyViewHolder holder, final int position) {

        final Member_GetterSetter member_getterSetter = memberGetterSetterArrayList.get(position);
        holder.memberName.setText(member_getterSetter.getFullName());
        //holder.memberTimeStamp.setText(member_getterSetter.getTimestampOfJoining());
        //holder.memberImg.setImageUrl(member_getterSetter.getUserImageUrl(), imageLoader);
        Glide.with(mContext)
                .load(member_getterSetter.getUserImageUrl())
                .error(R.drawable.default_users)
                .into(holder.memberImg);



        holder.rippleClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Log.d("POsitikon-", "" + memberGetterSetterArrayList.get(position).getFullName().toString());
                Log.d("POsitikon-", "" + memberGetterSetterArrayList.get(position).getUserImageUrl().toString());
                Intent intent = new Intent(mContext, MembersProfile.class);
                intent.putExtra("name1", memberGetterSetterArrayList.get(position).getFullName().toString());
                intent.putExtra("image1", memberGetterSetterArrayList.get(position).getUserImageUrl().toString());
                intent.putExtra("emailid1", memberGetterSetterArrayList.get(position).getEmailId().toString());
                intent.putExtra("mobNo1", memberGetterSetterArrayList.get(position).getPhoneNumber().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if (!isClicked) {
                    isClicked = !isClicked;
                    mContext.startActivity(intent);
                }

            }
        });

        SharedPreferences sharedPreferencesRole = mContext.getSharedPreferences(UIN.SHARED_PREF_FOR_CHECK_USER, Context.MODE_PRIVATE);

        String roleValue = sharedPreferencesRole.getString("USER_ROLE", "abc");

        if (roleValue.equals("1")) {

            holder.imgPopUpMenu.setVisibility(View.VISIBLE);
        }

        Log.d("MemADAPTER","OUT_IF_UIN_ROLE "+member_getterSetter.getUin_Role());
        if (member_getterSetter.getUin_Role() != null && member_getterSetter.getUin_Role().equals("1")){
            Log.d("InsideMemADAPTER","IN_IF_UIN_ROLE "+member_getterSetter.getUin_Role());
            holder.textViewAdmin.setVisibility(View.VISIBLE);
            holder.textViewAdmin.setText("Admin");
            holder.imgPopUpMenu.setVisibility(View.GONE);
        }


        SharedPreferences sharedPreferences1 = mContext.getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        userAccessToken = sharedPreferences1.getString("response", "123");

        if (userAccessToken.equals(member_getterSetter.getUserAccessToken())) {
            holder.memberName.setText("You");
        }

        if (member_getterSetter.getUin_Status().equals("Blocked")){
            holder.rippleClick.setAlpha(.5f);
        }


        holder.imgPopUpMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = mContext.getSharedPreferences(FullViewOfClientsProceed.MySharedPrefOnFullViewOfClientProceed, Context.MODE_PRIVATE);
                clientId=sharedPreferences.getString("clientId","abc");
                Log.d("clientId_MEMBERSAdapter",""+clientId);


                PopupMenu menu = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    menu = new PopupMenu(mContext,view,Gravity.CENTER);
                }

                if(! holder.textViewAdmin.getText().equals("Admin")){

                    if (member_getterSetter.getUin_Status().equals("Blocked")){
                        menu.getMenu().add("UnBlock Member");
                    }
                    if (!member_getterSetter.getUin_Status().equals("Blocked")){
                        menu.getMenu().add("Block Member");
                    }

                //Removing Make Admin on 24th Oct 2018
                //menu.getMenu().add("Make Admin");
                }

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (menuItem.getTitle().equals("Block Member")){

                            progressDialog.setMessage("Please wait !");
                            progressDialog.show();
                            clientMemberStatusUpdate(clientId,userAccessToken,member_getterSetter.getPhoneNumber(),"Blocked");
                        }

                        if (menuItem.getTitle().equals("UnBlock Member")){

                            progressDialog.setMessage("Please wait !");
                            progressDialog.show();
                            clientMemberStatusUpdate(clientId,userAccessToken,member_getterSetter.getPhoneNumber(),"Varified");
                        }

                        /*if (menuItem.getTitle().equals("Make Admin")){
                            adminAccessToMember(clientId,userAccessToken,member_getterSetter.getPhoneNumber());
                        }*/

                        return true;
                    }
                });

                menu.show();

            }
        });



/*
        holder.memberImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               */
/* Intent intent = new Intent(mContext,MembersProfile.class);

                intent.putExtra("",member_getterSetter.getFullName());
                mContext.startActivity(intent);*//*


                Log.d("POsitikon-",""+memberGetterSetterArrayList.get(position).getFullName().toString());
                Log.d("POsitikon-",""+memberGetterSetterArrayList.get(position).getUserImageUrl().toString());
                Intent intent = new Intent(mContext,MembersProfile.class);
                intent.putExtra("name1",memberGetterSetterArrayList.get(position).getFullName().toString());
                intent.putExtra("image1",memberGetterSetterArrayList.get(position).getUserImageUrl().toString());
                intent.putExtra("emailid1",memberGetterSetterArrayList.get(position).getEmailId().toString());
                intent.putExtra("mobNo1",memberGetterSetterArrayList.get(position).getPhoneNumber().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                mContext.startActivity(intent);

            }
        });

        holder.memberName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                */
/*Intent intent = new Intent(mContext,MembersProfile.class);
                mContext.startActivity(intent);
*//*


                Log.d("POsitikon-",""+memberGetterSetterArrayList.get(position).getFullName().toString());
                Log.d("POsitikon-",""+memberGetterSetterArrayList.get(position).getUserImageUrl().toString());
                Intent intent = new Intent(mContext,MembersProfile.class);
                intent.putExtra("name1",memberGetterSetterArrayList.get(position).getFullName().toString());
                intent.putExtra("image1",memberGetterSetterArrayList.get(position).getUserImageUrl().toString());
                intent.putExtra("emailid1",memberGetterSetterArrayList.get(position).getEmailId().toString());
                intent.putExtra("mobNo1",memberGetterSetterArrayList.get(position).getPhoneNumber().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                mContext.startActivity(intent);

            }
        });
*/


        /*holder.memberChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //21st March,2018
                SharedPreferences sharedPreferences = mContext.getSharedPreferences(FullViewOfClientsProceed.MySharedPrefOnFullViewOfClientProceed, Context.MODE_PRIVATE);
                String clientId=sharedPreferences.getString("clientId","abc");
                String clientName=sharedPreferences.getString("clientName","abc");
                String state=sharedPreferences.getString("state","abc");
                String clientImageURLPath=sharedPreferences.getString("clientImageURLPath","abc");

                int value=3;
                Intent intent = new Intent(mContext,ChatSDKLoginActivity.class);
                intent.putExtra("VALUE",value);

                // Start 10-april-2018            /////


                intent.putExtra("usernameniv",name);
               // intent.putExtra("userImageUrlMaim",memberGetterSetterArrayList.get(position).getUserImageUrl().toString());
                intent.putExtra("xxxxx",x);
                intent.putExtra("mobNumber",mobNumber);

 // End   10 April 2018
                intent.putExtra("CLIENTID",clientId);
                intent.putExtra("CLIENTNAME",clientName);
                intent.putExtra("STATE",state);
                intent.putExtra("CLIENTIMG",clientImageURLPath);
                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ////21st March,2018
            }
        });
*/
    }


    public class MyViewHolder extends RecyclerView.ViewHolder /*implements View.OnCreateContextMenuListener*/{

        public ImageView memberImg;
        Button memberChat;
        public AutofitTextView memberName;
        TextView memberTimeStamp;
        String response, userAccessToken;

        MaterialRippleLayout rippleClick;

        ImageView imgPopUpMenu;

        TextView textViewAdmin;

        public MyViewHolder(View itemView) {
            super(itemView);
            memberImg = (ImageView) itemView.findViewById(R.id.memberImg);
            memberName = (AutofitTextView) itemView.findViewById(R.id.memberName);
            //memberChat = (Button) itemView.findViewById(R.id.groupmmbrchat);
            //memberTimeStamp = (TextView)itemView.findViewById(R.id.memberTimeStamp);

            rippleClick = (MaterialRippleLayout) itemView.findViewById(R.id.rippleClick);

            imgPopUpMenu = (ImageView)itemView.findViewById(R.id.imgPopUpMenu);

            textViewAdmin = (TextView)itemView.findViewById(R.id.textViewAdmin);

            SharedPreferences prefs = mContext.getSharedPreferences(LogInActivity.MySharedPrefLogin, 0);
          /*  response = prefs.getString("response", "123");

            userAccessToken = response;
*/

            response = prefs.getString("response", "123");

            useracesstoken = response;

            Log.d("MemeberrAdapter", " " + response);
            Log.d("MemeberrAdapter", " " + useracesstoken);

            Log.d("MemeberrAdapter", " " + response);


            showProfileData();

            this.setIsRecyclable(false);

//            String test = "test";
////            if(test.isEmpty())
//            itemView.setOnCreateContextMenuListener(this);

            progressDialog = new ProgressDialog(mContext,R.style.DialogTheme);


        }

        /*@Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            Log.d("onCreateContextMenu","Member");

            contextMenu.setHeaderTitle("Select The Action");
            contextMenu.add(this.getAdapterPosition(), view.getId(), 0, "Delete Member");//groupId, itemId, order, title



        }*/

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

    private void showProfileData() {

        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_Show_UserProfile + "?token=" + useracesstoken, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                Log.d("SKipusernamenav", "Register Response: " + response1.toString());

                try {
                    //progressBar.setVisibility(View.GONE);
                    JSONObject object = new JSONObject(response1);
                    String response = object.getString("response");
                    String status = object.getString("status");
                    x = object.getJSONObject("response").getString("emailId").toString();
                    name = object.getJSONObject("response").getString("fullName").toString();

                    mobNumber = object.getJSONObject("response").getString("contactNumber").toString();

                    if (x.equals("null")) {
                        x = "";
                        // usernameniv.setText(object.getJSONObject("response").getString("fullName").toString());
                        //mailIdniv.setText("");
                    } else if (status.equals("success")) {
                        name = object.getJSONObject("response").getString("fullName").toString();
                        x = object.getJSONObject("response").getString("emailId").toString();

                        // usernameniv.setText(object.getJSONObject("response").getString("fullName").toString());
                        //mNumber.setText(object.getJSONObject("response").getString("contactNumber").toString());

                        // mailIdniv.setText(x);
                        //mailIdniv.setText(object.getJSONObject("response").getString("emailId").toString());
                        /// et_address.setText(object.getJSONObject("response").getString("address").toString());
                        //  et_UserName.setText(object.getJSONObject("response").getString("fullName").toString());
                      /*  Log.d("skipusername", "userName" + usernameniv);
                        Log.d("skipusermailid", "Mail" + mailIdniv);
*/
                    } else {
                        // Toast.makeText(getApplicationContext(),"Could  not able to load data",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    // Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

               /* if (error.getMessage()==null ||error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ProfileNavigationActivity.this, R.style.MyDialogTheme).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Network/Connection Error");

                    // Setting Dialog Message
                    alertDialog.setMessage("Internet Connection is poor OR The Server is taking too long to respond.Please try again later.Thank you.");

                    // Setting Icon to Dialog
                    //  alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed
                            // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                    Log.e(TAG, "Update Error: " + error.getMessage());

                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                } else if (error instanceof NetworkError) {
                    //TODO
                } else if (error instanceof ParseError) {
                    //TODO
                }
*/
            }
        }); /*{

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userAccessToken", userAccessToken);

                return params;
            }

        };
*/
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    private void clientMemberStatusUpdate(String clientId, String userAccessToken , String phoneNo,String status) {


        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_updateUINStatus+ "?client_Id=" + clientId +"&admin="+userAccessToken +"&status="+status;

        Log.d("MemberAdapterUrl", "URL-->" + url);

        Log.d("MemberAdapter--","CONTACT_NUMBER "+phoneNo);

        JSONObject jsonObject = new JSONObject();
        try {
            Log.d("MemberAdapter--1","CONTACT_NUMBER "+phoneNo);
            jsonObject.put("CONTACT_NUMBER",phoneNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("MemberAdapter", response.toString() );

                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }


                        String status = null;
                        String response1 = null;
                        try {
                            status = response.getString("status");
                            response1 = response.getString("response");
                            Log.d("MemberAdapter","RESP : "+response1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        if (status != null && status.equals("success")) {

                            Log.d("MemberAdapter","InIFPart");

                            String clientImageURLPath = FullViewOfClientsProceed.clientImageURLPath;

                            Log.d("MemberAdapter","clientImageURLPath "+clientImageURLPath);

                            Intent intent = new Intent(mContext,FullViewOfClientsProceed.class);
                            intent.putExtra("clientImagePath",clientImageURLPath);
                            intent.putExtra("FRAGMENT_ID","3");
                            mContext.startActivity(intent);


                        }  else {
                            Log.d("MemberAdapter","InElsePart");
                            Toast.makeText(mContext,response1,Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }


                Log.d("MemberAdapter", "Error: " + error.getMessage());
                VolleyLog.d("MemberAdapter", "Error: " + error.getMessage());
            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

        };

        // Adding request to request queue
        Volley.newRequestQueue(mContext).add(jsonObjReq);



    }



    private void adminAccessToMember(String clientId, String userAccessToken, String phoneNumber) {

        String tag_string_req = "req_clients";

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_clientAdmin+ "?contactNumber=" + phoneNumber +"&token="+userAccessToken + "&clientId="+clientId;

        Log.d("MainFeedAdapter1", "adminAccessToMemberURL-->" + url);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {

                Log.d("MemberAdapter", "adminAccessToMember--> " + response1);
                //parsing Json
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response1);
                    String response = jsonObject.getString("response");
                    String status = jsonObject.getString("status");
                    Log.d("MemberAdapter1Resp", "adminAccessToMember" + response);
                    Log.d("MemberAdapter1Status", "adminAccessToMember" + status);

                    if (status.equals("success")){
                        Log.d("adminAccessToMember","InIfPart");

                        String clientImageURLPath = FullViewOfClientsProceed.clientImageURLPath;

                        Log.d("adminAccessToMember","clientImageURLPath "+clientImageURLPath);

                        Intent intent = new Intent(mContext,FullViewOfClientsProceed.class);
                        intent.putExtra("clientImagePath",clientImageURLPath);
                        intent.putExtra("FRAGMENT_ID","3");
                        mContext.startActivity(intent);

                    }else {
                        Log.d("adminAccessToMember","InElsePart");
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



}
