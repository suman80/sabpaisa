package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import in.sabpaisa.droid.sabpaisa.Interfaces.AddMemberCallBack;
import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.Model.Member_GetterSetter;
import in.sabpaisa.droid.sabpaisa.R;
import me.grantland.widget.AutofitTextView;

public class AddMemberTo_A_GroupAdapter extends RecyclerView.Adapter<AddMemberTo_A_GroupAdapter.MyViewHolder> {

    ArrayList<Member_GetterSetter> memberGetterSetterArrayList;
    ArrayList<Member_GetterSetter> memberGetterSetterArrayList1 = new ArrayList<>();

    Context mContext;

    ArrayList<String> selectedData = new ArrayList<>();

    static AddMemberCallBack addMemberCallBack;


    public AddMemberTo_A_GroupAdapter(ArrayList<Member_GetterSetter> memberGetterSetterArrayList, Context context) {
        this.memberGetterSetterArrayList = memberGetterSetterArrayList;
        this.mContext = context;
        this.addMemberCallBack = (AddMemberCallBack) context;

    }



    @Override
    public AddMemberTo_A_GroupAdapter.MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_member_to_grp_custom, parent, false);
        return new AddMemberTo_A_GroupAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder( final AddMemberTo_A_GroupAdapter.MyViewHolder holder, int position) {

        final Member_GetterSetter member_getterSetter = memberGetterSetterArrayList.get(position);
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
                    selectedData.add(member_getterSetter.getPhoneNumber());
                    addMemberCallBack.setMemberData(selectedData);
                }
                else if(!myCheckBox.isChecked()) {
                    member_getterSetter.setSelected(false);
                    memberGetterSetterArrayList1.remove(member_getterSetter);
                    selectedData.remove(member_getterSetter.getPhoneNumber());
                }

                if (selectedData.isEmpty()){
                    addMemberCallBack.setMemberData(selectedData);
                }



            }
        });


        /*holder.addMemberCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.addMemberCheckBox.isChecked()){
                    selectedData.add(member_getterSetter.getPhoneNumber());
                    for (String val:selectedData) {
                        Log.d("addMemberCheckBox","selectedData : "+val);
                    }
                    addMemberCallBack.setMemberData(selectedData);
                }else {
                    selectedData.remove(member_getterSetter.getPhoneNumber());
                }

            }
        });*/


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
