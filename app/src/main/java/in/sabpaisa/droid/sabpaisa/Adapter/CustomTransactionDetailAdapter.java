package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.CustomTransactionReportActivity;
import in.sabpaisa.droid.sabpaisa.Model.CustomTransactionlistgettersetter;
import in.sabpaisa.droid.sabpaisa.R;

/**
 * Created by archana on 21/3/18.
 */

public  class CustomTransactionDetailAdapter extends RecyclerView.Adapter<CustomTransactionDetailAdapter.MyViewHolder> {
    int count;
    ArrayList<CustomTransactionlistgettersetter> todayTransactiongettersetters;
    Context mContext;


    @Override
    public CustomTransactionDetailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_custom_transaction_adapter, parent, false);
        return new CustomTransactionDetailAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CustomTransactionDetailAdapter.MyViewHolder holder, int position) {

        CustomTransactionlistgettersetter allTransactiongettersetter = todayTransactiongettersetters.get(position);

        //holder.memberImg.setImageUrl(member_getterSetter.getUserImageUrl(), imageLoader);

        String clientName=allTransactiongettersetter.getClientName();
        String numberofTransaction=allTransactiongettersetter.getNooftransactions();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext.getApplicationContext(), CustomTransactionReportActivity.class);
                mContext.startActivity(intent);

            }
        });


        if(!"null".equalsIgnoreCase(clientName) && clientName!=null)
        {
            holder.clientName.setText(clientName);
        }

        else
        {
            holder.clientName.setText("N/A");

        }

        if(!"null".equalsIgnoreCase(numberofTransaction)&&numberofTransaction!=null)
        {
            holder.nooftransactions.setText(numberofTransaction);
        }
        else
        {
            holder.nooftransactions.setText("0");


        }
    }



    public CustomTransactionDetailAdapter(ArrayList<CustomTransactionlistgettersetter> GetterSetterArrayList , Context context) {
        this.todayTransactiongettersetters= GetterSetterArrayList;
        this.mContext = context;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {


        private  TextView clientName,nooftransactions;
        public MyViewHolder(View itemView) {
            super(itemView);


            clientName=itemView.findViewById(R.id.custom_client_name);
            nooftransactions=itemView.findViewById(R.id._custom_numberoftransaction);

            itemView.setTag(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // if (clickListener != null) clickListener.onClick(v, getAdapterPosition());

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return todayTransactiongettersetters.size();
    }


}
