package com.concon.talkabout.talkabout.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.concon.talkabout.talkabout.dataType.Rules;

import java.util.ArrayList;

/**
 * Created by Pablitoh on 27/05/2015.
 */
public class DbManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Embriagados.db";
    public static final int VERSION_NUMBER = 1;

    public DbManager(Context context)
    {
        super(context, DATABASE_NAME , null, VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table phrases " +
                        "(_id integer primary key, phrase text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS phrases");
        onCreate(db);
    }

    public boolean insertPhrase  (String text)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("phrase", text);

        db.insert("phrases", null, contentValues);
        return true;
    }
    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from phrases where _id="+id+"", null );
        return res;
    }
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "phrases");
        return numRows;
    }
    public Integer deletePhrase (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("phrases",
                "_id = ? ",
                new String[] { Integer.toString(id) });
    }
    public Integer updatePhrase(long id,String text)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("phrase", text);
        return db.update("phrases",contentValues,"_id=" + id,null);
    }
    public Cursor getAllPhrases()
    {
        ArrayList<Rules> array_list = new ArrayList<Rules>();
        Rules rule = new Rules();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select _id,phrase from phrases order by _id desc", null );

        return res;
    }

    public ArrayList<String> getAllPhrasesAsArray() {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select _id,phrase from phrases order by _id desc", null);

        res.moveToFirst();
        while (!res.isAfterLast()) {
            array_list.add(res.getString(1));
            res.moveToNext();
        }
        return array_list;
    }

}
