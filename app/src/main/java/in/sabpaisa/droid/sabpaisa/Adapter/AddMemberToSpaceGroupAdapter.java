package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Interfaces.AddMemberCallBack;
import in.sabpaisa.droid.sabpaisa.Model.MemberSpaceModel;
import in.sabpaisa.droid.sabpaisa.R;
import me.grantland.widget.AutofitTextView;

public class AddMemberToSpaceGroupAdapter extends RecyclerView.Adapter<AddMemberToSpaceGroupAdapter.MyViewHolder> {

    ArrayList<MemberSpaceModel> memberGetterSetterArrayList;
    ArrayList<MemberSpaceModel> memberGetterSetterArrayList1 = new ArrayList<>();

    Context mContext;

    ArrayList<String> selectedData = new ArrayList<>();

    static AddMemberCallBack addMemberCallBack;


    public AddMemberToSpaceGroupAdapter(ArrayList<MemberSpaceModel> memberGetterSetterArrayList, Context context) {
        this.memberGetterSetterArrayList = memberGetterSetterArrayList;
        this.mContext = context;
        this.addMemberCallBack = (AddMemberCallBack) context;

    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_member_to_grp_custom, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder( final MyViewHolder holder, int position) {

        final MemberSpaceModel member_getterSetter = memberGetterSetterArrayList.get(position);
        holder.memberName.setText(member_getterSetter.getFullName());

        Glide.with(mContext)
                .load(member_getterSetter.getUserImageUrl())
                .error(R.drawable.default_users)
                .into(holder.memberImg);




        holder.addMemberCheckBox.setChecked(member_getterSetter.isSelected());


        holder.setItemClickListener(new MyViewHolder.ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                CheckBox myCheckBox= (CheckBox) v;

                if(myCheckBox.isChecked()) {
                    member_getterSetter.setSelected(true);
                    memberGetterSetterArrayList1.add(member_getterSetter);
                    selectedData.add(member_getterSetter.getContactNumber());
                    addMemberCallBack.setMemberData(selectedData);
                }
                else if(!myCheckBox.isChecked()) {
                    member_getterSetter.setSelected(false);
                    memberGetterSetterArrayList1.remove(member_getterSetter);
                    selectedData.remove(member_getterSetter.getContactNumber());
                }

                if (selectedData.isEmpty()){
                    addMemberCallBack.setMemberData(selectedData);
                }



            }
        });



    }

    @Override
    public int getItemCount() {
        return memberGetterSetterArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView memberImg;

        public AutofitTextView memberName;

        CheckBox addMemberCheckBox;

        ItemClickListener itemClickListener;

        MaterialRippleLayout rippleClick;

        public MyViewHolder(View itemView) {
            super(itemView);
            memberImg = (ImageView) itemView.findViewById(R.id.memberImg);
            memberName = (AutofitTextView) itemView.findViewById(R.id.memberName);
            rippleClick = (MaterialRippleLayout) itemView.findViewById(R.id.rippleClick);
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
