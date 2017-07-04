package in.sabpaisa.droid.sabpaisa;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class MainGroupAdapter1 extends
        RecyclerView.Adapter<MainGroupAdapter1.MyViewHolder> {
    Context mContext;
    private List<GroupListData> countryList;

    public MainGroupAdapter1(List<GroupListData> countryList) {
        this.countryList = countryList;
    }

    /*START Method to change data when put query in searchBar*/
    public void setItems(List<GroupListData> groupDatas) {
        this.countryList = groupDatas;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //     TextView text =holder.join;

        GroupListData c = countryList.get(position);
        holder.groupName.setText(c.getGroupName());
        holder.groupDescription.setText(c.getGroupDescription());
        holder.group_Count.setText(c.getGroupCount());
        //TextView text =holder.join;


        // holder.join.setText(c.getJoin());
    }
    /*END Method to change data when put query in searchBar*/

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_item_list, parent, false);
        return new MyViewHolder(v);
    }

    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView groupName;
        public TextView groupDescription;
        public TextView group_Count;




        public MyViewHolder(View view) {
            super(view);
            groupName = (TextView) view.findViewById(R.id.main_group_name);
            groupDescription = (TextView) view.findViewById(R.id.main_group_group_description);

            group_Count =(TextView)view.findViewById(R.id.main_feed_creation_time);
            /*join.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder.setTitle("Delete entry")
                            .setMessage("Are you sure you want to delete this entry?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                    //Toast.makeText(, "You clicked on OK", Toast.LENGTH_SHORT).show();

                }
            });*/

        }
    }
}
