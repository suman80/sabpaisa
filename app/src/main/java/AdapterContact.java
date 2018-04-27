import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class AdapterContact extends  RecyclerView.Adapter<AdapterContact.MyViewHolder> {


    @NonNull
    @Override
    public AdapterContact.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;



    }

    @Override
    public void onBindViewHolder(@NonNull AdapterContact.MyViewHolder myViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
            public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
