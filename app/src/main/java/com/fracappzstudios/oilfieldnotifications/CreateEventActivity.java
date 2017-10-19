package com.fracappzstudios.oilfieldnotifications;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.fracappzstudios.oilfieldnotifications.model.PeopelDirdetail;
import com.fracappzstudios.oilfieldnotifications.util.FindFilePath;
import com.fracappzstudios.oilfieldnotifications.util.Share;
import com.fracappzstudios.oilfieldnotifications.util.SharedPrefs;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiClient;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiService;
import com.fracappzstudios.oilfieldnotifications.webservice.response.AddEventRrespone;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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


public class CreateEventActivity extends AppCompatActivity {

    private static final String TAG = CreateEventActivity.class.getSimpleName();

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.tv_title_invite_people)TextView tv_title_invite_people;
    @BindView(R.id.tv_send)TextView tv_send;
    @BindView(R.id.et_event_name)EditText et_event_name;
    @BindView(R.id.et_event_details)EditText et_event_details;
    @BindView(R.id.tv_count_camera)TextView tv_count_camera;
    @BindView(R.id.tv_count_picture)TextView tv_count_picture;
    @BindView(R.id.tv_count_attachfile)TextView tv_count_attachfile;

    int countCam=0,countImg=0,countAttach=0;
    Uri picUri;
    private List<String> imgAttachment = new ArrayList<>();
    private List<String> imgNameAttachment = new ArrayList<>();

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    PeopelDirdetail peopelDirdetail;
    String profileImage="",user_list="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        ButterKnife.bind(this);
        initView();
        setFont();
    }

    private void initView()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        permissions.add(CAMERA);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        et_event_details.setImeOptions(EditorInfo.IME_ACTION_DONE);
        et_event_details.setRawInputType(InputType.TYPE_CLASS_TEXT);
    }

    private void setFont()
    {
        tv_send.setTypeface(Share.Font.regular);
        tv_title_invite_people.setTypeface(Share.Font.bold);
        et_event_name.setTypeface(Share.Font.regular);
        et_event_details.setTypeface(Share.Font.regular);
    }

    @OnClick(R.id.iv_back)
    public void onClickMyGroup(View view)
    {
        finish();
    }


    @OnClick(R.id.rl_camera)
    public void onClickCamera(View view)
    {
        try
        {
            if(imgAttachment.size() <5)
                startActivityForResult(getPickCameraChooserIntent(), 200);
            else
                showImageLimiteDialog();
        }
        catch (Exception e){e.printStackTrace();}
    }

    @OnClick(R.id.rl_image)
    public void onClickImage(View view)
    {
        try
        {
            if(imgAttachment.size() <5)
                startActivityForResult(getPickImageChooseIntent(), 201);
            else
                showImageLimiteDialog();
        }
        catch (Exception e){e.printStackTrace();}
    }
    @OnClick(R.id.rl_attach)
    public void onClickAttachFile(View view)
    {
        try
        {
            if(imgAttachment.size() <5)
            {
                startActivityForResult(getDocumentChooserIntent(),301);
            }
            else
                showImageLimiteDialog();

        }catch (Exception e){e.printStackTrace();}
    }

    @OnClick(R.id.tbw_rw_edit_attach)
    public void onClickEditSelection(View view)
    {
        Log.e(TAG,""+imgAttachment.size());
        if(imgAttachment.size() == 0)
            selectAattachmentFileDialog();
        else
        {
            showFileDialog();
        }
    }

    @OnClick(R.id.tv_send)
    public void onClickSendEvent(View view)
    {
        try
        {
            if(validation())
            {
                selectMemberDialog();
            }
        }
        catch (Exception e){e.printStackTrace();}
    }

    private boolean validation()
    {
        boolean is_valid = true;
        try
        {
            if(et_event_name.getText().toString().trim().length() == 0)
            {
                is_valid = false;
                final Dialog  dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dlg_group);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                TextView tv_msg = (TextView)dialog.findViewById(R.id.tv_msg);
                tv_msg.setText("Alert !");
                tv_msg.setTypeface(Share.Font.bold);
                TextView tv_msg_1 = (TextView)dialog.findViewById(R.id.tv_msg_1);
                tv_msg_1.setText("Please enter event name.");
                tv_msg_1.setTypeface(Share.Font.regular);

                Button btn_cancel = (Button)dialog.findViewById(R.id.btn_cancel);
                btn_cancel.setVisibility(View.GONE);
                Button btn_ok = (Button)dialog.findViewById(R.id.btn_ok);
                btn_ok.setTypeface(Share.Font.bold);
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return is_valid;
    }

    private void selectMemberDialog()
    {
        try
        {
            final Dialog createEventDialog = new Dialog(this);
            createEventDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            createEventDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            createEventDialog.setContentView(R.layout.dlg_create_event);
            createEventDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            createEventDialog.setCancelable(true);
            createEventDialog.setCanceledOnTouchOutside(false);
            createEventDialog.show();

            Button btn_cancel = (Button)createEventDialog.findViewById(R.id.btn_cancel);
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createEventDialog.cancel();
                }
            });
            TextView tv_select_member = (TextView)createEventDialog.findViewById(R.id.tv_group_details);
            TextView tv_send_toall = (TextView)createEventDialog.findViewById(R.id.tv_create_event);

            tv_send_toall.setText("Send to all members");
            tv_select_member.setText("Select members");

            btn_cancel.setTypeface(Share.Font.bold);
            tv_send_toall.setTypeface(Share.Font.regular);
            tv_select_member.setTypeface(Share.Font.regular);

            tv_send_toall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try
                    {
                        createEventDialog.cancel();
                        StringBuilder memberlist = new StringBuilder();
                        if(Share.groupdetail.getPeopelDirdetail() != null && Share.groupdetail.getPeopelDirdetail().size() > 0)
                        {
                            for (int index=0;index<Share.groupdetail.getPeopelDirdetail().size();index++)
                            {
                                if (!Share.groupdetail.getPeopelDirdetail().get(index).getMemberId().equals(SharedPrefs.getString(CreateEventActivity.this, SharedPrefs.USER_ID)))
                                {
                                    if(memberlist.length() == 0 && Share.groupdetail.getPeopelDirdetail().get(index).is_select())
                                        memberlist.append(Share.groupdetail.getPeopelDirdetail().get(index).getMemberId());
                                    else if(Share.groupdetail.getPeopelDirdetail().get(index).is_select())
                                        memberlist.append(","+Share.groupdetail.getPeopelDirdetail().get(index).getMemberId());
                                }
                            }
                        }
                        user_list = memberlist.toString();
                        createEvent();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            tv_select_member.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createEventDialog.cancel();
                    Intent selectMemberIntent = new Intent(CreateEventActivity.this,SelectMemberActivity.class);
                    selectMemberIntent.putExtra("eventMember","eventMemberList");
                    startActivityForResult(selectMemberIntent,300);
                }
            });

        }catch (Exception e){e.printStackTrace();}
    }

    private void showImageLimiteDialog()
    {
        try
        {
            final Dialog dialog = new Dialog(CreateEventActivity.this);
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
            tv_msg.setText("Alert!");
            tv_msg.setTypeface(Share.Font.bold);

            TextView tv_msg_1 = (TextView)dialog.findViewById(R.id.tv_msg_1);
            tv_msg_1.setText("You can not attach more than 5 files.");
            tv_msg_1.setTypeface(Share.Font.regular);

            Button btn_ok = (Button)dialog.findViewById(R.id.btn_ok);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    dialog.cancel();
                }
            });
        }catch (Exception e){e.printStackTrace();}
    }
    private void selectAattachmentFileDialog()
    {
        try
        {
            final Dialog dialog = new Dialog(CreateEventActivity.this);
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
            tv_msg.setText("Alert!");
            tv_msg.setTypeface(Share.Font.bold);

            TextView tv_msg_1 = (TextView)dialog.findViewById(R.id.tv_msg_1);
            tv_msg_1.setText("Please select files to attach.");
            tv_msg_1.setTypeface(Share.Font.regular);

            Button btn_ok = (Button)dialog.findViewById(R.id.btn_ok);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    dialog.cancel();
                }
            });
        }catch (Exception e){e.printStackTrace();}
    }

    private void showFileDialog()
    {
        try
        {
            final Dialog dialog = new Dialog(CreateEventActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.dlg_edit_attechment);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            TextView tv_cancel = (TextView)dialog.findViewById(R.id.tv_cancel);
            final LinearLayout ll_attechment = (LinearLayout)dialog.findViewById(R.id.ll_attechment);

            tv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
            addIMGAttachRow(ll_attechment);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void addIMGAttachRow(final LinearLayout ll_attechment)
    {
        try
        {
            ll_attechment.removeAllViews();
            for(int index=0;index<imgAttachment.size();index++)
            {
                final View view = getLayoutInflater().inflate(R.layout.row_attachfile,null,false);

                ImageView iv_close = (ImageView)view.findViewById(R.id.iv_close);
                TextView tv_text = (TextView)view.findViewById(R.id.tv_text);

                tv_text.setTypeface(Share.Font.regular);

                if(imgNameAttachment.size() > index)
                    tv_text.setText(""+imgNameAttachment.get(index));
                ll_attechment.addView(view);

                final int i = index;
                iv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try
                        {
                            if(imgNameAttachment.get(i).equals("assert.jpg"))
                            {
                                if(countImg > 0)
                                    countImg--;
                                if(countImg == 0)
                                    tv_count_picture.setVisibility(View.GONE);
                                tv_count_picture.setText(""+countImg);
                            }
                            else if(imgNameAttachment.get(i).equals("Camera attachment.jpg")){
                                if(countCam > 0)
                                    countCam--;
                                if(countCam == 0)
                                    tv_count_camera.setVisibility(View.GONE);
                                tv_count_camera.setText(""+countCam);
                            }
                            else
                            {
                                if(countAttach > 0)
                                    countAttach--;
                                if(countAttach == 0)
                                    tv_count_attachfile.setVisibility(View.GONE);
                                tv_count_attachfile.setText(""+countAttach);
                            }
                            imgAttachment.remove(i);
                            imgNameAttachment.remove(i);
                            ll_attechment.removeView(view);
                            addIMGAttachRow(ll_attechment);
                        }catch (Exception e){e.printStackTrace();}
                    }
                });
            }
        }catch (Exception e){e.printStackTrace();}
    }

    /*------------------------------------- Pickup image from camera or gallery -------------------------*/
    public Intent getPickCameraChooserIntent() {
        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();
        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (outputFileUri != null) {
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        }
        return captureIntent;
    }
    public Intent getPickImageChooseIntent()
    {
        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");

        return galleryIntent;
    }

    public Intent getDocumentChooserIntent()
    {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        return intent;
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            String fileName = new SimpleDateFormat("yyyyMMddHHmm'_camera.jpg'").format(new Date());
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), fileName));
            picUri = outputFileUri;
        }
        return outputFileUri;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try
        {
            if (resultCode == Activity.RESULT_OK && (requestCode == 200 || requestCode == 201))
            {
                String imgPath="";
                if (getCameraImageResultUri(data) != null)
                {
                    picUri = getCameraImageResultUri(data);
                    imgPath = FindFilePath.getPath(this,picUri);
                } else/* if (requestCode == 201)*/{
                    imgPath = getRealPathFromURI(this,picUri);
                }
                Log.e(TAG,"==============> "+imgPath);
                if(!imgPath.equals("") && imgPath.contains("camera"))
                {
                    imgAttachment.add(imgPath);
                    countCam++;
                    tv_count_camera.setVisibility(View.VISIBLE);
                    tv_count_camera.setText(""+countCam);
                    imgNameAttachment.add("Camera attachment.jpg");
                }
                else if(!imgPath.equals("")) {
                    imgAttachment.add(imgPath);
                    imgNameAttachment.add("assert.jpg");
                    countImg++;
                    tv_count_picture.setText(""+countImg);
                    tv_count_picture.setVisibility(View.VISIBLE);
                }
            }
            else if(resultCode == Activity.RESULT_OK && requestCode == 300 && data.getExtras() != null)
            {
                user_list = data.getExtras().getString("user_list");
                Log.e(TAG,"Event member_ids :-> "+user_list);
                createEvent();
            }
            else if(resultCode == Activity.RESULT_OK && requestCode == 301)
            {
                picUri = data.getData();
                countAttach++;
                tv_count_attachfile.setText(""+countAttach);
                if(countAttach !=0)
                {
                    tv_count_attachfile.setVisibility(View.VISIBLE);
                }
                String imgPath = FindFilePath.getPath(this,picUri);
                String filename=imgPath.substring(imgPath.lastIndexOf("/")+1);

                imgNameAttachment.add(""+filename);
                imgAttachment.add(imgPath);

                Log.i(TAG, "Doc Uri: " +filename+ " imgAttachment :- "+imgAttachment);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Uri getCameraImageResultUri(Intent data) {
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

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
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
        for (int i = 0; i < 34; i++)
            sb.append(chars[rnd.nextInt(chars.length)]);

        return sb.toString();

//        return randomStringBuilder.toString();
    }

    /*---------------------------------------WebService -------------------------------*/
    public void createEvent()
    {
        try
        {
            final ProgressDialog pd = ProgressDialog.show(this,"","Create event...",true,false);
            pd.show();

            String event_id = random();
            Log.e(TAG,"event_id :-> "+event_id);

            RequestBody rEventId = RequestBody.create(okhttp3.MultipartBody.FORM,event_id);
            RequestBody rUserId = RequestBody.create(okhttp3.MultipartBody.FORM,SharedPrefs.getString(this,SharedPrefs.USER_ID));
            RequestBody rGroup_id = RequestBody.create(okhttp3.MultipartBody.FORM,Share.groupdetail.getGroupId());
            RequestBody rEventName = RequestBody.create(okhttp3.MultipartBody.FORM,""+et_event_name.getText().toString());
            RequestBody rEventDetails = RequestBody.create(okhttp3.MultipartBody.FORM,""+et_event_details.getText().toString());
            RequestBody rUserList = RequestBody.create(okhttp3.MultipartBody.FORM,user_list);


            MultipartBody.Part bodyFile1=null,bodyFile2=null,bodyFile3=null,bodyFile4=null,bodyFile5=null;
            File file1=null,file2=null,file3=null,file4=null,file5=null;
            if(imgAttachment.size() != 0)
            {
                for (int index=0;index<imgAttachment.size();index++)
                {
                    if(index == 0)
                        file1 = new File(imgAttachment.get(index));
                    else if (index == 1)
                        file2 = new File(imgAttachment.get(index));
                    else if(index == 2)
                        file3 = new File(imgAttachment.get(index));
                    else if(index == 3)
                        file4 = new File(imgAttachment.get(index));
                    else if(index == 4)
                        file5 = new File(imgAttachment.get(index));
                }
            }

            if(file1 != null && file1.exists())
            {
                RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"),file1);
                bodyFile1 = MultipartBody.Part.createFormData("event_file1", file1.getName(), requestFile);
            }
            if(file2 != null && file1.exists())
            {
                RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"),file2);
                bodyFile2 = MultipartBody.Part.createFormData("event_file2", file2.getName(), requestFile);
            }
            if(file3 != null && file1.exists())
            {
                RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"),file3);
                bodyFile3 = MultipartBody.Part.createFormData("event_file3", file3.getName(), requestFile);
            }
            if(file4 != null && file1.exists())
            {
                RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"),file4);
                bodyFile4 = MultipartBody.Part.createFormData("event_file4", file4.getName(), requestFile);
            }
            if(file5 != null && file1.exists())
            {
                RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"),file5);
                bodyFile5 = MultipartBody.Part.createFormData("event_file5", file5.getName(), requestFile);
            }

            Call<AddEventRrespone> addEventRresponeCall = ApiClient.getClient().create(ApiService.class).addEvent(rEventId,rUserId,rGroup_id,rEventName,rEventDetails,bodyFile1,bodyFile2,bodyFile3,bodyFile4,bodyFile5,rUserList);
            addEventRresponeCall.enqueue(new Callback<AddEventRrespone>() {
                @Override
                public void onResponse(Call<AddEventRrespone> call, Response<AddEventRrespone> response) {
                    try
                    {
                        pd.cancel();
                        final Dialog dialog = new Dialog(CreateEventActivity.this);
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
                        tv_msg_1.setText(""+response.body().getNewEventuberAlpha().getMessage());
                        tv_msg_1.setTypeface(Share.Font.regular);

                        Button btn_ok = (Button)dialog.findViewById(R.id.btn_ok);
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                dialog.cancel();
                                finish();
                            }
                        });
                    }catch (Exception e){e.printStackTrace();}
                }

                @Override
                public void onFailure(Call<AddEventRrespone> call, Throwable t) {
                    pd.cancel();
                    t.printStackTrace();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
