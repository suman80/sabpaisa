package in.sabpaisa.droid.sabpaisa.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.braunster.chatsdk.activities.ChatSDKLoginActivity;
import com.braunster.chatsdk.activities.ChatSDKMainActivity;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.MemberAdapter;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.MembersProfile;
import in.sabpaisa.droid.sabpaisa.Model.Member_GetterSetter;
import in.sabpaisa.droid.sabpaisa.R;

/**
 * Created by archana on 7/3/18.
 */

public class NoOfGroupmemberAdapter extends RecyclerView.Adapter<NoOfGroupmemberAdapter.MyViewHolder> {
        int count;
        ArrayList<Member_GetterSetter> memberGetterSetterArrayList;
        Context mContext;
        String name,image,mobNo;
        String useracesstoken,response,x,mobNumber;

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
@Override
public NoOfGroupmemberAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_custom, parent, false);
        return new NoOfGroupmemberAdapter.MyViewHolder(v);
        }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        final Member_GetterSetter member_getterSetter = memberGetterSetterArrayList.get(i);
        myViewHolder.memberName.setText(member_getterSetter.getFullName());
        name=member_getterSetter.getFullName();
        mobNo=member_getterSetter.getCONTACT_NUMBER();
        image=member_getterSetter.getUserImageUrl();

        Log.d("Adapter_NAme",""+name);
        Log.d("Adapter_NAme",""+mobNo);
        Log.d("Adapter_NAme",""+image);

        //holder.memberTimeStamp.setText(member_getterSetter.getTimestampOfJoining());
        //holder.memberImg.setImageUrl(member_getterSetter.getUserImageUrl(), imageLoader);
        Glide.with(mContext)
                .load(member_getterSetter.getUserImageUrl())
                .error(R.drawable.default_users)
                .into(myViewHolder.memberImg);
        /*myViewHolder.memberChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent=new Intent(, ChatSDKMainActivity.class);


                SharedPreferences sharedPreferences = mContext.getSharedPreferences(FullViewOfClientsProceed.MySharedPrefOnFullViewOfClientProceed, Context.MODE_PRIVATE);
                String clientId=sharedPreferences.getString("clientId","abc");
                String clientName=sharedPreferences.getString("clientName","abc");
                String state=sharedPreferences.getString("state","abc");
                String clientImageURLPath=sharedPreferences.getString("clientImageURLPath","abc");

                int value=4;
                Intent intent = new Intent(mContext,ChatSDKLoginActivity.class);
                intent.putExtra("VALUE",value);


                //  Start 10-april-2018////////////////////////

                intent.putExtra("usernameniv",name);
                // intent.putExtra("userImageUrlMaim",memberGetterSetterArrayList.get(position).getUserImageUrl().toString());
                intent.putExtra("xxxxx",x);
                intent.putExtra("mobNumber",mobNumber);

                //  End 10-april-2018////////////////////////

                intent.putExtra("CLIENTID",clientId);
                intent.putExtra("CLIENTNAME",clientName);
                intent.putExtra("STATE",state);
                intent.putExtra("CLIENTIMG",clientImageURLPath);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               // ((Activity) mContext).overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);






                //v.getContext().startActivity(new Intent(mContext,ChatSDKMainActivity.class));

            }
        });*/

        myViewHolder.memberImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent=new Intent(, ChatSDKMainActivity.class);
               // v.getContext().startActivity(new Intent(mContext,MembersProfile.class));
                Log.d("POsitikon-",""+memberGetterSetterArrayList.get(i).getFullName().toString());
                Log.d("POsitikon-",""+memberGetterSetterArrayList.get(i).getUserImageUrl().toString());
                Intent intent = new Intent(mContext,MembersProfile.class);
                intent.putExtra("name1",memberGetterSetterArrayList.get(i).getFullName().toString());
                intent.putExtra("image1",memberGetterSetterArrayList.get(i).getUserImageUrl().toString());
                intent.putExtra("emailid1",memberGetterSetterArrayList.get(i).getEmailId().toString());
                intent.putExtra("mobNo1",memberGetterSetterArrayList.get(i).getPhoneNumber().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                mContext.startActivity(intent);
            }
        });
        myViewHolder.memberName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent=new Intent(, ChatSDKMainActivity.class);
                Log.d("POsitikon-",""+memberGetterSetterArrayList.get(i).getFullName().toString());
                Log.d("POsitikon-",""+memberGetterSetterArrayList.get(i).getUserImageUrl().toString());
                Log.d("POsitikon-",""+memberGetterSetterArrayList.get(i).getEmailId().toString());
                Log.d("POsitikon-",""+memberGetterSetterArrayList.get(i).getPhoneNumber().toString());
              //Log.d("size_position",""+getItemCount());
              /*  name=member_getterSetter.getFullName();
                mobNo=member_getterSetter.getCONTACT_NUMBER();
                image=member_getterSetter.getUserImageUrl();
*/
               // v.getContext().startActivity(new Intent(mContext,MembersProfile.class));
                Intent intent = new Intent(mContext,MembersProfile.class);
                intent.putExtra("name1",memberGetterSetterArrayList.get(i).getFullName().toString());
                intent.putExtra("image1",memberGetterSetterArrayList.get(i).getUserImageUrl().toString());
                intent.putExtra("emailid1",memberGetterSetterArrayList.get(i).getEmailId().toString());
                intent.putExtra("mobNo1",memberGetterSetterArrayList.get(i).getPhoneNumber().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                mContext.startActivity(intent);
               //((Activity) mContext).overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);

            }
        });
    }


    public NoOfGroupmemberAdapter(ArrayList<Member_GetterSetter> memberGetterSetterArrayList , Context context) {
        this.memberGetterSetterArrayList = memberGetterSetterArrayList;
        this.mContext = context;
        }


    /*START Method to change data when put query in searchBar*/
public void setItems(ArrayList<Member_GetterSetter> memberDatas) {
        this.memberGetterSetterArrayList = memberDatas;
        }



/*
@Override
public void onBindViewHolder(MemberAdapter.MyViewHolder holder, int position) {

        Member_GetterSetter member_getterSetter = memberGetterSetterArrayList.get(position);
        holder.memberName.setText(member_getterSetter.getFullName());
        //holder.memberTimeStamp.setText(member_getterSetter.getTimestampOfJoining());
        //holder.memberImg.setImageUrl(member_getterSetter.getUserImageUrl(), imageLoader);
        Glide.with(mContext)
        .load(member_getterSetter.getUserImageUrl())
        .error(R.drawable.default_users)
        .into(holder.memberImg);

        }
*/

public class MyViewHolder extends RecyclerView.ViewHolder {
Button memberChat;
    ImageView memberImg;
    TextView memberName,memberTimeStamp;
    public MyViewHolder(View itemView) {
        super(itemView);


        memberImg = (ImageView) itemView .findViewById(R.id.memberImg);
        memberName = (TextView)itemView.findViewById(R.id.memberName);
        //memberChat = (Button)itemView.findViewById(R.id.groupmmbrchat);
        //memberTimeStamp = (TextView)itemView.findViewById(R.id.memberTimeStamp);


        //  Start 10-april-2018////////////////////////

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

        //  end 10-april-2018////////////////////////

    }
}

    @Override
    public int getItemCount() {
        return memberGetterSetterArrayList.size();
    }


    //  Start 10-april-2018////////////////////////

    private void showProfileData() {

        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.Base_Url+AppConfig.App_api+AppConfig.URL_Show_UserProfile+"?token="+useracesstoken, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                Log.d("SKipusernamenav", "Register Response: " + response1.toString());

                try {
                    //progressBar.setVisibility(View.GONE);
                    JSONObject object = new JSONObject(response1);
                    String response = object.getString("response");
                    String status =object.getString("status");
                    x = object.getJSONObject("response").getString("emailId").toString();
                    name=object.getJSONObject("response").getString("fullName").toString();

                    mobNumber = object.getJSONObject("response").getString("contactNumber").toString();

                    if(x.equals("null"))
                    {

                        x="PleaseSetYourNAme@gmail.com";
                        // usernameniv.setText(object.getJSONObject("response").getString("fullName").toString());
                        //mailIdniv.setText("");
                    }
                    else   if (status.equals("success")) {
                        name=object.getJSONObject("response").getString("fullName").toString();
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
                    }else {
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

    //  End 10-april-2018////////////////////////


}

