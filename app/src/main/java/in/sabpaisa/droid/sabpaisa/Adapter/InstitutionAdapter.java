package in.sabpaisa.droid.sabpaisa.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Model.Institution;
import in.sabpaisa.droid.sabpaisa.R;

/**
 * Created by abc on 14-06-2017.
 */

public class InstitutionAdapter extends RecyclerView.Adapter<InstitutionAdapter.MyViewHolder> {
    int count;
    ArrayList<Institution> institutions;
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_institutions_tab, parent, false);
        return new MyViewHolder(v);
    }

    public InstitutionAdapter(ArrayList<Institution> institutions) {
        this.institutions = institutions;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Institution mainFeedData = institutions.get(position);
        holder.instituteName.setText(mainFeedData.getName());
        holder.instituteLocation.setText(mainFeedData.getLocation());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView instituteLogo,institutePic;
        TextView instituteName,instituteLocation;
        public MyViewHolder(View itemView) {
            super(itemView);

            instituteLogo = (ImageView)itemView.findViewById(R.id.iv_instituteLogo);
            institutePic = (ImageView)itemView.findViewById(R.id.iv_institutePic);
            instituteName = (TextView)itemView.findViewById(R.id.tv_instituteName);
            instituteLocation = (TextView)itemView.findViewById(R.id.tv_instituteLocation);
        }
    }

    @Override
    public int getItemCount() {
        return institutions.size();
    }
}
