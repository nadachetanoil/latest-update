package com.fracappzstudios.oilfieldnotifications.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Kathiriya Harshad on 6/28/2017.
 */

//SharedPreferences manager class
public class SharedPrefs {

    //SharedPreferences file name
    private static String SHARED_PREFS_FILE_NAME = "oilfield_shared_prefs";

    //here you can centralize all your shared prefs keys
    public static final String IS_REMEMBER="is_remeber";
    public static final String DEVICE_TOKEN="device_token";
    public static final String USER_ID = "user_id";
    public static final String FIRST_NAME="first_name";
    public static final String LAST_NAME="last_name";
    public static final String EMAIL="email";
    public static final String PHONE_NO="phone_no";
    public static final String COMPANY_NAME="company_name";
    public static final String SUPER_USER="super_user"; //Yes or No
    public static final String USERNAME = "username";
    public static final String PASSWORD="password";
    public static final String PROFILE_IMG="profile_image";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }
    public static boolean contain(Context context, String key) {
        return getPrefs(context).contains(key);
    }
    public static void clearPrefs(Context context) {
        String device_id = getString(context,DEVICE_TOKEN);
        String userName = SharedPrefs.getString(context, USERNAME);
        String pwd = SharedPrefs.getString(context,PASSWORD);
        getPrefs(context).edit().clear().commit();
        save(context,DEVICE_TOKEN,device_id);
        save(context,USERNAME,userName);
        save(context,PASSWORD,pwd);
        save(context,IS_REMEMBER,"true");
    }

    //Save Booleans
    public static void save(Context context, String key, boolean value) {
        getPrefs(context).edit().putBoolean(key, value).commit();
    }

    //Get Booleans
    public static boolean getBoolean(Context context, String key) {
        return getPrefs(context).getBoolean(key, false);
    }

    //Get Booleans if not found return a predefined default value
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getPrefs(context).getBoolean(key, defaultValue);
    }

    //Strings
    public static void save(Context context, String key, String value) {
        getPrefs(context).edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key) {
        return getPrefs(context).getString(key, "");
    }

    public static String getString(Context context, String key, String defaultValue) {
        return getPrefs(context).getString(key, defaultValue);
    }

    //Integers
    public static void save(Context context, String key, int value) {
        getPrefs(context).edit().putInt(key, value).commit();
    }

    public static int getInt(Context context, String key) {
        return getPrefs(context).getInt(key, 0);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return getPrefs(context).getInt(key, defaultValue);
    }

    //Floats
    public static void save(Context context, String key, float value) {
        getPrefs(context).edit().putFloat(key, value).commit();
    }

    public static float getFloat(Context context, String key) {
        return getPrefs(context).getFloat(key, 0);
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        return getPrefs(context).getFloat(key, defaultValue);
    }

    //Longs
    public static void save(Context context, String key, long value) {
        getPrefs(context).edit().putLong(key, value).commit();
    }

    public static long getLong(Context context, String key) {
        return getPrefs(context).getLong(key, 0);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        return getPrefs(context).getLong(key, defaultValue);
    }
}