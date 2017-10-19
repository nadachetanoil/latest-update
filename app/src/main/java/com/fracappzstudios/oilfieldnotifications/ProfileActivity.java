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
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
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
import com.fracappzstudios.oilfieldnotifications.model.Profiledetails;
import com.fracappzstudios.oilfieldnotifications.util.NetworkManager;
import com.fracappzstudios.oilfieldnotifications.util.Share;
import com.fracappzstudios.oilfieldnotifications.util.SharedPrefs;
import com.fracappzstudios.oilfieldnotifications.util.Urls;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiClient;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiService;
import com.fracappzstudios.oilfieldnotifications.webservice.response.NewGroupRrespone;
import com.fracappzstudios.oilfieldnotifications.webservice.response.UpdateProfileRrespone;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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


public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = ProfileActivity.class.getSimpleName();

    @BindView(R.id.iv_back)ImageView iv_back;
    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.et_email) EditText et_email;
    @BindView(R.id.et_first_name) EditText et_first_name;
    @BindView(R.id.et_last_name) EditText et_last_name;
    @BindView(R.id.et_company_name)EditText et_company_name;
    @BindView(R.id.et_phone_no)EditText et_phone_no;
    @BindView(R.id.et_password)EditText et_password;
    @BindView(R.id.et_confirm_password)EditText et_confirm_password;
    @BindView(R.id.btn_update)Button btn_update;
    @BindView(R.id.iv_user_profile)ImageView iv_user_profile;

    @BindView(R.id.v_line_p)View v_line_p;

    //pickup image
    Bitmap myBitmap;
    Uri picUri;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    PeopelDirdetail peopelDirdetail;
    String profileImage="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        iniView();
    }
    private void iniView()
    {
        try
        {
            setSupportActionBar(toolbar);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            Share.screenHeight = displayMetrics.heightPixels;
            Share.screenWidth = displayMetrics.widthPixels;

            if(getIntent().getExtras() != null && getIntent().getExtras().containsKey("peopelDirdetail"))
            {
                peopelDirdetail = getIntent().getExtras().getParcelable("peopelDirdetail");
                et_email.setText(peopelDirdetail.getEmail());
                et_first_name.setText(peopelDirdetail.getFirstName());
                et_last_name.setText(peopelDirdetail.getLastName());
                et_company_name.setText(peopelDirdetail.getCompanyName());
                et_phone_no.setText(peopelDirdetail.getPhone());
                et_password.setVisibility(View.GONE);
                et_confirm_password.setVisibility(View.GONE);
                btn_update.setVisibility(View.GONE);

                et_email.setEnabled(false);
                et_first_name.setEnabled(false);
                et_last_name.setEnabled(false);
                et_company_name.setEnabled(false);
                et_phone_no.setEnabled(false);
                v_line_p.setVisibility(View.GONE);

                profileImage = peopelDirdetail.getProfileImage();
                Log.e(TAG,"profile_image ===================> "+peopelDirdetail.getProfileImage());

            }
            else {
                et_email.setText(SharedPrefs.getString(this, SharedPrefs.EMAIL));
                et_first_name.setText(SharedPrefs.getString(this, SharedPrefs.FIRST_NAME));
                et_last_name.setText(SharedPrefs.getString(this,SharedPrefs.LAST_NAME));
                et_company_name.setText(SharedPrefs.getString(this,SharedPrefs.COMPANY_NAME));
                et_phone_no.setText(SharedPrefs.getString(this, SharedPrefs.PHONE_NO));
                iv_user_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(getPickImageChooserIntent(), 200);
                    }
                });
                permissions.add(CAMERA);
                permissions.add(WRITE_EXTERNAL_STORAGE);
                permissionsToRequest = findUnAskedPermissions(permissions);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (permissionsToRequest.size() > 0)
                        requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
                }
                profileImage = SharedPrefs.getString(this, SharedPrefs.PROFILE_IMG);
            }
            iv_user_profile.getLayoutParams().width = Share.screenWidth/2;
            iv_user_profile.getLayoutParams().height = Share.screenWidth/2;

            Picasso.with(this)
                    .load(Urls.PEOPLE_IMAGE_URL+profileImage)
                    .placeholder(R.drawable.ic_user_profile)
                    .resize(Share.screenWidth/2,Share.screenWidth/2)
                    .error(R.drawable.ic_user_profile)
                    .into(iv_user_profile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.iv_back)
    public void onClickBack(View view)
    {
        finish();
    }

    @OnClick(R.id.btn_update)
    public void onClickUpdateProfile(View view)
    {
        try
        {
            if(validation())
            {
                if(NetworkManager.isInternetConnected(this))
                    updateProfile();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private boolean validation()
    {
        boolean is_valid = true;
        try
        {
            if(et_email.getText().toString().trim().length() == 0)
            {
                Toast.makeText(this,"Enter email",Toast.LENGTH_SHORT).show();
                is_valid = false;
            }
            else if(et_email.getText().toString().trim().length() != 0 && !isValidEmail(et_email.getText().toString().trim()))
            {
                Toast.makeText(this,"Enter valid email",Toast.LENGTH_SHORT).show();
                is_valid = false;
            }
            else if(et_first_name.getText().toString().trim().length() == 0)
            {
                Toast.makeText(this,"Enter first name",Toast.LENGTH_SHORT).show();
                is_valid = false;
            }
            else if(et_last_name.getText().toString().trim().length() == 0)
            {
                Toast.makeText(this,"Enter last name",Toast.LENGTH_SHORT).show();
                is_valid = false;
            }
            else if(et_company_name.getText().toString().trim().length() == 0)
            {
                Toast.makeText(this,"Enter company name",Toast.LENGTH_SHORT).show();
                is_valid = false;
            }
            else if(et_phone_no.getText().toString().trim().length() == 0)
            {
                Toast.makeText(this,"Enter phone no",Toast.LENGTH_SHORT).show();
                is_valid = false;
            }

            if(et_password.getText().toString().length() != 0 && et_confirm_password.getText().toString().trim().length() == 0)
            {
                Toast.makeText(this,"Enter confirm password",Toast.LENGTH_SHORT).show();
                is_valid =false;
            }
            else if(et_password.getText().toString().length() != 0 && !et_confirm_password.getText().toString().equals(et_password.getText().toString()))
            {
                is_valid = false;
                Toast.makeText(this,"Password does not match",Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e){e.printStackTrace();}
        return is_valid;
    }
    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
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

        Bitmap bitmap;
        if (resultCode == Activity.RESULT_OK) {

            if (getPickImageResultUri(data) != null) {
                picUri = getPickImageResultUri(data);

                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);
                    myBitmap = rotateImageIfRequired(myBitmap, picUri);
//                    myBitmap = getResizedBitmap(myBitmap,imgUserWidth,imgUserHeight);
                    myBitmap = getResizedBitmap(myBitmap,Share.screenWidth/2,Share.screenWidth/2);
                    iv_user_profile.setImageBitmap(myBitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                bitmap = (Bitmap) data.getExtras().get("data");
                myBitmap = bitmap;
                myBitmap = getResizedBitmap(myBitmap,Share.screenWidth/2,Share.screenWidth/2);
                iv_user_profile.setImageBitmap(myBitmap);
            }
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
    /*------------------------------ Webservices ---------------------------*/

    private void updateProfile()
    {
        try
        {
            final ProgressDialog pd = ProgressDialog.show(this,"","Loading...",true,false);
            pd.show();
            RequestBody ruser_id =RequestBody.create(okhttp3.MultipartBody.FORM, SharedPrefs.getString(this,SharedPrefs.USER_ID));
            RequestBody remail =RequestBody.create(okhttp3.MultipartBody.FORM, et_email.getText().toString());
            RequestBody rfirst_name =RequestBody.create(okhttp3.MultipartBody.FORM,et_first_name.getText().toString());
            RequestBody rlast_name =RequestBody.create(okhttp3.MultipartBody.FORM,""+et_last_name.getText().toString());
            RequestBody rcompany_name =RequestBody.create(okhttp3.MultipartBody.FORM,""+ et_company_name.getText().toString());
            RequestBody rpassword =RequestBody.create(okhttp3.MultipartBody.FORM,""+ et_password.getText().toString());
            RequestBody rphone_no =RequestBody.create(okhttp3.MultipartBody.FORM,""+ et_phone_no.getText().toString());

            Call<UpdateProfileRrespone> updateProfileRresponeCall ;//= ApiClient.getClient().create(ApiService.class).updateProfile()
            if(picUri !=null && picUri.getPath().length() !=0)
            {
                File file = new File(getRealPathFromURI(picUri));
                Log.e(TAG,""+file.getAbsolutePath());
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("profile_image", file.getName(), requestFile);
                updateProfileRresponeCall = ApiClient.getClient().create(ApiService.class).updateProfile(ruser_id,remail,rfirst_name,rlast_name,rcompany_name,rpassword,rphone_no,body);
            }
            else
                updateProfileRresponeCall = ApiClient.getClient().create(ApiService.class).updateProfile(ruser_id,remail,rfirst_name,rlast_name,rcompany_name,rpassword,rphone_no,null);

            updateProfileRresponeCall.enqueue(new Callback<UpdateProfileRrespone>() {
                @Override
                public void onResponse(Call<UpdateProfileRrespone> call, Response<UpdateProfileRrespone> response)
                {
                    try
                    {
                        pd.cancel();
                        if(response.body().getProfileuberAlpha().getStatus().equals("success"))
                        {
                            Profiledetails  profiledetails = response.body().getProfileuberAlpha().getProfiledetails();
                            SharedPrefs.save(ProfileActivity.this, SharedPrefs.FIRST_NAME,profiledetails.getFirstName());
                            SharedPrefs.save(ProfileActivity.this, SharedPrefs.LAST_NAME,profiledetails.getLastName());
                            SharedPrefs.save(ProfileActivity.this,SharedPrefs.EMAIL,profiledetails.getEmail());
                            SharedPrefs.save(ProfileActivity.this,SharedPrefs.COMPANY_NAME,profiledetails.getCompanyName());
                            SharedPrefs.save(ProfileActivity.this,SharedPrefs.PHONE_NO,profiledetails.getPhone());
                            SharedPrefs.save(ProfileActivity.this,SharedPrefs.PROFILE_IMG,profiledetails.getProfileImage());

                            if(et_password.getText().toString().trim().length() !=0)
                                SharedPrefs.save(ProfileActivity.this, SharedPrefs.PASSWORD,et_password.getText().toString());

                            final Dialog dialog = new Dialog(ProfileActivity.this);
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
                            tv_msg_1.setText(""+response.body().getProfileuberAlpha().getMessage());
                            tv_msg_1.setTypeface(Share.Font.regular);

                            Button btn_ok = (Button)dialog.findViewById(R.id.btn_ok);
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {
                                    dialog.cancel();
                                    et_password.getText().clear();
                                    et_confirm_password.getText().clear();
                                }
                            });
//                            Toast.makeText(ProfileActivity.this,""+response.body().getProfileuberAlpha().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e){e.printStackTrace();}
                }

                @Override
                public void onFailure(Call<UpdateProfileRrespone> call, Throwable t) {
                    pd.cancel();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
