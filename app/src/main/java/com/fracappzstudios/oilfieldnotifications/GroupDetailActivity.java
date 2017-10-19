package com.fracappzstudios.oilfieldnotifications;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fracappzstudios.oilfieldnotifications.adapter.GroupMemberAdapter;
import com.fracappzstudios.oilfieldnotifications.adapter.SearchPeopleDirAdapter;
import com.fracappzstudios.oilfieldnotifications.model.PeopelDirdetail;
import com.fracappzstudios.oilfieldnotifications.util.NetworkManager;
import com.fracappzstudios.oilfieldnotifications.util.Share;
import com.fracappzstudios.oilfieldnotifications.util.SharedPrefs;
import com.fracappzstudios.oilfieldnotifications.util.Urls;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiClient;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiService;
import com.fracappzstudios.oilfieldnotifications.webservice.response.DeleteRespone;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kathiriya Harshad on 6/28/2017.
 */

public class GroupDetailActivity extends AppCompatActivity {

    private static final String TAG = GroupDetailActivity.class.getSimpleName();
    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.iv_group)ImageView iv_group;
    @BindView(R.id.tv_group_name)TextView tv_group_name;
    @BindView(R.id.tv_group_details)TextView tv_group_details;
    @BindView(R.id.rv_group_members)RecyclerView rv_group_members;
    @BindView(R.id.tv_count_member)TextView tv_count_member;
    @BindView(R.id.iv_group_edit)ImageView iv_group_edit;

    LinearLayoutManager linearLayoutManager;
    GroupMemberAdapter groupMemberAdapter;
    String group_id="",user_list="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        ButterKnife.bind(this);
        setFont();
    }

    private void initView()
    {
        try {
            linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rv_group_members.setLayoutManager(linearLayoutManager);

            if(Share.groupdetail != null)
            {
                group_id = Share.groupdetail.getGroupId();
                tv_group_name.setTypeface(Share.Font.bold);
                tv_group_details.setTypeface(Share.Font.regular);
                tv_count_member.setTypeface(Share.Font.thin_regular);

                tv_group_name.setText(""+Share.groupdetail.getGroupName());
                tv_group_details.setText(Share.groupdetail.getGroupDescription());
                tv_count_member.setText(""+Share.groupdetail.getPeopelDirdetail().size()+" MEMBERS");
                Picasso.with(this)
                        .load(Urls.GROUP_IMAGE_URL+Share.groupdetail.getGroupImage())
                        .placeholder(R.drawable.ic_group)
                        .error(R.drawable.ic_group)
                        .into(iv_group);

                groupMemberAdapter = new GroupMemberAdapter(this,Share.groupdetail.getPeopelDirdetail());
                rv_group_members.setAdapter(groupMemberAdapter);

                groupMemberAdapter.setOnItemClickListener(new SearchPeopleDirAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position, PeopelDirdetail peopelDirdetail) {
                        Intent profileIntent = new Intent(GroupDetailActivity.this, ProfileActivity.class);
                        profileIntent.putExtra("peopelDirdetail",peopelDirdetail);
                        startActivity(profileIntent);
                    }
                });
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setFont()
    {
        try
        {
            tv_group_name.setTypeface(Share.Font.bold);
            tv_group_details.setTypeface(Share.Font.regular);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    @OnClick(R.id.iv_back)
    public void onClickMyGroup(View view)
    {
        finish();
    }

    @OnClick(R.id.iv_group_edit)
    public void onClickGroupEdit(View view)
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

            TextView tv_edit_group = (TextView)createEventDialog.findViewById(R.id.tv_create_event);
            TextView tv_delete_group = (TextView)createEventDialog.findViewById(R.id.tv_group_details);

            tv_edit_group.setText("Edit Group");
            tv_delete_group.setText("Delete Group");
            btn_cancel.setTypeface(Share.Font.bold);
            tv_edit_group.setTypeface(Share.Font.regular);
            tv_delete_group.setTypeface(Share.Font.regular);


            Share.selectedMember.clear();
            user_list="";
            if (Share.groupdetail.getPeopelDirdetail() != null && Share.groupdetail.getPeopelDirdetail().size() > 0) {
                for (int index = 0; index < Share.groupdetail.getPeopelDirdetail().size(); index++) {
                    PeopelDirdetail peopelDirdetail = Share.groupdetail.getPeopelDirdetail().get(index);
                    Share.selectedMember.put(peopelDirdetail.getMemberId(), true);

                    if (!peopelDirdetail.getMemberId().equals(SharedPrefs.getString(GroupDetailActivity.this,SharedPrefs.USER_ID)))
                    {
                        if (user_list.length() == 0)
                            user_list = peopelDirdetail.getMemberId();
                        else
                            user_list +=","+peopelDirdetail.getMemberId();
                    }
                }
            }
            tv_edit_group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createEventDialog.cancel();
                    Intent updateGroupIntent = new Intent(GroupDetailActivity.this,NewGroupActivity.class);
                    updateGroupIntent.putExtra("update","update");
                    updateGroupIntent.putExtra("user_list",user_list);
                    startActivity(updateGroupIntent);
                }
            });

            tv_delete_group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createEventDialog.cancel();
                    if(NetworkManager.isInternetConnected(GroupDetailActivity.this))
                        deleteGroupRequest(Share.groupdetail.getGroupId());
                }
            });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /*---------------------------------- WebServices ------------------------------------*/
    public void deleteGroupRequest(String group_id)
    {
        try
        {
            final ProgressDialog pd = ProgressDialog.show(this,"","Loading...",true,false);
            pd.show();
            Call<DeleteRespone> deleteGroupResponeCall = ApiClient.getClient().create(ApiService.class).delete_group(SharedPrefs.getString(this,SharedPrefs.USER_ID),group_id);
            deleteGroupResponeCall.enqueue(new Callback<DeleteRespone>() {
                @Override
                public void onResponse(Call<DeleteRespone> call, Response<DeleteRespone> response) {
                    try
                    {
                        pd.cancel();
                        final Dialog dialog = new Dialog(GroupDetailActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setContentView(R.layout.dlg_signup);
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(false);
                        ImageView fab = (ImageView)dialog.findViewById(R.id.fab);
                        Button btn_ok = (Button)dialog.findViewById(R.id.btn_ok);
                        TextView tv_msg = (TextView)dialog.findViewById(R.id.tv_msg);
                        TextView tv_msg_1 = (TextView)dialog.findViewById(R.id.tv_msg_1);
                        tv_msg.setTypeface(Share.Font.regular);
                        tv_msg_1.setTypeface(Share.Font.regular);

                        if(response.body().getDeleteuberAlpha().getStatus().equals("success"))
                        {
                            fab.setImageResource(R.drawable.ic_right_circle);
                            btn_ok.setTypeface(Share.Font.regular);
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.cancel();
                                    Intent myGroupIntent = new Intent(GroupDetailActivity.this,MyGroupActivity.class);
                                    myGroupIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(myGroupIntent);
                                    finish();
                                }
                            });
                            tv_msg.setText("Success!");
                            tv_msg_1.setText(""+response.body().getDeleteuberAlpha().getMessage());
                            dialog.show();
                        }
                        else {
                            fab.setImageResource(R.drawable.ic_hand_circle);
                            btn_ok.setTypeface(Share.Font.regular);
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.cancel();
                                }
                            });
                            tv_msg.setVisibility(View.GONE);
                            tv_msg_1.setText(""+response.body().getDeleteuberAlpha().getMessage());
                            dialog.show();
                        }
                    }
                    catch (Exception e){e.printStackTrace();}
                }

                @Override
                public void onFailure(Call<DeleteRespone> call, Throwable t) {
                    t.printStackTrace();
                    pd.cancel();
                }
            });
        }
        catch (Exception e){e.printStackTrace();}
    }
}
