package com.example.liuhaifeng.leanclouddemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by liuhaifeng on 2017/1/20.
 */

public class FriendOpenHelper extends SQLiteOpenHelper {
    public FriendOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "friend", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE friend(owenr VARCHAR(20),name  VARCHAR(20) ,userId VARCHAR(20),avatarUrl VARCHAR(50),fg VARCHAR(20))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
