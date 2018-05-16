package in.sabpaisa.droid.sabpaisa;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

/*Comment Adapter with Helper Function for pagination*/
public class CommentAdapterDatabase extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    static Context context;
    private List<CommentData> commentList;
    private boolean isLoadingAdded = false;


    public CommentAdapterDatabase(Context context) {
        commentList = new ArrayList<>();
        this.context = context;
    }

    public List<CommentData> getComments() {
        return commentList;
    }

    public void setComments(List<CommentData> commentList) {
        this.commentList = commentList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.custom_loading_list_item, parent, false);
                viewHolder = new MyLoadViewHolder(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.comment_list_item, parent, false);
        viewHolder = new MyViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case ITEM:
                ((MyViewHolder)holder).bindData(commentList.get(position));

                break;

            case LOADING:
//                Do nothing
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == commentList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    @Override
    public int getItemCount() {
        return commentList == null ? 0 : commentList.size();
    }

    public void cleanUpAdapter() {

        this.clear();
        notifyDataSetChanged(); /* Important */

    }

    public void add(CommentData r) {
        commentList.add(r);
        notifyItemInserted(commentList.size() - 1);
    }

    public void addAll(List<CommentData> moveResults) {
        for (CommentData result : moveResults) {
            add(result);
        }
    }

    public void remove(CommentData r) {
        int position = commentList.indexOf(r);
        if (position > -1) {
            commentList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new CommentData());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = commentList.size() - 1;
        CommentData result = getItem(position);

        if (result != null) {
            commentList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public CommentData getItem(int position) {
        return commentList.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView main_feed_creation_time;
        public TextView main_feed_group_description;

        public MyViewHolder(View view) {
            super(view);
            main_feed_creation_time = (TextView) view.findViewById(R.id.main_feed_creation_time);
            main_feed_group_description = (TextView) view.findViewById(R.id.main_feed_group_description);
        }

        void bindData(CommentData commentData) {
            main_feed_creation_time.setText(commentData.getComment_date());
           // main_feed_group_description.setText(commentData.getCommentText());

            main_feed_group_description.setText(StringEscapeUtils.unescapeJava(commentData.getCommentText()));
        }
    }

    public class MyLoadViewHolder extends RecyclerView.ViewHolder {
        //        ProgressBar progressBar;
        public MyLoadViewHolder(View view) {
            super(view);
//      progressBar = (ProgressBar)view.findViewById(R.id.loadmore_progress);
        }
    }


}
