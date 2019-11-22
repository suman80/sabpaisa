package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Model.TodayTransactiongettersetter;
import in.sabpaisa.droid.sabpaisa.R;

/**
 * Created by archana on 21/3/18.
 */

public  class TodayTransactionAdapter extends RecyclerView.Adapter<TodayTransactionAdapter.MyViewHolder> {
   private int count;
   private ArrayList<TodayTransactiongettersetter> todayTransactiongettersetters;
   private Context mContext;

    @Override
    public TodayTransactionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_transaction_adapter, parent, false);
        return new TodayTransactionAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TodayTransactionAdapter.MyViewHolder holder, int position) {

        TodayTransactiongettersetter allTransactiongettersetter = todayTransactiongettersetters.get(position);

        holder.txnId.setText(allTransactiongettersetter.getTxnId());
        holder.today_transactionStatus.setText(allTransactiongettersetter.getTxnStatus());
        holder.txnDate.setText(allTransactiongettersetter.getTxnDate());
        holder.payerName.setText(allTransactiongettersetter.getPayerName());

        String amount=allTransactiongettersetter.getAmount();
        if(!"null".equalsIgnoreCase(amount)&&amount!=null)
        {
            holder.today_transactionAmount.setText(amount);

        }
        else
        {
            holder.today_transactionAmount.setText("0");

        }

       /* String x = allTransactiongettersetter.getTxnDate();
        long foo = Long.parseLong(x);

        Date date = new Date(foo);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        holder.txnDate.setText(formatter.format(date));
*/
    }

    public TodayTransactionAdapter(ArrayList<TodayTransactiongettersetter> GetterSetterArrayList , Context context) {
        this.todayTransactiongettersetters= GetterSetterArrayList;
        this.mContext = context;
    }


    /*

     */
    /*START Method to change data when put query in searchBar*//*

    public void setItems(ArrayList<AllTransactiongettersetter> memberDatas) {
        this.allTransactiongettersetters = memberDatas;
    }
*/


    public class MyViewHolder extends RecyclerView.ViewHolder {


       private TextView txnId,txnDate,today_transactionAmount,today_transactionStatus,payerName;
        public MyViewHolder(View itemView) {
            super(itemView);

            txnId= (TextView)itemView.findViewById(R.id.today_transactionId);
            txnDate= (TextView)itemView.findViewById(R.id.today_transactionDate);
            today_transactionAmount=itemView.findViewById(R.id.today_transactionAmount);
            today_transactionStatus=itemView.findViewById(R.id.today_transactionStatus);
            payerName=itemView.findViewById(R.id.payerName);
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
