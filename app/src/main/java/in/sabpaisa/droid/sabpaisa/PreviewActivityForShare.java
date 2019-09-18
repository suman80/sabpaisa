package in.sabpaisa.droid.sabpaisa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;
import in.sabpaisa.droid.sabpaisa.Util.VolleyMultipartRequest;

public class PreviewActivityForShare extends AppCompatActivity {

    ImageView previewImg;

    Bitmap bitmap;

    Button uploadImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_for_share);

        previewImg = (ImageView)findViewById(R.id.previewImg);
        uploadImg = (Button) findViewById(R.id.uploadImg);

        String imgUri = getIntent().getStringExtra("imageUri");

        final Uri myUri = Uri.parse(imgUri);

        previewImg.setImageURI(myUri);


        SharedPreferences sharedPreferences = getSharedPreferences(FullViewOfClientsProceed.MySharedPrefOnFullViewOfClientProceed, Context.MODE_PRIVATE);
        final String clientId = sharedPreferences.getString("clientId", "abc");
        Log.d("clientId_PAFS", "" + clientId);


        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    bitmap = MediaStore.Images.Media.getBitmap(view.getContext().getContentResolver(), myUri);
                    if (isOnline()) {
                        uploadBitmap(bitmap,clientId);
                    }else {
                        Toast.makeText(view.getContext(),"No Internet Connection !",Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
        });



    }




    private void uploadBitmap(final Bitmap bitmap,final String clientId) {

        //String url = "http://192.168.1.14:8000/testing/upload2";
        //String url = "https://mansha2.sabpaisa.in/testing/upload2/";
        String url = "192.168.1.150:8000/testing/upload2/";
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d("PAFS", "IMG_Res" + response);
                        try {

                            JSONObject obj = new JSONObject(new String(response.data));

                            Log.d("PAFS", "IMG_Res" + obj);

                            String status = obj.getString("status");

                            String url = obj.getString("url");

                            Log.d("PAFS___","Status and Url ____"+status+"   "+url);

                            if (status.equals("success")) {

                                Intent intent = new Intent(PreviewActivityForShare.this, CameraWebView.class);
                                intent.putExtra("URL", url);
                                startActivity(intent);

                            }else {
                                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error In Upoload", error.toString());

                        Toast.makeText(PreviewActivityForShare.this, "Image Upload Failed !", Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("remark", "test");
                params.put("client_id", clientId);
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                String imagename = "SabPaisa_Image";
                params.put("file", new DataPart(imagename + ".jpeg", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    /*
     * The method is taking Bitmap as an argument
     * then it will return the byte[] array for the given bitmap
     * and we will send this array to the server
     * here we are using JPEG Compression with 80% quality
     * you can give quality between 0 to 100
     * 0 means worse quality
     * 100 means best quality
     * */
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v("PAFS", "Internet Connection Not Present");
            return false;
        }
    }





}
