package in.sabpaisa.droid.sabpaisa.AppDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import in.sabpaisa.droid.sabpaisa.CommentData;
import in.sabpaisa.droid.sabpaisa.FeedData;
import in.sabpaisa.droid.sabpaisa.GroupListData;
import in.sabpaisa.droid.sabpaisa.Model.FeedDataForOffLine;
import in.sabpaisa.droid.sabpaisa.Model.GroupDataForOffLine;
import in.sabpaisa.droid.sabpaisa.Model.Institution;
import in.sabpaisa.droid.sabpaisa.Model.MemberOfflineDataModel;
import in.sabpaisa.droid.sabpaisa.Model.ParticularClientModelForOffline;
import io.fabric.sdk.android.services.concurrency.AsyncTask;

public class AppDbComments extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "SpAppComments.db";
    long result;

    // Columns names for Particular Clients
    public static final String TABLE_Particular_Client = "particular_client";
    public static final String Col_ID_AutoIncrement = "id";
    public static final String Col_clientId = "particular_client_id";
    public static final String Col_Client_Name = "client_name";
    public static final String Col_State = "state";
    public static final String Col_clientLogoPath = "clientLogoPath";
    public static final String Col_clientImagePath = "clientImagePath";

    // Column names for feeds
    public static final String TABLE_NAME = "feeds";
    public static final String Col_ID = "id";
    public static final String Col_Client_ID = "client_id";
    public static final String Col_FEED_ID = "feed_id";
    public static final String Col_FEED_NAME = "feed_name";
    public static final String Col_FEED_TEXT = "feed_text";
    public static final String Col_FEED_IMG = "feed_img";
    public static final String Col_FEED_LOGO = "feed_logo";



    // Column names for feeds comments
    public static final String TABLE_FEED_COMMENTS = "feed_comments";
    public static final String Col_ID_FeedComment = "id";
    public static final String Col_FEED_CommentFeedId = "feed_comment_feed_id";
    public static final String Col_FEED_CommentId = "feed_comment_id";
    public static final String Col_FEED_CommentText = "comment_Text";
    public static final String Col_FEED_commentByName = "comment_ByName";
    public static final String Col_FEED_commentDate = "commentDate";




    // Column names for Groups
    public static final String TABLE_NAME_GROUPS = "groups";
    public static final String Col_ID_GROUP_AutoIncrement = "id";
    public static final String Col_Client_ID_GROUP = "client_id";
    public static final String Col_GROUP_ID = "group_id";
    public static final String Col_GROUP_NAME = "group_name";
    public static final String Col_GROUP_TEXT = "group_text";
    public static final String Col_GROUP_STATUS = "group_status";
    public static final String Col_GROUP_USER_ACCESSTOKEN = "group_user_accesstoken";
    public static final String Col_GROUP_IMG = "group_img";
    public static final String Col_GROUP_LOGO = "group_logo";



    // Column names for Groups comments
    public static final String TABLE_GROUP_COMMENTS = "group_comments";
    public static final String Col_ID_GroupComment = "id";
    public static final String Col_GROUP_CommentGroupId = "group_comment_group_id";
    public static final String Col_GROUP_CommentId = "group_comment_id";
    public static final String Col_GROUP_CommentText = "comment_Text";
    public static final String Col_GROUP_commentByName = "comment_ByName";
    public static final String Col_GROUP_commentDate = "commentDate";



    // Column names for Members
    public static final String TABLE_NAME_MEMBERS = "members";
    public static final String Col_ID_MEMBERS = "id";
    public static final String Col_MEMBERS_Client_ID = "client_id";
    public static final String Col_MEMBERS_NAME = "members_name";
    public static final String Col_MEMBERS_IMG = "members_img";





    public AppDbComments(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlForClient = " CREATE TABLE " + TABLE_Particular_Client + "("
                +Col_ID_AutoIncrement+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +Col_clientId+" TEXT,"
                +Col_Client_Name+" TEXT,"
                +Col_State+" TEXT,"
                +Col_clientLogoPath+" TEXT,"
                +Col_clientImagePath+" TEXT"
                +")";


        String sqlForFeeds = " CREATE TABLE " + TABLE_NAME + "("
                +Col_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +Col_Client_ID+" TEXT,"
                +Col_FEED_ID+" TEXT,"
                +Col_FEED_NAME+" TEXT,"
                +Col_FEED_TEXT+" TEXT,"
                +Col_FEED_IMG+" TEXT,"
                +Col_FEED_LOGO+" TEXT"
                +")";


        String sqlForFeedsComments = " CREATE TABLE " + TABLE_FEED_COMMENTS + "("
                +Col_ID_FeedComment+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +Col_FEED_CommentFeedId+" TEXT,"
                +Col_FEED_CommentId+" INT,"
                +Col_FEED_CommentText+" TEXT,"
                +Col_FEED_commentByName+" TEXT,"
                +Col_FEED_commentDate+" TEXT"
                +")";



        String sqlForGroups = " CREATE TABLE " + TABLE_NAME_GROUPS + "("
                +Col_ID_GROUP_AutoIncrement+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +Col_Client_ID_GROUP+" TEXT,"
                +Col_GROUP_ID+" TEXT,"
                +Col_GROUP_NAME+" TEXT,"
                +Col_GROUP_TEXT+" TEXT,"
                +Col_GROUP_STATUS+" TEXT,"
                +Col_GROUP_USER_ACCESSTOKEN+" TEXT,"
                +Col_GROUP_IMG+" BLOB,"
                +Col_GROUP_LOGO+" BLOB"
                +")";


        String sqlForGroupComments = " CREATE TABLE " + TABLE_GROUP_COMMENTS + "("
                +Col_ID_GroupComment+" INTEGER PRIMARY KEY AUTOINCREMENT,"             //Auto Increment ID
                +Col_GROUP_CommentGroupId+" TEXT,"                                  //Group ID
                +Col_GROUP_CommentId+" INT,"                                            //Particular Comment ID
                +Col_GROUP_CommentText+" TEXT,"
                +Col_GROUP_commentByName+" TEXT,"
                +Col_GROUP_commentDate+" TEXT"
                +")";


        String sqlForMembers = " CREATE TABLE " + TABLE_NAME_MEMBERS + "("
                +Col_ID_MEMBERS+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +Col_MEMBERS_Client_ID+" TEXT,"
                +Col_MEMBERS_NAME+" TEXT,"
                +Col_MEMBERS_IMG+" TEXT"
                +")";


        db.execSQL(sqlForClient);

        db.execSQL(sqlForFeeds);

        db.execSQL(sqlForFeedsComments);

        db.execSQL(sqlForGroups);

        db.execSQL(sqlForGroupComments);

        db.execSQL(sqlForMembers);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        String sqlForClient = "DROP TABLE IF EXISTS "+TABLE_Particular_Client;
        String sqlForFeeds = "DROP TABLE IF EXISTS "+TABLE_NAME;
        String sqlForFeedsComments = "DROP TABLE IF EXISTS "+TABLE_FEED_COMMENTS;
        String sqlForGroups = "DROP TABLE IF EXISTS "+TABLE_NAME_GROUPS;
        String sqlForGroupComments = "DROP TABLE IF EXISTS "+TABLE_GROUP_COMMENTS;
        String sqlForMembers = "DROP TABLE IF EXISTS "+TABLE_NAME_MEMBERS;

        db.execSQL(sqlForClient);

        db.execSQL(sqlForFeeds);

        db.execSQL(sqlForFeedsComments);

        db.execSQL(sqlForGroups);

        db.execSQL(sqlForGroupComments);

        db.execSQL(sqlForMembers);

    }

    // Particular Client data
    public boolean insertClientData(final ParticularClientModelForOffline particularClientModelForOffline) {
        Log.d("Insert Data in  ", "client table");

        final SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_clientId, particularClientModelForOffline.getClientId());
        contentValues.put(Col_Client_Name, particularClientModelForOffline.getClientName());
        contentValues.put(Col_State, particularClientModelForOffline.getState());
        contentValues.put(Col_clientLogoPath, particularClientModelForOffline.getClientLogoPath());
        contentValues.put(Col_clientImagePath, particularClientModelForOffline.getClientImagePath());
        result = db.insert(TABLE_Particular_Client, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }


    public Cursor getParticularClientData(String clientId){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_Particular_Client + " WHERE " +
                Col_clientId + "=?",new String[]{clientId});
        return res;
    }


    public void deleteAllClientData(){
        SQLiteDatabase db = this.getWritableDatabase();

//        db.execSQL("delete from "+ TABLE_Particular_Client);
//        db.execSQL("delete from "+ TABLE_Particular_Client);
        Log.d("TABLE_Particular_Client","DELETE");
        db.delete(TABLE_Particular_Client, null, null);

        db.close();
    }

    // Feed Data
    public boolean insertFeedData (final FeedDataForOffLine feedDataForOffLine){

        final SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_Client_ID,feedDataForOffLine.getClientId());
        contentValues.put(Col_FEED_ID,feedDataForOffLine.getFeedId());
        contentValues.put(Col_FEED_NAME,feedDataForOffLine.getFeedName());
        contentValues.put(Col_FEED_TEXT,feedDataForOffLine.getFeedText());
        contentValues.put(Col_FEED_IMG,feedDataForOffLine.getImagePath());
        contentValues.put(Col_FEED_LOGO,feedDataForOffLine.getLogoPath());
        result = db.insert(TABLE_NAME,null,contentValues);

        if (result == -1){
            return false;
        }else {
            return true;
        }

    }

    public Cursor getParticularFeedData(String clientId){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " +
                Col_Client_ID + "=?",new String[]{clientId});
        return res;
    }


    public void deleteAllFeedData(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from "+ TABLE_NAME);
        db.close();
    }


    //Feeds Comments
    public boolean insertFeedComments (final CommentData commentData , final String feedId){

        final SQLiteDatabase db = this.getWritableDatabase();

        Log.d("Inside Insertion ", " for feed-Comment");

        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_FEED_CommentFeedId,feedId+"");
        contentValues.put(Col_FEED_CommentId,commentData.getCommentId());
        contentValues.put(Col_FEED_CommentText,commentData.getCommentText());
        contentValues.put(Col_FEED_commentByName,commentData.getCommentName());
        contentValues.put(Col_FEED_commentDate,commentData.getComment_date());
        result = db.insert(TABLE_FEED_COMMENTS,null,contentValues);


        if (result == -1){
            return false;
        }else {
            return true;
        }

    }


    public Cursor getParticularFeedComments(String feedId){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_FEED_COMMENTS + " WHERE " +
                Col_FEED_CommentFeedId + "=?",new String[]{feedId});
        return res;
    }



    public void deleteAllFeedCommentData(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from "+ TABLE_FEED_COMMENTS);
        db.close();
    }


    //Groups Data
    public boolean insertGroupData (final GroupDataForOffLine groupDataForOffLine , final String userAccessToken ){

        final SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_Client_ID_GROUP,groupDataForOffLine.getClientId());
        contentValues.put(Col_GROUP_ID,groupDataForOffLine.getGroupId());
        contentValues.put(Col_GROUP_NAME,groupDataForOffLine.getGroupName());
        contentValues.put(Col_GROUP_TEXT,groupDataForOffLine.getGroupText());
        contentValues.put(Col_GROUP_STATUS,groupDataForOffLine.getMemberStatus());
        contentValues.put(Col_GROUP_USER_ACCESSTOKEN,userAccessToken);
        contentValues.put(Col_GROUP_IMG,groupDataForOffLine.getGroupImage());
        contentValues.put(Col_GROUP_LOGO,groupDataForOffLine.getGroupLogo());
        result = db.insert(TABLE_NAME_GROUPS,null,contentValues);

        if (result == -1){
            return false;
        }else {
            return true;
        }

    }

    public Cursor getParticularGroupData(String clientId){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME_GROUPS + " WHERE " +
                Col_Client_ID_GROUP + "=?",new String[]{clientId});
        return res;
    }


    public void deleteAllGroupData(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from "+ TABLE_NAME_GROUPS);
        db.close();
    }


    //Group Comments
    public boolean insertGroupComments (final CommentData commentData , final String groupId){

        final SQLiteDatabase db = this.getWritableDatabase();

        Log.d("Inside Insertion ", " for group-Comment");

        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_GROUP_CommentGroupId,groupId+"");
        contentValues.put(Col_GROUP_CommentId,commentData.getCommentId());
        contentValues.put(Col_GROUP_CommentText,commentData.getCommentText());
        contentValues.put(Col_GROUP_commentByName,commentData.getCommentName());
        contentValues.put(Col_GROUP_commentDate,commentData.getComment_date());
        result = db.insert(TABLE_GROUP_COMMENTS,null,contentValues);


        if (result == -1){
            return false;
        }else {
            return true;
        }

    }


    public Cursor getParticularGroupComments(String groupId){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_GROUP_COMMENTS + " WHERE " +
                Col_GROUP_CommentGroupId + "=?",new String[]{groupId});
        return res;
    }



    public void deleteAllGroupCommentData(String groupId){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from "+ TABLE_GROUP_COMMENTS +" WHERE " +Col_GROUP_CommentGroupId + "=?",new String[]{groupId});
        db.close();
    }


    // Member Data
    public boolean insertMembersData (final String clientId,final MemberOfflineDataModel memberOfflineDataModel){

        final SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_MEMBERS_Client_ID,clientId);
        contentValues.put(Col_MEMBERS_NAME,memberOfflineDataModel.getFullName());
        contentValues.put(Col_MEMBERS_IMG,memberOfflineDataModel.getUserImageUrl());
        result = db.insert(TABLE_NAME_MEMBERS,null,contentValues);

        if (result == -1){
            return false;
        }else {
            return true;
        }

    }

    public Cursor getParticularMembersData(String clientId){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME_MEMBERS + " WHERE " +
                Col_MEMBERS_Client_ID + "=?",new String[]{clientId});
        return res;
    }


    public void deleteAllMembersData(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from "+ TABLE_NAME_MEMBERS);
        db.close();
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


    //https://stackoverflow.com/questions/20415309/android-sqlite-how-to-check-if-a-record-exists

    //https://stackoverflow.com/questions/3058909/how-does-one-check-if-a-table-exists-in-an-android-sqlite-database




}
