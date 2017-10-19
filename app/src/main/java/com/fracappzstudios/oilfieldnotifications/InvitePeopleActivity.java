package com.fracappzstudios.oilfieldnotifications;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fracappzstudios.oilfieldnotifications.util.Share;
import com.fracappzstudios.oilfieldnotifications.util.SharedPrefs;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiClient;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiService;
import com.fracappzstudios.oilfieldnotifications.webservice.response.InviteRespone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kathiriya Harshad on 6/28/2017.
 */


public class InvitePeopleActivity extends AppCompatActivity {

    private static final String TAG = InvitePeopleActivity.class.getSimpleName();

    @BindView(R.id.tv_title_invite_people)TextView tv_title_invite_people;
    @BindView(R.id.iv_back)ImageView iv_back;
    @BindView(R.id.tv_invite_people)TextView tv_invite_people;
    @BindView(R.id.tv_invite_header) TextView tv_invite_header;
    @BindView(R.id.tv_inv_info) TextView tv_inv_info;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_people);
        ButterKnife.bind(this);

        tv_title_invite_people.setTypeface(Share.Font.bold);
        tv_invite_header.setTypeface(Share.Font.bold);
        tv_inv_info.setTypeface(Share.Font.regular);
        tv_invite_people.setTypeface(Share.Font.regular);
    }

    @OnClick(R.id.iv_back)
    public void onClickMyGroup(View view)
    {
        finish();
    }

    @OnClick(R.id.tv_invite_people)
    public void onClickInviteByEmail(View view)
    {
        showInviteByPeopleDialog();
    }

    private void showInviteByPeopleDialog()
    {
        try
        {
            dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dlg_invite_by_email);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            final LinearLayout ll_email_list = (LinearLayout)dialog.findViewById(R.id.ll_email_list);
            ImageView iv_minus = (ImageView)dialog.findViewById(R.id.iv_minus);
            ImageView iv_plus = (ImageView)dialog.findViewById(R.id.iv_plus);
            TextView tv_cancel = (TextView)dialog.findViewById(R.id.tv_cancel);
            TextView tv_invite = (TextView)dialog.findViewById(R.id.tv_invite);

            tv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
            iv_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        View view = LayoutInflater.from(InvitePeopleActivity.this).inflate(R.layout.row_invite_email,ll_email_list,false);
                        ll_email_list.addView(view);
                        ll_email_list.invalidate();
                    }catch (Exception e){e.printStackTrace();}
                }
            });

            tv_invite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try
                    {
                        String inviteEmail="";

                        for (int index=0;index<ll_email_list.getChildCount();index++)
                        {
                            EditText et_email = (EditText)ll_email_list.getChildAt(index).findViewById(R.id.et_email);
                            if(!et_email.getText().toString().trim().equals("") && et_email.getText().toString().length() != 0)
                            {
                                inviteEmail +=","+et_email.getText().toString();
                            }
                        }

                        if(inviteEmail.length() == 0)
                        {
                            Toast.makeText(InvitePeopleActivity.this,"Enter invite emails",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            inviteEmail = inviteEmail.substring(1,inviteEmail.length());
                            Log.e(TAG,"inviteEmail :-> "+inviteEmail);
                            inviteEmailRequest(inviteEmail);
                        }
                    }
                    catch (Exception e){e.printStackTrace();}
                }
            });

            iv_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try
                    {
                        if(ll_email_list.getChildCount() != 1)
                        {
                            int i = ll_email_list.getChildCount()-1;
                            ll_email_list.removeViewAt(i);
                            ll_email_list.invalidate();
                        }
                    }catch (Exception e){e.printStackTrace();}
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    /*------------------------------------------------ webservices ---------------------------------------------*/

    private void inviteEmailRequest(String emails)
    {
        try
        {
            final ProgressDialog pd = ProgressDialog.show(this,"","Loading...",true,false);
            pd.show();

            Call<InviteRespone>  inviteResponeCall = ApiClient.getClient().create(ApiService.class).invite_by_mail(emails, SharedPrefs.getString(this,SharedPrefs.EMAIL),SharedPrefs.getString(this,SharedPrefs.USER_ID));
            inviteResponeCall.enqueue(new Callback<InviteRespone>() {
                @Override
                public void onResponse(Call<InviteRespone> call, Response<InviteRespone> response) {
                    try
                    {
                        pd.cancel();
                        if(response.body().getInviteuberAlpha().getStatus().equals("success"))
                        {
                            Toast.makeText(InvitePeopleActivity.this,response.body().getInviteuberAlpha().getMessage(),Toast.LENGTH_SHORT).show();
                            if(dialog != null)
                                dialog.cancel();
                        }
                        else
                            Toast.makeText(InvitePeopleActivity.this,response.body().getInviteuberAlpha().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e){e.printStackTrace();}
                }

                @Override
                public void onFailure(Call<InviteRespone> call, Throwable t) {
                    t.printStackTrace();
                    pd.cancel();
                }
            });

        }
        catch (Exception e){e.printStackTrace();}
    }
}
