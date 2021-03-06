package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import in.sabpaisa.droid.sabpaisa.Model.NotificationModelClass;
import in.sabpaisa.droid.sabpaisa.NotificationPopUPActivity;
import in.sabpaisa.droid.sabpaisa.Proceed_Feed_FullScreen;
import in.sabpaisa.droid.sabpaisa.Proceed_Group_FullScreen;
import in.sabpaisa.droid.sabpaisa.R;

public class NotificationAdapter extends
            RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    NotificationPopUPActivity notificationPopUPActivity = new NotificationPopUPActivity();
       Context mContext;
List<NotificationModelClass> notificationModelClassList;

    public NotificationAdapter(ArrayList<NotificationModelClass> notificationModelClassArrayList, Context context) {
        this.notificationModelClassList=notificationModelClassArrayList;
        this.mContext=context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

public TextView  groupNAme,Count,type;
RelativeLayout NotificationRecycleview;
        public MyViewHolder(View view) {
            super(view);

            groupNAme=(TextView)view.findViewById(R.id.groupname);
            Count=(TextView)view.findViewById(R.id.noOfCount);
            type=(TextView)view.findViewById(R.id.F);
            NotificationRecycleview = (RelativeLayout)view.findViewById(R.id.NotificationRecycleview);
             }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notificationpopup_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final NotificationModelClass notificationModelClass=notificationModelClassList.get(position);

holder.Count.setText(notificationModelClass.getCount());
holder.groupNAme.setText(notificationModelClass.getName());
        Log.d("count=group",String.valueOf(notificationModelClass.getCount().toString())+"   "+notificationModelClassList.size());
        if(notificationModelClassList.size()>0) {
            if (notificationModelClass.getIdentify().equals("Group")) {
                holder.type.setText("G");
                holder.type.setAllCaps(true);
                final String popup = "Notification";
/*
                holder.groupNAme.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(mContext, Proceed_Group_FullScreen.class);
                        NotificationModelClass recent = notificationModelClassList.get(position);
                        notificationModelClassList.remove(recent);
                  *//*
                        notifyItemRemoved(position);
                        //this line below gives you the animation and also updates the
                        //list items after the deleted item
                        notifyItemRangeChanged(position, getItemCount());*//*
                        //   notificationPopUPActivity.groupMap.remove(notificationModelClass);

                        //notifyItemRemoved(position+1);
//                       intent.putExtra("popup", popup);
                        notifyDataSetChanged();
                        intent.putExtra("groupText", notificationModelClass.getDescription().toString());
                        intent.putExtra("groupImage", notificationModelClass.getImagePath().toString());
                        intent.putExtra("groupName", notificationModelClass.getName().toString());
                        intent.putExtra("groupId", notificationModelClass.getId());
                        mContext.startActivity(intent);

                    }
                });
                holder.Count.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NotificationModelClass recent = notificationModelClassList.get(position);

                        Intent intent = new Intent(mContext, Proceed_Group_FullScreen.class);
                        notificationModelClassList.remove(recent);
                   *//*  ;
                        notifyItemRemoved(position);
                        //this line below gives you the animation and also updates the
                        //list items after the deleted item
                        notifyItemRangeChanged(position, getItemCount());
                        //    notificationPopUPActivity.groupMap.remove(notificationModelClass);
                     //   notifyItemRemoved(position+1);
                       // *//*
                        notifyDataSetChanged();
                        intent.putExtra("groupText", notificationModelClass.getDescription().toString());
                        intent.putExtra("popup", popup);
                        intent.putExtra("groupImage", notificationModelClass.getImagePath().toString());
                        intent.putExtra("groupName", notificationModelClass.getName().toString());
                        intent.putExtra("groupId", notificationModelClass.getId());
                        mContext.startActivity(intent);

                    }
                });*/


                holder.NotificationRecycleview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NotificationModelClass recent = notificationModelClassList.get(position);

                        Intent intent = new Intent(mContext, Proceed_Group_FullScreen.class);
                        notificationModelClassList.remove(recent);
                        notifyDataSetChanged();
                        intent.putExtra("groupText", notificationModelClass.getDescription().toString());
                        intent.putExtra("popup", popup);
                        intent.putExtra("groupImage", notificationModelClass.getImagePath().toString());
                        intent.putExtra("groupName", notificationModelClass.getName().toString());
                        intent.putExtra("groupId", notificationModelClass.getId());
                        mContext.startActivity(intent);

                    }
                });

            } else if (notificationModelClass.getIdentify().equals("Feed")) {
                final String popup = "Notification";

                holder.type.setText("F");
/*                holder.groupNAme.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(View view) {
                        NotificationModelClass recent = notificationModelClassList.get(position);

                        Intent intent = new Intent(mContext, Proceed_Feed_FullScreen.class);
                        notificationModelClassList.remove(recent);
                    *//*  notificationModelClassList.remove(position);
                        notifyItemRemoved(position);
                        //this line below gives you the animation and also updates the
                        //list items after the deleted item
                        notifyItemRangeChanged(position, getItemCount());
                        // notificationPopUPActivity.feedMap.remove(notificationModelClass);

                       //notifyItemRemoved(position+1);
                       notifyDataSetChanged();*//*
                        notifyDataSetChanged();
                        intent.putExtra("feedText", notificationModelClass.getDescription().toString());
                        intent.putExtra("feedImage", notificationModelClass.getImagePath().toString());
                        intent.putExtra("feedName", notificationModelClass.getName().toString());
                        intent.putExtra("feedId", notificationModelClass.getId());
                        intent.putExtra("popup", popup);
                        mContext.startActivity(intent);

                    }
                });
                holder.Count.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NotificationModelClass recent = notificationModelClassList.get(position);

                        Intent intent = new Intent(mContext, Proceed_Feed_FullScreen.class);
                        notificationModelClassList.remove(recent);
                  *//*
                     notifyItemRemoved(position);
                        //this line below gives you the animation and also updates the
                        //list items after the deleted item
                        notifyItemRangeChanged(position, getItemCount());
                        //notificationPopUPActivity.feedMap.remove(notificationModelClass);
                      //  notifyItemRemoved(position+1);*//*
                  notifyDataSetChanged();
                        intent.putExtra("feedText", notificationModelClass.getDescription().toString());
                        intent.putExtra("feedImage", notificationModelClass.getImagePath().toString());
                        intent.putExtra("feedName", notificationModelClass.getName().toString());
                        intent.putExtra("feedId", notificationModelClass.getId());
                        intent.putExtra("popup", popup);
                        mContext.startActivity(intent);

                    }
                });*/

                holder.NotificationRecycleview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NotificationModelClass recent = notificationModelClassList.get(position);

                        Intent intent = new Intent(mContext, Proceed_Feed_FullScreen.class);
                        notificationModelClassList.remove(recent);
                  /*
                     notifyItemRemoved(position);
                        //this line below gives you the animation and also updates the
                        //list items after the deleted item
                        notifyItemRangeChanged(position, getItemCount());
                        //notificationPopUPActivity.feedMap.remove(notificationModelClass);
                      //  notifyItemRemoved(position+1);*/
                        notifyDataSetChanged();
                        intent.putExtra("feedText", notificationModelClass.getDescription().toString());
                        intent.putExtra("feedImage", notificationModelClass.getImagePath().toString());
                        intent.putExtra("feedName", notificationModelClass.getName().toString());
                        intent.putExtra("feedId", notificationModelClass.getId());
                        intent.putExtra("popup", popup);
                        mContext.startActivity(intent);

                    }
                });

            }
        }
    }

    @Override
    public int getItemCount() {
        return notificationModelClassList.size();
    }

    public void removeItem(int position) {
        notificationModelClassList.remove(position);
        notifyItemRemoved(position);
        // Add whatever you want to do when removing an Item
    }
}