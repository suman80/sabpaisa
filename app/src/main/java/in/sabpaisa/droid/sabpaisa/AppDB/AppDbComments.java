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
import in.sabpaisa.droid.sabpaisa.Model.Institution;
import io.fabric.sdk.android.services.concurrency.AsyncTask;

public class AppDbComments extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "SpAppComments.db";

    // Columns names for Particular Clients
    public static final String TABLE_Particular_Client = "particular_client";
    public static final String Col_ID_AutoIncrement = "id";
    public static final String Col_clientId = "particular_client_id";
    public static final String Col_Client_Name = "client_name";
    public static final String Col_State = "state";
    public static final String Col_clientLogoPath = "clientLogoPath";
    public static final String Col_clientImagePath = "clientImagePath";
    public byte [] client_image,client_logo;


    // Column names for feeds
    public static final String TABLE_NAME = "feeds";
    public static final String Col_ID = "id";
    public static final String Col_Client_ID = "client_id";
    public static final String Col_FEED_ID = "feed_id";
    public static final String Col_FEED_NAME = "feed_name";
    public static final String Col_FEED_TEXT = "feed_text";
    public static final String Col_FEED_IMG = "feed_img";
    public static final String Col_FEED_LOGO = "feed_logo";
    public byte [] feed_image,feed_logo;
    long result;


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
    public byte [] group_image,group_logo;


    // Column names for Groups comments
    public static final String TABLE_GROUP_COMMENTS = "group_comments";
    public static final String Col_ID_GroupComment = "id";
    public static final String Col_GROUP_CommentGroupId = "group_comment_group_id";
    public static final String Col_GROUP_CommentId = "group_comment_id";
    public static final String Col_GROUP_CommentText = "comment_Text";
    public static final String Col_GROUP_commentByName = "comment_ByName";
    public static final String Col_GROUP_commentDate = "commentDate";





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
                +Col_clientLogoPath+" BLOB,"
                +Col_clientImagePath+" BLOB"
                +")";

        String sqlForFeeds = " CREATE TABLE " + TABLE_NAME + "("
                +Col_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +Col_Client_ID+" TEXT,"
                +Col_FEED_ID+" TEXT,"
                +Col_FEED_NAME+" TEXT,"
                +Col_FEED_TEXT+" TEXT,"
                +Col_FEED_IMG+" BLOB,"
                +Col_FEED_LOGO+" BLOB"
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

        Log.d("Going to create : ", "client table");
        db.execSQL(sqlForClient);
        Log.d("Going to create : ", "feed table");
        db.execSQL(sqlForFeeds);
        Log.d("Going to create : ", "feed_comments table");
        db.execSQL(sqlForFeedsComments);
        Log.d("created : ", "feed_comments table");
        Log.d("Going to create : ", "group table");
        db.execSQL(sqlForGroups);
        db.execSQL(sqlForGroupComments);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        String sqlForClient = "DROP TABLE IF EXISTS "+TABLE_Particular_Client;
        String sqlForFeeds = "DROP TABLE IF EXISTS "+TABLE_NAME;
        String sqlForFeedsComments = "DROP TABLE IF EXISTS "+TABLE_FEED_COMMENTS;
        String sqlForGroups = "DROP TABLE IF EXISTS "+TABLE_NAME_GROUPS;
        String sqlForGroupComments = "DROP TABLE IF EXISTS "+TABLE_GROUP_COMMENTS;

        db.execSQL(sqlForClient);
        db.execSQL(sqlForFeeds);
        db.execSQL(sqlForFeedsComments);
        db.execSQL(sqlForGroups);
        db.execSQL(sqlForGroupComments);

    }

    // Particular Client data
    public boolean insertClientData(final Institution institution) {
        Log.d("Insert Data in  ", "client table");
        new clientImageDownloader().execute(institution.getOrgWal());
        new clientLogoDownloader().execute(institution.getOrgLogo());

        final SQLiteDatabase db = this.getWritableDatabase();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                ContentValues contentValues = new ContentValues();
                contentValues.put(Col_clientId, institution.getOrganizationId());
                contentValues.put(Col_Client_Name, institution.getOrganization_name());
                contentValues.put(Col_State, institution.getOrgAddress());
                contentValues.put(Col_clientImagePath, client_image);
                contentValues.put(Col_clientLogoPath, client_logo);
                result = db.insert(TABLE_Particular_Client, null, contentValues);

            }
        }, 10000);

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

        db.execSQL("delete from "+ TABLE_Particular_Client);
        db.close();
    }


    // Feed Data
    public boolean insertFeedData (final FeedData feedData){
        new feedImageDownloader().execute(feedData.getImagePath());
        new feedLogoDownloader().execute(feedData.getLogoPath());
        //image = getByteFromUrl(feedData.getImagePath(), image);
        //logo = getByteFromUrl(feedData.getLogoPath(), logo);
        final SQLiteDatabase db = this.getWritableDatabase();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                ContentValues contentValues = new ContentValues();
                contentValues.put(Col_Client_ID,feedData.getClientId());
                contentValues.put(Col_FEED_ID,feedData.getFeedId());
                contentValues.put(Col_FEED_NAME,feedData.getFeedName());
                contentValues.put(Col_FEED_TEXT,feedData.getFeedText());
                contentValues.put(Col_FEED_IMG,feed_image);
                contentValues.put(Col_FEED_LOGO,feed_logo);
                result = db.insert(TABLE_NAME,null,contentValues);

            }
        }, 10000);

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
        //new feedImageDownloader().execute(feedData.getImagePath());
        //new feedLogoDownloader().execute(feedData.getLogoPath());
        //image = getByteFromUrl(feedData.getImagePath(), image);
        //logo = getByteFromUrl(feedData.getLogoPath(), logo);
        final SQLiteDatabase db = this.getWritableDatabase();
//        final Handler handler = new Handler();
        Log.d("Inside Insertion ", " for feed-Comment");
       /* handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms*/
                ContentValues contentValues = new ContentValues();
                contentValues.put(Col_FEED_CommentFeedId,feedId+"");
                contentValues.put(Col_FEED_CommentId,commentData.getCommentId());
                contentValues.put(Col_FEED_CommentText,commentData.getCommentText());
                contentValues.put(Col_FEED_commentByName,commentData.getCommentName());
                contentValues.put(Col_FEED_commentDate,commentData.getComment_date());
                result = db.insert(TABLE_FEED_COMMENTS,null,contentValues);

            //}
//        }, 10000);

        if (result == -1){
            return false;
        }else {
            return true;
        }
        //comment
        //comment
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
    public boolean insertGroupData (final GroupListData groupListData , final String userAccessToken){
        new groupImageDownloader().execute(groupListData.getImagePath());
        new groupLogoDownloader().execute(groupListData.getLogoPath());
        //image = getByteFromUrl(feedData.getImagePath(), image);
        //logo = getByteFromUrl(feedData.getLogoPath(), logo);
        final SQLiteDatabase db = this.getWritableDatabase();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                ContentValues contentValues = new ContentValues();
                contentValues.put(Col_Client_ID_GROUP,groupListData.getClientId());
                contentValues.put(Col_GROUP_ID,groupListData.getGroupId());
                contentValues.put(Col_GROUP_NAME,groupListData.getGroupName());
                contentValues.put(Col_GROUP_TEXT,groupListData.getGroupText());
                contentValues.put(Col_GROUP_STATUS,groupListData.getMemberStatus());
                contentValues.put(Col_GROUP_USER_ACCESSTOKEN,userAccessToken);
                contentValues.put(Col_GROUP_IMG,group_image);
                contentValues.put(Col_GROUP_LOGO,group_logo);
                result = db.insert(TABLE_NAME_GROUPS,null,contentValues);

            }
        }, 10000);

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
        //new feedImageDownloader().execute(feedData.getImagePath());
        //new feedLogoDownloader().execute(feedData.getLogoPath());
        //image = getByteFromUrl(feedData.getImagePath(), image);
        //logo = getByteFromUrl(feedData.getLogoPath(), logo);
        final SQLiteDatabase db = this.getWritableDatabase();
//        final Handler handler = new Handler();
        Log.d("Inside Insertion ", " for group-Comment");
       /* handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms*/
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_GROUP_CommentGroupId,groupId+"");
        contentValues.put(Col_GROUP_CommentId,commentData.getCommentId());
        contentValues.put(Col_GROUP_CommentText,commentData.getCommentText());
        contentValues.put(Col_GROUP_commentByName,commentData.getCommentName());
        contentValues.put(Col_GROUP_commentDate,commentData.getComment_date());
        result = db.insert(TABLE_GROUP_COMMENTS,null,contentValues);

        //}
//        }, 10000);

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

    /*public boolean isTableExists(String tableName, boolean openDb) {
        if(openDb) {
            if(mDatabase == null || !mDatabase.isOpen()) {
                mDatabase = getReadableDatabase();
            }

            if(!mDatabase.isReadOnly()) {
                mDatabase.close();
                mDatabase = getReadableDatabase();
            }
        }

        Cursor cursor = mDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }
    https://stackoverflow.com/questions/3058909/how-does-one-check-if-a-table-exists-in-an-android-sqlite-database
*/

    /************************Download Image*************************************************************/
    private class clientImageDownloader extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... url) {

            String myUrl = url [0];
            Bitmap bitmap = null;

            try {
                InputStream inputStream = new URL(myUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                client_image = getBytes(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
//            image = getBytes(bitmap);
        }
    }

    private class clientLogoDownloader extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... url) {

            String myUrl = url [0];
            Bitmap bitmap = null;

            try {
                InputStream inputStream = new URL(myUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                client_logo = getBytes(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
//            logo = getBytes(bitmap);
        }
    }


    private class feedImageDownloader extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... url) {

            String myUrl = url [0];
            Bitmap bitmap = null;

            try {
                InputStream inputStream = new URL(myUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                feed_image = getBytes(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
//            image = getBytes(bitmap);
        }
    }

    private class feedLogoDownloader extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... url) {

            String myUrl = url [0];
            Bitmap bitmap = null;

            try {
                InputStream inputStream = new URL(myUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                feed_logo = getBytes(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
//            logo = getBytes(bitmap);
        }
    }


    private class groupImageDownloader extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... url) {

            String myUrl = url [0];
            Bitmap bitmap = null;

            try {
                InputStream inputStream = new URL(myUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                feed_image = getBytes(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
//            image = getBytes(bitmap);
        }
    }

    private class groupLogoDownloader extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... url) {

            String myUrl = url [0];
            Bitmap bitmap = null;

            try {
                InputStream inputStream = new URL(myUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                feed_logo = getBytes(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
//            logo = getBytes(bitmap);
        }
    }


    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

}
