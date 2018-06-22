package in.sabpaisa.droid.sabpaisa.AppDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Blob;

import in.sabpaisa.droid.sabpaisa.Model.AllTransactiongettersetter;

public class AppDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SpApp.db";
   // Column names for User profile
    public static final String TABLE_NAME = "UserInfo";
    public static final String Col_ID = "id";
    public static final String Col_NAME = "name";
    public static final String Col_EMAIL = "email";
    public static final String Col_ADDRESS = "address";
    public static final String Col_PhoneNo = "phoneNumber";
    public static final String Col_userAccessToken = "userAccessToken";

//Column names for Transaction history
    public static final String TABLE_NAME_TRANSACTION = "TransactionTable";
    public static final String Col_Id = "id";
    public static final String Col_TXN_ID = "Txn_Id";
    public static final String Col_TXN_DATE = "Txn_Date";
    public static final String Col_TXN_AMT = "Txn_Amt";
    public static final String Col_TXN_Status = "Txn_Status";
    public static final String Col_userAccessToken_Txn = "userAccessToken_Txn";


    public AppDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = " CREATE TABLE " + TABLE_NAME + "("
                +Col_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +Col_NAME+" TEXT,"
                +Col_EMAIL+" TEXT,"
                +Col_ADDRESS+" TEXT,"
                +Col_PhoneNo+" TEXT,"
                +Col_userAccessToken+" TEXT"
                +")";

        String sqlForTransaction = " CREATE TABLE " + TABLE_NAME_TRANSACTION + "("
                +Col_Id+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +Col_TXN_ID+" TEXT,"
                +Col_TXN_DATE+" TEXT,"
                +Col_TXN_AMT+" TEXT,"
                +Col_TXN_Status+" TEXT,"
                +Col_userAccessToken_Txn+" TEXT"
                +")";


        db.execSQL(sql);
        db.execSQL(sqlForTransaction);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        String sql = "DROP TABLE IF EXISTS "+TABLE_NAME;
        db.execSQL(sql);

        String sqlForTransaction = "DROP TABLE IF EXISTS "+TABLE_NAME_TRANSACTION;
        db.execSQL(sqlForTransaction);

        onCreate(db);

    }


    public boolean insertData (String name , String email , String address , String number , String userAccessToken){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_NAME,name);
        contentValues.put(Col_EMAIL,email);
        contentValues.put(Col_ADDRESS,address);
        contentValues.put(Col_PhoneNo,number);
        contentValues.put(Col_userAccessToken,userAccessToken);
        long result = db.insert(TABLE_NAME,null,contentValues);

        if (result == -1){
            return false;
        }else {
            return true;
        }

    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getParticularData(String userAccessToken1){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " +
                Col_userAccessToken + "=?",new String[]{userAccessToken1});
        return res;
    }


    public boolean updateNameStatus(String userAccessToken1, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_NAME,name);
        db.update(TABLE_NAME, contentValues, Col_userAccessToken + "=?" ,new String[]{userAccessToken1});
        db.close();
        return true;
    }

    public boolean updateEmailStatus(String userAccessToken1, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_EMAIL,email);
        db.update(TABLE_NAME, contentValues, Col_userAccessToken + "=?" ,new String[]{userAccessToken1});
        db.close();
        return true;
    }

    public boolean updateAddressStatus(String userAccessToken1, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_ADDRESS,address);
        db.update(TABLE_NAME, contentValues, Col_userAccessToken + "=?" ,new String[]{userAccessToken1});
        db.close();
        return true;
    }



    //For AllTransactionSummary
    public boolean insertDataForTransaction (AllTransactiongettersetter allTransactiongettersetter){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_TXN_ID,allTransactiongettersetter.getSpTranscationId());
        contentValues.put(Col_TXN_DATE,allTransactiongettersetter.getTranscationDate());
        contentValues.put(Col_TXN_AMT,allTransactiongettersetter.getPaidAmount());
        contentValues.put(Col_TXN_Status,allTransactiongettersetter.getPaymentStatus());
        contentValues.put(Col_userAccessToken_Txn,allTransactiongettersetter.getUserAcceessToken());
        long result = db.insert(TABLE_NAME_TRANSACTION,null,contentValues);

        if (result == -1){
            return false;
        }else {
            return true;
        }

    }


    public Cursor getTransactionData(String userAccessTokenForTxn){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME_TRANSACTION + " WHERE " +
                Col_userAccessToken_Txn + "=?",new String[]{userAccessTokenForTxn});
        return res;
    }

    public void deleteAllTransactionData(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from "+ TABLE_NAME_TRANSACTION);
        db.close();
    }

//Comment
//Comment


}
