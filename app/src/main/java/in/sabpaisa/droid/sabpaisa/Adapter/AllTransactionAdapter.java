package in.sabpaisa.droid.sabpaisa.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Model.AllTransactiongettersetter;
import in.sabpaisa.droid.sabpaisa.Model.Member_GetterSetter;
import in.sabpaisa.droid.sabpaisa.R;

/**
 * Created by archana on 21/3/18.
 */

public class AllTransactionAdapter  extends RecyclerView.Adapter<AllTransactionAdapter.MyViewHolder> {
    int count;
    ArrayList<AllTransactiongettersetter> allTransactiongettersetters;
    Context mContext;
    @Override
    public AllTransactionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.alltxnlist_item, parent, false);
        return new AllTransactionAdapter.MyViewHolder(v);
    }





    @Override
    public void onBindViewHolder(AllTransactionAdapter.MyViewHolder holder, int position) {

        AllTransactiongettersetter allTransactiongettersetter = allTransactiongettersetters.get(position);
        holder.paidAmount.setText(allTransactiongettersetter.getPaidAmount());
        holder.spTranscationId.setText(allTransactiongettersetter.getSpTranscationId());
        holder.transcationDate.setText(allTransactiongettersetter.getTranscationDate());
        //holder.memberTimeStamp.setText(member_getterSetter.getTimestampOfJoining());
        //holder.memberImg.setImageUrl(member_getterSetter.getUserImageUrl(), imageLoader);


    }




    public AllTransactionAdapter(ArrayList<AllTransactiongettersetter> GetterSetterArrayList , Context context) {
        this.allTransactiongettersetters= GetterSetterArrayList;
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


     TextView id,clientName,spTranscationId,transcationDate,paidAmount;
       /* public ImageView memberImg;
        Button memberChat;
        public TextView memberName;
        TextView memberTimeStamp;*/
        public MyViewHolder(View itemView) {
            super(itemView);


            transcationDate= (TextView)itemView.findViewById(R.id.date);
            paidAmount= (TextView)itemView.findViewById(R.id.txnamnt);
            spTranscationId = (TextView)itemView.findViewById(R.id.txn);
        }
    }

    @Override
    public int getItemCount() {
        return allTransactiongettersetters.size();
    }
}
