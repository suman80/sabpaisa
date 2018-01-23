package in.sabpaisa.droid.sabpaisa;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;


public class MainGroupAdapter1 extends
        RecyclerView.Adapter<MainGroupAdapter1.MyViewHolder> {
    Context mContext;
    private List<GroupListData> countryList;

    public MainGroupAdapter1(List<GroupListData> countryList,Context context) {
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
                .error(R.drawable.default_users)
                .into(holder.Group_Logo);

        Glide.with(mContext)
                .load(c.getImagePath())
                .error(R.drawable.default_users)
                .into(holder.Group_Image);

        holder.Group_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Proceed_Group_FullScreen.class);
                intent.putExtra("groupName",c.getGroupName());
                intent.putExtra("groupText",c.getGroupText());
                intent.putExtra("groupImage",c.getImagePath());
                intent.putExtra("groupId",c.getGroupId());
                v.getContext().startActivity(intent);
            }
        });

        holder.Group_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Proceed_Group_FullScreen.class);
                intent.putExtra("groupName",c.getGroupName());
                intent.putExtra("groupText",c.getGroupText());
                intent.putExtra("groupImage",c.getImagePath());
                intent.putExtra("groupId",c.getGroupId());
                v.getContext().startActivity(intent);
            }
        });

        holder.Group_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Proceed_Group_FullScreen.class);
                intent.putExtra("groupName",c.getGroupName());
                intent.putExtra("groupText",c.getGroupText());
                intent.putExtra("groupImage",c.getImagePath());
                intent.putExtra("groupId",c.getGroupId());
                v.getContext().startActivity(intent);
            }
        });

        holder.Group_Logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Proceed_Group_FullScreen.class);
                intent.putExtra("groupName",c.getGroupName());
                intent.putExtra("groupText",c.getGroupText());
                intent.putExtra("groupImage",c.getImagePath());
                intent.putExtra("groupId",c.getGroupId());
                v.getContext().startActivity(intent);
            }
        });

        holder.joinmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

                String token = sharedPreferences.getString("response", "123");

                String groupId = c.getGroupId().toString();

                Log.d("tokenGRP"," "+token);
                Log.d("groupIdGRP"," "+groupId);

                addMember(token,groupId,v,c);

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
        public PhotoView Group_Image;
        public Button joinmember;

        public MyViewHolder(View view) {
            super(view);

            Group_name = (TextView) view.findViewById(R.id.Group_name);
            Group_description = (TextView) view.findViewById(R.id.Group_description);
            joinmember = (Button) view.findViewById(R.id.joinmember);
            Group_Logo = (ImageView) view.findViewById(R.id.Group_Logo);
            Group_Image = (PhotoView) view.findViewById(R.id.Group_Image);

        }
    }

/*
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loading.show();
        }

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            //loading.dismiss();
        }

    }

    //Code for fetching image from server
    private class DownloadLogoTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loading.show();
        }

        public DownloadLogoTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            //loading.dismiss();
        }

    }
*/


    public void addMember (final String token , final String groupId , final View view , final GroupListData groupListData)
    {

// Tag used to cancel the request
        String tag_string_req = "req_register";

        //pDialog.setMessage("Registering ...");
        //showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADD_Member+"token="+token+"&"+"groupId="+groupId, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {

//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response1);

                    final String response = jObj.getString("response");

                    Log.d("MemberResponse"," "+response);

                    String status =jObj.getString("status");

                    if (status!=null && status.equals("success")){

                        final AlertDialog alertDialog = new AlertDialog.Builder( view.getContext(), R.style.MyDialogTheme).create();
                        // Setting Dialog Title
                        alertDialog.setTitle("Group Member");

                        // Setting Dialog Message
                        alertDialog.setMessage("Request Sent for Approval ");

                        alertDialog.setCanceledOnTouchOutside(false);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
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
                        alertDialog.show();



                    }

                    else {

                        final AlertDialog alertDialog = new AlertDialog.Builder( view.getContext(), R.style.MyDialogTheme).create();
                        // Setting Dialog Title
                        alertDialog.setTitle("Group Member");

                        // Setting Dialog Message
                        alertDialog.setMessage(response);

                        alertDialog.setCanceledOnTouchOutside(false);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                ///////////////////////////////////////////////
                                if(response.equals("User already a member of the client with Status Approved")){
                                    Intent intent = new Intent(view.getContext(),Proceed_Group_FullScreen.class);
                                    intent.putExtra("groupName",groupListData.getGroupName());
                                    intent.putExtra("groupText",groupListData.getGroupText());
                                    intent.putExtra("groupImage",groupListData.getImagePath());
                                    intent.putExtra("groupId",groupListData.getGroupId());
                                    view.getContext().startActivity(intent);
                                }
                                ///////////////////////////////////////////////
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

                if (error.getMessage()==null ||error instanceof TimeoutError || error instanceof NoConnectionError) {
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






}
