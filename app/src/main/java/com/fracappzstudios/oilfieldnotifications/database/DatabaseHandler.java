package com.fracappzstudios.oilfieldnotifications.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.fracappzstudios.oilfieldnotifications.LoginScreenActivity;
import com.fracappzstudios.oilfieldnotifications.model.PeopelDirdetail;
import com.fracappzstudios.oilfieldnotifications.util.Share;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kathiriya Harshad on 6/28/2017.
 */

public class DatabaseHandler {
    private static final String TAG = DatabaseHandler.class.getSimpleName();
    // Database Version
    public static final int DATABASE_VERSION = 1;
    // Database Name
    public static final String DATABASE_NAME = "OilsField.sql";

    private static final String TABLE_PEOPLEDIR = "peopledir";

    public static final String KEY_ID="id";
    public static final String KEY_MEMBER_ID="member_id";
    public static final String KEY_USER_ID="user_id";
    public static final String KEY_FIRST_NAME="first_name";
    public static final String KEY_LAST_NAME="last_name";
    public static final String KEY_EMAIL="email";
    public static final String KEY_PROFILE_IMAGE="profile_image";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_COMPANY_NAME = "company_name";
    public static final String KEY_IS_DELETE="is_delete";

    private static SQLiteDatabase db;
    private Context context;
    private DBHelper dbHelper;

    private static class DBHelper extends SQLiteOpenHelper {
        DBHelper(Context context)
        {
            super(context,DATABASE_NAME, null,DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase _db) {
        }
        @Override
        public void onUpgrade(SQLiteDatabase _db, int _oldVersion,int _newVersion) {
            onCreate(_db);
        }
    }

    public DatabaseHandler(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
    }
    public DatabaseHandler open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }


    public void addPeopelDir(PeopelDirdetail peopelDirdetail,String user_id)
    {
        Log.e(TAG,""+peopelDirdetail.toString());
        if(isExitsPeopleDir(user_id,peopelDirdetail.getMemberId()))
        {
            Log.e(TAG,"Updae PeopelDir :==========***> "+user_id);
            ContentValues contentValues = new ContentValues();

            contentValues.put(KEY_FIRST_NAME,""+peopelDirdetail.getFirstName());
            contentValues.put(KEY_LAST_NAME,""+peopelDirdetail.getLastName());
            contentValues.put(KEY_EMAIL,""+peopelDirdetail.getEmail());
            contentValues.put(KEY_PROFILE_IMAGE,""+peopelDirdetail.getProfileImage());
            contentValues.put(KEY_PHONE,""+peopelDirdetail.getPhone());
            contentValues.put(KEY_COMPANY_NAME,""+peopelDirdetail.getCompanyName());
            contentValues.put(KEY_IS_DELETE,peopelDirdetail.getIs_delete());

            db.update(TABLE_PEOPLEDIR, contentValues, KEY_USER_ID + " = ? and "+KEY_MEMBER_ID+" = ?", new String[]{""+user_id,peopelDirdetail.getMemberId()});
//            Log.e(TAG,"updateReminder :~>"+peopelDirdetail.getMemberId());
        }
        else
        {
            String query = "insert into "+TABLE_PEOPLEDIR+"(user_id,member_id,first_name,last_name,email,profile_image,phone,company_name,is_delete) values " +
                    "("+user_id+",'"+
                    peopelDirdetail.getMemberId()+"','"
                    +peopelDirdetail.getFirstName()+"','"
                    +peopelDirdetail.getLastName()+"','"
                    +peopelDirdetail.getEmail()+"','"
                    +peopelDirdetail.getProfileImage()+"','"
                    +peopelDirdetail.getPhone()+"','"
                    +peopelDirdetail.getCompanyName()+"','"+peopelDirdetail.getIs_delete()+"')";
//            Log.e(TAG,"addPeopelDir :==========> "+query);
            db.execSQL(query);
        }
    }

    public boolean removePeopleDir(String id)
    {
        return db.delete(TABLE_PEOPLEDIR, KEY_ID + "=" + id, null) > 0;
    }
    public boolean removeTempPeopleDir(String user_id)
    {
        return db.delete(TABLE_PEOPLEDIR, KEY_IS_DELETE + " = '"+Share.TEMP_PEOPLEDIR_IS_DELETE+"' and "+ KEY_USER_ID + " ='" + user_id +"'", null) > 0;
    }
    public boolean isExitsPeopleDir(String user_id,String member_id)
    {
        String query = "Select * FROM " + TABLE_PEOPLEDIR + " WHERE " + KEY_USER_ID + " ='" + user_id +"' and member_id='"+member_id+"'";
//        Log.d(TAG,"isExitsPeopleDir query :-> "+query);

        Cursor cursor = db.rawQuery(query, null);
//        Log.e(TAG,"exitsReminder :~> "+cursor.getCount());
        if (cursor.moveToFirst())
        {
            cursor.close();
            return  true;
        }
        else
        {
            cursor.close();
            return false;
        }
    }

    public List<PeopelDirdetail> getAllPeopleDir(String user_id)
    {
        List<PeopelDirdetail> peopelDirdetailList = new ArrayList<>();

//        String query = "select * from "+TABLE_PEOPLEDIR+" where user_id = '"+user_id+"' and is_delete <> '"+ Share.TEMP_PEOPLEDIR_IS_DELETE+"' ORDER BY id DESC";
//        Log.e(TAG,"getAllPeopleDir query :-> "+query);
        String query = "select * from "+TABLE_PEOPLEDIR+" where user_id = '"+user_id+"' ORDER BY id DESC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount()>0)
        {
            while (cursor.moveToNext())
            {
                PeopelDirdetail peopelDirdetail = new PeopelDirdetail();
                peopelDirdetail.setId(""+cursor.getInt(0));
                peopelDirdetail.setUser_id(""+cursor.getString(1));
                peopelDirdetail.setMemberId(""+cursor.getString(2));
                peopelDirdetail.setFirstName(""+cursor.getString(3));
                peopelDirdetail.setLastName(""+cursor.getString(4));
                peopelDirdetail.setEmail(""+cursor.getString(5));
                peopelDirdetail.setProfileImage(cursor.getString(6));
                peopelDirdetail.setPhone(cursor.getString(7));
                peopelDirdetail.setCompanyName(cursor.getString(8));
                peopelDirdetail.setIs_delete(cursor.getString(9));
                peopelDirdetailList.add(peopelDirdetail);
//                Log.e(TAG,""+peopelDirdetail.toString());
            }
        }
        cursor.close();
        return peopelDirdetailList;
    }

    public Cursor getAllPeople(String user_id)
    {
        String query = "select * from "+TABLE_PEOPLEDIR+" where user_id = '"+user_id+"' ORDER BY id DESC";
//        Log.e(TAG,"getAllPeopleDir query :-> "+query);

        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }
}
