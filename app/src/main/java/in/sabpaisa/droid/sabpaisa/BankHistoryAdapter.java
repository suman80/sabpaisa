package in.sabpaisa.droid.sabpaisa;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SabPaisa on 27-07-2017.
 */

public class BankHistoryAdapter extends RecyclerView.Adapter<BankHistoryAdapter.MyViewHolder> {
    Context context;
    ArrayList<BankHistory> bankHistoryArrayList;

    public BankHistoryAdapter(Context context, ArrayList<BankHistory> bankHistoryArrayList) {
        this.context = context;
        this.bankHistoryArrayList = bankHistoryArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bank_history, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BankHistory bankHistory = bankHistoryArrayList.get(position);
        holder.formName.setText(bankHistory.getFormName());
        holder.formAmount.setText("" + bankHistory.getAmount());
    }

    @Override
    public int getItemCount() {
        return bankHistoryArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView formName, formAmount;

        public MyViewHolder(View itemView) {
            super(itemView);
            formName = (TextView) itemView.findViewById(R.id.tv_formName);
            formAmount = (TextView) itemView.findViewById(R.id.tv_formAmount);
        }
    }
}
