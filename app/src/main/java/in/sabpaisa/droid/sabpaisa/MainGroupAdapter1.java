package in.sabpaisa.droid.sabpaisa;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;


public class MainGroupAdapter1 extends RecyclerView.Adapter<MainGroupAdapter1.MyViewHolder> {
    Context mContext;
    private List<GroupListData> countryList;

    public MainGroupAdapter1(List<GroupListData> countryList) {
        this.countryList = countryList;
    }

    /*START Method to change data when put query in searchBar*/
    public void setItems(List<GroupListData> groupDatas) {
        this.countryList = groupDatas;
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //     TextView text =holder.join;

        final GroupListData groupListData  = countryList.get(position);
        holder.Group_name.setText(groupListData.getGroupName());
        holder.Group_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Proceed_Group_FullScreen.class);
                intent.putExtra("groupName",groupListData.getGroupName());
                intent.putExtra("groupText",groupListData.getGroupText());
                intent.putExtra("groupImage",groupListData.getImagePath());
                v.getContext().startActivity(intent);
            }
        });
        holder.Group_description.setText(groupListData.getGroupText());
        holder.Group_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Proceed_Group_FullScreen.class);
                intent.putExtra("groupName",groupListData.getGroupName());
                intent.putExtra("groupText",groupListData.getGroupText());
                intent.putExtra("groupImage",groupListData.getImagePath());
                v.getContext().startActivity(intent);
            }
        });
        new DownloadImageTask(holder.Group_Image).execute(groupListData.getImagePath());
        holder.Group_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Proceed_Group_FullScreen.class);
                intent.putExtra("groupName",groupListData.getGroupName());
                intent.putExtra("groupText",groupListData.getGroupText());
                intent.putExtra("groupImage",groupListData.getImagePath());
                v.getContext().startActivity(intent);
            }
        });
        new DownloadLogoTask(holder.Group_Logo).execute(groupListData.getLogoPath());

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
        public TextView Group_name;
        public TextView Group_description;
        public ImageView Group_Logo;
        public ImageView Group_Image;



        public MyViewHolder(View view) {
            super(view);
            Group_name = (TextView) view.findViewById(R.id.Group_name);
            Group_description = (TextView) view.findViewById(R.id.Group_description);
            Group_Logo = (ImageView) view.findViewById(R.id.Group_Logo);
            Group_Image = (ImageView) view.findViewById(R.id.Group_Image);


        }
    }


    //Code for fetching image from server
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loading.show();
        }

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            //loading.dismiss();
        }

    }

    //Code for fetching image from server
    private class DownloadLogoTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loading.show();
        }

        public DownloadLogoTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            //loading.dismiss();
        }

    }


}
