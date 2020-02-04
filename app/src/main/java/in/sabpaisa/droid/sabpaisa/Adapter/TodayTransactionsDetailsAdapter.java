package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.sabpaisa.droid.sabpaisa.Model.TodayTransactiongettersetter;
import in.sabpaisa.droid.sabpaisa.Model.TodayTransactionlistgettersetter;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.TodayTransactionActivity;
/**
 * Created by archana on 21/3/18.
 */

public  class TodayTransactionsDetailsAdapter extends RecyclerView.Adapter<TodayTransactionsDetailsAdapter.MyViewHolder> {
    int count;
    List<TodayTransactionlistgettersetter> todayTransactiongettersetters;
    List<TodayTransactiongettersetter> list;

    Context mContext;


    @Override
    public TodayTransactionsDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_today_transaction_adapter, parent, false);
        return new TodayTransactionsDetailsAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final TodayTransactionsDetailsAdapter.MyViewHolder holder, int position) {

        final TodayTransactionlistgettersetter allTransactiongettersetter = todayTransactiongettersetters.get(position);

        //holder.memberImg.setImageUrl(member_getterSetter.getUserImageUrl(), imageLoader);
          Log.d("alltransaction",""+allTransactiongettersetter.getTodayTransactiongettersetters());
        final List<TodayTransactiongettersetter> secondTransList=allTransactiongettersetter.getTodayTransactiongettersetters();

        Log.d("secondTransList",""+secondTransList);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(mContext.getApplicationContext(), TodayTransactionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("Birds", (ArrayList<? extends Parcelable>) secondTransList);
                intent.putExtras(bundle);
                mContext.startActivity(intent);*/

                /*Intent intent=new Intent(mContext.getApplicationContext(), TodayTransactionActivity.class);
                intent.putExtra("ArrayList",""+secondTransList);
                mContext.startActivity(intent);*/
            }
        });

        String clientName=allTransactiongettersetter.getClientName();

        Log.d("client_name second",""+allTransactiongettersetter.getTodayTransactiongettersetters());
        String  numberOftransactions=allTransactiongettersetter.getNumberoftransactions();
        if(!"null".equalsIgnoreCase(clientName)&&clientName!=null)
        {
            holder.client_name.setText(clientName);

        }
        else

        {
            holder.client_name.setText("N/A");

        }

        if(!"null".equalsIgnoreCase(numberOftransactions)&&numberOftransactions!=null)
        {
            holder.numberoftransactions.setText(numberOftransactions);

        }
        else
        {
            holder.numberoftransactions.setText("0");

        }
    }


    public TodayTransactionsDetailsAdapter(List<TodayTransactionlistgettersetter> GetterSetterArrayList , Context context) {
        this.todayTransactiongettersetters= GetterSetterArrayList;
        this.mContext = context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView client_name,numberoftransactions;
        public MyViewHolder(View itemView) {
            super(itemView);

            client_name=itemView.findViewById(R.id.client_name_today);
            numberoftransactions=itemView.findViewById(R.id.numberoftransaction_today);


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
