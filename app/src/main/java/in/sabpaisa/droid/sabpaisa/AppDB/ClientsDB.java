package in.sabpaisa.droid.sabpaisa.AppDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import in.sabpaisa.droid.sabpaisa.Model.ClientsDataModel;
import in.sabpaisa.droid.sabpaisa.Model.ClientsModel;
import in.sabpaisa.droid.sabpaisa.Model.ParticularClientModelForOffline;

public class ClientsDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ClientsDB.db";
    long result;

    // Columns names for Clients
    public static final String TABLE_Client = "Clients";
    public static final String Col_ID_AutoIncrement = "id";
    public static final String Col_clientId = "clientId";
    public static final String Col_client_Name = "clientName";
    public static final String Col_clientImagePath = "clientImagePath";
    public static final String Col_userAccesToken = "userAccesToken";
    public static final String Col_User_UIN = "userUIN";
    public static final String Col_COB_CUST_ID = "cobCustId";



    public ClientsDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlForClient = " CREATE TABLE " + TABLE_Client + "("
                +Col_ID_AutoIncrement+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +Col_clientId+" TEXT,"
                +Col_client_Name+" TEXT,"
                +Col_clientImagePath+" TEXT,"
                +Col_userAccesToken+" TEXT,"
                +Col_User_UIN+" TEXT,"
                +Col_COB_CUST_ID+" TEXT"
                +")";



        db.execSQL(sqlForClient);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        String sqlForClient = "DROP TABLE IF EXISTS "+TABLE_Client;

        db.execSQL(sqlForClient);

    }


    // Particular Client data
    public boolean insertClientData(final ClientsDataModel clientsDataModel) {

        final SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_clientId, clientsDataModel.getClientId());
        contentValues.put(Col_client_Name, clientsDataModel.getClientName());
        Log.d("ClientsDb", "Imgpath  " + clientsDataModel.getClientImageUrl());
        contentValues.put(Col_clientImagePath, clientsDataModel.getClientImageUrl());
        contentValues.put(Col_userAccesToken, clientsDataModel.getUserAccessToken());
        contentValues.put(Col_User_UIN, clientsDataModel.getUinNo());
        contentValues.put(Col_COB_CUST_ID, clientsDataModel.getCobCustId());
        result = db.insert(TABLE_Client, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }


    public Cursor getParticularClientData(String userAccesToken){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_Client + " WHERE " +
                Col_userAccesToken + "=?",new String[]{userAccesToken});
        return res;
    }


    public Cursor getParticularClientData1(String clientId , String userAccesstoken){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT " +Col_clientId +", "+ Col_userAccesToken+ " FROM "+ TABLE_Client + " WHERE "+Col_clientId+"=?"+ " AND "+Col_userAccesToken+"=?",new String[]{clientId,userAccesstoken});
        return res;
    }





    public boolean updateParticularClientData(final String name , final String image , final String userAccessToken , final String uinNo ,final String cobCustId) {

        final SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_client_Name, name);
        contentValues.put(Col_clientImagePath, image);
        contentValues.put(Col_User_UIN, uinNo);
        contentValues.put(Col_COB_CUST_ID, cobCustId);

        int effectedRow = db.update(TABLE_Client, contentValues ,Col_userAccesToken + "=?",new String[]{userAccessToken});

        if (effectedRow > 0) {
            return true;
        } else {
            return false;
        }

    }


    public void deleteAllClientData(){
        SQLiteDatabase db = this.getWritableDatabase();

//        db.execSQL("delete from "+ TABLE_Particular_Client);
//        db.execSQL("delete from "+ TABLE_Particular_Client);
        Log.d("TABLE_Particular_Client","DELETE");
        db.delete(TABLE_Client, null, null);

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



}
