package in.sabpaisa.droid.sabpaisa.Adapter;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import in.sabpaisa.droid.sabpaisa.AllContacts;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.MainActivity;
import in.sabpaisa.droid.sabpaisa.Model.ContactVO;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.SortArrayListAscendingDescending;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;

/**
 * Created by archana on 22/3/18.
 */

public class AllContactsAdapter  extends RecyclerView.Adapter<AllContactsAdapter.ContactViewHolder>{
    String key;
    private ArrayList<ContactVO> contactVOList;
    private ArrayList<String> nameList;
    private Context mContext;
    ArrayList<String> sortedArrayListAscending;
    int counter;
    Set<String> p;
    Cursor cursor;
    static Button sp;
    private Handler  updateBarHandler = new Handler() ;;

    String p1,p2,p3,p4,p5,name;

    public AllContactsAdapter(ArrayList<ContactVO> contactVOList, Context mContext){
        this.contactVOList = contactVOList;
        this.mContext = mContext;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.contactsrecylcerview, null);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        ContactVO contactVO = contactVOList.get(position);


        //getContacts();
       // for (int i = 0; i < nameList.size(); i++) {
            holder.tvContactName.setText(contactVO.getContactName());
        //}
        holder.tvPhoneNumber.setText(contactVO.getContactNumber());
        Log.d("contactVO",String.valueOf(contactVO.getInviteButtonVisibility()));
        Log.d("contactVOhuihu",String.valueOf(contactVO.getContactName()));
        if(contactVO.getInviteButtonVisibility()==0)
        {
            holder.invite.setVisibility(View.VISIBLE);
            holder.invite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("AllContactsAdapter","PressedOutside");
                    //Toast.makeText(mContext,"AllContactsAdapter--->Pressed",Toast.LENGTH_SHORT).show();
                    try {
                        Log.d("AllContactsAdapter","PressedInside");
                        /*Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, "SPApp");
                        String sAux = "\n Let me recommend you this application .\n this is the easy way to pay your fee\n It is very cool app try it once ,download it from the below link given... \n \n";
                        sAux = sAux+"\n"+"https://play.google.com/store/apps/details?id=in.sabpaisa.droid.sabpaisa";
                        i.putExtra(Intent.EXTRA_TEXT, sAux);

                        mContext.startActivity(Intent.createChooser(i, "Share via"));*/


                        shareIntentSpecificApps();

                    } catch (Exception e) {
                        //e.toString();
                    }
                }
            });
        }
        else
        {
            holder.invite.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return contactVOList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder{

        ImageView ivContactImage;
        TextView tvContactName;
        TextView tvPhoneNumber;
        Button invite;

        public ContactViewHolder(View itemView) {
            super(itemView);
            //ivContactImage = (ImageView) itemView.findViewById(R.id.ivContactImage);
            tvContactName = (TextView) itemView.findViewById(R.id.name);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.number);

            invite=(Button) itemView.findViewById(R.id.buttonInvite);
            // sp=(Button) itemView.findViewById(R.id.abc);



        }
    }





    public void getContacts() {

        //contactList = new ArrayList<String>();
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

        ContentResolver contentResolver = mContext.getContentResolver();

        cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

        // Iterate every contact in the phone
        if (cursor.getCount() > 0) {

            counter = 0;
            while (cursor.moveToNext()) {
                output = new StringBuffer();
                // Update the progress message
                updateBarHandler.post(new Runnable() {
                    public void run() {
                      //  pDialog.setMessage("Reading contacts : " + counter++ + "/" + cursor.getCount());
                    }
                });

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                if (hasPhoneNumber > 0) {


                    //  ContactVO contactVO=new ContactVO();
                    // output.append("\n First Name:" + name)

                    //This is to read multiple phone numbers associated with the same contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (phoneCursor.moveToNext()) {

                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.d("Actualphonenumber", "" + phoneNumber);
                        p2 = phoneNumber.replace(" ", "");
                        p1 = p2.replace("+91", "");
                        p3=p1.replace("(","");
                        p4=p3.replace(")","");
                        p5=p4.replace("-","");

                        //int n = contactList.size();
                        int m=nameList.size();

                       // Set<String> s = new LinkedHashSet<String>(contactList);

                        p = new LinkedHashSet<String>(nameList);
                        m = removeDuplicates(nameList, m);
                        for (int i = 0; i < m; i++) {
                            System.out.print(nameList.get(i) + " Separator");
                            Log.d("CLashasdh", "" + nameList.get(i));
                            // nameList.add(nameList.get(i));
                            Log.d("ReplacinsNumber", "" + nameList);
//                            contactVO.setContactName(name);
                        }


                        Log.d("Replace+91Number", "" + nameList);
                        Log.d("Replace+91NAmeadapter", "" + p);

                        SortArrayListAscendingDescending sortArrayList = new SortArrayListAscendingDescending(nameList);
                        ArrayList<String> unsortedArrayList = sortArrayList.getArrayList();
                        System.out.println("Unsorted ArrayList: " + unsortedArrayList);
                        sortedArrayListAscending = sortArrayList.sortAscending();
                        System.out.println("Sorted ArrayList in Ascending Order : " + sortedArrayListAscending);








                        HashSet<String> listToSet = new HashSet<String>(nameList);
//Creating Arraylist without duplicate commentborder
                        List<String> listWithoutDuplicates = new ArrayList<String>(listToSet);
                        Log.d("szlistwithoutduplicate1", "" + listToSet.size()); //should print 3 becaues of duplicates Android removed
                        Log.d("szlistwithoutduplicate1", "" + listWithoutDuplicates); //should print 3 becaues of duplicates Android removed

                       /* ArrayList<String> sortedArrayListDescending = sortArrayList.sortDescending();
                        System.out.println("Sorted ArrayList in Descending Order: " + sortedArrayListDescending);
*/
                       /* for (int i = 0; i < m; i++) {
                            System.out.print(nameList.get(i) + " Separator");
                            Log.d("CLashasdh", "" + nameList.get(i));
                        }*/
                       // Log.d("Replace+91", "" + s);
                      /*  ContactVO contactVO=new ContactVO();
                        contactVO.setContactName(p.toString());*/

                        Log.d("Replace+91", "" + p1);

                        //contactList.add(p5);
                        nameList.add(name);
                        Log.d("nameList12342",String.valueOf(nameList));
                        //Converting ArrayList to HashSet to remove duplicates
                       // HashSet<String> listToSet = new HashSet<String>(contactList);
//Creating Arraylist without duplicate commentborder
                       // List<String> listWithoutDuplicates = new ArrayList<String>(listToSet);
                       // Log.d("szlistwithoutduplicates", "" + listToSet.size()); //should print 3 becaues of duplicates Android removed
                        //Log.d("szlistwithoutduplicates", "" + listWithoutDuplicates); //should print 3 becaues of duplicates Android removed

                        for (int i = 0; i < nameList.size(); i++) {

                            for (int j = i + 1; j < nameList.size(); j++) {
                                if (nameList.get(i).equals(nameList.get(j))) {
                                    nameList.remove(j);
                                    j--;
                                }
                            }

                        }

                        System.out.println("Archanaaaa" + nameList);

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
                //Set<String> s= new LinkedHashSet<>(contactList);
            }
            //ContactsApi(contactList);
            // Dismiss the progressbar after 500 millisecondds
            updateBarHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                   // pDialog.cancel();
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




    public void shareIntentSpecificApps() {


        List<Intent> targetShareIntents = new ArrayList<Intent>();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        PackageManager pm = mContext.getPackageManager();
        List<ResolveInfo> resInfos = pm.queryIntentActivities(shareIntent, 0);
        if (!resInfos.isEmpty()) {
            System.out.println("Have package");
            for (ResolveInfo resInfo : resInfos) {
                String packageName = resInfo.activityInfo.packageName;
                Log.i("Package Name", packageName);

                if (packageName.contains("com.twitter.android") || packageName.contains("com.facebook.katana")
                        || packageName.contains("com.whatsapp") || packageName.contains("com.google.android.apps.plus")
                        || packageName.contains("com.google.android.talk") || packageName.contains("com.slack")
                        || packageName.contains("com.google.android.gm") || packageName.contains("com.facebook.orca")
                        || packageName.contains("com.yahoo.mobile") || packageName.contains("com.skype.raider")
                        || packageName.contains("com.android.mms")|| packageName.contains("com.linkedin.android")
                        || packageName.contains("com.google.android.apps.messaging")) {
                    Intent intent = new Intent();

                    String sAux = "\n Let me recommend you this application .\n this is the easy way to pay your fee\n It is very cool app try it once ,download it from the below link given... \n \n";
                    sAux = sAux + "\n" + "https://play.google.com/store/apps/details?id=in.sabpaisa.droid.sabpaisa";

                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                    intent.putExtra("AppName", resInfo.loadLabel(pm).toString());
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, sAux);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "SPApp");
                    intent.setPackage(packageName);
                    targetShareIntents.add(intent);
                }
            }
            if (!targetShareIntents.isEmpty()) {
                Collections.sort(targetShareIntents, new Comparator<Intent>() {
                    @Override
                    public int compare(Intent o1, Intent o2) {
                        return o1.getStringExtra("AppName").compareTo(o2.getStringExtra("AppName"));
                    }
                });
                Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0), "Select app to share");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
                mContext.startActivity(chooserIntent);
            } else {
                Toast.makeText(mContext, "No app to share.", Toast.LENGTH_LONG).show();
            }
        }


    }






}
