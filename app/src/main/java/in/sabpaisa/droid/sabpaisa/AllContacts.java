package in.sabpaisa.droid.sabpaisa;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;


import android.Manifest;
import android.support.v7.widget.Toolbar;

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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

import in.sabpaisa.droid.sabpaisa.Adapter.AllContactsAdapter;
import in.sabpaisa.droid.sabpaisa.Model.ContactVO;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;


public class AllContacts extends AppCompatActivity {
    private ShimmerRecyclerView mListView;
    private ProgressDialog pDialog;
    private android.os.Handler updateBarHandler;
    Button invitet1;
    ArrayList<String> contactList,nameList;
    Cursor cursor;
    String p1,p2,p3,p4,p5;
    Toolbar toolbar;
    String contact;
    JSONObject object1;
    String key;
    Button invite;
    ContactVO contactVO;
    String name;
    int counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_contacts);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        pDialog = new ProgressDialog(AllContacts.this);
        pDialog.setMessage("Reading contacts...");

        //invite=(Button) findViewById(R.id.abc);
        pDialog.setCancelable(false);
        pDialog.show();
        mListView = (ShimmerRecyclerView) findViewById(R.id.rvContacts);

        updateBarHandler = new Handler() ;
        // Since reading contacts takes more time, let's run it on a separate thread.
        new Thread(new Runnable() {

            @Override
            public void run() {
                getContacts();


            }
        }).start();
        Log.d("BeforeFunction",""+contactList);
    }

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
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
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void getContacts() {

        contactList = new ArrayList<String>();
        nameList = new ArrayList<String>();
        String phoneNumber = null;
        String email = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        StringBuffer output;

        ContentResolver contentResolver = getContentResolver();

        cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

        // Iterate every contact in the phone
        if (cursor.getCount() > 0) {

            counter = 0;
            while (cursor.moveToNext()) {
                output = new StringBuffer();
                // Update the progress message
                updateBarHandler.post(new Runnable() {
                    public void run() {
                        pDialog.setMessage("Reading contacts : " + counter++ + "/" + cursor.getCount());
                    }
                });

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
               // name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                if (hasPhoneNumber > 0) {


                    //  ContactVO contactVO=new ContactVO();
                     output.append("\n First Name:" + name);

                    //This is to read multiple phone numbers associated with the same contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (phoneCursor.moveToNext()) {

                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        name = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        Log.d("Actualphonenumber", "" + phoneNumber);
                        Log.d("Actualname", "" + name);
                        p2 = phoneNumber.replace(" ", "");
                        p1 = p2.replace("+91", "");
                        p3=p1.replace("(","");
                        p4=p3.replace(")","");
                        p5=p4.replace("-","");

                        int n = contactList.size();
                        int m=nameList.size();

                        Set<String> s = new LinkedHashSet<String>(contactList);
                        Set<String> p = new LinkedHashSet<String>(nameList);
                        n = removeDuplicates(contactList, n);
                        for (int i = 0; i < m; i++) {
                            System.out.print(nameList.get(i) + " Separator");
                            Log.d("CLashasdh", "" + nameList.get(i));
                            // nameList.add(nameList.get(i));
                            Log.d("ReplacinsNumber", "" + nameList);
//                            contactVO.setContactName(name);
                        }

                        Log.d("Replace+91Number", "" + nameList);
                        Log.d("Replace+91NAme", "" + p);
                        for (int i = 0; i < n; i++) {
                            System.out.print(contactList.get(i) + " Separator");
                            Log.d("CLashasdh", "" + contactList.get(i));
                        }
                        Log.d("Replace+91", "" + s);
                        Log.d("Replace+91", "" + p1);

                        contactList.add(p5);
                        // nameList.add(name.toString());
                        Log.d("nameList",String.valueOf(nameList));
                        //Converting ArrayList to HashSet to remove duplicates
                        HashSet<String> listToSet = new HashSet<String>(contactList);
//Creating Arraylist without duplicate commentborder
                        List<String> listWithoutDuplicates = new ArrayList<String>(listToSet);
                        Log.d("szlistwithoutduplicates", "" + listToSet.size()); //should print 3 becaues of duplicates Android removed
                        Log.d("szlistwithoutduplicates", "" + listWithoutDuplicates); //should print 3 becaues of duplicates Android removed

                        for (int i = 0; i < contactList.size(); i++) {

                            for (int j = i + 1; j < contactList.size(); j++) {
                                if (contactList.get(i).equals(contactList.get(j))) {
                                    contactList.remove(j);
                                    j--;
                                }
                            }

                        }

                        System.out.println("After Removing duplicate elements:" + contactList);
/*HashSet<String> listToSet1 = new HashSet<String>(nameList);
//Creating Arraylist without duplicate commentborder
                        List<String> listWithoutDuplicates1 = new ArrayList<String>(listToSet1);
                        Log.d("szlistwithoutduplicates", "" + listToSet.size()); //should print 3 becaues of duplicates Android removed
                        Log.d("szlistwithoutduplicates", "" + listWithoutDuplicates); //should print 3 becaues of duplicates Android removed

                        for (int i = 0; i < nameList.size(); i++) {

                            for (int j = i + 1; j < nameList.size(); j++) {
                                if (nameList.get(i).equals(nameList.get(j))) {
                                    nameList.remove(j);
                                    j--;
                                }
                            }

                        }*/

                        System.out.println("After Remarcahana" + nameList);

                        if (phoneNumber.length() == 10) {
                            p2 = p1;
                            p3 = p2.replace("+91", "");
                            Log.d("Length 10", "" + p2);
                            Log.d("Length 101", "" + p3);

                        }
                    }
                    phoneCursor.close();

                    // Read every email id associated with the contact
                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (emailCursor.moveToNext()) {

                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));

                        // output.append("\n Email:" + email);

                    }

                    emailCursor.close();
                }
                Set<String> s= new LinkedHashSet<>(contactList);
            }
            ContactsApi(contactList);
            // Dismiss the progressbar after 500 millisecondds
            updateBarHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    pDialog.cancel();
                }
            }, 500);
        }
    }

    static int removeDuplicates(List<String> arr, int n) {
        if (n == 0 || n == 1)
            return n;
        // To store index of next unique element
        int j = 0;
        // Doing same as done in Method 1
        // Just maintaining another updated index i.e. j
        for (int i = 0; i < n - 1; i++)
            if (arr.get(i) != arr.get(i + 1))
                arr.set(j++, arr.get(i));
                arr.set(j++, arr.get(n - 1));
        return j;
    }
    private void ContactsApi(final ArrayList<String> contactList ) {
        // Tag used to cancel the request
        String tag_string_req = "req_contacts";
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        map.put("contactList", contactList);
        JSONObject jObj1 = new JSONObject(map);
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                AppConfig.Base_Url+AppConfig.App_api+ AppConfig.URL_Contacts,jObj1, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("Contactsresponse ", " " + response.toString());
                try {

                    ArrayList<ContactVO> contactVOList;
                    AllContactsAdapter allContactsAdapter;
                    contactVOList=new ArrayList<ContactVO>();
                    JSONObject jObj = new JSONObject(String.valueOf(response));
                    String status = jObj.getString("status");
                    String response1 = jObj.getString("response");
                    Log.d("STATUS ", " " + status);
                    Log.d("STATUS1234567890", " " + response1);
                    JsonArray jsonObject1 = new JsonObject().getAsJsonArray("response1");
                    Log.d("tvji1145", " " + jsonObject1);
                    object1 = jObj.getJSONObject("response");
                    Log.d("tvji1145", " " + object1);
                    Iterator<String> iterator = object1.keys();
                    Log.d("tvji11452", " " + iterator);
                    int count=0;
                    while (iterator.hasNext()) {
                        key = iterator.next();
                        Log.d("UserContactList", "==>" + key);
                        Log.d("numbersREGorNot", "==>" + object1.optString(key));
                        contactVO=new ContactVO();

                       // contactVO.setContactName(name);
                        if (object1.optString(key).equals("User_Not_Registered")) {
                            Log.d("numbersREGorNotqyeuqye", "==>" + key);
                            Log.d("numbersREGorNotq", "==>" + object1.optString(key));
                            contactVO.setInviteButtonVisibility(0);
                           /* for(int i=0;i<nameList.size();i++) {
                          contactVO.setContactName(nameList.get(i).toString());
                            }*/
                        }
                        else
                        {
                            contactVO.setInviteButtonVisibility(1);
                            }


                        for (int i=0; i<count; count++) {
                            contactVO.setContactName(nameList.get(count).toString());
                      }
                        contactVO.setContactNumber(key.toString());
                        contactVOList.add(contactVO);
                        count++;
                        Log.d("contactVOList",""+contactVOList);
                    }
                    allContactsAdapter = new AllContactsAdapter(contactVOList,getApplicationContext());
                    mListView.setAdapter(allContactsAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getMessage()==null || error.getMessage()==null || error.getMessage()==null ||error instanceof TimeoutError||error instanceof NoConnectionError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(AllContacts.this, R.style.MyDialogTheme).create();
                    // Setting Dialog Title
                    alertDialog.setTitle("Network/Connection Error");
                    // Setting Dialog Message
                    alertDialog.setMessage("Internet Connection is poor OR The Server is taking too long to respond.Please try again later.Thank you.");

                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.appicon);
                    // Setting OK Button
                    alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed
                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();
                    Log.e("", "Contacts api Error: " + error.getMessage());
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
        }) {
            protected Map<String, String> getParams() {
                ArrayList<String> contactList = new ArrayList<String>();
                Map<String, String> params = new HashMap<String, String>();
                int i=0;
                for(String object: contactList){
                    params.put("contactList["+(i++)+"]", object);
                    // first send both data with same param name as contactList[] ....
                    // now send with params contactList[0],contactList[1] ..and so on
                }
                params.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                return params;
             }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}