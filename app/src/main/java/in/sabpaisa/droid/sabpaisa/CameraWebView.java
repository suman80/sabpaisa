package in.sabpaisa.droid.sabpaisa;

import android.app.Activity;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class CameraWebView extends AppCompatActivity {

    public static String TAG = "CWV__";
    WebView myWebView;
    private WebView webView;
    final Activity activity = this;
    public Uri imageUri;

    private static final int FILECHOOSER_RESULTCODE   = 2888;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_web_view);


        myWebView = (WebView) findViewById(R.id.webview);

        //String webViewUrl = "http://www.androidexample.com/media/webview/details.html";

        String url = getIntent().getStringExtra("URL");


        // Javascript inabled on webview
        myWebView.getSettings().setJavaScriptEnabled(true);

        // Other webview options
        myWebView.getSettings().setLoadWithOverviewMode(true);

        //webView.getSettings().setUseWideViewPort(true);

        //Other webview settings
        myWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        myWebView.setScrollbarFadingEnabled(false);
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        myWebView.getSettings().setAllowFileAccess(true);
        myWebView.getSettings().setSupportZoom(true);

        myWebView.getSettings().setJavaScriptEnabled(true);

        myWebView.getSettings().setUseWideViewPort(true);

        Log.d("CWV","___"+url);


        //Load url in webview
        //myWebView.loadUrl("https://mansha2.sabpaisa.in/"+url);
        myWebView.loadUrl("192.168.1.150:8000/"+url);





    }




    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {

        if(myWebView.canGoBack()) {

            myWebView.goBack();

        } else {
            // Let the system handle the back button
            super.onBackPressed();
        }
    }





}
