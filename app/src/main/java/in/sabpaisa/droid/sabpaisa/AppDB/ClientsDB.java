package in.sabpaisa.droid.sabpaisa.AppDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import in.sabpaisa.droid.sabpaisa.Model.ClientsModel;
import in.sabpaisa.droid.sabpaisa.Model.ParticularClientModelForOffline;

public class ClientsDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ClientsDB.db";
    long result;

    // Columns names for Clients
    public static final String TABLE_Particular_Client = "particularClient";
    public static final String Col_ID_AutoIncrement = "id";
    public static final String Col_clientId = "clientId";
    public static final String Col_client_Name = "clientName";
    public static final String Col_state = "state";
    public static final String Col_stateId = "stateId";
    public static final String Col_clientLogoPath = "clientLogoPath";
    public static final String Col_clientImagePath = "clientImagePath";
    public static final String Col_timeStampForParticularClient = "timeStampForParticularClient";


    // Columns names for Users
    public static final String TABLE_Users = "users";
    public static final String Col_User_ID_AutoIncrement = Col_ID_AutoIncrement;
    public static final String Col_userId = "userId";
    public static final String Col_userAccesToken = "userAccesToken";


    public ClientsDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlForClient = " CREATE TABLE " + TABLE_Particular_Client + "("
                +Col_ID_AutoIncrement+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +Col_clientId+" TEXT,"
                +Col_client_Name+" TEXT,"
                +Col_state+" TEXT,"
                +Col_stateId+" TEXT,"
                +Col_clientLogoPath+" TEXT,"
                +Col_clientImagePath+" TEXT,"
                +Col_timeStampForParticularClient+" LONG"
                +")";



        db.execSQL(sqlForClient);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        String sqlForClient = "DROP TABLE IF EXISTS "+TABLE_Particular_Client;

        db.execSQL(sqlForClient);

    }


    // Particular Client data
    public boolean insertClientData(final ClientsModel clientsModel) {

        final SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_clientId, clientsModel.getClientId());
        contentValues.put(Col_client_Name, clientsModel.getClient_Name());
        contentValues.put(Col_state, clientsModel.getState());
        contentValues.put(Col_stateId, clientsModel.getStateId());
        contentValues.put(Col_clientLogoPath, clientsModel.getClientLogoPath());
        contentValues.put(Col_clientImagePath, clientsModel.getClientImagePath());
        contentValues.put(Col_timeStampForParticularClient, clientsModel.getTimeStampForParticularClient());
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
