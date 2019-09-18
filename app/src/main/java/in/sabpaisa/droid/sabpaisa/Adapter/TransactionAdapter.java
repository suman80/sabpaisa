package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Model.TransactionDetail;
import in.sabpaisa.droid.sabpaisa.R;

/**
 * Created by abc on 17-06-2017.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {
    Context context;
    ArrayList<TransactionDetail> transactionDetails;

    public TransactionAdapter(Context context, ArrayList<TransactionDetail> transactionDetails) {
        this.context = context;
        this.transactionDetails = transactionDetails;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TransactionDetail transactionDetail = transactionDetails.get(position);
        holder.userName.setText(transactionDetail.getUserName());

        holder.userUPI.setText(transactionDetail.getUserUpi());
        holder.txnAmount.setText(""+transactionDetail.getAmount());
        if (transactionDetail.isStatus()) {
            holder.txnStatus.setText("Success");
            holder.transactionStatus.setImageResource(R.drawable.ic_txn_success);
        }else{
            holder.txnStatus.setText("Pending");
            holder.transactionStatus.setImageResource(R.drawable.ic_txn_fail);
        }
        holder.txnDate.setText(transactionDetail.getDate());
        holder.txnTime.setText(transactionDetail.getTime());
        if (position%2!=0) {
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.main_screen_bottom_color));
        }
        else{
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return transactionDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView transactionStatus;
        TextView userName,userUPI,txnAmount,txnStatus,txnDate,txnTime;
        ;


        RelativeLayout background;
        public MyViewHolder(View itemView) {
            super(itemView);
            transactionStatus = (ImageView)itemView.findViewById(R.id.iv_transactionStatus);
            userName = (TextView)itemView.findViewById(R.id.tv_userName);
            userUPI = (TextView)itemView.findViewById(R.id.tv_userUPI);
            txnAmount = (TextView)itemView.findViewById(R.id.tv_txnAmount);
            txnStatus = (TextView)itemView.findViewById(R.id.tv_txnStatus);
            txnDate = (TextView)itemView.findViewById(R.id.tv_txnDate);
            txnTime = (TextView)itemView.findViewById(R.id.tv_txnTime);
            background = (RelativeLayout)itemView.findViewById(R.id.rl_background);
        }
    }
}
