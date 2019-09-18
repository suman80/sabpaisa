package in.sabpaisa.droid.sabpaisa.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import in.sabpaisa.droid.sabpaisa.AddMemberToSpaceActivity;
import in.sabpaisa.droid.sabpaisa.AppController;
import in.sabpaisa.droid.sabpaisa.LogInActivity;
import in.sabpaisa.droid.sabpaisa.R;
import in.sabpaisa.droid.sabpaisa.Util.AppConfig;
import in.sabpaisa.droid.sabpaisa.Util.SkipClientDetailsScreen;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddMemberToSpaceDialogFragment1 extends DialogFragment {

    EditText nameEditText,numberEditText;
    ImageView contactChooserImg;
    Toolbar toolbar;
    String userAccessToken;
    ArrayList<String> memberNumberArraylist;
    String appCid;
    static final int RESULT_PICK_CONTACT = 2;

    //Values get from Skip Members Fragment
    String clientName,clientLogoPath,clientImagePath,state;

    TextView chooseMultipleContactTextView;

    public AddMemberToSpaceDialogFragment1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appCid = getArguments().getString("appCid");
        clientName = getArguments().getString("clientName");
        clientLogoPath = getArguments().getString("clientLogoPath");
        clientImagePath = getArguments().getString("clientImagePath");
        state = getArguments().getString("state");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_member_to_space_dialog_fragment1, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);

        userAccessToken = sharedPreferences.getString("response", "123");


        nameEditText = (EditText)view.findViewById(R.id.nameEditText);
        numberEditText = (EditText)view.findViewById(R.id.numberEditText);
        contactChooserImg = (ImageView)view.findViewById(R.id.contactChooserImg);
        chooseMultipleContactTextView = (TextView)view.findViewById(R.id.chooseMultipleContactTextView);
        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_previousback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("AddMemberDialogFragment");
                if (prev != null) {
                    DialogFragment df = (DialogFragment) prev;
                    df.dismiss();
                }
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Log.d("onMenuItemClick","MenuItemgetItemId__"+item.getItemId());
                Log.d("onMenuItemClick","MenuItemgetTitle__"+item.getTitle());



                if (item.getTitle().equals("OK")) {

                    memberNumberArraylist = new ArrayList<>();
                    String name = nameEditText.getText().toString();
                    String number = numberEditText.getText().toString();

                    if (name.isEmpty()){
                        nameEditText.setError("Please enter the name !");
                    }else if (number.length() == 0 || number.length()<10){
                        numberEditText.setError("Please enter your number correctly");
                    }else if (!isOnline()){
                        AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme).create();

                        alertDialog.setTitle("Network/Connection Error");

                        alertDialog.setMessage("Internet Connection is poor OR The Server is taking too long to respond.Please try again later.Thank you.");

                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();
                    }else {

                        memberNumberArraylist.add(number.trim());

                        addMemberToSpace(appCid, userAccessToken, memberNumberArraylist);
                    }
                }
                return true;
            }
        });

        toolbar.inflateMenu(R.menu.add_mem_personal_space);

        contactChooserImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
            }
        });


        chooseMultipleContactTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddMemberToSpaceActivity.class);
                intent.putExtra("appCid",appCid);
                intent.putExtra("clientName",clientName);
                intent.putExtra("clientLogoPath",clientLogoPath);
                intent.putExtra("clientImagePath",clientImagePath);
                intent.putExtra("state",state);
                startActivity(intent);
            }
        });



        return view;
    }




    private void contactPicked(Intent data, EditText cName, EditText cNumber) {
        Cursor cursor = null;
        try {
            String phoneNo = null;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContext().getContentResolver().query(uri, null, null, null, null);
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
//            contactEditText.setText(name);
//            contactNumberEditText.setText(phoneNo);
            cName.setText(name);


            if (phoneNo.startsWith("+91")){
                String number = phoneNo.replace("+91","");
                cNumber.setText(number);
            }else if (phoneNo.startsWith("0")){
                String number = phoneNo.substring(1);
                cNumber.setText(number);
            }else {
                cNumber.setText(phoneNo);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data, nameEditText, numberEditText);
                    break;

            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }




        private void addMemberToSpace(final String appCid,final String userAccessToken,final ArrayList<String> mobileNo) {

        String url = AppConfig.Base_Url + AppConfig.App_api + AppConfig.URL_addmembersToSpappClient;

        Log.d("AddMemberToSpace1","_URL "+url);

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

                Log.d("AddMemberToSpace1", "-->" + response1);
                //parsing Json
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(String.valueOf(response1));
                    final String success = jsonObject.getString("success");
                    final String failure = jsonObject.getString("failure");
                    String exception = jsonObject.getString("exception");
                    Log.d("successReturn", "" + success);
                    Log.d("failureReturn", "" + failure);

                    if (Integer.parseInt(failure) == 0){


                        Log.d("AddMemberToSpace1","InIfPart");

                        //Toast.makeText(getContext(),"Member Added Successfully",Toast.LENGTH_SHORT).show();

                        AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Member Added");

                        // Setting Dialog Message
                        alertDialog.setMessage("Registered members added successfully. Also an invitation has been sent to non-regestered members.");

                        // Setting Icon to Dialog
                        //  alertDialog.setIcon(R.drawable.tick);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(getContext(), SkipClientDetailsScreen.class);
                                intent.putExtra("appCid",appCid);
                                intent.putExtra("state",state);
                                intent.putExtra("clientLogoPath",clientLogoPath);
                                intent.putExtra("clientImagePath",clientImagePath);
                                intent.putExtra("clientName",clientName);
                                intent.putExtra("FRAGMENT_ID","2");
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();





                    }else {
                        Log.d("AddMemberToSpace1","InElsePart");

                        AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Add Member");
                        Object value = null;

                        try {
                            JSONObject object = new JSONObject(exception);

                            Iterator<String> iter = object.keys();
                            while (iter.hasNext()) {
                                String key = iter.next();
                                try {
                                    value = object.get(key);
                                } catch (JSONException e) {
                                    // Something went wrong!
                                }
                            }

                        }catch (Exception e){

                        }



                        // Setting Dialog Message
                        alertDialog.setMessage(value.toString());

                        // Setting Icon to Dialog
                        //  alertDialog.setIcon(R.drawable.tick);

                        // Setting OK Button
                        alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                if (Integer.parseInt(success)>0 && Integer.parseInt(failure)>0){
                                    Intent intent = new Intent(getContext(), SkipClientDetailsScreen.class);
                                    intent.putExtra("appCid",appCid);
                                    intent.putExtra("state",state);
                                    intent.putExtra("clientLogoPath",clientLogoPath);
                                    intent.putExtra("clientImagePath",clientImagePath);
                                    intent.putExtra("clientName",clientName);
                                    intent.putExtra("FRAGMENT_ID","2");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
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
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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


}
