package in.sabpaisa.droid.sabpaisa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
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
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.sabpaisa.droid.sabpaisa.Fragments.ProceedInstitiutionFragment;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.ProfileNavigationActivity;

import static com.google.firebase.crash.FirebaseCrash.log;
import static java.lang.System.*;

/**
 * Created by SabPaisa on 27-07-2017.
 */

public class WebViewActivity extends AppCompatActivity {
    ProgressBar progressBar;
    WebView webView;
    String url,userAccessToken, landing_page, url1;
    String clientcode,mode;
String token,paidAmount,clientName,transcationDate,spTranscationId;

public static String MYSharedpref="WebShared";






    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = (WebView) findViewById(R.id.myWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.loadUrl(postUrl);
        webView.setHorizontalScrollBarEnabled(false);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        SharedPreferences sharedPreferences = getSharedPreferences(ProfileNavigationActivity.MYSHAREDPREFPNA, Context.MODE_PRIVATE);
       // clientId=sharedPreferences.getString("clientId","123");
        userAccessToken=sharedPreferences.getString("response","123");
        String re=userAccessToken;

       // Log.d("UinDialogclientid",""+clientId);
        Log.d("useraccesstokenweb","****"+re);


        SharedPreferences sharedPreferences1 = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

       String response = sharedPreferences1.getString("response", "123");

        if(response!=null) {

           token = response;
            Log.d("webvieewtoken", " " + token);

            Log.d("webviewrt", " " + response);
        }


        Log.d("timesadate88",""+transcationDate);
        Log.d("timesampyyid88",""+spTranscationId);
        Log.d("timesampyyamout88",""+paidAmount);
        Log.d("timesampyyclnt88",""+clientName);
        Log.d("timesampyytokn88",""+token);


        //savetransaction("jhsajkdhasdjkasdnhjkasjdkasnjk","SP123454312","110.11","SabPaisa","20 March 2018 12:14:06");
       /* SharedPreferences sharedPreferences = getApplication().getSharedPreferences(ProceedInstitiutionFragment.MYSHAREDPREFProceed, MODE_PRIVATE);
        landing_page=sharedPreferences.getString("landing_page","123");
        Log.d("WebView",""+landing_page);
        url=landing_page;*/

       /* SharedPreferences sp=getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putString("id",token );
        Ed.putString("password",login_password.getText().toString() );
        Ed.commit();*/


        ////////////////////////////////////////////////
        //22nd March,2018
        url=getIntent().getStringExtra("URL");
        // url1=getIntent().getStringExtra("LP");
        //////////////////////////////////////////////////
        Log.d("webViewURL", " " + url);
        //Log.d("webViewURL"," "+url1);
        webView.loadUrl(url);
        //webView.loadUrl("https://"+url.trim());
        // webView.loadUrl("https://"+url1.trim());
/*------------------- //Added on 2nd Feb------------------------------------*/
        progressBar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);

/*
        webView.setWebViewClient(new MyWebViewClient());
*/


        final WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);



        webView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                WebView.HitTestResult hr = ((WebView)v).getHitTestResult();

                Log.d("TOuchevent", "getExtra = "+ hr.getExtra() + "\t\t Type=" + hr.getType());
                return false;
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


        /////////////////////////////////////////
        webView.addJavascriptInterface(new MyJavaScriptInterface(WebViewActivity.this), "HtmlViewer");





        webView.setWebViewClient(new WebViewClient() {
                                     @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                     @Override
                                     public void onPageFinished(WebView view, String url) {


                                         ////////////////////




                                         webView.evaluateJavascript("(function() { return (document.getElementsByName(\"cliencode\")[0].value); })();", new ValueCallback<String>() {
                                             @Override
                                             public void onReceiveValue(String html) {

                                                 ////SSNC1///

                                                 SharedPreferences.Editor editor = getSharedPreferences(MYSharedpref, MODE_PRIVATE).edit();
                                                 editor.putString("clientname", clientName);
                                                 editor.commit();

                                               clientName  =html.replace("\"","");
                                                 Log.d("HTML890tclientname", clientName);


                                                 Log.d("HTML8901clientname", html);

/*if(!html.isEmpty())
{
    finish();
}
  */                                              // ((Activity) ctx).finish();

                                             }
                                         });





                                         /*webView.evaluateJavascript("(function() { return (document.findElement(By.xPath(\"/div/span[contains(., 'Your Transaction was Cancelled ')[0].value]\").getText(); })();", new ValueCallback<String>() {
                                             @Override
                                             public void onReceiveValue(String html) {


                                                 clientcode=html.replace("\"","");
                                                 Log.d("", clientcode);
                                                 Log.d("HTML89011cliencode", html);


OK

                                             }
                                         });
*/
/*
                                         webView.evaluateJavascript("(function() { return (document.getElementsByName(\"epResponse.clientLName\")[0].value); })();", new ValueCallback<String>() {
                                             @Override
                                             public void onReceiveValue(String html) {

                                                 clientcode=html.replace("\"","");


                                                 Log.d("HTML89012clientnme", html);
                                                 Log.d("HTML89012clientnme", clientcode);

                                             }
                                         });*/


                                         webView.evaluateJavascript( "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();", new ValueCallback<String>() {
                                             @Override
                                             public void onReceiveValue(String html) {
                                                 Log.d("Archana", html); // code here


                                             } });
                                         webView.evaluateJavascript( "(function() { return (Html.fromHtml(getString(R.string.nice_html)[0].innerHTML+'</html>'); })();", new ValueCallback<String>() {
                                             @Override
                                             public void onReceiveValue(String html) {
                                                 Log.d("Arhu", html); // code here


                                             } });
                                         webView.evaluateJavascript("(function() { return (document.getElementsByName(\"epResponse.amount\")[0].value); })();", new ValueCallback<String>() {
                                             @Override
                                             public void onReceiveValue(String html) {


                                                 paidAmount=html.replace("\"","");
                                                 Log.d("HTML8901amount", paidAmount);
                                                 Log.d("HTML8901amount", html);


                                             }
                                         });
                                         webView.evaluateJavascript("(function() { return (document.getElementsByName(\"epResponse.SPTxnId\")[0].value); })();", new ValueCallback<String>() {
                                             @Override
                                             public void onReceiveValue(String html) {



                                                 Log.d("HTML890", html);


                                                  spTranscationId=html.replace("\"","");
                                                 Log.d("HTML890SPTxnid", spTranscationId);




if((!html.equals("null")))
{
   /* 20 March 2018 12:14:06*/

         String   ts = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss").format(new Date());
    Long tsLong = System.currentTimeMillis();
    transcationDate = tsLong.toString();
//    java.sql.Timestamp ts = java.sql.Timestamp.valueOf( transcationDate ) ;

    Log.d("timesadate",""+transcationDate);
    Log.d("timesadatesq",""+ts);
                Log.d("timesampyyid",""+spTranscationId);
                Log.d("timesampyyamout",""+paidAmount);
                Log.d("timesampyyclnt",""+clientName);
                Log.d("timesampyyclnt77",""+clientcode);

                Log.d("timesampyytokn",""+token);
    //savetransaction(token,spTranscationId,paidAmount,clientName,transcationDate);

    /*finish();

    Intent intent=new Intent(WebViewActivity.this,AllTransactionSummary.class);
    startActivity(intent);*/


}



                                             }
                                         });

                                         webView.evaluateJavascript("(function() { return ( document.select(\"div.container payment-section-wishlist.Cancelled\").first();\"})();", new ValueCallback<String>() {
                                             @Override
                                             public void onReceiveValue(String html) {

                                                  mode=html.replace("\"","");
                                                 // String mode1=mode.substring(19);
                                                 Log.d("bkunkl,", mode);
                                                 //Log.d("bkunkl,", mode1);

                                                 Log.d("mbhj", html);

                                             }
                                         });

                                webView.evaluateJavascript("(function() { return (document.getElementsById(\"txtEmail\")[0].value); })();", new ValueCallback<String>() {
                                             @Override
                                             public void onReceiveValue(String html) {

                                                 String emailID=html.replace("\"","");
                                                 Log.d("HTML89011emailId", emailID);

                                                 Log.d("HTML89011emailId", html);


                                             }
                                         });
                                webView.evaluateJavascript("(function() { return (document.getElementsById(\"txtMobileNumber\")[0].value); })();", new ValueCallback<String>() {
                                             @Override
                                             public void onReceiveValue(String html) {

                                                 String txtMobileNumber=html.replace("\"","");
                                                 Log.d("HTML890tMobileNumber", txtMobileNumber);

                                                 Log.d("HTML890MobileNumber", html);
                                             }
                                        });
                                webView.loadUrl("javascript:window.HtmlViewer.showHTML" +
                                                   "('&lt;html&gt;'+document.getElementsByTagName('html')[0].innerHTML+'&lt;/html&gt;');");


                                     }
                                 });



      /*  webView.setOnTouchListener(new View.OnTouchListener() {

            public final static int FINGER_RELEASED = 0;
            public final static int FINGER_TOUCHED = 1;
            public final static int FINGER_DRAGGING = 2;
            public final static int FINGER_UNDEFINED = 3;

            private int fingerState = FINGER_RELEASED;


            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        if (fingerState == FINGER_RELEASED) fingerState = FINGER_TOUCHED;
                        else fingerState = FINGER_UNDEFINED;
                        break;

                    case MotionEvent.ACTION_UP:
                        if(fingerState != FINGER_DRAGGING) {
                            fingerState = FINGER_RELEASED;

                            // Your onClick codes

                        }
                        else if (fingerState == FINGER_DRAGGING) fingerState = FINGER_RELEASED;
                        else fingerState = FINGER_UNDEFINED;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (fingerState == FINGER_TOUCHED || fingerState == FINGER_DRAGGING) fingerState = FINGER_DRAGGING;
                        else fingerState = FINGER_UNDEFINED;
                        break;

                    default:
                        fingerState = FINGER_UNDEFINED;

                }

                return false;
            }
        });

*/


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //Required functionality here
                Log.d("Alert", "" + message);
                return super.onJsAlert(view, url, message, result);
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






       /* Log.d("timesadate",""+transcationDate);
        Log.d("timesampyyid",""+spTranscationId);
        Log.d("timesampyyamout",""+paidAmount);
        Log.d("timesampyyclnt",""+clientName);
        Log.d("timesampyyclnt77",""+clientcode);

        Log.d("timesampyytokn",""+token);
        savetransaction(token,spTranscationId,paidAmount,clientName,transcationDate);

*/
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK: if (webView.canGoBack()) {
                    webView.goBack();
                }
                else {
                    finish();
                } return true;
            }
        } return super.onKeyDown(keyCode, event);
    }
    private class MyWebViewClient extends WebViewClient {

        @Override

        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return false;
        }
    }

    public class MyJavaScriptInterface {
        public Context ctx;
        public JSONObject jsonObject;
        //  SaabPisaPG saabPisaPG;


        Handler handlerForJavascriptInterface;
/*

        public MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
            this.saabPisaPG = iResult;
        }
*/

        public MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }


        @JavascriptInterface
        public void showHTML(final String html) {
            Log.d("All_HTml_Content", html);
            //  webView.loadUrl("javascript:  document.getEl('mer_txn').value");
            if (html.contains("\"status\":\"success\", \"response\"")) {

                if (html.contains("\"epResponse.SPTxnId\"")) {
                    handlerForJavascriptInterface = new Handler();
//code to use html content here
                    handlerForJavascriptInterface.post(new Runnable() {
                        @Override
                        public void run() {
/*Toast toast = Toast.makeText(ctx, "Page has been loaded in webview. html content :"+html, Toast.LENGTH_LONG);
toast.show();*/
                            Log.d("htmlcontent", "" + html);

                            //  webView.loadUrl("javascript:function myFunction() { var x = document.getElementById('thebox').value; Android.processHTML(x); } myFunction();");
                            String reqString = html.substring(31, html.length() - 20);

                            int reqString1 = (html.indexOf("epResponse.SPTxnId"));

                            Log.d("reqString", " " + reqString);

                        /* jsonObject = new JSONObject(reqString);
                         String response=jsonObject.getString("response");
                         JSONObject jsonObject1=new JSONObject(response);
                         String SabPaisaTxId=jsonObject1.getString("SabPaisaTxId");
                         String firstName=jsonObject1.getString("firstName");
                         String lastName=jsonObject1.getString("lastName");
                         String payMode=jsonObject1.getString("payMode");
                         String email=jsonObject1.getString("email");
                         String mobileNo=jsonObject1.getString("mobileNo");
                         String transDate=jsonObject1.getString("transDate");
                         String orgTxnAmount=jsonObject1.getString("orgTxnAmount");
*/


                        /*    Log.d("jsonObject", "" + jsonObject);
                            Log.d("jsonObjectresponse", "" + response);
                            Log.d("SabPaisaTxId", "" + SabPaisaTxId);
                            Log.d("firstName", "" + firstName);
                            Log.d("lastName", "" + lastName);
                            Log.d("payMode", "" + payMode);
                            Log.d("email", "" + email);
                            Log.d("mobileNo", "" + mobileNo);
                            Log.d("transDate", "" + transDate);
                            Log.d("orgTxnAmount", "" + orgTxnAmount);
*/
                            // new EDW().resonse(jsonObject);

                           /* Intent intent=new Intent("sabpaisa.neeraj.com.sabpaisaappintegrationdemo.MainActivity");
                            intent.putExtra("abc",mobileNo);
                            startActivity(intent);*/


                        }
                    });
                }
            }
        }

/*
    public void web(){

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log(doc.title());
        Log.d("",""+doc.title());

        Elements newsHeadlines = doc.select("#mp-itn b a");
        for (Element headline : newsHeadlines) {
            //log("%s\n\t%s",headline.attr("title"), headline.absUrl("href"));
            Log.d("%s\n\t%s",""+headline.attr("title")+ headline.absUrl("href"));

        }




ok



    }
*/
    }


    public  void savetransaction(final String token,final String spTranscationId,final  String paidAmount,final  String clientName,final String transcationDate)
    {
String urlJsonObj=AppConfig.Base_Url+AppConfig.URL_AllTransaction+token+"&spTranscationId="+spTranscationId+"&paidAmount="+paidAmount+"&clientName="+clientName+"&transcationDate="+transcationDate;
        urlJsonObj=urlJsonObj.trim().replaceFirst("[ ]","");
     //   urlJsonObj = urlJsonObj.substring(0, urlJsonObj.length() );

      //  urlJsonObj=urlJsonObj.trim().replaceFirst("0x2647f47f NORMAL null","");
      //  urlJsonObj = urlJsonObj.trim().replace(" ", "%20");




Log.d("urljsnweb",""+urlJsonObj);
        JsonObjectRequest jsonObjReq =new JsonObjectRequest(Request.Method.POST, urlJsonObj,null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d("savetransaction",""+response);

                try {
                    String status = response.getString("status");
                    String res = response.getString("response");
                    Log.d("savetransactionstts",""+status);
                    Log.d("savetransactiorespo",""+res);



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }); /*{

            @Override
            protected Map<String, String> getParams() {


                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("spTranscationId", spTranscationId);
                params.put("paidAmount", paidAmount);
                params.put("clientName", clientName);
                params.put("transcationDate", transcationDate);
                params.put("token",token );
                // params.put("dob", dob );


                return params;
            }*/
                //

        Log.d("urltchhi",""+jsonObjReq );


        AppController.getInstance().addToRequestQueue(jsonObjReq );
    }
}




