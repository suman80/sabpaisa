package in.sabpaisa.droid.sabpaisa.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = SmsReceiver.class.getSimpleName();
    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle data = intent.getExtras();
        try {
            if (data != null) {
                Log.i("Sms Receive", "Call SMS Recive");
                Object[] pdus = (Object[]) data.get("pdus");

                // for (int i = 0; i < pdus.length; i++) {
                for (Object aPdusObj : pdus) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);

                    String sender = smsMessage.getDisplayOriginatingAddress();
                    //You must check here if the sender is your provider and not another one with same text.

                    String messageBody = smsMessage.getMessageBody();

                    //Pass on the text to our listener.
                    mListener.messageReceived(messageBody);
                }

            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}