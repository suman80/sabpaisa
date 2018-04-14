package in.sabpaisa.droid.sabpaisa;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Set;

import in.sabpaisa.droid.sabpaisa.Adapter.AllContactsAdapter;
import in.sabpaisa.droid.sabpaisa.Model.ContactVO;
import in.sabpaisa.droid.sabpaisa.Util.CommonUtils;


public class AllContacts extends AppCompatActivity {
    private ListView mListView;
    private ProgressDialog pDialog;
    private android.os.Handler updateBarHandler;

    ArrayList<String> contactList;
    Cursor cursor;
    String p1,p2,p3;
    Toolbar toolbar;
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


        pDialog.setCancelable(false);
      pDialog.show();

        mListView = (ListView) findViewById(R.id.rvContacts);
        updateBarHandler = new Handler() ;



        // Since reading contacts takes more time, let's run it on a separate thread.
        new Thread(new Runnable() {

            @Override
            public void run() {
                getContacts();
            }
        }).start();

        // Set onclicklistener to the list item.
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id){
                //TODO Do whatever you want with the list data
                Toast.makeText(getApplicationContext(), "item clicked : \n" + contactList.get(position), Toast.LENGTH_SHORT).show();

            }
        });
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
            getContacts();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, contactList);
            mListView.setAdapter(adapter);
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
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public void getContacts() {


        contactList = new ArrayList<String>();

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

                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {

                    // output.append("\n First Name:" + name);

                    //This is to read multiple phone numbers associated with the same contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (phoneCursor.moveToNext()) {
                        // phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        //output.append("\n Phone number:" + phoneNumber);
                        //contactList.add(phoneNumber);

                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.d("Actualphonenumber", "" + phoneNumber);
                        p2 = phoneNumber.replace(" ", "");
                        p1 = p2.replace("+91", "");

                        int n = contactList.size();

                        Set<String> s = new LinkedHashSet<String>(contactList);


                        n = removeDuplicates(contactList, n);

                        for (int i = 0; i < n; i++) {
                            System.out.print(contactList.get(i) + " Separator");


                            Log.d("CLashasdh", "" + contactList.get(i));
                        }

                        Log.d("Replace+91", "" + s);
                        Log.d("Replace+91", "" + p1);

                        contactList.add(p1);
                        //Converting ArrayList to HashSet to remove duplicates
                        HashSet<String> listToSet = new HashSet<String>(contactList);
//Creating Arraylist without duplicate values
                        List<String> listWithoutDuplicates = new ArrayList<String>(listToSet);
                        Log.d("szlistwithoutduplicates", "" + listToSet.size()); //should print 3 becaues of duplicates Android removed
                        Log.d("szlistwithoutduplicates", "" + listWithoutDuplicates); //should print 3 becaues of duplicates Android removed


////////////////////////////////////////////////////////////////////
                        for (int i = 0; i < contactList.size(); i++) {

                            for (int j = i + 1; j < contactList.size(); j++) {
                                if (contactList.get(i).equals(contactList.get(j))) {
                                    contactList.remove(j);
                                    j--;

                                }
                            }

                        }

                        System.out.println("After Removing duplicate elements:" + contactList);

//////////                                 ////////////////////////////////

                        if (phoneNumber.length() == 10) {
                            p2 = p1;
                            p3 = p2.replace("+91", "");
                            Log.d("Length 10", "" + p2);
                            Log.d("Length 101", "" + p3);

                        }
                             phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                            //output.append("\n Phone number:" + phoneNumber);


                    }


                    phoneCursor.close();

                    // Read every email id associated with the contact
                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (emailCursor.moveToNext()) {

                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));

                        output.append("\n Email:" + email);

                    }

                    emailCursor.close();
                }

                Log.d("beforeadding", "" + p3);
                Set<String> s = new LinkedHashSet<>(contactList);

                Log.d("beforeadding", "" + s);
                // Add the contact to the ArrayList
               // contactList.add(output.toString());
            }

            // ListView has to be updated using a ui thread
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.single_contact_view, R.id.tvContactName, contactList);
                    Log.d("Contactlistaar", "" + contactList);

                    mListView.setAdapter(adapter);
                }
            });

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
}
