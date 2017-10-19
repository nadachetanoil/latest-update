package com.fracappzstudios.oilfieldnotifications;

import android.graphics.Typeface;

import com.fracappzstudios.oilfieldnotifications.util.Share;

/**
 * Created by Kathiriya Harshad on 6/22/2017.
 */

public final class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Share.Font.bold = Typeface.createFromAsset(getApplicationContext().getAssets(),"font/Titillium_Bold.otf");
        Share.Font.regular = Typeface.createFromAsset(getApplicationContext().getAssets(),"font/Titillium_Regular.otf");
        Share.Font.thin = Typeface.createFromAsset(getApplicationContext().getAssets(),"font/Titillium_Thin.otf");
        Share.Font.thin_bold = Typeface.createFromAsset(getApplicationContext().getAssets(),"font/Titillium_Thin_Bold.otf");
        Share.Font.thin_medium = Typeface.createFromAsset(getApplicationContext().getAssets(),"font/Titillium_Thin_Medium.otf");
        Share.Font.thin_regular = Typeface.createFromAsset(getApplicationContext().getAssets(),"font/Titillium_Thin_Regular.otf");
    }
}
