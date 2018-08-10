package in.sabpaisa.droid.sabpaisa;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;

import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;

public class ImageViewActivity extends AppCompatActivity {

    PhotoView photo_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_image_view);

        photo_view = (PhotoView)findViewById(R.id.photo_view);


        String image = getIntent().getExtras().getString("FULL_IMAGE");

        Glide.with(getApplicationContext())
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photo_view);

    }
}
