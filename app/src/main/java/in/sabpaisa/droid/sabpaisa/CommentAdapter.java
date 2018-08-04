package in.sabpaisa.droid.sabpaisa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.sabpaisa.droid.sabpaisa.Util.ProfileNavigationActivity;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    ImageLoader imageloader = AppController.getInstance().getImageLoader();
    Context mContext;
    Toolbar toolbar;
    private List<CommentData> commentList;
    private boolean multiSelect = false;
    private ArrayList<CommentData> selectedItems = new ArrayList<>();
    private android.support.v7.view.ActionMode.Callback actionModeCallbacks = new android.support.v7.view.ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(android.support.v7.view.ActionMode mode, Menu menu) {
            multiSelect = true;
            menu.add("Share");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(android.support.v7.view.ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(android.support.v7.view.ActionMode mode, MenuItem item) {

            /*for (CommentData Item : selectedItems) {
                commentList.remove(Item);
                Toast.makeText(mContext, Item.getCommentText(), Toast.LENGTH_LONG).show();

            }*/

            Log.d("selectedItems"," "+selectedItems);

            Intent intent = new Intent(mContext, SharingActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("SELECTED_LIST", selectedItems);
            intent.putExtras(bundle);
            mContext.startActivity(intent);

            toolbar.setVisibility(View.VISIBLE);
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(android.support.v7.view.ActionMode mode) {
            multiSelect = false;
            selectedItems.clear();
            toolbar.setVisibility(View.VISIBLE);
            notifyDataSetChanged();
        }
    };
/////////////////////////////////////////////////////////////////////////////////////////////////////

    public CommentAdapter(List<CommentData> countryList, Context context, Toolbar toolbar) {
        this.commentList = countryList;
        this.mContext = context;
        this.toolbar = toolbar;
    }
    public CommentAdapter(List<CommentData> countryList, Context context) {
        this.commentList = countryList;
        this.mContext = context;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CommentData commentData = commentList.get(position);
        Log.d("commentData", " " + commentData.getCommentName());
        Log.d("commentData", " " + commentData.getCommentText());
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

        Glide.with(mContext)
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

        holder.update(commentList.get(position));

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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView main_feed_creation_time;
        public TextView main_feed_group_description;
        public TextView main_feed_comment_username;
        public ImageView main_feed_comment_image;
        public RelativeLayout relative_comments;

        public MyViewHolder(View view) {
            super(view);
            main_feed_creation_time = (TextView) view.findViewById(R.id.main_feed_creation_time);
            main_feed_comment_username = (TextView) view.findViewById(R.id.username);
            main_feed_group_description = (TextView) view.findViewById(R.id.main_feed_group_description);
            main_feed_comment_image = (ImageView) view.findViewById(R.id.people_name_initial);
            relative_comments = (RelativeLayout) view.findViewById(R.id.relative_comments);
        }


        void selectItem(CommentData item) {
            if (multiSelect) {
                if (selectedItems.contains(item)) {
                    selectedItems.remove(item);
                    relative_comments.setBackgroundColor(Color.WHITE);
                } else {
                    selectedItems.add(item);
                    relative_comments.setBackgroundColor(Color.LTGRAY);
                }
            }
        }


        void update(final CommentData value) {
            //textView.setText(value + "");
            if (selectedItems.contains(value)) {
                relative_comments.setBackgroundColor(Color.LTGRAY);
            } else {
                relative_comments.setBackgroundColor(Color.WHITE);
            }
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ((AppCompatActivity) view.getContext()).startSupportActionMode(actionModeCallbacks);
                    //Toast.makeText(mContext,"pressed",Toast.LENGTH_SHORT).show();

                    toolbar.setVisibility(View.GONE);
                    selectItem(value);
                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectItem(value);
                }
            });

        }

    }


}
