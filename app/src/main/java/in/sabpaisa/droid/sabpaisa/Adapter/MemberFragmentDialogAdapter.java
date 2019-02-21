package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

import in.sabpaisa.droid.sabpaisa.Interfaces.AddMemberCallBack;
import in.sabpaisa.droid.sabpaisa.Model.ContactVO;
import in.sabpaisa.droid.sabpaisa.Model.Member_GetterSetter;
import in.sabpaisa.droid.sabpaisa.R;

public class MemberFragmentDialogAdapter extends RecyclerView.Adapter<MemberFragmentDialogAdapter.ContactViewHolder> {

    private ArrayList<ContactVO> contactVOList;

    private Context mContext;

    ArrayList<String> selectedData = new ArrayList<>();

    static AddMemberCallBack addMemberCallBack;

    Fragment fragment;

    ArrayList<ContactVO> memberGetterSetterArrayList1 = new ArrayList<>();


    public MemberFragmentDialogAdapter(ArrayList<ContactVO> contactVOList, Context mContext, Fragment fragment) {
        this.contactVOList = contactVOList;
        this.mContext = mContext;
        this.fragment=fragment;
        this.addMemberCallBack = (AddMemberCallBack) fragment;
    }

    @Override
    public MemberFragmentDialogAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_member_frag_dialog, null);
        MemberFragmentDialogAdapter.ContactViewHolder contactViewHolder = new MemberFragmentDialogAdapter.ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(MemberFragmentDialogAdapter.ContactViewHolder holder, int position) {
       final ContactVO contactVO = contactVOList.get(position);



        if (contactVO.getInviteButtonVisibility()==1){

            holder.tvContactName.setText(contactVO.getContactName());

            holder.tvPhoneNumber.setText(contactVO.getContactNumber());
            Log.d("contactVO", String.valueOf(contactVO.getInviteButtonVisibility()));
            Log.d("contactVOhuihu", String.valueOf(contactVO.getContactName()));


        }


        holder.addMemberCheckBox.setChecked(contactVO.isSelected());

        holder.setItemClickListener(new ContactViewHolder.ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                CheckBox myCheckBox= (CheckBox) v;

                if(myCheckBox.isChecked()) {
                    contactVO.setSelected(true);
                    //memberGetterSetterArrayList1.add(contactVO);
                    selectedData.add(contactVO.getContactNumber());
                    addMemberCallBack.setMemberData(selectedData);
                }
                else if(!myCheckBox.isChecked()) {
                    contactVO.setSelected(false);
                    //memberGetterSetterArrayList1.remove(contactVO);
                    selectedData.remove(contactVO.getContactNumber());
                }

                if (selectedData.isEmpty()){
                    addMemberCallBack.setMemberData(selectedData);
                }



            }
        });


    }

    @Override
    public int getItemCount() {
        return contactVOList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        TextView tvContactName;
        TextView tvPhoneNumber;
        CheckBox addMemberCheckBox;
        ItemClickListener itemClickListener;

        public ContactViewHolder(View itemView) {
            super(itemView);

            tvContactName = (TextView) itemView.findViewById(R.id.name);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.number);
            addMemberCheckBox = (CheckBox) itemView.findViewById(R.id.addMemberCheckBox);
            addMemberCheckBox.setOnClickListener(this);


        }


        public void setItemClickListener(ItemClickListener ic)
        {
            this.itemClickListener=ic;
        }
        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());
        }

        interface ItemClickListener {

            void onItemClick(View v,int pos);
        }




    }
}