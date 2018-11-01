package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.RequestQueue;
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

import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.MembersProfile;
import in.sabpaisa.droid.sabpaisa.Model.Member_GetterSetter;
import in.sabpaisa.droid.sabpaisa.PendingListOfUsers;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.UIN;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;
import me.grantland.widget.AutofitTextView;

public class PendingListOfUsersAdapter extends RecyclerView.Adapter<PendingListOfUsersAdapter.MyViewHolder> {
    int count;
    ArrayList<Member_GetterSetter> memberGetterSetterArrayList;
    Context mContext;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public static boolean isClicked = false;

    String userAccessToken, clientId;

    public PendingListOfUsersAdapter(ArrayList<Member_GetterSetter> memberGetterSetterArrayList, Context context) {
        this.memberGetterSetterArrayList = memberGetterSetterArrayList;
        this.mContext = context;
    }

    @Override
    public PendingListOfUsersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_list_of_user_custom, parent, false);
        return new PendingListOfUsersAdapter.MyViewHolder(v);
    }



    @Override
    public void onBindViewHolder(final PendingListOfUsersAdapter.MyViewHolder holder, final int position) {

        final Member_GetterSetter member_getterSetter = memberGetterSetterArrayList.get(position);

        holder.memberName.setText(member_getterSetter.getFullName());

        Glide.with(mContext)
                .load(member_getterSetter.getUserImageUrl())
                .error(R.drawable.default_users)
                .into(holder.memberImg);

        SharedPreferences sharedPreferencesRole = mContext.getSharedPreferences(UIN.SHARED_PREF_FOR_CHECK_USER, Context.MODE_PRIVATE);

        String roleValue = sharedPreferencesRole.getString("USER_ROLE", "abc");


        SharedPreferences sharedPreferences1 = mContext.getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        userAccessToken = sharedPreferences1.getString("response", "123");

        if (userAccessToken.equals(member_getterSetter.getUserAccessToken())) {
            holder.memberName.setText("You");
        }

        holder.declineMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("PendingListADAP_ID"," "+member_getterSetter.getId());
                decisionOnPendingRequest(member_getterSetter.getId(),"Blocked");
            }
        });

        holder.approveMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("PendingListADAP_ID"," "+member_getterSetter.getId());
                decisionOnPendingRequest(member_getterSetter.getId(),"Approved");

            }
        });



    }


    public class MyViewHolder extends RecyclerView.ViewHolder  {

        public ImageView memberImg;

        public AutofitTextView memberName;

        ImageView declineMember,approveMember;

        public MyViewHolder(View itemView) {
            super(itemView);
            memberImg = (ImageView) itemView.findViewById(R.id.memberImg);
            memberName = (AutofitTextView) itemView.findViewById(R.id.memberName);

            declineMember = (ImageView) itemView.findViewById(R.id.declineMember);
            approveMember = (ImageView) itemView.findViewById(R.id.approveMember);


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


    private void decisionOnPendingRequest(final String Id, final String status) {

        String tag_string_req = "req_clients";

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig. URL_decisionOnPendingRequest+ "?id=" + Id +"&status="+status ;

        Log.d("PendingListAdapter", "PendingListAdapterURL-->" + url);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {

                Log.d("PendingListAdapter", "PendingListAdapterRESP--> " + response1);
                //parsing Json
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response1);
                    String response = jsonObject.getString("response");
                    String status = jsonObject.getString("status");
                    Log.d("PendingListAdapterResp", "IN_TRY_RESP" + response);
                    Log.d("PendingListAdptrStatus", "IN_TRY_Status" + status);

                    if (status.equals("success")){
                        Log.d("PendingListAdapter","InIfPart");

                        //Toast.makeText(mContext,response,Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(mContext,PendingListOfUsers.class);
                        intent.putExtra("groupId",PendingListOfUsers.groupId);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);

                    }else {
                        Log.d("PendingListAdapter","InElsePart");
                        //Toast.makeText(mContext,response,Toast.LENGTH_SHORT).show();
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