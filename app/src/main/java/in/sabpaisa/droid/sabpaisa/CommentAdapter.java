package in.sabpaisa.droid.sabpaisa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.InputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.sabpaisa.droid.sabpaisa.Util.ProfileNavigationActivity;


public class CommentAdapter extends
        RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private List<CommentData> commentList;
    ImageLoader imageloader = AppController.getInstance().getImageLoader();
Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView main_feed_creation_time;
        public TextView main_feed_group_description;
        public TextView main_feed_comment_username;
        public ImageView main_feed_comment_image;

        public MyViewHolder(View view) {
            super(view);
            main_feed_creation_time = (TextView) view.findViewById(R.id.main_feed_creation_time);
            main_feed_comment_username = (TextView) view.findViewById(R.id.username);
            main_feed_group_description = (TextView) view.findViewById(R.id.main_feed_group_description);
            main_feed_comment_image = (ImageView) view.findViewById(R.id.people_name_initial);
        }
    }

    public CommentAdapter(List<CommentData> countryList,Context context) {
        this.commentList = countryList;
        this.mContext=context;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CommentData commentData = commentList.get(position);
        Log.d("commentData"," "+commentData.getCommentName());
        Log.d("commentData"," "+commentData.getCommentText());
        holder.main_feed_comment_username.setText(commentData.getCommentName());
        holder.main_feed_creation_time.setText(commentData.getComment_date());
        holder.main_feed_comment_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(mContext,MembersProfile.class);

                //intent.putExtra("",member_getterSetter.getFullName());
                mContext.startActivity(intent);*/
            }
        });

        holder.main_feed_comment_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(mContext,MembersProfile.class);
                mContext.startActivity(intent);*/
            }
        });
        //if (commentData.getCommentText()==null)
/*
        {
           // Toast.makeText("comment", "clicking the toolbar!", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder =new AlertDialog.Builder(CommentAdapter.this);
            builder.setTitle("No internet Connection");
            builder.setMessage("Please turn on internet connection to continue");
            builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }*/
        //StringEscapeUtils.unescapeJava(commentData.getCommentText());
        holder.main_feed_group_description.setText(StringEscapeUtils.unescapeJava(commentData.getCommentText()));
       // holder.main_feed_comment_image.setImageURI(Uri.parse(commentData.getUserImageUrl().toString()));

        Glide.with( mContext)
                .load(commentData.getUserImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.default_users)
                .into(holder.main_feed_comment_image);
       // holder.main_feed_comment_image.setImageUrl(commentData.getUserImageUrl(),imageloader);
        holder.main_feed_comment_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(mContext,MembersProfile.class);
                mContext.startActivity(intent);
*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_list_item, parent, false);
        return new MyViewHolder(v);
    }
    //Code for fetching image from server
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

}
