package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CropImageActivity1 extends AppCompatActivity {

    CropImageView cropImageView;

    public static Bitmap cropped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image1);

        cropImageView = (CropImageView)findViewById(R.id.cropImageView);

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);




    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                Log.d("resultUri","___________"+resultUri);


                Intent intent = new Intent(CropImageActivity1.this,PreviewActivityForShare.class);
                intent.putExtra("imageUri", resultUri.toString());
                startActivity(intent);


                //From here you can load the image however you need to, I recommend using the Glide library

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


}
