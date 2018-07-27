package in.sabpaisa.droid.sabpaisa;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.ProfileNavigationActivity;

//import static com.google.firebase.crash.FirebaseCrash.log;


public class WebViewActivity extends AppCompatActivity {
    public static String MYSharedpref = "WebShared";
    public static String finalstatus;
    ProgressBar progressBar;
    WebView webView;
    String url, userAccessToken, landing_page, url1;
    String clientcode, mode, status;
    String token, paidAmount, clientName, transcationDate, spTranscationId = null;
    String status_cancel, status_failure, status_pending, status_succes;
    String searchText = "Your Transaction is Cancelled";
    String searchTextCancelFromAtom = "Your Transaction was Cancelled";
    String searchText1 = "Your Transaction is Complete";
    String searchText2 = "Your Transaction failed";
    boolean statusCancel = false;
    boolean statusFail = false;
    boolean statusSuccess = false;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        webView = (WebView) findViewById(R.id.myWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.loadUrl(postUrl);
        webView.setHorizontalScrollBarEnabled(false);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        SharedPreferences sharedPreferences = getSharedPreferences(ProfileNavigationActivity.MYSHAREDPREFPNA, Context.MODE_PRIVATE);
        // clientId=sharedPreferences.getString("clientId","123");
        userAccessToken = sharedPreferences.getString("response", "123");
        String re = userAccessToken;

        // Log.d("UinDialogclientid",""+clientId);
        Log.d("useraccesstokenweb", "****" + re);


        SharedPreferences sharedPreferences1 = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        String response = sharedPreferences1.getString("response", "123");

        if (response != null) {

            token = response;
            Log.d("webvieewtoken", " " + token);

            Log.d("webviewrt", " " + response);
        }


        Log.d("timesadate88", "" + transcationDate);
        Log.d("timesampyyid88", "" + spTranscationId);
        Log.d("timesampyyamout88", "" + paidAmount);
        Log.d("timesampyyclnt88", "" + clientName);
        Log.d("timesampyytokn88", "" + token);
        //22nd March,2018
        url = getIntent().getStringExtra("URL");
        // url1=getIntent().getStringExtra("LP");
        Log.d("webViewURL", " " + url);
        webView.loadUrl(url);
        /*------------------- //Added on 2nd Feb------------------------------------*/
        progressBar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);

        final WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //Required functionality here
                Log.d("Alert", "" + message);
                return super.onJsAlert(view, url, message, result);
            }
        });
        /////////////////////////////////////////
        webView.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                Log.d("WebViewstart", "your current url when webpage loading start.." + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(webView, url);
               /* webView.loadUrl("javascript:window.HtmlViewer.showHTML" +
                        "('&lt;html&gt;'+document.getElementsByTagName('html')[0].innerHTML+'&lt;/html&gt;');");
*/
                if (url.matches(".*https://portal.sabpaisa.in/SabPaisaResponseHandler/resQuery.*"))
                    ;
                Log.d("WebView", "your current url when webpage loading.." + url);
                Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.d("WebView13123", "your current url when webpage loading.." + url);
                Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }

        });
        webView.addJavascriptInterface(new MyJavaScriptInterface(WebViewActivity.this), "HtmlViewer");

        webView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onPageFinished(WebView view, String url) {

                webView.evaluateJavascript("(function() { return (document.getElementsByName(\"cliencode\")[0].value); })();", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {

                        SharedPreferences.Editor editor = getSharedPreferences(MYSharedpref, MODE_PRIVATE).edit();
                        editor.putString("clientname", clientName);
                        editor.commit();
                        clientName = html.replace("\"", "");
                        Log.d("HTML890tclientname", clientName);
                        Log.d("HTML8901clientname", html);
                    }
                });

                webView.evaluateJavascript("(function() { return (document.getElementsByName(\"epResponse.amount\")[0].value); })();", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {


                        paidAmount = html.replace("\"", "");
                        Log.d("HTML8901amount", paidAmount);
                        Log.d("HTML8901amount", html);


                    }
                });
                webView.evaluateJavascript("(function() { return (document.getElementsByName(\"epResponse.SPTxnId\")[0].value); })();", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {

                        Log.d("HTML890", html);
                        spTranscationId = html.replace("\"", "");
                        Log.d("HTML890SPTxnid", spTranscationId);
//                                                 Log.d("HTML890SPTxnid_status", finalstatus);

                    }
                });

                webView.evaluateJavascript("(function() { return ( document.select(\"div.container payment-section-wishlist.Cancelled\").first();\"})();", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {

                        mode = html.replace("\"", "");
                        // String mode1=mode.substring(19);
                        Log.d("bkunkl,", mode);
                        //Log.d("bkunkl,", mode1);

                        Log.d("mbhj", html);

                    }
                });

                webView.evaluateJavascript("(function() { return (document.getElementsById(\"txtEmail\")[0].value); })();", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {

                        String emailID = html.replace("\"", "");
                        Log.d("HTML89011emailId", emailID);

                        Log.d("HTML89011emailId", html);


                    }
                });
                webView.evaluateJavascript("(function() { return (document.getElementsById(\"txtMobileNumber\")[0].value); })();", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {

                        String txtMobileNumber = html.replace("\"", "");
                        Log.d("HTML890tMobileNumber", txtMobileNumber);

                        Log.d("HTML890MobileNumber", html);
                    }
                });
                webView.loadUrl("javascript:window.HtmlViewer.showHTML" +
                        "('&lt;html&gt;'+document.getElementsByTagName('html')[0].innerHTML+'&lt;/html&gt;');");


            }
        });


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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    public void savetransaction(final String token, final String spTranscationId, final String paidAmount, final String clientName, final String transcationDate, final String payment_status) {

        String urlJsonObj = AppConfig.Base_Url + AppConfig.URL_AllTransaction + token + "&spTranscationId=" + spTranscationId + "&paidAmount=" + paidAmount + "&clientName=" + clientName + "&transcationDate=" + transcationDate + "&payment_status=" + payment_status;
        urlJsonObj = urlJsonObj.trim().replaceFirst("[ ]", "");
        Log.d("urljsnweb", "" + urlJsonObj);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d("savetransaction", "" + response);

                try {
                    String status = response.getString("status");
                    String res = response.getString("response");
                    Log.d("savetransactionstts", "" + status);
                    Log.d("savetransactiorespo", "" + res);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Log.d("urltchhi", "" + jsonObjReq);


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public class MyJavaScriptInterface {
        public Context ctx;
        public JSONObject jsonObject;
        //  SaabPisaPG saabPisaPG;


        Handler handlerForJavascriptInterface;

        public MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }


        @JavascriptInterface
        public void showHTML(final String html) {
            Log.d("All_HTml_Content", html);
            if (html.contains(searchText))
                Log.d("RAJ KUMAR", "true11");
            else
                Log.d("FAILING CITY", "STARLIN");
            status_cancel = String.valueOf((html.toLowerCase().contains(searchTextCancelFromAtom.toLowerCase())));
            status_succes = String.valueOf(html.toLowerCase().contains(searchText1.toLowerCase()));
            status_failure = String.valueOf(html.toLowerCase().contains(searchText2.toLowerCase()));
            // Log.d("status_cancel", String.valueOf(html.toLowerCase().contains(searchText.toLowerCase()))); // code here
            Log.d("status_cancel", status_cancel);
            if ((html.toLowerCase().contains(searchTextCancelFromAtom.toLowerCase()))) {

                status = "cancelled";
                finalstatus = status;
                Log.d("InIFParCaNcel__R", "----" + finalstatus);

                Log.d("STatus11", String.valueOf(html.toLowerCase().contains(searchText.toLowerCase()))); // code here


                if (!html.equals("null")) {
                    /* 20 March 2018 12:14:06*/

                    String ts = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss").format(new Date());
                    Long tsLong = System.currentTimeMillis();
                    transcationDate = tsLong.toString();
                    Log.d("statusfinal", "" + finalstatus);

                    finish();
                    savetransaction(token, spTranscationId, paidAmount, clientName, transcationDate, finalstatus);

                }


            } else if (html.toLowerCase().contains(searchText1.toLowerCase())) {
                status = "success";
                finalstatus = status;
                Log.d("InelseIFParSuccess", "----" + finalstatus);
                Log.d("STatus112", String.valueOf(html.toLowerCase().contains(searchText1.toLowerCase()))); // code here

                if (!html.equals("null"))
                // if(html != null && finalstatus != null && spTranscationId != null)
                {
                    /* 20 March 2018 12:14:06*/

                    String ts = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss").format(new Date());
                    Long tsLong = System.currentTimeMillis();
                    transcationDate = tsLong.toString();

                    Log.d("statusfinal", "" + finalstatus);

                    finish();
                    savetransaction(token, spTranscationId, paidAmount, clientName, transcationDate, finalstatus);

                }
            } else if (html.toLowerCase().contains(searchText2.toLowerCase())) {
                status = "failure";
                finalstatus = status;
                Log.d("InelseIFParfailure", "----" + finalstatus);
                Log.d("STatus113", String.valueOf(html.toLowerCase().contains(searchText2.toLowerCase()))); // code here

                if (!html.equals("null"))
                // if(html != null && finalstatus != null && spTranscationId != null)
                {
                    /* 20 March 2018 12:14:06*/

                    String ts = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss").format(new Date());
                    Long tsLong = System.currentTimeMillis();
                    transcationDate = tsLong.toString();
                    Log.d("statusfinal", "" + finalstatus);
                    finish();
                    savetransaction(token, spTranscationId, paidAmount, clientName, transcationDate, finalstatus);

                }
            }
        }

    }
}




