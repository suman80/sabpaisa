package in.sabpaisa.droid.sabpaisa.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.MainActivity;
import in.sabpaisa.droid.sabpaisa.Model.ClientsDataModel;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;

import static android.content.Context.MODE_PRIVATE;
import static in.sabpaisa.droid.sabpaisa.UIN.MYSHAREDPREFUIN;

public class HorizontalClientsRecyclerViewAdapter extends RecyclerView.Adapter<HorizontalClientsRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "HorizontalClientsAdptr";

    //vars
    private ArrayList<ClientsDataModel> mNames = new ArrayList<>();

    private Context mContext;

    public static ProgressDialog progressDialog;

    public HorizontalClientsRecyclerViewAdapter(Context context, ArrayList<ClientsDataModel> names) {
        mNames = names;

        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_horizontal_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        final ClientsDataModel clientsDataModel = mNames.get(position);


        Glide.with(mContext)
                .load(clientsDataModel.getClientImageUrl())
                .error(R.drawable.image_not_found)
                .into(holder.image);

        holder.name.setText(clientsDataModel.getClientName());

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on an image: " + mNames.get(position));

                verifyUIN(clientsDataModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.name);
        }
    }




    public void verifyUIN(final ClientsDataModel clientsDataModel) {
        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_VerifyUin + clientsDataModel.getUinNo() + "&client_Id=" + clientsDataModel.getClientId() + "&aceesToken=" + clientsDataModel.getUserAccessToken(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                Log.d("HCRCA_VerifyUin", response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    JSONObject jObj = new JSONObject(response);

                    String response1 = jObj.getString("response");
                    //Log.d(TAG, "Register Response1: " + response);
                    String status = jObj.getString("status");
                    //String response1 = response.getString("response");
                    // Log.i("status_UIN", "status=" + status);
                    Log.i("response_UIN", "Repsomse_UIN=" + response1);
                    if (status.equals("success") && response1.equals("UIN verified")) {

                        Log.d("InIfPArt", "UINVerifu");


                        /*SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREF_UIN_STATUS, MODE_PRIVATE).edit();
                        editor.putString("UIN_STATUS", "UIN_VERIFIED");
                        editor.putString("UIN_NUMBER", uinnnumber);
                        editor.commit();*/

                        progressDialog = new ProgressDialog(mContext);
                        progressDialog.setMessage("Please wait...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        SharedPreferences.Editor editor = mContext.getSharedPreferences(MYSHAREDPREFUIN, MODE_PRIVATE).edit();
                        Log.d("ClientIdHCRV",""+clientsDataModel.getClientId());
                        editor.putString("clientId", clientsDataModel.getClientId());
                        editor.putString("m", "abc");
                        editor.putString("userAccessToken", clientsDataModel.getUserAccessToken());
                        editor.commit();


                        Intent intent = new Intent(mContext, MainActivity.class);
                         intent.putExtra("clientId", clientsDataModel.getClientId());
                         intent.putExtra("userImageUrl", clientsDataModel.getClientImageUrl());
                         mContext.startActivity(intent);



                    } else if (status.equals("success") && response1.equals("Invalid UIN number")) {

                        Log.d("InelseIf1PArt", "UINVerifu");

                        AlertDialog alertDialog = new AlertDialog.Builder(mContext, R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("UIN Does not match");

                        // Setting Dialog Message
                        alertDialog.setMessage("Please Enter Correct UIN Number");

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

                    } else if (status.equals("failed") && response1.equals("User is Blocked")) {

                        Log.d("InElsePart", "UINVerifu");

                        AlertDialog alertDialog = new AlertDialog.Builder(mContext, R.style.MyDialogTheme).create();

                        alertDialog.setTitle("UIN");

                        alertDialog.setMessage(response1);

                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();

                    } else {

                        Log.d("InelsePArt", "UINVerifu");

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage() == null || error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(mContext, R.style.MyDialogTheme).create();

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

                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                } else if (error instanceof NetworkError) {
                    //TODO
                } else if (error instanceof ParseError) {
                    //TODO
                }
               /* VolleyLog.d("eclipse", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();*/

                // hide the progress dialog

            }
        });
        // Adds the JSON array request "arrayreq" to the request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        // AppController.getInstance().addToRequestQueue(jsonObjReq);
    }







}