package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        public MyViewHolder(View view) {
            super(view);

            groupNAme=(TextView)view.findViewById(R.id.groupname);
            Count=(TextView)view.findViewById(R.id.noOfCount);
            type=(TextView)view.findViewById(R.id.F);
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final NotificationModelClass notificationModelClass=notificationModelClassList.get(position);

holder.Count.setText(notificationModelClass.getCount());
holder.groupNAme.setText(notificationModelClass.getName());
        Log.d("count=group",String.valueOf(notificationModelClass.getCount().toString())+"   "+notificationModelClassList.size());
if(notificationModelClass.getIdentify().equals("Group")) {
    holder.type.setText("G");
    holder.type.setAllCaps(true);
    final String   popup="Notification";

    holder.groupNAme.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(mContext, Proceed_Group_FullScreen.class);
            notificationModelClassList.remove(position);
         //   notificationPopUPActivity.groupMap.remove(notificationModelClass);

            notifyItemRemoved(position);
            intent.putExtra("popup",popup);
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
            Intent intent = new Intent(mContext, Proceed_Group_FullScreen.class);
            notificationModelClassList.remove(position);
        //    notificationPopUPActivity.groupMap.remove(notificationModelClass);
            notifyItemRemoved(position);
            intent.putExtra("groupText", notificationModelClass.getDescription().toString());
            intent.putExtra("popup",popup);
            intent.putExtra("groupImage", notificationModelClass.getImagePath().toString());
            intent.putExtra("groupName", notificationModelClass.getName().toString());
            intent.putExtra("groupId", notificationModelClass.getId());

            mContext.startActivity(intent);

        }
    });
}

else if(notificationModelClass.getIdentify().equals("Feed"))
{     final String   popup="Notification";

    holder.type.setText("F");
    holder.groupNAme.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(mContext, Proceed_Feed_FullScreen.class);
            notificationModelClassList.remove(position);
           // notificationPopUPActivity.feedMap.remove(notificationModelClass);

            notifyItemRemoved(position);
            intent.putExtra("feedText", notificationModelClass.getDescription().toString());
            intent.putExtra("feedImage", notificationModelClass.getImagePath().toString());
            intent.putExtra("feedName", notificationModelClass.getName().toString());
            intent.putExtra("feedId", notificationModelClass.getId());
            intent.putExtra("popup",popup);
            mContext.startActivity(intent);

        }
    });
    holder.Count.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, Proceed_Feed_FullScreen.class);
            notificationModelClassList.remove(position);
            //notificationPopUPActivity.feedMap.remove(notificationModelClass);
            notifyItemRemoved(position);
            intent.putExtra("feedText", notificationModelClass.getDescription().toString());
            intent.putExtra("feedImage", notificationModelClass.getImagePath().toString());
            intent.putExtra("feedName", notificationModelClass.getName().toString());
            intent.putExtra("feedId", notificationModelClass.getId());
            intent.putExtra("popup",popup);
            mContext.startActivity(intent);

        }
    });
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