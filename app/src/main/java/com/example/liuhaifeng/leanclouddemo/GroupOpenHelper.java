package com.example.liuhaifeng.leanclouddemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by liuhaifeng on 2016/12/22.
 */

public class GroupOpenHelper extends SQLiteOpenHelper {

    public GroupOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "db", null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
                db.execSQL("CREATE TABLE groups(id INTEGER PRIMARY KEY AUTOINCREMENT,groupname VARCHAR(20),number INTEGER,name VARCHAR(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
