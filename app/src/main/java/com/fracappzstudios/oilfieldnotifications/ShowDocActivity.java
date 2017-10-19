package com.fracappzstudios.oilfieldnotifications;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.fracappzstudios.oilfieldnotifications.util.Share;
import com.fracappzstudios.oilfieldnotifications.util.Urls;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Kathiriya Harshad on 6/28/2017.
 */


public class ShowDocActivity extends AppCompatActivity {

    private static final String TAG = ShowDocActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.web_view)
    WebView web_view;

    String docname;
    Uri picUri;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_doc);
        ButterKnife.bind(this);
        initView();
    }

    private void initView()
    {
        try
        {
            setSupportActionBar(toolbar);
            permissions.add(WRITE_EXTERNAL_STORAGE);
            permissionsToRequest = findUnAskedPermissions(permissions);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0)
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }

            if(getIntent().getExtras().containsKey("docname"))
            {
                docname = getIntent().getExtras().getString("docname");

                Log.e(TAG,"File Path :- "+ Urls.Event_FILE_URL+docname);

                web_view.setWebViewClient(new AppWebViewClients());
                web_view.getSettings().setJavaScriptEnabled(true);
                web_view.getSettings().setUseWideViewPort(false);
                web_view.getSettings().setBuiltInZoomControls(true);
                web_view.getSettings().setSupportZoom(true);
                web_view.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url="+ Urls.Event_FILE_URL+docname);

            }
            else
                Toast.makeText(this,"Document not found.",Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){e.printStackTrace();}
    }
    @OnClick(R.id.iv_back)
    public void onClickMyGroup(View view)
    {
        finish();
    }

    @OnClick(R.id.iv_download)
    public void onClickDownload(View view)
    {
        try
        {
            String servicestring = Context.DOWNLOAD_SERVICE;
            DownloadManager downloadmanager;
            downloadmanager = (DownloadManager) getSystemService(servicestring);
            Uri uri = Uri.parse(Urls.Event_FILE_URL+docname);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,docname.substring(15,docname.length()));
            Long reference = downloadmanager.enqueue(request);
            Toast.makeText(this,"Start Downloading...",Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){e.printStackTrace();}
    }


    public class AppWebViewClients extends WebViewClient {

        ProgressDialog pd =new ProgressDialog(ShowDocActivity.this);
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            pd.show();
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            pd.cancel();
        }
    }

    /*-------------------------------Permission -------------------------------*/
    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (hasPermission(perms)) {

                    } else {

                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                //Log.d("API123", "permisionrejected " + permissionsRejected.size());
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }
}
