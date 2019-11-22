package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.sabpaisa.droid.sabpaisa.Model.AllTransactiongettersetter;
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
        holder.status.setText(allTransactiongettersetter.getPaymentStatus());
        holder.spTranscationId.setText(allTransactiongettersetter.getSpTranscationId());




        DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");

        Date result = new Date(allTransactiongettersetter.getTranscationDate());

        holder.transcationDate.setText(simple.format(result));

        Log.d("date_login",""+allTransactiongettersetter.getTranscationDate());
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


     TextView id,clientName,spTranscationId,transcationDate,paidAmount,status;
       /* public ImageView memberImg;
        Button memberChat;
        public TextView memberName;
        TextView memberTimeStamp;*/
        public MyViewHolder(View itemView) {
            super(itemView);


            transcationDate= (TextView)itemView.findViewById(R.id.date);
            paidAmount= (TextView)itemView.findViewById(R.id.txnamount);
            spTranscationId = (TextView)itemView.findViewById(R.id.txn);
            status = (TextView)itemView.findViewById(R.id.txnstts);
        }
    }

    @Override
    public int getItemCount() {
        return allTransactiongettersetters.size();
    }
}
