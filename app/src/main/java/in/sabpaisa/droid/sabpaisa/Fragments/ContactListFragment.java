package in.sabpaisa.droid.sabpaisa.Fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.ContactsAdapter;
import in.sabpaisa.droid.sabpaisa.Adapter.SavedUPIAdapter;
import in.sabpaisa.droid.sabpaisa.Interfaces.OnFragmentInteractionListener;
import in.sabpaisa.droid.sabpaisa.Model.ContactList;
import in.sabpaisa.droid.sabpaisa.Model.SavedUPI;
import in.sabpaisa.droid.sabpaisa.R;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by abc on 16-06-2017.
 */
@RuntimePermissions
public class ContactListFragment extends Fragment {
    View rootView;
    RecyclerView recyclerViewInstitutions;
    ContactsAdapter adapter;
    ArrayList<ContactList> contactLists;
    Cursor cursor;
    int counter;
    private Handler updateBarHandler=new Handler();
    ProgressBar progressBar;

    GetDataInterface sGetDataInterface;

    public interface GetDataInterface {
        ArrayList<ContactList> getFeedDataList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            sGetDataInterface= (GetDataInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "must implement GetDataInterface Interface");
        }
    }

    public void getDataFromActivity() {
        if(sGetDataInterface != null){
            this.contactLists = sGetDataInterface.getFeedDataList();
            adapter.setItems(this.contactLists);
            adapter.notifyDataSetChanged();
        }
    }
    public ContactListFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_contact_list, container, false);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
        recyclerViewInstitutions = (RecyclerView) rootView.findViewById(R.id.recycler_view_contact_list);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),10));
        recyclerViewInstitutions.setLayoutManager(llm);
        contactLists = new ArrayList<>();

        new ReadContacts().execute();

        return rootView;
    }

    private class ReadContacts extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            contactLists.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ContactListFragmentPermissionsDispatcher.loadArrayListWithCheck(ContactListFragment.this);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            adapter = new ContactsAdapter(getContext(),contactLists);
            recyclerViewInstitutions.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @NeedsPermission(Manifest.permission.READ_CONTACTS)
    public void loadArrayList() {
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        String _ID = ContactsContract.Contacts._ID;
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        ContentResolver contentResolver = getActivity().getContentResolver();
        cursor = contentResolver.query(CONTENT_URI, null,null, null,"upper("+ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");
        // Iterate every contact in the phone
        if (cursor.getCount() > 0) {
            counter = 0;
            while (cursor.moveToNext()) {
                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
                String phoneNumber=null;
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));
                if (hasPhoneNumber>0) {
                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contact_id, null, null);
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                    }
                    phoneCursor.close();
                    if (phoneNumber!=null) {
                        contactLists.add(new ContactList(name, phoneNumber));
                    }
                }
            }
        }

    }
}
