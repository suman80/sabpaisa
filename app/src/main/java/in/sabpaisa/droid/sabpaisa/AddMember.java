package in.sabpaisa.droid.sabpaisa;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class AddMember extends AppCompatActivity {

    static final int RESULT_PICK_CONTACT = 2;
    ImageView contactChooser;
    EditText memberNameEditText,memberNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        contactChooser = (ImageView)findViewById(R.id.contactChooser);
        memberNameEditText = (EditText)findViewById(R.id.memberNameEditText);
        memberNumberEditText = (EditText)findViewById(R.id.memberNumberEditText);

        contactChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);

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
                    contactPicked(data, memberNameEditText, memberNumberEditText);
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
    private void contactPicked(Intent data, EditText cName, EditText cNumber) {
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
//            contactEditText.setText(name);
//            contactNumberEditText.setText(phoneNo);
            cName.setText(name);
            cNumber.setText(phoneNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
