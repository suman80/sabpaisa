package in.sabpaisa.droid.sabpaisa.AppDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import in.sabpaisa.droid.sabpaisa.Model.FeedNotificatonModel;
import in.sabpaisa.droid.sabpaisa.Model.GroupNotificationModel;
import in.sabpaisa.droid.sabpaisa.Model.ParticularClientModelForOffline;

public class NotificationDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "NotificationDB.db";
    long result;

    // Column names for Feed Notification
    public static final String TABLE_FEEDNOTIFICATION = "FeedNotification";
    public static final String Col_FID = "Fid";
    public static final String Col_FEED_ID = "FeedId";
    //public static final String Col_FEED_NAME = "FeedName";
    //public static final String Col_FEED_BODY = "FeedBody";
    public static final String Col_FEED_NOTIFICATION_COUNT = "FeedNotificationCount";
    public static final String Col_FEED_RECENT_COMMENT_TIMESTAMP = "FeedRecentCommentTimestamp";
    public static final String Col_FEED_RECENT_OPEN_COMMENT_TIMESTAMP = "FeedRecentOpenCommentTimestamp";

    // Column names for Group Notification
    public static final String TABLE_GROUPNOTIFICATION = "GroupNotification";
    public static final String Col_GID = "Gid";
    public static final String Col_GROUP_ID = "GroupId";
    //public static final String Col_GROUP_NAME = "GroupName";
    //public static final String Col_GROUP_BODY = "GroupBody";
    public static final String Col_GROUP_NOTIFICATION_COUNT = "GroupNotificationCount";
    public static final String Col_GROUP_RECENT_COMMENT_TIMESTAMP = "GroupRecentCommentTimestamp";
    public static final String Col_GROUP_RECENT_OPEN_COMMENT_TIMESTAMP = "GroupRecentOpenCommentTimestamp";


    public NotificationDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlFeed = " CREATE TABLE " + TABLE_FEEDNOTIFICATION + "("
                +Col_FID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +Col_FEED_ID+" TEXT,"
                +Col_FEED_NOTIFICATION_COUNT+" INT,"
                +Col_FEED_RECENT_COMMENT_TIMESTAMP+" LONG,"
                +Col_FEED_RECENT_OPEN_COMMENT_TIMESTAMP+" LONG"
                +")";


        String sqlGroup = " CREATE TABLE " + TABLE_GROUPNOTIFICATION + "("
                +Col_GID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +Col_GROUP_ID+" TEXT,"
                +Col_GROUP_NOTIFICATION_COUNT+" INT,"
                +Col_GROUP_RECENT_COMMENT_TIMESTAMP+" LONG,"
                +Col_GROUP_RECENT_OPEN_COMMENT_TIMESTAMP+" LONG"
                +")";


        db.execSQL(sqlFeed);
        db.execSQL(sqlGroup);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        String sqlFeed = "DROP TABLE IF EXISTS "+TABLE_FEEDNOTIFICATION;
        db.execSQL(sqlFeed);

        String sqlGroup = "DROP TABLE IF EXISTS "+TABLE_GROUPNOTIFICATION;
        db.execSQL(sqlGroup);

        onCreate(db);

    }

    public boolean insertFeedNotificationData(final FeedNotificatonModel feedNotificatonModel) {

        final SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_FEED_ID, feedNotificatonModel.getFeedId());
        contentValues.put(Col_FEED_NOTIFICATION_COUNT, feedNotificatonModel.getFeedNotificationCount());
        contentValues.put(Col_FEED_RECENT_COMMENT_TIMESTAMP, feedNotificatonModel.getFeedRecentCommentTimeStamp());
        contentValues.put(Col_FEED_RECENT_OPEN_COMMENT_TIMESTAMP, feedNotificatonModel.getFeedRecentOpenCommentTimeStamp());

        result = db.insert(TABLE_FEEDNOTIFICATION, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean updateFeedNotificationData(final String feedId , final int count , final long feedRecentCommentTimestamp , final long feedRecentOpenCommentTimestamp) {

        final SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_FEED_NOTIFICATION_COUNT, count);
        if(feedRecentCommentTimestamp > 0)
        contentValues.put(Col_FEED_RECENT_COMMENT_TIMESTAMP, feedRecentCommentTimestamp);
        if(feedRecentOpenCommentTimestamp > 0)
            contentValues.put(Col_FEED_RECENT_OPEN_COMMENT_TIMESTAMP, feedRecentOpenCommentTimestamp);

        int effectedRow = db.update(TABLE_FEEDNOTIFICATION, contentValues ,Col_FEED_ID + "=?",new String[]{feedId});
        //result = db.update(TABLE_FEEDNOTIFICATION, contentValues ,Col_FEED_ID + "=?" +feedId,null);

        if (effectedRow > 0) {
            return true;
        } else {
            return false;
        }

    }


    public Cursor getParticularFeedNotificationData(String feedId){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_FEEDNOTIFICATION + " WHERE " +
                Col_FEED_ID + "=?",new String[]{feedId});
        return res;
    }


    public boolean insertGroupNotificationData(final GroupNotificationModel groupNotificatonModel) {

        final SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_GROUP_ID, groupNotificatonModel.getGroupId());
        contentValues.put(Col_GROUP_NOTIFICATION_COUNT, groupNotificatonModel.getGroupNotificationCount());
        contentValues.put(Col_GROUP_RECENT_COMMENT_TIMESTAMP, groupNotificatonModel.getGroupRecentCommentTimeStamp());
        contentValues.put(Col_GROUP_RECENT_OPEN_COMMENT_TIMESTAMP, groupNotificatonModel.getGroupRecentOpenCommentTimeStamp());

        result = db.insert(TABLE_GROUPNOTIFICATION, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }


    public boolean updateGroupNotificationData(final String groupId , final int count , final long groupRecentCommentTimestamp , final long groupRecentOpenCommentTimestamp) {

        final SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_GROUP_NOTIFICATION_COUNT, count);
        if(groupRecentCommentTimestamp > 0)
        contentValues.put(Col_GROUP_RECENT_COMMENT_TIMESTAMP, groupRecentCommentTimestamp);
        if(groupRecentOpenCommentTimestamp > 0)
            contentValues.put(Col_GROUP_RECENT_OPEN_COMMENT_TIMESTAMP, groupRecentOpenCommentTimestamp);

        int effectedRow = db.update(TABLE_GROUPNOTIFICATION, contentValues ,Col_GROUP_ID + "=?",new String[]{groupId});
        //result = db.update(TABLE_FEEDNOTIFICATION, contentValues ,Col_FEED_ID + "=?" +feedId,null);
        if (effectedRow > 0) {
            return true;
        } else {
            return false;
        }

    }


    public Cursor getParticularGroupNotificationData(String groupId){
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("In Fetch GroupId ", groupId);
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_GROUPNOTIFICATION + " WHERE " +
                Col_GROUP_ID + "=?",new String[]{groupId});
        Log.d("Groupdata_GrpId ", groupId);
        return res;
    }



    public boolean isTableExists(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }



}
