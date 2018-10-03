package in.sabpaisa.droid.sabpaisa;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

/**
 * Created by rajdeep on 16/9/18.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessagingService";
    String groupForNotification = "SpAppNotification";
    int notifyID = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        Map<String, String> dataMap = remoteMessage.getData();

        String body = dataMap.get("body");
        String title = dataMap.get("title");
        String userName = dataMap.get("userName");
        String userToken = dataMap.get("userToken");
        String feedId = dataMap.get("feedId");
        String groupId = dataMap.get("groupId");

        Log.d("BackGroundNoti : ", body + " " + body.trim().length() + " " + title + " " + feedId + " " + groupId + " " + userName);
        Log.d("NotificationBody : ", StringEscapeUtils.unescapeJava(body));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationForOreoAndAbove(title, body, feedId, groupId, userName);
        } else {
            createNotification(title, body, feedId, groupId, userName);
        }


    }

    private void createNotificationForOreoAndAbove(final String title, final String body, final String feedId, final String groupId, final String userName) {

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);
        String response = sharedPreferences.getString("response", "123");
        String userAccessToken = response;

        PendingIntent pendingIntent = null;

        if (feedId != null) {

            Intent intentMain = new Intent(this, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(intentMain);

            Intent intent = new Intent(this, Proceed_Feed_FullScreen.class);
            intent.putExtra("feedId", feedId);
            intent.putExtra("userAccessTokenFromNotification", userAccessToken);
            intent.putExtra("feedName", title); //Feed Name for toolbar

            stackBuilder.addNextIntent(intent);

            pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        } else if (groupId != null) {


            Intent intentMain = new Intent(this, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(intentMain);

            Intent intent = new Intent(this, Proceed_Group_FullScreen.class);
            intent.putExtra("groupId", groupId);
            intent.putExtra("userAccessTokenFromNotification", userAccessToken);
            intent.putExtra("groupName", title);
            stackBuilder.addNextIntent(intent);
            pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        } else {
            Log.d("createNtForOreoAndAbove", "In Else Part");
        }

        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = "in.sabpaisa.droid.sabpaisa.SPNotification";// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
// Create a notification and set the notification channel.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.appicon);

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            Notification notification =
                    new NotificationCompat.Builder(this)
                            .setContentTitle(title)
                            .setContentText(Html.fromHtml("<b>" + userName + " :" + "</b>" + StringEscapeUtils.unescapeJava(body)))
                            .setAutoCancel(true)
                            .setSmallIcon(R.drawable.sabpaisa1234)
                            .setLargeIcon(largeIcon)
                            .setContentIntent(pendingIntent)
                            .setVibrate(new long[]{1000, 1000/*, 1000, 1000, 1000*/})
                            .setLights(Color.RED, 3000, 3000)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            //.setGroup(groupForNotification)
                            //.setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml("<b>"+userName+" :"+"</b>"+body)))
                            .setChannelId(CHANNEL_ID).build();

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(mChannel);

// Issue the notification.
            Log.d("notifyID", " " + notifyID);
            mNotificationManager.notify(++notifyID, notification);

        }
    }

    private void createNotification(final String title, final String body, final String feedId, final String groupId, final String userName) {

        Log.d("In_createNotification", "Title: " + title + " & \n Body :" + body);

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(LogInActivity.MySharedPrefLogin, Context.MODE_PRIVATE);
        String response = sharedPreferences.getString("response", "123");
        String userAccessToken = response;

        PendingIntent pendingIntent = null;

        if (feedId != null) {

            Intent intentMain = new Intent(this, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(intentMain);

            Intent intent = new Intent(this, Proceed_Feed_FullScreen.class);
            intent.putExtra("feedId", feedId);
            intent.putExtra("userAccessTokenFromNotification", userAccessToken);
            intent.putExtra("feedName", title); //Feed Name for toolbar

            stackBuilder.addNextIntent(intent);

            pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        } else if (groupId != null) {

            Intent intentMain = new Intent(this, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(intentMain);

            Intent intent = new Intent(this, Proceed_Group_FullScreen.class);
            intent.putExtra("groupId", groupId);
            intent.putExtra("userAccessTokenFromNotification", userAccessToken);
            intent.putExtra("groupName", title);
            stackBuilder.addNextIntent(intent);
            pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        } else {
            Log.d("createNotification", "In Else Part");
        }


        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.appicon);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(Html.fromHtml("<b>" + userName + " :" + "</b>" + StringEscapeUtils.unescapeJava(body)))
                .setSmallIcon(R.drawable.sabpaisa1234)
                .setLargeIcon(largeIcon)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000/*, 1000, 1000, 1000*/})
                .setLights(Color.RED, 3000, 3000)
                //.setGroup(groupForNotification)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        //.setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml("<b>"+userName+" :"+"</b>"+body)));


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Log.d("notifyID1", " " + notifyID);
        notificationManager.notify(++notifyID, builder.build());

    }

}





