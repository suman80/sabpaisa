package in.sabpaisa.droid.sabpaisa;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import in.sabpaisa.droid.sabpaisa.AppDB.AppDbComments;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;

import static in.sabpaisa.droid.sabpaisa.MainActivitySkip.AppDecideFlag;
import static in.sabpaisa.droid.sabpaisa.MainActivitySkip.SUPER_ADMIN_SHAREDFREF;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    ImageLoader imageloader = AppController.getInstance().getImageLoader();
    Context mContext;
    Toolbar toolbar;
    private  List<CommentData> commentList;
    private boolean multiSelect = false;
    private ArrayList<CommentData> selectedItems = new ArrayList<>();

    String alreadyExistFile;
    ArrayList<String> arrayListAlreadyExistFile = new ArrayList<>();
    ArrayList<Long> list = new ArrayList<>();

    AppDbComments db;


    /*ColorGenerator generator = ColorGenerator.MATERIAL;
    String letter;*/

    private android.support.v7.view.ActionMode.Callback actionModeCallbacks = new android.support.v7.view.ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(android.support.v7.view.ActionMode mode, Menu menu) {
            multiSelect = true;
            menu.add("Share");

            String roleValue = null;

            if (AppDecideFlag){
                SharedPreferences sharedPreferencesRole = mContext.getSharedPreferences(SUPER_ADMIN_SHAREDFREF, Context.MODE_PRIVATE);

                roleValue = sharedPreferencesRole.getString("ROLE_VALUE", "abc");

                Log.d("CommentAdapter","roleValueFromPersonalSpace______"+roleValue);

            }else {
                SharedPreferences sharedPreferencesRole = mContext.getSharedPreferences(UIN.SHARED_PREF_FOR_CHECK_USER, Context.MODE_PRIVATE);

                roleValue = sharedPreferencesRole.getString("USER_ROLE", "abc");

                Log.d("CommentAdapter","roleValueFromClientSpace______"+roleValue);

            }



            Log.d("CommentAdapter","ProceedGRPSCRN_MEM_GRP_ROLE "+Proceed_Group_FullScreen.memberGroupRole);


            if (roleValue.equals("1") || (Proceed_Group_FullScreen.memberGroupRole != null && Proceed_Group_FullScreen.memberGroupRole.equals("2"))) {
                menu.add("Delete");
            }

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

            CharSequence title = item.getTitle();

            Log.d("CommentAdapter","onActionItemClicked "+title);


            if (title.equals("Share")) {
                Log.d("selectedItems", " " + selectedItems);
                //////////////////////////////////////////
                if(selectedItems.size()>3) {
                    Toast.makeText(mContext, "You can select at max 3 comment to share", Toast.LENGTH_SHORT).show();
                }
                ////////////////////////////////////////////////
                else {
                    Intent intent = new Intent(mContext, SharingActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("SELECTED_LIST", selectedItems);
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }

            }


            if (title.equals("Delete")){

                item.setVisible(true);

                Log.d("CommentAdapter", "InDeletePart");

                SharedPreferences sharedPreferences1 = mContext.getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

                String userAccessToken = sharedPreferences1.getString("response", "123");

                String feed_id=null;
                String groupId=null;

                if (!AppDecideFlag) {

                    SharedPreferences sharedPreferences2 = mContext.getSharedPreferences(Proceed_Feed_FullScreen.MY_PREFS_FOR_FEED_ID, Context.MODE_PRIVATE);

                     feed_id = sharedPreferences2.getString("feedId", null);

                    Log.d("CommentAdapterF", "feed_id " + feed_id);

                    SharedPreferences sharedPreferences3 = mContext.getSharedPreferences(Proceed_Group_FullScreen.MY_PREFS_FOR_GROUP_ID, Context.MODE_PRIVATE);

                     groupId = sharedPreferences3.getString("groupId", null);

                    Log.d("CommentAdapterF", "groupId " + groupId);

                }else {
                    SharedPreferences sharedPreferences2 = mContext.getSharedPreferences(FeedSpaceCommentsActivity.SHARED_PREF_FEED_ID_VALUE, Context.MODE_PRIVATE);

                     feed_id = sharedPreferences2.getString("feedId", null);

                    Log.d("CommentAdapterF", "feed_id " + feed_id);


                    SharedPreferences sharedPreferences3 = mContext.getSharedPreferences(GroupSpaceCommentActivity.SHARED_PREF_GROUP_ID_VALUE, Context.MODE_PRIVATE);

                     groupId = sharedPreferences3.getString("groupId", null);

                    Log.d("CommentAdapterF", "groupId " + groupId);


                }

                if (feed_id != null && !feed_id.equals("") && !feed_id.isEmpty()) {

                    for (CommentData commentData : selectedItems) {
                        Log.d("CommentAdapterF", "InDeletePartComntId " + commentData.getCommentId());
                        deleteFeedCommentData(commentData.getCommentId() + "", userAccessToken,feed_id,commentData);
                    }

                }

                if (groupId != null && !groupId.equals("") && !groupId.isEmpty()) {

                    for (CommentData commentData : selectedItems) {
                        Log.d("CommentAdapterG", "InDeletePartComntId " + commentData.getCommentId());
                        deleteGroupCommentData(commentData.getCommentId() + "", userAccessToken,groupId,commentData);
                    }

                }


            }

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
        db = new AppDbComments(context);
    }

    public CommentAdapter(List<CommentData> countryList, Context context) {
        this.commentList = countryList;
        this.mContext = context;
        db = new AppDbComments(context);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final CommentData commentData = commentList.get(position);
        Log.d("commentData", " " + commentData.getCommentName());
        Log.d("commentData", " " + commentData.getCommentText());
        holder.main_feed_comment_username.setText(commentData.getCommentName());
        holder.main_feed_creation_time.setText(commentData.getComment_date());

        //Gmail like round colorful round icon with 1 char name letter
        /*letter = String.valueOf(commentData.getCommentName().charAt(0));

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(letter, generator.getRandomColor());

        holder.main_feed_comment_image.setImageDrawable(drawable);
*/


        holder.main_feed_group_description.setText(StringEscapeUtils.unescapeJava(commentData.getCommentText()));

        Log.d("Comment File : ", commentData.getCommentImage());

        if (!(commentData.getCommentImage() == null || commentData.getCommentImage().equals("null") || commentData.getCommentImage().isEmpty())) {

            holder.commentImg.setVisibility(View.VISIBLE);
            holder.card_view.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(commentData.getCommentImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.commentImg);

            holder.commentImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    String arr[] = commentData.getCommentImage().split("/");

                    Log.d("Step1", " " + commentData.getCommentImage().split("/"));

                    String filenameSplitter[] = arr[arr.length - 1].split("_");

                    Log.d("Step2", " " + arr[arr.length - 1].split("_"));

                    String fileFormat = filenameSplitter[filenameSplitter.length - 1].split("\\.")[1];

                    Log.d("Step3", " " + filenameSplitter[filenameSplitter.length - 1].split("\\.")[1]);

                    Log.d("Format4 : ", arr[arr.length - 1] + "   ");
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < filenameSplitter.length - 1; i++) {

                        if (i > 0)
                            stringBuilder.append("_");
                        stringBuilder.append(filenameSplitter[i]);

                        Log.d("Step4", " " + stringBuilder);

                    }
                    stringBuilder.append("." + fileFormat);

                    String fname = stringBuilder.toString();


                    Intent intent = new Intent(mContext, ImageViewActivity.class);
                    intent.putExtra("FULL_IMAGE", commentData.getCommentImage());
                    intent.putExtra("FULL_IMAGE_NAME", fname);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });


        }



        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //Log.d("FilesInDownload", "Path: " + directory);
        File file = new File(directory.getAbsolutePath());
        File[] files = file.listFiles();
        if(!(files == null || files.length == 0)) {
            //Log.d("Files", "Size: "+ files.length);
            for (int i = 0; i < files.length; i++) {
                Log.d("FilesInDownloadFolder", "FileName:" + files[i].getName());
                alreadyExistFile = files[i].getName();
                arrayListAlreadyExistFile.add(alreadyExistFile);
            }
        }
//        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//        Log.d("FilesInDownload", "Path: " + directory);
//        //File file = new File(directory.getAbsolutePath());
//        File[] files = directory.listFiles();
//        Log.d("Files", "Size: "+ files.length);
//        for (int i = 0; i < files.length; i++)
//        {
//            Log.d("FilesInDownloadFolder", "FileName:" + files[i].getName());
//        }

//Code for pdf files
        if (!(commentData.getCommentImage() == null || commentData.getCommentImage().equals("null") || commentData.getCommentImage().isEmpty()) && commentData.getCommentImage().endsWith(".pdf")) {
            holder.commentImg.setVisibility(View.GONE);
            holder.card_view.setVisibility(View.GONE);
            holder.pdfFileImg.setVisibility(View.VISIBLE);
            holder.downloadFile.setVisibility(View.VISIBLE);
            holder.fileName.setVisibility(View.VISIBLE);

            String arr[] = commentData.getCommentImage().split("/");

            String filenameSplitter[] = arr[arr.length - 1].split("_");

            String fileFormat = filenameSplitter[filenameSplitter.length - 1].split("\\.")[1];
            Log.d("Format4 : ", arr[arr.length - 1] + "   ");
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < filenameSplitter.length - 1; i++) {

                if (i > 0)
                    stringBuilder.append("_");
                stringBuilder.append(filenameSplitter[i]);


            }
            stringBuilder.append("." + fileFormat);

            String fname = stringBuilder.toString();
            Log.d("Fname ", " " + fname);

            if (arrayListAlreadyExistFile.contains(/*arr[arr.length-1]*/fname)) {
                holder.downloadFile.setVisibility(View.GONE);
                Log.d("InIfPartWhere", " FileExist" + fname);

                holder.downloadedOpenFile.setVisibility(View.VISIBLE);


            } else {
                holder.downloadFile.setVisibility(View.VISIBLE);
                holder.downloadedOpenFile.setVisibility(View.GONE);
                Log.d("InElsePartWhere", " FileNOTExist " + fname);
            }


            holder.fileName.setText(/*arr[arr.length-1]*/fname);

        }

        holder.downloadedOpenFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/" + holder.fileName.getText());
                //Log.d("FilesInDownload", "Path: " + directory);

                Log.d("ToOpenFileGettingPath", " " + directory);

                if (directory == null) {
                    Toast.makeText(mContext, "Please ensure that file is downloaded successfully", Toast.LENGTH_SHORT).show();
                } else {

                    if (android.os.Build.VERSION.SDK_INT >= 24) {
                        // Do something for lollipop and above versions
                        openFileForNew(directory);
                    } else {
                        // do something for phones running an SDK before lollipop
                        openFile(directory);
                    }

                }

            }
        });

//Code for documents files
        if (!(commentData.getCommentImage() == null || commentData.getCommentImage().equals("null") || commentData.getCommentImage().isEmpty())
                && (commentData.getCommentImage().endsWith(".ppt") || commentData.getCommentImage().endsWith(".pptx")
                || commentData.getCommentImage().endsWith(".doc") || commentData.getCommentImage().endsWith(".docx")
        )) {


            Log.d("Inside if block", "Doc file Found" + commentData.getCommentImage());
            holder.commentImg.setVisibility(View.GONE);
            holder.card_view.setVisibility(View.GONE);
            holder.docFileImg.setVisibility(View.VISIBLE);
            holder.downloadFile.setVisibility(View.VISIBLE);
            holder.fileName.setVisibility(View.VISIBLE);

            String arr[] = commentData.getCommentImage().split("/");

            String filenameSplitter[] = arr[arr.length - 1].split("_");

            String fileFormat = filenameSplitter[filenameSplitter.length - 1].split("\\.")[1];
            Log.d("Format4 : ", arr[arr.length - 1] + "   ");
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < filenameSplitter.length - 1; i++) {

                if (i > 0)
                    stringBuilder.append("_");
                stringBuilder.append(filenameSplitter[i]);

            }
            stringBuilder.append("." + fileFormat);

            String fname = stringBuilder.toString();

            Log.d("fnameInDOCXCode", " " + fname);

            if (arrayListAlreadyExistFile.contains(/*arr[arr.length-1]*/fname)) {
                holder.downloadFile.setVisibility(View.GONE);
                Log.d("InIfPartWhere", " FileExist" + fname);
                holder.downloadedOpenFile.setVisibility(View.VISIBLE);
            } else {
                holder.downloadFile.setVisibility(View.VISIBLE);
                Log.d("InElsePartWhere", " FileNOTExist " + fname);
                holder.downloadedOpenFile.setVisibility(View.GONE);
            }

            holder.fileName.setText(fname);

        }

        if (!(commentData.getCommentImage() == null || commentData.getCommentImage().equals("null") || commentData.getCommentImage().isEmpty())
                && commentData.getCommentImage().endsWith(".xls") || commentData.getCommentImage().endsWith(".xlsx")) {

            Log.d("Inside if block", "XLS file Found" + commentData.getCommentImage());
            holder.commentImg.setVisibility(View.GONE);
            holder.card_view.setVisibility(View.GONE);
            holder.xlsFileImg.setVisibility(View.VISIBLE);
            holder.downloadFile.setVisibility(View.VISIBLE);
            holder.fileName.setVisibility(View.VISIBLE);

            String arr[] = commentData.getCommentImage().split("/");

            String filenameSplitter[] = arr[arr.length - 1].split("_");

            String fileFormat = filenameSplitter[filenameSplitter.length - 1].split("\\.")[1];
            Log.d("Format4 : ", arr[arr.length - 1] + "   ");
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < filenameSplitter.length - 1; i++) {

                if (i > 0)
                    stringBuilder.append("_");
                stringBuilder.append(filenameSplitter[i]);

            }
            stringBuilder.append("." + fileFormat);

            String fname = stringBuilder.toString();

            Log.d("fnameInDOCXCode", " " + fname);

            if (arrayListAlreadyExistFile.contains(/*arr[arr.length-1]*/fname)) {
                holder.downloadFile.setVisibility(View.GONE);
                Log.d("InIfPartWhere", " FileExist" + fname);
                holder.downloadedOpenFile.setVisibility(View.VISIBLE);
            } else {
                holder.downloadFile.setVisibility(View.VISIBLE);
                Log.d("InElsePartWhere", " FileNOTExist " + fname);
                holder.downloadedOpenFile.setVisibility(View.GONE);
            }

            holder.fileName.setText(fname);


        }

        holder.downloadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String arr[] = commentData.getCommentImage().split("/");

                Log.d("Step1", " " + commentData.getCommentImage().split("/"));

                String filenameSplitter[] = arr[arr.length - 1].split("_");

                Log.d("Step2", " " + arr[arr.length - 1].split("_"));

                String fileFormat = filenameSplitter[filenameSplitter.length - 1].split("\\.")[1];

                Log.d("Step3", " " + filenameSplitter[filenameSplitter.length - 1].split("\\.")[1]);

                Log.d("Format4 : ", arr[arr.length - 1] + "   ");
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < filenameSplitter.length - 1; i++) {

                    if (i > 0)
                        stringBuilder.append("_");
                    stringBuilder.append(filenameSplitter[i]);

                    Log.d("Step4", " " + stringBuilder);

                }
                stringBuilder.append("." + fileFormat);

                String fname = stringBuilder.toString();

                Log.d("Step5", " " + fname);

                Log.d("fnameInDownloadCode", " " + fname);

                holder.downloadFile.setVisibility(View.GONE);

                DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);

                Uri uri = Uri.parse(commentData.getCommentImage().replace(" ", "%20").replace("-", "%2D"));

                DownloadManager.Request request = new DownloadManager.Request(uri);

//                String arr[] = commentData.getCommentImage().split("/");
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fname);

                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                Long reference = downloadManager.enqueue(request);

                holder.downloadedOpenFile.setVisibility(View.VISIBLE);

            }
        });


        //Loading user image

        /*Glide.with(mContext)
                .load(commentData.getUserImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.default_users)
                .into(holder.main_feed_comment_image);*/


        Log.d("commentAdapterUserID","___"+commentData.getUserId());

        if (commentData.getUserId() != null || !commentData.getUserId().isEmpty() || !commentData.getUserId().equals("")) {

            Cursor res = db.getParticularUserImageData(commentData.getUserId());
            if (res.getCount() > 0) {
                StringBuffer stringBuffer = new StringBuffer();

                while (res.moveToNext()) {
                    stringBuffer.append(res.getString(0) + " ");
                    stringBuffer.append(res.getString(1) + " ");
                    stringBuffer.append(res.getString(2) + " ");

                    Glide.with(mContext)
                            .load(res.getString(2))
                            .error(R.drawable.default_users)
                            .into(holder.main_feed_comment_image);


                }

                Log.d("MemAdapterImgLocal", "stringBufferVal_ " + stringBuffer);
            }
            // Added on 26th March 2019
            else {

                Glide.with(mContext)
                        .load(commentData.getUserImageUrl())
                        .error(R.drawable.default_users)
                        .into(holder.main_feed_comment_image);
            }

        }else {
            Glide.with(mContext)
                    .load(R.drawable.default_users)
                    .into(holder.main_feed_comment_image);
        }



        holder.update(commentList.get(position));

    }




    @Override
    public int getItemCount() {
        return commentList.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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
        public ImageView main_feed_comment_image, commentImg, pdfFileImg, docFileImg, xlsFileImg;
        public RelativeLayout relative_comments;
        public ImageView downloadFile;
        public TextView downloadedOpenFile;
        public TextView fileName;
        public CardView card_view;

        public MyViewHolder(View view) {
            super(view);
            main_feed_creation_time = (TextView) view.findViewById(R.id.main_feed_creation_time);
            main_feed_comment_username = (TextView) view.findViewById(R.id.username);
            main_feed_group_description = (TextView) view.findViewById(R.id.main_feed_group_description);
            main_feed_comment_image = (ImageView) view.findViewById(R.id.people_name_initial);
            commentImg = (ImageView) view.findViewById(R.id.commentImg);
            pdfFileImg = (ImageView) view.findViewById(R.id.pdfFileImg);
            docFileImg = (ImageView) view.findViewById(R.id.docFileImg);
            xlsFileImg = (ImageView) view.findViewById(R.id.xlsFileImg);
            relative_comments = (RelativeLayout) view.findViewById(R.id.relative_comments);
            downloadFile = (ImageView) view.findViewById(R.id.downloadFile);
            downloadedOpenFile = (TextView) view.findViewById(R.id.downloadedOpenFile);
            fileName = (TextView) view.findViewById(R.id.fileName);
            card_view = (CardView) view.findViewById(R.id.card_view);
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


    private void openFile(File url) {

        try {

            Uri uri = Uri.fromFile(url);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                // WAV audio file
                intent.setDataAndType(uri, "application/x-wav");
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                    url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
                intent.setDataAndType(uri, "*/*");
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext, "No application found which can open the file", Toast.LENGTH_SHORT).show();
        }
    }


    private void openFileForNew(File url) {

        try {

            Uri uri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".my.package.name.provider", url);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                // WAV audio file
                intent.setDataAndType(uri, "application/x-wav");
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                    url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
                intent.setDataAndType(uri, "*/*");
            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.d("ActivityNotFndException", " " + e);
            Toast.makeText(mContext, "No application found which can open the file", Toast.LENGTH_SHORT).show();
        }
    }


    private void deleteFeedCommentData(final String commentId, final String userAccessToken,final String feed_id,final CommentData commentData) {

        String tag_string_req = "req_clients";

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_deleteFeedComments + "?comment_id=" + commentId +"&admin="+userAccessToken;

        Log.d("CommentAdapter","deleteFeedDataURL : "+url);

        StringRequest request = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {

                Log.d("CommentAdapterFeedData", "-->" + response1);
                //parsing Json
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response1);
                    String response = jsonObject.getString("response");
                    String status = jsonObject.getString("status");
                    Log.d("CommentAdapterResp", "" + response);
                    Log.d("CommentAdapterStatus", "" + status);

                    if (status.equals("success")){
                        Log.d("CommentAdapterFeedData","InIfPart");

                        /*String FeedsNm = Proceed_Feed_FullScreen.FeedsNm;
                        String FeedDiscription = Proceed_Feed_FullScreen.feedsDiscription;
                        String FeedImg = Proceed_Feed_FullScreen.feedImg;
                        String FeedLogo = Proceed_Feed_FullScreen.feedLogo;

                        Intent intent = new Intent(mContext,Proceed_Feed_FullScreen.class);
                        intent.putExtra("feedId",feed_id);
                        intent.putExtra("feedName",FeedsNm);
                        intent.putExtra("feedText",FeedDiscription);
                        intent.putExtra("feedImage",FeedImg);
                        intent.putExtra("feedLogo",FeedLogo);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);*/

                        if (!AppDecideFlag) {

                            if (commentData.getCommentImage() == null || commentData.getCommentImage().equals("") || commentData.getCommentImage().equals("null")) {
                                Log.d("CommentImageCheck", "Reload_activity_InIf__" + commentData.getCommentImage());

                                commentList.remove(commentData);
                                selectedItems.clear();
                                notifyDataSetChanged();


                            } else {


                                Log.d("CommentImageCheck", "Reload_activity_InElse__" + commentData.getCommentImage());

                                String FeedsNm = Proceed_Feed_FullScreen.FeedsNm;
                                String FeedDiscription = Proceed_Feed_FullScreen.feedsDiscription;
                                String FeedImg = Proceed_Feed_FullScreen.feedImg;
                                String FeedLogo = Proceed_Feed_FullScreen.feedLogo;

                                Intent intent = new Intent(mContext, Proceed_Feed_FullScreen.class);
                                intent.putExtra("feedId", feed_id);
                                intent.putExtra("feedName", FeedsNm);
                                intent.putExtra("feedText", FeedDiscription);
                                intent.putExtra("feedImage", FeedImg);
                                intent.putExtra("feedLogo", FeedLogo);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(intent);

                            }
                        }else {

                            if (commentData.getCommentImage() == null || commentData.getCommentImage().equals("") || commentData.getCommentImage().equals("null")) {
                                Log.d("CommentImageCheck", "Reload_activity_InIf__" + commentData.getCommentImage());

                                commentList.remove(commentData);
                                selectedItems.clear();
                                notifyDataSetChanged();


                            } else {


                                Log.d("CommentImageCheck", "Reload_activity_InElse__" + commentData.getCommentImage());

                                String FeedsNm = FeedSpaceCommentsActivity.feedName;
                                String FeedDiscription = FeedSpaceCommentsActivity.feedText;
                                String FeedImg = FeedSpaceCommentsActivity.feedImage;
                                String FeedLogo = FeedSpaceCommentsActivity.feedLogo;

                                Intent intent = new Intent(mContext, FeedSpaceCommentsActivity.class);
                                intent.putExtra("feedId", feed_id);
                                intent.putExtra("feedName", FeedsNm);
                                intent.putExtra("feedText", FeedDiscription);
                                intent.putExtra("feedImage", FeedImg);
                                intent.putExtra("feedLogo", FeedLogo);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(intent);

                            }

                        }


                    }else {
                        Log.d("CommentAdapterFeedData","InElsePart");
                        Toast.makeText(mContext,response,Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage() == null || error instanceof TimeoutError || error instanceof NoConnectionError) {
                    android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(mContext, R.style.MyDialogTheme).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Network/Connection Error");

                    // Setting Dialog Message
                    alertDialog.setMessage("Internet Connection is poor OR The Server is taking too long to respond.Please try again later.Thank you.");

                    // Setting Icon to Dialog
                    //  alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                    //Log.e(TAG, "Registration Error: " + error.getMessage());

                } else if (error instanceof AuthFailureError) {

                    //TODO
                } else if (error instanceof ServerError) {

                    //TODO
                } else if (error instanceof NetworkError) {

                    //TODO
                } else if (error instanceof ParseError) {

                    //TODO
                }


            }


        });

        AppController.getInstance().addToRequestQueue(request, tag_string_req);


    }


    private void deleteGroupCommentData(final String commentId, final String userAccessToken , final String groupId,final CommentData commentData) {

        String tag_string_req = "req_clients";

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_deleteGroupComments + "?comment_id=" + commentId +"&admin="+userAccessToken;

        Log.d("CommentAdapter","deleteGRPDataURL : "+url);

        StringRequest request = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {

                Log.d("CommentAdapterGRPData", "-->" + response1);
                //parsing Json
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(response1);
                    String response = jsonObject.getString("response");
                    String status = jsonObject.getString("status");
                    Log.d("CommentAdapterResp", "" + response);
                    Log.d("CommentAdapterStatus", "" + status);

                    if (status.equals("success")){
                        Log.d("CommentAdapterGRPData","InIfPart");

                        /*String GroupsNm = Proceed_Group_FullScreen.GroupsNm;
                        String GroupsDiscription = Proceed_Group_FullScreen.GroupsDiscription;
                        String GroupsImg = Proceed_Group_FullScreen.GroupsImg;
                        String GroupsLogo = Proceed_Group_FullScreen.groupLogo;

                        Intent intent = new Intent(mContext,Proceed_Group_FullScreen.class);
                        intent.putExtra("groupId",groupId);
                        intent.putExtra("groupName",GroupsNm);
                        intent.putExtra("memberGroupRole",Proceed_Group_FullScreen.memberGroupRole);
                        intent.putExtra("groupText",GroupsDiscription);
                        intent.putExtra("groupImage",GroupsImg);
                        intent.putExtra("groupLogo",GroupsLogo);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);*/

                        if (!AppDecideFlag) {

                            if (commentData.getCommentImage() == null || commentData.getCommentImage().equals("") || commentData.getCommentImage().equals("null")) {
                                Log.d("CommentImageCheck", "Reload_activity_InIf__" + commentData.getCommentImage());
                                commentList.remove(commentData);
                                selectedItems.clear();
                                notifyDataSetChanged();
                            } else {

                                Log.d("CommentImageCheck", "Reload_activity_InElse__" + commentData.getCommentImage());

                                String GroupsNm = Proceed_Group_FullScreen.GroupsNm;
                                String GroupsDiscription = Proceed_Group_FullScreen.GroupsDiscription;
                                String GroupsImg = Proceed_Group_FullScreen.GroupsImg;
                                String GroupsLogo = Proceed_Group_FullScreen.groupLogo;

                                Intent intent = new Intent(mContext, Proceed_Group_FullScreen.class);
                                intent.putExtra("groupId", groupId);
                                intent.putExtra("groupName", GroupsNm);
                                intent.putExtra("memberGroupRole", Proceed_Group_FullScreen.memberGroupRole);
                                intent.putExtra("groupText", GroupsDiscription);
                                intent.putExtra("groupImage", GroupsImg);
                                intent.putExtra("groupLogo", GroupsLogo);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(intent);
                            }
                        }else {

                            if (commentData.getCommentImage() == null || commentData.getCommentImage().equals("") || commentData.getCommentImage().equals("null")) {
                                Log.d("CommentImageCheck", "Reload_activity_InIf__" + commentData.getCommentImage());
                                commentList.remove(commentData);
                                selectedItems.clear();
                                notifyDataSetChanged();
                            } else {

                                Log.d("CommentImageCheck", "Reload_activity_InElse__" + commentData.getCommentImage());

                                String GroupsNm = GroupSpaceCommentActivity.GroupsNm;
                                String GroupsDiscription = GroupSpaceCommentActivity.GroupsDiscription;
                                String GroupsImg = GroupSpaceCommentActivity.GroupsImg;
                                String GroupsLogo = GroupSpaceCommentActivity.groupLogo;

                                Intent intent = new Intent(mContext, GroupSpaceCommentActivity.class);
                                intent.putExtra("groupId", groupId);
                                intent.putExtra("groupName", GroupsNm);
                                intent.putExtra("memberGroupRole", GroupSpaceCommentActivity.memberGroupRole);
                                intent.putExtra("groupText", GroupsDiscription);
                                intent.putExtra("groupImage", GroupsImg);
                                intent.putExtra("groupLogo", GroupsLogo);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(intent);

                            }

                        }

                    }else {
                        Log.d("CommentAdapterGRPData","InElsePart");
                        Toast.makeText(mContext,response,Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage() == null || error instanceof TimeoutError || error instanceof NoConnectionError) {
                    android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(mContext.getApplicationContext(), R.style.MyDialogTheme).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Network/Connection Error");

                    // Setting Dialog Message
                    alertDialog.setMessage("Internet Connection is poor OR The Server is taking too long to respond.Please try again later.Thank you.");

                    // Setting Icon to Dialog
                    //  alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                    //Log.e(TAG, "Registration Error: " + error.getMessage());

                } else if (error instanceof AuthFailureError) {

                    //TODO
                } else if (error instanceof ServerError) {

                    //TODO
                } else if (error instanceof NetworkError) {

                    //TODO
                } else if (error instanceof ParseError) {

                    //TODO
                }


            }


        });

        AppController.getInstance().addToRequestQueue(request, tag_string_req);


    }

}