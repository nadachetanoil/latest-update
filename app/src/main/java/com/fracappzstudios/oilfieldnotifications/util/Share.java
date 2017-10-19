package com.fracappzstudios.oilfieldnotifications.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.fracappzstudios.oilfieldnotifications.model.Eventdetail;
import com.fracappzstudios.oilfieldnotifications.model.Groupdetail;
import com.fracappzstudios.oilfieldnotifications.model.PeopelDirdetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kathiriya Harshad on 6/15/2017.
 */

public class Share
{
    public static final String PACKAGE_NAME = "com.fracappzstudios.oilfieldnotifications";

    public static int screenWidth;
    public static int screenHeight;

    public static final String TEMP_PEOPLEDIR_IS_DELETE="temp";

    public static Map<String,Boolean> selectedMember = new HashMap<>();
    public static Groupdetail groupdetail = new Groupdetail();
    public static Eventdetail eventdetail = new Eventdetail();

    public static final class Font{
        public static Typeface regular ;
        public static Typeface bold ;
        public static Typeface thin ;
        public static Typeface thin_regular ;
        public static Typeface thin_medium ;
        public static Typeface thin_bold;
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static void hide_keyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
