package com.example.diaryapp

import android.content.ContentValues.TAG
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

//DB 생성 클래스
class myDBHelper (context: Context, DBName : String) : SQLiteOpenHelper(context, DBName, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE TodoTBL (TodoContent CHAR(50) PRIMARY KEY)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS TodoTBL")
        onCreate(db)
    }
}