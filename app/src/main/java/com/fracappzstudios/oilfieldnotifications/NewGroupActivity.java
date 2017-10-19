package com.fracappzstudios.oilfieldnotifications;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fracappzstudios.oilfieldnotifications.model.PeopelDirdetail;
import com.fracappzstudios.oilfieldnotifications.util.NetworkManager;
import com.fracappzstudios.oilfieldnotifications.util.Share;
import com.fracappzstudios.oilfieldnotifications.util.SharedPrefs;
import com.fracappzstudios.oilfieldnotifications.util.Urls;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiClient;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiService;
import com.fracappzstudios.oilfieldnotifications.webservice.response.NewGroupRrespone;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Kathiriya Harshad on 6/28/2017.
 */

public class NewGroupActivity extends AppCompatActivity
{
    private static final String TAG = NewGroupActivity.class.getSimpleName();

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.iv_back)ImageView iv_back;
    @BindView(R.id.iv_group)ImageView iv_group;
    @BindView(R.id.et_group_name)EditText et_group_name;
    @BindView(R.id.et_group_description)EditText et_group_description;
    @BindView(R.id.btn_add_member)Button btn_add_member;
    @BindView(R.id.tv_create)TextView tv_create;

    //pickup image
    Bitmap myBitmap;
    Uri picUri;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;

    private final static int SELECT_MEMBER_RESULT=250;
    private final static int SELECT_IMAGE=200;

    String user_list="",user_id="",GroupId="",is_update="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initView();
        setFont();
    }

    private void setFont()
    {
        et_group_description.setTypeface(Share.Font.regular);
        et_group_name.setTypeface(Share.Font.regular);
        tv_create.setTypeface(Share.Font.thin_bold);
    }

    private void initView()
    {
        try
        {
            user_id = SharedPrefs.getString(this,SharedPrefs.USER_ID);
            permissions.add(CAMERA);
            permissions.add(WRITE_EXTERNAL_STORAGE);
            permissionsToRequest = findUnAskedPermissions(permissions);
            //get the permissions we have asked for before but are not granted..
            //we will store this in a global list to access later.


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0)
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }

            is_update="";
            et_group_description.setHorizontallyScrolling(false);
            if(getIntent().getExtras() != null && getIntent().getExtras().containsKey("update"))
            {
                is_update="update";
                GroupId = Share.groupdetail.getGroupId();

                et_group_name.setText(Share.groupdetail.getGroupName());
                et_group_description.setText(Share.groupdetail.getGroupDescription());
                Picasso.with(this)
                        .load(Urls.GROUP_IMAGE_URL+Share.groupdetail.getGroupImage())
                        .placeholder(R.drawable.ic_group)
                        .error(R.drawable.ic_group)
                        .into(iv_group);
                tv_create.setText("Update");

                user_list = getIntent().getStringExtra("user_list");
                Log.e(TAG,"Update =========================>"+Share.selectedMember.toString());
            }
            else {
                Share.selectedMember.clear();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.iv_back)
    public void onClickMyGroup(View view)
    {
        finish();
    }

    @OnClick(R.id.btn_add_member)
    public void onClickSelectMember(View view)
    {
        try
        {
            Intent selectMemberIntent = new Intent(this,SelectMemberActivity.class);
            selectMemberIntent.putExtra("is_update","update");
            startActivityForResult(selectMemberIntent,SELECT_MEMBER_RESULT);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.tv_create)
    public void onClickCreateGroup(View view)
    {
        try
        {
            if(validation())
            {
                Log.e(TAG,"GroupId :-> "+GroupId);
                Log.e(TAG,"user_id :-> "+user_id);
                Log.e(TAG,"group_name :-> "+et_group_name.getText().toString());
                Log.e(TAG,"group_description :-> "+et_group_description.getText().toString());
                Log.e(TAG,"group_image :-> "+(picUri != null?picUri.getPath():""));
                Log.e(TAG,"user_list :-> "+user_list);

                if(tv_create.getText().toString().equals("Update"))
                {
                    GroupId = Share.groupdetail.getGroupId();
                }
                else
                    GroupId = random();

                if(NetworkManager.isInternetConnected(this))
                    createNewGroup();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void onClickSelectGroupImage(View view)
    {
        startActivityForResult(getPickImageChooserIntent(), SELECT_IMAGE);
    }

    public boolean validation()
    {
        boolean is_valid = true;

        if(et_group_name.getText().toString().trim().length() == 0)
        {
            is_valid = false;
            Toast.makeText(this,"Enter group name.",Toast.LENGTH_SHORT).show();
        }

        return is_valid;
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
//        int randomLength = generator.nextInt(32);
//        char tempChar;
//        for (int i = 0; i < randomLength; i++){
//            tempChar = (char) (generator.nextInt(96) + 32);
//            randomStringBuilder.append(tempChar);
//        }

        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 32; i++)
            sb.append(chars[rnd.nextInt(chars.length)]);

        return sb.toString();

//        return randomStringBuilder.toString();
    }


    /*------------------------------------- Pickup image from camera or gallery -------------------------*/
    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);
        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
        return chooserIntent;
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_IMAGE)
        {
            Bitmap bitmap;
            if (getPickImageResultUri(data) != null) {
                picUri = getPickImageResultUri(data);

                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);
                    myBitmap = rotateImageIfRequired(myBitmap, picUri);
//                    myBitmap = getResizedBitmap(myBitmap,imgUserWidth,imgUserHeight);
                    iv_group.setImageBitmap(myBitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                bitmap = (Bitmap) data.getExtras().get("data");
                myBitmap = bitmap;
//                myBitmap = getResizedBitmap(myBitmap,imgUserWidth,imgUserHeight);
                iv_group.setImageBitmap(myBitmap);
            }
        }
        else if(requestCode == SELECT_MEMBER_RESULT && data.getExtras() != null)
        {
            user_list = data.getExtras().getString("user_list");
            Log.e(TAG,"member_ids :-> "+user_list);
        }
    }

    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }


        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("pic_uri", picUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        picUri = savedInstanceState.getParcelable("pic_uri");
    }

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
    private String getRealPathFromURI(Uri contentURI) {
        String result="";
            try
            {
                Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
                if (cursor == null) { // Source is Dropbox or other similar local file path
                    result = contentURI.getPath();
                } else {
                    cursor.moveToFirst();
                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    result = cursor.getString(idx);
                    cursor.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        return result;
    }
    /*-------------------------------WebServices -----------------------------------*/
    private void createNewGroup()
    {
        try
        {
            final ProgressDialog pd = ProgressDialog.show(this,"","Loading...",true,false);
            pd.show();
            RequestBody rGroupId =RequestBody.create(okhttp3.MultipartBody.FORM,GroupId);
            RequestBody ruser_id =RequestBody.create(okhttp3.MultipartBody.FORM,user_id);
            RequestBody ruser_list =RequestBody.create(okhttp3.MultipartBody.FORM,user_list);
            RequestBody rgroup_name =RequestBody.create(okhttp3.MultipartBody.FORM,""+et_group_name.getText().toString());
            RequestBody rgroup_description =RequestBody.create(okhttp3.MultipartBody.FORM,""+ et_group_description.getText().toString());

            Call<NewGroupRrespone> newGroupRresponeCall;
            if(picUri !=null && picUri.getPath().length() !=0)
            {
                File file = new File(getRealPathFromURI(picUri));
                Log.e(TAG,""+file.getAbsolutePath());
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("group_image", file.getName(), requestFile);
                newGroupRresponeCall = ApiClient.getClient().create(ApiService.class).createNewGroup(rGroupId,ruser_id,rgroup_name,rgroup_description,ruser_list,body);
            }
            else
                newGroupRresponeCall = ApiClient.getClient().create(ApiService.class).createNewGroup(rGroupId,ruser_id,rgroup_name,rgroup_description,ruser_list,null);

            newGroupRresponeCall.enqueue(new Callback<NewGroupRrespone>() {
                @Override
                public void onResponse(Call<NewGroupRrespone> call, final Response<NewGroupRrespone> response) {
                    try
                    {
                        pd.cancel();
                        if(response.body().getNewGroupuberAlpha().getStatus().equals("success"))
                        {
                            Share.selectedMember.clear();
                            Toast.makeText(NewGroupActivity.this,""+response.body().getNewGroupuberAlpha().getMessage(),Toast.LENGTH_SHORT).show();

                            final Dialog dialog = new Dialog(NewGroupActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            dialog.setContentView(R.layout.dlg_group);
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            dialog.setCancelable(true);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();

                            dialog.findViewById(R.id.btn_cancel).setVisibility(View.GONE);
                            ImageView fab = (ImageView)dialog.findViewById(R.id.fab);
                            fab.setImageResource(R.drawable.ic_hand_circle);

                            TextView tv_msg = (TextView)dialog.findViewById(R.id.tv_msg);
                            tv_msg.setText("Success!");
                            tv_msg.setTypeface(Share.Font.bold);

                            TextView tv_msg_1 = (TextView)dialog.findViewById(R.id.tv_msg_1);
                            tv_msg_1.setText(""+response.body().getNewGroupuberAlpha().getMessage());
                            tv_msg_1.setTypeface(Share.Font.regular);

                            Button btn_ok = (Button)dialog.findViewById(R.id.btn_ok);
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {
                                    dialog.cancel();
                                    Share.groupdetail = response.body().getNewGroupuberAlpha().getNewGroupdetails();
                                    finish();
                                }
                            });

                        }
                        else {
                            Toast.makeText(NewGroupActivity.this,""+response.body().getNewGroupuberAlpha().getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<NewGroupRrespone> call, Throwable t) {
                    pd.cancel();
                    t.printStackTrace();
                }
            });
            pd.cancel();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
