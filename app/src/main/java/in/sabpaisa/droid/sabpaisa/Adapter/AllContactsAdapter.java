package in.sabpaisa.droid.sabpaisa.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import in.sabpaisa.droid.sabpaisa.AllContacts;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.Model.ContactVO;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;

/**
 * Created by archana on 22/3/18.
 */

public class AllContactsAdapter  extends RecyclerView.Adapter<AllContactsAdapter.ContactViewHolder>{
String key;
    private List<ContactVO> contactVOList;
    private Context mContext;
    static Button sp;

    public AllContactsAdapter(List<ContactVO> contactVOList, Context mContext){
        this.contactVOList = contactVOList;
        this.mContext = mContext;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_contact_list, null);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        ContactVO contactVO = contactVOList.get(position);
        holder.tvContactName.setText(contactVO.getContactName());
        holder.tvPhoneNumber.setText(contactVO.getContactNumber());

    }

    @Override
    public int getItemCount() {
        return contactVOList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder{

        ImageView ivContactImage;
        TextView tvContactName;
        TextView tvPhoneNumber;

        public ContactViewHolder(View itemView) {
            super(itemView);
            ivContactImage = (ImageView) itemView.findViewById(R.id.ivContactImage);
            tvContactName = (TextView) itemView.findViewById(R.id.tvContactName);
           // tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber);

        sp=(Button) itemView.findViewById(R.id.abc);
        }
    }






    private void ContactsApi(final ArrayList<String> contactList ) {


        // Tag used to cancel the request
        String tag_string_req = "req_contacts";

        Map<String, List<String>> map = new HashMap<String, List<String>>();
        map.put("contactList", contactList);
        JSONObject jObj1 = new JSONObject(map);


        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                AppConfig.Base_Url+AppConfig.App_api+ AppConfig.URL_Contacts,jObj1, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("Contactsreponse ", " " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(String.valueOf(response));


                    String status = jObj.getString("status");
                    String response1 = jObj.getString("response");

                    Log.d("STATUS ", " " + status);
                    Log.d("STATUS1234567890 ", " " + response1);


                    //   JsonArray jsonObject=new JsonArray(response1.charAt(i));
                    // Log.d("tvji11", " "+ jsonObject);
                    JsonArray jsonObject1 = new JsonObject().getAsJsonArray("response1");
                    Log.d("tvji1145", " " + jsonObject1);

                    JSONObject object1 = jObj.getJSONObject("response");
                    Log.d("tvji1145", " " + object1);
                    Iterator<String> iterator = object1.keys();
                    Log.d("tvji11452", " " + iterator);
                    while (iterator.hasNext()) {
                        key = iterator.next();
                        Log.d("UserContactList", "==>" + key);

                        Log.d("numbersREGorNot", "==>" + object1.optString(key));
                        if (object1.optString(key).equals("User_Not_Registered"))
                        {
                            Log.d("numbersREGorNotqyeuqye", "==>pagalo"+key);


                            sp.setVisibility(View.VISIBLE);
                            //.setVisibility();
                        }

                    }







                } catch (JSONException e) {
                    e.printStackTrace();
                 //   Toast.makeText(g, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage()==null || error.getMessage()==null || error.getMessage()==null ||                error instanceof TimeoutError || error instanceof NoConnectionError) {
                    /*AlertDialog alertDialog = new AlertDialog.Builder(, R.style.MyDialogTheme).create();

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
                 */   Log.e("", "Cpntacts api Error: " + error.getMessage());
                    /*Toast.makeText(context,
                            context.getString(R.string.error_network_timeout),
                            Toast.LENGTH_LONG).show();*/
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
        }) {


            protected Map<String, String> getParams() {
                ArrayList<String> contactList = new ArrayList<String>();


                Map<String, String> params = new HashMap<String, String>();

                int i=0;
                for(String object: contactList){
                    params.put("contactList["+(i++)+"]", object);
                    // first send both data with same param name as contactList[] ....
                    // now send with params contactList[0],contactList[1] ..and so on
                }

                params.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}


