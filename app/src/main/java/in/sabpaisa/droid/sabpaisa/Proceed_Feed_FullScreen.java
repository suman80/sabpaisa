package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class Proceed_Feed_FullScreen extends AppCompatActivity {

    TextView feedsName,feed_description_details;
    ImageView feedImage;

    String FeedsNm,feedsDiscription,feedImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed__feed__full_screen);

        feedsName=(TextView)findViewById(R.id.feedsName);
        feed_description_details=(TextView)findViewById(R.id.feed_description_details);
        feedImage=(ImageView)findViewById(R.id.feedImage);

        FeedsNm=getIntent().getStringExtra("feedName");
        feedsDiscription=getIntent().getStringExtra("feedText");
        feedImg=getIntent().getStringExtra("feedImage");
        Log.d("FeedsNmPFF",""+FeedsNm);
        Log.d("feedsDiscriptionPFF",""+feedsDiscription);
        Log.d("feedImgPFF",""+feedImg);

        feedsName.setText(FeedsNm);
        feed_description_details.setText(feedsDiscription);
        new DownloadImageTask(feedImage).execute(feedImg);
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
