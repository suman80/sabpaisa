package in.sabpaisa.droid.sabpaisa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by abc on 03-06-2017.
 */

/*Helper class for Database*/
public class CommentsDB extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "CommentFeed.db";
    public static final String COMMENT_TABLE_NAME = "comment";
    public static final String COMMENT_COLUMN_ID = "_id";
    public static final String COMMENT_COLUMN_DESC = "comment_desc";
    public static final String COMMENT_COLUMN_TIMESTAMP = "comment_ts";
    public static final String COMMENT_COLUMN_BY = "comment_by";
    public static final String COMMENT_COLUMN_GROUP = "comment_gid";
    public static final String COMMENT_COLUMN_PAGING = "comment_page";
    private static final int DATABASE_VERSION = 1;

    public CommentsDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
    }

    public CommentsDB(Context applicationContext) {
        super(applicationContext, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + COMMENT_TABLE_NAME + "(" +
                COMMENT_COLUMN_ID + " INTEGER PRIMARY KEY, " +            //TODO PRIMARY KEY
                COMMENT_COLUMN_DESC + " TEXT, "+
                COMMENT_COLUMN_TIMESTAMP + " TEXT, " +
                COMMENT_COLUMN_BY + " INTEGER, " +
                COMMENT_COLUMN_GROUP + " TEXT, "+
                COMMENT_COLUMN_PAGING + " INTEGER) "
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + COMMENT_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertComment(String  id, String comment, String timestamp, String By, String GID, int page) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COMMENT_COLUMN_ID, id);
        contentValues.put(COMMENT_COLUMN_DESC, comment);
        contentValues.put(COMMENT_COLUMN_TIMESTAMP, timestamp);
        contentValues.put(COMMENT_COLUMN_BY, By);
        contentValues.put(COMMENT_COLUMN_GROUP, GID);
        contentValues.put(COMMENT_COLUMN_PAGING, page);
        db.insert(COMMENT_TABLE_NAME, null, contentValues);
        return true;
    }

    public void refreshTable(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + COMMENT_TABLE_NAME);
        onCreate(db);
    }

    public Cursor getCommentFromPage(int page) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + COMMENT_TABLE_NAME + " WHERE " +
                COMMENT_COLUMN_PAGING + "=?", new String[] { Integer.toString(page) } );
        return res;
    }
    public Cursor getComments() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + COMMENT_TABLE_NAME , new String[] { } );
        return res;
    }
}
