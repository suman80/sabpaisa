package in.sabpaisa.droid.sabpaisa;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import in.sabpaisa.droid.sabpaisa.Fragments.ProceedInstitiutionFragment;

/**
 * Created by SabPaisa on 27-07-2017.
 */

public class WebViewActivity extends AppCompatActivity {
    ProgressBar progressBar;
    WebView webView;
    String url,landing_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = (WebView) findViewById(R.id.myWebView);
        progressBar =(ProgressBar) findViewById(R.id.progressBar);

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(ProceedInstitiutionFragment.MYSHAREDPREFProceed, MODE_PRIVATE);
        landing_page=sharedPreferences.getString("landing_page","123");
        Log.d("WebView",""+landing_page);
        url=landing_page;
        webView.loadUrl(url);

        progressBar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);

        webView.setWebViewClient(new MyWebViewClient());

        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //ProgressBar progressBar = null;
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);

                } else {
                    progressBar.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    private class MyWebViewClient extends WebViewClient {

        @Override

        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return false;
        }
    }
}

