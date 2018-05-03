package in.sabpaisa.droid.sabpaisa.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
    private ArrayList<ContactVO> contactVOList;
    private Context mContext;
    static Button sp;

    public AllContactsAdapter(ArrayList<ContactVO> contactVOList, Context mContext){
        this.contactVOList = contactVOList;
        this.mContext = mContext;
    }



    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.contactsrecylcerview, null);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        ContactVO contactVO = contactVOList.get(position);

         holder.tvContactName.setText(contactVO.getContactName());
        holder.tvPhoneNumber.setText(contactVO.getContactNumber());
        Log.d("contactVO",String.valueOf(contactVO.getInviteButtonVisibility()));
        Log.d("contactVO",String.valueOf(contactVO.getContactName()));
        if(contactVO.getInviteButtonVisibility()==0)
        {
            holder.invite.setVisibility(View.VISIBLE);
            holder.invite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, "SPApp");
                        String sAux = "\n Let me recommend you this application .\n this is the easy way to pay your fee\n It is very cool app try it once ,download it from the below link given... \n \n";
                        sAux = sAux+"\n"+"https://play.google.com/store/apps/details?id=in.sabpaisa.droid.sabpaisa";
                        i.putExtra(Intent.EXTRA_TEXT, sAux);

                        mContext.startActivity(Intent.createChooser(i, "Share via"));
                    } catch (Exception e) {


                        //e.toString();
                    }
                }
            });
        }
        else
        {
            holder.invite.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return contactVOList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder{

        ImageView ivContactImage;
        TextView tvContactName;
        TextView tvPhoneNumber;
        Button invite;

        public ContactViewHolder(View itemView) {
            super(itemView);
            //ivContactImage = (ImageView) itemView.findViewById(R.id.ivContactImage);
            tvContactName = (TextView) itemView.findViewById(R.id.name);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.number);
            invite=(Button) itemView.findViewById(R.id.buttonInvite);
           // sp=(Button) itemView.findViewById(R.id.abc);
        }
    }







}

