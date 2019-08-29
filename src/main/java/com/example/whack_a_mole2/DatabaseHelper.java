package com.example.whack_a_mole2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "records_table";
    private static final String NAME = "NAME";
    private static final String SCORE = "SCORE";
    private static final String MISS = "MISS";
    private static final String BOMBS = "BOMBS";
    private static final String SECONDS = "SCONDS";
    private static final int LIMIT_NUMBER = 10;


    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME
                +"( ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,"
                +NAME +" TEXT,"
                +SCORE + " INTEGER,"
                +MISS + " INTEGER,"
                +BOMBS + " INTEGER,"
                +SECONDS + " INTEGER"
                +" )";

        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean addRecord(Record record){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME,record.getName());
        values.put(SCORE,record.getScore());
        values.put(MISS,record.getMiss());
        values.put(BOMBS,record.getBombs());
        values.put(SECONDS,(int)record.getSeconds()/1000);

        long rowId = db.insert(TABLE_NAME,null,values);
        db.close();

        return rowId != -1 ? true : false ;

    }

    public Cursor getRecords(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME +" ORDER BY "+SCORE+" DESC ";
        Cursor data = db.rawQuery(query,null);
        return data;
    }

    public void keepOnly10Best(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE ID "+" NOT IN " +
                "    ( SELECT ID FROM "+TABLE_NAME+" ORDER BY "+SCORE+" DESC LIMIT "+LIMIT_NUMBER+" )" );
        db.close();
    }

    public void cleanDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM "+TABLE_NAME;
        db.execSQL(query);
    }
}
