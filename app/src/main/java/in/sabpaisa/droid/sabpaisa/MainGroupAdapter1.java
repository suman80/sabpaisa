package in.sabpaisa.droid.sabpaisa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
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
import com.android.volley.toolbox.StringRequest;
import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;

public class MainGroupAdapter1 extends RecyclerView.Adapter<MainGroupAdapter1.MyViewHolder> {

    Context mContext;
    private List<GroupListData> countryList;
    public Button joinmember;

    String popup = "Group";

    String userAccessToken;

    public static boolean isClicked=false;

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

        final GroupListData c = countryList.get(position);
        holder.Group_name.setText(c.getGroupName());
        holder.Group_description.setText(c.getGroupText());
       /* new DownloadLogoTask(holder.Group_Logo).execute(c.getLogoPath());
        new DownloadImageTask(holder.Group_Image).execute(c.getImagePath());*/
        Glide.with(mContext)
                .load(c.getLogoPath())
                .error(R.drawable.image_not_found)
                .into(holder.Group_Logo);

        Glide.with(mContext)
                .load(c.getImagePath())
                .error(R.drawable.image_not_found)
                .into(holder.Group_Image);

        if (c.getMemberStatus().equals("Blocked")) {

            Log.d("RajBhai", "Checking fade effect");
            holder.linearLayoutGroupItemList.setEnabled(false);
            holder.linearLayoutGroupItemList.setAlpha(.5f);

            holder.imgPopUpMenu.setVisibility(View.GONE);

        }


/*
        holder.Group_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

                String token = sharedPreferences.getString("response", "123");

                String groupId = c.getGroupId().toString();

                Log.d("tokenGRP"," "+token);
                Log.d("groupIdGRP"," "+groupId);

                addMember(token,groupId,v,c);

                *//*Intent intent = new Intent(v.getContext(),Proceed_Group_FullScreen.class);
                intent.putExtra("groupName",c.getGroupName());
                intent.putExtra("groupText",c.getGroupText());
                intent.putExtra("groupImage",c.getImagePath());
                intent.putExtra("groupId",c.getGroupId());
                v.getContext().startActivity(intent);*//*
            }
        });

        holder.Group_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

                String token = sharedPreferences.getString("response", "123");

                String groupId = c.getGroupId().toString();

                Log.d("tokenGRP"," "+token);
                Log.d("groupIdGRP"," "+groupId);

                addMember(token,groupId,v,c);

               *//* Intent intent = new Intent(v.getContext(),Proceed_Group_FullScreen.class);
                intent.putExtra("groupName",c.getGroupName());
                intent.putExtra("groupText",c.getGroupText());
                intent.putExtra("groupImage",c.getImagePath());
                intent.putExtra("groupId",c.getGroupId());
                v.getContext().startActivity(intent);*//*
            }
        });

        holder.Group_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

                    String token = sharedPreferences.getString("response", "123");

                    String groupId = c.getGroupId().toString();

                    Log.d("tokenGRP", " " + token);
                    Log.d("groupIdGRP", " " + groupId);

                    addMember(token, groupId, v, c);


                    *//*Intent intent = new Intent(v.getContext(), Proceed_Group_FullScreen.class);
                    intent.putExtra("groupName", c.getGroupName());
                    intent.putExtra("groupText", c.getGroupText());
                    intent.putExtra("groupImage", c.getImagePath());
                    intent.putExtra("groupId", c.getGroupId());
                    v.getContext().startActivity(intent);*//*

            }
        });

        holder.Group_Logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

                String token = sharedPreferences.getString("response", "123");

                String groupId = c.getGroupId().toString();

                Log.d("tokenGRP"," "+token);
                Log.d("groupIdGRP"," "+groupId);

                addMember(token,groupId,v,c);

                *//*Intent intent = new Intent(v.getContext(),Proceed_Group_FullScreen.class);
                intent.putExtra("groupName",c.getGroupName());
                intent.putExtra("groupText",c.getGroupText());
                intent.putExtra("groupImage",c.getImagePath());
                intent.putExtra("groupId",c.getGroupId());

                v.getContext().startActivity(intent);*//*

            }
        });

        */

        holder.rippleClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

                String token = sharedPreferences.getString("response", "123");

                String groupId = c.getGroupId().toString();

                Log.d("tokenGRP", " " + token);
                Log.d("groupIdGRP", " " + groupId);

                addMember(token, groupId, view, c);
            }
        });


        holder.joinmember.setText(c.getMemberStatus());
        if (c.getMemberStatus().equals("Approved")) {
            holder.joinmember.setVisibility(View.INVISIBLE);
        }

        holder.joinmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

                String token = sharedPreferences.getString("response", "123");

                String groupId = c.getGroupId().toString();

                Log.d("tokenGRP", " " + token);
                Log.d("groupIdGRP", " " + groupId);

                addMember(token, groupId, v, c);

            }
        });

        SharedPreferences sharedPreferencesRole = mContext.getSharedPreferences(UIN.SHARED_PREF_FOR_CHECK_USER, Context.MODE_PRIVATE);

        String roleValue = sharedPreferencesRole.getString("USER_ROLE", "abc");

        if (roleValue.equals("1")) {

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

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (menuItem.getTitle().equals("Delete")){
                            deleteGroupData(groupId,userAccessToken);
                        }

                        if (menuItem.getTitle().equals("Add Member")){
                            Intent intent = new Intent(mContext,AddMemberTo_A_Group.class);
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
        public Button joinmember;
        public LinearLayout linearLayoutGroupItemList;
        MaterialRippleLayout rippleClick;
        ImageView imgPopUpMenu;

        public MyViewHolder(View view) {
            super(view);

            Group_name = (TextView) view.findViewById(R.id.Group_name);
            Group_description = (TextView) view.findViewById(R.id.Group_description);
            joinmember = (Button) view.findViewById(R.id.joinmember);
            Group_Logo = (ImageView) view.findViewById(R.id.Group_Logo);
            Group_Image = (ImageView) view.findViewById(R.id.Group_Image);
            linearLayoutGroupItemList = (LinearLayout) view.findViewById(R.id.linearLayoutGroupItemList);
            rippleClick = (MaterialRippleLayout) view.findViewById(R.id.rippleClick);
            imgPopUpMenu = (ImageView)view.findViewById(R.id.imgPopUpMenu);

        }



    }


    private void deleteGroupData(String groupId, String userAccessToken) {

        String tag_string_req = "req_clients";

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_deleteGroup+ "?groupId=" + groupId +"&admin="+userAccessToken;

        Log.d("MainFeedAdapter1", "URL-->" + url);

        StringRequest request = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {

                Log.d("MainFeedAdapter1", "-->" + response1);
                //parsing Json
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response1);
                    String response = jsonObject.getString("response");
                    String status = jsonObject.getString("status");
                    Log.d("MainFeedAdapter1Resp", "" + response);
                    Log.d("MainFeedAdapter1Status", "" + status);

                    if (status.equals("success")){
                        Log.d("MainFeedAdapter1","InIfPart");

                        String clientImageURLPath = FullViewOfClientsProceed.clientImageURLPath;

                        Log.d("MainFeedAdapter1","clientImageURLPath "+clientImageURLPath);

                        Intent intent = new Intent(mContext,FullViewOfClientsProceed.class);
                        intent.putExtra("clientImagePath",clientImageURLPath);
                        //intent.putExtra("deleteFrmGrp","1");
                        mContext.startActivity(intent);

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



    public void addMember(final String token, final String groupId, final View view, final GroupListData groupListData) {

// Tag used to cancel the request
        String tag_string_req = "req_register";

        //pDialog.setMessage("Registering ...");
        //showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_ADD_Member + "token=" + token + "&" + "groupId=" + groupId, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {

//                hideDialog();

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
                        popup = "Groups";
                        Intent intent = new Intent(view.getContext(), Proceed_Group_FullScreen.class);
                        intent.putExtra("popup", popup);
                        intent.putExtra("groupName", groupListData.getGroupName());
                        intent.putExtra("groupText", groupListData.getGroupText());
                        intent.putExtra("groupImage", groupListData.getImagePath());
                        intent.putExtra("groupId", groupListData.getGroupId());

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





}
