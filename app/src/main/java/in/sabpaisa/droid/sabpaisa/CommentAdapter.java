package in.sabpaisa.droid.sabpaisa;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class CommentAdapter extends
        RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private List<CommentData> commentList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView main_feed_creation_time;
        public TextView main_feed_group_description;

        public MyViewHolder(View view) {
            super(view);
            main_feed_creation_time = (TextView) view.findViewById(R.id.main_feed_creation_time);
            main_feed_group_description = (TextView) view.findViewById(R.id.main_feed_group_description);
        }
    }

    public CommentAdapter(List<CommentData> countryList) {
        this.commentList = countryList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CommentData commentData = commentList.get(position);
        Log.d("commentData"," "+commentData.getCommentName());
        Log.d("commentData"," "+commentData.getCommentText());
        holder.main_feed_creation_time.setText(commentData.getComment_date());
        holder.main_feed_group_description.setText(commentData.getCommentText());
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
}
