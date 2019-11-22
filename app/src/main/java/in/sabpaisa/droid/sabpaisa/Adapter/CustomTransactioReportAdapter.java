package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.sabpaisa.droid.sabpaisa.Model.CustomTransactionReportgettersetter;
import in.sabpaisa.droid.sabpaisa.R;

/**
 * Created by archana on 21/3/18.
 */

public  class CustomTransactioReportAdapter extends RecyclerView.Adapter<CustomTransactioReportAdapter.MyViewHolder> {
    int count;
    ArrayList<CustomTransactionReportgettersetter> customTransactiongettersetters;
    Context mContext;
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_transaction_adapter, parent, false);
        return new CustomTransactioReportAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        CustomTransactionReportgettersetter allTransactiongettersetter = customTransactiongettersetters.get(position);

        //holder.memberTimeStamp.setText(member_getterSetter.getTimestampOfJoining());
        //holder.memberImg.setImageUrl(member_getterSetter.getUserImageUrl(), imageLoader);
        holder.txnId.setText(allTransactiongettersetter.getCustomTxnId());
        holder.txnDate.setText(allTransactiongettersetter.getCustomTxnDate());
        holder.today_transactionStatus.setText(allTransactiongettersetter.getCustomTxnStatus());
        holder.payer_name.setText(allTransactiongettersetter.getPayer_name());

        String amount=allTransactiongettersetter.getCustomTxnAmount();
        if(!"null".equalsIgnoreCase(amount)&&amount!=null)
        {
            holder.today_transactionAmount.setText(amount);

        }
        else
        {
            holder.today_transactionAmount.setText("0");

        }

       /* String x = allTransactiongettersetter.getCustomTxnDate();
        long foo = Long.parseLong(x);

        Date date = new Date(foo);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        holder.txnDate.setText(formatter.format(date));
*/
    }


    public CustomTransactioReportAdapter(ArrayList<CustomTransactionReportgettersetter> GetterSetterArrayList , Context context) {
        this.customTransactiongettersetters= GetterSetterArrayList;
        this.mContext = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView txnId,txnDate,today_transactionAmount,today_transactionStatus,payer_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            txnId= (TextView)itemView.findViewById(R.id.today_transactionId1);
            txnDate= (TextView)itemView.findViewById(R.id.today_transactionDate1);
            today_transactionAmount=itemView.findViewById(R.id.today_transactionAmount1);
            today_transactionStatus=itemView.findViewById(R.id.today_transactionStatus1);
            payer_name=itemView.findViewById(R.id.payer_name);
        }
    }

    @Override
    public int getItemCount() {
        return customTransactiongettersetters.size();
    }
}
