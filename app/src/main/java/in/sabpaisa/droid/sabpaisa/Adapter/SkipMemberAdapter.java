package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

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

import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.Model.MemberSpaceModel;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import me.grantland.widget.AutofitTextView;

import static in.sabpaisa.droid.sabpaisa.MainActivitySkip.SUPER_ADMIN_SHAREDFREF;

public class SkipMemberAdapter extends RecyclerView.Adapter<SkipMemberAdapter.MyViewHolder> {

    public List<MemberSpaceModel> memberSpaceModelList;
    Context context;

    String roleValue;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.member_custom, parent, false);

        return new MyViewHolder(itemView);
    }

    public SkipMemberAdapter(List<MemberSpaceModel> arrayList, Context context) {
        this.memberSpaceModelList = arrayList;
        this.context = context;
    }

    /*START Method to change data when put query in searchBar*/
    public void setItems(List<MemberSpaceModel> memberDatas) {
        this.memberSpaceModelList = memberDatas;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final MemberSpaceModel memberSpaceModel = memberSpaceModelList.get(position);

        Log.d("SMA","userImage___"+memberSpaceModel.getUserImageUrl());

        Glide.with(context)
                .load(memberSpaceModel.getUserImageUrl())
                .error(R.drawable.default_users)
                .into(holder.memberImg);

        holder.memberName.setText(memberSpaceModel.getFullName());


        SharedPreferences sharedPreferencesRole = context.getSharedPreferences(SUPER_ADMIN_SHAREDFREF, Context.MODE_PRIVATE);

        roleValue = sharedPreferencesRole.getString("ROLE_VALUE", "abc");

        Log.d("roleValueSkipMemFrag"," "+roleValue);


        if (memberSpaceModel.getRoleId().equals("2") || roleValue.equals("1")){
            holder.imgPopUpMenu.setVisibility(View.VISIBLE);
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
                            removeMember(memberSpaceModel.getMemberId(),userAccessToken,holder.getAdapterPosition());

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
        return memberSpaceModelList.size();
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

        public ImageView memberImg;
        public AutofitTextView memberName;
        MaterialRippleLayout rippleClick;
        ImageView imgPopUpMenu;
        TextView textViewAdmin;


        public MyViewHolder(View view) {
            super(view);
            memberImg = (ImageView) itemView.findViewById(R.id.memberImg);
            memberName = (AutofitTextView) itemView.findViewById(R.id.memberName);
            rippleClick = (MaterialRippleLayout) itemView.findViewById(R.id.rippleClick);
            imgPopUpMenu = (ImageView)itemView.findViewById(R.id.imgPopUpMenu);
            textViewAdmin = (TextView)itemView.findViewById(R.id.textViewAdmin);

        }
    }


    private void removeMember(final String memberId,final String userAccessToken,final int position) {

        String tag_string_req = "req_clients";

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_removemembersToSpappClient+ "?memberId=" + memberId+"&admin="+userAccessToken;

        Log.d("SkipMemberAdapter", "adminAccessToMemberURL-->" + url);

        StringRequest request = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {

                Log.d("SkipMemberAdapter", "response1--> " + response1);
                //parsing Json
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response1);
                    String response = jsonObject.getString("response");
                    String status = jsonObject.getString("status");
                    Log.d("SkipMemberAdapter", "response__" + response);
                    Log.d("SkipMemberAdapter", "status__" + status);

                    if (status.equals("success")){
                        Log.d("SkipMemberAdapter","InIfPart");

                        memberSpaceModelList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, memberSpaceModelList.size());


                    }else {
                        Log.d("SkipMemberAdapter","InElsePart");
                        //Toast.makeText(context,response,Toast.LENGTH_SHORT).show();
                        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context, R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Remove Member");

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
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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