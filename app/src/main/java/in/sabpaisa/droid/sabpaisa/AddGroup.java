package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddGroup extends AppCompatActivity {

    EditText editText_GroupName,editText_GroupDescription;
    ImageView img_GroupImage,img_GroupLogo;
    Button btn_Cancel,btn_Save;

    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        editText_GroupName = (EditText)findViewById(R.id.editText_GroupName);
        editText_GroupDescription = (EditText)findViewById(R.id.editText_GroupDescription);
        img_GroupImage = (ImageView)findViewById(R.id.img_GroupImage);
        img_GroupLogo = (ImageView)findViewById(R.id.img_GroupLogo);
        btn_Cancel = (Button)findViewById(R.id.btn_Cancel);
        btn_Save = (Button)findViewById(R.id.btn_Save);


        img_GroupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        img_GroupLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickLogo();
            }
        });


    }

    public void pickImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("userImageUrl", imageUrl);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 200);


    }

    public void pickLogo() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("userImageUrl", imageUrl);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 300);


    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == 200 && resultCode == RESULT_OK && data != null) {

                Uri selectedimg = data.getData();

                //android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                img_GroupImage.setImageBitmap(android.provider.MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));
                Bitmap userImg = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg);

                //Todo API
                //uploadBitmap(userImg);

            }else if (requestCode == 300 && resultCode == RESULT_OK && data != null){

                Uri selectedimg = data.getData();

                //android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                img_GroupLogo.setImageBitmap(android.provider.MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));
                Bitmap userImg = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg);
                //Todo API
                //uploadBitmap(userImg);

            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }


    }


}
