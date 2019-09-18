package in.sabpaisa.droid.sabpaisa;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SabPaisa on 27-07-2017.
 */

public class PayFeeAdapter extends RecyclerView.Adapter<PayFeeAdapter.MyViewHolder> {
    Context context;
    private WebView webView;
    ArrayList<String> formNames;

    public PayFeeAdapter(Context context, ArrayList<String> formNames) {
        this.formNames = formNames;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_form, parent, false);
        return new MyViewHolder(v);
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String formName = formNames.get(position);
        holder.formName.setText(formName);

        holder.appyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), WebViewActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return formNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView formName, appyNow;



        public MyViewHolder(View itemView) {
            super(itemView);
            formName = (TextView) itemView.findViewById(R.id.tv_formName);
            appyNow = (TextView) itemView.findViewById(R.id.tv_appyNow);


            appyNow.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                   /* Intent intent = new Intent(this, WebViewActivity.class);
                    intent.putExtra("Url","http://43.252.89.223:9191/QwikCollect/Start.jsp?bid=28&cid=175" );
                    startActivity(intent);*/

                    /*final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://43.252.89.223:9191/QwikCollect/Start.jsp?bid=28&cid=175"));
                    v.getContext().startActivity(intent);*/
                   /* String url="http://43.252.89.223:9191/QwikCollect/Start.jsp?bid=28&cid=175";
                    webView.getSettings().setLoadsImagesAutomatically(true);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                    webView.loadUrl(url);
*/

                   /* webView.setWebViewClient(new WebViewClient(){

                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url){
                            view.loadUrl("http://43.252.89.223:9191/QwikCollect/Start.jsp?bid=28&cid=175");
                            return true;
                        }
                    });*/


                }
            });

        }





      /* appyNow.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.yourURL.com"));
                startActivity(intent);
            }
        });*/

    }
}