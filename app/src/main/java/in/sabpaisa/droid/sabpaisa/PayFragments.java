package in.sabpaisa.droid.sabpaisa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import in.sabpaisa.droid.sabpaisa.Adapter.ViewPagerAdapter;
import in.sabpaisa.droid.sabpaisa.Fragments.ProceedInstitiutionFragment;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;
import in.sabpaisa.droid.sabpaisa.Util.ProfileNavigationActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by SabPaisa on 03-07-2017.
 */

public class PayFragments extends Fragment {

    ViewPager viewPager;
    TabLayout tabs;
    View rootView;
    String landing_page;
    Button btn_payQC, btn_payLP;
    String responseQC, responseLP;


    private static final String TAG = PayFragments.class.getSimpleName();

    public PayFragments() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragments_pay, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(ProceedInstitiutionFragment.MYSHAREDPREFProceed, MODE_PRIVATE);
        landing_page = sharedPreferences.getString("landing_page", "123");
        Log.d("payfragment", "landing_page");
        //serviceName=sharedPreferences.getString("SERVICENAME","123");

       /* String landing_page = getArguments().getString("landing_page");
        Log.d("strtext",""+landing_page);*/
        /*Bundle bundle = getIntent().getExtras();
        String strtext = bundle.getString("landingpage");
        Log.d("strtext",""+strtext);*/
        /*viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);*/
        Button btn_pay = (Button) rootView.findViewById(R.id.btn_pay);


        tabs = (TabLayout) rootView.findViewById(R.id.tabs);
//        tabs.setupWithViewPager(viewPager);
        return rootView;
    }
//    Button btn_pay = (Button) rootView.findViewById(R.id.btn_pay);


    //utton13 = (Button) view.findViewById(R.id.button13);


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

/*
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(ProceedInstitiutionFragment.MYSHAREDPREFProceed, MODE_PRIVATE);
        landing_page=sharedPreferences.getString("landing_page","123");
        Log.d("payfragme",""+landing_page);*/

//Added on 2nd Feb

        btn_payQC = (Button) rootView.findViewById(R.id.btn_pay);
        btn_payLP = (Button) rootView.findViewById(R.id.btn_payLP);
//Added on 2nd Feb
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FullViewOfClientsProceed.MySharedPrefOnFullViewOfClientProceed, Context.MODE_PRIVATE);
        final String clientId = sharedPreferences.getString("clientId", "abc");
        Log.d("clientId_PAY", "" + clientId);


//Added on 2nd Feb


        paymentJSON(clientId);


    }

 /*   private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
       // adapter.addFragment(new FormFragments(), "Form Name");
        //adapter.addFragment(new PaymentHistoryFragment(), "Payment History");
        viewPager.setAdapter(adapter);
    }*/

//Added on 2nd Feb

    private void paymentJSON(final String clientId) {

        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.Base_Url+AppConfig.App_api+AppConfig.URL_Payment + "?clientId=" + clientId, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {
                Log.d(TAG, "PaymentRESP" + response1.toString());

                try {
                    //progressBar.setVisibility(View.GONE);
                    JSONObject object = new JSONObject(response1);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        responseQC = object.getJSONObject("response").getString("QC");
                        responseLP = object.getJSONObject("response").getString("LP");

                        Log.d("PaymentRESP", "response" + response1);
                        Log.d("PaymentRESP", "status" + status);

                        btn_payQC.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // do something

                           Intent i = new Intent(getContext(), WebViewActivity.class);
                             /* Intent intent = new Intent(Intent.ACTION_VIEW,
                              Uri.parse("http://43.252.89.223:9191/QwikCollect/Canarabank.jsp"));*/
                             i.putExtra("QC", responseQC);
                                startActivity(i);
                               //  Toast.makeText(getContext(), "No Data Found !", Toast.LENGTH_SHORT).show();

                            }
                        });


                        btn_payLP.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(getContext(), WebViewActivity.class);
                          /*      Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://43.252.89.223:9191/QwikCollect/Canarabank.jsp"));*/
                                i.putExtra("QC", responseLP);
                                startActivity(i);

                               // Toast.makeText(getContext(), "No Data Found !", Toast.LENGTH_SHORT).show();

                            }
                        });


                    } else if (status.equals("failure") && (response1.equals("No Record Found"))) {

                        Log.d("PayFrag", "InElseifPart");
                        btn_payQC.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // do something

//                               /*Intent i = new Intent(getContext(), WebViewActivity.class);
//                               *//*Intent intent = new Intent(Intent.ACTION_VIEW,
//                                Uri.parse("http://43.252.89.223:9191/QwikCollect/Canarabank.jsp"));*//*
//                                i.putExtra("QC", responseQC);
//                                startActivity(i);*/
                                Toast.makeText(getContext(), "No Data Found !", Toast.LENGTH_SHORT).show();

                            }
                        });


                        btn_payLP.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               /* Intent i = new Intent(getContext(), WebViewActivity.class);
                                *//*Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://43.252.89.223:9191/QwikCollect/Canarabank.jsp"));*//*
                                i.putExtra("QC", responseLP);
                                startActivity(i);
*/
                                Toast.makeText(getContext(), "No Data Found !", Toast.LENGTH_SHORT).show();

                            }
                        });
                        Toast.makeText(getContext(), "No Data Found1 !", Toast.LENGTH_SHORT).show();

                    } else {

                        btn_payQC.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme).create();

                                // Setting Dialog Title
                                alertDialog.setTitle("Payment through QC");

                                // Setting Dialog Message
                                alertDialog.setMessage("Payment Facility is not available.");

                                // Setting Icon to Dialog
                                //  alertDialog.setIcon(R.drawable.tick);

                                // Setting OK Button
                                alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        // Write your code here to execute after dialog closed
                                        // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                // Showing Alert Message
                                alertDialog.show();
                            }
                        });


                        btn_payLP.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme).create();

                                // Setting Dialog Title
                                alertDialog.setTitle("Payment through LP ");

                                // Setting Dialog Message
                                alertDialog.setMessage("Payment Facility is not available.");

                                // Setting Icon to Dialog
                                //  alertDialog.setIcon(R.drawable.tick);

                                // Setting OK Button
                                alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog closed
                                        // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                // Showing Alert Message
                                alertDialog.show();
                            }
                        });
                        // Toast.makeText(getContext(), "No Data Found2 !", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage() == null || error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Network/Connection Error");

                    // Setting Dialog Message
                    alertDialog.setMessage("Internet Connection is poor OR The Server is taking too long to respond.Please try again later.Thank you.");

                    // Setting Icon to Dialog
                    //  alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed
                            // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                    Log.e(TAG, "Update Error: " + error.getMessage());

                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                } else if (error instanceof NetworkError) {
                    //TODO
                } else if (error instanceof ParseError) {
                    //TODO
                }

            }
        }); /*{

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userAccessToken", userAccessToken);

                return params;
            }

        };
*/
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


}