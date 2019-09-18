package in.sabpaisa.droid.sabpaisa;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class Button_Listerner extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
notificationManager.cancel(intent.getExtras().getInt("id"));
        /*Toast.makeText(context, "Text clicked", Toast.LENGTH_SHORT).show();

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            //  notificationManager.cancel(intent.getExtras().getInt("id"));

            Intent Intent = new Intent(context, FullViewOfClientsProceed.class);
            PendingIntent PenginIntent = PendingIntent.getBroadcast(context, 0, Intent, 0);
            NotificationCompat.Builder  notificationBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle("Notification Demo").setSmallIcon(R.drawable.ic_launcher).setContentIntent(PenginIntent)
                    .setContentText("You have got notification.");
             notificationManager.notify(1, notificationBuilder.build());

        }*/


    }}
