package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Model.Bank;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.SendAccountIFSC;

/**
 * Created by abc on 16-06-2017.
 */

public class AccountIFSCAdapter extends RecyclerView.Adapter<AccountIFSCAdapter.MyViewHolder> {


    Context context;
    ArrayList<Bank> banks;
    public AccountIFSCAdapter(Context context,ArrayList<Bank> banks){
        this.banks = banks;
        this.context = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account_ifsc, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Bank bank = banks.get(position);
        holder.bankName.setText(bank.getName());
        holder.bankLogo.setImageDrawable(bank.getLogo());
        if (position%2==0) {
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.main_screen_bottom_color));
        }
        else{
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        holder.background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = (Bitmap)((BitmapDrawable) bank.getLogo()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] b = baos.toByteArray();

                Intent intent = new Intent(context, SendAccountIFSC.class);
                intent.putExtra("IFSC",bank.getIfsc());
                intent.putExtra("Logo",b);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return banks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView bankName;
        ImageView bankLogo;
        LinearLayout background;
        public MyViewHolder(View itemView) {
            super(itemView);
            bankName = (TextView)itemView.findViewById(R.id.tv_bankName);
            bankLogo = (ImageView)itemView.findViewById(R.id.iv_bankLogo);
            background = (LinearLayout)itemView.findViewById(R.id.ll_background);
        }
    }
}
