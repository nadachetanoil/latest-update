package com.fracappzstudios.oilfieldnotifications;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fracappzstudios.oilfieldnotifications.database.DatabaseHandler;
import com.fracappzstudios.oilfieldnotifications.database.importdatabase;
import com.fracappzstudios.oilfieldnotifications.model.Logindetails;
import com.fracappzstudios.oilfieldnotifications.util.AnimUtil;
import com.fracappzstudios.oilfieldnotifications.util.NetworkManager;
import com.fracappzstudios.oilfieldnotifications.util.Share;
import com.fracappzstudios.oilfieldnotifications.util.SharedPrefs;
import com.fracappzstudios.oilfieldnotifications.view.FullScreenVideoView;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiClient;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiService;
import com.fracappzstudios.oilfieldnotifications.webservice.response.ForgotPasswordResponse;
import com.fracappzstudios.oilfieldnotifications.webservice.response.LoginResponse;
import com.fracappzstudios.oilfieldnotifications.webservice.response.SignupResponse;
import com.google.firebase.iid.FirebaseInstanceId;
import com.vicmikhailau.maskededittext.MaskedEditText;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kathiriya Harshad on 6/28/2017.
 */

public class LoginScreenActivity extends AppCompatActivity {

    private static final String TAG = LoginScreenActivity.class.getSimpleName();

    @BindView(R.id.video_view)FullScreenVideoView mVideoView;
    @BindView(R.id.tv_title)TextView tv_title;
    @BindView(R.id.btn_one)AppCompatButton btn_one;
    @BindView(R.id.btn_two)AppCompatButton btn_two;
    @BindView(R.id.ll_sign_up)LinearLayout ll_sign_up;
    @BindView(R.id.ll_login)LinearLayout ll_login;

    AnimUtil animUtil;
    //Sign up
    @BindView(R.id.et_first_name)EditText et_first_name;
    @BindView(R.id.et_last_name) EditText et_last_name;
    @BindView(R.id.et_email)EditText et_email;
    @BindView(R.id.et_password)EditText et_password;
    @BindView(R.id.et_confirm_password)EditText et_confirm_password;
    @BindView(R.id.et_company_name)EditText et_company_name;
    @BindView(R.id.et_phone_no)MaskedEditText et_phone_no;

    //login
    @BindView(R.id.et_username)EditText et_username;
    @BindView(R.id.et_pwd)EditText et_pwd;
    @BindView(R.id.chk_remember_me)AppCompatCheckBox chk_remember_me;
    @BindView(R.id.btn_forgot_pwd)AppCompatButton btn_forgot_pwd;


    //Database
    final DatabaseHandler cdba = new DatabaseHandler(this);
    public static InputStream databaseInputStreamContact;
    String refreshedToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_screen);
        ButterKnife.bind(this);

        try
        {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            Share.screenHeight = displayMetrics.heightPixels;
            Share.screenWidth = displayMetrics.widthPixels;
            refreshedToken = FirebaseInstanceId.getInstance().getToken();
            new InsertTask().execute();
            setFont();
            validPhoneNo();
            animUtil = new AnimUtil(this);
            initVideoView();
            et_phone_no.getText().clear();
            et_phone_no.setHint(getResources().getString(R.string.phone_no));
            et_phone_no.setMask("(###)###-####");
        }
        catch (Exception e){e.printStackTrace();}
    }

    private void setFont()
    {
        et_first_name.setTypeface(Share.Font.regular);
        et_last_name.setTypeface(Share.Font.regular);
        et_email.setTypeface(Share.Font.regular);
        et_password.setTypeface(Share.Font.regular);
        et_confirm_password.setTypeface(Share.Font.regular);
        et_company_name.setTypeface(Share.Font.regular);
        et_phone_no.setTypeface(Share.Font.regular);
        et_username.setTypeface(Share.Font.regular);
        et_pwd.setTypeface(Share.Font.regular);
        btn_forgot_pwd.setTypeface(Share.Font.regular);
        btn_one.setTypeface(Share.Font.regular);
        btn_two.setTypeface(Share.Font.regular);
        chk_remember_me.setTypeface(Share.Font.regular);
        btn_forgot_pwd.setTypeface(Share.Font.regular);
    }

    private class InsertTask extends AsyncTask<String, Void, Boolean> {

        @SuppressLint("SdCardPath")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try
            {
                //create Calendar.sql database file on default database path
                File f = new File("/data/data/"+ Share.PACKAGE_NAME+"/databases/"+DatabaseHandler.DATABASE_NAME);
                if (f.exists()) {
                    Log.e("","Create Contacts database .... ");
                    cdba.open();
                    cdba.close();
                }
                else
                {
                    try
                    {
                        databaseInputStreamContact = getAssets().open(DatabaseHandler.DATABASE_NAME);
                        cdba.open();
                        cdba.close();
                        Log.e("","Create Contacts database .... ");
                        importdatabase.copyContactDataBase();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception ex)
            {
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {

            Boolean success = false;
            try {
                success = true;
            } catch (Exception e) {
                if (e.getMessage() != null)
                    e.printStackTrace();
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        Animation fadeInAnimation = AnimationUtils.loadAnimation(LoginScreenActivity.this, R.anim.fade_in_anim);
//        tv_title.startAnimation(fadeInAnimation);
        if(SharedPrefs.getString(this,SharedPrefs.IS_REMEMBER).equals("true"))
        {
            onClickLogin();
            chk_remember_me.setChecked(true);
            et_username.setText(SharedPrefs.getString(LoginScreenActivity.this,SharedPrefs.USERNAME));
            et_pwd.setText(SharedPrefs.getString(LoginScreenActivity.this,SharedPrefs.PASSWORD));
//            if(NetworkManager.isInternetConnected(this))
//                sendLoginRequest(SharedPrefs.getString(this, SharedPrefs.USERNAME),SharedPrefs.getString(this,SharedPrefs.PASSWORD));
        }
        else
            animUtil.loadFadeInAnim(tv_title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mVideoView != null) {
            mVideoView.stopPlayback();
            mVideoView = null;
        }
    }

    private void initVideoView() {
        mVideoView.setVideoPath(getVideoPath());
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mp) {
                mp.setLooping(true);
                startVideoPlayback();
            }
        });

    }

    private void startVideoPlayback() {
        // "forces" anti-aliasing - but increases time for taking frames - so keep it disabled
        mVideoView.start();
    }

    private String getVideoPath() {
        return "android.resource://" + getPackageName() + "/" + R.raw.welcome_video;
    }

    private void onClickSignUp()
    {
        try
        {
            animUtil.loadFadeOutAnim(tv_title);
            tv_title.setVisibility(View.GONE);
//            animUtil.loadSlideUpAnimation(ll_sign_up);
            ll_sign_up.setVisibility(View.VISIBLE);

            btn_one.setText(getResources().getString(R.string.confirm_signup));
            btn_two.setText(getResources().getString(R.string.cancel_sigup));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onClickCancelSignUp()
    {
        try
        {
            ll_sign_up.setVisibility(View.GONE);
            tv_title.setVisibility(View.VISIBLE);

            btn_one.setText(getResources().getString(R.string.loigin));
            btn_two.setText(getResources().getString(R.string.signup));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onClickCancelLogin()
    {
        try
        {
//            animUtil.loadSlideDownAnimation(ll_login);
            ll_login.setVisibility(View.GONE);

            tv_title.setVisibility(View.VISIBLE);
            animUtil.loadFadeInAnim(tv_title);

            btn_one.setText(getResources().getString(R.string.loigin));
            btn_two.setText(getResources().getString(R.string.signup));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onClickLogin()
    {
        try
        {
            animUtil.loadFadeOutAnim(tv_title);
            tv_title.setVisibility(View.INVISIBLE);

//            animUtil.loadSlideUpAnimation(ll_login);
            ll_login.setVisibility(View.VISIBLE);

            btn_one.setText(getResources().getString(R.string.confirm_login));
            btn_two.setText(getResources().getString(R.string.cancel_login));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private boolean validSignupData()
    {
        boolean is_valid = true;

        if(et_first_name.getText().toString().trim().length() == 0)
        {
            is_valid = false;
            Toast.makeText(this,"Enter first name",Toast.LENGTH_SHORT).show();
        }
        else if(et_last_name.getText().toString().trim().length() ==0)
        {
            is_valid=false;
            Toast.makeText(this,"Enter last name",Toast.LENGTH_SHORT).show();
        }
        else if(et_email.getText().toString().trim().length() == 0)
        {
            is_valid = false;
            Toast.makeText(this,"Enter email address",Toast.LENGTH_SHORT).show();
        }
        else if(!isValidEmail(et_email.getText().toString().trim()))
        {
            is_valid =false;
            Toast.makeText(this,"In-valid Email address",Toast.LENGTH_SHORT).show();
        }
        else if(et_password.getText().toString().trim().length() == 0)
        {
            is_valid = false;
            Toast.makeText(this,"Enter password",Toast.LENGTH_SHORT).show();
        }
        else if(et_confirm_password.getText().toString().trim().length() == 0)
        {
            is_valid = false;;
            Toast.makeText(this,"Enter confirm password",Toast.LENGTH_SHORT).show();
        }
        else if(!et_password.getText().toString().equals(et_confirm_password.getText().toString()))
        {
            is_valid = false;
            Toast.makeText(this,"Password does not match",Toast.LENGTH_SHORT).show();
        }
        else if(et_company_name.getText().toString().length() == 0)
        {
            is_valid = false;
            Toast.makeText(this,"Enter company name",Toast.LENGTH_SHORT).show();
        }
        else if(et_phone_no.getText().toString().length() ==0)
        {
            is_valid = false;
            Toast.makeText(this,"Enter phone number",Toast.LENGTH_SHORT).show();
        }
        return is_valid;
    }

    private void clearSignupText()
    {
        et_first_name.getText().clear();
        et_last_name.getText().clear();
        et_email.getText().clear();
        et_password.getText().clear();
        et_confirm_password.getText().clear();
        et_company_name.getText().clear();
        et_phone_no.getText().clear();
    }

    private boolean validLoginData()
    {
         boolean is_valid = true;

        if(et_username.getText().toString().trim().length() == 0)
        {
            is_valid=false;
            Toast.makeText(this,"Enter email address",Toast.LENGTH_SHORT).show();
        }
        else if(et_pwd.getText().toString().trim().length() == 0)
        {
            is_valid =  false;
            Toast.makeText(this,"Enter password",Toast.LENGTH_SHORT).show();
        }

        return is_valid;
    }

    public void validPhoneNo()
    {
        try
        {
            et_phone_no.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    /*---------------------------------------------------onClick-------------------------------------*/
    @OnClick(R.id.btn_two)
    public void onClickTwo(View view)
    {
        try
        {
            if(btn_two.getText().equals(getResources().getString(R.string.signup)))
            {
//                clearSignupText();
                onClickSignUp();
            }
            else  if(btn_two.getText().equals(getResources().getString(R.string.cancel_sigup)))
            {
                onClickCancelSignUp();
            }
            else if(btn_two.getText().equals(getResources().getString(R.string.cancel_login)))
            {
                onClickCancelLogin();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_one)
    public void onClickOne(View view)
    {
        if(btn_one.getText().equals(getResources().getString(R.string.loigin)) && ll_login.getVisibility() == View.GONE)
        {
//            et_username.getText().clear();
//            et_password.getText().clear();
            onClickLogin();
        }
        else if(btn_one.getText().equals(getResources().getString(R.string.confirm_signup)))
        {
            if(validSignupData())
            {
                sendSignupRequest();
            }
        }
        else if(btn_one.getText().equals(getResources().getString(R.string.confirm_login)))
        {
            if(validLoginData())
            {
                sendLoginRequest(et_username.getText().toString(),et_pwd.getText().toString());
            }
        }
    }

    @OnClick(R.id.btn_forgot_pwd)
    public void onClickForgotPassword(View view)
    {
        try
        {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.dlg_forgot_pwd);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            final EditText et_email_id = (EditText)dialog.findViewById(R.id.et_email_id);
            Button btn_ok = (Button)dialog.findViewById(R.id.btn_ok);
            Button btn_cancel = (Button)dialog.findViewById(R.id.btn_cancel);

            et_email_id.setTypeface(Share.Font.regular);
            btn_ok.setTypeface(Share.Font.regular);
            btn_cancel.setTypeface(Share.Font.regular);

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try
                    {
                        if(et_email_id.getText().toString().trim().length() == 0)
                        {
                            Toast.makeText(LoginScreenActivity.this,"Enter email address",Toast.LENGTH_SHORT).show();
                        }
                        else if(NetworkManager.isInternetConnected(LoginScreenActivity.this))
                        {
                            dialog.cancel();
                            getForgotPassword(et_email_id.getText().toString());
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /*--------------------------------- WebServices ------------------------------------*/
    private void sendSignupRequest()
    {
        try {

            final ProgressDialog pd = ProgressDialog.show(this,"","Loading...",true,false);
            pd.show();
            Call<SignupResponse> signupResponseCall = ApiClient.getClient().create(ApiService.class).sendSignupRequest(et_first_name.getText().toString(),
                                                        et_last_name.getText().toString(),
                                                        et_email.getText().toString(),
                                                        et_password.getText().toString(),
                                                        et_phone_no.getText().toString(),
                                                        et_company_name.getText().toString(),
                                                        "");
            signupResponseCall.enqueue(new Callback<SignupResponse>() {
                @Override
                public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response)
                {
                    pd.cancel();
                    final Dialog dialog = new Dialog(LoginScreenActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setContentView(R.layout.dlg_signup);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(false);

                    Button btn_ok = (Button)dialog.findViewById(R.id.btn_ok);
                    btn_ok.setTypeface(Share.Font.regular);
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                    if(response.body().getUberAlpha().getStatus().equals("success"))
                    {
                        TextView tv_msg = (TextView)dialog.findViewById(R.id.tv_msg);
                        tv_msg.setText("Success !");
                        TextView tv_msg_1 = (TextView)dialog.findViewById(R.id.tv_msg_1);
                        tv_msg_1.setText(response.body().getUberAlpha().getMessage());

                        tv_msg.setTypeface(Share.Font.regular);
                        tv_msg_1.setTypeface(Share.Font.regular);
                        dialog.show();
                        clearSignupText();
                        onClickCancelSignUp();
                    }
                    else
                    {
                        TextView tv_msg = (TextView)dialog.findViewById(R.id.tv_msg);
                        tv_msg.setText("Fail !");
                        TextView tv_msg_1 = (TextView)dialog.findViewById(R.id.tv_msg_1);
                        tv_msg_1.setText(""+response.body().getUberAlpha().getMessage());

                        tv_msg.setTypeface(Share.Font.regular);
                        tv_msg_1.setTypeface(Share.Font.regular);
                        dialog.show();
                    }
                }

                @Override
                public void onFailure(Call<SignupResponse> call, Throwable t) {
                    pd.cancel();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void sendLoginRequest(final String username, final String pwd)
    {
        try
        {
            if(refreshedToken == null)
                refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.e(TAG,"refreshedToken :-> "+refreshedToken);

            final ProgressDialog pd = ProgressDialog.show(this,"","Loading...",true,false);
            pd.show();
            Call<LoginResponse> loginResponseCall = ApiClient.getClient().create(ApiService.class).sendLoginRequest(username,pwd,""+refreshedToken,"android");
            loginResponseCall.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response)
                {
                    pd.cancel();
                    if(response.body().getLoginUberAlpha().getStatus().equals("success"))
                    {
                        Logindetails logindetails = response.body().getLoginUberAlpha().getLogindetails();
                        if(chk_remember_me.isChecked())
                            SharedPrefs.save(LoginScreenActivity.this, SharedPrefs.IS_REMEMBER,"true");
                        else
                            SharedPrefs.save(LoginScreenActivity.this, SharedPrefs.IS_REMEMBER,"false");

                        SharedPrefs.save(LoginScreenActivity.this, SharedPrefs.USER_ID,""+logindetails.getUserId());
                        SharedPrefs.save(LoginScreenActivity.this,SharedPrefs.FIRST_NAME,""+logindetails.getFirstName());
                        SharedPrefs.save(LoginScreenActivity.this,SharedPrefs.LAST_NAME,""+logindetails.getLastName());
                        SharedPrefs.save(LoginScreenActivity.this, SharedPrefs.EMAIL,""+logindetails.getEmail());
                        SharedPrefs.save(LoginScreenActivity.this,SharedPrefs.PHONE_NO,""+logindetails.getPhoneNo());
                        SharedPrefs.save(LoginScreenActivity.this, SharedPrefs.COMPANY_NAME,""+logindetails.getCompanyName());
                        SharedPrefs.save(LoginScreenActivity.this, SharedPrefs.DEVICE_TOKEN,""+logindetails.getDeviceToken());
                        SharedPrefs.save(LoginScreenActivity.this, SharedPrefs.SUPER_USER,""+logindetails.getSuperUser());
                        SharedPrefs.save(LoginScreenActivity.this,SharedPrefs.PROFILE_IMG,""+logindetails.getProfile_image());

                        SharedPrefs.save(LoginScreenActivity.this,SharedPrefs.USERNAME,""+username);
                        SharedPrefs.save(LoginScreenActivity.this, SharedPrefs.PASSWORD,""+pwd);

                        Intent homeIntent = new Intent(LoginScreenActivity.this,HomeActivity.class);
                        startActivity(homeIntent);
                        finish();
                    }
                    else
                    {
                        final Dialog dialog = new Dialog(LoginScreenActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setContentView(R.layout.dlg_signup);
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(false);

                        ImageView fab = (ImageView) dialog.findViewById(R.id.fab);
                        fab.setImageResource(R.drawable.ic_close);
                        Button btn_ok = (Button)dialog.findViewById(R.id.btn_ok);
                        btn_ok.setTypeface(Share.Font.regular);
                        btn_ok.setTextColor(Color.WHITE);
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });

                        TextView tv_msg = (TextView)dialog.findViewById(R.id.tv_msg);
                        tv_msg.setText("Fail !");
                        TextView tv_msg_1 = (TextView)dialog.findViewById(R.id.tv_msg_1);
                        tv_msg_1.setText(response.body().getLoginUberAlpha().getMessage());
                        tv_msg.setTypeface(Share.Font.regular);
                        tv_msg_1.setTypeface(Share.Font.regular);
                        dialog.show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {

                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getForgotPassword(String email)
    {
        try
        {
            final ProgressDialog pd = ProgressDialog.show(this,"","Loading...",true,false);
            pd.show();
            Call<ForgotPasswordResponse> forgotPasswordResponseCall = ApiClient.getClient().create(ApiService.class).getForgotPassword(email);
            forgotPasswordResponseCall.enqueue(new Callback<ForgotPasswordResponse>() {
                @Override
                public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                    pd.cancel();
                    try
                    {
                        final Dialog dialog = new Dialog(LoginScreenActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setContentView(R.layout.dlg_signup);
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(false);

                        Button btn_ok = (Button)dialog.findViewById(R.id.btn_ok);
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });

                        if(response.body().getForgotPwduberAlpha().getStatus().equals("success"))
                        {
                            TextView tv_msg = (TextView)dialog.findViewById(R.id.tv_msg);
                            tv_msg.setText("Success !");
                            TextView tv_msg_1 = (TextView)dialog.findViewById(R.id.tv_msg_1);
                            tv_msg_1.setText(response.body().getForgotPwduberAlpha().getMessage());

                            dialog.show();
                            clearSignupText();
                            onClickCancelSignUp();
                        }
                        else
                        {
                            TextView tv_msg = (TextView)dialog.findViewById(R.id.tv_msg);
                            tv_msg.setText("Fail !");
                            TextView tv_msg_1 = (TextView)dialog.findViewById(R.id.tv_msg_1);
                            tv_msg_1.setText(""+response.body().getForgotPwduberAlpha().getMessage());
                            dialog.show();
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {

                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
