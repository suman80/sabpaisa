package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Model.ContactList;
import in.sabpaisa.droid.sabpaisa.R;

/**
 * Created by abc on 16-06-2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {

    Context context;
    ArrayList<ContactList> contactLists;
    public interface UPIResponse {
        void onUpiSelect(String upi);
    }

    public UPIResponse selectedUPI;
    public ContactsAdapter(Context context, ArrayList<ContactList> contactLists) {
        this.contactLists = contactLists;
        this.context = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_list, parent, false);
        return new MyViewHolder(v);
    }

    public void setItems(ArrayList<ContactList> contactLists) {
        this.contactLists = contactLists;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ContactList contactList = contactLists.get(position);
        holder.userName.setText(contactList.getName());
        holder.userNumber.setText(contactList.getNumber());
        if (position%2!=0) {
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.main_screen_bottom_color));
        }
        else{
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        holder.background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    selectedUPI = (UPIResponse) context;
                } catch (ClassCastException e) {
                    throw new ClassCastException(context.toString()
                            + " must implement OnClickInAdapter");
                }
                String upi = holder.userNumber.getText().toString().trim()+"@upi";
                selectedUPI.onUpiSelect(upi);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout background;
        TextView userName,userNumber;
        public MyViewHolder(View itemView) {
            super(itemView);

            background = (LinearLayout)itemView.findViewById(R.id.ll_background);
            userName = (TextView)itemView.findViewById(R.id.tv_userName);
           // userNumber = (TextView)itemView.findViewById(R.id.tv_userNumber);
        }
    }
}
