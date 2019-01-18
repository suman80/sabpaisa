package in.sabpaisa.droid.sabpaisa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

/**
 * BroadcastReceiver to wait for SMS messages. This can be registered either
 * in the AndroidManifest or at runtime.  Should filter Intents on
 * SmsRetriever.SMS_RETRIEVED_ACTION.
 */
public class MySMSBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch(status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Get SMS message contents
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    // Extract one-time code from the message and complete verification
                    // by sending the code back to your server.
                    Log.d("MyBrdCst","__"+message);


                    String otp = message.replace("<#>Please Use this OTP to verify your Mobile on SabPaisa App: ","");
                    Log.d("MSBR","otp "+otp);

                    String otp1 = otp.trim().replace(otp.trim().substring(otp.length() - 12),"").trim();

                    Log.d("MSBR1","otp1 "+otp1);


                    Intent sendOtp = new Intent(ConstantsForUIUpdates.SEND_OTP);
                    sendOtp.putExtra("OTP", otp1);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(sendOtp);


                    break;
                case CommonStatusCodes.TIMEOUT:
                    // Waiting for SMS timed out (5 minutes)
                    // Handle the error ...


                    Intent otpNotRecieved = new Intent(ConstantsForUIUpdates.OTP_NOT_RECIEVED);
                    otpNotRecieved.putExtra("OTP", otpNotRecieved);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(otpNotRecieved);


                    break;
            }
        }
    }
}