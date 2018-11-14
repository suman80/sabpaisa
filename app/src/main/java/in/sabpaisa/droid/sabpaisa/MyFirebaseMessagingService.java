package in.sabpaisa.droid.sabpaisa;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
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

import in.sabpaisa.droid.sabpaisa.AppDB.NotificationDB;
import in.sabpaisa.droid.sabpaisa.Interfaces.NotificationInterface;
import in.sabpaisa.droid.sabpaisa.Model.FeedCommentsOfflineModel;
import in.sabpaisa.droid.sabpaisa.Model.FeedNotificatonModel;
import in.sabpaisa.droid.sabpaisa.Model.GroupNotificationModel;

/**
 * Created by rajdeep on 16/9/18.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessagingService";
    String groupForNotification = "SpAppNotification";
    int notifyID = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

    static NotificationInterface notificationInterface;

    NotificationDB db = new NotificationDB(this);

    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        Map<String, String> dataMap = remoteMessage.getData();

        String body = dataMap.get("body");
        String title = dataMap.get("title");
        String userName = dataMap.get("userName");
        String userToken = dataMap.get("userToken");
        final String feedId = dataMap.get("feedId");
        String groupId = dataMap.get("groupId");

        Log.d("BackGroundNoti : ", body + " " + body.trim().length() + " " + title + " " + feedId + " " + groupId + " " + userName);
        Log.d("NotificationBody : ", StringEscapeUtils.unescapeJava(body));



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            createNotificationForOreoAndAbove(title, body, feedId, groupId, userName);

            if (feedId != null){

                Log.d("MFMS","FEED_ID___"+feedId);

                /*Log.d("PFF_notificationFlag", "InsideLoop");
                Proceed_Feed_FullScreen.notificationFlag = feedId;

                Log.d("PFF_notificationFlag"," "+Proceed_Feed_FullScreen.notificationFlag);

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notificationInterface = new Proceed_Feed_FullScreen();

                        notificationInterface.setMemberData(feedId);

                    }
                });
*/

                ////////////////Notification Db//////////////////////////////////////////

                Cursor res = db.getParticularFeedNotificationData(feedId);
                if (res.getCount() > 0) {
                    StringBuffer stringBuffer = new StringBuffer();

                    int commentCount = 0;

                    while (res.moveToNext()) {
                        stringBuffer.append(res.getString(0) + " ");
                        stringBuffer.append(res.getString(1) + " ");
                        stringBuffer.append(res.getString(2) + " ");
                        commentCount = Integer.parseInt(res.getString(2));
                        stringBuffer.append(res.getString(3) + " ");
                        stringBuffer.append(res.getString(4) + " ");
                    }


                    boolean isUpdated = db.updateFeedNotificationData(feedId,commentCount+1,System.currentTimeMillis(), 0);

                    if (isUpdated == true){
                        Log.d("MyFirebMessServiceFeed","Updated "+isUpdated);
                    }else {
                        Log.d("MyFirebMessServiceFeed","NotUpdated "+isUpdated);
                    }

                    Log.d("MyFirebMessServiceFeed"," "+stringBuffer);


                }else {

                    FeedNotificatonModel feedNotificatonModel = new FeedNotificatonModel();
                    feedNotificatonModel.setFeedId(feedId);
                    feedNotificatonModel.setFeedNotificationCount(1);
                    feedNotificatonModel.setFeedRecentCommentTimeStamp(System.currentTimeMillis());
                    feedNotificatonModel.setFeedRecentOpenCommentTimeStamp(0);

                    boolean isInserted = db.insertFeedNotificationData(feedNotificatonModel);
                    if (isInserted == true) {


                        Log.d("MyFirebMessServiceFeed", "LocalDBInIfPart" + isInserted);

                    } else {

                        Log.d("MyFirebMessServiceFeed", "LocalDBInElsePart" + isInserted);

                    }

                }



            }

            else if (groupId != null) {
                Log.d("MFMS","GRP_ID___"+groupId);
                ////////////////Notification Db//////////////////////////////////////////

                Cursor res = db.getParticularGroupNotificationData(groupId);
                if (res.getCount() > 0) {
                    StringBuffer stringBuffer = new StringBuffer();

                    int commentCount = 0;

                    while (res.moveToNext()) {
                        stringBuffer.append(res.getString(0) + " ");
                        stringBuffer.append(res.getString(1) + " ");
                        stringBuffer.append(res.getString(2) + " ");
                        commentCount = Integer.parseInt(res.getString(2));
                        stringBuffer.append(res.getString(3) + " ");
                        stringBuffer.append(res.getString(4) + " ");
                    }

                    Log.d("MyFirebMessServiceGRP", "stringBuffer_ " + stringBuffer);

                    boolean isUpdated = db.updateGroupNotificationData(groupId, commentCount + 1, System.currentTimeMillis(), 0);

                    if (isUpdated == true) {
                        Log.d("MyFirebMessServiceGRP", "Updated " + isUpdated);
                    } else {
                        Log.d("MyFirebMessServiceGRP", "NotUpdated " + isUpdated);
                    }



                } else {

                    GroupNotificationModel groupNotificationModel = new GroupNotificationModel();
                    groupNotificationModel.setGroupId(groupId);
                    groupNotificationModel.setGroupNotificationCount(1);
                    groupNotificationModel.setGroupRecentCommentTimeStamp(System.currentTimeMillis());
                    groupNotificationModel.setGroupRecentOpenCommentTimeStamp(0);

                    boolean isInserted = db.insertGroupNotificationData(groupNotificationModel);
                    if (isInserted == true) {


                        Log.d("MyFirebMessServiceGRP", "LocalDBInIfPart" + isInserted);

                    } else {

                        Log.d("MyFirebMessServiceGRP", "LocalDBInElsePart" + isInserted);

                    }


                }

            }else {
                Log.d("MyFirebMessService", "InElsePart_SmthngWrng");
            }





        } else {

            createNotification(title, body, feedId, groupId, userName);


            if (feedId != null){
                Log.d("MFMS","FEED_ID___"+feedId);
                /*Log.d("PFF_notificationFlag", "InsideLoop");
                Proceed_Feed_FullScreen.notificationFlag = feedId;

                Log.d("PFF_notificationFlag"," "+Proceed_Feed_FullScreen.notificationFlag);

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notificationInterface = new Proceed_Feed_FullScreen();

                        notificationInterface.setMemberData(feedId);

                    }
                });*/


                ////////////////Notification DbinsertGroupNotificationData//////////////////////////////////////////

                Cursor res = db.getParticularFeedNotificationData(feedId);
                if (res.getCount() > 0) {
                    StringBuffer stringBuffer = new StringBuffer();

                    int commentCount = 0;

                    while (res.moveToNext()) {
                        stringBuffer.append(res.getString(0) + " ");
                        stringBuffer.append(res.getString(1) + " ");
                        stringBuffer.append(res.getString(2) + " ");
                        commentCount = Integer.parseInt(res.getString(2));
                        stringBuffer.append(res.getString(3) + " ");
                        stringBuffer.append(res.getString(4) + " ");
                    }

                    boolean isUpdated = db.updateFeedNotificationData(feedId,commentCount+1,System.currentTimeMillis(), 0);

                    if (isUpdated == true){
                        Log.d("MyFirebMessServiceFeed","Updated "+isUpdated);
                    }else {
                        Log.d("MyFirebMessServiceFeed","NotUpdated "+isUpdated);
                    }

                    Log.d("MyFirebMessServiceFeed"," "+stringBuffer);

                }else {

                    FeedNotificatonModel feedNotificatonModel = new FeedNotificatonModel();
                    feedNotificatonModel.setFeedId(feedId);
                    feedNotificatonModel.setFeedNotificationCount(1);
                    feedNotificatonModel.setFeedRecentCommentTimeStamp(System.currentTimeMillis());
                    feedNotificatonModel.setFeedRecentOpenCommentTimeStamp(0);

                    boolean isInserted = db.insertFeedNotificationData(feedNotificatonModel);
                    if (isInserted == true) {


                        Log.d("MyFirebMessServiceFeed", "LocalDBInIfPart" + isInserted);

                    } else {

                        Log.d("MyFirebMessServiceFeed", "LocalDBInElsePart" + isInserted);

                    }





                }



            }

            else if (groupId != null){
                Log.d("MFMS_Below_Oreo","GRP_ID___"+groupId);
                ////////////////Notification Db//////////////////////////////////////////

                Cursor res = db.getParticularGroupNotificationData(groupId);
                if (res.getCount() > 0) {
                    StringBuffer stringBuffer = new StringBuffer();

                    int commentCount = 0;

                    while (res.moveToNext()) {
                        stringBuffer.append(res.getString(0) + " ");
                        stringBuffer.append(res.getString(1) + " ");
                        stringBuffer.append(res.getString(2) + " ");
                        commentCount = Integer.parseInt(res.getString(2));
                        stringBuffer.append(res.getString(3) + " ");
                        stringBuffer.append(res.getString(4) + " ");
                    }

                    Log.d("MyFirebMessServiceGRP", "stringBuffer_ " + stringBuffer);

                    boolean isUpdated = db.updateGroupNotificationData(groupId,commentCount+1,System.currentTimeMillis(), 0);

                    if (isUpdated == true){
                        Log.d("MyFirebMessServiceGRP","Updated "+isUpdated);
                    }else {
                        Log.d("MyFirebMessServiceGRP","NotUpdated "+isUpdated);
                    }

                }else {

                    GroupNotificationModel groupNotificationModel = new GroupNotificationModel();
                    groupNotificationModel.setGroupId(groupId);
                    groupNotificationModel.setGroupNotificationCount(1);
                    groupNotificationModel.setGroupRecentCommentTimeStamp(System.currentTimeMillis());
                    groupNotificationModel.setGroupRecentOpenCommentTimeStamp(0);

                    boolean isInserted = db.insertGroupNotificationData(groupNotificationModel);
                    if (isInserted == true) {


                        Log.d("MyFirebMessServiceGRP", "LocalDBInIfPart" + isInserted);

                    } else {

                        Log.d("MyFirebMessServiceGRP", "LocalDBInElsePart" + isInserted);

                    }





                }



            }else {
                Log.d("MyFirebMessService", "InElsePart_SmthngWrng");
            }






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





