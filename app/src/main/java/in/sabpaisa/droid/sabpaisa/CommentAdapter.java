package in.sabpaisa.droid.sabpaisa;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import in.sabpaisa.droid.sabpaisa.Util.ProfileNavigationActivity;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    ImageLoader imageloader = AppController.getInstance().getImageLoader();
    Context mContext;
    Toolbar toolbar;
    private List<CommentData> commentList;
    private boolean multiSelect = false;
    private ArrayList<CommentData> selectedItems = new ArrayList<>();

    String alreadyExistFile;
    ArrayList<String> arrayListAlreadyExistFile = new ArrayList<>();
    ArrayList<Long> list = new ArrayList<>();

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
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final CommentData commentData = commentList.get(position);
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

        holder.main_feed_group_description.setText(StringEscapeUtils.unescapeJava(commentData.getCommentText()));

        Log.d("Comment File : ", commentData.getCommentImage());

        if (!(commentData.getCommentImage()==null || commentData.getCommentImage().equals("null") || commentData.getCommentImage().isEmpty())) {

            holder.commentImg.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(commentData.getCommentImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.commentImg);

            holder.commentImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    String arr[] = commentData.getCommentImage().split("/");

                    Log.d("Step1"," "+commentData.getCommentImage().split("/"));

                    String filenameSplitter[] = arr[arr.length-1].split("_");

                    Log.d("Step2"," "+arr[arr.length-1].split("_"));

                    String fileFormat = filenameSplitter[filenameSplitter.length-1].split("\\.")[1];

                    Log.d("Step3"," "+filenameSplitter[filenameSplitter.length-1].split("\\.")[1]);

                    Log.d("Format4 : ", arr[arr.length-1]+"   ");
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < filenameSplitter.length-1;i++){

                        if (i>0)
                            stringBuilder.append("_");
                        stringBuilder.append(filenameSplitter[i]);

                        Log.d("Step4"," "+stringBuilder);

                    }
                    stringBuilder.append("."+fileFormat);

                    String fname = stringBuilder.toString();


                    Intent intent = new Intent(mContext,ImageViewActivity.class);
                    intent.putExtra("FULL_IMAGE",commentData.getCommentImage());
                    intent.putExtra("FULL_IMAGE_NAME",fname);
                    mContext.startActivity(intent);
                }
            });


        }

        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //Log.d("FilesInDownload", "Path: " + directory);
        File file = new File(directory.getAbsolutePath());
        File[] files = file.listFiles();
        //Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            Log.d("FilesInDownloadFolder", "FileName:" + files[i].getName());
            alreadyExistFile = files[i].getName();
            arrayListAlreadyExistFile.add(alreadyExistFile);
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
        if (!(commentData.getCommentImage()==null || commentData.getCommentImage().equals("null") || commentData.getCommentImage().isEmpty()) && commentData.getCommentImage().endsWith(".pdf")) {
            holder.commentImg.setVisibility(View.GONE);
            holder.pdfFileImg.setVisibility(View.VISIBLE);
            holder.downloadFile.setVisibility(View.VISIBLE);
            holder.fileName.setVisibility(View.VISIBLE);

            String arr[] = commentData.getCommentImage().split("/");

            String filenameSplitter[] = arr[arr.length-1].split("_");

            String fileFormat = filenameSplitter[filenameSplitter.length-1].split("\\.")[1];
            Log.d("Format4 : ", arr[arr.length-1]+"   ");
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < filenameSplitter.length-1;i++){

                if (i>0)
                    stringBuilder.append("_");
                stringBuilder.append(filenameSplitter[i]);


            }
            stringBuilder.append("."+fileFormat);

            String fname = stringBuilder.toString();
            Log.d("Fname "," "+fname);

            if(arrayListAlreadyExistFile.contains(/*arr[arr.length-1]*/fname)){
                holder.downloadFile.setVisibility(View.GONE);
                Log.d("InIfPartWhere"," FileExist"+fname);

                holder.downloadedOpenFile.setVisibility(View.VISIBLE);


            }else{
                holder.downloadFile.setVisibility(View.VISIBLE);
                holder.downloadedOpenFile.setVisibility(View.GONE);
                Log.d("InElsePartWhere"," FileNOTExist "+fname);
            }


            holder.fileName.setText(/*arr[arr.length-1]*/fname);

        }

        holder.downloadedOpenFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/"+holder.fileName.getText());
                //Log.d("FilesInDownload", "Path: " + directory);

                Log.d("ToOpenFileGettingPath"," "+directory);

                if (directory == null){
                    Toast.makeText(mContext,"Please ensure that file is downloaded successfully",Toast.LENGTH_SHORT).show();
                }else {

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
        if (!(commentData.getCommentImage()==null || commentData.getCommentImage().equals("null") || commentData.getCommentImage().isEmpty())
                &&( commentData.getCommentImage().endsWith(".ppt") ||  commentData.getCommentImage().endsWith(".pptx")
                || commentData.getCommentImage().endsWith(".doc") ||  commentData.getCommentImage().endsWith(".docx")
                || commentData.getCommentImage().endsWith(".xls") ||  commentData.getCommentImage().endsWith(".xlsx")
                )) {

            Log.d("Inside if block","Doc file Found"+commentData.getCommentImage());
            holder.commentImg.setVisibility(View.GONE);
            holder.docFileImg.setVisibility(View.VISIBLE);
            holder.downloadFile.setVisibility(View.VISIBLE);
            holder.fileName.setVisibility(View.VISIBLE);

            String arr[] = commentData.getCommentImage().split("/");

            String filenameSplitter[] = arr[arr.length-1].split("_");

            String fileFormat = filenameSplitter[filenameSplitter.length-1].split("\\.")[1];
            Log.d("Format4 : ", arr[arr.length-1]+"   ");
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < filenameSplitter.length-1;i++){

                if (i>0)
                stringBuilder.append("_");
                stringBuilder.append(filenameSplitter[i]);

            }
            stringBuilder.append("."+fileFormat);

            String fname = stringBuilder.toString();

            Log.d("fnameInDOCXCode"," "+fname);

            if(arrayListAlreadyExistFile.contains(/*arr[arr.length-1]*/fname)){
                holder.downloadFile.setVisibility(View.GONE);
                Log.d("InIfPartWhere"," FileExist"+fname);
                holder.downloadedOpenFile.setVisibility(View.VISIBLE);
            }else{
                holder.downloadFile.setVisibility(View.VISIBLE);
                Log.d("InElsePartWhere"," FileNOTExist "+fname);
                holder.downloadedOpenFile.setVisibility(View.GONE);
            }

            holder.fileName.setText(fname);

        }


        holder.downloadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String arr[] = commentData.getCommentImage().split("/");

                Log.d("Step1"," "+commentData.getCommentImage().split("/"));

                String filenameSplitter[] = arr[arr.length-1].split("_");

                Log.d("Step2"," "+arr[arr.length-1].split("_"));

                String fileFormat = filenameSplitter[filenameSplitter.length-1].split("\\.")[1];

                Log.d("Step3"," "+filenameSplitter[filenameSplitter.length-1].split("\\.")[1]);

                Log.d("Format4 : ", arr[arr.length-1]+"   ");
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < filenameSplitter.length-1;i++){

                    if (i>0)
                    stringBuilder.append("_");
                    stringBuilder.append(filenameSplitter[i]);

                    Log.d("Step4"," "+stringBuilder);

                }
                stringBuilder.append("."+fileFormat);

                String fname = stringBuilder.toString();

                Log.d("Step5"," "+fname);

                Log.d("fnameInDownloadCode"," "+fname);

                holder.downloadFile.setVisibility(View.GONE);

                DownloadManager downloadManager = (DownloadManager)mContext.getSystemService(Context.DOWNLOAD_SERVICE);

                Uri uri = Uri.parse(commentData.getCommentImage().replace(" ","%20").replace("-","%2D"));

                DownloadManager.Request request = new DownloadManager.Request(uri);

//                String arr[] = commentData.getCommentImage().split("/");
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fname);

                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                Long reference = downloadManager.enqueue(request);

                holder.downloadedOpenFile.setVisibility(View.VISIBLE);

            }
        });



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
        public ImageView main_feed_comment_image,commentImg,pdfFileImg,docFileImg;
        public RelativeLayout relative_comments;
        public TextView downloadFile,downloadedOpenFile;
        public TextView fileName;

        public MyViewHolder(View view) {
            super(view);
            main_feed_creation_time = (TextView) view.findViewById(R.id.main_feed_creation_time);
            main_feed_comment_username = (TextView) view.findViewById(R.id.username);
            main_feed_group_description = (TextView) view.findViewById(R.id.main_feed_group_description);
            main_feed_comment_image = (ImageView) view.findViewById(R.id.people_name_initial);
            commentImg = (ImageView) view.findViewById(R.id.commentImg);
            pdfFileImg = (ImageView) view.findViewById(R.id.pdfFileImg);
            docFileImg = (ImageView) view.findViewById(R.id.docFileImg);
            relative_comments = (RelativeLayout) view.findViewById(R.id.relative_comments);
            downloadFile = (TextView) view.findViewById(R.id.downloadFile);
            downloadedOpenFile = (TextView) view.findViewById(R.id.downloadedOpenFile);
            fileName = (TextView) view.findViewById(R.id.fileName);
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

            Uri uri = FileProvider.getUriForFile(mContext,mContext.getApplicationContext().getPackageName()+ ".my.package.name.provider",url);

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
            Log.d("ActivityNotFndException"," "+e);
            Toast.makeText(mContext, "No application found which can open the file", Toast.LENGTH_SHORT).show();
        }
    }

}
