package in.sabpaisa.droid.sabpaisa;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class Proceed_Group_FullScreen extends AppCompatActivity {

    TextView groupsName,group_description_details;
    ImageView groupImage;

    String GroupsNm,GroupsDiscription,GroupsImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed__group__full_screen);

        groupsName=(TextView)findViewById(R.id.groupsName);
        group_description_details=(TextView)findViewById(R.id.group_description_details);
        groupImage=(ImageView)findViewById(R.id.groupImage);

        GroupsNm=getIntent().getStringExtra("groupName");
        GroupsDiscription=getIntent().getStringExtra("groupText");
        GroupsImg=getIntent().getStringExtra("groupImage");
        Log.d("FeedsNmPGFFS",""+GroupsNm);
        Log.d("feedsDiscriptionPGFS",""+GroupsDiscription);
        Log.d("feedImgPGFS",""+GroupsImg);

        groupsName.setText(GroupsNm);
        group_description_details.setText(GroupsDiscription);
        new DownloadImageTask(groupImage).execute(GroupsImg);

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
