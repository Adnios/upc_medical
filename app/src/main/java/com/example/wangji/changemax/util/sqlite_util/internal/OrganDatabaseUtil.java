package com.example.wangji.changemax.util.sqlite_util.internal;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wangji.changemax.model.internal.Organ;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by WangJi.
 */

public class OrganDatabaseUtil {

    private static final String DB_NAME = "medical_library.db";// 数据库名称
    private static final String TABLE_NAME = "t_organ";// 数据表名称
    private static final int DB_VERSION = 1;// 数据库版本

    // 表的字段名
    private static final String KEY_ID = "organ_id";
    private static final String KEY_NAME = "organ_name";
    private static final String KEY_OWN_PART = "organ_own_part";

    private SQLiteDatabase mDatabase;
    private Context mContext;
    private OrganDBOpenHelper mDbOpenHelper;//数据库打开帮助类

    public OrganDatabaseUtil(Context context) {
        mContext = context;
    }

    // 打开数据库
    public void openDataBase() {
        mDbOpenHelper = new OrganDBOpenHelper(mContext, DB_NAME, null, DB_VERSION);
        try {
            mDatabase = mDbOpenHelper.getWritableDatabase();// 获取可写数据库
        } catch (SQLException e) {
            mDatabase = mDbOpenHelper.getReadableDatabase();// 获取只读数据库
        }

        //BuildingLibrary(mDatabase);//建表
    }

    // 关闭数据库
    public void closeDataBase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    // 插入一条数据
    public long insertData(Organ organ) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, organ.getOrgan_name());
        values.put(KEY_OWN_PART, organ.getOrgan_own_part());

        return mDatabase.insert(TABLE_NAME, null, values);
    }


    // 删除一条数据
    public int deleteData(int id) {
        return mDatabase.delete(TABLE_NAME, KEY_ID + "=" + id, null);
    }

    // 更新一条数据
    public long updateData(int id, Organ organ) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, organ.getOrgan_name());
        values.put(KEY_OWN_PART, organ.getOrgan_own_part());

        return mDatabase.update(TABLE_NAME, values, KEY_ID + "=" + id, null);
    }

    // 查询一条数据
    public List<Organ> queryData(String key, String keyContent) {
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{KEY_ID, KEY_NAME, KEY_OWN_PART},
                key + "=" + keyContent,
                null, null, null, null);
        return convertToOrgan(results);
    }


    // 查询所有数据
    public List<Organ> queryDataList() {
        Cursor results = mDatabase.query(TABLE_NAME, new String[]{KEY_ID, KEY_NAME, KEY_OWN_PART},
                null, null, null, null, null);
        return convertToOrgan(results);
    }

    //将查询到的数据存储到List中return；
    private List<Organ> convertToOrgan(Cursor cursor) {
        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()) {
            return null;
        }
        List<Organ> mOrganList = new ArrayList<Organ>();
        for (int i = 0; i < resultCounts; i++) {
            Organ organ = new Organ();
            //存储student中的id,sId,name,cResult,mResult,eResutl
            organ.setOrgan_id(cursor.getInt(0));
            organ.setOrgan_name(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            organ.setOrgan_own_part(cursor.getString(cursor.getColumnIndex(KEY_OWN_PART)));

            //在list中添加organ对象
            mOrganList.add(organ);
            cursor.moveToNext();
        }
        //return；
        return mOrganList;
    }


    /**
     * 数据表打开帮助类
     */
    private static class OrganDBOpenHelper extends SQLiteOpenHelper {

        public OrganDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            final String sqlStr = "create table if not exists " + TABLE_NAME
                    + " ("
                    + KEY_ID + " integer primary key autoincrement, "
                    + KEY_NAME + " text not null, "
                    + KEY_OWN_PART + " text not null "
                    + ");";
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            final String sqlStr = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(sqlStr);
            onCreate(db);
        }
    }
}
