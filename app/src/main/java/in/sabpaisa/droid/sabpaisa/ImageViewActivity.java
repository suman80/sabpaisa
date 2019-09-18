package in.sabpaisa.droid.sabpaisa;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;

public class ImageViewActivity extends AppCompatActivity {

    PhotoView photo_view;
    Toolbar toolbar;
    String image,imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_image_view);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }*/

        image = getIntent().getExtras().getString("FULL_IMAGE");
        imageName = getIntent().getExtras().getString("FULL_IMAGE_NAME");

        Log.d("GotImage?"," "+image);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(imageName);

        setSupportActionBar(toolbar);

        photo_view = (PhotoView)findViewById(R.id.photo_view);




        if (!(image == null  &&  image.isEmpty())){
            Glide.with(getApplicationContext())
                    .load(image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(photo_view);
        }else {
            Toast.makeText(ImageViewActivity.this,"No Image Found !",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.download_image_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.downloadImage:

                String arr[] = image.split("/");

                Log.d("Step1"," "+image.split("/"));

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

                DownloadManager downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);

                Uri uri = Uri.parse(image.replace(" ","%20").replace("-","%2D"));

                DownloadManager.Request request = new DownloadManager.Request(uri);

//                String arr[] = commentData.getCommentImage().split("/");
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fname);

                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                Long reference = downloadManager.enqueue(request);




                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
