package in.sabpaisa.droid.sabpaisa;

import android.annotation.TargetApi;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import in.sabpaisa.droid.sabpaisa.Util.AppConfig;


public class MainGroupAdapter1 extends ArrayAdapter<GroupListData> {
    ArrayList<GroupListData> arrayList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;
    String pos;

    public MainGroupAdapter1( Context context,  int resource,  ArrayList<GroupListData> objects) {
        super(context, resource, objects);

        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        arrayList = objects;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        View v = convertView;
        if (v == null) {
            holder = new MainGroupAdapter1.ViewHolder();
            v = vi.inflate(Resource, null);

            holder.Group_name = (TextView) v.findViewById(R.id.Group_name);
            holder.Group_description = (TextView) v.findViewById(R.id.Group_description);
            holder.Group_Logo = (ImageView) v.findViewById(R.id.Group_Logo);
            holder.Group_Image = (ImageView) v.findViewById(R.id.Group_Image);
            holder.joinmember = (Button) v.findViewById(R.id.joinmember);


            v.setTag(holder);
        } else {
            holder = (MainGroupAdapter1.ViewHolder) v.getTag();
        }

        holder.Group_name.setText(arrayList.get(position).getGroupName());
        holder.Group_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Proceed_Group_FullScreen.class);
                intent.putExtra("groupName",arrayList.get(position).getGroupName());
                intent.putExtra("groupText",arrayList.get(position).getGroupText());
                intent.putExtra("groupImage",arrayList.get(position).getImagePath());
                intent.putExtra("groupId",arrayList.get(position).getGroupId());
                v.getContext().startActivity(intent);
            }
        });
        holder.Group_description.setText(arrayList.get(position).getGroupText());
        holder.Group_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Proceed_Group_FullScreen.class);
                intent.putExtra("groupName",arrayList.get(position).getGroupName());
                intent.putExtra("groupText",arrayList.get(position).getGroupText());
                intent.putExtra("groupImage",arrayList.get(position).getImagePath());
                intent.putExtra("groupId",arrayList.get(position).getGroupId());
                v.getContext().startActivity(intent);
            }
        });
        new DownloadImageTask(holder.Group_Image).execute(arrayList.get(position).getImagePath());
        holder.Group_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Proceed_Group_FullScreen.class);
                intent.putExtra("groupName",arrayList.get(position).getGroupName());
                intent.putExtra("groupText",arrayList.get(position).getGroupText());
                intent.putExtra("groupImage",arrayList.get(position).getImagePath());
                intent.putExtra("groupId",arrayList.get(position).getGroupId());
                v.getContext().startActivity(intent);
            }
        });
        new DownloadLogoTask(holder.Group_Logo).execute(arrayList.get(position).getLogoPath());
        holder.Group_Logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Proceed_Group_FullScreen.class);
                intent.putExtra("groupName",arrayList.get(position).getGroupName());
                intent.putExtra("groupText",arrayList.get(position).getGroupText());
                intent.putExtra("groupImage",arrayList.get(position).getImagePath());
                intent.putExtra("groupId",arrayList.get(position).getGroupId());
                v.getContext().startActivity(intent);
            }
        });

        holder.joinmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

                String token = sharedPreferences.getString("response", "123");

                String groupId = arrayList.get(position).getGroupId().toString();

                Log.d("tokenGRP"," "+token);
                Log.d("groupIdGRP"," "+groupId);

                addMember(token,groupId,v,arrayList.get(position));

            }
        });

        return v;
    }


    static class ViewHolder {

        public TextView Group_name;
        public TextView Group_description;
        public ImageView Group_Logo,Group_Image;
        public Button joinmember;

    }


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

                    String response = jObj.getString("response");

                    Log.d("MemberResponse"," "+response);

                    String status =jObj.getString("status");

                    if (status!=null && status.equals("success")){

                        final AlertDialog alertDialog = new AlertDialog.Builder( view.getContext(), R.style.MyDialogTheme).create();
                        // Setting Dialog Title
                        alertDialog.setTitle("Group Member");

                        // Setting Dialog Message
                        alertDialog.setMessage("Member added successfully Click On OK ");

                        alertDialog.setCanceledOnTouchOutside(false);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog closed

                                Intent intent = new Intent(view.getContext(),Proceed_Group_FullScreen.class);
                                intent.putExtra("groupName",groupListData.getGroupName());
                                intent.putExtra("groupText",groupListData.getGroupText());
                                intent.putExtra("groupImage",groupListData.getImagePath());
                                intent.putExtra("groupId",groupListData.getGroupId());
                                view.getContext().startActivity(intent);

                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();



                    }else {

                        final AlertDialog alertDialog = new AlertDialog.Builder( view.getContext(), R.style.MyDialogTheme).create();
                        // Setting Dialog Title
                        alertDialog.setTitle("Group Member");

                        // Setting Dialog Message
                        alertDialog.setMessage("You are already a member of this client");

                        alertDialog.setCanceledOnTouchOutside(false);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog closed
                                Intent intent = new Intent(view.getContext(),Proceed_Group_FullScreen.class);
                                intent.putExtra("groupName",groupListData.getGroupName());
                                intent.putExtra("groupText",groupListData.getGroupText());
                                intent.putExtra("groupImage",groupListData.getImagePath());
                                intent.putExtra("groupId",groupListData.getGroupId());
                                view.getContext().startActivity(intent);
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
