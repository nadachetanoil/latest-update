package com.fracappzstudios.oilfieldnotifications;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
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

import com.fracappzstudios.oilfieldnotifications.util.Share;
import com.fracappzstudios.oilfieldnotifications.util.SharedPrefs;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiClient;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiService;
import com.fracappzstudios.oilfieldnotifications.webservice.response.AddCommentRespone;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kathiriya Harshad on 6/28/2017.
 */

public class CreateCommentActivity extends AppCompatActivity {

    private static final String TAG = CreateCommentActivity.class.getSimpleName();

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.et_add_a_comment)EditText et_add_a_comment;
    @BindView(R.id.tv_send)TextView tv_send;
    @BindView(R.id.tv_add_comment)TextView tv_add_comment;

    String user_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_comment);
        ButterKnife.bind(this);
        initView();
    }

    private void initView()
    {
        try
        {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            et_add_a_comment.setTypeface(Share.Font.regular);
            tv_send.setTypeface(Share.Font.bold);
            tv_add_comment.setTypeface(Share.Font.bold);
        }
        catch (Exception e){e.printStackTrace();}
    }
    @OnClick(R.id.iv_back)
    public void onClickBack(View view)
    {
        finish();
    }

    @OnClick(R.id.tv_send)
    public void onClickSend(View view)
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
            if(et_add_a_comment.getText().toString().trim().length() == 0)
            {
                is_valid =false;
                final Dialog  dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dlg_group);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                TextView tv_msg = (TextView)dialog.findViewById(R.id.tv_msg);
                tv_msg.setText("Alert !");
                tv_msg.setTypeface(Share.Font.bold);
                TextView tv_msg_1 = (TextView)dialog.findViewById(R.id.tv_msg_1);
                tv_msg_1.setText("Please enter comment.");
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
        catch (Exception e){e.printStackTrace();}
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
                                if (!Share.groupdetail.getPeopelDirdetail().get(index).getMemberId().equals(SharedPrefs.getString(CreateCommentActivity.this, SharedPrefs.USER_ID)))
                                {
                                    if(memberlist.length() == 0 && Share.groupdetail.getPeopelDirdetail().get(index).is_select())
                                        memberlist.append(Share.groupdetail.getPeopelDirdetail().get(index).getMemberId());
                                    else if(Share.groupdetail.getPeopelDirdetail().get(index).is_select())
                                        memberlist.append(","+Share.groupdetail.getPeopelDirdetail().get(index).getMemberId());
                                }
                            }
                        }
                        user_list = memberlist.toString();
                        if(user_list.length() != 0)
                            sendComment();
                        else
                            Toast.makeText(CreateCommentActivity.this,"Select member",Toast.LENGTH_SHORT).show();
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
                    Intent selectMemberIntent = new Intent(CreateCommentActivity.this,SelectMemberActivity.class);
                    selectMemberIntent.putExtra("eventMember","eventMemberList");
                    startActivityForResult(selectMemberIntent,300);
                }
            });

        }catch (Exception e){e.printStackTrace();}
    }
    public static String random() {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 36; i++)
            sb.append(chars[rnd.nextInt(chars.length)]);

        return sb.toString();

//        return randomStringBuilder.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try
        {
            if(resultCode == Activity.RESULT_OK && requestCode == 300 && data.getExtras() != null)
            {
                user_list = data.getExtras().getString("user_list");
                Log.e(TAG,"Comment member_ids :-> "+user_list);
                sendComment();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /*------------------------------------ WebService -------------------------------*/
    private void sendComment()
    {
        try
        {
            final ProgressDialog pd = ProgressDialog.show(this,"","Send Comment",true,false);
            pd.show();
            String commentId = random();
            Call<AddCommentRespone> addCommentRresponeCall = ApiClient.getClient().create(ApiService.class).addComment(commentId,
                    Share.eventdetail.getEventId(),
                    ""+SharedPrefs.getString(this,SharedPrefs.USER_ID),
                    Share.groupdetail.getGroupId(),
                    et_add_a_comment.getText().toString(),
                    user_list);

            addCommentRresponeCall.enqueue(new Callback<AddCommentRespone>() {
                @Override
                public void onResponse(Call<AddCommentRespone> call, Response<AddCommentRespone> response) {
                        try
                        {
                            pd.cancel();
                            if(response.body().getCommentuberAlpha().getStatus().equals("success"))
                            {
                                final Dialog dialog = new Dialog(CreateCommentActivity.this);
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
                                tv_msg_1.setText(""+response.body().getCommentuberAlpha().getMessage());
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
                            }
                            else
                            {

                            }
                        }
                        catch (Exception e){e.printStackTrace();}
                }

                @Override
                public void onFailure(Call<AddCommentRespone> call, Throwable t) {
                    pd.cancel();
                    t.printStackTrace();
                }
            });
        }
        catch (Exception e){e.printStackTrace();}
    }
}
