package in.sabpaisa.droid.sabpaisa.Adapter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.olive.upi.transport.model.Account;
import com.olive.upi.transport.model.CustomerBankAccounts;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.R;

/**
 * Created by rajdeeps on 12/13/17.
 */

public class UPI_UserAccountCustomAdapter extends RecyclerView.Adapter<UPI_UserAccountCustomAdapter.MyViewHolder> {
        int count;
        ArrayList<Account> customerBankAccountsArrayList;

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
@Override
public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_account_custom, parent, false);
        return new MyViewHolder(v);
        }

public UPI_UserAccountCustomAdapter(ArrayList<Account> customerBankAccountsArrayList) {
        this.customerBankAccountsArrayList = customerBankAccountsArrayList;
        }

@RequiresApi(api = Build.VERSION_CODES.M)
@Override
public void onBindViewHolder(MyViewHolder holder, int position) {

final Account account=customerBankAccountsArrayList.get(position);
        holder.accountName.setText(account.getName());
        holder.accountNumber.setText(account.getMaskedAccnumber());
        holder.accountStatus.setText(account.getStatus());
        holder.accountVPA.setText(account.getVpa());



        }

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView accountName,accountNumber,accountStatus,accountVPA;
    public MyViewHolder(View itemView) {
        super(itemView);



        accountName = (TextView)itemView.findViewById(R.id.accountName);
        accountNumber = (TextView)itemView.findViewById(R.id.accountNumber);
        accountStatus = (TextView)itemView.findViewById(R.id.accountStatus);
        accountVPA = (TextView)itemView.findViewById(R.id.accountVPA);
    }
}

    @Override
    public int getItemCount() {
        return customerBankAccountsArrayList.size();
    }
}

