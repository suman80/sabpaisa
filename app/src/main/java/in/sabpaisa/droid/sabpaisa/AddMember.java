package in.sabpaisa.droid.sabpaisa;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import in.sabpaisa.droid.sabpaisa.Model.AddMemberTableModel;
import in.sabpaisa.droid.sabpaisa.Model.Member_GetterSetter;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.FullViewOfClientsProceed;

public class AddMember extends AppCompatActivity {

    static final int RESULT_PICK_CONTACT = 2;
    ImageView contactChooser;

    String clientId;

    ArrayList<String> fieldvalueArrayList;

    LinearLayout linearLayoutParent;

    TextView tv1;
    EditText et;

    List<EditText> editTextIDsList;

    Button btnAdd,btnCancel;

    HashMap<String,EditText> hashMap;

    String userAccessToken;

    Toolbar toolbar;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        contactChooser = (ImageView) findViewById(R.id.contactChooser);

        linearLayoutParent = (LinearLayout)findViewById(R.id.linearLayoutParent);

        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnCancel = (Button)findViewById(R.id.btnCancel);

        toolbar = (Toolbar)findViewById(R.id.toolbar);

        progressDialog = new ProgressDialog(AddMember.this,R.style.DialogTheme);

        toolbar.setTitle("Add Member");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_action_previousback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(FullViewOfClientsProceed.MySharedPrefOnFullViewOfClientProceed, Context.MODE_PRIVATE);
        clientId=sharedPreferences.getString("clientId","abc");
        Log.d("AddMember","CLIENT_ID_ :"+clientId);

        SharedPreferences sharedPreferences1 = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        userAccessToken = sharedPreferences1.getString("response", "123");


        contactChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);

            }
        });

        tableFields(clientId);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOnline()){

                progressDialog.setMessage("Please wait !");
                progressDialog.show();

                int count = 0;
                JSONObject jsonObject = new JSONObject();

                for (EditText id : editTextIDsList) {

                    Log.d("Values&IDs______" + fieldvalueArrayList.get(count), " " + id.getText().toString());

                    try {

                        if (fieldvalueArrayList.get(count).equals("CONTACT_NUMBER")) {

                            String number = id.getText().toString();

                            Log.d("CONTACT_NUMBER_Val_","__"+number);

                            if (number.startsWith("+91")){
                                number = number.replace("+91","");
                                Log.d("Num_AfterReplace91","__"+number);

                            }else if (number.startsWith("0")){
                                number=number.substring(1);
                                Log.d("Num_AfterSubStrng0","__"+number);
                            }

                            jsonObject.put(fieldvalueArrayList.get(count), number);

                        } else {
                            jsonObject.put(fieldvalueArrayList.get(count), id.getText().toString());
                        }
                        count++;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                /*Iterator<String> iter = jsonObject.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    try {
                        Object value = jsonObject.get(key);

                        Log.d("JSON_OBJ"," "+value);

                    } catch (JSONException e) {
                        // Something went wrong!
                    }
                }*/



                //Api
                saveUINtableData(clientId,userAccessToken,jsonObject);

            }else {
                    Toast.makeText(AddMember.this,"No Internet Connection !",Toast.LENGTH_SHORT).show();
                }

            }


        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok


        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    for(Map.Entry entry : hashMap.entrySet())
                        contactPicked(data, /*memberNameEditText,*/ (EditText) entry.getValue());
                    break;

            }
        } else {
            Log.e("AddMember", "Failed to pick contact");
        }

    }


    /**
     * Query the Uri and read contact details. Handle the picked contact data.
     *
     * @param data
     */
    private void contactPicked(Intent data, /*EditText cName,*/ EditText cNumber) {
        Cursor cursor = null;
        try {
            String phoneNo = null;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);

            StringBuilder stringBuilder = new StringBuilder();

            char[] arr = phoneNo.toCharArray();

            for (char c : arr) {
                if (c != ' ')
                    stringBuilder.append(c);
            }

            phoneNo = stringBuilder.toString();



            name = cursor.getString(nameIndex);
            // Set the value to the textviews

//            cName.setText(name);
            cNumber.setText(phoneNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void tableFields(String clientId) {

        String tag_string_req = "req_clients";

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_tableFields + "?clientId=" + clientId;
        //String url = "https://portal.sabpaisa.in/SabPaisaAppApi_Oct14/tableFields?clientId="+clientId;

        Log.d("AddMember", "URL-->" + url);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response1) {

                Log.d("AddMember", "Resp--> " + response1);
                //parsing Json
                JSONObject jsonObject = null;

                try {

                    //memberTableModelArrayList = new ArrayList<>();
                    fieldvalueArrayList = new ArrayList<>();
                    editTextIDsList = new ArrayList<>();
                    hashMap = new HashMap<>();

                    jsonObject = new JSONObject(response1);
                    String response = jsonObject.getString("response");
                    String status = jsonObject.getString("status");
                    Log.d("AddMember1Resp", " " + response);
                    Log.d("AddMember1Status", " " + status);

                    if (status.equals("success")) {
                        Log.d("AddMember", "InIfPart");

                        JSONArray jsonArray = jsonObject.getJSONArray("response");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                           String fieldValue = jsonObject1.getString("Field");

                            Log.d("fieldValue"," "+fieldValue);

                            fieldvalueArrayList.add(fieldValue);

                            if (fieldvalueArrayList.contains("id")/* && fieldvalueArrayList.contains("EmployeeId")*/
                                    && fieldvalueArrayList.contains("STATUS")&& fieldvalueArrayList.contains("role"))
                            {
                                fieldvalueArrayList.remove("id");
                                fieldvalueArrayList.remove("STATUS");
                                fieldvalueArrayList.remove("role");


                                Log.d("remainingValue"," "+fieldvalueArrayList.get(0));


                                for (int k = 0; k < fieldvalueArrayList.size(); k++) {


                                    tv1 = new TextView(AddMember.this);
                                    tv1.setText(fieldvalueArrayList.get(k));
                                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                                    tv1.setTextColor(getResources().getColor(R.color.bg_orange));
                                    tv1.setId(k);
                                    Log.d("TextView_","IDs_: "+tv1.getId());

                                    LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    tv1Params.bottomMargin = 5;
                                    tv1Params.leftMargin = 50;
                                    tv1Params.rightMargin = 50;
                                    linearLayoutParent.addView(tv1,tv1Params);

                                    et = new EditText(AddMember.this);
                                    et.setId(k);

                                    if(fieldvalueArrayList.get(k).equals("CONTACT_NUMBER")){
                                        et.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    }

                                    Log.d("EditText_","IDs_: "+et.getId());
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        et.setBackground(getDrawable(R.drawable.background_edittext));
                                    }

                                    editTextIDsList.add(et);

                                    if(fieldvalueArrayList.get(k).equals("CONTACT_NUMBER")) {
                                        hashMap.put(tv1.getText().toString(), et);
                                    }

                                    LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    editTextParams.leftMargin = 50;
                                    editTextParams.rightMargin = 50;
                                    linearLayoutParent.addView(et,editTextParams);



                                }



                            }


                        }


                    } else {
                        Log.d("AddMember", "InElsePart");

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage() == null || error instanceof TimeoutError || error instanceof NoConnectionError) {
                    android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(AddMember.this, R.style.MyDialogTheme).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Network/Connection Error");

                    // Setting Dialog Message
                    alertDialog.setMessage("Internet Connection is poor OR The Server is taking too long to respond.Please try again later.Thank you.");

                    // Setting Icon to Dialog
                    //  alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                    //Log.e(TAG, "Registration Error: " + error.getMessage());

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


        });

        AppController.getInstance().addToRequestQueue(request, tag_string_req);

    }


    private void saveUINtableData(String clientId, String userAccessToken , JSONObject jsonObject) {

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_saveUINtableData+ "?client_Id=" + clientId +"&admin="+userAccessToken;


        Log.d("AddMemberUrl", "URL-->" + url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d("AddMember", response.toString() );

                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                String status = null;
                String response1 = null;
                try {
                    status = response.getString("status");
                    response1 = response.getString("response");
                    Log.d("AddMember","RESP : "+response1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (status != null && status.equals("success")) {

                    Log.d("AddMember","InIFPart");
                    Toast.makeText(AddMember.this,response1,Toast.LENGTH_SHORT).show();
                    String clientImageURLPath = FullViewOfClientsProceed.clientImageURLPath;

                    Log.d("AddMember","clientImageURLPath "+clientImageURLPath);

                    Intent intent = new Intent(AddMember.this,FullViewOfClientsProceed.class);
                    intent.putExtra("clientImagePath",clientImageURLPath);
                    intent.putExtra("FRAGMENT_ID","3");
                    startActivity(intent);



                }  else {
                    Log.d("AddMember","InElsePart");
                    Toast.makeText(AddMember.this,response1,Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("AddMember", "Error: " + error.getMessage());
                VolleyLog.d("AddMember", "Error: " + error.getMessage());

                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

        };

        // Adding request to request queue
        Volley.newRequestQueue(AddMember.this).add(jsonObjReq);



    }



    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v("PGF", "Internet Connection Not Present");
            return false;
        }
    }





}
