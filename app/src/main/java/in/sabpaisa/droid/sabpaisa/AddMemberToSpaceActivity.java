package in.sabpaisa.droid.sabpaisa;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.gson.JsonObject;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import in.sabpaisa.droid.sabpaisa.Adapter.MemberFragmentDialogAdapter;
import in.sabpaisa.droid.sabpaisa.Fragments.AddMemberToSpaceDialogFragment;
import in.sabpaisa.droid.sabpaisa.Interfaces.AddMemberCallBack;
import in.sabpaisa.droid.sabpaisa.Model.ContactVO;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.SkipClientDetailsScreen;

public class AddMemberToSpaceActivity extends AppCompatActivity implements AddMemberCallBack {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private ShimmerRecyclerView mListView;
    private ProgressDialog pDialog;
    private android.os.Handler updateBarHandler;
    Button invitet1;
    ArrayList<String> contactList, nameList;
    Cursor cursor;
    String p1, p2, p3, p4, p5;
    Toolbar toolbar;

    JSONObject object1;
    Map<String, String> mapNameNumber;
    String key;

    ContactVO contactVO;
    String name;
    int counter;
    MaterialSearchView searchView;
    ArrayList<ContactVO> contactVOList;
    ArrayList<ContactVO> contactVOList1;
    //AllContactsAdapter allContactsAdapter;
    MemberFragmentDialogAdapter adapter;

    static ArrayList<String> memberNumberArraylist = new ArrayList<>();


    String appCid,userAccessToken,state,clientLogoPath,clientImagePath,clientName;

    EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member_to_space);

        appCid=getIntent().getStringExtra("appCid");
        state=getIntent().getStringExtra("state");
        clientLogoPath=getIntent().getStringExtra("clientLogoPath");
        clientImagePath=getIntent().getStringExtra("clientImagePath");
        clientName=getIntent().getStringExtra("clientName");

        editTextSearch = (EditText) findViewById(R.id.editTextSearch);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        userAccessToken = sharedPreferences.getString("response", "123");


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_action_previousback);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Log.d("onMenuItemClick","MenuItemgetItemId__"+item.getItemId());
                Log.d("onMenuItemClick","MenuItemgetTitle__"+item.getTitle());

                Log.d("AddMemberToPrsnlSpace","memberNumberArraylist_"+memberNumberArraylist.size());


                if (item.getTitle().equals("OK")) {


                    if (isOnline()){

                        if (memberNumberArraylist.size()>0) {
                            addMemberToSpace(appCid, userAccessToken, memberNumberArraylist);
                        }else {
                            Toast.makeText(AddMemberToSpaceActivity.this,"Please Add Members",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        AlertDialog alertDialog = new AlertDialog.Builder(AddMemberToSpaceActivity.this, R.style.MyDialogTheme).create();

                        alertDialog.setTitle("Network/Connection Error");

                        alertDialog.setMessage("Internet Connection is poor OR The Server is taking too long to respond.Please try again later.Thank you.");

                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();
                    }

                }
                return true;
            }
        });

        toolbar.inflateMenu(R.menu.add_mem_personal_space);




        mListView = (ShimmerRecyclerView)findViewById(R.id.rvContacts);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mListView.setLayoutManager(mLayoutManager);
        mListView.setItemAnimator(new DefaultItemAnimator());
        mListView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        /*updateBarHandler = new Handler();
        // Since reading contacts takes more time, let's run it on a separate thread.
        new Thread(new Runnable() {

            @Override
            public void run() {
                getContacts();

            }
        }).start();*/


        getContacts();

        Log.d("BeforeFunction", "" + contactList);

        if (memberNumberArraylist.size()>0) {

            memberNumberArraylist.clear();
        }



        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });



    }



    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getApplicationContext().checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            contactList = new ArrayList<String>();
            nameList = new ArrayList<String>();
            getContacts();

        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(AddMemberToSpaceActivity.this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getContacts() {

        contactVOList1 = new ArrayList<>();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);
        counter = 0;
        HashSet<String> hashSet = new HashSet<>();

        while (cursor.moveToNext()) {

            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            Log.d("AMTSA","gettingNames______"+name);
            String phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Log.d("AMTSA","gettingPhoneNumber______"+phonenumber);
            phonenumber = phonenumber.replaceAll(" ", "");
            ContactVO contactVO = new ContactVO();
            contactVO.setContactName(name);

            String number = null;

            if (phonenumber.startsWith("+91")){
                number=phonenumber.replace("+91","");
                contactVO.setContactNumber(number);
            }else if (phonenumber.startsWith("0")){
                number=phonenumber.substring(1);
                contactVO.setContactNumber(number);
            }else{
                contactVO.setContactNumber(phonenumber);
            }




            if (contactVO.getContactNumber() !=null && !contactVO.getContactNumber().isEmpty() && !hashSet.contains(contactVO.getContactNumber())) {


                contactVOList1.add(contactVO);
                hashSet.add(contactVO.getContactNumber());
                //Log.d("AMTSA__",contactVO.getContactNumber());

            }
        }


        cursor.close();

        if (contactVOList1 != null && !contactVOList1.isEmpty())
            Log.d("AMTSA","contactVOList1SIZE____________"+contactVOList1.size());



        adapter = new MemberFragmentDialogAdapter(contactVOList1, AddMemberToSpaceActivity.this,toolbar);
        mListView.setAdapter(adapter);




    }



    @Override
    public void setMemberData(ArrayList<String> memberData) {

        Log.d("memberDataVaL","__"+memberData);
        Log.d("member : ", " "+ memberNumberArraylist.size());

        memberNumberArraylist = memberData;

        Log.d("setMemberData","SIZE__"+memberNumberArraylist.size());


    }



    private void addMemberToSpace(final String appCid,final String userAccessToken,final ArrayList<String> mobileNo) {

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_addmembersToSpappClient;

        Log.d("AddMemberToSpace","_URL "+url);

        String tag_string_req = "req_clients";

        Map<String, List<String>> map = new HashMap<String, List<String>>();
        map.put("contactList", mobileNo);
        JSONObject jobj = new JSONObject(map);
        try {
            jobj.put("appCid",appCid);
            jobj.put("userToken",userAccessToken);
//            jobj.put("contactList", mobileNo);
        }catch (Exception e){
            Log.d("jobj","InsideCatch_"+e.getMessage());
        }

        Log.d("JOBJ_Val","__"+jobj);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,jobj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response1) {

                Log.d("AddMemberToSpace", "-->" + response1);
                //parsing Json
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(String.valueOf(response1));
                    final String success = jsonObject.getString("success");
                    final String failure = jsonObject.getString("failure");
                    JSONObject exception = jsonObject.getJSONObject("exception");
                    Log.d("successReturn", "" + success);
                    Log.d("failureReturn", "" + failure);

                    if (Integer.parseInt(failure) == 0){


                        Log.d("AddMemberToSpace","InIfPart");

                        Toast.makeText(AddMemberToSpaceActivity.this,"Member Added Successfully",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AddMemberToSpaceActivity.this, SkipClientDetailsScreen.class);
                        intent.putExtra("appCid",appCid);
                        intent.putExtra("state",state);
                        intent.putExtra("clientLogoPath",clientLogoPath);
                        intent.putExtra("clientImagePath",clientImagePath);
                        intent.putExtra("clientName",clientName);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        finish();

                    }else {
                        Log.d("AddMemberToSpace","InElsePart");
                        AlertDialog alertDialog = new AlertDialog.Builder(AddMemberToSpaceActivity.this, R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Add Member");
                        String result = "";
                        Iterator<String> itr = exception.keys();
                        while(itr.hasNext()){
                            String key = itr.next();
                            result +="\n"+ key+" : "+exception.getString(key);
                        }

                        // Setting Dialog Message
                        alertDialog.setMessage(/*exception*/result);

                        // Setting Icon to Dialog
                        //  alertDialog.setIcon(R.drawable.tick);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                if (Integer.parseInt(success)>0 && Integer.parseInt(failure)>0){
                                    Intent intent = new Intent(AddMemberToSpaceActivity.this, SkipClientDetailsScreen.class);
                                    intent.putExtra("appCid",appCid);
                                    intent.putExtra("state",state);
                                    intent.putExtra("clientLogoPath",clientLogoPath);
                                    intent.putExtra("clientImagePath",clientImagePath);
                                    intent.putExtra("clientName",clientName);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);

                                    finish();
                                }

                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage() == null || error instanceof TimeoutError || error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(AddMemberToSpaceActivity.this, R.style.MyDialogTheme).create();

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





    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v("Net Not Present", "Internet Connection Not Present");
            return false;
        }
    }


    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<ContactVO> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (ContactVO s : contactVOList1) {
            //if the existing elements contains the search input
            if (s.ContactName.toLowerCase().contains(text.toLowerCase().trim()) || s.ContactNumber.toLowerCase().contains(text.toLowerCase().trim())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdNames);
    }



}