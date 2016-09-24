package com.ysy.student.db;

import com.ysy.student.entity.TableContanst;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StudentDBHelper extends SQLiteOpenHelper {

	private static final String TAG = "StudentDBHelper";

	public static final String DB_NAME = "student_manager.db";
	public static final int VERSION = 1;

	public StudentDBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);

	}

	public StudentDBHelper(Context context) {
		this(context, DB_NAME, null, VERSION);
	}

	@Override
	//
	public void onCreate(SQLiteDatabase db) {
		
	
		Log.v(TAG, "onCreate");
		db.execSQL("create table "
				+ TableContanst.STUDENT_TABLE
				+ "(_id Integer primary key AUTOINCREMENT,"
				+ "name char,apartment char, sex char, classes char, phone_number char,birthday date, "
				+ "modify_time DATETIME)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.v(TAG, "onUpgrade");
	}

}
