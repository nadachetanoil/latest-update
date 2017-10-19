package com.fracappzstudios.oilfieldnotifications.database;

import android.annotation.SuppressLint;
import android.util.Log;

import com.fracappzstudios.oilfieldnotifications.LoginScreenActivity;
import com.fracappzstudios.oilfieldnotifications.util.Share;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Kathiriya Harshad on 6/28/2017.
 */

public class importdatabase
{
    @SuppressLint("SdCardPath")
    @SuppressWarnings("unused")
    public static void copyContactDataBase()
    {
        try
        {
            Log.e("Database","copyContactDataBase");
            // create databaseOutputStream variable for open the default database
            OutputStream databaseOutputStream = new FileOutputStream("/data/data/"+ Share.PACKAGE_NAME+"/databases/"+DatabaseHandler.DATABASE_NAME);
            //declare databaseInputStream variable use for copy data
            InputStream databaseInputStream;
            //default size of the buffer
            byte[] buffer = new byte[1024];
            //declare length variable for databaseInputStream length
            int length;
            //assign the object  of splash activity shared by importdatabase activity
            databaseInputStream = LoginScreenActivity.databaseInputStreamContact;

            //start copy data
            while ((length = databaseInputStream.read(buffer)) > 0)
            {
                databaseOutputStream.write(buffer);
            }
            databaseInputStream.close();
            databaseOutputStream.flush();
            databaseOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
