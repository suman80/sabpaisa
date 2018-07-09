package in.sabpaisa.droid.sabpaisa.AppDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AppDbImage extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "SpAppImg.db";

    long result;

    // Columns names for Particular Clients Image
    public static final String TABLE_ParticularClientImage = "particularClientImg";
    public static final String Col_ImgID_AutoIncrement = "id";
    public static final String Col_ImgclientId = "ImgClientId";
    public static final String Col_clientLogoPath = "clientLogoPath";
    public static final String Col_clientImagePath = "clientImagePath";


    public AppDbImage(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlForClientImage = " CREATE TABLE " + TABLE_ParticularClientImage + "("
                +Col_ImgID_AutoIncrement+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +Col_ImgclientId+" TEXT,"
                +Col_clientLogoPath+" TEXT,"
                +Col_clientImagePath+" TEXT"
                +")";

        db.execSQL(sqlForClientImage);

        }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {


        String sqlForClientImage = "DROP TABLE IF EXISTS "+TABLE_ParticularClientImage;
        db.execSQL(sqlForClientImage);

    }



    // Particular Client Image data
    public boolean insertClientImageData(final String clientId , final String logo ,final String image) {
        Log.d("Insert Data in  ", "client Image table");

        final SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_ImgclientId, clientId);
        contentValues.put(Col_clientLogoPath, logo);
        contentValues.put(Col_clientImagePath, image);
        result = db.insert(TABLE_ParticularClientImage, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }


    public Cursor getParticularClientImageData(String clientId){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_ParticularClientImage + " WHERE " +
                Col_ImgclientId + "=?",new String[]{clientId});
        return res;
    }


    public void deleteAllImageData(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from "+ TABLE_ParticularClientImage);
        db.close();
    }


}
