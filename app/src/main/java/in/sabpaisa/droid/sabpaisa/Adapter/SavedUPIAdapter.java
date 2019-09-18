package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Model.SavedUPI;
import in.sabpaisa.droid.sabpaisa.R;

/**
 * Created by abc on 16-06-2017.
 */

public class SavedUPIAdapter extends RecyclerView.Adapter<SavedUPIAdapter.MyViewHolder> {
    ArrayList<SavedUPI> savedUPIs;
    Context context;
    public interface UPIResponse {
        void onUpiSelect(String upi);
    }

    public UPIResponse selectedUPI;

    public SavedUPIAdapter(Context context, ArrayList<SavedUPI> savedUPIs) {
        this.savedUPIs = savedUPIs;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_upi, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        SavedUPI savedUPI = savedUPIs.get(position);
        holder.upi.setText(savedUPI.getUPI());
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
                selectedUPI.onUpiSelect(holder.upi.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return savedUPIs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout background;
        TextView upi;
        public MyViewHolder(View itemView) {
            super(itemView);
            background = (LinearLayout)itemView.findViewById(R.id.ll_background);
            upi = (TextView)itemView.findViewById(R.id.tv_savedupi);
        }
    }
}
